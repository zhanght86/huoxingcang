/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.DriveInformation;
import com.marstor.msa.bean.TapeInformation;
import com.marstor.msa.bean.VaultInformation;
import com.marstor.msa.cdp.managedbean.*;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.util.MyConstants;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@ManagedBean(name = "vaultsBean")
@ViewScoped
public class VaultsBean implements Serializable {

    private ArrayList<VaultInformation> diskGroups = new ArrayList();
    private VaultInformation selectedDG;
    private TapeInformation selectedD;
    private DriveInformation sDrive;
    private List<TapeInformation> disks;
    private Map<Integer, TapeInformation[]> disksMap = new HashMap();
    private List<Details> dgInfo = new ArrayList();
    private Map dgInfos = new HashMap();
    private DriveInformation[] lun;
    private Map<Integer, DriveInformation[]> luns = new HashMap();
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.vaults";
    VaultInformation[] vaults;
    public int userType = -1;
    public int VAULT_TYPE_OFFLINE = -1;
    private TapeInformation[] tapes;
    public String valutName;

    public VaultsBean() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        vaults = vtl.getVaults();
        initDiskGroups();
        user();
    }
    
    public void user() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        userType = user.getType();
    }
    

    public String addVtl() {
        int vaultsCount = 0;
        //获取当前带架数量
        if(vaults==null || vaults.length<1){
            vaultsCount = 0;
        }else{
            vaultsCount = vaults.length;
        }
        if (vaultsCount >= MyConstants.MAX_VAULTS) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get(basename, "vaults_full"), ""));
            return null;
        }

        return "add_vault?faces-redirect=true";
    }
    
     public void deleteVault(){
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        tapes = vtl.getVaultTapesInfo(this.selectedDG.id);
        this.valutName = this.selectedDG.name;
        if (tapes != null && tapes.length > 0) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "vault_nodel"), ""));
            RequestContext.getCurrentInstance().execute("delTapes.show()");
            return;
        } else {
            RequestContext.getCurrentInstance().execute("deleteVault.show()"); 
        }
    }
     public String moveTapes_real() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
         for (TapeInformation tape : tapes) {
             if (tape.state != MoveTape.TAPE_STATE_ENABLE) {
                continue;
            }
            if (tape.state == MoveTape.TAPE_STATE_CREATING) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "creating_nomove"), ""));
                return null;
            }
            if (tape.state == MoveTape.TAPE_STATE_FAILED) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "damage_nomove"), ""));
                return null;
            }
            SystemOutPrintln.print_vtl("tape.getTapeID()="+tape.id+"destVaultID="+MyConstants.VAULT_TYPE_FREE);
            boolean ret = vtl.moveTapeFromVaultToVault(tape.id, MyConstants.VAULT_TYPE_FREE);
            if (!ret) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), ""));
                return null;
            }
        }
         
       
        boolean bRet = vtl.deleteVault(this.selectedDG.id);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail2"), ""));
        }
         return "vaults?faces-redirect=true";
    }
    public String deleteVault_real(){
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        boolean bRet = vtl.deleteVault(this.selectedDG.id);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail2"), ""));
        }
        return "vaults?faces-redirect=true";
    }

    public void getExpansions(VaultInformation dg) {
        getDisks(dg);

//        if (dgInfos.get(dg.id) == null) {
//            dgInfo.get(0).setValue(dg.id + "");
//            dgInfo.get(1).setValue(dg.name);
//            dgInfo.get(2).setValue(dg.vendorID);
//            dgInfo.get(3).setValue(dg.productID);
//            dgInfo.get(4).setValue(dg.revision);
//            dgInfo.get(5).setValue(dg.numberOfSlots + "");
//            dgInfo.get(6).setValue(dg.numberOfPorts + "");
//            dgInfo.get(7).setValue(dg.numberOfDrives + "");
//            dgInfo.get(8).setValue(disksMap.get(dg.id).length + "");
//            dgInfo.get(9).setValue(dg.serialNumber);
//            dgInfo.get(10).setValue(dg.GUID);
//            this.dgInfos.put(dg.id, dgInfo);
//        }
//
//
//        if (luns.get(dg.id) == null) {
//            VtlInterface scsi = InterfaceFactory.getVtlInterfaceInstance();
//
//            luns.put(dg.id, scsi.getTapeLibraryDrivesInfo(dg.id));
//        }
//        lun = luns.get(dg.id);
    }

    public void getDisks(VaultInformation dg) {
        disks = new ArrayList();

        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        TapeInformation[] tapes = vtl.getVaultTapesInfo(dg.id);
         if (tapes == null) {
            return;
        }
        for (TapeInformation tape : tapes) {
            if (tape.locationTypeID == MyConstants.LOCATION_TYPE_TAPELIBRARY) {
                continue;
            }
              String vaultStr = res.get(basename, "vault");
            for (VaultInformation vault : vaults) {
                String vaultName = vault.name;
                if (vault.id == MyConstants.VAULT_TYPE_FREE) {
                    vaultName = res.get(basename, "vault_type_free");
                } else if (vault.id == MyConstants.VAULT_TYPE_IMPORT) {
                    continue;
                } else if (vault.id == MyConstants.VAULT_TYPE_FOREIGN) {
                    continue;
                } else if (vault.name.equals("offline")) {
                    vaultName = res.get(basename, "offline");
                }
                if (tape.locationID == vault.id) {
                 String str =  vaultStr + ":" + vaultName;
                    break;
                }
            }
            disks.add(tape);
        }
    }

    public String getType(String lun) {
        if (lun.startsWith("fc")) {
            return "FC";
        } else if (lun.startsWith("iscsi")) {
            return "iSCSI";
        } else if (lun.equalsIgnoreCase("all")) {
            return res.get(basename, "all");
        }
        return "";
    }

    public String getStrLun(String lun) {
        if (lun.equalsIgnoreCase("all")) {
            return res.get(basename, "alll");
        } else {
            return lun;
        }
    }



    public void deleteVTL() {
        VtlInterface cdp = InterfaceFactory.getVtlInterfaceInstance();
        boolean ret = cdp.deleteTapeLibrary(this.selectedDG.id);
        if (ret) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success2"), ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail2"), ""));
        }
    }

