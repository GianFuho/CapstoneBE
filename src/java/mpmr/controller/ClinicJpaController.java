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
import mpmr.dto.Clinic;
import mpmr.dto.Examination;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class ClinicJpaController implements Serializable {

    public ClinicJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clinic clinic) throws PreexistingEntityException, Exception {
        if (clinic.getUserInforCollection() == null) {
            clinic.setUserInforCollection(new ArrayList<UserInfor>());
        }
        if (clinic.getExaminationCollection() == null) {
            clinic.setExaminationCollection(new ArrayList<Examination>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<UserInfor> attachedUserInforCollection = new ArrayList<UserInfor>();
            for (UserInfor userInforCollectionUserInforToAttach : clinic.getUserInforCollection()) {
                userInforCollectionUserInforToAttach = em.getReference(userInforCollectionUserInforToAttach.getClass(), userInforCollectionUserInforToAttach.getId());
                attachedUserInforCollection.add(userInforCollectionUserInforToAttach);
            }
            clinic.setUserInforCollection(attachedUserInforCollection);
            Collection<Examination> attachedExaminationCollection = new ArrayList<Examination>();
            for (Examination examinationCollectionExaminationToAttach : clinic.getExaminationCollection()) {
                examinationCollectionExaminationToAttach = em.getReference(examinationCollectionExaminationToAttach.getClass(), examinationCollectionExaminationToAttach.getId());
                attachedExaminationCollection.add(examinationCollectionExaminationToAttach);
            }
            clinic.setExaminationCollection(attachedExaminationCollection);
            em.persist(clinic);
            for (UserInfor userInforCollectionUserInfor : clinic.getUserInforCollection()) {
                Clinic oldClinicIdOfUserInforCollectionUserInfor = userInforCollectionUserInfor.getClinicId();
                userInforCollectionUserInfor.setClinicId(clinic);
                userInforCollectionUserInfor = em.merge(userInforCollectionUserInfor);
                if (oldClinicIdOfUserInforCollectionUserInfor != null) {
                    oldClinicIdOfUserInforCollectionUserInfor.getUserInforCollection().remove(userInforCollectionUserInfor);
                    oldClinicIdOfUserInforCollectionUserInfor = em.merge(oldClinicIdOfUserInforCollectionUserInfor);
                }
            }
            for (Examination examinationCollectionExamination : clinic.getExaminationCollection()) {
                Clinic oldClinicIdOfExaminationCollectionExamination = examinationCollectionExamination.getClinicId();
                examinationCollectionExamination.setClinicId(clinic);
                examinationCollectionExamination = em.merge(examinationCollectionExamination);
                if (oldClinicIdOfExaminationCollectionExamination != null) {
                    oldClinicIdOfExaminationCollectionExamination.getExaminationCollection().remove(examinationCollectionExamination);
                    oldClinicIdOfExaminationCollectionExamination = em.merge(oldClinicIdOfExaminationCollectionExamination);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClinic(clinic.getId()) != null) {
                throw new PreexistingEntityException("Clinic " + clinic + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clinic clinic) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clinic persistentClinic = em.find(Clinic.class, clinic.getId());
//            Collection<UserInfor> userInforCollectionOld = persistentClinic.getUserInforCollection();
//            Collection<UserInfor> userInforCollectionNew = clinic.getUserInforCollection();
//            Collection<Examination> examinationCollectionOld = persistentClinic.getExaminationCollection();
//            Collection<Examination> examinationCollectionNew = clinic.getExaminationCollection();
//            List<String> illegalOrphanMessages = null;
//            for (UserInfor userInforCollectionOldUserInfor : userInforCollectionOld) {
//                if (!userInforCollectionNew.contains(userInforCollectionOldUserInfor)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain UserInfor " + userInforCollectionOldUserInfor + " since its clinicId field is not nullable.");
//                }
//            }
//            for (Examination examinationCollectionOldExamination : examinationCollectionOld) {
//                if (!examinationCollectionNew.contains(examinationCollectionOldExamination)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain Examination " + examinationCollectionOldExamination + " since its clinicId field is not nullable.");
//                }
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
//            Collection<UserInfor> attachedUserInforCollectionNew = new ArrayList<UserInfor>();
//            for (UserInfor userInforCollectionNewUserInforToAttach : userInforCollectionNew) {
//                userInforCollectionNewUserInforToAttach = em.getReference(userInforCollectionNewUserInforToAttach.getClass(), userInforCollectionNewUserInforToAttach.getId());
//                attachedUserInforCollectionNew.add(userInforCollectionNewUserInforToAttach);
//            }
//            userInforCollectionNew = attachedUserInforCollectionNew;
//            clinic.setUserInforCollection(userInforCollectionNew);
//            Collection<Examination> attachedExaminationCollectionNew = new ArrayList<Examination>();
//            for (Examination examinationCollectionNewExaminationToAttach : examinationCollectionNew) {
//                examinationCollectionNewExaminationToAttach = em.getReference(examinationCollectionNewExaminationToAttach.getClass(), examinationCollectionNewExaminationToAttach.getId());
//                attachedExaminationCollectionNew.add(examinationCollectionNewExaminationToAttach);
//            }
//            examinationCollectionNew = attachedExaminationCollectionNew;
//            clinic.setExaminationCollection(examinationCollectionNew);
            clinic = em.merge(clinic);
//            for (UserInfor userInforCollectionNewUserInfor : userInforCollectionNew) {
//                if (!userInforCollectionOld.contains(userInforCollectionNewUserInfor)) {
//                    Clinic oldClinicIdOfUserInforCollectionNewUserInfor = userInforCollectionNewUserInfor.getClinicId();
//                    userInforCollectionNewUserInfor.setClinicId(clinic);
//                    userInforCollectionNewUserInfor = em.merge(userInforCollectionNewUserInfor);
//                    if (oldClinicIdOfUserInforCollectionNewUserInfor != null && !oldClinicIdOfUserInforCollectionNewUserInfor.equals(clinic)) {
//                        oldClinicIdOfUserInforCollectionNewUserInfor.getUserInforCollection().remove(userInforCollectionNewUserInfor);
//                        oldClinicIdOfUserInforCollectionNewUserInfor = em.merge(oldClinicIdOfUserInforCollectionNewUserInfor);
//                    }
//                }
//            }
//            for (Examination examinationCollectionNewExamination : examinationCollectionNew) {
//                if (!examinationCollectionOld.contains(examinationCollectionNewExamination)) {
//                    Clinic oldClinicIdOfExaminationCollectionNewExamination = examinationCollectionNewExamination.getClinicId();
//                    examinationCollectionNewExamination.setClinicId(clinic);
//                    examinationCollectionNewExamination = em.merge(examinationCollectionNewExamination);
//                    if (oldClinicIdOfExaminationCollectionNewExamination != null && !oldClinicIdOfExaminationCollectionNewExamination.equals(clinic)) {
//                        oldClinicIdOfExaminationCollectionNewExamination.getExaminationCollection().remove(examinationCollectionNewExamination);
//                        oldClinicIdOfExaminationCollectionNewExamination = em.merge(oldClinicIdOfExaminationCollectionNewExamination);
//                    }
//                }
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = clinic.getId();
                if (findClinic(id) == null) {
                    throw new NonexistentEntityException("The clinic with id " + id + " no longer exists.");
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
            Clinic clinic;
            try {
                clinic = em.getReference(Clinic.class, id);
                clinic.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clinic with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UserInfor> userInforCollectionOrphanCheck = clinic.getUserInforCollection();
            for (UserInfor userInforCollectionOrphanCheckUserInfor : userInforCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clinic (" + clinic + ") cannot be destroyed since the UserInfor " + userInforCollectionOrphanCheckUserInfor + " in its userInforCollection field has a non-nullable clinicId field.");
            }
            Collection<Examination> examinationCollectionOrphanCheck = clinic.getExaminationCollection();
            for (Examination examinationCollectionOrphanCheckExamination : examinationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clinic (" + clinic + ") cannot be destroyed since the Examination " + examinationCollectionOrphanCheckExamination + " in its examinationCollection field has a non-nullable clinicId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(clinic);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clinic> findClinicEntities() {
        return findClinicEntities(true, -1, -1);
    }

    public List<Clinic> findClinicEntities(int maxResults, int firstResult) {
        return findClinicEntities(false, maxResults, firstResult);
    }

    private List<Clinic> findClinicEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clinic.class));
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

    public Clinic findClinic(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clinic.class, id);
        } finally {
            em.close();
        }
    }

    public int getClinicCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clinic> rt = cq.from(Clinic.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Long count() {
        return (Long) getEntityManager().createNamedQuery("Clinic.count").getSingleResult();
    }
    public Long countInDistrict(String district) {
        return (Long) getEntityManager().createNamedQuery("Clinic.countInDistrict").setParameter("district", district).getSingleResult();
    }

    public List<Clinic> findAll() {
        return (List<Clinic>) getEntityManager().createNamedQuery("Clinic.findAll").getResultList();
    }
    public List<Clinic> findByLikeName(String name) {
        return (List<Clinic>) getEntityManager().createNamedQuery("Clinic.findByLikeName").setParameter("name", name).getResultList();
    }
    public List<Clinic> findByDistrict(String district) {
        return (List<Clinic>) getEntityManager().createNamedQuery("Clinic.findByDistrict").setParameter("district", district).getResultList();
    }
}
