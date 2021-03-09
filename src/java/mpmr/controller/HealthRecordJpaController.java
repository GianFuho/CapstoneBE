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
import mpmr.dto.SharedInformation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.dto.DiseaseHealthRecord;
import mpmr.dto.HealthRecord;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class HealthRecordJpaController implements Serializable {

    public HealthRecordJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HealthRecord healthRecord) throws PreexistingEntityException, Exception {
        if (healthRecord.getSharedInformationCollection() == null) {
            healthRecord.setSharedInformationCollection(new ArrayList<SharedInformation>());
        }
        if (healthRecord.getDiseaseHealthRecordCollection() == null) {
            healthRecord.setDiseaseHealthRecordCollection(new ArrayList<DiseaseHealthRecord>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserInfor userId = healthRecord.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                healthRecord.setUserId(userId);
            }
            Collection<SharedInformation> attachedSharedInformationCollection = new ArrayList<SharedInformation>();
            for (SharedInformation sharedInformationCollectionSharedInformationToAttach : healthRecord.getSharedInformationCollection()) {
                sharedInformationCollectionSharedInformationToAttach = em.getReference(sharedInformationCollectionSharedInformationToAttach.getClass(), sharedInformationCollectionSharedInformationToAttach.getId());
                attachedSharedInformationCollection.add(sharedInformationCollectionSharedInformationToAttach);
            }
            healthRecord.setSharedInformationCollection(attachedSharedInformationCollection);
            Collection<DiseaseHealthRecord> attachedDiseaseHealthRecordCollection = new ArrayList<DiseaseHealthRecord>();
            for (DiseaseHealthRecord diseaseHealthRecordCollectionDiseaseHealthRecordToAttach : healthRecord.getDiseaseHealthRecordCollection()) {
                diseaseHealthRecordCollectionDiseaseHealthRecordToAttach = em.getReference(diseaseHealthRecordCollectionDiseaseHealthRecordToAttach.getClass(), diseaseHealthRecordCollectionDiseaseHealthRecordToAttach.getId());
                attachedDiseaseHealthRecordCollection.add(diseaseHealthRecordCollectionDiseaseHealthRecordToAttach);
            }
            healthRecord.setDiseaseHealthRecordCollection(attachedDiseaseHealthRecordCollection);
            em.persist(healthRecord);
            if (userId != null) {
                userId.getHealthRecordCollection().add(healthRecord);
                userId = em.merge(userId);
            }
            for (SharedInformation sharedInformationCollectionSharedInformation : healthRecord.getSharedInformationCollection()) {
                HealthRecord oldHealthRecordIdOfSharedInformationCollectionSharedInformation = sharedInformationCollectionSharedInformation.getHealthRecordId();
                sharedInformationCollectionSharedInformation.setHealthRecordId(healthRecord);
                sharedInformationCollectionSharedInformation = em.merge(sharedInformationCollectionSharedInformation);
                if (oldHealthRecordIdOfSharedInformationCollectionSharedInformation != null) {
                    oldHealthRecordIdOfSharedInformationCollectionSharedInformation.getSharedInformationCollection().remove(sharedInformationCollectionSharedInformation);
                    oldHealthRecordIdOfSharedInformationCollectionSharedInformation = em.merge(oldHealthRecordIdOfSharedInformationCollectionSharedInformation);
                }
            }
            for (DiseaseHealthRecord diseaseHealthRecordCollectionDiseaseHealthRecord : healthRecord.getDiseaseHealthRecordCollection()) {
                HealthRecord oldHealthRecordIdOfDiseaseHealthRecordCollectionDiseaseHealthRecord = diseaseHealthRecordCollectionDiseaseHealthRecord.getHealthRecordId();
                diseaseHealthRecordCollectionDiseaseHealthRecord.setHealthRecordId(healthRecord);
                diseaseHealthRecordCollectionDiseaseHealthRecord = em.merge(diseaseHealthRecordCollectionDiseaseHealthRecord);
                if (oldHealthRecordIdOfDiseaseHealthRecordCollectionDiseaseHealthRecord != null) {
                    oldHealthRecordIdOfDiseaseHealthRecordCollectionDiseaseHealthRecord.getDiseaseHealthRecordCollection().remove(diseaseHealthRecordCollectionDiseaseHealthRecord);
                    oldHealthRecordIdOfDiseaseHealthRecordCollectionDiseaseHealthRecord = em.merge(oldHealthRecordIdOfDiseaseHealthRecordCollectionDiseaseHealthRecord);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHealthRecord(healthRecord.getId()) != null) {
                throw new PreexistingEntityException("HealthRecord " + healthRecord + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HealthRecord healthRecord) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HealthRecord persistentHealthRecord = em.find(HealthRecord.class, healthRecord.getId());
            UserInfor userIdOld = persistentHealthRecord.getUserId();
            UserInfor userIdNew = healthRecord.getUserId();
//            Collection<SharedInformation> sharedInformationCollectionOld = persistentHealthRecord.getSharedInformationCollection();
//            Collection<SharedInformation> sharedInformationCollectionNew = healthRecord.getSharedInformationCollection();
//            Collection<DiseaseHealthRecord> diseaseHealthRecordCollectionOld = persistentHealthRecord.getDiseaseHealthRecordCollection();
//            Collection<DiseaseHealthRecord> diseaseHealthRecordCollectionNew = healthRecord.getDiseaseHealthRecordCollection();
//            List<String> illegalOrphanMessages = null;
//            for (SharedInformation sharedInformationCollectionOldSharedInformation : sharedInformationCollectionOld) {
//                if (!sharedInformationCollectionNew.contains(sharedInformationCollectionOldSharedInformation)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain SharedInformation " + sharedInformationCollectionOldSharedInformation + " since its healthRecordId field is not nullable.");
//                }
//            }
//            for (DiseaseHealthRecord diseaseHealthRecordCollectionOldDiseaseHealthRecord : diseaseHealthRecordCollectionOld) {
//                if (!diseaseHealthRecordCollectionNew.contains(diseaseHealthRecordCollectionOldDiseaseHealthRecord)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain DiseaseHealthRecord " + diseaseHealthRecordCollectionOldDiseaseHealthRecord + " since its healthRecordId field is not nullable.");
//                }
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                healthRecord.setUserId(userIdNew);
            }
//            Collection<SharedInformation> attachedSharedInformationCollectionNew = new ArrayList<SharedInformation>();
//            for (SharedInformation sharedInformationCollectionNewSharedInformationToAttach : sharedInformationCollectionNew) {
//                sharedInformationCollectionNewSharedInformationToAttach = em.getReference(sharedInformationCollectionNewSharedInformationToAttach.getClass(), sharedInformationCollectionNewSharedInformationToAttach.getId());
//                attachedSharedInformationCollectionNew.add(sharedInformationCollectionNewSharedInformationToAttach);
//            }
//            sharedInformationCollectionNew = attachedSharedInformationCollectionNew;
//            healthRecord.setSharedInformationCollection(sharedInformationCollectionNew);
//            Collection<DiseaseHealthRecord> attachedDiseaseHealthRecordCollectionNew = new ArrayList<DiseaseHealthRecord>();
//            for (DiseaseHealthRecord diseaseHealthRecordCollectionNewDiseaseHealthRecordToAttach : diseaseHealthRecordCollectionNew) {
//                diseaseHealthRecordCollectionNewDiseaseHealthRecordToAttach = em.getReference(diseaseHealthRecordCollectionNewDiseaseHealthRecordToAttach.getClass(), diseaseHealthRecordCollectionNewDiseaseHealthRecordToAttach.getId());
//                attachedDiseaseHealthRecordCollectionNew.add(diseaseHealthRecordCollectionNewDiseaseHealthRecordToAttach);
//            }
//            diseaseHealthRecordCollectionNew = attachedDiseaseHealthRecordCollectionNew;
//            healthRecord.setDiseaseHealthRecordCollection(diseaseHealthRecordCollectionNew);
            healthRecord = em.merge(healthRecord);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getHealthRecordCollection().remove(healthRecord);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getHealthRecordCollection().add(healthRecord);
                userIdNew = em.merge(userIdNew);
            }
//            for (SharedInformation sharedInformationCollectionNewSharedInformation : sharedInformationCollectionNew) {
//                if (!sharedInformationCollectionOld.contains(sharedInformationCollectionNewSharedInformation)) {
//                    HealthRecord oldHealthRecordIdOfSharedInformationCollectionNewSharedInformation = sharedInformationCollectionNewSharedInformation.getHealthRecordId();
//                    sharedInformationCollectionNewSharedInformation.setHealthRecordId(healthRecord);
//                    sharedInformationCollectionNewSharedInformation = em.merge(sharedInformationCollectionNewSharedInformation);
//                    if (oldHealthRecordIdOfSharedInformationCollectionNewSharedInformation != null && !oldHealthRecordIdOfSharedInformationCollectionNewSharedInformation.equals(healthRecord)) {
//                        oldHealthRecordIdOfSharedInformationCollectionNewSharedInformation.getSharedInformationCollection().remove(sharedInformationCollectionNewSharedInformation);
//                        oldHealthRecordIdOfSharedInformationCollectionNewSharedInformation = em.merge(oldHealthRecordIdOfSharedInformationCollectionNewSharedInformation);
//                    }
//                }
//            }
//            for (DiseaseHealthRecord diseaseHealthRecordCollectionNewDiseaseHealthRecord : diseaseHealthRecordCollectionNew) {
//                if (!diseaseHealthRecordCollectionOld.contains(diseaseHealthRecordCollectionNewDiseaseHealthRecord)) {
//                    HealthRecord oldHealthRecordIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord = diseaseHealthRecordCollectionNewDiseaseHealthRecord.getHealthRecordId();
//                    diseaseHealthRecordCollectionNewDiseaseHealthRecord.setHealthRecordId(healthRecord);
//                    diseaseHealthRecordCollectionNewDiseaseHealthRecord = em.merge(diseaseHealthRecordCollectionNewDiseaseHealthRecord);
//                    if (oldHealthRecordIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord != null && !oldHealthRecordIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord.equals(healthRecord)) {
//                        oldHealthRecordIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord.getDiseaseHealthRecordCollection().remove(diseaseHealthRecordCollectionNewDiseaseHealthRecord);
//                        oldHealthRecordIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord = em.merge(oldHealthRecordIdOfDiseaseHealthRecordCollectionNewDiseaseHealthRecord);
//                    }
//                }
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = healthRecord.getId();
                if (findHealthRecord(id) == null) {
                    throw new NonexistentEntityException("The healthRecord with id " + id + " no longer exists.");
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
            HealthRecord healthRecord;
            try {
                healthRecord = em.getReference(HealthRecord.class, id);
                healthRecord.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The healthRecord with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<SharedInformation> sharedInformationCollectionOrphanCheck = healthRecord.getSharedInformationCollection();
            for (SharedInformation sharedInformationCollectionOrphanCheckSharedInformation : sharedInformationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This HealthRecord (" + healthRecord + ") cannot be destroyed since the SharedInformation " + sharedInformationCollectionOrphanCheckSharedInformation + " in its sharedInformationCollection field has a non-nullable healthRecordId field.");
            }
            Collection<DiseaseHealthRecord> diseaseHealthRecordCollectionOrphanCheck = healthRecord.getDiseaseHealthRecordCollection();
            for (DiseaseHealthRecord diseaseHealthRecordCollectionOrphanCheckDiseaseHealthRecord : diseaseHealthRecordCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This HealthRecord (" + healthRecord + ") cannot be destroyed since the DiseaseHealthRecord " + diseaseHealthRecordCollectionOrphanCheckDiseaseHealthRecord + " in its diseaseHealthRecordCollection field has a non-nullable healthRecordId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UserInfor userId = healthRecord.getUserId();
            if (userId != null) {
                userId.getHealthRecordCollection().remove(healthRecord);
                userId = em.merge(userId);
            }
            em.remove(healthRecord);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HealthRecord> findHealthRecordEntities() {
        return findHealthRecordEntities(true, -1, -1);
    }

    public List<HealthRecord> findHealthRecordEntities(int maxResults, int firstResult) {
        return findHealthRecordEntities(false, maxResults, firstResult);
    }

    private List<HealthRecord> findHealthRecordEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HealthRecord.class));
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

    public HealthRecord findHealthRecord(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HealthRecord.class, id);
        } finally {
            em.close();
        }
    }

    public int getHealthRecordCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HealthRecord> rt = cq.from(HealthRecord.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public HealthRecord findByUserId(String id) {
        return (HealthRecord) getEntityManager().createNamedQuery("HealthRecord.findByUserId").setParameter("userid", id).getSingleResult();
    }
}
