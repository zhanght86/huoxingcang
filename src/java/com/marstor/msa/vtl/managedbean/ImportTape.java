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
import com.marstor.msa.vtl.model.ExportTapeWebModel;
import com.marstor.msa.vtl.util.MyConstants;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "importTapeBean")
@ViewScoped
public class ImportTape  implements Serializable{
    private String mediaPath;
    public String namePath;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.import_tape";
    private ArrayList<ExportTapeWeb> tapes = new ArrayList();
    private List<String> ts = new ArrayList();
    private String selected;
    public ArrayList<TapeInformation> importedTapes;
    VaultInformation[] vs;
    public ExportTapeWebModel tapeModel;
    private ExportTapeWeb[] selectedTapes;

    public ImportTape() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        mediaPath = "/" + request.getParameter("name");
        namePath = request.getParameter("name");
        initData();
    }

    public final void initData() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        TapeInformation[] allTape = vtl.getAllTapesInfo();
        SystemOutPrintln.print_vtl("allTape.length="+allTape.length);
        this.importedTapes = new ArrayList();
        List<String> importedTapesName = new ArrayList();
        if (allTape != null) {
            for (int i = 0; i < allTape.length; i++) {
                if (allTape[i].path.equals(mediaPath)) {
                    this.importedTapes.add(allTape[i]);
                    importedTapesName.add(allTape[i].name);
                }
            }
        }
        String[] volTapes = vtl.ListTapeFile(mediaPath);
        if (importedTapes != null && importedTapes.size() > 0) {
         
            if (volTapes != null && volTapes.length > 0) {
                for (String tapeName : volTapes) {
                    if (!importedTapesName.contains(tapeName)) {
                        ExportTapeWeb tape = new ExportTapeWeb();
                        tape.setSelected(false);
                        tape.setName(tapeName);
                        this.tapes.add(tape);
                    }
                }
            }
        } else {
            if (volTapes != null && volTapes.length > 0) {
                for (String tapeName : volTapes) {
                    ExportTapeWeb tape = new ExportTapeWeb();
                    tape.setSelected(false);
                    tape.setName(tapeName);
                    this.tapes.add(tape);
                }
            }
        }
        tapeModel = new ExportTapeWebModel(tapes);

        vs = vtl.getVaults();
        if (vs != null && vs.length > 0) {
            for (VaultInformation v : vs) {
                if (v.id == MyConstants.VAULT_TYPE_FREE) {
                    continue;
//                vaultInfo.name = java.util.ResourceBundle.getBundle("com/marstor/msa/common/dialog/resources/Dialog").getString("¿Õ°×´ø¼Ü");
                } else if (v.id == MyConstants.VAULT_TYPE_IMPORT) {
                    continue;
                } else if (v.id == MyConstants.VAULT_TYPE_FOREIGN) {
                    continue;
                } else if (v.name.equals("offline")) {
                    v.name = res.get(basename, "offline");
                }

                this.ts.add(v.name);
            }
            
            if (ts != null && ts.size() > 0) {
                selected = ts.get(0);
                 SystemOutPrintln.print_vtl("selected)="+selected);
            }
            
        }

    }

    public List<String> getTs() {
        return ts;
    }

    public void setTs(List<String> ts) {
        this.ts = ts;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String importTape() {
        
        if (selectedTapes == null || selectedTapes.length < 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "select"), ""));
            return null;
        }
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        for (ExportTapeWeb tape : this.selectedTapes) {
            String vaultStr = selected;
            SystemOutPrintln.print_vtl("vaultStr="+vaultStr);
            int vaultID = -2;
            for (VaultInformation v : vs) {
//                if (vaultStr.equalsIgnoreCase("ÀëÏß´ø¼Ü")) {
//                    vaultStr = "offline";
//                }
                if (v.name.equals(vaultStr)) {
                    vaultID = v.id;
                    break;
                }
            }
            SystemOutPrintln.print_vtl("tape.getName()="+tape.getName()+"mediaPath="+mediaPath+",vaultID="+vaultID+",tape.state="+tape.state);
            boolean ret = vtl.importTape(tape.getName(), mediaPath, vaultID);
            if (!ret) {
                String lastError = vtl.getLastError();
                if (lastError.equals("File exist")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "exit"), ""));
                    return null;
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), ""));
                    return null;
                }  
            }
        }
        return "storage_area?faces-redirect=true";
    }

    public ArrayList<ExportTapeWeb> getTapes() {
        return tapes;
    }

    public void setTapes(ArrayList<ExportTapeWeb> tapes) {
        this.tapes = tapes;
    }

    public String getNamePath() {
        return namePath;
    }

    public void setNamePath(String namePath) {
        this.namePath = namePath;
    }

    public ExportTapeWebModel getTapeModel() {
        return tapeModel;
    }

    public void setTapeModel(ExportTapeWebModel tapeModel) {
        this.tapeModel = tapeModel;
    }

    public ExportTapeWeb[] getSelectedTapes() {
        return selectedTapes;
    }

    public void setSelectedTapes(ExportTapeWeb[] selectedTapes) {
        this.selectedTapes = selectedTapes;
    }
}
