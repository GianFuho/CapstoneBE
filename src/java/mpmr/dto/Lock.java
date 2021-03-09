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
@Table(name = "Lock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lock.findAll", query = "SELECT l FROM Lock l")
    , @NamedQuery(name = "Lock.findById", query = "SELECT l FROM Lock l WHERE l.id = :id")
    , @NamedQuery(name = "Lock.findByTimeLockStart", query = "SELECT l FROM Lock l WHERE l.timeLockStart = :timeLockStart")
    , @NamedQuery(name = "Lock.findByRangeTimeLock", query = "SELECT l FROM Lock l WHERE l.rangeTimeLock = :rangeTimeLock")
    , @NamedQuery(name = "Lock.findByTimeUnlock", query = "SELECT l FROM Lock l WHERE l.timeUnlock = :timeUnlock")
    , @NamedQuery(name = "Lock.findByReasonLock", query = "SELECT l FROM Lock l WHERE l.reasonLock = :reasonLock")
    , @NamedQuery(name = "Lock.findByAdminIdLock", query = "SELECT l FROM Lock l WHERE l.adminIdLock = :adminIdLock")
    , @NamedQuery(name = "Lock.findByAdminIdUnlock", query = "SELECT l FROM Lock l WHERE l.adminIdUnlock = :adminIdUnlock")})
public class Lock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "TimeLockStart")
    private String timeLockStart;
    @Column(name = "RangeTimeLock")
    private Integer rangeTimeLock;
    @Column(name = "TimeUnlock")
    private String timeUnlock;
    @Basic(optional = false)
    @Column(name = "ReasonLock")
    private String reasonLock;
    @Basic(optional = false)
    @Column(name = "AdminId_Lock")
    private String adminIdLock;
    @Column(name = "AdminId_Unlock")
    private String adminIdUnlock;
    @JoinColumn(name = "RatingId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Rating ratingId;
    @JoinColumn(name = "UserId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private UserInfor userId;

    public Lock() {
    }

    public Lock(Integer id) {
        this.id = id;
    }

    public Lock(Integer id, String timeLockStart, String reasonLock, String adminIdLock) {
        this.id = id;
        this.timeLockStart = timeLockStart;
        this.reasonLock = reasonLock;
        this.adminIdLock = adminIdLock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimeLockStart() {
        return timeLockStart;
    }

    public void setTimeLockStart(String timeLockStart) {
        this.timeLockStart = timeLockStart;
    }

    public Integer getRangeTimeLock() {
        return rangeTimeLock;
    }

    public void setRangeTimeLock(Integer rangeTimeLock) {
        this.rangeTimeLock = rangeTimeLock;
    }

    public String getTimeUnlock() {
        return timeUnlock;
    }

    public void setTimeUnlock(String timeUnlock) {
        this.timeUnlock = timeUnlock;
    }

    public String getReasonLock() {
        return reasonLock;
    }

    public void setReasonLock(String reasonLock) {
        this.reasonLock = reasonLock;
    }

    public String getAdminIdLock() {
        return adminIdLock;
    }

    public void setAdminIdLock(String adminIdLock) {
        this.adminIdLock = adminIdLock;
    }

    public String getAdminIdUnlock() {
        return adminIdUnlock;
    }

    public void setAdminIdUnlock(String adminIdUnlock) {
        this.adminIdUnlock = adminIdUnlock;
    }

    public Rating getRatingId() {
        return ratingId;
    }

    public void setRatingId(Rating ratingId) {
        this.ratingId = ratingId;
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
        if (!(object instanceof Lock)) {
            return false;
        }
        Lock other = (Lock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.Lock[ id=" + id + " ]";
    }
    
}
