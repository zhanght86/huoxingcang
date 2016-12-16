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
    private String selectTimeUnit = "日";
    

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

        FacesContext context = FacesContext.getCurrentInstance();  //session域，更改VMList类中vmInfoList值
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
//               System.out.println("初始化天");
            timemap.put("日", DAY);
            timemap.put("小时", HOUR);
            selectTimeUnit = "日";
        }
        else
        {
           
//            System.out.println("初始化小时");
            timemap.put("小时", HOUR);
            timemap.put("日", DAY);
            selectTimeUnit = "小时";
        }
        
        OracleInfo bean2 = new OracleInfo(res.get("db"), dbNameNum);
        oracleList.add(bean2);
    }
    
    public void ChangeState()
    {
        tnSconfig = InterfaceFactory.getOracleInterfaceInstance().getTNSconfig();
        if (tnSconfig.getTimeUnit() == tnSconfig.getDAYUNIT())
        {
            selectTimeUnit = "日";
        }
        else
        {
           

            selectTimeUnit = "小时";
        }
    }
    public void save()
    {
        
        tnSconfig.setTimeUnit(this.timemap.get(this.selectTimeUnit));
        tnSconfig.setIsAutoCleanBackupSet(hasCleanServer);
        boolean modifyTNSconfig = InterfaceFactory.getOracleInterfaceInstance().modifyTNSconfig(tnSconfig);
        if(modifyTNSconfig)
        {
             FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "备份集删除策略保存成功", ""));
            System.out.println("save successfully");
        }
        else
        {
             FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "备份集保存策略失败，开始时间应在当前‘系统时间'之后", ""));
            System.out.println("save failed");
        }
    }
}
