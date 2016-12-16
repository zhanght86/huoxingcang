/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.nas.model.FileSysSnapSYNCInfo;
import com.marstor.msa.nas.model.GlobalSnapSYNCInfo;
import com.marstor.msa.sync.bean.FileSystemInfo;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class ConfigAutoSnap implements Serializable {

    private boolean startOrNot;
    private boolean previousStartOrNot;
    // private boolean deleteAutoSnapOrNot;
    // private boolean deleteAutoSnapRendered;
    private boolean existSnapOrNot;
    private String path;
    private String interval;//单位：分钟
    private String reserveNum;//单位：个
    private boolean inputDisabled;
    private int maxNum;
    private String module;
    private String returnURL;
    private String title = "";

    public ConfigAutoSnap() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        path = request.getParameter("path");
//        List<FileSystemInfo> fileSystems = InterfaceFactory.getSYNCInterfaceInstance().getFileSystemInfo();
//        if (fileSystems == null) {
//            return;
//        }
        String returnURL = request.getParameter("returnURL");
        String module = request.getParameter("module");
        if (module != null) {
            this.module = module;
        }
        MSAResource res = new MSAResource();
        if (this.module.equals("nas")) {
            this.title = res.get("nasTitle");
        } else if (this.module.equals("vtl")) {
            this.title = res.get("vtlTitle");
        }
        if (returnURL != null) {
            this.returnURL = returnURL;
        }
        Debug.print("path: " + path);
        Debug.print("init ConfigAutoSnap page get file System name : ");

        if (path != null) {
            FileSystemInfo fileSys = FileSysSnapSYNCInfo.getFileSystemInfo(this.path);
            if (fileSys != null) {
                Debug.print("file System name: " + fileSys.getStrName() + "    getiIsOpen:" + fileSys.getiIsOpen());
                if (fileSys.getSyncStatusInfo().getExistSnapshot() == 1) {
                    //this.deleteAutoSnapRendered = true;
                    this.existSnapOrNot = true;
                } else {
                    //  this.deleteAutoSnapRendered = false;
                    this.existSnapOrNot = false;
                }
                if (fileSys.getiIsOpen() == 1) {
                    // this.interval = String.valueOf(fileSys.getlSnapshotInterval());
                    //  Integer.valueOf(this.interval) * 60
                    this.interval = String.valueOf(fileSys.getlSnapshotInterval() / 60);
                    this.reserveNum = String.valueOf(fileSys.getiMaxNum());
                    this.startOrNot = true;
                    this.previousStartOrNot = true;
                } else {
                    int autoSnapshotMin = GlobalSnapSYNCInfo.getInstance().getMinAutoSnapNum();
                    int autoSnapshotInterval = GlobalSnapSYNCInfo.getInstance().getAutoSnapInterval();
                    if (autoSnapshotMin != -1) {
                        this.reserveNum = String.valueOf(autoSnapshotMin);
                    } else {
                        this.reserveNum = "";
                    }
                    if (autoSnapshotInterval != -1) {
                        if (autoSnapshotInterval >= 60) {
                            this.interval = String.valueOf(autoSnapshotInterval / 60);
                        } else {
                            this.interval = String.valueOf(autoSnapshotInterval);
                        }
                    } else {
                        this.interval = "";
                    }
                    this.startOrNot = false;
                    this.previousStartOrNot = false;
                }
                maxNum = fileSys.getiMaxNum();
            }
        }

        if (!this.startOrNot) {
            this.inputDisabled = true;
        } else {
            this.inputDisabled = false;
        }


    }

