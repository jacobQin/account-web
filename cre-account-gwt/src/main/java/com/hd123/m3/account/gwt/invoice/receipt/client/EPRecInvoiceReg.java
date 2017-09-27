/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	EPRecInvoiceReg.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.receipt.client;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.m3.account.gwt.base.client.AccEPBpmModule;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.commons.client.util.BBillTypeUtils;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.receipt.client.rpc.RecInvoiceRegService;
import com.hd123.m3.account.gwt.invoice.receipt.client.rpc.RecInvoiceRegServiceAsync;
import com.hd123.m3.account.gwt.invoice.receipt.client.ui.RecInvoiceRegCreatePage;
import com.hd123.m3.account.gwt.invoice.receipt.client.ui.RecInvoiceRegEditPage;
import com.hd123.m3.account.gwt.invoice.receipt.client.ui.RecInvoiceRegLogViewPage;
import com.hd123.m3.account.gwt.invoice.receipt.client.ui.RecInvoiceRegSearchPage;
import com.hd123.m3.account.gwt.invoice.receipt.client.ui.RecInvoiceRegViewPage;
import com.hd123.m3.account.gwt.invoice.receipt.intf.client.RecInvoiceRegUrlParams;
import com.hd123.m3.account.gwt.invoice.receipt.intf.client.perm.RecInvoiceRegPermDef;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author chenpeisi
 * 
 */
public class EPRecInvoiceReg extends AccEPBpmModule {
  public static EPRecInvoiceReg getInstance() {
    return EPRecInvoiceReg.getInstance(EPRecInvoiceReg.class);
  }

  @Override
  protected void doDispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = RecInvoiceRegSearchPage.START_NODE;

