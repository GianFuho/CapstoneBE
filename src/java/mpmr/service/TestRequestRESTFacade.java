/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.TestRequestJpaController;
import mpmr.dto.TestRequest;
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
@Path("testrequest")
public class TestRequestRESTFacade {

    private TestRequestJpaController getJpaController() {
        return new TestRequestJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public TestRequestRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(TestRequest entity) {
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
    public Response edit(TestRequest entity) {
        try {
            getJpaController().edit(entity);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @GET
    @Secured({Role.user, Role.doctor})
    @Path("findbyexaminationid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestRequest> findByExaminationId(@PathParam("id") String id) {
        return getJpaController().findByExaminationId(id);
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") String id) {
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
    public TestRequest find(@PathParam("id") String id) {
        return getJpaController().findTestRequest(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestRequest> findAll() {
        return getJpaController().findTestRequestEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestRequest> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findTestRequestEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getTestRequestCount());
    }

}
