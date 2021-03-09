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
@Table(name = "Package_Test")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PackageTest.findAll", query = "SELECT p FROM PackageTest p")
    , @NamedQuery(name = "PackageTest.findById", query = "SELECT p FROM PackageTest p WHERE p.id = :id")
    , @NamedQuery(name = "PackageTest.findByName", query = "SELECT p FROM PackageTest p inner join p.packageId i WHERE i.name = :name")
    , @NamedQuery(name = "PackageTest.findByCount", query = "SELECT p.packageId FROM PackageTest p  GROUP BY p.packageId HAVING COUNT(p.packageId.id) = :count")
    , @NamedQuery(name = "PackageTest.findByTestId", query = "SELECT p FROM PackageTest p inner join p.testId t WHERE t.id = :testid")
    , @NamedQuery(name = "PackageTest.findByPackageId", query = "SELECT p FROM PackageTest p inner join p.packageId i WHERE i.id = :packageId")

})
public class PackageTest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "Decimal(10,0)")
    private Integer id;
    @JoinColumn(name = "PackageId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Package packageId;
    @JoinColumn(name = "TestId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Test testId;

    public PackageTest() {
    }

    public PackageTest(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Package getPackageId() {
        return packageId;
    }

    public void setPackageId(Package packageId) {
        this.packageId = packageId;
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
        if (!(object instanceof PackageTest)) {
            return false;
        }
        PackageTest other = (PackageTest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.PackageTest[ id=" + id + " ]";
    }

}
