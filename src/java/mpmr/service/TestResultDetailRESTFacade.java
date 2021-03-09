/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.TestResultDetailJpaController;
import mpmr.dto.TestResultDetail;
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
@Path("testresultdetail")
public class TestResultDetailRESTFacade {

    private TestResultDetailJpaController getJpaController() {
        return new TestResultDetailJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public TestResultDetailRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(TestResultDetail entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getId().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @GET
    @Secured({Role.user, Role.doctor})
    @Path("findbytestresultid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestResultDetail> findByTestResultId(@PathParam("id") String id) {
        return getJpaController().findByTestResultId(id);
    }

    @GET
    @Secured({Role.user, Role.doctor})
    @Path("findbytestvalue/{testId}/{startValue}/{endValue}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestResultDetail> findByTestValue(@PathParam("testId") String testId, @PathParam("startValue") float startValue, @PathParam("endValue") float endValue) {
        return getJpaController().findByTestValue(testId, startValue, endValue);
    }

    @GET
    @Secured(Role.doctor)
    @Path("findbytestid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestResultDetail> findByTestId(@PathParam("id") String id) {
        return getJpaController().findByTestId(id);
    }

    @GET
    @Secured(Role.admin)
    @Path("findbytestidcheck/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<TestResultDetail> findByTestIdCheck(@PathParam("id") String id) {
        try {
            return getJpaController().findByTestId(id);
        } catch (Exception e) {
            return null;
        }
    }

    @PUT
    @Path("edit")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response edit(TestResultDetail entity) {
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TestResultDetail find(@PathParam("id") String id) {
        return getJpaController().findTestResultDetail(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TestResultDetail> findAll() {
        return getJpaController().findTestResultDetailEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TestResultDetail> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findTestResultDetailEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getTestResultDetailCount());
    }

}
