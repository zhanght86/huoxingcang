/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.common.bean.VolumeGroupInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vtl.util.MyUtility;
import com.marstor.msa.vtl.web.VtlInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
/**
 *
 * @author Administrator
 */
@ManagedBean(name = "addTapeBeanX")
@ViewScoped
public class CreateTapeX  implements Serializable{

    private String barcode = "MARS1000000000";
    private String mediaPath;
    private String count = 1+"";
    private String size = 10+"";
    private boolean byneed = true;
    private boolean compression;
    private boolean worm;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "vtl.add_tape_x";
    private String volume;
    private List volumes;
    private int index = 0;

    public CreateTapeX() {
//        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        initData();
        initVolumes();
    }

    public final void initData() {
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        this.barcode = "TAPE"+ vtl.getCurrentTapeBarcode();
          SystemOutPrintln.print_vtl("barcode="+barcode);
    }
      public final void initVolumes() {
        VolumeInterface common = InterfaceFactory.getVolumeInterfaceInstance();
        VolumeGroupInformation[] vgs = common.getAllVolumeGroup();
        if (vgs == null) {
            return;
        }
        volumes = new ArrayList();
        for (VolumeGroupInformation vg : vgs) {
            volumes.add(vg.name);
        }
    }

    public String createTape() {
        SystemOutPrintln.print_vtl("this.count="+this.count);
        if (!MyUtility.checkNum(this.count, false)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v0"),""));
            return null;
        }
        
//         LicenseInformation capacityLicense = node.serverNode.getXXModuleXXXFunctionLicense(Constants.MODEL_ID_BASIC, Constants.FUNCTIONID_BASIC_DISK_CAPACITY);
//        long licenseCapacity = capacityLicense.getFunctionNumber() * 1024L;
//        if (!MyUtility.checkSize(size.trim(), false, licenseCapacity)) {
//            Constants.showErrorMessage("磁带大小超出上限！");
//            tfCount.requestFocus();
//            return null;
//        }
        
        if (!MyUtility.checkNum(size, false)) {  //暂时用
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v2"),""));
            return null;
        }
        
        if (!MyUtility.checkTapeCode(this.barcode)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v1"),""));
            return null;
        }
        boolean allzero = true;
        String bnum = this.barcode.substring(4);
        for (int k = 0; k < bnum.length(); k++) {
            if (bnum.substring(k).startsWith("0")) {
                continue;
            } else {
                allzero = false;
                break;
            }
        }
        if (allzero) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "v1"), ""));
            return null;
        }
         int countInt = Integer.parseInt(this.count);
        int sizeInt  = Integer.parseInt(this.size);
        mediaPath = "/" + volume + "/TAPE";
         int demandSize = 0;
        if (byneed) {
            demandSize = 4;
        }
        VtlInterface vtl = InterfaceFactory.getVtlInterfaceInstance();
        String pre = this.barcode.substring(0, 4);
        for (int i = 0; i < countInt; i++) {

            String num = this.barcode.substring(4);
            SystemOutPrintln.print_vtl("num=" + num);
            if (!num.startsWith("0")) {
                num = String.valueOf(Long.valueOf(num) + index);
            } else {
                for (int k = 1; k < num.length(); k++) {
                    if (num.substring(k).startsWith("0")) {
                        continue;
                    } else {
                        String tmp = "";
                        num = String.valueOf(Long.valueOf(num.substring(k)) + index);
                        for (int j = 0; j < k; j++) {
                            tmp = tmp + "0";
                        }
                        num = tmp + num;
                        break;
                    }
                }
            }
             SystemOutPrintln.print_vtl("num2="+num);
            
             boolean isBarcodeExit = vtl.isLegalTapeBarcode(pre+num);
             if(!isBarcodeExit){
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "barcode_exit"), ""));
                return null;
             }
            
            
            System.out.println("i="+(i+1));
            boolean ret = vtl.createTape(mediaPath, pre+num, sizeInt, demandSize, compression, worm);
            if (!ret) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), ""));
                return null;
            }
            index++;
            
        }
       
        return "tapes?faces-redirect=true";
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List getVolumes() {
        return volumes;
    }

    public void setVolumes(List volumes) {
        this.volumes = volumes;
    }
    
    
}
