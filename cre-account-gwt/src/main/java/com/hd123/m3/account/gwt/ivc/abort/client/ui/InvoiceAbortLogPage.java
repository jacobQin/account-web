/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceAbortLogPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月18日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.abort.client.ui;

import com.hd123.m3.account.gwt.ivc.abort.client.EPInvoiceAbort;
import com.hd123.m3.account.gwt.ivc.abort.client.biz.BInvoiceAbort;
import com.hd123.m3.account.gwt.ivc.abort.intf.client.InvoiceAbortUrlParams.Log;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmLogPage2;

/**
 * 发票作废单|日志查看页面。
 * 
 * @author lixiaohong
 * @since 1.7
 *
 */
public class InvoiceAbortLogPage extends BaseBpmLogPage2<BInvoiceAbort> implements Log{
  public static InvoiceAbortLogPage instance = null;

  public static InvoiceAbortLogPage getInstance() {
    if (instance == null)
      instance = new InvoiceAbortLogPage();
    return instance;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  protected EPInvoiceAbort getEP() {
    return EPInvoiceAbort.getInstance();
  }

}
