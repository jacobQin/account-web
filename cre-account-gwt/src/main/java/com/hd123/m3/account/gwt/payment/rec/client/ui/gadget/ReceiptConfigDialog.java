/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	ReceiptConfigDialog.java
 * 模块说明：	
 * 修改历史：
 * 2015-9-6 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.payment.rec.client.biz.BReceiptConfig;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptService;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;

/**
 * 收款单选项配置
 * 
 * @author liuguilin
 * 
 */
public class ReceiptConfigDialog extends RDialog {

  public static final ButtonConfig BUTTON_CONFIG = new ButtonConfig(M.M.confirm());

  private BReceiptConfig config;
  private RCheckBox showTaxBox;

  public ReceiptConfigDialog() {
    setCaptionText(M.M.config());
    setWorkingAreaPadding(5);
    setButtonsHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    setWidth("450px");
    setHeight("150px");
    setWidget(drawSelf());

    setButtons(new ButtonConfig[] {
        BUTTON_CONFIG, BUTTON_CANCEL });
    BUTTON_CONFIG.setClickToClose(false);
    getButton(BUTTON_CONFIG).addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        doSave();
      }
    });
  }

  private Widget drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setSpacing(8);
    panel.setWidth("100%");

    RForm form = new RForm(1);
    form.setLabelWidth(0.4f);
    form.setWidth("100%");
    
    showTaxBox = new RCheckBox(M.M.showTax());
    showTaxBox.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        config.setShowTax(showTaxBox.getValue());
      }
    });
    form.addField(showTaxBox);
    
    panel.add(form);

    return panel;
  }

  @Override
  public void center() {
    super.center();
    refresh();
  }

  private void refresh() {
    ReceiptService.Locator.getService().loadConfig(new AsyncCallback<BReceiptConfig>() {
      @Override
      public void onFailure(Throwable caught) {
        RMsgBox.showError(M.M.loadFailed(), caught);
      }

      @Override
      public void onSuccess(BReceiptConfig result) {
        if (result == null) {
          result = new BReceiptConfig();
        }
        config = result;
        showTaxBox.setValue(config.isShowTax());
      }
    });
  }

  private void doSave() {
    RLoadingDialog.show(M.M.saving());
    ReceiptService.Locator.getService().saveConfig(config, new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable caught) {
        RLoadingDialog.hide();
        RMsgBox.showError(M.M.saveFailed(), caught);
      }

      @Override
      public void onSuccess(Void result) {
        RLoadingDialog.hide();
        hide();
      }
    });
  }

  public interface M extends ConstantsWithLookup {
    public static M M = GWT.create(M.class);

    @DefaultStringValue("确定")
    String confirm();

    @DefaultStringValue("收款配置")
    String config();

    @DefaultStringValue("是否显示税额")
    String showTax();
    
    @DefaultStringValue("启用发票库存")
    String useInvoiceStock();

    @DefaultStringValue("正在保存收款配置...")
    String saving();

    @DefaultStringValue("加载收款配置过程中发生错误。")
    String loadFailed();

    @DefaultStringValue("保存收款配置过程中发生错误。")
    String saveFailed();
  }
}
