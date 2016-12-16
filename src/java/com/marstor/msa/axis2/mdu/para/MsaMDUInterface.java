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
     * @introduction ��½
     * @param username �û���
     * @return
     */
    public boolean loginMDU(String username);

    /**
     * @introduction �˳�
     * @param username �û���
     * @return
     */
    public boolean logoutMDU(String username);
    
    public String getMDU();

    /**  
     * @introduction ����Ŀ¼
     * @param username �û���
     * @param path Ŀ¼��
     * @return
     */
    public boolean mkdir(String username, String path);

    /**
     * @introduction ��ȡĿ¼�б�
     * @param username �û���
     * @param path Ŀ¼ȫ·��
     * @return
     */
    public String[] listDirectory(String username , String path);
    
        
}
