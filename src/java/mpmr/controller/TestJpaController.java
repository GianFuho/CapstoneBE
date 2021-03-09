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
import mpmr.dto.TestResultDetail;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mpmr.dto.PackageTest;
import mpmr.dto.Test;
import mpmr.dto.TestTestRequest;
import mpmr.dto.TestResultSample;
import mpmr.controller.exceptions.IllegalOrphanException;
import mpmr.controller.exceptions.NonexistentEntityException;
import mpmr.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author Admin
 */
public class TestJpaController implements Serializable {

    public TestJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Test test) throws PreexistingEntityException, Exception {
        if (test.getTestResultDetailCollection() == null) {
            test.setTestResultDetailCollection(new ArrayList<TestResultDetail>());
        }
        if (test.getPackageTestCollection() == null) {
            test.setPackageTestCollection(new ArrayList<PackageTest>());
        }
        if (test.getTestTestRequestCollection() == null) {
            test.setTestTestRequestCollection(new ArrayList<TestTestRequest>());
        }
        if (test.getTestResultSampleCollection() == null) {
            test.setTestResultSampleCollection(new ArrayList<TestResultSample>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<TestResultDetail> attachedTestResultDetailCollection = new ArrayList<TestResultDetail>();
            for (TestResultDetail testResultDetailCollectionTestResultDetailToAttach : test.getTestResultDetailCollection()) {
                testResultDetailCollectionTestResultDetailToAttach = em.getReference(testResultDetailCollectionTestResultDetailToAttach.getClass(), testResultDetailCollectionTestResultDetailToAttach.getId());
                attachedTestResultDetailCollection.add(testResultDetailCollectionTestResultDetailToAttach);
            }
            test.setTestResultDetailCollection(attachedTestResultDetailCollection);
            Collection<PackageTest> attachedPackageTestCollection = new ArrayList<PackageTest>();
            for (PackageTest packageTestCollectionPackageTestToAttach : test.getPackageTestCollection()) {
                packageTestCollectionPackageTestToAttach = em.getReference(packageTestCollectionPackageTestToAttach.getClass(), packageTestCollectionPackageTestToAttach.getId());
                attachedPackageTestCollection.add(packageTestCollectionPackageTestToAttach);
            }
            test.setPackageTestCollection(attachedPackageTestCollection);
            Collection<TestTestRequest> attachedTestTestRequestCollection = new ArrayList<TestTestRequest>();
            for (TestTestRequest testTestRequestCollectionTestTestRequestToAttach : test.getTestTestRequestCollection()) {
                testTestRequestCollectionTestTestRequestToAttach = em.getReference(testTestRequestCollectionTestTestRequestToAttach.getClass(), testTestRequestCollectionTestTestRequestToAttach.getId());
                attachedTestTestRequestCollection.add(testTestRequestCollectionTestTestRequestToAttach);
            }
            test.setTestTestRequestCollection(attachedTestTestRequestCollection);
            Collection<TestResultSample> attachedTestResultSampleCollection = new ArrayList<TestResultSample>();
            for (TestResultSample testResultSampleCollectionTestResultSampleToAttach : test.getTestResultSampleCollection()) {
                testResultSampleCollectionTestResultSampleToAttach = em.getReference(testResultSampleCollectionTestResultSampleToAttach.getClass(), testResultSampleCollectionTestResultSampleToAttach.getId());
                attachedTestResultSampleCollection.add(testResultSampleCollectionTestResultSampleToAttach);
            }
            test.setTestResultSampleCollection(attachedTestResultSampleCollection);
            em.persist(test);
            for (TestResultDetail testResultDetailCollectionTestResultDetail : test.getTestResultDetailCollection()) {
                Test oldTestIdOfTestResultDetailCollectionTestResultDetail = testResultDetailCollectionTestResultDetail.getTestId();
                testResultDetailCollectionTestResultDetail.setTestId(test);
                testResultDetailCollectionTestResultDetail = em.merge(testResultDetailCollectionTestResultDetail);
                if (oldTestIdOfTestResultDetailCollectionTestResultDetail != null) {
                    oldTestIdOfTestResultDetailCollectionTestResultDetail.getTestResultDetailCollection().remove(testResultDetailCollectionTestResultDetail);
                    oldTestIdOfTestResultDetailCollectionTestResultDetail = em.merge(oldTestIdOfTestResultDetailCollectionTestResultDetail);
                }
            }
            for (PackageTest packageTestCollectionPackageTest : test.getPackageTestCollection()) {
                Test oldTestIdOfPackageTestCollectionPackageTest = packageTestCollectionPackageTest.getTestId();
                packageTestCollectionPackageTest.setTestId(test);
                packageTestCollectionPackageTest = em.merge(packageTestCollectionPackageTest);
                if (oldTestIdOfPackageTestCollectionPackageTest != null) {
                    oldTestIdOfPackageTestCollectionPackageTest.getPackageTestCollection().remove(packageTestCollectionPackageTest);
                    oldTestIdOfPackageTestCollectionPackageTest = em.merge(oldTestIdOfPackageTestCollectionPackageTest);
                }
            }
            for (TestTestRequest testTestRequestCollectionTestTestRequest : test.getTestTestRequestCollection()) {
                Test oldTestIdOfTestTestRequestCollectionTestTestRequest = testTestRequestCollectionTestTestRequest.getTestId();
                testTestRequestCollectionTestTestRequest.setTestId(test);
                testTestRequestCollectionTestTestRequest = em.merge(testTestRequestCollectionTestTestRequest);
                if (oldTestIdOfTestTestRequestCollectionTestTestRequest != null) {
                    oldTestIdOfTestTestRequestCollectionTestTestRequest.getTestTestRequestCollection().remove(testTestRequestCollectionTestTestRequest);
                    oldTestIdOfTestTestRequestCollectionTestTestRequest = em.merge(oldTestIdOfTestTestRequestCollectionTestTestRequest);
                }
            }
            for (TestResultSample testResultSampleCollectionTestResultSample : test.getTestResultSampleCollection()) {
                Test oldTestIdOfTestResultSampleCollectionTestResultSample = testResultSampleCollectionTestResultSample.getTestId();
                testResultSampleCollectionTestResultSample.setTestId(test);
                testResultSampleCollectionTestResultSample = em.merge(testResultSampleCollectionTestResultSample);
                if (oldTestIdOfTestResultSampleCollectionTestResultSample != null) {
                    oldTestIdOfTestResultSampleCollectionTestResultSample.getTestResultSampleCollection().remove(testResultSampleCollectionTestResultSample);
                    oldTestIdOfTestResultSampleCollectionTestResultSample = em.merge(oldTestIdOfTestResultSampleCollectionTestResultSample);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTest(test.getId()) != null) {
                throw new PreexistingEntityException("Test " + test + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Test test) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Test persistentTest = em.find(Test.class, test.getId());
            Collection<TestResultDetail> testResultDetailCollectionOld = persistentTest.getTestResultDetailCollection();
            Collection<TestResultDetail> testResultDetailCollectionNew = test.getTestResultDetailCollection();
            Collection<PackageTest> packageTestCollectionOld = persistentTest.getPackageTestCollection();
            Collection<PackageTest> packageTestCollectionNew = test.getPackageTestCollection();
            Collection<TestTestRequest> testTestRequestCollectionOld = persistentTest.getTestTestRequestCollection();
            Collection<TestTestRequest> testTestRequestCollectionNew = test.getTestTestRequestCollection();
            Collection<TestResultSample> testResultSampleCollectionOld = persistentTest.getTestResultSampleCollection();
            Collection<TestResultSample> testResultSampleCollectionNew = test.getTestResultSampleCollection();
            List<String> illegalOrphanMessages = null;
//            for (TestResultDetail testResultDetailCollectionOldTestResultDetail : testResultDetailCollectionOld) {
//                if (!testResultDetailCollectionNew.contains(testResultDetailCollectionOldTestResultDetail)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain TestResultDetail " + testResultDetailCollectionOldTestResultDetail + " since its testId field is not nullable.");
//                }
//            }
//            for (PackageTest packageTestCollectionOldPackageTest : packageTestCollectionOld) {
//                if (!packageTestCollectionNew.contains(packageTestCollectionOldPackageTest)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain PackageTest " + packageTestCollectionOldPackageTest + " since its testId field is not nullable.");
//                }
//            }
//            for (TestTestRequest testTestRequestCollectionOldTestTestRequest : testTestRequestCollectionOld) {
//                if (!testTestRequestCollectionNew.contains(testTestRequestCollectionOldTestTestRequest)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain TestTestRequest " + testTestRequestCollectionOldTestTestRequest + " since its testId field is not nullable.");
//                }
//            }
//            for (TestResultSample testResultSampleCollectionOldTestResultSample : testResultSampleCollectionOld) {
//                if (!testResultSampleCollectionNew.contains(testResultSampleCollectionOldTestResultSample)) {
//                    if (illegalOrphanMessages == null) {
//                        illegalOrphanMessages = new ArrayList<String>();
//                    }
//                    illegalOrphanMessages.add("You must retain TestResultSample " + testResultSampleCollectionOldTestResultSample + " since its testId field is not nullable.");
//                }
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
//            Collection<TestResultDetail> attachedTestResultDetailCollectionNew = new ArrayList<TestResultDetail>();
//            for (TestResultDetail testResultDetailCollectionNewTestResultDetailToAttach : testResultDetailCollectionNew) {
//                testResultDetailCollectionNewTestResultDetailToAttach = em.getReference(testResultDetailCollectionNewTestResultDetailToAttach.getClass(), testResultDetailCollectionNewTestResultDetailToAttach.getId());
//                attachedTestResultDetailCollectionNew.add(testResultDetailCollectionNewTestResultDetailToAttach);
//            }
//            testResultDetailCollectionNew = attachedTestResultDetailCollectionNew;
//            test.setTestResultDetailCollection(testResultDetailCollectionNew);
//            Collection<PackageTest> attachedPackageTestCollectionNew = new ArrayList<PackageTest>();
//            for (PackageTest packageTestCollectionNewPackageTestToAttach : packageTestCollectionNew) {
//                packageTestCollectionNewPackageTestToAttach = em.getReference(packageTestCollectionNewPackageTestToAttach.getClass(), packageTestCollectionNewPackageTestToAttach.getId());
//                attachedPackageTestCollectionNew.add(packageTestCollectionNewPackageTestToAttach);
//            }
//            packageTestCollectionNew = attachedPackageTestCollectionNew;
//            test.setPackageTestCollection(packageTestCollectionNew);
//            Collection<TestTestRequest> attachedTestTestRequestCollectionNew = new ArrayList<TestTestRequest>();
//            for (TestTestRequest testTestRequestCollectionNewTestTestRequestToAttach : testTestRequestCollectionNew) {
//                testTestRequestCollectionNewTestTestRequestToAttach = em.getReference(testTestRequestCollectionNewTestTestRequestToAttach.getClass(), testTestRequestCollectionNewTestTestRequestToAttach.getId());
//                attachedTestTestRequestCollectionNew.add(testTestRequestCollectionNewTestTestRequestToAttach);
//            }
//            testTestRequestCollectionNew = attachedTestTestRequestCollectionNew;
//            test.setTestTestRequestCollection(testTestRequestCollectionNew);
//            Collection<TestResultSample> attachedTestResultSampleCollectionNew = new ArrayList<TestResultSample>();
//            for (TestResultSample testResultSampleCollectionNewTestResultSampleToAttach : testResultSampleCollectionNew) {
//                testResultSampleCollectionNewTestResultSampleToAttach = em.getReference(testResultSampleCollectionNewTestResultSampleToAttach.getClass(), testResultSampleCollectionNewTestResultSampleToAttach.getId());
//                attachedTestResultSampleCollectionNew.add(testResultSampleCollectionNewTestResultSampleToAttach);
//            }
//            testResultSampleCollectionNew = attachedTestResultSampleCollectionNew;
            test.setTestResultSampleCollection(testResultSampleCollectionNew);
            test = em.merge(test);
//            for (TestResultDetail testResultDetailCollectionNewTestResultDetail : testResultDetailCollectionNew) {
//                if (!testResultDetailCollectionOld.contains(testResultDetailCollectionNewTestResultDetail)) {
//                    Test oldTestIdOfTestResultDetailCollectionNewTestResultDetail = testResultDetailCollectionNewTestResultDetail.getTestId();
//                    testResultDetailCollectionNewTestResultDetail.setTestId(test);
//                    testResultDetailCollectionNewTestResultDetail = em.merge(testResultDetailCollectionNewTestResultDetail);
//                    if (oldTestIdOfTestResultDetailCollectionNewTestResultDetail != null && !oldTestIdOfTestResultDetailCollectionNewTestResultDetail.equals(test)) {
//                        oldTestIdOfTestResultDetailCollectionNewTestResultDetail.getTestResultDetailCollection().remove(testResultDetailCollectionNewTestResultDetail);
//                        oldTestIdOfTestResultDetailCollectionNewTestResultDetail = em.merge(oldTestIdOfTestResultDetailCollectionNewTestResultDetail);
//                    }
//                }
//            }
//            for (PackageTest packageTestCollectionNewPackageTest : packageTestCollectionNew) {
//                if (!packageTestCollectionOld.contains(packageTestCollectionNewPackageTest)) {
//                    Test oldTestIdOfPackageTestCollectionNewPackageTest = packageTestCollectionNewPackageTest.getTestId();
//                    packageTestCollectionNewPackageTest.setTestId(test);
//                    packageTestCollectionNewPackageTest = em.merge(packageTestCollectionNewPackageTest);
//                    if (oldTestIdOfPackageTestCollectionNewPackageTest != null && !oldTestIdOfPackageTestCollectionNewPackageTest.equals(test)) {
//                        oldTestIdOfPackageTestCollectionNewPackageTest.getPackageTestCollection().remove(packageTestCollectionNewPackageTest);
//                        oldTestIdOfPackageTestCollectionNewPackageTest = em.merge(oldTestIdOfPackageTestCollectionNewPackageTest);
//                    }
//                }
//            }
//            for (TestTestRequest testTestRequestCollectionNewTestTestRequest : testTestRequestCollectionNew) {
//                if (!testTestRequestCollectionOld.contains(testTestRequestCollectionNewTestTestRequest)) {
//                    Test oldTestIdOfTestTestRequestCollectionNewTestTestRequest = testTestRequestCollectionNewTestTestRequest.getTestId();
//                    testTestRequestCollectionNewTestTestRequest.setTestId(test);
//                    testTestRequestCollectionNewTestTestRequest = em.merge(testTestRequestCollectionNewTestTestRequest);
//                    if (oldTestIdOfTestTestRequestCollectionNewTestTestRequest != null && !oldTestIdOfTestTestRequestCollectionNewTestTestRequest.equals(test)) {
//                        oldTestIdOfTestTestRequestCollectionNewTestTestRequest.getTestTestRequestCollection().remove(testTestRequestCollectionNewTestTestRequest);
//                        oldTestIdOfTestTestRequestCollectionNewTestTestRequest = em.merge(oldTestIdOfTestTestRequestCollectionNewTestTestRequest);
//                    }
//                }
//            }
//            for (TestResultSample testResultSampleCollectionNewTestResultSample : testResultSampleCollectionNew) {
//                if (!testResultSampleCollectionOld.contains(testResultSampleCollectionNewTestResultSample)) {
//                    Test oldTestIdOfTestResultSampleCollectionNewTestResultSample = testResultSampleCollectionNewTestResultSample.getTestId();
//                    testResultSampleCollectionNewTestResultSample.setTestId(test);
//                    testResultSampleCollectionNewTestResultSample = em.merge(testResultSampleCollectionNewTestResultSample);
//                    if (oldTestIdOfTestResultSampleCollectionNewTestResultSample != null && !oldTestIdOfTestResultSampleCollectionNewTestResultSample.equals(test)) {
//                        oldTestIdOfTestResultSampleCollectionNewTestResultSample.getTestResultSampleCollection().remove(testResultSampleCollectionNewTestResultSample);
//                        oldTestIdOfTestResultSampleCollectionNewTestResultSample = em.merge(oldTestIdOfTestResultSampleCollectionNewTestResultSample);
//                    }
//                }
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = test.getId();
                if (findTest(id) == null) {
                    throw new NonexistentEntityException("The test with id " + id + " no longer exists.");
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
            Test test;
            try {
                test = em.getReference(Test.class, id);
                test.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The test with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TestResultDetail> testResultDetailCollectionOrphanCheck = test.getTestResultDetailCollection();
            for (TestResultDetail testResultDetailCollectionOrphanCheckTestResultDetail : testResultDetailCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Test (" + test + ") cannot be destroyed since the TestResultDetail " + testResultDetailCollectionOrphanCheckTestResultDetail + " in its testResultDetailCollection field has a non-nullable testId field.");
            }
            Collection<PackageTest> packageTestCollectionOrphanCheck = test.getPackageTestCollection();
            for (PackageTest packageTestCollectionOrphanCheckPackageTest : packageTestCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Test (" + test + ") cannot be destroyed since the PackageTest " + packageTestCollectionOrphanCheckPackageTest + " in its packageTestCollection field has a non-nullable testId field.");
            }
            Collection<TestTestRequest> testTestRequestCollectionOrphanCheck = test.getTestTestRequestCollection();
            for (TestTestRequest testTestRequestCollectionOrphanCheckTestTestRequest : testTestRequestCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Test (" + test + ") cannot be destroyed since the TestTestRequest " + testTestRequestCollectionOrphanCheckTestTestRequest + " in its testTestRequestCollection field has a non-nullable testId field.");
            }
            Collection<TestResultSample> testResultSampleCollectionOrphanCheck = test.getTestResultSampleCollection();
            for (TestResultSample testResultSampleCollectionOrphanCheckTestResultSample : testResultSampleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Test (" + test + ") cannot be destroyed since the TestResultSample " + testResultSampleCollectionOrphanCheckTestResultSample + " in its testResultSampleCollection field has a non-nullable testId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(test);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Test> findTestEntities() {
        return findTestEntities(true, -1, -1);
    }

    public List<Test> findTestEntities(int maxResults, int firstResult) {
        return findTestEntities(false, maxResults, firstResult);
    }

    private List<Test> findTestEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Test.class));
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

    public Test findTest(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Test.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Test> rt = cq.from(Test.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public List<Test> findAll() {
        return (List<Test>) getEntityManager().createNamedQuery("Test.findAll").getResultList();
    }
}
