/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	AccountSourceType.java
 * 模块说明：	
 * 修改历史：
 * 2015-3-17 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.acc.client;

/**
 * 账款来源类型
 * 
 * @author huangjunxian
 * 
 */
public class BAccountSourceType {
  /** 账款金额限制 */
  public static final String amountLimit = "amountLimit";
  /** 按科目取最大值 */
  public static final String maxSubject = "maxSubject";
  /** 平衡清算 */
  public static final String liquidate = "liquidate";
  /** 会员折扣分摊 */
  public static final String memberApportion = "memberApportion";
  /** 积分分摊 */
  public static final String scoreApportion = "scoreApportion";
  /** 商品类别超额分段比例提成 */
  public static final String abovePieceCategory = "abovePieceCategory";
  /** 商品类别销售固定比例提成 */
  public static final String normalCategory = "normalCategory";
  /** 商品类别销售分段比例提成 */
  public static final String pieceCategory = "pieceCategory";
  /** 销售固定比例提成 */
  public static final String normalSaleRate = "normalSaleRate";
  /** 超额分段销售比例提成 */
  public static final String abovePieceSaleRate = "abovePieceSaleRate";
  /** 分段销售比例提成 */
  public static final String pieceSaleRate = "pieceSaleRate";
  /** 银行手续扣率 */
  public static final String bankRate = "bankRate";
  /** 付款方式扣率 */
  public static final String paymentTypeRate = "paymentTypeRate";
  /** 日面积单价 */
  public static final String areaPrice = "areaPrice";
  /** 销售额周期保底 */
  public static final String cycleMinSaleRate = "cycleMinSaleRate";
  /** 销售提成周期保底 */
  public static final String cycleMinSaleDeduct = "cycleMinSaleDeduct";
  /** 日固定金额 */
  public static final String dayFixed = "dayFixed";
  /** 账款取最大值 */
  public static final String maximum = "maximum";
  /** 销售额保底 */
  public static final String minSaleRate = "minSaleRate";
  /** 销售提成保底 */
  public static final String minSaleDeduct = "minSaleDeduct";
  /** 月固定金额 */
  public static final String monthFixed = "monthFixed";
  /** 时段总额 */
  public static final String periodTotal = "periodTotal";
  /** 应付款返款 */
  public static final String rebate = "rebate";
  /** 账款合计 */
  public static final String summary = "summary";
  /** 日单价年递增 */
  public static final String yearGrowthDayPrice = "yearGrowthDayPrice";
  /** 月单价年递增 */
  public static final String yearGrowthMonthPrice = "yearGrowthMonthPrice";
  /** 促销结算条款 */
  public static final String prom = "prom";
  /** 调租条款 */
  public static final String adjustAccount = "adjustAccount";
  /** 其它 */
  public static final String other = "other";
}
