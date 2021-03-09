/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.HealthRecordJpaController;
import mpmr.dto.HealthRecord;
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
@Path("healthrecord")
public class HealthRecordRESTFacade {

    private HealthRecordJpaController getJpaController() {
        return new HealthRecordJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public HealthRecordRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(HealthRecord entity) {
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
    public Response edit(HealthRecord entity) {
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
    public HealthRecord find(@PathParam("id") String id) {
        return getJpaController().findHealthRecord(id);
    }

    @GET
    @Secured(Role.user)
    @Path("findbyuserid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public HealthRecord findByUserId(@PathParam("id") String id) {
        try {
            return getJpaController().findByUserId(id);
        } catch (Exception e) {
            return null;
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<HealthRecord> findAll() {
        return getJpaController().findHealthRecordEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<HealthRecord> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findHealthRecordEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getHealthRecordCount());
    }

}
