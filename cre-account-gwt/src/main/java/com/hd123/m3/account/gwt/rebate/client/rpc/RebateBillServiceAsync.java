/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateBillServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.rebate.client.biz.BContract;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesBill;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.sales.RebateLineFilter;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2Async;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 销售返款单|异步接口。
 * 
 * @author lixiaohong
 * @since 1.12
 *
 */
public interface RebateBillServiceAsync extends M3BpmModuleService2Async<BRebateBill> {
  void queryProductRpts(RebateLineFilter filter, AsyncCallback<RPageData<BSalesBill>> callback);

  void getContractDoc(String contractId, AsyncCallback<BContract> callback);
}
