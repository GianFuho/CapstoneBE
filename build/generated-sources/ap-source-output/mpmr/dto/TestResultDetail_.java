package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.Test;
import mpmr.dto.TestResult;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(TestResultDetail.class)
public class TestResultDetail_ { 

    public static volatile SingularAttribute<TestResultDetail, Float> indexValueResult;
    public static volatile SingularAttribute<TestResultDetail, String> description;
    public static volatile SingularAttribute<TestResultDetail, Test> testId;
    public static volatile SingularAttribute<TestResultDetail, TestResult> testResultId;
    public static volatile SingularAttribute<TestResultDetail, String> id;

}