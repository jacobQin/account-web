/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleLogPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月11日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client.ui;

import com.hd123.m3.account.gwt.ivc.recycle.client.EPInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.intf.client.InvoiceRecycleUrlParams;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmLogPage2;

/**
 * 发票回收单|日志查看页面。
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
public class InvoiceRecycleLogPage extends BaseBpmLogPage2<BInvoiceRecycle>{
  public static InvoiceRecycleLogPage instance = null;

  public static InvoiceRecycleLogPage getInstance() {
    if (instance == null)
      instance = new InvoiceRecycleLogPage();
    return instance;
  }

  @Override
  public String getStartNode() {
    return InvoiceRecycleUrlParams.START_NODE_LOG;
  }

  @Override
  protected EPInvoiceRecycle getEP() {
    return EPInvoiceRecycle.getInstance();
  }

}
