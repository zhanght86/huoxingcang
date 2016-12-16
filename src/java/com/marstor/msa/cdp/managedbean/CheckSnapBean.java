/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpRollbackTaskInfo;
import com.marstor.msa.cdp.util.SpacesTrans;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.javadb.SyncDBUtil;
import com.marstor.msa.nas.model.GlobalSnapSYNCInfo;
import com.marstor.msa.sync.axis2.client.SyncClient;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "checkSnapBean")
@ViewScoped
public class CheckSnapBean {

    private List<Snapshot> snapList = new ArrayList<Snapshot>();
    private Snapshot snapshot = null;
    private String snapName = "";
    private String snapTime = "";
    private String useTime = "";
    private String localSnapName = "";
    private CdpRollbackTaskInfo rb;
    private MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
    private MsaSYNCInterface syncInterface = InterfaceFactory.getSYNCInterfaceInstance();
    private String ip;
    private String port;
    private String remoteDiskGroup;
    private String localDiskGroup;
    
     private int protectType;
     private int autoSnapshotInterval = 0;
     private int iMaxNum = 0;
    
    private MSAGlobalResource global = new MSAGlobalResource();

    private boolean cantEdit = false;
    private boolean rollback_ok = false;
    private MSAResource res = new MSAResource();
    private String basename = "cdp.check_snap";

    private String radioValue;
    private String startTime;
    private String endTime;
    private String username;
    private String password;
    private String SshPassword;
    private String compressLevel;
    private String sourcePath;
    private String Sshport;
    private String guid;
    private String selectPath;
    private List<String> groupNames;
    private String lable = res.get("tip1");
    private int userType = 0;
    private boolean isRollbacking = false;

    /**
     * Creates a new instance of CheckSnapBean
     */
    public CheckSnapBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        port = request.getParameter("port");
        remoteDiskGroup = request.getParameter("remotediskgroup");
        selectPath = remoteDiskGroup;
        if (remoteDiskGroup.startsWith("/")) {
            remoteDiskGroup = remoteDiskGroup.substring(1);
        }
        localDiskGroup = request.getParameter("localdiskgroup");
        System.out.println(" localDiskGroup=" + localDiskGroup + ",ip=" + ip + ", port=" + port + ",  remoteDiskGroup=" + remoteDiskGroup);

        radioValue = request.getParameter("radioValue");
        startTime = request.getParameter("startTime");
        endTime = request.getParameter("endTime");
        username = request.getParameter("username");
        password = request.getParameter("password");
        SshPassword = request.getParameter("SshPassword");
        compressLevel = request.getParameter("compressLevel");
        sourcePath = request.getParameter("sourcePath");
        Sshport = request.getParameter("Sshport");
        guid = request.getParameter("guid");
        this.groupNames = Arrays.asList(request.getParameterValues("groupName"));
        
        protectType = Integer.valueOf(request.getParameter("level"));
        autoSnapshotInterval = Integer.valueOf(request.getParameter("AutoSnapshotInterval"));
        iMaxNum = Integer.valueOf(request.getParameter("iMaxNum"));

        cantEdit = false;
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        userType = user.getType();
        if (userType != 2) {
            cantEdit = true;
        }

