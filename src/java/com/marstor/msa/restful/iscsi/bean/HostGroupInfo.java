/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.iscsi.bean;

import com.marstor.msa.common.bean.HostGroup;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class HostGroupInfo implements Serializable {
    

    private String group_name;
    private List<String> group_member;

    public HostGroupInfo() {
    }

    public HostGroupInfo(HostGroup hostGroup) {
        this.group_name = hostGroup.getGroupName();
        this.group_member = hostGroup.getGroupMember();
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public List<String> getGroup_member() {
        return group_member;
    }

    public void setGroup_member(List<String> group_member) {
        this.group_member = group_member;
    }








    
}
