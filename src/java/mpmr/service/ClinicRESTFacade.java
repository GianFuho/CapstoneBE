/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.ClinicJpaController;
import mpmr.dto.Clinic;
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
@Path("clinic")
public class ClinicRESTFacade {

    private ClinicJpaController getJpaController() {
        return new ClinicJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public ClinicRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(Clinic entity) {
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
    public Response edit(Clinic entity) {
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
    @Secured({Role.user, Role.admin})
    @Path("getall")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Clinic> getAll() {
        return getJpaController().findAll();
    }

    @GET
    @Secured({Role.user, Role.admin})
    @Path("getlikename/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Clinic> findByLikeName(@PathParam("name") String id) {
        return getJpaController().findByLikeName(id);
    }

    @GET
    @Secured({Role.user, Role.admin})
    @Path("findbydistrict/{district}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Clinic> findByDistrict(@PathParam("district") String district) {
        return getJpaController().findByDistrict(district);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Clinic find(@PathParam("id") String id) {
        return getJpaController().findClinic(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Clinic> findAll() {
        return getJpaController().findClinicEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Clinic> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findClinicEntities(max, first);
    }

//    @GET
//    @Path("count")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String count() {
//        return String.valueOf(getJpaController().getClinicCount());
//    }
    @GET
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public String count() {
        return String.valueOf(getJpaController().count());
    }

    @GET
    @Secured(Role.admin)
    @Path("countdistrict/{district}")
    @Produces({MediaType.TEXT_PLAIN})
    public String countInDistrict(@PathParam("district") String district) {
        return String.valueOf(getJpaController().countInDistrict(district));
    }
}
