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
import mpmr.dto.Clinic;
import mpmr.dto.UserInfor;
import mpmr.dto.Rating;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.dto.Examination;
import mpmr.dto.TestRequest;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class ExaminationJpaController implements Serializable {

    public ExaminationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Examination examination) throws PreexistingEntityException, Exception {
        if (examination.getRatingCollection() == null) {
            examination.setRatingCollection(new ArrayList<Rating>());
        }
        if (examination.getTestRequestCollection() == null) {
            examination.setTestRequestCollection(new ArrayList<TestRequest>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clinic clinicId = examination.getClinicId();
            if (clinicId != null) {
                clinicId = em.getReference(clinicId.getClass(), clinicId.getId());
                examination.setClinicId(clinicId);
            }
            UserInfor userId = examination.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                examination.setUserId(userId);
            }
            Collection<Rating> attachedRatingCollection = new ArrayList<Rating>();
            for (Rating ratingCollectionRatingToAttach : examination.getRatingCollection()) {
                ratingCollectionRatingToAttach = em.getReference(ratingCollectionRatingToAttach.getClass(), ratingCollectionRatingToAttach.getId());
                attachedRatingCollection.add(ratingCollectionRatingToAttach);
            }
            examination.setRatingCollection(attachedRatingCollection);
            Collection<TestRequest> attachedTestRequestCollection = new ArrayList<TestRequest>();
            for (TestRequest testRequestCollectionTestRequestToAttach : examination.getTestRequestCollection()) {
                testRequestCollectionTestRequestToAttach = em.getReference(testRequestCollectionTestRequestToAttach.getClass(), testRequestCollectionTestRequestToAttach.getId());
                attachedTestRequestCollection.add(testRequestCollectionTestRequestToAttach);
            }
            examination.setTestRequestCollection(attachedTestRequestCollection);
            em.persist(examination);
            if (clinicId != null) {
                clinicId.getExaminationCollection().add(examination);
                clinicId = em.merge(clinicId);
            }
            if (userId != null) {
                userId.getExaminationCollection().add(examination);
                userId = em.merge(userId);
            }
            for (Rating ratingCollectionRating : examination.getRatingCollection()) {
                Examination oldExaminationIdOfRatingCollectionRating = ratingCollectionRating.getExaminationId();
                ratingCollectionRating.setExaminationId(examination);
                ratingCollectionRating = em.merge(ratingCollectionRating);
                if (oldExaminationIdOfRatingCollectionRating != null) {
                    oldExaminationIdOfRatingCollectionRating.getRatingCollection().remove(ratingCollectionRating);
                    oldExaminationIdOfRatingCollectionRating = em.merge(oldExaminationIdOfRatingCollectionRating);
                }
            }
            for (TestRequest testRequestCollectionTestRequest : examination.getTestRequestCollection()) {
                Examination oldExaminationIdOfTestRequestCollectionTestRequest = testRequestCollectionTestRequest.getExaminationId();
                testRequestCollectionTestRequest.setExaminationId(examination);
                testRequestCollectionTestRequest = em.merge(testRequestCollectionTestRequest);
                if (oldExaminationIdOfTestRequestCollectionTestRequest != null) {
                    oldExaminationIdOfTestRequestCollectionTestRequest.getTestRequestCollection().remove(testRequestCollectionTestRequest);
                    oldExaminationIdOfTestRequestCollectionTestRequest = em.merge(oldExaminationIdOfTestRequestCollectionTestRequest);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findExamination(examination.getId()) != null) {
                throw new PreexistingEntityException("Examination " + examination + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Examination examination) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Examination persistentExamination = em.find(Examination.class, examination.getId());
            Clinic clinicIdOld = persistentExamination.getClinicId();
            Clinic clinicIdNew = examination.getClinicId();
            UserInfor userIdOld = persistentExamination.getUserId();
            UserInfor userIdNew = examination.getUserId();
//            Collection<Rating> ratingCollectionOld = persistentExamination.getRatingCollection();
//            Collection<Rating> ratingCollectionNew = examination.getRatingCollection();
//            Collection<TestRequest> testRequestCollectionOld = persistentExamination.getTestRequestCollection();
//            Collection<TestRequest> testRequestCollectionNew = examination.getTestRequestCollection();
//            List<String> illegalOrphanMessages = null;
//            for (Rating ratingCollectionOldRating : ratingCollectionOld) {
//                if (!ratingCollectionNew.contains(ratingCollectionOldRating)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain Rating " + ratingCollectionOldRating + " since its examinationId field is not nullable.");
//                }
//            }
//            for (TestRequest testRequestCollectionOldTestRequest : testRequestCollectionOld) {
//                if (!testRequestCollectionNew.contains(testRequestCollectionOldTestRequest)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain TestRequest " + testRequestCollectionOldTestRequest + " since its examinationId field is not nullable.");
//                }
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
            if (clinicIdNew != null) {
                clinicIdNew = em.getReference(clinicIdNew.getClass(), clinicIdNew.getId());
                examination.setClinicId(clinicIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                examination.setUserId(userIdNew);
            }
//            Collection<Rating> attachedRatingCollectionNew = new ArrayList<Rating>();
//            for (Rating ratingCollectionNewRatingToAttach : ratingCollectionNew) {
//                ratingCollectionNewRatingToAttach = em.getReference(ratingCollectionNewRatingToAttach.getClass(), ratingCollectionNewRatingToAttach.getId());
//                attachedRatingCollectionNew.add(ratingCollectionNewRatingToAttach);
//            }
//            ratingCollectionNew = attachedRatingCollectionNew;
//            examination.setRatingCollection(ratingCollectionNew);
//            Collection<TestRequest> attachedTestRequestCollectionNew = new ArrayList<TestRequest>();
//            for (TestRequest testRequestCollectionNewTestRequestToAttach : testRequestCollectionNew) {
//                testRequestCollectionNewTestRequestToAttach = em.getReference(testRequestCollectionNewTestRequestToAttach.getClass(), testRequestCollectionNewTestRequestToAttach.getId());
//                attachedTestRequestCollectionNew.add(testRequestCollectionNewTestRequestToAttach);
//            }
//            testRequestCollectionNew = attachedTestRequestCollectionNew;
//            examination.setTestRequestCollection(testRequestCollectionNew);
            examination = em.merge(examination);
            if (clinicIdOld != null && !clinicIdOld.equals(clinicIdNew)) {
                clinicIdOld.getExaminationCollection().remove(examination);
                clinicIdOld = em.merge(clinicIdOld);
            }
            if (clinicIdNew != null && !clinicIdNew.equals(clinicIdOld)) {
                clinicIdNew.getExaminationCollection().add(examination);
                clinicIdNew = em.merge(clinicIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getExaminationCollection().remove(examination);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getExaminationCollection().add(examination);
                userIdNew = em.merge(userIdNew);
            }
//            for (Rating ratingCollectionNewRating : ratingCollectionNew) {
//                if (!ratingCollectionOld.contains(ratingCollectionNewRating)) {
//                    Examination oldExaminationIdOfRatingCollectionNewRating = ratingCollectionNewRating.getExaminationId();
//                    ratingCollectionNewRating.setExaminationId(examination);
//                    ratingCollectionNewRating = em.merge(ratingCollectionNewRating);
//                    if (oldExaminationIdOfRatingCollectionNewRating != null && !oldExaminationIdOfRatingCollectionNewRating.equals(examination)) {
//                        oldExaminationIdOfRatingCollectionNewRating.getRatingCollection().remove(ratingCollectionNewRating);
//                        oldExaminationIdOfRatingCollectionNewRating = em.merge(oldExaminationIdOfRatingCollectionNewRating);
//                    }
//                }
//            }
//            for (TestRequest testRequestCollectionNewTestRequest : testRequestCollectionNew) {
//                if (!testRequestCollectionOld.contains(testRequestCollectionNewTestRequest)) {
//                    Examination oldExaminationIdOfTestRequestCollectionNewTestRequest = testRequestCollectionNewTestRequest.getExaminationId();
//                    testRequestCollectionNewTestRequest.setExaminationId(examination);
//                    testRequestCollectionNewTestRequest = em.merge(testRequestCollectionNewTestRequest);
//                    if (oldExaminationIdOfTestRequestCollectionNewTestRequest != null && !oldExaminationIdOfTestRequestCollectionNewTestRequest.equals(examination)) {
//                        oldExaminationIdOfTestRequestCollectionNewTestRequest.getTestRequestCollection().remove(testRequestCollectionNewTestRequest);
//                        oldExaminationIdOfTestRequestCollectionNewTestRequest = em.merge(oldExaminationIdOfTestRequestCollectionNewTestRequest);
//                    }
//                }
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = examination.getId();
                if (findExamination(id) == null) {
                    throw new NonexistentEntityException("The examination with id " + id + " no longer exists.");
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
            Examination examination;
            try {
                examination = em.getReference(Examination.class, id);
                examination.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examination with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Rating> ratingCollectionOrphanCheck = examination.getRatingCollection();
            for (Rating ratingCollectionOrphanCheckRating : ratingCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Examination (" + examination + ") cannot be destroyed since the Rating " + ratingCollectionOrphanCheckRating + " in its ratingCollection field has a non-nullable examinationId field.");
            }
            Collection<TestRequest> testRequestCollectionOrphanCheck = examination.getTestRequestCollection();
            for (TestRequest testRequestCollectionOrphanCheckTestRequest : testRequestCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Examination (" + examination + ") cannot be destroyed since the TestRequest " + testRequestCollectionOrphanCheckTestRequest + " in its testRequestCollection field has a non-nullable examinationId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Clinic clinicId = examination.getClinicId();
            if (clinicId != null) {
                clinicId.getExaminationCollection().remove(examination);
                clinicId = em.merge(clinicId);
            }
            UserInfor userId = examination.getUserId();
            if (userId != null) {
                userId.getExaminationCollection().remove(examination);
                userId = em.merge(userId);
            }
            em.remove(examination);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Examination> findExaminationEntities() {
        return findExaminationEntities(true, -1, -1);
    }

    public List<Examination> findExaminationEntities(int maxResults, int firstResult) {
        return findExaminationEntities(false, maxResults, firstResult);
    }

    private List<Examination> findExaminationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Examination.class));
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

    public Examination findExamination(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Examination.class, id);
        } finally {
            em.close();
        }
    }

    public int getExaminationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Examination> rt = cq.from(Examination.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Examination> findByUserInforId(String id) {
        return (List<Examination>) getEntityManager().createNamedQuery("Examination.findByUserInforId").setParameter("userid", id).getResultList();
    }

    public List<Examination> findByUserInforIdDone(String id) {
        return (List<Examination>) getEntityManager().createNamedQuery("Examination.findByUserInforIdDone").setParameter("userid", id).getResultList();
    }
 public List<Examination> findByDoctorIdDone(String id) {
        return (List<Examination>) getEntityManager().createNamedQuery("Examination.findByDoctorIdDone").setParameter("doctorId", id).getResultList();
    }
    public List<Examination> findByClinicId(String id) {
        return (List<Examination>) getEntityManager().createNamedQuery("Examination.findByClinicId").setParameter("clinicid", id).getResultList();
    }

    public List<Examination> findByTime(Date startDate, Date endDate, String id) {
        return (List<Examination>) getEntityManager().createNamedQuery("Examination.findByTime").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("userId", id).getResultList();
    }
    public Long findByTimeClinicId(Date startDate, Date endDate, String id) {
        return (Long) getEntityManager().createNamedQuery("Examination.findByTimeClinicId").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("clinicid", id).getSingleResult();
    }

    public List<Examination> findByDoctorId(String doctorId) {
        return (List<Examination>) getEntityManager().createNamedQuery("Examination.findByDoctorId").setParameter("doctorId", doctorId).getResultList();
    }
}
