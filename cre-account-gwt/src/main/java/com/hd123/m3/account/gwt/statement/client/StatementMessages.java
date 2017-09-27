/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementMessages.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author huangjunxian
 * 
 */
public interface StatementMessages extends CommonsMessages {
  public static StatementMessages M = GWT.create(StatementMessages.class);

  @DefaultMessage("单据类型")
  String billType();

  @DefaultMessage("当前结转期")
  String monthSettle();

  @DefaultMessage("...")
  String doing();

  @DefaultMessage("提交")
  String submit();

  @DefaultMessage("恢复出账")
  String cleanSettle();

  @DefaultMessage("审核")
  String audit();

  @DefaultMessage("拖欠金额")
  String owedAmount();

  @DefaultMessage("添加")
  String add();

  @DefaultMessage("设置最后缴款日")
  String setLastPayDate();

  @DefaultMessage("搜索条件")
  String search_caption();

  @DefaultMessage("审核不通过")
  String rejected();

  @DefaultMessage("发送")
  String send();

  @DefaultMessage("发送说明")
  String send_remark();

  @DefaultMessage("确认不通过")
  String refused_confirm();

  @DefaultMessage("确认")
  String confirm();

  @DefaultMessage("确认说明")
  String confirm_remark();

  @DefaultMessage("作废")
  String abort();

  @DefaultMessage("作废原因")
  String abort_reason();

  @DefaultMessage("出账")
  String calc();

  @DefaultMessage("付款")
  String pay();

  @DefaultMessage("付款发票登记")
  String payIvc();

  @DefaultMessage("收款")
  String receipt();

  @DefaultMessage("收款发票登记")
  String receiptIvc();

  @DefaultMessage("调整")
  String adjust();

  @DefaultMessage("科目收付情况")
  String subjectInfo();

  @DefaultMessage("科目账款结算情况")
  String subject_accountInfo();

  @DefaultMessage("科目账款结算情况：{0}")
  String subject_accInfoCaption(String value);

  @DefaultMessage("请选择：")
  String choose();

  @DefaultMessage("代码")
  String code();

  @DefaultMessage("名称")
  String name();

  @DefaultMessage("说明")
  String remark();

  @DefaultMessage("授权组")
  String permGroup();

  @DefaultMessage("账款")
  String account();

  @DefaultMessage("账款科目")
  String account_Subject();

  @DefaultMessage("账款明细")
  String account_detail();

  @DefaultMessage("账款金额限制条款")
  String amountLimit();

  @DefaultMessage("最大值条款")
  String max_subject_info();

  @DefaultMessage("平衡清算条款")
  String liquidate_info();

  @DefaultMessage("编辑账款")
  String account_edit();

  @DefaultMessage("添加账款")
  String account_add();

  @DefaultMessage("账款金额")
  String account_total();

  @DefaultMessage("账单结算")
  String statementSettle();

  @DefaultMessage("发票登记")
  String invoiceReg();

  @DefaultMessage("已结清")
  String cleaned();

  @DefaultMessage("未结清")
  String unCleaned();

  @DefaultMessage("已完成")
  String finished();

  @DefaultMessage("未完成")
  String unFinished();

  @DefaultMessage("更多")
  String more();

  @DefaultMessage("原因")
  String reason();

  @DefaultMessage("账单信息")
  String statementInfo();

  @DefaultMessage("账款合计")
  String accountTotalInfo();

  @DefaultMessage("账款信息")
  String accountInfo();

  @DefaultMessage("红色标记为由最大值或平衡清算条款产生。")
  String accInfo_remark();

  @DefaultMessage("存在无效账款！")
  String exists_desActiveAcc();

  @DefaultMessage("无效账款")
  String desActiveAcc();

  @DefaultMessage("应付合计")
  String payTotalInfo();

  @DefaultMessage("应收合计")
  String receiptTotalInfo();

  @DefaultMessage("应付款金额/税额")
  String field_payTotal();

  @DefaultMessage("应付开票金额/税额")
  String field_ivcPayTotal();

  @DefaultMessage("应收款金额/税额")
  String field_receiptTotal();

  @DefaultMessage("应收开票金额/税额")
  String field_ivcReceiptTotal();

  @DefaultMessage("行号")
  String lineNumber();

  @DefaultMessage("是")
  String yes();

  @DefaultMessage("否")
  String no();

  @DefaultMessage(" 到 ")
  String to();

