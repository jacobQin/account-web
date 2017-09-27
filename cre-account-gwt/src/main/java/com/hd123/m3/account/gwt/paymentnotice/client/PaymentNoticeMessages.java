/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeMessages.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-11 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author zhuhairui
 * 
 */
public interface PaymentNoticeMessages extends CommonsMessages {
  public static PaymentNoticeMessages M = GWT.create(PaymentNoticeMessages.class);

  @DefaultMessage("...")
  String doing();

  @DefaultMessage("[空]")
  String empty_text();

  @DefaultMessage("授权组")
  String permGroup();

  @DefaultMessage("审核")
  String audit();
  
  @DefaultMessage(" 等于")
  String equal();

  @DefaultMessage("审核不通过")
  String notPassAudit();

  @DefaultMessage("审核意见")
  String auditOpinion();

  @DefaultMessage("作废原因")
  String abortReason();

  @DefaultMessage("原因")
  String reason();

  @DefaultMessage("{0}{1}“{2}”失败!")
  String actionFailed2(String action, String entityCaption, String entityStr);

  @DefaultMessage("请先选择{0}!")
  String requiredCounterpart(String caption);

  @DefaultMessage("合计")
  String aggregate();

  @DefaultMessage("账单明细")
  String statementLine();

  @DefaultMessage("说明")
  String remark();

  @DefaultMessage("账单编号")
  String statementBillNum();

  @DefaultMessage("合同编号")
  String contract_number();

  @DefaultMessage("结转期")
  String settleNo();

  @DefaultMessage("账单")
  String statement();

  @DefaultMessage("对账组")
  String accGroup();

  @DefaultMessage("明细行不能为空！")
  String lineNotNull();

  @DefaultMessage("{0}{1}成功!")
  String actionSuccess(String action, String entityCaption);

  @DefaultMessage("无法查找指定的{0}。")
  String notFind(String entityCaption);

  @DefaultMessage("第{0}行")
  String lineNumber(int line);

  @DefaultMessage("重复。")
  String repeat();

  @DefaultMessage("清除")
  String clear();

  @DefaultMessage("搜索条件")
  String searchCondition();

  @DefaultMessage("搜索结果")
  String searchResult();

  @DefaultMessage("{0} 类似于")
  String like(String value);

  @DefaultMessage("{0} 等于")
  String valueEquals(String value);

  @DefaultMessage("批量生成{0}")
  String batch_caption(String moduleCaption);

  @DefaultMessage("空单据")
  String batch_emptyBill();

  @DefaultMessage("选择")
  String batch_select();

  @DefaultMessage("设置规则")
  String batch_selectRules();

  @DefaultMessage("执行")
  String batch_execute();

  @DefaultMessage("完成")
  String batch_finish();

  @DefaultMessage("生成{0}")
  String batch_generate(String value);

  @DefaultMessage("生成规则")
  String batch_generateRule();

  @DefaultMessage("正在生成{0}：“{1}”的{2}...")
  String batch_generating(String counterpartCaption, String counterpart, String moudleCaption);

  @DefaultMessage("生成{0}：“{1}”的{2}过程发生错误。")
  String batch_generating_error(String counterpartCaption, String counterpart, String moudleCaption);

  @DefaultMessage("请确认是否继续？")
  String batch_continue();

  @DefaultMessage("用户中断。")
  String batch_msg_userInterrupt();

  @DefaultMessage("批量生成过程发生中断")
  String batch_interrupt();

  @DefaultMessage("{0}完成。")
  String batch_msg_finish(String action);

  @DefaultMessage("按合同拆单")
  String batch_splitByContract();

  @DefaultMessage("查看单据")
  String batch_viewBill();

  @DefaultMessage("下一步")
  String batch_next();

  @DefaultMessage("选择的{0}不能为空。")
  String batch_msg_emptyCounterparts(String caption);

  @DefaultMessage("生成通知单号：")
  String batch_billNumber_caption();

  @DefaultMessage("改变{0}会清空账单明细，是否继续？")
  String changeConfirm(String field);

  @DefaultMessage("扩展信息")
  String extendInfo();

  @DefaultMessage("请先填写{0}。")
  String pleaseFillInFirst(String caption);

  @DefaultMessage("收付款通知单流程")
  String paymentNoticeBPM();

  @DefaultMessage("完成流程任务")
  String complete_task();

  @DefaultMessage("位置")
  String positon();

  @DefaultMessage("店招")
  String contractTitle();

  @DefaultMessage("楼层")
  String floor();

  @DefaultMessage("合作方式")
  String coopMode();

}
