package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.DiseaseHealthRecord;
import mpmr.dto.SharedInformation;
import mpmr.dto.UserInfor;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(HealthRecord.class)
public class HealthRecord_ { 

    public static volatile CollectionAttribute<HealthRecord, SharedInformation> sharedInformationCollection;
    public static volatile SingularAttribute<HealthRecord, String> medicalNote;
    public static volatile SingularAttribute<HealthRecord, Float> weight;
    public static volatile SingularAttribute<HealthRecord, String> id;
    public static volatile SingularAttribute<HealthRecord, String> bloodType;
    public static volatile SingularAttribute<HealthRecord, UserInfor> userId;
    public static volatile SingularAttribute<HealthRecord, Float> height;
    public static volatile CollectionAttribute<HealthRecord, DiseaseHealthRecord> diseaseHealthRecordCollection;

}