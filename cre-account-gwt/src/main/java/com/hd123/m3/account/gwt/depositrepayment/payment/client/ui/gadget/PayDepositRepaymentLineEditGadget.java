/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositRepaymentLineEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-20 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.client.ui.gadget;

import java.math.BigDecimal;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.actionname.DepositRepaymentActionName;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLine;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.ui.dialog.AdvanceSubjectBrowserDialog;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.ui.form.AdvanceSubjectUCNBox;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentLineDef;
import com.hd123.m3.account.gwt.depositrepayment.payment.client.rpc.PayDepositRepaymentService;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.RPCCommand;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBoxBase;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
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
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author zhuhairui
 * 
 */
public class PayDepositRepaymentLineEditGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers, HasFocusables {

  public PayDepositRepaymentLineEditGadget() {
    drawSelf();
  }

  @Override
  public Focusable getFocusable(String field) {
    if (BDepositRepaymentLine.FN_SUBJECT.equals(field)) {
      return subjectUCNBox;
    } else if (BDepositRepaymentLine.FN_AMOUNT.equals(field)) {
      return amountField;
    }
    return null;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  public void setEntity(BDepositRepayment entity) {
    this.entity = entity;
  }

  public void clearQueryConditions() {
    if (subjectUCNBox != null)
      ((AdvanceSubjectBrowserDialog) subjectUCNBox.getBrowser()).clearConditions();
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == DepositRepaymentActionName.CHANGE_ACCOUNTUNIT) {
      String accountUnitUuid = (String) event.getParameters().get(0);
      subjectUCNBox.setAccountUnitUuid(accountUnitUuid);
    } else if (event.getActionName() == DepositRepaymentActionName.CHANGE_COUNTERPART) {
      String counterpartUuid = (String) event.getParameters().get(0);
      subjectUCNBox.setCounterpartUuid(counterpartUuid);
    } else if (event.getActionName() == DepositRepaymentActionName.CHANGE_CONTRACT) {
      String contractUuid = (String) event.getParameters().get(0);
      subjectUCNBox.setContractUuid(contractUuid);
    } else if (event.getActionName() == DepositRepaymentActionName.CHANGE_LINE) {
      int row = ((Integer) event.getParameters().get(0)).intValue();
      setCaption(DepositRepaymentMessage.M.resultLine(row + 1));
      line = entity.getLines().get(row);
      Boolean resetFocus = (Boolean) event.getParameters().get(1);
      changeLine(resetFocus.booleanValue());
    }
  }

  public void clearValidResults() {
    repaymentInfoForm.clearValidResults();
  }

  public void markActiveElement() {
    element = GWTUtil.getActiveElement();
    GWTUtil.blurActiveElement();
  }

  private BDepositRepayment entity;
  private BDepositRepaymentLine line;

  private RAction prevAction;
  private RAction nextAction;
  private RAction deleteAction;

  private RForm repaymentInfoForm;
  private AdvanceSubjectUCNBox subjectUCNBox;
  private RNumberBox amountField;
  private RTextBox remarkField;

  private RViewNumberField remainTotalField;
  private Element element;

  private Handler_textField handler = new Handler_textField();

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    Widget w = drawDepositRepaymentPanel();
    mvp.add(0, w);

    w = drawReferencePanel();
    mvp.add(1, w);

    setStyleName(RCaptionBox.STYLENAME_CONCISE);
    setCaption(DepositRepaymentMessage.M.resultLine(1));
    setWidth("100%");
    setContent(vp);

    getCaptionBar().addButton(new RToolbarSeparator());

