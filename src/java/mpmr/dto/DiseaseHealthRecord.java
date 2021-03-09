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
@Table(name = "Disease_Health_Record")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiseaseHealthRecord.findAll", query = "SELECT d FROM DiseaseHealthRecord d")
    , @NamedQuery(name = "DiseaseHealthRecord.findById", query = "SELECT d FROM DiseaseHealthRecord d WHERE d.id = :id")})
public class DiseaseHealthRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @JoinColumn(name = "DiseaseId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Disease diseaseId;
    @JoinColumn(name = "Health_RecordId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private HealthRecord healthRecordId;

    public DiseaseHealthRecord() {
    }

    public DiseaseHealthRecord(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Disease getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Disease diseaseId) {
        this.diseaseId = diseaseId;
    }

    public HealthRecord getHealthRecordId() {
        return healthRecordId;
    }

    public void setHealthRecordId(HealthRecord healthRecordId) {
        this.healthRecordId = healthRecordId;
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
        if (!(object instanceof DiseaseHealthRecord)) {
            return false;
        }
        DiseaseHealthRecord other = (DiseaseHealthRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.DiseaseHealthRecord[ id=" + id + " ]";
    }
    
}
