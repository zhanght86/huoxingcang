/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.resources;

import com.marstor.msa.restful.volume.bean.VolumeDisk;
import com.marstor.msa.restful.volume.bean.VolumeDiskRes;
import com.marstor.msa.restful.volume.bean.VolumeGroup;
import com.marstor.msa.restful.volume.bean.VolumeGroupInfo;
import com.marstor.msa.restful.volume.bean.VolumeGroupRes;
import com.marstor.msa.restful.resources.filter.RestfulAuthority;
import com.marstor.msa.restful.service.VolumeService;
import com.marstor.msa.restful.service.ErrorService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Administrator
 */
@Path("volume")
@RestfulAuthority
public class VolumeResources {

    private final VolumeService service = new VolumeService();

    @GET
    @Path("disk")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X GET http://192.168.1.232:80/v1/common/disk -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X GET http://localhost:8080/v1/common/disk -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response getUnusedDisk() {
        
        VolumeDisk[] disks = service.getUnusedDisk();
        Response response;        
        if(disks == null){
            response = ErrorService.errorHandling(service.getError());
        }else{
            VolumeDiskRes disks_res = new VolumeDiskRes(disks);
            response = Response.status(200).entity(disks_res).build();
        }        
        return response;
    }

    @POST
    @Path("volume")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X POST http://localhost:8080/v1/common/volume -H 'Content-Type:application/json' -H 'Accept: application/json' -d ?????????????
    //curl -s -X POST http://localhost:8080/v1/common/volume/yly/yly -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response createVolume(VolumeGroupInfo volume_group) {
        VolumeGroup volume = service.createVolume(volume_group.getVolume_name(), volume_group.getVolume_type(), volume_group.getDisks());
        if(volume ==null){
            return ErrorService.errorHandling(service.getError());
        }
        VolumeGroupRes volume_group_res = new VolumeGroupRes(volume);
        return Response.status(201).entity(volume_group_res).build();
    }
    
    @DELETE
    @Path("volume/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/volume/yly -H 'Content-Type:application/json' -H 'Accept: application/json'??????????
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/volume/yly -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response deleteVolume(@PathParam("name")String name) {
        boolean isDelete = service.deleteVolune(name);
        if(!isDelete){
            return ErrorService.errorHandling(service.getError());
        }
        return Response.status(204).entity("").build();
    }
 

}
