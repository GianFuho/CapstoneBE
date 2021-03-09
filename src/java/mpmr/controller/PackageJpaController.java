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
import mpmr.dto.Package;
import mpmr.dto.PackageTest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class PackageJpaController implements Serializable {

    public PackageJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Package package1) throws PreexistingEntityException, Exception {
        if (package1.getPackageTestCollection() == null) {
            package1.setPackageTestCollection(new ArrayList<PackageTest>());
        }
        if (package1.getPackageCollection() == null) {
            package1.setPackageCollection(new ArrayList<Package>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Package packageId = package1.getPackageId();
            if (packageId != null) {
                packageId = em.getReference(packageId.getClass(), packageId.getId());
                package1.setPackageId(packageId);
            }
            Collection<PackageTest> attachedPackageTestCollection = new ArrayList<PackageTest>();
            for (PackageTest packageTestCollectionPackageTestToAttach : package1.getPackageTestCollection()) {
                packageTestCollectionPackageTestToAttach = em.getReference(packageTestCollectionPackageTestToAttach.getClass(), packageTestCollectionPackageTestToAttach.getId());
                attachedPackageTestCollection.add(packageTestCollectionPackageTestToAttach);
            }
            package1.setPackageTestCollection(attachedPackageTestCollection);
            Collection<Package> attachedPackageCollection = new ArrayList<Package>();
            for (Package packageCollectionPackageToAttach : package1.getPackageCollection()) {
                packageCollectionPackageToAttach = em.getReference(packageCollectionPackageToAttach.getClass(), packageCollectionPackageToAttach.getId());
                attachedPackageCollection.add(packageCollectionPackageToAttach);
            }
            package1.setPackageCollection(attachedPackageCollection);
            em.persist(package1);
            if (packageId != null) {
                packageId.getPackageCollection().add(package1);
                packageId = em.merge(packageId);
            }
            for (PackageTest packageTestCollectionPackageTest : package1.getPackageTestCollection()) {
                Package oldPackageIdOfPackageTestCollectionPackageTest = packageTestCollectionPackageTest.getPackageId();
                packageTestCollectionPackageTest.setPackageId(package1);
                packageTestCollectionPackageTest = em.merge(packageTestCollectionPackageTest);
                if (oldPackageIdOfPackageTestCollectionPackageTest != null) {
                    oldPackageIdOfPackageTestCollectionPackageTest.getPackageTestCollection().remove(packageTestCollectionPackageTest);
                    oldPackageIdOfPackageTestCollectionPackageTest = em.merge(oldPackageIdOfPackageTestCollectionPackageTest);
                }
            }
            for (Package packageCollectionPackage : package1.getPackageCollection()) {
                Package oldPackageIdOfPackageCollectionPackage = packageCollectionPackage.getPackageId();
                packageCollectionPackage.setPackageId(package1);
                packageCollectionPackage = em.merge(packageCollectionPackage);
                if (oldPackageIdOfPackageCollectionPackage != null) {
                    oldPackageIdOfPackageCollectionPackage.getPackageCollection().remove(packageCollectionPackage);
                    oldPackageIdOfPackageCollectionPackage = em.merge(oldPackageIdOfPackageCollectionPackage);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPackage(package1.getId()) != null) {
                throw new PreexistingEntityException("Package " + package1 + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Package package1) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Package persistentPackage = em.find(Package.class, package1.getId());
            Package packageIdOld = persistentPackage.getPackageId();
            Package packageIdNew = package1.getPackageId();
//            Collection<PackageTest> packageTestCollectionOld = persistentPackage.getPackageTestCollection();
//            Collection<PackageTest> packageTestCollectionNew = package1.getPackageTestCollection();
//            Collection<Package> packageCollectionOld = persistentPackage.getPackageCollection();
//            Collection<Package> packageCollectionNew = package1.getPackageCollection();
//            List<String> illegalOrphanMessages = null;
//            for (PackageTest packageTestCollectionOldPackageTest : packageTestCollectionOld) {
//                if (!packageTestCollectionNew.contains(packageTestCollectionOldPackageTest)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain PackageTest " + packageTestCollectionOldPackageTest + " since its packageId field is not nullable.");
//                }
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
            if (packageIdNew != null) {
                packageIdNew = em.getReference(packageIdNew.getClass(), packageIdNew.getId());
                package1.setPackageId(packageIdNew);
            }
//            Collection<PackageTest> attachedPackageTestCollectionNew = new ArrayList<PackageTest>();
//            for (PackageTest packageTestCollectionNewPackageTestToAttach : packageTestCollectionNew) {
//                packageTestCollectionNewPackageTestToAttach = em.getReference(packageTestCollectionNewPackageTestToAttach.getClass(), packageTestCollectionNewPackageTestToAttach.getId());
//                attachedPackageTestCollectionNew.add(packageTestCollectionNewPackageTestToAttach);
//            }
//            packageTestCollectionNew = attachedPackageTestCollectionNew;
//            package1.setPackageTestCollection(packageTestCollectionNew);
//            Collection<Package> attachedPackageCollectionNew = new ArrayList<Package>();
//            for (Package packageCollectionNewPackageToAttach : packageCollectionNew) {
//                packageCollectionNewPackageToAttach = em.getReference(packageCollectionNewPackageToAttach.getClass(), packageCollectionNewPackageToAttach.getId());
//                attachedPackageCollectionNew.add(packageCollectionNewPackageToAttach);
//            }
//            packageCollectionNew = attachedPackageCollectionNew;
//            package1.setPackageCollection(packageCollectionNew);
            package1 = em.merge(package1);
            if (packageIdOld != null && !packageIdOld.equals(packageIdNew)) {
                packageIdOld.getPackageCollection().remove(package1);
                packageIdOld = em.merge(packageIdOld);
            }
            if (packageIdNew != null && !packageIdNew.equals(packageIdOld)) {
                packageIdNew.getPackageCollection().add(package1);
                packageIdNew = em.merge(packageIdNew);
            }
//            for (PackageTest packageTestCollectionNewPackageTest : packageTestCollectionNew) {
//                if (!packageTestCollectionOld.contains(packageTestCollectionNewPackageTest)) {
//                    Package oldPackageIdOfPackageTestCollectionNewPackageTest = packageTestCollectionNewPackageTest.getPackageId();
//                    packageTestCollectionNewPackageTest.setPackageId(package1);
//                    packageTestCollectionNewPackageTest = em.merge(packageTestCollectionNewPackageTest);
//                    if (oldPackageIdOfPackageTestCollectionNewPackageTest != null && !oldPackageIdOfPackageTestCollectionNewPackageTest.equals(package1)) {
//                        oldPackageIdOfPackageTestCollectionNewPackageTest.getPackageTestCollection().remove(packageTestCollectionNewPackageTest);
//                        oldPackageIdOfPackageTestCollectionNewPackageTest = em.merge(oldPackageIdOfPackageTestCollectionNewPackageTest);
//                    }
//                }
//            }
//            for (Package packageCollectionOldPackage : packageCollectionOld) {
//                if (!packageCollectionNew.contains(packageCollectionOldPackage)) {
//                    packageCollectionOldPackage.setPackageId(null);
//                    packageCollectionOldPackage = em.merge(packageCollectionOldPackage);
//                }
//            }
//            for (Package packageCollectionNewPackage : packageCollectionNew) {
//                if (!packageCollectionOld.contains(packageCollectionNewPackage)) {
//                    Package oldPackageIdOfPackageCollectionNewPackage = packageCollectionNewPackage.getPackageId();
//                    packageCollectionNewPackage.setPackageId(package1);
//                    packageCollectionNewPackage = em.merge(packageCollectionNewPackage);
//                    if (oldPackageIdOfPackageCollectionNewPackage != null && !oldPackageIdOfPackageCollectionNewPackage.equals(package1)) {
//                        oldPackageIdOfPackageCollectionNewPackage.getPackageCollection().remove(packageCollectionNewPackage);
//                        oldPackageIdOfPackageCollectionNewPackage = em.merge(oldPackageIdOfPackageCollectionNewPackage);
//                    }
//                }
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = package1.getId();
                if (findPackage(id) == null) {
                    throw new NonexistentEntityException("The package with id " + id + " no longer exists.");
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
            Package package1;
            try {
                package1 = em.getReference(Package.class, id);
                package1.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The package1 with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PackageTest> packageTestCollectionOrphanCheck = package1.getPackageTestCollection();
            for (PackageTest packageTestCollectionOrphanCheckPackageTest : packageTestCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Package (" + package1 + ") cannot be destroyed since the PackageTest " + packageTestCollectionOrphanCheckPackageTest + " in its packageTestCollection field has a non-nullable packageId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Package packageId = package1.getPackageId();
            if (packageId != null) {
                packageId.getPackageCollection().remove(package1);
                packageId = em.merge(packageId);
            }
            Collection<Package> packageCollection = package1.getPackageCollection();
            for (Package packageCollectionPackage : packageCollection) {
                packageCollectionPackage.setPackageId(null);
                packageCollectionPackage = em.merge(packageCollectionPackage);
            }
            em.remove(package1);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Package> findPackageEntities() {
        return findPackageEntities(true, -1, -1);
    }

    public List<Package> findPackageEntities(int maxResults, int firstResult) {
        return findPackageEntities(false, maxResults, firstResult);
    }

    private List<Package> findPackageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Package.class));
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

    public Package findPackage(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Package.class, id);
        } finally {
            em.close();
        }
    }

    public int getPackageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Package> rt = cq.from(Package.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Package findByPackageId(String id) {
        return (Package) getEntityManager().createNamedQuery("Package.findByPackageId").setParameter("packageid", id).getSingleResult();
    }

    public List<Package> getAll() {
        return (List<Package>) getEntityManager().createNamedQuery("Package.findAll").getResultList();
    }
}
