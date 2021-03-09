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
import mpmr.dto.FamilyGroup;
import mpmr.dto.UserFamilyGroup;
import mpmr.dto.UserInfor;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class UserFamilyGroupJpaController implements Serializable {

    public UserFamilyGroupJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserFamilyGroup userFamilyGroup) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FamilyGroup familyGroupId = userFamilyGroup.getFamilyGroupId();
            if (familyGroupId != null) {
                familyGroupId = em.getReference(familyGroupId.getClass(), familyGroupId.getId());
                userFamilyGroup.setFamilyGroupId(familyGroupId);
            }
            UserInfor userId = userFamilyGroup.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                userFamilyGroup.setUserId(userId);
            }
            em.persist(userFamilyGroup);
            if (familyGroupId != null) {
                familyGroupId.getUserFamilyGroupCollection().add(userFamilyGroup);
                familyGroupId = em.merge(familyGroupId);
            }
            if (userId != null) {
                userId.getUserFamilyGroupCollection().add(userFamilyGroup);
                userId = em.merge(userId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUserFamilyGroup(userFamilyGroup.getId()) != null) {
                throw new PreexistingEntityException("UserFamilyGroup " + userFamilyGroup + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserFamilyGroup userFamilyGroup) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserFamilyGroup persistentUserFamilyGroup = em.find(UserFamilyGroup.class, userFamilyGroup.getId());
            FamilyGroup familyGroupIdOld = persistentUserFamilyGroup.getFamilyGroupId();
            FamilyGroup familyGroupIdNew = userFamilyGroup.getFamilyGroupId();
            UserInfor userIdOld = persistentUserFamilyGroup.getUserId();
            UserInfor userIdNew = userFamilyGroup.getUserId();
            if (familyGroupIdNew != null) {
                familyGroupIdNew = em.getReference(familyGroupIdNew.getClass(), familyGroupIdNew.getId());
                userFamilyGroup.setFamilyGroupId(familyGroupIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                userFamilyGroup.setUserId(userIdNew);
            }
            userFamilyGroup = em.merge(userFamilyGroup);
            if (familyGroupIdOld != null && !familyGroupIdOld.equals(familyGroupIdNew)) {
                familyGroupIdOld.getUserFamilyGroupCollection().remove(userFamilyGroup);
                familyGroupIdOld = em.merge(familyGroupIdOld);
            }
            if (familyGroupIdNew != null && !familyGroupIdNew.equals(familyGroupIdOld)) {
                familyGroupIdNew.getUserFamilyGroupCollection().add(userFamilyGroup);
                familyGroupIdNew = em.merge(familyGroupIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getUserFamilyGroupCollection().remove(userFamilyGroup);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getUserFamilyGroupCollection().add(userFamilyGroup);
                userIdNew = em.merge(userIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userFamilyGroup.getId();
                if (findUserFamilyGroup(id) == null) {
                    throw new NonexistentEntityException("The userFamilyGroup with id " + id + " no longer exists.");
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
            UserFamilyGroup userFamilyGroup;
            try {
                userFamilyGroup = em.getReference(UserFamilyGroup.class, id);
                userFamilyGroup.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userFamilyGroup with id " + id + " no longer exists.", enfe);
            }
            FamilyGroup familyGroupId = userFamilyGroup.getFamilyGroupId();
            if (familyGroupId != null) {
                familyGroupId.getUserFamilyGroupCollection().remove(userFamilyGroup);
                familyGroupId = em.merge(familyGroupId);
            }
            UserInfor userId = userFamilyGroup.getUserId();
            if (userId != null) {
                userId.getUserFamilyGroupCollection().remove(userFamilyGroup);
                userId = em.merge(userId);
            }
            em.remove(userFamilyGroup);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserFamilyGroup> findUserFamilyGroupEntities() {
        return findUserFamilyGroupEntities(true, -1, -1);
    }

    public List<UserFamilyGroup> findUserFamilyGroupEntities(int maxResults, int firstResult) {
        return findUserFamilyGroupEntities(false, maxResults, firstResult);
    }

    private List<UserFamilyGroup> findUserFamilyGroupEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserFamilyGroup.class));
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

    public UserFamilyGroup findUserFamilyGroup(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserFamilyGroup.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserFamilyGroupCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserFamilyGroup> rt = cq.from(UserFamilyGroup.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<UserFamilyGroup> findByUserId(String userId) {
        return (List<UserFamilyGroup>) getEntityManager().createNamedQuery("UserFamilyGroup.findByUserId").setParameter("userId", userId).getResultList();
    }

    public List<UserFamilyGroup> findGroupId(String familyGroupId) {
        return (List<UserFamilyGroup>) getEntityManager().createNamedQuery("UserFamilyGroup.findGroupId").setParameter("familyGroupId", familyGroupId).getResultList();
    }
    public List<UserFamilyGroup> findGroupLeader(String familyGroupId) {
        return (List<UserFamilyGroup>) getEntityManager().createNamedQuery("UserFamilyGroup.findGroupLeader").setParameter("familyGroupId", familyGroupId).setParameter("grouprole", "Leader").getResultList();
    }

    public Long count(String id) {
        return (Long) getEntityManager().createNamedQuery("UserFamilyGroup.findCount").setParameter("familyGroupId", id).getSingleResult();
    }

}
