/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.managedbean;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "processBean")
@RequestScoped
public class ProcessBean  implements Serializable{

    public String strProcess = "0%";
    

    /**
     * Creates a new instance of ProcessBean
     */
    public ProcessBean() {
        haveprocess();
    }

    public String haveprocess() {
        
        String volumeName = null;
        String badDiskName = null;
        String newDiskName = null;
        FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
        DataDiskReplaceBean data = (DataDiskReplaceBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{dataDiskReplaceBean}", DataDiskReplaceBean.class).getValue(context.getELContext());
        volumeName = data.volumeName;
        badDiskName = data.badDiskName;
        newDiskName = data.newDiskName;

        SystemOutPrintln.print_volume("volumeName=" + volumeName + ",badDiskName=" + badDiskName + ",newDiskName=" + newDiskName);
        VolumeInterface volumeIn = InterfaceFactory.getVolumeInterfaceInstance();
        String state = volumeIn.queryReplaceState(volumeName, badDiskName, newDiskName);
        SystemOutPrintln.print_volume("进度条state=" + state);
        String returnStr = null;
//        state= 0+"";
        if (state.equalsIgnoreCase("Complete")) {
            strProcess = "100%";
            data.notUnusedDisk = false;
             data.cantButtonUse = false;
             data.cantuse = false;
             data.isPrimaryOnline = false;
             data.isSameName = false;
             data.isprocess = false;
            RequestContext.getCurrentInstance().execute("window.location.href='volumegroup.xhtml'");
//             RequestContext.getCurrentInstance().execute("href='volumegroup.xhtml'");

//            returnStr = "volumegroup?faces-redirect=true";
        } else {
            strProcess = state;
            returnStr = strProcess;
             SystemOutPrintln.print_volume("进度条=" + strProcess);
             
        }
        SystemOutPrintln.print_volume("进度条=" + returnStr);
        return returnStr;
    }

    public String getStrProcess() {
        return strProcess;
    }

    public void setStrProcess(String strProcess) {
        this.strProcess = strProcess;
    }
}
