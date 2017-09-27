/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	EmailConstants.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月10日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.email;

/**
 * 定义与word模板有关的常量
 * 
 * @author mengyinkun
 */
public class EmailConstants {
  /***************************** 缴费明细有关的常量 *******************************/
  /** 科目 */
  public static final String MODEL_SUBJECT = "subject";
  /** 不含税金额 */
  public static final String MODEL_WITHOUTTAX = "withoutTax";
  /** 税额 */
  public static final String MODEL_TAX = "tax";
  /** 价税合计金额 */
  public static final String MODEL_TOTAL = "total";
  /** 费用周期 */
  public static final String MODEL_FEEDATE = "feeDate";
}
