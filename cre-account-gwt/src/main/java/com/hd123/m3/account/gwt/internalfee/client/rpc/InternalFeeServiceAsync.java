/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviServiceAsync;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * @author liuguilin
 * 
 */
public interface InternalFeeServiceAsync extends M3ModuleServiceAsync, M3ModuleNaviServiceAsync {
  public void query(List<Condition> conditions, PageSort pageSort,
      AsyncCallback<RPageData<BInternalFee>> callback);

  public void load(String uuid, AsyncCallback<BInternalFee> callback);

  public void loadByNumber(String number, AsyncCallback<BInternalFee> callback);

  public void create(AsyncCallback<BInternalFee> callback);

  public void save(BInternalFee bill, BProcessContext task, AsyncCallback<BInternalFee> callback);

  public void delete(String uuid, long version, AsyncCallback callback);

  public void effect(String uuid, String comment, long version, AsyncCallback callback);

  public void abort(String uuid, String comment, long version, AsyncCallback callback);

  public void executeTask(BOperation operation, BInternalFee entity, BTaskContext task,
      boolean saveBeforeAction, AsyncCallback<String> callback);

  public void queryVendors(CodeNameFilter filter, Boolean state, Boolean freezed,
      AsyncCallback<RPageData<BUCN>> callback);

  public void getVendorByCode(String code, Boolean state, Boolean freezed,
      AsyncCallback<BUCN> callback);
}
