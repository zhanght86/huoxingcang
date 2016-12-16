/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrator
 */
@ManagedBean
@SessionScoped
public class SharePathTable implements Serializable{

    private PathDataModel model = new PathDataModel();
    private Path selectedPath = new Path();
    private Path editPath = new Path();
    private HtmlCommandButton onLine;
    private HtmlCommandButton offLine;
    private String  path;
    private boolean authorityButtonStatus;

    public SharePathTable() {

        Path p = new Path("SYSVOL/NAS/test", false, false, false, "40.0KB", "1.52TB", "/SYSVOL/NAS/test");
        model.addPath(p);
        p = new Path("SYSVOL/NAS/aaa", false, false, false, "40.0KB", "1.52TB", "/SYSVOL/NAS/test");
        model.addPath(p);
    }

    public boolean isAuthorityButtonStatus() {
        return authorityButtonStatus;
    }

    public void setAuthorityButtonStatus(boolean authorityButtonStatus) {
        this.authorityButtonStatus = authorityButtonStatus;
    }

    public PathDataModel getModel() {
        return model;
    }

    public void setModel(PathDataModel model) {
        this.model = model;
    }

    public Path getSelectedPath() {
        return selectedPath;
    }

    public void setSelectedPath(Path selectedPath) {
        this.selectedPath = selectedPath;
    }

    public Path getEditPath() {
        return editPath;
    }

    public void setEditPath(Path editPath) {
        this.editPath = editPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HtmlCommandButton getOffLine() {
        return offLine;
    }

    public void setOffLine(HtmlCommandButton offLine) {
        this.offLine = offLine;
    }

    public HtmlCommandButton getOnLine() {
        return onLine;
    }

    public void setOnLine(HtmlCommandButton onLine) {
        this.onLine = onLine;
    }

    public void addPath(String pathName) {
        Path p = new Path(pathName);
        this.model.addPath(p);
    }

    public void removePath() {
        this.model.removePath(selectedPath);
    }
//    public void onRowSelect(SelectEvent event) {
//       
//        //FacesContext context = FacesContext.getCurrentInstance();
//        //ShareList share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext());
//        ACL acl = (ACL) event.getObject();
//        Path p = (Path) event.getObject();
//        //share.setSelectedACL(acl);
//        if(p.isStatus()) {
//            this.onLine.setDisabled(true);
//            this.offLine.setDisabled(false);
//        }
//
//    }

    public void configStatusForPath() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        String path = context.getExternalContext().getRequestParameterMap().get("path");
        Path p = this.model.getPathByName(path);
//        if (p.isCifsStatus() || p.isNfsStatus()) {
//            RequestContext.getCurrentInstance().addCallbackParam("result", Constant.closeShare);
//            return;
//        }
//        this.model.configStatus(path, true);
        p.setStatus(false);

    }

//    public void initConfigStatus() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        String path = context.getExternalContext().getRequestParameterMap().get("path");
//        this.path = path;
//        Path p = this.model.getPathByName(path);
//        if (p.isStatus()) {
//            if (MySession.getShareFromSession().getSpecifiedPathStatus(path) || MySession.getNFSData().getSpecifiedPathStatus(path)) {
//                RequestContext.getCurrentInstance().addCallbackParam("result", "failed");
//                return;
//            }else {
//                RequestContext.getCurrentInstance().addCallbackParam("result", "ok");
//                return;
//            }
//             //p.setStatus(true);
//        }
//        else {
//            p.setStatus(true);
//            RequestContext.getCurrentInstance().addCallbackParam("result", "on");
//        }
//
//    }
    public void initConfigParam() {
        editPath = new Path();
        editPath.setName(this.selectedPath.getName());
        editPath.setStartDeduplicate(this.selectedPath.isStartDeduplicate());
        editPath.setStartCompress(this.selectedPath.isStartCompress());
        if(!editPath.isStartDeduplicate()) {
            //editPath.disableDataCheck();
            editPath.setStartDataCheckBoxStatus(true);
                    
        }else {
            editPath.setStartDataCheckBoxStatus(false);
        }
        if(!editPath.isStartCompress()) {
            //editPath.disableDataCheck();
            editPath.setLevelSpinnerStatus(true);
             
        }else {
            editPath.setLevelSpinnerStatus(false);
        }
        editPath.setStartQuota(this.selectedPath.isStartQuota());
        if(!editPath.isStartQuota()) {
            //editPath.disableMaxSpaceText();
            editPath.setMaxSpaceStatus(true);
        }else {
            
            editPath.setMaxSpaceStatus(false);
        }
        editPath.setSelectedLevel(this.selectedPath.getSelectedLevel());
        editPath.setMaxSpace(this.selectedPath.getMaxSpace());
        editPath.setSelectedBlockSize(this.selectedPath.getSelectedBlockSize());
        
    }
    
