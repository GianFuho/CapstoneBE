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
import mpmr.dto.UserInfor;
import mpmr.dto.Lock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.dto.Rating;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class RatingJpaController implements Serializable {

    public RatingJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rating rating) throws PreexistingEntityException, Exception {
        if (rating.getLockCollection() == null) {
            rating.setLockCollection(new ArrayList<Lock>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Examination examinationId = rating.getExaminationId();
            if (examinationId != null) {
                examinationId = em.getReference(examinationId.getClass(), examinationId.getId());
                rating.setExaminationId(examinationId);
            }
            UserInfor userId = rating.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                rating.setUserId(userId);
            }
            Collection<Lock> attachedLockCollection = new ArrayList<Lock>();
            for (Lock lockCollectionLockToAttach : rating.getLockCollection()) {
                lockCollectionLockToAttach = em.getReference(lockCollectionLockToAttach.getClass(), lockCollectionLockToAttach.getId());
                attachedLockCollection.add(lockCollectionLockToAttach);
            }
            rating.setLockCollection(attachedLockCollection);
            em.persist(rating);
            if (examinationId != null) {
                examinationId.getRatingCollection().add(rating);
                examinationId = em.merge(examinationId);
            }
            if (userId != null) {
                userId.getRatingCollection().add(rating);
                userId = em.merge(userId);
            }
            for (Lock lockCollectionLock : rating.getLockCollection()) {
                Rating oldRatingIdOfLockCollectionLock = lockCollectionLock.getRatingId();
                lockCollectionLock.setRatingId(rating);
                lockCollectionLock = em.merge(lockCollectionLock);
                if (oldRatingIdOfLockCollectionLock != null) {
                    oldRatingIdOfLockCollectionLock.getLockCollection().remove(lockCollectionLock);
                    oldRatingIdOfLockCollectionLock = em.merge(oldRatingIdOfLockCollectionLock);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRating(rating.getId()) != null) {
                throw new PreexistingEntityException("Rating " + rating + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rating rating) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rating persistentRating = em.find(Rating.class, rating.getId());
            Examination examinationIdOld = persistentRating.getExaminationId();
            Examination examinationIdNew = rating.getExaminationId();
            UserInfor userIdOld = persistentRating.getUserId();
            UserInfor userIdNew = rating.getUserId();
//            Collection<Lock> lockCollectionOld = persistentRating.getLockCollection();
//            Collection<Lock> lockCollectionNew = rating.getLockCollection();
//            List<String> illegalOrphanMessages = null;
//            for (Lock lockCollectionOldLock : lockCollectionOld) {
//                if (!lockCollectionNew.contains(lockCollectionOldLock)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain Lock " + lockCollectionOldLock + " since its ratingId field is not nullable.");
//                }
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
            if (examinationIdNew != null) {
                examinationIdNew = em.getReference(examinationIdNew.getClass(), examinationIdNew.getId());
                rating.setExaminationId(examinationIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                rating.setUserId(userIdNew);
            }
//            Collection<Lock> attachedLockCollectionNew = new ArrayList<Lock>();
//            for (Lock lockCollectionNewLockToAttach : lockCollectionNew) {
//                lockCollectionNewLockToAttach = em.getReference(lockCollectionNewLockToAttach.getClass(), lockCollectionNewLockToAttach.getId());
//                attachedLockCollectionNew.add(lockCollectionNewLockToAttach);
//            }
//            lockCollectionNew = attachedLockCollectionNew;
//            rating.setLockCollection(lockCollectionNew);
            rating = em.merge(rating);
            if (examinationIdOld != null && !examinationIdOld.equals(examinationIdNew)) {
                examinationIdOld.getRatingCollection().remove(rating);
                examinationIdOld = em.merge(examinationIdOld);
            }
            if (examinationIdNew != null && !examinationIdNew.equals(examinationIdOld)) {
                examinationIdNew.getRatingCollection().add(rating);
                examinationIdNew = em.merge(examinationIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getRatingCollection().remove(rating);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getRatingCollection().add(rating);
                userIdNew = em.merge(userIdNew);
            }
//            for (Lock lockCollectionNewLock : lockCollectionNew) {
//                if (!lockCollectionOld.contains(lockCollectionNewLock)) {
//                    Rating oldRatingIdOfLockCollectionNewLock = lockCollectionNewLock.getRatingId();
//                    lockCollectionNewLock.setRatingId(rating);
//                    lockCollectionNewLock = em.merge(lockCollectionNewLock);
//                    if (oldRatingIdOfLockCollectionNewLock != null && !oldRatingIdOfLockCollectionNewLock.equals(rating)) {
//                        oldRatingIdOfLockCollectionNewLock.getLockCollection().remove(lockCollectionNewLock);
//                        oldRatingIdOfLockCollectionNewLock = em.merge(oldRatingIdOfLockCollectionNewLock);
//                    }
//                }
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = rating.getId();
                if (findRating(id) == null) {
                    throw new NonexistentEntityException("The rating with id " + id + " no longer exists.");
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
            Rating rating;
            try {
                rating = em.getReference(Rating.class, id);
                rating.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rating with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Lock> lockCollectionOrphanCheck = rating.getLockCollection();
            for (Lock lockCollectionOrphanCheckLock : lockCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rating (" + rating + ") cannot be destroyed since the Lock " + lockCollectionOrphanCheckLock + " in its lockCollection field has a non-nullable ratingId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Examination examinationId = rating.getExaminationId();
            if (examinationId != null) {
                examinationId.getRatingCollection().remove(rating);
                examinationId = em.merge(examinationId);
            }
            UserInfor userId = rating.getUserId();
            if (userId != null) {
                userId.getRatingCollection().remove(rating);
                userId = em.merge(userId);
            }
            em.remove(rating);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rating> findRatingEntities() {
        return findRatingEntities(true, -1, -1);
    }

    public List<Rating> findRatingEntities(int maxResults, int firstResult) {
        return findRatingEntities(false, maxResults, firstResult);
    }

    private List<Rating> findRatingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rating.class));
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

    public Rating findRating(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rating.class, id);
        } finally {
            em.close();
        }
    }

    public int getRatingCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rating> rt = cq.from(Rating.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Rating findByExaminationId(String id) {
        return (Rating) getEntityManager().createNamedQuery("Rating.findByExaminationId").setParameter("examinationid", id).getSingleResult();
    }
    public List<Rating> findByClinicId(String id) {
        return (List<Rating>) getEntityManager().createNamedQuery("Rating.findByClinicId").setParameter("clinicid", id).setParameter("status","Rated").getResultList();
    }

    public List<Rating> findByStatus(String status) {
        return (List<Rating>) getEntityManager().createNamedQuery("Rating.findByStatus").setParameter("status", status).getResultList();
    }
}
