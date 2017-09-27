/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateBillService.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.rebate.client.biz.BContract;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesBill;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.sales.RebateLineFilter;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 销售额返款单|同步接口。
 * 
 * @author lixiaohong
 * @since 1.12
 */
@RemoteServiceRelativePath("rebate/bill")
public interface RebateBillService extends M3BpmModuleService2<BRebateBill> {
  /** 是否按笔返款(boolean, default = false) */
  public static final String OPTION_REBATE_SINGLE = "rebateSingle";
  public static final String OPTION_ACCOUNT_OPTIONS = "accountOptions";
  public static final String OPTION_PAYMENT_TYPE = "paymentType";

  public static class Locator {
    private static RebateBillServiceAsync service = null;

    public static RebateBillServiceAsync getService() {
      if (service == null)
        service = GWT.create(RebateBillService.class);
      return service;
    }
  }

  /**
   * 查询指定条件的销售明细
   * 
   * @param filter
   * @return
   * @throws ClientBizException
   */
  RPageData<BSalesBill> queryProductRpts(RebateLineFilter filter) throws ClientBizException;

  /**
   * 得到合同文档
   * 
   * @return
   */
  BContract getContractDoc(String contractId);
}
