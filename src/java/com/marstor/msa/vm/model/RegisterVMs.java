/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import com.marstor.msa.disk.model.VirtualDetail;
import java.io.Serializable;
import java.util.List;


/**
 * @introduction 获得注册的虚拟机列表
 * @author Administrator
 */
public class RegisterVMs  implements Serializable{

    /**
     * Creates a new instance of RegisterVMList
     */
    public RegisterVMs() {
    }
    public static final int VM_RUN = 1;  //虚拟机状态：运行
    public static final int VM_PAUSE = 2;  //虚拟机状态：暂停
    public static final int VM_STOP = 3;  //虚拟机状态：关闭
    public static final int VM_Aborted = 4;  //虚拟机状态：异常关闭
    public static final int VM_Inaccessible = 5;  //虚拟机状态：损坏
    public static final int VM_STOPING = 6;  //虚拟机状态：正在关闭中

    private String vmName;  //虚拟机名称
    private int remotePort;  //远程端口号
    private long memorySize;  //内存大小
    private int vmState;  //虚拟机状态
    private String state;  //虚拟机状态
    private String belongVol;
    
    private boolean isStart;
    private boolean isPause;
    private boolean isResume;
    private boolean notStart;
    private boolean notPause;
    private boolean notResume;
    private boolean notDelete;
    private boolean notCheck;
    private boolean notReboot;
    private boolean notClose;
    private String uuid;
    private String path;
     public List<VirtualDetail> detail;
  

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }
    
    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public int getVmState() {
        return vmState;
    }

    public void setVmState(int vmState) {
        this.vmState = vmState;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isIsStart() {
        return isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public boolean isIsPause() {
        return isPause;
    }

    public void setIsPause(boolean isPause) {
        this.isPause = isPause;
    }

    public boolean isIsResume() {
        return isResume;
    }

    public void setIsResume(boolean isResume) {
        this.isResume = isResume;
    }

    public boolean isNotDelete() {
        return notDelete;
    }

    public void setNotDelete(boolean notDelete) {
        this.notDelete = notDelete;
    }

    public boolean isNotCheck() {
        return notCheck;
    }

    public void setNotCheck(boolean notCheck) {
        this.notCheck = notCheck;
    }

    public boolean isNotStart() {
        return notStart;
    }

    public void setNotStart(boolean notStart) {
        this.notStart = notStart;
    }

    public boolean isNotPause() {
        return notPause;
    }

    public void setNotPause(boolean notPause) {
        this.notPause = notPause;
    }

    public boolean isNotResume() {
        return notResume;
    }

    public void setNotResume(boolean notResume) {
        this.notResume = notResume;
    }

    public boolean isNotReboot() {
        return notReboot;
    }

    public void setNotReboot(boolean notReboot) {
        this.notReboot = notReboot;
    }

    public boolean isNotClose() {
        return notClose;
    }

    public void setNotClose(boolean notClose) {
        this.notClose = notClose;
    }

    public String getBelongVol() {
        return belongVol;
    }

    public void setBelongVol(String belongVol) {
        this.belongVol = belongVol;
    }

    public List<VirtualDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<VirtualDetail> detail) {
        this.detail = detail;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
}
