/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.resources;

import com.marstor.msa.restful.iscsi.bean.AliasRes;
import com.marstor.msa.restful.iscsi.bean.HostGroupInfo;
import com.marstor.msa.restful.iscsi.bean.HostGroupMember;
import com.marstor.msa.restful.iscsi.bean.HostGroupRes;
import com.marstor.msa.restful.iscsi.bean.HostGroupsRes;
import com.marstor.msa.restful.iscsi.bean.ISCSIInitiator;
import com.marstor.msa.restful.iscsi.bean.ISCSIInitiatorRes;
import com.marstor.msa.restful.iscsi.bean.InitiatorChap;
import com.marstor.msa.restful.iscsi.bean.LunsRes;
import com.marstor.msa.restful.iscsi.bean.Property;
import com.marstor.msa.restful.cdp.bean.RemoveView;
import com.marstor.msa.restful.iscsi.bean.Target;
import com.marstor.msa.restful.iscsi.bean.TargetChap;
import com.marstor.msa.restful.iscsi.bean.TargetInfo;
import com.marstor.msa.restful.iscsi.bean.TargetNameRes;
import com.marstor.msa.restful.iscsi.bean.TargetRes;
import com.marstor.msa.restful.iscsi.bean.TargetTPG;
import com.marstor.msa.restful.iscsi.bean.TargetsRes;
import com.marstor.msa.restful.iscsi.bean.View;
import com.marstor.msa.restful.iscsi.bean.ViewInfo;
import com.marstor.msa.restful.iscsi.bean.ViewsRes;
import com.marstor.msa.restful.resources.filter.RestfulAuthority;
import com.marstor.msa.restful.service.ErrorService;
import com.marstor.msa.restful.service.SCSIService;
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
 * @author Administrator
 */
@Path("iscsi")
@RestfulAuthority
public class SCSIResources {

    private final SCSIService service = new SCSIService();

    @GET
    @Path("target")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X GET http://localhost:8080/v1/iscsi/target -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X GET http://localhost:8080/v1/iscsi/target -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response getAllTarget() {
        SCSIService services = new SCSIService();
        Target[] targers = services.getAllTarget();
        Response response;        
        if(targers == null){
            response = ErrorService.errorHandling(service.getError());
        }else{
            TargetsRes targets_res = new TargetsRes(targers);
            response = Response.status(200).entity(targets_res).build();
        }        
        return response;
    }

    @POST
    @Path("target")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X POST http://localhost:8080/v1/iscsi/target -H 'Content-Type:application/json' -H 'Accept: application/json' -d ?????????????
    //curl -s -X POST http://localhost:8080/v1/iscsi/target/yly/yly -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response createiSCSITarget(TargetInfo target_info) {
        Target target = service.createiSCSITarget(target_info.getAlias_name(), target_info.getTarget_name());
        if(target ==null){
            return ErrorService.errorHandling(service.getError());
        }
        TargetRes target_res = new TargetRes(target);
        return Response.status(201).entity(target_res).build();
    }
    
    @DELETE
    @Path("target/{alias_name}/{target_name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/target/yly/yly -H 'Content-Type:application/json' -H 'Accept: application/json'??????????
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/target/yly/yly -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response deleteiSCSITarget(@PathParam("alias_name")String alias_name,@PathParam("target_name") String target_name) {
        boolean isDelete = service.deleteiSCSITarget(alias_name, target_name);
        if(!isDelete){
            return ErrorService.errorHandling(service.getError());
        }
        return Response.status(204).entity("").build();
    }
    
    @GET
    @Path("alias_name")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X GET http://localhost:8080/v1/iscsi/alias_name -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X GET http://localhost:8080/v1/iscsi/alias_name -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response getAvailableAlias(){
        String availableAlias = service.getAvailableAlias();
        if(availableAlias==null){
            return ErrorService.errorHandling(service.getError());
        }
        AliasRes alias_res  = new AliasRes (availableAlias);
        return Response.status(200).entity(alias_res).build();     
    }
    
    @GET
    @Path("target_name")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X GET http://localhost:8080/v1/iscsi/target_name -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X GET http://localhost:8080/v1/iscsi/target_name -H 'Content-Type:application/xml' -H 'Accept: application/xml'
     public Response getAvailableTargetName(){
        String availableTargetName = service.getAvailableTargetName();
        if(availableTargetName==null){
            return ErrorService.errorHandling(service.getError());
        }
        TargetNameRes target_name_res = new TargetNameRes(availableTargetName);
        return Response.status(200).entity(target_name_res).build();  
    }
     
    @POST
    @Path("target_tpg")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X POST http://localhost:8080/v1/iscsi/target_tpg -H 'Content-Type:application/json' -H 'Accept: application/json'?????????????
    //curl -s -X POST http://localhost:8080/v1/iscsi/target_tpg -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response bindTargetTPG(TargetTPG target_tpg) {
        Target target = service.bindTargetTPG(target_tpg.getIp(), target_tpg.getTarget_name());
        if (target == null) {
            return ErrorService.errorHandling(service.getError());
        }
         TargetRes target_res = new TargetRes(target);
        return Response.status(201).entity(target_res).build();
    }
    
    @GET
    @Path("host_group")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X GET http://localhost:8080/v1/iscsi/host_group -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X GET http://localhost:8080/v1/iscsi/host_group -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response getiSCSIHostGroups() {
        HostGroupInfo[] hostGroups = service.getiSCSIHostGroups();
        if(hostGroups == null){
            return ErrorService.errorHandling(service.getError());
        }
        HostGroupsRes host_group_res = new HostGroupsRes(hostGroups);
        return Response.status(200).entity(host_group_res).build();
    }
    
    @POST
    @Path("host_group")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X POST http://localhost:8080/v1/iscsi/host_group -H 'Content-Type:application/json' -H 'Accept: application/json'??????????
    //curl -s -X POST http://localhost:8080/v1/iscsi/host_group -H 'Content-Type:application/xml' -H 'Accept: application/xml'??????????
    public Response createHostGroup(Property property){
       HostGroupInfo hostGroup = service.createHostGroup(property.getName());
        if(hostGroup ==null){
            return ErrorService.errorHandling(service.getError());
        }
        HostGroupRes host_group_res = new HostGroupRes(hostGroup);
        return Response.status(201).entity(host_group_res).build();
    }
    
    @DELETE
    @Path("host_group/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/host_group/yly -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/host_group/yly -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response deleteHostGroup(@PathParam("name") String name){
        boolean isDelete = service.deleteHostGroup(name);
        if(!isDelete){
            return ErrorService.errorHandling(service.getError());
        }
        return Response.status(204).entity("").build();
    }
    
