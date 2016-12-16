/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.oracle.managedbean;

import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.oracle.bean.BackupJob;
import com.marstor.msa.oracle.bean.ExecuteBackup;
import com.marstor.msa.oracle.bean.Strategy;
import com.marstor.msa.oracle.validator.OracleValidator;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "bPropertyBean")
@ViewScoped
public class BackupPropertyBean implements Serializable {

    private String dbName = "";
    private String backupScript = "";
    private String jobName = "";
    private MSAResource res = new MSAResource();
    private String backupJobName="";
    public BackupPropertyBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        String name = request.getParameter("dbName");
        if (null != name) {
            this.dbName = name;
        }

        String job = request.getParameter("jobName");
        if (null != job) {
            this.jobName = job;
        }

        String script = request.getParameter("script");
        if (null != script) {
            this.backupScript = script;
        }

        initStrategy();

    }

    private void initStrategy() {
        if ((dbName == null) || (jobName == null)) {
            return;
        }
        List<BackupJob> allJobInfo = InterfaceFactory.getOracleInterfaceInstance().getAllJobInfo(dbName);
        BackupJob job = null;
        for (BackupJob backup : allJobInfo) {
            if (backup.taskName.equals(jobName)) {
                job = backup;
            }
        }
        this.backupJobName=jobName;
        if (null != job) {
            backupScript = job.script;
            Strategy strategy = job.strategy;

            if (1 == strategy.runFrequency) {
                strategyType = 1;
            }

            if (2 == strategy.runFrequency) {
                strategyType = 2;
                beginDate = BackupStrategyBean.stringToDate(strategy.beginDatetime);
                bBeginDate = false;
                bNoEnd = true;
                bEndDate = true;
                bCycle = true;
                bWeek = true;
                bMonth = true;
            }

            if (3 == strategy.runFrequency) {
                strategyType = 3;
                bBeginDate = false;
                if (strategy.endDatetime.equals("")) {
                    isNoEnd = true;
                    bEndDate = true;
                } else {
                    isNoEnd = false;
                    bEndDate = false;
                }


                bNoEnd = false;
                bCycle = false;
                beginDate = BackupStrategyBean.stringToDate(strategy.beginDatetime);
                endDate = BackupStrategyBean.stringToDate(strategy.endDatetime);

                iCycle = strategy.times;
                iType = strategy.type;

                if (4 == iType) {
                    bWeek = false;
                    for (Integer iw : strategy.list) {
                        if (1 == iw) {
                            week1 = true;
                        }

                        if (2 == iw) {
                            week2 = true;
                        }

                        if (3 == iw) {
                            week3 = true;
                        }

                        if (4 == iw) {
                            week4 = true;
                        }

                        if (5 == iw) {
                            week5 = true;
                        }

                        if (6 == iw) {
                            week6 = true;
                        }

                        if (7 == iw) {
                            week7 = true;
                        }
                    }
                }

                if (5 == iType) {
                    bMonth = false;
                    for (Integer im : strategy.list) {
                        if (1 == im) {
                            month1 = true;
                        }
                        if (2 == im) {
                            month2 = true;
                        }
                        if (3 == im) {
                            month3 = true;
                        }
                        if (4 == im) {
                            month4 = true;
                        }
                        if (5 == im) {
                            month5 = true;
                        }
                        if (6 == im) {
                            month6 = true;
                        }
                        if (7 == im) {
                            month7 = true;
                        }
                        if (8 == im) {
                            month8 = true;
                        }
                        if (9 == im) {
                            month9 = true;
                        }
                        if (10 == im) {
                            month10 = true;
                        }
                        if (11 == im) {
                            month11 = true;
                        }
                        if (12 == im) {
                            month12 = true;
                        }
                        if (13 == im) {
                            month13 = true;
                        }
                        if (14 == im) {
                            month14 = true;
                        }
                        if (15 == im) {
                            month15 = true;
                        }
                        if (16 == im) {
                            month16 = true;
                        }
                        if (17 == im) {
                            month17 = true;
                        }
                        if (18 == im) {
                            month18 = true;
                        }
                        if (19 == im) {
                            month19 = true;
                        }
                        if (20 == im) {
                            month20 = true;
                        }
                        if (21 == im) {
                            month21 = true;
                        }
                        if (22 == im) {
                            month22 = true;
                        }
                        if (23 == im) {
                            month23 = true;
                        }
                        if (24 == im) {
                            month24 = true;
                        }
                        if (25 == im) {
                            month25 = true;
                        }
                        if (26 == im) {
                            month26 = true;
                        }
                        if (27 == im) {
                            month27 = true;
                        }
                        if (28 == im) {
                            month28 = true;
                        }
                        if (29 == im) {
                            month29 = true;
                        }
                        if (30 == im) {
                            month30 = true;
                        }
                        if (31 == im) {
                            month31 = true;
                        }
                        if (32 == im) {
                            month32 = true;
                        }

                    }
                }
            }
        }
    }

    public String getDBName() {
        return dbName;
    }

    public void setBackupScript(String backupScript) {
        this.backupScript = backupScript;
    }

    public String getBackupScript() {
        return backupScript;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }
    private int strategyType = 1;

    public void setStrategyType(int strategyType) {
        this.strategyType = strategyType;
    }

    public int getStrategyType() {
        return strategyType;
    }
    private Date beginDate;

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }
    private Date endDate;

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    private boolean bBeginDate = true;

    public void setBBeginDate(boolean bBeginDate) {
        this.bBeginDate = bBeginDate;
    }

    public boolean getBBeginDate() {
        return bBeginDate;
    }
    private boolean bNoEnd = true;

    public void setBNoEnd(boolean bNoEnd) {
        this.bNoEnd = bNoEnd;
    }

    public boolean getBNoEnd() {
        return bNoEnd;
    }
    private boolean isNoEnd = true;

    public void setIsNoEnd(boolean isNoEnd) {
        this.isNoEnd = isNoEnd;
    }

    public boolean getIsNoEnd() {
        return isNoEnd;
    }
    private boolean bEndDate = true;

    public void setBEndDate(boolean bEndDate) {
        this.bEndDate = bEndDate;
    }

    public boolean getBEndDate() {
        return bEndDate;
    }

    public void typeListener() {
        if (1 == strategyType) {
            bBeginDate = true;
            bNoEnd = true;
            bEndDate = true;
            bCycle = true;
            bWeek = true;
            bMonth = true;
        }

        if (2 == strategyType) {
            bBeginDate = false;
            bNoEnd = true;
            bEndDate = true;
            bCycle = true;
            bWeek = true;
            bMonth = true;

        }

        if (3 == strategyType) {
            bBeginDate = false;
            bNoEnd = false;
            bCycle = false;
            if(iType!=5&&iType!=4&&iType!=3&&iType!=2&&iType!=1)
            {
                iType=1;
            }
            iCycle=1;
            if (4 == iType) {
                bWeek = false;
                bMonth = true;
            }

            if (5 == iType) {
                bWeek = true;
                bMonth = false;
            }
            
        }
    }

    public void endTimeListener() {
        if (isNoEnd) {
            bEndDate = true;
        } else {
            bEndDate = false;
        }

    }
    private boolean bCycle = true;

    public void getBCycle(boolean bCycle) {
        this.bCycle = bCycle;
    }

    public boolean getBCycle() {
        return bCycle;
    }
    private int iCycle;

    public void setICycle(int iCycle) {
        this.iCycle = iCycle;
    }

    public int getICycle() {
        return iCycle;
    }
    private int times;
    private int iType = 0;

    public void setTimes(int times) {
        this.times = times;
    }

    public int getTimes() {
        return times;
    }

    public void setIType(int iType) {
        this.iType = iType;
    }

    public int getIType() {
        return iType;
    }

    public void cycleListener() {
        if (4 == iType) {
            bWeek = false;
            bMonth = true;
        } else if (5 == iType) {
            bMonth = false;
            bWeek = true;
        } else {
            bWeek = true;
            bMonth = true;
        }
    }
    private boolean bWeek = true;

    public void setBWeek(boolean bWeek) {
        this.bWeek = bWeek;
    }

    public boolean getBWeek() {
        return bWeek;
    }
    private boolean bMonth = true;

    public void setBMonth(boolean bMonth) {
        this.bMonth = bMonth;
    }

    public boolean getBMonth() {
        return bMonth;
    }

    private Strategy createStrategy() {
        Strategy strategy = new Strategy();
        strategy.runFrequency = strategyType;
        if (null != beginDate) {
            strategy.beginDatetime = dateToString(beginDate);
        }
        System.out.println("isNoEnd"+isNoEnd);
        System.out.println("bEndDate"+bEndDate);
        if (this.isNoEnd)
        {
            strategy.endDatetime = "";
        } else
        {
            if (null != endDate)
            {
                strategy.endDatetime = dateToString(endDate);
            }
        }
        strategy.times = iCycle;
        strategy.type = iType;

        ArrayList<Integer> list = new ArrayList<Integer>();
        if (4 == iType) {
            if (week1) {
                list.add(1);
            }

            if (week2) {
                list.add(2);
            }

            if (week3) {
                list.add(3);
            }

            if (week4) {
                list.add(4);
            }

            if (week5) {
                list.add(5);
            }

            if (week6) {
                list.add(6);
            }

            if (week7) {
                list.add(7);
            }
        }

        if (5 == iType) {
            if (month1) {
                list.add(1);
            }

            if (month2) {
                list.add(2);
            }

            if (month3) {
                list.add(3);
            }

            if (month4) {
                list.add(4);
            }
            if (month5) {
                list.add(5);
            }

            if (month6) {
                list.add(6);
            }

            if (month7) {
                list.add(7);
            }

            if (month8) {
                list.add(8);
            }
            if (month9) {
                list.add(9);
            }

            if (month10) {
                list.add(10);
            }

            if (month11) {
                list.add(11);
            }
            if (month12) {
                list.add(12);
            }

            if (month13) {
                list.add(13);
            }
            if (month14) {
                list.add(14);
            }
            if (month15) {
                list.add(15);
            }

            if (month16) {
                list.add(16);
            }

            if (month17) {
                list.add(17);
            }

            if (month18) {
                list.add(18);
            }
            if (month19) {
                list.add(19);
            }

            if (month20) {
                list.add(20);
            }
            if (month21) {
                list.add(21);
            }
            if (month22) {
                list.add(22);
            }
            if (month23) {
                list.add(23);
            }
            if (month24) {
                list.add(24);
            }
            if (month24) {
                list.add(24);
            }
            if (month25) {
                list.add(25);
            }
            if (month26) {
                list.add(26);
            }
            if (month27) {
                list.add(27);
            }
            if (month28) {
                list.add(28);
            }
            if (month29) {
                list.add(29);
            }
            if (month30) {
                list.add(30);
            }
            if (month31) {
                list.add(31);
            }
            if (month32) {
                list.add(32);
            }
        }
        strategy.list = list;
        return strategy;
    }

    public String createScript() {
        String para = "dbName=" + dbName + "&type=property" + "&jobName=" + jobName;

        return "create_backup_script?faces-redirect=true" + para;
    }

    public String sendJob() {
        
        if (!OracleValidator.checkNetServiceName(jobName)) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("BackupJobNameError"), ""));
            return null;
        }

        if (backupScript.equals("")) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("BackupJobScriptError"), ""));
            return null;
        }
        
        System.out.println(this.strategyType);
                if(this.strategyType==3)
                {
                    System.out.println("iType:"+this.iType);
                    if(this.iType==4)
                    {
                        
                        if(!(this.week1||this.week2||this.week3||this.week4||this.week5||this.week6||this.week7))
                        {
                            System.out.println("周策略未保存");
                             FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "周策略未选择。", ""));
                            return null;
                        }
                        
                    }
                    if(this.iType==5)
                    {
                        if(!(this.month1||this.month2||this.month3||this.month4||this.month5||this.month6||this.week7
                           ||this.month8||this.month9||this.month10||this.month11||this.month12||this.month13||this.month14
                                ||this.month15||this.month16||this.month17||this.month18||this.month19||this.month20||this.month21
                                ||this.month22||this.month23||this.month24||this.month25||this.month26||this.month27||this.month28
                                ||this.month29||this.month30||this.month31||this.month32))
                            
                        {
                            System.out.println("月策略未保存");
                           FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "月策略未选择。", ""));
                            return null;
                        }
                    }
                }

        ExecuteBackup backup = new ExecuteBackup();
        backup.databaseName = dbName;
        backup.script = backupScript;
        backup.strategy = createStrategy();
        if (this.strategyType != 1)
        {
            try
            {
                dateToString(this.beginDate);

            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("BackupStartTimeError"), ""));
                return null;
            }
        }
        System.out.println("时间检查完毕");
        backup.taskName = jobName;
        backup.taskType = 1;
        boolean executeBackupJob = InterfaceFactory.getOracleInterfaceInstance().executeBackupJob(backup);
        if (executeBackupJob) {
            return "oracle_database?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("BackupJobExecuteFailed"), ""));
            return null;
        }
    }
    private boolean week1 = false;

    public void setWeek1(boolean week) {
        this.week1 = week;
    }

    public boolean getWeek1() {
        return week1;
    }
    private boolean week2 = false;

    public void setWeek2(boolean week) {
        this.week2 = week;
    }

    public boolean getWeek2() {
        return week2;
    }
    private boolean week3 = false;

    public void setWeek3(boolean week) {
        this.week3 = week;
    }

    public boolean getWeek3() {
        return week3;
    }
    private boolean week4 = false;

    public void setWeek4(boolean week) {
        this.week4 = week;
    }

    public boolean getWeek4() {
        return week4;
    }
    private boolean week5 = false;

    public void setWeek5(boolean week) {
        this.week5 = week;
    }

    public boolean getWeek5() {
        return week5;
    }
    private boolean week6 = false;

    public void setWeek6(boolean week) {
        this.week6 = week;
    }

    public boolean getWeek6() {
        return week6;
    }
    private boolean week7 = false;

    public void setWeek7(boolean week) {
        this.week7 = week;
    }

    public boolean getWeek7() {
        return week7;
    }
    public boolean month1 = false;
    public boolean month2 = false;
    public boolean month3 = false;
    public boolean month4 = false;
    public boolean month5 = false;
    public boolean month6 = false;
    public boolean month7 = false;
    public boolean month8 = false;
    public boolean month9 = false;
    public boolean month10 = false;
    public boolean month11 = false;
    public boolean month12 = false;
    public boolean month13 = false;
    public boolean month14 = false;
    public boolean month15 = false;
    public boolean month16 = false;
    public boolean month17 = false;
    public boolean month18 = false;
    public boolean month19 = false;
    public boolean month20 = false;
    public boolean month21 = false;
    public boolean month22 = false;
    public boolean month23 = false;
    public boolean month24 = false;
    public boolean month25 = false;
    public boolean month26 = false;
    public boolean month27 = false;
    public boolean month28 = false;
    public boolean month29 = false;
    public boolean month30 = false;
    public boolean month31 = false;
    public boolean month32 = false;

    public void setMonth1(boolean month) {
        this.month1 = month;
    }

    public boolean getMonth1() {
        return month1;
    }

    public void setMonth2(boolean month) {
        this.month2 = month;
    }

    public boolean getMonth2() {
        return month2;
    }

    public void setMonth3(boolean month) {
        this.month3 = month;
    }

    public boolean getMonth3() {
        return month3;
    }

    public void setMonth4(boolean month) {
        this.month4 = month;
    }

    public boolean getMonth4() {
        return month4;
    }

    public void setMonth5(boolean month) {
        this.month5 = month;
    }

    public boolean getMonth5() {
        return month5;
    }

    public void setMonth6(boolean month) {
        this.month6 = month;
    }

    public boolean getMonth6() {
        return month6;
    }

    public void setMonth7(boolean month) {
        this.month7 = month;
    }

    public boolean getMonth7() {
        return month7;
    }

    public void setMonth8(boolean month) {
        this.month8 = month;
    }

    public boolean getMonth8() {
        return month8;
    }

    public void setMonth9(boolean month) {
        this.month9 = month;
    }

    public boolean getMonth9() {
        return month9;
    }

    public void setMonth10(boolean month) {
        this.month10 = month;
    }

    public boolean getMonth10() {
        return month10;
    }

    public void setMonth11(boolean month) {
        this.month11 = month;
    }

    public boolean getMonth11() {
        return month11;
    }

    public void setMonth12(boolean month) {
        this.month12 = month;
    }

    public boolean getMonth12() {
        return month12;
    }

    public void setMonth13(boolean month) {
        this.month13 = month;
    }

    public boolean getMonth13() {
        return month13;
    }

    public void setMonth14(boolean month) {
        this.month14 = month;
    }

    public boolean getMonth14() {
        return month14;
    }

    public void setMonth15(boolean month) {
        this.month15 = month;
    }

    public boolean getMonth15() {
        return month15;
    }

    public void setMonth16(boolean month) {
        this.month16 = month;
    }

    public boolean getMonth16() {
        return month16;
    }

    public void setMonth17(boolean month) {
        this.month17 = month;
    }

    public boolean getMonth17() {
        return month17;
    }

    public void setMonth18(boolean month) {
        this.month18 = month;
    }

    public boolean getMonth18() {
        return month18;
    }

    public void setMonth19(boolean month) {
        this.month19 = month;
    }

    public boolean getMonth19() {
        return month19;
    }

    public void setMonth20(boolean month) {
        this.month20 = month;
    }

    public boolean getMonth20() {
        return month20;
    }

    public void setMonth21(boolean month) {
        this.month21 = month;
    }

    public boolean getMonth21() {
        return month21;
    }

    public void setMonth22(boolean month) {
        this.month22 = month;
    }

    public boolean getMonth22() {
        return month22;
    }

    public void setMonth23(boolean month) {
        this.month23 = month;
    }

    public boolean getMonth23() {
        return month23;
    }

    public void setMonth24(boolean month) {
        this.month24 = month;
    }

    public boolean getMonth24() {
        return month24;
    }

    public void setMonth25(boolean month) {
        this.month25 = month;
    }

    public boolean getMonth25() {
        return month25;
    }

    public void setMonth26(boolean month) {
        this.month26 = month;
    }

    public boolean getMonth26() {
        return month26;
    }

    public void setMonth27(boolean month) {
        this.month27 = month;
    }

    public boolean getMonth27() {
        return month27;
    }

    public void setMonth28(boolean month) {
        this.month28 = month;
    }

    public boolean getMonth28() {
        return month28;
    }

    public void setMonth29(boolean month) {
        this.month29 = month;
    }

    public boolean getMonth29() {
        return month29;
    }

    public void setMonth30(boolean month) {
        this.month30 = month;
    }

    public boolean getMonth30() {
        return month30;
    }

    public void setMonth31(boolean month) {
        this.month31 = month;
    }

    public boolean getMonth31() {
        return month31;
    }

    public void setMonth32(boolean month) {
        this.month32 = month;
    }

    public boolean getMonth32() {
        return month32;
    }

    public void weekAllSelect() {
        week1 = true;
        week2 = true;
        week3 = true;
        week4 = true;
        week5 = true;
        week6 = true;
        week7 = true;
    }

    public void weekAllCancle() {
        week1 = false;
        week2 = false;
        week3 = false;
        week4 = false;
        week5 = false;
        week6 = false;
        week7 = false;
    }

    public void monthAllSelect() {
        month1 = true;
        month2 = true;
        month3 = true;
        month4 = true;
        month5 = true;
        month6 = true;
        month7 = true;
        month8 = true;
        month9 = true;
        month10 = true;
        month11 = true;
        month12 = true;
        month13 = true;
        month14 = true;
        month15 = true;
        month16 = true;
        month17 = true;
        month18 = true;
        month19 = true;
        month20 = true;
        month21 = true;
        month22 = true;
        month23 = true;
        month24 = true;
        month25 = true;
        month26 = true;
        month27 = true;
        month28 = true;
        month29 = true;
        month30 = true;
        month31 = true;
        month32 = true;

    }

    public void monthAllCancle() {
        month1 = false;
        month2 = false;
        month3 = false;
        month4 = false;
        month5 = false;
        month6 = false;
        month7 = false;
        month8 = false;
        month9 = false;
        month10 = false;
        month11 = false;
        month12 = false;
        month13 = false;
        month14 = false;
        month15 = false;
        month16 = false;
        month17 = false;
        month18 = false;
        month19 = false;
        month20 = false;
        month21 = false;
        month22 = false;
        month23 = false;
        month24 = false;
        month25 = false;
        month26 = false;
        month27 = false;
        month28 = false;
        month29 = false;
        month30 = false;
        month31 = false;
        month32 = false;

    }

    public static String dateToString(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = f.format(date);
        return format;
    }

    public static Date stringToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException ex) {
//            Logger.getLogger(BackupStrategyBean.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("无法解析时间格式");
            date = null;
        }

        return date;
    }
}
