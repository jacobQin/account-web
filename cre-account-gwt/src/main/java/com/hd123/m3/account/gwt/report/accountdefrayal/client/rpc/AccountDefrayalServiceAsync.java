/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountDefRayalServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-9 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.report.accountdefrayal.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.report.accountdefrayal.client.biz.BAccountDefrayal;
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
public interface AccountDefrayalServiceAsync extends M3ModuleServiceAsync {

  public void query(List<Condition> conditions, List<String> visibleColumnNames, PageSort pageSort,
      AsyncCallback<RPageData<BAccountDefrayal>> callback);
}
