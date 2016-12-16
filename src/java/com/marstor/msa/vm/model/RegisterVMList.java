/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @introduction ���ע���������б�
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
    public static final int VM_RUN = 1;  //�����״̬������
    public static final int VM_PAUSE = 2;  //�����״̬����ͣ
    public static final int VM_STOP = 3;  //�����״̬���ر�
    public static final int VM_Aborted = 4;  //�����״̬���쳣�ر�
    public static final int VM_Inaccessible = 5;  //�����״̬����
    public static final int VM_STOPING = 6;  //�����״̬�����ڹر���

    private String vmName;  //���������
    private int remotePort;  //Զ�̶˿ں�
    private long memorySize;  //�ڴ��С
    private int vmState;  //�����״̬
    private String state;  //�����״̬

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
