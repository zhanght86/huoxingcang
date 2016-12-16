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
@ManagedBean
@ViewScoped
public class TapesAttrs  implements Serializable{


    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.tapes_attrs";
    private List<TapeInformation> tapes = new ArrayList();
    private int id;
    public String name;
    public boolean allcompress;
    public boolean allreadfile;

    public TapesAttrs() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        id = Integer.valueOf(request.getParameter("id"));
        name = request.getParameter("name");
        
        initData();
    }

    public final void initData() {
        allcompress = true;
        allreadfile = true;
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        TapeInformation[] allTape = vtl.getVaultTapesInfo(id);
        if (allTape == null) {
            allcompress = false;
            allreadfile = false;
            return;
        }
        if(allTape !=null  && allTape.length<1){
            allcompress = false;
            allreadfile = false;
        }
        for (TapeInformation tape : allTape) {
            if (tape.state != MoveTape.TAPE_STATE_ENABLE) {
                continue;
            }
            if(!tape.enableCompression){
                 allcompress = false;
            }
            if(!tape.writeProtect){
                allreadfile = false;
            }
            this.tapes.add(tape);
        }

    }

    public void changeAllcompress(boolean isSt) {
        if(tapes != null && tapes.size()>0){
            if(isSt){
                for(TapeInformation tape : tapes){
                    tape.setEnableCompression(true);
                }
            }else{
                for(TapeInformation tape : tapes){
                    tape.setEnableCompression(false);
                }
            }
        }
    }

    public void changeAllreadfile(boolean isSt) {
         if(tapes != null && tapes.size()>0){
            if(isSt){
                for(TapeInformation tape : tapes){
                    tape.setWriteProtect(true);
                }
            }else{
                for(TapeInformation tape : tapes){
                    tape.setWriteProtect(false);
                }
            }
        }
    }
    
    public void changeNotAllcompress() {

        if (tapes != null && tapes.size() > 0) {
            boolean isall = true;
            for (TapeInformation tape : tapes) {
                if (!tape.enableCompression) {
                    isall = false;
                    allcompress = false;
                    break;
                }
            }
            if(isall){
                allcompress = true;
            }

        }
    }
    
    public void changeNoAllreadfile() {

        if (tapes != null && tapes.size() > 0) {
            boolean isall = true;
            for (TapeInformation tape : tapes) {
                if (!tape.writeProtect) {
                    isall = false;
                    allreadfile = false;
                    break;
                }
            }
            if (isall) {
                allreadfile = true;
            }

        }
    }

    public String modifyTapes() {
        if (tapes == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null"), ""));
            return null;
        }
        if (tapes != null && tapes.size() < 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "null"), ""));
            return null;

        }
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        for (TapeInformation tape : this.tapes) {
            
            SystemOutPrintln.print_vtl("tape.id="+tape.id+",tape.name="+tape.name+",tape.enableCompression="+tape.enableCompression+",tape.writeProtect="+tape.writeProtect+" ,tape.onDemand="+ tape.onDemand+",tape.tapeSize="+tape.tapeSize);
            boolean ret = vtl.modifyTape(tape.id, tape.name,
                    tape.writeProtect, tape.enableCompression, tape.onDemand, (int)(tape.tapeSize/1024L/1024L/1024L));
            if (!ret) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), ""));
                return null;
            }
        }
        return "vaults?faces-redirect=true";
    }

    public List<TapeInformation> getTapes() {
        return tapes;
    }

    public void setTapes(List<TapeInformation> tapes) {
        this.tapes = tapes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAllcompress() {
        return allcompress;
    }

    public void setAllcompress(boolean allcompress) {
        this.allcompress = allcompress;
    }

    public boolean isAllreadfile() {
        return allreadfile;
    }

    public void setAllreadfile(boolean allreadfile) {
        this.allreadfile = allreadfile;
    }
    
    
}
