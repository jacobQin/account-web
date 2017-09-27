/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayServiceAsync.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.payment.client.rpc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLine;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositTotal;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviServiceAsync;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author chenpeisi
 * 
 */
public interface PayDepositServiceAsync extends M3ModuleServiceAsync, M3ModuleNaviServiceAsync {

  public void query(List<Condition> conditions, List<String> visibleColumns, PageSort pageSort,
      AsyncCallback<RPageData<BDeposit>> callback);

  public void load(String uuid, AsyncCallback<BDeposit> callback);

  public void loadByNumber(String billNumber, AsyncCallback<BDeposit> callback);

  public void create(AsyncCallback<BDeposit> callback);

  public void createByAdvance(String advanceUuid, AsyncCallback<BDeposit> callback);

  public void save(BDeposit pay, BProcessContext task, AsyncCallback<BDeposit> callback);

  public void remove(String uuid, long oca, AsyncCallback<Void> callback);

  public void effect(String uuid, long version, AsyncCallback callback);
  
  public void abort(String uuid, long version, AsyncCallback callback);

  public void getAdvance(String accountUnitUuid, String counterpartUuid, String subjectUuid,
      String contractUuid, AsyncCallback<BDepositTotal> callback);

  public void getContract(String accountUnitUuid, String counterpartUuid, String counterpartType,
      AsyncCallback<BContract> callback);

  public void executeTask(BOperation operation, BDeposit entity, BTaskContext task,
      boolean saveBeforeAction, AsyncCallback<String> callback);

  public void getPaymentTypes(AsyncCallback<List<BUCN>> callback);

  public void getBanks(AsyncCallback<List<BBank>> callback);

  public void getCurrentEmployee(String id, AsyncCallback<BUCN> callback);

  public void getAdvances(String accountUnitUuid, String counterpartUuid,
      Collection<String> subjectUuids, String contractUuid,
      AsyncCallback<Map<String, BDepositTotal>> callback);

  public void importFromContract(String contractUuid, String accountUnitUuid,
      String counterpartUuid, AsyncCallback<List<BDepositLine>> callback);

}
