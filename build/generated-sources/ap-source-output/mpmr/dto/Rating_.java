package mpmr.dto;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.Examination;
import mpmr.dto.Lock;
import mpmr.dto.UserInfor;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(Rating.class)
public class Rating_ { 

    public static volatile SingularAttribute<Rating, String> timeExpire;
    public static volatile SingularAttribute<Rating, Float> rate;
    public static volatile SingularAttribute<Rating, String> comment;
    public static volatile SingularAttribute<Rating, String> id;
    public static volatile SingularAttribute<Rating, Date> time;
    public static volatile CollectionAttribute<Rating, Lock> lockCollection;
    public static volatile SingularAttribute<Rating, Examination> examinationId;
    public static volatile SingularAttribute<Rating, UserInfor> userId;
    public static volatile SingularAttribute<Rating, String> status;

}