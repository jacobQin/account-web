/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentMessages.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client;

import java.math.BigDecimal;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author subinzhu
 * 
 */
public interface ReceiptMessages extends CommonsMessages {
  public static ReceiptMessages M = GWT.create(ReceiptMessages.class);

  @DefaultMessage("收款单")
  String receipt();

  @DefaultMessage("合计")
  String accountTotal();

  @DefaultMessage("应收/实收金额")
  String shouldRecAndRecTotal();

  @DefaultMessage("应收金额")
  String shouldReceiveTotal();

  @DefaultMessage("本次结算金额/实收金额")
  String currentRecAndRecTotal();

  @DefaultMessage("产生预存款")
  String generateRec();

  @DefaultMessage("改变{0}会清空所有账款信息以及收款信息，确定需要继续吗？")
  String changeRecConfirm(String field);

  @DefaultMessage("改变{0}会清空所有账款信息以及付款信息，确定需要继续吗？")
  String changeConfirm(String field);

  @DefaultMessage("第 {0} 行")
  String lineNumber(int line);

  @DefaultMessage("单据")
  String bill();

  @DefaultMessage("收款金额")
  String receiptTotal();

  @DefaultMessage("余额")
  String remainTotal();

  @DefaultMessage("结算组")
  String settleGroup();

  @DefaultMessage("从账单导入...")
  String statementImport();

  @DefaultMessage("从发票登记导入...")
  String invoiceRegImport();

  @DefaultMessage("从收付款通知单导入...")
  String paymentNoticeImport();

  @DefaultMessage("从来源单据导入...")
  String sourceBillImport();

  @DefaultMessage("从账款导入...")
  String accountImport();

  @DefaultMessage("更多")
  String more();

  @DefaultMessage("操作")
  String operate();

  @DefaultMessage("作废")
  String abort();

  @DefaultMessage("审核意见")
  String approveOpinion();

  @DefaultMessage("复核意见")
  String reCheckOpinion();

  @DefaultMessage("作废原因")
  String abortReason();

  @DefaultMessage("正在{0}“{1}”...")
  String beDoing(String action, String entityCaption);

  @DefaultMessage("{0}{1}“{2}”失败!")
  String actionFailed2(String action, String entityCaption, String entityStr);

  @DefaultMessage("收款单新建页面")
  String receiptCreatePage();

  @DefaultMessage("收款单编辑页面")
  String receiptEditPage();

  @DefaultMessage("{0}不允许为空。")
  String notNull(String captionName);

  @DefaultMessage("第{0}行的{1}不能为空。")
  String lineNotNull(int i, String fieldName);

  @DefaultMessage("{0}:第{1}行的{2}不能为空。")
  String lineNotNull2(String caption, int i, String fieldName);

  @DefaultMessage("第{0}行的{1}必须大于0。")
  String minValue(int i, String fieldName);

  @DefaultMessage("{0}：{1}必须大于{2}。")
  String minValue2(String caption, String a, String b);

  @DefaultMessage("{0}不能小于{1}。")
  String cannotLessThan(String a, String b);

  @DefaultMessage("{0}：第{1}行的{2}不能小于{3}。")
  String cannotLessThan2(String caption, int i, String a, String b);
  
  @DefaultMessage("{0}：不能小于{1}。")
  String cannotLessThan3(String caption, int i);

  @DefaultMessage("{0}必须大于{1}。")
  String mustMoreThan(String a, String b);

  @DefaultMessage("{0}：第{1}行的{2}必须大于{3}。")
  String mustMoreThan2(String caption, int i, String a, String b);

  @DefaultMessage("{0}：第{1}行的{2}不能大于{3}。")
  String cannotMoreThan2(String caption, int i, String a, String b);

  @DefaultMessage("{0}：第{1}行的{2}的绝对值不能大于{3}的绝对值。")
  String absCannotMoreThan2(String caption, int i, String a, String b);

