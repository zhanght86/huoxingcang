/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.bean.TapeInformation;
import com.marstor.msa.bean.TapeLibraryInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.util.MyConstants;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
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
public class TapeRename  implements Serializable{
;
    public int tapeID = -1;
    private TapeLibraryInformation vtl;
    private String name;
    private String oldname;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.tape_rename";

    public TapeRename() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        this.tapeID = Integer.valueOf(request.getParameter("id"));
        this.name = request.getParameter("name");
        oldname =  request.getParameter("name");
    }

    
    
    public String modifyTape(){
        if (!MyConstants.checkTapeName(name)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "notapename"), ""));
            return null;
        }
        if (oldname.equals(name)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "samename"), ""));
            return null;
        }
        VtlInterface vtli = InterfaceFactory.getVtlInterfaceInstance();
        TapeInformation[] tapes = vtli.getAllTapesInfo();
        for (TapeInformation tape : tapes) {
            if (tape.name.equals(name)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "exitename"), ""));
                return null;
            }
        }
        
       
        boolean suc = vtli.renameTape(tapeID, name);
        if (!suc) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "f0"), ""));
            return null;
        }
        return "vaults?faces-redirect=true";
    }
    public TapeLibraryInformation getVtl() {
        return vtl;
    }

    public void setVtl(TapeLibraryInformation vtl) {
        this.vtl = vtl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
