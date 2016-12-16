/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import java.io.Serializable;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author tianwenbo
 */
@ManagedBean(name = "confirmDialogBean")
@ViewScoped
public class ConfirmDialogBean implements Serializable{

    private boolean validate = true;
    private String code;

    public boolean isValidate()
    {
        return validate;
    }

    public void setValidate(boolean validate)
    {
        this.validate = validate;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void onCheckBox(String uid)
    {
        System.out.println(uid);
        System.out.println("validate=" + validate);
        if(validate)
        {
         this.code=this.generateCode()+"";
        }
//        setCode(generateCode()+"");
        
    }

    public long generateCode()
    {
        return Math.round(Math.random()*89999+10000);
    }

}
