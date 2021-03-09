package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.Examination;
import mpmr.dto.UserInfor;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(Clinic.class)
public class Clinic_ { 

    public static volatile SingularAttribute<Clinic, String> image;
    public static volatile CollectionAttribute<Clinic, Examination> examinationCollection;
    public static volatile SingularAttribute<Clinic, String> address;
    public static volatile SingularAttribute<Clinic, String> coordinate;
    public static volatile SingularAttribute<Clinic, String> phone;
    public static volatile SingularAttribute<Clinic, String> district;
    public static volatile SingularAttribute<Clinic, String> name;
    public static volatile SingularAttribute<Clinic, String> description;
    public static volatile SingularAttribute<Clinic, String> id;
    public static volatile CollectionAttribute<Clinic, UserInfor> userInforCollection;
    public static volatile SingularAttribute<Clinic, String> status;

}