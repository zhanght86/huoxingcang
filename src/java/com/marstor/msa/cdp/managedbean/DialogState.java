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
 * @author ����
 */
@ManagedBean(name = "dialogState")
@ViewScoped
/**
 * dialog�Ĺ���bean
 */
public class DialogState implements Serializable
{

    /**
     * checkbox��״̬��¼��Ϊtrueʱ������
     */
    private boolean codeinputstate;
    /**
     * ���ɵ���֤��
     */
    private String code;
    /**
     * ������������֤�룬���ڱȽ�
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
     * ��javascript�з����Ƿ�������֤���״̬���ַ���
     *
     * @return ��������֤���״̬���ַ���
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
     * ÿ��������֤�붼����һ�ξɵ���֤��
     *
     * @return �����ɵ���֤���ַ���
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
