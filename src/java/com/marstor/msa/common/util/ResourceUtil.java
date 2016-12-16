/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tianwenbo
 */
public class ResourceUtil {

    private static ResourceUtil resourceUtil;
    private ClassLoader loader;

    private ResourceUtil()
    {
        init();
    }

    public static ResourceUtil getResourceUtilInstance()
    {
        if (resourceUtil == null)
        {
            resourceUtil = new ResourceUtil();
        }
        return resourceUtil;
    }

    private void init()
    {
        try
        {
            String path = this.getAppPath();
            path = path.substring(0, path.lastIndexOf("WEB-INF"))+ "resources/languages";
            System.out.println("path=" + path);
            File file = new File(path);
//            ArrayList<File> resourceFiles = this.getResourceFiles(file);
//            URL[] urls = new URL[resourceFiles.size()];
//            for (int i = 0; i < urls.length; i++)
//            {
//                urls[i] = resourceFiles.get(i).toURI().toURL();
//                System.out.println(urls[i]);
//            }
            URL[] urls = new URL[]{file.toURI().toURL()};
            loader = new URLClassLoader(urls);  
        }
        catch (MalformedURLException ex)
        {
            Logger.getLogger(ResourceUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<File> getResourceFiles(File file)
    {
        ArrayList<File> files = new ArrayList<File>();
        File[] list = file.listFiles();
        if (list == null)
        {
            return files;
        }
        for (File f : list)
        {
            if (f.isDirectory())
            {
                files.addAll(getResourceFiles(f));
            }
            else if (f.isFile() && f.getName().endsWith(".properties"))
            {
                files.add(f);
            }
        }
        return files;
    }

    private String getAppPath()
    {
//        System.out.println(this.getClass().getResource("/").getPath());
//        String strPath = this.getClass().getResource("/").getPath();
       
        URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        String strPath = url.getPath();
        strPath = strPath.replace('\\', '/');
        if (strPath.endsWith("/"))
        {
            strPath = strPath.substring(0, strPath.length() - 1);
        }
        int nIndex = strPath.lastIndexOf("/");
        strPath = strPath.substring(0, nIndex);
        if (!strPath.endsWith("/"))
        {
            strPath = strPath + "/";
        }
        return strPath;
    }

    public ResourceBundle getResourceBundle(String name)
    {
        ResourceBundle bundle;
        try
        {
           
            bundle = ResourceBundle.getBundle(name, Locale.getDefault(), loader);
        }
        catch (java.util.MissingResourceException e)
        {
            e.printStackTrace();
            return null;
        }
        return bundle;
    }

}
