/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	DepositRepaymentMessage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.intf.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author zhuhairui
 * 
 */
public interface DepositRepaymentMessage extends CommonsMessages {
  public static DepositRepaymentMessage M = GWT.create(DepositRepaymentMessage.class);

  @DefaultMessage("...")
  String doing();

  @DefaultMessage("复核")
  String recheck();

  @DefaultMessage("复核通过")
  String recheckSeccess();

  @DefaultMessage("复核不通过")
  String recheckFailed();

  @DefaultMessage("完成")
  String finish();

  @DefaultMessage("银行")
  String bank();

  @DefaultMessage("原因")
  String reason();

  @DefaultMessage("合计")
  String total();

  @DefaultMessage("添加行")
  String addLine();

  @DefaultMessage("在上方插入行")
  String insertLine();

  @DefaultMessage("还款信息")
  String repaymentInfo();

  @DefaultMessage("还款明细")
  String repaymentLines();

  @DefaultMessage("参考信息")
  String referenceInfo();

  @DefaultMessage("账户余额")
  String accountBalance();

  @DefaultMessage("作废原因")
  String abortReason();

  @DefaultMessage("结算组")
  String settleGroup();

  @DefaultMessage("第 {0} 行")
  String resultLine(int count);

  @DefaultMessage("验证")
  String validate();

  @DefaultMessage("{0}不允许为空。")
  String lineNotNull(String captionName);

  @DefaultMessage("第{0}行和{1}行的{2}重复。")
  String duplicate(int i, int j, String fieldName);

  @DefaultMessage("第{0}行的{1}不能为空。")
  String notNull(int i, String fieldName);
  
  @DefaultMessage("第{0}行的科目：未找到代码{1}。")
  String notFindSubjectCode(int i, String subjectCode);

  @DefaultMessage("{0}必须大于0")
  String min(String fieldName);

  @DefaultMessage("第{0}行的{1}必须大于0")
  String minValue(int i, String fieldName);

  @DefaultMessage("{0}:不能大于{1}")
  String max(String fieldName1, String fieldName2);

  @DefaultMessage("第{0}行的{1}不能大于{2}")
  String maxValue(int i, String fieldName1, String fieldName2);

  @DefaultMessage("生效")
  String effect();

  @DefaultMessage("正在{0}“{1}”...")
  String beDoing(String action, String entityCaption);

  @DefaultMessage("{0}{1}“{2}”失败!")
  String actionFailed2(String action, String entityCaption, String entityStr);

  @DefaultMessage("{0}{1}“{2}”过程中发生错误")
  String processError2(String action, String entityCaption, String entityStr);

  @DefaultMessage("授权组")
  String permGroup();

  @DefaultMessage("取得付款方式")
  String getPayments();

  @DefaultMessage("取得银行资料")
  String getBanks();

  @DefaultMessage("改变商户将清空预付款还款单明细行，是否继续？")
  String changeCounterpart();

  @DefaultMessage("改变项目将清空预付款还款单明细行，是否继续？")
  String changeAccountUnit();

  @DefaultMessage("当前员工")
  String currentEmployee();

  @DefaultMessage("预付款还款单")
  String payDepositRepayment();

  @DefaultMessage("预存款还款单")
  String recDepositRepayment();

  @DefaultMessage("[空]")
  String nullOption();
  
  @DefaultMessage("科目：与第{0}行重复")
  String duplicateSubject2(int index);

  @DefaultMessage("不允许空值;")
  String notNull2();
}
