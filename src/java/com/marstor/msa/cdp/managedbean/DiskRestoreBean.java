/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import com.marstor.msa.cdp.bean.CdpDiskGroupInfo;
import com.marstor.msa.cdp.bean.CdpDiskInfo;
import com.marstor.msa.cdp.model.ClientDiskDetail;
import com.marstor.msa.cdp.model.ClientDiskInfo;
import com.marstor.msa.cdp.model.ClientInfo;
import com.marstor.msa.cdp.model.ClientMirroredDiskInfo;
import com.marstor.msa.cdp.model.ExtendInfo;
import com.marstor.msa.cdp.socket.ClientAPI;
import com.marstor.msa.cdp.util.CDPConstants;
import com.marstor.msa.cdp.util.Debug;
import com.marstor.msa.cdp.util.Utility;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.MyConstants;
import com.marstor.msa.util.InterfaceFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@ManagedBean(name = "diskRestoreBean")
@ViewScoped
public class DiskRestoreBean {

    private ClientInfo client = new ClientInfo();
    private List<ClientMirroredDiskInfo> mirrorDisks = new ArrayList();
    private ClientMirroredDiskInfo selected;
    private MSAResource res = new MSAResource();
    private MSAGlobalResource global = new MSAGlobalResource();
    private String basename = "cdp.d_restore";
    private String ip = "192.168.1.87";
    private int port = 1100;
    public long diskNum = -1;
    public int pcount;
    public String size;
    public String cdpType;
    private String strSize;
    private String strCDPType;
    public List<CdpDiskGroupInfo> cdpGroups;
    public ArrayList<CdpDiskInfo> cdpDisks;
    private Map<String, ExtendInfo> extendInfos = new HashMap();
    public int mirrorNum = 10000;

    public DiskRestoreBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        ip = request.getParameter("ip");
        if (request.getParameter("port") != null) {
            port = Integer.parseInt(request.getParameter("port"));
        }
        if (request.getParameter("dNum") != null) {
            diskNum = Long.parseLong(request.getParameter("dNum"));
        }

        client.setIp(ip);
        client.setPort(port);
        if (request.getParameter("pCount") != null && !request.getParameter("pCount").equals("null")) {
            pcount = Integer.parseInt(request.getParameter("pCount"));
        }

