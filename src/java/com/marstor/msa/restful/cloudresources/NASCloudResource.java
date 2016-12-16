/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cloudresources;

import com.marstor.msa.cdp.web.impl.MsaCDPInterfaceImpl;
import com.marstor.msa.common.bean.VolumeGroupInformation;
import com.marstor.msa.nas.bean.SharePath;
import com.marstor.msa.nas.bean.Volume;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.restful.cdp.bean.CdpDiskGroup;
import com.marstor.msa.restful.cdp.bean.CdpSnapshot;
import com.marstor.msa.restful.cdp.cloudbean.BeanResponse;
import com.marstor.msa.restful.cdp.cloudbean.GroupResponse;
import com.marstor.msa.restful.cdp.cloudbean.NameInfo;
import com.marstor.msa.restful.cdp.cloudbean.CDPSnapshotResponse;
import com.marstor.msa.restful.cdp.cloudbean.VolumeName;
import com.marstor.msa.restful.cdp.cloudbean.VolumesResponse;
import com.marstor.msa.restful.nas.cloudbean.NASResponse;
import com.marstor.msa.restful.nas.cloudbean.CreateShareParam;
import com.marstor.msa.restful.nas.cloudbean.NASSnapshotResponse;
import com.marstor.msa.restful.nas.cloudbean.SharePathResponse;
import com.marstor.msa.restful.service.CDPServices;
import com.marstor.msa.restful.service.NASServices;
import com.marstor.msa.restful.service.SyncService;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.sync.web.impl.MsaSYNCInterfaceImpl;
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

/**
 *
 * @author zeroz
 */
@Path("v1")
public class NASCloudResource {

