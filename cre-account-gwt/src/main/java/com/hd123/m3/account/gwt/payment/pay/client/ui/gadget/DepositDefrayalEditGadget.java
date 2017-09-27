/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： DepositDefrayalGadget.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.pay.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.payment.commons.client.AdvanceSubjectFilter;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentDepositDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BRemainTotal;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentHasFocusables;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.SubjectComboBox.WidgetRes;
import com.hd123.m3.account.gwt.payment.commons.client.rpc.PaymentCommonsService;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDepositDefrayalDef;
import com.hd123.m3.account.gwt.payment.pay.client.EPPayment;
import com.hd123.m3.account.gwt.payment.pay.client.PaymentMessages;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 按总额收款扣预存款明细行编辑控件
 * 
 * @author subinzhu
 * 
 */
public class DepositDefrayalEditGadget extends RCaptionBox implements RActionHandler,
    HasRActionHandlers, PaymentHasFocusables, Focusable {

  private BPayment bill;
  private List<BPaymentDepositDefrayal> values;
  private FlexTable tableTitle = new FlexTable();
  private RVerticalPanel detailTable;
  private List<DepositDefrayalLineEditGadget> detailGadgets = new ArrayList<DepositDefrayalLineEditGadget>();

  /** 科目下拉框选项值 */
  private List<BUCN> subjects = new ArrayList<BUCN>();
  /** 合同下拉框选项值 */
  private List<BUCN> contracts = new ArrayList<BUCN>();

  private List<Message> messages = new ArrayList<Message>();

  public DepositDefrayalEditGadget() {
    super();
    drawSelf();
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();

    vp.add(drawDetailsTitle());
    vp.add(initDetail());

    setCaption(PaymentMessages.M.depositBillPay());
    setStyleName(RCaptionBox.STYLENAME_CONCISE);
    setWidth("100%");
    setContent(vp);
  }

  private Widget drawDetailsTitle() {
    if (tableTitle == null)
      tableTitle = new FlexTable();

    tableTitle.clear();

    tableTitle.setWidget(0, 0, new HTML(PaymentMessages.M.depositPaySubject()));
    tableTitle.getColumnFormatter().setWidth(0, DepositDefrayalLineEditGadget.WIDTH_DETAIL_COL0);

    tableTitle
        .setWidget(0, 1, new HTML(PPaymentDepositDefrayalDef.constants.contract_billNumber()));
    tableTitle.getColumnFormatter().setWidth(1, DepositDefrayalLineEditGadget.WIDTH_DETAIL_COL1);

    tableTitle.setWidget(0, 2, new HTML(PaymentMessages.M.remainTotal()));
    tableTitle.getColumnFormatter().setWidth(2, DepositDefrayalLineEditGadget.WIDTH_DETAIL_COL2);

    tableTitle.setWidget(0, 3, new HTML(PPaymentDepositDefrayalDef.constants.total()));
    tableTitle.getColumnFormatter().setWidth(3, DepositDefrayalLineEditGadget.WIDTH_DETAIL_COL3);

    tableTitle.setWidget(0, 4, new HTML(PPaymentDepositDefrayalDef.constants.remark()));
    tableTitle.getColumnFormatter().setWidth(4, DepositDefrayalLineEditGadget.WIDTH_DETAIL_COL4);
    return tableTitle;
  }

  private Widget initDetail() {
    if (detailTable == null) {
      detailTable = new RVerticalPanel();
    }

    detailTable.clear();
    detailGadgets.clear();

    return detailTable;
  }

  @Override
  public void clearValidResults() {
    messages.clear();
    for (DepositDefrayalLineEditGadget g : detailGadgets)
      g.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return messages.isEmpty();
  }

  @Override
  public List<Message> getInvalidMessages() {
    for (DepositDefrayalLineEditGadget g : detailGadgets)
      messages.addAll(g.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    clearValidResults();
    boolean isValid = true;
    for (DepositDefrayalLineEditGadget g : detailGadgets)
      isValid &= g.validate();
    isValid &= validRepeat();
    return isValid;
  }

  /** 重复验证 */
  private boolean validRepeat() {
    boolean valid = true;
    for (int i = values.size() - 1; i >= 0; i--) {
      BPaymentDepositDefrayal deposit = values.get(i);
      for (int j = 0; j < i; j++) {
        BPaymentDepositDefrayal d = values.get(j);
        if (deposit.getSubject() != null && d.getSubject() != null
            && deposit.getSubject().equals(d.getSubject())
            && ObjectUtil.equals(deposit.getContract(), d.getContract())) {
          String msg = PaymentMessages.M
              .lineRepeatError(
                  PaymentMessages.M.depositBillPay() + PaymentMessages.M.lineNumber(i + 1)
                      + PaymentMessages.M.subjectAndContract(), j + 1);
          detailGadgets.get(i).addRepeatError(msg);
          valid = false;
          break;
        }
      }
    }
    return valid;
  }

  public List<BPaymentDepositDefrayal> getValue() {
    return values;
  }

  public void setValue(BPayment bill) {
    this.bill = bill;
    this.values = bill.getDeposits();

    refreshSubjects();
    refreshContracts();

    refreshLineNumber();
    refreshDetail();
  }

  private void refreshLineNumber() {
    for (int i = 0; i < values.size(); i++) {
      values.get(i).setLineNumber(i);
    }
  }

  private void refreshDetail() {
    initDetail();

    if (values == null)
      return;

    if (values.isEmpty()) {
      BPaymentDepositDefrayal depositDefrayal = new BPaymentDepositDefrayal();
      depositDefrayal.setSubject(EPPayment.getInstance().getDefaultOption().getPrePaySubject());
      values.add(depositDefrayal);
    }

    for (int i = 0; i < values.size(); i++)
      addDetail(values.get(i), i);
  }

  @Override
  public void onAction(RActionEvent event) {
    if (ObjectUtil.equals(event.getActionName(),
        DepositDefrayalLineEditGadget.ActionName.ACTION_DEPOSITDEFRAYALLINE_ADDDETAIL)) {
      int index = (Integer) event.getParameters().iterator().next();
      BPaymentDepositDefrayal newDetail = new BPaymentDepositDefrayal();

      insertDetail(newDetail, index + 1);
      refreshLineNumber();
    } else if (ObjectUtil.equals(event.getActionName(),
        DepositDefrayalLineEditGadget.ActionName.ACTION_DEPOSITDEFRAYALLINE_REMOVEDETAIL)) {
      int index = (Integer) event.getParameters().iterator().next();
      removeDetail(index);
      refreshLineNumber();

      RActionEvent.fire(DepositDefrayalEditGadget.this, ActionName.ACTION_DEPOSITDEFRAYAL_CHANGE);
    } else if (ObjectUtil.equals(event.getActionName(),
        DepositDefrayalLineEditGadget.ActionName.ACTION_DEPOSITDEFRAYALLINE_TOTALCHANGE)) {
      RActionEvent.fire(DepositDefrayalEditGadget.this, ActionName.ACTION_DEPOSITDEFRAYAL_CHANGE);
    }
  }

  private void insertDetail(BPaymentDepositDefrayal detail, int insertRow) {
    values.add(insertRow, detail);

    addDetail(detail, insertRow);
  }

  private void addDetail(BPaymentDepositDefrayal detail, int insertRow) {
    DepositDefrayalLineEditGadget gadget = new DepositDefrayalLineEditGadget();
    gadget.setBill(bill);
    gadget.refreshSubjects(subjects);
    gadget.refreshContracts(contracts);
    gadget.setValue(detail);
    gadget.addActionHandler(this);
    detailGadgets.add(insertRow, gadget);
    detailTable.insert(gadget, insertRow);
  }

  private void removeDetail(int currentRow) {
    if (values.size() > 1) {
      values.remove(currentRow);
      detailGadgets.remove(currentRow);
      detailTable.remove(currentRow);
    } else {
      values.clear();
      detailGadgets.clear();
      detailTable.clear();

      BPaymentDepositDefrayal newDetail = new BPaymentDepositDefrayal();
      newDetail.setSubject(EPPayment.getInstance().getDefaultOption().getPrePaySubject());
      insertDetail(newDetail, 0);
    }
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  public static class ActionName {
    /** 改变值 */
    public static final String ACTION_DEPOSITDEFRAYAL_CHANGE = "depositDefrayal_change";
  }

  @Override
  public Focusable getFocusable(int lineNumber, String field) {
    if (detailGadgets == null || detailGadgets.isEmpty() || lineNumber < 0
        || detailGadgets.size() <= lineNumber)
      return null;
    else
      return detailGadgets.get(lineNumber).getFocusable(field);
  }

  /**
   * 刷新预存款科目余额
   */
  public void refreshRemainTotal() {
    if (detailGadgets == null || detailGadgets.isEmpty())
      return;

    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid())
        || bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid())) {
      for (BPaymentDepositDefrayal d : values) {
        d.setRemainTotal(BigDecimal.ZERO);
      }
    } else {
      List<BRemainTotal> remainTotalIds = new ArrayList<BRemainTotal>();
      for (BPaymentDepositDefrayal d : values) {
        if (d.getSubject() == null) {
          d.setRemainTotal(BigDecimal.ZERO);
          continue;
        }
        BRemainTotal r = new BRemainTotal();
        r.setSubject(d.getSubject().getUuid());
        r.setContract((d.getContract() == null || StringUtil.isNullOrBlank(d.getContract()
            .getUuid())) ? "-" : d.getContract().getUuid());
        remainTotalIds.add(r);
      }
      getRemainTotals(remainTotalIds);
    }
    for (DepositDefrayalLineEditGadget g : detailGadgets) {
      g.refreshRemainTotal();
    }
  }

  /**
   * 批量获取预存款科目余额
   * 
   * @param ramainTotals
   */
  private void getRemainTotals(List<BRemainTotal> remainTotalIds) {
    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid()))
      return;

    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show(PaymentMessages.M.loading2(PaymentMessages.M.depositPaySubject()
        + PaymentMessages.M.remainTotal()));
    PaymentCommonsService.Locator.getService().getDepositSubjectRemainTotals(
        bill.getAccountUnit().getUuid(), bill.getCounterpart().getUuid(), remainTotalIds,
        new RBAsyncCallback2<List<BRemainTotal>>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentMessages.M.actionFailed(PaymentMessages.M.load(),
                PaymentMessages.M.depositPaySubject() + PaymentMessages.M.remainTotal());
            RMsgBox.showErrorAndBack(msg, caught);
          }

          @Override
          public void onSuccess(List<BRemainTotal> result) {
            RLoadingDialog.hide();
            for (BPaymentDepositDefrayal value : values) {
              if (value.getSubject() == null)
                continue;

              for (BRemainTotal r : result) {
                if (value.getSubject().getUuid().equals(r.getSubject())
                    && (((value.getContract() == null || StringUtil.isNullOrBlank(value
                        .getContract().getUuid())) && r.getContract().equals("-")) || (value
                        .getContract() != null
                        && !StringUtil.isNullOrBlank(value.getContract().getUuid()) && value
                        .getContract().getUuid().equals(r.getContract())))) {
                  value.setRemainTotal(r.getRemainTotal());
                }
              }
            }
          }
        });
  }

  /**
   * 刷新科目和合同下拉框的选项
   */
  public void refreshSubjectsAndContracts() {
    refreshSubjects();
    refreshContracts();

    if (values == null || values.isEmpty()) {
      detailGadgets.clear();
      return;
    }
    for (DepositDefrayalLineEditGadget g : detailGadgets) {
      g.refreshSubjects(subjects);
      g.refreshContracts(contracts);
    }
  }

  /**
   * 刷新科目下拉值选项
   */
  private void refreshSubjects() {
    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid())
        || bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid())) {
      subjects.clear();
      return;
    }

    GWTUtil.enableSynchronousRPC();
    AdvanceSubjectFilter filter = new AdvanceSubjectFilter();
    filter.setAccountUnitUuid(bill.getAccountUnit().getUuid());
    filter.setDirectionType(DirectionType.payment.getDirectionValue());
    filter.setCounterpartUuid(bill.getCounterpart().getUuid());
    PaymentCommonsService.Locator.getService().getSubjects(filter, new AsyncCallback<List<BUCN>>() {

      @Override
      public void onFailure(Throwable arg0) {
        String msg = CommonsMessages.M.actionFailed(CommonsMessages.M.load(),
            WidgetRes.R.paymentSubject());
        RMsgBox.show(msg);
      }

      @Override
      public void onSuccess(List<BUCN> result) {
        subjects.clear();
        if (result == null || result.isEmpty()) {
          return;
        }
        subjects.addAll(result);
      }
    });
  }

  /**
   * 刷新合同下拉值选项
   */
  private void refreshContracts() {
    if (bill.getAccountUnit() == null || StringUtil.isNullOrBlank(bill.getAccountUnit().getUuid())
        || bill.getCounterpart() == null
        || StringUtil.isNullOrBlank(bill.getCounterpart().getUuid())) {
      contracts.clear();
      return;
    }

    GWTUtil.enableSynchronousRPC();
    PaymentCommonsService.Locator.getService().getContracts(bill.getAccountUnit().getUuid(),
        bill.getCounterpart().getUuid(), new AsyncCallback<List<BUCN>>() {

          @Override
          public void onFailure(Throwable arg0) {
            String msg = CommonsMessages.M.actionFailed(CommonsMessages.M.load(),
                PaymentMessages.M.contract());
            RMsgBox.show(msg);
          }

          @Override
          public void onSuccess(List<BUCN> result) {
            contracts.clear();
            if (result == null || result.isEmpty()) {
              return;
            }
            contracts.addAll(result);
          }
        });
  }

  @Override
  public int getTabIndex() {
    Focusable field = getFocusableField();
    return field == null ? 0 : field.getTabIndex();
  }

  @Override
  public void setAccessKey(char key) {
    Focusable field = getFocusableField();
    if (field != null)
      field.setAccessKey(key);
  }

  @Override
  public void setFocus(boolean focused) {
    Focusable field = getFocusableField();
    if (field != null)
      field.setFocus(focused);
  }

  @Override
  public void setTabIndex(int index) {
    Focusable field = getFocusableField();
    if (field != null)
      field.setTabIndex(index);
  }

  private Focusable getFocusableField() {
    if (detailGadgets.isEmpty())
      return null;
    return detailGadgets.get(0);
  }
}
