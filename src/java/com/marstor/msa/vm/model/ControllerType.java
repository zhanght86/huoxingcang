/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vm.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "controllerType")
@RequestScoped
public class ControllerType implements Serializable{

    /**
     * Creates a new instance of ControllerType
     */
    public ControllerType() {
    }
    private String controllerName;//控制器名称
    private int controllerType;//控制器类型
    private int isHave;  //0没有，1有
    private String operation;
    private String icon;
    private int controllerChipset;//控制器芯片组
    private boolean edit;  //是否可编辑控制器
    private boolean add_del;  //是否可添加或删除控制器

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public int getControllerType() {
        return controllerType;
    }

    public void setControllerType(int controllerType) {
        this.controllerType = controllerType;
    }

    public int getIsHave() {
        return isHave;
    }

    public void setIsHave(int isHave) {
        this.isHave = isHave;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getControllerChipset() {
        return controllerChipset;
    }

    public void setControllerChipset(int controllerChipset) {
        this.controllerChipset = controllerChipset;
    }

    public boolean isAdd_del() {
        return add_del;
    }

    public void setAdd_del(boolean add_del) {
        this.add_del = add_del;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    
   
    
}
