/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.axis2.sync.impl;

import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.axis2.common.impl.AxisLoginServices;
import com.marstor.msa.javadb.SyncDBUtil;
import com.marstor.msa.nas.bean.JoinDomainParameters;
import com.marstor.msa.nas.shell.ImportAndExportManager;
import com.marstor.msa.axis2.sync.model.CloneOrigin;
import com.marstor.msa.axis2.sync.model.TargetHostFileSystem;
import com.marstor.msa.axis2.sync.para.ImportNasUserParameter;
import com.marstor.msa.axis2.sync.para.RollbackSnapshotParameter;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.bean.MarsHost;
import com.marstor.msa.sync.bean.NasBean;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.sync.bean.SnapshotParameter;
import com.marstor.msa.sync.bean.StatusInfo;
import com.marstor.msa.sync.bean.SyncMapping;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.util.ConsoleCommandLine;
import com.marstor.xml.XMLConstructor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.databinding.utils.BeanUtil;

/**
 *
 * @author Administrator
 */
public class AxisSyncInterfaceImpl {

    private final MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();
    private static String error;

    private void setError(String s) {
        error = s;
    }

    public String getError() {
        return error;
    }

    public int login(String username,String password) {
        System.out.println("AxisSync login");
        AxisLoginServices loginService = new AxisLoginServices();
        return loginService.login(username, password);
    }
    
    public String createSnapshot(String fileSystem, String snapshotName, String expirationDate) {
        String name = sync.createSnapshot(fileSystem, snapshotName, expirationDate);
        if (name == null) {
            this.setError(sync.getError());
            return null;
        }
        return name;
    }

