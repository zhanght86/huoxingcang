/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.vm.model;

import java.io.Serializable;

/**
 * @introduction оƬ�����ࣨ�������У�
 * @author Administrator
 */
public class ChipTypes implements Serializable{
    public String ControllerName;  //����������
    public String[] chipTypes;  //оƬ����

    public String getControllerName() {
        return ControllerName;
    }

    public void setControllerName(String ControllerName) {
        this.ControllerName = ControllerName;
    }

    public String[] getChipTypes() {
        return chipTypes;
    }

    public void setChipTypes(String[] chipTypes) {
        this.chipTypes = chipTypes;
    }
    

}
