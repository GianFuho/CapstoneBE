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
@Table(name = "Rating")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rating.findAll", query = "SELECT r FROM Rating r")
    , @NamedQuery(name = "Rating.findById", query = "SELECT r FROM Rating r WHERE r.id = :id")
    , @NamedQuery(name = "Rating.findByComment", query = "SELECT r FROM Rating r WHERE r.comment = :comment")
    , @NamedQuery(name = "Rating.findByRate", query = "SELECT r FROM Rating r WHERE r.rate = :rate")
    , @NamedQuery(name = "Rating.findByTime", query = "SELECT r FROM Rating r WHERE r.time = :time")
    , @NamedQuery(name = "Rating.findByExaminationId", query = "SELECT r FROM Rating r inner join r.examinationId e WHERE e.id = :examinationid")
    , @NamedQuery(name = "Rating.findByClinicId", query = "SELECT r FROM Rating r inner join r.examinationId e inner join e.clinicId c WHERE c.id = :clinicid AND r.status = :status")
    , @NamedQuery(name = "Rating.findByTimeExpire", query = "SELECT r FROM Rating r WHERE r.timeExpire = :timeExpire")
    , @NamedQuery(name = "Rating.findByStatus", query = "SELECT r FROM Rating r WHERE r.status = :status")})
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private String id;
    @Column(name = "Comment")
    private String comment;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Rate")
    private Float rate;
    @Column(name = "Time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Column(name = "TimeExpire")
    private String timeExpire;
    @Basic(optional = false)
    @Column(name = "Status")
    private String status;
    @JoinColumn(name = "ExaminationId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Examination examinationId;
    @JoinColumn(name = "UserId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private UserInfor userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ratingId")
    private Collection<Lock> lockCollection;

    public Rating() {
    }

    public Rating(String id) {
        this.id = id;
    }

    public Rating(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Examination getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(Examination examinationId) {
        this.examinationId = examinationId;
    }

    public UserInfor getUserId() {
        return userId;
    }

    public void setUserId(UserInfor userId) {
        this.userId = userId;
    }

    @XmlTransient
    public Collection<Lock> getLockCollection() {
        return lockCollection;
    }

    public void setLockCollection(Collection<Lock> lockCollection) {
        this.lockCollection = lockCollection;
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
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.Rating[ id=" + id + " ]";
    }

}
