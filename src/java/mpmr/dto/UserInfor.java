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
@Table(name = "UserInfor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserInfor.findAll", query = "SELECT u FROM UserInfor u")
    , @NamedQuery(name = "UserInfor.findById", query = "SELECT u FROM UserInfor u WHERE u.id = :id")
    , @NamedQuery(name = "UserInfor.findByFullname", query = "SELECT u FROM UserInfor u WHERE u.fullname = :fullname")
    , @NamedQuery(name = "UserInfor.findByDob", query = "SELECT u FROM UserInfor u WHERE u.dob = :dob")
    , @NamedQuery(name = "UserInfor.findByAddress", query = "SELECT u FROM UserInfor u WHERE u.address = :address")
    , @NamedQuery(name = "UserInfor.findByPhone", query = "SELECT u FROM UserInfor u inner join u.roleId r WHERE u.phone = :phone and r.id = :roleId")
    , @NamedQuery(name = "UserInfor.findByStatus", query = "SELECT u FROM UserInfor u WHERE u.status = :status")
    , @NamedQuery(name = "UserInfor.findByPassword", query = "SELECT u FROM UserInfor u WHERE u.password = :password")
    , @NamedQuery(name = "UserInfor.findByUsername", query = "SELECT u FROM UserInfor u WHERE u.username = :username")
    , @NamedQuery(name = "UserInfor.findByMail", query = "SELECT u FROM UserInfor u WHERE u.mail = :mail")
    , @NamedQuery(name = "UserInfor.findByGender", query = "SELECT u FROM UserInfor u WHERE u.gender = :gender")
    , @NamedQuery(name = "UserInfor.findByImage", query = "SELECT u FROM UserInfor u WHERE u.image = :image")
    , @NamedQuery(name = "UserInfor.findByToken", query = "SELECT u FROM UserInfor u WHERE u.token = :token")
    , @NamedQuery(name = "UserInfor.count", query = "SELECT COUNT(r) FROM UserInfor r inner join r.roleId ri WHERE ri.id = :roleid")
    , @NamedQuery(name = "UserInfor.countRole", query = "SELECT COUNT(r) FROM UserInfor r inner join r.roleId ri inner join r.clinicId ci WHERE ri.id = :roleid and ci.id = :clinicid ")
    , @NamedQuery(name = "UserInfor.findByRoleId", query = "SELECT u FROM UserInfor u inner join u.roleId r WHERE r.id = :roleid")
    , @NamedQuery(name = "UserInfor.findDoctorByClinicId", query = "SELECT u FROM UserInfor u inner join u.roleId r inner join u.clinicId c  WHERE r.id = :roleid and c.id = :clinicid")
    , @NamedQuery(name = "UserInfor.login", query = "SELECT r FROM UserInfor r WHERE r.username = :username AND r.password = :password")
})
public class UserInfor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private String id;
    @Basic(optional = false)
    @Column(name = "Fullname")
    private String fullname;
    @Basic(optional = false)
    @Column(name = "Dob")
    private String dob;
    @Basic(optional = false)
    @Column(name = "Address")
    private String address;
    @Basic(optional = false)
    @Column(name = "Phone")
    private String phone;
    @Basic(optional = false)
    @Column(name = "Status")
    private String status;
    @Basic(optional = false)
    @Column(name = "Password")
    private String password;
    @Basic(optional = false)
    @Column(name = "Username")
    private String username;
    @Basic(optional = false)
    @Column(name = "Mail")
    private String mail;
    @Basic(optional = false)
    @Column(name = "Gender")
    private int gender;
    @Column(name = "Image")
    private String image;
    @Basic(optional = false)
    @Column(name = "Token")
    private String token;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<UserFamilyGroup> userFamilyGroupCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Rating> ratingCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Request> requestCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Lock> lockCollection;
    @JoinColumn(name = "ClinicId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Clinic clinicId;
    @JoinColumn(name = "RoleId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private RoleUser roleId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Examination> examinationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<HealthRecord> healthRecordCollection;

    public UserInfor() {
    }

    public UserInfor(String id) {
        this.id = id;
    }

    public UserInfor(String id, String fullname, String dob, String address, String phone, String status, String password, String username, String mail, int gender, String token) {
        this.id = id;
        this.fullname = fullname;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.status = status;
        this.password = password;
        this.username = username;
        this.mail = mail;
        this.gender = gender;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @XmlTransient
    public Collection<UserFamilyGroup> getUserFamilyGroupCollection() {
        return userFamilyGroupCollection;
    }

    public void setUserFamilyGroupCollection(Collection<UserFamilyGroup> userFamilyGroupCollection) {
        this.userFamilyGroupCollection = userFamilyGroupCollection;
    }

    @XmlTransient
    public Collection<Rating> getRatingCollection() {
        return ratingCollection;
    }

    public void setRatingCollection(Collection<Rating> ratingCollection) {
        this.ratingCollection = ratingCollection;
    }

    @XmlTransient
    public Collection<Request> getRequestCollection() {
        return requestCollection;
    }

    public void setRequestCollection(Collection<Request> requestCollection) {
        this.requestCollection = requestCollection;
    }

    @XmlTransient
    public Collection<Lock> getLockCollection() {
        return lockCollection;
    }

    public void setLockCollection(Collection<Lock> lockCollection) {
        this.lockCollection = lockCollection;
    }

    public Clinic getClinicId() {
        return clinicId;
    }

    public void setClinicId(Clinic clinicId) {
        this.clinicId = clinicId;
    }

    public RoleUser getRoleId() {
        return roleId;
    }

    public void setRoleId(RoleUser roleId) {
        this.roleId = roleId;
    }

    @XmlTransient
    public Collection<Examination> getExaminationCollection() {
        return examinationCollection;
    }

    public void setExaminationCollection(Collection<Examination> examinationCollection) {
        this.examinationCollection = examinationCollection;
    }

    @XmlTransient
    public Collection<HealthRecord> getHealthRecordCollection() {
        return healthRecordCollection;
    }

    public void setHealthRecordCollection(Collection<HealthRecord> healthRecordCollection) {
        this.healthRecordCollection = healthRecordCollection;
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
        if (!(object instanceof UserInfor)) {
            return false;
        }
        UserInfor other = (UserInfor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.UserInfor[ id=" + id + " ]";
    }

}
