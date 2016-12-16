/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.bean.NicStatistics;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Administrator
 */
public class NetCard_bak implements Serializable{

    private String name;
    private CartesianChartModel networkModel = new CartesianChartModel();
    private int x = 0;
    private java.util.Queue<NetCardData> transmitQueue = new LinkedList<NetCardData>();
    private java.util.Queue<NetCardData> receiveQueue = new LinkedList<NetCardData>();
    private int maxSpeed;
    private String minX;
    private MSAResource res = new MSAResource();

    //  Queue<String> queue = new LinkedList<String>();
    //    public NetworkCard(String name, String read_KB_per_second, String write_KB_per_second) {
    //        this.name = name;
    //        ChartSeries series;
    //        series = new ChartSeries("发送");
    //        series.set(xValue, Double.parseDouble(write_KB_per_second));
    //
    //        networkModel.addSeries(series);
    //
    //        series = new ChartSeries("接收");
    //        series.set(xValue, Double.parseDouble(read_KB_per_second));
    //        networkModel.addSeries(series);
    //    }
    //    }
    public NetCard_bak() {
    }

    public NetCard_bak(NicStatistics nic) {
        this.name = nic.name;
        this.maxSpeed = nic.NIC_speed_MB / 10;

        String time = this.getCurrentTime();
        NetCardData data;
        data = new NetCardData(time, Double.parseDouble(nic.write_KB_per_second) / 1024);
        this.transmitQueue.offer(data);
        data = new NetCardData(time, Double.parseDouble(nic.read_KB_per_second) / 1024);
        this.receiveQueue.offer(data);
        ChartSeries series1, series2;
        series1 = new ChartSeries(res.get("transmit"));
        series1.set(time, Double.parseDouble(nic.write_KB_per_second) / 1024);

        series2 = new ChartSeries(res.get("receive"));
        series2.set(time, Double.parseDouble(nic.read_KB_per_second) / 1024);

        if (time != null) {
            String strArray[] = time.trim().split(":");

            if (strArray.length > 2) {
                String hour = strArray[0], mininute = strArray[1], second = strArray[2];

                for (int i = 0; i < 9; i++) {
                    second = String.valueOf(Integer.parseInt(second) + 5);
                    if (Integer.parseInt(second) >= 60) {
                        second = "0" + String.valueOf(Integer.parseInt(second) + 5 - 60);
                        if ((Integer.parseInt(mininute) + 1) == 60) {
                            mininute = "00";
                            if (Integer.parseInt(hour) + 1 == 25) {
                                hour = "01";
                            }
                        }
                    }
                    time = hour + ":" + mininute + ":" + second;
                    series1.set(time, null);
                    series2.set(time, null);
                }
            }
        }
        //   this.minX = time;
        networkModel.addSeries(series1);
        networkModel.addSeries(series2);
        x++;

    }

    public String getMinX() {
        return minX;
    }

