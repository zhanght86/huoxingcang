package com.marstor.msa.common.managedbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.marstor.msa.common.util.MSAResource;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author tianwenbo
 */
public class MyTimeZone implements Serializable{
    private static MSAResource res = new MSAResource();
    private static String basename = "common.system_time";

    private static final  ArrayList<String> zone = new  ArrayList<String>() {
        
        {            
            add("Etc/GMT+12");
            add("Etc/GMT+11");
            add("Etc/GMT+10");
            add( "Etc/GMT+9");
            add("Etc/GMT+8");
            add("Etc/GMT+7");
            add("Etc/GMT+6");
            add("Etc/GMT+5");
            add("Etc/GMT+4");
            add("Etc/GMT+3");
            add("Etc/GMT+2");
            add("Etc/GMT+1");
            add("UTC");
            add("Etc/GMT-1");
            add("Etc/GMT-2");
            add("Etc/GMT-3");
            add("Etc/GMT-4");
            add("Etc/GMT-5");
            add("Etc/GMT-6");
            add("Etc/GMT-7");
            add("PRC");
            add("Etc/GMT-9");
            add("Etc/GMT-10");
            add("Etc/GMT-11");
            add("Etc/GMT-12");            
        }
    };
    
   private static final ArrayList<String> zoneString = new ArrayList<String>() {
        
        {
            add(res.get(basename, "GMT+12"));
            add(res.get(basename, "GMT+11"));
            add(res.get(basename, "GMT+10"));
            add(res.get(basename, "GMT+9"));
            add(res.get(basename, "GMT+8"));
            add(res.get(basename, "GMT+7"));
            add(res.get(basename, "GMT+6"));
            add(res.get(basename, "GMT+5"));
            add(res.get(basename, "GMT+4"));
            add(res.get(basename, "GMT+3"));
            add(res.get(basename, "GMT+2"));
            add(res.get(basename, "GMT+1"));
            add(res.get(basename, "UTC"));
            add(res.get(basename, "GMT-1"));
            add(res.get(basename, "GMT-2"));
            add(res.get(basename, "GMT-3"));
            add(res.get(basename, "GMT-4"));
            add(res.get(basename, "GMT-5"));
            add(res.get(basename, "GMT-6"));
            add(res.get(basename, "GMT-7"));
            add(res.get(basename, "PRC"));
            add(res.get(basename, "GMT-9"));
            add(res.get(basename, "GMT-10"));
            add(res.get(basename, "GMT-11"));
            add(res.get(basename, "GMT-12"));
            
        }
    };

    public static ArrayList<String> getTimeZoneStrings()
    {        
        return zoneString;
    }
    
    public static String getZoneFromZoneString(String string)
    {
        return zone.get(zoneString.indexOf(string));
    }
    
    public static String getStringValueZone(String z)
    {
        return zoneString.get(zone.indexOf(z));
    }
    public static int getZoneIndex(String z)
    {
        return zone.indexOf(z);
    } 

}
