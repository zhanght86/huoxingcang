/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.ServiceInformation;
import com.marstor.msa.common.model.SysService;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "serviceBean")
@ViewScoped
public class ServiceBean implements Serializable {

    private MSAResource res = new MSAResource();
    private List<SysService> list;
    private SysService sService;
    private int userType;
    private static final String onLine = "../resources/common/picture/online.png";
    private static final String offLine = "../resources/common/picture/offline.png";

    public ServiceBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        HttpSession session = request.getSession();
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        userType = user.getType();
        initList();
    }

    public List<SysService> getList() {

        return list;
    }

    private void initList() {
        list = new ArrayList<SysService>();
        boolean bRunning = true;
        CommonInterface instance = InterfaceFactory.getCommonInterfaceInstance();
        while (bRunning) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServiceBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            ServiceInformation[] services = instance.getServices();
            for (ServiceInformation info : services) {
                if (info.getStatus().equals("online") || info.getStatus().equals("disabled")) {
                    bRunning = false;
                } else {
                    bRunning = true;
                    break;
                }
            }
        }

        ServiceInformation[] infos = instance.getServices();
        for (ServiceInformation info : infos) {
            SysService sysService = new SysService();

            if (info.name().equals("iSCSIInitiator")) {
                sysService.setStrName("iSCSI Initiator");
            } else {
                sysService.setStrName(info.name());
            }

            sysService.setiID(info.getServiceID());
            sysService.setStrStatus(info.getStatus());
            if (info.getStatus().equals("online")) {
                if (userType == 2) {
                    sysService.setBVisible(false);
                } else {
                    sysService.setBVisible(true);
                }
                sysService.setStrCondition(res.get("bStart"));
                sysService.setStrOperation(res.get("stop"));
                sysService.setStrImage(offLine);
            } else if (info.getStatus().equals("disabled")) {
                sysService.setBVisible(true);
                sysService.setStrCondition(res.get("bStop"));
                sysService.setStrOperation(res.get("start"));
                sysService.setStrImage(onLine);
            }
            System.out.println(info.name() + "---" + info.status + "---" + info.serviceID);
            list.add(sysService);
        }
    }

    public void setList(List<SysService> list) {
        this.list = list;
    }

    public SysService getSService() {
        return sService;
    }

    public void setSService(SysService sService) {
        this.sService = sService;
    }

    public void operation() {
        boolean operation = false;
        if (sService.getStrStatus().equals("online")) {
            operation = stopService();
            if (operation) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("stopSuccess"), ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("stopFailed"), ""));
            }
        } else {
            operation = startService();
            if (operation) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("startSuccess"), ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("startFailed"), ""));
            }
        }
        initList();
    }

    public void restartService() {
        if (!stopService()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("restartFailed"), ""));
            return;
        }
        initList();
        if (!startService()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("restartFailed"), ""));
            return;
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("restartSuccess"), ""));
        initList();
    }

    private boolean startService() {
        CommonInterface instance = InterfaceFactory.getCommonInterfaceInstance();
        boolean setService = instance.setService(sService.getiID(), 1);
        return setService;
    }

    private boolean stopService() {
        CommonInterface instance = InterfaceFactory.getCommonInterfaceInstance();
        boolean setService = instance.setService(sService.getiID(), 2);
        return setService;
    }
}
