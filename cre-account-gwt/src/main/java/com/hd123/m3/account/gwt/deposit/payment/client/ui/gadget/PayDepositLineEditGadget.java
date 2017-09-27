/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayLineEditGadget.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.payment.client.ui.gadget;

import java.math.BigDecimal;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.validation.HasFocusables;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.SubjectBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.deposit.commons.client.actionname.DepositActionName;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLine;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositTotal;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositLineDef;
import com.hd123.m3.account.gwt.deposit.payment.client.rpc.PayDepositService;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.LocalCommand;
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

/**
 * @author chenpeisi
 * 
 */
public class PayDepositLineEditGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers, HasFocusables {

  public PayDepositLineEditGadget() {
    drawSelf();
  }

  private BDeposit entity;
  private BDepositLine line;

  private RAction prevAction;
  private RAction nextAction;
  private RAction deleteAction;

  private RForm payInfoForm;
  private SubjectUCNBox subjectUCNBox;
  private RNumberBox totalField;
  private RTextBox remarkField;

  private RViewNumberField remainTotalField;
  private RViewNumberField contractTotalField;
  private RViewNumberField unDepositTotalField;
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

    mvp.add(0, drawSubjectPanel());

    mvp.add(1, drawBalancePanel());

    setStyleName(RCaptionBox.STYLENAME_CONCISE);
    setCaption(DepositMessage.M.resultLine(1));
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

  private Widget drawSubjectPanel() {
    payInfoForm = new RForm(1);
    payInfoForm.setWidth("100%");

    subjectUCNBox = new SubjectUCNBox(BSubjectType.predeposit.name(), new Integer(
        DirectionType.payment.getDirectionValue()));
    subjectUCNBox.setCaption(PDepositLineDef.constants.subject());
    subjectUCNBox.getBrowser().addSelectionHandler(new SelectionHandler<BSubject>() {
      @Override
      public void onSelection(SelectionEvent<BSubject> event) {
        BSubject item = event.getSelectedItem();
        subjectUCNBox.setRawValue(item);
        subjectUCNBox.setValue(item.getSubject(), true);
      }
    });
    subjectUCNBox.setRequired(true);
    subjectUCNBox.addChangeHandler(handler);
    payInfoForm.addField(subjectUCNBox);

    totalField = new RNumberBox(PDepositLineDef.amount);
    totalField.setWidth("50%");
    totalField.setSelectAllOnFocus(true);
    totalField.setScale(2);
    totalField.setTextAlignment(RTextBoxBase.ALIGN_RIGHT);
    totalField.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2);
    totalField.setFormat(M3Format.fmt_money);
    totalField.addChangeHandler(handler);
    payInfoForm.addField(totalField);

