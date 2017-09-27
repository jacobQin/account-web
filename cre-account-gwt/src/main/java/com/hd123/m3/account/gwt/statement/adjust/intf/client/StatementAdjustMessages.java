/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementAdjustMessages.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-22 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.intf.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author zhuhairui
 * 
 */
public interface StatementAdjustMessages extends CommonsMessages {
  public static StatementAdjustMessages M = GWT.create(StatementAdjustMessages.class);

  @DefaultMessage("...")
  String doing();

  @DefaultMessage("第 {0} 行")
  String lineNumber(int line);

  @DefaultMessage("对账组")
  String accountGroup();

  @DefaultMessage("项目")
  String businessUnit();

  @DefaultMessage("提交")
  String submit();

  @DefaultMessage("审核")
  String audit();

  @DefaultMessage("审核通过")
  String passAudit();

  @DefaultMessage("审核不通过")
  String notPassAudit();

  @DefaultMessage("作废")
  String abort();

  @DefaultMessage("原因")
  String reason();

  @DefaultMessage("作废原因")
  String abortReason();

  @DefaultMessage("审核原因")
  String auditReason();

  @DefaultMessage("添加行")
  String addLine();

  @DefaultMessage("在上方插入行")
  String insertLine();

  @DefaultMessage("合计")
  String aggregate();

  @DefaultMessage("审核信息")
  String auditInfo();

  @DefaultMessage("作废信息")
  String abortInfo();

  @DefaultMessage("账单调整明细")
  String statementAdjust();

  @DefaultMessage("账单")
  String statement();

  @DefaultMessage("账单明细")
  String statementLine();

  @DefaultMessage("无法查找指定的{0}。")
  String notFind(String entityCaption);

  @DefaultMessage("科目")
  String subject();

  @DefaultMessage("第{0}行的")
  String belongToline(int line);

  @DefaultMessage("不能")
  String cannot();

  @DefaultMessage("为空")
  String beBblank();

  @DefaultMessage("或")
  String or();

  @DefaultMessage("小于{0}")
  String lessThan(String value);

  @DefaultMessage("等于{0}")
  String valueEquals(String value);

  @DefaultMessage("大于{0}")
  String greaterThan(String value);

  @DefaultMessage("明细行")
  String detailLines();

  @DefaultMessage("参考信息")
  String referenceInform();

  @DefaultMessage("导入")
  String imp();

  @DefaultMessage("调整应收金额")
  String receiptTotal_total();

  @DefaultMessage("调整应付金额")
  String paymentTotal_total();

  @DefaultMessage("代码")
  String code();

  @DefaultMessage("名称")
  String name();

  @DefaultMessage("账单号：未找到单号 {0}")
  String cannotfindStatement(String billNumber);

  @DefaultMessage("请先选择：{0}")
  String seleteData(String entityCaption);

  @DefaultMessage("从账单导入...")
  String impFromBill();

  @DefaultMessage("请选择-{0}")
  String selectData2(String entityCaption);

  @DefaultMessage("账单账款")
  String statement2();

  @DefaultMessage("来源单据类型")
  String sourceBillType();

  @DefaultMessage("来源单据")
  String sourceBill();

  @DefaultMessage("审核意见")
  String auditOpinion();

  @DefaultMessage("结算单位")
  String accountUnit();

  @DefaultMessage("账款科目：在此业务单位中对应的的结算单位和单头的结算单位不相等。")
  String subjectError();

  @DefaultMessage("根据账单标识创建")
  String createByStatement();

  @DefaultMessage("重新选择账单会清空明细，是否继续？")
  String selectAgain();

  @DefaultMessage("重新选择合同会清空账单，是否继续？")
  String selectContract();

  @DefaultMessage("重新选择商户会清空账单，是否继续？")
  String selectCounterpart();

  @DefaultMessage("重新选择项目会清空账单，是否继续？")
  String selectAccountUnit();

  @DefaultMessage("说明：超过最大长度127;")
  String remarkIllegal();

  @DefaultMessage("重复")
  String repeat();

  @DefaultMessage("与")
  String and();

  @DefaultMessage("请先选择{0}")
  String pleaseSelect(String caption);

  @DefaultMessage("科目收付情况")
  String accDefrayal();

  @DefaultMessage("记账日期不在账单周期范围({0})之内！")
  String accountDateRange(String dateRange);

  @DefaultMessage("记账日期不能为空！")
  String accountDateIsNotNull();

  @DefaultMessage("edit")
  String editScence();

  @DefaultMessage("单据类型")
  String billType();

  @DefaultMessage("正在{0}“{1}”...")
  String beDoing(String action, String entityCaption);

  @DefaultMessage("{0}{1}“{2}”过程中发生错误")
  String processError2(String action, String entityCaption, String entityStr);

  @DefaultMessage("验证")
  String validate();
}
