/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.vm.model;

import java.io.Serializable;

/**
 * @introduction 芯片类型类（控制器中）
 * @author Administrator
 */
public class ChipTypes implements Serializable{
    public String ControllerName;  //控制器名称
    public String[] chipTypes;  //芯片类型

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
