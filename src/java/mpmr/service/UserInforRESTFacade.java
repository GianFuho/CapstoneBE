/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mpmr.controller.UserInforJpaController;
import mpmr.dto.UserInfor;
import java.net.URI;
import java.util.Date;
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
@Path("userinfor")
public class UserInforRESTFacade {

    private UserInforJpaController getJpaController() {
        return new UserInforJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public UserInforRESTFacade() {
    }

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(UserInfor user) throws Exception {
        try {
            UserInfor info = getJpaController().login(user.getUsername(), user.getPassword());
            issueToken(info);
            return Response.ok(info).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private String issueToken(UserInfor info) throws Exception {
        String jwt = Jwts.builder().setId(info.getRoleId().getName()).setSubject(info.getFullname()).setIssuer("giang").setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "flzxsqcysyhljt").compact();
        info.setToken(jwt);
        byte[] b = jwt.getBytes("UTF-8");
        System.out.println(b.length);
        getJpaController().edit(info);
        return jwt;
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(UserInfor entity) {
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
    public Response edit(UserInfor entity) {
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
    public UserInfor find(@PathParam("id") String id) {
        return getJpaController().findUserInfor(id);
    }

    @GET
    @Secured({Role.admin, Role.receptionist, Role.doctor, Role.user})
    @Path("findbyphone/{phone}")
    @Produces({MediaType.APPLICATION_JSON})
    public UserInfor findByPhone(@PathParam("phone") String phone) {
        try {
            return getJpaController().findByPhone(phone);
        } catch (Exception e) {
            return null;
        }

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserInfor> findAll() {
        return getJpaController().findUserInforEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserInfor> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findUserInforEntities(max, first);
    }

    @GET
    @Secured({Role.admin, Role.receptionist, Role.doctor})
    @Path("roleid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserInfor> findByRoleId(@PathParam("id") int id) {
        return getJpaController().findByRoleId(id);
    }

    @GET
    @Secured({Role.admin, Role.user, Role.doctor, Role.receptionist})
    @Path("finddoctorbyclinicid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserInfor> findDoctorByClinicId(@PathParam("id") String id) {
        return getJpaController().findDoctorByClinicId(id);
    }

    @GET
    @Secured({Role.admin, Role.user, Role.doctor, Role.receptionist})
    @Path("findrecepbyclinicid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserInfor> findRecepByClinicId(@PathParam("id") String id) {
        return getJpaController().findRecepByClinicId(id);
    }

    @GET
    @Path("getall")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UserInfor> getAll() {
        return getJpaController().findAll();
    }

//    @GET
//    @Path("count")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String count() {
//        return String.valueOf(getJpaController().getUserInforCount());
//    }
    @GET
    @Path("count/{id}")
    @Secured(Role.admin)
    @Produces({MediaType.TEXT_PLAIN})
    public String count(@PathParam("id") int id) {
        return String.valueOf(getJpaController().count(id));
    }

    @GET
    @Path("count/{id}/{clinicId}")
    @Secured({Role.admin, Role.doctor, Role.receptionist})
    @Produces({MediaType.TEXT_PLAIN})
    public String countRole(@PathParam("id") int id, @PathParam("clinicId") String clinicId) {
        return String.valueOf(getJpaController().countRole(id, clinicId));
    }
}