//    @GET
//    @Path("initiator")
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X GET http://localhost:8080/v1/iscsi/initiator -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X GET http://localhost:8080/v1/iscsi/initiator -H 'Content-Type:application/xml' -H 'Accept: application/xml'
//     public Response getiSCSIInitiator(){
//         ISCSIInitiator[] initiators = service.getiSCSIInitiator();
//         if(initiators == null){
//             return ErrorService.errorHandling(service.getError());
//         }
//         ISCSIInitiatorsRes initiators_res = new ISCSIInitiatorsRes(initiators);
//        return Response.status(200).entity(initiators_res).build(); 
//     }
     
//    @POST
//    @Path("initiator")
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Consumes(MediaType.APPLICATION_JSON)
    //curl -s -X POST http://localhost:8080/v1/iscsi/initiator -H 'Content-Type:application/json' -H 'Accept: application/json'???????????
    //curl -s -X POST http://localhost:8080/v1/iscsi/initiator -H 'Content-Type:application/xml' -H 'Accept: application/xml'
//     public Response createiSCSIInitiator(Property property){
//         ISCSIInitiator initiator = service.createiSCSIInitiator(property.name);
//        if(initiator ==null){
//           return ErrorService.errorHandling(service.getError());
//        }
//        
//        ISCSIInitiatorRes initiator_res = new ISCSIInitiatorRes(initiator);
//        return Response.status(200).entity(initiator_res).build();    
//    }
     
