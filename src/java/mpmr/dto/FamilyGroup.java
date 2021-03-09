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
@Table(name = "Family_Group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FamilyGroup.findAll", query = "SELECT f FROM FamilyGroup f")
    , @NamedQuery(name = "FamilyGroup.findById", query = "SELECT f FROM FamilyGroup f WHERE f.id = :id")
    , @NamedQuery(name = "FamilyGroup.findByName", query = "SELECT f FROM FamilyGroup f WHERE f.name = :name")
    , @NamedQuery(name = "FamilyGroup.findByStatus", query = "SELECT f FROM FamilyGroup f WHERE f.status = :status")
    , @NamedQuery(name = "FamilyGroup.findByAvatar", query = "SELECT f FROM FamilyGroup f WHERE f.avatar = :avatar")})
public class FamilyGroup implements Serializable {

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
    @Column(name = "Avatar")
    private String avatar;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "familyGroupId")
    private Collection<UserFamilyGroup> userFamilyGroupCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "familyGroupId")
    private Collection<Request> requestCollection;

    public FamilyGroup() {
    }

    public FamilyGroup(String id) {
        this.id = id;
    }

    public FamilyGroup(String id, String name, String status) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @XmlTransient
    public Collection<UserFamilyGroup> getUserFamilyGroupCollection() {
        return userFamilyGroupCollection;
    }

    public void setUserFamilyGroupCollection(Collection<UserFamilyGroup> userFamilyGroupCollection) {
        this.userFamilyGroupCollection = userFamilyGroupCollection;
    }

    @XmlTransient
    public Collection<Request> getRequestCollection() {
        return requestCollection;
    }

    public void setRequestCollection(Collection<Request> requestCollection) {
        this.requestCollection = requestCollection;
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
        if (!(object instanceof FamilyGroup)) {
            return false;
        }
        FamilyGroup other = (FamilyGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.FamilyGroup[ id=" + id + " ]";
    }
    
}
