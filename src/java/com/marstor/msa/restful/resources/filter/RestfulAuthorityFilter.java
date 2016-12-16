/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.resources.filter;


import com.marstor.msa.common.bean.SystemInformation;
import com.marstor.msa.common.model.IconData;
import com.marstor.msa.common.treeNode.TreeNodeData;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
import com.marstor.msa.util.InterfaceFactory;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.global;
import javax.annotation.Priority;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author wanghe
 */
@Provider
@RestfulAuthority
@Priority(Priorities.USER)
public class RestfulAuthorityFilter implements ContainerRequestFilter, ContainerResponseFilter
{   
  static  Map<String,Byte>  moduleMap=new HashMap();
     static   Map<String,Byte>  functionMap=new HashMap();
     private Reg[] license =null;
     CommonInterface common=null;
     static
     {
         moduleMap.put("cdp", Module.MODULE_CDP);
         moduleMap.put("nas", Module.MODULE_NAS);
         moduleMap.put("vtl", Module.MODULE_VTL);
         moduleMap.put("iscsi", Module.MODULE_BASIC);
         moduleMap.put("fc", Module.MODULE_BASIC);
     }
     public RestfulAuthorityFilter()
     {
          
         System.out.println("RestfulAuthorityFilter ini");
          common = InterfaceFactory.getCommonInterfaceInstance();
         license =new Reg[4];
         license[0]=new Reg();
         license[0].setModuleID(Module.MODULE_CDP);
          license[1]=new Reg();
         license[1].setModuleID(Module.MODULE_NAS);
          license[2]=new Reg();
         license[2].setModuleID(Module.MODULE_VTL);
          license[3]=new Reg();
         license[3].setModuleID(Module.MODULE_BASIC);
     }
    @Override
    public void filter(ContainerRequestContext crc) throws IOException
    {
        MSAResource res = new MSAResource();
      String authorityName =   res.get("oem", "RestfulHeaderAuthorityStr");
      String password = crc.getHeaderString(authorityName);       
        System.out.println("token :"+common.getOpenApiKey());
        
        
        System.out.println(crc.getUriInfo().getAbsolutePath().toString());
        String url=crc.getUriInfo().getAbsolutePath().toString();
        String url2=url.substring(url.indexOf("v1")+3,url.length());
        System.out.println("url2"+url2);
        String moduleStr=url2.substring(0,url2.indexOf("/"));
        System.out.println(moduleStr);
        
       
        if(password!=null&&password.equals(common.getOpenApiKey()))
        {
            
            System.out.println("token is right");
            for (int i = 0; i < license.length; i++)
            {
//                System.out.println(Thread.currentThread().getStackTrace()[1].getClassName()+":"+Thread.currentThread().getStackTrace()[1].getMethodName()+":"+i);
//                System.out.println(moduleMap);
//                System.out.println(license[i]);
//                System.out.println(license[i].getModuleID());
//                System.out.println(moduleMap.get(moduleStr));
                if(moduleMap.get(moduleStr) == license[i].getModuleID())
                {
                    
                    System.out.println(moduleStr+"功能权限检测成功");
                    return;
                }
            }
            System.out.println(moduleStr+"权限检测失败，无法执行操作。");
            System.out.println("相应功能并未启动");
             Response build = Response.status(401).build();
             crc.abortWith(build);
        }
        else
        {
            System.out.println("token is wrong");
        Response build = Response.status(401).build();
        
        crc.abortWith(build);
        }
        
       
    }

    @Override
    public void filter(ContainerRequestContext crc, ContainerResponseContext crc1) throws IOException
    {
        System.out.println("filter2 is running");
        
    }
    

}
