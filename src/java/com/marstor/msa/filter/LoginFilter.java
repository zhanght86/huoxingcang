/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.filter;

import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.model.IconData;
import com.marstor.msa.common.treeNode.TreeNodeData;
import com.marstor.msa.common.util.MyEncryp;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
import com.marstor.msa.servlet.MSASessionLisener;
import com.marstor.msa.util.InterfaceFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.primefaces.model.TreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Administrator
 */
public class LoginFilter implements Filter
{

    FilterConfig fc;
    private int state = 1;

    @Override
    public void init(FilterConfig fc) throws ServletException
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.fc = fc;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest hreq = (HttpServletRequest) request;
        HttpServletResponse hres = (HttpServletResponse) response;
        HttpSession session = hreq.getSession(true);
        String req_uri = hreq.getRequestURI();
        //nothing to do
        if (req_uri.contains(hreq.getContextPath() + "/template/login.xhtml") || req_uri.equals(hreq.getContextPath() + "/volume/reboot.xhtml")
                || req_uri.contains("/javax.faces.resource/") || req_uri.endsWith("about.xhtml"))
        {
            System.out.println("");
            chain.doFilter(request, response);
            return;
        }

        //no session goto login
        if (req_uri.equals("/") || session == null || session.isNew())
        {
            System.out.println("no session goto login");
            hres.sendRedirect(hreq.getContextPath() + "/template/login.xhtml");
            return;
        }
        //no user session invalid or overtime
        SystemUserInformation user = (SystemUserInformation) session.getAttribute("user");
        System.out.println("session中用户不为空");
        if (user == null)
        {
            System.out.println("用户为空，跳转回登录主页面");
            hres.sendRedirect(hreq.getContextPath() + "/template/login.xhtml");
            return;
        }

