/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	OptionServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.commons.client.biz.BConfigOption;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.commons.client.biz.BOption;
import com.hd123.m3.account.gwt.commons.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.option.client.BIncompleteMonthByRealDaysOption;
import com.hd123.m3.account.gwt.option.client.BRebateByBillOption;
import com.hd123.m3.account.gwt.option.client.BStoreMonthDaysOption;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;

/**
 * @author zhuhairui
 * 
 */
public interface OptionServiceAsync extends ModuleServiceAsync {

  public void getOption(AsyncCallback<BOption> callback);

  public void getConfigOption(AsyncCallback<BConfigOption> callback);

  public void getDefaultOption(AsyncCallback<BDefaultOption> callback);

  public void setConfigOption(BConfigOption configOption, AsyncCallback<Void> callback);

  public void setDefaultOption(BDefaultOption defaultOption, AsyncCallback<Void> callback);

  public void saveStoreMonthDaysOptions(List<BStoreMonthDaysOption> options,
      AsyncCallback<Void> callback);

  public void getIncompleteMonthByRealDaysOptions(AsyncCallback<List<BIncompleteMonthByRealDaysOption>> callback);
  
  public void saveIncompleteMonthByRealDaysOption(List<BIncompleteMonthByRealDaysOption> options,
      AsyncCallback<Void> callback);

  public void getStoreMonthDaysOptions(AsyncCallback<List<BStoreMonthDaysOption>> callback);

  public void getSubjectByCode(String code, AsyncCallback<BUCN> callback);

  public void querySubjects(CodeNameFilter filter, AsyncCallback<RPageData<BUCN>> callback);

  public void getPaymentTypeByCode(String code, AsyncCallback<BUCN> callback);

  public void queryPaymentTypes(CodeNameFilter filter, AsyncCallback<RPageData<BUCN>> callback);

  public void queryProcessDefinition(String keyPrefix,
      AsyncCallback<List<BProcessDefinition>> callback);

  public void getProcessDefinition(String key, AsyncCallback<BProcessDefinition> callback);

  public void getPaymentTypes(AsyncCallback<List<BUCN>> callback);
  
  public void saveRebateByBillOption(List<BRebateByBillOption> options,
      AsyncCallback<Void> callback);
  
  public void getRebateByBillOptions(AsyncCallback<List<BRebateByBillOption>> callback);
}
