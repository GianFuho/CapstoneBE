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
@Table(name = "User_Family_Group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserFamilyGroup.findAll", query = "SELECT u FROM UserFamilyGroup u")
    , @NamedQuery(name = "UserFamilyGroup.findById", query = "SELECT u FROM UserFamilyGroup u WHERE u.id = :id")
    , @NamedQuery(name = "UserFamilyGroup.findByUserId", query = "SELECT u FROM UserFamilyGroup u inner join u.userId i WHERE i.id = :userId")
    , @NamedQuery(name = "UserFamilyGroup.findCount", query = "SELECT Count(u) FROM UserFamilyGroup u inner join u.familyGroupId i WHERE i.id = :familyGroupId")
    , @NamedQuery(name = "UserFamilyGroup.findGroupId", query = "SELECT u FROM UserFamilyGroup u inner join u.familyGroupId i WHERE i.id = :familyGroupId")
    , @NamedQuery(name = "UserFamilyGroup.findGroupLeader", query = "SELECT u FROM UserFamilyGroup u inner join u.familyGroupId i WHERE i.id = :familyGroupId and u.groupRole = :grouprole")
    , @NamedQuery(name = "UserFamilyGroup.findByGroupRole", query = "SELECT u FROM UserFamilyGroup u WHERE u.groupRole = :groupRole")})
public class UserFamilyGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "Decimal(10,0)")
    private Integer id;
    @Column(name = "Group_Role")
    private String groupRole;
    @JoinColumn(name = "Family_GroupId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private FamilyGroup familyGroupId;
    @JoinColumn(name = "UserId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private UserInfor userId;

    public UserFamilyGroup() {
    }

    public UserFamilyGroup(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupRole() {
        return groupRole;
    }

    public void setGroupRole(String groupRole) {
        this.groupRole = groupRole;
    }

    public FamilyGroup getFamilyGroupId() {
        return familyGroupId;
    }

    public void setFamilyGroupId(FamilyGroup familyGroupId) {
        this.familyGroupId = familyGroupId;
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
        if (!(object instanceof UserFamilyGroup)) {
            return false;
        }
        UserFamilyGroup other = (UserFamilyGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mpmr.UserFamilyGroup[ id=" + id + " ]";
    }

}
