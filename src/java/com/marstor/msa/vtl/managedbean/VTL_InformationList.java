/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.TapeInformation;
import com.marstor.msa.bean.TapeLibraryInformation;
import com.marstor.msa.bean.VaultInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.model.TapeBean;
import com.marstor.msa.vtl.model.VTLBean;
import com.marstor.msa.vtl.model.VaultBean;
import com.marstor.msa.vtl.util.MyConstants;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

@ManagedBean(name = "vtl_InformationList")
@ViewScoped
public class VTL_InformationList  implements Serializable{

    private List<TapeLibraryInformation> vtlList = new ArrayList();
    private VTLBean vtlBean;
    private List<VaultInformation> vaultList = new ArrayList();
    private VaultBean vaultBean;
    private List<TapeInformation> tapeList = new ArrayList();
    private TapeBean tapeBean;
    private VTLBean selectVTL;
    private MSAResource res = new MSAResource();
    private String basename = "vtl.vtl_root";

    public VTLBean getSelectVTL() {
        return selectVTL;
    }

    public void setSelectVTL(VTLBean selectVTL) {
        System.out.println("setSelectVTL   " + selectVTL);
        this.selectVTL = selectVTL;
    }

    /**
     * Creates a new instance of System_hardWareStateList
     */
    public VTL_InformationList() {
        this.getVTLInformation();
        this.getVaultInformation();
        this.getTapeInformation();
    }

    public final void getVTLInformation() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        this.vtlList = Arrays.asList(vtl.getTapeLibrarysInfo());
    }

    public final void getVaultInformation() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        List<VaultInformation> list = Arrays.asList(vtl.getVaults());
        for (VaultInformation vault : list) {
            if (vault.id == MyConstants.VAULT_TYPE_FREE) {
                this.vaultList.add(vault);
            } else if (vault.id == MyConstants.VAULT_TYPE_FOREIGN) {
                continue;
            } else if (vault.id == MyConstants.VAULT_TYPE_IMPORT) {
                continue;
            } else if (vault.name.equalsIgnoreCase("offline")) {
                this.vaultList.add(vault);
            } else {
                this.vaultList.add(vault);
            }
        }
    }

    public final void getTapeInformation() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        this.tapeList = Arrays.asList(vtl.getAllTapesInfo());
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("NIC Edited", ((VaultBean) event.getObject()).getName());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("NIC Cancelled", ((VaultBean) event.getObject()).getName());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<TapeLibraryInformation> getVtlList() {
        return vtlList;
    }

    public void setVtlList(List<TapeLibraryInformation> vtlList) {
        this.vtlList = vtlList;
    }

    public List<VaultInformation> getVaultList() {
        return vaultList;
    }

    public void setVaultList(List<VaultInformation> vaultList) {
        this.vaultList = vaultList;
    }

    public List<TapeInformation> getTapeList() {
        return tapeList;
    }

    public void setTapeList(List<TapeInformation> tapeList) {
        this.tapeList = tapeList;
    }

    public String enToStr(VaultInformation vault) {
        if (vault.id == MyConstants.VAULT_TYPE_FREE) {
            return res.get(basename, "vault_type_free");
        }else if (vault.name.equals("offline")) {
            return res.get(basename, "offline");
        } else {
            return vault.name;
        }

    }
}
