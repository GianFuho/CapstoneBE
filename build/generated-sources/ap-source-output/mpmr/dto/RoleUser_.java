package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.UserInfor;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(RoleUser.class)
public class RoleUser_ { 

    public static volatile SingularAttribute<RoleUser, String> name;
    public static volatile SingularAttribute<RoleUser, Integer> id;
    public static volatile CollectionAttribute<RoleUser, UserInfor> userInforCollection;

}