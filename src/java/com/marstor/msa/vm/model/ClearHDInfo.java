/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @introduction �������Ӳ����Ϣ
 * @author Administrator
 */
@ManagedBean(name = "clearHDInfo")
@RequestScoped
public class ClearHDInfo implements Serializable{

    public String hdName;  //Ӳ��ȫ·������
    public boolean usage;  //true ��������ʹ�ã�false����û���� ���ĸ������ʹ���ˣ������·�������Ϊ�մ����Ӳ��δ��ʹ��
    public String state;  //Ӳ��״̬,inaccessible������
    
    /**
     * Creates a new instance of GetHDINFO
     */
    public ClearHDInfo() {
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public String getHdName() {
        return hdName;
    }

    public void setHdName(String hdName) {
        this.hdName = hdName;
    }

    public boolean isUsage() {
        return usage;
    }

    public void setUsage(boolean usage) {
        this.usage = usage;
    }



   

}
