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
import mpmr.dto.Package;
import mpmr.dto.PackageTest;
import mpmr.dto.Test;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class PackageTestJpaController implements Serializable {

    public PackageTestJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PackageTest packageTest) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Package packageId = packageTest.getPackageId();
            if (packageId != null) {
                packageId = em.getReference(packageId.getClass(), packageId.getId());
                packageTest.setPackageId(packageId);
            }
            Test testId = packageTest.getTestId();
            if (testId != null) {
                testId = em.getReference(testId.getClass(), testId.getId());
                packageTest.setTestId(testId);
            }
            em.persist(packageTest);
            if (packageId != null) {
                packageId.getPackageTestCollection().add(packageTest);
                packageId = em.merge(packageId);
            }
            if (testId != null) {
                testId.getPackageTestCollection().add(packageTest);
                testId = em.merge(testId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPackageTest(packageTest.getId()) != null) {
                throw new PreexistingEntityException("PackageTest " + packageTest + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PackageTest packageTest) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PackageTest persistentPackageTest = em.find(PackageTest.class, packageTest.getId());
            Package packageIdOld = persistentPackageTest.getPackageId();
            Package packageIdNew = packageTest.getPackageId();
            Test testIdOld = persistentPackageTest.getTestId();
            Test testIdNew = packageTest.getTestId();
            if (packageIdNew != null) {
                packageIdNew = em.getReference(packageIdNew.getClass(), packageIdNew.getId());
                packageTest.setPackageId(packageIdNew);
            }
            if (testIdNew != null) {
                testIdNew = em.getReference(testIdNew.getClass(), testIdNew.getId());
                packageTest.setTestId(testIdNew);
            }
            packageTest = em.merge(packageTest);
            if (packageIdOld != null && !packageIdOld.equals(packageIdNew)) {
                packageIdOld.getPackageTestCollection().remove(packageTest);
                packageIdOld = em.merge(packageIdOld);
            }
            if (packageIdNew != null && !packageIdNew.equals(packageIdOld)) {
                packageIdNew.getPackageTestCollection().add(packageTest);
                packageIdNew = em.merge(packageIdNew);
            }
            if (testIdOld != null && !testIdOld.equals(testIdNew)) {
                testIdOld.getPackageTestCollection().remove(packageTest);
                testIdOld = em.merge(testIdOld);
            }
            if (testIdNew != null && !testIdNew.equals(testIdOld)) {
                testIdNew.getPackageTestCollection().add(packageTest);
                testIdNew = em.merge(testIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = packageTest.getId();
                if (findPackageTest(id) == null) {
                    throw new NonexistentEntityException("The packageTest with id " + id + " no longer exists.");
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
            PackageTest packageTest;
            try {
                packageTest = em.getReference(PackageTest.class, id);
                packageTest.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The packageTest with id " + id + " no longer exists.", enfe);
            }
            Package packageId = packageTest.getPackageId();
            if (packageId != null) {
                packageId.getPackageTestCollection().remove(packageTest);
                packageId = em.merge(packageId);
            }
            Test testId = packageTest.getTestId();
            if (testId != null) {
                testId.getPackageTestCollection().remove(packageTest);
                testId = em.merge(testId);
            }
            em.remove(packageTest);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PackageTest> findPackageTestEntities() {
        return findPackageTestEntities(true, -1, -1);
    }

    public List<PackageTest> findPackageTestEntities(int maxResults, int firstResult) {
        return findPackageTestEntities(false, maxResults, firstResult);
    }

    private List<PackageTest> findPackageTestEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PackageTest.class));
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

    public PackageTest findPackageTest(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PackageTest.class, id);
        } finally {
            em.close();
        }
    }

    public int getPackageTestCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PackageTest> rt = cq.from(PackageTest.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public PackageTest findByTestId(String id) {
        return (PackageTest) getEntityManager().createNamedQuery("PackageTest.findByTestId").setParameter("testid", id).getSingleResult();
    }

    public List<PackageTest> findByPackageId(String id) {
        return (List<PackageTest>) getEntityManager().createNamedQuery("PackageTest.findByPackageId").setParameter("packageId", id).getResultList();
    }

    public List<PackageTest> findByCount(int count) {
        return (List<PackageTest>) getEntityManager().createNamedQuery("PackageTest.findByCount").setParameter("count", count).getResultList();
    }

    public List<PackageTest> findAll() {
        return (List<PackageTest>) getEntityManager().createNamedQuery("PackageTest.findAll").getResultList();
    }

    public List<PackageTest> findByName(String name) {
        return (List<PackageTest>) getEntityManager().createNamedQuery("PackageTest.findByName").setParameter("name", name).getResultList();
    }
}
