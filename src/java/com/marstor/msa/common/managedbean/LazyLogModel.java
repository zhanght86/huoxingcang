/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.model.LogInfo;
import static com.marstor.msa.common.managedbean.SystemLogBean.getTime;
import com.marstor.msa.common.util.JavaDBConnect;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.util.PathUtil;
import com.marstor.msa.vtl.util.MyUtility;
import com.marstor.xml.XMLParser;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrator
 */
public class LazyLogModel extends LazyDataModel<LogInfo> implements Serializable{

    private static MSAResource res = new MSAResource();
    private static PathUtil pu = new PathUtil();
    public static Date begin;
    public static  Date end;
    private List<String> typeList;
    private List<LogInfo> list;
    public static int selcetType = 0x0F;
     /**
     * 解析的XML的键值对存放的map，long型为ID，List为信息
     */
    private HashMap<Long, List<String>> map;
    public LazyLogModel() {
        this.map = new HashMap<Long, List<String>>();
        this.put2map();
        initDate();
        LazyLogModel.selcetType = 0x0F;
    }
    
    public LazyLogModel(Date dBegin, Date dEnd, int strType) {
        LazyLogModel.begin = dBegin;
        LazyLogModel.end = dEnd;
        LazyLogModel.selcetType = strType;
    }

    @Override
    public LogInfo getRowData(String rowKey) {
        for (LogInfo log : list) {
            if (log.getType().equals(rowKey)) {
                return log;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(LogInfo log) {
        return log.getType();
    }

    @Override
    public List<LogInfo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        list = new ArrayList<LogInfo>();
        JavaDBConnect conn = new JavaDBConnect();
        Connection connection = null;
        if (conn.connect()) {
            connection = conn.getConnection();
        } else {
            return null;
        }

        Statement statement;
        try {
            /*
             * 字段说明：1、id Long; 2、datetime int; 3、日志id long 4、日志类型type int;
             * 5、用户名 string ; 6 参数 String; 7 flag int; 8、 moduleID int
             */
            statement = connection.createStatement();
            ResultSet rs;
            int iResultCount = 0;
            int logType = getLogType();
            long beginTime = LazyLogModel.begin.getTime();
            long endTime = LazyLogModel.end.getTime();
            int count = 0;
            long lBig = first + pageSize;
            String strSQL;

            if (15 == logType) {
                strSQL = "SELECT COUNT (*) FROM log WHERE datetime>" + beginTime + " AND datetime<" + endTime;
            } else {
                strSQL = "SELECT COUNT (*) FROM log WHERE datetime>" + beginTime + " AND datetime<" + endTime + " AND type=" + logType;
            }
            rs = statement.executeQuery(strSQL);
            if (rs.next()) {
                this.setRowCount(rs.getInt(1));
            } else {
                this.setRowCount(0);
            }

            if (15 == logType) {
                strSQL = "SELECT * FROM log WHERE datetime>" + beginTime + " AND datetime<" + endTime + " ORDER BY id DESC OFFSET " + first + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY";
            } else {
                strSQL = "SELECT * FROM log WHERE datetime>" + beginTime + " AND datetime<" + endTime + " AND type=" + logType + " ORDER BY id DESC OFFSET " + first + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY";
            }
            rs = statement.executeQuery(strSQL);

            while (rs.next()) {
                LogInfo log = new LogInfo();
                log.setTime(getTime(rs.getLong(2)));
                String string = rs.getString(6);
                String[] split = string.split("\t",-1);

                log.setDetail(getLogDetailByMap(rs.getInt(8), rs.getLong(3), split));
                log.setUser(String.valueOf(rs.getString(5)));
                log.setType(getLogType(rs.getInt(4)));
                log.setId(rs.getLong(1));
                list.add(log);
            }
            connection.close();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                connection.close();
            } catch (SQLException ex1) {
                Logger.getLogger(SystemLogBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
            list = null;
            return null;
        }
    }

    private void initDate() {
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

    private void initLogType() {
        this.typeList = new ArrayList<String>();
        this.typeList.add(res.get("allType"));
        this.typeList.add(res.get("info"));
        this.typeList.add(res.get("warning"));
        this.typeList.add(res.get("error"));
        this.typeList.add(res.get("debug"));
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

    private int getLogType() {
        int tempLogType = 15;
        if (MyUtility.LOG_TYPE_ALL == LazyLogModel.selcetType) {
            return MyUtility.LOG_TYPE_ALL;
        } else if (MyUtility.LOG_TYPE_NORMAL == LazyLogModel.selcetType) {
            return MyUtility.LOG_TYPE_NORMAL;
        } else if (MyUtility.LOG_TYPE_WARNING == LazyLogModel.selcetType) {
            return MyUtility.LOG_TYPE_WARNING;
        } else if (MyUtility.LOG_TYPE_ERROR == LazyLogModel.selcetType) {
            return MyUtility.LOG_TYPE_ERROR;
        } else if (MyUtility.LOG_TYPE_DEBUG == LazyLogModel.selcetType) {
            return MyUtility.LOG_TYPE_DEBUG;
        }
        return tempLogType;
    }

    /*
     * 模块号： 1 fc 2 Vtl 3 vm 4 backup 5 cdp 6 nas 7 同步 8 oracle一体机
     * 10盘阵
     */
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
    /**
     * 从map中查询编号为aLong的对象的字符串中文值，并替换参数
     *
     * @param aInt 无意义
     * @param aLong map的key log的编号
     * @param split 字符串数组参数，用于组成完整打印信息
     * @return
     */
    public String getLogDetailByMap(int aInt, long aLong, String[] split)
    {
        String chinese = this.map.get(aLong).get(2);
        for (Integer y = 0; y < split.length; y++)
        {
            chinese = chinese.replace("%" + y.toString(), split[y].trim());
        }
        return chinese;
    }
    /**
     * 将xml中的内容读取到map对象中去。
     */
    public void put2map()
    {
        
        List<String> list;
        for (int j = 1; j <= 8; j++)
        {

            String strPath = pu.getLogPath() + res.get(String.valueOf(j));
            File file = new File(strPath);
            XMLParser parser = new XMLParser(file);
            Element root = parser.getRootElement();
            NodeList nl = root.getElementsByTagName("Bean");
            //System.out.print(nl.getLength());
            for (int i = 0; i < nl.getLength(); i++)
            {
                list = new ArrayList<String>();
                Element temp = (Element) nl.item(i);

                Long temp_id = Long.parseLong(temp.getElementsByTagName("ResourceID").item(0).getFirstChild().getNodeValue());
                // Long temp_id = xml_parser.getLongNodeContent("/MarsMSA/ResourceInfo/Bean/ResourceID", i);
                String temp_Author = temp.getElementsByTagName("Author").item(0).getFirstChild().getNodeValue();
                list.add(temp_Author);
                String temp_Macro = temp.getElementsByTagName("Macro").item(0).getFirstChild().getNodeValue();
                // String temp_Macro = xml_parser.getNodeContent("/MarsMSA/ResourceInfo/Bean/Macro", i);
                list.add(temp_Macro);
                String temp_Chinese = temp.getElementsByTagName("Chinese").item(0).getFirstChild().getNodeValue();
                //String temp_Chinese = xml_parser.getNodeContent("/MarsMSA/ResourceInfo/Bean/Chinese", i);
                list.add(temp_Chinese);

                String temp_English = temp.getElementsByTagName("English").item(0).getFirstChild().getNodeValue();
                //String temp_English = xml_parser.getNodeContent("/MarsMSA/ResourceInfo/Bean/English", i);
                list.add(temp_English);
                this.map.put(temp_id, list);
            }
        }
    }
}
