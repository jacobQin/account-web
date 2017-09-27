/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountSettleMessages.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-11 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author huangjunxian
 * 
 */
public interface AccountSettleMessages extends CommonsMessages {

  public static AccountSettleMessages M = GWT.create(AccountSettleMessages.class);

  @DefaultMessage("[空]")
  String empty_text();

  @DefaultMessage("选择合同")
  String select_contract();

  @DefaultMessage("选择出账账单")
  String select_accSettle();

  @DefaultMessage("配置")
  String config();

  @DefaultMessage("账单出账")
  String select_result();

  @DefaultMessage("执行出账")
  String execute();

  @DefaultMessage("完成")
  String finish();

  @DefaultMessage("执行出账中断")
  String interrupt();

  @DefaultMessage("全部")
  String nullOptionText();

  @DefaultMessage(" 等于")
  String captionEquals();

  @DefaultMessage(" 类似于")
  String like();

  @DefaultMessage("下一步")
  String next();

  @DefaultMessage("查看账单")
  String viewStatement();

  @DefaultMessage("出账")
  String calculate();

  @DefaultMessage("完成流程任务")
  String complete_task();

  @DefaultMessage("合同")
  String contract();

  @DefaultMessage("合同编号")
  String contract_number();

  @DefaultMessage("生成账单号：")
  String statement_caption();

  @DefaultMessage("空账单")
  String statement_empty();

  @DefaultMessage("店招")
  String contract_title();

  @DefaultMessage("核算楼层")
  String floor();

  @DefaultMessage("业态")
  String category();

  @DefaultMessage("楼宇")
  String building();

  @DefaultMessage("位置")
  String positon();

  @DefaultMessage("合作方式")
  String coopmode();

  @DefaultMessage("合同类型")
  String contractCategory();

  @DefaultMessage("结算周期名称")
  String settle_name();

  @DefaultMessage("结算周期起止日期")
  String settle_dateRange();

  @DefaultMessage("账单流程")
  String statement_process();

  @DefaultMessage("出账时间")
  String accountTime();

  @DefaultMessage("出账方式")
  String calculate_type();

  @DefaultMessage("-")
  String empty_billNumber();

  @DefaultMessage("请确认是否继续？")
  String msg_continue();

  @DefaultMessage("搜索出账记录过程发生错误。")
  String msg_query();

  @DefaultMessage("选择的账单出账记录不能为空。")
  String msg_emptyRecords();

  @DefaultMessage("用户中断。")
  String msg_userInterrupt();

  @DefaultMessage("{0}完成。")
  String msg_finish(String action);

  @DefaultMessage("正在生成“{0}：{1}, {2}:{3}, {4}:{5} ”的账单...")
  String msg_execute_ing(String counterpartCaption, String counterpartValue,
      String settleNameCaption, String settleNameValue, String contractNumberCaption,
      String contractNumber);

  @DefaultMessage("生成“{0}：{1}, {2}:{3}, {4}:{5} ”的账单过程发生错误。")
  String msg_execute_error(String counterpartCaption, String counterpartValue,
      String settleNameCaption, String settleNameValue, String contractNumberCaption,
      String contractNumber);

  @DefaultMessage("查询出账记录发生错误。")
  String query_error();

}
