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
import mpmr.dto.Lock;
import mpmr.dto.Rating;
import mpmr.dto.UserInfor;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class LockJpaController implements Serializable {

    public LockJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Lock lock) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rating ratingId = lock.getRatingId();
            if (ratingId != null) {
                ratingId = em.getReference(ratingId.getClass(), ratingId.getId());
                lock.setRatingId(ratingId);
            }
            UserInfor userId = lock.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                lock.setUserId(userId);
            }
            em.persist(lock);
            if (ratingId != null) {
                ratingId.getLockCollection().add(lock);
                ratingId = em.merge(ratingId);
            }
            if (userId != null) {
                userId.getLockCollection().add(lock);
                userId = em.merge(userId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLock(lock.getId()) != null) {
                throw new PreexistingEntityException("Lock " + lock + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Lock lock) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Lock persistentLock = em.find(Lock.class, lock.getId());
            Rating ratingIdOld = persistentLock.getRatingId();
            Rating ratingIdNew = lock.getRatingId();
            UserInfor userIdOld = persistentLock.getUserId();
            UserInfor userIdNew = lock.getUserId();
            if (ratingIdNew != null) {
                ratingIdNew = em.getReference(ratingIdNew.getClass(), ratingIdNew.getId());
                lock.setRatingId(ratingIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                lock.setUserId(userIdNew);
            }
            lock = em.merge(lock);
            if (ratingIdOld != null && !ratingIdOld.equals(ratingIdNew)) {
                ratingIdOld.getLockCollection().remove(lock);
                ratingIdOld = em.merge(ratingIdOld);
            }
            if (ratingIdNew != null && !ratingIdNew.equals(ratingIdOld)) {
                ratingIdNew.getLockCollection().add(lock);
                ratingIdNew = em.merge(ratingIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getLockCollection().remove(lock);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getLockCollection().add(lock);
                userIdNew = em.merge(userIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = lock.getId();
                if (findLock(id) == null) {
                    throw new NonexistentEntityException("The lock with id " + id + " no longer exists.");
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
            Lock lock;
            try {
                lock = em.getReference(Lock.class, id);
                lock.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lock with id " + id + " no longer exists.", enfe);
            }
            Rating ratingId = lock.getRatingId();
            if (ratingId != null) {
                ratingId.getLockCollection().remove(lock);
                ratingId = em.merge(ratingId);
            }
            UserInfor userId = lock.getUserId();
            if (userId != null) {
                userId.getLockCollection().remove(lock);
                userId = em.merge(userId);
            }
            em.remove(lock);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Lock> findLockEntities() {
        return findLockEntities(true, -1, -1);
    }

    public List<Lock> findLockEntities(int maxResults, int firstResult) {
        return findLockEntities(false, maxResults, firstResult);
    }

    private List<Lock> findLockEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lock.class));
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

    public Lock findLock(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lock.class, id);
        } finally {
            em.close();
        }
    }

    public int getLockCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lock> rt = cq.from(Lock.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
