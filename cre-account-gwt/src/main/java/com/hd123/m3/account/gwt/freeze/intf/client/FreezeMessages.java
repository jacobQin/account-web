package com.hd123.m3.account.gwt.freeze.intf.client;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

public interface FreezeMessages extends CommonsMessages {
  public static FreezeMessages M = GWT.create(FreezeMessages.class);

  @DefaultMessage("...")
  String doing();

  @DefaultMessage("解冻")
  String unfreeze();

  @DefaultMessage("解冻原因")
  String unfreezeReason();

  @DefaultMessage("冻结")
  String freeze();

  @DefaultMessage("请填写：")
  String pleaseWrite();

  @DefaultMessage("冻结原因")
  String freezeReason();

  @DefaultMessage("合同编号")
  String contractNum();

  @DefaultMessage("{0}{1}“{2}”失败!")
  String actionFailed2(String action, String entityCaption, String entityStr);

  @DefaultMessage("合计")
  String aggregate();

  @DefaultMessage("账款明细")
  String accountLine();

  @DefaultMessage("明细行不能为空！")
  String lineNotNull();

  @DefaultMessage("第 {0} 行")
  String lineNumber(int line);

  @DefaultMessage("重复。")
  String repeat();

  @DefaultMessage("{0}{1}成功!")
  String actionSuccess(String action, String entityCaption);

  @DefaultMessage("无法查找指定的{0}。")
  String notFind(String entityCaption);

  @DefaultMessage("改变商户后会清空账款明细，是否继续？")
  String selectCounterpart();

  @DefaultMessage("改变项目后会清空账款明细，是否继续？")
  String selectAccountUnit();

  @DefaultMessage("-")
  String noneBillNumber();
}