  @DefaultMessage("{0} 等于")
  String captionEquals(String caption);

  @DefaultMessage("全部")
  String all();

  @DefaultMessage("是否开票")
  String isInvoiced();

  @DefaultMessage("起止日期")
  String dateRange();

  @DefaultMessage("来源单据类型 等于")
  String filter_billType();

  @DefaultMessage("来源单号 起始于")
  String filter_billNumber();

  @DefaultMessage("账款科目代码 类似于")
  String filter_subjectCode();

  @DefaultMessage("账款科目名称 类似于")
  String filter_subjectName();

  @DefaultMessage("账期时间 介于")
  String filter_dateRange();

  @DefaultMessage("账款名称：{0}")
  String acc_caption(String caption);

  @DefaultMessage("账款金额：{0}")
  String acc_total(String total);

  @DefaultMessage("免租时段：{0}，免租比例：{1}，免租金额：{2}")
  String acc_freeTotalDetail(String period, String rate, String total);

  @DefaultMessage("免租金额合计：{0}")
  String acc_freeTotal(String freeTotal);

  @DefaultMessage("账款有效期：{0}")
  String acc_dateRange(String dateRange);

  @DefaultMessage("免租期：{0}")
  String acc_freeDateRange(String freeDateRange);

  @DefaultMessage("免租比例：{0}")
  String acc_freePercent(String freePercent);

  @DefaultMessage("没有结算情况...")
  String acc_notExists();

  @DefaultMessage("-")
  String self_billNumber();

  @DefaultMessage("本账单")
  String self_caption();

  @DefaultMessage("{0}：{1}")
  String pair_value(String caption, String value);

  @DefaultMessage("第 {0} 行")
  String caption_line_no(String lineNo);

  @DefaultMessage("店招")
  String contractTitle();

  @DefaultMessage("合同编号")
  String contractBillNumber();

  @DefaultMessage("楼层")
  String floor();

  @DefaultMessage("楼层代码")
  String floorCode();

  @DefaultMessage("楼层名称")
  String floorName();

  @DefaultMessage("合作方式")
  String coopMode();

  @DefaultMessage("结算周期名称")
  String settlementCaption();

  @DefaultMessage("结算周期起止日期")
  String settlementRangeDate();

  @DefaultMessage("结算周期起始日期")
  String settlementBeginDate();

  @DefaultMessage("结算周期截止日期")
  String settlementEndDate();

  @DefaultMessage("计划出账日期")
  String planDate();

  @DefaultMessage("出账时间")
  String accountTime();

  @DefaultMessage("出账方式")
  String billCalculateType();

  @DefaultMessage("账单")
  String statement();

  @DefaultMessage("账单单号")
  String statementBillNumber();

  @DefaultMessage("位置")
  String position();

  @DefaultMessage("位置代码")
  String positionCode();

  @DefaultMessage("位置名称")
  String positionName();

  @DefaultMessage("自动")
  String auto();

  @DefaultMessage("手工")
  String manul();

  @DefaultMessage("已出账")
  String isAccountSettel();

  @DefaultMessage("出账日志")
  String accountSettelLog();

  @DefaultMessage("出账周期")
  String accountRange();

  @DefaultMessage("账单出账日志")
  String statementAccountSettelLog();

  @DefaultMessage("应付金额/税额")
  String payTotal();

  @DefaultMessage("总金额/税额")
  String total();

  @DefaultMessage("应收金额/税额")
  String receiptTotal();

  @DefaultMessage("合计")
  String sumTotal();

  @DefaultMessage("验证项目与商户")
  String validate();

  @DefaultMessage("重新选择合同会清空账单明细，是否继续？")
  String selectContract();

  @DefaultMessage("重新选择项目会清空账单明细，是否继续？")
  String selectAccountUnit();

  @DefaultMessage("重新选择商户会清空账单明细，是否继续？")
  String selectCounterpart();

  @DefaultMessage("账单明细")
  String statementLine();

  @DefaultMessage("附件")
  String attachement();

  @DefaultMessage("在上方插入行")
  String insertLine();

  @DefaultMessage("账单号")
  String billNumber();

  @DefaultMessage("开发票")
  String invoice();

  @DefaultMessage("搜索科目...")
  String search_subject();

  @DefaultMessage("明细行")
  String detailLines();

  @DefaultMessage("参考信息")
  String reference();

  @DefaultMessage("销售额")
  String saleTotal();
}