    public void setMinX(String minX) {
        this.minX = minX;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CartesianChartModel getNetworkModel() {
        return networkModel;
    }

    public void setNetworkModel(CartesianChartModel networkModel) {
        this.networkModel = networkModel;
    }

    public int getxValue() {
        return x;
    }

    public void setxValue(int xValue) {
        this.x = xValue;
    }

    public void updateModel(NicStatistics nic) {

        String time = this.getCurrentTime();
        SystemOutPrintln.print_common(" current time: " + time);
        NetCardData data = new NetCardData();
        //data.setCurrentTime(time);
//        if (this.networkModel.getSeries().size() < 2) {
//            System.out.println("Series size less than 2.");
//            return;
//        }
//        ChartSeries transmit = this.networkModel.getSeries().get(0);
//        ChartSeries recieve = this.networkModel.getSeries().get(1);
        //如果已经获取到了10个时间点的数据，则需要删除队列中第一个值，然后添加一个新值，最后根据队列的值刷新ChartSeries
        if (x > 9) {
            SystemOutPrintln.print_common("xValue > 10");
//
//            transmit.setData(new LinkedHashMap<Object, Number>());
//            recieve.setData(new LinkedHashMap<Object, Number>());
            this.transmitQueue.poll();
            this.receiveQueue.poll();
            data = new NetCardData(time, Double.parseDouble(nic.write_KB_per_second) / 1024);
            //data.setSpeed(Double.parseDouble(nic.write_KB_per_second));
            this.transmitQueue.offer(data);
            data = new NetCardData(time, Double.parseDouble(nic.read_KB_per_second) / 1024);
            //  data.setSpeed(Double.parseDouble(nic.read_KB_per_second));
            this.receiveQueue.offer(data);

            this.updateChartSeries();

        } else {
            //如果没有获取到10个时间点的数据，则需要通过计算向ChartSeries中添加值（纵坐标值为null）
            data = new NetCardData(time, Double.parseDouble(nic.write_KB_per_second) / 1024);
            // data.setSpeed(Double.parseDouble(nic.write_KB_per_second));
            this.transmitQueue.offer(data);
            data = new NetCardData(time, Double.parseDouble(nic.read_KB_per_second) / 1024);
            // data.setSpeed(Double.parseDouble(nic.read_KB_per_second));
            this.receiveQueue.offer(data);
            this.updateChartSeries();
            if (this.networkModel.getSeries().size() < 2) {
                SystemOutPrintln.print_common("Series size less than 2.");
                return;
            }
            ChartSeries transmit = this.networkModel.getSeries().get(0);
            ChartSeries recieve = this.networkModel.getSeries().get(1);
            String strArray[] = time.trim().split(":");
            if (strArray.length > 2) {
                String hour = strArray[0], mininute = strArray[1], second = strArray[2];
                //int second = Integer.parseInt(strArray[2]) + 5;
                for (int i = x; i < 9; i++) {
                    second = String.valueOf(Integer.parseInt(second) + 5);
                    if (Integer.parseInt(second) >= 60) {
                        second = "0" + String.valueOf(Integer.parseInt(second) + 5 - 60);
                        if ((Integer.parseInt(mininute) + 1) == 60) {
                            mininute = "00";
                            if (Integer.parseInt(hour) + 1 == 25) {
                                hour = "01";
                            }
                        }
                    }
                    time = hour + ":" + mininute + ":" + second;
                    transmit.set(time, null);
                    recieve.set(time, null);
                }
            }
            x++;
        }
    }

    public String getCurrentTime() {
        //获取当前时间
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        Date date = common.getSystemTime();
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateStr = formatter.format(date);

        return dateStr;
    }

    public void updateChartSeries() {
        if (this.networkModel.getSeries().size() < 2) {
            SystemOutPrintln.print_common("Series size less than 2.");
            return;
        }
        ChartSeries transmit = this.networkModel.getSeries().get(0);
        ChartSeries recieve = this.networkModel.getSeries().get(1);
        transmit.setData(new LinkedHashMap<Object, Number>());
        recieve.setData(new LinkedHashMap<Object, Number>());
        Iterator<NetCardData> it;
        it = transmitQueue.iterator();
        if (it == null) {
            return;
        }
        //int i = 0;
        while (it.hasNext()) {
            NetCardData cardData = it.next();
            SystemOutPrintln.print_common("transmit speed" + cardData.getSpeed());
            SystemOutPrintln.print_common("transmit time " + cardData.getCurrentTime());
            transmit.set(cardData.getCurrentTime(), cardData.getSpeed());
            //i++;
        }
        //i = 0;
        it = receiveQueue.iterator();
        if (it == null) {
            return;
        }
        while (it.hasNext()) {
            NetCardData cardData = it.next();
            SystemOutPrintln.print_common("resieve speed" + cardData.getSpeed());
            SystemOutPrintln.print_common("resieve time" + cardData.getCurrentTime());
            recieve.set(cardData.getCurrentTime(), cardData.getSpeed());
        }

    }
}
