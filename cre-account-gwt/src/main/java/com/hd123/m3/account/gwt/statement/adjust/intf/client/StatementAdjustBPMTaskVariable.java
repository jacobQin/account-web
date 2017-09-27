/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementAdjustBPMTaskVariable.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-20 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.intf.client;

import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;

/**
 * BPM任务变量
 * 
 * @author huangjunxian
 * 
 */
public class StatementAdjustBPMTaskVariable {
  /** uuid */
  public static final String TASK_UUID = BpmModuleUrlParams.KEY_ENTITY_UUID;
  /** 单号 */
  public static final String TASK_BILLNUMBER = "billNumber";
  /** 单据名称，格式为：账单调整单单号+合同编号 */
  public static final String TASK_BILLCAPTION = "billCaption";
}
