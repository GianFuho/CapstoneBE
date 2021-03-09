package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.FamilyGroup;
import mpmr.dto.UserInfor;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(UserFamilyGroup.class)
public class UserFamilyGroup_ { 

    public static volatile SingularAttribute<UserFamilyGroup, FamilyGroup> familyGroupId;
    public static volatile SingularAttribute<UserFamilyGroup, Integer> id;
    public static volatile SingularAttribute<UserFamilyGroup, UserInfor> userId;
    public static volatile SingularAttribute<UserFamilyGroup, String> groupRole;

}