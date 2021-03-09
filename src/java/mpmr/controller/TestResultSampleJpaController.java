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
import mpmr.dto.TestResultSample;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class TestResultSampleJpaController implements Serializable {

    public TestResultSampleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TestResultSample testResultSample) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Test testId = testResultSample.getTestId();
            if (testId != null) {
                testId = em.getReference(testId.getClass(), testId.getId());
                testResultSample.setTestId(testId);
            }
            em.persist(testResultSample);
            if (testId != null) {
                testId.getTestResultSampleCollection().add(testResultSample);
                testId = em.merge(testId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTestResultSample(testResultSample.getId()) != null) {
                throw new PreexistingEntityException("TestResultSample " + testResultSample + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TestResultSample testResultSample) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TestResultSample persistentTestResultSample = em.find(TestResultSample.class, testResultSample.getId());
            Test testIdOld = persistentTestResultSample.getTestId();
            Test testIdNew = testResultSample.getTestId();
            if (testIdNew != null) {
                testIdNew = em.getReference(testIdNew.getClass(), testIdNew.getId());
                testResultSample.setTestId(testIdNew);
            }
            testResultSample = em.merge(testResultSample);
            if (testIdOld != null && !testIdOld.equals(testIdNew)) {
                testIdOld.getTestResultSampleCollection().remove(testResultSample);
                testIdOld = em.merge(testIdOld);
            }
            if (testIdNew != null && !testIdNew.equals(testIdOld)) {
                testIdNew.getTestResultSampleCollection().add(testResultSample);
                testIdNew = em.merge(testIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = testResultSample.getId();
                if (findTestResultSample(id) == null) {
                    throw new NonexistentEntityException("The testResultSample with id " + id + " no longer exists.");
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
            TestResultSample testResultSample;
            try {
                testResultSample = em.getReference(TestResultSample.class, id);
                testResultSample.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The testResultSample with id " + id + " no longer exists.", enfe);
            }
            Test testId = testResultSample.getTestId();
            if (testId != null) {
                testId.getTestResultSampleCollection().remove(testResultSample);
                testId = em.merge(testId);
            }
            em.remove(testResultSample);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TestResultSample> findTestResultSampleEntities() {
        return findTestResultSampleEntities(true, -1, -1);
    }

    public List<TestResultSample> findTestResultSampleEntities(int maxResults, int firstResult) {
        return findTestResultSampleEntities(false, maxResults, firstResult);
    }

    private List<TestResultSample> findTestResultSampleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TestResultSample.class));
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

    public TestResultSample findTestResultSample(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TestResultSample.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestResultSampleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TestResultSample> rt = cq.from(TestResultSample.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public List<TestResultSample> findAll() {
        return (List<TestResultSample>) getEntityManager().createNamedQuery("TestResultSample.findAll").getResultList();
    }
    public List<TestResultSample> findByTestId(String testId) {
        return (List<TestResultSample>) getEntityManager().createNamedQuery("TestResultSample.findByTestId").setParameter("testId", testId).getResultList();
    }
}