  @DefaultMessage("{0}不能大于{1}。")
  String cannotMoreThan(String a, String b);

  @DefaultMessage("{0}的绝对值不能大于{1}的绝对值。")
  String absCannotMoreThan(String a, String b);

  @DefaultMessage("{0}必须等于{1}。")
  String mustBeEquals(String a, String b);

  @DefaultMessage("{0}不能等于{1}。")
  String cannotBeEquals(String a, String b);

  @DefaultMessage("{0}{1}“{2}”过程中发生错误")
  String processError2(String action, String entityCaption, String entityStr);

  @DefaultMessage("账款抵扣")
  String deduction();

  @DefaultMessage("预存款冲扣（合同）")
  String depositRecHasContract();

  @DefaultMessage("预存款冲扣（无合同）")
  String depositRecHasNoContract();

  @DefaultMessage("请先填写{0}")
  String pleaseFillInFirst(String field);

  @DefaultMessage("正在加载{0}...")
  String loading2(String entityCaption);

  @DefaultMessage("滞纳金条款")
  String overdueTerm();

  @DefaultMessage("滞纳金")
  String overdue();

  @DefaultMessage("计算金额")
  String calculate_total();

  @DefaultMessage("当前选中的行")
  String selectedRows();

  @DefaultMessage("结算单位")
  String accountUnit();

  @DefaultMessage("改变后会清空所有收款信息，确定需要继续吗？")
  String paymentChangeConfirm();

  @DefaultMessage("预存款科目")
  String depositRecSubject();

  @DefaultMessage("{0}：与第{1}行重复。")
  String lineRepeatError(String caption, int line);

  @DefaultMessage("[科目+合同]与第{0}行重复。")
  String repeatError(int line);

  @DefaultMessage("{0}{1}成功!")
  String actionSuccess(String action, String entityCaption);

  @DefaultMessage("正负符号必须和{0}一致。")
  String signSymbolMustBeSameAs(String target);

  @DefaultMessage("{0}：第{1}行的{2}的收付方向必须和{3}一致。")
  String signSymbolMustBeSameAs2(String caption, int line, String field1, String field2);

  @DefaultMessage("{0}和{1}的收付方向必须一致。")
  String signSymbolMustBeSameAs3(String a, String b);

  @DefaultMessage("实收金额必须大于等于0。")
  String defrayalTotalZero_error();

  @DefaultMessage("应收金额/税额")
  String originTotal();

  @DefaultMessage("应收金额")
  String originTotal_total();

  @DefaultMessage("应收税额")
  String originTotal_tax();

  @DefaultMessage("本次应收金额")
  String unpayedTotal_total();

  @DefaultMessage("未收金额/税额")
  String leftTotal();

  @DefaultMessage("未收金额")
  String leftTotal_total();

  @DefaultMessage("未收税额")
  String leftTotal_tax();

  @DefaultMessage("滞纳金金额")
  String overdue_total();

  @DefaultMessage("收款方式")
  String paymentType();

  @DefaultMessage("[科目+合同]")
  String subjectAndContract();

  @DefaultMessage("{0}：第{1}行的{2}超过最大长度128。")
  String remarkError(String caption, int i, String field);

  @DefaultMessage("合同")
  String contract();

  @DefaultMessage("该收款单产生了预存款金额，金额为{0}，确定要保存吗？")
  String saveRecConfirm(double value);

  @DefaultMessage("该付款单产生了预付款金额，金额为{0}，确定要保存吗？")
  String savePayConfirm(double value);

  @DefaultMessage("单据类型")
  String billType();

  @DefaultMessage("默认配置")
  String defaultOption();

  @DefaultMessage("银行列表")
  String banks();
  
  @DefaultMessage("银行")
  String bank();

  @DefaultMessage("当前员工")
  String currentEmployee();

  @DefaultMessage("长度")
  String length();

  @DefaultMessage("发票登记")
  String ivcRegiste();

  @DefaultMessage("收款单单号")
  String receiptNumber();

