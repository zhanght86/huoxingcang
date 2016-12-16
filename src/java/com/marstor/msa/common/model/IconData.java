package com.marstor.msa.common.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * <p>Title: Mars Backup Advanced SysManager</p>
 *
 * <p>Description: This is Mars Backup Advanced SysManager Module Written In
 * Java</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Beijing Asian Corp. Ltd</p>
 *
 * @author Ding Huaming
 * @version 2.0
 */
public class IconData implements Serializable {

    private static final int SELECTNONE = 0;
    private static final int SELECTPART = 1;
    private static final int SELECTALL = 2;
    private Icon m_Icon = null;
    private String m_strLabel = "";
    private String m_URL = "";
    private String m_strTarget = "right_frame";
    private String m_IconStr = "blank";
    private String iconString = "";
    private Object m_Data = null;
    private int m_nStatus = SELECTNONE;
    private Icon m_icSelectPart = null;
    private Icon m_icSelectNone = null;
    private Icon m_icSelectAll = null;
    private String m_strClass = "";

    public IconData(Icon icon, String strLabel, String strURL, Object data) {
        m_Icon = icon;
        m_strLabel = strLabel;
        m_URL = strURL;
        m_Data = data;
    }

    public IconData(Icon icon, String strLabel, String strURL) {
        m_Icon = icon;
        m_strLabel = strLabel;
        m_URL = strURL;
    }

    public IconData(String strLabel, String strURL) {
        m_strLabel = strLabel;
        m_URL = strURL;
    }

    public IconData(String strLabel, String strURL, String strIcon) {

        m_strLabel = strLabel;
        m_URL = strURL;
        if (strIcon == null || strIcon.equals("")) {
            m_IconStr = "blank";
        } else {
            m_IconStr = strIcon;
        }
    }
    
    public IconData(String strLabel, String strURL, String strIcon, String uuid, String target) {
        m_strLabel = strLabel;
        m_URL = strURL;
        m_strTarget = target;
        if (strIcon == null || strIcon.equals("")) {
            m_IconStr = "blank";
        } else {
            m_IconStr = strIcon;
        }        
    }
    
    

    public IconData(String strLabel, String strURL, String strIcon, String strclass) {

        m_strLabel = strLabel;
        m_URL = strURL;
        if (strIcon == null || strIcon.equals("")) {
            m_IconStr = "blank";
        } else {
            m_IconStr = strIcon;
        }
        m_strClass = strclass;
    }

    public void setStatusIcon(Icon icSelectNone,
            Icon icSelectPart,
            Icon icSelectAll) {
        m_icSelectNone = icSelectNone;
        m_icSelectPart = icSelectPart;
        m_icSelectAll = icSelectAll;
    }

    public boolean isSelectAll() {
        return m_Icon.equals(m_icSelectAll);
    }

    public boolean isSelectPart() {
        return m_Icon.equals(m_icSelectPart);
    }

    public boolean isSelectNone() {
        return m_Icon.equals(m_icSelectNone);
    }

    public void setSelectAll() {
        m_Icon = m_icSelectAll;
    }

    public void setSelectPart() {
        m_Icon = m_icSelectPart;
    }

    public void setSelectNone() {
        m_Icon = m_icSelectNone;
    }

    public Icon getIcon() {
        return m_Icon;
    }

    public Object getData() {
        return m_Data;
    }

    public String getLabel() {
        return m_strLabel;
    }

    public void setLabel(String strLabel) {
        m_strLabel = strLabel;
    }

    public void setData(Object data) {
        m_Data = data;
    }

    public void setIcon(Icon icon) {
        m_Icon = icon;
    }

    public String getM_IconStr() {
        return m_IconStr;
    }

    public void setM_IconStr(String m_IconStr) {
        this.m_IconStr = m_IconStr;
    }

    @Override
    public String toString() {
        if (m_strLabel == null) {
            return "";
        }
        return m_strLabel.toString();
    }

    public Icon getM_Icon() {
        return m_Icon;
    }

    public void setM_Icon(Icon m_Icon) {
        this.m_Icon = m_Icon;
    }

    public String getM_strLabel() {
        return m_strLabel;
    }

    public void setM_strLabel(String m_strLabel) {
        this.m_strLabel = m_strLabel;
    }

    public String getM_URL() {
        return m_URL;
    }

    public void setM_URL(String m_URL) {
        this.m_URL = m_URL;
    }

    public String getM_strClass() {
        return m_strClass;
    }

    public void setM_strClass(String m_strClass) {
        this.m_strClass = m_strClass;
    }

    public String getM_strTarget() {
        return m_strTarget;
    }

    public void setM_strTarget(String m_strTarget) {
        this.m_strTarget = m_strTarget;
    }
    
}
