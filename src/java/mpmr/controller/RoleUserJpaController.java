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
import mpmr.dto.UserInfor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.dto.RoleUser;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class RoleUserJpaController implements Serializable {

    public RoleUserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RoleUser roleUser) throws PreexistingEntityException, Exception {
        if (roleUser.getUserInforCollection() == null) {
            roleUser.setUserInforCollection(new ArrayList<UserInfor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<UserInfor> attachedUserInforCollection = new ArrayList<UserInfor>();
            for (UserInfor userInforCollectionUserInforToAttach : roleUser.getUserInforCollection()) {
                userInforCollectionUserInforToAttach = em.getReference(userInforCollectionUserInforToAttach.getClass(), userInforCollectionUserInforToAttach.getId());
                attachedUserInforCollection.add(userInforCollectionUserInforToAttach);
            }
            roleUser.setUserInforCollection(attachedUserInforCollection);
            em.persist(roleUser);
            for (UserInfor userInforCollectionUserInfor : roleUser.getUserInforCollection()) {
                RoleUser oldRoleIdOfUserInforCollectionUserInfor = userInforCollectionUserInfor.getRoleId();
                userInforCollectionUserInfor.setRoleId(roleUser);
                userInforCollectionUserInfor = em.merge(userInforCollectionUserInfor);
                if (oldRoleIdOfUserInforCollectionUserInfor != null) {
                    oldRoleIdOfUserInforCollectionUserInfor.getUserInforCollection().remove(userInforCollectionUserInfor);
                    oldRoleIdOfUserInforCollectionUserInfor = em.merge(oldRoleIdOfUserInforCollectionUserInfor);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRoleUser(roleUser.getId()) != null) {
                throw new PreexistingEntityException("RoleUser " + roleUser + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RoleUser roleUser) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RoleUser persistentRoleUser = em.find(RoleUser.class, roleUser.getId());
            Collection<UserInfor> userInforCollectionOld = persistentRoleUser.getUserInforCollection();
            Collection<UserInfor> userInforCollectionNew = roleUser.getUserInforCollection();
            List<String> illegalOrphanMessages = null;
            for (UserInfor userInforCollectionOldUserInfor : userInforCollectionOld) {
                if (!userInforCollectionNew.contains(userInforCollectionOldUserInfor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserInfor " + userInforCollectionOldUserInfor + " since its roleId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<UserInfor> attachedUserInforCollectionNew = new ArrayList<UserInfor>();
            for (UserInfor userInforCollectionNewUserInforToAttach : userInforCollectionNew) {
                userInforCollectionNewUserInforToAttach = em.getReference(userInforCollectionNewUserInforToAttach.getClass(), userInforCollectionNewUserInforToAttach.getId());
                attachedUserInforCollectionNew.add(userInforCollectionNewUserInforToAttach);
            }
            userInforCollectionNew = attachedUserInforCollectionNew;
            roleUser.setUserInforCollection(userInforCollectionNew);
            roleUser = em.merge(roleUser);
            for (UserInfor userInforCollectionNewUserInfor : userInforCollectionNew) {
                if (!userInforCollectionOld.contains(userInforCollectionNewUserInfor)) {
                    RoleUser oldRoleIdOfUserInforCollectionNewUserInfor = userInforCollectionNewUserInfor.getRoleId();
                    userInforCollectionNewUserInfor.setRoleId(roleUser);
                    userInforCollectionNewUserInfor = em.merge(userInforCollectionNewUserInfor);
                    if (oldRoleIdOfUserInforCollectionNewUserInfor != null && !oldRoleIdOfUserInforCollectionNewUserInfor.equals(roleUser)) {
                        oldRoleIdOfUserInforCollectionNewUserInfor.getUserInforCollection().remove(userInforCollectionNewUserInfor);
                        oldRoleIdOfUserInforCollectionNewUserInfor = em.merge(oldRoleIdOfUserInforCollectionNewUserInfor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = roleUser.getId();
                if (findRoleUser(id) == null) {
                    throw new NonexistentEntityException("The roleUser with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RoleUser roleUser;
            try {
                roleUser = em.getReference(RoleUser.class, id);
                roleUser.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roleUser with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UserInfor> userInforCollectionOrphanCheck = roleUser.getUserInforCollection();
            for (UserInfor userInforCollectionOrphanCheckUserInfor : userInforCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RoleUser (" + roleUser + ") cannot be destroyed since the UserInfor " + userInforCollectionOrphanCheckUserInfor + " in its userInforCollection field has a non-nullable roleId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(roleUser);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RoleUser> findRoleUserEntities() {
        return findRoleUserEntities(true, -1, -1);
    }

    public List<RoleUser> findRoleUserEntities(int maxResults, int firstResult) {
        return findRoleUserEntities(false, maxResults, firstResult);
    }

    private List<RoleUser> findRoleUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RoleUser.class));
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

    public RoleUser findRoleUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RoleUser.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoleUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RoleUser> rt = cq.from(RoleUser.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