//    @DELETE
//    @Path("initiator/{name}")
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Consumes(MediaType.APPLICATION_JSON)
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/initiator/yly -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/initiator/yly -H 'Content-Type:application/xml' -H 'Accept: application/xml'
//     public Response deleteiSCSIInitiator(@PathParam("name") String name){
//       boolean isDelete = service.deleteiSCSIInitiator(name);
//        if(!isDelete){
//            return ErrorService.errorHandling(service.getError());
//        }
//        return Response.status(200).entity("").build();
//    }
     
    @POST
    @Path("host_group_member")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
     //curl -s -X POST http://localhost:8080/v1/iscsi/host_group_member -H 'Content-Type:application/json' -H 'Accept: application/json'???????????
    //curl -s -X POST http://localhost:8080/v1/iscsi/host_group_member -H 'Content-Type:application/xml' -H 'Accept: application/xml'
     public Response addHostGroupMember(HostGroupMember host_group_member_info) {
         
         //获取所有initiator
        ISCSIInitiator initiators[] = service.getiSCSIInitiator();
        for (ISCSIInitiator initiator : initiators) {
            if (initiator.getName().equals(host_group_member_info.getMember())) {
                
                return ErrorService.errorHandling("member name is exit.");
            }
        }
          boolean isCreat = service.createiSCSIInitiator(host_group_member_info.getMember());
        if(!isCreat){
           return ErrorService.errorHandling(service.getError());
        }
  
        HostGroupInfo hostGroup = service.addHostGroupMember(host_group_member_info.getGroup_name(), host_group_member_info.getMember());
        if(hostGroup ==null){
            return ErrorService.errorHandling(service.getError());
        }
        HostGroupRes host_group_res = new HostGroupRes(hostGroup);
        return Response.status(201).entity(host_group_res).build();
    }

    @DELETE
    @Path("host_group_member/{group_name}/{member}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/host_group_member/yly/yly -H 'Content-Type:application/json' -H 'Accept: application/json'????
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/host_group_member/yly/yly -H 'Content-Type:application/xml' -H 'Accept: application/xml'
       public Response removeHostGroupMember(@PathParam("group_name") String group_name,@PathParam("member") String member) {

        boolean isRemove = service.removeHostGroupMember(group_name, member);
        if(!isRemove){
            return ErrorService.errorHandling(service.getError());
        }
        
        boolean isDelete = service.deleteiSCSIInitiator(member);
        if(!isDelete){
            return ErrorService.errorHandling(service.getError());
        }
        return Response.status(204).entity("").build();
        
    }
     
    @PUT
    @Path("target_chap")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
     //curl -s -X PUT http://localhost:8080/v1/iscsi/target_chap -H 'Content-Type:application/json' -H 'Accept: application/json'???????????
    //curl -s -X PUT http://localhost:8080/v1/iscsi/target_chap -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response setTargetChap(TargetChap target_chap) {

        Target target = service.setTargetChap(target_chap.isStat_up(), target_chap.getUser_name(), target_chap.getPassword(), target_chap.getTarget_name());
        if(target ==null){
            return ErrorService.errorHandling(service.getError());
        }
         TargetRes target_res = new TargetRes(target);
        return Response.status(201).entity(target_res).build();
    }
    
    @PUT
    @Path("initiator_chap")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
     //curl -s -X PUT http://localhost:8080/v1/iscsi/initiator_chap -H 'Content-Type:application/json' -H 'Accept: application/json'???????????
    //curl -s -X PUT http://localhost:8080/v1/iscsi/initiator_chap -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response setInitiatorChap(InitiatorChap initiator_chap){
 
        ISCSIInitiator iSCSIInitiator = service.setInitiatorChap(initiator_chap.isStat_up(), initiator_chap.getUser_name(), initiator_chap.getPassword(), initiator_chap.getInitiator_name());
        if(iSCSIInitiator ==null){
            return ErrorService.errorHandling(service.getError());
        }
         ISCSIInitiatorRes initiator_res = new ISCSIInitiatorRes(iSCSIInitiator);
        return Response.status(201).entity(initiator_res).build();
    }
    
    @GET
    @Path("view/{disk_lu}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X GET http://localhost:8080/v1/iscsi/view/yly -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X GET http://localhost:8080/v1/iscsi/view/yly -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response getLUNView(@PathParam("disk_lu") String disk_lu){
         View[] views = service.getLUNView(disk_lu);
         if(views == null){
           return ErrorService.errorHandling(service.getError());
         }
         ViewsRes views_res = new ViewsRes(views);
        return Response.status(200).entity(views_res).build();
        
    }
    
    @POST
    @Path("view")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X POST http://localhost:8080/v1/iscsi/view -H 'Content-Type:application/json' -H 'Accept: application/json'???????????
    //curl -s -X POST http://localhost:8080/v1/iscsi/view -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response addView(ViewInfo view){
        View[] allView = service.addView(view.getHost_group_name(), view.getLun(), view.getLu());
        if(allView == null){
             return ErrorService.errorHandling(service.getError());
         }
         ViewsRes views_res = new ViewsRes(allView);
        return Response.status(201).entity(views_res).build();
    }
    
    
    @DELETE
    @Path("view")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/view -H 'Content-Type:application/json' -H 'Accept: application/json'?????
    //curl -s -X DELETE http://localhost:8080/v1/iscsi/view -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response removeView(RemoveView remove_view){
  
        boolean isRemove = service.removeView(remove_view.getDisk_lu(), remove_view.getLun_mapping());
        if (!isRemove) {
           return ErrorService.errorHandling(service.getError());
        }
        return Response.status(204).entity("").build();
    }
    
    @GET
    @Path("luns/{host_group_name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //curl -s -X GET http://localhost:8080/v1/iscsi/luns/yly -H 'Content-Type:application/json' -H 'Accept: application/json'
    //curl -s -X GET http://localhost:8080/v1/iscsi/luns/yly -H 'Content-Type:application/xml' -H 'Accept: application/xml'
    public Response getHostGroupAvailableLuns(@PathParam("host_group_name") String host_group_name){
        int[] lun = service.getHostGroupAvailableLuns(host_group_name);
        if(lun == null){
            return ErrorService.errorHandling(service.getError());
        }
        LunsRes luns_res = new LunsRes(lun);
        return Response.status(200).entity(luns_res).build();
    }
    
    

}
