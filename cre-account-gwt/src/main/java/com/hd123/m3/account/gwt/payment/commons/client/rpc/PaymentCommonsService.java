/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentService.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.rpc;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.payment.commons.client.AdvanceSubjectFilter;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentOverdueTerm;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BRemainTotal;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountInvoice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountSourceBill;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author subinzhu
 * 
 */
@RemoteServiceRelativePath("account/payment/commons")
public interface PaymentCommonsService extends M3ModuleService {

  public static class Locator {
    private static PaymentCommonsServiceAsync service = null;

    public static PaymentCommonsServiceAsync getService() {
      if (service == null)
        service = GWT.create(PaymentCommonsService.class);
      return service;
    }
  }

  /**
   * 根据方向获取预存款科目列表
   * 
   * @param direction
   *          科目方向。
   * @return 返回科目列表
   * @throws Exception
   */
  public List<BUCN> getSubjects(AdvanceSubjectFilter filter) throws Exception;

  /**
   * 查询账款。
   * 
   * @param filter
   *          筛选条件
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
   * @param counterpartUuid
   *          对方结算中心uuid， not null
   * @return 账款列表
   * @throws ClientBizException
   */
  public RPageData<BAccountNotice> queryAccountByPaymentNotice(AccountDataFilter filter)
      throws ClientBizException;

  /**
   * 从发票登记单添加账款。
   * 
   * @param filter
   *          筛选条件
   * @return 账款列表
   * @throws ClientBizException
   */
  public RPageData<BAccountInvoice> queryAccountByInvoiceReg(AccountDataFilter filter)
      throws ClientBizException;

  /**
   * 获取账款的滞纳金条款
   * 
   * @param direction
   * @param accounts
   * @return 滞纳金条款Map
   * @throws ClientBizException
   */
  public Map<BAccount, List<BPaymentOverdueTerm>> getOverdueTerms(int direction,
      Collection<BAccount> accounts) throws ClientBizException;

  /**
   * 获取预存款科目余额
   * 
   * @param accountUnit
   * @param counterpart
   * @param billUuid
   * @param subject
   * @return
   * @throws ClientBizException
   */
  public BigDecimal getDepositSubjectRemainTotal(String accountUnit, String counterpart,
      String billUuid, String subject) throws ClientBizException;

  /**
   * 取得指定的对方单位的预存款余额
   * 
   * @param accountUnit
   *          结算中心,为null返回BigDecimal.ZERO;
   * @param counterpart
   *          对方单位，为null返回BigDecimal.ZERO;
   * @throws ClientBizException
   */
  public BigDecimal getDepositCounterpartRemainTotal(String accountUnit, String counterpart)
      throws ClientBizException;

  /**
   * 批量获取预存款科目余额
   * 
   * @param accountUnit
   * @param counterpart
   * @param remainTotalIds
   * @return
   * @throws ClientBizException
   */
  public List<BRemainTotal> getDepositSubjectRemainTotals(String accountUnit, String counterpart,
      List<BRemainTotal> remainTotalIds) throws ClientBizException;

  /**
   * 根据合同的业务单位uuid以及合同的对方结算单位uuid获取该结算单位的所有合同
   * 
   * @param businessUnitUuid
   *          合同的业务单位uuid
   * @param counterPartUuid
   *          合同的对方结算单位uuid
   * @return 返回合同列表
   * @throws Exception
   */
  public List<BUCN> getContracts(String businessUnitUuid, String counterPartUuid) throws Exception;

  /**
   * 获取单据类型
   * 
   * @return 单据类型
   * @throws Exception
   */
  public List<BBillType> getBillTypes(int direction) throws Exception;

  /**
   * 获取默认配置
   * 
   * @return 默认配置
   * @throws Exception
   */
  public BDefaultOption getDefaultOption() throws Exception;

  /**
   * 获取付款方式列表
   * 
   * @return 付款方式列表
   * @throws Exception
   */
  public List<BUCN> getPaymentTypes() throws Exception;

  /**
   * 获取银行列表
   * 
   * @return 银行列表
   * @throws Exception
   */
  public List<BBank> getBanks() throws Exception;

  /**
   * 获取当前员工
   * 
   * @param userId
   *          用户ID
   * @return 当前员工
   * @throws Exception
   */
  public BUCN getCurrentEmployee(String userId) throws Exception;

  /**
   * 获取发票类型
   * 
   * @return
   * @throws Exception
   */
  public Map<String, String> getInvoiceTypeMap() throws Exception;
}
