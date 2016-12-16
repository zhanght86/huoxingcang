/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.model;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.oracle.bean.TNSconfig;
import com.marstor.msa.oracle.managedbean.DatabaseNameBean;
import com.marstor.msa.oracle.managedbean.NetServiceNameBean;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "oracle")
@ViewScoped
public class Oracle implements Serializable
{

    private List oracleList = new ArrayList();
    private MSAResource res = new MSAResource();
    private boolean First=true;
    private boolean hasCleanServer = false;
    private Date starttime = new Date();
    private int DAY = 1;
    private int HOUR = 2;
    private String selectTimeUnit = "��";
    

    private Map<String, Integer> timemap;

    private TNSconfig tnSconfig;

    public TNSconfig getTnSconfig()
    {
        return tnSconfig;
    }

    public void setTnSconfig(TNSconfig tnSconfig)
    {
        this.tnSconfig = tnSconfig;
    }

    public Map<String, Integer> getTimemap()
    {
        return timemap;
    }

    public void setTimemap(Map<String, Integer> timemap)
    {
        this.timemap = timemap;
    }

    public Oracle()
    {
        

        initList();
    }

    public String getSelectTimeUnit()
    {
//        System.out.println("get "+this.selectTimeUnit);
        return selectTimeUnit;
    }

    public void setSelectTimeUnit(String selectTimeUnit)
    {
        this.selectTimeUnit = selectTimeUnit;
    }

    public boolean isHasCleanServer()
    {
        return hasCleanServer;
    }

    public void setHasCleanServer(boolean hasCleanServer)
    {
        this.hasCleanServer = hasCleanServer;
    }

    public List getList()
    {
        return oracleList;
    }

    public Date getStarttime()
    {
        return starttime;
    }

    public void setStarttime(Date starttime)
    {
        this.starttime = starttime;
    }

    public void initList()
    {

        FacesContext context = FacesContext.getCurrentInstance();  //session�򣬸���VMList����vmInfoListֵ
        NetServiceNameBean nsNameBean = (NetServiceNameBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{NSNameBean}", NetServiceNameBean.class).getValue(context.getELContext());
        int nsNameNum = nsNameBean.getNetServiceName().size();

        OracleInfo bean1 = new OracleInfo(res.get("nsname"), nsNameNum);
        oracleList.add(bean1);

        DatabaseNameBean dbNameBean = (DatabaseNameBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{DatabaseBean}", DatabaseNameBean.class).getValue(context.getELContext());
        int dbNameNum = dbNameBean.getList().size();
        timemap = new HashMap<String, Integer>();
        tnSconfig = InterfaceFactory.getOracleInterfaceInstance().getTNSconfig();
      
        if(this.First)
        {
             System.out.println("first");
             hasCleanServer=tnSconfig.isIsAutoCleanBackupSet();
            this.First=false;
        }
        if (tnSconfig.getTimeUnit() == tnSconfig.getDAYUNIT())
        {
//               System.out.println("��ʼ����");
            timemap.put("��", DAY);
            timemap.put("Сʱ", HOUR);
            selectTimeUnit = "��";
        }
        else
        {
           
//            System.out.println("��ʼ��Сʱ");
            timemap.put("Сʱ", HOUR);
            timemap.put("��", DAY);
            selectTimeUnit = "Сʱ";
        }
        
        OracleInfo bean2 = new OracleInfo(res.get("db"), dbNameNum);
        oracleList.add(bean2);
    }
    
    public void ChangeState()
    {
        tnSconfig = InterfaceFactory.getOracleInterfaceInstance().getTNSconfig();
        if (tnSconfig.getTimeUnit() == tnSconfig.getDAYUNIT())
        {
            selectTimeUnit = "��";
        }
        else
        {
           

            selectTimeUnit = "Сʱ";
        }
    }
    public void save()
    {
        
        tnSconfig.setTimeUnit(this.timemap.get(this.selectTimeUnit));
        tnSconfig.setIsAutoCleanBackupSet(hasCleanServer);
        boolean modifyTNSconfig = InterfaceFactory.getOracleInterfaceInstance().modifyTNSconfig(tnSconfig);
        if(modifyTNSconfig)
        {
             FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "���ݼ�ɾ�����Ա���ɹ�", ""));
            System.out.println("save successfully");
        }
        else
        {
             FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "���ݼ��������ʧ�ܣ���ʼʱ��Ӧ�ڵ�ǰ��ϵͳʱ��'֮��", ""));
            System.out.println("save failed");
        }
    }
}
