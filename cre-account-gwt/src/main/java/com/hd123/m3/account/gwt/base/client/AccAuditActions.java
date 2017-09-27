/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-common
 * 文件名：	AccAuditActions.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-14 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.base.client;

import com.hd123.rumba.webframe.gwt.audit.client.BAuditActions;

/**
 * 提供一组预定义的审计动作常量。
 * 
 * @author huangjunxian
 * 
 */
public class AccAuditActions extends BAuditActions {
  /**
   * 动作常量，意味着操作人发出“恢复删除”资源的指令。<br>
   * 建议在关键字参数中指明删除的资源的键属性。
   */
  public static final String UNDELETE = "undelete";
  /**
   * 动作常量，意味着操作人发出“批量恢复删除”资源的指令。<br>
   * 建议在关键字参数或事件数据参数中指明被选中恢复删除资源范围信息。
   */
  public static final String BATCH_UNDELETE = "batch-undelete";
  /**
   * 动作常量，意味着操作人发出“查看日志”指定资源的指令。<br>
   * 建议在关键字参数中指明所查看的资源的键属性。
   */
  public static final String VIEWLOG = "viewLog";

  /**
   * 动作常量，意味着操作人发出“生效”资源的指令。<br>
   * 建议在关键字参数中指明生效的资源的键属性。
   */
  public static final String EFFECT = "effect";
  /**
   * 动作常量，意味着操作人发出“作废”资源的指令。<br>
   * 建议在关键字参数中指明作废的资源的键属性。
   */
  public static final String ABORT = "abort";
  /**
   * 动作常量，意味着操作人发出“批量启用”资源的指令。<br>
   * 建议在关键字参数或事件数据参数中指明被选中启用资源范围信息。
   */
  public static final String BATCH_ENABLE = "batch-enable";
  /**
   * 动作常量，意味着操作人发出“启用”资源的指令。<br>
   * 建议在关键字参数中指明启用的资源的键属性。
   */
  public static final String ENABLE = "enable";
  /**
   * 动作常量，意味着操作人发出“批量禁用”资源的指令。<br>
   * 建议在关键字参数或事件数据参数中指明被选中禁用资源范围信息。
   */
  public static final String BATCH_DISABLE = "batch-disable";
  /**
   * 动作常量，意味着操作人发出“禁用”资源的指令。<br>
   * 建议在关键字参数中指明禁用的资源的键属性。
   */
  public static final String DISABLE = "disable";
  /**
   * 动作常量，意味着操作人发出“标记过时”资源的指令。<br>
   * 建议在关键字参数中指明标记过时的资源的键属性。
   */
  public static final String MARKSEASON = "markSeason";
  /**
   * 动作常量，意味着操作人发出“取消过时”资源的指令。<br>
   * 建议在关键字参数中指明取消过时的资源的键属性。
   */
  public static final String CANCELSEASON = "cancelSeason";
  /**
   * 动作常量，意味着操作人发出“保存用户配置”资源的指令。<br>
   * 建议在关键字参数中指明保存用户配置的资源的键属性。
   */
  public static final String SAVE_USER_HABITS = "saveUserHabits";
}
