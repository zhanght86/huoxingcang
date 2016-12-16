/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.treeNode;

import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.model.IconData;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyEncryp;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
import com.marstor.msa.mdu.util.Debug;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.net.URL;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "treeBean")
@ViewScoped
public class TreeBean implements Serializable {

    private MSAResource res = new MSAResource();
    private static final Logger logger = Logger.getLogger(TreeBean.class.getName());
    private TreeNode root_sys;
    private TreeNode root_backup;
    private TreeNode root_equip;
    private TreeNodeData root_cloud;
    private TreeNodeData root_fun;
    private TreeNodeData selectedNode;
    private TreeNode[] selectedNodes;
    private URL IMG_URL_Device = getClass().getResource("../resources/picture/edit.jpg");
    private boolean notTreenode = true;
    private boolean notMonitor = true;
    private boolean containECD = false;

    public TreeBean() {
        initTree();
    }

    private void initTree() {
        CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
        Reg[] license = commonInterfaceInstance.getLicense();

        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        int currUserType = user.getType();
        System.out.println("currUserType=" + currUserType);
        int sysvol_state = (Integer) (session.getAttribute("sysvolstate"));
        if (sysvol_state != 1 || currUserType == 4) {
            notTreenode = true;
        } else {
            notTreenode = false;
        }

        //当没有系统卷时控制器界面都亮
        notMonitor = false;
        if (currUserType == 4) {
            notMonitor = true;
        } else {
            notMonitor = false;
        }
        session.setAttribute("notMonitor", notMonitor);
        session.setAttribute("notTreenode", notTreenode);

        // 创建系统管理树根节点
        IconData data = new IconData("sysRoot", "#", "systemTitleIcon");
        root_sys = new TreeNodeData(data, null, "#");

        // 创建系统管理树叶子节点
        if (currUserType == 4) {
            data = new IconData(res.get("Equip"), "", "controllerTitleIcon", "treenodedisable");
            TreeNode sys_equipNode = new TreeNodeData(data, root_sys, "../common/monitor.xhtml");
            data = new IconData(res.get("User"), "../common/system_user.xhtml", "userTitleIcon", "treenodeenable");
            TreeNode sys_useNode = new TreeNodeData(data, root_sys, "../common/system_user.xhtml");
            data = new IconData(res.get("Time"), "", "timeTitleIcon", "treenodedisable");
            TreeNode sys_timeNode = new TreeNodeData(data, root_sys, "#");
            data = new IconData(res.get("Load"), "", "statusTitleIcon", "treenodedisable");
            TreeNode sys_hardNode = new TreeNodeData(data, root_sys, "../common/system_state_chart.xhtml");
            data = new IconData(res.get("Email"), "", "mailTitleIcon", "treenodedisable");
            TreeNode sys_mailNode = new TreeNodeData(data, root_sys, "../common/system_warnemail.xhtml");
            data = new IconData(res.get("Log"), "", "logTitleIcon", "treenodedisable");
            TreeNode sys_logNode = new TreeNodeData(data, root_sys, "../common/system_log.xhtml");
            data = new IconData(res.get("Service"), "", "serviceTitleIcon", "treenodedisable");
            TreeNode sys_serviceNode = new TreeNodeData(data, root_sys, "../common/system_service.xhtml");
        } else {
            data = new IconData(res.get("Equip"), "../common/monitor.xhtml", "controllerTitleIcon", "treenodeenable");
            TreeNode sys_equipNode = new TreeNodeData(data, root_sys, "../common/monitor.xhtml");
            data = new IconData(res.get("User"), "../common/system_user.xhtml", "userTitleIcon", "treenodeenable");
            TreeNode sys_useNode = new TreeNodeData(data, root_sys, "../common/system_user.xhtml");
            data = new IconData(res.get("Time"), "../common/system_time.xhtml", "timeTitleIcon", "treenodeenable");
            TreeNode sys_timeNode = new TreeNodeData(data, root_sys, "#");
            data = new IconData(res.get("Load"), "../common/system_state_chart.xhtml", "statusTitleIcon", "treenodeenable");
            TreeNode sys_hardNode = new TreeNodeData(data, root_sys, "../common/system_state_chart.xhtml");
            data = new IconData(res.get("Email"), "../common/system_warnemail.xhtml", "mailTitleIcon", "treenodeenable");
            TreeNode sys_mailNode = new TreeNodeData(data, root_sys, "../common/system_warnemail.xhtml");
            data = new IconData(res.get("Log"), "../common/system_log.xhtml", "logTitleIcon", "treenodeenable");
            TreeNode sys_logNode = new TreeNodeData(data, root_sys, "../common/system_log.xhtml");
            data = new IconData(res.get("Service"), "../common/system_service.xhtml", "serviceTitleIcon", "treenodeenable");
            TreeNode sys_serviceNode = new TreeNodeData(data, root_sys, "../common/system_service.xhtml");
        }

        // 创建设备管理树根节点
        data = new IconData(res.get("ScsiRoot"), "#", "deviceTitleIcon");
        root_equip = new TreeNodeData(data, null, "#");

        data = new IconData(res.get("Network"), "../common/system_network.xhtml", "netManageTitleIcon");
        TreeNode sys_netNode = new TreeNodeData(data, root_equip, "#");

        // 创建卷组管理树子节点
        data = new IconData(res.get("VolumeList"), "../volume/volumegroup.xhtml", "raidVolumnTitleIcon");
        TreeNode volume_listNode = new TreeNodeData(data, root_equip, "../volume/volumegroup.xhtml");
        data = new IconData(res.get("DiskList"), "../volume/disklist.xhtml", "physicDiskTitleIcon");
        TreeNode volume_diskNode = new TreeNodeData(data, root_equip, "../volume/disklist.xhtml");

        // 创建SCSI树子节点
        data = new IconData(res.get("HostGroup"), "../scsi/scsi_host.xhtml", "hostGroupTitleIcon");
        TreeNode scsi_hostNode = new TreeNodeData(data, root_equip, "#");
        data = new IconData(res.get("Target"), "../scsi/scsi_target.xhtml", "targetTitleIcon");
        TreeNode scsi_targetNode = new TreeNodeData(data, root_equip, "#");
        data = new IconData(res.get("Initiator"), "../scsi/scsi_initiator.xhtml", "initiatorTitleIcon");
        TreeNode scsi_iscsi_initiatorNode = new TreeNodeData(data, root_equip, "#");

        // 创建存储功能树叶子节点
        if (null == license) {
            return;
        }

        //快照与同步模块 更名为复制 复制归入系统内
//        for (int i = 0; i < license.length; i++) {
//            if (7 == license[i].getModuleID() && Module.FUNCTIONID_COMMON == license[i].getFunctionID()) {
        //快照与同步模块根节点
        data = new IconData(res.get("Copy"), "../sync/sync_list_mars_host.xhtml", "disasterTitleIcon");
        TreeNode fun_syncNode = new TreeNodeData(data, root_equip, "../sync/sync_list_mars_host.xhtml");
//            }
//        }

        // 创建存储功能树根节点
        data = new IconData(res.get("FunRoot"), "#", "storageTitleIcon");
        root_fun = new TreeNodeData(data, null, "#");

        // 创建盘阵和CDP模块
        // 盘阵和CDP模块根节点
        data = new IconData(res.get("Disk"), "../cdp/da_cdp.xhtml", "diskCDPTitleIcon");
        TreeNodeData fun_cdpNode = new TreeNodeData(data, root_fun, "#");

        // 盘阵和CDP模块子节点   
        data = new IconData(res.get("DiskGroup"), "../cdp/dgs.xhtml", "diskGroupTitleIcon");
        TreeNode fun_cdp_diskGroupNode = new TreeNodeData(data, fun_cdpNode, "#");
        data = new IconData(res.get("CDPProtect"), "../cdp/cdp.xhtml", "cdpProtectTitleIcon");
        TreeNode fun_cdp_diskGroupNode1 = new TreeNodeData(data, fun_cdpNode, "#");

        for (int i = 0; i < license.length; i++) {
            if (Module.MODULE_CDP == license[i].getModuleID() && Module.FUNCTIONID_CDP_CAPACITY == license[i].getFunctionID()) {
                if (0 != Integer.valueOf(license[i].getFunctionNumber())) {
                    data = new IconData(res.get("VolumeCopy"), "../cdp/clients.xhtml", "hostImageTitleIcon");
                    TreeNode fun_cdp_mirrorNode = new TreeNodeData(data, fun_cdpNode, "#");
                }
            }
        }

        // 虚拟磁带库模块
        for (int i = 0; i < license.length; i++) {
            if (Module.MODULE_VTL == license[i].getModuleID() && Module.FUNCTIONID_VTL == license[i].getFunctionID()) {
                // 虚拟磁带库模块
                data = new IconData(res.get("VTL"), "../vtl/vtl_root.xhtml", "vitualTapeTitleIcon");
                TreeNodeData fun_vtlNode = new TreeNodeData(data, root_fun, "#");

                // 虚拟磁带库模块
                data = new IconData(res.get("StorgeSpace"), "../vtl/storage_area.xhtml", "storageSpaceTitleIcon");
                TreeNode fun_vtl_storage = new TreeNodeData(data, fun_vtlNode, "#");
                data = new IconData(res.get("TapeLibrary"), "../vtl/vtls.xhtml", "tapeLibraryTitleIcon");
                TreeNode fun_vtl_libraryNode = new TreeNodeData(data, fun_vtlNode, "#");
                data = new IconData(res.get("TapeHolder"), "../vtl/vaults.xhtml", "tapeholderTitleIcon");
                TreeNode fun_vtl_ShelfNode = new TreeNodeData(data, fun_vtlNode, "#");
            }
        }

        // NAS网络存储模块
        for (int i = 0; i < license.length; i++) {
            if (Module.MODULE_NAS == license[i].getModuleID() && Module.FUNCTIONID_COMMON == license[i].getFunctionID()) {
                // NAS网络存储模块
                data = new IconData(res.get("NAS"), "../nas/nas_display.xhtml", "nasTitleIcon");
                TreeNodeData fun_nasNode = new TreeNodeData(data, root_fun, "#");

                data = new IconData(res.get("Share"), "../nas/nas_path.xhtml", "shareTitleIcon");
                TreeNode fun_nas_pathNode = new TreeNodeData(data, fun_nasNode, "#");
                data = new IconData(res.get("CIFS"), "../nas/nas_domain_config.xhtml", "cifsTitleIcon");
                TreeNode fun_nas_global_CIFSNode = new TreeNodeData(data, fun_nasNode, "#");
                data = new IconData(res.get("NFS"), "../nas/nas_global_nfs.xhtml", "nfsTitleIcon");
                TreeNode fun_nas_global_cifs_Node = new TreeNodeData(data, fun_nasNode, "#");
            }
        }

        //虚拟机模块
        for (int i = 0; i < license.length; i++) {
            if (Module.MODULE_VM == license[i].getModuleID() && Module.FUNCTIONID_VM == license[i].getFunctionID()) {
                // 虚拟机模块根节点
                data = new IconData(res.get("VMware"), "../vm/vm.xhtml", "virtualMachineTitleIcon");
//                 data = new IconData(res.get("VMware"), "/vbox/","virtualMachineTitleIcon");
                TreeNodeData fun_vmNode = new TreeNodeData(data, root_fun, "#");

                // 虚拟机模块子节点
                data = new IconData(res.get("VMwareList"), "../vm/vm_VMListTable.xhtml", "virtualManageTitleIcon");
                TreeNode fun_vm_listNode = new TreeNodeData(data, fun_vmNode, "../vm/vm_VMListTable.xhtml");
                data = new IconData(res.get("ISO"), "../vm/vm_isoInfoTable.xhtml", "isoFileTitleIcon");
                TreeNode fun_vm_ISONode = new TreeNodeData(data, fun_vmNode, "../vm/vm_isoInfoTable.xhtml");
                data = new IconData(res.get("CleanDisk"), "../vm/vm_clearDiskInfoTable.xhtml", "cleanVirtualDiskTitleIcon");
                TreeNode fun_vm_diskNode = new TreeNodeData(data, fun_vmNode, "../vm/vm_clearDiskInfoTable.xhtml");
            }
        }

        // 创建备份功能树根节点
        data = new IconData(res.get("VolumeRoot"), "#", "backupTitleIcon");
        root_backup = new TreeNodeData(data, null, "#");

        // 创建统一备份模块
        for (int i = 0; i < license.length; i++) {
            if (Module.MODULE_MBA == license[i].getModuleID()) {
                // 备份模块根节点
                if (!InterfaceFactory.getMBAInterfaceInstance().judgeMBAIsInstall()) {
                    data = new IconData(res.get("Backup"), "../backups/mba_not_install.xhtml", "uniteBackupTitleIcon");
                    TreeNodeData fun_backupNode = new TreeNodeData(data, root_backup, "#");
                    break;
                }
                data = new IconData(res.get("Backup"), "../backups/backup_state.xhtml", "uniteBackupTitleIcon");
                TreeNodeData fun_backupNode = new TreeNodeData(data, root_backup, "#");

                // 备份模块子节点
                data = new IconData(res.get("Center"), "../backups/backup_center.xhtml", "backupCenterTitleIcon");
                TreeNode fun_backup_ServiceNode = new TreeNodeData(data, fun_backupNode, "../backups/backup_center.xhtml");
                data = new IconData(res.get("Maintenance"), "../backups/maintenance.xhtml", "sysMaintenanceTitleIcon");
                TreeNode fun_backup_cleanNode = new TreeNodeData(data, fun_backupNode, "../backups/maintenance.xhtml");
                data = new IconData(res.get("Client"), "../backups/agent_manage.xhtml", "clientTitleIcon");
                TreeNode fun_backup_proNode = new TreeNodeData(data, fun_backupNode, "../backups/agent_manage.xhtml");
            }
        }

        // Oracle备份模块
        for (int i = 0; i < license.length; i++) {
            if ((Module.MODULE_ORACLE == license[i].getModuleID() && 1 == license[i].getFunctionID())) {
                // Oracle模块根节点
                IconData data1 = new IconData(res.get("Oracle"), "../oracle/oracle_root.xhtml", "oracleBackupTitleIcon");
                TreeNodeData fun_oracleNode = new TreeNodeData(data1, root_backup, "#");

                // Oracle模块子节点
                IconData data2 = new IconData(res.get("NetServiceName"), "../oracle/oracle_nsname.xhtml", "netServiceTitleIcon");
                TreeNode fun_oracle_nsNameNode = new TreeNodeData(data2, fun_oracleNode, "../oracle/oracle_nsname.xhtml");
                data = new IconData(res.get("Database"), "../oracle/oracle_database.xhtml", "databaseTitleIcon");
                TreeNode fun_oracle_databaseNode = new TreeNodeData(data, fun_oracleNode, "../oracle/oracle_database.xhtml");
            }
        }

        for (int i = 0; i < license.length; i++) {
            if ((Module.MODULE_VMware == license[i].getModuleID() && Module.FUNCTIONID_VMware == license[i].getFunctionID()) ||
                 (Module.MODULE_DM == license[i].getModuleID() && Module.FUNCTIONID_DM == license[i].getFunctionID()) ||
                  (Module.MODULE_KingBase == license[i].getModuleID() && Module.FUNCTIONID_KingBase == license[i].getFunctionID()) ||
                   (Module.MODULE_GBASE == license[i].getModuleID() && Module.FUNCTIONID_GBASE == license[i].getFunctionID())||
                   (Module.MODULE_FILE == license[i].getModuleID() && Module.FUNCTIONID_FILE == license[i].getFunctionID())||
                   (Module.MODULE_ORACLE_MBA== license[i].getModuleID() && Module.FUNCTIONID_ORACLE_MBA  == license[i].getFunctionID())||
                   (Module.MODULE_MYSQL== license[i].getModuleID() && Module.FUNCTIONID_MYSQL == license[i].getFunctionID())||
                   (Module.MODULE_MSSQL== license[i].getModuleID() && Module.FUNCTIONID_MSSQL == license[i].getFunctionID())
                    ) {
                if (user.type != 2) {
                    data = new IconData(res.get("AdvanceBackup"), "javascript:void(0);", "advanceBackupIcon", "", "");
                } else {
                    data = new IconData(res.get("AdvanceBackup"), "/template/gotoMBA.xhtml", "advanceBackupIcon", "", "_blank");
                }
                TreeNodeData vmware_BackupNode = new TreeNodeData(data, root_backup, "#");
                break;
            }
        }

        for (int i = 0; i < license.length; i++) {
            if ((Module.MODULE_ECD == license[i].getModuleID() && Module.FUNCTIONID_ECD == license[i].getFunctionID())) {
                containECD = true;
                data = new IconData(res.get("Cloud"), "../ecd/template/login.xhtml", "cloudDiskIcon", "", "_blank");
                TreeNodeData ecd_Node = new TreeNodeData(data, root_fun, "#");
                break;
            }
        }
        session.setAttribute("containECD", containECD);

    }

//    public void ECDRegister(int groupNum, int userNum) {
//        Debug.print("groupNum=" + groupNum);
//        Debug.print("userNum=" + userNum);
//        JavaDBConnect db = new JavaDBConnect();
//        if (!db.connect()) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "connect db failed!", ""));
//            return;
//        }
//        db.update(groupNum, userNum);
//    }
    public TreeNode getRoot_sys() {
        return root_sys;
    }

