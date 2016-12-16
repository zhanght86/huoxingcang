/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class System_time_timeZoneBean implements Serializable {

    private List<String> zoneList;
//    private TimeZone zone;
    private String zoneName;
    private String names[] = null;
    private MSAResource res = new MSAResource();
    private String basename = "common.system_time";
    private String accordionActive1 = "";

    /**
     * Creates a new instance of System_time_timeZoneBean
     */
    public System_time_timeZoneBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
        accordionActive1 = request.getParameter("accordionActive1");
        if (accordionActive1 == null || accordionActive1.equals("")) {
            accordionActive1 = "0";
        }
        
        this.getTimeZones();
        
    }
    
    public void getTimeZones(){
        zoneList = new ArrayList();
        CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
//        List<String>  timeZoneList= common.getAvailableTimeZone();
        ArrayList<String>  timeZoneList= MyTimeZone.getTimeZoneStrings();
        if(timeZoneList != null){
            zoneList = timeZoneList;
        }
        String name = common.getCurrentTimeZone();
         SystemOutPrintln.print_common("zoneName=" + name);
        if (name != null) {
            if(MyTimeZone.getZoneIndex(name) == -1){
               this.zoneName =  MyTimeZone.getStringValueZone("PRC");
            }else{
                this.zoneName = MyTimeZone.getStringValueZone(name);
            }
            SystemOutPrintln.print_common("name=" + name);
            SystemOutPrintln.print_common("zoneName" + zoneName);
            
        }

        
//        names = new String[] {"PRC(中国标准时间)","PST8PDT（太平洋标准时间）","Pacific/Apia(西萨摩亚时间)"};
//        for(int i=0; i<names.length; i++){
//            zoneList.add(names[i]);
//        }
        
    }
//
//    public List<TimeZone> getZoneList() {
//        return zoneList;
//    }
//
//    public void setZoneList(List<TimeZone> zoneList) {
//        this.zoneList = zoneList;
//    }
//
//    public TimeZone getZone() {
//        return zone;
//    }
//
//    public void setZone(TimeZone zone) {
//        this.zone = zone;
//    }

    public List<String> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<String> zoneList) {
        this.zoneList = zoneList;
    }

    public String getZoneName() {
        return zoneName;
    }
    
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
    
    public void saveTimeZone(String strTime){
         CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
         SystemOutPrintln.print_common("strTime="+strTime);
         SystemOutPrintln.print_common(MyTimeZone.getZoneFromZoneString(strTime));
        boolean timeZone = common.setTimeZone(MyTimeZone.getZoneFromZoneString(strTime));
//        timeZone = false;
        if(timeZone == true){
            this.getTimeZones();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get(basename, "timezone_ok"),  ""));
             RequestContext.getCurrentInstance().execute("reboottip.show()");
        }else{
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get(basename, "timezone_fail"),  ""));
        }
//        zoneName = strTime;
//        
//        System.out.println("1231111111111111111111111111111="+strTime);
//        System.out.println("zoneName="+zoneName);
    }
    
    public void reboot(){
         RequestContext.getCurrentInstance().execute("window.top.location.href='../volume/reboot.xhtml'");
    }
    
    public String cancle(){
      return "system_time?faces-redirect=true&amp;accordionActive1=1" ; 
    }

    public String getAccordionActive1() {
        return accordionActive1;
    }

    public void setAccordionActive1(String accordionActive1) {
        this.accordionActive1 = accordionActive1;
    }
    
}
