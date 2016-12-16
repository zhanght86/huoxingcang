/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.model.ClientDiskDetail;
import com.marstor.msa.cdp.model.ClientDiskInfo;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.model.ClientPartitionDetail;
import com.marstor.msa.cdp.model.ClientPartitionInfo;
import com.marstor.msa.cdp.model.LinuxClientDiskDetail;
import com.marstor.msa.cdp.model.LinuxClientDiskInfo;
import com.marstor.msa.cdp.model.LinuxClientPartitionDetail;
import com.marstor.msa.cdp.model.LinuxClientPartitionInfo;
import com.marstor.msa.cdp.bean.LuInfoBean;
import com.marstor.msa.cdp.model.LinuxVolumeDetail;
import com.marstor.msa.cdp.model.LinuxVolumeGroupInfo;
import com.marstor.msa.cdp.model.LinuxVolumeInfo;
import com.marstor.msa.cdp.socket.CDPClientSocket;
import com.marstor.msa.cdp.socket.LinuxClientAPI;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "linuxClientBean")
@ViewScoped
public class LinuxClientBean {

    private ClientInfo client = new ClientInfo();
    private LinuxClientDiskInfo selectedD = new LinuxClientDiskInfo();
    private LinuxClientPartitionInfo selectedP = new LinuxClientPartitionInfo();
    private LinuxVolumeGroupInfo selectedVG = new LinuxVolumeGroupInfo();
    private LinuxVolumeInfo selectedV = new LinuxVolumeInfo();
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.client_linux";
    private String ip = "192.168.1.87";
    private int port = 1100;
    boolean bShowCreateCDP = false;
    boolean bShowDeleteCDP = false;
    boolean bShowStartCDP = false;
    boolean bShowStopCDP = false;
    boolean bShowRestoreData = false;
    private TreeNode root = new DefaultTreeNode();
    private TreeNode rootV = new DefaultTreeNode();
    private Object selected = new Object();
    private String device;
    private String pDevice;
    private String srcDevice;
    private boolean bShowRDetail;
    private boolean disable = false;
    private List<String> disksDest_Restore = new ArrayList(); //磁盘恢复类型的磁盘
    private List<String> disksSrc_Restore = new ArrayList(); //磁盘镜像(恢复数据)类型的磁盘号
    private List<String> pDisksDest_Restore = new ArrayList(); //分区恢复类型的分区的磁盘号
    private List<String> pDisksSrc_Restore = new ArrayList();  //分区镜像(恢复数据)类型的磁盘号
    private List<String> partitionsDest_Restore = new ArrayList();//分区恢复类型的分区的磁盘号和分区号的组合
    private List<String> partitionsSrc_Restore = new ArrayList(); //分区镜像类型的分区的磁盘号和分区号的组合
    private List<String> vDisksSrc_Restore = new ArrayList(); //卷镜像(恢复数据)类型磁盘
    private List<String> vPartitionsSrc_Restore = new ArrayList(); //卷镜像(恢复数据)类型的分区
    private List<String> vgDest_Restore = new ArrayList(); //卷恢复(恢复数据)类型卷组
    private List<String> vDest_Restore = new ArrayList(); //卷恢复(恢复数据)类型的卷

