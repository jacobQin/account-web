/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.payment.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviServiceAsync;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author chenpeisi
 * 
 */
public interface PayInvoiceRegServiceAsync extends M3ModuleServiceAsync, M3ModuleNaviServiceAsync {

  public void query(List<Condition> conditions, List<String> visibleColumns, PageSort pageSort,
      AsyncCallback<RPageData<BInvoiceReg>> callback);

  public void load(String uuid, AsyncCallback<BInvoiceReg> callback);

  public void loadByNumber(String billNumber, AsyncCallback<BInvoiceReg> callback);

  public void create(AsyncCallback<BInvoiceReg> callback);

  public void createByStatement(String statementUuid, int direction,
      AsyncCallback<BInvoiceReg> callback);

  public void createByPayment(String paymentUuid, AsyncCallback<BInvoiceReg> callback);

  public void remove(String uuid, long version, AsyncCallback<Void> callback);

  public void effect(String uuid, long version, AsyncCallback<Void> callback);

  public void abort(String uuid, long version, AsyncCallback<Void> callback);

  public void save(BInvoiceReg entity, BProcessContext task, AsyncCallback<BInvoiceReg> callback);

  public void getInvoiceCode(String uuid, AsyncCallback<String> callback);

  public void getConfig(AsyncCallback<BInvoiceRegConfig> callback);

  public void getDefaultValue(AsyncCallback<BDefaultOption> callback);

  public void executeTask(BOperation operation, BInvoiceReg entity, BTaskContext task,
      boolean saveBeforeAction, AsyncCallback<String> callback);
}