        if (req_uri.equals(hreq.getContextPath() + "/template/gotoMBA.xhtml"))
        {
            System.out.println("跳转MBA");
            CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
            Reg[] license = commonInterfaceInstance.getLicense();
            //此处的代理ID需要根据需求文档中定义的代理ID来定义，在mba解析中，会严格遵守ID来进行解析。
            //每个代理的标识从低位（右边起）第x位，x是代理的id，这个在mba数据库中有记录。
            //例如Vmware代理ID是5，那么Vmware就是0x00000010(十六进制)。
            int agents_CODE = 0x00000000;
            int agent_Vmware = 0x00000010;
            int agent_DM = 0x00000020;
            int agent_KingBase = 0x00000040;
            int agent_GBase = 0x00000100;
            int agent_FILE = 0x00000001;
            int agent_Oracle = 0x00000008;
            int agent_MySQL = 0x00000200;
            int agent_MSSQL = 0x00000002;  //SQL Server
            
            for (int i = 0; i < license.length; i++)
            {
                if ((Module.MODULE_VMware == license[i].getModuleID() && 1 == license[i].getFunctionID()))
                {
                    
                    agents_CODE = agents_CODE | agent_Vmware;
                    System.out.println("Vmware代理权限添加");
                }
                if ((Module.MODULE_GBASE == license[i].getModuleID() && 1 == license[i].getFunctionID()))
                {
                    agents_CODE = agents_CODE | agent_GBase;
                    System.out.println("Gbase代理权限添加");
                }
                if ((Module.MODULE_DM == license[i].getModuleID() && 1 == license[i].getFunctionID()))
                {
                    agents_CODE = agents_CODE | agent_DM;
                    System.out.println("DM代理权限添加");
                }
                //人大金仓的注册码
                if ((Module.MODULE_KingBase == license[i].getModuleID() && 1 == license[i].getFunctionID()))
                {
                    agents_CODE = agents_CODE | agent_KingBase;
                    System.out.println("KingBase代理权限添加");
                }
                //文件代理的注册码
                if ((Module.MODULE_FILE== license[i].getModuleID() && 1 == license[i].getFunctionID()))
                {
                    agents_CODE = agents_CODE | agent_FILE;
                    System.out.println("文件代理权限添加");
                }
                //Oracle代理的注册码
                if ((Module.MODULE_ORACLE_MBA== license[i].getModuleID() && 1 == license[i].getFunctionID()))
                {
                    agents_CODE = agents_CODE | agent_Oracle;
                    System.out.println("Oracle代理权限添加");
                }
                 //MySQL代理的注册码
                if ((Module.MODULE_MYSQL== license[i].getModuleID() && 1 == license[i].getFunctionID()))
                {
                    agents_CODE = agents_CODE | agent_MySQL;
                    System.out.println("MySQL代理权限添加");
                }
                  //MSSQL代理的注册码
                if ((Module.MODULE_MSSQL== license[i].getModuleID() && 1 == license[i].getFunctionID()))
                {
                    agents_CODE = agents_CODE | agent_MSSQL;
                    System.out.println("MSSQL代理权限添加");
                }

            }

            String uuid = "uuid=" + UUID.randomUUID().toString();

            System.out.println();
            char[] chParam = uuid.toCharArray();
            String param = new String(MyEncryp.Encode64(chParam));
            //生成uuid和MD5加密
            FileWriter fw = null;
            try
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = factory.newDocumentBuilder();
                Document document = db.newDocument();
                Element rootElement = document.createElement("Persons");

                Element name = document.createElement("username");
                name.setTextContent(user.name);
                Element usertype = document.createElement("usertype");
                usertype.setTextContent("11");
                Element limitTime = document.createElement("timestamp");
                limitTime.setTextContent(System.currentTimeMillis() + "");
                Element agents = document.createElement("agents");
                agents.setTextContent(agents_CODE + "");
                rootElement.appendChild(usertype);
                rootElement.appendChild(name);
                rootElement.appendChild(limitTime);
                rootElement.appendChild(agents);
                document.appendChild(rootElement);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                Source xmlSource = new DOMSource(document);
                Result outputTarget = new StreamResult(new File("/tmp/" + uuid + ".xml"));
                transformer.transform(xmlSource, outputTarget);
                hres.sendRedirect(hreq.getContextPath() + "/backup/vmware_backup.xhtml?" + param);
                return;
            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {
            }

        }
        System.out.println("进入注册码验证阶段");
        if (req_uri.equals(hreq.getContextPath() + "/template/framework.xhtml"))
        {
            Reg[] license = InterfaceFactory.getCommonInterfaceInstance().getLicense();
            if (null == license)
            {
                System.out.println("注册码不存在，跳转到注册码导入界面");
                hres.sendRedirect(hreq.getContextPath() + "/common/register_info.xhtml");
            }
            int sysvol_state = InterfaceFactory.getVolumeInterfaceInstance().getSYSVOLstate();
            session.setAttribute("sysvolstate", sysvol_state);

        } else
        {
            if (req_uri.equals(hreq.getContextPath() + "/template/login.xhtml"))
            {
                // hreq.getRequestDispatcher("/template/framework.xhtml").forward(hreq, hres);
                hres.sendRedirect(hreq.getContextPath() + "/template/framework.xhtml");
                return;
            } else
            {
                System.out.println("跳转至注销界面");
                if (req_uri.equals(hreq.getContextPath() + "/template/logout.xhtml"))
                {
                    session.invalidate();
                    if (user.type == 2)
                    {
                        MSASessionLisener.adminSession = null;
                        System.out.println("User type = " + user.type + "LoginOut...");
                    }
                    hres.sendRedirect(hreq.getContextPath() + "/template/login.xhtml");
                    return;
                }
            }
        }
        System.out.println("进入下一环节");
        chain.doFilter(request, response);

    }

    @Override
    public void destroy()
    {
    }

    public Boolean valid(HttpServletRequest hreq, ServletResponse hres)
    {
        String userName = hreq.getParameter("userName");
        String userPasswd = hreq.getParameter("userPasswd");
        if (userName != null && userName.equals("admin") && userPasswd != null && userPasswd.equals("admin"))
        {
            HttpSession session = hreq.getSession();
            session.setAttribute("user", "admin");
            return true;
        } else
        {
            return false;
        }
    }

    public static void main(String[] args)
    {
//        int agent_Vmware = 0x00000010;
//        int agent_DM = 0x00000020;
//        int agent_KingBase = 0x00000040;
//        System.out.println(Integer.valueOf("00000010", 16));
//        System.out.println(Integer.toHexString(agent_Vmware | agent_DM | agent_KingBase));
        int agents_CODE = 0x00000000;
            int agent_Vmware = 0x00000010;
            int agent_DM = 0x00000020;
            int agent_KingBase = 0x00000040;
            int agent_GBase = 0x00000100;
            System.out.println(agents_CODE|agent_Vmware|agent_DM|agent_KingBase|agent_GBase);

    }
}
