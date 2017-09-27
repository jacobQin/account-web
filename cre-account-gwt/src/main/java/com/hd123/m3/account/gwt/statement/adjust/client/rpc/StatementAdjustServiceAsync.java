/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementAdjustServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.QStatement;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget.StatementBrowseFilter;
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
public interface StatementAdjustServiceAsync extends M3ModuleServiceAsync, M3ModuleNaviServiceAsync {

  public void query(List<Condition> conditions, List<String> visibleColumnNames, PageSort pageSort,
      AsyncCallback<RPageData<BStatementAdjust>> callback);

  public void queryStatement(StatementBrowseFilter filter,
      AsyncCallback<RPageData<QStatement>> callback);

  public void queryStatementByBillNum(String billNumber, AsyncCallback<QStatement> callback);

  public void create(AsyncCallback<BStatementAdjust> callback);

  public void createByStatement(String statementUuid, AsyncCallback<BStatementAdjust> callback);

  public void save(BStatementAdjust statementAdjust, BProcessContext task,
      AsyncCallback<BStatementAdjust> callback);

  public void load(String uuid, String scence, AsyncCallback<BStatementAdjust> callback);

  public void loadByNumber(String billNumber, String scence,
      AsyncCallback<BStatementAdjust> callback);

  public void delete(String uuid, long version, AsyncCallback<Void> callback);

  public void abort(String uuid, long version, boolean effectStatement, String comment,
      AsyncCallback<Void> callback);

  public void getOnlyOneContractByCounterpart(String counterpartUuid, String counterpartType,
      AsyncCallback<BContract> callback);

  public void getStatementByUuid(String uuid, AsyncCallback<QStatement> callback);

  public void getBillType(AsyncCallback<BBillType> callback);

  public void effect(String uuid, String comment, long oca, AsyncCallback callback);

  public void executeTask(BOperation operation, BStatementAdjust entity, BTaskContext task,
      boolean saveBeforeAction, AsyncCallback<String> callback);

  public void validateAccountUnitAndCounterpartAndContractByStatement(QStatement statement,
      AsyncCallback<String> callback);

}
