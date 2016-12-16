/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.backup.managedbean;

import com.marstor.msa.backup.model.DatabaseParameter;
import com.marstor.msa.backup.model.Device;
import com.marstor.msa.backup.model.DeviceDetail;
import com.marstor.msa.backup.model.DriveInfo;
import com.marstor.msa.backup.model.StorageDeviceBean;
import com.marstor.msa.backup.model.TapeInfo;
import com.marstor.msa.backup.util.Util;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "bdevice")
@ViewScoped
public class DeviceDetailBean implements Serializable {

    private String strDevice;
    private List<DeviceDetail> list = new ArrayList();

    public DeviceDetailBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        strDevice = request.getParameter("device");
        init();
    }

    public void setList(List<DeviceDetail> list) {
        this.list = list;
    }

    public List<DeviceDetail> getList() {
        return list;
    }

    private void init() {
        InputStream in = Util.getRemoteXMLSteram();
        if (null == in) {
            return;
        }

        DatabaseParameter dbBean = Util.getDBBean(in);
        QueryDatabase query = new QueryDatabase();
        List<StorageDeviceBean> listDeviceInfo = query.getStorageDeviceInfo(dbBean);
        for (StorageDeviceBean sdb : listDeviceInfo) {
            if (sdb.getStrName().equals(strDevice)) {
                List<DriveInfo> listDriveInfo = sdb.getListDriveInfo();
                List<TapeInfo> listTapeInfo = sdb.getListTapeInfo();

                DeviceDetail param0 = new DeviceDetail();
                param0.setName(setResource("DeviceName"));
                param0.setValue(sdb.getStrName());
                list.add(param0);

                DeviceDetail param1 = new DeviceDetail();
                param1.setName(setResource("DeviceType"));
                param1.setValue(sdb.getStrType());
                list.add(param1);

                DeviceDetail param2 = new DeviceDetail();
                param2.setName(setResource("Host"));
                param2.setValue(sdb.getStrHostName());
                list.add(param2);

                DeviceDetail param3 = new DeviceDetail();
                param3.setName(setResource("SlotNumber"));
                param3.setValue(String.valueOf(sdb.getiSlotNum()));
                list.add(param3);

                DeviceDetail param4 = new DeviceDetail();
                param4.setName(setResource("TapeNumber"));
                param4.setValue(String.valueOf(sdb.getListTapeInfo().size()));
                list.add(param4);

                DeviceDetail param5 = new DeviceDetail();
                param5.setName(setResource("DriverNumber"));
                param5.setValue(String.valueOf(sdb.getiDriveNum()));
                list.add(param5);

                for (DriveInfo di : listDriveInfo) {
                    DeviceDetail param6 = new DeviceDetail();
                    String strIsLock = "";
                    if (di.getiIsLocked() == 1) {
                        strIsLock = this.setResource("Yes");
                    } else {
                        strIsLock = this.setResource("No");
                    }
                    param6.setName(setResource("Driver"));
                    String s = this.setResource("DriverID") + ": " + di.getiDriveNumber() + ",\n"
                            + this.setResource("Lock") + ": " + strIsLock + ",\n";
                    s = s.substring(0, s.length() - 2);
                    s += "\n";
                    param6.setValue(s);
                    list.add(param6);
                }

                for (TapeInfo ti : listTapeInfo) {
                    DeviceDetail param7 = new DeviceDetail();
                    String strIsWriteProtect = "";
                    String strUsedCycle = "";
                    String strIsLocked = "";
                    String strBarCode = "";

                    if (ti.getiIsWriteProtect() == 1) {
                        strIsWriteProtect = this.setResource("Yes");
                    } else {
                        strIsWriteProtect = this.setResource("No");
                    }

                    if (ti.getiUsedCycle() == 1) {
                        strUsedCycle = this.setResource("Yes");
                    } else {
                        strUsedCycle = this.setResource("No");
                    }

                    if (ti.getiIsLocked() == 1) {
                        strIsLocked = this.setResource("Yes");
                    } else {
                        strIsLocked = this.setResource("No");
                    }

                    if (ti.getStrBarCode().equals("")) {
                        strBarCode = this.setResource("None");
                    } else {
                        strBarCode = ti.getStrBarCode();
                    }
                    
                    param7.setName(setResource("TapeDevice"));
                    String s = this.setResource("TapeLocation") + ": " + ti.getiSrcslot() + ",\n"
                            + this.setResource("UsedSize") + ": " + Util.convert(ti.getlCapacity() - ti.getlUsedSize()) + ",\n"
                            + this.setResource("Capacity") + ": " + Util.convert(ti.getlCapacity()) + ",\n"
                            + this.setResource("WriteProtect") + ": " + strIsWriteProtect + ",\n"
                            + this.setResource("UsedCycle") + ": " + strUsedCycle + ",\n"
                            + this.setResource("Lock") + ": " + strIsLocked + ",\n"
                            + this.setResource("Label") + ": " + ti.getStrLabel() + ",\n"
                            + this.setResource("Barcode") + ": " + strBarCode + ",\n";
                    s = s.substring(0, s.length() - 2);
                    s += "\n";
                    param7.setValue(s);
                    list.add(param7);
                }


            }
        }
    }

    private String setResource(String resource) {
        return java.util.ResourceBundle.getBundle("com/marstor/msa/backup/res/BA").getString(resource);
    }
}
