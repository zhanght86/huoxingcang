/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.model.LogInfo;
import com.marstor.msa.common.util.JavaDBConnect;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.PathUtil;
import com.marstor.msa.javadb.DBUtility;
import com.marstor.msa.vtl.util.MyUtility;
import com.marstor.xml.XMLParser;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "logDetail")
@ViewScoped
public class SystemLogDetailBean implements Serializable{

    private MSAResource res = new MSAResource();
    private static PathUtil pu = new PathUtil();
    private LogInfo log;
    private long logID;

    public SystemLogDetailBean() {
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) exContext.getRequest();

        if (null != request.getParameter("id")) {
            logID = Long.valueOf(request.getParameter("id"));
            System.out.println("LogID is : " + logID);
            initLog();
        }
    }

    public LogInfo getLog() {
        return log;
    }

    public void setLog(LogInfo log) {
        this.log = log;
    }

    private boolean initLog() {

        JavaDBConnect conn = new JavaDBConnect();
        Connection connection = DBUtility.getDBConn();

        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM log WHERE id = " + logID);
            /*
             * 字段说明：1、id Long; 2、datetime int; 3、日志id long 4、日志类型type int;
             * 5、用户名 string ; 6 参数 String; 7 flag int; 8、 moduleID int
             */
            while (rs.next()) {
                log = new LogInfo();
                log.setTime(SystemLogBean.getTime(rs.getLong(2)));
                String string = rs.getString(6);
                String[] split = string.split("\t");
                String detail = getLogDetail(rs.getInt(8), rs.getLong(3), split);

                log.setDetail(detail);
                log.setType(getLogType(rs.getInt(4)));
            }

            rs.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (SQLException ex1) {
                Logger.getLogger(SystemLogBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return true;
    }

    private String getLogType(int i) {
        if (i == MyUtility.LOG_TYPE_ALL) {
            return res.get("allType");
        } else if (i == MyUtility.LOG_TYPE_NORMAL) {
            return res.get("info");
        } else if (i == MyUtility.LOG_TYPE_WARNING) {
            return res.get("warning");
        } else if (i == MyUtility.LOG_TYPE_ERROR) {
            return res.get("error");
        } else if (i == MyUtility.LOG_TYPE_DEBUG) {
            return res.get("debug");
        }
        return "";
    }

    private String getLogDetail(int aInt, long aLong, String[] split) {
        try {
            String strPath = pu.getLogPath() + res.get(String.valueOf(aInt));

            File file = new File(strPath);
            XMLParser parser = new XMLParser(file);
            for (int i = 0; i < parser.getNodeCount("MarsMSA/ResourceInfo/Bean"); i++) {
                long id = parser.getLongNodeContent("MarsMSA/ResourceInfo/Bean/ResourceID", i);
                if (id == aLong) {
                    String author = parser.getNodeContent("MarsMSA/ResourceInfo/Bean/Author", i);
                    String macro = parser.getNodeContent("MarsMSA/ResourceInfo/Bean/Macro", i);
                    String chinese = parser.getNodeContent("MarsMSA/ResourceInfo/Bean/Chinese", i);
                    String english = parser.getNodeContent("MarsMSA/ResourceInfo/Bean/English", i);
                    for (int y = 0; y < split.length; y++) {
                        chinese = chinese.replace("%" + y, split[y]);
                    }
                    return chinese;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String notFound = "";
        for (String s : split) {
            notFound = notFound + s;
        }
        return notFound;
    }
}
