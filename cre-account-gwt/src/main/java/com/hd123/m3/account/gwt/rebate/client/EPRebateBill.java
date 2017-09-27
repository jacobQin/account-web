/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	EPRebateBill.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client;

import java.util.List;
import java.util.Map;

import com.hd123.m3.account.gwt.rebate.client.biz.BAccountOption;
import com.hd123.m3.account.gwt.rebate.client.biz.BAccountOptionJsonUtil;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.rpc.RebateBillService;
import com.hd123.m3.account.gwt.rebate.client.rpc.RebateBillServiceAsync;
import com.hd123.m3.account.gwt.rebate.client.ui.RebateBillCreatePage;
import com.hd123.m3.account.gwt.rebate.client.ui.RebateBillEditPage;
import com.hd123.m3.account.gwt.rebate.client.ui.RebateBillLogPage;
import com.hd123.m3.account.gwt.rebate.client.ui.RebateBillSearchPage;
import com.hd123.m3.account.gwt.rebate.client.ui.RebateBillViewPage;
import com.hd123.m3.account.gwt.rebate.intf.client.RebateBillUrlParams;
import com.hd123.m3.account.gwt.rebate.intf.client.dd.PRebateBillDef;
import com.hd123.m3.commons.gwt.base.client.util.BUCNUtils;
import com.hd123.m3.commons.gwt.bpm.client.EPBpmModule2;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;

/**
 * 销售额返款单|入口
 * 
 * @author lixiaohong
 * @since 1.12
 *
 */
public class EPRebateBill extends EPBpmModule2<BRebateBill> {
  public static final String RESOURCE_KEY = "account.rebate";
  public static final String RESOURCE_PATH = "account/rebate";

  public static EPRebateBill getInstance() {
    return EPRebateBill.getInstance(EPRebateBill.class);
  }

  @Override
  public RebateBillServiceAsync getModuleService() {
    return RebateBillService.Locator.getService();
  }

  @Override
  public String getPrintTemplate() {
    return RESOURCE_PATH;
  }

  @Override
  public String getModuleCaption() {
    return PRebateBillDef.TABLE_CAPTION;
  }

  @Override
  public String getPermResource() {
    return RESOURCE_KEY;
  }
  
  public native void log(String text)  
  /*-{ 
      $wnd.console.log(text); 
  }-*/;  

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    if (start == null) {
      start = RebateBillUrlParams.START_NODE_SEARCH;
    }
    if (RebateBillUrlParams.START_NODE_SEARCH.equals(start)) {
      showContentPage(RebateBillSearchPage.getInstance(), urlParams);
    } else if (RebateBillUrlParams.START_NODE_CREATE.equals(start)) {
      showContentPage(RebateBillCreatePage.getInstance(), urlParams);
    } else if (RebateBillUrlParams.START_NODE_EDIT.equals(start)) {
      showContentPage(RebateBillEditPage.getInstance(), urlParams);
    } else if (RebateBillUrlParams.START_NODE_VIEW.equals(start)) {
      showContentPage(RebateBillViewPage.getInstance(), urlParams);
    } else if (RebateBillUrlParams.START_NODE_LOG.equals(start)) {
      showContentPage(RebateBillLogPage.getInstance(), urlParams);
    }
  }

  private Map<String, BAccountOption> accountOptions;

  /** 获取是否按笔返款 */
  public boolean isRebateByBill(String storeUuid) {
    if (storeUuid == null) {
      return false;
    }
    if (accountOptions == null) {
      accountOptions = BAccountOptionJsonUtil.mapFromJson(getModuleContext().get(
          RebateBillService.OPTION_ACCOUNT_OPTIONS));
    }

    BAccountOption option = accountOptions.get(storeUuid);
    if (option == null) {
      option = accountOptions.get(BAccountOption.DEFAULT_OPTION);
    }
    return option != null ? option.getRebateByBill() : false;
  }

  private List<HasUCN> paymentTypes;

  public List<HasUCN> getPaymentTypes() {
    if (paymentTypes == null) {
      paymentTypes = BUCNUtils.fromJson(getModuleContext().get(
          RebateBillService.OPTION_PAYMENT_TYPE));
    }
    return paymentTypes;
  }
}
