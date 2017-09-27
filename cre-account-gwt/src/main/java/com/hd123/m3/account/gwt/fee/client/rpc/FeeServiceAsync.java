/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeServiceAsync.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.fee.client.biz.BAccSettle;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeTemplate;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviServiceAsync;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.m3.commons.gwt.widget.client.biz.BImpParams;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.quartz.gwt.client.rpc.BJobScheduleHandler;

/**
 * @author subinzhu
 * 
 */
public interface FeeServiceAsync extends M3ModuleServiceAsync, M3ModuleNaviServiceAsync {

  public void load(String uuid, AsyncCallback<BFee> callback);

  public void loadByNumber(String number, AsyncCallback<BFee> callback);

  public void create(AsyncCallback<BFee> callback);

  public void save(BFee bill, BProcessContext task, AsyncCallback<BFee> callback);

  public void delete(String uuid, long oca, AsyncCallback callback);

  public void effect(String uuid, String comment, long oca, AsyncCallback callback);

  public void abort(String uuid, String comment, long oca, AsyncCallback callback);

  public void query(List<Condition> conditions, PageSort pageSort,
      AsyncCallback<RPageData<BFee>> callback);

  public void getOnlyOneContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType, AsyncCallback<BContract> callback);

  public void executeTask(BOperation operation, BFee entity, BTaskContext task,
      boolean saveBeforeAction, AsyncCallback<String> callback);

  public void getAccSettle(String contractUuid, AsyncCallback<BAccSettle> callback);

  public void exportFile(BFeeTemplate template, AsyncCallback<String> callback);

  public void importFile(BImpParams impParams, String permGroupId, String permGroupTitle,
      AsyncCallback<BJobScheduleHandler> callback);
}
