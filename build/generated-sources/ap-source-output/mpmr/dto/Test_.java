package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.PackageTest;
import mpmr.dto.TestResultDetail;
import mpmr.dto.TestResultSample;
import mpmr.dto.TestTestRequest;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(Test.class)
public class Test_ { 

    public static volatile CollectionAttribute<Test, PackageTest> packageTestCollection;
    public static volatile CollectionAttribute<Test, TestResultDetail> testResultDetailCollection;
    public static volatile SingularAttribute<Test, String> name;
    public static volatile SingularAttribute<Test, String> description;
    public static volatile CollectionAttribute<Test, TestTestRequest> testTestRequestCollection;
    public static volatile SingularAttribute<Test, String> id;
    public static volatile CollectionAttribute<Test, TestResultSample> testResultSampleCollection;
    public static volatile SingularAttribute<Test, String> status;

}