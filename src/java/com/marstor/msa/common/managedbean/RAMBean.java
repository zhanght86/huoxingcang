/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.chart.MeterGaugeChartModel;  
/**
 *
 * @author Administrator
 */
@ManagedBean(name = "ramBean")
@RequestScoped
public class RAMBean implements Serializable
{   
    private MeterGaugeChartModel cpuModel; 
    private MeterGaugeChartModel liveCPUModel; 
    private MeterGaugeChartModel tmpModel;  
    private MeterGaugeChartModel memModel;
    
    public RAMBean() {
        createCPUModel();  
        createTemModel();
        createMemModel();
    }  
    
    public MeterGaugeChartModel getLiveCPUModel() {  
        int random1 = (int)(Math.random() * 100);   
        liveCPUModel.setValue(random1);
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^" + random1);
        return liveCPUModel;  
    }  

    public void setLiveCPUModel(MeterGaugeChartModel liveCPUModel) {
        this.liveCPUModel = liveCPUModel;
    }
    
    public void count(){
        this.cpuModel.setValue(5);
        this.tmpModel.setValue(10);
    }   

    private void createCPUModel() {  
  
        List<Number> intervals = new ArrayList<Number>(){{  
            add(60);
            add(80);
            add(100);
        }};  
  
        cpuModel = new MeterGaugeChartModel(60, intervals);
               
    }
    
    private void createTemModel() {  
  
        List<Number> intervals = new ArrayList<Number>(){{  
            add(60);
            add(80);
            add(120);
        }};  
  
        tmpModel = new MeterGaugeChartModel(60, intervals); 
    }
    
    private void createMemModel() {  
  
        List<Number> intervals = new ArrayList<Number>(){{  
            add(80);
            add(90);
            add(100);
        }};  
  
        memModel = new MeterGaugeChartModel(60, intervals); 
    }

    public MeterGaugeChartModel getCpuModel() {
        int random1 = (int)(Math.random() * 100); 
        cpuModel.setValue(random1); 
        return cpuModel;
    }

    public void setCpuModel(MeterGaugeChartModel cpuModel) {
        this.cpuModel = cpuModel;
    }

    public MeterGaugeChartModel getTmpModel() {
        int random1 = (int)(Math.random() * 100); 
        tmpModel.setValue(random1); 
        return tmpModel;
    }

    public void setTmpModel(MeterGaugeChartModel tmpModel) {
        this.tmpModel = tmpModel;
    }

    public MeterGaugeChartModel getMemModel() {
        int random1 = (int)(Math.random() * 100); 
        memModel.setValue(random1); 
        return memModel;
    }

    public void setMemModel(MeterGaugeChartModel memModel) {
        this.memModel = memModel;
    }
   
}
