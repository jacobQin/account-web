/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AccountSettleServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BAccountSettle;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;

/**
 * @author huangjunxian
 * 
 */
public interface AccountSettleServiceAsync extends ModuleServiceAsync {

  void query(AccountSettleFilter filter, AsyncCallback<RPageData<BAccountSettle>> callback);

  void calculate(String calculateId, List<BAccountSettle> accountSettles, String processDefKey,
      String permGroupId, String permGroupTitle, AsyncCallback<String> callback);

  void queryProcessDefinition(AsyncCallback<List<BProcessDefinition>> callback);

  void getOnlyOneContract(String accountUnitUuid, String counterpartUuid, String counterpartType,
      AsyncCallback<BContract> callback);

  void getCoopModes(AsyncCallback<List<String>> callback);

  void getCalculateId(AsyncCallback<String> callback);
}
