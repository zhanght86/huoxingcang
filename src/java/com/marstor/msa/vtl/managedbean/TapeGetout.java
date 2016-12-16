/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.TapeInformation;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class TapeGetout  implements Serializable{

    private List<String> rightTapes = new ArrayList();
    private List<String> leftTapes = new ArrayList();
    private List<TapeInformation> leftTapeList = new ArrayList();
    public int tapeLibraryID = -1;
    public String tapeLibraryName;
    private VaultInformation[] vaults;
    private List<String> ts = new ArrayList();
    private String vault;
    private DualListModel<String> model;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.tape_getout";
    private boolean confirmSave;
    TapeInformation[] tapes;

    public TapeGetout() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.tapeLibraryID = Integer.valueOf(request.getParameter("id"));
        tapeLibraryName = request.getParameter("name");
        initVaults();
        initLeftTapes();

        model = new DualListModel<String>(leftTapes, rightTapes);
    }

    public final void initVaults() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        vaults = vtl.getVaults();
        SystemOutPrintln.print_vtl("tape get out");
        if (vaults != null && vaults.length > 0) {
            for (VaultInformation va : vaults) {
                if (va.id == MyConstants.VAULT_TYPE_FREE) {
                    va.name = res.get(basename, "vault_type_free");
                } else if (va.id == MyConstants.VAULT_TYPE_IMPORT) {
                    SystemOutPrintln.print_vtl("tape get out VAULT_TYPE_IMPORT");
//                vault.name = "导入带架";
                    continue;
                } else if (va.id == MyConstants.VAULT_TYPE_FOREIGN) {
                    SystemOutPrintln.print_vtl("tape get out VAULT_TYPE_FOREIGN");
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
        tapes = vtl.getTapeLibraryTapesInfo(tapeLibraryID);
        if (tapes != null && tapes.length > 0) {
            for (int i = 0; i < tapes.length; i++) {
                if (tapes[i].elementType != MyConstants.CHANGER_ELEMENT_TYPE_DRIVE) {
                    this.leftTapes.add(tapes[i].name);
                    leftTapeList.add(tapes[i]);
                }
            }
        }

    }

//    public final void initRightTapes() {
//        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
//        this.leftTapes = Arrays.asList(vtl.getVaultTapesInfo(vaults[0].id));
//    }
    public String save() {
        if (vault == null || vault.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v1"), ""));
            return null;
        }

        rightTapes = (List<String>) this.model.getTarget();
        int size = rightTapes.size();
        if (size == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v0"), ""));
            return null;
        }
        if (vault != null && vault.equals(res.get(basename, "vault_type_free")) && !this.confirmSave) {
            RequestContext.getCurrentInstance().execute("confirmMoveToFreeVault.show()");
            return null;
        }
        int vaultID = 0;
        if (vault.equals(res.get(basename, "vault_type_free"))) {
            vaultID = MyConstants.VAULT_TYPE_FREE;
        } else if (vault.equals(res.get(basename, "offline"))) {
            SystemOutPrintln.print_vtl("select offline: ");
            for (int n = 0; n < vaults.length; n++) {
                SystemOutPrintln.print_vtl("vault name: " + vaults[n].getName() + "  vault ID: " + vaults[n].getId());
                if (vaults[n].getName().equals(res.get(basename, "offline"))) {
                    SystemOutPrintln.print_vtl("vault name: " + vaults[n].getName() + "  vault ID: " + vaults[n].getId());
                    vaultID = vaults[n].getId();
                    break;
                }
            }
        } else {
            for (VaultInformation va : vaults) {
                if (va.name.equals(vault)) {
                    vaultID = va.id;
                    break;
                }
            }
        }

        List<TapeInformation> tapeList = new ArrayList();
        if (tapes != null && tapes.length > 0) {
            for (int m = 0; m < size; m++) {
                for (int k = 0; k < tapes.length; k++) {

                    if (rightTapes.get(m).equals(tapes[k].name)) {
                        tapeList.add(tapes[k]);
                        break;
                    }
                }
            }
        }
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        boolean flag = false;
        SystemOutPrintln.print_vtl("move Tape From TapeLibaray To Vault : ");
        for (TapeInformation tape : tapeList) {
            if (tape.elementType == MyConstants.CHANGER_ELEMENT_TYPE_DRIVE) {
                flag = true;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v2"), ""));
                return null;
            }
            if (flag) {
                break;
            }
            SystemOutPrintln.print_vtl("vault ID: " + vaultID);
            boolean suc = vtl.moveTapeFromTapeLibarayToVault(tape.id, vaultID);
            if (!suc) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f0"), ""));
                return null;
            }
           

        }

        return "vtls?faces-redirect=true";
    }

    public String confirmVaultAsFree() {
//        if (vault != null && vault.equals(res.get(basename, "vault_type_free"))) {
//            RequestContext.getCurrentInstance().execute("confirmMoveToFreeVault.show()");
//            return null;
//        }
        this.confirmSave = true;
        return this.save();
    }

    public String getVault() {
        return vault;
    }

    public void setVault(String vault) {
        this.vault = vault;
    }

    public DualListModel<String> getModel() {
        return model;
    }

    public void setModel(DualListModel<String> model) {
        this.model = model;
    }

    public VaultInformation[] getVaults() {
        return vaults;
    }

    public void setVaults(VaultInformation[] vaults) {
        this.vaults = vaults;
    }

    public String getTapeLibraryName() {
        return tapeLibraryName;
    }

    public void setTapeLibraryName(String tapeLibraryName) {
        this.tapeLibraryName = tapeLibraryName;
    }

    public List<String> getTs() {
        return ts;
    }

    public void setTs(List<String> ts) {
        this.ts = ts;
    }
}
