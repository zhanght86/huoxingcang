package com.marstor.msa.common.util;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
@ManagedBean(name = "res")
@RequestScoped
public final class MSAResource implements Serializable { 

    public MSAResource()
    {
    }

    public String getRequestPage()
    {
        HttpServletRequest s = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = s.getRequestURI();
        String context = s.getContextPath();
        int indexS = context.length();
        int indexE = url.lastIndexOf(".");
        if(indexE<0)
        {
            return "";
        }
        return url.substring(indexS + 1, indexE).replaceAll("/", ".");
    }

    public String get(String key)
    {
        String basename = getRequestPage();
        return this.get(basename, key);
    }

    public String get(String basename, String key)
    {
        ResourceUtil resourceUtilInstance = ResourceUtil.getResourceUtilInstance();
        ResourceBundle bundle = resourceUtilInstance.getResourceBundle(basename);
        if (bundle == null)
        {
            return "";
        }
        return bundle.getString(key);
    }
}
