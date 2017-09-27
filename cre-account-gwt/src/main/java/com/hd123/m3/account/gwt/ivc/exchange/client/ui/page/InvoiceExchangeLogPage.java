/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeLogPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.page;

import com.hd123.m3.account.gwt.ivc.exchange.client.EPInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.InvoiceExchangeUrlParams.Log;
import com.hd123.m3.commons.gwt.bpm.client.EPBpmModule2;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmLogPage2;

/**
 * 发票交换单审计日志|查看页面
 * 
 * @author LiBin
 * @since 1.7
 *
 */
public class InvoiceExchangeLogPage extends BaseBpmLogPage2<BInvoiceExchange> implements Log{

  private static InvoiceExchangeLogPage instance = null;

  public static InvoiceExchangeLogPage getInstance() {
    if (instance == null)
      instance = new InvoiceExchangeLogPage();
    return instance;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  
  @Override
  protected EPBpmModule2 getEP() {
    return EPInvoiceExchange.getInstance();
  }

}
