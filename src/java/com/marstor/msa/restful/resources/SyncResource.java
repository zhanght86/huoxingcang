/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.resources;

import com.marstor.msa.restful.sync.bean.CDPCloneParam;
import com.marstor.msa.restful.sync.bean.CloneNameParam;
import com.marstor.msa.restful.sync.bean.CloneOriginMapRes;
import com.marstor.msa.restful.sync.bean.FileSystemParam;
import com.marstor.msa.restful.sync.bean.ListSnapshotRes;
import com.marstor.msa.restful.sync.bean.LocalSyncInfoParam;
import com.marstor.msa.restful.sync.bean.MarsHostRes;
import com.marstor.msa.restful.sync.bean.NameRes;
import com.marstor.msa.restful.sync.bean.RemoteSyncInfoParam;
import com.marstor.msa.restful.sync.bean.SnapshotParam;
import com.marstor.msa.restful.sync.bean.SyncMappingParam;
import com.marstor.msa.restful.sync.bean.SyncMappingRes;
import com.marstor.msa.restful.sync.bean.SyncTimingTaskParam;
import com.marstor.msa.restful.sync.bean.TargetServiceFileSystemRes;
import com.marstor.msa.restful.sync.bean.TargetServiceParam;
import com.marstor.msa.restful.service.ErrorService;
import javax.ws.rs.Path;
import com.marstor.msa.restful.service.SyncService;
import com.marstor.msa.sync.bean.MarsHost;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.sync.bean.SyncMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author zeroz
 */
    
@Path("sync")
public class SyncResource {
    
