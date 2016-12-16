/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.servlet;

import com.marstor.msa.main.MainClass;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author Administrator
 */
public class MSAServerServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;
    private MainClass msaserver;

    @Override
    public void init() throws ServletException
    {
        System.out.println("---------------------start msaserver-----------------------");
        msaserver = new MainClass();
        msaserver.startOnWeb();
        System.out.println("---------------------start msaserver end-----------------------");

    }

    @Override
    public void destroy()
    {
        msaserver.stopWeb();
    }
}
