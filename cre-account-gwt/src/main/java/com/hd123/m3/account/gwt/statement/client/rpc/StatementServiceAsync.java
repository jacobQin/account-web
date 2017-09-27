/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.rpc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.statement.client.biz.BAccountSettleLog;
import com.hd123.m3.account.gwt.statement.client.biz.BAttachment;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccDetail;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviServiceAsync;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 异步调用接口
 * 
 * @author huangjunxian
 * 
 */
public interface StatementServiceAsync extends M3ModuleServiceAsync, M3ModuleNaviServiceAsync {
  public void query(List<Condition> conditions, List<String> visibleColumnNames, PageSort pageSort,
      AsyncCallback<RPageData<BStatement>> callback);

  public void load(String uuid, String startNode, AsyncCallback<BStatement> callback);

  public void loadByNumber(String billNumber, String startNode, AsyncCallback<BStatement> callback);

  public void remove(String uuid, long version, AsyncCallback<Void> callback);

  public void effect(String uuid, long version, AsyncCallback callback);

  public void abort(String uuid, long version, AsyncCallback<Void> callback);

  public void queryAccount(StatementAccFilter filter, Collection<String> excludedUuids,
      AsyncCallback<RPageData<BStatementLine>> callback);

  public void getAccountDetail(String id, boolean active,
      AsyncCallback<BStatementAccDetail> callback);

  public void getBillType(AsyncCallback<BBillType> callback);

  public void getBillTypeMap(AsyncCallback<Map<String, String>> callback);

  public void executeTask(BOperation operation, BStatement entity, BTaskContext task,
      boolean saveBeforeAction, AsyncCallback<String> callback);

  public void queryAccountSettleLog(List<Condition> conditions, PageSort pageSort,
      AsyncCallback<RPageData<BAccountSettleLog>> callback);

  public void uploadFile(String fileName, AsyncCallback<BAttachment> callback);

  public void create(AsyncCallback<BStatement> callback);

  public void save(BStatement entity, BProcessContext task, AsyncCallback<BStatement> callback);

  public void getOnlyOneContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType, AsyncCallback<BContract> callback);

  public void queryLines(StatementAccFilter filter, boolean includeSelf,
      AsyncCallback<List<BStatementLine>> callback);

  public void queryDesLines(StatementAccFilter filter, boolean includeSelf,
      AsyncCallback<List<BStatementLine>> callback);

  public void queryAccDetails(StatementAccFilter filter, boolean includeSelf,
      AsyncCallback<List<BStatementAccDetail>> callback);

  public void getSubjectByCode(String code, AsyncCallback<BUCN> callback);

  public void validateBeforeAction(String uuid, String action, AsyncCallback<String> callback);

  public void cleanSettle(String uuid, AsyncCallback<Void> callback);

  public void isMaxSettled(String accountSettleUuid, AsyncCallback<Boolean> callback);
}
