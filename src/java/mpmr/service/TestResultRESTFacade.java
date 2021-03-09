/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.TestResultJpaController;
import mpmr.dto.TestResult;
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
@Path("testresult")
public class TestResultRESTFacade {

    private TestResultJpaController getJpaController() {
        return new TestResultJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public TestResultRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(TestResult entity) {
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
    public Response edit(TestResult entity) {
        try {
            getJpaController().edit(entity);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
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
    public TestResult find(@PathParam("id") String id) {
        return getJpaController().findTestResult(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestResult> findAll() {
        return getJpaController().findTestResultEntities();
    }

    @GET
    @Secured({Role.user,Role.doctor})
    @Path("findbytestrequestid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public TestResult findByTestRequestId(@PathParam("id") String id) {
        return getJpaController().findByTestRequestId(id);
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestResult> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findTestResultEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getTestResultCount());
    }

}
