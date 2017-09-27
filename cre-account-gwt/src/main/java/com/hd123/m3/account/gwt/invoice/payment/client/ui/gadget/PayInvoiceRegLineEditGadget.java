/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	RecInvoiceRegLineGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.payment.client.ui.gadget;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.invoice.commons.client.actionname.InvoiceRegActionName;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegLine;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegLineDef;
import com.hd123.m3.account.gwt.invoice.payment.client.EPPayInvoiceReg;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedFlowField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RHyperlinkField;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * @author chenpeisi
 * 
 */
public class PayInvoiceRegLineEditGadget extends Composite implements RActionHandler,
    HasRActionHandlers, HasFocusables {

  public PayInvoiceRegLineEditGadget() {
    drawSelf();
  }

  private EPPayInvoiceReg ep = EPPayInvoiceReg.getInstance();
  private BInvoiceReg entity;
  private BInvoiceRegLine line;

  private RCaptionBox box;
  private RAction prevAction;
  private RAction nextAction;
  private RAction deleteAction;

  private RCaptionBox leftBox;
  private RForm leftForm;
  private RViewStringField sourceBillTypeField;
  private DispatchLinkField sourceBillNumberField;
  private RHyperlinkField subjectField;
  private RViewStringField taxRateField;
  private RTextBox remarkField;

  private RCaptionBox rightBox;
  private RForm rightForm;
  private RViewNumberField totalUnRegField;
  private RViewNumberField taxUnRegField;
  private RNumberBox totalField;
  private RNumberBox amountField;
  private RNumberBox taxField;

  private Element element;
  private Handler_textField handler = new Handler_textField();

  public void setEntity(BInvoiceReg entity) {
    this.entity = entity;
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RCombinedFlowField editor = new RCombinedFlowField() {
      {
        addField(drawLeftPanel());
        addHorizontalSpacing(10);
        addField(drawRightPanel());
      }
    };
    vp.add(editor);

    box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InvoiceRegMessage.M.lineNumber(0));
    box.setWidth("100%");
    box.setContent(vp);

    box.getCaptionBar().addButton(new RToolbarSeparator());

    prevAction = new RAction(RActionFacade.PREVIOUS_LINE, new Handler_prevAction());
    prevAction.setHotKey(null);
    RToolbarButton prevButton = new RToolbarButton(prevAction);
    prevButton.setShowText(false);
    box.getCaptionBar().addButton(prevButton);

    nextAction = new RAction(RActionFacade.NEXT_LINE, new Handler_nextAction());
    nextAction.setHotKey(null);
    RToolbarButton nextButton = new RToolbarButton(nextAction);
    nextButton.setShowText(false);
    box.getCaptionBar().addButton(nextButton);

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    deleteAction.setHotKey(null);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    box.getCaptionBar().addButton(deleteButton);

    initWidget(box);
  }

  private Widget drawLeftPanel() {
    leftForm = new RForm(1);
    leftForm.setWidth("100%");

    sourceBillTypeField = new RViewStringField(
        PInvoiceRegLineDef.constants.acc1_sourceBill_billType());
    leftForm.addField(sourceBillTypeField);

    sourceBillNumberField = new DispatchLinkField(
        PInvoiceRegLineDef.constants.acc1_sourceBill_billNumber());
    sourceBillNumberField.setLinkKey(GRes.R.dispatch_key());
    leftForm.addField(sourceBillNumberField);

    subjectField = new RHyperlinkField(PInvoiceRegLineDef.constants.acc1_subject());
    subjectField.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        if (line == null || line.getAcc1() == null || line.getAcc1().getSubject() == null
            || line.getAcc1().getSubject().getUuid() == null)
          return;
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID,
            line.getAcc1().getSubject().getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
        } catch (Exception e) {
          String msg = InvoiceRegMessage.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
          RMsgBox.showError(msg, e);
        }
      }
    });
    leftForm.addField(subjectField);

    taxRateField = new RViewStringField(PInvoiceRegLineDef.constants.acc1_taxRate());
    leftForm.addField(taxRateField);

    remarkField = new RTextBox(PInvoiceRegLineDef.remark);
    remarkField.addChangeHandler(handler);
    leftForm.addField(remarkField);

    leftForm.rebuild();

    leftBox = new RCaptionBox();
    leftBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    leftBox.setCaption(InvoiceRegMessage.M.bill());
    leftBox.setWidth("49%");
    leftBox.setContent(leftForm);

    return leftBox;
  }

  private Widget drawRightPanel() {
    rightForm = new RForm(1);
    rightForm.setWidth("100%");

    totalUnRegField = new RViewNumberField();
    totalUnRegField.setFormat(GWTFormat.fmt_money);
    totalUnRegField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    taxUnRegField = new RViewNumberField();
    taxUnRegField.setFormat(GWTFormat.fmt_money);
    taxUnRegField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    rightForm.addField(new RCombinedField() {
      {
        setCaption(InvoiceRegMessage.M.unRegTotal());
        addField(totalUnRegField, 0.5f);
        addField(taxUnRegField, 0.5f);
      }
    });

    totalField = new RNumberBox();
    totalField.setTextAlignment(RTextBox.ALIGN_RIGHT);
    totalField.setFormat(GWTFormat.fmt_money);
    totalField.setScale(2);
    totalField.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2, false);
    totalField.setEnabled(EPPayInvoiceReg.getInstance().getConfig().getRegTotalWritable());
    totalField.setSelectAllOnFocus(true);
    totalField.addChangeHandler(handler);
    rightForm.addField(new RCombinedField() {
      {
        setCaption(PInvoiceRegLineDef.constants.total_total());
        setRequired(true);
        addField(totalField, 0.5f);
        addField(new HTML(), 0.5f);
      }
    });

    amountField = new RNumberBox();
    amountField.setTextAlignment(RTextBox.ALIGN_RIGHT);
    amountField.setFormat(GWTFormat.fmt_money);
    amountField.setScale(2);
    amountField.setEnabled(EPPayInvoiceReg.getInstance().getConfig().getRegTotalWritable());
    amountField.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2);
    amountField.setSelectAllOnFocus(true);
    amountField.addChangeHandler(handler);

    taxField = new RNumberBox();
    taxField.setEnterToTab(false);
    taxField.setEnabled(EPPayInvoiceReg.getInstance().getConfig().getRegTotalWritable());
    taxField.setTextAlignment(RTextBox.ALIGN_RIGHT);
    taxField.setFormat(GWTFormat.fmt_money);
    taxField.setScale(2);
    taxField.setSelectAllOnFocus(true);
    taxField.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == RKey.KEY_CODE_ENTER) {
          GWTUtil.blurActiveElement();
          if (entity.getLines().get(entity.getLines().size() - 1) == line) {
            RActionEvent.fire(PayInvoiceRegLineEditGadget.this, InvoiceRegActionName.ADD_INVOICE,
                "");
          } else {
            remarkField.setFocus(true);
            element = GWTUtil.getActiveElement();
            RActionEvent.fire(PayInvoiceRegLineEditGadget.this, InvoiceRegActionName.NEXT_INVOICE);
          }
        }
      }
    });
    taxField.addChangeHandler(handler);
    rightForm.addField(new RCombinedField() {
      {
        setCaption(InvoiceRegMessage.M.amountTax());
        setRequired(true);
        addField(amountField, 0.5f);
        addField(taxField, 0.5f);
      }
    });

    rightForm.rebuild();

    rightBox = new RCaptionBox();
    rightBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    rightBox.setCaption(PInvoiceRegLineDef.constants.total_total());
    rightBox.setWidth("49%");
    rightBox.setContent(rightForm);

    return rightBox;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == InvoiceRegActionName.CHANGE_LINE) {
      int row = ((Integer) event.getParameters().get(0)).intValue();
      Boolean isNew = (Boolean) event.getParameters().get(1);
      box.setCaption(InvoiceRegMessage.M.lineNumber(row + 1));
      line = entity.getLines().get(row);
      changeLine(isNew.booleanValue());
      if (line.getAcc1().getSourceBill() == null
          || line.getAcc1().getSourceBill().getBillNumber() == null)
        clearValidResults();
    }
  }

  @Override
  public Focusable getFocusable(String field) {
    if (BInvoiceRegLine.FN_ACCOUNTTOTAL.equals(field)) {
      return totalField;
    } else if (BInvoiceRegLine.FN_ACCOUNTTAX.equals(field)) {
      return taxField;
    }
    return null;
  }

  private void changeLine(boolean isNew) {
    int size = entity.getLines().size();
    boolean prevDisable = entity.getLines().get(0) == line;
    prevAction.setEnabled(!prevDisable);
    boolean nextDisable = entity.getLines().get(size - 1) == line;
    nextAction.setEnabled(!nextDisable);

    refresh();
    if (isNew)
      remarkField.setFocus(true);
    else if (element != null)
      GWTUtil.focus(element);
    clearValidResults();
  }

  private void refresh() {
    assert line != null;
    if (line.getAcc1().getSourceBill() != null
        && line.getAcc1().getSourceBill().getBillType() != null) {
      BBillType billType = EPPayInvoiceReg.getInstance().getBillTypeMap()
          .get(line.getAcc1().getSourceBill().getBillType());
      if (billType != null)
        sourceBillTypeField.setValue(billType.getCaption());
    } else {
      sourceBillTypeField.setValue(null);
    }
    if (line.getAcc1().getSourceBill() == null) {
      sourceBillNumberField.clearValue();
    } else {
      sourceBillNumberField.setValue(line.getAcc1().getSourceBill().getBillType(), line.getAcc1()
          .getSourceBill().getBillNumber());
    }
    subjectField.setValue(line.getAcc1().getSubject() == null ? null : line.getAcc1().getSubject()
        .toFriendlyStr());
    taxRateField.setValue(line.getAcc1().getTaxRate() == null ? null : line.getAcc1().getTaxRate()
        .caption());
    remarkField.setValue(line.getRemark());

    refreshTotal();
    clearValidResults();

    RActionEvent.fire(PayInvoiceRegLineEditGadget.this, InvoiceRegActionName.CHANGE_LINE_VALUE);
  }

  private void refreshTotal() {
    int scale = ep.getScale();
    RoundingMode roundingMode = ep.getRoundingMode();
    totalUnRegField.setValue(line.getUnregTotal() == null ? null : line.getUnregTotal().getTotal()
        .divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP));
    taxUnRegField.setValue(line.getUnregTotal() == null ? null : line.getUnregTotal().getTax()
        .divide(BigDecimal.ONE, scale, roundingMode));
    totalField.setValue(line.getRegTotal() == null ? null : line.getRegTotal().getTotal()
        .divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP));
    taxField.setValue(line.getRegTotal() == null ? null : line.getRegTotal().getTax()
        .divide(BigDecimal.ONE, scale, roundingMode));
    amountField.setValue(line.getAmount());

    totalField.setEnabled(EPPayInvoiceReg.getInstance().getConfig().getRegTotalWritable());
    taxField.setEnabled(EPPayInvoiceReg.getInstance().getConfig().getRegTotalWritable());
    amountField.setEnabled(EPPayInvoiceReg.getInstance().getConfig().getRegTotalWritable());
  }

  private class Handler_prevAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      element = GWTUtil.getActiveElement();
      GWTUtil.blurActiveElement();
      RActionEvent.fire(PayInvoiceRegLineEditGadget.this, InvoiceRegActionName.PREV_LINE);
    }
  }

  private class Handler_nextAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      element = GWTUtil.getActiveElement();
      GWTUtil.blurActiveElement();
      RActionEvent.fire(PayInvoiceRegLineEditGadget.this, InvoiceRegActionName.NEXT_LINE);
    }
  }

  private class Handler_deleteAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(PayInvoiceRegLineEditGadget.this, InvoiceRegActionName.DELETE_CURRENT_LINE);
    }
  }

  private class Handler_textField implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == remarkField) {
        line.setRemark(remarkField.getValue());
      } else if (event.getSource() == totalField) {
        line.changeTotal(totalField.getValueAsBigDecimal(), ep.getScale(), ep.getRoundingMode());
        refreshTotal();
        validateTotal();
      } else if (event.getSource() == taxField) {
        line.changeTax(taxField.getValueAsBigDecimal());
        refreshTotal();
        validateTotal();
      } else if (event.getSource() == amountField) {
        line.changeAmount(amountField.getValueAsBigDecimal(), ep.getScale(), ep.getRoundingMode());
        refreshTotal();
        validateTotal();
      }
      RActionEvent.fire(PayInvoiceRegLineEditGadget.this, InvoiceRegActionName.CHANGE_LINE_VALUE);
    }

    private void validateTotal() {
      if (line.getRegTotal().getTotal().compareTo(BigDecimal.ZERO) == 0) {
        totalField.addErrorMessage(InvoiceRegMessage.M
            .mustGreaterThanZreo1(PInvoiceRegLineDef.constants.total_total()));
      } else if (line.getRegTotal().getTotal().abs()
          .compareTo(line.getUnregTotal().getTotal().abs()) > 0) {
        totalField.addErrorMessage(InvoiceRegMessage.M.maxValue1(
            PInvoiceRegLineDef.constants.total_total(),
            PInvoiceRegLineDef.constants.unregTotal_total()));
      } else if (line.getRegTotal().getTotal().multiply(line.getUnregTotal().getTotal())
          .compareTo(BigDecimal.ZERO) < 0) {
        totalField.addErrorMessage(InvoiceRegMessage.M.inconsistent1(
            PInvoiceRegLineDef.constants.total_total(),
            PInvoiceRegLineDef.constants.unregTotal_total()));
      } else if (line.getRegTotal().getTax().abs().compareTo(line.getUnregTotal().getTax().abs()) > 0) {
        taxField
            .addErrorMessage(InvoiceRegMessage.M.maxValue1(
                PInvoiceRegLineDef.constants.total_tax(),
                PInvoiceRegLineDef.constants.unregTotal_tax()));
      } else if (line.getRegTotal().getTax().multiply(line.getUnregTotal().getTax())
          .compareTo(BigDecimal.ZERO) < 0) {
        taxField
            .addErrorMessage(InvoiceRegMessage.M.inconsistent1(
                PInvoiceRegLineDef.constants.total_tax(),
                PInvoiceRegLineDef.constants.unregTotal_tax()));
      }
    }
  }

  public void clearValidResults() {
    leftForm.clearValidResults();
    rightForm.clearValidResults();
  }
}
