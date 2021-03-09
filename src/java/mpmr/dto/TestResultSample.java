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
@Table(name = "Test_Result_Sample")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestResultSample.findAll", query = "SELECT t FROM TestResultSample t")
    , @NamedQuery(name = "TestResultSample.findById", query = "SELECT t FROM TestResultSample t WHERE t.id = :id")
    , @NamedQuery(name = "TestResultSample.findByIndexValueMax", query = "SELECT t FROM TestResultSample t WHERE t.indexValueMax = :indexValueMax")
    , @NamedQuery(name = "TestResultSample.findByIndexValueMin", query = "SELECT t FROM TestResultSample t WHERE t.indexValueMin = :indexValueMin")
    , @NamedQuery(name = "TestResultSample.findByType", query = "SELECT t FROM TestResultSample t WHERE t.type = :type")
    , @NamedQuery(name = "TestResultSample.findByTestId", query = "SELECT t FROM TestResultSample t inner join t.testId i WHERE i.id = :testId")
    , @NamedQuery(name = "TestResultSample.findByDescription", query = "SELECT t FROM TestResultSample t WHERE t.description = :description")})
public class TestResultSample implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private String id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Index_Value_Max")
    private Float indexValueMax;
    @Column(name = "Index_Value_Min")
    private Float indexValueMin;
    @Column(name = "Type")
    private String type;
    @Column(name = "Description")
    private String description;
    @JoinColumn(name = "TestId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Test testId;

    public TestResultSample() {
    }

    public TestResultSample(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getIndexValueMax() {
        return indexValueMax;
    }

    public void setIndexValueMax(Float indexValueMax) {
        this.indexValueMax = indexValueMax;
    }

    public Float getIndexValueMin() {
        return indexValueMin;
    }

    public void setIndexValueMin(Float indexValueMin) {
        this.indexValueMin = indexValueMin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestResultSample)) {
            return false;
        }
        TestResultSample other = (TestResultSample) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.TestResultSample[ id=" + id + " ]";
    }

}
