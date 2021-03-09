package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.HealthRecord;
import mpmr.dto.SharedInformationType;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(SharedInformation.class)
public class SharedInformation_ { 

    public static volatile SingularAttribute<SharedInformation, SharedInformationType> sharedInformationTypeId;
    public static volatile SingularAttribute<SharedInformation, String> sharedInformationContext;
    public static volatile SingularAttribute<SharedInformation, String> fGroupId;
    public static volatile SingularAttribute<SharedInformation, HealthRecord> healthRecordId;
    public static volatile SingularAttribute<SharedInformation, Integer> id;

}