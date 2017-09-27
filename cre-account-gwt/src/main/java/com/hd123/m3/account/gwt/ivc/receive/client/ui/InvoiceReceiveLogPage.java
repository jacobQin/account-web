/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceReceiveLogPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月14日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.receive.client.ui;

import com.hd123.m3.account.gwt.ivc.receive.client.EPInvoiceReceive;
import com.hd123.m3.account.gwt.ivc.receive.client.biz.BInvoiceReceive;
import com.hd123.m3.account.gwt.ivc.receive.intf.client.InvoiceReceiveUrlParams.Log;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmLogPage2;

/**
 * 发票领用单|日志查看页面。
 * 
 * @author lixiaohong
 * @since 1.7
 *
 */
public class InvoiceReceiveLogPage extends BaseBpmLogPage2<BInvoiceReceive> implements Log{
  public static InvoiceReceiveLogPage instance = null;

  public static InvoiceReceiveLogPage getInstance() {
    if (instance == null)
      instance = new InvoiceReceiveLogPage();
    return instance;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  protected EPInvoiceReceive getEP() {
    return EPInvoiceReceive.getInstance();
  }

}
