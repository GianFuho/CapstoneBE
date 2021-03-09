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
@Table(name = "Clinic")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clinic.findAll", query = "SELECT c FROM Clinic c")
    , @NamedQuery(name = "Clinic.findById", query = "SELECT c FROM Clinic c WHERE c.id = :id")
    , @NamedQuery(name = "Clinic.findByName", query = "SELECT c FROM Clinic c WHERE c.name = :name")
    , @NamedQuery(name = "Clinic.findByLikeName", query = "SELECT c FROM Clinic c WHERE c.name LIKE lower(concat('%', concat(:name, '%')))")
    , @NamedQuery(name = "Clinic.findByAddress", query = "SELECT c FROM Clinic c WHERE c.address = :address")
    , @NamedQuery(name = "Clinic.findByPhone", query = "SELECT c FROM Clinic c WHERE c.phone = :phone")
    , @NamedQuery(name = "Clinic.findByCoordinate", query = "SELECT c FROM Clinic c WHERE c.coordinate = :coordinate")
    , @NamedQuery(name = "Clinic.findByStatus", query = "SELECT c FROM Clinic c WHERE c.status = :status")
    , @NamedQuery(name = "Clinic.findByImage", query = "SELECT c FROM Clinic c WHERE c.image = :image")
    , @NamedQuery(name = "Clinic.findByDescription", query = "SELECT c FROM Clinic c WHERE c.description = :description")
    , @NamedQuery(name = "Clinic.findByDistrict", query = "SELECT c FROM Clinic c WHERE c.district = :district")
    , @NamedQuery(name = "Clinic.count", query = "SELECT COUNT(c) FROM Clinic c")
    , @NamedQuery(name = "Clinic.countInDistrict", query = "SELECT COUNT(c) FROM Clinic c WHERE c.district = :district")
})
public class Clinic implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private String id;
    @Basic(optional = false)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @Column(name = "Address")
    private String address;
    @Basic(optional = false)
    @Column(name = "Phone")
    private String phone;
    @Column(name = "Coordinate")
    private String coordinate;
    @Basic(optional = false)
    @Column(name = "Status")
    private String status;
    @Column(name = "Image")
    private String image;
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @Column(name = "District")
    private String district;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicId")
    private Collection<UserInfor> userInforCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicId")
    private Collection<Examination> examinationCollection;

    public Clinic() {
    }

    public Clinic(String id) {
        this.id = id;
    }

    public Clinic(String id, String name, String address, String phone, String status, String district) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.status = status;
        this.district = district;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @XmlTransient
    public Collection<UserInfor> getUserInforCollection() {
        return userInforCollection;
    }

    public void setUserInforCollection(Collection<UserInfor> userInforCollection) {
        this.userInforCollection = userInforCollection;
    }

    @XmlTransient
    public Collection<Examination> getExaminationCollection() {
        return examinationCollection;
    }

    public void setExaminationCollection(Collection<Examination> examinationCollection) {
        this.examinationCollection = examinationCollection;
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
        if (!(object instanceof Clinic)) {
            return false;
        }
        Clinic other = (Clinic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.Clinic[ id=" + id + " ]";
    }

}
