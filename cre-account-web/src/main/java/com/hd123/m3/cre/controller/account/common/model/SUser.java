/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	SUser.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月2日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common.model;

import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * 用户对象
 * 
 * @author LiBin
 *
 */
public class SUser extends UCN{

  private static final long serialVersionUID = -1578252495042275931L;
  
  private String namespace;
  private String id;
  private String fullName;
  
  public String getNamespace() {
    return namespace;
  }
  
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getFullName() {
    return fullName;
  }
  
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  
}
