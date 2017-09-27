/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegLogPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月14日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui;

import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmLogPage2;

/**
 * 发票登记单|日志查看页面。
 * 
 * @author chenrizhang
 * @since 1.7
 *
 */
public class InvoiceRegLogPage extends BaseBpmLogPage2<BInvoiceReg> {
  public static InvoiceRegLogPage instance = null;

  public static InvoiceRegLogPage getInstance() {
    if (instance == null)
      instance = new InvoiceRegLogPage();
    return instance;
  }

  @Override
  protected EPInvoiceRegReceipt getEP() {
    return EPInvoiceRegReceipt.getInstance();
  }

}
