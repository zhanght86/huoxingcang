/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.disk.managedbean;

import com.marstor.msa.common.bean.DiskPool;
import com.marstor.msa.common.bean.VirtualDiskInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.ValidateUtility;
import com.marstor.msa.common.web.DiskInterface;
import com.marstor.msa.disk.model.DiskRootInfo;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "diskRootBean")
@ViewScoped
public class DiskRootBean implements Serializable {
    List<DiskRootInfo> all;
    DiskRootInfo info;

    /**
     * Creates a new instance of DiskRootBean
     */
    public DiskRootBean() {
       this.initDiskRoot();
    }
    public void initDiskRoot(){
        MSAResource resources = new MSAResource();
        all = new ArrayList();
        int num = 0;
        long size = 0;
         //获取所有磁盘池
        DiskInterface diskIn = InterfaceFactory.getDiskInterfaceInstance();
        DiskPool[] diskpools = diskIn.getAllDiskPool();
        if (diskpools != null && diskpools.length > 0) {
            num = diskpools.length;
        }
        info = new DiskRootInfo();
        info.setName(resources.get("disk.diskroot", "name"));
        info.setNum(num + "");
        all.add(info);
    }

    public List<DiskRootInfo> getAll() {
        return all;
    }

    public void setAll(List<DiskRootInfo> all) {
        this.all = all;
    }
    
    
}
