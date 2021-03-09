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
import mpmr.dto.Request;
import mpmr.dto.UserInfor;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class RequestJpaController implements Serializable {

    public RequestJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Request request) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FamilyGroup familyGroupId = request.getFamilyGroupId();
            if (familyGroupId != null) {
                familyGroupId = em.getReference(familyGroupId.getClass(), familyGroupId.getId());
                request.setFamilyGroupId(familyGroupId);
            }
            UserInfor userId = request.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                request.setUserId(userId);
            }
            em.persist(request);
            if (familyGroupId != null) {
                familyGroupId.getRequestCollection().add(request);
                familyGroupId = em.merge(familyGroupId);
            }
            if (userId != null) {
                userId.getRequestCollection().add(request);
                userId = em.merge(userId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRequest(request.getId()) != null) {
                throw new PreexistingEntityException("Request " + request + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Request request) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Request persistentRequest = em.find(Request.class, request.getId());
            FamilyGroup familyGroupIdOld = persistentRequest.getFamilyGroupId();
            FamilyGroup familyGroupIdNew = request.getFamilyGroupId();
            UserInfor userIdOld = persistentRequest.getUserId();
            UserInfor userIdNew = request.getUserId();
            if (familyGroupIdNew != null) {
                familyGroupIdNew = em.getReference(familyGroupIdNew.getClass(), familyGroupIdNew.getId());
                request.setFamilyGroupId(familyGroupIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                request.setUserId(userIdNew);
            }
            request = em.merge(request);
            if (familyGroupIdOld != null && !familyGroupIdOld.equals(familyGroupIdNew)) {
                familyGroupIdOld.getRequestCollection().remove(request);
                familyGroupIdOld = em.merge(familyGroupIdOld);
            }
            if (familyGroupIdNew != null && !familyGroupIdNew.equals(familyGroupIdOld)) {
                familyGroupIdNew.getRequestCollection().add(request);
                familyGroupIdNew = em.merge(familyGroupIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getRequestCollection().remove(request);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getRequestCollection().add(request);
                userIdNew = em.merge(userIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = request.getId();
                if (findRequest(id) == null) {
                    throw new NonexistentEntityException("The request with id " + id + " no longer exists.");
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
            Request request;
            try {
                request = em.getReference(Request.class, id);
                request.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The request with id " + id + " no longer exists.", enfe);
            }
            FamilyGroup familyGroupId = request.getFamilyGroupId();
            if (familyGroupId != null) {
                familyGroupId.getRequestCollection().remove(request);
                familyGroupId = em.merge(familyGroupId);
            }
            UserInfor userId = request.getUserId();
            if (userId != null) {
                userId.getRequestCollection().remove(request);
                userId = em.merge(userId);
            }
            em.remove(request);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Request> findRequestEntities() {
        return findRequestEntities(true, -1, -1);
    }

    public List<Request> findRequestEntities(int maxResults, int firstResult) {
        return findRequestEntities(false, maxResults, firstResult);
    }

    private List<Request> findRequestEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Request.class));
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

    public Request findRequest(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Request.class, id);
        } finally {
            em.close();
        }
    }

    public int getRequestCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Request> rt = cq.from(Request.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public List<Request> findByStatus(String status , String id) {
        return (List<Request>) getEntityManager().createNamedQuery("Request.findByStatus").setParameter("status", status).setParameter("id", id).getResultList();
    }
}
