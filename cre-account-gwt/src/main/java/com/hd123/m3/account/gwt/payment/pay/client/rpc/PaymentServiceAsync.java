/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-4 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.rpc;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.pay.client.biz.BPaymentConfig;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviServiceAsync;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author subinzhu
 * 
 */
public interface PaymentServiceAsync extends M3ModuleServiceAsync, M3ModuleNaviServiceAsync {

  public void query(List<Condition> conditions, PageSort pageSort,
      AsyncCallback<RPageData<BPayment>> callback);

  public void create(AsyncCallback<BPayment> callback);

  public void createByStatements(Collection<String> statementUuids, AsyncCallback<BPayment> callback);

  public void createByInvoices(Collection<String> invoiceUuids, AsyncCallback<BPayment> callback);

  public void createByPaymentNotices(Collection<String> noticeUuids,
      AsyncCallback<BPayment> callback);

  public void createBySourceBills(Collection<String> sourceBillUuids,
      AsyncCallback<BPayment> callback);

  public void createByAccounts(Collection<String> accountUuids, AsyncCallback<BPayment> callback);

  public void save(BPayment bill, BProcessContext task, AsyncCallback<BPayment> callback);

  public void delete(String uuid, long oca, AsyncCallback callback);

  public void abort(String uuid, String comment, long oca, AsyncCallback callback);

  public void effect(String uuid, String comment, long oca, AsyncCallback callback);

  public void executeTask(BOperation operation, BPayment entity, BTaskContext task,
      boolean saveBeforeAction, AsyncCallback<String> callback);

  public void fetchReceiptTotal(String uuid, String uuid2, AsyncCallback<BTotal> callback);

  public void load(String uuid, boolean isForEdit, AsyncCallback<BPayment> callback);

  public void loadByNumber(String billNumber, boolean isForEdit, AsyncCallback<BPayment> callback);

  public void saveConfig(BPaymentConfig config, AsyncCallback<Void> callback);

  public void registe(BPayment bill, AsyncCallback<Void> callback);

  public void saveInvoice(BPayment bill, AsyncCallback<Void> callback);

  public void loadRegiste(String uuid, AsyncCallback<BPayment> callback);
}