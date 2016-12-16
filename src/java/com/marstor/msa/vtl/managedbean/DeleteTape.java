/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.TapeInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.model.DeleteTapeWeb;
import com.marstor.msa.vtl.model.MoveTapeModel;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "delTapeBean")
@ViewScoped
public class DeleteTape  implements Serializable{

    private String mediaPath;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.del_tape";
    private ArrayList<DeleteTapeWeb> tapes = new ArrayList();
    public String name;
    public String volume;
    public MoveTapeModel tapeModel;
    private DeleteTapeWeb[] selectedTapess;

    public DeleteTape() {
       
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        name = request.getParameter("name");
        volume = name.split("/")[0];
        mediaPath = "/" + request.getParameter("name");
        initData();
    }

    public final void initData() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        TapeInformation[] allTape = vtl.getAllTapesInfo();
        if (allTape == null) {
            return;
        }
        for (int i = 0; i < allTape.length; i++) {
            if (allTape[i].path.equals(mediaPath)) {
                DeleteTapeWeb tape = new DeleteTapeWeb();
                tape.setName(allTape[i].name);
                tape.setBarcode(allTape[i].barcode);
                tape.setTapeID(allTape[i].id);
                tape.setFilePath(allTape[i].path);
                this.tapes.add(tape);
            }
        }
         tapeModel = new MoveTapeModel(tapes);
    }
    
       public void deleteTape() {
        if (selectedTapess == null || selectedTapess.length < 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "noselect"), ""));
            return;
        }

        for (DeleteTapeWeb tape : this.selectedTapess) {
            if (tape.state == MoveTape.TAPE_STATE_CREATING) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get(basename, "creating_cantdel"), ""));
                return;
            }
        }

        RequestContext.getCurrentInstance().execute("deletetape.show()");
    }
    

    public String deleteTape_real() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        
        for (DeleteTapeWeb tape : selectedTapess) {
            SystemOutPrintln.print_vtl("tape.getFilePath()="+tape.getFilePath());
            boolean ret = vtl.deleteTape(tape.getTapeID(), tape.getFilePath());
            if (!ret) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), ""));
                return null;
            }
        }
         String param = "name=" + name;
         return "del_tape?faces-redirect=true&amp;" + param;
//        return "storage_area?faces-redirect=true";
    }

    public ArrayList<DeleteTapeWeb> getTapes() {
        return tapes;
    }

    public void setTapes(ArrayList<DeleteTapeWeb> tapes) {
        this.tapes = tapes;
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

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
    
}
