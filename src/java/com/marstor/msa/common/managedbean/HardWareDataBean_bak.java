/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.model.NetCard;
import com.marstor.msa.bean.CPUInfo;
import com.marstor.msa.common.bean.DiskModel;
import com.marstor.msa.common.bean.DiskStatistics;
import com.marstor.msa.common.model.DiskStatus;
import com.marstor.msa.common.bean.MemStatistics;
import com.marstor.msa.common.bean.NicStatistics;
import com.marstor.msa.common.bean.PhysicalDiskInformation;
import com.marstor.msa.common.bean.SystemInformation;
import com.marstor.msa.common.bean.VolumeStatistics;
import com.marstor.msa.common.model.NetCard_bak;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.chart.MeterGaugeChartModel;

/**
 *
 * @author Administrator
 */
public class HardWareDataBean_bak implements Serializable {

    private MeterGaugeChartModel cpuModel = new MeterGaugeChartModel();
    private MeterGaugeChartModel tmpModel = new MeterGaugeChartModel();
    private MeterGaugeChartModel memModel = new MeterGaugeChartModel();
    private MeterGaugeChartModel readDiskModel = new MeterGaugeChartModel();
    private MeterGaugeChartModel writeDiskModel = new MeterGaugeChartModel();
    // private CartesianChartModel networkModel;
    private ArrayList<VolumeStatistics> volumns = new ArrayList<VolumeStatistics>();
    private ArrayList<NetCard_bak> netCards = new ArrayList<NetCard_bak>();
    private HashMap<String, NicStatistics> cardMap = new HashMap<String, NicStatistics>();
//    private HashMap<String, Integer> diskPositionToTmp = new HashMap<String, Integer>();
//    private HashMap<String, String> diskNameToPosition = new HashMap<String, String>();
//    private HashMap<String, Integer> diskPositionToState = new HashMap<String, Integer>();
    private HashMap<Integer, DiskStatistics> diskStatistics = new HashMap<Integer, DiskStatistics>();
    private int netCardNum = 0;
    private CommonInterface commonInterface =InterfaceFactory.getCommonInterfaceInstance();
    private DiskStatus diskStatus = new DiskStatus();
    private boolean isStopUpdateSys = false, isStopUpdateNet = true, isStopUpdateDisk = true;
    private boolean firstLoading = true;
    private boolean isDisk6, isDisk8, isDisk16;
    private int memoryTotal;
    private boolean isDisplayDiskPicture;

    public HardWareDataBean_bak() {
        System.out.println("init model");
        initCPUModel();
        initTemModel();
        initMemModel();
        initReadDiskModel();
        initWriteDiskModel();
        this.initNetWorkCard();
        this.initDiskStatus();

    }

    //
    //    public void count() {
    //        this.cpuModel.setValue(5);
    //    }
    //    }
    
    public boolean isIsDisplayDiskPicture() {
        return isDisplayDiskPicture;
    }

    public void setIsDisplayDiskPicture(boolean isDisplayDiskPicture) {
        this.isDisplayDiskPicture = isDisplayDiskPicture;
    }
    
    public boolean isIsDisk6() {
        return isDisk6;
    }

    public void setIsDisk6(boolean isDisk6) {
        this.isDisk6 = isDisk6;
    }

    public boolean isIsDisk8() {
        return isDisk8;
    }

    public void setIsDisk8(boolean isDisk8) {
        this.isDisk8 = isDisk8;
    }

    public boolean isIsDisk16() {
        return isDisk16;
    }

    public void setIsDisk16(boolean isDisk16) {
        this.isDisk16 = isDisk16;
    }

    public boolean isIsStopUpdateSys() {
        return isStopUpdateSys;
    }

    public void setIsStopUpdateSys(boolean isStopUpdateSys) {
        this.isStopUpdateSys = isStopUpdateSys;
    }

    public boolean isIsStopUpdateNet() {
        return isStopUpdateNet;
    }

