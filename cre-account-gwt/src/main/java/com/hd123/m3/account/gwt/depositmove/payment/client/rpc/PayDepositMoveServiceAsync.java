/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositMoveServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.payment.client.rpc;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BAdvanceContract;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviServiceAsync;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author zhuhairui
 * 
 */
public interface PayDepositMoveServiceAsync extends M3ModuleServiceAsync, M3ModuleNaviServiceAsync {

  public void query(List<Condition> conditions, List<String> visibleColumnNames, PageSort pageSort,
      AsyncCallback<RPageData<BDepositMove>> callback);

  public void load(String uuid, AsyncCallback<BDepositMove> callback);

  public void loadByNumber(String billNumber, AsyncCallback<BDepositMove> callback);

  public void create(AsyncCallback<BDepositMove> callback);

  public void createByAdvance(String advanceUuid, AsyncCallback<BDepositMove> callback);

  public void save(BDepositMove entity, BProcessContext task, AsyncCallback<BDepositMove> callback);

  public void remove(String uuid, long version, AsyncCallback callback);

  public void getUniqueContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType, boolean isGetAdvance, AsyncCallback<BAdvanceContract> callback);

  public void getBalance(String accountUnitUuid, String counterpartUuid, String subjectUuid,
      String contractUuid, AsyncCallback<BigDecimal> callback);

  public void effect(String uuid, String comment, long oca, AsyncCallback callback);
  
  public void abort(String uuid, String comment, long oca, AsyncCallback callback);

  public void executeTask(BOperation operation, BDepositMove entity, BTaskContext task,
      boolean saveBeforeAction, AsyncCallback<String> callback);
}
