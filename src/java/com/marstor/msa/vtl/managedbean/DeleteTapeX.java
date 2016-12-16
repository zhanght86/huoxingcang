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
@ManagedBean
@ViewScoped
public class DeleteTapeX  implements Serializable{

    private String barcode;
    private String mediaPath;
    public String name;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.del_tape_x";
    private ArrayList<DeleteTapeWeb> tapes = new ArrayList();
    private DeleteTapeWeb[] selectedTapess;
    private int id;
    public MoveTapeModel tapeModel;
    private String nameStr;

    public DeleteTapeX() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        id = Integer.valueOf(request.getParameter("id"));
        mediaPath = "/" + request.getParameter("name");
        nameStr = request.getParameter("name");
        name = res.get(basename, "freevault");
        initData();
    }

    public final void initData() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        TapeInformation[] allTape = vtl.getVaultTapesInfo(id);
        if (allTape == null) {
            return;
        }
        for (int i = 0; i < allTape.length; i++) {

            DeleteTapeWeb tape = new DeleteTapeWeb();
            tape.setName(allTape[i].name);

            tape.setBarcode(allTape[i].barcode);
            tape.setTapeID(allTape[i].id);
            tape.setFilePath(allTape[i].path);
            this.tapes.add(tape);

        }
        tapeModel = new MoveTapeModel(tapes);
    }

     public String deleteTape_return() {
        String param = "id=" + id + "&name=" + nameStr;
        return "del_tape_x?faces-redirect=true&amp;" + param;
    }
    public void deleteTape() {
        if (selectedTapess == null || selectedTapess.length < 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,res.get(basename, "noselect"), ""));
            return;
        }

        for (DeleteTapeWeb tape : this.selectedTapess) {
            if (tape.state == MoveTape.TAPE_STATE_CREATING) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "creating_cantdel"), ""));
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
        String param = "id=" + id + "&name=" + nameStr;
        return "del_tape_x?faces-redirect=true&amp;" + param;
    }

    public ArrayList<DeleteTapeWeb> getTapes() {
        return tapes;
    }

    public void setTapes(ArrayList<DeleteTapeWeb> tapes) {
        this.tapes = tapes;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
