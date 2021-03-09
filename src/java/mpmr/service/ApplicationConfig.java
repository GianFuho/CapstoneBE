/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Admin
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(mpmr.service.AuthenticationFilter.class);
        resources.add(mpmr.service.ClinicRESTFacade.class);
        resources.add(mpmr.service.CorsFilter.class);
        resources.add(mpmr.service.DiseaseHealthRecordRESTFacade.class);
        resources.add(mpmr.service.DiseaseRESTFacade.class);
        resources.add(mpmr.service.ExaminationRESTFacade.class);
        resources.add(mpmr.service.FamilyGroupRESTFacade.class);
        resources.add(mpmr.service.HealthRecordRESTFacade.class);
        resources.add(mpmr.service.LockRESTFacade.class);
        resources.add(mpmr.service.PackageRESTFacade.class);
        resources.add(mpmr.service.PackageTestRESTFacade.class);
        resources.add(mpmr.service.RatingRESTFacade.class);
        resources.add(mpmr.service.RequestRESTFacade.class);
        resources.add(mpmr.service.RoleUserRESTFacade.class);
        resources.add(mpmr.service.SharedInformationRESTFacade.class);
        resources.add(mpmr.service.SharedInformationTypeRESTFacade.class);
        resources.add(mpmr.service.TestRESTFacade.class);
        resources.add(mpmr.service.TestRequestRESTFacade.class);
        resources.add(mpmr.service.TestResultDetailRESTFacade.class);
        resources.add(mpmr.service.TestResultRESTFacade.class);
        resources.add(mpmr.service.TestResultSampleRESTFacade.class);
        resources.add(mpmr.service.TestTestRequestRESTFacade.class);
        resources.add(mpmr.service.UserFamilyGroupRESTFacade.class);
        resources.add(mpmr.service.UserInforRESTFacade.class);
    }

}
