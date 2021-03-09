package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.Disease;
import mpmr.dto.HealthRecord;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(DiseaseHealthRecord.class)
public class DiseaseHealthRecord_ { 

    public static volatile SingularAttribute<DiseaseHealthRecord, HealthRecord> healthRecordId;
    public static volatile SingularAttribute<DiseaseHealthRecord, Integer> id;
    public static volatile SingularAttribute<DiseaseHealthRecord, Disease> diseaseId;

}