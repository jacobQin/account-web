package com.hd123.m3.account.gwt.payment.rec.client.biz;

public class ActionName {
  /** 付款方式改变 */
  public static final String ACTION_LINEOVERDUELINE_CASHDEFRAYALCHANGE = "lineOverdueLine_cashDefrayalChange";
  /** 预付款冲扣改变 */
  public static final String ACTION_LINEOVERDUELINE_DEPOSITDEFRAYALCHANGE = "lineOverdueLine_depositDefrayalChange";

  /** 换行 */
  public static final String ACTION_LINEACCOUNTLINE_CHANGELINENO = "lineAccountLine_changeLineNo";

  /** 选择第一行 */
  public static final String ACTION_LINEACCOUNTLINE_SELECT_FIRST = "lineAccountLine_selectFirst";
  /** 滞纳金金额改变 */
  public static final String ACTION_LINEACCOUNTLINE_OVERDUETOTALCHANGE = "lineAccountLine_overdueTotalChange";
  /** 预付款冲扣改变 */
  public static final String ACTION_LINEACCOUNTLINE_DEPOSITDEFRAYALCHANGE = "lineAccountLine_depositDefrayalChange";
  /** 合计 */
  public static final String ACTION_OVERDUELINE_AGGREGATE = "overdueLine_aggregate";

  /** 改变收款日期 */
  public static final String ACTION_GENERALEDIT_RECEIVEDATECHANGED = "GENERALEDIT_receiveDateChanged";
  /** 批量删除行 */
  public static final String BATCH_DELETE_EDIT = "batch_delete_value_edit";

  /** 增加一行 */
  public static final String ACTION_LINECASHDEFRAYALLINE_ADDDETAIL = "LINECASHDEFRAYALLINE_ADD_DETAIL";
  /** 删除一行 */
  public static final String ACTION_LINECASHDEFRAYALLINE_REMOVEDETAIL = "LINECASHDEFRAYALLINE_REMOVE_DETAIL";

