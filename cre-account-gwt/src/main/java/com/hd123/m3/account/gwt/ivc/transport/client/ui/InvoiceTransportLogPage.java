/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceTransportLogPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.client.ui;

import com.hd123.m3.account.gwt.ivc.transport.client.EPInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.client.biz.BInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.intf.client.InvoiceTransportUrlParams;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmLogPage2;

/**
 * 发票调拨单|日志查看页面。
 * 
 * @author lixiaohong
 * @since 1.9
 *
 */
public class InvoiceTransportLogPage extends BaseBpmLogPage2<BInvoiceTransport>{
  public static InvoiceTransportLogPage instance = null;

  public static InvoiceTransportLogPage getInstance() {
    if (instance == null)
      instance = new InvoiceTransportLogPage();
    return instance;
  }

  @Override
  public String getStartNode() {
    return InvoiceTransportUrlParams.START_NODE_LOG;
  }

  @Override
  protected EPInvoiceTransport getEP() {
    return EPInvoiceTransport.getInstance();
  }

}
