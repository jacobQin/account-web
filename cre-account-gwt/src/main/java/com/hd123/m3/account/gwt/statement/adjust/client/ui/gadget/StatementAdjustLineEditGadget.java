/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementAdjustLineEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-11 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectStoreTaxRate;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.statement.adjust.client.EPStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLine;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustMessages;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.dd.PStatementAdjustDef;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.dd.PStatementAdjustLineDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxRate;
import com.hd123.m3.commons.gwt.widget.client.ui.TaxRateComboBox;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.LocalCommand;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.hotkey.RHotKey;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * @author zhuhairui
 * 
 */
public class StatementAdjustLineEditGadget extends Composite implements RActionHandler,
    HasRActionHandlers, HasFocusables {

  @Override
  public void onAction(final RActionEvent event) {
    if (event.getActionName() == StatementAdjustLineEditGrid.ActionName.BEFORE_CHANGE_LINE) {
      element = GWTUtil.getActiveElement();
    }

    if (event.getActionName() == StatementAdjustLineEditGrid.ActionName.CHANGE_LINE) {
      CommandQueue.offer(new LocalCommand() {
        @Override
        public void onCall(CommandQueue queue) {
          int row = ((Integer) event.getParameters().get(0)).intValue();
          box.setCaption(StatementAdjustMessages.M.lineNumber(row + 1));
          line = statementAdjust.getLines().get(row);
          Boolean isAdd = event.getParameters().size() > 1 ? (Boolean) event.getParameters().get(1)
              : Boolean.FALSE;
          changeLine(isAdd.booleanValue());
          queue.goon();
        }
      });
      CommandQueue.awake();
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public Focusable getFocusable(String field) {
    if (BStatementAdjustLine.FN_STATEMENTSUBJECT.equals(field))
      return subjectField;
    else if (BStatementAdjustLine.FN_TOTAL.equals(field))
      return totalField;
    else if (BStatementAdjustLine.FN_TAXRATE.equals(field))
      return taxRateField;
    else if (BStatementAdjustLine.FN_REMARK.equals(field))
      return remarkBoxField;
    else if (BStatementAdjustLine.FN_ACCOUNTDATE.equals(field))
      return accountDateField;
    return null;
  }

  public void setStatementAdjust(BStatementAdjust statementAdjust) {
    this.statementAdjust = statementAdjust;
  }

  public boolean isValid() {
    return rightForm.isValid();
  }

  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(rightForm.getInvalidMessages());
    return list;
  }

  public boolean validate() {
    clearValidResults();
    boolean valid = rightForm.validate();
    return valid;
  }

  public void clearValidResults() {
    leftForm.clearValidResults();
    rightForm.clearValidResults();
  }

  public void setFocus() {
    subjectField.setFocus(true);
  }

  public void onHideClearCondition() {
    if (subjectField != null) {
      subjectField.clearConditions();
    }
  }

  public static class ActionName {
    /** 上一行 */
    public static final String PREVLINE = "prevLine";
    /** 下一行 */
    public static final String NEXTLINE = "nextLine";
    /** 变值 */
    public static final String CHANGE_VALUE = "change_value";
    /** 删除当前行 */
    public static final String DELETE_LINE = "delete_line";
    /** 新增行 */
    public static final String ADD_LINE = "add_line";
    /** 改变行 */
    public static final String CHANGE_COUNTERPART = "counterpart_change";
  }

  public StatementAdjustLineEditGadget() {
    super();
    drawSelf();
  }

  private EPStatementAdjust ep = EPStatementAdjust.getInstance();
  private BStatementAdjust statementAdjust;
  private BStatementAdjustLine line;

  private RAction prevAction;
  private RAction nextAction;
  private RAction deleteAction;

  private RCaptionBox box;

  private Handler_textField textFieldHandler = new Handler_textField();
  private RForm leftForm;
  private SubjectUCNBox subjectField;
  private RNumberBox totalField;
  private RViewNumberField taxField;
  private TaxRateComboBox taxRateField;
  private RViewStringField directionField;
  private RCheckBox invoiceField;

  private RForm rightForm;
  private RDateBox accountDateField;
  private RDateBox beginDateField;
  private RDateBox endDateField;
  private RDateBox lastPayDateField;
  private RTextBox remarkBoxField;

  private Element element;

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel();
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    Widget w = drawLeftPanel();
    mvp.add(0, w);

    w = drawRightPanel();
    mvp.add(1, w);

    box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    box.setCaption(StatementAdjustMessages.M.lineNumber(1));
    box.setContent(vp);

    box.getCaptionBar().addButton(new RToolbarSeparator());

    prevAction = new RAction(RActionFacade.PREVIOUS_LINE, new Handler_prevAction());
    prevAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_UP));
    RToolbarButton pervButton = new RToolbarButton(prevAction);
    pervButton.setShowText(false);
    box.getCaptionBar().addButton(pervButton);

    nextAction = new RAction(RActionFacade.NEXT_LINE, new Handler_nextAction());
    nextAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_DOWN));
    RToolbarButton nextButton = new RToolbarButton(nextAction);
    nextButton.setShowText(false);
    box.getCaptionBar().addButton(nextButton);

    box.getCaptionBar().addButton(new RToolbarSeparator());

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    deleteAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_DELETE));
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    box.getCaptionBar().addButton(deleteButton);

    initWidget(box);
  }

  private Widget drawLeftPanel() {
    leftForm = new RForm(1);
    leftForm.setWidth("50%");

    subjectField = new SubjectUCNBox(BSubjectType.credit.toString(), null, Boolean.TRUE, null);
    subjectField.setRequired(true);
    subjectField.setCaption(PStatementAdjustLineDef.constants.subject());
    subjectField.addChangeHandler(new SubjectChangeHandler());
    leftForm.addField(subjectField);

    totalField = new RNumberBox();
    totalField.setSelectAllOnFocus(true);
    totalField.addChangeHandler(textFieldHandler);
    totalField.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
    totalField.setFormat(M3Format.fmt_money);

    taxField = new RViewNumberField();
    taxField.setFormat(M3Format.fmt_money);
    taxField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    RCombinedField cnbField = new RCombinedField() {
      {
        setCaption(PStatementAdjustLineDef.constants.amount_total() + "/"
            + PStatementAdjustLineDef.constants.amount_tax());
        addField(totalField, 0.5f);
        addField(taxField, 0.5f);
        setRequired(true);
      }
    };
    leftForm.addField(cnbField);

    taxRateField = new TaxRateComboBox(PStatementAdjustLineDef.constants.taxRate_rate());
    taxRateField.setMaxDropdownRowCount(10);
    taxRateField.setRequired(true);
    taxRateField.refreshOptions();
    taxRateField.addChangeHandler(textFieldHandler);
    leftForm.addField(taxRateField);

    directionField = new RViewStringField(PStatementAdjustLineDef.constants.direction());
    leftForm.addField(directionField);

    invoiceField = new RCheckBox(PStatementAdjustLineDef.constants.invoice());
    invoiceField.addChangeHandler(textFieldHandler);
    leftForm.addField(invoiceField);

    leftForm.rebuild();

    RCaptionBox captionBox = new RCaptionBox();
    captionBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    captionBox.setCaption(PStatementAdjustLineDef.constants.subject());
    captionBox.setWidth("100%");
    captionBox.setContent(leftForm);
    return captionBox;
  }

  private Widget drawRightPanel() {
    rightForm = new RForm(1);
    rightForm.setWidth("50%");

    accountDateField = new RDateBox();
    accountDateField.setRequired(true);
    accountDateField.setCaption(PStatementAdjustLineDef.constants.accountDate());
    accountDateField.addChangeHandler(textFieldHandler);
    rightForm.addField(accountDateField);

    beginDateField = new RDateBox();
    beginDateField.setCaption(PStatementAdjustLineDef.constants.dateRange_beginDate());
    beginDateField.addValueChangeHandler(new ValueChangeHandler<Date>() {
      @Override
      public void onValueChange(ValueChangeEvent<Date> event) {
        if (event.getValue() != null && line.getEndDate() != null) {
          if (event.getValue().after(line.getEndDate())) {
            beginDateField.addErrorMessage(PStatementAdjustLineDef.constants.dateRange_beginDate()
                + StatementAdjustMessages.M.cannot()
                + StatementAdjustMessages.M.greaterThan(PStatementAdjustLineDef.constants
                    .dateRange_endDate()));
          } else {
            endDateField.clearValidResults();
          }
        }

        line.setBeginDate(event.getValue());
        RActionEvent.fire(StatementAdjustLineEditGadget.this, ActionName.CHANGE_VALUE, new Integer(
            line.getLineNumber() - 1));
      }
    });
    rightForm.addField(beginDateField);

    endDateField = new RDateBox();
    endDateField.setCaption(PStatementAdjustLineDef.constants.dateRange_endDate());
    endDateField.addValueChangeHandler(new ValueChangeHandler<Date>() {
      @Override
      public void onValueChange(ValueChangeEvent<Date> event) {
        if (event.getValue() != null && line.getBeginDate() != null) {
          if (event.getValue().before(line.getBeginDate())) {
            endDateField.addErrorMessage(PStatementAdjustLineDef.constants.dateRange_endDate()
                + StatementAdjustMessages.M.cannot()
                + PStatementAdjustLineDef.constants.dateRange_beginDate());
          } else {
            beginDateField.clearValidResults();
          }
        }

        line.setEndDate(event.getValue());
        RActionEvent.fire(StatementAdjustLineEditGadget.this, ActionName.CHANGE_VALUE, new Integer(
            line.getLineNumber() - 1));
      }
    });
    rightForm.addField(endDateField);

    lastPayDateField = new RDateBox();
    lastPayDateField.setCaption(PStatementAdjustLineDef.constants.lastPayDate());
    lastPayDateField.addChangeHandler(textFieldHandler);
    rightForm.addField(lastPayDateField);

    remarkBoxField = new RTextBox(PStatementAdjustLineDef.remark);
    remarkBoxField.setCaption(PStatementAdjustLineDef.constants.remark());
    remarkBoxField.addChangeHandler(textFieldHandler);
    // 作为明细行最后一个可编辑控件，须响应ENTER添加行事件
    remarkBoxField.setEnterToTab(false);
    remarkBoxField.addKeyDownHandler(new KeyDownHandler() {

      @Override
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == RKey.KEY_CODE_ENTER) {
          GWTUtil.blurActiveElement();
          if (statementAdjust.getLines().get(statementAdjust.getLines().size() - 1) == line)
            RActionEvent.fire(StatementAdjustLineEditGadget.this, ActionName.ADD_LINE, "");
          else {
            subjectField.setFocus(true);
            element = GWTUtil.getActiveElement();
            RActionEvent.fire(StatementAdjustLineEditGadget.this, ActionName.NEXTLINE, "");
          }
        }
      }
    });
    rightForm.addField(remarkBoxField);

    rightForm.rebuild();

    RCaptionBox captionBox = new RCaptionBox();
    captionBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    captionBox.setCaption(StatementAdjustMessages.M.referenceInform());
    captionBox.setWidth("100%");
    captionBox.setContent(rightForm);
    return captionBox;
  }

  private void refresh() {
    subjectField.setValue(line.getSubject());
    if (line.getTotal() != null) {
      totalField.setValue(line.getTotal().getTotal());
      taxField.setValue(line.getTotal().getTax());
    } else {
      totalField.clearValue();
      taxField.clearValue();
    }
    taxRateField.setValue(line.getTaxRate());
    if (line.getDirection() != 0)
      directionField.setValue(DirectionType.getCaptionByValue(line.getDirection()));
    else
      directionField.clearValue();
    invoiceField.setValue(line.isInvoice());
    accountDateField.setValue(line.getAccountDate());
    beginDateField.setValue(line.getBeginDate());
    endDateField.setValue(line.getEndDate());
    lastPayDateField.setValue(line.getLastPayDate());
    remarkBoxField.setValue(line.getRemark());

    /** 用于控制gadget不可编辑 */
    if (line.getSourceAccountId() != null) {
      subjectField.setReadOnly(true);
      taxRateField.setReadOnly(true);
      invoiceField.setReadOnly(true);
      beginDateField.setReadOnly(true);
      endDateField.setReadOnly(true);
      lastPayDateField.setReadOnly(true);
      remarkBoxField.setReadOnly(true);
      accountDateField.setReadOnly(true);
    } else {
      subjectField.setReadOnly(false);
      taxRateField.setReadOnly(false);
      invoiceField.setReadOnly(false);
      beginDateField.setReadOnly(false);
      endDateField.setReadOnly(false);
      lastPayDateField.setReadOnly(false);
      remarkBoxField.setReadOnly(false);
      accountDateField.setReadOnly(false);
    }
  }

  private void changeLine(boolean isAdd) {
    int size = statementAdjust.getLines().size();
    boolean prevDisable = statementAdjust.getLines().get(0) == line;
    prevAction.setEnabled(!prevDisable);
    boolean nextDisable = statementAdjust.getLines().get(size - 1) == line;
    nextAction.setEnabled(!nextDisable);

    assert line != null;
    refresh();

    // 焦点定位到换行前的位置
    if (element != null)
      try {
        GWTUtil.focus(element);
      } catch (Exception e) {
        // do nothing
      }
    if (isAdd)
      subjectField.setFocus(true);
    clearValidResults();
  }

  private class Handler_prevAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      element = GWTUtil.getActiveElement();
      GWTUtil.blurActiveElement();
      RActionEvent.fire(StatementAdjustLineEditGadget.this, ActionName.PREVLINE, "");
    }
  }

  private class Handler_nextAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      element = GWTUtil.getActiveElement();
      GWTUtil.blurActiveElement();
      RActionEvent.fire(StatementAdjustLineEditGadget.this, ActionName.NEXTLINE, "");
    }
  }

  private class Handler_deleteAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(StatementAdjustLineEditGadget.this, ActionName.DELETE_LINE, "");
    }
  }

  private class SubjectChangeHandler implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (statementAdjust.getBcontract() == null
          || StringUtil.isNullOrBlank(statementAdjust.getBcontract().getUuid())) {
        RMsgBox.showError(StatementAdjustMessages.M.pleaseSelect(PStatementAdjustDef.constants
            .contract()));
        subjectField.setValue(line.getSubject());
        subjectField.clearValidResults();
        return;
      }
      if (subjectField.getValue() != null
          && !StringUtil.isNullOrBlank(subjectField.getValue().getUuid()))
        setSubject(subjectField.getValue());
      RActionEvent.fire(StatementAdjustLineEditGadget.this, ActionName.CHANGE_VALUE, new Integer(
          line.getLineNumber() - 1));
    }
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == totalField) {
        changeTotal();
      } else if (event.getSource() == taxRateField) {
        line.setTaxRate(taxRateField.getValue());
        changeTotal();
      } else if (event.getSource() == invoiceField) {
        line.setInvoice(invoiceField.getValue());
      } else if (event.getSource() == remarkBoxField) {
        line.setRemark(remarkBoxField.getValue());
      } else if (event.getSource() == accountDateField) {
        line.setAccountDate(accountDateField.getValue());
      } else if (event.getSource() == lastPayDateField) {
        line.setLastPayDate(lastPayDateField.getValue());
      }

      RActionEvent.fire(StatementAdjustLineEditGadget.this, ActionName.CHANGE_VALUE, new Integer(
          line.getLineNumber() - 1));
    }
  }

  private void changeTotal() {
    BigDecimal total = totalField.getValueAsBigDecimal() == null ? BigDecimal.ZERO : totalField
        .getValueAsBigDecimal();

    line.changeTotal(total, ep.getScale(), ep.getRoundingMode());
    totalField.setValue(line.getTotal().getTotal());
    taxField.setValue(line.getTotal().getTax());
  }

  private void setSubject(final BUCN subject) {
    line.setSubject(subjectField.getValue());
    taxRateField.setValue(getDefaultTax((subjectField.getRawValue())));
    line.setTaxRate(taxRateField.getValue());
    line.setDirection((subjectField.getRawValue()).getDirection());
    changeTotal();
    refresh();
    clearValidResults();
  }
  
  private BTaxRate getDefaultTax(BSubject subject){
    BTaxRate defaultTax = subject.getTaxRate();
    if(statementAdjust.getAccountUnit()==null||subject.getStoreTaxRates()==null||subject.getStoreTaxRates().isEmpty()){
      return defaultTax;
    }
    for(BSubjectStoreTaxRate taxRate:subject.getStoreTaxRates()){
      if(taxRate.getStore().equals(statementAdjust.getAccountUnit())){
        return taxRate.getTaxRate();
      }
    }
    return defaultTax;
  }
}
