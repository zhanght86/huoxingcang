/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.sync.bean.Snapshot;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
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
@ManagedBean(name = "createSnap_vtlBean")
@ViewScoped
public class CreateSnap_vtlBean  implements Serializable{

    /**
     * Creates a new instance of CreateSnap_vtlBean
     */
    private String path = "";
    private String snapAlias;
    private String expirationDate;//单位：天
    private String radioValue;
    private boolean expirDisabled;
    private String module;
    private String returnURL;

    public CreateSnap_vtlBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        if (request.getParameter("path") != null) {
            path = request.getParameter("path");
        }
        this.radioValue = "1";
        this.expirationDate = "1";
        String returnURL = request.getParameter("returnURL");
        String module = request.getParameter("module");
        if (module != null) {
            this.module = module;
        }
        if (returnURL != null) {
            this.returnURL = returnURL;
        }

    }

    public boolean isExpirDisabled() {
        return expirDisabled;
    }

    public void setExpirDisabled(boolean expirDisabled) {
        this.expirDisabled = expirDisabled;
    }

    public String getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(String radioValue) {
        this.radioValue = radioValue;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSnapAlias() {
        return snapAlias;
    }

    public void setSnapAlias(String snapAlias) {
        this.snapAlias = snapAlias;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void listen() {
        if (this.radioValue.equals("1")) {
            this.expirDisabled = false;
        } else {
            this.expirDisabled = true;
        }
    }

    public String save() {
        //验证手动快照的名字，命名不能为auto
        MSAResource res = new MSAResource();
        if (this.snapAlias == null || snapAlias.equalsIgnoreCase("")) {
            //Constants.showWarningMessage(this.setResource("manual.snapshot.anotherName.null"));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("aliasNotEmpty"), res.get("aliasNotEmpty")));
            return null;
        }
        if (snapAlias.length() < 21) {
            if (!snapAlias.matches("[a-zA-Z0-9]+")) {
                //快照名称由字母、数字或两者组合而成，且最长不超过20个字符(名称不能为“AUTO”)。
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("snapComposed"), res.get("snapComposed")));
                return null;
            }
            if (this.snapAlias.equalsIgnoreCase("AUTO")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("aliasAuto"), res.get("aliasAuto")));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("aliasMaxLength"),res.get("aliasMaxLength")));
            return null;
        }
        List<Snapshot> snaps = InterfaceFactory.getSYNCInterfaceInstance().getGetManualSnapshot(path);
        if (snaps == null) {
            return null;
        }
        for (int i = 0; i < snaps.size(); i++) {
            String snapName = snaps.get(i).getStrName();
            String array[] = snapName.split("_");
            String alias = "";
            if (array != null && array.length > 1) {
                alias = array[array.length - 2];
            }
            if (this.snapAlias.equals(alias)) {
                //快照名称已存在。
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("snapNameExist"), res.get("snapNameExist")));
                return null;
            }
        }

        if (this.radioValue.equals("1")) {
            //验证有效期是否为数字，最小值为1
            if (this.expirationDate.matches("^[0-9]*[1-9][0-9]*$")) {
//                System.out.println("整型长度#########"+(Integer.SIZE - 1));

                if (expirationDate.length() < 10) {
                    int n = Integer.parseInt(expirationDate);
//                System.out.println("n=" + n);
                    if (n < 1) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("inputValidDays"), res.get("inputValidDays")));
                        return null;
                    }
                    int param[] = InterfaceFactory.getSYNCInterfaceInstance().getServerCfg();
                    if (param != null && param.length > 3) {
                        int manualSnapshotOutofDay = param[3];
                        if (n > manualSnapshotOutofDay) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("snapValid") + manualSnapshotOutofDay + res.get("day") + "。", res.get("snapValid") + manualSnapshotOutofDay + res.get("day") + "。"));
                            return null;
                        }
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("inputValidDays"), res.get("inputValidDays")));
                    return null;
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("inputValidDays"), res.get("inputValidDays")));
                return null;
            }
        } else {
            this.expirationDate = -1 + "";
        }
        String snapName = InterfaceFactory.getSYNCInterfaceInstance().createSnapshot(path, this.snapAlias, this.expirationDate);
        if (snapName == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createManualSnapFailed"),  res.get("createManualSnapFailed")));
            return null;
        }
        String param = "path=" + this.path + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "vtl_snap_sync?faces-redirect=true&amp;" + param;

    }

    public String goBack() {
        String param = "path=" + this.path + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "vtl_snap_sync?faces-redirect=true&amp;" + param;
    }
}
