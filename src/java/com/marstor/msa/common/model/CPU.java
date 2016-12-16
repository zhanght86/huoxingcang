/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.managedbean.SystemOutPrintln;
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
@ManagedBean(name = "cPUBean")
@RequestScoped
public class CPU implements Serializable
{

    public int id;                    //id
    public int physical_id;           //
    public int core_id;               //核ID
    public int freq;                  //主频MHz
    public int current_freq;          //当前主频MHz
    public int temp;                  //温度℃
    public double loadavg_1m;           //1分钟平均负载 
    public double loadavg_5m;           //5分钟平均负载
    public double loadavg_15m;          //15分钟平均负载 
    
    private MeterGaugeChartModel cpuModel; 
    private MeterGaugeChartModel liveCPUModel; 
    private MeterGaugeChartModel tmpModel;  
    private MeterGaugeChartModel memModel;
    
    public CPU() {
        createCPUModel();  
        createTemModel();
        createMemModel();
    }  
    
    public MeterGaugeChartModel getLiveCPUModel() {  
        int random1 = (int)(Math.random() * 100);   
        liveCPUModel.setValue(random1);
        SystemOutPrintln.print_common("random1=" + random1);
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
       
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhysical_id() {
        return physical_id;
    }

    public void setPhysical_id(int physical_id) {
        this.physical_id = physical_id;
    }

    public int getCore_id() {
        return core_id;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public int getCurrent_freq() {
        return current_freq;
    }

    public void setCurrent_freq(int current_freq) {
        this.current_freq = current_freq;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public double getLoadavg_1m() {
        return loadavg_1m;
    }

    public void setLoadavg_1m(double loadavg_1m) {
        this.loadavg_1m = loadavg_1m;
    }

    public double getLoadavg_5m() {
        return loadavg_5m;
    }

    public void setLoadavg_5m(double loadavg_5m) {
        this.loadavg_5m = loadavg_5m;
    }

    public double getLoadavg_15m() {
        return loadavg_15m;
    }

    public void setLoadavg_15m(double loadavg_15m) {
        this.loadavg_15m = loadavg_15m;
    }

   

   
}