    remarkField = new RTextBox(PDepositLineDef.remark);
    remarkField.setEnterToTab(false);
    remarkField.addChangeHandler(handler);
    remarkField.addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == RKey.KEY_CODE_ENTER) {
          GWTUtil.blurActiveElement();
          if (entity.getLines().get(entity.getLines().size() - 1) == line)
            RActionEvent.fire(PayDepositLineEditGadget.this, DepositActionName.ADD_LINE);
          else {
            subjectUCNBox.setFocus(true);
            element = GWTUtil.getActiveElement();
            RActionEvent.fire(PayDepositLineEditGadget.this, DepositActionName.NEXT_LINE);
          }
        }
      }
    });
    payInfoForm.addField(remarkField);

    payInfoForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(DepositMessage.M.payDepositInfo());
    box.setWidth("100%");
    box.setContent(payInfoForm);
    return box;
  }

  private Widget drawBalancePanel() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    remainTotalField = new RViewNumberField();
    remainTotalField.setWidth("38%");
    remainTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    remainTotalField.setCaption(PDepositLineDef.constants.remainTotal());
    remainTotalField.setFormat(M3Format.fmt_money);
    form.addField(remainTotalField);

    contractTotalField = new RViewNumberField();
    contractTotalField.setWidth("38%");
    contractTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    contractTotalField.setCaption(DepositMessage.M.contractTotal());
    contractTotalField.setFormat(M3Format.fmt_money);
    form.addField(contractTotalField);

    unDepositTotalField = new RViewNumberField();
    unDepositTotalField.setWidth("38%");
    unDepositTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    unDepositTotalField.setCaption(DepositMessage.M.unDepositTotal());
    unDepositTotalField.setFormat(M3Format.fmt_money);
    form.addField(unDepositTotalField);

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(DepositMessage.M.referenceInfo());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  @Override
  public Focusable getFocusable(String field) {
    if (BDepositLine.FN_SUBJECT.equals(field)) {
      return subjectUCNBox;
    } else if (BDepositLine.FN_TOTAL.equals(field)) {
      return totalField;
    }
    return null;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  public void clearQueryConditions() {
    if (subjectUCNBox != null)
      ((SubjectBrowserDialog) subjectUCNBox.getBrowser()).clearConditions();
  }

  public void setEntity(BDeposit entity) {
    this.entity = entity;
  }

  @Override
  public void onAction(final RActionEvent event) {
    if (event.getActionName() == DepositActionName.BEFORE_CHANGE_LINE) {
      element = GWTUtil.getActiveElement();
    }
    if (event.getActionName() == DepositActionName.CHANGE_LINE) {
      CommandQueue.offer(new LocalCommand() {
        public void onCall(CommandQueue queue) {
          int row = ((Integer) event.getParameters().get(0)).intValue();
          setCaption(DepositMessage.M.resultLine(row + 1));
          line = entity.getLines().get(row);
          Boolean isAdd = event.getParameters().size() > 1 ? (Boolean) event.getParameters().get(1)
              : Boolean.FALSE;
          changeLine(isAdd.booleanValue());
          queue.goon();
        }
      });
      CommandQueue.awake();
    }
  }

  private void changeLine(final boolean isNew) {
    int size = entity.getLines().size();
    boolean prevDisable = entity.getLines().get(0) == line;
    prevAction.setEnabled(!prevDisable);
    boolean nextDisable = entity.getLines().get(size - 1) == line;
    nextAction.setEnabled(!nextDisable);

    assert line != null;
    refresh(line.getSubject() == null && line.getTotal() != null);
    Timer t = new Timer() {

      @Override
      public void run() {
        if (isNew)
          subjectUCNBox.setFocus(true);
        else if (element != null) {
          // 焦点定位到换行前的位置
          GWTUtil.focus(element);
        }
      }
    };
    t.schedule(200);
  }

  private void fetchLineExt() {
    if (line.getSubject() != null && entity.getCounterpart() != null
        && entity.getAccountUnit() != null && line.getSubject().getUuid() != null
        && entity.getCounterpart().getUuid() != null && entity.getAccountUnit().getUuid() != null) {
      CommandQueue.offer(new RPCCommand<BDepositTotal>() {
        @Override
        public void onCall(CommandQueue queue, AsyncCallback<BDepositTotal> callback) {
          RLoadingDialog.show();

          String contractUuid = null;
          if (entity.getContract() != null)
            contractUuid = entity.getContract().getUuid();

          PayDepositService.Locator.getService().getAdvance(entity.getAccountUnit().getUuid(),
              entity.getCounterpart().getUuid(), line.getSubject().getUuid(), contractUuid,
              callback);
        }

        @Override
        public void onFailure(CommandQueue queue, Throwable t) {
          RLoadingDialog.hide();
          String msg = DepositMessage.M.actionFailed(DepositMessage.M.fetch(),
              DepositMessage.M.referenceInfo());
          RMsgBox.showError(msg, t);
          queue.abort();
        }

        @Override
        public void onSuccess(CommandQueue queue, BDepositTotal result) {
          RLoadingDialog.hide();
          line.setRemainTotal(result.getAdvanceTotal());
          if (result.getContractTotal() != null) {
            line.setContractTotal(result.getContractTotal());
            if (line.getTotal() == null || BigDecimal.ZERO.compareTo(line.getTotal()) == 0) {
              line.setTotal(line.getUnDepositTotal());
            }
          }
          refresh(line.getSubject() == null && line.getTotal() != null);

          RActionEvent.fire(PayDepositLineEditGadget.this, DepositActionName.CHANGE_TOTAL);
          
          RActionEvent.fire(PayDepositLineEditGadget.this, DepositActionName.CHANGE_VALUE,
              new Integer(line.getLineNumber() - 1));
          queue.goon();
        }
      });
      CommandQueue.awake();
    } else {
      refresh(line.getSubject() == null && line.getTotal() != null);
    }
  }

  private void refresh(boolean validate) {
    assert line != null;

    subjectUCNBox.setValue(line.getSubject());
    if (line.getTotal() == null)
      line.setTotal(BigDecimal.ZERO);
    totalField.setValue(line.getTotal());
    remarkField.setValue(line.getRemark());
    if (line.getRemainTotal() == null)
      line.setRemainTotal(BigDecimal.ZERO);
    remainTotalField.setValue(line.getRemainTotal());
    if (line.getContractTotal() == null) {
      line.setContractTotal(BigDecimal.ZERO);
    }
    contractTotalField.setValue(line.getContractTotal());
    unDepositTotalField.setValue(line.getUnDepositTotal());

    if (validate)
      clearValidResults();
  }

  public void clearValidResults() {
    payInfoForm.clearValidResults();
  }

  private class Handler_prevAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      markActiveElement();
      RActionEvent.fire(PayDepositLineEditGadget.this, DepositActionName.PREV_LINE);
    }
  }

  private class Handler_nextAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      markActiveElement();
      RActionEvent.fire(PayDepositLineEditGadget.this, DepositActionName.NEXT_LINE);
    }
  }

  private class Handler_deleteAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RActionEvent.fire(PayDepositLineEditGadget.this, DepositActionName.DELETE_CURRENT_LINE);
    }
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == subjectUCNBox) {
        line.setSubject(subjectUCNBox.getValue());
        if (subjectUCNBox.validate())
          fetchLineExt();
      } else if (event.getSource() == totalField) {
        line.setTotal(totalField.getValueAsBigDecimal());
        if (line.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
          totalField.addErrorMessage(DepositMessage.M.min(PDepositLineDef.constants.amount()));
        }
        unDepositTotalField.setValue(line.getUnDepositTotal());
        RActionEvent.fire(PayDepositLineEditGadget.this, DepositActionName.CHANGE_TOTAL);
      } else if (event.getSource() == remarkField) {
        line.setRemark(remarkField.getValue());
      }
      markActiveElement();
      RActionEvent.fire(PayDepositLineEditGadget.this, DepositActionName.CHANGE_VALUE, new Integer(
          line.getLineNumber() - 1));
    }
  }

  private void markActiveElement() {
    element = GWTUtil.getActiveElement();
    GWTUtil.blurActiveElement();
  }
}
