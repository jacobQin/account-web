/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	InvoiceRegConfigDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-6 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.commons.client.ui.dialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.account.gwt.invoice.commons.client.rpc.InvoiceRegService;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.panel.RMessagePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 发票登记单选项配置
 * 
 * @author chenpeisi
 * 
 */
public class InvoiceRegConfigDialog extends RDialog implements RValidatable {

  public static final ButtonConfig BUTTON_CONFIRM = new ButtonConfig(InvoiceRegMessage.M.ok());

  public InvoiceRegConfigDialog(int direction, Callback callback) {
    super();
    this.callback = callback;
    this.direction = direction;

    drawSelf();

    if (DirectionType.payment.getDirectionValue() == direction)
      setCaptionText(WidgetRes.R.payInvoiceRegConfig());
    else
      setCaptionText(WidgetRes.R.recInvoiceRegConfig());
  }

  private Callback callback;
  private int direction;
  private BInvoiceRegConfig config;

  private RMessagePanel messagePanel;

  private RForm form;
  private RCombinedField diffTotalField;
  private RHTMLField diffTotalHTML;
  private RNumberBox diffTotalLoField;
  private RNumberBox diffTotalHiField;
  private RCombinedField diffTaxField;
  private RHTMLField diffTaxHTML;
  private RNumberBox diffTaxLoField;
  private RNumberBox diffTaxHiField;
  private RCheckBox regTotalWritableField;

