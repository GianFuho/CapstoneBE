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
import mpmr.dto.SharedInformation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.dto.SharedInformationType;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class SharedInformationTypeJpaController implements Serializable {

    public SharedInformationTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SharedInformationType sharedInformationType) throws PreexistingEntityException, Exception {
        if (sharedInformationType.getSharedInformationCollection() == null) {
            sharedInformationType.setSharedInformationCollection(new ArrayList<SharedInformation>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SharedInformation> attachedSharedInformationCollection = new ArrayList<SharedInformation>();
            for (SharedInformation sharedInformationCollectionSharedInformationToAttach : sharedInformationType.getSharedInformationCollection()) {
                sharedInformationCollectionSharedInformationToAttach = em.getReference(sharedInformationCollectionSharedInformationToAttach.getClass(), sharedInformationCollectionSharedInformationToAttach.getId());
                attachedSharedInformationCollection.add(sharedInformationCollectionSharedInformationToAttach);
            }
            sharedInformationType.setSharedInformationCollection(attachedSharedInformationCollection);
            em.persist(sharedInformationType);
            for (SharedInformation sharedInformationCollectionSharedInformation : sharedInformationType.getSharedInformationCollection()) {
                SharedInformationType oldSharedInformationTypeIdOfSharedInformationCollectionSharedInformation = sharedInformationCollectionSharedInformation.getSharedInformationTypeId();
                sharedInformationCollectionSharedInformation.setSharedInformationTypeId(sharedInformationType);
                sharedInformationCollectionSharedInformation = em.merge(sharedInformationCollectionSharedInformation);
                if (oldSharedInformationTypeIdOfSharedInformationCollectionSharedInformation != null) {
                    oldSharedInformationTypeIdOfSharedInformationCollectionSharedInformation.getSharedInformationCollection().remove(sharedInformationCollectionSharedInformation);
                    oldSharedInformationTypeIdOfSharedInformationCollectionSharedInformation = em.merge(oldSharedInformationTypeIdOfSharedInformationCollectionSharedInformation);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSharedInformationType(sharedInformationType.getId()) != null) {
                throw new PreexistingEntityException("SharedInformationType " + sharedInformationType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SharedInformationType sharedInformationType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SharedInformationType persistentSharedInformationType = em.find(SharedInformationType.class, sharedInformationType.getId());
            Collection<SharedInformation> sharedInformationCollectionOld = persistentSharedInformationType.getSharedInformationCollection();
            Collection<SharedInformation> sharedInformationCollectionNew = sharedInformationType.getSharedInformationCollection();
            List<String> illegalOrphanMessages = null;
            for (SharedInformation sharedInformationCollectionOldSharedInformation : sharedInformationCollectionOld) {
                if (!sharedInformationCollectionNew.contains(sharedInformationCollectionOldSharedInformation)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SharedInformation " + sharedInformationCollectionOldSharedInformation + " since its sharedInformationTypeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<SharedInformation> attachedSharedInformationCollectionNew = new ArrayList<SharedInformation>();
            for (SharedInformation sharedInformationCollectionNewSharedInformationToAttach : sharedInformationCollectionNew) {
                sharedInformationCollectionNewSharedInformationToAttach = em.getReference(sharedInformationCollectionNewSharedInformationToAttach.getClass(), sharedInformationCollectionNewSharedInformationToAttach.getId());
                attachedSharedInformationCollectionNew.add(sharedInformationCollectionNewSharedInformationToAttach);
            }
            sharedInformationCollectionNew = attachedSharedInformationCollectionNew;
            sharedInformationType.setSharedInformationCollection(sharedInformationCollectionNew);
            sharedInformationType = em.merge(sharedInformationType);
            for (SharedInformation sharedInformationCollectionNewSharedInformation : sharedInformationCollectionNew) {
                if (!sharedInformationCollectionOld.contains(sharedInformationCollectionNewSharedInformation)) {
                    SharedInformationType oldSharedInformationTypeIdOfSharedInformationCollectionNewSharedInformation = sharedInformationCollectionNewSharedInformation.getSharedInformationTypeId();
                    sharedInformationCollectionNewSharedInformation.setSharedInformationTypeId(sharedInformationType);
                    sharedInformationCollectionNewSharedInformation = em.merge(sharedInformationCollectionNewSharedInformation);
                    if (oldSharedInformationTypeIdOfSharedInformationCollectionNewSharedInformation != null && !oldSharedInformationTypeIdOfSharedInformationCollectionNewSharedInformation.equals(sharedInformationType)) {
                        oldSharedInformationTypeIdOfSharedInformationCollectionNewSharedInformation.getSharedInformationCollection().remove(sharedInformationCollectionNewSharedInformation);
                        oldSharedInformationTypeIdOfSharedInformationCollectionNewSharedInformation = em.merge(oldSharedInformationTypeIdOfSharedInformationCollectionNewSharedInformation);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sharedInformationType.getId();
                if (findSharedInformationType(id) == null) {
                    throw new NonexistentEntityException("The sharedInformationType with id " + id + " no longer exists.");
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
            SharedInformationType sharedInformationType;
            try {
                sharedInformationType = em.getReference(SharedInformationType.class, id);
                sharedInformationType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sharedInformationType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<SharedInformation> sharedInformationCollectionOrphanCheck = sharedInformationType.getSharedInformationCollection();
            for (SharedInformation sharedInformationCollectionOrphanCheckSharedInformation : sharedInformationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SharedInformationType (" + sharedInformationType + ") cannot be destroyed since the SharedInformation " + sharedInformationCollectionOrphanCheckSharedInformation + " in its sharedInformationCollection field has a non-nullable sharedInformationTypeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(sharedInformationType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SharedInformationType> findSharedInformationTypeEntities() {
        return findSharedInformationTypeEntities(true, -1, -1);
    }

    public List<SharedInformationType> findSharedInformationTypeEntities(int maxResults, int firstResult) {
        return findSharedInformationTypeEntities(false, maxResults, firstResult);
    }

    private List<SharedInformationType> findSharedInformationTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SharedInformationType.class));
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

    public SharedInformationType findSharedInformationType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SharedInformationType.class, id);
        } finally {
            em.close();
        }
    }

    public int getSharedInformationTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SharedInformationType> rt = cq.from(SharedInformationType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
