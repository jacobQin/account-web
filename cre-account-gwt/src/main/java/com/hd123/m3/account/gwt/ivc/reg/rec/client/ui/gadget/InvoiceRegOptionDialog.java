/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-investment-web
 * 文件名：	OptionDialog.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月30日 - lizongyi - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceRegOptions;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.rpc.InvoiceRegService;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstFieldUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RContainerValidator;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.panel.RMessagePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;

/**
 * @author lizongyi
 * 
 */
public class InvoiceRegOptionDialog extends RDialog implements RValidatable {

  private static final ButtonConfig MY_BUTTON_OK = new ButtonConfig("确定", false);
  private static final ButtonConfig[] BUTTONS = new ButtonConfig[] {
      MY_BUTTON_OK, BUTTON_CANCEL };

  public InvoiceRegOptionDialog() {
    setCaptionText(M.defualtCaption());
    setWidth("400px");
    setWorkingAreaPadding(2);

    setWidget(drawSelf());
    setButtons(BUTTONS);
    getButton(MY_BUTTON_OK).addClickHandler(new Handler_okAction());
    setButtonsHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
  }

  private RVerticalPanel root;
  private RMessagePanel messagePanel;

  private RCheckBox allowSplitRegField;
  private RCheckBox useInvoiceStockField;

  private Widget drawSelf() {
    root = new RVerticalPanel();
    root.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    root.setWidth("100%");
    root.setSpacing(0);

    messagePanel = new RMessagePanel();
    root.add(messagePanel);

    drawContent();

    return root;
  }

  public void drawContent() {
    RForm form = new RForm(1);
    form.setWidth("98%");
    form.setLabelWidth((float) 0.3);
    form.setCellVerticalPadding(5);

    allowSplitRegField = new RCheckBox();
    allowSplitRegField.setText(M.allowSplitReg());
    form.addField(allowSplitRegField);

    useInvoiceStockField = new RCheckBox();
    useInvoiceStockField.setText(M.useInvoiceStock());
    if (EPInvoiceRegReceipt.getInstance().getEnabledExtInvoiceSystem() == false) {
      form.addField(useInvoiceStockField);
    }

    root.add(form);
  }

  public void showDialog() {
    BInvoiceRegOptions option = EPInvoiceRegReceipt.getInstance().getOptions();
    allowSplitRegField.setValue(option.isAllowSplitReg());
    useInvoiceStockField.setValue(option.isUseInvoiceStock());
    clearValidResults();
    FocusOnFirstFieldUtil.focusOnFirstField(root);
    super.center();
  }

  @Override
  public void clearValidResults() {
    RContainerValidator.clearValidResults(root);
  }

  @Override
  public boolean isValid() {
    return RContainerValidator.isValid(root);
  }

  @Override
  public List<Message> getInvalidMessages() {
    return RContainerValidator.getInvalidMessages(root);
  }

  @Override
  public boolean validate() {
    messagePanel.clearMessages();
    boolean isValid = RContainerValidator.validate(root);
    if (isValid == false) {
      messagePanel.putMessages(getInvalidMessages());
    }
    return isValid;
  }

  /******************* private class *****************************/
  private class Handler_okAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      GWTUtil.blurActiveElement();
      if (validate() == false) {
        return;
      }
      doSave();
    }
  }

  private void doSave() {
    BInvoiceRegOptions options = new BInvoiceRegOptions();
    options.setAllowSplitReg(allowSplitRegField.getValue());
    options.setUseInvoiceStock(useInvoiceStockField.getValue());

    RLoadingDialog.show("正在保存配置");
    InvoiceRegService.Locator.getService().saveOptions(options, new AsyncCallback<Void>() {

      @Override
      public void onSuccess(Void result) {
        RLoadingDialog.hide();
        hide();
        EPInvoiceRegReceipt.getInstance().getOptions()
            .setAllowSplitReg(allowSplitRegField.getValue());
        EPInvoiceRegReceipt.getInstance().getOptions()
            .setUseInvoiceStock(useInvoiceStockField.getValue());
      }

      @Override
      public void onFailure(Throwable caught) {
        RLoadingDialog.hide();
        String msg = CommonsMessages.M.actionFailed(CommonsMessages.M.save(), M.defualtCaption());
        RMsgBox.showError(msg, caught);
      }
    });
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("发票登记单配置")
    String defualtCaption();

    @DefaultMessage("允许账款多次开票")
    String allowSplitReg();

    @DefaultMessage("启用发票库存")
    String useInvoiceStock();
  }

}
