/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.resources;

import com.marstor.msa.restful.cdp.bean.StartupParam;
import com.marstor.msa.restful.cdp.bean.CdpDisk;
import com.marstor.msa.restful.cdp.bean.CdpDiskGroup;
import com.marstor.msa.restful.cdp.bean.CdpRecord;
import com.marstor.msa.restful.cdp.bean.CdpRollbackTask;
import com.marstor.msa.restful.cdp.bean.CdpSnapshot;
import com.marstor.msa.restful.cdp.bean.CreateDiskGroupParam;
import com.marstor.msa.restful.cdp.bean.CreateDiskParam;
import com.marstor.msa.restful.cdp.bean.DiskGroupMappingParam;
import com.marstor.msa.restful.cdp.bean.DiskGroupRes;
import com.marstor.msa.restful.cdp.bean.DiskGroupsRes;
import com.marstor.msa.restful.cdp.bean.DiskRes;
import com.marstor.msa.restful.cdp.bean.HandleRes;
import com.marstor.msa.restful.cdp.bean.MappingParam;
import com.marstor.msa.restful.cdp.bean.MountParam;
import com.marstor.msa.restful.cdp.bean.QuerySnapshotParam;
import com.marstor.msa.restful.cdp.bean.RecordRes;
import com.marstor.msa.restful.cdp.bean.RollbackParam;
import com.marstor.msa.restful.cdp.bean.RollbackStatusParam;
import com.marstor.msa.restful.cdp.bean.RollbackTaskRes;
import com.marstor.msa.restful.cdp.bean.SetLogPolicyParam;
import com.marstor.msa.restful.cdp.bean.SnapshotIDRes;
import com.marstor.msa.restful.cdp.bean.SnapshotRes;
import com.marstor.msa.restful.cdp.bean.UnMountParam;
import com.marstor.msa.restful.resources.filter.RestfulAuthority;
import com.marstor.msa.restful.service.CDPServices;
import com.marstor.msa.restful.service.ErrorService;
import java.util.List;
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
@Path("cdp")
@RestfulAuthority
public class CDPResources {