    public void setIsStopUpdateNet(boolean isStopUpdateNet) {
        this.isStopUpdateNet = isStopUpdateNet;
    }

    public boolean isIsStopUpdateDisk() {
        return isStopUpdateDisk;
    }

    public void setIsStopUpdateDisk(boolean isStopUpdateDisk) {
        this.isStopUpdateDisk = isStopUpdateDisk;
    }

    public DiskStatus getDiskStatus() {
        return diskStatus;
    }

    public void setDiskStatus(DiskStatus diskStatus) {
        this.diskStatus = diskStatus;
    }

    public int getNetCardNum() {
        return netCardNum;
    }

    public void setNetCardNum(int netCardNum) {
        this.netCardNum = netCardNum;
    }

    public MeterGaugeChartModel getReadDiskModel() {
        return readDiskModel;
    }

    public void setReadDiskModel(MeterGaugeChartModel readDiskModel) {
        this.readDiskModel = readDiskModel;
    }

    public MeterGaugeChartModel getWriteDiskModel() {
        return writeDiskModel;
    }

    public void setWriteDiskModel(MeterGaugeChartModel writeDiskModel) {
        this.writeDiskModel = writeDiskModel;
    }

    public MeterGaugeChartModel getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(MeterGaugeChartModel cpuModel) {
        this.cpuModel = cpuModel;
    }

    public MeterGaugeChartModel getTmpModel() {
        return tmpModel;
    }

    public void setTmpModel(MeterGaugeChartModel tmpModel) {
        this.tmpModel = tmpModel;
    }

    public MeterGaugeChartModel getMemModel() {
        return memModel;
    }

    public void setMemModel(MeterGaugeChartModel memModel) {
        this.memModel = memModel;
    }

    public ArrayList<NetCard_bak> getNetCards() {
        return netCards;
    }

    public void setNetCards(ArrayList<NetCard_bak> netCards) {
        this.netCards = netCards;
    }

    private void initCPUModel() {
        System.out.println("init cpu model:");
        ArrayList<CPUInfo> cpus = commonInterface.getCPUStatistics();
        if (cpus == null) {
            System.out.println("Get cpu failed");
            return;
        }
        if (cpus.size() > 0) {
            CPUInfo cpu = cpus.get(0);
            List<Number> intervals = new ArrayList<Number>() {
                {
                    add(60);
                    add(80);
                    add(100);
                }
            };
            double percent = cpu.loadavg_1m / 256.0 * 100;
            if (percent > 100) {
                percent = 100;
            }
            cpuModel = new MeterGaugeChartModel(percent, intervals);
            System.out.println("cpu percent " + percent);
        }
    }

    private void initTemModel() {
        System.out.println("init tem model:");
        ArrayList<CPUInfo> cpus = this.commonInterface.getCPUStatistics();
        if (cpus == null) {
            System.out.println("Get cpu failed");
            return;
        }
        if (cpus.size() > 0) {
            CPUInfo cpu = cpus.get(0);
            List<Number> intervals = new ArrayList<Number>() {
                {
                    add(60);
                    add(80);
                    add(120);
                }
            };
            int temp = cpu.temp;
            if (temp > 120) {
                temp = 120;
            }
            tmpModel = new MeterGaugeChartModel(temp, intervals);
            System.out.println("cpu temp " + cpu.temp);
        }

    }

