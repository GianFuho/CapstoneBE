/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.PackageTestJpaController;
import mpmr.dto.PackageTest;
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
@Path("packagetest")
public class PackageTestRESTFacade {

    private PackageTestJpaController getJpaController() {
        return new PackageTestJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public PackageTestRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(PackageTest entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getId().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(PackageTest entity) {
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
    public PackageTest find(@PathParam("id") Integer id) {
        return getJpaController().findPackageTest(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<PackageTest> findAll() {
        return getJpaController().findPackageTestEntities();
    }

    @GET
    @Path("getall")
    @Produces({MediaType.APPLICATION_JSON})
    public List<PackageTest> getAll() {
        return getJpaController().findAll();
    }

    @GET
    @Secured(Role.user)
    @Path("findbytestid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public PackageTest findByTestId(@PathParam("id") String id) {
        return getJpaController().findByTestId(id);
    }

    @GET
    @Secured({Role.user,Role.doctor,Role.receptionist})
    @Path("findbypackageid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<PackageTest> findByPackageId(@PathParam("id") String id) {
        return getJpaController().findByPackageId(id);
    }

    @GET
    @Secured({Role.admin, Role.doctor, Role.receptionist})
    @Path("findbyname/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<PackageTest> findByName(@PathParam("name") String name) {
        return getJpaController().findByName(name);
    }

    @GET
    @Secured({Role.user, Role.doctor})
    @Path("findByCount/{count}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<PackageTest> findByCount(@PathParam("count") int count) {
        return getJpaController().findByCount(count);
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<PackageTest> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findPackageTestEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getPackageTestCount());
    }

}
