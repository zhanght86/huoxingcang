/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.axis2.mdu.para;

import com.marstor.msa.axis2.mdu.model.ListFiles;

/**
 *
 * @author yly
 */
public interface MsaMDUInterface {
    
    public String getError();
    
    /** 
     * @introduction 登陆
     * @param username 用户名
     * @return
     */
    public boolean loginMDU(String username);

    /**
     * @introduction 退出
     * @param username 用户名
     * @return
     */
    public boolean logoutMDU(String username);
    
    public String getMDU();

    /**  
     * @introduction 创建目录
     * @param username 用户名
     * @param path 目录名
     * @return
     */
    public boolean mkdir(String username, String path);

    /**
     * @introduction 获取目录列表
     * @param username 用户名
     * @param path 目录全路径
     * @return
     */
    public String[] listDirectory(String username , String path);
    
        
}
