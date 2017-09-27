/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	OptionMessages.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.util.client.GwtUrl;

/**
 * @author zhuhairui
 * 
 */
public interface OptionMessages extends CommonsMessages {

  public static OptionMessages M = GWT.create(OptionMessages.class);

  @DefaultMessage("[空]")
  String empty_text();

  @DefaultMessage("结算配置")
  String moduleCaption();

  @DefaultMessage("配置")
  String config();

  @DefaultMessage("默认值")
  String defaultValue();

  @DefaultMessage("启用对账组限制")
  String useAccountGroup();

  @DefaultMessage("启用对账组限制后，相应的账务对账单据只能由该对账组的员工查看和操作。")
  String useAccountGroupMessage();

  @DefaultMessage("启用结算组限制")
  String useSettleGroup();

  @DefaultMessage("启用结算组限制后，账款登记以及收付款只能由该结算组的员工查看和操作。")
  String useSettleGroupMessage();

  @DefaultMessage("账务月天数")
  String monthDays();

  @DefaultMessage("实际月天数")
  String actualMonthDays();

  @DefaultMessage("月末天数按财务月计算")
  String incompleteMonthByRealDays();

  @DefaultMessage("启用月末天数按财务月计算后，非整月天数=财务月天数-(实际月天数-实际非整月天数)。")
  String incompleteMonthByRealDaysMessage();

  @DefaultMessage("收款单银行是否必填")
  String receiptBankRequired();

  @DefaultMessage("是否启用外部发票系统")
  String extinvSystemRequired();
  
  @DefaultMessage("是否启用核算主体")
  String accObjectEnbled();
  
  @DefaultMessage("是否启用保证金按单管理")
  String byDepositEnabled();
  
  @DefaultMessage("已终止合同产生0账单")
  String genZeroStatement();

  @DefaultMessage("启用已终止合同产生0账单后，相应的已终止合同出账过程无论存不存在账款信息，都会生成空白账单。")
  String genZeroStatementMessage();

  @DefaultMessage("计算精度")
  String calculationAccuracy();

  @DefaultMessage("{0}位小数")
  String scale(String scale);

  @DefaultMessage("舍入算法")
  String roundingMode();

  @DefaultMessage("手续费计算方式")
  String feeMode();

  @DefaultMessage("财务付款方式")
  String paymentType();

  @DefaultMessage("税率")
  String taxRate();

  @DefaultMessage("预存款科目")
  String preReceiveSubject();

  @DefaultMessage("预付款科目")
  String prePaySubject();

  @DefaultMessage("取消")
  String cancel();

  @DefaultMessage("无法导航到指定模块({0})。")
  String navigateError(GwtUrl url);

  @DefaultMessage("显示{0}页面时发生错误。")
  String showPageError(String page);

  @DefaultMessage("账单流程定义路径")
  String statementBPMPath();

  @DefaultMessage("账单默认流程")
  String statementDefaultBPM();

  @DefaultMessage("收款单默认收款方式")
  String defalutReceiptPaymentType();

  @DefaultMessage("收款发票登记单默认发票类型")
  String defalutInvoiceType();

  @DefaultMessage("收付款通知单流程定义路径")
  String paymentNoticeBPMPath();

  @DefaultMessage("收付款通知单默认流程")
  String paymentNoticeDefaultBPM();

  @DefaultMessage("请重新填写！")
  String writeAgain();

  @DefaultMessage("BPM流程")
  String bPM();

  @DefaultMessage("设置计划出账日期在每月的几号到月末之间，账单结转期记作下个月的结转期。")
  String settleNoRuleRemark();

  @DefaultMessage("每月{0}号到月末")
  String settleNoRuleValue(int day);

  @DefaultMessage("账单结转期规则")
  String settleNoRule();

  @DefaultMessage("每月")
  String perMonth();

  @DefaultMessage("号")
  String day();

  @DefaultMessage("到月末")
  String toLastDayOfMonth();

  @DefaultMessage("收款单滞纳金默认值")
  String receiptOverdueDefaults();

  @DefaultMessage("删除")
  String remove();

  @DefaultMessage("获取项目财务月天数配置项错误。")
  String getStoreMonthDaysError();

  @DefaultMessage("保存项目财务月天数配置项错误。")
  String saveIncompleteMonthByRealDaysError();

  @DefaultMessage("获取项目月末天数按财务月计算错误。")
  String getIncompleteMonthByRealDaysError();

  @DefaultMessage("保存项目月末天数按财务月计算错误。")
  String saveStoreMonthDaysError();

  @DefaultMessage("获取返款单是否按笔返款错误")
  String getRebateByBillOptionsError();

  @DefaultMessage("保存返款单是否按笔返款错误")
  String saveRebateByBillOptionsError();

  @DefaultMessage("按项目设置")
  String setByStore();

  @DefaultMessage("添加项目...")
  String addStore();

  @DefaultMessage("确定")
  String ok();

  @DefaultMessage("财务月天数：不能为空")
  String monthDaysIsNull();

  @DefaultMessage("收款单长短款配置")
  String receiptDiffAmount();

  @DefaultMessage("长短款付款方式限额")
  String paymentTypeLimits();

  @DefaultMessage("{0}:必须大于0")
  String paymentTypeLimitsNotNull(String caption);

  @DefaultMessage("返款单是否按笔返款")
  String rebateByBill();
  
  @DefaultMessage("收款金额默认等于应收金额")
  String receiptEqualsUnpayed();
  
}