  private void drawSelf() {
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    setWidget(root);

    root.add(drawMainPanel());

    setButtonsHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    setButtons(new ButtonConfig[] {
        BUTTON_CONFIRM, BUTTON_CANCEL });

    BUTTON_CONFIRM.setClickToClose(false);
    setWorkingAreaPadding(5);
    setWidth("500px");
    setShowCloseButton(false);

    getButton(BUTTON_CONFIRM).addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        doSave();
      }
    });

    getButton(BUTTON_CANCEL).addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        hide();
      }
    });
  }

  private void doSave() {
    GWTUtil.blurActiveElement();
    if (validate() == false)
      return;
    readConfig();
    RLoadingDialog.show(InvoiceRegMessage.M.actionDoing(InvoiceRegMessage.M.save()));
    InvoiceRegService.Locator.getService().saveConfig(config, direction, new RBAsyncCallback2() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        hide();
        String msg = WidgetRes.R.actionFailed();
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(Object result) {
        RLoadingDialog.hide();
        hide();
        String msg = WidgetRes.R.onSucess();
        RMsgBox.show(msg);
        InvoiceRegConfigDialog.this.callback.execute(config);
      }
    });
  }

  /**
   * 回调接口
   */
  public static interface Callback {
    void execute(BInvoiceRegConfig result);
  }

  private void readConfig() {
    config.setTotalDiffHi(diffTotalHiField.getValueAsBigDecimal());
    config.setTotalDiffLo(diffTotalLoField.getValueAsBigDecimal());
    config.setTaxDiffHi(diffTaxHiField.getValueAsBigDecimal());
    config.setTaxDiffLo(diffTaxLoField.getValueAsBigDecimal());
    config.setRegTotalWritable(regTotalWritableField.getValue());
  }

  private Widget drawMainPanel() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);

    messagePanel = new RMessagePanel();
    panel.add(messagePanel);

    diffTotalHTML = new RHTMLField();
    diffTotalHTML.setText(WidgetRes.R.totalHTML());

    diffTotalHiField = new RNumberBox(WidgetRes.R.diffTotalHi());
    diffTotalHiField.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
    diffTotalHiField.setSelectAllOnFocus(true);
    diffTotalHiField.setRequired(true);
    diffTotalHiField.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2, false);
    diffTotalHiField.setValueType(BigDecimal.class);
    diffTotalHiField.setScale(2);
    diffTotalHiField.setFormat(GWTFormat.fmt_money);
    diffTotalHiField.setValidator(new RValidator() {

      @Override
      public Message validate(Widget sender, String value) {
        if (diffTotalHiField.getMessages().isEmpty()) {
          if (diffTotalHiField.getValue() != null
              && diffTotalLoField.getValue() != null
              && diffTotalHiField.getValue().doubleValue() < diffTotalLoField.getValue()
                  .doubleValue())
            return Message.error(InvoiceRegMessage.M.greaterThanOrEqual(
                diffTotalHiField.getCaption(), diffTotalLoField.getCaption()), diffTotalHiField);
        }
        return null;
      }
    });

    diffTotalLoField = new RNumberBox(WidgetRes.R.diffTotalLo());
    diffTotalLoField.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
    diffTotalLoField.setSelectAllOnFocus(true);
    diffTotalLoField.setRequired(true);
    diffTotalLoField.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2, false);
    diffTotalLoField.setValueType(BigDecimal.class);
    diffTotalLoField.setFormat(GWTFormat.fmt_money);
    diffTotalLoField.setScale(2);

    diffTotalField = new RCombinedField() {
      {
        addField(diffTotalLoField, 0.35f);
        addField(diffTotalHTML, 0.3f);
        addField(diffTotalHiField, 0.35f);
      }

      @Override
      public boolean validate() {
        return diffTotalHiField.validate() && diffTotalLoField.validate();
      }

      @Override
      public void clearValidResults() {
        diffTotalHiField.clearValidResults();
        diffTotalLoField.clearValidResults();
      }

      @Override
      public boolean isValid() {
        return diffTotalHiField.isValid() && diffTotalLoField.isValid();
      }

      @Override
      public List<Message> getInvalidMessages() {
        List<Message> messages = new ArrayList<Message>();
        messages.addAll(diffTotalHiField.getInvalidMessages());
        messages.addAll(diffTotalLoField.getInvalidMessages());

        return messages;
      }
    };
    panel.add(diffTotalField);

    diffTaxHTML = new RHTMLField();
    diffTaxHTML.setText(WidgetRes.R.taxHTML());

    diffTaxHiField = new RNumberBox(WidgetRes.R.diffTaxHi());
    diffTaxHiField.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
    diffTaxHiField.setSelectAllOnFocus(true);
    diffTaxHiField.setRequired(true);
    diffTaxHiField.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2, false);
    diffTaxHiField.setValueType(BigDecimal.class);
    diffTaxHiField.setScale(2);
    diffTaxHiField.setFormat(GWTFormat.fmt_money);
    diffTaxHiField.setValidator(new RValidator() {

      @Override
      public Message validate(Widget sender, String value) {
        if (diffTaxHiField.getMessages().isEmpty()) {
          if (diffTaxHiField.getValue() != null && diffTaxLoField.getValue() != null
              && diffTaxHiField.getValue().doubleValue() < diffTaxLoField.getValue().doubleValue())
            return Message.error(
                InvoiceRegMessage.M.greaterThanOrEqual(diffTaxHiField.getCaption(),
                    diffTaxLoField.getCaption()), diffTaxHiField);
        }
        return null;
      }
    });

    diffTaxLoField = new RNumberBox(WidgetRes.R.diffTaxLo());
    diffTaxLoField.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
    diffTaxLoField.setSelectAllOnFocus(true);
    diffTaxLoField.setRequired(true);
    diffTaxLoField.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2, false);
    diffTaxLoField.setValueType(BigDecimal.class);
    diffTaxLoField.setFormat(GWTFormat.fmt_money);
    diffTaxLoField.setScale(2);
    diffTaxLoField.setFormat(GWTFormat.fmt_money);

    diffTaxField = new RCombinedField() {
      {
        addField(diffTaxLoField, 0.35f);
        addField(diffTaxHTML, 0.3f);
        addField(diffTaxHiField, 0.35f);
      }

      @Override
      public boolean validate() {
        return diffTaxHiField.validate() && diffTaxLoField.validate();
      }

      @Override
      public void clearValidResults() {
        diffTaxHiField.clearValidResults();
        diffTaxLoField.clearValidResults();
      }

      @Override
      public boolean isValid() {
        return diffTaxHiField.isValid() && diffTaxLoField.isValid();
      }

      @Override
      public List<Message> getInvalidMessages() {
        List<Message> messages = new ArrayList<Message>();
        messages.addAll(diffTaxHiField.getInvalidMessages());
        messages.addAll(diffTaxLoField.getInvalidMessages());

        return messages;
      }
    };
    panel.add(diffTaxField);

    form = new RForm(1);
    form.setWidth("100%");

    regTotalWritableField = new RCheckBox(WidgetRes.R.regTotalWriteable());
    form.addField(regTotalWritableField);

    panel.add(form);

    return panel;
  }

  public void refresh(BInvoiceRegConfig config) {
    this.config = config;

    diffTotalHiField.setValue(config.getTotalDiffHi());
    diffTotalLoField.setValue(config.getTotalDiffLo());
    diffTaxHiField.setValue(config.getTaxDiffHi());
    diffTaxLoField.setValue(config.getTaxDiffLo());
    regTotalWritableField.setValue(config.getRegTotalWritable());

    clearValidResults();
    messagePanel.clearMessages();
  }

  @Override
  public void clearValidResults() {
    diffTotalField.clearValidResults();
    diffTaxField.clearValidResults();
    form.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return diffTotalField.isValid() && diffTaxField.isValid() && form.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(diffTotalField.getInvalidMessages());
    messages.addAll(diffTaxField.getInvalidMessages());
    messages.addAll(form.getInvalidMessages());

    return messages;
  }

  @Override
  public boolean validate() {
    messagePanel.clearMessages();
    boolean valid = form.validate();
    valid &= diffTotalField.validate();
    valid &= diffTaxField.validate();
    if (valid == false) {
      messagePanel.putMessages(getInvalidMessages());
    }
    return valid;
  }

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes R = GWT.create(WidgetRes.class);

    @DefaultStringValue("付款发票登记单选项")
    String payInvoiceRegConfig();

    @DefaultStringValue("收款发票登记单选项")
    String recInvoiceRegConfig();

    @DefaultStringValue("≤发票总额-账款总额≤")
    String totalHTML();

    @DefaultStringValue("≤发票税额-账款税额≤")
    String taxHTML();

    @DefaultStringValue("发票与账款最大差异金额")
    String diffTotal();

    @DefaultStringValue("差异金额上限")
    String diffTotalHi();

    @DefaultStringValue("差异金额下限")
    String diffTotalLo();

    @DefaultStringValue("差异税额上限")
    String diffTaxHi();

    @DefaultStringValue("差异税额下限")
    String diffTaxLo();

    @DefaultStringValue("是否可修改登记金额/税额")
    String regTotalWriteable();

    @DefaultStringValue("保存选项成功")
    String onSucess();

    @DefaultStringValue("保存选项失败")
    String actionFailed();
  }
}
