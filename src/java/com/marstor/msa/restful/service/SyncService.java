/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.service;


import com.marstor.msa.sync.bean.MarsHost;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.sync.bean.SyncMapping;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zeroz
 */
public class SyncService {
    private MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();
    private String error;
    
    public String getError() {
        return error;
    }
    
    public List<SyncMapping> getAllSyncMapping() {
        List<SyncMapping> mappings = sync.getAllSyncMapping();
        if(mappings == null) {
            this.error = sync.getError();
            return null;
        }
        return mappings;
    }
    
    public List<SyncMapping> getSyncMapping(String srcFileSystem) {
        List<SyncMapping> mappings = sync.getSyncMapping(srcFileSystem);
        if(mappings == null) {
            this.error = sync.getError();
            return null;
        }
        return mappings;
    }
    
    public boolean suspendSync(String srcFileSystem, int iDescFileSystemHashCode, boolean local) {
        List<SyncMapping> mappings = sync.getSyncMapping(srcFileSystem);
        boolean result = false;
        if(mappings == null) {
            this.error = sync.getError();
            return result;
        }
        result = sync.suspendSync(srcFileSystem, iDescFileSystemHashCode, local);
        if(!result) {
            this.error = sync.getError();
        }
        return result;
    }
    
    public boolean abortSync(String descFileSystem){
        boolean result = sync.abortSync(descFileSystem);
        if(!result) {
            this.error = sync.getError();
        }
        return result;
    }
    

//    public boolean deleteSync(String srcFileSystem, int iDescFileSystemHashCode, boolean local) {
//        List<SyncMapping> mappings = sync.getSyncMapping(srcFileSystem);
//        boolean result = false;
//        if(mappings == null) {
//            this.error = sync.getError();
//            return result;
//        }
//        result = sync.deleteSync(srcFileSystem, iDescFileSystemHashCode, local);
//        if(!result) {
//            this.error = sync.getError();
//        }
//        return result;
//    }
    
    
//    关闭后删除
    public boolean closeSync(String srcFileSystem, int iDescFileSystemHashCode, boolean local) {
        List<SyncMapping> mappings = sync.getSyncMapping(srcFileSystem);
        boolean result = false;
        if(mappings == null) {
            this.error = sync.getError();
            return result;
        }
        result = sync.closeSync(srcFileSystem, iDescFileSystemHashCode, local);
        if(!result) {
            this.error = sync.getError();
        }
        return result;
    }
    
    
//    弃用
//    设置完本地或远程同步，自动开启
//    public boolean startSync(String srcFileSystem) {
//        List<SyncMapping> mappings = sync.getSyncMapping(srcFileSystem);
//        boolean result = false;
//        if(mappings == null) {
//            this.error = sync.getError();
//            return result;
//        }
//        boolean local = true;
//        int iDescFileSystemHashCode = mappings.hashCode();
//        result = sync.startSync(srcFileSystem, iDescFileSystemHashCode, local);
//        if(!result) {
//            this.error = sync.getError();
//            return result;
//        }
//        return result;
//    }
    
    public boolean resumeSync(String srcFileSystem, int iDescFileSystemHashCode, boolean local) {
        List<SyncMapping> mappings = sync.getSyncMapping(srcFileSystem);
        boolean result = false;
        if(mappings == null) {
            this.error = sync.getError();
            return result;
        }
        boolean isRollBack = sync.isRollbacking(srcFileSystem);
        result = sync.resumeSync(srcFileSystem, iDescFileSystemHashCode, local, isRollBack);
        if(!result) {
            this.error = sync.getError();
        }
        return result;
    }
    
    public boolean modifySyncTimingTask(String srcFileSystem, int iDescFileSystemHashCode, Calendar jobStartTime, Calendar jobEndTime, boolean bTimingJob, boolean local) {
        List<SyncMapping> mappings = sync.getSyncMapping(srcFileSystem);
        boolean result = false;
        if(mappings == null) {
            this.error = sync.getError();
            return result;
        }
        result = sync.moidfySyncTimingTask(srcFileSystem, iDescFileSystemHashCode, jobStartTime, jobEndTime, bTimingJob, local);
        if(!result) {
            this.error = sync.getError();
        }
        return result;
    }
    
    public boolean deleteAllSnapshot(String fileSystem) {
        boolean result = sync.deleteAllSnapshot(fileSystem, true);
        if(!result) {
            this.error = sync.getError();
            return result;
        }
        return result;
    }
    
    public List<MarsHost> getMarsHost() {
        List<MarsHost> marsHosts = sync.getMarsHost();
        if(marsHosts == null) {
            this.error = sync.getError();
            return null;
        }
        return marsHosts;
    }
 

//   设置后自动开启
    public boolean setLocalSyncInfo(String srcFileSystem, String descFileSystem, Calendar jobStartTime, Calendar jobEndTime, boolean bTimingJob) {
        List<SyncMapping> mappings = sync.getSyncMapping(srcFileSystem);
        boolean result = false;
        if(mappings == null) {
            this.error = sync.getError();
            return result;
        }
        result = sync.setLocalSyncInfo(srcFileSystem, descFileSystem, jobStartTime, jobEndTime, bTimingJob);
        if(!result) {
            this.error = sync.getError();
        }
        return result;
    }
    
    public boolean setRemoteSyncInfo(String ip, String port, String username, String password,
            String rootPassword, String gzipLevel, String srcFileSystem, String descFileSystem, 
            String sshPort, String transferPort, Calendar jobStartTime, Calendar jobEndTime, boolean bTimingJob) {
        
        List<SyncMapping> mappings = sync.getSyncMapping(srcFileSystem);
        boolean result = false;
        if(mappings == null) {
            this.error = sync.getError();
            return result;
        }
        result = sync.setRemoteSyncInfo(ip, port, username, password, rootPassword,
                gzipLevel, srcFileSystem, descFileSystem, sshPort, transferPort, jobStartTime, jobEndTime, bTimingJob);
        if(!result) {
            this.error = sync.getError();
        }
        return result;
    }
    
    public Map<String, List<String>> getTargetServiceFileSystem(String ip, String port, String username, String password) {
        HashMap<String, List<String>> maps= sync.getTargetServerFileSystem(ip, port, username, password);
        if(maps == null) {
            this.error = sync.getError();
            return null;
        }
        return maps;
    }
    
    public String createCDPClone(String snapshotName, Date date) {
        String cloneName = sync.createCDPClone(snapshotName, date);
        if(cloneName == null) {
            this.error = sync.getError();
            return null;
        }
        return cloneName;
    }
    
    public Map<String, String> getCloneOriginMap() {
        Map<String, String> maps = sync.getCloneOriginMap();
        if(maps == null) {
            this.error = sync.getError();
            return null;
        }
        return maps;
    }
    
    public List<Snapshot> getManualSnapshot(String srcFileSystem) {
        List<Snapshot> manualSnapshots = sync.getGetManualSnapshot(srcFileSystem);
        if(manualSnapshots == null) {
            this.error = sync.getError();
            return null;
        }
        return manualSnapshots;
    }
    
    public String createSnapshot(String fileSystem, String snapshotName, String expirationDate) {
        String name = sync.createSnapshot(fileSystem, snapshotName, expirationDate);
        if(name == null ) {
            this.error = sync.getError();
            return null;
        }
        return name;
    }
    
    public boolean deleteCDPClone(String cloneName) {
        boolean bResult = sync.deleteCDPClone(cloneName);
        if(!bResult) {
            this.error = sync.getError();
            return bResult;
        }
        return bResult;
    }
    
    
    
    
    
    
        }
