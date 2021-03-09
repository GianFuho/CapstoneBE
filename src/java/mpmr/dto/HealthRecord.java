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
@Table(name = "Health_Record")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HealthRecord.findAll", query = "SELECT h FROM HealthRecord h")
    , @NamedQuery(name = "HealthRecord.findById", query = "SELECT h FROM HealthRecord h WHERE h.id = :id")
    , @NamedQuery(name = "HealthRecord.findByBloodType", query = "SELECT h FROM HealthRecord h WHERE h.bloodType = :bloodType")
    , @NamedQuery(name = "HealthRecord.findByHeight", query = "SELECT h FROM HealthRecord h WHERE h.height = :height")
    , @NamedQuery(name = "HealthRecord.findByWeight", query = "SELECT h FROM HealthRecord h WHERE h.weight = :weight")
    , @NamedQuery(name = "HealthRecord.findByUserId", query = "SELECT h FROM HealthRecord h inner join h.userId u WHERE u.id = :userid")
    , @NamedQuery(name = "HealthRecord.findByMedicalNote", query = "SELECT h FROM HealthRecord h WHERE h.medicalNote = :medicalNote")})
public class HealthRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private String id;
    @Column(name = "BloodType")
    private String bloodType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Height")
    private Float height;
    @Column(name = "Weight")
    private Float weight;
    @Column(name = "Medical_Note")
    private String medicalNote;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "healthRecordId")
    private Collection<SharedInformation> sharedInformationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "healthRecordId")
    private Collection<DiseaseHealthRecord> diseaseHealthRecordCollection;
    @JoinColumn(name = "UserId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private UserInfor userId;

    public HealthRecord() {
    }

    public HealthRecord(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getMedicalNote() {
        return medicalNote;
    }

    public void setMedicalNote(String medicalNote) {
        this.medicalNote = medicalNote;
    }

    @XmlTransient
    public Collection<SharedInformation> getSharedInformationCollection() {
        return sharedInformationCollection;
    }

    public void setSharedInformationCollection(Collection<SharedInformation> sharedInformationCollection) {
        this.sharedInformationCollection = sharedInformationCollection;
    }

    @XmlTransient
    public Collection<DiseaseHealthRecord> getDiseaseHealthRecordCollection() {
        return diseaseHealthRecordCollection;
    }

    public void setDiseaseHealthRecordCollection(Collection<DiseaseHealthRecord> diseaseHealthRecordCollection) {
        this.diseaseHealthRecordCollection = diseaseHealthRecordCollection;
    }

    public UserInfor getUserId() {
        return userId;
    }

    public void setUserId(UserInfor userId) {
        this.userId = userId;
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
        if (!(object instanceof HealthRecord)) {
            return false;
        }
        HealthRecord other = (HealthRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.HealthRecord[ id=" + id + " ]";
    }

}
