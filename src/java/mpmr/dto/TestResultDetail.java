/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "Test_Result_Detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestResultDetail.findAll", query = "SELECT t FROM TestResultDetail t")
    , @NamedQuery(name = "TestResultDetail.findById", query = "SELECT t FROM TestResultDetail t WHERE t.id = :id")
    , @NamedQuery(name = "TestResultDetail.findByIndexValueResult", query = "SELECT t FROM TestResultDetail t WHERE t.indexValueResult = :indexValueResult")
    , @NamedQuery(name = "TestResultDetail.findByTestResultId", query = "SELECT t FROM TestResultDetail t inner join t.testResultId r WHERE r.id = :testResultId")
    , @NamedQuery(name = "TestResultDetail.findByTestId", query = "SELECT t FROM TestResultDetail t inner join t.testId r WHERE r.id = :testId")
    , @NamedQuery(name = "TestResultDetail.findByTestName", query = "SELECT t FROM TestResultDetail t inner join t.testId r WHERE r.name = :testName")
    , @NamedQuery(name = "TestResultDetail.findByTestValue", query = "SELECT t FROM TestResultDetail t inner join t.testId r WHERE r.id = :testId AND t.indexValueResult >= :startValue AND t.indexValueResult <= :endValue")
    , @NamedQuery(name = "TestResultDetail.findByDescription", query = "SELECT t FROM TestResultDetail t WHERE t.description = :description")})
public class TestResultDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private String id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Index_Value_Result")
    private Float indexValueResult;
    @Column(name = "Description")
    private String description;
    @JoinColumn(name = "TestId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Test testId;
    @JoinColumn(name = "Test_ResultId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private TestResult testResultId;

    public TestResultDetail() {
    }

    public TestResultDetail(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getIndexValueResult() {
        return indexValueResult;
    }

    public void setIndexValueResult(Float indexValueResult) {
        this.indexValueResult = indexValueResult;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Test getTestId() {
        return testId;
    }

    public void setTestId(Test testId) {
        this.testId = testId;
    }

    public TestResult getTestResultId() {
        return testResultId;
    }

    public void setTestResultId(TestResult testResultId) {
        this.testResultId = testResultId;
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
        if (!(object instanceof TestResultDetail)) {
            return false;
        }
        TestResultDetail other = (TestResultDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.TestResultDetail[ id=" + id + " ]";
    }

}
