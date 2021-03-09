/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.TestTestRequestJpaController;
import mpmr.dto.TestTestRequest;
import java.net.URI;
import java.util.List;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Admin
 */
@Path("testtestrequest")
public class TestTestRequestRESTFacade {

    private TestTestRequestJpaController getJpaController() {
        return new TestTestRequestJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public TestTestRequestRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(TestTestRequest entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getId().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Path("edit")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response edit(TestTestRequest entity) {
        try {
            getJpaController().edit(entity);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            getJpaController().destroy(id);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public TestTestRequest find(@PathParam("id") Integer id) {
        return getJpaController().findTestTestRequest(id);
    }

    @GET
    @Secured({Role.user,Role.doctor})
    @Path("testrequestid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestTestRequest> findByTestRequestId(@PathParam("id") String id) {
        return getJpaController().findByTestRequestId(id);
    }

    @GET
    @Secured({Role.user,Role.doctor})
    @Path("findbyexamination/{examinationId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestTestRequest> findByExamination(@PathParam("examinationId") String examinationId) {
        return getJpaController().findByExamination(examinationId);
    }
    @GET
    @Path("findTestRequestIdByCount/{count}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestTestRequest> findTestRequestIdByCount(@PathParam("count") int count) {
        return getJpaController().findTestRequestIdByCount(count);
    }

    @GET
    @Path("findcountbytestrequestid/{id}")
    @Secured({Role.doctor,Role.user})
    @Produces({MediaType.TEXT_PLAIN})
    public String findCountByTestRequestId(@PathParam("id") String id) {
        return String.valueOf(getJpaController().findCountByTestRequestId(id));
    }    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestTestRequest> findAll() {
        return getJpaController().findTestTestRequestEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestTestRequest> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findTestTestRequestEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getTestTestRequestCount());
    }

}
