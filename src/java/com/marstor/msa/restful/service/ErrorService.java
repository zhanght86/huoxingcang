/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.service;
import com.marstor.msa.restful.error.bean.ErrorInfo;
import com.marstor.msa.restful.error.bean.ErrorInfoRes;
import javax.ws.rs.core.Response;
/**
 *
 * @author Administrator
 */
public class ErrorService {
    
    public static Response errorHandling(String error){
        ErrorInfo errorInfo = new ErrorInfo(500,1,error);
        ErrorInfoRes errorInfoRes = new ErrorInfoRes(errorInfo);
        return Response.status(500).entity(errorInfoRes).build();
    }
    
}
