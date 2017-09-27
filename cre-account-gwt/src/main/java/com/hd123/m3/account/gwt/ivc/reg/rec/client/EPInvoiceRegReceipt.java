/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	EPInvoiceReg.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.util.BBillTypeUtils;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.CounterpartConfigLoader;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceRegOptions;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.InvoiceRegService;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.InvoiceRegServiceAsync;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.InvoiceRegCreatePage;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.InvoiceRegEditPage;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.InvoiceRegLogPage;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.InvoiceRegSearchPage;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.InvoiceRegViewPage;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegPermDef;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.m3.commons.gwt.bpm.client.EPBpmModule2;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmSearchPage2;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.http.Url;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票登记单|入口
 * 
 * @author chenrizhang
 * @since 1.7
 * 
 */
public class EPInvoiceRegReceipt extends EPBpmModule2<BInvoiceReg> {

  public static FieldDef PInvoiceRegDef_counterpart = PInvoiceRegDef.counterpart.clone();

  public static EPInvoiceRegReceipt getInstance() {
    return EPInvoiceRegReceipt.getInstance(EPInvoiceRegReceipt.class);
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    CounterpartConfigLoader.getInstance().initial();
    PInvoiceRegDef_counterpart.setCaption(CounterpartConfigLoader.getInstance().getCaption(
        BCounterpart.COUNTERPART_TYPE_TENANT));
  }

  @Override
  public InvoiceRegServiceAsync getModuleService() {
    return InvoiceRegService.Locator.getService();
  }

  @Override
  public String getPrintTemplate() {
    return InvoiceRegPermDef.RESOURCE_PATH;
  }

  @Override
  public String getModuleCaption() {
    return "收款发票登记单";
  }

  @Override
  public String getPermResource() {
    return InvoiceRegPermDef.RESOURCE_KEY;
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    if (start == null) {
      start = BpmModuleUrlParams.START_NODE_SEARCH;
    }
    if (BpmModuleUrlParams.START_NODE_SEARCH.equals(start)) {
      showContentPage(InvoiceRegSearchPage.getInstance(), urlParams);
    } else if (BpmModuleUrlParams.START_NODE_CREATE.equals(start)) {
      showContentPage(InvoiceRegCreatePage.getInstance(), urlParams);
    } else if (BpmModuleUrlParams.START_NODE_EDIT.equals(start)) {
      showContentPage(InvoiceRegEditPage.getInstance(), urlParams);
    } else if (BpmModuleUrlParams.START_NODE_VIEW.equals(start)) {
      showContentPage(InvoiceRegViewPage.getInstance(), urlParams);
    } else if (BpmModuleUrlParams.START_NODE_LOG.equals(start)) {
      showContentPage(InvoiceRegLogPage.getInstance(), urlParams);
    }
  }

  /** 搜索页面 */
  public void jumpToSearchPage(Message msg) {
    if (isBpmMode()) {
      // 模块间跳转
      Url url = getCurrentNoBpmUrl(BpmModuleUrlParams.START_NODE_SEARCH);
      RWindow.navigate(url.getUrl());
    } else {
      JumpParameters params = new JumpParameters();
      params.setStart(BpmModuleUrlParams.START_NODE_SEARCH);
      params.getUrlRef().set(InvoiceRegSearchPage.PN_STORE_ID,
          InvoiceRegSearchPage.getInstance().getCurrentStoreUuid());
      if (msg != null) {
        params.getMessages().add(msg);
      }
      jump(params);
    }
  }

  public void jumpToSearchPage(String keyword, Message msg) {
    if (StringUtil.isNullOrBlank(keyword)) {
      jumpToSearchPage(msg);
    } else if (isBpmMode()) {
      // 模块间跳转
      Url url = getCurrentNoBpmUrl(BpmModuleUrlParams.START_NODE_SEARCH);
      url.getUnion().set(BaseBpmSearchPage2.FIELD_BILLNUMBER, keyword);
      RWindow.navigate(url.getUrl());
    } else {
      JumpParameters params = new JumpParameters();
      params.setStart(BpmModuleUrlParams.START_NODE_SEARCH);
      params.getUrlRef().set(BaseBpmSearchPage2.FIELD_BILLNUMBER, keyword);
      params.getUrlRef().set(InvoiceRegSearchPage.PN_STORE_ID,
          InvoiceRegSearchPage.getInstance().getCurrentStoreUuid());
      if (msg != null) {
        params.getMessages().add(msg);
      }
      jump(params);
    }
  }

  public Map<String, String> getInvoiceTypes() {
    String invoiceType = getModuleContext().get(InvoiceRegService.KEY_INVOICE_TYPE);
    if (invoiceType == null) {
      return new HashMap<String, String>();
    } else {
      return CollectionUtil.toMap(invoiceType);
    }
  }

  public String getDefaultInvoiceType() {
    String defaultInvoiceType = getModuleContext().get(InvoiceRegService.KEY_DEFALUT_INVOICE_TYPE);
    return defaultInvoiceType;
  }

  private Map<String, BBillType> billTypes;

  public Map<String, BBillType> getBillTypes() {
    if (billTypes == null) {
      billTypes = new HashMap<String, BBillType>();
      List<BBillType> list = BBillTypeUtils.fromJson(getModuleContext().get(
          InvoiceRegService.KEY_BILL_TYPES));
      for (BBillType type : list) {
        billTypes.put(type.getName(), type);
      }
    }
    return billTypes;
  }

  private BInvoiceRegOptions options;

  public BInvoiceRegOptions getOptions() {
    if (options == null) {
      options = new BInvoiceRegOptions();
      options.setAllowSplitReg(StringUtil.toBoolean(
          getModuleContext().get(InvoiceRegService.OPTION_ALLOW_SPLIT_REG), false));

      options.setUseInvoiceStock(StringUtil.toBoolean(
          getModuleContext().get(InvoiceRegService.OPTION_USEINVOICESTOCK), false));
    }
    return options;
  }

  /** 是否启用外部发票系统 */
  public Boolean getEnabledExtInvoiceSystem() {
    return Boolean.valueOf(getModuleContext().get(InvoiceRegService.ENABLED_EXTINVOICESYSTEM));
  }

  /** 获取选项配置小数精度 */
  public int getScale() {
    return Integer.valueOf(getModuleContext().get(InvoiceRegService.SCALE));
  }

  /** 获取选项配置舍入算法 */
  public RoundingMode getRoundingMode() {
    return RoundingMode.valueOf(getModuleContext().get(InvoiceRegService.ROUNDING_MODE));
  }
}
