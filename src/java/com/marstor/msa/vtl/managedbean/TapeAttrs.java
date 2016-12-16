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
public class TapeAttrs  implements Serializable{

    private int valutid = -2;
    public String valutname = "";
    public String tapeName = "";
    public boolean writeProtect = false;
    public boolean onDemand = false;
    public boolean enableCompress = false;
    public String tapeSize;
    private boolean worm = false;
    private TapeInformation tape;
    private int id;
    private MSAResource res = new MSAResource();
    private String basename = "vtl.tape_attrs";
    private boolean canModifyTapeCapacity = true;
    private boolean canProtectedAndCompress = true;


    public TapeAttrs() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        id = Integer.valueOf(request.getParameter("id"));
        valutid = Integer.valueOf(request.getParameter("valutid"));
        initData();
        cantuse();
    }

    public final void initData() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        this.tape = vtl.getTapeInfo(id);
        if(tape == null){
            return;
        }
        tapeName = tape.name;
        tapeSize = tape.tapeSize/(1024 * 1024 * 1024) + "";
        writeProtect = tape.writeProtect;
        onDemand = tape.onDemand;
        enableCompress = tape.enableCompression;
        worm = tape.worm;
   
        VaultInformation vault = vtl.getVault(valutid);
        if (vault == null) {
            return;
        }
        valutname = vault.name;
       SystemOutPrintln.print_vtl("valutid="+valutid);
        SystemOutPrintln.print_vtl("valutname="+valutname);
        if (valutid == MyConstants.VAULT_TYPE_FREE) {
            valutname = res.get(basename, "vault_type_free");
        } else if (valutid == MyConstants.VAULT_TYPE_IMPORT) {
        } else if (valutid == MyConstants.VAULT_TYPE_FOREIGN) {
        } else if (valutname.equals("offline")) {
            valutname = res.get(basename, "vault_type_offline");
        }
  
    }
    
    public void cantuse() {
        if (tape.locationTypeID == MyConstants.LOCATION_TYPE_VAULT) {
            if (valutname.equals(res.get(basename, "vault_type_free"))) {
                canModifyTapeCapacity = false;
            } else if (valutname.equals(res.get(basename, "vault_type_offline"))) {
            } else {
                canProtectedAndCompress = false;
            }
        }
    }

    public String modifyTape() {
          if (tape.elementType != 0) {
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "nomodify"), ""));
            return null;
        }
        if (!MyConstants.checkSize(tapeSize)) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "no_tapeSize"), ""));
            return null;
        }
        int tapeSizeI = Integer.parseInt(tapeSize);
        if (!canModifyTapeCapacity) {
            if ((1024L * 1024L * 1024L * tapeSizeI) < tape.tapeSize) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "no_mintapeSize"), ""));
                return null;
            }
        }
        
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        SystemOutPrintln.print_vtl("writeProtect="+writeProtect);
        boolean ret = vtl.modifyTape(id, tapeName, 
                writeProtect, enableCompress, onDemand, tapeSizeI);
        if (!ret) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), ""));
            return null;
        }
        return "vaults?faces-redirect=true";
    }
    
   


    public TapeInformation getTape() {
        return tape;
    }

    public void setTape(TapeInformation tape) {
        this.tape = tape;
    }
    
    public boolean isWorm() {
        return worm;
    }

    public void setWorm(boolean worm) {
        this.worm = worm;
    }

    public String getTapeName() {
        return tapeName;
    }

    public void setTapeName(String tapeName) {
        this.tapeName = tapeName;
    }

    public boolean isWriteProtect() {
        return writeProtect;
    }

    public void setWriteProtect(boolean writeProtect) {
        this.writeProtect = writeProtect;
    }

    public boolean isOnDemand() {
        return onDemand;
    }

    public void setOnDemand(boolean onDemand) {
        this.onDemand = onDemand;
    }

    public boolean isEnableCompress() {
        return enableCompress;
    }

    public void setEnableCompress(boolean enableCompress) {
        this.enableCompress = enableCompress;
    }

    public String getTapeSize() {
        return tapeSize;
    }

    public void setTapeSize(String tapeSize) {
        this.tapeSize = tapeSize;
    }

    public String getValutname() {
        return valutname;
    }

    public void setValutname(String valutname) {
        this.valutname = valutname;
    }

    public boolean isCanModifyTapeCapacity() {
        return canModifyTapeCapacity;
    }

    public void setCanModifyTapeCapacity(boolean canModifyTapeCapacity) {
        this.canModifyTapeCapacity = canModifyTapeCapacity;
    }

    public boolean isCanProtectedAndCompress() {
        return canProtectedAndCompress;
    }

    public void setCanProtectedAndCompress(boolean canProtectedAndCompress) {
        this.canProtectedAndCompress = canProtectedAndCompress;
    }
    

}