    private void initMemModel() {
        System.out.println("init mem model:");
        MemStatistics mem = this.commonInterface.getMemStatistics();
        SystemInformation sysInfo = this.commonInterface.getSystemInfo();
        if (mem == null || sysInfo == null || mem.getFree() == null || sysInfo.memory == null) {
            System.out.println("Get mem failed");
            return;
        }
        Double free = Math.ceil(Double.valueOf(mem.getFree()));//mem.getFree()返回的空闲内存的单位是kB
        String array[] = sysInfo.memory.trim().split(" ");
        if (array.length < 1) {
            System.out.println("Get mem failed");
            return;
        }
        String memoryStr = array[0];
        // int total;//单位G
//        if (Double.parseDouble(memoryStr) < 1000) {
//            total = 1024;
//        } else {
//            total = (int) Double.parseDouble(memoryStr) / 1000 * 1024;
//
//        }
        this.memoryTotal = (int) Math.ceil(Double.parseDouble(memoryStr) / 1024);
        List<Number> intervals = new ArrayList<Number>();
        intervals.add(this.memoryTotal * 3 / 4);
        intervals.add(this.memoryTotal * 7 / 8);
        intervals.add(this.memoryTotal);
        // 界面上显示的单位是GB
        double memFree = free / 1024 / 1024;
        double usedFree = this.memoryTotal - memFree;
        if (usedFree > this.memoryTotal) {
            usedFree = this.memoryTotal;
        }
        
        memModel = new MeterGaugeChartModel(usedFree, intervals);
        System.out.println("mem free " + memFree);
        System.out.println("used free " + memFree);
    }

    private void initReadDiskModel() {
        System.out.println("init read disk model:");
        ArrayList<VolumeStatistics> volumns = this.commonInterface.getDiskStatistics();
        if (volumns == null) {
            System.out.println("Get volumn failed");
            return;
        }
        double sum = 0.0;
        int max = 0;
        //获取所有磁盘的读数据速率之和
        for (VolumeStatistics volume : volumns) {
            if (volume.name.equals("msapool") || volume.name.equals("syspool")) {
                continue;
            }
            ArrayList<DiskStatistics> disks = volume.getDisks();
            if (disks != null) {
                for (DiskStatistics disk : disks) {
                    sum = sum + disk.kilobytes_read_per_second;
                }
            }
        }
        //磁盘读写速率如果小于1G，则最大值为1G；如果大于1G，小于2G，则最大值为2G
        if (sum / 1024 < 1024) {
            max = 1024;
        } else {
            max = ((int) Math.ceil(sum / 1024 / 1024)) * 1024;
        }
        List<Number> intervals = new ArrayList<Number>();
        intervals.add(max * 0.8);
        intervals.add(max * 0.9);
        intervals.add(max);
        double readSpeed = sum / 1024;
        if (readSpeed > max) {
            readSpeed = max;
        }
        readDiskModel = new MeterGaugeChartModel(readSpeed, intervals);
        System.out.println("read disk : " + readSpeed);
    }

    private void initWriteDiskModel() {
        System.out.println("init write disk model:");
        ArrayList<VolumeStatistics> volumns = this.commonInterface.getDiskStatistics();
        if (volumns == null) {
            System.out.println("Get volumn failed");
            return;
        }
        double sum = 0.0;
        int max = 0;
        //获取所有磁盘的写数据速率之和
        for (VolumeStatistics volume : volumns) {
            if (volume.name.equals("msapool") || volume.name.equals("syspool")) {
                continue;
            }
            ArrayList<DiskStatistics> disks = volume.getDisks();
            if (disks != null) {
                for (DiskStatistics disk : disks) {
                    sum = sum + disk.kilobytes_write_per_second;
                }
            }
        }
        if (sum / 1024 < 1024) {
            max = 1024;
        } else {
            max = ((int) Math.ceil(sum / 1024 / 1024)) * 1024;
        }
        List<Number> intervals = new ArrayList<Number>();
        intervals.add(max * 0.8);
        intervals.add(max * 0.9);
        intervals.add(max);
        double writeSpeed = sum / 1024;
        if (writeSpeed > max) {
            writeSpeed = max;
        }
        writeDiskModel = new MeterGaugeChartModel(writeSpeed, intervals);
        System.out.println("write disk : " + writeSpeed);
    }

