/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "Examination")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Examination.findAll", query = "SELECT e FROM Examination e")
    , @NamedQuery(name = "Examination.findById", query = "SELECT e FROM Examination e WHERE e.id = :id")
    , @NamedQuery(name = "Examination.findByTimeStart", query = "SELECT e FROM Examination e WHERE e.timeStart = :timeStart")
    , @NamedQuery(name = "Examination.findByTimeFinish", query = "SELECT e FROM Examination e WHERE e.timeFinish = :timeFinish")
    , @NamedQuery(name = "Examination.findByType", query = "SELECT e FROM Examination e WHERE e.type = :type")
    , @NamedQuery(name = "Examination.findByDiagnose", query = "SELECT e FROM Examination e WHERE e.diagnose = :diagnose")
    , @NamedQuery(name = "Examination.findByAdvise", query = "SELECT e FROM Examination e WHERE e.advise = :advise")
    , @NamedQuery(name = "Examination.findByNote", query = "SELECT e FROM Examination e WHERE e.note = :note")
    , @NamedQuery(name = "Examination.findByUserInforId", query = "SELECT e FROM Examination e inner join e.userId u WHERE u.id = :userid AND e.diagnose IS NULL ORDER BY e.timeStart DESC")
    , @NamedQuery(name = "Examination.findByUserInforIdDone", query = "SELECT e FROM Examination e inner join e.userId u WHERE u.id = :userid AND e.diagnose IS NOT NULL ORDER BY e.timeStart DESC")
    , @NamedQuery(name = "Examination.findByDoctorIdDone", query = "SELECT e FROM Examination e WHERE e.doctorId = :doctorId AND e.diagnose IS NOT NULL ORDER BY e.timeStart DESC")
    , @NamedQuery(name = "Examination.findByClinicId", query = "SELECT e FROM Examination e inner join e.clinicId c WHERE c.id = :clinicid")
    , @NamedQuery(name = "Examination.findByTime", query = "SELECT e FROM Examination e inner join e.userId i WHERE e.timeStart >= :startDate AND e.timeStart <= :endDate AND i.id = :userId")
    , @NamedQuery(name = "Examination.findByTimeClinicId", query = "SELECT COUNT(e) FROM Examination e inner join e.clinicId i WHERE e.timeStart >= :startDate AND e.timeStart <= :endDate AND i.id = :clinicid")
    , @NamedQuery(name = "Examination.findByDoctorId", query = "SELECT e FROM Examination e WHERE e.doctorId = :doctorId AND e.diagnose IS NULL ORDER BY e.timeStart ASC")})
public class Examination implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private String id;
    @Basic(optional = false)
    @Column(name = "TimeStart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStart;
    @Basic(optional = false)
    @Column(name = "TimeFinish")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeFinish;
    @Basic(optional = false)
    @Column(name = "Type")
    private String type;
    @Column(name = "Diagnose")
    private String diagnose;
    @Column(name = "Advise")
    private String advise;
    @Column(name = "Note")
    private String note;
    @Basic(optional = false)
    @Column(name = "DoctorId")
    private String doctorId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "examinationId")
    private Collection<Rating> ratingCollection;
    @JoinColumn(name = "ClinicId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Clinic clinicId;
    @JoinColumn(name = "UserId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private UserInfor userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "examinationId")
    private Collection<TestRequest> testRequestCollection;

    public Examination() {
    }

    public Examination(String id) {
        this.id = id;
    }

    public Examination(String id, Date timeStart, Date timeFinish, String type, String doctorId) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeFinish = timeFinish;
        this.type = type;
        this.doctorId = doctorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(Date timeFinish) {
        this.timeFinish = timeFinish;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public String getAdvise() {
        return advise;
    }

    public void setAdvise(String advise) {
        this.advise = advise;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    @XmlTransient
    public Collection<Rating> getRatingCollection() {
        return ratingCollection;
    }

    public void setRatingCollection(Collection<Rating> ratingCollection) {
        this.ratingCollection = ratingCollection;
    }

    public Clinic getClinicId() {
        return clinicId;
    }

    public void setClinicId(Clinic clinicId) {
        this.clinicId = clinicId;
    }

    public UserInfor getUserId() {
        return userId;
    }

    public void setUserId(UserInfor userId) {
        this.userId = userId;
    }

    @XmlTransient
    public Collection<TestRequest> getTestRequestCollection() {
        return testRequestCollection;
    }

    public void setTestRequestCollection(Collection<TestRequest> testRequestCollection) {
        this.testRequestCollection = testRequestCollection;
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
        if (!(object instanceof Examination)) {
            return false;
        }
        Examination other = (Examination) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.Examination[ id=" + id + " ]";
    }

}
