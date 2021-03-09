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
import mpmr.dto.Examination;
import mpmr.dto.TestResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.dto.TestRequest;
import mpmr.dto.TestTestRequest;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class TestRequestJpaController implements Serializable {

    public TestRequestJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TestRequest testRequest) throws PreexistingEntityException, Exception {
        if (testRequest.getTestResultCollection() == null) {
            testRequest.setTestResultCollection(new ArrayList<TestResult>());
        }
        if (testRequest.getTestTestRequestCollection() == null) {
            testRequest.setTestTestRequestCollection(new ArrayList<TestTestRequest>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Examination examinationId = testRequest.getExaminationId();
            if (examinationId != null) {
                examinationId = em.getReference(examinationId.getClass(), examinationId.getId());
                testRequest.setExaminationId(examinationId);
            }
            Collection<TestResult> attachedTestResultCollection = new ArrayList<TestResult>();
            for (TestResult testResultCollectionTestResultToAttach : testRequest.getTestResultCollection()) {
                testResultCollectionTestResultToAttach = em.getReference(testResultCollectionTestResultToAttach.getClass(), testResultCollectionTestResultToAttach.getId());
                attachedTestResultCollection.add(testResultCollectionTestResultToAttach);
            }
            testRequest.setTestResultCollection(attachedTestResultCollection);
            Collection<TestTestRequest> attachedTestTestRequestCollection = new ArrayList<TestTestRequest>();
            for (TestTestRequest testTestRequestCollectionTestTestRequestToAttach : testRequest.getTestTestRequestCollection()) {
                testTestRequestCollectionTestTestRequestToAttach = em.getReference(testTestRequestCollectionTestTestRequestToAttach.getClass(), testTestRequestCollectionTestTestRequestToAttach.getId());
                attachedTestTestRequestCollection.add(testTestRequestCollectionTestTestRequestToAttach);
            }
            testRequest.setTestTestRequestCollection(attachedTestTestRequestCollection);
            em.persist(testRequest);
            if (examinationId != null) {
                examinationId.getTestRequestCollection().add(testRequest);
                examinationId = em.merge(examinationId);
            }
            for (TestResult testResultCollectionTestResult : testRequest.getTestResultCollection()) {
                TestRequest oldTestRequestIdOfTestResultCollectionTestResult = testResultCollectionTestResult.getTestRequestId();
                testResultCollectionTestResult.setTestRequestId(testRequest);
                testResultCollectionTestResult = em.merge(testResultCollectionTestResult);
                if (oldTestRequestIdOfTestResultCollectionTestResult != null) {
                    oldTestRequestIdOfTestResultCollectionTestResult.getTestResultCollection().remove(testResultCollectionTestResult);
                    oldTestRequestIdOfTestResultCollectionTestResult = em.merge(oldTestRequestIdOfTestResultCollectionTestResult);
                }
            }
            for (TestTestRequest testTestRequestCollectionTestTestRequest : testRequest.getTestTestRequestCollection()) {
                TestRequest oldTestRequestIdOfTestTestRequestCollectionTestTestRequest = testTestRequestCollectionTestTestRequest.getTestRequestId();
                testTestRequestCollectionTestTestRequest.setTestRequestId(testRequest);
                testTestRequestCollectionTestTestRequest = em.merge(testTestRequestCollectionTestTestRequest);
                if (oldTestRequestIdOfTestTestRequestCollectionTestTestRequest != null) {
                    oldTestRequestIdOfTestTestRequestCollectionTestTestRequest.getTestTestRequestCollection().remove(testTestRequestCollectionTestTestRequest);
                    oldTestRequestIdOfTestTestRequestCollectionTestTestRequest = em.merge(oldTestRequestIdOfTestTestRequestCollectionTestTestRequest);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTestRequest(testRequest.getId()) != null) {
                throw new PreexistingEntityException("TestRequest " + testRequest + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TestRequest testRequest) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TestRequest persistentTestRequest = em.find(TestRequest.class, testRequest.getId());
            Examination examinationIdOld = persistentTestRequest.getExaminationId();
            Examination examinationIdNew = testRequest.getExaminationId();
            Collection<TestResult> testResultCollectionOld = persistentTestRequest.getTestResultCollection();
            Collection<TestResult> testResultCollectionNew = testRequest.getTestResultCollection();
            Collection<TestTestRequest> testTestRequestCollectionOld = persistentTestRequest.getTestTestRequestCollection();
            Collection<TestTestRequest> testTestRequestCollectionNew = testRequest.getTestTestRequestCollection();
            List<String> illegalOrphanMessages = null;
            for (TestResult testResultCollectionOldTestResult : testResultCollectionOld) {
                if (!testResultCollectionNew.contains(testResultCollectionOldTestResult)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TestResult " + testResultCollectionOldTestResult + " since its testRequestId field is not nullable.");
                }
            }
            for (TestTestRequest testTestRequestCollectionOldTestTestRequest : testTestRequestCollectionOld) {
                if (!testTestRequestCollectionNew.contains(testTestRequestCollectionOldTestTestRequest)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TestTestRequest " + testTestRequestCollectionOldTestTestRequest + " since its testRequestId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (examinationIdNew != null) {
                examinationIdNew = em.getReference(examinationIdNew.getClass(), examinationIdNew.getId());
                testRequest.setExaminationId(examinationIdNew);
            }
            Collection<TestResult> attachedTestResultCollectionNew = new ArrayList<TestResult>();
            for (TestResult testResultCollectionNewTestResultToAttach : testResultCollectionNew) {
                testResultCollectionNewTestResultToAttach = em.getReference(testResultCollectionNewTestResultToAttach.getClass(), testResultCollectionNewTestResultToAttach.getId());
                attachedTestResultCollectionNew.add(testResultCollectionNewTestResultToAttach);
            }
            testResultCollectionNew = attachedTestResultCollectionNew;
            testRequest.setTestResultCollection(testResultCollectionNew);
            Collection<TestTestRequest> attachedTestTestRequestCollectionNew = new ArrayList<TestTestRequest>();
            for (TestTestRequest testTestRequestCollectionNewTestTestRequestToAttach : testTestRequestCollectionNew) {
                testTestRequestCollectionNewTestTestRequestToAttach = em.getReference(testTestRequestCollectionNewTestTestRequestToAttach.getClass(), testTestRequestCollectionNewTestTestRequestToAttach.getId());
                attachedTestTestRequestCollectionNew.add(testTestRequestCollectionNewTestTestRequestToAttach);
            }
            testTestRequestCollectionNew = attachedTestTestRequestCollectionNew;
            testRequest.setTestTestRequestCollection(testTestRequestCollectionNew);
            testRequest = em.merge(testRequest);
            if (examinationIdOld != null && !examinationIdOld.equals(examinationIdNew)) {
                examinationIdOld.getTestRequestCollection().remove(testRequest);
                examinationIdOld = em.merge(examinationIdOld);
            }
            if (examinationIdNew != null && !examinationIdNew.equals(examinationIdOld)) {
                examinationIdNew.getTestRequestCollection().add(testRequest);
                examinationIdNew = em.merge(examinationIdNew);
            }
            for (TestResult testResultCollectionNewTestResult : testResultCollectionNew) {
                if (!testResultCollectionOld.contains(testResultCollectionNewTestResult)) {
                    TestRequest oldTestRequestIdOfTestResultCollectionNewTestResult = testResultCollectionNewTestResult.getTestRequestId();
                    testResultCollectionNewTestResult.setTestRequestId(testRequest);
                    testResultCollectionNewTestResult = em.merge(testResultCollectionNewTestResult);
                    if (oldTestRequestIdOfTestResultCollectionNewTestResult != null && !oldTestRequestIdOfTestResultCollectionNewTestResult.equals(testRequest)) {
                        oldTestRequestIdOfTestResultCollectionNewTestResult.getTestResultCollection().remove(testResultCollectionNewTestResult);
                        oldTestRequestIdOfTestResultCollectionNewTestResult = em.merge(oldTestRequestIdOfTestResultCollectionNewTestResult);
                    }
                }
            }
            for (TestTestRequest testTestRequestCollectionNewTestTestRequest : testTestRequestCollectionNew) {
                if (!testTestRequestCollectionOld.contains(testTestRequestCollectionNewTestTestRequest)) {
                    TestRequest oldTestRequestIdOfTestTestRequestCollectionNewTestTestRequest = testTestRequestCollectionNewTestTestRequest.getTestRequestId();
                    testTestRequestCollectionNewTestTestRequest.setTestRequestId(testRequest);
                    testTestRequestCollectionNewTestTestRequest = em.merge(testTestRequestCollectionNewTestTestRequest);
                    if (oldTestRequestIdOfTestTestRequestCollectionNewTestTestRequest != null && !oldTestRequestIdOfTestTestRequestCollectionNewTestTestRequest.equals(testRequest)) {
                        oldTestRequestIdOfTestTestRequestCollectionNewTestTestRequest.getTestTestRequestCollection().remove(testTestRequestCollectionNewTestTestRequest);
                        oldTestRequestIdOfTestTestRequestCollectionNewTestTestRequest = em.merge(oldTestRequestIdOfTestTestRequestCollectionNewTestTestRequest);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = testRequest.getId();
                if (findTestRequest(id) == null) {
                    throw new NonexistentEntityException("The testRequest with id " + id + " no longer exists.");
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
            TestRequest testRequest;
            try {
                testRequest = em.getReference(TestRequest.class, id);
                testRequest.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The testRequest with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TestResult> testResultCollectionOrphanCheck = testRequest.getTestResultCollection();
            for (TestResult testResultCollectionOrphanCheckTestResult : testResultCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TestRequest (" + testRequest + ") cannot be destroyed since the TestResult " + testResultCollectionOrphanCheckTestResult + " in its testResultCollection field has a non-nullable testRequestId field.");
            }
            Collection<TestTestRequest> testTestRequestCollectionOrphanCheck = testRequest.getTestTestRequestCollection();
            for (TestTestRequest testTestRequestCollectionOrphanCheckTestTestRequest : testTestRequestCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TestRequest (" + testRequest + ") cannot be destroyed since the TestTestRequest " + testTestRequestCollectionOrphanCheckTestTestRequest + " in its testTestRequestCollection field has a non-nullable testRequestId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Examination examinationId = testRequest.getExaminationId();
            if (examinationId != null) {
                examinationId.getTestRequestCollection().remove(testRequest);
                examinationId = em.merge(examinationId);
            }
            em.remove(testRequest);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TestRequest> findTestRequestEntities() {
        return findTestRequestEntities(true, -1, -1);
    }

    public List<TestRequest> findTestRequestEntities(int maxResults, int firstResult) {
        return findTestRequestEntities(false, maxResults, firstResult);
    }

    private List<TestRequest> findTestRequestEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TestRequest.class));
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

    public TestRequest findTestRequest(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TestRequest.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestRequestCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TestRequest> rt = cq.from(TestRequest.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public List<TestRequest> findByExaminationId(String id) {
        return (List<TestRequest>) getEntityManager().createNamedQuery("TestRequest.findByExaminationId").setParameter("examinationid", id).getResultList();
    }
    
}