//    public boolean isDeleteAutoSnapRendered() {
//        return deleteAutoSnapRendered;
//    }
//
//    public void setDeleteAutoSnapRendered(boolean deleteAutoSnapRendered) {
//        this.deleteAutoSnapRendered = deleteAutoSnapRendered;
//    }
//
//    public boolean isDeleteAutoSnapOrNot() {
//        return deleteAutoSnapOrNot;
//    }
//
//    public void setDeleteAutoSnapOrNot(boolean deleteAutoSnapOrNot) {
//        this.deleteAutoSnapOrNot = deleteAutoSnapOrNot;
//    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStartOrNot() {
        return startOrNot;
    }

    public void setStartOrNot(boolean startOrNot) {
        this.startOrNot = startOrNot;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getReserveNum() {
        return reserveNum;
    }

    public void setReserveNum(String reserveNum) {
        this.reserveNum = reserveNum;
    }

    public boolean isInputDisabled() {
        return inputDisabled;
    }

    public void setInputDisabled(boolean inputDisabled) {
        this.inputDisabled = inputDisabled;
    }

    public void listen() {
        if (this.isStartOrNot()) {
            this.inputDisabled = false;
        } else {
            this.inputDisabled = true;
        }
    }

    public String save() {
        //  FileSysSnapSYNCInfo fileSys = new FileSysSnapSYNCInfo(path);
        // int isOpen = fileSys.getiIsOpen();
        // int maxNum = fileSys.getiMaxNum();
//
//            String fileSystem = jTextField3.getText();
//            String snapshotInterval = jTextField1.getText();  //快照间隔
//            String maxNum = jTextField2.getText();  //保留数量
        MSAResource res = new MSAResource();
        if (this.startOrNot) {
            String snapshotIntervalUnit;
            int exactInterval = GlobalSnapSYNCInfo.getInstance().getAutoSnapInterval();
            int minSnap = GlobalSnapSYNCInfo.getInstance().getMinAutoSnapNum();
            int maxSnap = GlobalSnapSYNCInfo.getInstance().getMaxAutoSnapNum();
            //验证自动快照时间间隔

            if (this.interval == null || this.interval.equalsIgnoreCase("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalCanNotEmpty"), res.get("intervalCanNotEmpty")));
                return null;
            }

            if (interval.matches("^[0-9]*[1-9][0-9]*$")) {
                int n = -1;
                if (exactInterval < 60) {
                    snapshotIntervalUnit = res.get("second");
                } else {
                    snapshotIntervalUnit = res.get("minute");
                    exactInterval = exactInterval / 60;
                }
                if (snapshotIntervalUnit.equals(res.get("minute"))) {
                    if (this.interval.length() > 3) {
                        // 时间间隔为1—999分钟。
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalLimit"), res.get("intervalLimit")));
                        return null;
                    } else {
                        n = Integer.parseInt(this.interval);
                        if (n < 1 || n > 999) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalLimit"), res.get("intervalLimit")));
                            return null;
                        }
                    }

                } else {
                    if (this.interval.length() > 5) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalLimit"), res.get("intervalLimit")));
                        return null;
                    } else {
                        n = Integer.parseInt(this.interval);
                        if (n < 1 || n > 59940) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalLimit"), res.get("intervalLimit")));
                            return null;
                        }
                    }
                }

            } else {
                //请在时间间隔栏输入有效数字。
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("inputValidNum"), res.get("inputValidNum")));
                return null;
            }
            int trueSnapshotInterval = 0;
            if (snapshotIntervalUnit.equals(res.get("minute"))) {
                trueSnapshotInterval = Integer.valueOf(this.interval) * 60;  //单位转化为秒
            }
            if (trueSnapshotInterval < exactInterval) {
                if (snapshotIntervalUnit.equals(res.get("minute"))) {
                    //时间间隔不能小于
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalCanNotLess") + exactInterval / 60 + res.get("minute") + "。", res.get("intervalCanNotLess") + exactInterval / 60 + res.get("minute") + "。"));
                    return null;
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("intervalCanNotLess") + exactInterval + res.get("second") + "。", res.get("intervalCanNotLess") + exactInterval + res.get("second") + "。"));
                    return null;
                }
            }

            //验证快照保留数量
            if (this.reserveNum == null || this.reserveNum.equalsIgnoreCase("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("reserveNumNotEmpty"), res.get("reserveNumNotEmpty")));
                return null;
            }
            if (reserveNum.matches("^[0-9]*[1-9][0-9]*$")) {
                try {
                    int n = Integer.parseInt(reserveNum);
//                    Debug.print("n=" + n);
                    if (n < 1) {
                        //请在时间间隔栏输入有效数字。
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("inputValidNum"), res.get("inputValidNum")));
                        return null;
                    }
                    if (Integer.valueOf(this.reserveNum) < minSnap || Integer.valueOf(this.reserveNum) > maxSnap) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("reserveNumIs") + minSnap + "-" + maxSnap + "。", res.get("reserveNumIs") + minSnap + "-" + maxSnap + "。"));
                        return null;
                    }
                } catch (Exception e) {
                    Debug.print("NumberFormatException: " + e.getMessage());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("inputValidSnapNum"), res.get("inputValidSnapNum")));
                    return null;
                }
