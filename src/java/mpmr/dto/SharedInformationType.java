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
@Table(name = "Shared_Information_Type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SharedInformationType.findAll", query = "SELECT s FROM SharedInformationType s")
    , @NamedQuery(name = "SharedInformationType.findById", query = "SELECT s FROM SharedInformationType s WHERE s.id = :id")
    , @NamedQuery(name = "SharedInformationType.findByName", query = "SELECT s FROM SharedInformationType s WHERE s.name = :name")})
public class SharedInformationType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sharedInformationTypeId")
    private Collection<SharedInformation> sharedInformationCollection;

    public SharedInformationType() {
    }

    public SharedInformationType(Integer id) {
        this.id = id;
    }

    public SharedInformationType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<SharedInformation> getSharedInformationCollection() {
        return sharedInformationCollection;
    }

    public void setSharedInformationCollection(Collection<SharedInformation> sharedInformationCollection) {
        this.sharedInformationCollection = sharedInformationCollection;
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
        if (!(object instanceof SharedInformationType)) {
            return false;
        }
        SharedInformationType other = (SharedInformationType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.SharedInformationType[ id=" + id + " ]";
    }
    
}
