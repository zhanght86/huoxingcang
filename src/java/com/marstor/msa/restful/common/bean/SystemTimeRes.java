/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.common.bean;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class SystemTimeRes  implements Serializable{
    private Date system_time;
;
    public SystemTimeRes() {
    }   

    public SystemTimeRes(Date system_time) {
        this.system_time = system_time;
    }

    public Date getSystem_time() {
        return system_time;
    }

    public void setSystem_time(Date system_time) {
        this.system_time = system_time;
    }
    
    
}
