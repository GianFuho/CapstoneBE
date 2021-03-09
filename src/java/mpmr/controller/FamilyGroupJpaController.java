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
import mpmr.dto.UserFamilyGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.dto.FamilyGroup;
import mpmr.dto.Request;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class FamilyGroupJpaController implements Serializable {

    public FamilyGroupJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FamilyGroup familyGroup) throws PreexistingEntityException, Exception {
        if (familyGroup.getUserFamilyGroupCollection() == null) {
            familyGroup.setUserFamilyGroupCollection(new ArrayList<UserFamilyGroup>());
        }
        if (familyGroup.getRequestCollection() == null) {
            familyGroup.setRequestCollection(new ArrayList<Request>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<UserFamilyGroup> attachedUserFamilyGroupCollection = new ArrayList<UserFamilyGroup>();
            for (UserFamilyGroup userFamilyGroupCollectionUserFamilyGroupToAttach : familyGroup.getUserFamilyGroupCollection()) {
                userFamilyGroupCollectionUserFamilyGroupToAttach = em.getReference(userFamilyGroupCollectionUserFamilyGroupToAttach.getClass(), userFamilyGroupCollectionUserFamilyGroupToAttach.getId());
                attachedUserFamilyGroupCollection.add(userFamilyGroupCollectionUserFamilyGroupToAttach);
            }
            familyGroup.setUserFamilyGroupCollection(attachedUserFamilyGroupCollection);
            Collection<Request> attachedRequestCollection = new ArrayList<Request>();
            for (Request requestCollectionRequestToAttach : familyGroup.getRequestCollection()) {
                requestCollectionRequestToAttach = em.getReference(requestCollectionRequestToAttach.getClass(), requestCollectionRequestToAttach.getId());
                attachedRequestCollection.add(requestCollectionRequestToAttach);
            }
            familyGroup.setRequestCollection(attachedRequestCollection);
            em.persist(familyGroup);
            for (UserFamilyGroup userFamilyGroupCollectionUserFamilyGroup : familyGroup.getUserFamilyGroupCollection()) {
                FamilyGroup oldFamilyGroupIdOfUserFamilyGroupCollectionUserFamilyGroup = userFamilyGroupCollectionUserFamilyGroup.getFamilyGroupId();
                userFamilyGroupCollectionUserFamilyGroup.setFamilyGroupId(familyGroup);
                userFamilyGroupCollectionUserFamilyGroup = em.merge(userFamilyGroupCollectionUserFamilyGroup);
                if (oldFamilyGroupIdOfUserFamilyGroupCollectionUserFamilyGroup != null) {
                    oldFamilyGroupIdOfUserFamilyGroupCollectionUserFamilyGroup.getUserFamilyGroupCollection().remove(userFamilyGroupCollectionUserFamilyGroup);
                    oldFamilyGroupIdOfUserFamilyGroupCollectionUserFamilyGroup = em.merge(oldFamilyGroupIdOfUserFamilyGroupCollectionUserFamilyGroup);
                }
            }
            for (Request requestCollectionRequest : familyGroup.getRequestCollection()) {
                FamilyGroup oldFamilyGroupIdOfRequestCollectionRequest = requestCollectionRequest.getFamilyGroupId();
                requestCollectionRequest.setFamilyGroupId(familyGroup);
                requestCollectionRequest = em.merge(requestCollectionRequest);
                if (oldFamilyGroupIdOfRequestCollectionRequest != null) {
                    oldFamilyGroupIdOfRequestCollectionRequest.getRequestCollection().remove(requestCollectionRequest);
                    oldFamilyGroupIdOfRequestCollectionRequest = em.merge(oldFamilyGroupIdOfRequestCollectionRequest);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFamilyGroup(familyGroup.getId()) != null) {
                throw new PreexistingEntityException("FamilyGroup " + familyGroup + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FamilyGroup familyGroup) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FamilyGroup persistentFamilyGroup = em.find(FamilyGroup.class, familyGroup.getId());
            Collection<UserFamilyGroup> userFamilyGroupCollectionOld = persistentFamilyGroup.getUserFamilyGroupCollection();
            Collection<UserFamilyGroup> userFamilyGroupCollectionNew = familyGroup.getUserFamilyGroupCollection();
            Collection<Request> requestCollectionOld = persistentFamilyGroup.getRequestCollection();
            Collection<Request> requestCollectionNew = familyGroup.getRequestCollection();
            List<String> illegalOrphanMessages = null;
            for (UserFamilyGroup userFamilyGroupCollectionOldUserFamilyGroup : userFamilyGroupCollectionOld) {
                if (!userFamilyGroupCollectionNew.contains(userFamilyGroupCollectionOldUserFamilyGroup)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserFamilyGroup " + userFamilyGroupCollectionOldUserFamilyGroup + " since its familyGroupId field is not nullable.");
                }
            }
            for (Request requestCollectionOldRequest : requestCollectionOld) {
                if (!requestCollectionNew.contains(requestCollectionOldRequest)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Request " + requestCollectionOldRequest + " since its familyGroupId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<UserFamilyGroup> attachedUserFamilyGroupCollectionNew = new ArrayList<UserFamilyGroup>();
            for (UserFamilyGroup userFamilyGroupCollectionNewUserFamilyGroupToAttach : userFamilyGroupCollectionNew) {
                userFamilyGroupCollectionNewUserFamilyGroupToAttach = em.getReference(userFamilyGroupCollectionNewUserFamilyGroupToAttach.getClass(), userFamilyGroupCollectionNewUserFamilyGroupToAttach.getId());
                attachedUserFamilyGroupCollectionNew.add(userFamilyGroupCollectionNewUserFamilyGroupToAttach);
            }
            userFamilyGroupCollectionNew = attachedUserFamilyGroupCollectionNew;
            familyGroup.setUserFamilyGroupCollection(userFamilyGroupCollectionNew);
            Collection<Request> attachedRequestCollectionNew = new ArrayList<Request>();
            for (Request requestCollectionNewRequestToAttach : requestCollectionNew) {
                requestCollectionNewRequestToAttach = em.getReference(requestCollectionNewRequestToAttach.getClass(), requestCollectionNewRequestToAttach.getId());
                attachedRequestCollectionNew.add(requestCollectionNewRequestToAttach);
            }
            requestCollectionNew = attachedRequestCollectionNew;
            familyGroup.setRequestCollection(requestCollectionNew);
            familyGroup = em.merge(familyGroup);
            for (UserFamilyGroup userFamilyGroupCollectionNewUserFamilyGroup : userFamilyGroupCollectionNew) {
                if (!userFamilyGroupCollectionOld.contains(userFamilyGroupCollectionNewUserFamilyGroup)) {
                    FamilyGroup oldFamilyGroupIdOfUserFamilyGroupCollectionNewUserFamilyGroup = userFamilyGroupCollectionNewUserFamilyGroup.getFamilyGroupId();
                    userFamilyGroupCollectionNewUserFamilyGroup.setFamilyGroupId(familyGroup);
                    userFamilyGroupCollectionNewUserFamilyGroup = em.merge(userFamilyGroupCollectionNewUserFamilyGroup);
                    if (oldFamilyGroupIdOfUserFamilyGroupCollectionNewUserFamilyGroup != null && !oldFamilyGroupIdOfUserFamilyGroupCollectionNewUserFamilyGroup.equals(familyGroup)) {
                        oldFamilyGroupIdOfUserFamilyGroupCollectionNewUserFamilyGroup.getUserFamilyGroupCollection().remove(userFamilyGroupCollectionNewUserFamilyGroup);
                        oldFamilyGroupIdOfUserFamilyGroupCollectionNewUserFamilyGroup = em.merge(oldFamilyGroupIdOfUserFamilyGroupCollectionNewUserFamilyGroup);
                    }
                }
            }
            for (Request requestCollectionNewRequest : requestCollectionNew) {
                if (!requestCollectionOld.contains(requestCollectionNewRequest)) {
                    FamilyGroup oldFamilyGroupIdOfRequestCollectionNewRequest = requestCollectionNewRequest.getFamilyGroupId();
                    requestCollectionNewRequest.setFamilyGroupId(familyGroup);
                    requestCollectionNewRequest = em.merge(requestCollectionNewRequest);
                    if (oldFamilyGroupIdOfRequestCollectionNewRequest != null && !oldFamilyGroupIdOfRequestCollectionNewRequest.equals(familyGroup)) {
                        oldFamilyGroupIdOfRequestCollectionNewRequest.getRequestCollection().remove(requestCollectionNewRequest);
                        oldFamilyGroupIdOfRequestCollectionNewRequest = em.merge(oldFamilyGroupIdOfRequestCollectionNewRequest);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = familyGroup.getId();
                if (findFamilyGroup(id) == null) {
                    throw new NonexistentEntityException("The familyGroup with id " + id + " no longer exists.");
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
            FamilyGroup familyGroup;
            try {
                familyGroup = em.getReference(FamilyGroup.class, id);
                familyGroup.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The familyGroup with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UserFamilyGroup> userFamilyGroupCollectionOrphanCheck = familyGroup.getUserFamilyGroupCollection();
            for (UserFamilyGroup userFamilyGroupCollectionOrphanCheckUserFamilyGroup : userFamilyGroupCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This FamilyGroup (" + familyGroup + ") cannot be destroyed since the UserFamilyGroup " + userFamilyGroupCollectionOrphanCheckUserFamilyGroup + " in its userFamilyGroupCollection field has a non-nullable familyGroupId field.");
            }
            Collection<Request> requestCollectionOrphanCheck = familyGroup.getRequestCollection();
            for (Request requestCollectionOrphanCheckRequest : requestCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This FamilyGroup (" + familyGroup + ") cannot be destroyed since the Request " + requestCollectionOrphanCheckRequest + " in its requestCollection field has a non-nullable familyGroupId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(familyGroup);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FamilyGroup> findFamilyGroupEntities() {
        return findFamilyGroupEntities(true, -1, -1);
    }

    public List<FamilyGroup> findFamilyGroupEntities(int maxResults, int firstResult) {
        return findFamilyGroupEntities(false, maxResults, firstResult);
    }

    private List<FamilyGroup> findFamilyGroupEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FamilyGroup.class));
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

    public FamilyGroup findFamilyGroup(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FamilyGroup.class, id);
        } finally {
            em.close();
        }
    }

    public int getFamilyGroupCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FamilyGroup> rt = cq.from(FamilyGroup.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
