/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;
import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class FileSysSnapSYNCInfo implements Serializable{

    private String strName;//文件系统名称
    private String strUsed;
    private String strAvailable;
    private int lSnapshotInterval;
    private int iMaxNum;
    private int iIsOpen;
    private int iRollbackProperty;
    // private static SyncStatus status;
//    private boolean existSnapOrNot;
//    private boolean openAutoSnapOrNot;
//    private boolean syncSourceOrNot;
//    private boolean syncTargetOrNot;
//    private boolean onLineOrNot;
//    private boolean suspendSyncOrNot;
//    private boolean rollBackOrNot;
    private static MsaSYNCInterface sync = InterfaceFactory.getSYNCInterfaceInstance();
    private FileSystemInfo fileSys;
    // private  int status[];
    //private String  targetFileSys ;

    public FileSysSnapSYNCInfo(String strName) {
        this.strName = strName;
        fileSys = this.getFileSystem();
        if (fileSys == null) {
            return;
        }
        iIsOpen = fileSys.getiIsOpen();
        iMaxNum = fileSys.getiMaxNum();
        lSnapshotInterval = fileSys.getlSnapshotInterval();
        this.iRollbackProperty = fileSys.getiRollbackProperty();


    }

    public FileSysSnapSYNCInfo() {
    }

    public static FileSystemInfo getFileSystemInfo(String fileSysName) {
        List<FileSystemInfo> fileSysList = sync.getFileSystemInfo();
        if (fileSysList == null) {
            return null;
        }
        for (FileSystemInfo sys : fileSysList) {
            if (sys.getStrName().equals(fileSysName)) {
                return sys;
            }
        }
        return null;
    }

    public static SyncStatus getLocalSyncStatus(String fileSys) {
        SyncStatus status = new SyncStatus();
        String array[] = sync.getLocalFileSystemStatus(fileSys);
        if (array == null || array.length < 7) {
            Debug.print("getRemoteFileSystemStatus array error");
            return null;
        }
        if (array[1].equals("1")) {
            status.setExistSnapOrNot(true);
        } else {
            status.setExistSnapOrNot(false);
        }
        if (array[0].equals("1")) {
            status.setFileSysExistOrNot(true);
        } else {
            status.setFileSysExistOrNot(false);
        }
        if (array[3].equals("1")) {
            status.setSyncSourceOrNot(true);
        } else {
            status.setSyncSourceOrNot(false);
        }
        if (array[4].equals("1")) {
            status.setSyncTargetOrNot(true);
        } else {
            status.setSyncTargetOrNot(false);
        }
        if (array[2].equals("1")) {
            status.setOnLineOrNot(true);
        } else {
            status.setOnLineOrNot(false);
        }
        if (array[5].equals("1")) {
            status.setSuspendSyncOrNot(true);
        } else {
            status.setSuspendSyncOrNot(false);
        }
        if (array[6].equals("1")) {
            status.setRollBackOrNot(true);
        } else {
            status.setRollBackOrNot(false);
        }
        // Debug.print("@@@@@@@@@@@@@############### exist snap array[1] : " + array[1]);
        // Debug.print("@@@@@@@@@@@@@############### online or not array[2] : " + array[2]);
        // Debug.print("@@@@@@@@@@@@@############### rollback or not array[6] : " + array[6]);
        return status;
//     * int[0] 文件系统是否存在
//     * int[1] 文件系统是否存在快照
//     * int[2] 文件系统是否上线
//     * int[3] 文件系统是不是同步源文件系统
//     * int[4] 文件系统是不是同步目标文件系统
//     * int[5] 文件系统是否暂停同步
//     * int[6] 文件系统是否回滚
        //[7] 已用空间
        //[8] 可用空间
    }

    public static SyncStatus getRemoteSyncStatus(String fileSys, String ip, String port, String username, String password) {
        SyncStatus status = new SyncStatus();
        //public int[] getRemoteFileSystemStatus(String descFileSystem,String ip,String port,String username,String password);
        String array[] = sync.getRemoteFileSystemStatus(fileSys, ip, port, username, password);

        if (array == null || array.length < 7) {
            Debug.print("getRemoteFileSystemStatus array error");
            return null;
        }
        // Debug.print("@@@@@@@@@@@@@############### getRemoteFileSystemStatus array size : " + array.length);
//        if (array[1] == null) {
//            Debug.print("@@@@@@@@@@@@@############### getRemoteFileSystemStatus array[1] null ");
//        }
        if (array[1].equals("1")) {
            status.setExistSnapOrNot(true);
        } else {
            status.setExistSnapOrNot(false);
        }
        if (array[0].equals("1")) {
            status.setFileSysExistOrNot(true);
        } else {
            status.setFileSysExistOrNot(false);
        }
        if (array[3].equals("1")) {
            status.setSyncSourceOrNot(true);
        } else {
            status.setSyncSourceOrNot(false);
        }
        if (array[4].equals("1")) {
            status.setSyncTargetOrNot(true);
        } else {
            status.setSyncTargetOrNot(false);
        }
        if (array[2].equals("1")) {
            status.setOnLineOrNot(true);
        } else {
            status.setOnLineOrNot(false);
        }
        if (array[5].equals("1")) {
            status.setSuspendSyncOrNot(true);
        } else {
            status.setSuspendSyncOrNot(false);
        }
        if (array[6].equals("1")) {
            status.setRollBackOrNot(true);
        } else {
            status.setRollBackOrNot(false);
        }
        return status;

    }

    public FileSystemInfo getFileSystem() {
        List<FileSystemInfo> fileSysList = sync.getFileSystemInfo();
        for (FileSystemInfo sys : fileSysList) {
            if (sys.getStrName().equals(this.strName)) {
                return sys;
            }
        }
        return null;
    }

//    public int[] getLocalFileSystemStatus() {
//        int status[] = sync.getLocalFileSystemStatus(this.targetFileSys);
//        if (status == null) {
//            return null;
//        }
//        return status;
//    }
    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrUsed() {
        return strUsed;
    }

    public void setStrUsed(String strUsed) {
        this.strUsed = strUsed;
    }

    public String getStrAvailable() {
        return strAvailable;
    }

    public void setStrAvailable(String strAvailable) {
        this.strAvailable = strAvailable;
    }

    public int getlSnapshotInterval() {
//        if (this.fileSys != null) {
//            return this.fileSys.getlSnapshotInterval();
//        }
        return this.getlSnapshotInterval();
    }

    public void setlSnapshotInterval(int lSnapshotInterval) {
        this.lSnapshotInterval = lSnapshotInterval;
    }

    public int getiMaxNum() {
//        if (this.fileSys != null) {
//            return this.fileSys.getiMaxNum();
//        }
        return this.iMaxNum;
    }

    public void setiMaxNum(int iMaxNum) {
        this.iMaxNum = iMaxNum;
    }

    public int getiIsOpen() {
//        if (this.fileSys != null) {
//            return this.fileSys.getiIsOpen();
//        }
        return this.iIsOpen;
    }

    public void setiIsOpen(int iIsOpen) {
        this.iIsOpen = iIsOpen;
    }

    public MsaSYNCInterface getSync() {
        return sync;
    }

    public void setSync(MsaSYNCInterface sync) {
        this.sync = sync;
    }

    public int getiRollbackProperty() {
        return iRollbackProperty;
    }

    public void setiRollbackProperty(int iRollbackProperty) {
        this.iRollbackProperty = iRollbackProperty;
    }
//
//    public boolean isExistSnapOrNot() {
//        return existSnapOrNot;
//    }
//
//    public void setExistSnapOrNot(boolean existSnapOrNot) {
//        this.existSnapOrNot = existSnapOrNot;
//    }
//
//    public boolean isOpenAutoSnapOrNot() {
//        return openAutoSnapOrNot;
//    }
//
//    public void setOpenAutoSnapOrNot(boolean openAutoSnapOrNot) {
//        this.openAutoSnapOrNot = openAutoSnapOrNot;
//    }
//
//    public Boolean isSyncSourceOrNot(String descFileSystem) {
//        if (this.status == null) {
//            Debug.print("**********############# get Local FileSystem Status failed");
//            return null;
//        }
//        if (this.status[2] == 1) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }
//
//    public void setSyncSourceOrNot(boolean syncSourceOrNot) {
//        this.syncSourceOrNot = syncSourceOrNot;
//    }
//
//    public Boolean isSyncTargetOrNot(String descFileSystem) {
//        if (this.status == null) {
//            Debug.print("**********############# get Local FileSystem Status failed");
//            return null;
//        }
//        if (this.status[3] == 1) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public void setSyncTargetOrNot(boolean syncTargetOrNot) {
//        this.syncTargetOrNot = syncTargetOrNot;
//    }
//
//    public boolean isOnLineOrNot() {
//        return onLineOrNot;
//    }
//
//    public void setOnLineOrNot(boolean onLineOrNot) {
//        this.onLineOrNot = onLineOrNot;
//    }
//
//    public Boolean isSuspendSyncOrNot(String descFileSystem) {
//        if (this.status == null) {
//            Debug.print("**********############# get Local FileSystem Status failed");
//            return null;
//        }
//        if (this.status[5] == 1) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public void setSuspendSyncOrNot(boolean suspendSyncOrNot) {
//        this.suspendSyncOrNot = suspendSyncOrNot;
//    }
//
//    public boolean isRollBackOrNot() {
//        return rollBackOrNot;
//    }
//
//    public void setRollBackOrNot(boolean rollBackOrNot) {
//        this.rollBackOrNot = rollBackOrNot;
//    }
}
