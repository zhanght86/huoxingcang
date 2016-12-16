/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.TapeInformation;
import com.marstor.msa.bean.TapeLibraryInformation;
import com.marstor.msa.bean.VaultInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.util.MyConstants;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class TapePutin  implements Serializable{

    private List<String> rightTapes = new ArrayList();
    private List<String> leftTapes = new ArrayList();
    private List<TapeInformation> leftTapeList = new ArrayList();
    private String groupName;
    public int tapeLibraryID = -1;
    private VaultInformation[] vaults;
    private String vault = "";
    private DualListModel<String> model;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.tape_putin";
    private List<String> ts = new ArrayList();
    public static final int TAPE_STATE_ENABLE = 0;
    public static final int TAPE_STATE_CREATING = 1;
    public static final int TAPE_STATE_FAILED = 2;
    public String tip;
    public int size =0;
    public TapeLibraryInformation lib;

    public TapePutin() {
        tip = res.get(basename, "tip");
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.tapeLibraryID = Integer.valueOf(request.getParameter("id"));
        this.groupName = request.getParameter("name");
        size = Integer.valueOf(request.getParameter("size"));
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        lib = vtl.getTapeLibraryInfo(tapeLibraryID);
        initVaults();
        initLeftTapes();
//        initRightTapes();
        model = new DualListModel<String>(leftTapes, rightTapes);
        setLeftSlots();
    }

    public final void initVaults() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        vaults = vtl.getVaults();
        if (vaults != null && vaults.length > 0) {
            for (VaultInformation va : vaults) {
                if (va.id == MyConstants.VAULT_TYPE_FREE) {
                    va.name = res.get(basename, "vault_type_free");
                } else if (va.id == MyConstants.VAULT_TYPE_IMPORT) {
//                vault.name = "导入带架";
                    continue;
                } else if (va.id == MyConstants.VAULT_TYPE_FOREIGN) {
//                vault.szName = "离线带架";
                    continue;
                } else if (va.name.equals("offline")) {
                    va.name = res.get(basename, "offline");
                }
                this.ts.add(va.name);
            }
        }
    }

    public final void initLeftTapes() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();

        int vaultID = 0;
        if (vaults != null && vaults.length > 0) {
            if (vault.equals(res.get(basename, "vault_type_free"))) {
                vaultID = MyConstants.VAULT_TYPE_FREE;
            } else if (vault.equals(res.get(basename, "offline"))) {
                VaultInformation[] valuts = vtl.getVaults();
                for (int n = 0; n < valuts.length; n++) {
                    if (valuts[n].getName().equals("offline")) {
                        vaultID = valuts[n].getId();
                        break;
                    }
                }
            } else {
                if (vaults != null && vaults.length > 0) {
                    for (VaultInformation va : vaults) {
                        if (va.name.equals(vault)) {
                            vaultID = va.id;
                            break;
                        }
                    }
                }
            }

        }
        
        //获得左面相应带架下的磁带
        leftTapeList = new ArrayList();
        TapeInformation[] tapes = vtl.getVaultTapesInfo(vaultID);
        for (TapeInformation tape : tapes) {
            if (tape.state == TAPE_STATE_CREATING || tape.state == TAPE_STATE_FAILED) {
                continue;
            }
            if (tape.locationTypeID != MyConstants.LOCATION_TYPE_VAULT) {
                continue;
            }
            if (tape.locationID != vaultID) {
                continue;
            }

            leftTapeList.add(tape);
            this.leftTapes.add(tape.name);
        }
    }
    
     public void setLeftSlots() {
       
        int sizeInt = rightTapes.size();
        int leftSize = lib.numberOfSlots -lib.numberOfTapes- sizeInt;
       tip = leftSize+"";
    }
    

//    public final void initRightTapes() {
//        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
//        this.rightTapes = Arrays.asList(vtl.getTapeLibraryTapesInfo(tapeLibraryID));
//    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void refreshLeft(String vault) {
//           List<String>  rightTapess= (List<String>) this.model.getTarget();
//           System.out.println("right rightTapess size="+rightTapess.size());
//           rightTapes = new ArrayList();
//         for(String name :rightTapess){
//               rightTapes.add(name);
//         }
        
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        int id = 0;
        if (vault.equals(res.get(basename, "vault_type_free"))) {
            id = MyConstants.VAULT_TYPE_FREE;
        } else if (vault.equals(res.get(basename, "offline"))) {
            VaultInformation[] valuts = vtl.getVaults();
            for (int n = 0; n < valuts.length; n++) {
                if (valuts[n].getName().equals("offline")) {
                    id = valuts[n].getId();
                    break;
                }
            }

        } else {
            if (vaults != null && vaults.length > 0) {
                for (VaultInformation va : vaults) {
                    if (va.name.equals(vault)) {
                        id = va.id;
                        break;
                    }
                }
            }
        }
        
        leftTapeList = new ArrayList();
         leftTapes = new ArrayList();
          //获得左面相应带架下的磁带
        TapeInformation[] tapes = vtl.getVaultTapesInfo(id);
        for (TapeInformation tape : tapes) {
            if (tape.state == TAPE_STATE_CREATING || tape.state == TAPE_STATE_FAILED) {  
                continue;
            }
            if (tape.locationTypeID != MyConstants.LOCATION_TYPE_VAULT) {
                continue;
            }
            if (tape.locationID != id) {
                continue;
            }

            leftTapeList.add(tape);
            this.leftTapes.add(tape.name);
        }
     
     
       
