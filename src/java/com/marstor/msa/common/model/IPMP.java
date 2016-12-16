/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.bean.IpmpTargetInformation;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
//@ManagedBean(name = "iPMP")
//@RequestScoped
public class IPMP implements Serializable{
    
    public String IPMPGroup;
    public String groupName;
    public String state;
    public String type;
    public List<IPMPDetail> detailList;
    public List<IpmpTargetInformation> targetList;
    

    /**
     * Creates a new instance of IPMP
     */
    public IPMP() {
    }

    public String getIPMPGroup() {
        return IPMPGroup;
    }

    public void setIPMPGroup(String IPMPGroup) {
        this.IPMPGroup = IPMPGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<IPMPDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<IPMPDetail> detailList) {
        this.detailList = detailList;
    }

    public List<IpmpTargetInformation> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<IpmpTargetInformation> targetList) {
        this.targetList = targetList;
    }

 
  

    

   
    
    
}