        init();
    }

    private void init() {

        List<Snapshot> remoteAuto = syncInterface.getRemoteAutoSnapshot(ip, port, remoteDiskGroup);
        List<Snapshot> remoteManual = syncInterface.getRemoteManualSnapshot(ip, port, remoteDiskGroup);
        List<Snapshot> remoteList = new ArrayList<Snapshot>();
        if (remoteManual != null && remoteManual.size() > 0) {
            remoteList.addAll(remoteManual);
        }
        if (remoteAuto != null && remoteAuto.size() > 0) {
            remoteList.addAll(remoteAuto);
        }
        if (remoteList != null && remoteList.size() > 0) {
            Collections.sort(remoteList);
            for (Snapshot s : remoteList) {
                String name = s.isbAuto() ? "自动" : s.getAlias();
                System.out.println(s.getCreatedTime() + ",n=" + name + ",name=" + s.getStrName() + ",usetime=" + s.getStrUsed());
            }
        } else {
            lable = res.get("tip2");
            return;
        }

        String localPath = "";
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        List<CdpDiskGroupInfo> diskGroups = cdp.getDiskGroupInfos();
        for (CdpDiskGroupInfo g : diskGroups) {
            if (g.getDiskGroupName().equals(localDiskGroup)) {
                localPath = g.getDiskGroupPath();
            }
        }
        if (localPath.startsWith("/")) {
            localPath = localPath.substring(1);
        }
        List<Snapshot> localList = new ArrayList<Snapshot>();
        SyncDBUtil su = new SyncDBUtil();
        localList = su.querySnapshot(localPath);
        if (localList != null && localList.size() > 0) {
            Collections.sort(localList);
            System.out.println("本地快照");
            for (Snapshot s : localList) {
                String n = s.isbAuto() ? "自动" : s.getAlias();
                System.out.println(s.getCreatedTime() + ",name=" + s.getStrName() + ",n=" + n + ",usetime=" + s.getStrUsed());
            }
        } else {
            lable = lable = res.get("tip2");;
            return;
        }
        snapshot = new Snapshot();
        boolean flag = false;

        double usetimeDouble = 0;
        double usetimeDouble_l = 0;
        for (int i = 0; i < remoteList.size(); i++) {
            Snapshot r = remoteList.get(remoteList.size() - 1 - i);
            String reSnap = r.getStrName();
            if (reSnap.contains("@")) {
                reSnap = reSnap.split("@")[1];
            }
            for (Snapshot l : localList) {
                String loSnap = l.getStrName();
                if (loSnap.contains("@")) {
                    loSnap = loSnap.split("@")[1];
                }
                if (reSnap.equals(loSnap)) {
                    localSnapName = l.getStrName();
                    String timeStr = l.getStrUsed();
                    if (timeStr != null && !timeStr.equals("")) {
                        usetimeDouble_l = this.convertSizeToByte(timeStr);
                    }
                    snapshot = r;
                    snapName = snapshot.getStrName();
                    if (snapName.contains("@")) {
                        snapName = snapName.split("@")[1];
                    }
                    snapTime = snapshot.getCreatedTime();
                    flag = true;

//                    break;
                }
                if (flag) {
                    String useTimeStr = l.getStrUsed();
                    if (useTimeStr != null && !useTimeStr.equals("")) {
                        usetimeDouble = usetimeDouble + this.convertSizeToByte(useTimeStr);
                    }
                }
            }
            if (flag) {
                usetimeDouble = usetimeDouble - usetimeDouble_l;
                useTime = SpacesTrans.getHumanReadingSizeB((long) usetimeDouble);
                break;
            } else {
                snapList.add(r);
            }

        }

        if (snapName.equals("")) {
            lable = res.get("tip2");
            cantEdit = true;
            rollback_ok = true;
            return;
        } else {
            if (snapList == null || snapList.size() == 0) {
                rollback_ok = false;
                cantEdit = true;
                if (userType != 2) {
                    rollback_ok = true;
                }
            }else{
                 rollback_ok = true;
            }
            
        }

    }
    public String getSnapName_to(String name) {
        if (name != null && !name.equals("")) {
            if (name.contains("@")) {
                name = name.split("@")[1];
            }
        }
        return name;

    }

    public static double convertSizeToByte(String strSize) {
        if (strSize == null || strSize.equals("")) {
            return 0;
        }
        int iLength = strSize.length();
        String strNumber;
        char c;
        boolean bRet;
        if (iLength > 1) {
            c = strSize.charAt(iLength - 1);
            strNumber = strSize.substring(0, iLength - 1);
        } else {
            c = 'a';
            strNumber = strSize;
        }
        if (true) {
            switch (c) {
                case 'T':
                    return Double.parseDouble(strNumber) * 1024 * 1024 * 1024 * 1024;
                case 'G':
                    return Double.parseDouble(strNumber) * 1024 * 1024 * 1024;
                case 'M':
                    return Double.parseDouble(strNumber) * 1024 * 1024;
                case 'K':
                    return Double.parseDouble(strNumber) * 1024;
                case 'B':
                    return Double.parseDouble(strNumber);
                default:
                    return Double.parseDouble(strNumber);
            }
        }
        return 0;
    }

    public void handleDelete() {
        if (!snapName.equals("")) {
            System.out.println("111111111111111111111111111111");
            RequestContext.getCurrentInstance().execute("deletesnap.show()");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "lable"), ""));
        }
    }

    //检测是否是在回滚中
    public void checkRollbacking() {
        boolean rollbacking = InterfaceFactory.getSYNCInterfaceInstance().isRollbacking(this.sourcePath);
        if (!rollbacking) {
            isRollbacking = false;
            cantEdit = true;
            rollback_ok = false;
            RequestContext.getCurrentInstance().execute("rollbackpoll.stop();");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("rollbackok"), ""));
            lable=res.get("rollbackok");
        } else {
            isRollbacking = true;
        }
    }

    public void handle() {
//        String notDelete = "";
//        for (Snapshot s : snapList) {
//            boolean delete = syncInterface.deleteRemoteSnapshot(ip, port, s.getStrName());
//            if (delete == false) {
//                notDelete = notDelete + s.getStrName() + "  ";
//            }
//        }
//        if (notDelete.equals("")) {
//           
//
//            // syncInterface.setRemoteSyncInfo(this.selectedIP, this.selectHost.getIp(),this.selectHost.getPort(), this.selectHost.getUsername(), this.selectHost.getPassword() );
//            String param = "path=" + this.sourcePath + "&guid=" + guid + "&dg=" + this.localDiskGroup;
//            for (String name : this.groupNames) {
//                param += "&groupName=" + name;
//            }
//            return "copy?faces-redirect=true&amp;" + param;
//
//        } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "delete" + notDelete + "delete_false"), ""));
//            return null;
//        }
        System.out.println("ip=" + ip + ",port=" + port + ",snapshot.getStrName()=" + snapshot.getStrName());
        isRollbacking = true;
        cantEdit = true;
        boolean roll = syncInterface.rollbackRemoteSnapshot(ip, port, snapshot.getStrName());

        if (!roll) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addRemoteTargetFailed"), res.get("addRemoteTargetFailed")));
            return;
        } else {
            rollback_ok = false;
            cantEdit = true;
            snapList = null;
        }

    }

    public String copy() {
        int transferPort = GlobalSnapSYNCInfo.getInstance().getTransferPort();
        if (transferPort == -1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("getModuleConfigInfoFailed"), res.get("getModuleConfigInfoFailed")));
            return null;
        }

        boolean flag = true;
        boolean isFixedTime = this.radioValue.equals("1") ? false : true;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date startDate, endDate;
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        try {
            startDate = df.parse(this.startTime);
            endDate = df.parse(this.endTime);
            startCal.setTime(startDate);
            endCal.setTime(endDate);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        
//        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
//        CdpDiskGroupInfo group = cdp.getDiskGroupInfo(guid);
//        if (group != null) {
            flag = flag && cdp.setRemoteProtectType(ip, Integer.parseInt(port),
                    username, password, this.selectPath, protectType);
            if (protectType == com.marstor.msa.cdp.util.CDPConstants.PROTECT_SNAPSHOT) {
                SyncClient client = new SyncClient(ip, Integer.parseInt(port));
                flag = flag && client.configSnapshotProtect(this.selectPath, autoSnapshotInterval, iMaxNum);
            }
//        }
        System.out.println("last ****************localSnapName=" + localSnapName);
        flag = flag && syncInterface.setRemoteSyncInfo(ip, port, username, password, SshPassword, this.compressLevel, this.sourcePath, this.selectPath, Sshport, String.valueOf(transferPort), startCal, endCal, isFixedTime, localSnapName);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addRemoteTargetFailed"), res.get("addRemoteTargetFailed")));
            return null;
        }

        String param = "path=" + this.sourcePath + "&guid=" + guid + "&dg=" + this.localDiskGroup;
        for (String name : this.groupNames) {
            param += "&groupName=" + name;
        }
        return "copy?faces-redirect=true&amp;" + param;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<Snapshot> getSnapList() {
        return snapList;
    }

    public void setSnapList(List<Snapshot> snapList) {
        this.snapList = snapList;
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public String getSnapName() {
        return snapName;
    }

    public void setSnapName(String snapName) {
        this.snapName = snapName;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getRemoteDiskGroup() {
        return remoteDiskGroup;
    }

    public void setRemoteDiskGroup(String remoteDiskGroup) {
        this.remoteDiskGroup = remoteDiskGroup;
    }

    public String getLocalDiskGroup() {
        return localDiskGroup;
    }

    public void setLocalDiskGroup(String localDiskGroup) {
        this.localDiskGroup = localDiskGroup;
    }

    public boolean isCantEdit() {
        return cantEdit;
    }

    public void setCantEdit(boolean cantEdit) {
        this.cantEdit = cantEdit;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getSnapTime() {
        return snapTime;
    }

    public void setSnapTime(String snapTime) {
        this.snapTime = snapTime;
    }

    public boolean isRollback_ok() {
        return rollback_ok;
    }

    public void setRollback_ok(boolean rollback_ok) {
        this.rollback_ok = rollback_ok;
    }

    public boolean isIsRollbacking() {
        return isRollbacking;
    }

    public void setIsRollbacking(boolean isRollbacking) {
        this.isRollbacking = isRollbacking;
    }

}
