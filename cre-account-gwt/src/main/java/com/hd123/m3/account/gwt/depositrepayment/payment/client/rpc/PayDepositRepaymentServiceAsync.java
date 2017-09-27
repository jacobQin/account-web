/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PayDepositRepaymentServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.client.rpc;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviServiceAsync;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 预付款还款单异步接口
 * 
 * @author chenpeisi
 * 
 */
public interface PayDepositRepaymentServiceAsync extends M3ModuleServiceAsync,
    M3ModuleNaviServiceAsync {

  public void loadByNumber(String keyword, AsyncCallback<BDepositRepayment> callback);

  public void load(String uuid, AsyncCallback<BDepositRepayment> callback);

  public void query(List<Condition> conditions, List<String> visibleColumnNames, PageSort pageSort,
      AsyncCallback<RPageData<BDepositRepayment>> callback);

  public void remove(String uuid, long version, AsyncCallback<Void> callback);

  public void effect(String uuid, String comment, long oca, AsyncCallback callback);
  
  public void abort(String uuid, String comment, long oca, AsyncCallback callback);

  public void create(AsyncCallback<BDepositRepayment> callback);

  public void createByAdvance(String advanceUuid, AsyncCallback<BDepositRepayment> callback);

  public void save(BDepositRepayment entity, BProcessContext task,
      AsyncCallback<BDepositRepayment> callback);

  public void getUniqueAdvance(String accountUnitUuid, String counterpartUuid,
      String counterpartType, AsyncCallback<BContract> callback);

  public void getAdvance(String accountUnitUuid, String counterpartUuid, String subjectUuid,
      String contractUuid, AsyncCallback<BigDecimal> callback);

  public void executeTask(BOperation operation, BDepositRepayment entity, BTaskContext task,
      boolean saveBeforeAction, AsyncCallback<String> callback);

  public void getPaymentTypes(AsyncCallback<List<BUCN>> callback);

  public void getBanks(AsyncCallback<List<BBank>> callback);

  public void getCurrentEmployee(String id, AsyncCallback<BUCN> callback);

}
