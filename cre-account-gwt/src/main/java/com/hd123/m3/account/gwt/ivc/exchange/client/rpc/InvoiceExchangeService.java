/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeService.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchangeAccountDetails;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;

/**
 * 发票交换单GWT服务|接口
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
@RemoteServiceRelativePath("invoice/exchange")
public interface InvoiceExchangeService extends M3BpmModuleService2<BInvoiceExchange> {

  /** 键：当前用户 */
  public static final String KEY_CURRENT_USER = "currentUser";

  public static class Locator {
    private static InvoiceExchangeServiceAsync service = null;

    public static InvoiceExchangeServiceAsync getService() {
      if (service == null)
        service = GWT.create(InvoiceExchangeService.class);
      return service;
    }
  }

  /**
   * 根据发票号码查询账款明细。
   * 
   * @param invoiceNumbers
   *          发票号码，传入null或者空列表将返回空对象。
   * @param billUuid
   *          单据uuid，为null则表示账款仅仅允许是未锁定的，非null则允许被传入单据锁定。
   * @return 返回账款明细，正常情况下不会返回null。
   * @throws ClientBizException
   */
  public BInvoiceExchangeAccountDetails getAccountLinesByInvoceNumbers(List<String> invoiceNumbers,
      String billUuid) throws ClientBizException;
  
  /**
   * 获取所有单据类型。
   * 
   * @return 单据类型列表。
   */
  List<BBillType> getAllTypes();

}
