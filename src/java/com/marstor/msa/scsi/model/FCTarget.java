/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.scsi.model;

import com.marstor.msa.common.util.MSAResource;

/**
 *
 * @author Administrator
 */
public class FCTarget {
    private String name;
    private String alias;
    private String status="";
//    private Chap chap = new Chap();
//    private IPBing  bing = new IPBing();

    public FCTarget(String name, String alias, String status) {
       if(name == null) {
           return;
       }
        this.name = name.toUpperCase();
        if(alias == null) {
            return ;
        }
        this.alias = alias.toUpperCase();
        MSAResource res = new MSAResource();
        if(status.equalsIgnoreCase("Online")) {
            this.status = res.get("online");
        }else if(status.equalsIgnoreCase("offline")) {
            this.status = res.get("offLine");
        }else if(status.equalsIgnoreCase("offlining")) {
            this.status = res.get("offLining");
        }
    }

    public FCTarget() {
    }

    public FCTarget(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