    private void initNetWorkCard() {
        System.out.println("init network card:");
        this.netCardNum = 0;
        ArrayList<NicStatistics> nics = this.commonInterface.getNicStatistics();
        if (nics == null) {
            System.out.println("Get network failed");
            return;
        }
        for (NicStatistics nic : nics) {
            NetCard_bak card = new NetCard_bak(nic);
            this.netCards.add(card);
            this.cardMap.put(nic.name, nic);
            this.netCardNum++;
        }
        this.netCardNum = this.netCardNum - 1;
    }

    private void initDiskStatus() {
        int model[] =  this.commonInterface.getDiksModel();
        if(model.length>2) {
            this.isDisplayDiskPicture = model[2]==1 ? true : false;
        }else  {
             System.out.println("get disk model failed:");
             return;
        }
        if(!this.isDisplayDiskPicture) {
            return;
        }
        ArrayList<DiskStatistics> disks = this.commonInterface.getPhysDiskStatistics();
        System.out.println("init disk status:");
        for (DiskStatistics disk : disks) {
            System.out.println("disk card model:" + disk.cardModel);
            System.out.println("disk name: " + disk.getDiskName());
            System.out.println("disk position: " + disk.position);
            System.out.println("disk state: " + disk.state);
            System.out.println("disk temp: " + disk.temperature);
            if (disk.cardModel != 2) { //如果磁盘位于机箱内部
                if (!disk.position.equals("")) {
                    this.diskStatistics.put(Integer.parseInt(disk.position), disk);
                }

            }
        }
        this.diskStatus = new DiskStatus(diskStatistics);

    }

    public void updateSysModel() {
        this.updateCpuModel();
        this.updateTmpModel();
        this.updateMemModel();
        this.updateReadDiskModel();
        this.updateWriteDiskModel();
    }

    public void updateCpuModel() {
        System.out.println("update cpu model:");
        ArrayList<CPUInfo> cpus = commonInterface.getCPUStatistics();
        if (cpus == null) {
            System.out.println("Get cpu failed");
            return;
        }
        if (cpus.size() > 0) {
            CPUInfo cpu = cpus.get(0);
//            double percent = cpu.loadavg_1m / 256.0 * 100;
//            if (percent > 100) {
//                percent = 100;
//            }
            double percent = commonInterface.getCPUPercent();
            cpuModel.setValue(percent);
            System.out.println("cpu percent " + percent);
        }
//        int random1 = (int) (Math.random() * 100);
//        cpuModel.setValue(random1);
    }

    public void updateTmpModel() {
        System.out.println("update tmp model:");
        ArrayList<CPUInfo> cpus = commonInterface.getCPUStatistics();
        if (cpus == null) {
            System.out.println("Get cpu failed");
            return;
        }
        if (cpus.size() > 0) {
            CPUInfo cpu = cpus.get(0);
//            List<Number> intervals = new ArrayList<Number>() {
//                {
//                    add(60);
//                    add(80);
//                    add(120);
//                }
//            };
            int temp = cpu.temp;
            if (temp > 120) {
                temp = 120;
            }
            tmpModel.setValue(temp);
            System.out.println("cpu temp " + temp);
            // tmpModel = new MeterGaugeChartModel(cpu.temp, intervals);
        }
//        int random1 = (int) (Math.random() * 100);
//        cpuModel.setValue(random1);
    }

    public void updateMemModel() {
        System.out.println("update mem model:");
        MemStatistics mem = this.commonInterface.getMemStatistics();
        SystemInformation sysInfo = this.commonInterface.getSystemInfo();
        if (mem == null || sysInfo == null || mem.getFree() == null || sysInfo.memory
                == null) {
            System.out.println("Get mem failed");
            return;
        }
        Double free = Math.ceil(Double.valueOf(mem.getFree()));
        // float free = Float.valueOf(mem.getFree());
//        String array[] = sysInfo.memory.trim().split(" ");
//        if (array.length < 1) {
//            System.out.println("Get mem failed");
//            return;
//        }
//        float total = Float.valueOf(array[0]) * 1024;
        double memFree = free / 1024 / 1024;
        
        double usedFree = this.memoryTotal - memFree;
        if (usedFree > this.memoryTotal) {
            usedFree = this.memoryTotal;
        }
        this.memModel.setValue(usedFree);
        System.out.println("mem percent " + memFree);
        System.out.println("userd percent " + usedFree);
    }

