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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean(name = "networkBean")
@RequestScoped
public class NetworkBean implements Serializable {

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
    public NetworkBean() {
        createLinearModel();
        this.getLinearModel();

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
        
        ChartSeries series2 = series.get(1);
        for (int m = 0; m < 10; m++) {
            series2.set(m, (int) (Math.random() * 10 + 30));
        }
        return linearModel;
    }

    private void createLinearModel() {
        linearModel = new CartesianChartModel();
        LineChartSeries data1 = new LineChartSeries();
        data1.setMarkerStyle("none");
        data1.setLabel("·¢ËÍ");
        for (int m = 0; m < 10; m++) {
            data1.set(m, (int) (Math.random() * 10 + 30));
        }
        linearModel.addSeries(data1);
        
        LineChartSeries data2 = new LineChartSeries();
        data2.setMarkerStyle("none");
        data2.setLabel("½ÓÊÕ");
        for (int m = 0; m < 10; m++) {
            data2.set(m, (int) (Math.random() * 10 + 30));
        }
        linearModel.addSeries(data2);

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
