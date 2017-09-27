/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentMessages.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author subinzhu
 * 
 */
public interface PaymentMessages extends CommonsMessages {
  public static PaymentMessages M = GWT.create(PaymentMessages.class);

  @DefaultMessage("付款日期")
  String paymentDate();

  @DefaultMessage("合计")
  String accountTotal();

  @DefaultMessage("应收金额")
  String receiptTotal();

  @DefaultMessage("应付/实付金额")
  String shouldPayAndPayTotal();

  @DefaultMessage("应付金额")
  String shouldPayTotal();

  @DefaultMessage("产生预付款")
  String generatePay();

  @DefaultMessage("改变{0}会清空所有账款信息以及收款信息，确定需要继续吗？")
  String changeRecConfirm(String field);

  @DefaultMessage("改变{0}会清空所有账款信息以及付款信息，确定需要继续吗？")
  String changeConfirm(String field);

  @DefaultMessage("第 {0} 行")
  String lineNumber(int line);

  @DefaultMessage("单据")
  String bill();

  @DefaultMessage("付款金额")
  String paymentTotal();

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

  @DefaultMessage("付款信息")
  String paymentInfo();

  @DefaultMessage("付款单新建页面")
  String paymentCreatePage();

  @DefaultMessage("付款单编辑页面")
  String paymentEditPage();

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

  @DefaultMessage("预付款冲扣（合同）")
  String depositPayHasContract();

  @DefaultMessage("预付款冲扣（无合同）")
  String depositPayHasNoContract();

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

  @DefaultMessage("改变后会清空所有付款信息，确定需要继续吗？")
  String paymentChangeConfirm();

  @DefaultMessage("预付款科目")
  String depositPaySubject();

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

  @DefaultMessage("付款单")
  String payment();

  @DefaultMessage("本次应付金额/税额")
  String unpayedTotal();

  @DefaultMessage("应付金额/税额")
  String originTotal();

  @DefaultMessage("应付金额")
  String originTotal_total();

  @DefaultMessage("应付税额")
  String originTotal_tax();

  @DefaultMessage("本次应付金额")
  String unpayedTotal_total();

  @DefaultMessage("本次应付税额")
  String unpayedTotal_tax();

  @DefaultMessage("未付金额/税额")
  String leftTotal();

  @DefaultMessage("未付金额")
  String leftTotal_total();

  @DefaultMessage("未付税额")
  String leftTotal_tax();

  @DefaultMessage("账款实付金额")
  String total_total();

  @DefaultMessage("实付金额")
  String defrayalTotal();

  @DefaultMessage("付款方式")
  String paymentType();

  @DefaultMessage("预付款冲扣")
  String depositLinePay();

  @DefaultMessage("扣预付款")
  String depositBillPay();

  @DefaultMessage("实付")
  String defrayal();

  @DefaultMessage("按总额付款")
  String billPay();

  @DefaultMessage("按科目付款")
  String linePay();

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

  @DefaultMessage("当前员工")
  String currentEmployee();

  @DefaultMessage("长度")
  String length();

  @DefaultMessage("发票登记")
  String ivcRegiste();

  @DefaultMessage("付款单单号")
  String paymentNumber();

  @DefaultMessage("项目")
  String store();

  @DefaultMessage("商户")
  String counterpart();

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

  @DefaultMessage("[空]")
  String nullOption();

  @DefaultMessage("没有可以进行发票登记的账款明细。")
  String noRegisteAccounts();

  @DefaultMessage("付款发票登记单配置")
  String ivcRegConfig();

  @DefaultMessage("{0}不允许为空。")
  String notNullField(String field);

  @DefaultMessage("开票金额不允许等于0。")
  String ivcTotalNoZero();

  @DefaultMessage("开票金额与账款实付方向不一致。")
  String ivcTotalDirectionError();

  @DefaultMessage("开票金额-账款实付={0}，不在付款发票登记单选项配置的金额范围内。")
  String noValidTotalRange(String total);

  @DefaultMessage("开票税额-账款实付税额={0}，不在付款发票登记单选项配置的税额范围内。")
  String noValidTaxRange(String tax);
  
  @DefaultMessage("{0}必须大于0。")
  String totalZero_error(String s);
  
  @DefaultMessage("保存并登记")
  String saveRegiter();
  
  @DefaultMessage("返回")
  String returnButton();
}
