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
@Table(name = "Test_Request")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestRequest.findAll", query = "SELECT t FROM TestRequest t")
    , @NamedQuery(name = "TestRequest.findById", query = "SELECT t FROM TestRequest t WHERE t.id = :id")
    , @NamedQuery(name = "TestRequest.findByTimeStart", query = "SELECT t FROM TestRequest t WHERE t.timeStart = :timeStart")
    , @NamedQuery(name = "TestRequest.findByTimeFinish", query = "SELECT t FROM TestRequest t WHERE t.timeFinish = :timeFinish")
    , @NamedQuery(name = "TestRequest.findByExaminationId", query = "SELECT t FROM TestRequest t inner join t.examinationId e WHERE e.id = :examinationid")
    , @NamedQuery(name = "TestRequest.findByDescription", query = "SELECT t FROM TestRequest t WHERE t.description = :description")})
public class TestRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private String id;
    @Basic(optional = false)
    @Column(name = "TimeStart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStart;
    @Column(name = "TimeFinish")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeFinish;
    @Column(name = "Description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testRequestId")
    private Collection<TestResult> testResultCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testRequestId")
    private Collection<TestTestRequest> testTestRequestCollection;
    @JoinColumn(name = "ExaminationId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Examination examinationId;

    public TestRequest() {
    }

    public TestRequest(String id) {
        this.id = id;
    }

    public TestRequest(String id, Date timeStart) {
        this.id = id;
        this.timeStart = timeStart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(Date timeFinish) {
        this.timeFinish = timeFinish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<TestResult> getTestResultCollection() {
        return testResultCollection;
    }

    public void setTestResultCollection(Collection<TestResult> testResultCollection) {
        this.testResultCollection = testResultCollection;
    }

    @XmlTransient
    public Collection<TestTestRequest> getTestTestRequestCollection() {
        return testTestRequestCollection;
    }

    public void setTestTestRequestCollection(Collection<TestTestRequest> testTestRequestCollection) {
        this.testTestRequestCollection = testTestRequestCollection;
    }

    public Examination getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(Examination examinationId) {
        this.examinationId = examinationId;
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
        if (!(object instanceof TestRequest)) {
            return false;
        }
        TestRequest other = (TestRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.TestRequest[ id=" + id + " ]";
    }

}
