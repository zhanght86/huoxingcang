/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.bean.CdpLogRecord;
import com.marstor.msa.cdp.bean.CdpRollbackTaskInfo;
import com.marstor.msa.cdp.model.GlobalProtectStatus;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.bean.ViewInformation;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
@ManagedBean(name = "recordBean")
@ViewScoped
public class RecordBean {

    private String guid;
    private String path;
    private int rHandle;
    private long sID;
    private int count = 50;
    private int reverse = 0;
    private boolean forward = true;
    private CdpLogRecord[] recordInfos;
    private CdpLogRecord selected = new CdpLogRecord();
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
    private String basename = "cdp.record";
    private Date date = new Date();
    boolean rolling = false;
    CdpRollbackTaskInfo rb;
    List<CdpDiskInfo> disks;
    private CdpDiskGroupInfo group;
    private GlobalProtectStatus globalStatus;
    private String dg;
    private List<String> groupNames;

    public RecordBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        guid = request.getParameter("guid");
        path = request.getParameter("path");
        sID = Long.parseLong(request.getParameter("sID"));
        this.groupNames = Arrays.asList(request.getParameterValues("groupName"));
        initGroup();
        initDisks();
        getQueryRecordHandle(this.sID);
        this.recordInfos = getRecords(this.rHandle, count, 1, 0);
        if (recordInfos != null) {
            Collections.sort(Arrays.asList(recordInfos));
        }
        globalStatus = new GlobalProtectStatus(path, 2, group, dg, groupNames);
    }

    public GlobalProtectStatus getGlobalStatus() {
        return globalStatus;
    }

    public void setGlobalStatus(GlobalProtectStatus globalStatus) {
        this.globalStatus = globalStatus;
    }

    private void initGroup() {
        group = cdp.getDiskGroupInfo(guid);
        this.dg = group.getDiskGroupName();
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public void preRollback() {
        ViewInformation[] lun = null;
        if (disks != null && disks.size() > 0) {
            SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
            if (disks.get(0).getLuInfoBean() != null) {
                lun = scsi.getLUNView(disks.get(0).getLuInfoBean().getlUName());
            }
        }
        if (this.group.isCDPStarted()) {
            if (lun != null && lun.length > 0) {
                RequestContext.getCurrentInstance().execute("SdisLUNDel.show()");
            } else {
                RequestContext.getCurrentInstance().execute("SdeleteDGBox.show()");
            }
        } else {
            if (lun != null && lun.length > 0) {
                RequestContext.getCurrentInstance().execute("disLUNDel.show()");
            } else {
                RequestContext.getCurrentInstance().execute("deleteDGBox.show()");
            }
        }
    }

    public String rollback() {
        if (disks == null) {
            return null;
        }
        SCSIInterface scsi = InterfaceFactory.getSCSIInterfaceInstance();
        if (this.group.isCDPStarted()) {
            cdp.stopCDP(this.group.getDiskGroupGuid(), this.group.getDiskGroupPath(),
                    this.group.getProtectType());
        }
//        if (this.group.getiMount() == CDPConstants.TRUE) {
//            ViewInformation[] lun = null;
//            if (disks.size() > 0) {
//                if (disks.get(0).getLuInfoBean() != null) {
//                    lun = scsi.getLUNView(disks.get(0).getLuInfoBean().getlUName());
//                }
//            }
//            if (lun != null && lun.length > 0) {
//                for (int i = 0; i < lun.length; i++) {
//                    for (int j = 0; j < this.group.getDiskCount(); j++) {
//                        scsi.removeView(disks.get(j).getLuInfoBean().getlUName(), lun[i].entry);
//                    }
//                }
//            }
//        }
//        ViewInformation[] views = scsi.getLUNView(disks.get(0).getDiskGuid());
        int len = disks.size();
        String[] lus;
        lus = new String[len];
        for (int i = 0; i < len; i++) {
            lus[i] = disks.get(i).getLuInfoBean().getlUName();
        }
//        if (views != null && views.length > 0) {
        cdp.offlineLU(this.group.getDiskGroupName(), lus);
//        }
        boolean success = cdp.cdpRollback(path, guid, 0, selected.getLogTime());
        this.rolling = true;
        if (!success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return null;
        }

        String param = "guid=" + guid + "&path=" + path
                + "&record=1" + "&level=2";
        for(String name : this.groupNames){
            param += "&groupName=" + name;
        }
        return "snapshot_r?faces-redirect=true&amp;" + param;
    }

    public final void getRollbackInfo() {
        rb = cdp.getRollbackInfo(guid, path);
    }

    public String getDiskName(long diskID) {
        String name = "";

        for (CdpDiskInfo disk : disks) {
            if (disk.getDiskID() == diskID) {
                name = disk.getDiskName();
                break;
            }
        }
        return name;
    }
    private Integer progress;

    public Integer getProgress() {
        if (progress == null) {
            progress = 0;
        } else {
            getRollbackInfo();
            if (rb == null) {
                return progress;
            }
            progress = (int) ((double) rb.getCurrentRollbackSize() * 100 / (double) rb.getTotalRollbackSize());

            if (progress > 100) {
                progress = 100;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "success"), global.get("error_mark")));
            }
        }

        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void onComplete() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Progress Completed", "Progress Completed"));
    }

    public void cancel() {
        progress = null;

        rolling = false;
    }

    public void seekRev() {
        if (this.forward) {
            this.reverse = 1;
        } else {
            this.reverse = 0;
        }
        this.forward = false;
        CdpLogRecord[] rds = getRecords(this.rHandle, count, 0, this.recordInfos.length);
        if (rds == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "none"), global.get("error_mark")));
            return;
        }
        if (rds.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "first"), global.get("error_mark")));
            return;
        }
        this.recordInfos = rds;
        if (recordInfos != null) {
            Collections.sort(Arrays.asList(recordInfos));
        }
    }

    public void seekNext() {
        if (!this.forward) {
            this.reverse = 1;
        } else {
            this.reverse = 0;
        }
        this.forward = true;
        CdpLogRecord[] rds = getRecords(this.rHandle, count, 1, this.recordInfos.length);
        if (rds == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "none"), global.get("error_mark")));
            return;
        }
        if (rds.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "last"), global.get("error_mark")));
            return;
        }
        this.recordInfos = rds;
        if (recordInfos != null) {
            Collections.sort(Arrays.asList(recordInfos));
        }
    }

    public void changeCount() {
        closeQueryHandle();
        getQueryRecordHandle(this.sID);
        CdpLogRecord[] rds = getRecords(this.rHandle, count, 1, this.recordInfos.length);
        if (rds == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "none"), global.get("error_mark")));
            return;
        }
        if (rds.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "last"), global.get("error_mark")));
            return;
        }
        this.recordInfos = rds;
        if (recordInfos != null) {
            Collections.sort(Arrays.asList(recordInfos));
        }
    }

    private void getQueryRecordHandle(long snapShotID) {
        cdp = InterfaceFactory.getCDPInterfaceInstance();
        this.rHandle = cdp.getQueryLogRecordHandle(guid, snapShotID);
    }

    private CdpLogRecord[] getRecords(int handle, int count, int forwardislast, int currentCount) {
        cdp = InterfaceFactory.getCDPInterfaceInstance();
        return cdp.queryLogRecordInfos(path, handle, count, forwardislast, reverse, currentCount);
    }

    private void closeQueryHandle() {
        cdp = InterfaceFactory.getCDPInterfaceInstance();
        cdp.closeQueryLogRecordHandle(this.rHandle);
    }

    public CdpLogRecord[] getRecordInfos() {
        return recordInfos;
    }

    public void setRecordInfos(CdpLogRecord[] recordInfos) {
        this.recordInfos = recordInfos;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CdpLogRecord getSelected() {
        return selected;
    }

    public void setSelected(CdpLogRecord selected) {
        this.selected = selected;
    }

    public boolean isRolling() {
        return rolling;
    }

    public void setRolling(boolean rolling) {
        this.rolling = rolling;
    }

    public String record() {
        String param = "sID=" + this.sID;
        return "record?faces-redirect=true&amp;" + param;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private void initDisks() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        CdpDiskInfo[] ds = cdp.getDiskInfos(guid, path);
        disks = Arrays.asList(ds);
    }

    public String back() {
        String param = "guid=" + this.guid + "&path=" + this.path + "&level=2";
        for(String name : this.groupNames){
            param += "&groupName=" + name;
        }
        return "snapshot_r?faces-redirect=true&amp;" + param;
    }
}
