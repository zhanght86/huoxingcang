/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.managedbean;

import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.util.InterfaceFactory;
import com.marstor.msa.vbox.web.MsaVMInterface;
import com.marstor.msa.vm.model.ISOPathBean;
import com.marstor.msa.vm.model.ISOPathDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "iSOPathList")
@ViewScoped
public class ISOPathList implements Serializable{

    public ISOPathBean isoPath;
    public List<ISOPathBean> isoList;
    public String newISOPath;
    private ISOPathDataModel mediumISOsModel;
    private ISOPathBean[] selectedISOs;
    private ISOPathBean selectedISO;
    public boolean cantEdit;
    private MSAResource res = new MSAResource();
    private String basename = "vm.vm_isoInfoTable";
    /**
     * Creates a new instance of ISOPathList
     */
    public ISOPathList() {
        getISOList();
        mediumISOsModel = new ISOPathDataModel(isoList);
        getMenu();
    }

     /**
     * 按钮初始化函数
     */
    public void getMenu() {
        cantEdit = false;
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        int userType = user.getType();
        if (userType != 2) {
            cantEdit = true;
        }
    }
    public ISOPathBean getIsoPath() {
        return isoPath;
    }

    public void setIsoPath(ISOPathBean isoPath) {
        this.isoPath = isoPath;
    }

    public List<ISOPathBean> getIsoList() {
        return isoList;
    }

    public void setIsoList(List<ISOPathBean> isoList) {
        this.isoList = isoList;
    }

    public ISOPathBean[] getSelectedISOs() {
        return selectedISOs;
    }

    public void setSelectedISOs(ISOPathBean[] selectedISOs) {
        this.selectedISOs = selectedISOs;
    }

    public void getISOList() {
        isoList = new ArrayList();
        MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
        List<String>  getISOInfos = vmface.getISOInfo();
        if (getISOInfos != null && getISOInfos.size() > 0) {
            for (String iso : getISOInfos) {
                isoPath = new ISOPathBean();
                isoPath.setIsoPath(iso);
                isoList.add(isoPath);

            }
        }

       

//        isoPath = new ISOPathBean();
//        isoPath.setIsoPath("SYStem/tab");
//        isoList.add(isoPath);
//
//        isoPath = new ISOPathBean();
//        isoPath.setIsoPath("Volume/lib");
//        isoList.add(isoPath);
//
//        isoPath = new ISOPathBean();
//        isoPath.setIsoPath("Disk/new");
//        isoList.add(isoPath);
//
//        isoPath = new ISOPathBean();
//        isoPath.setIsoPath("SYStem/baidu");
//        isoList.add(isoPath);
//
//        isoPath = new ISOPathBean();
//        isoPath.setIsoPath("SAS/box");
//        isoList.add(isoPath);
//
//        isoPath = new ISOPathBean();
//        isoPath.setIsoPath("SAS/vol");
//        isoList.add(isoPath);
//
//        isoPath = new ISOPathBean();
//        isoPath.setIsoPath("Disk/google");
//        isoList.add(isoPath);
//
//        isoPath = new ISOPathBean();
//        isoPath.setIsoPath("SYStem/java");
//        isoList.add(isoPath);
//
//        isoPath = new ISOPathBean();
//        isoPath.setIsoPath("SYStem/jdk");
//        isoList.add(isoPath);
//
//        isoPath = new ISOPathBean();
//        isoPath.setIsoPath("SYStem/jsp");
//        isoList.add(isoPath);

    }

    public ISOPathDataModel getMediumISOsModel() {
        return mediumISOsModel;
    }

    public void handleDelete() {

        if (selectedISOs != null && selectedISOs.length > 0) {
            RequestContext.getCurrentInstance().execute("deleteISO.show()");
            return;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "pleaseselete"), ""));
            return;
        }
    }
    
    public String handleDelete_real() {
       
        
        if (selectedISOs != null) {
            MsaVMInterface vmface = InterfaceFactory.getVMInterfaceInstance();
            for (int i = 0; i < selectedISOs.length; i++) {
               String path=  selectedISOs[i].getIsoPath();
               SystemOutPrintln.print_vm("path="+path);
                boolean removeiso = vmface.deleteISO(path);
                if (removeiso) {
                    SystemOutPrintln.print_vm("clear iso success");
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "removeiso_fail"), ""));
                    return null;
                }
            }
        }
      return "vm_isoInfoTable?faces-redirect=true";
//        if (selectedISOs != null) {
//            System.out.println("#########iso=" + selectedISOs.length);
//            for (int i = 0; i < selectedISOs.length; i++) {
//                isoList.remove(selectedISOs[i]);
//            }
//        }
    }

    public ISOPathBean getSelectedISO() {
        return selectedISO;
    }

    public void setSelectedISO(ISOPathBean selectedISO) {
        this.selectedISO = selectedISO;
    }

    public void handleDeleteISO() {

        isoList.remove(selectedISO);
    }

    public String getNewISOPath() {
        return newISOPath;
    }

    public void setNewISOPath(String newISOPath) {
        this.newISOPath = newISOPath;
    }

    public boolean isCantEdit() {
        return cantEdit;
    }

    public void setCantEdit(boolean cantEdit) {
        this.cantEdit = cantEdit;
    }


}