    @Path("disk_group")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createDiskGroup(final CreateDiskGroupParam param) {
        CDPServices service = new CDPServices();
        CdpDiskGroup group = service.createDiskGroup(param.getVolume_name(), param.getDisk_group_name());
        DiskGroupRes entity = new DiskGroupRes(group);
        Response response;
        if (group != null) {
            response = Response.status(201).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }

    @Path("disk_group/{disk_group_guid}")
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteDiskGroup(@PathParam("disk_group_guid") String diskGroupGuid) {
        CDPServices service = new CDPServices();
        boolean bResult = service.deleteDiskGroup(diskGroupGuid);
        Response response;
        if (!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(204).build();
        }
        return response;
    }

    @Path("disk_group")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDiskGroups() {
        CDPServices service = new CDPServices();
        List<CdpDiskGroup> groups = service.getDiskGroups();
        DiskGroupsRes entity = new DiskGroupsRes(groups);
        Response response;
        if (groups != null) {
            response = Response.status(200).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }

    @Path("disk_group/{disk_group_guid}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDiskGroup(@PathParam("disk_group_guid") String diskGroupGuid) {
        CDPServices service = new CDPServices();
        CdpDiskGroup group = service.getDiskGroup(diskGroupGuid);
        DiskGroupRes entity = new DiskGroupRes(group);
        Response response;
        if (group != null) {
            response = Response.status(200).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }

    @Path("disk")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createDiskJoinGroup(CreateDiskParam param) {
        CDPServices service = new CDPServices();
        CdpDiskGroup group = service.createDiskJoinGroup(param.getDisk_group_guid(), param.getDisk_size(),
                param.isDemand_assignment(), param.getDisk_name());
        DiskGroupRes entity = new DiskGroupRes(group);
        Response response;
        if (group != null) {
            response = Response.status(201).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }

    @Path("disk/{disk_guid}/{disk_group_guid}")
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteDiskFromGroup(@PathParam("disk_guid") String diskGuid, @PathParam("disk_group_guid") String diskGroupGuid) {
        CDPServices service = new CDPServices();
        boolean bResult = service.deleteDiskFromGroup(diskGuid, diskGroupGuid);
        if (!bResult) {
            return ErrorService.errorHandling(service.getError());
        } else {
            return Response.status(204).build();
        }
    }

    @Path("disk/{disk_group_guid}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDisks(@PathParam("disk_group_guid") String diskGroupGuid) {
        CDPServices service = new CDPServices();
        CdpDisk[] disks = service.getDisks(diskGroupGuid);
        DiskRes entity = new DiskRes(disks);
        Response response;
        if (disks != null) {
            response = Response.status(200).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }

    @Path("cdp_status")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response startupAction(StartupParam param) {
        CDPServices service = new CDPServices();
        Response response;
        boolean bResult;
        if (param.getCdp_status().equals("on")) {
            bResult = service.startCDP(param.getDisk_group_guid());
            if (!bResult) {
                response = ErrorService.errorHandling(service.getError());
            } else {
                response = Response.status(201).build();
            }
        } else {
            bResult = service.stopCDP(param.getDisk_group_guid());
            if (!bResult) {
                response = ErrorService.errorHandling(service.getError());
            } else {
                response = Response.status(201).build();
            }
        }
        return response;
    }

    @Path("snapshot_handle/{disk_group_guid}/{snapshot_id}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getQuerySnapshotHandle(@PathParam("disk_group_guid") String diskGroupGuid, @PathParam("snapshot_id") long snapshotID) {
        CDPServices service = new CDPServices();
        int handle = service.getQuerySnapshotHandle(diskGroupGuid, snapshotID);
        HandleRes entity = new HandleRes(handle);
        Response response;
        if (handle != -1) {
            response = Response.status(200).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }

    @Path("snapshot_handle/{query_handle}")
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response closeQuerySnapshotHandle(@PathParam("query_handle") int queryHandle) {
        CDPServices service = new CDPServices();
        boolean bResult = service.closeQuerySnapshotHandle(queryHandle);
        if (!bResult) {
            return ErrorService.errorHandling(service.getError());
        } else {
            return Response.status(204).build();
        }
    }

    @Path("snapshot")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response querySnapshot(QuerySnapshotParam param) {
        CDPServices service = new CDPServices();
        CdpSnapshot[] infos = service.querySnapshotInfos(param.getDisk_group_guid(), param.getQuery_handle(),
                param.getQuery_count(), param.isForward(), param.isReverse(),
                param.getCurrent_count());
        SnapshotRes entity = new SnapshotRes(infos);
        Response response;
        if (infos != null) {
            response = Response.status(200).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }

    @Path("record_handle/{disk_group_guid}/{snapshot_id}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getQueryRecordHandle(@PathParam("disk_group_guid") String diskGroupGuid, @PathParam("snapshot_id") long snapshotID) {
        CDPServices service = new CDPServices();
        int handle = service.getQueryLogRecordHandle(diskGroupGuid, snapshotID);
        HandleRes entity = new HandleRes(handle);
        Response response;
        if (handle != -1) {
            response = Response.status(200).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }

    @Path("record_handle/{query_handle}")
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response closeQueryRecordHandle(@PathParam("query_handle") int queryHandle) {
        CDPServices service = new CDPServices();
        boolean bResult = service.closeQueryLogRecordHandle(queryHandle);
        if (!bResult) {
            return ErrorService.errorHandling(service.getError());
        } else {
            return Response.status(204).build();
        }
    }

    @Path("record")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response queryRecord(QuerySnapshotParam param) {
        CDPServices service = new CDPServices();
        CdpRecord[] infos = service.queryLogRecordInfos(param.getDisk_group_guid(), param.getQuery_handle(),
                param.getQuery_count(), param.isForward(), param.isReverse(),
                param.getCurrent_count());
        RecordRes entity = new RecordRes(infos);
        Response response;
        if (infos != null) {
            response = Response.status(201).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }

    @Path("log_policy")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response setLogPolicy(SetLogPolicyParam param) {
        CDPServices service = new CDPServices();
        boolean bResult = service.setLogPolicy(param.getDisk_group_guid(), param.getLog_size(),
                param.getLog_retention_time(), param.getLog_after_full_strategy(), param.getSnapshot_interval());
        if (!bResult) {
            return ErrorService.errorHandling(service.getError());
        } else {
            return Response.status(201).build();
        }
    }

    @Path("disk_group_mount")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response diskgroupOnline(MountParam param) {
        CDPServices service = new CDPServices();
        Response response;
        boolean bResult = service.online(param.getDisk_group_path());
        if (!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(201).build();
        }

        return response;
    }

    @Path("disk_group_unmount")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response diskgroupOffline(UnMountParam param) {
        CDPServices service = new CDPServices();
        Response response;
        boolean bResult = service.offline(param.getDisk_group_guid());
        if (!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            response = Response.status(201).build();
        }
        return response;
    }

    @Path("mapping_status")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response diskAction(MappingParam param) {
        CDPServices service = new CDPServices();
        Response response;
        boolean bResult;
        if (param.getMapping_status().equals("online")) {
            bResult = service.onlineLU(param.getDisk_group_guid());
            if (!bResult) {
                response = ErrorService.errorHandling(service.getError());
            } else {
                response = Response.status(201).build();
            }
        } else {
            bResult = service.offlineLU(param.getDisk_group_guid());
            if (!bResult) {
                response = ErrorService.errorHandling(service.getError());
            } else {
                response = Response.status(201).build();
            }
        }
        return response;
    }

    @Path("rollback_status")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response rollbackAction(RollbackStatusParam param) {
        CDPServices service = new CDPServices();
        Response response;
        boolean bResult;
        if (param.isSave()) {
            bResult = service.saveRollback(param.getDisk_group_guid());
            if (!bResult) {
                response = ErrorService.errorHandling(service.getError());
            } else {
                response = Response.status(201).build();
            }
        } else {
            bResult = service.cancelRollback(param.getDisk_group_guid());
            if (!bResult) {
                response = ErrorService.errorHandling(service.getError());
            } else {
                response = Response.status(201).build();
            }
        }
        return response;
    }

    @Path("snapshot_action")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response cdpRollback(final RollbackParam param) {
        CDPServices service = new CDPServices();
        Response response;
        boolean bResult = service.cdpRollback(param.getDisk_group_guid(),
                param.getRollback_time());
        if (!bResult) {
            response = ErrorService.errorHandling(service.getError());
        } else {
            return Response.status(202).build();
        }
        return response;
    }

    @Path("rollback_task/{disk_group_guid}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRollbackInfo(@PathParam("disk_group_guid") String diskGroupGuid) {
        CDPServices service = new CDPServices();
        CdpRollbackTask task = service.getRollbackInfo(diskGroupGuid);
        RollbackTaskRes entity = new RollbackTaskRes(task);
        Response response;
        if (task != null) {
            response = Response.status(200).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }

    @Path("mapping_creation")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response mappingDiskGroup(DiskGroupMappingParam param) {
        CDPServices service = new CDPServices();
        boolean bResult = service.mappingDiskGroup(param.getHost_group_name(), param.getDisk_group_guid(), param.getDisks());
        if (!bResult) {
            return ErrorService.errorHandling(service.getError());
        } else {
            return Response.status(201).build();
        }
    }

    @Path("mapping_elimination")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response unmappingDiskGroup(DiskGroupMappingParam param) {
        CDPServices service = new CDPServices();
        boolean bResult = service.unmappingDiskGroup(param.getHost_group_name(), param.getDisk_group_guid(), param.getDisks());
        if (!bResult) {
            return ErrorService.errorHandling(service.getError());
        } else {
            return Response.status(204).build();
        }
    }
    
    @Path("snapshot_id/{disk_group_guid}/{snapshot_time}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAppointedTimeSnapshotID(@PathParam("disk_group_guid") String diskGroupGuid, @PathParam("snapshot_time") long time) {
        CDPServices service = new CDPServices();
        long id = service.getAppointedTimeSnapshotID(diskGroupGuid, time);
        SnapshotIDRes entity = new SnapshotIDRes(id);
        Response response;
        if (id != -2) {
            response = Response.status(200).entity(entity).build();
        } else {
            response = ErrorService.errorHandling(service.getError());
        }
        return response;
    }
}
