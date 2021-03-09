/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.PackageJpaController;
import mpmr.dto.Package;
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
@Path("package")
public class PackageRESTFacade {

    private PackageJpaController getJpaController() {
        return new PackageJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public PackageRESTFacade() {
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(Package entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getId().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(Package entity) {
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
    public Package find(@PathParam("id") String id) {
        return getJpaController().findPackage(id);
    }

    @GET
    @Secured({Role.user, Role.admin, Role.doctor, Role.receptionist})
    @Path("findbypackageid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Package findByPackageId(@PathParam("id") String id) {
        return getJpaController().findByPackageId(id);
    }

    @GET
    @Secured({Role.user, Role.admin, Role.doctor, Role.receptionist})
    @Path("getall")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Package> getAll() {
        return getJpaController().getAll();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Package> findAll() {
        return getJpaController().findPackageEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Package> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findPackageEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getPackageCount());
    }

}
