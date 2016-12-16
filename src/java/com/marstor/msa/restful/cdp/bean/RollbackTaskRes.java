/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.cdp.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class RollbackTaskRes {
    private CdpRollbackTask rollback_task;

    public RollbackTaskRes() {
    }
    
    

    public RollbackTaskRes(CdpRollbackTask rollback_task) {
        this.rollback_task = rollback_task;
    }

    public CdpRollbackTask getRollback_task() {
        return rollback_task;
    }

    public void setRollback_task(CdpRollbackTask rollback_task) {
        this.rollback_task = rollback_task;
    }
    
}