        strSize = request.getParameter("size");
        if (request.getParameter("size") != null && !strSize.equals("null")) {
            size = Utility.getSizeString(Long.parseLong(request.getParameter("size"))) + " GB ";
        }
        strCDPType = request.getParameter("cdpType");
        if (request.getParameter("cdpType") != null && !strCDPType.equals("null")) {
            cdpType = Utility.getCDPTypeString(Integer.parseInt(request.getParameter("cdpType")));
        }
        if (request.getParameter("mirror") != null && !request.getParameter("mirror").equals("null")) {
            mirrorNum = Integer.parseInt(request.getParameter("mirror"));
        }
        initMirrorDisks();
    }

    public final void initMirrorDisks() {
        ClientMirroredDiskInfo[] DiskInfos = ClientAPI.GetMirrorDisksInfo(this.client);
        if (DiskInfos == null) {
            ClientAPI.GetErrorCode();
            return;
        }
        for (int i = 0; i < DiskInfos.length; i++) {
            ClientDiskDetail DiskDetail = ClientAPI.GetSourceDiskDetail(DiskInfos[i].DiskNumber, this.client);
            if (DiskDetail == null) {
                ClientAPI.GetErrorCode();
                return;
            }
            if (!DiskDetail.ProductID.equalsIgnoreCase(MyConstants.Product_ID)
                    || DiskDetail.CDPType != 0) {
                DiskInfos[i] = null;
            }
        }

        getCDPData();

        for (int i = 0; i < DiskInfos.length; i++) {
            ClientMirroredDiskInfo DiskInfo = DiskInfos[i];

            if (DiskInfos[i] != null && !DiskInfo.Used
                    && DiskInfo.DiskNumber != this.diskNum) {

                mirrorDisks.add(DiskInfo);

                //  String serialNumber = doubleReverse(DiskInfo.serialNumber);
                String serialNumber = DiskInfo.serialNumber;
                for (CdpDiskInfo disk : this.cdpDisks) {
                    if (disk.getLuInfoBean() == null) {
                        continue;
                    }
                    if (!serialNumber.equals(disk.getLuInfoBean().getSerialNum())) {
                        continue;
                    }
                    for (CdpDiskGroupInfo group : this.cdpGroups) {
                        if (!group.getDiskGroupGuid().equals(disk.getDiskGroupGuid())) {
                            continue;
                        } else {
                            ExtendInfo e = new ExtendInfo();
                            e.setDg(group.getDiskGroupName());
                            e.setDname(disk.getDiskName());
                            extendInfos.put(DiskInfo.serialNumber, e);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void getCDPData() {
        MsaCDPInterface cdp = InterfaceFactory.getCDPInterfaceInstance();
        this.cdpGroups = cdp.getDiskGroupInfos();
        if (this.cdpGroups == null) {
            return;
        }
        this.cdpDisks = new ArrayList();
        for (CdpDiskGroupInfo group : this.cdpGroups) {
            if (group.getDiskGroupGuid().equals("")) {
                continue;
            }
            CdpDiskInfo[] infos = cdp.getDiskInfos(group.getDiskGroupGuid(), group.getDiskGroupPath());
            if (infos != null) {
                this.cdpDisks.addAll(Arrays.asList(infos));
            }
        }
    }

    public void setting() {
        int f = this.fast ? 1 : 0;
        if (selected.DiskSize > Long.parseLong(this.strSize)) {
            RequestContext.getCurrentInstance().execute("restore.show()");
            return;
        }
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                (int) diskNum, 0, selected.DiskNumber, 0, this.client);

        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.mirrorNum = selected.DiskNumber;
    }

    public void setting0() {
        int f = 0;
        if (selected.DiskSize > Long.parseLong(this.strSize)) {
            RequestContext.getCurrentInstance().execute("restore0.show()");
            return;
        }
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                (int) diskNum, 0, selected.DiskNumber, 0, this.client);

        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.mirrorNum = selected.DiskNumber;
    }

    public void setting1() {
        int f = 1;
        if (selected.DiskSize > Long.parseLong(this.strSize)) {
            RequestContext.getCurrentInstance().execute("restore1.show()");
            return;
        }
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                (int) diskNum, 0, selected.DiskNumber, 0, this.client);

        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.mirrorNum = selected.DiskNumber;
    }

    public boolean getStopRendered(int num) {
        if (num == this.mirrorNum) {
            return true;
        }
        return false;
    }

    public String process() {
        String param = "ip=" + ip + "&port=" + port
                + "&dNum=" + this.diskNum
                + "&pCount=" + this.pcount
                + "&size=" + this.strSize
                + "&cdpType=" + this.strCDPType
                + "&mirror=" + this.mirrorNum;
        Debug.print("DiskRestoreBean --> DiskRestoreProgressBean:" + param);
        return "d_restore_pro?faces-redirect=true&amp;" + param;
    }

    public String getHeader3(ClientMirroredDiskInfo m) {
        if (extendInfos.get(m.serialNumber) == null) {
            return "";
        }
        ExtendInfo e = extendInfos.get(m.serialNumber);
        return e.getDg();
    }

    public String getHeader4(ClientMirroredDiskInfo m) {
        if (extendInfos.get(m.serialNumber) == null) {
            return "";
        }
        ExtendInfo e = extendInfos.get(m.serialNumber);
        return e.getDname();
    }

    public void cancelRestore() {
        boolean bRet = ClientAPI.CancelRestore(CDPConstants.CDP_RESTORE_TYPE_DISK, (int) this.diskNum, 0, this.client);
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail0"), global.get("error_mark")));
            return;
        }
        this.mirrorNum = 10000;
    }

    public String cancel() {
        RequestContext.getCurrentInstance().execute("restore.hide()");
        return null;
    }

    public String cancel0() {
        RequestContext.getCurrentInstance().execute("restore0.hide()");
        return null;
    }

    public String cancel1() {
        RequestContext.getCurrentInstance().execute("restore1.hide()");
        return null;
    }

    public void restore() {
        int f = this.fast ? 1 : 0;
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                (int) diskNum, 0, selected.DiskNumber, 0, this.client);
        RequestContext.getCurrentInstance().execute("restore.hide()");
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.mirrorNum = selected.DiskNumber;
    }

    public void restore0() {
        int f = 0;
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                (int) diskNum, 0, selected.DiskNumber, 0, this.client);
        RequestContext.getCurrentInstance().execute("restore0.hide()");
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.mirrorNum = selected.DiskNumber;
    }

    public void restore1() {
        int f = 1;
        boolean bRet = ClientAPI.RestoreData(CDPConstants.CDP_RESTORE_TYPE_DISK, f,
                (int) diskNum, 0, selected.DiskNumber, 0, this.client);
        RequestContext.getCurrentInstance().execute("restore1.hide()");
        if (!bRet) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "fail"), global.get("error_mark")));
            return;
        }
        this.mirrorNum = selected.DiskNumber;
    }

    public List<ClientMirroredDiskInfo> getMirrorDisks() {
        return mirrorDisks;
    }

    public void setMirrorDisks(List<ClientMirroredDiskInfo> mirrorDisks) {
        this.mirrorDisks = mirrorDisks;
    }

    public ClientMirroredDiskInfo getSelected() {
        return selected;
    }

    public void setSelected(ClientMirroredDiskInfo selected) {
        this.selected = selected;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(long diskNum) {
        this.diskNum = diskNum;
    }

    public int getPcount() {
        return pcount;
    }

    public void setPcount(int pcount) {
        this.pcount = pcount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCdpType() {
        if (this.mirrorNum == 10000) {
            return "未设置";
        } else {
            return "磁盘恢复";
        }
    }

    public void setCdpType(String cdpType) {
        this.cdpType = cdpType;
    }
    boolean fast = false;

    public boolean isFast() {
        return fast;
    }

    public void setFast(boolean fast) {
        this.fast = fast;
    }

    public void change() {
    }

    public int getMirrorNum() {
        return mirrorNum;
    }

    public void setMirrorNum(int mirrorNum) {
        this.mirrorNum = mirrorNum;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);//定义输入对象
        String input = "";
        boolean right = false;
        do {
            input = sc.nextLine();//输入一个字符串
            right = input.matches("[0-9]*");//正则表达式
            if (!right)//不符合
            {
                System.out.print("输入有错,请重输: ");
            }
        } while (!right);
    }

}
