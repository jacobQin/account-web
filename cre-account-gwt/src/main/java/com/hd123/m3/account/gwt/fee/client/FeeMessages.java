/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	FeeMessages.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author subinzhu
 * 
 */
public interface FeeMessages extends CommonsMessages {
  public static FeeMessages M = GWT.create(FeeMessages.class);

  @DefaultMessage("{0}：未找到代码 {1}")
  String cannotFindCode(String caption, String code);

  @DefaultMessage("{0}：不允许空值")
  String notNull(String caption);

  @DefaultMessage("{0}：不允许等于0")
  String notZero(String caption);

  @DefaultMessage("第 {0} 行")
  String lineNumber(int line);

  @DefaultMessage("第 {0} 行的")
  String belongToline(int line);

  @DefaultMessage("参考信息")
  String referenceInformation();

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

  @DefaultMessage("删除")
  String delete();

  @DefaultMessage("无法导航到指定模块({0})。")
  String navigateError(String url);

  @DefaultMessage("显示{0}页面时发生错误。")
  String showPageError(String page);

  @DefaultMessage("合计")
  String aggregate();

  @DefaultMessage("添加行")
  String addLine();

  @DefaultMessage("在上方插入行")
  String insertAboveLine();

  @DefaultMessage("批量添加")
  String batchAdd();

  @DefaultMessage("{0}{1}失败!")
  String actionFailed(String action, String entityCaption);

  @DefaultMessage("{0}{1}“{2}”失败!")
  String actionFailed2(String action, String entityCaption, String entityStr);

  @DefaultMessage("{0}{1}成功!")
  String actionSuccess(String action, String entityCaption);

  @DefaultMessage("正在{0}“{1}”...")
  String beDoing(String action, String entityCaption);

  @DefaultMessage("对账组")
  String accountGroup();

  @DefaultMessage("提交")
  String submit();

  @DefaultMessage("审核")
  String approve();

  @DefaultMessage("更多")
  String more();

  @DefaultMessage("作废")
  String abort();

  @DefaultMessage("科目收付情况")
  String subjectRAndPInfo();

  @DefaultMessage("是否{0}当前选中的{1}（共计 {2} 条）？")
  String dealWithSelectedDataComfirm(String action, String entityCaption, int count);

  @DefaultMessage("{0}{1}“{2}”过程中发生错误")
  String processError2(String action, String entityCaption, String entityStr);

  @DefaultMessage("审核意见")
  String approveOpinion();

  @DefaultMessage("作废原因")
  String abortReason();

  @DefaultMessage("改变收付方向会清空明细行，是否继续？")
  String changeDirectionConfirm();

  @DefaultMessage("结算单位")
  String accountUnit();

  @DefaultMessage("账款科目：在此业务单位中对应的的结算单位和单头的结算单位不相等。")
  String subjectError();

  @DefaultMessage("请先选择{0}")
  String pleaseSelect(String caption);

  @DefaultMessage("验证")
  String validate();

  @DefaultMessage("获取结算周期")
  String getAccSettle();

  @DefaultMessage("不存在临时费用类型的结算周期。")
  String accSettleNoExists();

  @DefaultMessage("必须大于所属结算周期的最大已出账账期的截止日期{0}。")
  String mustLaggerThenAccSettleEndDate(String date);

  @DefaultMessage("必须大于等于合同起始日期{0}。")
  String mustLaggerThenContractBeginDate(String date);

  @DefaultMessage("必须小于等于合同结算周期的截止日期{0}。")
  String mustLessThenContractEndDate(String date);

  @DefaultMessage("执行进度")
  String executProcess();

  @DefaultMessage("导出模板")
  String exportTemplate();

  @DefaultMessage("请先选择需要{0}的{1}。")
  String seleteDataToAction(String action, String entityCaption);
  
  @DefaultMessage("立即收款")
  String quickReceive();
}
