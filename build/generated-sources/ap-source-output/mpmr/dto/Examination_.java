package mpmr.dto;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.Clinic;
import mpmr.dto.Rating;
import mpmr.dto.TestRequest;
import mpmr.dto.UserInfor;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(Examination.class)
public class Examination_ { 

    public static volatile SingularAttribute<Examination, String> note;
    public static volatile SingularAttribute<Examination, Clinic> clinicId;
    public static volatile SingularAttribute<Examination, Date> timeStart;
    public static volatile SingularAttribute<Examination, String> doctorId;
    public static volatile SingularAttribute<Examination, String> advise;
    public static volatile SingularAttribute<Examination, String> diagnose;
    public static volatile CollectionAttribute<Examination, TestRequest> testRequestCollection;
    public static volatile SingularAttribute<Examination, String> id;
    public static volatile SingularAttribute<Examination, String> type;
    public static volatile SingularAttribute<Examination, Date> timeFinish;
    public static volatile SingularAttribute<Examination, UserInfor> userId;
    public static volatile CollectionAttribute<Examination, Rating> ratingCollection;

}