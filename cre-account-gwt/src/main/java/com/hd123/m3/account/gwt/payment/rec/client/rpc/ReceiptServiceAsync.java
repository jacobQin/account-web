/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiptServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-4 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.rpc;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.rec.client.biz.BReceiptConfig;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviServiceAsync;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author subinzhu
 * 
 */
public interface ReceiptServiceAsync extends M3ModuleServiceAsync, M3ModuleNaviServiceAsync {

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
  
  public void createByAccountIds(Collection<String> accountIds, AsyncCallback<BPayment> callback);
  
  public void save(BPayment bill, BProcessContext task, AsyncCallback<BPayment> callback);

  public void delete(String uuid, long oca, AsyncCallback callback);

  public void abort(String uuid, String comment, long oca, AsyncCallback callback);

  public void effect(String uuid, String comment, long oca, AsyncCallback callback);

  public void executeTask(BOperation operation, BPayment entity, BTaskContext task,
      boolean saveBeforeAction, AsyncCallback<String> callback);

  public void load(String uuid, boolean isForEdit, AsyncCallback<BPayment> callback);

  public void loadByNumber(String billNumber, boolean isForEdit, AsyncCallback<BPayment> callback);

  public void loadConfig(AsyncCallback<BReceiptConfig> callback);

  public void saveConfig(BReceiptConfig config, AsyncCallback<Void> callback);

  public void loadRegiste(String uuid, AsyncCallback<BPayment> callback);

  public void sortLines(List<BPaymentAccountLine> lines, List<Order> orders,
      AsyncCallback<List<BPaymentAccountLine>> callback);

  public void getContract(String accountUnitUuid, String counterpartUuid, String counterpartType,
      AsyncCallback<BContract> callback);
  
  public void filterSubjects(List<BUCN> subjects, BUCN store,BUCN subject,BUCN contract,AsyncCallback<List<BUCN>> callback);
}