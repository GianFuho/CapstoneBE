package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.Clinic;
import mpmr.dto.Examination;
import mpmr.dto.HealthRecord;
import mpmr.dto.Lock;
import mpmr.dto.Rating;
import mpmr.dto.Request;
import mpmr.dto.RoleUser;
import mpmr.dto.UserFamilyGroup;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(UserInfor.class)
public class UserInfor_ { 

    public static volatile SingularAttribute<UserInfor, String> image;
    public static volatile CollectionAttribute<UserInfor, Examination> examinationCollection;
    public static volatile CollectionAttribute<UserInfor, Request> requestCollection;
    public static volatile SingularAttribute<UserInfor, Clinic> clinicId;
    public static volatile SingularAttribute<UserInfor, String> address;
    public static volatile SingularAttribute<UserInfor, String> mail;
    public static volatile SingularAttribute<UserInfor, Integer> gender;
    public static volatile SingularAttribute<UserInfor, RoleUser> roleId;
    public static volatile CollectionAttribute<UserInfor, Rating> ratingCollection;
    public static volatile SingularAttribute<UserInfor, String> token;
    public static volatile SingularAttribute<UserInfor, String> password;
    public static volatile SingularAttribute<UserInfor, String> phone;
    public static volatile CollectionAttribute<UserInfor, UserFamilyGroup> userFamilyGroupCollection;
    public static volatile SingularAttribute<UserInfor, String> dob;
    public static volatile SingularAttribute<UserInfor, String> id;
    public static volatile SingularAttribute<UserInfor, String> fullname;
    public static volatile CollectionAttribute<UserInfor, Lock> lockCollection;
    public static volatile SingularAttribute<UserInfor, String> status;
    public static volatile SingularAttribute<UserInfor, String> username;
    public static volatile CollectionAttribute<UserInfor, HealthRecord> healthRecordCollection;

}