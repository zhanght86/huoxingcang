/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean(name = "thirdTabBean")
@RequestScoped
public class ThirdTabBean {

    private CartesianChartModel linearModel;
    private static int i = 0;
    private static Map s1 = new HashMap();
    private static Map s2 = new HashMap();
    private static Map s3 = new HashMap();
    private static Map s4 = new HashMap();

    /**
     * Creates a new instance of System_hardWareStateList
     */
    public ThirdTabBean() {
        createLinearModel();
        getLinearModel();

    }


    public CartesianChartModel getLinearModel() {
        return linearModel;
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
//        LineChartSeries data1 = new LineChartSeries();
//        data1.setMarkerStyle("none");
//        data1.setLabel("发送");
//        for (int m = 0; m < 10; m++) {
//            data1.set(m, (int) (Math.random() * 10 + 30));
//        }
//        linearModel.addSeries(data1);

        linearModel.addSeries(getStockChartData("发送", 30, s1));
        linearModel.addSeries(getStockChartData("接收", 20, s2));
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
}