    public LinuxClientBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        if (request.getParameter("port") != null) {
            port = Integer.parseInt(request.getParameter("port"));
        }
        if (request.getParameter("dNum") != null) {
            device = request.getParameter("dNum");
        }
        if (request.getParameter("pNum") != null) {
            pDevice = request.getParameter("pNum");
        }
        if (request.getParameter("sNum") != null) {
            srcDevice = request.getParameter("sNum");
        }
        client.setIp(ip);
        client.setPort(port);
        connectHost();
    }

    public String initObjects() {
        root = new DefaultTreeNode();
        rootV = new DefaultTreeNode();
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        Map<String, LuInfoBean> VDs = cdp.getAllDiskInfos();
        if (client != null && client.getLinuxClientDisks() != null) {
            for (LinuxClientDiskInfo disk : client.getLinuxClientDisks()) {
                LinuxClientDiskDetail DiskDetail = LinuxClientAPI.GetSourceDiskDetail(disk.device, this.client);

                if (DiskDetail == null) {
                    LinuxClientAPI.GetErrorCode();
                    return null;
                }
                if (DiskDetail.CDPType == CDPConstants.CDP_STATUS_INIT && DiskDetail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
                    this.disksDest_Restore.add(DiskDetail.device);
                    this.disksSrc_Restore.add(DiskDetail.cdpDevice);

                    //                    RequestContext.getCurrentInstance().execute("window.location.href = 'd_restore_pro_LinuxX.xhtml?faces-redirect=true&amp;ip=" + ip + "&amp;port=" + port + "&amp;device=" + DiskDetail.device + "';");

//                    this.dJumpPara = "d_restore_pro_LinuxX.xhtml?faces-redirect=true&amp;ip=" + ip + "&amp;port=" + port + "&amp;device=" + DiskDetail.device;
//                    this.dRestoring = true;

                    //                    if (DiskDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
//                            || DiskDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
//                        boolean bRet = LinuxClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, DiskDetail.device, "", this.client);
//                        if (!bRet) {
//                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
//                            return;
//                        }
//                        bRet = LinuxClientAPI.ScanDisks(client);
//                        if (!bRet) {
//                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "SDFailed"), global.get("error_mark")));
//                            return;
//                        }
//                    }
                }
                disk.setDetail(DiskDetail);
//            String serialNumber = doubleReverse(DiskDetail.SerialNumber);
                String serialNumber = DiskDetail.SerialNumber;
                boolean flag = true;
                if (VDs != null) {
                    for (LuInfoBean vd : VDs.values()) {
                        if (vd.getSerialNum().equals("")) {
                            continue;
                        }
                        if (serialNumber.equals(vd.getSerialNum())) {
                            disk.setIsMsa(true);
                            TreeNode d = new DefaultTreeNode(disk, root);
                            if (disk.PartitionInfos == null || disk.PartitionInfos.length == 0) {
                                flag = false;
                                break;
                            }

                            for (LinuxClientPartitionInfo partition : disk.PartitionInfos) {
                                LinuxClientPartitionDetail PartitionDetail = LinuxClientAPI.GetSourcePartitionDetail(disk.device, partition.pDevice, this.client);
                                if (PartitionDetail == null) {
                                    LinuxClientAPI.GetErrorCode();
                                    return null;
                                }
                                partition.setDetail(PartitionDetail);
                                partition.setIsMsa(true);
                                partition.setDiskCDPType(disk.CDPType);
                                TreeNode p = new DefaultTreeNode(partition, d);
                                if (PartitionDetail.CDPType == CDPConstants.CDP_STATUS_INIT && PartitionDetail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
                                    this.pDisksDest_Restore.add(PartitionDetail.device);
                                    this.partitionsDest_Restore.add(PartitionDetail.device + PartitionDetail.pDevice);
                                    this.pDisksSrc_Restore.add(PartitionDetail.cdpDevice);
                                    this.partitionsSrc_Restore.add(PartitionDetail.device + PartitionDetail.cdpPDevice);
                                    //                                    RequestContext.getCurrentInstance().execute("window.location.href = 'p_restore_pro_linux.xhtml?faces-redirect=true&amp;ip=" + ip + "&amp;port=" + port + "&amp;device=" + PartitionDetail.device + "&amp;pDevice=" + PartitionDetail.pDevice + "';");
//                                    this.pJumpPara = "p_restore_pro_linux.xhtml?faces-redirect=true&amp;ip=" + ip + "&amp;port=" + port + "&amp;device=" + PartitionDetail.device + "&amp;pDevice=" + PartitionDetail.pDevice;
//                                    this.pRestoring = true;

                                    //                                    if (PartitionDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
//                                            || PartitionDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
//                                        boolean bRet = LinuxClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, PartitionDetail.device, PartitionDetail.pDevice, this.client);
//                                        if (!bRet) {
//                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "cancelRestoreFailed"), global.get("error_mark")));
//                                            return;
//                                        }
//                                        bRet = LinuxClientAPI.ScanDisks(client);
//                                        if (!bRet) {
//                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "SDFailed"), global.get("error_mark")));
//                                            return;
//                                        }
//                                    }
                                }

                            }
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag) {
                    TreeNode d = new DefaultTreeNode(disk, root);
                    if (disk.PartitionInfos == null || disk.PartitionInfos.length == 0) {
                        continue;
                    }
                    for (LinuxClientPartitionInfo partition : disk.PartitionInfos) {
                        LinuxClientPartitionDetail PartitionDetail = LinuxClientAPI.GetSourcePartitionDetail(disk.device, partition.pDevice, this.client);
                        if (PartitionDetail == null) {
                            LinuxClientAPI.GetErrorCode();
                            return null;
                        }
                        partition.setDetail(PartitionDetail);
                        partition.setDiskCDPType(disk.CDPType);
                        TreeNode p = new DefaultTreeNode(partition, d);
                        if (PartitionDetail.CDPType == CDPConstants.CDP_STATUS_INIT && PartitionDetail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
                            this.pDisksDest_Restore.add(PartitionDetail.device);
                            this.partitionsDest_Restore.add(PartitionDetail.device + PartitionDetail.pDevice);
                            this.pDisksSrc_Restore.add(PartitionDetail.cdpDevice);
                            this.partitionsSrc_Restore.add(PartitionDetail.cdpDevice + PartitionDetail.cdpPDevice);
                            //                                    RequestContext.getCurrentInstance().execute("window.location.href = 'p_restore_pro_linux.xhtml?faces-redirect=true&amp;ip=" + ip + "&amp;port=" + port + "&amp;device=" + PartitionDetail.device + "&amp;pDevice=" + PartitionDetail.pDevice + "';");
//                            this.pJumpPara = "p_restore_pro_linux.xhtml?faces-redirect=true&amp;ip=" + ip + "&amp;port=" + port + "&amp;device=" + PartitionDetail.device + "&amp;pDevice=" + PartitionDetail.pDevice;
//                            this.pRestoring = true;

                            //                                    if (PartitionDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_COMPLETED
//                                            || PartitionDetail.SyncStatus == CDPConstants.CDP_STATUS_HOOK_SYNC_FAILED) {
//                                        boolean bRet = LinuxClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, PartitionDetail.device, PartitionDetail.pDevice, this.client);
//                                        if (!bRet) {
//                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "cancelRestoreFailed"), global.get("error_mark")));
//                                            return;
//                                        }
//                                        bRet = LinuxClientAPI.ScanDisks(client);
//                                        if (!bRet) {
//                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "SDFailed"), global.get("error_mark")));
//                                            return;
//                                        }
//                                    }
                        }
                    }
                }
            }
        }

        if (client != null && client.getLinuxVGs() != null) {
            for (LinuxVolumeGroupInfo disk : client.getLinuxVGs()) {
                TreeNode d = new DefaultTreeNode(disk, rootV);
                if (disk.VolumeInfos == null || disk.vCount == 0) {
                    continue;
                }
                for (LinuxVolumeInfo partition : disk.VolumeInfos) {
                    LinuxVolumeDetail PartitionDetail = LinuxClientAPI.GetVolumeDetail(disk.vgName, partition.vName, this.client);
                    if (PartitionDetail == null) {
                        LinuxClientAPI.GetErrorCode();
                        return null;
                    }

                    partition.setDetail(PartitionDetail);
                    TreeNode p = new DefaultTreeNode(partition, d);
                    if (PartitionDetail.CDPType == CDPConstants.CDP_STATUS_INIT && PartitionDetail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
                        this.vgDest_Restore.add(PartitionDetail.vgName);
                        this.vDest_Restore.add(PartitionDetail.vgName + PartitionDetail.vName);
                        this.vDisksSrc_Restore.add(PartitionDetail.CDP_VG);
                        this.vPartitionsSrc_Restore.add(PartitionDetail.CDP_VG + PartitionDetail.CDP_V);
                        //                                    RequestContext.getCurrentInstance().execute("window.location.href = 'v_restore_pro.xhtml?faces-redirect=true&amp;ip=" + ip + "&amp;port=" + port + "&amp;vgName=" + PartitionDetail.vgName
//                + "&amp;vName=" + PartitionDetail.vName + "';");

//                        this.vJumpPara = "v_restore_pro.xhtml?faces-redirect=true&amp;ip=" + ip + "&amp;port=" + port + "&amp;vgName=" + PartitionDetail.vgName + "&amp;vName=" + PartitionDetail.vName;
//                        this.vRestoring = true;
                    }
                }
            }
        }
        disable = true;
        return null;
    }

    public final void connectHost() {
        if (ip == null) {
            return;
        }

        CDPClientSocket clientSock = new CDPClientSocket();
        if (!clientSock.Connect(ip, port)) {
            return;
        }
        client.setLinuxClientDisks(LinuxClientAPI.GetSourceDisksInfo(client));
        client.setLinuxVGs(LinuxClientAPI.GetVGsInfo(client));
        if (client.getLinuxClientDisks() == null && client.getLinuxVGs() == null) {
            LinuxClientAPI.GetErrorCode();
            return;
        }
        initObjects();
    }

    public String CDPAction(Object o) {
        String s;
        if (o instanceof LinuxClientDiskInfo) {
            this.selectedD = (LinuxClientDiskInfo) o;
            s = dCDP();
        } else {
            this.selectedP = (LinuxClientPartitionInfo) o;
            s = pCDP();
        }
        return s;
    }

    public String CDPActionV(Object o) {
        this.selectedV = (LinuxVolumeInfo) o;
        if (this.selectedV.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
            RequestContext.getCurrentInstance().execute("stopVolumeCDP.show()");
            return null;
        } else {
            String param = "ip=" + ip + "&port=" + port + "&dNum=" + this.selectedV.vgName
                    + "&pNum=" + this.selectedV.vName + "&sNum=" + this.selectedV.CDP_VG;
            return "start_vcdp.xhtml?faces-redirect=true&amp;" + param;
        }
    }

    public String RestoreAction(Object o) {
        String s;
        if (o instanceof LinuxClientDiskInfo) {
            this.selectedD = (LinuxClientDiskInfo) o;
            s = dRestore();
        } else {
            this.selectedP = (LinuxClientPartitionInfo) o;
            s = pRestore();
        }
        return s;
    }

    public String RestoreActionV(Object o) {
        this.selectedV = (LinuxVolumeInfo) o;
        String dMirror = "";
        String pMirror = "";
        if (selectedV.detail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            dMirror = selectedV.detail.CDP_VG;
            pMirror = selectedV.detail.CDP_V;
        }
        String param = "ip=" + ip + "&port=" + port;
        return "v_restore?faces-redirect=true&amp;" + param + "&vgName=" + selectedV.vgName
                + "&vName=" + selectedV.vName
                + "&vSize=" + this.selectedV.vSize + "&cdpType=" + this.selectedV.CDPType
                + "&dMirror=" + dMirror
                + "&pMirror=" + pMirror;
    }

    public String DetailsAction(Object o) {
        String s;
        if (o instanceof LinuxClientDiskInfo) {
            this.selectedD = (LinuxClientDiskInfo) o;
            s = dDetails();
        } else {
            this.selectedP = (LinuxClientPartitionInfo) o;
            s = pDetails();
        }
        return s;
    }

    public String DetailsActionV(Object o) {
        this.selectedV = (LinuxVolumeInfo) o;
        String param = "ip=" + ip + "&port=" + port + "&vgName=" + selectedV.vgName
                + "&vName=" + selectedV.vName;
        return "volume?faces-redirect=true&amp;" + param;
    }

    public void dDelMirror() {
        this.selectedD = (LinuxClientDiskInfo) this.selected;
        boolean bRet = LinuxClientAPI.DeleteCDP(CDPConstants.CDP_TYPE_DISK, this.selectedD.device, "", this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));

        }
    }

    public void pDelMirror() {
        this.selectedP = (LinuxClientPartitionInfo) this.selected;
        boolean bRet = LinuxClientAPI.DeleteCDP(CDPConstants.CDP_TYPE_PARTITION, this.selectedP.device, selectedP.pDevice, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), global.get("error_mark")));
        }
    }

    public void vDelMirror() {
        this.selectedV = (LinuxVolumeInfo) this.selected;
        boolean bRet = LinuxClientAPI.DeleteVCDP(CDPConstants.CDP_TYPE_VOLUME, this.selectedV.vgName, selectedV.vName, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail1"), global.get("error_mark")));
        }
    }

    public void dStopCDP() {
        System.out.println("selectedD.DiskNumber:" + this.selectedD.device);
        this.selectedD = (LinuxClientDiskInfo) this.selected;
        System.out.println("selected.DiskNumber:" + this.selectedD.device);
        boolean bRet = LinuxClientAPI.StopCDP(CDPConstants.CDP_TYPE_DISK, this.selectedD.device, "", this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail2"), global.get("error_mark")));
        }
    }

    public void pStopCDP() {
        System.out.println("selectedP.PartitionNumber:" + this.selectedP.device);
        this.selectedP = (LinuxClientPartitionInfo) this.selected;
        System.out.println("selected.PartitionNumber:" + this.selectedP.device);
        boolean bRet = LinuxClientAPI.StopCDP(CDPConstants.CDP_TYPE_PARTITION,
                this.selectedP.device, this.selectedP.pDevice, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail3"), global.get("error_mark")));
        }
    }

    public void vStopCDP() {
        this.selectedV = (LinuxVolumeInfo) this.selected;
        boolean bRet = LinuxClientAPI.StopVCDP(CDPConstants.CDP_TYPE_VOLUME,
                this.selectedV.vgName, this.selectedV.vName, this.client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail3"), global.get("error_mark")));
        }
    }

    public String dCDP() {
        if (this.selectedD.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
            RequestContext.getCurrentInstance().execute("stopDiskCDP.show()");
            return null;
        } else {
            String param = "ip=" + ip + "&port=" + port
                    + "&dNum=" + this.selectedD.device + "&sNum=" + this.selectedD.cdpDevice;
            return "start_dcdp_linux.xhtml?faces-redirect=true&amp;" + param;
        }
    }

    public String pCDP() {
        if (this.selectedP.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
            RequestContext.getCurrentInstance().execute("stopPartitionCDP.show()");
            return null;
        } else {
            String param = "ip=" + ip + "&port=" + port + "&dNum=" + this.selectedP.device
                    + "&pNum=" + this.selectedP.pDevice + "&sNum=" + this.selectedP.cdpDevice;
            return "start_pcdp_linux.xhtml?faces-redirect=true&amp;" + param;
        }
    }

    public String mirror(Object o) {
//        RequestContext.getCurrentInstance().execute("PF('block').show()");
        String s;
        if (o instanceof LinuxClientDiskInfo) {
            this.selectedD = (LinuxClientDiskInfo) o;
            s = dMirror();
        } else {
            this.selectedP = (LinuxClientPartitionInfo) o;
            s = pMirror();
        }
        return s;
    }

    public String mirrorV(Object o) {
        this.selectedV = (LinuxVolumeInfo) o;
        if (this.selectedV.CDPType == CDPConstants.CDP_TYPE_INIT) {
            String param = "ip=" + ip + "&port=" + port
                    + "&vgName=" + this.selectedV.vgName + "&vName=" + this.selectedV.vName
                    + "&vSize=" + this.selectedV.vSize + "&cdpType=" + this.selectedV.CDPType;
            return "v_cdp?faces-redirect=true&amp;" + param;
        } else {
            RequestContext.getCurrentInstance().execute("deleteVolumeCDP.show()");
            return null;
        }
    }

    public String getNum(Object o) {
        String s;
        if (o instanceof LinuxClientDiskInfo) {
            s = ((LinuxClientDiskInfo) o).device;
        } else {
            s = ((LinuxClientPartitionInfo) o).pDevice;
        }
        return s;
    }

    public String getNumV(Object o) {
        String s;
        if (o instanceof LinuxVolumeGroupInfo) {
            s = ((LinuxVolumeGroupInfo) o).vgName;
        } else {
            s = ((LinuxVolumeInfo) o).vName;
        }
        return s;
    }

    public String addStar(Object o) {
        String s;
        if (o instanceof LinuxClientDiskInfo) {
            s = ((LinuxClientDiskInfo) o).isMsa ? "*" : "";
        } else {
            s = ((LinuxClientPartitionInfo) o).isMsa ? "*" : "";
        }
        return s;
    }

    public String getSize(Object o) {
        String s;
        if (o instanceof LinuxClientDiskInfo) {
            s = this.getStrSize(((LinuxClientDiskInfo) o).size);
        } else {
            s = this.getStrSize(((LinuxClientPartitionInfo) o).PartitionSize);
        }
        return s;
    }

    public String getSizeV(Object o) {
        String s;
        if (o instanceof LinuxVolumeGroupInfo) {
            s = this.getStrSize(((LinuxVolumeGroupInfo) o).vgSize);
        } else {
            s = this.getStrSize(((LinuxVolumeInfo) o).vSize);
        }
        return s;
    }

    public String getUsedSize(Object o) {
        String s;
        if (o instanceof LinuxClientDiskInfo) {
            LinuxClientDiskInfo disk = (LinuxClientDiskInfo) o;
            s = Integer.toString(disk.PartitionCount);
        } else {
            LinuxClientPartitionInfo partition = (LinuxClientPartitionInfo) o;

            if (partition.MountPoint.equals("")) {
                s = "";
            } else {
//                long usedSize = partition.PartitionSize - partition.detail.FreeSize;
//                System.out.println("----------------usedSize----------------");
//                System.out.println("---------------" + usedSize + "-------------");
//                s = this.getStrSize(usedSize, partition.PartitionSize);
                s = Utility.getSizeString(partition.detail.UsedSize) + " GB" + "(" + partition.MountPoint + ")";
            }
        }
        return s;
    }

    public String getUsedSizeV(Object o) {
        String s;
        if (o instanceof LinuxVolumeGroupInfo) {
            s = "";
        } else {
            LinuxVolumeInfo partition = (LinuxVolumeInfo) o;
            if (partition.MountPoint.equals("")) {
                s = "";
            } else {
                long usedSize = partition.detail.UsedSize;
                s = Utility.getSizeString(usedSize) + " GB" + "(" + partition.MountPoint + ")";
            }
        }
        return s;
    }

    public String getCDPType(Object o) {
        String s;
        boolean isDest_Restore;
        boolean isSrc_Restore;
        boolean isvSrc_Restore;
        if (o instanceof LinuxClientDiskInfo) {
            isDest_Restore = this.disksDest_Restore.contains(((LinuxClientDiskInfo) o).device);
            isSrc_Restore = this.disksSrc_Restore.contains(((LinuxClientDiskInfo) o).device);
            boolean ispdDest_Restore = this.pDisksDest_Restore.contains(((LinuxClientDiskInfo) o).device);
            boolean ispdSrc_Restore = this.pDisksSrc_Restore.contains(((LinuxClientDiskInfo) o).device);
            isvSrc_Restore = this.vDisksSrc_Restore.contains(((LinuxClientDiskInfo) o).device);
            if (isDest_Restore) {
                s = "磁盘恢复";
            } else if (isSrc_Restore) {
                s = "磁盘镜像";
            } else if (ispdDest_Restore) {
                s = "分区恢复";
            } else if (ispdSrc_Restore) {
                s = "分区镜像";
            } else if (isvSrc_Restore) {
                s = "LVM卷镜像";
            } else {
                s = Utility.getCDPTypeString(((LinuxClientDiskInfo) o).CDPType);
            }
        } else {
            String p = ((LinuxClientPartitionInfo) o).device + ((LinuxClientPartitionInfo) o).pDevice;

            isDest_Restore = this.partitionsDest_Restore.contains(p);
            isSrc_Restore = this.partitionsSrc_Restore.contains(p);
            isvSrc_Restore = this.vPartitionsSrc_Restore.contains(p);

            if (isDest_Restore) {
                s = "分区恢复";
            } else if (isSrc_Restore) {
                s = "分区镜像";
            } else if (isvSrc_Restore) {
                s = "LVM卷镜像";
            } else {
                s = Utility.getCDPTypeString(((LinuxClientPartitionInfo) o).CDPType);
            }
        }
        return s;
    }

    public String getCDPTypeV(Object o) {
        String s;
        if (o instanceof LinuxVolumeGroupInfo) {
            boolean isvDest_Restore = this.vgDest_Restore.contains(((LinuxVolumeGroupInfo) o).vgName);

            if (isvDest_Restore) {
                s = "LVM卷恢复";
            } else {
                s = "";
            }

        } else {
            String p = ((LinuxVolumeInfo) o).vgName + ((LinuxVolumeInfo) o).vName;

            boolean isvDest_Restore = this.vDest_Restore.contains(p);
            if (isvDest_Restore) {
                s = "LVM卷恢复";
            } else {
                s = Utility.getCDPTypeString(((LinuxVolumeInfo) o).CDPType);
            }

        }
        return s;
    }

    public String getMirror(Object o) {
        String s;
        if (o instanceof LinuxClientDiskInfo) {
            s = getDStrMirror((LinuxClientDiskInfo) o);
        } else {
            s = getPStrMirror((LinuxClientPartitionInfo) o);
        }
        return s;
    }

    public String getMirrorV(Object o) {
        String s;
        if (o instanceof LinuxVolumeGroupInfo) {
            s = "";
        } else {
            s = getVStrMirror((LinuxVolumeInfo) o);
        }
        return s;
    }

    public String getStatus(Object o) {
        String s;
        if (o instanceof LinuxClientDiskInfo) {
            s = getDStrStatus((LinuxClientDiskInfo) o);
        } else {
            s = getPStrStatus((LinuxClientPartitionInfo) o);
        }
        return s;
    }

    public String getStatusV(Object o) {
        String s;
        if (o instanceof LinuxVolumeGroupInfo) {
            s = "";
        } else {
            s = getVStrStatus((LinuxVolumeInfo) o);
        }
        return s;
    }

    public boolean isDisable(Object o, int i) {
        boolean dis;
        if (o instanceof LinuxClientDiskInfo) {
            this.selectedD = (LinuxClientDiskInfo) o;
            dis = DiskDisable((LinuxClientDiskInfo) o, i);
        } else {
            this.selectedP = (LinuxClientPartitionInfo) o;
            dis = PartitionDisable((LinuxClientPartitionInfo) o, i);
        }
        return dis;
    }

    public boolean isDisableX(Object o, int i) {
        boolean dis;
        if (o instanceof LinuxClientDiskInfo) {
            this.selectedD = (LinuxClientDiskInfo) o;
            dis = DiskDisable((LinuxClientDiskInfo) o, i);
        } else {
            this.selectedP = (LinuxClientPartitionInfo) o;
            dis = PartitionDisableX((LinuxClientPartitionInfo) o, i);
        }
        return dis;
    }

    public boolean isDisableV(Object o, int i) {
        boolean dis;
        if (o instanceof LinuxVolumeGroupInfo) {
            dis = false;
        } else {
            this.selectedV = (LinuxVolumeInfo) o;
            dis = VDisable((LinuxVolumeInfo) o, i);
        }
        return dis;
    }

    public String getO1(Object o) {
        int type;
        if (o instanceof LinuxClientDiskInfo) {
            type = ((LinuxClientDiskInfo) o).CDPType;
        } else {
            type = ((LinuxClientPartitionInfo) o).CDPType;
        }

        if (type == CDPConstants.CDP_TYPE_INIT) {
            return res.get(basename, "o1_0");
        } else {
            return res.get(basename, "o1_1");
        }
    }

    public String getO1V(Object o) {
        int type;
        if (o instanceof LinuxVolumeGroupInfo) {
            return "";
        } else {
            type = ((LinuxVolumeInfo) o).CDPType;
        }

        if (type == CDPConstants.CDP_TYPE_INIT) {
            return res.get(basename, "o1_0");
        } else {
            return res.get(basename, "o1_1");
        }
    }

    public String getO2(Object o) {
        int status;
        if (o instanceof LinuxClientDiskInfo) {
            status = ((LinuxClientDiskInfo) o).CDPStatus;
        } else {
            status = ((LinuxClientPartitionInfo) o).CDPStatus;
        }
        if (status == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
            return res.get(basename, "o2_1");
        } else {
            return res.get(basename, "o2_0");
        }
    }

    public String getO2V(Object o) {
        int status;
        if (o instanceof LinuxVolumeGroupInfo) {
            return "";
        } else {
            status = ((LinuxVolumeInfo) o).CDPStatus;
        }
        if (status == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
            return res.get(basename, "o2_1");
        } else {
            return res.get(basename, "o2_0");
        }
    }

    public boolean DiskDisable(LinuxClientDiskInfo D, int o) {
        bShowCreateCDP = false;
        bShowDeleteCDP = false;
        bShowStartCDP = false;
        bShowStopCDP = false;
        bShowRestoreData = false;
        bShowRDetail = true;

        if (D.CDPType == CDPConstants.CDP_TYPE_DISK) {
            if (D.CDPStatus != CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowDeleteCDP = true;
            }
            if (D.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowStopCDP = true;
            } else {
                bShowStartCDP = true;
            }
        } else if (D.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
        } else {
            bShowCreateCDP = true;
        }

        if ((D.detail.RestoreStatus != CDPConstants.CDP_RESTORE_STATUS_RUNNING)
                && (D.CDPType == CDPConstants.CDP_TYPE_INIT)) {
            bShowRestoreData = true;
        }


        //CDPType改为（0:未设置 1:磁盘保护 2:分区保护 3：磁盘镜像 4：分区镜像）
        if (D.CDPType == 3 || D.CDPType == 4) {
            return false;
        }

        boolean isDest_Restore = this.disksDest_Restore.contains(D.device);
        boolean isSrc_Restore = this.disksSrc_Restore.contains(D.device);
        boolean ispdDest_Restore = this.pDisksDest_Restore.contains(D.device);
        boolean ispdSrc_Restore = this.pDisksSrc_Restore.contains(D.device);
        boolean isvSrc_Restore = this.vDisksSrc_Restore.contains(D.device);

        if (isDest_Restore) {//磁盘恢复
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = true;
            bShowRDetail = false;
        } else if (isSrc_Restore) {//磁盘镜像
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        } else if (ispdDest_Restore) {//分区恢复
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        } else if (ispdSrc_Restore) {//分区镜像
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        } else if (isvSrc_Restore) {//卷镜像
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        }


        switch (o) {
            case 1:
                if (D.CDPType == CDPConstants.CDP_TYPE_INIT) {
                    return bShowCreateCDP;
                } else {
                    return bShowDeleteCDP;
                }

            case 20:
                return bShowStartCDP;
            case 21:
                return bShowStopCDP;
            case 22:
                if (D.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                    return bShowStopCDP;
                } else {
                    return bShowStartCDP;
                }
            case 3:
                return bShowRestoreData;
            case 4:
                return bShowRDetail;
            default:
                return false;
        }
    }

    public boolean VGDisable(LinuxVolumeGroupInfo D, int o) {
        bShowCreateCDP = false;
        bShowDeleteCDP = false;
        bShowStartCDP = false;
        bShowStopCDP = false;
        bShowRestoreData = false;
        bShowRDetail = true;

        switch (o) {
            case 4:
                return bShowRDetail;
            default:
                return false;
        }
    }

    public boolean PartitionDisable(LinuxClientPartitionInfo P, int o) {
        bShowCreateCDP = false;
        bShowDeleteCDP = false;
        bShowStartCDP = false;
        bShowStopCDP = false;
        bShowRestoreData = false;
        bShowRDetail = true;

        if (P.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
            if (P.CDPStatus != CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowDeleteCDP = true;
            }
            if (P.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowStopCDP = true;
            } else {
                bShowStartCDP = true;
            }
        } else if (P.CDPType == CDPConstants.CDP_TYPE_DISK) {
        } else {
            bShowCreateCDP = true;
        }

        if ((P.detail.RestoreStatus != CDPConstants.CDP_RESTORE_STATUS_RUNNING)
                && (P.CDPType == CDPConstants.CDP_TYPE_INIT)) {
            bShowRestoreData = true;
        }

        //CDPType改为（0:未设置 1:磁盘保护 2:分区保护 3：磁盘镜像 4：分区镜像）
        if (P.CDPType == 3 || P.CDPType == 4) {
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = true;
        }

        //CDPType改为（0:未设置 1:磁盘保护 2:分区保护 3：磁盘镜像 4：分区镜像）
        if (P.diskCDPType == 1 || P.diskCDPType == 3 || P.diskCDPType == 4) {
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = true;
        }
//        
//        if(P.PartitionType == CDPConstants.LINUX_PARTITION_0x82 
//               || P.PartitionType == CDPConstants.LINUX_PARTITION_EXTENDED){
//            bShowCreateCDP = false;
//            bShowDeleteCDP = false;
//            bShowStartCDP = false;
//            bShowStopCDP = false;
//            bShowRestoreData = false;
//            bShowRDetail = true;
//        }
        boolean isDiskDest_Restore = this.disksDest_Restore.contains(P.device);
        boolean isDiskSrc_Restore = this.disksSrc_Restore.contains(P.device);

        String p = P.device + P.pDevice;

        boolean isDest_Restore = this.partitionsDest_Restore.contains(p);
        boolean isSrc_Restore = this.partitionsSrc_Restore.contains(p);
        boolean isvSrc_Restore = this.vPartitionsSrc_Restore.contains(p);

        if (isDiskDest_Restore) {//磁盘恢复
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        } else if (isDiskSrc_Restore) {//磁盘镜像
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        } else if (isDest_Restore) {//分区恢复
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = true;
            bShowRDetail = false;
        } else if (isSrc_Restore) {//分区镜像
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        } else if (isvSrc_Restore) {//卷镜像
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = false;
        }

        switch (o) {
            case 1:
                if (P.CDPType == CDPConstants.CDP_TYPE_INIT) {
                    return bShowCreateCDP;
                } else {
                    return bShowDeleteCDP;
                }
            case 20:
                return bShowStartCDP;
            case 21:
                return bShowStopCDP;
            case 22:
                if (P.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                    return bShowStopCDP;
                } else {
                    return bShowStartCDP;
                }
            case 3:
                return bShowRestoreData;
            case 4:
                return bShowRDetail;
            default:
                return false;
        }


    }

    public boolean PartitionDisableX(LinuxClientPartitionInfo P, int o) {
        bShowCreateCDP = true;
        bShowDeleteCDP = true;
        bShowStartCDP = true;
        bShowStopCDP = true;
        bShowRestoreData = true;
        bShowRDetail = true;

        if (P.PartitionType == CDPConstants.LINUX_PARTITION_0x82
                || P.PartitionType == CDPConstants.LINUX_PARTITION_EXTENDED) {
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = false;
            bShowRDetail = true;
        }

        switch (o) {
            case 1:
                if (P.CDPType == CDPConstants.CDP_TYPE_INIT) {
                    return bShowCreateCDP;
                } else {
                    return bShowDeleteCDP;
                }
            case 20:
                return bShowStartCDP;
            case 21:
                return bShowStopCDP;
            case 22:
                if (P.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                    return bShowStopCDP;
                } else {
                    return bShowStartCDP;
                }
            case 3:
                return bShowRestoreData;
            case 4:
                return bShowRDetail;
            default:
                return false;
        }


    }

    public void scanDisks() {
        boolean bRet = LinuxClientAPI.ScanDisks(client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "SDFailed"), global.get("error_mark")));
        }
    }

    public void scanVolumes() {
        boolean bRet = LinuxClientAPI.ScanVolumes(client);
        if (bRet) {
            connectHost();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "SVFailed"), global.get("error_mark")));
        }
    }

    public boolean VDisable(LinuxVolumeInfo P, int o) {
        bShowCreateCDP = false;
        bShowDeleteCDP = false;
        bShowStartCDP = false;
        bShowStopCDP = false;
        bShowRestoreData = false;
        bShowRDetail = true;

        if (P.CDPType == CDPConstants.CDP_TYPE_VOLUME) {
            if (P.CDPStatus != CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowDeleteCDP = true;
            }
            if (P.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                bShowStopCDP = true;
            } else {
                bShowStartCDP = true;
            }
        } else {
            bShowCreateCDP = true;
        }

        if ((P.detail.RestoreStatus != CDPConstants.CDP_RESTORE_STATUS_RUNNING)
                && (P.CDPType == CDPConstants.CDP_TYPE_INIT)) {
            bShowRestoreData = true;
        }


        String p = P.vgName + P.vName;

        boolean isDest_Restore = this.vDest_Restore.contains(p);

        if (isDest_Restore) {//卷恢复
            bShowCreateCDP = false;
            bShowDeleteCDP = false;
            bShowStartCDP = false;
            bShowStopCDP = false;
            bShowRestoreData = true;
            bShowRDetail = false;
        }

        switch (o) {
            case 1:
                if (P.CDPType == CDPConstants.CDP_TYPE_INIT) {
                    return bShowCreateCDP;
                } else {
                    return bShowDeleteCDP;
                }
            case 20:
                return bShowStartCDP;
            case 21:
                return bShowStopCDP;
            case 22:
                if (P.CDPStatus == CDPConstants.CDP_STATUS_HOOK_RUNNING) {
                    return bShowStopCDP;
                } else {
                    return bShowStartCDP;
                }
            case 3:
                return bShowRestoreData;
            case 4:
                return bShowRDetail;
            default:
                return false;
        }
    }

    public String getStrSize(long size, long total) {
        String s = Utility.getSizeString(size) + " GB";
        DecimalFormat df = new DecimalFormat("0.00");
        // 除数  
        BigDecimal bd = new BigDecimal(size);
        // 被除数  
        BigDecimal bd2 = new BigDecimal(total);
        // 进行除法运算,保留200位小数,末位使用四舍五入方式,返回结果  
        BigDecimal result = bd.divide(bd2, 200, BigDecimal.ROUND_HALF_DOWN);
        s = s + "（" + String.valueOf(df.format(result.multiply(new BigDecimal(100)))) + "%）";
        return s;
    }

    public String getDStrMirror(LinuxClientDiskInfo d) {
        if (d.CDPType == CDPConstants.CDP_TYPE_DISK) {
            return res.get(basename, "disk") + d.cdpDevice;
        } else {
            return "";
        }
    }

    public String getDStrStatus(LinuxClientDiskInfo d) {
        if (d.CDPType == CDPConstants.CDP_TYPE_DISK) {
            return Utility.getCDPStatusString(d.CDPStatus);
        } else if (d.CDPType == CDPConstants.CDP_TYPE_INIT) {
            return Utility.getCDPStatusString(d.CDPStatus, d.CDPType, d.detail.RestoreStatus);
        } else {
            return "";
        }
    }

    public String getPStrMirror(LinuxClientPartitionInfo p) {
        if (p.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
            return res.get(basename, "disk") + p.cdpDevice
                    + res.get(basename, "p") + p.cdpPDevice;
        } else {
            return "";
        }
    }

    public String getVStrMirror(LinuxVolumeInfo p) {
        if (p.CDPType == CDPConstants.CDP_TYPE_VOLUME) {
            return res.get(basename, "disk") + p.CDP_VG
                    + res.get(basename, "p") + p.CDP_V;
        } else {
            return "";
        }
    }

    public String getPStrStatus(LinuxClientPartitionInfo p) {
        if (p.CDPType == CDPConstants.CDP_TYPE_PARTITION) {
            return Utility.getCDPStatusString(p.CDPStatus);
        } else if (p.CDPType == CDPConstants.CDP_TYPE_INIT) {
            return Utility.getCDPStatusString(p.CDPStatus, p.CDPType, p.detail.RestoreStatus);
        } else {
            return "";
        }
    }

    public String getVStrStatus(LinuxVolumeInfo p) {
        if (p.CDPType == CDPConstants.CDP_TYPE_VOLUME) {
            return Utility.getCDPStatusString(p.CDPStatus);
        } else if (p.CDPType == CDPConstants.CDP_TYPE_INIT) {
            return Utility.getCDPStatusString(p.CDPStatus, p.CDPType, p.detail.RestoreStatus);
        } else {
            return "";
        }
    }

    public String dMirror() {
//        RequestContext.getCurrentInstance().execute("PF('block').show()");
        if (this.selectedD.CDPType == CDPConstants.CDP_TYPE_INIT) {
            String param = "ip=" + ip + "&port=" + port
                    + "&device=" + this.selectedD.device
                    + "&pCount=" + this.selectedD.PartitionCount
                    + "&size=" + this.selectedD.size
                    + "&cdpType=" + this.selectedD.CDPType;
            return "d_cdp_linux?faces-redirect=true&amp;" + param;
        } else {
            RequestContext.getCurrentInstance().execute("deleteDiskCDP.show()");
            return null;
        }
    }

    public String pMirror() {
        //       RequestContext.getCurrentInstance().execute("PF('block').show()");
        if (this.selectedP.CDPType == CDPConstants.CDP_TYPE_INIT) {
            String param = "ip=" + ip + "&port=" + port
                    + "&device=" + this.selectedP.device + "&pDevice=" + this.selectedP.pDevice
                    + "&pSize=" + this.selectedP.PartitionSize + "&cdpType=" + this.selectedP.CDPType;
            return "p_cdp_linux?faces-redirect=true&amp;" + param;
        } else {
            RequestContext.getCurrentInstance().execute("deletePartitionCDP.show()");
            return null;
        }
    }

    public String dRestore() {
        this.selectedD = (LinuxClientDiskInfo) this.selected;
        String mirrorNum = "";
        if (selectedD.detail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            mirrorNum = selectedD.detail.cdpDevice;
        }
        String param = "ip=" + ip + "&port=" + port
                + "&device=" + this.selectedD.device
                + "&pCount=" + this.selectedD.PartitionCount
                + "&size=" + this.selectedD.size
                + "&cdpType=" + this.selectedD.CDPType
                + "&mirror=" + mirrorNum;
        Debug.print("linuxClientBean --> linuxDiskRestoreBean:" + param);
        return "d_restore_linux?faces-redirect=true&amp;" + param;
    }

    public String pRestore() {
        this.selectedP = (LinuxClientPartitionInfo) this.selected;
        String dMirror = "";
        String pMirror = "";
        if (selectedP.detail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            dMirror = selectedP.detail.cdpDevice;
            pMirror = selectedP.detail.cdpPDevice;
        }
        String param = "ip=" + ip + "&port=" + port
                + "&device=" + this.selectedP.device + "&pDevice=" + this.selectedP.pDevice
                + "&pSize=" + this.selectedP.PartitionSize + "&cdpType=" + this.selectedP.CDPType
                + "&dMirror=" + dMirror
                + "&pMirror=" + pMirror;
        return "p_restore_linux?faces-redirect=true&amp;" + param;
    }

    public String dDetails() {
        this.selectedD = (LinuxClientDiskInfo) this.selected;
        if (selectedD.CDPType == CDPConstants.CDP_STATUS_INIT && selectedD.detail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            if (selectedD.detail.SyncPercent != 100) {
                String param = "ip=" + ip + "&port=" + port + "&device=" + selectedD.device;
                return "d_restore_pro_LinuxX?faces-redirect=true&amp;" + param;
            }
        }
        String param = "ip=" + ip + "&port=" + port + "&device=" + selectedD.device;
        return "disk_linux?faces-redirect=true&amp;" + param;
    }

    public String pDetails() {
        this.selectedP = (LinuxClientPartitionInfo) this.selected;
        if (selectedP.CDPType == CDPConstants.CDP_STATUS_INIT && selectedP.detail.RestoreStatus == CDPConstants.CDP_RESTORE_STATUS_RUNNING) {
            if (selectedP.detail.SyncPercent != 100) {
                String param = "ip=" + ip + "&port=" + port + "&device=" + selectedP.device + "&pDevice=" + selectedP.pDevice;
                return "p_restore_pro_linux?faces-redirect=true&amp;" + param;
            }
        }
        String param = "ip=" + ip + "&port=" + port + "&device=" + selectedP.device
                + "&pDevice=" + selectedP.pDevice;
        return "partition_linux?faces-redirect=true&amp;" + param;
    }

    public String getStrSize(long size) {
        return Utility.getSizeString(size) + " GB";
    }

    public String getStrType(int type) {
        return Utility.getCDPTypeString(type);
    }

    public LinuxClientDiskInfo getSelectedD() {
        return selectedD;
    }

    public void setSelectedD(LinuxClientDiskInfo selectedD) {
        this.selectedD = selectedD;
    }

    public LinuxClientPartitionInfo getSelectedP() {
        return selectedP;
    }

    public void setSelectedP(LinuxClientPartitionInfo selectedP) {
        this.selectedP = selectedP;
    }

    public ClientInfo getClient() {
        return client;
    }

    public void setClient(ClientInfo client) {
        this.client = client;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Object getSelected() {
        return selected;
    }

    public void setSelected(Object selected) {
        this.selected = selected;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getRootV() {
        return rootV;
    }

    public void setRootV(TreeNode rootV) {
        this.rootV = rootV;
    }
    public int mode = 1;
    public int auto = 3;
    public boolean immediate;
    public boolean fast = false;
    public boolean protectAfterRestart;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public boolean isProtectAfterRestart() {
        return protectAfterRestart;
    }

    public void setProtectAfterRestart(boolean protectAfterRestart) {
        this.protectAfterRestart = protectAfterRestart;
    }

    public boolean isFast() {
        return fast;
    }

    public void setFast(boolean fast) {
        this.fast = fast;
    }

    public String dStartCDP() {
        if (!this.protectAfterRestart) {
            auto = 0;
        }
        int f = this.fast ? 1 : 0;
        boolean bRet = LinuxClientAPI.StartCDPSyn(CDPConstants.CDP_TYPE_DISK, mode, auto, f, client,
                this.device, this.pDevice);
        if (bRet) {
            connectHost();
            if (this.immediate) {
                MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
                LinuxClientDiskDetail DiskDetail = LinuxClientAPI.GetSourceDiskDetail(srcDevice, this.client);
                if (DiskDetail == null) {
                    LinuxClientAPI.GetErrorCode();
                    return null;
                }
                cdp.createMirrorProtectLinux(ip, String.valueOf(port), this.device, "", true, DiskDetail.SerialNumber);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail21"), global.get("error_mark")));
            return null;
        }
        String param = "ip=" + ip + "&port=" + port;
        return "client_linux.xhtml?faces-redirect=true&amp;" + param;
    }

    public String pStartCDP() {
        if (!this.protectAfterRestart) {
            auto = 0;
        }
        int f = this.fast ? 1 : 0;
        boolean bRet = LinuxClientAPI.StartCDPSyn(CDPConstants.CDP_TYPE_PARTITION, mode, auto, f, client,
                this.device, this.pDevice);
        if (bRet) {
            connectHost();
            if (this.immediate) {
                MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
                LinuxClientDiskDetail DiskDetail = LinuxClientAPI.GetSourceDiskDetail(this.srcDevice, this.client);
                if (DiskDetail == null) {
                    LinuxClientAPI.GetErrorCode();
                    return null;
                }
                cdp.createMirrorProtectLinux(ip, String.valueOf(port), this.device, this.pDevice, false, DiskDetail.SerialNumber);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail31"), global.get("error_mark")));
        }
        String param = "ip=" + ip + "&port=" + port;
        return "client_linux.xhtml?faces-redirect=true&amp;" + param;
    }

    public String vStartCDP() {
        if (!this.protectAfterRestart) {
            auto = 0;
        }
        int f = this.fast ? 1 : 0;
        boolean bRet = LinuxClientAPI.StartVCDPSyn(CDPConstants.CDP_TYPE_VOLUME, mode, auto, f, client,
                this.device, this.pDevice);
        if (bRet) {
            connectHost();
            if (this.immediate) {
                MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
                LinuxClientDiskDetail DiskDetail = LinuxClientAPI.GetSourceDiskDetail(srcDevice, this.client);
                if (DiskDetail == null) {
                    LinuxClientAPI.GetErrorCode();
                    return null;
                }
                cdp.createMirrorProtectLinuxV(ip, String.valueOf(port), this.device, this.pDevice, DiskDetail.SerialNumber);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail41"), global.get("error_mark")));
            return null;
        }
        String param = "ip=" + ip + "&port=" + port;
        return "client_linux.xhtml?faces-redirect=true&amp;" + param;
    }

    public String returnStr() {
        String param = "ip=" + ip + "&port=" + port;
        return "client_linux.xhtml?faces-redirect=true&amp;" + param;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String jumpToRestoring() {
        if (this.dRestoring) {
            return this.dJumpPara;
        } else if (this.pRestoring) {
            return this.pJumpPara;
        } else if (this.vRestoring) {
            return this.vJumpPara;
        } else {
            return null;
        }
    }
    private boolean dRestoring = false;
    private boolean pRestoring = false;
    private boolean vRestoring = false;
    private String dJumpPara;
    private String pJumpPara;
    private String vJumpPara;

    public LinuxVolumeGroupInfo getSelectedVG() {
        return selectedVG;
    }

    public void setSelectedVG(LinuxVolumeGroupInfo selectedVG) {
        this.selectedVG = selectedVG;
    }

    public LinuxVolumeInfo getSelectedV() {
        return selectedV;
    }

    public void setSelectedV(LinuxVolumeInfo selectedV) {
        this.selectedV = selectedV;
    }

    public MSAResource getRes() {
        return res;
    }

    public void setRes(MSAResource res) {
        this.res = res;
    }

    public MSAGlobalResource getGlobal() {
        return global;
    }

    public void setGlobal(MSAGlobalResource global) {
        this.global = global;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public boolean isbShowCreateCDP() {
        return bShowCreateCDP;
    }

    public void setbShowCreateCDP(boolean bShowCreateCDP) {
        this.bShowCreateCDP = bShowCreateCDP;
    }

    public boolean isbShowDeleteCDP() {
        return bShowDeleteCDP;
    }

    public void setbShowDeleteCDP(boolean bShowDeleteCDP) {
        this.bShowDeleteCDP = bShowDeleteCDP;
    }

    public boolean isbShowStartCDP() {
        return bShowStartCDP;
    }

    public void setbShowStartCDP(boolean bShowStartCDP) {
        this.bShowStartCDP = bShowStartCDP;
    }

    public boolean isbShowStopCDP() {
        return bShowStopCDP;
    }

    public void setbShowStopCDP(boolean bShowStopCDP) {
        this.bShowStopCDP = bShowStopCDP;
    }

    public boolean isbShowRestoreData() {
        return bShowRestoreData;
    }

    public void setbShowRestoreData(boolean bShowRestoreData) {
        this.bShowRestoreData = bShowRestoreData;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getpDevice() {
        return pDevice;
    }

    public void setpDevice(String pDevice) {
        this.pDevice = pDevice;
    }

    public String getSrcDevice() {
        return srcDevice;
    }

    public void setSrcDevice(String srcDevice) {
        this.srcDevice = srcDevice;
    }

    public boolean isbShowRDetail() {
        return bShowRDetail;
    }

    public void setbShowRDetail(boolean bShowRDetail) {
        this.bShowRDetail = bShowRDetail;
    }

    public boolean isDRestoring() {
        return dRestoring;
    }

    public void setDRestoring(boolean dRestoring) {
        this.dRestoring = dRestoring;
    }

    public boolean isPRestoring() {
        return pRestoring;
    }

    public void setPRestoring(boolean pRestoring) {
        this.pRestoring = pRestoring;
    }

    public boolean isVRestoring() {
        return vRestoring;
    }

    public void setVRestoring(boolean vRestoring) {
        this.vRestoring = vRestoring;
    }

    public String getDJumpPara() {
        return dJumpPara;
    }

    public void setDJumpPara(String dJumpPara) {
        this.dJumpPara = dJumpPara;
    }

    public String getPJumpPara() {
        return pJumpPara;
    }

    public void setPJumpPara(String pJumpPara) {
        this.pJumpPara = pJumpPara;
    }

    public String getVJumpPara() {
        return vJumpPara;
    }

    public void setVJumpPara(String vJumpPara) {
        this.vJumpPara = vJumpPara;
    }

    public void change() {
    }
}
