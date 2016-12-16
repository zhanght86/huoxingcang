/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.managedbean;

import com.marstor.msa.volume.model.Disk;
import com.marstor.msa.volume.model.DiskStates;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * ∞¸¿®Œ¥∑÷≈‰°¢»»±∏≈Ãavailableµƒ¥≈≈Ã
 * @author Administrator
 */
@ManagedBean(name = "unusedDiskBean")
@SessionScoped
public class UnusedDiskBean extends DiskStates{
    public Disk diskBean;
    public List<Disk> diskList;
    /**
     * Creates a new instance of unusedDiskBean
     */
    public UnusedDiskBean() {
        addDiskList();
        
    }
    
    public List<Disk> addDiskList(){
        diskList = new ArrayList();
        
        diskBean = new Disk();
        diskBean.setPosition("SASø® 1");
        diskBean.setNum("5");
        diskBean.setSize("233");
        diskBean.setDiskName("c0t7d0");
        diskBean.setState(DISK_MODEL_UNUSED+"");
        diskList.add(diskBean);
        
        diskBean = new Disk();
        diskBean.setPosition("SASø® 2");
        diskBean.setNum("9");
        diskBean.setSize("1863");
        diskBean.setDiskName("c3t50014EE05833F874d0");
        diskBean.setState(DISK_MODEL_UNUSED+"");
        diskList.add(diskBean);
        
        diskBean = new Disk();
        diskBean.setPosition("SASø® 2");
        diskBean.setNum("11");
        diskBean.setSize("1863");
        diskBean.setDiskName("c3t50014EE0AD899AD6d0");
        diskBean.setState(DISK_MODEL_UNUSED+"");
        diskList.add(diskBean);
        
        diskBean = new Disk();
        diskBean.setPosition("SASø® 2");
        diskBean.setNum("16");
        diskBean.setSize("1863");
        diskBean.setDiskName("c3t50014EE0AD899733d0");
        diskBean.setState(SPARE_DISK_STATE_AVAILIBLE+"");
        diskList.add(diskBean);
        return diskList;
    }

    public List<Disk> getDiskList() {
        return diskList;
    }

    public void setDiskList(List<Disk> diskList) {
        this.diskList = diskList;
    }
    
    
    
}
