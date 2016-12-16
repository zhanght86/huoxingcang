/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.resources;

import com.marstor.msa.restful.fc.bean.FCInitiator;
import com.marstor.msa.restful.fc.bean.FCInitiatorTarget;
import com.marstor.msa.restful.fc.bean.FCInitiatorTargetRes;
import com.marstor.msa.restful.fc.bean.FCInitiatorsRes;
import com.marstor.msa.restful.iscsi.bean.HostGroupInfo;
import com.marstor.msa.restful.iscsi.bean.HostGroupsRes;
import com.marstor.msa.restful.iscsi.bean.View;
import com.marstor.msa.restful.iscsi.bean.ViewsRes;
import com.marstor.msa.restful.resources.filter.RestfulAuthority;
import com.marstor.msa.restful.service.ErrorService;
import com.marstor.msa.restful.service.FCService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Administrator
 */
@Path("fc")
@RestfulAuthority
public class FCResources {

    private final FCService service = new FCService();

    @GET
    @Path("fc_host_group")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X GET http://localhost:8080/v1/iscsi/host_group -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X GET http://localhost:8080/v1/iscsi/host_group -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response getFCHostGroups() {
        HostGroupInfo[] hostGroups = service.getFCHostGroups();
        if (hostGroups == null) {
            return ErrorService.errorHandling(service.getError());
        }
        HostGroupsRes host_group_res = new HostGroupsRes(hostGroups);
        return Response.status(200).entity(host_group_res).build();
    }

    @GET
    @Path("fc_initiator")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    curl -s -X GET http://localhost:8080/v1/iscsi/initiator -H 'Content-Type:application/json' -H 'Accept: application/json'
//    curl -s -X GET http://localhost:8080/v1/iscsi/initiator -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response getFCInitiator() {
        FCInitiator[] initiators = service.getFCInitiator();
        if (initiators == null) {
            return ErrorService.errorHandling(service.getError());
        }
        FCInitiatorsRes initiators_res = new FCInitiatorsRes(initiators);
        return Response.status(200).entity(initiators_res).build();
    }

    @GET
    @Path("fc_initiator_target/{fc_initiator}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X GET http://localhost:8080/v1/iscsi/view/yly -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X GET http://localhost:8080/v1/iscsi/view/yly -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response getFCInitiatorTarget(@PathParam("fc_initiator") String fc_initiator) {
        FCInitiatorTarget[] views = service.getFCInitiatorTarget(fc_initiator);
        if (views == null) {
            return ErrorService.errorHandling(service.getError());
        }
        FCInitiatorTargetRes views_res = new FCInitiatorTargetRes(views);
        return Response.status(200).entity(views_res).build();
    }

}
