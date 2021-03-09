package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.Rating;
import mpmr.dto.UserInfor;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(Lock.class)
public class Lock_ { 

    public static volatile SingularAttribute<Lock, String> reasonLock;
    public static volatile SingularAttribute<Lock, Rating> ratingId;
    public static volatile SingularAttribute<Lock, String> timeLockStart;
    public static volatile SingularAttribute<Lock, String> timeUnlock;
    public static volatile SingularAttribute<Lock, Integer> id;
    public static volatile SingularAttribute<Lock, Integer> rangeTimeLock;
    public static volatile SingularAttribute<Lock, String> adminIdUnlock;
    public static volatile SingularAttribute<Lock, UserInfor> userId;
    public static volatile SingularAttribute<Lock, String> adminIdLock;

}