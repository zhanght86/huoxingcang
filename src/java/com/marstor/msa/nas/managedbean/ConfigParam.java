/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.managedbean;

import com.marstor.msa.nas.util.Debug;
import com.marstor.msa.nas.model.MySession;
import com.marstor.msa.nas.model.Path;
import com.marstor.msa.nas.model.PathDataModel;
import com.marstor.msa.nas.model.SharePathTable;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyMenu;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlPanelGrid;
/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class ConfigParam implements Serializable{
    
    
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



    public ConfigParam() {
        blockSize.add("8K");
        blockSize.add("16K");
        blockSize.add("32K");
        blockSize.add("64K");
        
      SharePathTable  share =   MySession.getShareData();
     
      PathDataModel  model  =  share.getModel();
      Path  path = model.getPathByName(share.getEditPath().getName());
        setName(path.getName());
       setStartDeduplicate(path.isStartDeduplicate());
       setStartCompress(path.isStartCompress());
        setStartCheck(path.isStartCheck());
//        if(!editPath.isStartDeduplicate()) {
//            editPath.setStartCheck(false);
//        }else {
//            editPath.setStartCheck(true);
//        }
        setStartQuota(path.isStartQuota());
//        if(!editPath.isStartQuota()) {
//            editPath.disableMaxSpaceText();
//        }else {
//            
//            editPath.enableMaxSpaceText();
//        }
       setSelectedLevel(path.getSelectedLevel());
       setMaxSpace(path.getMaxSpace());
       setSelectedBlockSize(path.getSelectedBlockSize());
      //  this.name = path.getName();
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



    public void setCifsOnOrOff(String cifsOnOrOff) {
        this.cifsOnOrOff = cifsOnOrOff;
    }



    public void setNfsOnOrOff(String nfsOnOrOff) {
        this.nfsOnOrOff = nfsOnOrOff;
    }



    public void setOnOrOff(String onOrOff) {
        this.onOrOff = onOrOff;
    }
    public void changDuplicate() {
        if(this.startDeduplicate) {
            this.checkData.setDisabled(false);
        }else {
             this.checkData.setDisabled(true);
        }
    }
    public void  changeCompress() {
        if(this.startCompress) {
            this.level.setDisabled(false);
        }else {
             this.level.setDisabled(true);
        }
    }
    public void  changeQuota() {
        if(this.startQuota) {
            this.maxSpaceText.setDisabled(false);
           
        }else {
            this.maxSpaceText.setDisabled(true);
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
    public void  saveConfigParam() {
        SharePathTable  share =   MySession.getShareData();
        
       Path sharePath = share.getModel().getPathByName(this.getName());
       sharePath.setStartDeduplicate(this.isStartDeduplicate());
      // sharePath.setStartCheck(Boolean.parseBoolean(this.editPath.getCheckData().getValue().toString()));
     //  sharePath.getCheckData().setValue(this.editPath.getCheckData().getValue());
       sharePath.setStartCheck(this.isStartCheck());
       sharePath.setStartCompress(this.isStartCompress());
       sharePath.setSelectedLevel(this.getSelectedLevel());
       sharePath.setStartQuota(this.isStartQuota());
       sharePath.setMaxSpace(this.getMaxSpace());
       sharePath.setSelectedBlockSize(this.getSelectedBlockSize());
       
       
    }
    
}
