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
import mpmr.dto.DiseaseHealthRecord;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.dto.Disease;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class DiseaseJpaController implements Serializable {

    public DiseaseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Disease disease) throws PreexistingEntityException, Exception {
        if (disease.getDiseaseHealthRecordCollection() == null) {
            disease.setDiseaseHealthRecordCollection(new ArrayList<DiseaseHealthRecord>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DiseaseHealthRecord> attachedDiseaseHealthRecordCollection = new ArrayList<DiseaseHealthRecord>();
            for (DiseaseHealthRecord diseaseHealthRecordCollectionDiseaseHealthRecordToAttach : disease.getDiseaseHealthRecordCollection()) {
                diseaseHealthRecordCollectionDiseaseHealthRecordToAttach = em.getReference(diseaseHealthRecordCollectionDiseaseHealthRecordToAttach.getClass(), diseaseHealthRecordCollectionDiseaseHealthRecordToAttach.getId());
                attachedDiseaseHealthRecordCollection.add(diseaseHealthRecordCollectionDiseaseHealthRecordToAttach);
            }
            disease.setDiseaseHealthRecordCollection(attachedDiseaseHealthRecordCollection);
            em.persist(disease);
            for (DiseaseHealthRecord diseaseHealthRecordCollectionDiseaseHealthRecord : disease.getDiseaseHealthRecordCollection()) {
                Disease oldDiseaseIdOfDiseaseHealthRecordCollectionDiseaseHealthRecord = diseaseHealthRecordCollectionDiseaseHealthRecord.getDiseaseId();
                diseaseHealthRecordCollectionDiseaseHealthRecord.setDiseaseId(disease);
                diseaseHealthRecordCollectionDiseaseHealthRecord = em.merge(diseaseHealthRecordCollectionDiseaseHealthRecord);
                if (oldDiseaseIdOfDiseaseHealthRecordCollectionDiseaseHealthRecord != null) {
                    oldDiseaseIdOfDiseaseHealthRecordCollectionDiseaseHealthRecord.getDiseaseHealthRecordCollection().remove(diseaseHealthRecordCollectionDiseaseHealthRecord);
                    oldDiseaseIdOfDiseaseHealthRecordCollectionDiseaseHealthRecord = em.merge(oldDiseaseIdOfDiseaseHealthRecordCollectionDiseaseHealthRecord);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDisease(disease.getId()) != null) {
                throw new PreexistingEntityException("Disease " + disease + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Disease disease) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Disease persistentDisease = em.find(Disease.class, disease.getId());
            Collection<DiseaseHealthRecord> diseaseHealthRecordCollectionOld = persistentDisease.getDiseaseHealthRecordCollection();
            Collection<DiseaseHealthRecord> diseaseHealthRecordCollectionNew = disease.getDiseaseHealthRecordCollection();
            List<String> illegalOrphanMessages = null;
            for (DiseaseHealthRecord diseaseHealthRecordCollectionOldDiseaseHealthRecord : diseaseHealthRecordCollectionOld) {
                if (!diseaseHealthRecordCollectionNew.contains(diseaseHealthRecordCollectionOldDiseaseHealthRecord)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DiseaseHealthRecord " + diseaseHealthRecordCollectionOldDiseaseHealthRecord + " since its diseaseId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DiseaseHealthRecord> attachedDiseaseHealthRecordCollectionNew = new ArrayList<DiseaseHealthRecord>();
            for (DiseaseHealthRecord diseaseHealthRecordCollectionNewDiseaseHealthRecordToAttach : diseaseHealthRecordCollectionNew) {
                diseaseHealthRecordCollectionNewDiseaseHealthRecordToAttach = em.getReference(diseaseHealthRecordCollectionNewDiseaseHealthRecordToAttach.getClass(), diseaseHealthRecordCollectionNewDiseaseHealthRecordToAttach.getId());
                attachedDiseaseHealthRecordCollectionNew.add(diseaseHealthRecordCollectionNewDiseaseHealthRecordToAttach);
            }
            diseaseHealthRecordCollectionNew = attachedDiseaseHealthRecordCollectionNew;
            disease.setDiseaseHealthRecordCollection(diseaseHealthRecordCollectionNew);
            disease = em.merge(disease);
            for (DiseaseHealthRecord diseaseHealthRecordCollectionNewDiseaseHealthRecord : diseaseHealthRecordCollectionNew) {
                if (!diseaseHealthRecordCollectionOld.contains(diseaseHealthRecordCollectionNewDiseaseHealthRecord)) {
                    Disease oldDiseaseIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord = diseaseHealthRecordCollectionNewDiseaseHealthRecord.getDiseaseId();
                    diseaseHealthRecordCollectionNewDiseaseHealthRecord.setDiseaseId(disease);
                    diseaseHealthRecordCollectionNewDiseaseHealthRecord = em.merge(diseaseHealthRecordCollectionNewDiseaseHealthRecord);
                    if (oldDiseaseIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord != null && !oldDiseaseIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord.equals(disease)) {
                        oldDiseaseIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord.getDiseaseHealthRecordCollection().remove(diseaseHealthRecordCollectionNewDiseaseHealthRecord);
                        oldDiseaseIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord = em.merge(oldDiseaseIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = disease.getId();
                if (findDisease(id) == null) {
                    throw new NonexistentEntityException("The disease with id " + id + " no longer exists.");
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
            Disease disease;
            try {
                disease = em.getReference(Disease.class, id);
                disease.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The disease with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DiseaseHealthRecord> diseaseHealthRecordCollectionOrphanCheck = disease.getDiseaseHealthRecordCollection();
            for (DiseaseHealthRecord diseaseHealthRecordCollectionOrphanCheckDiseaseHealthRecord : diseaseHealthRecordCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Disease (" + disease + ") cannot be destroyed since the DiseaseHealthRecord " + diseaseHealthRecordCollectionOrphanCheckDiseaseHealthRecord + " in its diseaseHealthRecordCollection field has a non-nullable diseaseId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(disease);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Disease> findDiseaseEntities() {
        return findDiseaseEntities(true, -1, -1);
    }

    public List<Disease> findDiseaseEntities(int maxResults, int firstResult) {
        return findDiseaseEntities(false, maxResults, firstResult);
    }

    private List<Disease> findDiseaseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Disease.class));
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

    public Disease findDisease(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Disease.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiseaseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Disease> rt = cq.from(Disease.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
