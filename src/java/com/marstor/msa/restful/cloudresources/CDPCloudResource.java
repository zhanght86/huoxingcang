/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cloudresources;

import com.marstor.msa.cdp.web.impl.MsaCDPInterfaceImpl;
import com.marstor.msa.common.bean.VolumeGroupInformation;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.restful.cdp.bean.CdpDiskGroup;
import com.marstor.msa.restful.cdp.bean.CdpSnapshot;
import com.marstor.msa.restful.cdp.bean.CreateDiskGroupParam;
import com.marstor.msa.restful.cdp.bean.DiskGroupRes;
import com.marstor.msa.restful.cdp.bean.DiskGroupsRes;
import com.marstor.msa.restful.cdp.cloudbean.BeanResponse;
import com.marstor.msa.restful.cdp.cloudbean.CreateGroupParam;
import com.marstor.msa.restful.cdp.cloudbean.NameInfo;
import com.marstor.msa.restful.cdp.cloudbean.GroupResponse;
import com.marstor.msa.restful.cdp.cloudbean.ListGroupsResponse;
import com.marstor.msa.restful.cdp.cloudbean.CDPSnapshotResponse;
import com.marstor.msa.restful.cdp.cloudbean.VolumeName;
import com.marstor.msa.restful.cdp.cloudbean.VolumesResponse;
import com.marstor.msa.restful.service.CDPServices;
import com.marstor.msa.restful.service.ErrorService;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author zeroz
 */
@Path("v1")
public class CDPCloudResource {

    @Path("webserver/cdplistvolumes")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public VolumesResponse ListVolumes() {

        VolumeInterface service = InterfaceFactory.getVolumeInterfaceInstance();
        VolumeGroupInformation[] volumes = service.getAllVolumeGroup();

        VolumesResponse response = new VolumesResponse();
        List<VolumeName> volumeNames = new ArrayList<VolumeName>();
        VolumeName volumeName = new VolumeName();

        if (volumes != null) {
            response.setCode(0);
            response.setMessage_cn("成功");
            response.setMessage_en("success");

            for (VolumeGroupInformation temp : volumes) {
                volumeName.setName(temp.getName());
                volumeNames.add(volumeName);
            }
            response.setVolumes(volumeNames);
            return response;
        }
        response.setCode(1);
        response.setMessage_cn("失败");
        response.setMessage_en("fail");
        return response;
    }

    @Path("webserver/listgroups/{volumename}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ListGroupsResponse listGroups(@PathParam("volumename") String volumeName, @DefaultValue("1") @QueryParam("protectedtype") int protectedType) {

        CDPServices service = new CDPServices();
        ListGroupsResponse response = new ListGroupsResponse();
        List<NameInfo> diskGroupNames = new ArrayList<NameInfo>();

        List<CdpDiskGroup> groups = service.getDiskGroups();

        if (groups != null) {
            response.setCode(0);
            response.setMessage_cn("成功");
            response.setMessage_en("success");
            response.setVolume_name(volumeName);
            if (protectedType == 1) {
                for (CdpDiskGroup group : groups) {
                    String groupPath;
                    groupPath = group.getDisk_group_path();
                    String[] temp = groupPath.split("/");
                    if (temp[1].equals(volumeName)) {
                        NameInfo nameTemp = new NameInfo();
                        nameTemp.setName(group.getDisk_group_name());

                        System.out.println("status : " + group.getDisk_group_path() + group.getCdp_status());

                        if ("on".equals(group.getCdp_status())) {
                            nameTemp.setProtected_type(2);
                        } else {
                            nameTemp.setProtected_type(3);
                        }
                        diskGroupNames.add(nameTemp);
                    }
                }
                System.out.println("*------------start-----------*");
                for (NameInfo b : diskGroupNames) {
                    System.out.println("[" + b.getName() + "]");
                }
                System.out.println("*--------end------------*");

                response.setGroups(diskGroupNames);
            } else if (protectedType == 2) {
                for (CdpDiskGroup group : groups) {
                    if (volumeName.equals(group.getDisk_group_path().split("/")[1]) && "on".equals(group.getCdp_status())) {
                        NameInfo temp = new NameInfo();
                        temp.setName(group.getDisk_group_name());
                        temp.setProtected_type(2);
                        diskGroupNames.add(temp);
                    }
                }
                response.setGroups(diskGroupNames);
            } else if (protectedType == 3) {
                for (CdpDiskGroup group : groups) {
                    if (volumeName.equals(group.getDisk_group_path().split("/")[1]) && "off".equals(group.getCdp_status())) {
                        NameInfo temp = new NameInfo();
                        temp.setName(group.getDisk_group_name());
                        temp.setProtected_type(3);
                        diskGroupNames.add(temp);
                    }
                }
                response.setGroups(diskGroupNames);
            } else {
                System.out.println("输出参数有误");
                response.setCode(1);
                response.setMessage_cn("失败");
                response.setMessage_en("fail");
            }
        } else {
            System.out.println("获取资源为空");
            response.setCode(1);
            response.setMessage_cn("失败");
            response.setMessage_en("fail");
        }
        return response;

//        if (groups != null) {
//            response.setCode(0);
//            response.setMessage_cn("success");
//            response.setMessage_en("success");
//            response.setVolume_name(volumeName);
////        获取全部磁盘组
//            for (CdpDiskGroup group : groups) {
//                String status;
//                String groupPath;
//                groupPath = group.getDisk_group_path();
//                String[] temp = groupPath.split("/");
//                if (temp[1].equals(volumeName)) {
//                    NameInfo nameTemp = new NameInfo();
//                    nameTemp.setName(group.getDisk_group_name());
//
//                    System.out.println("status : " + group.getDisk_group_path() + group.getCdp_status());
//
//                    if ("on".equals(status = group.getCdp_status())) {
//                        nameTemp.setProtected_type(2);
//                    } else {
//                        nameTemp.setProtected_type(3);
//                    }
//                    diskGroupNames.add(nameTemp);
//                }
//            }
//            System.out.println("*------------start-----------*");
//            for (NameInfo b : diskGroupNames) {
//                System.out.println("[" + b.getName() + "]");
//            }
//            System.out.println("*--------end------------*");
//
//            response.setGroups(diskGroupNames);
//            return response;
//        }
//        response.setCode(1);
//        response.setMessage_cn("失败");
//        response.setMessage_en("fail");
//        return response;
    }