    public boolean destroySnapshot(String snapshotName) {
        boolean result = sync.destroySnapshot(snapshotName);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean rollbackSnapshot(OMElement element) {
        try {
            RollbackSnapshotParameter para = SyncServerUtil.getResquestObject(element, RollbackSnapshotParameter.class);
            if (para == null) {
                this.setError("parser object failed");
                return false;
            }
            String srcFileSystem = para.getSrcFileSystem();
            String lastSnapshot = para.getLastSnapshot();
            String rollbackSnapshot = para.getRollbackSnapshot();
//            List<String> listSnapshot = Arrays.asList(para.getSnapshots());
            List<String> listSnapshot = new ArrayList();
            String [] snapshots = para.getSnapshots();
            listSnapshot.addAll(Arrays.asList(snapshots));
            boolean result = sync.rollbackSnapshot(srcFileSystem, lastSnapshot, rollbackSnapshot, listSnapshot);
            Debug.print("rollbackSnapshot result = " + result);
            if (!result) {
                this.setError(sync.getError());
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cancelRollback(String srcFileSystem) {
        boolean result = sync.cancelRollback(srcFileSystem);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean saveCurrentStatus(String srcFileSystem) {
        boolean result = sync.saveCurrentStatus(srcFileSystem);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public OMElement getFileSystemInfos() {
        List<FileSystemInfo> fileSystemInfos = sync.getFileSystemInfo();
        if (fileSystemInfos == null) {
            this.setError(sync.getError());
            return null;
        }
        FileSystemInfo[] fileSystemInfoArray = new FileSystemInfo[fileSystemInfos.size()];
        fileSystemInfos.toArray(fileSystemInfoArray);
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                fileSystemInfoArray, new QName("FileSystemInfo"), false, null);
        return element;
    }

    public boolean createTimingSnapshot(String srcFileSystem, int timeInterval, int maxNum) {
        boolean result = sync.createTimingSnapshot(srcFileSystem, timeInterval, maxNum);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean cancelTimingSnapshot(String srcFileSystem, boolean deleteAutoSnapshot) {
        boolean result = sync.cancelTimingSnapshot(srcFileSystem, deleteAutoSnapshot);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public String getServerCfg() {
        int[] cfgs = sync.getServerCfg();
        StringBuilder sb = new StringBuilder(9);
        for (int i = 0; i < cfgs.length; i++) {
            sb.append(i);
            if (i < cfgs.length - 1) {
                sb.append(",");
            }
        }
        String cfg = sb.toString();
        return cfg;
    }

    public OMElement getGetManualSnapshot(String srcFileSystem) {
        List<Snapshot> snapshots = sync.getGetManualSnapshot(srcFileSystem);
        if (snapshots == null) {
            this.setError(sync.getError());
            return null;
        }
        Snapshot[] snapshotArray = new Snapshot[snapshots.size()];
        snapshots.toArray(snapshotArray);
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                snapshotArray, new QName("Snapshot"), false, null);
        return element;
    }

    public OMElement getGetAutoSnapshot(String srcFileSystem) {
        List<Snapshot> snapshots = sync.getGetAutoSnapshot(srcFileSystem);
        if (snapshots == null) {
            this.setError(sync.getError());
            return null;
        }
        Snapshot[] snapshotArray = new Snapshot[snapshots.size()];
        snapshots.toArray(snapshotArray);
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                snapshotArray, new QName("Snapshot"), false, null);
        return element;
    }

    public OMElement getSnapshotListAfterRollback(String fileSystem) {
        List<String> snapshots = sync.getSnapshotListAfterRollback(fileSystem);
        if (snapshots == null) {
            this.setError(sync.getError());
            return null;
        }
        String[] snapshotArray = new String[snapshots.size()];
        snapshots.toArray(snapshotArray);
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                snapshotArray, new QName("Snapshot"), false, null);
        return element;
    }

    public OMElement getTargetServerFileSystem(String ip, String port, String username, String password) {
        HashMap<String, List<String>> map = sync.getTargetServerFileSystem(ip, port, username,password);
        TargetHostFileSystem targetHostFileSystem = new TargetHostFileSystem();
        List<String> pools = map.get("Volume");
        List<String> fileSystems = map.get("FileSystem");
        String[] poolArray = new String[pools.size()];
        String[] fileSystemArray = new String[fileSystems.size()];
        pools.toArray(poolArray);
        fileSystems.toArray(fileSystemArray);
        targetHostFileSystem.setPools(poolArray);
        targetHostFileSystem.setFileSystems(fileSystemArray);
        return SyncServerUtil.generateResponseObject(targetHostFileSystem);
    }

    public OMElement getLocalServerFileSystem(String authname) {
        HashMap<String, List<String>> map = sync.getLocalServerFileSystem_auth(authname);
        TargetHostFileSystem targetHostFileSystem = new TargetHostFileSystem();
        List<String> pools = map.get("Volume");
        List<String> fileSystems = map.get("FileSystem");
        String[] poolArray = new String[pools.size()];
        String[] fileSystemArray = new String[fileSystems.size()];
        pools.toArray(poolArray);
        fileSystems.toArray(fileSystemArray);
        targetHostFileSystem.setPools(poolArray);
        targetHostFileSystem.setFileSystems(fileSystemArray);
        return SyncServerUtil.generateResponseObject(targetHostFileSystem);
    }

    public boolean setLocalSyncInfo(String srcFileSystem, String descFileSystem, String jobStartTime, String jobEndTime, boolean bTimingJob) {
        Calendar calendarStart = SyncServerUtil.convertStringToCalendar(jobStartTime);
        Calendar calendarEnd = SyncServerUtil.convertStringToCalendar(jobEndTime);
        boolean result = sync.setLocalSyncInfo(srcFileSystem, descFileSystem, calendarStart, calendarEnd, bTimingJob);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean setRemoteSyncInfo(String ip, int port, String username, String password, String rootPassword, String gzipLevel,
            String srcFileSystem, String descFileSystem, String sshPort, String transferPort,
            String jobStartTime, String jobEndTime, boolean bTimingJob) {
        Calendar calendarStart = SyncServerUtil.convertStringToCalendar(jobStartTime);
        Calendar calendarEnd = SyncServerUtil.convertStringToCalendar(jobEndTime);
        boolean result = sync.setRemoteSyncInfo(ip, String.valueOf(port), username, password, rootPassword, gzipLevel, srcFileSystem, descFileSystem, sshPort, transferPort, calendarStart, calendarEnd, bTimingJob);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean startSync(String srcFileSystem, int iFileSystemHashCode, boolean local) {
        boolean result = sync.startSync(srcFileSystem, iFileSystemHashCode, local);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean closeSync(String srcFileSystem, int iFileSystemHashCode, boolean local) {
        boolean result = sync.closeSync(srcFileSystem, iFileSystemHashCode, local);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean suspendSync(String srcFileSystem, int iFileSystemHashCode, boolean local) {
        boolean result = sync.suspendSync(srcFileSystem, iFileSystemHashCode, local);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean resumeSync(String srcFileSystem, int iFileSystemHashCode, boolean local, boolean rollback) {
        boolean result = sync.resumeSync(srcFileSystem, iFileSystemHashCode, local, rollback);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean abortSync(String descFileSystem) {
        boolean result = sync.abortSync(descFileSystem);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean deleteSync(String srcFileSystem, int iFileSystemHashCode, boolean local) {
        boolean result = sync.deleteSync(srcFileSystem, iFileSystemHashCode, local);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public String getLocalFileSystemStatus(String descFileSystem) {
        String[] status = sync.getLocalFileSystemStatus(descFileSystem);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < status.length; i++) {
            sb.append(status[i]);
            if (i < status.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public String getRemoteFileSystemStatus(String descFileSystem, String ip, String port, String username, String password) {
        String[] status = sync.getRemoteFileSystemStatus(descFileSystem, ip, port, username, password);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < status.length; i++) {
            sb.append(status[i]);
            if (i < status.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public boolean getFileSystemSyncStatus(String srcFileSystem) {
        boolean result = sync.getFileSystemSyncStatus(srcFileSystem);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean importNasUser(OMElement element) {
        ImportNasUserParameter para = SyncServerUtil.getResquestObject(element, ImportNasUserParameter.class);
        if (para == null) {
            this.setError("parser object failed");
            return false;
        }
        NasBean nasBean = para.getNasBean();
        String ip = para.getIp();
        String port = para.getPort();
        String user = para.getUser();
        String pwd = para.getPwd();
        boolean result = sync.importNasUser(nasBean, ip, port, user, pwd);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public OMElement getSyncMapping(String srcFileSystem) {
        List<SyncMapping> mappings = sync.getSyncMapping(srcFileSystem);
        if (mappings == null) {
            setError(sync.getError());
            return null;
        }
        com.marstor.msa.axis2.sync.model.SyncMapping[] mappings_axis = new com.marstor.msa.axis2.sync.model.SyncMapping[mappings.size()];
        for (int i = 0; i < mappings.size(); i++) {
            SyncMapping mapping = mappings.get(i);
            com.marstor.msa.axis2.sync.model.SyncMapping mapping_axis = SyncServerUtil.convertSyncMapping(mapping);
            mappings_axis[i] = mapping_axis;
        }
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                mappings_axis, new QName("SyncMapping"), false, null);
        return element;
    }

    public OMElement getAllSyncMapping() {
        List<SyncMapping> mappings = sync.getAllSyncMapping();
        if (mappings == null) {
            setError(sync.getError());
            return null;
        }
        com.marstor.msa.axis2.sync.model.SyncMapping[] mappings_axis = new com.marstor.msa.axis2.sync.model.SyncMapping[mappings.size()];
        for (int i = 0; i < mappings.size(); i++) {
            SyncMapping mapping = mappings.get(i);
            com.marstor.msa.axis2.sync.model.SyncMapping mapping_axis = SyncServerUtil.convertSyncMapping(mapping);
            mappings_axis[i] = mapping_axis;
        }
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                mappings_axis, new QName("SyncMapping"), false, null);
        return element;
    }

    public String getSyncCount() {
        int[] count = sync.getSyncCount();
        return count[0] + "," + count[1];
    }

    public OMElement getMarsHost() {
        List<MarsHost> marsHosts = sync.getMarsHost();
        if (marsHosts == null) {
            setError(sync.getError());
            return null;
        }
        MarsHost[] marsHostArray = new MarsHost[marsHosts.size()];
        marsHosts.toArray(marsHostArray);
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                marsHostArray, new QName("MarsHost"), false, null);
        return element;
    }

    public boolean addMarsHost(OMElement element) {
        MarsHost marsHost = SyncServerUtil.getResquestObject(element, MarsHost.class);
        if (marsHost == null) {
            this.setError("parser object failed");
            return false;
        }
        boolean result = sync.addMarsHost(marsHost);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean deleteMarsHost(OMElement element) {
        MarsHost marsHost = SyncServerUtil.getResquestObject(element, MarsHost.class);
        if (marsHost == null) {
            this.setError("parser object failed");
            return false;
        }
        boolean result = sync.deleteMarsHost(marsHost);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean modifyMarsHost(String ip, OMElement element) {
        MarsHost marsHost = SyncServerUtil.getResquestObject(element, MarsHost.class);
        if (marsHost == null) {
            this.setError("parser object failed");
            return false;
        }
        boolean result = sync.modifyMarsHost(ip, marsHost);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public OMElement getConfigMarsHostIP() {
        List<String> ips = sync.getConfigMarsHostIP();
        if (ips == null) {
            setError(sync.getError());
            return null;
        }
        String[] ipArray = new String[ips.size()];
        ips.toArray(ipArray);
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                ipArray, new QName("IP"), false, null);
        return element;
    }

    public boolean isRollbacking(String fileSystem) {
        boolean result = sync.isRollbacking(fileSystem);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean setSyncMaxCount(int count) {
        boolean result = sync.setSyncMaxCount(count);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean setTransferPort(int port) {
        boolean result = sync.setTransferPort(port);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public String createClone(String snapshotName) {
        String name = sync.createClone(snapshotName);
        if (name == null) {
            this.setError(sync.getError());
            return null;
        }
        return name;
    }

    public boolean deleteClone(String cloneName) {
        boolean result = sync.deleteClone(cloneName);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean deleteCDPClone(String cloneName) {
        boolean result = sync.deleteCDPClone(cloneName);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean moidfySyncTimingTask(String srcFileSystem, int iFileSystemHashCode,
            String jobStartTime, String jobEndTime, boolean bTimingJob, boolean local) {
        Calendar calendarStart = SyncServerUtil.convertStringToCalendar(jobStartTime);
        Calendar calendarEnd = SyncServerUtil.convertStringToCalendar(jobEndTime);
        boolean result = sync.moidfySyncTimingTask(srcFileSystem, iFileSystemHashCode, calendarStart, calendarEnd, bTimingJob, local);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public OMElement getAllFileSystemStatus() {
        Map<String, StatusInfo> map = sync.getAllFileSystemStatus();
        if (map == null) {
            this.setError(sync.getError());
            return null;
        }
        Collection c = map.values();
        StatusInfo[] statusInfos = new StatusInfo[c.size()];
        c.toArray(statusInfos);
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                statusInfos, new QName("StatusInfo"), false, null);
        return element;
    }

    public OMElement getCloneOriginMap() {
        Map<String, String> map = sync.getCloneOriginMap();
        if (map == null) {
            this.setError(sync.getError());
            return null;
        }
        Set<String> keys = map.keySet();
        Iterator<String> iter = keys.iterator();
        CloneOrigin[] origins = new CloneOrigin[keys.size()];
        int index = 0;
        while (iter.hasNext()) {
            String key = iter.next();
            CloneOrigin origin = new CloneOrigin();
            origin.setClone(key);
            origin.setSnapshot(map.get(key));
            origins[index] = origin;
            index++;
        }
        OMElement element = BeanUtil.getOMElement(new QName("root"),
                origins, new QName("CloneOrigin"), false, null);
        return element;
    }

    public boolean configSnapshotProtect(String srcFileSystem, int timeInterval, int maxNum) {
        boolean result = sync.configSnapshotProtect(srcFileSystem, timeInterval, maxNum);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean deleteAllSnapshot(String fileSystem, boolean bDeleteClone) {
        boolean result = sync.deleteAllSnapshot(fileSystem, bDeleteClone);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean deleteAllSnapshot(String fileSystem) {
        boolean result = sync.deleteAllSnapshot(fileSystem);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public OMElement getFileSystemInfo(String fileSystem) {
        FileSystemInfo info = sync.getFileSystemInfo(fileSystem);
        if (info == null) {
            this.setError(sync.getError());
            return null;
        }
        return SyncServerUtil.generateResponseObject(info);
    }

    public boolean clearRollbackFlag(String fileSystem) {
        boolean result = sync.saveCurrentStatus(fileSystem);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }

    public boolean recvSnapshot(String fileSystem) {
        boolean result = sync.recvSnapshot(fileSystem);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }
    
    public boolean rollbackLocalSnapshot(String snapshot) {
        boolean result = sync.rollbackLocalSnapshot(snapshot);
        if (!result) {
            this.setError(sync.getError());
            return false;
        }
        return true;
    }
    
    public void dbRoot(String fileSystem) {
        try {
            FileWriter fw = null;
            BufferedWriter bufw = null;

            String basename = "/" + fileSystem + "/db";
            String base = "/" + fileSystem + "/zfs";
            SyncDBUtil dbu = new SyncDBUtil();
            List<Snapshot> snaplist = dbu.querySnapshot(fileSystem);
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&ddddddddddddddd1111&&&&&&&&&&&&");
            fw = new FileWriter(basename);
            /**
             * 为了提高写入的效率，使用了字符流的缓冲区。 创建了一个字符写入流的缓冲区对象，并和指定要被缓冲的流对象相关联。
             */
            bufw = new BufferedWriter(fw);
             bufw.newLine();
            //使用缓冲区中的方法将数据写入到缓冲区中。
            int num = 0;
            if (snaplist != null && snaplist.size() > 0) {
                for (int i = (snaplist.size() - 1); i > -1; i--) {
                    bufw.write(snaplist.get(i).getStrName() + "," + snaplist.get(i).getStrUsed() + "\n");
                    num++;
                    if (num == 1000) {
                        num = 0;
                        bufw.flush();
                    }
                }
            }
            //使用缓冲区中的方法，将数据刷新到目的地文件中去。
            bufw.flush();
            //关闭缓冲区,同时关闭了fw流对象
            bufw.close();

            String command = "zfs list -t snapshot -r -H -o name,written  " + fileSystem;
            ConsoleCommandLine consoleCommandLine = new ConsoleCommandLine();
            String[] temp;
            if (consoleCommandLine.executeCmdLine(command) != ConsoleCommandLine.RET_OK) {
                //log
                String errorInfo = consoleCommandLine.getErrorString();
                System.out.println(errorInfo);
                
            } else {
                temp = consoleCommandLine.getOutputStringArray();
  
                fw = new FileWriter(base);
                /**
                 * 为了提高写入的效率，使用了字符流的缓冲区。 创建了一个字符写入流的缓冲区对象，并和指定要被缓冲的流对象相关联。
                 */
                bufw = new BufferedWriter(fw);
                 
                bufw.newLine();
                //使用缓冲区中的方法将数据写入到缓冲区中。
                int numz = 0;
                if (temp != null && temp.length > 0) {
                    for (String name : temp) {

                        String[] na = name.split(" +|\t+");  //根据空格（包括多个空格）分离
                        bufw.write(na[0] + "," + na[1] + "\n");
                        numz++;
                        if (numz == 1000) {
                            numz = 0;
                            bufw.flush();
                        }
                    }
                }
                //使用缓冲区中的方法，将数据刷新到目的地文件中去。
                bufw.flush();
                //关闭缓冲区,同时关闭了fw流对象
                bufw.close();

            }
           
        } catch (IOException ex) {
            Logger.getLogger(AxisSyncInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

    }
    
//        try {
//            File f = new File(basename);
//            if (!f.exists()) {
//                if (!f.createNewFile()) {
//                }
//            }
//            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8");
//            if (snaplist != null && snaplist.size() > 0) {
//                for(int i=(snaplist.size()-1); i>-1; i--){
//                    writer.write(snaplist.get(i).getStrName() + "," + snaplist.get(i).getStrUsed() + "\n");
//                }
//            }
//
//            writer.close();
//  
//        } catch (IOException ex) {
//            Logger.getLogger(AxisSyncInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
    
//    //获取快照后放入生成文件让入root中
//                try {
//                    File f = new File(base);
//                    if (!f.exists()) {
//                        if (!f.createNewFile()) {
//                        }
//                    }
//                    OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8");
//                    if (temp != null && temp.length > 0) {
//                        for (String name : temp) {
//                            
//                            String[] na = name.split(" +|\t+");
//                            writer.write(na[0] + ","+ na[1] + "\n");
//                        }
//                    }
//                    
//                    writer.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(AxisSyncInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                } 
    
    
    
//    public boolean insertSnapshotService(OMElement element) {
//        try {
//            SnapshotParameter para = SyncServerUtil.getResquestObject(element, SnapshotParameter.class);
//            if (para == null) {
//                this.setError("parser object failed");
//                return false;
//            }
//            String zfsName = para.getZfsName();
//            Snapshot[] snapshots = para.getSnapshots();
//            SyncDBUtil syncdb = new SyncDBUtil();
//
//            boolean result = syncdb.insertSnapshotService(zfsName, snapshots);
//            Debug.print("insertSnapshotService result = " + result);
//            if (!result) {
//                this.setError(sync.getError());
//                return false;
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
}
