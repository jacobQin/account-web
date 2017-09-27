/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-web
 * 文件名：	AccountBasicConstants.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月27日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account;

/**
 * 账务基础常量。
 * 
 * @author LiBin
 *
 */
public class AccountBasicConstants {

  /** =========================查询字段============================ */
  /** 单号(String) */
  public static final String FILTER_BILLNUMBER = "billNumber";
  /** 项目(String) */
  public static final String FILTER_STORE_UUID = "storeUuid";
  /** 对方单位(String) */
  public static final String FILTER_COUNTERPART_UUID = "counterpartUuid";
  /** 合同(String) */
  public static final String FILTER_CONTRACT_UUID = "contractUuid";
  /** 结转期(String) */
  public static final String FILTER_SETTLENO = "settleNo";
  /** 记账日期(Date) */
  public static final String FILTER_ACCOUNTDATE = "accountDate";
  /** 合作方式(String) */
  public static final String FILTER_COOPMODE = "coopMode";
  /** 账款方向 */
  public static final String FILTER_DIRECTION = "direction";

  /** =========================排序字段============================ */
  /** 单号 */
  public static final String SORT_BILLNUMBER = "billNumber";
  /** 业务状态 */
  public static final String SORT_BIZSTATE = "bizState";
  /** 项目代码 */
  public static final String SORT_STORE = "accountUnit";
  /** 对方单位代码 */
  public static final String SORT_COUNTERPART = "counterpart";
  /** 记账日期 */
  public static final String SORT_ACCOUNTDATE = "accountDate";
  /** 结转期(String) */
  public static final String SORT_SETTLENO = "settleNo";

}
