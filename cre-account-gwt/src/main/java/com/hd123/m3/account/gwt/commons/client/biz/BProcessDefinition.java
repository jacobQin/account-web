/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	BProcessDefinition.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-14 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import java.io.Serializable;

/**
 * 流程定义|B对象
 * 
 * @author zhuhairui
 * 
 */
public class BProcessDefinition implements Serializable {
  private static final long serialVersionUID = -6315716716986517911L;

  public static final String FIELD_KEY = "key";
  public static final String FIELD_NAME = "name";

  public BProcessDefinition() {

  }

  public BProcessDefinition(String key, String name) {
    this.key = key;
    this.name = name;
  }

  private String key;
  private String name;

  /** 流程key */
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  /** 名称 */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
