/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	InvoiceRegService.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.account.gwt.invoice.commons.client.ui.filter.AccountDataFilter;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author chenpeisi
 * 
 */
@RemoteServiceRelativePath("account/invoiceReg")
public interface InvoiceRegService extends M3ModuleService {

  public static class Locator {
    private static InvoiceRegServiceAsync service = null;

    public static InvoiceRegServiceAsync getService() {
      if (service == null)
        service = GWT.create(InvoiceRegService.class);
      return service;
    }
  }

  /**
   * 查询账款。
   * 
   * @param filter
   *          筛选条件
   * @param counterAccountUnitUuids
   *          对方结算中心包含的对方结算单位uuid集合 not null。
   * @return 账款列表
   * @throws ClientBizException
   */
  public RPageData<BAccount> queryAccount(AccountDataFilter filter) throws ClientBizException;

  /**
   * 从账单添加账款。
   * 
   * @param filter
   *          筛选条件
   * @return 账款列表
   * @throws ClientBizException
   */
  public RPageData<BAccountStatement> queryAccountByStatement(AccountDataFilter filter)
      throws ClientBizException;

  /**
   * 从来源单据添加账款。
   * 
   * @param filter
   *          筛选条件
   * @return 账款列表
   * @throws ClientBizException
   */
  public RPageData<BAccountSourceBill> queryAccountByBill(AccountDataFilter filter)
      throws ClientBizException;

  /**
   * 从收付款通知单添加账款。
   * 
   * @param filter
   *          筛选条件
   * @return 账款列表
   * @throws ClientBizException
   */
  public RPageData<BAccountNotice> queryAccountByPaymentNotice(AccountDataFilter filter)
      throws ClientBizException;

  /**
   * 保存发票登记单选项配置。
   * 
   * @param config
   *          选项配置
   * @param direction
   *          收付方向
   * @throws ClientBizException
   */
  public void saveConfig(BInvoiceRegConfig config, int direction) throws ClientBizException;

}
