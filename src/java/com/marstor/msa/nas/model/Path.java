/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;

import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyMenu;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlPanelGrid;
/**
 *
 * @author Administrator
 */
public class Path implements Serializable{
    
    private String name="";
    private  boolean status;
    private  boolean nfsStatus;
    private  boolean cifsStatus;
    private String  onOrOff="" ;
    private String  nfsOnOrOff="" ;
    private String  cifsOnOrOff="" ;
    private String used = "";
    private String available = "";
    private String mountpoint = "";
    private String setStatus;
    private boolean startDeduplicate;
    private boolean startCheck;
    private boolean startCompress;
    private boolean startQuota; //≈‰∂Óπ‹¿Ì
    private String maxSpace;
    private ArrayList<String> blockSize = new ArrayList<String>();
    private String  selectedBlockSize;
    private  HtmlSelectBooleanCheckbox checkData;
    private  HtmlSelectManyMenu level;
    private HtmlInputText maxSpaceText ;
    private int selectedLevel =1;
    private HtmlPanelGrid  pannel;
    private boolean startDataCheckBoxStatus;
    private boolean levelSpinnerStatus;
    private boolean maxSpaceStatus;
    private boolean authorityButtonStatus;

    public Path(String  name,boolean status, boolean nfsStatus, boolean cifsStatus,String used,String available,String mountpoint) {
        this.status = status;
        this.nfsStatus = nfsStatus;
        this.cifsStatus = cifsStatus;
        this.name = name;
        this.used = used;
        this.available = available;
        this.mountpoint = mountpoint;
        this.initBlockSize();
    }

    public Path() {
        initBlockSize();
        
    }

    public Path(String  name) {
        this.name = name;
        this.nfsStatus = false;
        this.cifsStatus = false;
        this.mountpoint = "/"+this.name;
        this.used = "40.0KB";
        this.available = "1.52TB";
        this.status = true;
        initBlockSize();
    }

    public void initBlockSize() {
        blockSize.add("4K");
        blockSize.add("8K");
        blockSize.add("16K");
        blockSize.add("32K");
        blockSize.add("64K");
        blockSize.add("128K");
    }
    public boolean isLevelSpinnerStatus() {
        return levelSpinnerStatus;
    }

    public void setLevelSpinnerStatus(boolean levelSpinnerStatus) {
        this.levelSpinnerStatus = levelSpinnerStatus;
    }

    public boolean isMaxSpaceStatus() {
        return maxSpaceStatus;
    }

    public void setMaxSpaceStatus(boolean maxSpaceStatus) {
        this.maxSpaceStatus = maxSpaceStatus;
    }

    public boolean isStartDataCheckBoxStatus() {
        return startDataCheckBoxStatus;
    }

    public void setStartDataCheckBoxStatus(boolean startDataCheckBoxStatus) {
        this.startDataCheckBoxStatus = startDataCheckBoxStatus;
    }

    public HtmlPanelGrid getPannel() {
        return pannel;
    }

    public void setPannel(HtmlPanelGrid pannel) {
        this.pannel = pannel;
    }

    public int getSelectedLevel() {
        return selectedLevel;
    }

    public void setSelectedLevel(int selectedLevel) {
        this.selectedLevel = selectedLevel;
    }

    

    public HtmlInputText getMaxSpaceText() {
        return maxSpaceText;
    }

    public void setMaxSpaceText(HtmlInputText maxSpaceText) {
        this.maxSpaceText = maxSpaceText;
    }

    public HtmlSelectManyMenu getLevel() {
        return level;
    }

    public void setLevel(HtmlSelectManyMenu level) {
        this.level = level;
    }

    public HtmlSelectBooleanCheckbox getCheckData() {
        return checkData;
    }

    public void setCheckData(HtmlSelectBooleanCheckbox checkData) {
        this.checkData = checkData;
    }

    public String getSelectedBlockSize() {
        return selectedBlockSize;
    }

    public void setSelectedBlockSize(String selectedBlockSize) {
        this.selectedBlockSize = selectedBlockSize;
    }

