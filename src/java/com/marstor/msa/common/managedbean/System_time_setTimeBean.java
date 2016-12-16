/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class System_time_setTimeBean implements Serializable {

    private String dateString;
    private Date date;  
    public int time;
    private MSAResource res = new MSAResource();
    private String basename = "common.system_time";
    

    /**
     * Creates a new instance of System_time_setTimeBean
     */
    public System_time_setTimeBean() {
        this.currentTime();
    }
    public void currentTime() {
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        Date sys = common.getSystemTime();
        date = sys;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateString = formatter.format(sys);
      
//        String name = common.getCurrentTimeZone();
//        
//
//        System.out.println("Locale.getDefault() marstorZone="+ Locale.getDefault()+",name="+name);
//        TimeZone tz = TimeZone.getTimeZone(name);
        TimeZone tz = TimeZone.getDefault();
        time = tz.getRawOffset();
         SystemOutPrintln.print_common("tz="+ tz);
         SystemOutPrintln.print_common("time="+time);

    }

    public String getDateString() {
         
         SystemOutPrintln.print_common("getDateString"+ dateString);
        
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getDateTime() {
       
         SystemOutPrintln.print_common("getDateTime()"+this.date.getTime());
        return this.date.getTime();

    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

 

    
    //本地时间转化为火星舱时间
    public static String local2Marstor(String utcTime, String utcTimePatten,String localTimePatten) {
         Locale marstorZone = Locale.getDefault();
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        String name = common.getCurrentTimeZone();
        
           SystemOutPrintln.print_common("Locale.getDefault() marstorZone="+ Locale.getDefault());
        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }
    
    
    public void saveTime(Date datetime) {
         SystemOutPrintln.print_common("saveTime"+datetime.toString());
         SystemOutPrintln.print_common("datetime"+datetime.getTime());
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = formatter.format(datetime);
//        System.out.println("*******************time=" + time);
        
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
        boolean setTine = common.setSystemTime(datetime);
        if(setTine == true){
              SystemOutPrintln.print_common("set time succeed" );
             currentTime();
               RequestContext.getCurrentInstance().addCallbackParam("dateTime", this.getDateTime());
                RequestContext.getCurrentInstance().addCallbackParam("istrue", "true");
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "settine_ok"),  ""));

        }else{
             SystemOutPrintln.print_common("set time fail" );
              RequestContext.getCurrentInstance().addCallbackParam("istrue", "false");
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "settine_fail"),  ""));
        }  
    }
    
    
    
    
}
