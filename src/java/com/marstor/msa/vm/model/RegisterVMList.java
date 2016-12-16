/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @introduction 获得注册的虚拟机列表
 * @author Administrator
 */
@ManagedBean(name = "registerVMList")
@RequestScoped
public class RegisterVMList  implements Serializable{

    /**
     * Creates a new instance of RegisterVMList
     */
    public RegisterVMList() {
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
    
    
}
