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
        count.setProperty("��������");
        count.setCount(9);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("����������(GB)");
        count.setCount(8663);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("���ݴ�����");
        count.setCount(5);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("δ�ô�����");
        count.setCount(4);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("�ȱ�������");
        count.setCount(0);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("�����������");
        count.setCount(0);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("д���������");
        count.setCount(0);
        countList.add(count);
        
        count = new DiskCount();
        count.setProperty("��������");
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
