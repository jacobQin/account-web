/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-11 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.QBatchStatement;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.QStatement;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.filter.BatchFilter;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.filter.StatementFilter;
import com.hd123.m3.commons.gwt.base.client.biz.HasPerm;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviServiceAsync;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 异步调用接口
 * 
 * @author zhuhairui
 * 
 */
public interface PaymentNoticeServiceAsync extends M3ModuleServiceAsync, M3ModuleNaviServiceAsync {

  public void query(List<Condition> conditions, PageSort pageSort,
      AsyncCallback<RPageData<BPaymentNotice>> callback);

  public void create(AsyncCallback<BPaymentNotice> callback);

  public void queryStatement(StatementFilter filter, List<String> statementUuids,
      AsyncCallback<RPageData<QStatement>> callback);

  public void load(String uuid, AsyncCallback<BPaymentNotice> callback);

  public void loadByNumber(String billNumber, AsyncCallback<BPaymentNotice> callback);

  public void delete(String uuid, long version, AsyncCallback<Void> callback);

  public void effect(String uuid, String latestComment, long oca, AsyncCallback callback);

  public void abort(String uuid, long version, String comment, AsyncCallback<Void> callback);

  public void save(BPaymentNotice notice, BProcessContext task,
      AsyncCallback<BPaymentNotice> callback);

  public void generateBill(QBatchStatement accUnitAndCounterpart, boolean isSpligBill,
      HasPerm perm, String defKey, BTaskContext task, AsyncCallback<List<String>> callback);

  public void executeTask(BOperation operation, BPaymentNotice entity, BTaskContext task,
      boolean saveBeforeAction, AsyncCallback<String> callback);

  public void queryBatchStatement(BatchFilter filter,
      AsyncCallback<RPageData<QBatchStatement>> callback);

  public void queryProcessDefinition(AsyncCallback<List<BProcessDefinition>> callback);

  public void getCoopModes(AsyncCallback<List<String>> callback);

}
