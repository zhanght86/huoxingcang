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
public class TapePutinX  implements Serializable{

    private List<String> rightTapes = new ArrayList();
    private List<String> leftTapes = new ArrayList();
    private String groupName;
    public int vaultID = -1;
    private TapeLibraryInformation[] libs;
    private DualListModel<String> model;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.tape_putin_x";
    private List<String> libList = new ArrayList();
    private String selected ="";
    public String valutName;
    public String tip =0+"";
     private List<TapeInformation> leftTapeList = new ArrayList();
    private TapeLibraryInformation lib;

    public TapePutinX() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.vaultID = Integer.valueOf(request.getParameter("id"));
        
        initTapeLibrarys();
        initLeftTapes();
         model = new DualListModel<String>(leftTapes, rightTapes);
         setLeftSlots();
//        initRightTapes();
     
    }

    public final void initTapeLibrarys() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        VaultInformation[] vaults = vtl.getVaults();
        if (vaults != null && vaults.length > 0) {
            for (VaultInformation va : vaults) {
                if (va.id == vaultID) {
                    valutName = va.name;
                    break;
                }
            }
        }
        if (vaultID == MyConstants.VAULT_TYPE_FREE) {
            valutName = res.get(basename, "vault_type_free");
        } else if (valutName.equals("offline")) {
            valutName = res.get(basename, "offline");
        }

        //获得所有带库
        libs = vtl.getTapeLibrarysInfo();
        if (libs != null && libs.length > 0) {
            for (int i = 0; i < libs.length; i++) {
                this.libList.add(libs[i].name);
                if(i == 0){
                    selected = libs[i].name;
                }
            }
        }else{
            this.libList.add(" ");
        }
        
    }
    public final void initLeftTapes() {
        //获得左面相应带架下的磁带
        leftTapeList = new ArrayList();
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        TapeInformation[] tapes = vtl.getVaultTapesInfo(vaultID);
        for (TapeInformation tape : tapes) {
            if (tape.state != TapePutin.TAPE_STATE_ENABLE) {
                continue;
            }
            leftTapeList.add(tape);
            this.leftTapes.add(tape.name);
        }
    }
    
    public void setLeftSlots() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        int libID = -1;
         if (libs == null) {
            return;
        }
        if (libs != null && libs.length > 0) {
            for (int i = 0; i < libs.length; i++) {
                if (libs[i].name.equals(selected)) {
                    libID = libs[i].id;
                }
            }
        }
        lib = vtl.getTapeLibraryInfo(libID);
        if (lib == null) {
            return;
        }

        int size = rightTapes.size();
//        int leftSize = lib.numberOfSlots - lib.numberOfTapes - size;
        int leftSize = lib.numberOfSlots - lib.numberOfTapes;
        tip = leftSize + "";

    }

    public List<String> getLibList() {
        return libList;
    }

    public void setLibList(List<String> libList) {
        this.libList = libList;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }


    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String save() {
        if (selected == null || selected.equals("") || selected.equals(" ")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v0"), ""));
            return null;
        } 
        
        rightTapes= (List<String>) this.model.getTarget(); 
        int rsize = rightTapes.size();
        SystemOutPrintln.print_vtl("right size="+rsize);
        if (rsize == 0) {
            SystemOutPrintln.print_vtl("return*********right size="+rsize);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v1"), ""));
            return null;
        }
SystemOutPrintln.print_vtl("(rsize + lib.numberOfTapes)="+(rsize + lib.numberOfTapes));
SystemOutPrintln.print_vtl("(rsize + ="+rsize);
SystemOutPrintln.print_vtl("lib.numberOfTapes)="+lib.numberOfTapes);
SystemOutPrintln.print_vtl("lib.numberOfSlots="+lib.numberOfSlots);
        if ((rsize + lib.numberOfTapes) > lib.numberOfSlots) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v2"), ""));
            return null;
        }
        
         List<TapeInformation> tapeList = new ArrayList();
        if (leftTapeList != null && leftTapeList.size() > 0) {
            for (int m = 0; m < rsize; m++) {
                for (int k = 0; k < leftTapeList.size(); k++) {
                    if (rightTapes.get(m).equals(leftTapeList.get(k).name)) {
                        tapeList.add(leftTapeList.get(k));
                          SystemOutPrintln.print_vtl("leftTapeList.get(k)="+leftTapeList.get(k).name);
                        break;
                    }
                }
            }

        }

        int tapeLibraryID = lib.id;
        SystemOutPrintln.print_vtl("tapeLibraryID="+tapeLibraryID);
        int[] tapesID = new int[rsize];
           for (int i = 0; i < rsize; i++) {
             
            int state = tapeList.get(i).state;
            if (state == MyConstants.TAPE_STATE_CREATING) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v3"), ""));
                return null;
            }
            if (state == MyConstants.TAPE_STATE_FAILED) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v4"), ""));
                return null;
            }
            tapesID[i] =  tapeList.get(i).id;
        }

        //排序
        Arrays.sort(tapesID);
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        for (int id : tapesID) {
            boolean suc = vtl.moveTapeFromVaultToTapeLibrary(id, tapeLibraryID);
            if (!suc) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f0"),""));
                return null;
            }
        }
        return "vaults?faces-redirect=true";
    }

    public List<String> getRightTapes() {
        return rightTapes;
    }

    public void setRightTapes(List<String> rightTapes) {
        this.rightTapes = rightTapes;
    }

    public List<String> getLeftTapes() {
        return leftTapes;
    }

    public void setLeftTapes(List<String> leftTapes) {
        this.leftTapes = leftTapes;
    }

    public DualListModel<String> getModel() {
        return model;
    }

    public void setModel(DualListModel<String> model) {
        this.model = model;
    }

   

    public int getTapeLibraryID() {
        return vaultID;
    }

    public void setTapeLibraryID(int tapeLibraryID) {
        this.vaultID = tapeLibraryID;
    }

    public String getValutName() {
        return valutName;
    }

    public void setValutName(String valutName) {
        this.valutName = valutName;
    }
    
}
