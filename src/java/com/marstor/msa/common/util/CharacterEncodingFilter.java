/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.util;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;



/**
 *
 * @author Administrator
 */
@ManagedBean(name = "characterEncodingFilter")
@RequestScoped
public class CharacterEncodingFilter implements Filter{ //解决上传中文乱码问题

    //在使用primefaces的组件p:fileUpload进行文件上传的时候，如果上传文件名是中文的，上传到服务器看到的文件名就会使乱码
    /**
     * Creates a new instance of CharacterEncodingFilter
     */
    public CharacterEncodingFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
       
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
         request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);

    }

    public void destroy() {
        
    }


}
