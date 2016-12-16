/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.model;

import com.marstor.msa.common.bean.AggregationInformation;
import com.marstor.msa.common.bean.NetConfigInformation;
import com.marstor.msa.common.bean.NicStatistics;
import com.marstor.msa.common.managedbean.SystemOutPrintln;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.dtrace.bean.IPMonitoringData;
import com.marstor.msa.dtrace.web.MonitorInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
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
public class NetCard implements Serializable {

    private String name;
    private String ip;
    private CartesianChartModel networkModel = new CartesianChartModel();
    private int x = 0;
    private java.util.Queue<NetCardData> transmitQueue = new LinkedList<NetCardData>();
    private java.util.Queue<NetCardData> receiveQueue = new LinkedList<NetCardData>();
    private double maxSpeed;
    private int maxY;
    private String minX;
    private MSAResource res = new MSAResource();
    private MonitorInterface monitor = InterfaceFactory.getMonitorInterfaceInstance();
    private int interval = 3;

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
    public NetCard() {
    }

    public NetCard(NetConfigInformation nic) {
        this.name = nic.name;
        this.ip = nic.address;
    }

    public NetCard(AggregationInformation nic) {
        this.name = nic.getName();
        this.ip = nic.getAddress();
    }

    @Override
    public String toString() {
        String data = this.ip + "[" + this.name + "]";
        return data;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public final void initSeries(IPMonitoringData ipmd) {
        this.x = 0;
        transmitQueue = new LinkedList<NetCardData>();
        receiveQueue = new LinkedList<NetCardData>();
        this.networkModel = new CartesianChartModel();
        if (Integer.parseInt(ipmd.getSend_KB_per_second()) > Integer.parseInt(ipmd.getReceive_KB_per_second())) {
            this.maxSpeed = Double.parseDouble(ipmd.getSend_KB_per_second()) / 1024;
        } else {
            this.maxSpeed = Double.parseDouble(ipmd.getReceive_KB_per_second()) / 1024;
        }
        this.maxY = (int) (this.maxSpeed * 10.0 / 7.0);
        String time = this.getCurrentTime();
        NetCardData data;
        ChartSeries series1, series2;

        data = new NetCardData(time, Double.parseDouble(ipmd.getSend_KB_per_second()) / 1024);
        this.transmitQueue.offer(data);
        series1 = new ChartSeries(res.get("transmit"));
        series1.set(time, data.getSpeed());

        data = new NetCardData(time, Double.parseDouble(ipmd.getReceive_KB_per_second()) / 1024);
        this.receiveQueue.offer(data);

        series2 = new ChartSeries(res.get("receive"));
        series2.set(time, data.getSpeed());

        if (time != null) {
            String strArray[] = time.trim().split(":");

            if (strArray.length > 2) {
                String hour = strArray[0], minute = strArray[1], second = strArray[2];

                for (int i = 0; i < 9; i++) {
                    second = String.valueOf(Integer.parseInt(second) + this.interval);
                    if (Integer.parseInt(second) >= 60) {
                        minute = String.valueOf(Integer.parseInt(minute) + 1);
                        second = "0" + String.valueOf(Integer.parseInt(second) - 60);
                        if (Integer.parseInt(minute) == 60) {
                            minute = "00";
                            hour = String.valueOf(Integer.parseInt(hour) + 1);
                            if (Integer.parseInt(hour) == 24) {
                                hour = "00";
                            } else if (Integer.parseInt(hour) < 10) {
                                hour = "0" + String.valueOf(Integer.parseInt(hour));
                            }
                        } else if (Integer.parseInt(minute) < 10) {
                            minute = "0" + String.valueOf(Integer.parseInt(minute));
                        }
                    } else if (Integer.parseInt(second) < 10) {
                        second = "0" + String.valueOf(Integer.parseInt(second));
                    }
                    
                    time = hour + ":" + minute + ":" + second;
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

    public int getMaxY() {
        if (maxY == 0) {
            return 1;
        }
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
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

    public void updateModel() {
        String time = this.getCurrentTime();
        SystemOutPrintln.print_common(" current time: " + time);
        NetCardData data;

        //如果已经获取到了10个时间点的数据，则需要删除队列中第一个值，然后添加一个新值，最后根据队列的值刷新ChartSeries
        if (x > 9) {
            SystemOutPrintln.print_common("xValue > 10");
            this.transmitQueue.poll();
            this.receiveQueue.poll();
            IPMonitoringData ipmd = monitor.getIPMonitoringData(ip);
            System.out.println("IP;" + ip);
            System.out.println("Send;" + Double.parseDouble(ipmd.getSend_KB_per_second()) / 1024);
            System.out.println("Receive;" + Double.parseDouble(ipmd.getReceive_KB_per_second()) / 1024);
            data = new NetCardData(time, Double.parseDouble(ipmd.getSend_KB_per_second()) / 1024);
            //data.setSpeed(Double.parseDouble(nic.write_KB_per_second));
            this.transmitQueue.offer(data);
            data = new NetCardData(time, Double.parseDouble(ipmd.getReceive_KB_per_second()) / 1024);
            //  data.setSpeed(Double.parseDouble(nic.read_KB_per_second));
            this.receiveQueue.offer(data);
            resetMaxY();
            this.updateChartSeries();

        } else {
            IPMonitoringData ipmd = monitor.getIPMonitoringData(ip);
            System.out.println("IP;" + ip);
            System.out.println("Send;" + Double.parseDouble(ipmd.getSend_KB_per_second()) / 1024);
            System.out.println("Receive;" + Double.parseDouble(ipmd.getReceive_KB_per_second()) / 1024);
            //如果没有获取到10个时间点的数据，则需要通过计算向ChartSeries中添加值（纵坐标值为null）
            data = new NetCardData(time, Double.parseDouble(ipmd.getSend_KB_per_second()) / 1024);
            // data.setSpeed(Double.parseDouble(nic.write_KB_per_second));
            this.transmitQueue.offer(data);
            data = new NetCardData(time, Double.parseDouble(ipmd.getReceive_KB_per_second()) / 1024);
            // data.setSpeed(Double.parseDouble(nic.read_KB_per_second));
            this.receiveQueue.offer(data);
            if (this.networkModel.getSeries().size() < 2) {
                SystemOutPrintln.print_common("Series size less than 2.");
                return;
            }
            ChartSeries transmit = this.networkModel.getSeries().get(0);
            ChartSeries recieve = this.networkModel.getSeries().get(1);
            String strArray[] = time.trim().split(":");
            if (strArray.length > 2) {
                String hour = strArray[0], minute = strArray[1], second = strArray[2];
                //int second = Integer.parseInt(strArray[2]) + 5;
                for (int i = x; i < 9; i++) {
                    second = String.valueOf(Integer.parseInt(second) + this.interval);
                    if (Integer.parseInt(second) >= 60) {
                        minute = String.valueOf(Integer.parseInt(minute) + 1);
                        second = "0" + String.valueOf(Integer.parseInt(second) - 60);
                        if (Integer.parseInt(minute) == 60) {
                            minute = "00";
                            hour = String.valueOf(Integer.parseInt(hour) + 1);
                            if (Integer.parseInt(hour) == 24) {
                                hour = "00";
                            } else if (Integer.parseInt(hour) < 10) {
                                hour = "0" + String.valueOf(Integer.parseInt(hour));
                            }
                        } else if (Integer.parseInt(minute) < 10) {
                            minute = "0" + String.valueOf(Integer.parseInt(minute));
                        }
                    } else if (Integer.parseInt(second) < 10) {
                        second = "0" + String.valueOf(Integer.parseInt(second));
                    }
                    time = hour + ":" + minute + ":" + second;
                    transmit.set(time, null);
                    recieve.set(time, null);
                }
            }
            resetMaxY();
            this.updateChartSeries();
            x++;
        }
    }

    public void updateModel(IPMonitoringData ipmd) {
        String time = this.getCurrentTime();
        SystemOutPrintln.print_common(" current time: " + time);
        NetCardData data;

        //如果已经获取到了10个时间点的数据，则需要删除队列中第一个值，然后添加一个新值，最后根据队列的值刷新ChartSeries
        if (x > 9) {
            SystemOutPrintln.print_common("xValue > 10");
            this.transmitQueue.poll();
            this.receiveQueue.poll();
            System.out.println("IP;" + ip);
            System.out.println("Send;" + Double.parseDouble(ipmd.getSend_KB_per_second()) / 1024);
            System.out.println("Receive;" + Double.parseDouble(ipmd.getReceive_KB_per_second()) / 1024);
            data = new NetCardData(time, Double.parseDouble(ipmd.getSend_KB_per_second()) / 1024);
            //data.setSpeed(Double.parseDouble(nic.write_KB_per_second));
            this.transmitQueue.offer(data);
            data = new NetCardData(time, Double.parseDouble(ipmd.getReceive_KB_per_second()) / 1024);
            //  data.setSpeed(Double.parseDouble(nic.read_KB_per_second));
            this.receiveQueue.offer(data);
            resetMaxY();
            this.updateChartSeries();
        } else {
            System.out.println("IP;" + ip);
            System.out.println("Send;" + Double.parseDouble(ipmd.getSend_KB_per_second()) / 1024);
            System.out.println("Receive;" + Double.parseDouble(ipmd.getReceive_KB_per_second()) / 1024);
            //如果没有获取到10个时间点的数据，则需要通过计算向ChartSeries中添加值（纵坐标值为null）
            data = new NetCardData(time, Double.parseDouble(ipmd.getSend_KB_per_second()) / 1024);
            // data.setSpeed(Double.parseDouble(nic.write_KB_per_second));
            this.transmitQueue.offer(data);
            data = new NetCardData(time, Double.parseDouble(ipmd.getReceive_KB_per_second()) / 1024);
            // data.setSpeed(Double.parseDouble(nic.read_KB_per_second));
            this.receiveQueue.offer(data);
            resetMaxY();
            this.updateChartSeries();
            x++;
        }
    }

    public void resetMaxY() {
        double max = 0;
        Iterator<NetCardData> trans = this.transmitQueue.iterator();
        while (trans.hasNext()) {
            double speed = trans.next().getSpeed();
            System.out.println("Transmit speed:" + speed);
            if (speed > max) {
                max = speed;
            }
        }
        System.out.println("Max speed 1:" + this.maxSpeed);
        trans = this.receiveQueue.iterator();
        while (trans.hasNext()) {
            double speed = trans.next().getSpeed();
            System.out.println("Receive speed:" + speed);
            if (speed > max) {
                max = speed;
            }
        }
//        System.out.println("Max speed 2:" + this.maxSpeed);
//        if (Integer.parseInt(ipmd.getSend_KB_per_second()) > Integer.parseInt(ipmd.getReceive_KB_per_second())) {
//            temp = Double.parseDouble(ipmd.getSend_KB_per_second()) / 1024;
//        } else {
//            temp = Double.parseDouble(ipmd.getReceive_KB_per_second()) / 1024;
//        }
        this.maxSpeed = max;
        System.out.println("Max speed 3:" + this.maxSpeed);
        this.maxY = (int) (this.maxSpeed * 10.0 / 7.0);
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
        System.out.println("*******Length of TransmitQueue:" + transmitQueue.size());
        int i = 0;
        String firstTime = "";
        String time;
        String strArray[];
        String hour = "";
        String minute = "";
        String second = "";
        while (it.hasNext()) {
            NetCardData cardData = it.next();
            if (i == 0) {
                firstTime = cardData.getCurrentTime();
                strArray = firstTime.trim().split(":");
                hour = strArray[0];
                minute = strArray[1];
                second = strArray[2];
                transmit.set(firstTime, cardData.getSpeed());
                SystemOutPrintln.print_common("transmit speed" + cardData.getSpeed());
                SystemOutPrintln.print_common("transmit time " + firstTime);
            } else {
                second = String.valueOf(Integer.parseInt(second) + this.interval);
                if (Integer.parseInt(second) >= 60) {
                    minute = String.valueOf(Integer.parseInt(minute) + 1);
                    second = "0" + String.valueOf(Integer.parseInt(second) - 60);
                    if (Integer.parseInt(minute) == 60) {
                        minute = "00";
                        hour = String.valueOf(Integer.parseInt(hour) + 1);
                        if (Integer.parseInt(hour) == 24) {
                            hour = "00";
                        } else if (Integer.parseInt(hour) < 10) {
                            hour = "0" + String.valueOf(Integer.parseInt(hour));
                        }
                    } else if (Integer.parseInt(minute) < 10) {
                        minute = "0" + String.valueOf(Integer.parseInt(minute));
                    }
                } else if (Integer.parseInt(second) < 10) {
                    second = "0" + String.valueOf(Integer.parseInt(second));
                }
                time = hour + ":" + minute + ":" + second;
                transmit.set(time, cardData.getSpeed());
            }
            i++;
        }
        if (i < 10) {
            for (int j = i; j < 10; j++) {
                second = String.valueOf(Integer.parseInt(second) + this.interval);
                if (Integer.parseInt(second) >= 60) {
                    minute = String.valueOf(Integer.parseInt(minute) + 1);
                    second = "0" + String.valueOf(Integer.parseInt(second) - 60);
                    if (Integer.parseInt(minute) == 60) {
                        minute = "00";
                        hour = String.valueOf(Integer.parseInt(hour) + 1);
                        if (Integer.parseInt(hour) == 24) {
                            hour = "00";
                        } else if (Integer.parseInt(hour) < 10) {
                            hour = "0" + String.valueOf(Integer.parseInt(hour));
                        }
                    } else if (Integer.parseInt(minute) < 10) {
                        minute = "0" + String.valueOf(Integer.parseInt(minute));
                    }
                } else if (Integer.parseInt(second) < 10) {
                    second = "0" + String.valueOf(Integer.parseInt(second));
                }
                time = hour + ":" + minute + ":" + second;
                transmit.set(time, null);
            }
        }
        i = 0;
        it = receiveQueue.iterator();
        if (it == null) {
            return;
        }
        System.out.println("*******Length of receiveQueue:" + receiveQueue.size());
        while (it.hasNext()) {
            NetCardData cardData = it.next();
            if (i == 0) {
                strArray = firstTime.trim().split(":");
                hour = strArray[0];
                minute = strArray[1];
                second = strArray[2];
                recieve.set(firstTime, cardData.getSpeed());
                SystemOutPrintln.print_common("recieve speed" + cardData.getSpeed());
                SystemOutPrintln.print_common("recieve time " + firstTime);
            } else {
                second = String.valueOf(Integer.parseInt(second) + this.interval);
                if (Integer.parseInt(second) >= 60) {
                    minute = String.valueOf(Integer.parseInt(minute) + 1);
                    second = "0" + String.valueOf(Integer.parseInt(second) - 60);
                    if (Integer.parseInt(minute) == 60) {
                        minute = "00";
                        hour = String.valueOf(Integer.parseInt(hour) + 1);
                        if (Integer.parseInt(hour) == 24) {
                            hour = "00";
                        } else if (Integer.parseInt(hour) < 10) {
                            hour = "0" + String.valueOf(Integer.parseInt(hour));
                        }
                    } else if (Integer.parseInt(minute) < 10) {
                        minute = "0" + String.valueOf(Integer.parseInt(minute));
                    }
                } else if (Integer.parseInt(second) < 10) {
                    second = "0" + String.valueOf(Integer.parseInt(second));
                }
                time = hour + ":" + minute + ":" + second;
                recieve.set(time, cardData.getSpeed());
            }
            i++;
        }
        if (i < 10) {
            for (int j = i; j < 10; j++) {
                second = String.valueOf(Integer.parseInt(second) + this.interval);
                if (Integer.parseInt(second) >= 60) {
                    minute = String.valueOf(Integer.parseInt(minute) + 1);
                    second = "0" + String.valueOf(Integer.parseInt(second) - 60);
                    if (Integer.parseInt(minute) == 60) {
                        minute = "00";
                        hour = String.valueOf(Integer.parseInt(hour) + 1);
                        if (Integer.parseInt(hour) == 24) {
                            hour = "00";
                        } else if (Integer.parseInt(hour) < 10) {
                            hour = "0" + String.valueOf(Integer.parseInt(hour));
                        }
                    } else if (Integer.parseInt(minute) < 10) {
                        minute = "0" + String.valueOf(Integer.parseInt(minute));
                    }
                } else if (Integer.parseInt(second) < 10) {
                    second = "0" + String.valueOf(Integer.parseInt(second));
                }
                time = hour + ":" + minute + ":" + second;
                recieve.set(time, null);
            }
        }
    }
}
