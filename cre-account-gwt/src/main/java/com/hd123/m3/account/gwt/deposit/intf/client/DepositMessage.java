/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	DepositMessage.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-16 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.intf.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author chenpeisi
 * 
 */
public interface DepositMessage extends CommonsMessages {

  public static DepositMessage M = GWT.create(DepositMessage.class);

  @DefaultMessage("付款方式")
  String paymentType();

  @DefaultMessage("当前员工")
  String currentEmployee();

  @DefaultMessage("完成")
  String finish();

  @DefaultMessage("付款信息")
  String paymentInfo();

  @DefaultMessage("收款信息")
  String receiptInfo();

  @DefaultMessage("银行")
  String bank();

  @DefaultMessage("原因")
  String reason();

  @DefaultMessage("合计")
  String total();

  @DefaultMessage("合同金额")
  String contractTotal();

  @DefaultMessage("合同待收金额")
  String unDepositTotal();

  @DefaultMessage("预付款明细")
  String paymentLine();

  @DefaultMessage("预存款明细")
  String receiptLine();

  @DefaultMessage("添加行")
  String addLine();

  @DefaultMessage("在上方插入行")
  String insertLine();

  @DefaultMessage("从合同导入")
  String importFromContract();

  @DefaultMessage("预付款信息")
  String payDepositInfo();

  @DefaultMessage("预存款信息")
  String recDepositInfo();

  @DefaultMessage("参考信息")
  String referenceInfo();

  @DefaultMessage("作废原因")
  String abortReason();

  @DefaultMessage("第 {0} 行")
  String resultLine(int count);

  @DefaultMessage("付款日期")
  String paymentDate();

  @DefaultMessage("收款日期")
  String receiptDate();

  @DefaultMessage("验证")
  String validate();

  @DefaultMessage("查找")
  String fetch();

  @DefaultMessage("{0}不允许为空。")
  String lineNotNull(String captionName);

  @DefaultMessage("第{0}行和{1}行重复。")
  String duplicate(int i, int j);

  @DefaultMessage("第{0}行的{1}不能为空。")
  String notNull(int i, String fieldName);

  @DefaultMessage("{0}:必须大于0。")
  String min(String fieldName);

  @DefaultMessage("第{0}行的{1}必须大于0。")
  String minValue(int i, String fieldName);

  @DefaultMessage("第{0}行：{1}不能超过最大精度15位。")
  String maxValue(int i, String fieldName);
  
  @DefaultMessage("请先选择合同。")
  String selectContractFirst();

  @DefaultMessage("请先填写基本信息。")
  String fillBasicInfo();

  @DefaultMessage("[空]")
  String nullOption();

  @DefaultMessage("合同{0}不存在可用的预存款条款。")
  String noDepositTerm(String contract);
  
  @DefaultMessage("不允许空值;")
  String notNull2();
  
  @DefaultMessage("科目：与第{0}行重复")
  String duplicateSubject2(int index);
}
