/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.util;

/**
 *
 * @author tianwenbo
 */
public class SelectOneItem {

    private Object value;     //值
    private String label;     //显示内容
    private String tooltip;   //描述提示
    private int length_limit = 16;  //字符长度限制

    public SelectOneItem(String value)
    {
        this(value, 16);
    }

    public SelectOneItem(String value, int limit)
    {
        this(value, value, limit);
    }

    public SelectOneItem(String label, Object value, int limit)
    {
        this.length_limit = limit;
        this.label = label;
        this.tooltip = label;
        if (label.length() > length_limit)
        {
            this.label = label.substring(0, length_limit) + "...";
        }
        else
        {
            this.label = tooltip;
        }
        this.value = value;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getTooltip()
    {
        return tooltip;
    }

    public void setTooltip(String tooltip)
    {
        this.tooltip = tooltip;
    }

    public int getLength_limit()
    {
        return length_limit;
    }

    public void setLength_limit(int length_limit)
    {
        this.length_limit = length_limit;
    }

    @Override
    public String toString()
    {
        return this.tooltip;
    }

    @Override
    public boolean equals(Object object)
    {    
        if (object == this)
        {
            return true;
        }
        if (!(object instanceof SelectOneItem))
        {
            return false;
        }
        SelectOneItem other = (SelectOneItem) object;
        if (this.value.equals(other.value))
        {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 89 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

}
