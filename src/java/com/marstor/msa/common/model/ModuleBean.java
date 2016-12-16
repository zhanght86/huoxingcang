package com.marstor.msa.common.model;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "module")
@RequestScoped
public class ModuleBean implements Serializable{
    private String module = "";
    private String function = "";
    private String value = "";
    
    public ModuleBean(){   
    }
    
    public ModuleBean(String module, String function, String value){
        this.module = module;
        this.function = function;
        this.value = value;        
    }
    
    public String getModule(){
        return module;
    }
    
    public String getFunction(){
        return function;
    }
    
    public String getValue(){
        return value;
    }
}
