/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.dto;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "Test")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Test.findAll", query = "SELECT t FROM Test t")
    , @NamedQuery(name = "Test.findById", query = "SELECT t FROM Test t WHERE t.id = :id")
    , @NamedQuery(name = "Test.findByName", query = "SELECT t FROM Test t WHERE t.name = :name")
    , @NamedQuery(name = "Test.findByStatus", query = "SELECT t FROM Test t WHERE t.status = :status")
    , @NamedQuery(name = "Test.findByDescription", query = "SELECT t FROM Test t WHERE t.description = :description")})
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private String id;
    @Basic(optional = false)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @Column(name = "Status")
    private String status;
    @Column(name = "Description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testId")
    private Collection<TestResultDetail> testResultDetailCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testId")
    private Collection<PackageTest> packageTestCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testId")
    private Collection<TestTestRequest> testTestRequestCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testId")
    private Collection<TestResultSample> testResultSampleCollection;

    public Test() {
    }

    public Test(String id) {
        this.id = id;
    }

    public Test(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @XmlTransient
    public Collection<PackageTest> getPackageTestCollection() {
        return packageTestCollection;
    }

    public void setPackageTestCollection(Collection<PackageTest> packageTestCollection) {
        this.packageTestCollection = packageTestCollection;
    }

    @XmlTransient
    public Collection<TestTestRequest> getTestTestRequestCollection() {
        return testTestRequestCollection;
    }

    public void setTestTestRequestCollection(Collection<TestTestRequest> testTestRequestCollection) {
        this.testTestRequestCollection = testTestRequestCollection;
    }

    @XmlTransient
    public Collection<TestResultSample> getTestResultSampleCollection() {
        return testResultSampleCollection;
    }

    public void setTestResultSampleCollection(Collection<TestResultSample> testResultSampleCollection) {
        this.testResultSampleCollection = testResultSampleCollection;
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
        if (!(object instanceof Test)) {
            return false;
        }
        Test other = (Test) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.Test[ id=" + id + " ]";
    }
    
}
