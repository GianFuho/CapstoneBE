/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.RatingJpaController;
import mpmr.dto.Rating;
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
@Path("rating")
public class RatingRESTFacade {

    private RatingJpaController getJpaController() {
        return new RatingJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public RatingRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(Rating entity) {
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
    public Response edit(Rating entity) {
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
    public Rating find(@PathParam("id") String id) {
        return getJpaController().findRating(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Rating> findAll() {
        return getJpaController().findRatingEntities();
    }

    @GET
    @Secured(Role.user)
    @Path("findbyexaminationid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Rating findByExaminationId(@PathParam("id") String id) {
        return getJpaController().findByExaminationId(id);
    }

    @GET
    @Secured({Role.user, Role.admin})
    @Path("findbyclinicid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Rating> findByClinicId(@PathParam("id") String id) {
        try {
            List<Rating> rating = getJpaController().findByClinicId(id);
            return rating;
        } catch (Exception e) {
            return null;
        }
    }

    @GET
    @Secured({Role.user,Role.admin})
    @Path("findbystatus/{status}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Rating> findByStatus(@PathParam("status") String status) {
        return getJpaController().findByStatus(status);
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Rating> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findRatingEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getRatingCount());
    }

}