//    public final void initName() {
//        for (int i = 0; i < 11; i++) {
//            Details g = new Details();
//            g.setName(res.get(basename, "name" + i));
//            dgInfo.add(g);
//        }
//    }

    public final void initDiskGroups() {
        if (vaults == null) {
            return;
        }
         for (int n = 0; n < vaults.length; n++) {
             if (vaults[n].name.equalsIgnoreCase("offline")) {
                 VAULT_TYPE_OFFLINE = vaults[n].id;
                 break;
            }
         }
        
        for (int i = 0; i < vaults.length; i++) {

            String vaultName = vaults[i].name;
            if (vaults[i].id == MyConstants.VAULT_TYPE_FREE) {
                vaultName = res.get(basename, "vault_type_free");
            } else if (vaults[i].id == MyConstants.VAULT_TYPE_IMPORT) {
                vaultName = "导入带架";
                continue;
            } else if (vaults[i].id == MyConstants.VAULT_TYPE_FOREIGN) {
                vaultName = "离线带架";
                continue;
            } else if (vaults[i].name.equalsIgnoreCase("offline")) {
                vaultName = res.get(basename, "offline");
            }
            vaults[i].setName(vaultName);
            this.diskGroups.add(vaults[i]);
        }
    }

    public String vaultName(VaultInformation vault) {
        if (vault.id == MyConstants.VAULT_TYPE_FREE) {
            return res.get(basename, "vault_type_free");
        }else if (vault.name.equals("offline")) {
            return res.get(basename, "offline");
        } else {
            return vault.name;
        }
    }
    
    public boolean renderTapesAttrs(VaultInformation vault){
        boolean falg = true;
        if(vault.id == MyConstants.VAULT_TYPE_FREE || vault.name.equalsIgnoreCase("offline") || vault.name.equalsIgnoreCase(res.get(basename, "offline"))){
            falg = false;
        }else{
            falg = true;
        }  
//        if (userType != 2) {
//            falg = false;
//        }
        return falg ;
         
    }
    
      public boolean renderMoveTapes(){
        boolean falg = true;

         if (userType != 2) {
            falg = false;
        }
         return falg;
    }
    
    public boolean renderDeleteTapes(VaultInformation vault){
        boolean falg = false;
        if(vault.id == MyConstants.VAULT_TYPE_FREE){
            falg = true;
        }else{
            falg = false;
        }  
//         if (userType != 2) {
//            falg = false;
//        }
         return falg;
    }

    public String moveTape() {
        String param = "id=" + selectedDG.id + "&name=" + selectedDG.name;
        return "move_tape?faces-redirect=true&amp;" + param;
    }

    public String tapePutinX() {
        String param = "id=" + selectedDG.id + "&name=" + selectedDG.name;
        return "tape_putin_x?faces-redirect=true&amp;" + param;
    }
    
    public String deleteTape() {
        String param = "id=" + selectedDG.id + "&name=" + selectedDG.name;
        return "del_tape_x?faces-redirect=true&amp;" + param;
    }

    public String tapesAttrs() {
        SystemOutPrintln.print_vtl("selectedDG.id"+ selectedDG.id);
        String param = "id=" + selectedDG.id + "&name=" + selectedDG.name;
        return "tapes_attrs?faces-redirect=true&amp;" + param;
    }
    
    public String tapeAttrs() {
        String param = "id=" + selectedD.id+ "&valutid=" + selectedDG.id;
        return "tape_attrs?faces-redirect=true&amp;" + param;
    }

    public String renameTape() {
        String param = "id=" + selectedD.id + "&name=" + selectedD.name;
        return "tape_rename?faces-redirect=true&amp;" + param;
    }
    
    public boolean renameTape_render(TapeInformation tape) {
        boolean flag = true;
        //如果磁带不在带架 禁止修改
        if (tape.elementType != 0) {
            flag = false;
        }
        //如果磁带在离线带架上 禁止修改
        SystemOutPrintln.print_vtl("tape="+tape.name);
        SystemOutPrintln.print_vtl("VAULT_TYPE_OFFLINE="+VAULT_TYPE_OFFLINE);
         SystemOutPrintln.print_vtl("tape.locationID="+tape.locationID);
          SystemOutPrintln.print_vtl("tape.locationTypeID="+tape.locationTypeID);
        if (tape.locationTypeID == MyConstants.LOCATION_TYPE_VAULT && tape.locationID == VAULT_TYPE_OFFLINE) {
            
            flag = false;
        }
        //如果磁带在驱动上 禁止修改属性
        if (tape.elementType == 4) {
            flag = false;
        }
 SystemOutPrintln.print_vtl("tape.flag="+flag);
        return flag;
    }


    public List<Details> getDgInfo() {
        return dgInfo;
    }

    public void setDgInfo(List<Details> dgInfo) {
        this.dgInfo = dgInfo;
    }

    public ArrayList<VaultInformation> getDiskGroups() {
        return diskGroups;
    }

    public void setDiskGroups(ArrayList<VaultInformation> diskGroups) {
        this.diskGroups = diskGroups;
    }

    public VaultInformation getSelectedDG() {
        return selectedDG;
    }

    public void setSelectedDG(VaultInformation selectedDG) {
        this.selectedDG = selectedDG;
    }

    public TapeInformation getSelectedD() {
        return selectedD;
    }

    public void setSelectedD(TapeInformation selectedD) {
        this.selectedD = selectedD;
    }

    public List<TapeInformation> getDisks() {
        return disks;
    }

    public void setDisks(List<TapeInformation> disks) {
        this.disks = disks;
    }

    public Map<Integer, TapeInformation[]> getDisksMap() {
        return disksMap;
    }

    public void setDisksMap(Map<Integer, TapeInformation[]> disksMap) {
        this.disksMap = disksMap;
    }

    public Map getDgInfos() {
        return dgInfos;
    }

    public void setDgInfos(Map dgInfos) {
        this.dgInfos = dgInfos;
    }

    public DriveInformation[] getLun() {
        return lun;
    }

    public void setLun(DriveInformation[] lun) {
        this.lun = lun;
    }

    public Map<Integer, DriveInformation[]> getLuns() {
        return luns;
    }

    public void setLuns(Map<Integer, DriveInformation[]> luns) {
        this.luns = luns;
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

    public DriveInformation getSDrive() {
        return sDrive;
    }

    public void setSDrive(DriveInformation sDrive) {
        this.sDrive = sDrive;
    }

    public String getValutName() {
        return valutName;
    }

    public void setValutName(String valutName) {
        this.valutName = valutName;
    }
    
}
