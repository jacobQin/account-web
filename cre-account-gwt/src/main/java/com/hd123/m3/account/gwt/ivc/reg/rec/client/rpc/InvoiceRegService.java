/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegService.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceRegOptions;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountPayment;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 发票登记单|同步接口。
 * 
 * @author chenrizhang
 * @since 1.7
 * 
 */
@RemoteServiceRelativePath("invoice/reg/rec")
public interface InvoiceRegService extends M3BpmModuleService2<BInvoiceReg> {
  public static final String KEY_INVOICE_TYPE = "invoiceType";
  /** 默认发票类型 */
  public static final String KEY_DEFALUT_INVOICE_TYPE = "defalutInvoiceType";
  public static final String KEY_BILL_TYPES = "billTypes";

  /** 收款单是否启用发票库存配置 */
  public static final String OPTION_USEINVOICESTOCK = "/account/useInvoiceStock";
  /** 收款单是否启用账款可以多次开票 */
  public static final String OPTION_ALLOW_SPLIT_REG = "/account/allowSplitReg";
  /** 计算精度,小数位数 */
  public static final String SCALE = "scale";
  /** 计算精度,舍入算法 */
  public static final String ROUNDING_MODE = "roundingMode";
  /** 是否启用外部发票系统 */
  public static final String ENABLED_EXTINVOICESYSTEM = "isEnabledExtInvoiceSystem";

  public static class Locator {
    private static InvoiceRegServiceAsync service = null;

    public static InvoiceRegServiceAsync getService() {
      if (service == null)
        service = GWT.create(InvoiceRegService.class);
      return service;
    }
  }

  BInvoiceReg createByPayment(String paymentUuid) throws Exception;

  BInvoiceReg createByStatement(String statementUuid) throws Exception;

  BInvoiceReg createByNotice(String noticeUuid) throws Exception;

  RPageData<BAccount> queryAccounts(IvcRegAccountFilter filter) throws Exception;

  RPageData<BAccountStatement> queryAccStatements(IvcRegAccountFilter filter) throws Exception;

  RPageData<BAccountPayment> queryAccPayments(IvcRegAccountFilter filter) throws Exception;

  RPageData<BAccountNotice> queryAccNotices(IvcRegAccountFilter filter) throws Exception;

  void saveOptions(BInvoiceRegOptions config) throws Exception;

  void print(String uuid, long version) throws Exception;
}
