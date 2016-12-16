/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.common.bean;

import com.marstor.msa.bean.IMarsXMLBean;
import com.marstor.msa.common.bean.DiskStatistics;
import com.marstor.msa.common.bean.PhysicalDiskInformation;
import com.marstor.xml.XMLConstructor;
import com.marstor.xml.XMLParser;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class Disk
{
    public double read_per_cecond  = 0.0;           //每秒读操作数
    public double write_per_cecond  = 0.0;          //每秒写操作数
    public double kilobytes_read_per_second = 0.0;  //每秒写KB
    public double kilobytes_write_per_second = 0.0; //每秒读KB
    public double wait = 0.0;                       //保留
    public double actv = 0.0;                       //保留
    public double wsvc_t = 0.0;                     //保留
    public double asvc_t = 0.0;                     //保留
    public double wait_percent = 0.0;               //空闲百分比
    public double busy_percent = 0.0;               //忙时百分比 
    public int soft_error = 0;                      //软件错误
    public int hard_error = 0;                      //硬件件错误
    public int transport_error = 0;                 //传输错误
    public int total_error = 0;                     //总错误
    public int temperature = -1;                     //温度

    public Disk() {
        
    }   

    public Disk(DiskStatistics origin) {
        read_per_cecond = origin.read_per_cecond;
        write_per_cecond = origin.write_per_cecond;
        kilobytes_read_per_second = origin.kilobytes_read_per_second;
        kilobytes_write_per_second = origin.kilobytes_write_per_second;
        wait = origin.wait;
        actv = origin.actv;
    }

    public double getRead_per_cecond()
    {
        return read_per_cecond;
    }

    public void setRead_per_cecond(double read_per_cecond)
    {
        this.read_per_cecond = read_per_cecond;
    }

    public double getWrite_per_cecond()
    {
        return write_per_cecond;
    }

    public void setWrite_per_cecond(double write_per_cecond)
    {
        this.write_per_cecond = write_per_cecond;
    }

    public double getKilobytes_read_per_second()
    {
        return kilobytes_read_per_second;
    }

    public void setKilobytes_read_per_second(double kilobytes_read_per_second)
    {
        this.kilobytes_read_per_second = kilobytes_read_per_second;
    }

    public double getKilobytes_write_per_second()
    {
        return kilobytes_write_per_second;
    }

    public void setKilobytes_write_per_second(double kilobytes_write_per_second)
    {
        this.kilobytes_write_per_second = kilobytes_write_per_second;
    }

    public double getWait()
    {
        return wait;
    }

    public void setWait(double wait)
    {
        this.wait = wait;
    }

    public double getActv()
    {
        return actv;
    }

    public void setActv(double actv)
    {
        this.actv = actv;
    }

    public double getWsvc_t()
    {
        return wsvc_t;
    }

    public void setWsvc_t(double wsvc_t)
    {
        this.wsvc_t = wsvc_t;
    }

    public double getAsvc_t()
    {
        return asvc_t;
    }

    public void setAsvc_t(double asvc_t)
    {
        this.asvc_t = asvc_t;
    }

    public double getWait_percent()
    {
        return wait_percent;
    }

    public void setWait_percent(double wait_percent)
    {
        this.wait_percent = wait_percent;
    }

    public double getBusy_percent()
    {
        return busy_percent;
    }

    public void setBusy_percent(double busy_percent)
    {
        this.busy_percent = busy_percent;
    }

    public int getSoft_error()
    {
        return soft_error;
    }

    public void setSoft_error(int soft_error)
    {
        this.soft_error = soft_error;
    }

    public int getHard_error()
    {
        return hard_error;
    }

    public void setHard_error(int hard_error)
    {
        this.hard_error = hard_error;
    }

    public int getTransport_error()
    {
        return transport_error;
    }

    public void setTransport_error(int transport_error)
    {
        this.transport_error = transport_error;
    }

    public int getTotal_error()
    {
        return total_error;
    }

    public void setTotal_error(int total_error)
    {
        this.total_error = total_error;
    }

    public int getTemperature()
    {
        return temperature;
    }

    public void setTemperature(int temperature)
    {
        this.temperature = temperature;
    }

   
    
    
    
}