    public void configParam() {
        
       Path sharePath = this.model.getPathByName(this.editPath.getName());
       sharePath.setStartDeduplicate(this.editPath.isStartDeduplicate());
       sharePath.setStartCheck(this.editPath.isStartCheck());
      // sharePath.getCheckData().setValue(this.editPath.getCheckData().getValue());
       sharePath.setStartCompress(this.editPath.isStartCompress());
       sharePath.setSelectedLevel(this.editPath.getSelectedLevel());
       sharePath.setStartQuota(this.editPath.isStartQuota());
      // sharePath.getMaxSpaceText().setValue((this.editPath.getMaxSpaceText().getValue().toString()));
      sharePath.setMaxSpace(this.editPath.getMaxSpace());
       sharePath.setSelectedBlockSize(this.editPath.getSelectedBlockSize());
       
       
    }
    public void  initParamDialog() {
        editPath = new Path();
        editPath.setName(this.selectedPath.getName());
        editPath.setStartDeduplicate(this.selectedPath.isStartDeduplicate());
        editPath.setStartCompress(this.selectedPath.isStartCompress());
         editPath.setStartCheck(this.selectedPath.isStartCheck());
//        if(!editPath.isStartDeduplicate()) {
//            editPath.setStartCheck(false);
//        }else {
//            editPath.setStartCheck(true);
//        }
        editPath.setStartQuota(this.selectedPath.isStartQuota());
//        if(!editPath.isStartQuota()) {
//            editPath.disableMaxSpaceText();
//        }else {
//            
//            editPath.enableMaxSpaceText();
//        }
        editPath.setSelectedLevel(this.selectedPath.getSelectedLevel());
        editPath.setMaxSpace(this.selectedPath.getMaxSpace());
        editPath.setSelectedBlockSize(this.selectedPath.getSelectedBlockSize());
    }
    public void  saveConfigParam() {
        
       Path sharePath = this.model.getPathByName(this.editPath.getName());
       sharePath.setStartDeduplicate(this.editPath.isStartDeduplicate());
      // sharePath.setStartCheck(Boolean.parseBoolean(this.editPath.getCheckData().getValue().toString()));
     //  sharePath.getCheckData().setValue(this.editPath.getCheckData().getValue());
       sharePath.setStartCheck(this.editPath.isStartCheck());
       sharePath.setStartCompress(this.editPath.isStartCompress());
       sharePath.setSelectedLevel(this.editPath.getSelectedLevel());
       sharePath.setStartQuota(this.editPath.isStartQuota());
       sharePath.setMaxSpace(this.editPath.getMaxSpace());
       sharePath.setSelectedBlockSize(this.editPath.getSelectedBlockSize());
       
       
    }
    
    public void  rowSelectListen(SelectEvent event) {
       Path p = (Path) event.getObject();
        if(!p.isStatus()) {
            this.authorityButtonStatus = true;
        }else {
            this.authorityButtonStatus = false;
        }
    }
}
