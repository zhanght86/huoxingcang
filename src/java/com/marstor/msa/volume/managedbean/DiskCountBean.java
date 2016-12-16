/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.volume.managedbean;

import com.marstor.msa.volume.model.DiskCount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "countBean")
@RequestScoped
public class DiskCountBean  implements Serializable{
    public DiskCount count;
    public List<DiskCount> countList;

    /**
     * Creates a new instance of CountImpl
     */
    public DiskCountBean() {
        getCounts();
    }
    
    public void getCounts(){
        countList = new ArrayList();
        
        count = new DiskCount();
        count.setProperty("磁盘总数");
        count.setCount(9);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("磁盘总容量(GB)");
        count.setCount(8663);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("数据磁盘数");
        count.setCount(5);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("未用磁盘数");
        count.setCount(4);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("热备磁盘数");
        count.setCount(0);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("读缓存磁盘数");
        count.setCount(0);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("写缓存磁盘数");
        count.setCount(0);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("卷组总数");
        count.setCount(4);
        countList.add(count);
    }

    public List<DiskCount> getCountList() {
        return countList;
    }

    public void setCountList(List<DiskCount> countList) {
        this.countList = countList;
    }
    
}
