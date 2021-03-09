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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "Package")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Package.findAll", query = "SELECT p FROM Package p")
    , @NamedQuery(name = "Package.findById", query = "SELECT p FROM Package p WHERE p.id = :id")
    , @NamedQuery(name = "Package.findByName", query = "SELECT p FROM Package p WHERE p.name = :name")
    , @NamedQuery(name = "Package.findByStatus", query = "SELECT p FROM Package p WHERE p.status = :status")
    , @NamedQuery(name = "Package.findByPackageId", query = "SELECT p FROM Package p inner join p.packageId i WHERE i.id = :packageid")
    , @NamedQuery(name = "Package.findByDescription", query = "SELECT p FROM Package p WHERE p.description = :description")})
public class Package implements Serializable {

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "packageId")
    private Collection<PackageTest> packageTestCollection;
    @OneToMany(mappedBy = "packageId")
    private Collection<Package> packageCollection;
    @JoinColumn(name = "PackageId", referencedColumnName = "Id")
    @ManyToOne
    private Package packageId;

    public Package() {
    }

    public Package(String id) {
        this.id = id;
    }

    public Package(String id, String name, String status) {
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
    public Collection<PackageTest> getPackageTestCollection() {
        return packageTestCollection;
    }

    public void setPackageTestCollection(Collection<PackageTest> packageTestCollection) {
        this.packageTestCollection = packageTestCollection;
    }

    @XmlTransient
    public Collection<Package> getPackageCollection() {
        return packageCollection;
    }

    public void setPackageCollection(Collection<Package> packageCollection) {
        this.packageCollection = packageCollection;
    }

    public Package getPackageId() {
        return packageId;
    }

    public void setPackageId(Package packageId) {
        this.packageId = packageId;
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
        if (!(object instanceof Package)) {
            return false;
        }
        Package other = (Package) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.Package[ id=" + id + " ]";
    }

}
