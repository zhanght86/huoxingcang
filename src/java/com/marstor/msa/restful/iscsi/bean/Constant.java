/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.marstor.msa.restful.iscsi.bean;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class Constant implements Serializable{
    public final static int NONE = 0;
    public final static int CHAP = 1;
    public final static int TARGET_FC = 0;
    public final static int TARGET_FCoE = 1;
    public final static int TARGET_iSCSI = 2;
    
}
