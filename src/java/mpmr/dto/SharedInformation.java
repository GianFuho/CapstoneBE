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
@Table(name = "Shared_Information")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SharedInformation.findAll", query = "SELECT s FROM SharedInformation s")
    , @NamedQuery(name = "SharedInformation.findById", query = "SELECT s FROM SharedInformation s WHERE s.id = :id")
    , @NamedQuery(name = "SharedInformation.findByType", query = "SELECT s FROM SharedInformation s inner join s.sharedInformationTypeId t WHERE t.id = :id")
    , @NamedQuery(name = "SharedInformation.findByHealthId", query = "SELECT s FROM SharedInformation s inner join s.healthRecordId i WHERE i.id = :healthRecordId")
    , @NamedQuery(name = "SharedInformation.findBySharedInformationContext", query = "SELECT s FROM SharedInformation s WHERE s.sharedInformationContext = :sharedInformationContext")
    , @NamedQuery(name = "SharedInformation.findBySharedInformationType", query = "SELECT s FROM SharedInformation s inner join s.sharedInformationTypeId t WHERE t.id = :id")
    , @NamedQuery(name = "SharedInformation.findByHealthFGroupId", query = "SELECT s FROM SharedInformation s inner join s.healthRecordId t WHERE t.id = :id and s.fGroupId = :fGroupId")
    , @NamedQuery(name = "SharedInformation.findByContextFGroupId", query = "SELECT s FROM SharedInformation s WHERE s.sharedInformationContext = :sharedInformationContext and s.fGroupId = :fGroupId")
    , @NamedQuery(name = "SharedInformation.findByFGroupId", query = "SELECT s FROM SharedInformation s WHERE s.fGroupId = :fGroupId")})
public class SharedInformation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "Decimal(10,0)")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Shared_Information_Context")
    private String sharedInformationContext;
    @Column(name = "FGroupId")
    private String fGroupId;
    @JoinColumn(name = "Health_RecordId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private HealthRecord healthRecordId;
    @JoinColumn(name = "Shared_Information_TypeId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private SharedInformationType sharedInformationTypeId;

    public SharedInformation() {
    }

    public SharedInformation(Integer id) {
        this.id = id;
    }

    public SharedInformation(Integer id, String sharedInformationContext) {
        this.id = id;
        this.sharedInformationContext = sharedInformationContext;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSharedInformationContext() {
        return sharedInformationContext;
    }

    public void setSharedInformationContext(String sharedInformationContext) {
        this.sharedInformationContext = sharedInformationContext;
    }

    public String getFGroupId() {
        return fGroupId;
    }

    public void setFGroupId(String fGroupId) {
        this.fGroupId = fGroupId;
    }

    public HealthRecord getHealthRecordId() {
        return healthRecordId;
    }

    public void setHealthRecordId(HealthRecord healthRecordId) {
        this.healthRecordId = healthRecordId;
    }

    public SharedInformationType getSharedInformationTypeId() {
        return sharedInformationTypeId;
    }

    public void setSharedInformationTypeId(SharedInformationType sharedInformationTypeId) {
        this.sharedInformationTypeId = sharedInformationTypeId;
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
        if (!(object instanceof SharedInformation)) {
            return false;
        }
        SharedInformation other = (SharedInformation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.SharedInformation[ id=" + id + " ]";
    }
    
}
