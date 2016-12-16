/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.cdp.managedbean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author 王鹤
 */
@ManagedBean(name = "dialogState")
@ViewScoped
/**
 * dialog的管理bean
 */
public class DialogState implements Serializable
{

    /**
     * checkbox的状态记录，为true时，挑勾
     */
    private boolean codeinputstate;
    /**
     * 生成的验证码
     */
    private String code;
    /**
     * 保存起来的验证码，用于比较
     */
    private String oldcode = "";

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getOldcode()
    {
        return oldcode;
    }

    public void setOldcode(String oldcode)
    {
        this.oldcode = oldcode;
    }

    public DialogState()
    {
        this.codeinputstate = false;

    }

    public boolean isCodeinputstate()
    {
        return codeinputstate;
    }

    /**
     * 向javascript中返回是否启用验证码的状态的字符串
     *
     * @return 否启用验证码的状态的字符串
     */
    public String getFlag()
    {
        if (this.isRendered())
        {
            return "true";
        } else
        {
            return "false";
        }
    }

    public boolean isRendered()
    {
        return !codeinputstate;
    }

    public void setCodeinputstate(boolean codeinputstate)
    {
        this.codeinputstate = codeinputstate;
    }

    /**
     * 每次生成验证码都更新一次旧的验证码
     *
     * @return 新生成的验证码字符串
     */
    public String getCode()
    {

        code = "" + random0_9() + random0_9() + random0_9() + random0_9() + random0_9() + "";
        this.oldcode = code;
        return this.oldcode;
    }

    private int random0_9()
    {
        int random;
        return random = (int) (Math.random() * 10);
    }

    public void cancel()
    {
        this.codeinputstate = false;
    }
    public void restart()
    {
        RequestContext.getCurrentInstance().execute("reboot.show()");
    }
     public void shutdown()
    {
        RequestContext.getCurrentInstance().execute("shutdown.show()");
    }
}
