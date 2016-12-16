/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.log.LogItem;
import com.marstor.msa.log.MSALogger;
import com.marstor.msa.parameter.GlobalParameter;
import com.marstor.msa.servlet.MSASessionLisener;
import com.marstor.msa.util.InterfaceFactory;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginBean implements Serializable {

    MSAResource res = new MSAResource();
    private String username;
    private String password;
    public boolean isrender = false;
    private boolean multiple = false;
    private boolean bKick = true;
    public static String kickedUser;
    public static String kickedIP;

    public LoginBean()
    {
        if (GlobalParameter.bAllowMultiAdminLogin)
        {
            multiple = true;
        } else
        {
            multiple = false;
        }
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isIsrender()
    {
        return isrender;
    }

    public void setIsrender(boolean isrender)
    {
        this.isrender = isrender;
    }

    public String login()
    {
        if (true == multiple)
        {
            return multipleLogin();
        } else
        {
            return userLoginExam();
        }
    }

    public String multipleLogin()
    {
        FacesMessage msg = null;
        boolean loggedIn = false;
        isrender = false;
        RequestContext context = RequestContext.getCurrentInstance();

        String returnValue = null;
        if (password == null || username == null || password.equals("") || username.equals(""))
        {
            isrender = true;
            loggedIn = false;
            returnValue = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("userOrPasswordError"), "");
        } else
        {
            CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
            SystemUserInformation user = common.login(username, password, 0);

            if (user != null)
            {      
                System.out.println("用户:"+username+"登录成功");
                ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
                HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
                HttpSession sess = request.getSession();
                sess.setAttribute("user", user);
                Date time = common.getSystemTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(time);
                sess.setAttribute("time", dateString);

                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
                returnValue = "/template/framework.xhtml?faces-redirect=true";             
            } else
            {
                System.out.println("用户:"+username+"登录失败");
                isrender = true;
                loggedIn = false;
                returnValue = null;
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("userOrPasswordError"), "");
            }
        }
        context.addCallbackParam("loggedIn", loggedIn);
        return returnValue;
    }

    public String userLoginExam()
    {
        isrender = false;
        FacesMessage msg = null;
        boolean loggedIn = false;
        RequestContext context = RequestContext.getCurrentInstance();

        String returnValue = null;
        if (password == null || username == null || password.equals("") || username.equals(""))
        {
            returnValue = null;
            isrender = true;
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("userOrPasswordError"), "");
        } else
        {
            CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
            SystemUserInformation user = common.login(username, password, 0);

            ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
            Date time = common.getSystemTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(time);
            
            if (null == user) 
            {
                isrender = true;
                loggedIn = false;
                returnValue = null;
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("userOrPasswordError"), "");
            } else if (user.type != 2) 
            {
                System.out.println("用户:"+username+"登录成功");
                        HttpSession sess = request.getSession();
                        sess.setAttribute("user", user);
                        sess.setAttribute("time", dateString);

                        loggedIn = true;
                        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
                        returnValue = "/template/framework.xhtml?faces-redirect=true";
                    } else if (MSASessionLisener.adminSession == null) 
                    {
                        MSASessionLisener.adminSession = request.getSession();
                        MSASessionLisener.adminSession.setAttribute("user", user);
                        MSASessionLisener.adminSession.setAttribute("time", dateString);
                        LoginBean.kickedIP = request.getRemoteAddr();
                        LoginBean.kickedUser = username;

                        loggedIn = true;
                        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
                        returnValue = "framework.xhtml?faces-redirect=true";
                    } else 
                    {
                        RequestContext.getCurrentInstance().execute("askLogin.show();");
                    }
                
        }
        context.addCallbackParam("loggedIn", loggedIn);
        return returnValue;
    }

    public String userLogin()
    {
        RequestContext.getCurrentInstance().execute("askLogin.hide();");
        isrender = false;
        FacesMessage msg = null;
        boolean loggedIn = false;
        RequestContext context = RequestContext.getCurrentInstance();

        String returnValue = null;
        if (password == null || username == null || password.equals("") || username.equals(""))
        {
            returnValue = null;
            isrender = true;
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("userOrPasswordError"), "");
        } else
        {
            if (null != MSASessionLisener.adminSession)
            {
                try
                {
                    Object attribute = MSASessionLisener.adminSession.getAttribute("user");
                    MSASessionLisener.adminSession.invalidate();
                } catch (IllegalStateException e)
                {
                    bKick = false;
                }
                MSASessionLisener.adminSession = null;
            }

            CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
            SystemUserInformation user = common.login(username, password, 0);

            if (user != null)
            {
                ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
                HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
                MSASessionLisener.adminSession = request.getSession();
                MSASessionLisener.adminSession.setAttribute("user", user);
                Date time = common.getSystemTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(time);
                MSASessionLisener.adminSession.setAttribute("time", dateString);

                if (bKick)
                {
                    MSALogger.log("System", 1, LogItem.LOG_NORMAL, 100004, new String[]
                    {
                        username, request.getRemoteAddr(), LoginBean.kickedUser, LoginBean.kickedIP
                    });
                }

                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
                returnValue = "/template/framework.xhtml?faces-redirect=true";
            } else
            {
                isrender = true;
                loggedIn = false;
                returnValue = null;
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, res.get("userOrPasswordError"), "");
            }
        }
        context.addCallbackParam("loggedIn", loggedIn);
        return returnValue;
    }
}