  // 改造后用到
  /** 分摊后刷新相关控件，包括基本信息合计，产生预存款，滞纳金 */
  public static final String ACTION_REFRESH_AFFTER_APPORTION = "refresh_after_apportion";
  /** 改变付款金额值 */
  public static final String ACTION_PAYMENTINFO_CHANGE = "paymentinfo_change";
  /** 账款发生删除、添加需要重新分摊计算 */
  public static final String ACTION_ACCOUNTLINE_CHANGE = "account_line_change";
  /** 按总额收款时：触发此事件进行重新分摊 */
  public static final String ACTION_SHARE_CALCULATION = "shareCalculation";
  /**账款明细收款金额发生变化触发此事件*/
  public static final String ACTION_RECEIVABLE_CHANGE = "receivableChange";
  /** 改变滞纳金金额 */
  public static final String ACTION_BILLOVERDUELINE_UNPAYEDTOTALCHANGE = "billOverdueLine_unpayedTotalChange";
  /** 改变滞纳金金额 */
  public static final String ACTION_BILLACCOUNTLINE_CHANGE = "billAccountLine_change";
  /** 改变商户后，刷新预存款科目余额 （明细行） */
  public static final String ACTION_GENERALCREATE_REFRESH_REMAINTOTAL = "GENERALCREATE_refresh_remainTotal";
  /** 改变商户后，刷新科目以及合同下拉控件值 */
  public static final String ACTION_GENERALCREATE_REFRESH_SUBJECTS_AND_CONTRACTS = "GENERALCREATE_refresh_subjects_and_contracts";
  /** 修改项目,刷新银行资料 */
  public static final String ACTION_GENERALCREATE_REFRESH_BANKS = "GENERALCREATE_refresh_banks";
  /** 改变商户 */
  public static final String ACTION_GENERALCREATE_COUONTERPARTCHANGED = "GENERALCREATE_counterpartChanged";
  /** 改变收款方式 */
  public static final String ACTION_GENERALCREATE_DEFRAYALTYPECHANGED = "GENERALCREATE_defrayalTypeChanged";
  /** 改变实收金额 */
  public static final String ACTION_LINEACCOUNTLINE_TOTALCHANGE = "lineAccountLine_totalChange";
  /** 改变明细行收款值 */
  public static final String ACTION_LINECASHDEFRAYALLINE_CHANGE = "lineCashDefrayalLine_change";
  /** 付款明细中付款方式/金额改变 */
  public static final String ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE = "lineAccountLine_cashDefrayalChange";
  /** 删除操作后重新合计 */
  public static final String ACTION_ACCOUNTLINE_AGGREGATE = "accountLine_aggregate";
  /** 改变收款日期 */
  public static final String ACTION_GENERALCREATE_RECEIVEDATECHANGED = "GENERALCREATE_receiveDateChanged";
  /** 换行 */
  public static final String ACTION_ACCOUNTLINE_CHANGELINENO = "accountLine_changeLineNo";
  /** 改变滞纳金金额 */
  public static final String ACTION_BILLACCOUNTLINE_CHANGE2 = "billAccountLine_change2";
  /** 增加账款行，并且账款滞纳金科目存在 */
  public static final String ACTION_BILLACCOUNTLINE2_ADD = "billAccountLine2_add";
  /** 按科目多分类改变值 */
  public static final String ACTION_LINECASHDEFRAYAL_CHANGE = "lineCashDefrayal_change";
  /** 改变明细行说明值 */
  public static final String ACTION_LINEACCOUNTLINE_CHANGE = "lineAccountLine_change";
  /** 上一行 */
  public static final String ACTION_LINEACCOUNTLINE_PREV = "lineAccountLine_prev";
  /** 下一行 */
  public static final String ACTION_LINEACCOUNTLINE_NEXT = "lineAccountLine_next";
  /** 刷新收款单的滞纳金明细行 */
  public static final String ACTION_ACCOUNTLINE_REFRESHOVERDUELINES = "accountLine_removeOverdueLines";
  /** 删除 */
  public static final String ACTION_LINEACCOUNTLINE_DELETE = "lineAccountLine_delete";
  /** 改变行 */
  public static final String CREATEPAGE_CHANGE_LINE = "createPage_change_line";
  /** 改变行 */
  public static final String EDITPAGE_CHANGE_LINE = "editPage_change_line";
  /** 改变行以及定位收付明细行 */
  public static final String EDITPAGE_CHANGE_LINE_AND_LINEDEFRAYALLINE = "editPage_change_line_and_lineDefrayalLine";
  /** 改变行以及定位收付明细行 */
  public static final String CREATEPAGE_CHANGE_LINE_AND_LINEDEFRAYALLINE = "createPage_change_line_and_lineDefrayalLine";
  /** 改变本次结算金额 */
  public static final String ACTION_BILLOVERDUELINE_TOTALCHANGE = "billOverdueLine_totalChange";
  /** 改变值 */
  public static final String ACTION_CASHDEFRAYALLINE_TOTALCHANGE = "cashDefrayalLine_change";
  /** 增加一行 */
  public static final String ACTION_CASHDEFRAYALLINE_ADDDETAIL = "cashDefrayalLine_ADD_DETAIL";
  /** 删除一行 */
  public static final String ACTION_CASHDEFRAYALLINE_REMOVEDETAIL = "cashDefrayalLine_REMOVE_DETAIL";
  /** 改变值 */
  public static final String ACTION_CASHDEFRAYAL_CHANGE = "cashDefrayal_change";
  /** 改变商户 */
  public static final String ACTION_GENERALCREATE_COUNTERPARTCHANGED = "GENERALCREATE_counterpartChanged";
  /** 批量删除行 */
  public static final String BATCH_DELETE = "batch_delete_value_create";
  /** 收款单-代收明细-赋值合同 */
  public static final String ACTION_SET_CONTRACT = "set_contract";
}
