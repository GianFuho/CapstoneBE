/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mpmr.dto.Test;
import mpmr.dto.TestResult;
import mpmr.dto.TestResultDetail;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class TestResultDetailJpaController implements Serializable {

    public TestResultDetailJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TestResultDetail testResultDetail) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Test testId = testResultDetail.getTestId();
            if (testId != null) {
                testId = em.getReference(testId.getClass(), testId.getId());
                testResultDetail.setTestId(testId);
            }
            TestResult testResultId = testResultDetail.getTestResultId();
            if (testResultId != null) {
                testResultId = em.getReference(testResultId.getClass(), testResultId.getId());
                testResultDetail.setTestResultId(testResultId);
            }
            em.persist(testResultDetail);
            if (testId != null) {
                testId.getTestResultDetailCollection().add(testResultDetail);
                testId = em.merge(testId);
            }
            if (testResultId != null) {
                testResultId.getTestResultDetailCollection().add(testResultDetail);
                testResultId = em.merge(testResultId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTestResultDetail(testResultDetail.getId()) != null) {
                throw new PreexistingEntityException("TestResultDetail " + testResultDetail + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TestResultDetail testResultDetail) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TestResultDetail persistentTestResultDetail = em.find(TestResultDetail.class, testResultDetail.getId());
            Test testIdOld = persistentTestResultDetail.getTestId();
            Test testIdNew = testResultDetail.getTestId();
            TestResult testResultIdOld = persistentTestResultDetail.getTestResultId();
            TestResult testResultIdNew = testResultDetail.getTestResultId();
            if (testIdNew != null) {
                testIdNew = em.getReference(testIdNew.getClass(), testIdNew.getId());
                testResultDetail.setTestId(testIdNew);
            }
            if (testResultIdNew != null) {
                testResultIdNew = em.getReference(testResultIdNew.getClass(), testResultIdNew.getId());
                testResultDetail.setTestResultId(testResultIdNew);
            }
            testResultDetail = em.merge(testResultDetail);
            if (testIdOld != null && !testIdOld.equals(testIdNew)) {
                testIdOld.getTestResultDetailCollection().remove(testResultDetail);
                testIdOld = em.merge(testIdOld);
            }
            if (testIdNew != null && !testIdNew.equals(testIdOld)) {
                testIdNew.getTestResultDetailCollection().add(testResultDetail);
                testIdNew = em.merge(testIdNew);
            }
            if (testResultIdOld != null && !testResultIdOld.equals(testResultIdNew)) {
                testResultIdOld.getTestResultDetailCollection().remove(testResultDetail);
                testResultIdOld = em.merge(testResultIdOld);
            }
            if (testResultIdNew != null && !testResultIdNew.equals(testResultIdOld)) {
                testResultIdNew.getTestResultDetailCollection().add(testResultDetail);
                testResultIdNew = em.merge(testResultIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = testResultDetail.getId();
                if (findTestResultDetail(id) == null) {
                    throw new NonexistentEntityException("The testResultDetail with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TestResultDetail testResultDetail;
            try {
                testResultDetail = em.getReference(TestResultDetail.class, id);
                testResultDetail.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The testResultDetail with id " + id + " no longer exists.", enfe);
            }
            Test testId = testResultDetail.getTestId();
            if (testId != null) {
                testId.getTestResultDetailCollection().remove(testResultDetail);
                testId = em.merge(testId);
            }
            TestResult testResultId = testResultDetail.getTestResultId();
            if (testResultId != null) {
                testResultId.getTestResultDetailCollection().remove(testResultDetail);
                testResultId = em.merge(testResultId);
            }
            em.remove(testResultDetail);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TestResultDetail> findTestResultDetailEntities() {
        return findTestResultDetailEntities(true, -1, -1);
    }

    public List<TestResultDetail> findTestResultDetailEntities(int maxResults, int firstResult) {
        return findTestResultDetailEntities(false, maxResults, firstResult);
    }

    private List<TestResultDetail> findTestResultDetailEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TestResultDetail.class));
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

    public TestResultDetail findTestResultDetail(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TestResultDetail.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestResultDetailCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TestResultDetail> rt = cq.from(TestResultDetail.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     public List<TestResultDetail> findByTestResultId(String id) {
        return (List<TestResultDetail>) getEntityManager().createNamedQuery("TestResultDetail.findByTestResultId").setParameter("testResultId", id).getResultList();
    }
       public List<TestResultDetail> findByTestId(String id) {
        return (List<TestResultDetail>) getEntityManager().createNamedQuery("TestResultDetail.findByTestId").setParameter("testId", id).getResultList();
    }
       public List<TestResultDetail> findByTestName(String name) {
        return (List<TestResultDetail>) getEntityManager().createNamedQuery("TestResultDetail.findByTestName").setParameter("testName", name).getResultList();
    }
        public List<TestResultDetail> findByTestValue(String testId, float startValue, float endValue) {
        return (List<TestResultDetail>) getEntityManager().createNamedQuery("TestResultDetail.findByTestValue").setParameter("testId", testId).setParameter("startValue", startValue).setParameter("endValue", endValue).getResultList();
    }
}
