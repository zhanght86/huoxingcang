/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import javax.faces.component.UICommand;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyMenu;
import javax.faces.component.html.HtmlSelectOneListbox;

/**
 *
 * @author Administrator
 */
public class NFS implements Serializable{

    private String path;
    private boolean status;
    private boolean anonoyRW;
    private boolean anonoyVisit;
    private boolean configButtonStatus;
    private List<String> rw = new ArrayList<String>(); //有读写权限的IP地址
    private ArrayList<String> readOnly = new ArrayList<String>();
    private ArrayList<String> root = new ArrayList<String>();
    private String statusDisplay;
    private HtmlCommandButton addRWButton;
    private HtmlCommandButton delRWButton;
    private HtmlCommandButton addROButton;
    private HtmlCommandButton delROButton;
    private HtmlCommandButton addRootButton;
    private HtmlCommandButton delRootButton;
    private  HtmlSelectBooleanCheckbox anonBox;
    private  HtmlSelectBooleanCheckbox anonRWBox;
    private  HtmlSelectManyMenu level;
    
    private HtmlInputText text = new HtmlInputText();
    private String selectedRW;
    private String selectedRO;
    private String selectedRoot;
    private String errorInfo;

    public NFS(String path, boolean status) {
        this.path = path;
        this.status = status;
        if (this.status) {
            this.statusDisplay = Constant.open;
            //this.button.setDisabled(false);
        } else {
            this.statusDisplay = Constant.notOpen;
            // this.button.setDisabled(true);
        }

    }

