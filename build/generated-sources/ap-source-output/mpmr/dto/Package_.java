package mpmr.dto;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mpmr.dto.Package;
import mpmr.dto.PackageTest;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-01-27T08:59:35")
@StaticMetamodel(Package.class)
public class Package_ { 

    public static volatile CollectionAttribute<Package, PackageTest> packageTestCollection;
    public static volatile CollectionAttribute<Package, Package> packageCollection;
    public static volatile SingularAttribute<Package, String> name;
    public static volatile SingularAttribute<Package, Package> packageId;
    public static volatile SingularAttribute<Package, String> description;
    public static volatile SingularAttribute<Package, String> id;
    public static volatile SingularAttribute<Package, String> status;

}