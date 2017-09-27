/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-web
 * 文件名：	StatementConstants.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月8日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.statement;

/**
 * 账单常量
 * 
 * @author chenganbang
 *
 */
public class StatementConstants {

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
  /** 出账时间(Date) */
  public static final String FILTER_ACCOUNTTIME = "accountTime";
  /** 计划出账日期(Date) */
  public static final String FILTER_PLANDATE = "planDate";
  /** 账单周期(Date) */
  public static final String FIELD_ACCOUNTRANGE = "accountRange";
  /** 合作方式(String) */
  public static final String FILTER_COOPMODE = "coopMode";
  /** 科目 */
  public static final String FILTER_SUBJECT = "subject";
  /** 楼层(String) */
  public static final String FILTER_FLOOR_UUID = "floorUuid";
  /** 位置(String) */
  public static final String FILTER_POSITION_UUID = "positionUuid";
  /** 结算周期名称(String) */
  public static final String FILTER_SETTLEMENT_CAPTION = "settlementCaption";
  /** 出账方式(String) */
  public static final String FILTER_BILL_CALCULATE_TYPE = "billCalculateType";
  /** 账单类型(String) */
  public static final String FILTER_STATEMENT_TYPE = "statementType";
  /** 发票登记(String) */
  public static final String FILTER_INVOICEREG = "invoiceReg";
  /** 是否已出账(String) */
  public static final String FILTER_ACCOUNTED = "accounted";
  /** 楼宇(String) */
  public static final String FILTER_BUILDING_FIELD = "buildingField";
  /** 位置(String) */
  public static final String FILTER_POSITION_FIELD = "positionField";
  /** 账款状态(String) */
  public static final String FILTER_SETTLESTATE = "settleState";
  /** 应付金额(Array) */
  public static final String FILTER_PAYTOTAL = "payTotal";
  /** 应收金额(Array) */
  public static final String FILTER_RECEIPTTOTAL = "receiptTotal";

  /** =========================公共排序字段============================ */
  /** 单号 */
  public static final String ORDER_BY_BILLNUMBER = "billNumber";
  /** 对方单位 */
  public static final String ORDER_BY_COUNTERPART = "counterpart";
  /** 合同 */
  public static final String ORDER_BY_CONTRACT = "contract";
  /** 合作方式 */
  public static final String ORDER_BY_COOPMODE = "coopMode";

  /** 项目 */
  public static final String ORDER_BY_STORE = "store";
  /** 结转期(String) */
  public static final String ORDER_BY_SETTLENO = "settleNo";
  /** 账单(String) */
  public static final String ORDER_BY_STATEMENTSTATE = "statementState";
  // =========================出账日志排序字段============================
  /** 项目 */
  public static final String ORDER_BY_ACCOUNTUUID = "accountUnit";
  /** 楼层 */
  public static final String ORDER_BY_FLOOR = "floor";
  /** 结算周期名称 */
  public static final String ORDER_BY_CAPTION = "settlementCaption";
  /** 周期起始日期 */
  public static final String ORDER_BY_BEGINDATE = "settlementDateRange";
  /** 计划出账日期 */
  public static final String ORDER_BY_PLAN_DATE = "planDate";
  /** 出账时间 */
  public static final String ORDER_BY_ACCOUNTTIME = "accountTime";
  /** 出账方式(String) */
  public static final String ORDER_BY_CALCULATE_TYPE = "billCalculateType";
}
