/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.vm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @introduction ���ÿ������ϵͳ
 * @author Administrator
 */
public class OSName implements Serializable{
    public String osName; //����ϵͳ����
    private List<OperatingSystem> osType = new ArrayList();//ÿ������ϵͳ�¶�Ӧ�ĸ����汾

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public List<OperatingSystem> getOsType() {
        return osType;
    }

    public void setOsType(List<OperatingSystem> osType) {
        this.osType = osType;
    }

}
