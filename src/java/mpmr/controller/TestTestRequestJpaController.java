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
import mpmr.dto.TestRequest;
import mpmr.dto.TestTestRequest;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class TestTestRequestJpaController implements Serializable {

    public TestTestRequestJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TestTestRequest testTestRequest) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Test testId = testTestRequest.getTestId();
            if (testId != null) {
                testId = em.getReference(testId.getClass(), testId.getId());
                testTestRequest.setTestId(testId);
            }
            TestRequest testRequestId = testTestRequest.getTestRequestId();
            if (testRequestId != null) {
                testRequestId = em.getReference(testRequestId.getClass(), testRequestId.getId());
                testTestRequest.setTestRequestId(testRequestId);
            }
            em.persist(testTestRequest);
            if (testId != null) {
                testId.getTestTestRequestCollection().add(testTestRequest);
                testId = em.merge(testId);
            }
            if (testRequestId != null) {
                testRequestId.getTestTestRequestCollection().add(testTestRequest);
                testRequestId = em.merge(testRequestId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTestTestRequest(testTestRequest.getId()) != null) {
                throw new PreexistingEntityException("TestTestRequest " + testTestRequest + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TestTestRequest testTestRequest) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TestTestRequest persistentTestTestRequest = em.find(TestTestRequest.class, testTestRequest.getId());
            Test testIdOld = persistentTestTestRequest.getTestId();
            Test testIdNew = testTestRequest.getTestId();
            TestRequest testRequestIdOld = persistentTestTestRequest.getTestRequestId();
            TestRequest testRequestIdNew = testTestRequest.getTestRequestId();
            if (testIdNew != null) {
                testIdNew = em.getReference(testIdNew.getClass(), testIdNew.getId());
                testTestRequest.setTestId(testIdNew);
            }
            if (testRequestIdNew != null) {
                testRequestIdNew = em.getReference(testRequestIdNew.getClass(), testRequestIdNew.getId());
                testTestRequest.setTestRequestId(testRequestIdNew);
            }
            testTestRequest = em.merge(testTestRequest);
            if (testIdOld != null && !testIdOld.equals(testIdNew)) {
                testIdOld.getTestTestRequestCollection().remove(testTestRequest);
                testIdOld = em.merge(testIdOld);
            }
            if (testIdNew != null && !testIdNew.equals(testIdOld)) {
                testIdNew.getTestTestRequestCollection().add(testTestRequest);
                testIdNew = em.merge(testIdNew);
            }
            if (testRequestIdOld != null && !testRequestIdOld.equals(testRequestIdNew)) {
                testRequestIdOld.getTestTestRequestCollection().remove(testTestRequest);
                testRequestIdOld = em.merge(testRequestIdOld);
            }
            if (testRequestIdNew != null && !testRequestIdNew.equals(testRequestIdOld)) {
                testRequestIdNew.getTestTestRequestCollection().add(testTestRequest);
                testRequestIdNew = em.merge(testRequestIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = testTestRequest.getId();
                if (findTestTestRequest(id) == null) {
                    throw new NonexistentEntityException("The testTestRequest with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TestTestRequest testTestRequest;
            try {
                testTestRequest = em.getReference(TestTestRequest.class, id);
                testTestRequest.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The testTestRequest with id " + id + " no longer exists.", enfe);
            }
            Test testId = testTestRequest.getTestId();
            if (testId != null) {
                testId.getTestTestRequestCollection().remove(testTestRequest);
                testId = em.merge(testId);
            }
            TestRequest testRequestId = testTestRequest.getTestRequestId();
            if (testRequestId != null) {
                testRequestId.getTestTestRequestCollection().remove(testTestRequest);
                testRequestId = em.merge(testRequestId);
            }
            em.remove(testTestRequest);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TestTestRequest> findTestTestRequestEntities() {
        return findTestTestRequestEntities(true, -1, -1);
    }

    public List<TestTestRequest> findTestTestRequestEntities(int maxResults, int firstResult) {
        return findTestTestRequestEntities(false, maxResults, firstResult);
    }

    private List<TestTestRequest> findTestTestRequestEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TestTestRequest.class));
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

    public TestTestRequest findTestTestRequest(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TestTestRequest.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestTestRequestCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TestTestRequest> rt = cq.from(TestTestRequest.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<TestTestRequest> findByTestRequestId(String id) {
        return (List<TestTestRequest>) getEntityManager().createNamedQuery("TestTestRequest.findByTestRequestId").setParameter("testrequestid", id).getResultList();
    }

    public List<TestTestRequest> findByExamination(String examinationId) {
        return (List<TestTestRequest>) getEntityManager().createNamedQuery("TestTestRequest.findByExamination").setParameter("examinationId", examinationId).getResultList();
    }
    public List<TestTestRequest> findTestRequestIdByCount(int count) {
        return (List<TestTestRequest>) getEntityManager().createNamedQuery("TestTestRequest.findTestRequestIdByCount").setParameter("count", count).getResultList();
    }
    public Long findCountByTestRequestId(String id) {
        return (Long) getEntityManager().createNamedQuery("TestTestRequest.findCountByTestRequestId").setParameter("testrequestid", id).getSingleResult();
    }
}
