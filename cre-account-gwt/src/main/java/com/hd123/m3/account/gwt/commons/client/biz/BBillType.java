/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	BBillType.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.commons.client.biz;

import java.io.Serializable;

/**
 * 单据类型
 * 
 * @author chenpeisi
 * 
 */
public class BBillType implements Serializable {
  private static final long serialVersionUID = 4870218422816919399L;

  private String name;
  private String caption;
  private String className;
  private int direction;

  /** 名称 */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** 标题 */
  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  /** 实体接口类名 */
  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  /** 收付方向 */
  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public BBillType clone() {
    BBillType type = new BBillType();
    type.name = name;
    type.caption = caption;
    type.className = className;
    type.direction = direction;
    return type;
  }
}
