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
import mpmr.dto.Disease;
import mpmr.dto.DiseaseHealthRecord;
import mpmr.dto.HealthRecord;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class DiseaseHealthRecordJpaController implements Serializable {

    public DiseaseHealthRecordJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DiseaseHealthRecord diseaseHealthRecord) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Disease diseaseId = diseaseHealthRecord.getDiseaseId();
            if (diseaseId != null) {
                diseaseId = em.getReference(diseaseId.getClass(), diseaseId.getId());
                diseaseHealthRecord.setDiseaseId(diseaseId);
            }
            HealthRecord healthRecordId = diseaseHealthRecord.getHealthRecordId();
            if (healthRecordId != null) {
                healthRecordId = em.getReference(healthRecordId.getClass(), healthRecordId.getId());
                diseaseHealthRecord.setHealthRecordId(healthRecordId);
            }
            em.persist(diseaseHealthRecord);
            if (diseaseId != null) {
                diseaseId.getDiseaseHealthRecordCollection().add(diseaseHealthRecord);
                diseaseId = em.merge(diseaseId);
            }
            if (healthRecordId != null) {
                healthRecordId.getDiseaseHealthRecordCollection().add(diseaseHealthRecord);
                healthRecordId = em.merge(healthRecordId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDiseaseHealthRecord(diseaseHealthRecord.getId()) != null) {
                throw new PreexistingEntityException("DiseaseHealthRecord " + diseaseHealthRecord + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DiseaseHealthRecord diseaseHealthRecord) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiseaseHealthRecord persistentDiseaseHealthRecord = em.find(DiseaseHealthRecord.class, diseaseHealthRecord.getId());
            Disease diseaseIdOld = persistentDiseaseHealthRecord.getDiseaseId();
            Disease diseaseIdNew = diseaseHealthRecord.getDiseaseId();
            HealthRecord healthRecordIdOld = persistentDiseaseHealthRecord.getHealthRecordId();
            HealthRecord healthRecordIdNew = diseaseHealthRecord.getHealthRecordId();
            if (diseaseIdNew != null) {
                diseaseIdNew = em.getReference(diseaseIdNew.getClass(), diseaseIdNew.getId());
                diseaseHealthRecord.setDiseaseId(diseaseIdNew);
            }
            if (healthRecordIdNew != null) {
                healthRecordIdNew = em.getReference(healthRecordIdNew.getClass(), healthRecordIdNew.getId());
                diseaseHealthRecord.setHealthRecordId(healthRecordIdNew);
            }
            diseaseHealthRecord = em.merge(diseaseHealthRecord);
            if (diseaseIdOld != null && !diseaseIdOld.equals(diseaseIdNew)) {
                diseaseIdOld.getDiseaseHealthRecordCollection().remove(diseaseHealthRecord);
                diseaseIdOld = em.merge(diseaseIdOld);
            }
            if (diseaseIdNew != null && !diseaseIdNew.equals(diseaseIdOld)) {
                diseaseIdNew.getDiseaseHealthRecordCollection().add(diseaseHealthRecord);
                diseaseIdNew = em.merge(diseaseIdNew);
            }
            if (healthRecordIdOld != null && !healthRecordIdOld.equals(healthRecordIdNew)) {
                healthRecordIdOld.getDiseaseHealthRecordCollection().remove(diseaseHealthRecord);
                healthRecordIdOld = em.merge(healthRecordIdOld);
            }
            if (healthRecordIdNew != null && !healthRecordIdNew.equals(healthRecordIdOld)) {
                healthRecordIdNew.getDiseaseHealthRecordCollection().add(diseaseHealthRecord);
                healthRecordIdNew = em.merge(healthRecordIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = diseaseHealthRecord.getId();
                if (findDiseaseHealthRecord(id) == null) {
                    throw new NonexistentEntityException("The diseaseHealthRecord with id " + id + " no longer exists.");
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
            DiseaseHealthRecord diseaseHealthRecord;
            try {
                diseaseHealthRecord = em.getReference(DiseaseHealthRecord.class, id);
                diseaseHealthRecord.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The diseaseHealthRecord with id " + id + " no longer exists.", enfe);
            }
            Disease diseaseId = diseaseHealthRecord.getDiseaseId();
            if (diseaseId != null) {
                diseaseId.getDiseaseHealthRecordCollection().remove(diseaseHealthRecord);
                diseaseId = em.merge(diseaseId);
            }
            HealthRecord healthRecordId = diseaseHealthRecord.getHealthRecordId();
            if (healthRecordId != null) {
                healthRecordId.getDiseaseHealthRecordCollection().remove(diseaseHealthRecord);
                healthRecordId = em.merge(healthRecordId);
            }
            em.remove(diseaseHealthRecord);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DiseaseHealthRecord> findDiseaseHealthRecordEntities() {
        return findDiseaseHealthRecordEntities(true, -1, -1);
    }

    public List<DiseaseHealthRecord> findDiseaseHealthRecordEntities(int maxResults, int firstResult) {
        return findDiseaseHealthRecordEntities(false, maxResults, firstResult);
    }

    private List<DiseaseHealthRecord> findDiseaseHealthRecordEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiseaseHealthRecord.class));
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

    public DiseaseHealthRecord findDiseaseHealthRecord(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiseaseHealthRecord.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiseaseHealthRecordCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiseaseHealthRecord> rt = cq.from(DiseaseHealthRecord.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
