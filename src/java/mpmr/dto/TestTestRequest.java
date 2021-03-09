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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "Test_Test_Request")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestTestRequest.findAll", query = "SELECT t FROM TestTestRequest t")
    , @NamedQuery(name = "TestTestRequest.findById", query = "SELECT t FROM TestTestRequest t WHERE t.id = :id")
    , @NamedQuery(name = "TestTestRequest.findByTestRequestId", query = "SELECT t FROM TestTestRequest t inner join t.testRequestId r WHERE r.id = :testrequestid")
    , @NamedQuery(name = "TestTestRequest.findCountByTestRequestId", query = "SELECT COUNT(t) FROM TestTestRequest t inner join t.testRequestId r WHERE r.id = :testrequestid")
    , @NamedQuery(name = "TestTestRequest.findTestRequestIdByCount", query = "SELECT t.testRequestId FROM TestTestRequest t inner join t.testRequestId r inner join r.examinationId e ON e.diagnose IS NOT NULL GROUP BY t.testRequestId HAVING COUNT(t.testRequestId.id) = :count")
    , @NamedQuery(name = "TestTestRequest.findByExamination", query = "SELECT t FROM TestTestRequest t inner join t.testRequestId r inner join r.examinationId e WHERE e.id = :examinationId")
})
public class TestTestRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "Decimal(10,0)")
    private Integer id;
    @JoinColumn(name = "TestId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Test testId;
    @JoinColumn(name = "Test_RequestId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private TestRequest testRequestId;

    public TestTestRequest() {
    }

    public TestTestRequest(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Test getTestId() {
        return testId;
    }

    public void setTestId(Test testId) {
        this.testId = testId;
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
        if (!(object instanceof TestTestRequest)) {
            return false;
        }
        TestTestRequest other = (TestTestRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.TestTestRequest[ id=" + id + " ]";
    }

}
