/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	EPInvoiceExchange.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.m3.account.gwt.base.client.AccSessions;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.rpc.InvoiceExchangeService;
import com.hd123.m3.account.gwt.ivc.exchange.client.rpc.InvoiceExchangeServiceAgent;
import com.hd123.m3.account.gwt.ivc.exchange.client.rpc.InvoiceExchangeServiceAgent.Callback;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.page.InvoiceExchangeCreatePage;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.page.InvoiceExchangeEditPage;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.page.InvoiceExchangeLogPage;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.page.InvoiceExchangeSearchPage;
import com.hd123.m3.account.gwt.ivc.exchange.client.ui.page.InvoiceExchangeViewPage;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd.PInvoiceExchangeDef;
import com.hd123.m3.commons.gwt.base.client.M3Sessions;
import com.hd123.m3.commons.gwt.base.client.util.BUCNUtils;
import com.hd123.m3.commons.gwt.bpm.client.EPBpmModule2;
import com.hd123.m3.commons.gwt.bpm.client.rpc2.M3BpmModuleService2Async;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;

/**
 * 发票交换单|模块入口
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class EPInvoiceExchange extends EPBpmModule2<BInvoiceExchange> {
  public static final String RESOURCE_KEY = "account.invoice.exchange";
  public static final String RESOURCE_PATH = "account/ivc/exchange";

  public static EPInvoiceExchange getInstance() {
    return EPInvoiceExchange.getInstance(EPInvoiceExchange.class);
  }

  @Override
  public M3BpmModuleService2Async getModuleService() {
    return InvoiceExchangeService.Locator.getService();
  }

  @Override
  public String getPrintTemplate() {
    return RESOURCE_PATH;
  }

  @Override
  public String getModuleCaption() {
    return PInvoiceExchangeDef.TABLE_CAPTION;
  }

  @Override
  public String getPermResource() {
    return RESOURCE_KEY;
  }

  private Map<String, String> counterpartTypeMap;
  private Map<String, String> captionMap;

  /**
   * 获取对方单位类型。
   * 
   * @return
   */
  public Map<String, String> getCounterpartTypeMap() {
    if (counterpartTypeMap == null) {
      String str = getModuleContext().get(AccSessions.KEY_COUNTERPART_TYPE);
      if (str == null) {
        counterpartTypeMap = new HashMap<String, String>();
      } else {
        counterpartTypeMap = CollectionUtil.toMap(str);
      }
    }
    return counterpartTypeMap;
  }

  public Map<String, String> getCaptionMap() {
    if (captionMap == null) {
      String str = getModuleContext().get(M3Sessions.KEY_FORM_CAPTION);
      if (str == null) {
        captionMap = new HashMap<String, String>();
      } else {
        captionMap = CollectionUtil.toMap(str);
      }
    }
    return captionMap;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    if (StringUtil.isNullOrBlank(start)) {
      start = InvoiceExchangeSearchPage.START_NODE;
    }
    if (InvoiceExchangeSearchPage.START_NODE.equals(start)) {
      showContentPage(InvoiceExchangeSearchPage.getInstance(), urlParams);
    } else if (InvoiceExchangeCreatePage.START_NODE.equals(start)) {
      showContentPage(InvoiceExchangeCreatePage.getInstance(), urlParams);
    } else if (InvoiceExchangeViewPage.START_NODE.equals(start)) {
      showContentPage(InvoiceExchangeViewPage.getInstance(), urlParams);
    } else if (InvoiceExchangeEditPage.START_NODE.equals(start)) {
      showContentPage(InvoiceExchangeEditPage.getInstance(), urlParams);
    } else if (InvoiceExchangeLogPage.START_NODE.equals(start)) {
      showContentPage(InvoiceExchangeLogPage.getInstance(), urlParams);
    }
  }

  /** 当前用户 */
  public BUCN getCurrentUserUCN() {
    String userStr = getModuleContext().get(InvoiceExchangeService.KEY_CURRENT_USER);
    if (userStr == null) {
      return new BUCN();
    } else {
      List<HasUCN> users = BUCNUtils.fromJson(userStr);
      if (users.size() == 1) {
        return new BUCN(users.get(0));
      }
      return new BUCN();
    }
  }

  private List<BBillType> billTypes = new ArrayList<BBillType>();

  public String getBillTypeCaptionByName(String name) {
    if (StringUtil.isNullOrBlank(name)) {
      return name;
    }

    if (billTypes.isEmpty()) {
      GWTUtil.enableSynchronousRPC();
      InvoiceExchangeServiceAgent.getAllTypes(new Callback<List<BBillType>>() {
        @Override
        public void execute(List<BBillType> entity) {
          billTypes.addAll(entity);
        }
      });
    }

    for (BBillType billType : billTypes) {
      if (name.equals(billType.getName())) {
        return billType.getCaption();
      }
    }

    return name;
  }

}
