package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.Request;
import mpmr.dto.UserFamilyGroup;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(FamilyGroup.class)
public class FamilyGroup_ { 

    public static volatile CollectionAttribute<FamilyGroup, Request> requestCollection;
    public static volatile CollectionAttribute<FamilyGroup, UserFamilyGroup> userFamilyGroupCollection;
    public static volatile SingularAttribute<FamilyGroup, String> name;
    public static volatile SingularAttribute<FamilyGroup, String> id;
    public static volatile SingularAttribute<FamilyGroup, String> avatar;
    public static volatile SingularAttribute<FamilyGroup, String> status;

}