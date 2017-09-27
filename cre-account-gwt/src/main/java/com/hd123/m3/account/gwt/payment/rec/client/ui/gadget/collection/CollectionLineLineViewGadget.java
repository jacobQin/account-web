/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	CollectionLineSingleLineEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.collection;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineDefrayal;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentAccountLineDef;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.AccountLineMultiViewBox;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.BillPaymentTypeViewGadget;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
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
 * 付款方式查看控件（按科目多收款）
 * 
 * @author LiBin
 * 
 */
public class CollectionLineLineViewGadget extends Composite implements RActionHandler, HasRActionHandlers {

  private BPayment bill;
  private BPaymentCollectionLine line;

  private RCaptionBox box;
  private RAction prevAction;
  private RAction nextAction;

  private BillPaymentTypeViewGadget paymentTypeGadget;

  private RCaptionBox rightDownBox;
  private RTextArea remarkField;

  public void setBill(BPayment bill) {
    this.bill = bill;
    if(bill.getCollectionLines().isEmpty()==false){
      line = bill.getCollectionLines().get(0);
      refresh();
    }
  }

  public CollectionLineLineViewGadget() {
    drawSelf();
  }

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    mvp.add(0,drawRightUpPanel());
    mvp.add(1, drawRightDownPanel());

    box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(ReceiptMessages.M.lineNumber(0));
    box.setWidth("100%");
    box.setContent(vp);

    box.getCaptionBar().addButton(new RToolbarSeparator());

    prevAction = new RAction(RActionFacade.PREVIOUS_LINE, new Handler_prevAction());
    prevAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_UP));
    RToolbarButton prevButton = new RToolbarButton(prevAction);
    prevButton.setShowText(false);
    box.getCaptionBar().addButton(prevButton);

    nextAction = new RAction(RActionFacade.NEXT_LINE, new Handler_nextAction());
    nextAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_DOWN));
    RToolbarButton nextButton = new RToolbarButton(nextAction);
    nextButton.setShowText(false);
    box.getCaptionBar().addButton(nextButton);

    initWidget(box);
  }

  private Widget drawRightUpPanel() {
    paymentTypeGadget = new BillPaymentTypeViewGadget();
    return paymentTypeGadget;
  }

  private Widget drawRightDownPanel() {
    remarkField = new RTextArea();
    remarkField.setReadOnly(true);

    rightDownBox = new RCaptionBox();
    rightDownBox.setContent(remarkField);
    rightDownBox.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    rightDownBox.setCaption(PPaymentAccountLineDef.constants.remark());
    rightDownBox.setWidth("100%");

    return rightDownBox;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == AccountLineMultiViewBox.ActionName.ACTION_CHANGELINENO) {
      int row = ((Integer) event.getParameters().get(0)).intValue();
      box.setCaption(ReceiptMessages.M.lineNumber(row + 1));

      if (row < 0 || row >= bill.getCollectionLines().size()) {
        prevAction.setEnabled(false);
        nextAction.setEnabled(false);
        clearValue();
      } else {
        line = bill.getCollectionLines().get(row);
        changeLine();
      }
    }
  }

  /**
   * 清除值
   */
  private void clearValue() {
    clearRightForm();
    clearRemark();
  }

  private void clearRightForm() {
    paymentTypeGadget.initDetail();
  }

  private void clearRemark() {
    remarkField.clearValue();
  }

  private void changeLine() {
    int size = bill.getCollectionLines().size();
    boolean prevDisable = bill.getCollectionLines().get(0) == line;
    prevAction.setEnabled(!prevDisable);
    boolean nextDisable = bill.getCollectionLines().get(size - 1) == line;
    nextAction.setEnabled(!nextDisable);

    refresh();
  }

  private void refresh() {
    assert line != null;

    refreshRightUpForm();
    refreshRightDownForm();
  }


  private void refreshRightUpForm() {
    List<BPaymentLineDefrayal> defrayals = new ArrayList<BPaymentLineDefrayal>();
    defrayals.addAll(line.getCashs());
    defrayals.addAll(line.getDeposits());
    defrayals.addAll(line.getDeductions());
    paymentTypeGadget.setValues(defrayals);
  }

  private void refreshRightDownForm() {
    remarkField.setValue(line.getRemark());
  }

  private class Handler_prevAction implements ClickHandler {
    public void onClick(ClickEvent event) {
      GWTUtil.blurActiveElement();
      RActionEvent.fire(CollectionLineLineViewGadget.this, ActionName.ACTION_PREV);
    }
  }

  private class Handler_nextAction implements ClickHandler {
    public void onClick(ClickEvent event) {
      GWTUtil.blurActiveElement();
      RActionEvent.fire(CollectionLineLineViewGadget.this, ActionName.ACTION_NEXT);
    }
  }

  public static class ActionName {
    /** 上一行 */
    public static final String ACTION_PREV = "prev";
    /** 下一行 */
    public static final String ACTION_NEXT = "next";
  }

}
