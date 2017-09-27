/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.rpc;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author subinzhu
 * 
 */
public interface PaymentCommonsServiceAsync extends M3ModuleServiceAsync {

  public void getSubjects(AdvanceSubjectFilter filter, AsyncCallback<List<BUCN>> callback);

  public void queryAccount(AccountDataFilter filter, AsyncCallback<RPageData<BAccount>> callback);

  public void queryAccountByStatement(AccountDataFilter filter,
      AsyncCallback<RPageData<BAccountStatement>> callback);

  public void queryAccountByBill(AccountDataFilter filter,
      AsyncCallback<RPageData<BAccountSourceBill>> callback);

  public void queryAccountByPaymentNotice(AccountDataFilter filter,
      AsyncCallback<RPageData<BAccountNotice>> callback);

  public void queryAccountByInvoiceReg(AccountDataFilter filter,
      AsyncCallback<RPageData<BAccountInvoice>> callback);

  public void getOverdueTerms(int direction, Collection<BAccount> accounts,
      AsyncCallback<Map<BAccount, List<BPaymentOverdueTerm>>> callback);

  public void getDepositSubjectRemainTotal(String accountUnit, String counterpart, String billUuid,
      String subject, AsyncCallback<BigDecimal> callback);

  public void getDepositCounterpartRemainTotal(String accountUnit, String counterpart,
      AsyncCallback<BigDecimal> callback);

  public void getDepositSubjectRemainTotals(String accountUnit, String counterpart,
      List<BRemainTotal> remainTotalIds, AsyncCallback<List<BRemainTotal>> callback);

  public void getContracts(String businessUnitUuid, String counterPartUuid,
      AsyncCallback<List<BUCN>> callback);

  public void getBillTypes(int Direction, AsyncCallback<List<BBillType>> callback);

  public void getDefaultOption(AsyncCallback<BDefaultOption> callback);

  public void getPaymentTypes(AsyncCallback<List<BUCN>> callback);

  public void getBanks(AsyncCallback<List<BBank>> callback);

  public void getCurrentEmployee(String userId, AsyncCallback<BUCN> callback);

  public void getInvoiceTypeMap(AsyncCallback<Map<String, String>> callback);
}
