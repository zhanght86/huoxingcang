/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;
import com.marstor.msa.nas.model.Share;
import com.marstor.msa.nas.model.ShareListBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
/**
 *
 * @author Administrator
 */
@javax.faces.bean.ManagedBean(name = "editlink")
@javax.faces.bean.SessionScoped
public class EditBeanLink implements Validator {

    private String path = "";
    private String shareNameEdit = "";
    private String shareNameView = "";
    private String cifs = "";
    private boolean  cifsOnOrOff;
    private HtmlInputText shareText;
    private String  errorInfo;
    private String sl;
    private HtmlCommandButton  button ;

  
    public HtmlInputText getShareText() {
        return shareText;
    }

    public void setShareText(HtmlInputText shareText) {
        this.shareText = shareText;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public HtmlCommandButton getButton() {
        return button;
    }

    public void setButton(HtmlCommandButton button) {
        this.button = button;
    }

    
    
    public EditBeanLink() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        ShareListBean  share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext()); 
        path = share.getPath();
        cifs = share.getCifs();
        shareNameEdit = share.getShareName();
        if(cifs.equals("已开启")) {
           cifsOnOrOff = true;
        }else {
            cifsOnOrOff = false; 
//            shareNameView = share.getShareName();
        }
       // this.button.setValue("test");
       // this.button.setDisabled(false);
        
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    
    public String getShareNameView() {
        return shareNameView;
    }

    public void setShareNameView(String shareNameView) {
        this.shareNameView = shareNameView;
    }


    public boolean isCifsOnOrOff() {
        return cifsOnOrOff;
    }

    public void setCifsOnOrOff(boolean cifsOnOrOff) {
        this.cifsOnOrOff = cifsOnOrOff;
    }
    
    
     public String getPath() {

        return path;
    }

    public String getShareNameEdit() {
        return shareNameEdit;
    }

    public void setShareNameEdit(String shareNameEdit) {
        this.shareNameEdit = shareNameEdit;
    }

    

    public void setPath(String path) {
        this.path = path;
    }

    

    public String getCifs() {
        return cifs;
    }

    public void setCifs(String cifs) {
        this.cifs = cifs;
    }
//    public void save() {
//        
//        
//        FacesContext context = FacesContext.getCurrentInstance();
//        ShareListBean  share = (ShareListBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{sharelist}", ShareListBean.class).getValue(context.getELContext()); 
////        Share bean = new Share();
////        bean.setCifs(cifs);
////        bean.setPath(path);
//        //share.getSelectedShareBean().setPath(path);
//        //share.getSelectedShareBean().setCifs(cifs);
//        if(this.isCifsOnOrOff()&&share.isShareNameExist(this.path,this.shareNameEdit)) {
//            //this.errorInfo = "??????????????";
//            share.setErrorInfo("共享名称已经存在");
//            
//            sl = "失败";
//            RequestContext.getCurrentInstance().addCallbackParam("sl", sl);
//            return ;
//        }
//        Share  bean = share.getShareByPath(path);
////        if(cifsOnOrOff) {
////            bean.setCifs("已开启");
////            bean.setShareName(shareNameEdit);
////        }else{
////            bean.setCifs("已关闭");
////            bean.setShareName("");
////        }
//        sl = "成功";
//        
//        RequestContext.getCurrentInstance().addCallbackParam("sl", sl);
//        //return  sl;
//    }
    public void setText() {
        if(cifsOnOrOff){
            shareText.setDisabled(false);
        }else {
            shareText.setDisabled(true);
        }
    }

   
    public void validateShareName(FacesContext context, UIComponent uic, Object value) throws ValidatorException {
        String  share = (String) value;
        if(!share.matches("^[a-zA-Z0-9]+$")&& this.cifsOnOrOff) {
            FacesMessage message = new FacesMessage("ShareName can not be empty.");
            throw  new ValidatorException(message);
        }
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String  shareName = (String) o;
        
        Debug.print(uic+"aaafdsgfg");
        if(shareName==null || shareName.equals("")) {
           FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "共享名称不能为空", "共享名称不能为空");
            throw new ValidatorException(message); 
        }
        if(!shareName.matches("^[a-zA-Z0-9]+$")) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "共享名由字母和数字组成", "输入的共享名称必须由字母和数字组成");
            throw new ValidatorException(message);
        }
        
        
    }
    
    public String backOut() {
        
        return  "nas_cifs";
    }
    public void change() {
        if(this.cifsOnOrOff) {
           //this.shareNameEdit = "true";
           // this.shareText.setReadonly(false);
            this.shareText.setDisabled(false);
            this.button.setDisabled(false);
        }
        if(!this.cifsOnOrOff) {
           // this.shareNameEdit = "false";
            //this.shareText.setReadonly(true);
            this.shareText.setDisabled(true);
            this.button.setDisabled(true);
        }
    }
    
}

