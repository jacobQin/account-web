/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegService.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.payment.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviService;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author chenpeisi
 * 
 */
@RemoteServiceRelativePath("account/payInvoiceReg")
public interface PayInvoiceRegService extends M3ModuleService, M3ModuleNaviService {

  /** 计算精度,小数位数 */
  public static final String SCALE = "scale";
  /** 计算精度,舍入算法 */
  public static final String ROUNDING_MODE = "roundingMode";
  public static final String KEY_INVOICE_TYPE = "invoiceType";
  public static final String KEY_BILL_TYPES = "billTypes";

  public static class Locator {
    private static PayInvoiceRegServiceAsync service = null;

    public static PayInvoiceRegServiceAsync getService() {
      if (service == null)
        service = GWT.create(PayInvoiceRegService.class);
      return service;
    }
  }

  /**
   * 查询付款发票登记单。
   * 
   * @param condition
   * @param visibleColumnNames
   * @param pageSort
   * @return
   * @throws ClientBizException
   */
  public RPageData<BInvoiceReg> query(List<Condition> condition, List<String> visibleColumnNames,
      PageSort pageSort) throws ClientBizException;

  /**
   * 根据uuid取得付款发票登记单。
   * 
   * @param uuid
   *          付款发票登记单标识uuid not null。
   * @return 付款发票登记单
   * @throws ClientBizException
   */
  public BInvoiceReg load(String uuid) throws ClientBizException;

  /**
   * 根据单号取得付款发票登记单。
   * 
   * @param billNumber
   *          付款发票登记单单号 not null。
   * @return 付款发票登记单
   * @throws ClientBizException
   */
  public BInvoiceReg loadByNumber(String billNumber) throws ClientBizException;

  /**
   * 删除付款发票登记单。
   * 
   * @param uuid
   *          付款发票登记单标识uuid not null。
   * @param version
   *          版本控制 version not null。
   * @throws ClientBizException
   */
  public void remove(String uuid, long version) throws ClientBizException;

  /**
   * 创建付款发票登记单。
   * 
   * @return
   * @throws ClientBizException
   */
  public BInvoiceReg create() throws ClientBizException;

  /**
   * 根据账单创建付款发票登记单。
   * 
   * @param statementUuid
   *          账单uuid not null
   * @param direction
   *          收付方向
   * @return 付款发票登记单
   * @throws ClientBizException
   */
  public BInvoiceReg createByStatement(String statementUuid, int direction)
      throws ClientBizException;

  /**
   * 根据付款单创建付款发票登记单。
   * 
   * @param paymentUuid
   *          付款单uuid not null
   * @return 付款发票登记单
   * @throws ClientBizException
   */
  public BInvoiceReg createByPayment(String paymentUuid) throws ClientBizException;

  /**
   * 生效付款发票登记单。
   * 
   * @param uuid
   *          付款发票登记单标识uuid not null。
   * @param version
   *          版本控制 oca not null。]
   * @param remark
   * 
   * @throws ClientBizException
   */
  public void effect(String uuid, long version) throws ClientBizException;

  /**
   * 作废付款发票登记单。
   * 
   * @param uuid
   *          付款发票登记单标识uuid not null。
   * @param version
   *          版本控制 version not null。
   * @throws ClientBizException
   */
  public void abort(String uuid, long version) throws ClientBizException;

  /**
   * 保存付款发票登记单。
   * 
   * @param entity
   * @return 付款发票登记单
   * @throws ClientBizException
   */
  public BInvoiceReg save(BInvoiceReg entity, BProcessContext task) throws ClientBizException;

  /**
   * 获取发票代码。
   * 
   * @param uuid
   *          对方结算中心uuid not null
   * @return 发票代码
   * @throws ClientBizException
   */
  public String getInvoiceCode(String uuid) throws ClientBizException;

  /**
   * 执行BPM任务。
   * 
   * @param outgoing
   *          任务出口。not null。
   * @param detailedTask
   *          任务详情。
   * @param entity
   *          付款发票登记单。not null。
   * @throws ClientBizException
   */
  public String executeTask(BOperation operation, BInvoiceReg entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException;

  /**
   * 
   * @return
   * @throws ClientBizException
   */
  public BInvoiceRegConfig getConfig() throws ClientBizException;

  /**
   * 
   * @return
   * @throws ClientBizException
   */
  public BDefaultOption getDefaultValue() throws ClientBizException;
}