    @Path("webserver/listonegroup/{volumename}/{groupname}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public GroupResponse listOneGroup(@PathParam("volumename") String volumeName, @PathParam("groupname") String groupName) {
        CDPServices service = new CDPServices();
        String diskGroupName = "/" + volumeName + "/DISK/" + groupName;
        System.out.println("****diskgroupname : " + diskGroupName);
        GroupResponse response = new GroupResponse();
        String status;

        List<CdpDiskGroup> groups = service.getDiskGroups();

        for (CdpDiskGroup group : groups) {
            System.out.println("****group.getDisk_group_name()---" + group.getDisk_group_path());
            if (group.getDisk_group_path().equals(diskGroupName)) {
                response.setCode(0);
                response.setMessage_cn("成功");
                response.setMessage_en("success");
                response.setName(group.getDisk_group_name());
                response.setUuid(group.getDisk_group_guid());
//               zfsname getDisk_group_path ?
                response.setZfsname(group.getDisk_group_path());
                response.setCount(group.getDisk_count());
                if ("on".equals((status = group.getCdp_status()))) {
                    response.setIsprotect(1);
                }
                return response;
            }
        }
        response.setCode(1);
        response.setMessage_cn("失败");
        response.setMessage_en("fail");
        return response;
    }

    @Path("replic/listcdpsnapshot/{volumename}/{groupname}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public CDPSnapshotResponse getNextSnapshot(@PathParam("volumename") String volumeName, @PathParam("groupname") String groupName, @QueryParam("lastsnapshotid") int lastSnapshotId) {
        CDPServices service = new CDPServices();

        CDPSnapshotResponse response = new CDPSnapshotResponse();
        GroupResponse gResponse = listOneGroup(volumeName, groupName);

        String uuid = gResponse.getUuid();
        System.out.println("uuid : " + uuid);

//        获取句柄
        int handle = 0;
        if (lastSnapshotId == 0) {
            handle = service.getQuerySnapshotHandle(uuid, -1);
        } else {
            handle = service.getQuerySnapshotHandle(uuid, lastSnapshotId);
        }

        if (handle != -1) {

//        获取查询的快照
            CdpSnapshot[] snapshots = service.querySnapshotInfos(uuid, handle, 2, true, false, 5);

            if (snapshots.length >= 2) {
                System.out.println("snapshot LENGTH:" + snapshots.length);
                System.out.println("Firsttt");
                System.out.println(snapshots[0].getSnapshot_name());
                System.out.println("Secondd");
                System.out.println(snapshots[1].getSnapshot_name());

//        获取当前快照时间
                Date startTime = snapshots[0].getSnapshot_time();
//        获取从当前快照起快照数量
                int snapshotCount = MsaCDPInterfaceImpl.getInstance().getSnapshotCount(uuid, startTime, null);

                if (snapshotCount != 0) {
                    response.setCode(0);
                    response.setMessage_cn("成功");
                    response.setMessage_en("success");
                    response.setSnapshotcount(snapshotCount);
                    response.setName(groupName);
                    response.setUuid(uuid);

//                    zfsname getZfsname ?
                    response.setZfsname(gResponse.getZfsname());
                    response.setCount(gResponse.getCount());
                    response.setIsprotect(gResponse.getIsprotect());
                    response.setCreatetime(snapshots[1].getSnapshot_time());
                    response.setSnapshotname(snapshots[1].getSnapshot_name());
                    response.setSnapshotfullname(gResponse.getZfsname() + "@" + snapshots[1].getSnapshot_name());
                    response.setSnapshotshotsize(snapshots[1].getSnapshot_size());
                } else {
                    response.setCode(1);
                    response.setMessage_cn("失败");
                    response.setMessage_en("fail");
                }
            } else {
                System.out.println("快照为0");
                response.setCode(1);
                response.setMessage_cn("失败");
                response.setMessage_en("fail");
            }
        } else {
            System.out.println("句柄错误" + handle);
            response.setCode(1);
            response.setMessage_cn("失败");
            response.setMessage_en("fail");
        }
        return response;
    }

    @Path("replic/creategroup")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public BeanResponse createGroup(final CreateGroupParam param) {
        CDPServices service = new CDPServices();
        BeanResponse response = new BeanResponse();
        if (listOneGroup(param.getVolumename(), param.getGroupname()).getCode() != 0) {
            CdpDiskGroup group = service.createDiskGroup(param.getVolumename(), param.getGroupname());

            if (group != null) {
                response.setCode(0);
                response.setMessage_cn("成功");
                response.setMessage_en("success");
            } else {
                response.setCode(1);
                response.setMessage_cn("失败");
                response.setMessage_en("fail");
            }
        } else {
            System.out.println("has existed");
            response.setCode(1);
            response.setMessage_cn("失败");
            response.setMessage_en("fail");
        }

        return response;
    }

}
