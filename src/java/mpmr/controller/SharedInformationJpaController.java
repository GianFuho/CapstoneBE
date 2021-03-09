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
import mpmr.dto.HealthRecord;
import mpmr.dto.SharedInformation;
import mpmr.dto.SharedInformationType;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class SharedInformationJpaController implements Serializable {

    public SharedInformationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SharedInformation sharedInformation) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HealthRecord healthRecordId = sharedInformation.getHealthRecordId();
            if (healthRecordId != null) {
                healthRecordId = em.getReference(healthRecordId.getClass(), healthRecordId.getId());
                sharedInformation.setHealthRecordId(healthRecordId);
            }
            SharedInformationType sharedInformationTypeId = sharedInformation.getSharedInformationTypeId();
            if (sharedInformationTypeId != null) {
                sharedInformationTypeId = em.getReference(sharedInformationTypeId.getClass(), sharedInformationTypeId.getId());
                sharedInformation.setSharedInformationTypeId(sharedInformationTypeId);
            }
            em.persist(sharedInformation);
            if (healthRecordId != null) {
                healthRecordId.getSharedInformationCollection().add(sharedInformation);
                healthRecordId = em.merge(healthRecordId);
            }
            if (sharedInformationTypeId != null) {
                sharedInformationTypeId.getSharedInformationCollection().add(sharedInformation);
                sharedInformationTypeId = em.merge(sharedInformationTypeId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSharedInformation(sharedInformation.getId()) != null) {
                throw new PreexistingEntityException("SharedInformation " + sharedInformation + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SharedInformation sharedInformation) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SharedInformation persistentSharedInformation = em.find(SharedInformation.class, sharedInformation.getId());
            HealthRecord healthRecordIdOld = persistentSharedInformation.getHealthRecordId();
            HealthRecord healthRecordIdNew = sharedInformation.getHealthRecordId();
            SharedInformationType sharedInformationTypeIdOld = persistentSharedInformation.getSharedInformationTypeId();
            SharedInformationType sharedInformationTypeIdNew = sharedInformation.getSharedInformationTypeId();
            if (healthRecordIdNew != null) {
                healthRecordIdNew = em.getReference(healthRecordIdNew.getClass(), healthRecordIdNew.getId());
                sharedInformation.setHealthRecordId(healthRecordIdNew);
            }
            if (sharedInformationTypeIdNew != null) {
                sharedInformationTypeIdNew = em.getReference(sharedInformationTypeIdNew.getClass(), sharedInformationTypeIdNew.getId());
                sharedInformation.setSharedInformationTypeId(sharedInformationTypeIdNew);
            }
            sharedInformation = em.merge(sharedInformation);
            if (healthRecordIdOld != null && !healthRecordIdOld.equals(healthRecordIdNew)) {
                healthRecordIdOld.getSharedInformationCollection().remove(sharedInformation);
                healthRecordIdOld = em.merge(healthRecordIdOld);
            }
            if (healthRecordIdNew != null && !healthRecordIdNew.equals(healthRecordIdOld)) {
                healthRecordIdNew.getSharedInformationCollection().add(sharedInformation);
                healthRecordIdNew = em.merge(healthRecordIdNew);
            }
            if (sharedInformationTypeIdOld != null && !sharedInformationTypeIdOld.equals(sharedInformationTypeIdNew)) {
                sharedInformationTypeIdOld.getSharedInformationCollection().remove(sharedInformation);
                sharedInformationTypeIdOld = em.merge(sharedInformationTypeIdOld);
            }
            if (sharedInformationTypeIdNew != null && !sharedInformationTypeIdNew.equals(sharedInformationTypeIdOld)) {
                sharedInformationTypeIdNew.getSharedInformationCollection().add(sharedInformation);
                sharedInformationTypeIdNew = em.merge(sharedInformationTypeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sharedInformation.getId();
                if (findSharedInformation(id) == null) {
                    throw new NonexistentEntityException("The sharedInformation with id " + id + " no longer exists.");
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
            SharedInformation sharedInformation;
            try {
                sharedInformation = em.getReference(SharedInformation.class, id);
                sharedInformation.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sharedInformation with id " + id + " no longer exists.", enfe);
            }
            HealthRecord healthRecordId = sharedInformation.getHealthRecordId();
            if (healthRecordId != null) {
                healthRecordId.getSharedInformationCollection().remove(sharedInformation);
                healthRecordId = em.merge(healthRecordId);
            }
            SharedInformationType sharedInformationTypeId = sharedInformation.getSharedInformationTypeId();
            if (sharedInformationTypeId != null) {
                sharedInformationTypeId.getSharedInformationCollection().remove(sharedInformation);
                sharedInformationTypeId = em.merge(sharedInformationTypeId);
            }
            em.remove(sharedInformation);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SharedInformation> findSharedInformationEntities() {
        return findSharedInformationEntities(true, -1, -1);
    }

    public List<SharedInformation> findSharedInformationEntities(int maxResults, int firstResult) {
        return findSharedInformationEntities(false, maxResults, firstResult);
    }

    private List<SharedInformation> findSharedInformationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SharedInformation.class));
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

    public SharedInformation findSharedInformation(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SharedInformation.class, id);
        } finally {
            em.close();
        }
    }

    public int getSharedInformationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SharedInformation> rt = cq.from(SharedInformation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public SharedInformation findByHealthId(String id) {
        return (SharedInformation) getEntityManager().createNamedQuery("SharedInformation.findByHealthId").setParameter("healthRecordId", id).getSingleResult();
    }

    public List<SharedInformation> findByType(int id) {
        return (List<SharedInformation>) getEntityManager().createNamedQuery("SharedInformation.findByType").setParameter("id", id).getResultList();
    }

    public List<SharedInformation> findBySharedInformationContext(String sharedInformationContext) {
        return (List<SharedInformation>) getEntityManager().createNamedQuery("SharedInformation.findBySharedInformationContext").setParameter("sharedInformationContext", sharedInformationContext).getResultList();
    }

    public List<SharedInformation> findByHealthFGroupId(String id, String fGroupId) {
        return (List<SharedInformation>) getEntityManager().createNamedQuery("SharedInformation.findByHealthFGroupId").setParameter("id", id).setParameter("fGroupId", fGroupId).getResultList();
    }

    public List<SharedInformation> findBySharedInformationType(int id) {
        return (List<SharedInformation>) getEntityManager().createNamedQuery("SharedInformation.findBySharedInformationType").setParameter("id", id).getResultList();
    }

    public List<SharedInformation> findByContextFGroupId(String sharedInformationContext, String fGroupId) {
        return (List<SharedInformation>) getEntityManager().createNamedQuery("SharedInformation.findByContextFGroupId").setParameter("sharedInformationContext", sharedInformationContext).setParameter("fGroupId", fGroupId).getResultList();
    }

}
