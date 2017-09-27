/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BProcessDefinition.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-1 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.biz;

import com.hd123.rumba.gwt.base.client.BMessageUCN;

/**
 * 流程定义|B对象
 * 
 * @author zhuhairui
 * 
 */
public class BProcessDefinition extends BMessageUCN {
  private static final long serialVersionUID = 6557876342271919012L;

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
