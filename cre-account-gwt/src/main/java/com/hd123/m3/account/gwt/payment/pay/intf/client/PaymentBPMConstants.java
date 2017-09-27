/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	FeeBPMConstants.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-17 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.intf.client;

import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;

/**
 * @author huangjunxian
 * 
 */
public class PaymentBPMConstants {
  /** 业务动作 */
  public static interface BusinessAction {

    /** 删除 */
    public static final String DELETE = "delete";

    /** 审批(通过) */
    public static final String APPROVE = "approve";

    /** 作废 */
    public static final String ABORT = "abort";
  }

  /** 任务变量 */
  public static interface TaskVariable {
    /** uuid */
    public static final String TASK_UUID = BpmModuleUrlParams.KEY_ENTITY_UUID;
    /** 单号 */
    public static final String TASK_BILLNUMBER = "billNumber";
    /** 单据名称，格式为：付款单单号+结算单位+对方单位 */
    public static final String TASK_BILLCAPTION = "billCaption";
  }
}
