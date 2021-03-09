/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.SharedInformationJpaController;
import mpmr.dto.SharedInformation;
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
@Path("sharedinformation")
public class SharedInformationRESTFacade {

    private SharedInformationJpaController getJpaController() {
        return new SharedInformationJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public SharedInformationRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(SharedInformation entity) {
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
    public Response edit(SharedInformation entity) {
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public SharedInformation find(@PathParam("id") Integer id) {
        return getJpaController().findSharedInformation(id);
    }

    @GET
    @Path("findbyhealthid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public SharedInformation findByHealthId(@PathParam("id") String id) {
        return getJpaController().findByHealthId(id);
    }
    @GET
    @Secured({Role.admin, Role.user, Role.doctor, Role.receptionist})
    @Path("findbyhealthfgroupid/{id}/{fGroupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<SharedInformation> findByHealthFGroupId(@PathParam("id") String id,@PathParam("fGroupId") String fGroupId) {
        return getJpaController().findByHealthFGroupId(id,fGroupId);
    }
    @GET
    @Path("findbytype/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<SharedInformation> findByHealthId(@PathParam("id") int id) {
        return getJpaController().findByType(id);
    }
    @GET
    @Path("findbysharedInformationtype/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<SharedInformation> findBySharedInformationType(@PathParam("id") int id) {
        return getJpaController().findBySharedInformationType(id);
    }
    @GET
    @Path("findbysharedinformationcontext/{sharedInformationContext}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<SharedInformation> findBySharedInformationContext(@PathParam("sharedInformationContext") String sharedInformationContext) {
        return getJpaController().findBySharedInformationContext(sharedInformationContext);
    }
    @GET
    @Path("findbycontextfgroupid/{sharedInformationContext}/{fGroupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<SharedInformation> findByContextFGroupId(@PathParam("sharedInformationContext") String sharedInformationContext,@PathParam("fGroupId") String fGroupId) {
        return getJpaController().findByContextFGroupId(sharedInformationContext,fGroupId);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<SharedInformation> findAll() {
        return getJpaController().findSharedInformationEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<SharedInformation> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findSharedInformationEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getSharedInformationCount());
    }

}
