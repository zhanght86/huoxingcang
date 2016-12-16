/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.model.LogInfo;
import com.marstor.msa.common.log.LogID;
import com.marstor.msa.common.log.MyLog;
import com.marstor.msa.common.util.JavaDBConnect;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.PathUtil;
import com.marstor.msa.javadb.DBUtility;
import com.marstor.msa.vtl.util.MyUtility;
import com.marstor.xml.XMLParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "systemLog")
@ViewScoped
public class SystemLogBean implements Serializable {

    private static MSAResource res = new MSAResource();
    private static PathUtil pu = new PathUtil();
    private Date begin;
    private Date end;
    private List<String> typeList;
    private String selcetType = res.get("allType");
    private String basename = "/tmp/Logs.txt";
    private List<LogInfo> list;
    private LogInfo selectedLog;
    private static List<XMLParser> parserList;
    private StreamedContent file;
    private LazyDataModel<LogInfo> lazyModel;
    private String zipname = "/tmp/Logs.zip";
    private StreamedContent logFile;

    public LazyDataModel<LogInfo> getLazyModel()
    {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<LogInfo> lazyModel)
    {
        this.lazyModel = lazyModel;
    }

    public StreamedContent getFile()
    {
        initLog();
        try
        {
            File f = new File(basename);
            if (!f.exists())
            {
                if (!f.createNewFile())
                {
                }
            }
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8");
            for (LogInfo log : list)
            {
                writer.write(log.getTime() + "    " + log.getType() + "    " + log.getUser() + "    " + log.getDetail() + "\r\n");
            }
            writer.close();
            InputStream stream = new FileInputStream(f);
            file = new DefaultStreamedContent(stream, "", "Logs.txt");
            return file;
        }
        catch (IOException ex)
        {
            Logger.getLogger(SystemLogBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("createLogFileError"), ""));
            return null;
        }
    }

    public SystemLogBean()
    {
        initDate();
        initLogType();
        lazyModel = new LazyLogModel();
    }

    private void initDate()
    {
        Date date = new Date();
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(date);
        cStart.add(Calendar.MONTH, -1);
        begin = new Date(cStart.getTimeInMillis());

        Calendar cEnd = Calendar.getInstance();
        cEnd.setTime(date);
        cEnd.add(Calendar.DAY_OF_MONTH, 1);
        end = new Date(cEnd.getTimeInMillis());
    }

    private void initLogType()
    {
        this.typeList = new ArrayList<String>();
        this.typeList.add(res.get("allType"));
        this.typeList.add(res.get("info"));
        this.typeList.add(res.get("warning"));
        this.typeList.add(res.get("error"));
        this.typeList.add(res.get("debug"));
    }

    public LogInfo getSelectedLog()
    {
        return selectedLog;
    }

    public void setSelectedLog(LogInfo selectedLog)
    {
        this.selectedLog = selectedLog;
    }

    public List<String> getTypeList()
    {
        return typeList;
    }

    public String getSelcetType()
    {
        return selcetType;
    }

    public void setSelcetType(String selcetType)
    {
        this.selcetType = selcetType;
    }

    public Date getBegin()
    {
        return begin;
    }

    public void setBegin(Date begin)
    {
        this.begin = begin;
    }

    public Date getEnd()
    {
        return end;
    }

    public void setEnd(Date end)
    {
        this.end = end;
    }

    public List<LogInfo> getList()
    {
        return list;
    }

    public void setList(List<LogInfo> list)
    {
        this.list = list;
    }

    private boolean initLog()
    {
        list = new ArrayList<LogInfo>();
        JavaDBConnect conn = new JavaDBConnect();
        Connection connection = DBUtility.getDBConn();

        Statement statement;
        try
        {
            /*
             * 字段说明：1、id Long; 2、datetime int; 3、日志id long 4、日志类型type int;
             * 5、用户名 string ; 6 参数 String; 7 flag int; 8、 moduleID int
             */
            statement = connection.createStatement();
            ResultSet rs;
            int logType = getLogType();
            long beginTime = begin.getTime();
            long endTime = end.getTime();

            if (15 == logType)
            {
                rs = statement.executeQuery("SELECT * FROM log WHERE datetime>" + beginTime + " AND datetime<" + endTime + " ORDER BY id DESC");
            }
            else
            {
                rs = statement.executeQuery("SELECT * FROM log WHERE datetime>" + beginTime + " AND datetime<" + endTime + " AND type=" + logType + " ORDER BY id DESC");
            }
            while (rs.next())
            {
                LogInfo log = new LogInfo();
                log.setTime(getTime(rs.getLong(2)));
                String string = rs.getString(6);
                String[] split = string.split("\t");
                String detail = this.getLogDetail(rs.getInt(8), rs.getLong(3), split);

                log.setDetail(detail);
                log.setUser(String.valueOf(rs.getString(5)));
                log.setType(getLogType(rs.getInt(4)));
                log.setId(rs.getLong(1));
                list.add(log);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        finally
        {
            try
            {
                if (null != connection)
                {
                    connection.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static String getTime(long time)
    {
        Date date = new Date(time);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(date);
    }

    public void appointedLog()
    {
        LazyLogModel.begin = begin;
        LazyLogModel.end = end;
        LazyLogModel.selcetType = getLogType();
    }

    private String getLogType(int i)
    {
        if (i == MyUtility.LOG_TYPE_ALL)
        {
            return res.get("allType");
        }
        else if (i == MyUtility.LOG_TYPE_NORMAL)
        {
            return res.get("info");
        }
        else if (i == MyUtility.LOG_TYPE_WARNING)
        {
            return res.get("warning");
        }
        else if (i == MyUtility.LOG_TYPE_ERROR)
        {
            return res.get("error");
        }
        else if (i == MyUtility.LOG_TYPE_DEBUG)
        {
            return res.get("debug");
        }
        return "";
    }

    private int getLogType()
    {
        int tempLogType = 15;
        if (res.get("allType").equals(String.valueOf(this.selcetType)))
        {
            return MyUtility.LOG_TYPE_ALL;
        }
        else if (res.get("info").equals(String.valueOf(this.selcetType)))
        {
            return MyUtility.LOG_TYPE_NORMAL;
        }
        else if (res.get("warning").equals(String.valueOf(this.selcetType)))
        {
            return MyUtility.LOG_TYPE_WARNING;
        }
        else if (res.get("error").equals(String.valueOf(this.selcetType)))
        {
            return MyUtility.LOG_TYPE_ERROR;
        }
        else if (res.get("debug").equals(String.valueOf(this.selcetType)))
        {
            return MyUtility.LOG_TYPE_DEBUG;
        }
        return tempLogType;
    }

    public void deleteLogs()
    {
        JavaDBConnect conn = new JavaDBConnect();
        Connection connection = DBUtility.getDBConn();

        Statement statement;
        try
        {
            statement = connection.createStatement();
            if (15 == getLogType())
            {
                statement.executeUpdate("DELETE FROM log WHERE datetime>" + begin.getTime() + " AND datetime<" + end.getTime());
            }
            else
            {
                statement.executeUpdate("DELETE FROM log WHERE datetime>" + begin.getTime() + " AND datetime<" + end.getTime() + " AND type=" + getLogType());
            }

            /*
             * 字段说明：1、id Long; 2、datetime int; 3、日志id long 4、日志类型type int;
             * 5、用户名 string ; 6 参数 String; 7 flag int; 8、 moduleID int
             */
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            MyLog.errorLog("audit", LogID.LOG_CLEAR_LOG_FAILED, ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("deleteLogError"), ""));
            return;
        }
        finally
        {
            try
            {
                if (null != connection)
                {
                    connection.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        MyLog.normalLog("audit", LogID.LOG_CLEAR_LOG_SUCCESS,begin.toString(),end.toString());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.get("deleteAppointLogSuccess"), ""));
    }

    public String showDetail()
    {
        String param = "id=" + selectedLog.getId();
        return "system_log_detail?faces-redirect=true" + param;
    }

    /*
     * 模块号： 1 fc 2 Vtl 3 vm 4 backup 5 cdp 6 nas 7 同步 8 oracle一体机
     * 10盘阵
     */
    private String getLogDetail(int aInt, long aLong, String[] split)
    {
        try
        {
            String strPath = pu.getLogPath() + res.get(String.valueOf(aInt));

            File file = new File(strPath);
            XMLParser parser = new XMLParser(file);
            for (int i = 0; i < parser.getNodeCount("MarsMSA/ResourceInfo/Bean"); i++)
            {
                long id = parser.getLongNodeContent("MarsMSA/ResourceInfo/Bean/ResourceID", i);
                if (id == aLong)
                {
                    String author = parser.getNodeContent("MarsMSA/ResourceInfo/Bean/Author", i);
                    String macro = parser.getNodeContent("MarsMSA/ResourceInfo/Bean/Macro", i);
                    String chinese = parser.getNodeContent("MarsMSA/ResourceInfo/Bean/Chinese", i);
                    String english = parser.getNodeContent("MarsMSA/ResourceInfo/Bean/English", i);
                    for (int y = 0; y < split.length; y++)
                    {
                        chinese = chinese.replace("%" + y, split[y]);
                    }
                    return chinese;
                }
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        String notFound = "";
        for (String s : split)
        {
            notFound = notFound + s;
        }
        return notFound;
    }
    
    public StreamedContent getLogFile() {
        File[] files = getSrcLogFile();
        if (files == null) {
            return null;
        }
        ZipOutputStream zipOut = null;
        InputStream input = null;
        try {
            File f = new File(zipname);
            if (!f.exists()) {
                if(!f.createNewFile())
                {
                    return null;
                }
            }
            zipOut = new ZipOutputStream(new FileOutputStream(f));
            for (int i = 0; i < files.length; ++i) {
                input = new FileInputStream(files[i]);
                zipOut.putNextEntry(new ZipEntry(files[i].getName()));
//                zipOut.putNextEntry(new ZipEntry(f.getName() + File.separator + files[i].getName()));
                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = input.read(buff)) != -1) {
                    zipOut.write(buff, 0, len);
                }
                input.close();
            }
            zipOut.close();
            InputStream stream = new FileInputStream(f);
            logFile = new DefaultStreamedContent(stream, "", "Logs.zip");
            return logFile;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SystemLogBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SystemLogBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(SystemLogBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public File[] getSrcLogFile() {
        File[] files = new File[3];
        File f1 = new File("/var/msa/marscdp.log");
        File f2 = new File("/SYSVOL/SYSTEM/tomcat6/logs/catalina.out");
        File f3 = new File("/var/adm/messages");
        if (f1.exists() && f2.exists() && f3.exists()) {
            files[0] = f1;
            files[1] = f2;
            files[2] = f3;
            return files;
        } else {
            return null;
        }
    }
}