  @DefaultMessage("项目")
  String store();

  @DefaultMessage("商户")
  String counterpart();

  @DefaultMessage("收款日期")
  String receiptDate();

  @DefaultMessage("发票信息")
  String invoiceInfo();

  @DefaultMessage("登记信息")
  String registeInfo();

  @DefaultMessage("发票类型")
  String ivcType();

  @DefaultMessage("开票日期")
  String ivcDate();

  @DefaultMessage("发票代码")
  String ivcCode();

  @DefaultMessage("发票号码")
  String ivcNumber();

  @DefaultMessage("开票金额")
  String ivcTotal();

  @DefaultMessage("登记发票")
  String registe();
  
  @DefaultMessage("保存发票")
  String saveInvoice();

  @DefaultMessage("没有可以进行发票登记的账款明细。")
  String noRegisteAccounts();

  @DefaultMessage("收款发票登记单配置")
  String ivcRegConfig();

  @DefaultMessage("[空]")
  String nullOption();

  @DefaultMessage("{0}不允许为空。")
  String notNullField(String field);

  @DefaultMessage("开票金额不允许等于0。")
  String ivcTotalNoZero();

  @DefaultMessage("开票金额与账款实收方向不一致。")
  String ivcTotalDirectionError();

  @DefaultMessage("开票金额-账款实收={0}，不在收款发票登记单选项配置的金额范围内。")
  String noValidTotalRange(String total);

  @DefaultMessage("开票税额-账款实收税额={0}，不在收款发票登记单选项配置的税额范围内。")
  String noValidTaxRange(String tax);
  
  @DefaultMessage("收款金额和扣减预收金额之和不能为负数。")
  String forbidNegative();
  
  @DefaultMessage("收款金额和扣减预收金额之和应属于[{0},0]。")
  String totalBetween(String s);
  
  @DefaultMessage("存在代收明细，账款明细金额总和应该大于等于0")
  String existCollectionLines();
  
  @DefaultMessage("{0}和{1}符号必须一致。")
  String sameSign(String field1,String field2);
  
  @DefaultMessage("{0}不能等于0。")
  String totalZero_error(String s);
  
  @DefaultMessage("保存并登记")
  String saveRegiter();
  
  @DefaultMessage("返回")
  String returnButton();
  
  @DefaultMessage("{0}：第{1}行{2}")
  String indexError(String caption, int a, String b);
  
  @DefaultMessage("本次应收金额为负数，不能填写扣预存款")
  String noDeposit();
  
  @DefaultMessage("应收金额为负数，不能填写扣预存款")
  String lineNoDeposit();
  
  @DefaultMessage("代收明细")
  String collectionDetail();
  
  @DefaultMessage("起始日期不能晚于截止日期。")
  String beginDateAfterEndDate();
  
  @DefaultMessage("截止日期不能早于起始日期。")
  String endDateBeforeBeginDate();
  
  @DefaultMessage("账款实收金额")
  String accRealReceiveTotal();
  
  @DefaultMessage("发票代码")
  String invoiceCode();
  
  @DefaultMessage("发票号码")
  String invoiceNumber();
  
  @DefaultMessage("付款方式{0}超出长短款金额限制{1}")
  String errorPaymentLimit(String paymentType,BigDecimal total);
  
  @DefaultMessage("{0}：第{1}行的付款方式{2}超出长短款金额限制{3}。")
  String lineErrorPaymentLimit(String caption, int i, String a, String b);
  
  @DefaultMessage("付款方式{0}总额超出长短款金额限制{1}。")
  String errorTotalPaymentLimit(String payType, BigDecimal total);
  
  @DefaultMessage("{0}：付款方式{1}总额不能超出长短款金额限制{2}。")
  String lineTotalErrorPaymentLimit(String caption, String payType, BigDecimal b);
  
  @DefaultMessage("{0}：{1}不能大于{2}。")
  String cannotMoreThanRemainTotal(String caption, String a, String b);
}