    prevAction = new RAction(RActionFacade.PREVIOUS_LINE, new Handler_prevAction());
    prevAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_UP));
    RToolbarButton prevButton = new RToolbarButton(prevAction);
    prevButton.setShowText(false);
    getCaptionBar().addButton(prevButton);

    nextAction = new RAction(RActionFacade.NEXT_LINE, new Handler_nextAction());
    nextAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_DOWN));
    RToolbarButton nextButton = new RToolbarButton(nextAction);
    nextButton.setShowText(false);
    getCaptionBar().addButton(nextButton);

    getCaptionBar().addButton(new RToolbarSeparator());

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    deleteAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_DELETE));
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);
  }

  private Widget drawDepositRepaymentPanel() {
    repaymentInfoForm = new RForm(1);
    repaymentInfoForm.setWidth("100%");

    subjectUCNBox = new AdvanceSubjectUCNBox(PDepositRepaymentLineDef.constants.subject());
    subjectUCNBox.setDirection(DirectionType.payment.getDirectionValue());
    subjectUCNBox.setRequired(true);
    subjectUCNBox.addChangeHandler(handler);
    repaymentInfoForm.addField(subjectUCNBox);

    amountField = new RNumberBox(PDepositRepaymentLineDef.amount);
    amountField.setWidth("50%");
    amountField.setSelectAllOnFocus(true);
    amountField.setScale(2);
    amountField.setTextAlignment(RTextBoxBase.ALIGN_RIGHT);
    amountField.setFormat(GWTFormat.fmt_money);
    amountField.addChangeHandler(handler);
    repaymentInfoForm.addField(amountField);

    remarkField = new RTextBox(PDepositRepaymentLineDef.remark);
    remarkField.setEnterToTab(false);
    remarkField.addChangeHandler(handler);
    remarkField.addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == RKey.KEY_CODE_ENTER) {
          GWTUtil.blurActiveElement();
          if (entity.getLines().get(entity.getLines().size() - 1) == line)
            RActionEvent.fire(PayDepositRepaymentLineEditGadget.this,
                DepositRepaymentActionName.ADD_LINE);
          else {
            subjectUCNBox.setFocus(true);
            element = GWTUtil.getActiveElement();
            RActionEvent.fire(PayDepositRepaymentLineEditGadget.this,
                DepositRepaymentActionName.NEXT_LINE);
          }
        }
      }
    });
    repaymentInfoForm.addField(remarkField);

    repaymentInfoForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(DepositRepaymentMessage.M.repaymentInfo());
    box.setWidth("100%");
    box.setContent(repaymentInfoForm);
    return box;
  }

  private Widget drawReferencePanel() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    remainTotalField = new RViewNumberField();
    remainTotalField.setWidth("38%");
    remainTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    remainTotalField.setCaption(DepositRepaymentMessage.M.accountBalance());
    remainTotalField.setFormat(GWTFormat.fmt_money);
    form.addField(remainTotalField);

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(DepositRepaymentMessage.M.referenceInfo());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private void changeLine(boolean isNew) {
    int size = entity.getLines().size();
    boolean prevDisable = entity.getLines().get(0) == line;
    prevAction.setEnabled(!prevDisable);
    boolean nextDisable = entity.getLines().get(size - 1) == line;
    nextAction.setEnabled(!nextDisable);

    assert line != null;
    if (line.getRemainAmount() == null || BigDecimal.ZERO.compareTo(line.getRemainAmount()) == 0) {
      fetchLineExt();
    } else {
      refresh(line.getSubject() == null && line.getAmount() != null);
    }
    if (isNew)
      subjectUCNBox.setFocus(true);
    else if (element != null)
      GWTUtil.focus(element);
  }

  private void fetchLineExt() {
    if (line.getSubject() != null && entity.getCounterpart() != null
        && line.getSubject().getUuid() != null && entity.getCounterpart().getUuid() != null
        && entity.getAccountUnit() != null && entity.getAccountUnit().getUuid() != null) {
      CommandQueue.offer(new RPCCommand<BigDecimal>() {
        @Override
        public void onCall(CommandQueue queue, AsyncCallback<BigDecimal> callback) {
          RLoadingDialog.show();

          String contractUuid = null;
          if (entity.getContract() != null)
            contractUuid = entity.getContract().getUuid();

          PayDepositRepaymentService.Locator.getService().getAdvance(
              entity.getAccountUnit().getUuid(), entity.getCounterpart().getUuid(),
              line.getSubject().getUuid(), contractUuid, callback);
        }

        @Override
        public void onFailure(CommandQueue queue, Throwable t) {
          RLoadingDialog.hide();
          String msg = "查找过程中发生错误。";
          RMsgBox.showError(msg, t);
          queue.abort();
        }

        @Override
        public void onSuccess(CommandQueue queue, BigDecimal result) {
          RLoadingDialog.hide();
          line.setRemainAmount(result);
          refresh(line.getSubject() == null && line.getAmount() != null);

          RActionEvent.fire(PayDepositRepaymentLineEditGadget.this,
              DepositRepaymentActionName.CHANGE_VALUE, new Integer(line.getLineNumber() - 1));
          queue.goon();
        }
      });
      CommandQueue.awake();
    } else {
      refresh(line.getSubject() == null && line.getAmount() != null);
    }
  }

  private void refresh(boolean validate) {
    assert line != null;

    subjectUCNBox.setValue(line.getSubject());
    if (line.getAmount() == null)
      line.setAmount(BigDecimal.ZERO);
    amountField.setValue(line.getAmount());
    remarkField.setValue(line.getRemark());
    remainTotalField.setValue(line.getRemainAmount() == null ? null : line.getRemainAmount());

    if (validate)
      clearValidResults();
  }

  private class Handler_prevAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      markActiveElement();
      RActionEvent.fire(PayDepositRepaymentLineEditGadget.this,
          DepositRepaymentActionName.PREV_LINE);
    }
  }

  private class Handler_nextAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      markActiveElement();
      RActionEvent.fire(PayDepositRepaymentLineEditGadget.this,
          DepositRepaymentActionName.NEXT_LINE);
    }
  }

  private class Handler_deleteAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(PayDepositRepaymentLineEditGadget.this,
          DepositRepaymentActionName.DELETE_CURRENT_LINE);
    }
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == subjectUCNBox) {
        line.setSubject(subjectUCNBox.getValue());
        if (subjectUCNBox.validate())
          fetchLineExt();
      } else if (event.getSource() == amountField) {
        line.setAmount(amountField.getValueAsBigDecimal());
        if (line.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
          amountField.addErrorMessage(DepositRepaymentMessage.M
              .min(PDepositRepaymentLineDef.constants.amount()));
        } else if (line.getAmount().compareTo(line.getRemainAmount()) > 0) {
          amountField.addErrorMessage(DepositRepaymentMessage.M.max(
              PDepositRepaymentLineDef.constants.amount(),
              DepositRepaymentMessage.M.accountBalance()));
        }
        RActionEvent.fire(PayDepositRepaymentLineEditGadget.this,
            DepositRepaymentActionName.CHANGE_REPAYMENTTOTAL);
      } else if (event.getSource() == remarkField) {
        line.setRemark(remarkField.getValue());
      }
      markActiveElement();
      RActionEvent.fire(PayDepositRepaymentLineEditGadget.this,
          DepositRepaymentActionName.CHANGE_VALUE, new Integer(line.getLineNumber() - 1));
    }
  }

}
