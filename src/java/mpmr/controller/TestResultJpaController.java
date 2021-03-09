/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mpmr.dto.TestRequest;
import mpmr.dto.TestResultDetail;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.dto.TestResult;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class TestResultJpaController implements Serializable {

    public TestResultJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TestResult testResult) throws PreexistingEntityException, Exception {
        if (testResult.getTestResultDetailCollection() == null) {
            testResult.setTestResultDetailCollection(new ArrayList<TestResultDetail>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TestRequest testRequestId = testResult.getTestRequestId();
            if (testRequestId != null) {
                testRequestId = em.getReference(testRequestId.getClass(), testRequestId.getId());
                testResult.setTestRequestId(testRequestId);
            }
            Collection<TestResultDetail> attachedTestResultDetailCollection = new ArrayList<TestResultDetail>();
            for (TestResultDetail testResultDetailCollectionTestResultDetailToAttach : testResult.getTestResultDetailCollection()) {
                testResultDetailCollectionTestResultDetailToAttach = em.getReference(testResultDetailCollectionTestResultDetailToAttach.getClass(), testResultDetailCollectionTestResultDetailToAttach.getId());
                attachedTestResultDetailCollection.add(testResultDetailCollectionTestResultDetailToAttach);
            }
            testResult.setTestResultDetailCollection(attachedTestResultDetailCollection);
            em.persist(testResult);
            if (testRequestId != null) {
                testRequestId.getTestResultCollection().add(testResult);
                testRequestId = em.merge(testRequestId);
            }
            for (TestResultDetail testResultDetailCollectionTestResultDetail : testResult.getTestResultDetailCollection()) {
                TestResult oldTestResultIdOfTestResultDetailCollectionTestResultDetail = testResultDetailCollectionTestResultDetail.getTestResultId();
                testResultDetailCollectionTestResultDetail.setTestResultId(testResult);
                testResultDetailCollectionTestResultDetail = em.merge(testResultDetailCollectionTestResultDetail);
                if (oldTestResultIdOfTestResultDetailCollectionTestResultDetail != null) {
                    oldTestResultIdOfTestResultDetailCollectionTestResultDetail.getTestResultDetailCollection().remove(testResultDetailCollectionTestResultDetail);
                    oldTestResultIdOfTestResultDetailCollectionTestResultDetail = em.merge(oldTestResultIdOfTestResultDetailCollectionTestResultDetail);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTestResult(testResult.getId()) != null) {
                throw new PreexistingEntityException("TestResult " + testResult + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TestResult testResult) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TestResult persistentTestResult = em.find(TestResult.class, testResult.getId());
            TestRequest testRequestIdOld = persistentTestResult.getTestRequestId();
            TestRequest testRequestIdNew = testResult.getTestRequestId();
            Collection<TestResultDetail> testResultDetailCollectionOld = persistentTestResult.getTestResultDetailCollection();
            Collection<TestResultDetail> testResultDetailCollectionNew = testResult.getTestResultDetailCollection();
            List<String> illegalOrphanMessages = null;
            for (TestResultDetail testResultDetailCollectionOldTestResultDetail : testResultDetailCollectionOld) {
                if (!testResultDetailCollectionNew.contains(testResultDetailCollectionOldTestResultDetail)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TestResultDetail " + testResultDetailCollectionOldTestResultDetail + " since its testResultId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (testRequestIdNew != null) {
                testRequestIdNew = em.getReference(testRequestIdNew.getClass(), testRequestIdNew.getId());
                testResult.setTestRequestId(testRequestIdNew);
            }
            Collection<TestResultDetail> attachedTestResultDetailCollectionNew = new ArrayList<TestResultDetail>();
            for (TestResultDetail testResultDetailCollectionNewTestResultDetailToAttach : testResultDetailCollectionNew) {
                testResultDetailCollectionNewTestResultDetailToAttach = em.getReference(testResultDetailCollectionNewTestResultDetailToAttach.getClass(), testResultDetailCollectionNewTestResultDetailToAttach.getId());
                attachedTestResultDetailCollectionNew.add(testResultDetailCollectionNewTestResultDetailToAttach);
            }
            testResultDetailCollectionNew = attachedTestResultDetailCollectionNew;
            testResult.setTestResultDetailCollection(testResultDetailCollectionNew);
            testResult = em.merge(testResult);
            if (testRequestIdOld != null && !testRequestIdOld.equals(testRequestIdNew)) {
                testRequestIdOld.getTestResultCollection().remove(testResult);
                testRequestIdOld = em.merge(testRequestIdOld);
            }
            if (testRequestIdNew != null && !testRequestIdNew.equals(testRequestIdOld)) {
                testRequestIdNew.getTestResultCollection().add(testResult);
                testRequestIdNew = em.merge(testRequestIdNew);
            }
            for (TestResultDetail testResultDetailCollectionNewTestResultDetail : testResultDetailCollectionNew) {
                if (!testResultDetailCollectionOld.contains(testResultDetailCollectionNewTestResultDetail)) {
                    TestResult oldTestResultIdOfTestResultDetailCollectionNewTestResultDetail = testResultDetailCollectionNewTestResultDetail.getTestResultId();
                    testResultDetailCollectionNewTestResultDetail.setTestResultId(testResult);
                    testResultDetailCollectionNewTestResultDetail = em.merge(testResultDetailCollectionNewTestResultDetail);
                    if (oldTestResultIdOfTestResultDetailCollectionNewTestResultDetail != null && !oldTestResultIdOfTestResultDetailCollectionNewTestResultDetail.equals(testResult)) {
                        oldTestResultIdOfTestResultDetailCollectionNewTestResultDetail.getTestResultDetailCollection().remove(testResultDetailCollectionNewTestResultDetail);
                        oldTestResultIdOfTestResultDetailCollectionNewTestResultDetail = em.merge(oldTestResultIdOfTestResultDetailCollectionNewTestResultDetail);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = testResult.getId();
                if (findTestResult(id) == null) {
                    throw new NonexistentEntityException("The testResult with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TestResult testResult;
            try {
                testResult = em.getReference(TestResult.class, id);
                testResult.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The testResult with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TestResultDetail> testResultDetailCollectionOrphanCheck = testResult.getTestResultDetailCollection();
            for (TestResultDetail testResultDetailCollectionOrphanCheckTestResultDetail : testResultDetailCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TestResult (" + testResult + ") cannot be destroyed since the TestResultDetail " + testResultDetailCollectionOrphanCheckTestResultDetail + " in its testResultDetailCollection field has a non-nullable testResultId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TestRequest testRequestId = testResult.getTestRequestId();
            if (testRequestId != null) {
                testRequestId.getTestResultCollection().remove(testResult);
                testRequestId = em.merge(testRequestId);
            }
            em.remove(testResult);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TestResult> findTestResultEntities() {
        return findTestResultEntities(true, -1, -1);
    }

    public List<TestResult> findTestResultEntities(int maxResults, int firstResult) {
        return findTestResultEntities(false, maxResults, firstResult);
    }

    private List<TestResult> findTestResultEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TestResult.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TestResult findTestResult(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TestResult.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestResultCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TestResult> rt = cq.from(TestResult.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public TestResult findByTestRequestId(String id) {
        return (TestResult) getEntityManager().createNamedQuery("TestResult.findByTestRequestId").setParameter("testrequestid", id).getSingleResult();
    }
}
