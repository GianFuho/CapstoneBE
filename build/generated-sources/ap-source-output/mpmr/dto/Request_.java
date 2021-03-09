package mpmr.dto;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.FamilyGroup;
import mpmr.dto.UserInfor;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(Request.class)
public class Request_ { 

    public static volatile SingularAttribute<Request, FamilyGroup> familyGroupId;
    public static volatile SingularAttribute<Request, Integer> id;
    public static volatile SingularAttribute<Request, Date> dateRequest;
    public static volatile SingularAttribute<Request, UserInfor> userId;
    public static volatile SingularAttribute<Request, String> status;

}