//       System.out.println("id="+id+",vtl.getVaultTapesInfo(id)[0].name="+vtl.getVaultTapesInfo(id)[0].name); 
         model = new DualListModel<String>(leftTapes, rightTapes);
        SystemOutPrintln.print_vtl("this.leftTapes.size()="+this.leftTapes.size());
        
    }

    public String save() {
         VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        rightTapes= (List<String>) this.model.getTarget(); 
        int rsize = rightTapes.size();
        SystemOutPrintln.print_vtl("right size="+rsize);
        if (rsize == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v0"), ""));
            return null;
        }
        
        if ((rsize + lib.numberOfTapes) > lib.numberOfSlots) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v1"), ""));
            return null;
        }
        int vaultID = 0;
        if (vault.equals(res.get(basename, "vault_type_free"))) {
            vaultID = MyConstants.VAULT_TYPE_FREE;
        } else if (vault.equals(res.get(basename, "offline"))) {
            VaultInformation[] valuts = vtl.getVaults();
            for (int n = 0; n < valuts.length; n++) {
                if (valuts[n].getName().equals("offline")) {
                    vaultID = valuts[n].getId();
                    break;
                }
            }
        } else {
            if (vaults != null && vaults.length > 0) {
                for (VaultInformation va : vaults) {
                    if (va.name.equals(vault)) {
                        vaultID = va.id;
                        break;
                    }
                }
            }
        }
        
        List<TapeInformation> tapeList = new ArrayList();
//        TapeInformation[] tapes = vtl.getVaultTapesInfo(vaultID);
        if (leftTapeList != null && leftTapeList.size() > 0) {
            for (int m = 0; m < rsize; m++) {
                for (int k = 0; k < leftTapeList.size(); k++) {
                    if (rightTapes.get(m).equals(leftTapeList.get(k).name)) {
                        tapeList.add(leftTapeList.get(k));
                          SystemOutPrintln.print_vtl("leftTapeList.get(k)="+leftTapeList.get(k).name);
                        break;
                    }
                }
                int i=0;
                SystemOutPrintln.print_vtl("int i="+(i+1));
            }

        }
        
//  System.out.println("################$$$$$$$$ " + rightTapes.get(0) +",ightTapes.get(i).state="+rightTapes.get(0).state+",rightTapes.get(i).id="+rightTapes.get(0).id);
        int[] tapesID = new int[rsize];
        for (int i = 0; i < rsize; i++) {
             
            int state = tapeList.get(i).state;
            if (state == MyConstants.TAPE_STATE_CREATING) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v2"), ""));
                return null;
            }
            if (state == MyConstants.TAPE_STATE_FAILED) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v3"), ""));
                return null;
            }
            tapesID[i] =  tapeList.get(i).id;
        }


        //排序
        Arrays.sort(tapesID);

        for (int id : tapesID) {
            boolean suc = vtl.moveTapeFromVaultToTapeLibrary(id, tapeLibraryID);
            if(!suc){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f0"), ""));
                return null;
            }
        }
        
        
        rightTapes = new ArrayList();
        leftTapes = new ArrayList();
        vaults = null;
        ts = new ArrayList();
        lib = vtl.getTapeLibraryInfo(tapeLibraryID);
        initVaults();
        initLeftTapes();
        model = new DualListModel<String>(leftTapes, rightTapes);
        setLeftSlots();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success"), ""));
        return null;
//        return "vtls?faces-redirect=true";
    }
    
    public String cancle(){
        return "vtls?faces-redirect=true";
    }

    public String getVault() {
        return vault;
    }

    public void setVault(String vault) {
        this.vault = vault;
    }



    public int getTapeLibraryID() {
        return tapeLibraryID;
    }

    public void setTapeLibraryID(int tapeLibraryID) {
        this.tapeLibraryID = tapeLibraryID;
    }

    public VaultInformation[] getVaults() {
        return vaults;
    }

    public void setVaults(VaultInformation[] vaults) {
        this.vaults = vaults;
    }



    public List<String> getTs() {
        return ts;
    }

    public void setTs(List<String> ts) {
        this.ts = ts;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public DualListModel<String> getModel() {
        return model;
    }

    public void setModel(DualListModel<String> model) {
        this.model = model;
    }
    
    
}
