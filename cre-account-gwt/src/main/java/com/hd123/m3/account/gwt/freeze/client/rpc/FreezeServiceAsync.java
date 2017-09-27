/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	FreezeServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-3 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreeze;
import com.hd123.m3.account.gwt.freeze.client.ui.filter.AccountFilter;
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
public interface FreezeServiceAsync extends M3ModuleServiceAsync, M3ModuleNaviServiceAsync {
  public void query(List<Condition> conditions, PageSort pageSort,
      AsyncCallback<RPageData<BFreeze>> callback);

  public void load(String uuid, AsyncCallback<BFreeze> callback);

  public void loadByNumber(String billNumber, AsyncCallback<BFreeze> callback);

  public void unfreeze(String uuid, long version, String unfreezeReason,
      AsyncCallback<Void> callback);

  public void freeze(List<String> accountUuids, BFreeze freeze, String freezeReason,
      AsyncCallback<BFreeze> callback);

  public void create(AsyncCallback<BFreeze> callback);

  public void queryAccount(AccountFilter filter, List<String> accUuids,
      AsyncCallback<RPageData<BAccount>> callback);

}