//                if (reserveNum.length() < 11) {
//                    int n = Integer.parseInt(reserveNum);
////                    Debug.print("n=" + n);
//                    if (n < 1) {
//                        //请在时间间隔栏输入有效数字。
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("inputValidNum"), res.get("inputValidNum")));
//                        return null;
//                    }
//                    if (Integer.valueOf(this.reserveNum) < minSnap || Integer.valueOf(this.reserveNum) > maxSnap) {
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("reserveNumIs") + minSnap + "-" + maxSnap + "。", res.get("reserveNumIs") + minSnap + "-" + maxSnap + "。"));
//                        return null;
//                    }
//                } else {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("reserveNumIs") + minSnap + "-" + maxSnap + "。", res.get("reserveNumIs") + minSnap + "-" + maxSnap + "。"));
//                    return null;
//                }

            } else {
                //请在快照保留数量栏输入有效数字。
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("inputValidSnapNum"), res.get("inputValidSnapNum")));
                return null;
            }

            //验证时间间隔

            if (this.previousStartOrNot && Integer.valueOf(this.reserveNum) < maxNum) {
            } else {
                //发送启动或修改自动快照的命令
                boolean flag = InterfaceFactory.getSYNCInterfaceInstance().createTimingSnapshot(this.path, trueSnapshotInterval, Integer.parseInt(this.reserveNum));
                if (!flag) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("autoSnapConfigFailed"), res.get("autoSnapConfigFailed")));
                    return null;
                }
            }


        } else {
            //如果曾经开启过
            if (this.previousStartOrNot) {
                //判断是否删除自动快照
                if (this.existSnapOrNot) {
                    RequestContext.getCurrentInstance().execute("deleteAutoSnap.show()");
                    return null;
                }
                boolean flag = InterfaceFactory.getSYNCInterfaceInstance().cancelTimingSnapshot(this.path, false);
                if (!flag) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("autoSnapConfigFailed"), res.get("autoSnapConfigFailed")));
                    return null;
                }
            }

        }
        String param = "path=" + this.path + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "nas_snap_sync?faces-redirect=true&amp;" + param;
    }

    public String deleteAutoSnap() {
        MSAResource res = new MSAResource();
        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().cancelTimingSnapshot(this.path, true);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("autoSnapConfigFailed"), res.get("autoSnapConfigFailed")));
            return null;
        }
        String param = "path=" + this.path + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "nas_snap_sync?faces-redirect=true&amp;" + param;
    }

    public String goBack() {
        String param = "path=" + this.path + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        return "nas_snap_sync?faces-redirect=true&amp;" + param;
    }

    public String cancelDeleteSnap() {
        MSAResource res = new MSAResource();
        boolean flag = InterfaceFactory.getSYNCInterfaceInstance().cancelTimingSnapshot(this.path, false);
        if (!flag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("autoSnapConfigFailed"), res.get("autoSnapConfigFailed")));
            return null;
        }
        String param = "path=" + this.path + "&" + "module=" + this.module + "&" + "returnURL=" + this.returnURL;
        //RequestContext.getCurrentInstance().execute("window.location.href='nas_snap_sync.xhtml?faces-redirect=true&amp;path=" + this.path + "'");
        return "nas_snap_sync?faces-redirect=true&amp;" + param;
    }
}
