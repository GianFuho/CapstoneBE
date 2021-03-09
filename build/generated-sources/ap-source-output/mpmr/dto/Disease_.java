package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.DiseaseHealthRecord;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(Disease.class)
public class Disease_ { 

    public static volatile SingularAttribute<Disease, String> name;
    public static volatile SingularAttribute<Disease, String> description;
    public static volatile SingularAttribute<Disease, String> id;
    public static volatile SingularAttribute<Disease, String> status;
    public static volatile CollectionAttribute<Disease, DiseaseHealthRecord> diseaseHealthRecordCollection;

}