    @Path("webserver/naslistvolumes")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public VolumesResponse getVolumes() {
        NASServices service = new NASServices();

        VolumesResponse response = new VolumesResponse();
        List<VolumeName> volumeNames = new ArrayList<VolumeName>();
        VolumeName volumeName = new VolumeName();

        List<Volume> volumes = service.getVolumes();

        if (volumes != null) {
            response.setCode(0);
            response.setMessage_cn("成功");
            response.setMessage_en("success");

            for (Volume temp : volumes) {
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

    @Path("webserver/listnas/{volumename}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public SharePathResponse ListNAS(@PathParam("volumename") String volumeName, @DefaultValue("1") @QueryParam("protectedtype") int protectedType) {

        NASServices service = new NASServices();

        List<SharePath> paths = service.getAllSharePaths();
        List<NameInfo> shareDirs = new ArrayList<NameInfo>();
        SharePathResponse response = new SharePathResponse();

        if (paths != null) {
            response.setCode(0);
            response.setMessage_cn("成功");
            response.setMessage_en("success");
            response.setVolumename(volumeName);

            if (protectedType == 1) {
                for (SharePath path : paths) {
                    String volume = path.getVolumeName();
                    FileSystemInfo fileSystemInfo = FileSysSnapSYNCInfo.getFileSystemInfo(path.getPath());

                    if (volume.equals(volumeName)) {
                        NameInfo nameInfo = new NameInfo();
                        nameInfo.setName(path.getPath().split("/")[2]);
                        if (fileSystemInfo.getiIsOpen() == 1) {
                            System.out.println("****iIsOpen : " + fileSystemInfo.getiIsOpen());
                            nameInfo.setProtected_type(2);
                        } else {
                            nameInfo.setProtected_type(3);
                        }
                        shareDirs.add(nameInfo);
                    }
                }
                response.setSharedirs(shareDirs);
            } else if (protectedType == 2) {
                for (SharePath path : paths) {
                    String volume = path.getVolumeName();
                    FileSystemInfo fileSystemInfo = FileSysSnapSYNCInfo.getFileSystemInfo(path.getPath());

                    if (volume.equals(volumeName) && fileSystemInfo.getiIsOpen() == 1) {
                        NameInfo nameInfo = new NameInfo();
                        nameInfo.setName(path.getPath().split("/")[2]);
                        nameInfo.setProtected_type(2);
                        shareDirs.add(nameInfo);
                    }
                }
                response.setSharedirs(shareDirs);
            } else if (protectedType == 3) {
                for (SharePath path : paths) {
                    String volume = path.getVolumeName();
                    FileSystemInfo fileSystemInfo = FileSysSnapSYNCInfo.getFileSystemInfo(path.getPath());

                    if (volume.equals(volumeName) && fileSystemInfo.getiIsOpen() != 1) {
                        NameInfo nameInfo = new NameInfo();
                        nameInfo.setName(path.getPath().split("/")[2]);
                        nameInfo.setProtected_type(3);
                        shareDirs.add(nameInfo);
                    }
                }
                response.setSharedirs(shareDirs);
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

    }

    @Path("webserver/listonenas/{volumename}/{nasname}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public NASResponse listOneNAS(@PathParam("volumename") String volumeName, @PathParam("nasname") String nasName) {
        NASServices service = new NASServices();
        String sharePath = volumeName + "/NAS/" + nasName;
        System.out.println("****sharePath : " + sharePath);
        NASResponse response = new NASResponse();
        String status;

        List<SharePath> paths = service.getAllSharePaths();

        for (SharePath path : paths) {
            System.out.println("****group.getDisk_group_name()---" + path.getPath());
            if (path.getPath().equals(sharePath)) {

                FileSystemInfo fileSystemInfo = FileSysSnapSYNCInfo.getFileSystemInfo(sharePath);

                response.setCode(0);
                response.setMessage_cn("成功");
                response.setMessage_en("success");
                response.setName(path.getPath().split("/")[2]);
                response.setZfsname(path.getPath());
                if (fileSystemInfo.getiIsOpen() == 1) {
                    response.setIsprotect(2);
                } else {
                    response.setIsprotect(3);
                }
                return response;
            }
        }
        response.setCode(1);
        response.setMessage_cn("失败");
        response.setMessage_en("fail");
        return response;
    }

    @Path("replic/listnassnapshot/{volumename}/{nasname}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public NASSnapshotResponse getNextSnapshot(@PathParam("volumename") String volumeName, @PathParam("nasname") String nasName, @QueryParam("lastsnapshotname") String lastSnapshotName) {
        NASServices service = new NASServices();
        CDPServices cdpService = new CDPServices();

        NASSnapshotResponse response = new NASSnapshotResponse();
        NASResponse nResponse = listOneNAS(volumeName, nasName);
        List<Snapshot> snapshots = MsaSYNCInterfaceImpl.getInstance().getGetAutoSnapshot(volumeName + "/NAS/" + nasName);

        Snapshot[] snapshotArr = (Snapshot[]) snapshots.toArray(new Snapshot[snapshots.size()]);
        
        System.out.println("snapshotArr" + snapshots.size() + "***" + snapshotArr.length);
        if (snapshots.size() >= 2) {
            if ("0".equals(lastSnapshotName)) {
                response.setSnapshotcount(snapshotArr.length);
                response.setName(nasName);
                response.setZfsname(nResponse.getZfsname());
                response.setIsprotect(nResponse.getIsprotect());
                response.setCreatetime(snapshotArr[1].getCreatedTime());
                response.setSnapshotname(snapshotArr[1].getStrName().split("@")[1]);
                response.setSnapshotfullname(snapshotArr[1].getStrName());
                response.setSnapshotshotsize(snapshotArr[1].getStrUsed());
            } else {
                String fullName = volumeName + "/NAS/" + nasName + "@" + lastSnapshotName;
                System.out.println("fullname : " + fullName);
                for (int i = 0; i < snapshotArr.length; i++) {
                    if (fullName.equals(snapshotArr[i + 1].getStrName())) {
                        response.setSnapshotcount(snapshotArr.length);
                        response.setName(nasName);
                        response.setZfsname(nResponse.getZfsname());
                        response.setIsprotect(nResponse.getIsprotect());
                        response.setCreatetime(snapshotArr[i + 1].getCreatedTime());
                        response.setSnapshotname(snapshotArr[i + 1].getStrName().split("@")[1]);
                        response.setSnapshotfullname(snapshotArr[i + 1].getStrName());
                        response.setSnapshotshotsize(snapshotArr[i + 1].getStrUsed());
                    }
                }
            }
        } else {
            System.out.println("快照为0");
            response.setCode(1);
            response.setMessage_cn("失败");
            response.setMessage_en("fail");
        }
        return response;

//        System.out.println("snapshot size : " + snapshots.size());
//        if (snapshots.size() != 0) {
//            response.setCode(0);
//            response.setMessage_cn("成功");
//            response.setMessage_en("success");
//            response.setSnapshotcount(snapshots.size());
//            response.setName(nasName);
//            response.setZfsname(nResponse.getZfsname());
//            response.setIsprotect(nResponse.getIsprotect());
//            for(Snapshot snapshot: snapshots) {
////                snapshot.
//            }
//            response.setCreatetime();
//        } else {
//            System.out.println("快照为0");
//            response.setCode(1);
//            response.setMessage_cn("失败");
//            response.setMessage_en("fail");
//        }
    }

    @Path("replic/createnas")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public BeanResponse createSharePath(CreateShareParam param) {
        NASServices service = new NASServices();
        BeanResponse response = new BeanResponse();
        if (listOneNAS(param.getVolumename(), param.getNasname()).getCode() != 0) {
            System.out.println("aaa");
            boolean result = service.createSharePath(param.getVolumename(), param.getNasname());

            if (result) {
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
        System.out.println("aaa");
        return response;
    }

    @Path("webservice/nasinfo")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<SharePath> nasInfo() {
        NASServices service = new NASServices();
        List<SharePath> paths = service.getAllSharePaths();

        for (SharePath temp : paths) {
            System.out.println(temp.getPath());
            System.out.println(temp.getVolumeName());
            System.out.println(temp.getCifsShareName());
            System.out.println(temp.getCasesensitivity());
            System.out.println(temp.getAvailable());
            System.out.println(temp.getUsed());
            System.out.println(temp.getMountpoint());
        }

        return paths;
    }

    @Path("webservice/getnasprostatus/{nasname}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public BeanResponse getProStatus(@PathParam("nasname") String path) {

        path = "SYSVOL/NAS/" + path;

        FileSystemInfo info = FileSysSnapSYNCInfo.getFileSystemInfo(path);
        int result = info.getiIsOpen();

        BeanResponse response = new BeanResponse();
        if (result == 1) {
            response.setCode(0);
            response.setMessage_cn("成功");
            response.setMessage_en("success");
            return response;
        }
        response.setCode(1);
        response.setMessage_cn("失败");
        response.setMessage_en("fail");
        return response;
    }
}
