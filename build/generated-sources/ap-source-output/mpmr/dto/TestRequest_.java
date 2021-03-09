package mpmr.dto;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.Examination;
import mpmr.dto.TestResult;
import mpmr.dto.TestTestRequest;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(TestRequest.class)
public class TestRequest_ { 

    public static volatile SingularAttribute<TestRequest, Date> timeStart;
    public static volatile SingularAttribute<TestRequest, String> description;
    public static volatile CollectionAttribute<TestRequest, TestTestRequest> testTestRequestCollection;
    public static volatile SingularAttribute<TestRequest, String> id;
    public static volatile CollectionAttribute<TestRequest, TestResult> testResultCollection;
    public static volatile SingularAttribute<TestRequest, Date> timeFinish;
    public static volatile SingularAttribute<TestRequest, Examination> examinationId;

}