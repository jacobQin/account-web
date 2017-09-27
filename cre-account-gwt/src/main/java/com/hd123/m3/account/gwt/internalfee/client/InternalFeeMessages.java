/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeMessages.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client;

import com.google.gwt.core.shared.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author liuguilin
 * 
 */
public interface InternalFeeMessages extends CommonsMessages {

  public static InternalFeeMessages M = GWT.create(InternalFeeMessages.class);

  @DefaultMessage("作废原因")
  String abortReason();

  @DefaultMessage("正在{0}“{1}”...")
  String beDoing(String action, String entityCaption);

  @DefaultMessage("{0}{1}“{2}”失败!")
  String actionFailed2(String action, String entityCaption, String entityStr);

  @DefaultMessage("供应商")
  String vendor();

  @DefaultMessage("科目")
  String subject();

  @DefaultMessage("不能")
  String cannot();

  @DefaultMessage("小于{0}")
  String lessThan(String value);

  @DefaultMessage("大于{0}")
  String greaterThan(String value);

  @DefaultMessage("改变收付方向会清空明细行，是否继续？")
  String changeDirectionConfirm();

  @DefaultMessage("合计")
  String aggregate();

  @DefaultMessage("在上方插入行")
  String insertLine();

  @DefaultMessage("第{0}行的")
  String belongToline(int line);

  @DefaultMessage("为空")
  String beBblank();

  @DefaultMessage("或")
  String or();

  @DefaultMessage("等于{0}")
  String valueEquals(String value);

  @DefaultMessage("明细行")
  String detailLines();

  @DefaultMessage("{0}{1}成功!")
  String actionSuccess(String action, String entityCaption);

  @DefaultMessage("无法导航到指定模块({0})。")
  String navigateError(String url);

  @DefaultMessage("{0}{1}“{2}”过程中发生错误")
  String processError2(String action, String entityCaption, String entityStr);

  @DefaultMessage("原因")
  String cause();

  @DefaultMessage("第{0}行和第{1}行明细的科目发生重复。")
  String duplicate(int line1, int line2);

  @DefaultMessage("与第{0}行明细的科目发生重复。")
  String duplicateWith(int line);

  @DefaultMessage("批量添加")
  String batchAdd();
}