    public void updateReadDiskModel() {
        System.out.println("update read disk model:");
        ArrayList<VolumeStatistics> volumns = this.commonInterface.getDiskStatistics();
        if (volumns == null) {
            System.out.println("Get volumn failed");
            return;
        }
        double sum = 0.0;
        int max = 0;
        //int num = 0;
        for (VolumeStatistics volume : volumns) {
            ArrayList<DiskStatistics> disks = volume.getDisks();
            if (disks != null) {
                for (DiskStatistics disk : disks) {
                    sum = sum + disk.kilobytes_read_per_second;
                    // num = num + 20;
                }
            }
        }
        if (sum / 1024 < 1024) {
            max = 1024;
        } else {
            max = ((int) Math.ceil(sum / 1024 / 1024)) * 1024;
        }
        List<Number> intervals = new ArrayList<Number>();
        intervals.add(max * 0.8);
        intervals.add(max * 0.9);
        intervals.add(max);
        this.readDiskModel.setIntervals(intervals);
        double readSpeed = sum / 1024;
        if (readSpeed > max) {
            readSpeed = max;
        }
        this.readDiskModel.setValue(readSpeed);
        System.out.println("read disk : " + readSpeed);
    }

    public void updateWriteDiskModel() {
        System.out.println("update write disk model:");
        ArrayList<VolumeStatistics> volumns = this.commonInterface.getDiskStatistics();
        if (volumns == null) {
            System.out.println("Get volumn failed");
            return;
        }
        double sum = 0.0;
        int max = 0;
        for (VolumeStatistics volume : volumns) {

            if (volume == null || volume.name.equals("syspool") || volume.name.equals("msapool")) {
                continue;
            }
            ArrayList<DiskStatistics> disks = volume.getDisks();
            if (disks != null) {
                for (DiskStatistics disk : disks) {
                    sum = sum + disk.kilobytes_write_per_second;
                }
            }
        }
        if (sum / 1024 < 1024) {
            max = 1024;
        } else {
            max = ((int) Math.ceil(sum / 1024 / 1024)) * 1024;
        }
        List<Number> intervals = new ArrayList<Number>();
        intervals.add(max * 0.8);
        intervals.add(max * 0.9);
        intervals.add(max);
        this.writeDiskModel.setIntervals(intervals);
        double writeSpeed = sum / 1024;
        if (writeSpeed > max) {
            writeSpeed = max;
        }
        this.writeDiskModel.setValue(writeSpeed);
        System.out.println("write disk : " + writeSpeed);
    }

    public void updateNetWorkCard() {
        System.out.println("update network card:");
        ArrayList<NicStatistics> nics = this.commonInterface.getNicStatistics();
        if (nics == null) {
            System.out.println("Get network failed");
            return;
        }
        for (NicStatistics nic : nics) {
            this.cardMap.put(nic.name, nic);
        }
        for (NetCard_bak card : this.netCards) {
            card.updateModel(this.cardMap.get(card.getName()));
        }
    }

    public void updateDisk_6_Status() {
        System.out.println("update disk 6 status:");
        ArrayList<DiskStatistics> disks = this.commonInterface.getPhysDiskStatistics();
        for (DiskStatistics disk : disks) {
            System.out.println("disk card model:" + disk.cardModel);
            System.out.println("disk name: " + disk.getDiskName());
            System.out.println("disk position: " + disk.position);
            System.out.println("disk state: " + disk.state);
            System.out.println("disk temp: " + disk.temperature);
            if (disk.cardModel != 2) { //如果磁盘位于机箱内部
                if (!disk.position.equals("")) {
                    this.diskStatistics.put(Integer.parseInt(disk.position), disk);
                }

            }
        }
        this.diskStatus.updateDisk_6(diskStatistics);
    }

