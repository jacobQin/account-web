/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceInstockLogPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月9日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.instock.client.ui;

import com.hd123.m3.account.gwt.ivc.instock.client.EPInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.biz.BInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.intf.client.InvoiceInstockUrlParams.Log;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmLogPage2;

/**
 * 发票入库单|日志查看页面。
 * 
 * @author lixiaohong
 * @since 1.0
 *
 */
public class InvoiceInstockLogPage extends BaseBpmLogPage2<BInvoiceInstock> implements Log{
  public static InvoiceInstockLogPage instance = null;

  public static InvoiceInstockLogPage getInstance() {
    if (instance == null)
      instance = new InvoiceInstockLogPage();
    return instance;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  protected EPInvoiceInstock getEP() {
    return EPInvoiceInstock.getInstance();
  }

}
