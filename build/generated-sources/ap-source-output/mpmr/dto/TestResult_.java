package mpmr.dto;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.TestRequest;
import mpmr.dto.TestResultDetail;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(TestResult.class)
public class TestResult_ { 

    public static volatile SingularAttribute<TestResult, String> evaluation;
    public static volatile CollectionAttribute<TestResult, TestResultDetail> testResultDetailCollection;
    public static volatile SingularAttribute<TestResult, String> description;
    public static volatile SingularAttribute<TestResult, String> id;
    public static volatile SingularAttribute<TestResult, Date> timeFinish;
    public static volatile SingularAttribute<TestResult, Date> timeReturn;
    public static volatile SingularAttribute<TestResult, TestRequest> testRequestId;

}