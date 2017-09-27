/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	BSettlement.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月17日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.model;

/**
 * 费用周期
 * 
 * @author chenganbang
 *
 */
public class BSettlement {
  private String caption = "";
  private String dateStr = "";

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getDateStr() {
    return dateStr;
  }

  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }
}
