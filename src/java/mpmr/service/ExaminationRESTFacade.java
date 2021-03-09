/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpmr.service;

import mpmr.controller.ExaminationJpaController;
import mpmr.dto.Examination;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@Path("examination")
public class ExaminationRESTFacade {

    private ExaminationJpaController getJpaController() {
        return new ExaminationJpaController(Persistence.createEntityManagerFactory("MPMRCapstonePU"));

    }

    public ExaminationRESTFacade() {
    }

    @POST
    @Path("insert")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(Examination entity) {
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
    public Response edit(Examination entity) {
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
    public Examination find(@PathParam("id") String id) {
        return getJpaController().findExamination(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Examination> findAll() {
        return getJpaController().findExaminationEntities();
    }

    @GET
    @Secured({Role.user, Role.admin, Role.receptionist, Role.doctor})
    @Path("findbyuserinforid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Examination> findByUserInforId(@PathParam("id") String id) {
        return getJpaController().findByUserInforId(id);
    }

    @GET
    @Secured({Role.user, Role.admin, Role.receptionist, Role.doctor})
    @Path("findbyuserinforiddone/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Examination> findByUserInforIdDone(@PathParam("id") String id) {
        return getJpaController().findByUserInforIdDone(id);
    }

    @GET
    @Secured({Role.user, Role.admin, Role.receptionist, Role.doctor})
    @Path("findbydoctoriddone/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Examination> findByDoctorIdDone(@PathParam("id") String id) {
        return getJpaController().findByDoctorIdDone(id);
    }

    @GET
    @Secured({Role.user, Role.admin, Role.receptionist, Role.doctor})
    @Path("findbydoctorid/{doctorId}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Examination> findByDoctorId(@PathParam("doctorId") String doctorId) {
        return getJpaController().findByDoctorId(doctorId);
    }

    @GET
    @Path("{startDate}/{endDate}/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Examination> findByTime(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate, @PathParam("id") String id) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date dBegin = sdf.parse(startDate);
        Date dEnd = sdf.parse(endDate);
        return getJpaController().findByTime(dBegin, dEnd, id);
    }

    @GET
    @Path("month/{startDate}/{endDate}/{id}")
    @Produces({MediaType.TEXT_PLAIN})
    public String findByTimeClinicId(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate, @PathParam("id") String id) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date dBegin = sdf.parse(startDate);
        Date dEnd = sdf.parse(endDate);
        return String.valueOf(getJpaController().findByTimeClinicId(dBegin, dEnd, id));
    }

    @GET
    @Secured({Role.user, Role.doctor, Role.receptionist, Role.admin})
    @Path("findbyclinicid/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Examination> findByClinicId(@PathParam("id") String id) {
        return getJpaController().findByClinicId(id);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(getJpaController().getExaminationCount());
    }

}
