/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "Test_Result")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestResult.findAll", query = "SELECT t FROM TestResult t")
    , @NamedQuery(name = "TestResult.findById", query = "SELECT t FROM TestResult t WHERE t.id = :id")
    , @NamedQuery(name = "TestResult.findByTimeFinish", query = "SELECT t FROM TestResult t WHERE t.timeFinish = :timeFinish")
    , @NamedQuery(name = "TestResult.findByTimeReturn", query = "SELECT t FROM TestResult t WHERE t.timeReturn = :timeReturn")
    , @NamedQuery(name = "TestResult.findByEvaluation", query = "SELECT t FROM TestResult t WHERE t.evaluation = :evaluation")
    , @NamedQuery(name = "TestResult.findByTestRequestId", query = "SELECT t FROM TestResult t inner join t.testRequestId r WHERE r.id = :testrequestid")
    , @NamedQuery(name = "TestResult.findByDescription", query = "SELECT t FROM TestResult t WHERE t.description = :description")})
public class TestResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private String id;
    @Column(name = "TimeFinish")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeFinish;
    @Column(name = "TimeReturn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeReturn;
    @Column(name = "Evaluation")
    private String evaluation;
    @Column(name = "Description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testResultId")
    private Collection<TestResultDetail> testResultDetailCollection;
    @JoinColumn(name = "Test_RequestId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private TestRequest testRequestId;

    public TestResult() {
    }

    public TestResult(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(Date timeFinish) {
        this.timeFinish = timeFinish;
    }

    public Date getTimeReturn() {
        return timeReturn;
    }

    public void setTimeReturn(Date timeReturn) {
        this.timeReturn = timeReturn;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<TestResultDetail> getTestResultDetailCollection() {
        return testResultDetailCollection;
    }

    public void setTestResultDetailCollection(Collection<TestResultDetail> testResultDetailCollection) {
        this.testResultDetailCollection = testResultDetailCollection;
    }

    public TestRequest getTestRequestId() {
        return testRequestId;
    }

    public void setTestRequestId(TestRequest testRequestId) {
        this.testRequestId = testRequestId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestResult)) {
            return false;
        }
        TestResult other = (TestResult) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.TestResult[ id=" + id + " ]";
    }

}
