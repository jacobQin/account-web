/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	DepositMoveMessage.java
 * 模块说明：	
 * 修改历史：
 * 2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.intf.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author zhuhairui
 * 
 */
public interface DepositMoveMessage extends CommonsMessages {
  public static DepositMoveMessage M = GWT.create(DepositMoveMessage.class);

  @DefaultMessage("...")
  String doing();

  @DefaultMessage("和")
  String and();

  @DefaultMessage("原因")
  String reason();

  @DefaultMessage("转入")
  String in();

  @DefaultMessage("转出")
  String out();

  @DefaultMessage("信息")
  String info();

  @DefaultMessage("当前余额")
  String balance();

  @DefaultMessage("不能超过")
  String notExcees();

  @DefaultMessage("不能相同")
  String notSame();

  @DefaultMessage("验证")
  String validate();

  @DefaultMessage("正在{0}“{1}”...")
  String beDoing(String action, String entityCaption);

  @DefaultMessage("{0}{1}“{2}”失败!")
  String actionFailed2(String action, String entityCaption, String entityStr);

  @DefaultMessage("改变项目将清空科目信息，是否继续？")
  String changeAccountUnit();

  @DefaultMessage("改变商户将清空{0}，是否继续？")
  String changeCounterpart(String object);

  @DefaultMessage("改变合同将清空{0}，是否继续？")
  String changeContract(String object);

  @DefaultMessage("请先选择项目！")
  String selectAccountUnit();

  @DefaultMessage("请先选择{0}商户！")
  String selectCounterpart(String direction);

  @DefaultMessage("转入商户类型与转出商户类型不一致")
  String counterpartConflict();
}
