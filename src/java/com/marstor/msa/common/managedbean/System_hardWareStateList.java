/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.model.CPU;
import com.marstor.msa.common.bean.DiskStatistics;
import com.marstor.msa.common.bean.NicStatistics;
import com.marstor.msa.common.model.NicStatisticsModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean(name = "system_hardWareStateList")
@RequestScoped
public class System_hardWareStateList implements Serializable{

    private List<CPU> cpuList;
    private CPU cpuState;
    private List<NicStatistics> nicList;
    private NicStatistics nicState;
    private List<DiskStatistics> diskList;
    private DiskStatistics diskState;
    private NicStatisticsModel nicsModel;
    private CartesianChartModel linearModel;
    private CartesianChartModel diskLinearModel;
    private static int i = 0;
    private static Map s1 = new HashMap();
    private static Map s2 = new HashMap();
    private static Map s3 = new HashMap();
    private static Map s4 = new HashMap();

    /**
     * Creates a new instance of System_hardWareStateList
     */
    public System_hardWareStateList() {
        createLinearModel();
        this.getLinearModel();
//        createDiskLinearModel();
//        getDiskLinearModel();

        this.getCPUStateList();
        this.getNicStateList();
        this.getDiskStateList();

        nicsModel = new NicStatisticsModel(nicList);
    }

    public CartesianChartModel getLinearModel() {
        return linearModel;
    }

    public CartesianChartModel getDiskLinearModel() {
        return diskLinearModel;
    }
    
    public CartesianChartModel updateModel(){
        List<ChartSeries> series = linearModel.getSeries();
        ChartSeries series1 = series.get(0);
        for (int m = 0; m < 10; m++) {
            series1.set(m, (int) (Math.random() * 10 + 30));
        }
        return linearModel;
    }

    private void createLinearModel() {
        linearModel = new CartesianChartModel();
        LineChartSeries data1 = new LineChartSeries();
        data1.setMarkerStyle("none");
        data1.setLabel("发送");
        for (int m = 0; m < 10; m++) {
            data1.set(m, (int) (Math.random() * 10 + 30));
        }
        linearModel.addSeries(data1);

//        linearModel.addSeries(getStockChartData("发送", 30, s1));
//        linearModel.addSeries(getStockChartData("接收", 20, s2));
    }

    private void createDiskLinearModel() {
        diskLinearModel = new CartesianChartModel();

        diskLinearModel.addSeries(getStockChartData("发送", 30, s3));
        diskLinearModel.addSeries(getStockChartData("接收", 20, s4));
    }

    private LineChartSeries getStockChartData(String label, int num, Map s) {
        LineChartSeries data = new LineChartSeries();
        data.setMarkerStyle("none");
        data.setLabel(label);
        if (i >= 100 && i < 200) {
            s.remove(i - 100);
        } else if (i >= 200) {
            s.remove((i % 100 + (100 * (i / 100 - 1))));
        }
        s.put(i++, (int) (Math.random() * 10 + num));
        data.setData(s);

        return data;
    }

    public void getCPUStateList() {
        cpuList = new ArrayList();

        cpuState = new CPU();
        cpuState.setId(0);
        cpuState.setCore_id(0);
        cpuState.setFreq(2400);
        cpuState.setCurrent_freq(1197);
        cpuState.setTemp(38);
        cpuState.setLoadavg_1m(0.25);
        cpuState.setLoadavg_5m(0.19);
        cpuState.setLoadavg_15m(0.29);
        cpuList.add(cpuState);

        cpuState = new CPU();
        cpuState.setId(0);
        cpuState.setCore_id(1);
        cpuState.setFreq(2400);
        cpuState.setCurrent_freq(1197);
        cpuState.setTemp(38);
        cpuState.setLoadavg_1m(0.25);
        cpuState.setLoadavg_5m(0.19);
        cpuState.setLoadavg_15m(0.29);
        cpuList.add(cpuState);

        cpuState = new CPU();
        cpuState.setId(0);
        cpuState.setCore_id(2);
        cpuState.setFreq(2400);
        cpuState.setCurrent_freq(1197);
        cpuState.setTemp(38);
        cpuState.setLoadavg_1m(0.25);
        cpuState.setLoadavg_5m(0.19);
        cpuState.setLoadavg_15m(0.29);
        cpuList.add(cpuState);

        cpuState = new CPU();
        cpuState.setId(0);
        cpuState.setCore_id(3);
        cpuState.setFreq(2400);
        cpuState.setCurrent_freq(1197);
        cpuState.setTemp(38);
        cpuState.setLoadavg_1m(0.25);
        cpuState.setLoadavg_5m(0.19);
        cpuState.setLoadavg_15m(0.29);
        cpuList.add(cpuState);
    }

