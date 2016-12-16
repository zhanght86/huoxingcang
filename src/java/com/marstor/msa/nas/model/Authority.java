/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.nas.model;
import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
/**
 *
 * @author Administrator
 */
//@javax.faces.bean.ManagedBean(name = "authority")
//@javax.faces.bean.RequestScoped
public class Authority implements Serializable {
    
    private String  name;
    private boolean enabled;
    
    //private  HashMap map = new HashMap();

    public Authority() {
        
//        map.put(Constant.getOneAuthorityName(Constant.read), true);
//        map.put(Constant.getOneAuthorityName(Constant.write), false);
//        map.put(Constant.getOneAuthorityName(Constant.execute), false);
    }

    public Authority(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
    
    


//    public HashMap getMap() {
//        return map;
//    }
//
//    public void setMap(HashMap map) {
//        this.map = map;
//    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
