/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	DepositMoveServiceAsync.java
 * 模块说明：	
 * 修改历史：
 *  2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BAdvanceContract;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.filter.AdvanceContractFilter;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.filter.AdvanceSubjectFilter;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleServiceAsync;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 预存款转移单|异步接口
 * 
 * @author zhuhairui
 * 
 */
public interface DepositMoveServiceAsync extends M3ModuleServiceAsync {

  public void querySubject(AdvanceSubjectFilter filter, AsyncCallback<RPageData<BUCN>> callback);

  public void getSubjectByCode(String code, int direction, String accountUnitUuid,
      String counterpartUuid, String contractUuid, boolean isQueryAdvance, AsyncCallback callback);

  public void queryContract(AdvanceContractFilter filter,
      AsyncCallback<RPageData<BAdvanceContract>> callback);

  public void getContractByBillNumber(String billNumber, int direction, String accountUnituuid,
      boolean isQueryAdvance, AsyncCallback<BAdvanceContract> callback);

}
