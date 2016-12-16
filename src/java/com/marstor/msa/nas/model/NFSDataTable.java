/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

import javax.faces.component.html.HtmlSelectBooleanCheckbox;
/**
 *
 * @author Administrator
 */
@ManagedBean
@SessionScoped
public class NFSDataTable implements Serializable{
    
    private NFSDataModel  dataModel = new NFSDataModel();
    private NFS  selectedNFS = new NFS();
    private NFS  configNFS = new NFS();
    private  HtmlSelectBooleanCheckbox test;

    public NFSDataTable() {
        
        ShareListBean  share = MySession.getShareFromSession();
        List<Share> paths =  share.getList();
        for(Share p : paths) {
            NFS  nfs = new NFS(p.getPath(), false);
            dataModel.addNfsData(nfs);
        }
        
//        NFS  nfs;
//        ArrayList<String>  ipStr1 = new ArrayList<String>();
//        nfs = new NFS("/SYSVOL/NAS/test1", true);
//        ipStr1.add("10.10.1.1");
//        ipStr1.add("10.10.1.5");
//        nfs.setRw(ipStr1);
////        ipStr1 = new ArrayList<String>();
////        ipStr1.add("*");
////        nfs.setReadOnly(ipStr1);
//        dataModel.addNfsData(nfs);
//        
//        List<String>  ipStr2 = new ArrayList<String>();
//        ipStr2.add("10.20.1.1");
//        ipStr2.add("10.20.1.5");
//        nfs = new NFS("/SYSVOL/NAS/test2", false);
//        nfs.setRw(ipStr2);
////        ipStr2 = new ArrayList<String>();
////        ipStr2.add("*");
//        dataModel.addNfsData(nfs);
        
                
    }

    public HtmlSelectBooleanCheckbox getTest() {
        return test;
    }

    public void setTest(HtmlSelectBooleanCheckbox test) {
        this.test = test;
    }

    public NFSDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(NFSDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public NFS getSelectedNFS() {
        return selectedNFS;
    }

    public void setSelectedNFS(NFS selectedNFS) {
        this.selectedNFS = selectedNFS;
    }

    public NFS getConfigNFS() {
        return configNFS;
    }

    public void setConfigNFS(NFS configNFS) {
        this.configNFS = configNFS;
    }
    
    public String  configNFSAction() {
       
        FacesContext context = FacesContext.getCurrentInstance();
        String  path="";
        path = context.getExternalContext().getRequestParameterMap().get("path");
        configNFS = new NFS();
        configNFS.setPath(path);
        NFS  nfs = this.dataModel.getNFSByPath(path);
        configNFS.setStatus(nfs.isStatus());
        configNFS.setRw(nfs.getRw());
        configNFS.setReadOnly(nfs.getReadOnly());
        configNFS.setRoot(nfs.getRoot());
        if(nfs.isStatus()) {
            configNFS.enableButton();
           configNFS.enableCheckbox();
        }else {
            configNFS.disableButton();
           configNFS.disableCheckbox();
        }
//        if(this.test != null) {
//            test.setDisabled(true);
//        }
        
//        if(!nfs.isStatus()) {
//            selectedNFS.disableButton();
//        }
        return "nas_nfs_edit?faces-redirect=true";
    }
    public void  save() {
       
        if(configNFS.getRw().size()<1 && configNFS.getReadOnly().size()<1) {
            RequestContext.getCurrentInstance().addCallbackParam("result", Constant.wrongConfigNFS);
            return ;
        }
        
        this.dataModel.updateNFS(configNFS);
        RequestContext.getCurrentInstance().addCallbackParam("result", Constant.configSuccess);
    }
   public boolean getSpecifiedPathStatus(String  path) {
        NFS  nfs  = this.getDataModel().getNFSByPath(path);
        return  nfs.isStatus();
    }
    
}