    public NFS() {
        //this.button.setValue("添加");
//        if(!this.status) {
//            this.button.setDisabled(true);
//        }else {
//             this.button.setDisabled(false);
//        }
        this.status = false;
        this.configButtonStatus = true; //链接按钮的disable属性
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public boolean isConfigButtonStatus() {
        return configButtonStatus;
    }

    public void setConfigButtonStatus(boolean configButtonStatus) {
        this.configButtonStatus = configButtonStatus;
    }

    public HtmlSelectManyMenu getLevel() {
        return level;
    }

    public void setLevel(HtmlSelectManyMenu level) {
        this.level = level;
    }

    public HtmlSelectBooleanCheckbox getAnonBox() {
        return anonBox;
    }

    public void setAnonBox(HtmlSelectBooleanCheckbox anonBox) {
        this.anonBox = anonBox;
    }

    public HtmlSelectBooleanCheckbox getAnonRWBox() {
        return anonRWBox;
    }

    public void setAnonRWBox(HtmlSelectBooleanCheckbox anonRWBox) {
        this.anonRWBox = anonRWBox;
    }

    public HtmlCommandButton getAddRootButton() {
        return addRootButton;
    }

    public void setAddRootButton(HtmlCommandButton addRootButton) {
        this.addRootButton = addRootButton;
    }

    public HtmlCommandButton getDelRootButton() {
        return delRootButton;
    }

    public void setDelRootButton(HtmlCommandButton delRootButton) {
        this.delRootButton = delRootButton;
    }

    public String getSelectedRoot() {
        return selectedRoot;
    }

    public void setSelectedRoot(String selectedRoot) {
        this.selectedRoot = selectedRoot;
    }

    public HtmlCommandButton getAddROButton() {
        return addROButton;
    }

    public void setAddROButton(HtmlCommandButton addROButton) {
        this.addROButton = addROButton;
    }

    public HtmlCommandButton getDelROButton() {
        return delROButton;
    }

    public void setDelROButton(HtmlCommandButton delROButton) {
        this.delROButton = delROButton;
    }

    public String getSelectedRO() {
        return selectedRO;
    }

    public void setSelectedRO(String selectedRO) {
        this.selectedRO = selectedRO;
    }

    public String getSelectedRW() {
        return selectedRW;
    }

    public void setSelectedRW(String selectedRW) {
        this.selectedRW = selectedRW;
    }

    public HtmlCommandButton getDelRWButton() {
        return delRWButton;
    }

    public void setDelRWButton(HtmlCommandButton delRWButton) {
        this.delRWButton = delRWButton;
    }

    public HtmlInputText getText() {
        return text;
    }

    public void setText(HtmlInputText text) {
        this.text = text;
    }

    public HtmlCommandButton getAddRWButton() {
        return addRWButton;
    }

    public void setAddRWButton(HtmlCommandButton addRWButton) {
        this.addRWButton = addRWButton;
    }

    public String getStatusDisplay() {
        if (this.status) {
            this.statusDisplay = Constant.open;
            //this.button.setDisabled(false);
        } else {
            this.statusDisplay = Constant.notOpen;
            // this.button.setDisabled(true);
        }
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public boolean isAnonoyRW() {
        return anonoyRW;
    }

    public void setAnonoyRW(boolean anonoyRW) {
        this.anonoyRW = anonoyRW;
    }

    public boolean isAnonoyVisit() {
        return anonoyVisit;
    }

    public void setAnonoyVisit(boolean anonoyVisit) {
        this.anonoyVisit = anonoyVisit;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<String> getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(ArrayList<String> readOnly) {
        this.readOnly = readOnly;
    }

    public ArrayList<String> getRoot() {
        return root;
    }

    public void setRoot(ArrayList<String> root) {
        this.root = root;
    }

    public List<String> getRw() {
        return rw;
    }

    public void setRw(List<String> rw) {
        this.rw = rw;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
        if (this.status) {
            this.statusDisplay = Constant.open;
        } else {
            this.statusDisplay = Constant.notOpen;
        }
    }

    public void listen() {
        if (!this.status) {
            this.addRWButton.setDisabled(true);
            this.delRWButton.setDisabled(true);
            this.addROButton.setDisabled(true);
            this.delROButton.setDisabled(true);
            this.addRootButton.setDisabled(true);
            this.delRootButton.setDisabled(true);
             this.anonBox.setDisabled(true);
              this.anonRWBox.setDisabled(true);
            

            //this.text.setReadonly(true);
        } else {
            this.addRWButton.setDisabled(false);
            this.delRWButton.setDisabled(false);
            this.addROButton.setDisabled(false);
            this.delROButton.setDisabled(false);
            this.addRootButton.setDisabled(false);
            this.delRootButton.setDisabled(false);
             this.anonBox.setDisabled(false);
              this.anonRWBox.setDisabled(false);
            //this.anonoyRW = true;
           // this.anonoyVisit = true;
            //this.text.setReadonly(false);
        }
    }

    public void disableButton() {
        if (this.addRWButton != null) {
            this.addRWButton.setDisabled(true);
        }
        if (this.delRWButton != null) {
            this.delRWButton.setDisabled(true);
        }
        if (this.addROButton != null) {
            this.addROButton.setDisabled(true);
        }
        if (this.delROButton != null) {
            this.delROButton.setDisabled(true);
        }
        if (this.addRootButton != null) {
            this.addRootButton.setDisabled(true);
        }
        if (this.delRootButton != null) {
            this.delRootButton.setDisabled(true);
        }

    }

    public void enableButton() {
        if (this.addRWButton != null) {
            this.addRWButton.setDisabled(false);
        }
        if (this.delRWButton != null) {
            this.delRWButton.setDisabled(false);
        }
        if (this.addROButton != null) {
            this.addROButton.setDisabled(false);
        }
        if (this.delROButton != null) {
            this.delROButton.setDisabled(false);
        }
        if (this.addRootButton != null) {
            this.addRootButton.setDisabled(false);
        }
        if (this.delRootButton != null) {
            this.delRootButton.setDisabled(false);
        }


    }

    public void enableCheckbox() {
//        this.anonoyRW = true;
//        this.anonoyVisit = true;
         if (this.anonBox != null) {
            this.anonBox.setDisabled(false);
        }
        if (this.anonRWBox != null) {
            this.anonRWBox.setDisabled(false);
        }
    }
    public void disableCheckbox() {
        if (this.anonBox != null) {
            this.anonBox.setDisabled(true);
        }
        if (this.anonRWBox != null) {
            this.anonRWBox.setDisabled(true);
        }
    }
    public void addToRW(String ip) {
        this.rw.add(ip);
    }

    public void addToRO(String ip) {
        this.readOnly.add(ip);
    }

    public void addToRoot(String ip) {
        this.root.add(ip);
    }

    public void delRWIP() {
        this.rw.remove(this.selectedRW);
    }

    public void delROIP() {
        this.readOnly.remove(this.selectedRO);
    }

    public void delRootIP() {
        this.root.remove(this.selectedRoot);
    }
}