    public void updateDisk_8_Status() {
        System.out.println("update disk 8 status:");
        ArrayList<DiskStatistics> disks = this.commonInterface.getPhysDiskStatistics();
        for (DiskStatistics disk : disks) {
            System.out.println("disk card model:" + disk.cardModel);
            System.out.println("disk name: " + disk.getDiskName());
            System.out.println("disk position: " + disk.position);
            System.out.println("disk state: " + disk.state);
            System.out.println("disk temp: " + disk.temperature);
            if (disk.cardModel != 2) { //如果磁盘位于机箱内部
                if (!disk.position.equals("")) {
                    this.diskStatistics.put(Integer.parseInt(disk.position), disk);
                }

            }
        }
        this.diskStatus.updateDisk_8(diskStatistics);
    }

    public void updateDisk_16_Status() {
        System.out.println("update disk 16 status:");
        ArrayList<DiskStatistics> disks = this.commonInterface.getPhysDiskStatistics();
        for (DiskStatistics disk : disks) {
            System.out.println("disk card model:" + disk.cardModel);
            System.out.println("disk name: " + disk.getDiskName());
            System.out.println("disk position: " + disk.position);
            System.out.println("disk state: " + disk.state);
            System.out.println("disk temp: " + disk.temperature);
            if (disk.cardModel != 2) { //如果磁盘位于机箱内部
                if (!disk.position.equals("")) {
                    this.diskStatistics.put(Integer.parseInt(disk.position), disk);
                }

            }
        }
        this.diskStatus.updateDisk_16(diskStatistics);
    }
//        if (this.diskStatus.getTmp().equals(DiskStatus.tmp51)) {
//            this.diskStatus.setTmp(DiskStatus.tmp50);
//            this.diskStatus.setDisk(DiskStatus.trayBlueGreen);
//        } else {
//            this.diskStatus.setTmp(DiskStatus.tmp51);
//            this.diskStatus.setDisk(DiskStatus.trayBlueRed);
//        }
//        if (this.diskStatus.getDisk_6_1_state().equals(DiskStatus.c57)) {
//            this.diskStatus.setDisk_6_1_state(DiskStatus.c58);
//        } else {
//            this.diskStatus.setDisk_6_1_state(DiskStatus.c57);
//        }
//        if (this.diskStatus.getDisk_8_1().equals(DiskStatus.b47)) {
//            this.diskStatus.setDisk_8_1(DiskStatus.b48);
//        } else {
//            this.diskStatus.setDisk_8_1(DiskStatus.b47);
//        }
//        if (this.diskStatus.getDisk_16_1().equals(DiskStatus.a15)) {
//            this.diskStatus.setDisk_16_1(DiskStatus.a16);
//        } else {
//            this.diskStatus.setDisk_16_1(DiskStatus.a15);
//        }

    public void onTabChange(TabChangeEvent event) {
        System.out.println("Tab ID: " + event.getTab().getId());
        String tabID = event.getTab().getId();
        if (tabID != null) {
            if (tabID.equals("tab_net")) {
                // this.initNetWorkCard();
                this.isStopUpdateSys = true;
                this.isStopUpdateNet = false;
                this.isStopUpdateDisk = true;
            } else if (tabID.equals("tab_disk")) {
                // this.initDiskStatus();
                this.isStopUpdateSys = true;
                this.isStopUpdateNet = true;
                this.isStopUpdateDisk = false;
            } else if (tabID.equals("tab_sys")) {
                //  this.initReadDiskModel();
                //   this.initWriteDiskModel();
                this.isStopUpdateSys = false;
                this.isStopUpdateNet = true;
                this.isStopUpdateDisk = true;
            }
        }
//          System.out.println("############### Tab title: " + event.getTab().getTitle());

    }
}
