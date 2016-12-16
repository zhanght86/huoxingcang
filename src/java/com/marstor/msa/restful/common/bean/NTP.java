/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.common.bean;

import com.marstor.msa.common.bean.NTPStatistic;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Li Mengyang <li.mengyang@marstor.com>
 */
@XmlRootElement
public class NTP {
    private boolean enabled;
    private ArrayList<String> servers;

    public NTP(NTPStatistic data) {
        enabled = data.bEnable;
        servers = data.servers;
    }

    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ArrayList<String> getServers() {
        return servers;
    }

    public void setServers(ArrayList<String> servers) {
        this.servers = servers;
    }
    
    
    
}
