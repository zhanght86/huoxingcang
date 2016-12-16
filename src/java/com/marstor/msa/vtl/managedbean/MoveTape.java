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
import com.marstor.msa.vtl.model.DeleteTapeWeb;
import com.marstor.msa.vtl.model.ExportTapeWeb;
import com.marstor.msa.vtl.model.MoveTapeModel;
import com.marstor.msa.vtl.util.MyConstants;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "moveTape")
@ViewScoped
public class MoveTape  implements Serializable{
    public static final int TAPE_STATE_ENABLE = 0;
    public static final int TAPE_STATE_CREATING = 1;
    public static final int TAPE_STATE_FAILED = 2;
//    private String mediaPath;
    private int id;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.move_tape";
    private ArrayList<String> vaults = new ArrayList();
    private String selected;
    private ArrayList<DeleteTapeWeb> tapes = new ArrayList();
    public String name;
    public VaultInformation[] allVaults;
    private MoveTapeModel tapeModel;
    private DeleteTapeWeb[] selectedTapess;
    public int destVaultID = -1;

    public MoveTape() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
//        mediaPath = "/" + request.getParameter("name");
        name = request.getParameter("name");
        id = Integer.valueOf(request.getParameter("id"));
        initData();
    }

    public final void initData() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        allVaults = vtl.getVaults();
        if (allVaults == null) {
            return;
        }
        int offlineID = -1;
        for (int k = 0; k < allVaults.length; k++) {
            if (allVaults[k].name.equals("offline")) {
                offlineID = allVaults[k].id;
                break;
            }
        }
        if (id == MyConstants.VAULT_TYPE_FREE) {
            name = res.get(basename, "freevault");
        } else if (id == offlineID) {
            name = res.get(basename, "offlinevault");
        }

        for (int i = 0; i < allVaults.length; i++) {
            if (allVaults[i].id == id) {
                continue;
            } else if (allVaults[i].id == MyConstants.VAULT_TYPE_FREE) {
                allVaults[i].name = res.get(basename, "freevault");
            } else if (allVaults[i].id == MyConstants.VAULT_TYPE_IMPORT) {
                continue;
            } else if (allVaults[i].id == MyConstants.VAULT_TYPE_FOREIGN) {
                continue;
            } else if (allVaults[i].name.equals("offline")) {
                allVaults[i].name = res.get(basename, "offlinevault");
            }
            this.vaults.add(allVaults[i].name);
        }
        
        
        TapeInformation[] allTape = vtl.getVaultTapesInfo(id);
        if (allTape == null) {
            return;
        }
        for (int i = 0; i < allTape.length; i++) {
            if (allTape[i].state != TAPE_STATE_ENABLE) {
                continue;
            }
            DeleteTapeWeb tape = new DeleteTapeWeb();
            tape.setSelected(false);
            tape.setName(allTape[i].name);
            tape.setBarcode(allTape[i].barcode);
            tape.setTapeID(allTape[i].id);
            tape.setState(allTape[i].state);
            this.tapes.add(tape);

        }
        tapeModel = new MoveTapeModel(tapes);
    }

    public String moveTapes() {
        if (selected == null || selected.equals("") || selected.equals(" ")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "noselecttarget"), ""));
            return null;

        }
        if (selectedTapess == null || selectedTapess.length <1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "selecttape"), ""));
            return null;
        }

        String selectValut = selected;
//        if (selected.equals("¿Õ°×´ø¼Ü")) {
//            destVaultID = MyConstants.VAULT_TYPE_FREE;
//        } else if (selected.equals("ÀëÏß´ø¼Ü")) {
//            selectValut = "offline";
//        }
        for (int i = 0; i < allVaults.length; i++) {
               SystemOutPrintln.print_vtl("for allVaults[i].name=" + allVaults[i].name);
            if (selectValut.equals(allVaults[i].name)) {
             
                destVaultID = allVaults[i].id;
                 SystemOutPrintln.print_vtl("for destVaultID=" + destVaultID);
                break;
            }
        }
        SystemOutPrintln.print_vtl("destVaultID=" + destVaultID);

        
        for (DeleteTapeWeb tape : this.selectedTapess) {
            if (tape.state == TAPE_STATE_CREATING) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "creating_nomove"), ""));
                return null;
            }
            if (tape.state == TAPE_STATE_FAILED) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "damage_nomove"), ""));
                return null;
            }

        }  
        if (destVaultID == MyConstants.VAULT_TYPE_FREE) {
            RequestContext.getCurrentInstance().execute("delTapes.show()");
        } else {
            return moveTapes_real();
        }
        return null;
    }
    
    public String moveTapes_real() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
         for (DeleteTapeWeb tape : this.selectedTapess) {
            if (tape.state == TAPE_STATE_CREATING) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "creating_nomove"), ""));
                return null;
            }
            if (tape.state == TAPE_STATE_FAILED) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "damage_nomove"), ""));
                return null;
            }
            SystemOutPrintln.print_vtl("tape.getTapeID()="+tape.getTapeID()+"destVaultID="+destVaultID);
            boolean ret = vtl.moveTapeFromVaultToVault(tape.getTapeID(), destVaultID);
            if (!ret) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), ""));
                return null;
            }
        }
         return "vaults?faces-redirect=true";
    }

    public ArrayList<String> getVaults() {
        return vaults;
    }

    public void setVaults(ArrayList<String> vaults) {
        this.vaults = vaults;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public ArrayList<DeleteTapeWeb> getTapes() {
        return tapes;
    }

    public void setTapes(ArrayList<DeleteTapeWeb> tapes) {
        this.tapes = tapes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MoveTapeModel getTapeModel() {
        return tapeModel;
    }

    public void setTapeModel(MoveTapeModel tapeModel) {
        this.tapeModel = tapeModel;
    }

    public DeleteTapeWeb[] getSelectedTapess() {
        return selectedTapess;
    }

    public void setSelectedTapess(DeleteTapeWeb[] selectedTapess) {
        this.selectedTapess = selectedTapess;
    }
    
}
