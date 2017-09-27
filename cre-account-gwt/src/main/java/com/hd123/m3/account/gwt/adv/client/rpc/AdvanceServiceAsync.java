/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AdvanceServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.adv.client.biz.BAdvanceData;
import com.hd123.m3.account.gwt.adv.client.biz.BAdvanceLog;
import com.hd123.m3.account.gwt.adv.client.filter.AdvanceFilter;
import com.hd123.m3.account.gwt.adv.client.filter.AdvanceLogFilter;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;

/**
 * 异步调用接口
 * 
 * @author zhuhairui
 * 
 */
public interface AdvanceServiceAsync extends ModuleServiceAsync {

  public void getAdvanceDataByCounterpartUnitUuid(String counterpartUnitUuid,
      AsyncCallback<BAdvanceData> callback);

  public void queryCounterpartUnit(AdvanceFilter filter, AsyncCallback<RPageData<BCounterpart>> callback);

  public void queryLog(AdvanceLogFilter filter, AsyncCallback<RPageData<BAdvanceLog>> callback);

  public void getAdvanceDataByAccountUnitUuid(String accountUnitUuid,
      AsyncCallback<BAdvanceData> callback);
}
