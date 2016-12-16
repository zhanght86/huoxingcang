/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.common.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class SystemUserRes  implements Serializable{
    private SystemUser system_user
;
    public SystemUserRes() {
    }   

    public SystemUserRes(SystemUser disk_group) {
        this.system_user = disk_group;
    }

    public SystemUser getSystem_user() {
        return system_user;
    }

    public void setSystem_user(SystemUser system_user) {
        this.system_user = system_user;
    }

    
    
}