      if (RecInvoiceRegSearchPage.START_NODE.equals(start)) {
        showContentPage(RecInvoiceRegSearchPage.getInstance(), urlParams);
      } else if (RecInvoiceRegCreatePage.START_NODE.equals(start)) {
        showContentPage(RecInvoiceRegCreatePage.getInstance(), urlParams);
      } else if (RecInvoiceRegEditPage.START_NODE.equals(start)) {
        showContentPage(RecInvoiceRegEditPage.getInstance(), urlParams);
      } else if (RecInvoiceRegViewPage.START_NODE.equals(start)) {
        showContentPage(RecInvoiceRegViewPage.getInstance(), urlParams);
      } else if (RecInvoiceRegLogViewPage.START_NODE.equals(start)) {
        showContentPage(RecInvoiceRegLogViewPage.getInstance(), urlParams);
      }

    } catch (Exception e) {
      String msg = InvoiceRegMessage.M.errorWhenShowPage();
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  public String getModuleCaption() {
    return RecInvoiceRegUrlParams.MODULE_CAPTION;
  }

  @Override
  public RecInvoiceRegServiceAsync getModuleService() {
    return RecInvoiceRegService.Locator.getService();
  }

  public void showSearchBox(boolean show) {
    if (show == false && searchBox != null) {
      getTitleBar().remove(searchBox);
    } else if (show && searchBox != null) {
      getTitleBar().add(searchBox, RToolbar.ALIGN_RIGHT);
    }
  }

  /**
   * 返回打印模板路径
   * 
   * @return
   */
  public String getPrintTemplate() {
    return RecInvoiceRegPermDef.PRINT_PATH;
  }

  private RSearchBox searchBox;

  protected void onCreate() {
    super.onCreate();

    searchBox = new RSearchBox();
    searchBox.addSearchHandler(new SearchBoxHandler());
    appendSearchBox();
  }

  public void appendSearchBox() {
    getTitleBar().add(new RToolbarSeparator(), RToolbar.ALIGN_RIGHT);

    getTitleBar().add(searchBox, RToolbar.ALIGN_RIGHT);
  }

  private class SearchBoxHandler implements RSearchHandler {
    @Override
    public void onSearch(RSearchEvent event) {
      if (event.isBack()) {
        JumpParameters params = RecInvoiceRegSearchPage.getInstance().getLastParams();
        jump(params);
        return;
      }
      if (event.isEmpty()) {
        JumpParameters params = new JumpParameters(RecInvoiceRegSearchPage.START_NODE);
        jump(params);
        return;
      }
      doSearch(event.getKeyword());
    }
  }

  private void doSearch(final String keyword) {
    RLoadingDialog.show(InvoiceRegMessage.M.actionDoing(InvoiceRegMessage.M.search()));
    RecInvoiceRegService.Locator.getService().loadByNumber(keyword,
        new RBAsyncCallback2<BInvoiceReg>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            params.setStart(RecInvoiceRegSearchPage.START_NODE);
            params.getUrlRef().set(RecInvoiceRegSearchPage.PN_KEYWORD, keyword);
            jump(params);
          }

          public void onSuccess(BInvoiceReg result) {
            RLoadingDialog.hide();
            JumpParameters params = new JumpParameters();
            if (result == null) {
              params.setStart(RecInvoiceRegSearchPage.START_NODE);
              params.getUrlRef().set(RecInvoiceRegSearchPage.PN_KEYWORD, keyword);
            } else {
              params.setStart(RecInvoiceRegViewPage.START_NODE);
              params.getUrlRef().set(RecInvoiceRegViewPage.PN_UUID, result.getUuid());
            }
            jump(params);
          }
        });
  }

  private BInvoiceRegConfig config;
  private BDefaultOption defaultValue;

  /** 收款发票登记单选项配置 */
  public BInvoiceRegConfig getConfig() {
    GWTUtil.enableSynchronousRPC();
    if (config == null) {
      RecInvoiceRegService.Locator.getService().getConfig(
          new RBAsyncCallback2<BInvoiceRegConfig>() {

            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.get(),
                  InvoiceRegMessage.M.config());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(BInvoiceRegConfig result) {
              config = result;
            }
          });
    }
    return config;
  }

  public void setConfig(BInvoiceRegConfig config) {
    this.config = config;
  }

  /** 默认值 */
  public BDefaultOption getDefaultValue() {
    GWTUtil.enableSynchronousRPC();
    if (defaultValue == null) {
      RecInvoiceRegService.Locator.getService().getDefaultValue(
          new RBAsyncCallback2<BDefaultOption>() {

            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.get(),
                  InvoiceRegMessage.M.defaultValue());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(BDefaultOption result) {
              defaultValue = result;
            }
          });
    }
    return defaultValue;
  }

  public Map<String, String> getInvoiceTypeMap() {
    String invoiceType = getModuleContext().get(RecInvoiceRegService.KEY_INVOICE_TYPE);
    if (invoiceType == null) {
      return new HashMap<String, String>();
    } else {
      return CollectionUtil.toMap(invoiceType);
    }
  }

  private Map<String, BBillType> billTypes;

  public Map<String, BBillType> getBillTypeMap() {
    if (billTypes == null) {
      billTypes = new HashMap<String, BBillType>();
      List<BBillType> list = BBillTypeUtils.fromJson(getModuleContext().get(
          RecInvoiceRegService.KEY_BILL_TYPES));
      for (BBillType type : list) {
        billTypes.put(type.getName(), type);
      }
    }
    return billTypes;
  }

  @Override
  public String getPermResource() {
    return RecInvoiceRegPermDef.RESOURCE_RECINVOICEREG;
  }

  /** 获取选项配置小数精度 */
  public int getScale() {
    String a = getModuleContext().get(RecInvoiceRegService.SCALE);

    return Integer.valueOf(a);
  }

  /** 获取选项配置舍入算法 */
  public RoundingMode getRoundingMode() {
    return RoundingMode.valueOf(getModuleContext().get(RecInvoiceRegService.ROUNDING_MODE));
  }
}
