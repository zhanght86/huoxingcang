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
import com.marstor.msa.vtl.model.ExportTapeWeb;
import com.marstor.msa.vtl.model.ExportTapeWebModel;
import com.marstor.msa.vtl.util.MyUtility;
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
@ManagedBean(name = "exportTapeBean")
@ViewScoped
public class ExportTape  implements Serializable{

    private String barcode;
    private String mediaPath;
    private int count;
    private int size;
    private int demandSize;
    private boolean byneed;
    private boolean compression;
    private boolean worm;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.export_tape";
    private ArrayList<ExportTapeWeb> tapes = new ArrayList();
    public String name;
    public ExportTapeWebModel tapeModel;
    private ExportTapeWeb[] selectedTapes;

    public ExportTape() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        name = request.getParameter("name");
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
                ExportTapeWeb tape = new ExportTapeWeb();
                tape.setName(allTape[i].name);
                tape.setTapeID(allTape[i].id);
                if (allTape[i].locationTypeID == MyUtility.LOCATION_TYPE_TAPELIBRARY) {
                    tape.setState(res.get(basename, "using"));
                } else {
                    tape.setState(res.get(basename, "free"));
                }
                this.tapes.add(tape);
            }
        }
         tapeModel = new ExportTapeWebModel(tapes);
    }
    
    

    public String exportTape() {
        if (selectedTapes == null || selectedTapes.length < 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "select"), ""));
            return null;
        }
        
         for (int i = 0; i < selectedTapes.length; i++) {

             if (selectedTapes[i].state.equals(res.get(basename, "using"))) {
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "using_noexport"), ""));
                 return null;
                }
            
        }
        
        RequestContext.getCurrentInstance().execute("exportTape.show()");   
        return null;
    }
    
    public String exportTape_real() {
   
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();;
        for (ExportTapeWeb tape : this.selectedTapes) {
            SystemOutPrintln.print_vtl("export select="+tape.getName());
            SystemOutPrintln.print_vtl("tape.getTapeID()="+tape.getTapeID());
            boolean ret = vtl.exportTape(tape.getTapeID());
            if (!ret) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"),""));
                return null;
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


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDemandSize() {
        return demandSize;
    }

    public void setDemandSize(int demandSize) {
        this.demandSize = demandSize;
    }

    public boolean isCompression() {
        return compression;
    }

    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    public boolean isWorm() {
        return worm;
    }

    public void setWorm(boolean worm) {
        this.worm = worm;
    }

    public boolean isByneed() {
        return byneed;
    }

    public void setByneed(boolean byneed) {
        this.byneed = byneed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