    @Path("all_sync_mapping")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllSyncMapping() {
        SyncService service = new SyncService();
        Response response;
        List<SyncMapping> mappings = service.getAllSyncMapping();
        SyncMappingRes entity = new SyncMappingRes(mappings);
        if(mappings == null) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(200).entity(entity).build();
        }
        return response;
    }
    
    
    
    @Path("sync_mapping")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSyncMapping(FileSystemParam param) {
        SyncService service = new SyncService();
        Response response;
        List<SyncMapping> mappings = service.getSyncMapping(param.getFile_system());
        SyncMappingRes entity = new SyncMappingRes(mappings);
        
        if(mappings != null) {
            response = Response.status(200).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }
    
    @Path("suspend_sync")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response suspendSync(SyncMappingParam param) {
        SyncService service = new SyncService();
        Response response;
        boolean bResult = service.suspendSync(param.getSrc_file_system(), param.getHash_code(), param.isLocal());
        if(!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(201).build();
        }
        return response;
    }
    
    @Path("abort_sync")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response abortSync(FileSystemParam param) {
        SyncService service = new SyncService();
        Response response;
        boolean bResult = service.abortSync(param.getFile_system());
        if(!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(201).build();
        }  
        return response;
    }
    
//    @Path("delete_sync")
//    @PUT
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response deleteSync(SyncMappingParam param) {
//        SyncService service = new SyncService();
//        Response response;
//        boolean bResult = service.deleteSync(param.getSrc_file_system(), param.getHash_code(), param.isLocal());
//        if(!bResult){
//            response = ErrorService.errorHandling(service.getError());
//        } else {
//            response = Response.status(201).build();
//        }
//        return response;
//    }
    
    @Path("close_sync")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response closeSync(SyncMappingParam param) {
        SyncService service = new SyncService();
        Response response;
        boolean bResult = service.closeSync(param.getSrc_file_system(), param.getHash_code(), param.isLocal());
        if(!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(201).build();
        }
        return response;
    }
    
    @Path("resume_sync")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response resumeSync(SyncMappingParam param) {
        SyncService service = new SyncService();
        Response response;
        boolean bResult = service.resumeSync(param.getSrc_file_system(), param.getHash_code(), param.isLocal());
        if(!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(201).build();
        }
        return response;
    }
    
    @Path("sync_timing_task")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response modifySyncTimingTask(SyncTimingTaskParam param) {
        SyncService service = new SyncService();
        Response response;
        boolean bResult = service.modifySyncTimingTask(param.getSrc_file_system(), 
                param.getHash_code(), param.getJob_start_time(), param.getJob_end_time(), param.isBegin_timing_job(), param.isLocal());
        if(!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(201).build();
        }
        return response;
    }
    
    @Path("delete_all_snapshot")
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteAllSnapshot(FileSystemParam param) {
        SyncService service = new SyncService();
        Response response;
        boolean bResult = service.deleteAllSnapshot(param.getFile_system());
        if(!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(204).build();
        }
        return response;
    }
    
    
//    curl -s -X GET http://192.168.1.232:80/v1/sync/mars_host -H 'Content-Type:application/json' -H 'X-Marstor-Tokens:11cfd3f9d9923afc4f353fe809cfb7ec'
    @Path("mars_host")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getMarsHost() {
        SyncService service = new SyncService();
        Response response;
        List<MarsHost> hosts = service.getMarsHost();
        MarsHostRes entity = new MarsHostRes(hosts);
        if(hosts != null) {
            response = Response.status(200).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }
    
    @Path("local_sync_info")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response setLocalSyncInfo(LocalSyncInfoParam param) {
        SyncService service = new SyncService();
        Response response;
        boolean bResult = service.setLocalSyncInfo(param.getSrc_file_system(), param.getDesc_file_system(), param.getJob_start_time(), param.getJob_end_time(), param.isBegin_timing_job());
        if(!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(201).build();
        }
        return response;
    }
    
    @Path("remote_sync_info")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response setRemoteSyncInfo(RemoteSyncInfoParam param) {
        SyncService service = new SyncService();
        Response response;
        boolean bResult = service.setRemoteSyncInfo(param.getIp(), param.getPort(),
                param.getUsername(), param.getPassword(), param.getRoot_password(),
                param.getGzip_level(), param.getSrc_file_system(), param.getDesc_file_system(),
                param.getSsh_port(), param.getTransfer_port(), param.getJob_start_time(), 
                param.getJob_end_time(), param.isBegin_timing_job());
        if(!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(201).build();
        }
        return response;
    }
    
    @Path("target_service_file_system")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getTargetServiceFileSystem(TargetServiceParam param) {
        SyncService service = new SyncService();
        Response response;
        Map<String, List<String>> maps= service.getTargetServiceFileSystem(param.getIp(), param.getPort(), param.getUsername(), param.getPassword());
        TargetServiceFileSystemRes entity = new TargetServiceFileSystemRes(maps);
        if(maps == null) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(200).entity(entity).build();
        }
        return response;
    }
    
    @Path("create_cdp_clone")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createCDPCLone(CDPCloneParam param) {
        SyncService service = new SyncService();
        Response response;
        String name = service.createCDPClone(param.getSnapshot_name(), param.getDate());
        NameRes entity = new NameRes(name);
        if(name == null) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(201).entity(entity).build();
        }
        return response;
    }
    
    @Path("get_clone_origin_map")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCloneOriginMap() {
        SyncService service = new SyncService();
        Response response;
        Map<String, String> maps = service.getCloneOriginMap();
        CloneOriginMapRes entity = new CloneOriginMapRes(maps);
        if(maps == null) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(200).entity(entity).build();
        }
        return response;
    }
    
    @Path("manual_snapshot")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getManualSnapshot(FileSystemParam param) {
           SyncService service = new SyncService();
           Response response;
           List<Snapshot> snapshot = service.getManualSnapshot(param.getFile_system());
           ListSnapshotRes entity = new ListSnapshotRes(snapshot);
           if(snapshot == null) {
               response = ErrorService.errorHandling(service.getError());
           } else {
               response = Response.status(200).entity(entity).build();
           }
           return response;
    }
    
    @Path("snapshot")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createSnapshot(SnapshotParam param) {
        SyncService service = new SyncService();
        Response response;
        String name = service.createSnapshot(param.getFile_system(), param.getSnapshot_name(), param.getExpiration_date());
        NameRes entity = new NameRes(name);
        if(name == null) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(201).entity(entity).build();
        }
        return response;
    }
    
    @Path("delete_cdp_clone")
    @DELETE
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response deleteCDPClone(CloneNameParam param) {
        SyncService service = new SyncService();
        Response response;
        boolean bResult = service.deleteCDPClone(param.getClone_name());
        if(bResult) {
            response = Response.status(204).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
        
    }
    
    
    
    
    
}
