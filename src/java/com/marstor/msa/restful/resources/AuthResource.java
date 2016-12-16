/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.resources;

import com.marstor.msa.common.bean.SystemInformation;
import com.marstor.msa.common.bean.SystemUserInformation;
import static com.marstor.msa.common.managedbean.WarnEmaiBean.password;
import com.marstor.msa.common.model.IconData;
import com.marstor.msa.common.treeNode.TreeNodeData;
import com.marstor.msa.restful.resources.*;
import com.marstor.msa.restful.token.bean.AuthDomain;
import com.marstor.msa.common.util.MSAGlobalResource;
import com.marstor.msa.common.util.MSAResource;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.encrypt.reg.Module;
import com.marstor.msa.encrypt.reg.Reg;
import com.marstor.msa.restful.resources.filter.RestfulAuthority;
import com.marstor.msa.util.InterfaceFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author wanghe
 */
@Path("auth")
public class AuthResource
{
    private final int UserType_Admin=2;
    @Path("tokens")
    @POST
    @Produces(
            {
                MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
            })
    @Consumes(MediaType.APPLICATION_JSON)
    //$ curl -X POST http://192.168.1.232:80/v1/auth/tokens -i -d '{"auth":{"username":"admin","password":"admin"}}' -H 'Content-Type:application/json'
    public Response Token(AuthDomain auth)
    {
        CommonInterface commonInterfaceInstance = InterfaceFactory.getCommonInterfaceInstance();
        Reg[] license = commonInterfaceInstance.getLicense();
        MSAResource res = new MSAResource();
        String authorityName = res.get("oem", "RestfulHeaderAuthorityStr");
        if (auth != null)
        {
            for (Reg templicense : license)
            {
                if (Module.MODULE_OPEN_API == templicense.getModuleID() && Module.FUNCTIONID_OPEN_API == templicense.getFunctionID())
                {
                    CommonInterface common = InterfaceFactory.getCommonInterfaceInstance();
                    SystemUserInformation user = common.login(auth.getAuth().getUsername(), auth.getAuth().getPassword(), 0);
                    if(user!=null&&user.type==UserType_Admin)
                    {
                        System.out.println("token :"+common.getOpenApiKey());
                        return Response.status(201).header(authorityName, common.getOpenApiKey()).build();
                    }
                }
            }
        }

        return Response.status(401).build();

    }
}