    public boolean isStartCompress() {
        return startCompress;
    }

    public void setStartCompress(boolean startCompress) {
        this.startCompress = startCompress;
    }

    public ArrayList<String> getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(ArrayList<String> blockSize) {
        this.blockSize = blockSize;
    }

    public String getMaxSpace() {
        return maxSpace;
    }

    public void setMaxSpace(String maxSpace) {
        this.maxSpace = maxSpace;
    }

    public boolean isStartCheck() {
        return startCheck;
    }

    public void setStartCheck(boolean startCheck) {
        this.startCheck = startCheck;
    }

    public boolean isStartDeduplicate() {
        return startDeduplicate;
    }

    public void setStartDeduplicate(boolean startDeduplicate) {
        this.startDeduplicate = startDeduplicate;
    }

    public boolean isStartQuota() {
        return startQuota;
    }

    public void setStartQuota(boolean startQuota) {
        this.startQuota = startQuota;
    }

    public String getSetStatus() {
        if(this.status) {
            this.setStatus= Constant.offLine;
        }else {
            this.setStatus =Constant.onLine;
        }
        return setStatus;
    }

    public void setSetStatus(String setStatus) {
        this.setStatus = setStatus;
    }
    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getMountpoint() {
        return mountpoint;
    }

    public void setMountpoint(String mountpoint) {
        this.mountpoint = mountpoint;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }
    

    public boolean isCifsStatus() {
        return cifsStatus;
    }

    public void setCifsStatus(boolean cifsStatus) {
        this.cifsStatus = cifsStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNfsStatus() {
        return nfsStatus;
    }

    public void setNfsStatus(boolean nfsStatus) {
        this.nfsStatus = nfsStatus;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCifsOnOrOff() {
        if(this.cifsStatus) {
            this.cifsOnOrOff = Constant.open;
        }else {
             this.cifsOnOrOff = Constant.notOpen;
        }
        return cifsOnOrOff;
    }

    public void setCifsOnOrOff(String cifsOnOrOff) {
        this.cifsOnOrOff = cifsOnOrOff;
    }

    public String getNfsOnOrOff() {
        if(this.nfsStatus) {
            this.nfsOnOrOff = Constant.open;
        }else {
             this.nfsOnOrOff = Constant.notOpen;
        }
        return nfsOnOrOff;
    }

    public void setNfsOnOrOff(String nfsOnOrOff) {
        this.nfsOnOrOff = nfsOnOrOff;
    }

    public String getOnOrOff() {
        if(this.status) {
            this.onOrOff = Constant.on;
        }else {
            this.onOrOff = Constant.off;
        }
        return onOrOff;
    }

    public void setOnOrOff(String onOrOff) {
        this.onOrOff = onOrOff;
    }
    public void changDuplicate() {
        if(this.startDeduplicate) {
           // this.checkData.setDisabled(false);
            this.startDataCheckBoxStatus = false;
        }else {
             //this.checkData.setDisabled(true);
            this.startDataCheckBoxStatus = true;
        }
    }
    public void  changeCompress() {
        if(this.startCompress) {
            this.levelSpinnerStatus = false;
            //this.level.setDisabled(false);
        }else {
             //this.level.setDisabled(true);
            this.levelSpinnerStatus = true;
        }
    }
    public void  changeQuota() {
        if(this.startQuota) {
            //this.maxSpaceText.setDisabled(false);
           this.maxSpaceStatus = false;
        }else {
             this.maxSpaceStatus = true;
            //this.maxSpaceText.setDisabled(true);
        }
    }
    public void disableDataCheck() {
        if(this.checkData != null){
            this.checkData.setDisabled(true);
        }
    }
    public void enableDataCheck() {
        if(this.checkData != null){
            this.checkData.setDisabled(false);
        }
    }
    public void  disableMaxSpaceText() {
        if(this.maxSpaceText != null) {
            this.maxSpaceText.setDisabled(true);
        }
    }
    public void  enableMaxSpaceText() {
        if(this.maxSpaceText != null) {
            this.maxSpaceText.setDisabled(false);
        }
    }
    
    
}
