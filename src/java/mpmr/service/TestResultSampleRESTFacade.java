/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.TestResultSampleJpaController;
import mpmr.dto.TestResultSample;
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
@Path("testresultsample")
public class TestResultSampleRESTFacade {

    private TestResultSampleJpaController getJpaController() {
        return new TestResultSampleJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public TestResultSampleRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(TestResultSample entity) {
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
    public Response edit(TestResultSample entity) {
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
    public TestResultSample find(@PathParam("id") String id) {
        return getJpaController().findTestResultSample(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestResultSample> findAll() {
        return getJpaController().findTestResultSampleEntities();
    }

    @GET
    @Secured({Role.admin, Role.user,Role.doctor,Role.receptionist})
    @Path("findbytestid/{testId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestResultSample> findByTestId(@PathParam("testId") String testId) {
        return getJpaController().findByTestId(testId);
    }

    @GET
    @Secured({Role.user, Role.admin,Role.doctor,Role.receptionist})
    @Path("getall")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestResultSample> getAll() {
        return getJpaController().findAll();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestResultSample> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findTestResultSampleEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getTestResultSampleCount());
    }

}
