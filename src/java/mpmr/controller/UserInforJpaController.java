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
import mpmr.dto.RoleUser;
import mpmr.dto.UserFamilyGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.dto.Rating;
import mpmr.dto.Request;
import mpmr.dto.Lock;
import mpmr.dto.Examination;
import mpmr.dto.HealthRecord;
import mpmr.dto.UserInfor;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class UserInforJpaController implements Serializable {

    public UserInforJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserInfor userInfor) throws PreexistingEntityException, Exception {
        if (userInfor.getUserFamilyGroupCollection() == null) {
            userInfor.setUserFamilyGroupCollection(new ArrayList<UserFamilyGroup>());
        }
        if (userInfor.getRatingCollection() == null) {
            userInfor.setRatingCollection(new ArrayList<Rating>());
        }
        if (userInfor.getRequestCollection() == null) {
            userInfor.setRequestCollection(new ArrayList<Request>());
        }
        if (userInfor.getLockCollection() == null) {
            userInfor.setLockCollection(new ArrayList<Lock>());
        }
        if (userInfor.getExaminationCollection() == null) {
            userInfor.setExaminationCollection(new ArrayList<Examination>());
        }
        if (userInfor.getHealthRecordCollection() == null) {
            userInfor.setHealthRecordCollection(new ArrayList<HealthRecord>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clinic clinicId = userInfor.getClinicId();
            if (clinicId != null) {
                clinicId = em.getReference(clinicId.getClass(), clinicId.getId());
                userInfor.setClinicId(clinicId);
            }
            RoleUser roleId = userInfor.getRoleId();
            if (roleId != null) {
                roleId = em.getReference(roleId.getClass(), roleId.getId());
                userInfor.setRoleId(roleId);
            }
            Collection<UserFamilyGroup> attachedUserFamilyGroupCollection = new ArrayList<UserFamilyGroup>();
            for (UserFamilyGroup userFamilyGroupCollectionUserFamilyGroupToAttach : userInfor.getUserFamilyGroupCollection()) {
                userFamilyGroupCollectionUserFamilyGroupToAttach = em.getReference(userFamilyGroupCollectionUserFamilyGroupToAttach.getClass(), userFamilyGroupCollectionUserFamilyGroupToAttach.getId());
                attachedUserFamilyGroupCollection.add(userFamilyGroupCollectionUserFamilyGroupToAttach);
            }
            userInfor.setUserFamilyGroupCollection(attachedUserFamilyGroupCollection);
            Collection<Rating> attachedRatingCollection = new ArrayList<Rating>();
            for (Rating ratingCollectionRatingToAttach : userInfor.getRatingCollection()) {
                ratingCollectionRatingToAttach = em.getReference(ratingCollectionRatingToAttach.getClass(), ratingCollectionRatingToAttach.getId());
                attachedRatingCollection.add(ratingCollectionRatingToAttach);
            }
            userInfor.setRatingCollection(attachedRatingCollection);
            Collection<Request> attachedRequestCollection = new ArrayList<Request>();
            for (Request requestCollectionRequestToAttach : userInfor.getRequestCollection()) {
                requestCollectionRequestToAttach = em.getReference(requestCollectionRequestToAttach.getClass(), requestCollectionRequestToAttach.getId());
                attachedRequestCollection.add(requestCollectionRequestToAttach);
            }
            userInfor.setRequestCollection(attachedRequestCollection);
            Collection<Lock> attachedLockCollection = new ArrayList<Lock>();
            for (Lock lockCollectionLockToAttach : userInfor.getLockCollection()) {
                lockCollectionLockToAttach = em.getReference(lockCollectionLockToAttach.getClass(), lockCollectionLockToAttach.getId());
                attachedLockCollection.add(lockCollectionLockToAttach);
            }
            userInfor.setLockCollection(attachedLockCollection);
            Collection<Examination> attachedExaminationCollection = new ArrayList<Examination>();
            for (Examination examinationCollectionExaminationToAttach : userInfor.getExaminationCollection()) {
                examinationCollectionExaminationToAttach = em.getReference(examinationCollectionExaminationToAttach.getClass(), examinationCollectionExaminationToAttach.getId());
                attachedExaminationCollection.add(examinationCollectionExaminationToAttach);
            }
            userInfor.setExaminationCollection(attachedExaminationCollection);
            Collection<HealthRecord> attachedHealthRecordCollection = new ArrayList<HealthRecord>();
            for (HealthRecord healthRecordCollectionHealthRecordToAttach : userInfor.getHealthRecordCollection()) {
                healthRecordCollectionHealthRecordToAttach = em.getReference(healthRecordCollectionHealthRecordToAttach.getClass(), healthRecordCollectionHealthRecordToAttach.getId());
                attachedHealthRecordCollection.add(healthRecordCollectionHealthRecordToAttach);
            }
            userInfor.setHealthRecordCollection(attachedHealthRecordCollection);
            em.persist(userInfor);
            if (clinicId != null) {
                clinicId.getUserInforCollection().add(userInfor);
                clinicId = em.merge(clinicId);
            }
            if (roleId != null) {
                roleId.getUserInforCollection().add(userInfor);
                roleId = em.merge(roleId);
            }
            for (UserFamilyGroup userFamilyGroupCollectionUserFamilyGroup : userInfor.getUserFamilyGroupCollection()) {
                UserInfor oldUserIdOfUserFamilyGroupCollectionUserFamilyGroup = userFamilyGroupCollectionUserFamilyGroup.getUserId();
                userFamilyGroupCollectionUserFamilyGroup.setUserId(userInfor);
                userFamilyGroupCollectionUserFamilyGroup = em.merge(userFamilyGroupCollectionUserFamilyGroup);
                if (oldUserIdOfUserFamilyGroupCollectionUserFamilyGroup != null) {
                    oldUserIdOfUserFamilyGroupCollectionUserFamilyGroup.getUserFamilyGroupCollection().remove(userFamilyGroupCollectionUserFamilyGroup);
                    oldUserIdOfUserFamilyGroupCollectionUserFamilyGroup = em.merge(oldUserIdOfUserFamilyGroupCollectionUserFamilyGroup);
                }
            }
            for (Rating ratingCollectionRating : userInfor.getRatingCollection()) {
                UserInfor oldUserIdOfRatingCollectionRating = ratingCollectionRating.getUserId();
                ratingCollectionRating.setUserId(userInfor);
                ratingCollectionRating = em.merge(ratingCollectionRating);
                if (oldUserIdOfRatingCollectionRating != null) {
                    oldUserIdOfRatingCollectionRating.getRatingCollection().remove(ratingCollectionRating);
                    oldUserIdOfRatingCollectionRating = em.merge(oldUserIdOfRatingCollectionRating);
                }
            }
            for (Request requestCollectionRequest : userInfor.getRequestCollection()) {
                UserInfor oldUserIdOfRequestCollectionRequest = requestCollectionRequest.getUserId();
                requestCollectionRequest.setUserId(userInfor);
                requestCollectionRequest = em.merge(requestCollectionRequest);
                if (oldUserIdOfRequestCollectionRequest != null) {
                    oldUserIdOfRequestCollectionRequest.getRequestCollection().remove(requestCollectionRequest);
                    oldUserIdOfRequestCollectionRequest = em.merge(oldUserIdOfRequestCollectionRequest);
                }
            }
            for (Lock lockCollectionLock : userInfor.getLockCollection()) {
                UserInfor oldUserIdOfLockCollectionLock = lockCollectionLock.getUserId();
                lockCollectionLock.setUserId(userInfor);
                lockCollectionLock = em.merge(lockCollectionLock);
                if (oldUserIdOfLockCollectionLock != null) {
                    oldUserIdOfLockCollectionLock.getLockCollection().remove(lockCollectionLock);
                    oldUserIdOfLockCollectionLock = em.merge(oldUserIdOfLockCollectionLock);
                }
            }
            for (Examination examinationCollectionExamination : userInfor.getExaminationCollection()) {
                UserInfor oldUserIdOfExaminationCollectionExamination = examinationCollectionExamination.getUserId();
                examinationCollectionExamination.setUserId(userInfor);
                examinationCollectionExamination = em.merge(examinationCollectionExamination);
                if (oldUserIdOfExaminationCollectionExamination != null) {
                    oldUserIdOfExaminationCollectionExamination.getExaminationCollection().remove(examinationCollectionExamination);
                    oldUserIdOfExaminationCollectionExamination = em.merge(oldUserIdOfExaminationCollectionExamination);
                }
            }
            for (HealthRecord healthRecordCollectionHealthRecord : userInfor.getHealthRecordCollection()) {
                UserInfor oldUserIdOfHealthRecordCollectionHealthRecord = healthRecordCollectionHealthRecord.getUserId();
                healthRecordCollectionHealthRecord.setUserId(userInfor);
                healthRecordCollectionHealthRecord = em.merge(healthRecordCollectionHealthRecord);
                if (oldUserIdOfHealthRecordCollectionHealthRecord != null) {
                    oldUserIdOfHealthRecordCollectionHealthRecord.getHealthRecordCollection().remove(healthRecordCollectionHealthRecord);
                    oldUserIdOfHealthRecordCollectionHealthRecord = em.merge(oldUserIdOfHealthRecordCollectionHealthRecord);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUserInfor(userInfor.getId()) != null) {
                throw new PreexistingEntityException("UserInfor " + userInfor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserInfor userInfor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserInfor persistentUserInfor = em.find(UserInfor.class, userInfor.getId());
            Clinic clinicIdOld = persistentUserInfor.getClinicId();
            Clinic clinicIdNew = userInfor.getClinicId();
            RoleUser roleIdOld = persistentUserInfor.getRoleId();
            RoleUser roleIdNew = userInfor.getRoleId();
//            Collection<UserFamilyGroup> userFamilyGroupCollectionOld = persistentUserInfor.getUserFamilyGroupCollection();
//            Collection<UserFamilyGroup> userFamilyGroupCollectionNew = userInfor.getUserFamilyGroupCollection();
//            Collection<Rating> ratingCollectionOld = persistentUserInfor.getRatingCollection();
//            Collection<Rating> ratingCollectionNew = userInfor.getRatingCollection();
//            Collection<Request> requestCollectionOld = persistentUserInfor.getRequestCollection();
//            Collection<Request> requestCollectionNew = userInfor.getRequestCollection();
//            Collection<Lock> lockCollectionOld = persistentUserInfor.getLockCollection();
//            Collection<Lock> lockCollectionNew = userInfor.getLockCollection();
//            Collection<Examination> examinationCollectionOld = persistentUserInfor.getExaminationCollection();
//            Collection<Examination> examinationCollectionNew = userInfor.getExaminationCollection();
//            Collection<HealthRecord> healthRecordCollectionOld = persistentUserInfor.getHealthRecordCollection();
//            Collection<HealthRecord> healthRecordCollectionNew = userInfor.getHealthRecordCollection();
//            List<String> illegalOrphanMessages = null;
//            for (UserFamilyGroup userFamilyGroupCollectionOldUserFamilyGroup : userFamilyGroupCollectionOld) {
//                if (!userFamilyGroupCollectionNew.contains(userFamilyGroupCollectionOldUserFamilyGroup)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain UserFamilyGroup " + userFamilyGroupCollectionOldUserFamilyGroup + " since its userId field is not nullable.");
//                }
//            }
//            for (Rating ratingCollectionOldRating : ratingCollectionOld) {
//                if (!ratingCollectionNew.contains(ratingCollectionOldRating)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain Rating " + ratingCollectionOldRating + " since its userId field is not nullable.");
//                }
//            }
//            for (Request requestCollectionOldRequest : requestCollectionOld) {
//                if (!requestCollectionNew.contains(requestCollectionOldRequest)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain Request " + requestCollectionOldRequest + " since its userId field is not nullable.");
//                }
//            }
//            for (Lock lockCollectionOldLock : lockCollectionOld) {
//                if (!lockCollectionNew.contains(lockCollectionOldLock)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain Lock " + lockCollectionOldLock + " since its userId field is not nullable.");
//                }
//            }
//            for (Examination examinationCollectionOldExamination : examinationCollectionOld) {
//                if (!examinationCollectionNew.contains(examinationCollectionOldExamination)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain Examination " + examinationCollectionOldExamination + " since its userId field is not nullable.");
//                }
//            }
//            for (HealthRecord healthRecordCollectionOldHealthRecord : healthRecordCollectionOld) {
//                if (!healthRecordCollectionNew.contains(healthRecordCollectionOldHealthRecord)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain HealthRecord " + healthRecordCollectionOldHealthRecord + " since its userId field is not nullable.");
//                }
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
            if (clinicIdNew != null) {
                clinicIdNew = em.getReference(clinicIdNew.getClass(), clinicIdNew.getId());
                userInfor.setClinicId(clinicIdNew);
            }
            if (roleIdNew != null) {
                roleIdNew = em.getReference(roleIdNew.getClass(), roleIdNew.getId());
                userInfor.setRoleId(roleIdNew);
            }
//            Collection<UserFamilyGroup> attachedUserFamilyGroupCollectionNew = new ArrayList<UserFamilyGroup>();
//            for (UserFamilyGroup userFamilyGroupCollectionNewUserFamilyGroupToAttach : userFamilyGroupCollectionNew) {
//                userFamilyGroupCollectionNewUserFamilyGroupToAttach = em.getReference(userFamilyGroupCollectionNewUserFamilyGroupToAttach.getClass(), userFamilyGroupCollectionNewUserFamilyGroupToAttach.getId());
//                attachedUserFamilyGroupCollectionNew.add(userFamilyGroupCollectionNewUserFamilyGroupToAttach);
//            }
//            userFamilyGroupCollectionNew = attachedUserFamilyGroupCollectionNew;
//            userInfor.setUserFamilyGroupCollection(userFamilyGroupCollectionNew);
//            Collection<Rating> attachedRatingCollectionNew = new ArrayList<Rating>();
//            for (Rating ratingCollectionNewRatingToAttach : ratingCollectionNew) {
//                ratingCollectionNewRatingToAttach = em.getReference(ratingCollectionNewRatingToAttach.getClass(), ratingCollectionNewRatingToAttach.getId());
//                attachedRatingCollectionNew.add(ratingCollectionNewRatingToAttach);
//            }
//            ratingCollectionNew = attachedRatingCollectionNew;
//            userInfor.setRatingCollection(ratingCollectionNew);
//            Collection<Request> attachedRequestCollectionNew = new ArrayList<Request>();
//            for (Request requestCollectionNewRequestToAttach : requestCollectionNew) {
//                requestCollectionNewRequestToAttach = em.getReference(requestCollectionNewRequestToAttach.getClass(), requestCollectionNewRequestToAttach.getId());
//                attachedRequestCollectionNew.add(requestCollectionNewRequestToAttach);
//            }
//            requestCollectionNew = attachedRequestCollectionNew;
//            userInfor.setRequestCollection(requestCollectionNew);
//            Collection<Lock> attachedLockCollectionNew = new ArrayList<Lock>();
//            for (Lock lockCollectionNewLockToAttach : lockCollectionNew) {
//                lockCollectionNewLockToAttach = em.getReference(lockCollectionNewLockToAttach.getClass(), lockCollectionNewLockToAttach.getId());
//                attachedLockCollectionNew.add(lockCollectionNewLockToAttach);
//            }
//            lockCollectionNew = attachedLockCollectionNew;
//            userInfor.setLockCollection(lockCollectionNew);
//            Collection<Examination> attachedExaminationCollectionNew = new ArrayList<Examination>();
//            for (Examination examinationCollectionNewExaminationToAttach : examinationCollectionNew) {
//                examinationCollectionNewExaminationToAttach = em.getReference(examinationCollectionNewExaminationToAttach.getClass(), examinationCollectionNewExaminationToAttach.getId());
//                attachedExaminationCollectionNew.add(examinationCollectionNewExaminationToAttach);
//            }
//            examinationCollectionNew = attachedExaminationCollectionNew;
//            userInfor.setExaminationCollection(examinationCollectionNew);
//            Collection<HealthRecord> attachedHealthRecordCollectionNew = new ArrayList<HealthRecord>();
//            for (HealthRecord healthRecordCollectionNewHealthRecordToAttach : healthRecordCollectionNew) {
//                healthRecordCollectionNewHealthRecordToAttach = em.getReference(healthRecordCollectionNewHealthRecordToAttach.getClass(), healthRecordCollectionNewHealthRecordToAttach.getId());
//                attachedHealthRecordCollectionNew.add(healthRecordCollectionNewHealthRecordToAttach);
//            }
//            healthRecordCollectionNew = attachedHealthRecordCollectionNew;
//            userInfor.setHealthRecordCollection(healthRecordCollectionNew);
            userInfor = em.merge(userInfor);
            if (clinicIdOld != null && !clinicIdOld.equals(clinicIdNew)) {
                clinicIdOld.getUserInforCollection().remove(userInfor);
                clinicIdOld = em.merge(clinicIdOld);
            }
            if (clinicIdNew != null && !clinicIdNew.equals(clinicIdOld)) {
                clinicIdNew.getUserInforCollection().add(userInfor);
                clinicIdNew = em.merge(clinicIdNew);
            }
            if (roleIdOld != null && !roleIdOld.equals(roleIdNew)) {
                roleIdOld.getUserInforCollection().remove(userInfor);
                roleIdOld = em.merge(roleIdOld);
            }
            if (roleIdNew != null && !roleIdNew.equals(roleIdOld)) {
                roleIdNew.getUserInforCollection().add(userInfor);
                roleIdNew = em.merge(roleIdNew);
            }
//            for (UserFamilyGroup userFamilyGroupCollectionNewUserFamilyGroup : userFamilyGroupCollectionNew) {
//                if (!userFamilyGroupCollectionOld.contains(userFamilyGroupCollectionNewUserFamilyGroup)) {
//                    UserInfor oldUserIdOfUserFamilyGroupCollectionNewUserFamilyGroup = userFamilyGroupCollectionNewUserFamilyGroup.getUserId();
//                    userFamilyGroupCollectionNewUserFamilyGroup.setUserId(userInfor);
//                    userFamilyGroupCollectionNewUserFamilyGroup = em.merge(userFamilyGroupCollectionNewUserFamilyGroup);
//                    if (oldUserIdOfUserFamilyGroupCollectionNewUserFamilyGroup != null && !oldUserIdOfUserFamilyGroupCollectionNewUserFamilyGroup.equals(userInfor)) {
//                        oldUserIdOfUserFamilyGroupCollectionNewUserFamilyGroup.getUserFamilyGroupCollection().remove(userFamilyGroupCollectionNewUserFamilyGroup);
//                        oldUserIdOfUserFamilyGroupCollectionNewUserFamilyGroup = em.merge(oldUserIdOfUserFamilyGroupCollectionNewUserFamilyGroup);
//                    }
//                }
//            }
//            for (Rating ratingCollectionNewRating : ratingCollectionNew) {
//                if (!ratingCollectionOld.contains(ratingCollectionNewRating)) {
//                    UserInfor oldUserIdOfRatingCollectionNewRating = ratingCollectionNewRating.getUserId();
//                    ratingCollectionNewRating.setUserId(userInfor);
//                    ratingCollectionNewRating = em.merge(ratingCollectionNewRating);
//                    if (oldUserIdOfRatingCollectionNewRating != null && !oldUserIdOfRatingCollectionNewRating.equals(userInfor)) {
//                        oldUserIdOfRatingCollectionNewRating.getRatingCollection().remove(ratingCollectionNewRating);
//                        oldUserIdOfRatingCollectionNewRating = em.merge(oldUserIdOfRatingCollectionNewRating);
//                    }
//                }
//            }
//            for (Request requestCollectionNewRequest : requestCollectionNew) {
//                if (!requestCollectionOld.contains(requestCollectionNewRequest)) {
//                    UserInfor oldUserIdOfRequestCollectionNewRequest = requestCollectionNewRequest.getUserId();
//                    requestCollectionNewRequest.setUserId(userInfor);
//                    requestCollectionNewRequest = em.merge(requestCollectionNewRequest);
//                    if (oldUserIdOfRequestCollectionNewRequest != null && !oldUserIdOfRequestCollectionNewRequest.equals(userInfor)) {
//                        oldUserIdOfRequestCollectionNewRequest.getRequestCollection().remove(requestCollectionNewRequest);
//                        oldUserIdOfRequestCollectionNewRequest = em.merge(oldUserIdOfRequestCollectionNewRequest);
//                    }
//                }
//            }
//            for (Lock lockCollectionNewLock : lockCollectionNew) {
//                if (!lockCollectionOld.contains(lockCollectionNewLock)) {
//                    UserInfor oldUserIdOfLockCollectionNewLock = lockCollectionNewLock.getUserId();
//                    lockCollectionNewLock.setUserId(userInfor);
//                    lockCollectionNewLock = em.merge(lockCollectionNewLock);
//                    if (oldUserIdOfLockCollectionNewLock != null && !oldUserIdOfLockCollectionNewLock.equals(userInfor)) {
//                        oldUserIdOfLockCollectionNewLock.getLockCollection().remove(lockCollectionNewLock);
//                        oldUserIdOfLockCollectionNewLock = em.merge(oldUserIdOfLockCollectionNewLock);
//                    }
//                }
//            }
//            for (Examination examinationCollectionNewExamination : examinationCollectionNew) {
//                if (!examinationCollectionOld.contains(examinationCollectionNewExamination)) {
//                    UserInfor oldUserIdOfExaminationCollectionNewExamination = examinationCollectionNewExamination.getUserId();
//                    examinationCollectionNewExamination.setUserId(userInfor);
//                    examinationCollectionNewExamination = em.merge(examinationCollectionNewExamination);
//                    if (oldUserIdOfExaminationCollectionNewExamination != null && !oldUserIdOfExaminationCollectionNewExamination.equals(userInfor)) {
//                        oldUserIdOfExaminationCollectionNewExamination.getExaminationCollection().remove(examinationCollectionNewExamination);
//                        oldUserIdOfExaminationCollectionNewExamination = em.merge(oldUserIdOfExaminationCollectionNewExamination);
//                    }
//                }
//            }
//            for (HealthRecord healthRecordCollectionNewHealthRecord : healthRecordCollectionNew) {
//                if (!healthRecordCollectionOld.contains(healthRecordCollectionNewHealthRecord)) {
//                    UserInfor oldUserIdOfHealthRecordCollectionNewHealthRecord = healthRecordCollectionNewHealthRecord.getUserId();
//                    healthRecordCollectionNewHealthRecord.setUserId(userInfor);
//                    healthRecordCollectionNewHealthRecord = em.merge(healthRecordCollectionNewHealthRecord);
//                    if (oldUserIdOfHealthRecordCollectionNewHealthRecord != null && !oldUserIdOfHealthRecordCollectionNewHealthRecord.equals(userInfor)) {
//                        oldUserIdOfHealthRecordCollectionNewHealthRecord.getHealthRecordCollection().remove(healthRecordCollectionNewHealthRecord);
//                        oldUserIdOfHealthRecordCollectionNewHealthRecord = em.merge(oldUserIdOfHealthRecordCollectionNewHealthRecord);
//                    }
//                }
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = userInfor.getId();
                if (findUserInfor(id) == null) {
                    throw new NonexistentEntityException("The userInfor with id " + id + " no longer exists.");
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
            UserInfor userInfor;
            try {
                userInfor = em.getReference(UserInfor.class, id);
                userInfor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userInfor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UserFamilyGroup> userFamilyGroupCollectionOrphanCheck = userInfor.getUserFamilyGroupCollection();
            for (UserFamilyGroup userFamilyGroupCollectionOrphanCheckUserFamilyGroup : userFamilyGroupCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserInfor (" + userInfor + ") cannot be destroyed since the UserFamilyGroup " + userFamilyGroupCollectionOrphanCheckUserFamilyGroup + " in its userFamilyGroupCollection field has a non-nullable userId field.");
            }
            Collection<Rating> ratingCollectionOrphanCheck = userInfor.getRatingCollection();
            for (Rating ratingCollectionOrphanCheckRating : ratingCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserInfor (" + userInfor + ") cannot be destroyed since the Rating " + ratingCollectionOrphanCheckRating + " in its ratingCollection field has a non-nullable userId field.");
            }
            Collection<Request> requestCollectionOrphanCheck = userInfor.getRequestCollection();
            for (Request requestCollectionOrphanCheckRequest : requestCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserInfor (" + userInfor + ") cannot be destroyed since the Request " + requestCollectionOrphanCheckRequest + " in its requestCollection field has a non-nullable userId field.");
            }
            Collection<Lock> lockCollectionOrphanCheck = userInfor.getLockCollection();
            for (Lock lockCollectionOrphanCheckLock : lockCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserInfor (" + userInfor + ") cannot be destroyed since the Lock " + lockCollectionOrphanCheckLock + " in its lockCollection field has a non-nullable userId field.");
            }
            Collection<Examination> examinationCollectionOrphanCheck = userInfor.getExaminationCollection();
            for (Examination examinationCollectionOrphanCheckExamination : examinationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserInfor (" + userInfor + ") cannot be destroyed since the Examination " + examinationCollectionOrphanCheckExamination + " in its examinationCollection field has a non-nullable userId field.");
            }
            Collection<HealthRecord> healthRecordCollectionOrphanCheck = userInfor.getHealthRecordCollection();
            for (HealthRecord healthRecordCollectionOrphanCheckHealthRecord : healthRecordCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserInfor (" + userInfor + ") cannot be destroyed since the HealthRecord " + healthRecordCollectionOrphanCheckHealthRecord + " in its healthRecordCollection field has a non-nullable userId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Clinic clinicId = userInfor.getClinicId();
            if (clinicId != null) {
                clinicId.getUserInforCollection().remove(userInfor);
                clinicId = em.merge(clinicId);
            }
            RoleUser roleId = userInfor.getRoleId();
            if (roleId != null) {
                roleId.getUserInforCollection().remove(userInfor);
                roleId = em.merge(roleId);
            }
            em.remove(userInfor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserInfor> findUserInforEntities() {
        return findUserInforEntities(true, -1, -1);
    }

    public List<UserInfor> findUserInforEntities(int maxResults, int firstResult) {
        return findUserInforEntities(false, maxResults, firstResult);
    }

    private List<UserInfor> findUserInforEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserInfor.class));
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

    public UserInfor findUserInfor(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserInfor.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserInforCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserInfor> rt = cq.from(UserInfor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public UserInfor login(String u, String p) {
        return (UserInfor) getEntityManager().createNamedQuery("UserInfor.login").setParameter("username", u).setParameter("password", p).getSingleResult();
    }

    public UserInfor findByPhone(String phone) {
        return (UserInfor) getEntityManager().createNamedQuery("UserInfor.findByPhone").setParameter("phone", phone).setParameter("roleId", 3).getSingleResult();
    }

    public Long count(int id) {
        return (Long) getEntityManager().createNamedQuery("UserInfor.count").setParameter("roleid", id).getSingleResult();
    }

    public Long countRole(int id, String clinicId) {
        return (Long) getEntityManager().createNamedQuery("UserInfor.countRole").setParameter("roleid", id).setParameter("clinicid", clinicId).getSingleResult();
    }

    public List<UserInfor> findAll() {
        return (List<UserInfor>) getEntityManager().createNamedQuery("UserInfor.findAll").getResultList();
    }

    public List<UserInfor> findByRoleId(int id) {
        return (List<UserInfor>) getEntityManager().createNamedQuery("UserInfor.findByRoleId").setParameter("roleid", id).getResultList();
    }

    public List<UserInfor> findDoctorByClinicId(String id) {
        return (List<UserInfor>) getEntityManager().createNamedQuery("UserInfor.findDoctorByClinicId").setParameter("roleid", 2).setParameter("clinicid", id).getResultList();
    }

    public List<UserInfor> findRecepByClinicId(String id) {
        return (List<UserInfor>) getEntityManager().createNamedQuery("UserInfor.findDoctorByClinicId").setParameter("roleid", 4).setParameter("clinicid", id).getResultList();
    }
}