    public void getNicStateList() {
        nicList = new ArrayList();

        nicState = new NicStatistics();
        nicState.setName("e100g0");
        nicState.setRead_KB_per_second(6978.0 + "");
        nicState.setWrite_KB_per_second(169.6 + "");
        nicState.setTotal_read_KB("619.67GB");
        nicState.setTotal_write_KB("153.93GB");
        nicList.add(nicState);

        nicState = new NicStatistics();
        nicState.setName("e100g1");
        nicState.setRead_KB_per_second(0.00 + "");
        nicState.setWrite_KB_per_second(0.00 + "");
        nicState.setTotal_read_KB("0.00KB");
        nicState.setTotal_write_KB("0.00KB");
        nicList.add(nicState);

        nicState = new NicStatistics();
        nicState.setName("ibp0.po");
        nicState.setRead_KB_per_second(0.04 + "");
        nicState.setWrite_KB_per_second(0.00 + "");
        nicState.setTotal_read_KB("42.08MB");
        nicState.setTotal_write_KB("25.74GB");
        nicList.add(nicState);

    }

    public void getDiskStateList() {
        diskList = new ArrayList();

        diskState = new DiskStatistics();
        diskState.setPoolName("SYSVOL");
        diskState.setDiskName("c0t50014EE6AAAFD7A5d0aaaaaaaaaaaaaaaaaa");
        diskState.setRead_per_cecond(0.0);
        diskState.setWrite_per_cecond(11.4);
        diskState.setKilobytes_read_per_second(0.0);
        diskState.setKilobytes_write_per_second(36.0);
        diskState.setTotal_error(1469);
        diskState.setTemperature(38);
        diskList.add(diskState);

        diskState = new DiskStatistics();
        diskState.setPoolName("SYSVOL");
        diskState.setDiskName("c0t50014EE0577BD284d0");
        diskState.setRead_per_cecond(514.0);
        diskState.setWrite_per_cecond(268.4);
        diskState.setKilobytes_read_per_second(17491.2);
        diskState.setKilobytes_write_per_second(26264.5);
        diskState.setTotal_error(1470);
        diskState.setTemperature(38);
        diskList.add(diskState);

        diskState = new DiskStatistics();
        diskState.setPoolName("SYSVOL");
        diskState.setDiskName("c0t50014EE6555DC7FFd0");
        diskState.setRead_per_cecond(352.0);
        diskState.setWrite_per_cecond(356.2);
        diskState.setKilobytes_read_per_second(16382.6);
        diskState.setKilobytes_write_per_second(32910.3);
        diskState.setTotal_error(1480);
        diskState.setTemperature(36);
        diskList.add(diskState);

        diskState = new DiskStatistics();
        diskState.setPoolName("tjxtest");
        diskState.setDiskName("c0t50014EE6555A7060d0");
        diskState.setRead_per_cecond(3.0);
        diskState.setWrite_per_cecond(6.8);
        diskState.setKilobytes_read_per_second(8.1);
        diskState.setKilobytes_write_per_second(9.4);
        diskState.setTotal_error(1943);
        diskState.setTemperature(34);
        diskList.add(diskState);
    }

    public NicStatisticsModel getNicsModel() {
        return nicsModel;
    }

    public void setNicsModel(NicStatisticsModel nicsModel) {
        this.nicsModel = nicsModel;
    }

    public List<CPU> getCpuList() {
        return cpuList;
    }

    public List<NicStatistics> getNicList() {
        return nicList;
    }

    public List<DiskStatistics> getDiskList() {
        return diskList;
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("NIC Edited", ((NicStatistics) event.getObject()).getName());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("NIC Cancelled", ((NicStatistics) event.getObject()).getName());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
