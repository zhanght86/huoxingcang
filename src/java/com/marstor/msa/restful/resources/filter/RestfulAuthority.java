/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.resources.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.NameBinding;

/**
 *
 * @author wanghe
 */
@NameBinding
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
//RetentionPolicy策略
/*
 SOURCE,// Annotation is discarded by the compiler  
  
  CLASS,// Annotation is stored in the class file, but ignored by the VM  
  
  RUNTIME// Annotation is stored in the class file and read by the VM  
*/
//@Inherited 
//用于定义是否会影响到子类。
public @interface RestfulAuthority
{
    
}