    public TreeNode getRoot_backup() {
        return root_backup;
    }

    public TreeNode getRoot_equip() {
        return root_equip;
    }

    public TreeNodeData getRoot_fun() {
        return root_fun;
    }

    public TreeNodeData getRoot_cloud() {
        return root_cloud;
    }

    public void setRoot_cloud(TreeNodeData root_cloud) {
        this.root_cloud = root_cloud;
    }

    public void setRoot_fun(TreeNodeData root_fun) {
        this.root_fun = root_fun;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public TreeNodeData getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNodeData selectedNode) {
        this.selectedNode = selectedNode;
    }

    public boolean isNotTreenode() {
        return notTreenode;
    }

    public void setNotTreenode(boolean notTreenode) {
        this.notTreenode = notTreenode;
    }

    public boolean isNotMonitor() {
        return notMonitor;
    }

    public void setNotMonitor(boolean notMonitor) {
        this.notMonitor = notMonitor;
    }

    public void onNodeExpand(NodeExpandEvent event) {
        SystemOutPrintln.print_common("扩展的结点:" + event.getTreeNode().toString());
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Expanded", event.getTreeNode().toString());

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        SystemOutPrintln.print_common("收的结点:" + event.getTreeNode().toString());
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Collapsed", event.getTreeNode().toString());

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onNodeSelect(NodeSelectEvent event) {
        SystemOutPrintln.print_common("选中的结点:" + event.getTreeNode().toString());
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onNodeUnselect(NodeUnselectEvent event) {
        SystemOutPrintln.print_common("未选中的结点:" + event.getTreeNode().toString());
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getTreeNode().toString());

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onDragDrop(DragDropEvent event) {
        TreeNode node = (TreeNode) event.getData();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "DragDrop", node + " moved to " + node.getParent());

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void displaySelectedMultiple() {
        if (selectedNodes != null && selectedNodes.length > 0) {
            StringBuilder builder = new StringBuilder();

            for (TreeNode node : selectedNodes) {
                builder.append(node.getData().toString());
                builder.append("<br />");
            }

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", builder.toString());

            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void displaySelectedSingle() {
        if (selectedNode != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", selectedNode.getData().toString());

            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void deleteNode() {

        selectedNode.getChildren().clear();
        selectedNode.getParent().getChildren().remove(selectedNode);
        selectedNode.setParent(null);

        selectedNode = null;
    }

    public String setResource(String resource) {
        return java.util.ResourceBundle.getBundle("com/marstor/msa/common/resources/messages").getString(resource);
    }

    public void onSelectNode(NodeSelectEvent event) {

//      System.out.println("%%%%%%%%%%%%%%%%%%%%url");
        event.getTreeNode().setSelected(true);
        event.getTreeNode().setExpanded(true);

        IconData data = (IconData) event.getTreeNode().getData();
        String name = data.getM_strLabel();
        String url = data.getM_URL();
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("name", name);
        context.addCallbackParam("url", url);
        context.addCallbackParam("target", "right_frame");
//            

//            FacesContext
//                    .getCurrentInstance()
//                    .getApplication()
//                    .getNavigationHandler()
//                    .handleNavigation(FacesContext.getCurrentInstance(),
//                    data.getM_strLabel(), data.getM_URL()+""+"?faces-redirect=true");
//            System.out.println("输出++++++++="+"../nas/"+name);
    }

    public void onSelectCloudNode(NodeSelectEvent event) {

//      System.out.println("%%%%%%%%%%%%%%%%%%%%url");
        event.getTreeNode().setSelected(true);

        IconData data = (IconData) event.getTreeNode().getData();
        String name = data.getM_strLabel();
        String url = data.getM_URL();
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("name", name);
        context.addCallbackParam("url", url);
        context.addCallbackParam("target", "_blank");
//            

//            FacesContext
//                    .getCurrentInstance()
//                    .getApplication()
//                    .getNavigationHandler()
//                    .handleNavigation(FacesContext.getCurrentInstance(),
//                    data.getM_strLabel(), data.getM_URL()+""+"?faces-redirect=true");
//            System.out.println("输出++++++++="+"../nas/"+name);
    }

    public void onFunctionSelectNode(NodeSelectEvent event) {

//      System.out.println("%%%%%%%%%%%%%%%%%%%%url");
        event.getTreeNode().setSelected(true);
        event.getTreeNode().setExpanded(true);

        IconData data = (IconData) event.getTreeNode().getData();
        String name = data.getM_strLabel();
        String url = data.getM_URL();
//            System.out.println("%%%%%%%%%%%%%%%%%%%%url="+url);
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("name", name);
        context.addCallbackParam("url", url);
        context.addCallbackParam("target", "right_frame");
//            

//            FacesContext
//                    .getCurrentInstance()
//                    .getApplication()
//                    .getNavigationHandler()
//                    .handleNavigation(FacesContext.getCurrentInstance(),
//                    data.getM_strLabel(), data.getM_URL()+""+"?faces-redirect=true");
//            System.out.println("输出++++++++="+"../nas/"+name);
    }

    public void onTabChange(TabChangeEvent event) {
        String tab = event.getTab().getTitle();
        RequestContext.getCurrentInstance().execute("javascript:alert('"+ tab +"');");
        Debug.print("TAB Title" + event.getTab().getTitle());
        if (event.getTab().getTitle().equals("企业网盘")) {
            RequestContext.getCurrentInstance().execute("javascript:toECD();");
        }
    }

    public String getStrURL() {
        return "../nas/vm_isoInfoTable.xhtml";
    }

    public String StylehasAuthority2MBA(int usertype, String linkname) {
        if (linkname.equals("VMWare备份") && (usertype != 2)) {
            return "color:#ccc;";
        }
        return "";
    }

    public boolean isContainECD() {
        return containECD;
    }

    public void setContainECD(boolean containECD) {
        this.containECD = containECD;
    }

}
