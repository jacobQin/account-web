/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeBPMTaskVariable.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.intf.client;

import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;

/**
 * @author liuguilin
 * 
 */
public class InternalFeeBPMTaskVariable {
  /** uuid */
  public static final String TASK_UUID = BpmModuleUrlParams.KEY_ENTITY_UUID;
  /** 单号 */
  public static final String TASK_BILLNUMBER = "_billNumber";
  /** 单据名称，格式为：内部费用单单号 */
  public static final String TASK_BILLCAPTION = "_billCaption";
}
