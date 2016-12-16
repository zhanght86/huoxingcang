package com.marstor.msa.scsi.managedbean; 

import com.marstor.msa.scsi.util.*;

import com.marstor.msa.common.bean.TargetInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.scsi.validator.TargetAliasValidator;
import com.marstor.msa.util.InterfaceFactory;
import java.io.PrintStream;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class CreateISCSITarget
{
  private String name;
  private String alias;
  private String targetStr = "";
  private String num = "";

  public CreateISCSITarget()
  {
    SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
    this.name = scsi.getAvailableTargetName();

    this.alias = scsi.getAvailableAlias();
    if (this.name == null)
      return;

    String[] array = this.name.trim().split(":");
    if ((array != null) && (array.length > 1)) {
      this.targetStr = array[0];
      this.targetStr += ":";
      this.num = array[1];
    }
  }

  public String getNum()
  {
    return this.num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  public String getTargetStr() {
    return this.targetStr;
  }

  public void setTargetStr(String targetStr) {
    this.targetStr = targetStr;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAlias() {
    return this.alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String save() {
    MSAResource res;
    this.name = this.targetStr + this.num;
    String[] array = this.name.trim().split(":");
    if ((array != null) && (array.length > 1)) {
      String num = array[1];
      if ((num.length() > 64) || (!(num.matches("^[0-9]+$")))) {
        res = new MSAResource();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("invalidTarget"), res.get("invalidTarget")));
        return null;
      }
    }
    if (this.alias.equals("")) {
      res = new MSAResource();
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("emptyAlias"), res.get("emptyAlias")));
      return null;
    }
    if (!(TargetAliasValidator.checkAliasName(this.alias))) {
      res = new MSAResource();
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("invalidAlias"), res.get("invalidAlias")));
      return null;
    }
    SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
    boolean flag = scsi.createiSCSITarget(this.alias, this.name);
    if (!(flag)) {
    //  print();
     res = new MSAResource();
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("addFailed"), res.get("addFailed")));
      Debug.print("Createiscsitarget " + flag);
      return null;
    }
    Debug.print("all targets ");
    TargetInformation[] allTargetInfo = scsi.getAllTarget();
    TargetInformation[] arr = allTargetInfo; 
    int len = arr.length;
    for (int i = 0; i < len; ++i) 
    { TargetInformation targetInfo = arr[i];
      Debug.print("target " + targetInfo.getTargetName());
    }

    return "scsi_target?faces-redirect=true&amp;activeIndex=1";
    //return "/scsi/scsi_target.xhtml#iSCSI";
  }

//  public void print() {
//    SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
//    TargetInformation[] allTargetInfo = scsi.getAllTarget();
//    TargetInformation[] arr$ = allTargetInfo; int len$ = arr$.length; for (int i$ = 0; i$ < len$; ++i$) { TargetInformation targetInfo = arr$[i$];
//
//      Debug.print("target " + targetInfo.getTargetName());
//    }
//  }

  public void changeInputText()
  {
    if (!(this.name.startsWith(this.targetStr + ":")))
      this.name = this.targetStr + ":";
  }
}