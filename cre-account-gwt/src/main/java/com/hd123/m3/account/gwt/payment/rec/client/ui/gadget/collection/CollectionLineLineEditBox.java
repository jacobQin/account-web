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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BUsageType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox.Callback;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCashDefrayal;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineDefrayalLineLocator;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentLineLocator;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCollectionLineDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.biz.ActionName;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.m3.commons.gwt.util.client.bill.BillConfig;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridCellWidgetRenderer;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.geom.Point;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.util.client.message.MessageLevel;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.hotkey.RHotKey;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSpace;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSplitButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.gwt.widget2e.client.util.MessageHelper;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 代收明细编辑控件（按科目多收款）
 * 
 * @author LiBin
 * 
 */
public class CollectionLineLineEditBox extends RCaptionBox implements HasRActionHandlers,
    RActionHandler {

  /** 账款明细行 */
  private List<BPaymentCollectionLine> lines = new ArrayList<BPaymentCollectionLine>();
  private BPayment bill;
  private EPReceipt ep = EPReceipt.getInstance();
  private BContract contract = null;
  private int currentRow = -1;

  private HTML totalCount;
  private RToolbarSplitButton appendButton;
  private RAction addAction;
  private RAction insertAction;
  private RAction deleteAction;

  private EditGrid<BPaymentCollectionLine> grid;
  private RGridColumnDef lineNumberCol;// 行号
  private RGridColumnDef subjectCol; // 科目
  private RGridColumnDef contractCodeCol; // 合同编号
  private RGridColumnDef contractNameCol; // 店招
  private RGridColumnDef beginDateCol;// 起始日期
  private RGridColumnDef endDateCol; // 截止日期
  private RGridColumnDef receivablleTotalCol;// 应收金额
  private RGridColumnDef realTotalCol;// 账款实收金额
  private RGridColumnDef unreceivableCol;// 未收金额
  private RGridColumnDef remarkCol; // 说明

  private boolean checkEmpty;

  private CollectionLineLineEditGadget collectionLineLineEditGadget;

  public void setBill(BPayment bill) {
    this.bill = bill;
    if (this.bill.getCollectionLines().isEmpty()) {
      initLines();
    }
    collectionLineLineEditGadget.setBill(this.bill);
    setLines(this.bill.getCollectionLines());
  }

  private void initLines() {
    this.bill.getCollectionLines().clear();
    BPaymentCollectionLine line = new BPaymentCollectionLine();
    line.getCashs().add(new BPaymentLineCash());
    this.bill.getCollectionLines().add(line);
  }

  public BPayment getBill() {
    return bill;
  }

  private void setLines(List<BPaymentCollectionLine> lines) {
    this.lines = lines;
    currentRow = 0;
    grid.setValues(this.lines);
  }

  public CollectionLineLineEditBox() {
    drawToolbar();
    drawSelf();
  }

  private void drawToolbar() {
    ActionHandler handler = new ActionHandler();

    totalCount = new HTML(ReceiptMessages.M.resultTotal(0));
    getCaptionBar().addButton(totalCount);

    getCaptionBar().addButton(new RToolbarSpace("6px"));

    RPopupMenu addMenu = new RPopupMenu();

    addAction = new RAction(RActionFacade.APPEND, handler);
    addAction.setCaption(ReceiptMessages.M.addLine());
    addAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(addAction));

    insertAction = new RAction(ReceiptMessages.M.insertLineBeforeCurrentLine(), handler);
    insertAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(insertAction));

    addMenu.addSeparator();

    appendButton = new RToolbarSplitButton(addAction);
    appendButton.setMenu(addMenu);
    appendButton.setShowText(false);
    getCaptionBar().addButton(appendButton);

    getCaptionBar().addButton(new RToolbarSpace("6px"));

    deleteAction = new RAction(RActionFacade.DELETE, handler);
    deleteAction.setHotKey(null);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);
    panel.setStyleName(RCaptionBox.STYLENAME_STANDARD);
    setContent(panel);

    panel.add(drawGrid());

    collectionLineLineEditGadget = new CollectionLineLineEditGadget();
    this.addActionHandler(collectionLineLineEditGadget);
    panel.add(collectionLineLineEditGadget);

    setEditing(true);
    setBorder(false);
    setWidth("100%");
    getCaptionBar().setShowCollapse(true);
    setCaption(ReceiptMessages.M.collectionDetail());

    addActionHandler();
  }

  private Widget drawGrid() {
    grid = new EditGrid<BPaymentCollectionLine>();
    grid.setCaption(PPaymentCollectionLineDef.TABLE_CAPTION);
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.addHighlightHandler(new GridRowHighlightHandler());

    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.setDataProducer(provider);
    grid.addRefreshHandler(new GridRefreshHandler());

    CellRendererFactory rendererFactory = new CellRendererFactory();

    lineNumberCol = new RGridColumnDef(PPaymentCollectionLineDef.lineNumber);
    lineNumberCol.setWidth(BillConfig.COLUMNWIDTH_BILLLINENUMBER);
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PPaymentCollectionLineDef.subject);
    subjectCol.setRendererFactory(rendererFactory);
    subjectCol.setOverflowEllipsis(true);
    subjectCol.setWidth("260px");
    grid.addColumnDef(subjectCol);

    contractCodeCol = new RGridColumnDef(PPaymentCollectionLineDef.contractCode);
    contractCodeCol.setRendererFactory(rendererFactory);
    contractCodeCol.setOverflowEllipsis(true);
    contractCodeCol.setWidth("200px");
    grid.addColumnDef(contractCodeCol);

    contractNameCol = new RGridColumnDef(PPaymentCollectionLineDef.contractName);
    contractNameCol.setOverflowEllipsis(true);
    contractNameCol.setWidth("120px");
    grid.addColumnDef(contractNameCol);

    beginDateCol = new RGridColumnDef(PPaymentCollectionLineDef.beginTime);
    beginDateCol.setRendererFactory(rendererFactory);
    beginDateCol.setWidth("150px");
    grid.addColumnDef(beginDateCol);

    endDateCol = new RGridColumnDef(PPaymentCollectionLineDef.endTime);
    endDateCol.setRendererFactory(rendererFactory);
    endDateCol.setWidth("150px");
    grid.addColumnDef(endDateCol);

    receivablleTotalCol = new RGridColumnDef(PPaymentCollectionLineDef.receivableTotal);
    receivablleTotalCol.setWidth("120px");
    receivablleTotalCol.setRendererFactory(rendererFactory);
    receivablleTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(receivablleTotalCol);

    realTotalCol = new RGridColumnDef(PPaymentCollectionLineDef.realTotal);
    realTotalCol.setWidth("120px");
    realTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(realTotalCol);

    unreceivableCol = new RGridColumnDef(PPaymentCollectionLineDef.unreceivableTotal);
    unreceivableCol.setWidth("120px");
    unreceivableCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(unreceivableCol);

    remarkCol = new RGridColumnDef(PPaymentCollectionLineDef.remark);
    remarkCol.setRendererFactory(rendererFactory);
    remarkCol.setWidth("100px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    grid.setAllowHorizontalScrollBar(true);

    return grid;
  }

  private void addActionHandler() {
    collectionLineLineEditGadget.addActionHandler(this);
    addActionHandler(collectionLineLineEditGadget);
  }

  public void onHide() {
    refreshTotalCount(0);
  }

  @Override
  public void clearValidResults() {
    grid.clearValidResults();
  }

  @Override
  public boolean isValid() {
    Boolean valid = grid.isValid() && collectionLineLineEditGadget.isValid();
    return valid;
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(grid.getInvalidMessages());
    messages.addAll(collectionLineLineEditGadget.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    checkEmpty = true;
    boolean isValid = grid.validate();
    isValid &= collectionLineLineEditGadget.validate();
    checkEmpty = false;
    return isValid;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  private void refreshTotalCount(int size) {
    totalCount.setHTML(ReceiptMessages.M.resultTotal(size));
  }

  private void fireReceivableChange() {
    RActionEvent.fire(CollectionLineLineEditBox.this, ActionName.ACTION_RECEIVABLE_CHANGE);
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == ActionName.ACTION_LINEACCOUNTLINE_PREV) {
      if (currentRow > 0) {
        RActionEvent.fire(this, ActionName.ACTION_ACCOUNTLINE_CHANGELINENO,
            Integer.valueOf(--currentRow), Boolean.FALSE);
        grid.setCurrentRow(currentRow);
      }
    } else if (event.getActionName() == ActionName.ACTION_LINEACCOUNTLINE_NEXT) {
      if (currentRow < lines.size() - 1) {
        RActionEvent.fire(this, ActionName.ACTION_ACCOUNTLINE_CHANGELINENO,
            Integer.valueOf(++currentRow), Boolean.FALSE);
        grid.setCurrentRow(currentRow);
      }
    } else if (event.getActionName() == ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE) {
      if (currentRow >= 0 && currentRow < lines.size())
        grid.refreshRow(currentRow);
      RActionEvent.fire(CollectionLineLineEditBox.this,
          ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE);
    } else if (event.getActionName() == ActionName.ACTION_LINEACCOUNTLINE_DELETE) {
      doDeleteCurRow();
    } else if (event.getActionName() == ActionName.ACTION_LINEACCOUNTLINE_CHANGE) {
      if (currentRow >= 0 && currentRow < lines.size())
        grid.refreshRow(currentRow);
    } else if ((event.getActionName() == ActionName.CREATEPAGE_CHANGE_LINE || event.getActionName() == ActionName.EDITPAGE_CHANGE_LINE)) {
      PaymentLineLocator locator = (PaymentLineLocator) event.getParameters().get(0);
      if (PaymentLineLocator.ACCOUNTTAB_ID.intValue() == locator.getTabId().intValue()) {
        currentRow = locator.getLineNumber();
        grid.setCurrentRow(currentRow);
        grid.refreshRow(currentRow);
      }
      RActionEvent.fire(CollectionLineLineEditBox.this, event.getActionName(), locator);
    } else if ((event.getActionName() == ActionName.CREATEPAGE_CHANGE_LINE_AND_LINEDEFRAYALLINE || event
        .getActionName() == ActionName.EDITPAGE_CHANGE_LINE_AND_LINEDEFRAYALLINE)) {
      PaymentLineDefrayalLineLocator locator = (PaymentLineDefrayalLineLocator) event
          .getParameters().get(0);
      if (PaymentLineDefrayalLineLocator.ACCOUNTMULTITAB_ID.intValue() == locator.getTabId()
          .intValue()) {
        currentRow = locator.getOutLineNumber().intValue();
        grid.setCurrentRow(currentRow);
        grid.refreshRow(currentRow);
      }
      RActionEvent.fire(CollectionLineLineEditBox.this, event.getActionName(), locator);
    } else if ((event.getSource() instanceof CollectionLineLineEditGadget == false) // 必须循环监听
        && event.getActionName() == ActionName.ACTION_GENERALCREATE_REFRESH_BANKS) {
      RActionEvent.fire(CollectionLineLineEditBox.this, event.getActionName());
    } else if (event.getActionName() == ActionName.ACTION_SET_CONTRACT) {
      contract = (BContract) event.getParameters().get(0);
      for (BPaymentCollectionLine line : lines) {
        line.setContract(contract == null ? null : new BUCN(contract.getUuid(), contract
            .getBillNumber(), contract.getTitle()));
      }
      for (int i = 0; i < lines.size(); i++) {
        Widget widget = grid.getWidget(i, 3);
        if (widget instanceof ContractRenderer) {
          ((ContractRenderer) widget).field.setAccountUnitUuid(bill.getAccountUnit() == null ? null
              : bill.getAccountUnit().getUuid());
          ((ContractRenderer) widget).field.setCounterpartUuid(bill.getCounterpart() == null ? null
              : bill.getCounterpart().getUuid());
          if (((ContractRenderer) widget).field.getRawValue() != null)
            ((ContractRenderer) widget).field.getRawValue().getMessages().clear();
          ((ContractRenderer) widget).rawValue = contract;
        }
      }
      grid.refresh();
    }

  }

  private void doDeleteCurRow() {
    if (bill.getCollectionLines().size() == 1) {
      BPaymentCollectionLine line = lines.get(0);
      doRefreshPaymentBeforDelete(line);
      bill.getCollectionLines().clear();
      initLines();
    } else {
      BPaymentCollectionLine line = lines.get(currentRow);
      doRefreshPaymentBeforDelete(line);
      lines.remove(currentRow);
      currentRow = lines.size() - 1;
    }

    doRefresh(true);
  }

  private void doRefresh(boolean focusOnFirst) {
    // 重置收款单单头中的产生的预存款金额
    bill.aggregate(ep.getScale(), ep.getRoundingMode());
    refreshLineNumber();
    grid.refresh();
    refreshTotalCount(lines.size());
    RActionEvent.fire(this, ActionName.ACTION_ACCOUNTLINE_CHANGELINENO,
        Integer.valueOf(currentRow), Boolean.valueOf(focusOnFirst));
    RActionEvent.fire(CollectionLineLineEditBox.this, ActionName.ACTION_ACCOUNTLINE_AGGREGATE);
  }

  private void refreshLineNumber() {
    for (int i = 0; i < lines.size(); i++) {
      BPaymentCollectionLine line = lines.get(i);
      line.setLineNumber(i + 1);
    }
  }

  /**
   * 扣除收款单总的应收金额。
   * 
   * @param line
   */
  private void doRefreshPaymentBeforDelete(BPaymentLine line) {
    // 扣除应收金额
    bill.setUnpayedTotal(bill.getUnpayedTotal().subtract(line.getUnpayedTotal()));
    bill.setTotal(bill.getTotal().subtract(line.getTotal()));

    // 扣除实收金额
    for (BPaymentLineCash lineDefrayal : line.getCashs()) {
      if (lineDefrayal.getTotal() != null
          && BigDecimal.ZERO.compareTo(lineDefrayal.getTotal()) != 0)
        bill.setDefrayalTotal(bill.getDefrayalTotal().subtract(lineDefrayal.getTotal()));
      BPaymentCashDefrayal defrayal = getPaymentLineCash(lineDefrayal);
      if (defrayal != null) {
        if (defrayal.getTotal().compareTo(lineDefrayal.getTotal()) == 0) {
          bill.getCashs().remove(defrayal);
        }
        defrayal.setTotal(defrayal.getTotal().subtract(lineDefrayal.getTotal()));
      }
    }

  }

  private BPaymentCashDefrayal getPaymentLineCash(BPaymentLineCash cash) {
    for (int i = bill.getCashs().size() - 1; i >= 0; i++) {
      BPaymentCashDefrayal c = bill.getCashs().get(i);
      if (c.getPaymentType() == null || cash.getPaymentType() == null) {
        continue;
      }

      if (c.getBank() == null && cash.getBank() == null) {
        if (c.getPaymentType().equals(cash.getPaymentType())) {
          return c;
        }
      }
      if (c.getBank() != null && cash.getBank() != null) {
        if (c.getPaymentType().equals(cash.getPaymentType()) && c.getBank().equals(cash.getBank())) {
          return c;
        }
      }
    }

    return null;
  }

  private class GridRefreshHandler implements RefreshHandler<RGridCellInfo> {
    @Override
    public void onRefresh(RefreshEvent<RGridCellInfo> event) {
      refreshTotalCount(lines.size());
    }
  }

  private class GridRowHighlightHandler implements HighlightHandler<Point> {
    @Override
    public void onHighlight(HighlightEvent<Point> event) {
      currentRow = event.getHighlighted().getY();
      RActionEvent.fire(CollectionLineLineEditBox.this, ActionName.ACTION_ACCOUNTLINE_CHANGELINENO,
          Integer.valueOf(currentRow), Boolean.FALSE);
    }
  }

  private class ActionHandler implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == addAction) {
        grid.appendValue(lines.size());
      } else if (event.getSource() == insertAction) {
        int index = grid.getCurrentRow() > 0 ? grid.getCurrentRow() : 0;
        grid.appendValue(index);
      } else if (event.getSource() == deleteAction) {
        doDelete();
      }
    }
  }

  public void doDelete() {
    List list = grid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(ReceiptMessages.M.seleteDataToAction(ReceiptMessages.M.delete(),
          ReceiptMessages.M.line()));
      return;
    }

    RMsgBox.showConfirm(
        ReceiptMessages.M.actionComfirm(ReceiptMessages.M.delete(),
            ReceiptMessages.M.selectedRows()), true, new RMsgBox.ConfirmCallback() {
          @Override
          public void onClosed(boolean confirmed) {
            if (confirmed) {
              doDeleteRows();
              doRefresh(true);
            }
          }
        });
  }

  private void doDeleteRows() {
    List<Integer> selections = grid.getSelections();

    boolean lastRow = false;
    if (bill.getCollectionLines().size() == selections.size()) {
      lastRow = true;
    }

    boolean deleteCurLine = false;
    for (int i = selections.size() - 1; i >= 0; i--) {
      int row = selections.get(i).intValue();
      BPaymentCollectionLine accountLine = lines.get(row);
      doRefreshPaymentBeforDelete(accountLine);
      lines.remove(row);
      if (row == currentRow)
        deleteCurLine = true;
    }

    if (lastRow) {
      initLines();
    }
    // 重置收款单单头中的产生的预存款金额
    bill.setDepositTotal(bill.getDefrayalTotal().subtract(bill.getTotal().getTotal()));

    if (deleteCurLine)
      currentRow = lines.size() - 1;
    else {
      for (Integer row : selections) {
        if (row.intValue() < currentRow)
          currentRow--;
      }
    }

    RActionEvent.fire(CollectionLineLineEditBox.this,
        ActionName.ACTION_ACCOUNTLINE_REFRESHOVERDUELINES);
  }

  private class GridDataProvider implements RGridDataProvider,
      EditGridDataProduce<BPaymentCollectionLine> {

    @Override
    public BPaymentCollectionLine create() {
      return new BPaymentCollectionLine();
    }

    @Override
    public int getRowCount() {
      return lines.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (lines.size() == 0) {
        return null;
      }

      BPaymentCollectionLine line = lines.get(row);

      if (lineNumberCol.getIndex() == col) {
        return row + 1;
      } else if (subjectCol.getIndex() == col) {
        return line.getSubject() == null ? null : line.getSubject().toFriendlyStr();
      } else if (contractCodeCol.getIndex() == col) {
        return line.getContract() == null ? null : line.getContract().getCode();
      } else if (contractNameCol.getIndex() == col) {
        return line.getContract() == null ? null : line.getContract().getName();
      } else if (beginDateCol.getIndex() == col) {
        return line.getBeginDate();
      } else if (endDateCol.getIndex() == col) {
        return line.getEndDate();
      } else if (receivablleTotalCol.getIndex() == col) {
        return line.getUnpayedTotal().getTotal();
      } else if (realTotalCol.getIndex() == col) {
        return getTotalStr(line.getTotal());
      } else if (unreceivableCol.getIndex() == col) {
        return GWTFormat.fmt_money.format(line.getUnReceivableTotal());
      } else if (remarkCol.getIndex() == col) {
        return line.getRemark();
      }
      return line;
    }

    private String getTotalStr(BTotal total) {
      if (total == null || total.getTotal() == null) {
        return null;
      }
      return GWTFormat.fmt_money.format(total.getTotal());
    }
  }

  private class CellRendererFactory implements RCellRendererFactory {

    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        Object data) {
      String fieldName = colDef.getName();
      if (fieldName.equals(receivablleTotalCol.getName())) {
        return new ReceivableTotalRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(remarkCol.getName())) {
        return new RemarkRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(beginDateCol.getName())) {
        return new BeginDateRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(endDateCol.getName())) {
        return new EndDateRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(subjectCol.getName())) {
        return new SubjectRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(contractCodeCol.getName())) {
        return new ContractRenderer(grid, colDef, row, col, data);
      }
      return null;
    }

  }

  private class SubjectRenderer extends EditGridCellWidgetRenderer implements
      ValueChangeHandler<BUCN>, ChangeHandler, RValidator {
    private SubjectUCNBox field;

    public SubjectRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new SubjectUCNBox(null, 1, true, BUsageType.tempFee.name());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addValueChangeHandler(this);
      field.addChangeHandler(this);
      field.setValidator(this);

      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentCollectionLine line = lines.get(getRow());
      field.setValue(line.getSubject());
    }

    @Override
    public void onValueChange(ValueChangeEvent<BUCN> event) {
      final BPaymentCollectionLine line = lines.get(getRow());
      BSubject subject = field.getRawValue();
      if (subject != null) {
        line.setSubject(subject.getSubject());
        line.setRate(subject.getTaxRate());
      } else {
        line.setSubject(null);
      }
    }

    @Override
    public void onChange(ChangeEvent event) {
      final BPaymentCollectionLine line = lines.get(getRow());
      if (field.getValue() == null || field.getValue().getUuid() == null) {
        if (StringUtil.isNullOrBlank(field.getCodeBox().getValue()) == false) {
          line.setSubject(new BUCN(null, field.getCodeBox().getValue(), null));
        }
      } else {
        line.setSubject(field.getValue());
      }
    }

    @Override
    public Message validate(Widget sender, String value) {
      BPaymentCollectionLine line = lines.get(getRow());
      if (checkEmpty == false || line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) <= 0) {
        return null;
      }
      if (field.getValue() == null || field.getValue().getUuid() == null) {
        return Message.error(ReceiptMessages.M.notNullField(field.getFieldCaption()), field);
      }
      return null;
    }
  }

  private class ContractRenderer extends EditGridCellWidgetRenderer implements RValidator {
    private ContractBrowseBox field;
    private BContract rawValue = contract;

    public ContractRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new ContractBrowseBox(PPaymentCollectionLineDef.constants.contract(), false,
          new Callback() {
            @Override
            public void execute(BContract result) {
              onChangeContract();
            }
          }, ep.getCaptionMap());
      field.setAccountUnitUuid(bill == null || bill.getAccountUnit() == null ? null : bill
          .getAccountUnit().getUuid());
      field.setCounterpartUuid(bill == null || bill.getCounterpart() == null ? null : bill
          .getCounterpart().getUuid());
      field.setCounterTypeMap(ep.getCounterpartTypeMap());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.setRequiresAccountUnitAndCountpart(true);
      field.setValidator(this);
      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentCollectionLine line = lines.get(getRow());
      if (line.getContract() != null) {
        BContract bcontract = new BContract();
        bcontract.setUuid(line.getContract().getUuid());
        bcontract.setCode(line.getContract().getCode());
        bcontract.setName(line.getContract().getName());
        field.setRawValue(bcontract);
      } else {
        field.setRawValue(rawValue);
      }
      field.setValue(line.getContract() == null ? null : line.getContract().getCode());
    }

    private void onChangeContract() {
      BPaymentCollectionLine line = lines.get(getRow());
      BContract contract = field.getRawValue();
      if (field.isValid() == false)
        return;
      if (contract != null) {
        line.setContract(new BUCN(contract.getUuid(), contract.getBillNumber(), contract.getTitle()));
      } else {
        line.setContract(null);
      }
      rawValue = contract;
      grid.refreshRow(getRow());
    }

    @Override
    public Message validate(Widget sender, String value) {

      BPaymentCollectionLine line = lines.get(getRow());
      if (MessageHelper.toHighPriorityMessage(field.getRawValue(), MessageLevel.ERROR) != null) {
        return MessageHelper.toHighPriorityMessage(field.getRawValue(), MessageLevel.ERROR);
      }
      if (shouldCheckNull(line)
          && (field.getRawValue() == null || field.getRawValue().getUuid() == null)) {
        return Message.error(ReceiptMessages.M.notNullField(field.getFieldCaption()), field);
      }
      return null;
    }
  }

  /** 应收金额单元格渲染器 */
  private class ReceivableTotalRenderer extends EditGridCellWidgetRenderer implements
      ChangeHandler, RValidator {
    private RNumberBox field;

    public ReceivableTotalRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col,
        Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RNumberBox(colDef.getCaption());
      field.setSelectAllOnFocus(true);
      field.setScale(2);
      field.setFormat(GWTFormat.fmt_money);
      field.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
      field.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2, false);
      field.setMinValue(0, true);
      field.setRequired(true);
      field.addChangeHandler(this);
      field.setValidator(this);
      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentCollectionLine detail = lines.get(getRow());
      field.setValue(detail.getUnpayedTotal().getTotal());
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentCollectionLine value = lines.get(getRow());
      if (field.getValue() == null) {
        return;
      }
      value.getUnpayedTotal().setTotal(field.getValueAsBigDecimal());
      fireReceivableChange();
      grid.refreshRow(getRow());
    }

    @Override
    public Message validate(Widget sender, String value) {
      BPaymentCollectionLine line = lines.get(getRow());

      if (checkEmpty == false) {
        return null;
      }

      if (line.getUnReceivableTotal().compareTo(BigDecimal.ZERO) != 0) {
        return Message.error(
            ReceiptMessages.M.mustBeEquals(field.getCaption(),
                ReceiptMessages.M.accRealReceiveTotal()), field);
      }

      return null;
    }

  }

  private class RemarkRenderer extends EditGridCellWidgetRenderer implements ChangeHandler {
    public RemarkRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private RTextBox field;

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RTextBox(colDef.getFieldDef());
      field.setWidth("100%");
      field.setMaxLength(128);
      field.setEnterToTab(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addChangeHandler(this);
      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentCollectionLine line = lines.get(getRow());
      field.setValue(line.getRemark());
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentCollectionLine line = lines.get(getRow());
      line.setRemark(field.getValue());
    }
  }

  private class BeginDateRenderer extends EditGridCellWidgetRenderer implements ChangeHandler,
      RValidator {
    public BeginDateRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private RDateBox field;

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RDateBox(colDef.getFieldDef());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.setRequired(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.setValidator(this);
      field.addChangeHandler(this);
      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentCollectionLine line = lines.get(getRow());
      field.setValue(line.getBeginDate());
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentCollectionLine line = lines.get(getRow());
      line.setBeginDate(field.getValue());
    }

    @Override
    public Message validate(Widget sender, String value) {
      BPaymentCollectionLine line = lines.get(getRow());

      if (shouldCheckNull(line) && field.getValue() == null) {
        return Message.error(ReceiptMessages.M.notNullField(field.getFieldCaption()), field);
      }

      if (checkEmpty) {
        return null;
      }

      if (field.getValue() != null && line.getEndDate() != null
          && field.getValue().after(line.getEndDate())) {
        return Message.error(ReceiptMessages.M.beginDateAfterEndDate(), field);
      }
      return null;
    }
  }

  private class EndDateRenderer extends EditGridCellWidgetRenderer implements ChangeHandler,
      RValidator {
    public EndDateRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private RDateBox field;

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RDateBox(colDef.getFieldDef());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.setRequired(true);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.setValidator(this);
      field.addChangeHandler(this);
      return field;
    }

    @Override
    public void setValue(Object value) {
      BPaymentCollectionLine line = lines.get(getRow());
      field.setValue(line.getEndDate());
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentCollectionLine line = lines.get(getRow());
      line.setEndDate(field.getValue());
    }

    @Override
    public Message validate(Widget sender, String value) {
      BPaymentCollectionLine line = lines.get(getRow());

      if (shouldCheckNull(line) && field.getValue() == null) {
        return Message.error(ReceiptMessages.M.notNullField(field.getFieldCaption()), field);
      }

      if (field.getValue() != null && line.getBeginDate() != null
          && field.getValue().before(line.getBeginDate())) {
        return Message.error(ReceiptMessages.M.endDateBeforeBeginDate(), field);
      }
      return null;
    }
  }

  /** 需要检查空的条件：checkEmpty=true且应收金额大于0 */
  private boolean shouldCheckNull(BPaymentCollectionLine line) {
    return checkEmpty && line.getUnpayedTotal().getTotal().compareTo(BigDecimal.ZERO) > 0;
  }

  public void setContract(BContract contract) {
    this.contract = contract;
  }
}
