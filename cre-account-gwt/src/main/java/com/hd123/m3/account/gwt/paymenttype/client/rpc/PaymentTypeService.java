/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	PaymentTypeService.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.paymenttype.client.biz.BPaymentType;
import com.hd123.rumba.webframe.gwt.base.client.ModuleService;

/**
 * 付款方式|同步接口
 * 
 * @author zhuhairui
 * 
 */
@RemoteServiceRelativePath("account/paymentType")
public interface PaymentTypeService extends ModuleService {
  /** 持久化单元 */
  public static final String PU_ACCOUNT_PAYMENTTYPE = "account_paymentType";

  public static class Locator {
    private static PaymentTypeServiceAsync service = null;

    public static PaymentTypeServiceAsync getService() {
      if (service == null)
        service = GWT.create(PaymentTypeService.class);
      return service;
    }
  }

  /**
   * 创建收银付款方式的B对象。
   * 
   * @return 收银付款方式。
   * @throws Exception
   */
  public BPaymentType create() throws Exception;

  /**
   * 取得收银付款方式的列表。
   * 
   * @return 收银付款方式的列表。
   * @throws Exception
   */
  public List<BPaymentType> getAll() throws Exception;

  /**
   * 取得所有可用的收银付款方式。
   * 
   * @return 所有可用的收银付款方式。
   * @throws Exception
   */
  public List<BPaymentType> getAllValid() throws Exception;

  /**
   * 保存收银付款方式。
   * 
   * @param entity
   * @return 保存后的收银付款方式。
   * @throws Exception
   */
  public BPaymentType save(BPaymentType entity) throws Exception;

  /**
   * 启用收银付款方式。
   * 
   * @param uuid
   * @param oca
   * @return 收银付款方式。
   * @throws Exception
   */
  public BPaymentType enable(String uuid, long oca) throws Exception;

  /**
   * 停用收银付款方式。
   * 
   * @param uuid
   * @param oca
   * @return 收银付款方式。
   * @throws Exception
   */
  public BPaymentType disable(String uuid, long oca) throws Exception;

  /**
   * 根据uuid加载收银付款方式。
   * 
   * @param uuid
   * @return 收银付款方式。
   * @throws Exception
   */
  public BPaymentType load(String uuid) throws Exception;

  /**
   * 根据代码加载收银付款方式。
   * 
   * @param code
   * @return 收银付款方式。
   * @throws Exception
   */
  public BPaymentType loadByCode(String code) throws Exception;
}
