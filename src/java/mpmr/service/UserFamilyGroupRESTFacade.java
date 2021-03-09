/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.UserFamilyGroupJpaController;
import mpmr.dto.UserFamilyGroup;
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
@Path("userfamilygroup")
public class UserFamilyGroupRESTFacade {

    private UserFamilyGroupJpaController getJpaController() {
        return new UserFamilyGroupJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public UserFamilyGroupRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(UserFamilyGroup entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getId().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response edit(UserFamilyGroup entity) {
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
    public UserFamilyGroup find(@PathParam("id") Integer id) {
        return getJpaController().findUserFamilyGroup(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<UserFamilyGroup> findAll() {
        return getJpaController().findUserFamilyGroupEntities();
    }

    @GET
    @Path("findByUserId/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserFamilyGroup> findByUserId(@PathParam("userId") String userId) {
        return getJpaController().findByUserId(userId);
    }

    @GET
    @Path("findByGroupId/{groupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserFamilyGroup> findByGroupId(@PathParam("groupId") String groupId) {
        return getJpaController().findGroupId(groupId);
    }
    @GET
    @Path("findgroupleader/{groupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserFamilyGroup> findGroupLeader(@PathParam("groupId") String groupId) {
        return getJpaController().findGroupLeader(groupId);
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<UserFamilyGroup> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findUserFamilyGroupEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getUserFamilyGroupCount());
    }

    @GET
    @Path("count/{id}")
    @Secured(Role.user)
    @Produces({MediaType.TEXT_PLAIN})
    public String count(@PathParam("id") String id) {
        return String.valueOf(getJpaController().count(id));
    }

}
