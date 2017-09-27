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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BUsageType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox.Callback;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BBank;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentCollectionLine;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLineCash;
import com.hd123.m3.account.gwt.payment.commons.client.gadget.PaymentTypeComboBox;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCashDefrayalDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentCollectionLineDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentLineCashDef;
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
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.util.client.message.MessageLevel;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
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
 * 代收明细编辑表格（按科目单收款）
 * 
 * @author LiBin
 * 
 */
public class CollectionLineSingleLineEditGrid extends RCaptionBox implements HasRActionHandlers,
    RActionHandler {
  private EPReceipt ep = EPReceipt.getInstance();
  private BPayment payment;
  private BContract contract;
  /** 账款明细行 */
  private List<BPaymentCollectionLine> lines = new ArrayList<BPaymentCollectionLine>();

  private HTML totalCount;
  private RAction addAction;
  private RAction insertAction;
  private RToolbarSplitButton appendButton;
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
  private RGridColumnDef paymentTypeCol;// 收款方式
  private RGridColumnDef payTotalCol;// 收款金额
  private RGridColumnDef bankCol;// 银行
  private RGridColumnDef remarkCol; // 说明

  private boolean checkEmpty;

  public void setPayment(BPayment payment) {
    this.payment = payment;
    this.lines = payment.getCollectionLines();
    if (this.lines.isEmpty()) {
      BPaymentCollectionLine line = new BPaymentCollectionLine();
      line.getCashs().add(getDefaultCash());
      this.lines.add(line);
    }
    grid.setValues(this.lines);
    resetBankOptions();
  }

  public void refresh() {
    grid.refresh();
  }

  public CollectionLineSingleLineEditGrid() {
    super();
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
    setEditing(true);
    setBorder(false);
    setWidth("100%");
    setContent(drawGrid());
    getCaptionBar().setShowCollapse(true);
    setCaption(ReceiptMessages.M.collectionDetail());
  }

  private Widget drawGrid() {
    grid = new EditGrid<BPaymentCollectionLine>();
    grid.setCaption(PPaymentCollectionLineDef.TABLE_CAPTION);
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);

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
    subjectCol.setWidth("220px");
    grid.addColumnDef(subjectCol);

    contractCodeCol = new RGridColumnDef(PPaymentCollectionLineDef.contractCode);
    contractCodeCol.setRendererFactory(rendererFactory);
    contractCodeCol.setOverflowEllipsis(true);
    contractCodeCol.setWidth("150px");
    grid.addColumnDef(contractCodeCol);

    contractNameCol = new RGridColumnDef(PPaymentCollectionLineDef.contractName);
    contractNameCol.setOverflowEllipsis(true);
    contractNameCol.setWidth("100px");
    grid.addColumnDef(contractNameCol);

    beginDateCol = new RGridColumnDef(PPaymentCollectionLineDef.beginTime);
    beginDateCol.setRendererFactory(rendererFactory);
    beginDateCol.setWidth("100px");
    grid.addColumnDef(beginDateCol);

    endDateCol = new RGridColumnDef(PPaymentCollectionLineDef.endTime);
    endDateCol.setRendererFactory(rendererFactory);
    endDateCol.setWidth("100px");
    grid.addColumnDef(endDateCol);

    receivablleTotalCol = new RGridColumnDef(PPaymentCollectionLineDef.receivableTotal);
    receivablleTotalCol.setWidth("80px");
    receivablleTotalCol.setRendererFactory(rendererFactory);
    receivablleTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(receivablleTotalCol);

    realTotalCol = new RGridColumnDef(PPaymentCollectionLineDef.realTotal);
    realTotalCol.setWidth("80px");
    realTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(realTotalCol);

    unreceivableCol = new RGridColumnDef(PPaymentCollectionLineDef.unreceivableTotal);
    unreceivableCol.setWidth("80px");
    unreceivableCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(unreceivableCol);

    paymentTypeCol = new RGridColumnDef(PPaymentLineCashDef.paymentType);
    paymentTypeCol.setWidth("100px");
    paymentTypeCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(paymentTypeCol);

    payTotalCol = new RGridColumnDef(PPaymentLineCashDef.total);
    payTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    payTotalCol.setWidth("100px");
    payTotalCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(payTotalCol);

    bankCol = new RGridColumnDef(PPaymentLineCashDef.bankAccount);
    bankCol.setWidth("100px");
    bankCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(bankCol);

    remarkCol = new RGridColumnDef(PPaymentCollectionLineDef.remark);
    remarkCol.setRendererFactory(rendererFactory);
    remarkCol.setWidth("100px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    grid.setAllowHorizontalScrollBar(true);

    return grid;
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
    Boolean valid = grid.isValid();
    return valid;
  }

  @Override
  public List<Message> getInvalidMessages() {
    return grid.getInvalidMessages();
  }

  @Override
  public boolean validate() {
    checkEmpty = true;
    boolean isValid = grid.validate();
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
    RActionEvent.fire(CollectionLineSingleLineEditGrid.this, ActionName.ACTION_RECEIVABLE_CHANGE);
  }

  private class GridRefreshHandler implements RefreshHandler<RGridCellInfo> {
    @Override
    public void onRefresh(RefreshEvent<RGridCellInfo> event) {
      refreshTotalCount(lines.size());
    }
  }

  private class ActionHandler implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == addAction) {
        BPaymentCollectionLine line = new BPaymentCollectionLine();
        line.getCashs().add(new BPaymentLineCash());
        grid.appendValues(line);
      } else if (event.getSource() == insertAction) {
        int index = grid.getCurrentRow() > 0 ? grid.getCurrentRow() : 0;
        grid.appendValue(index);
      } else if (event.getSource() == deleteAction) {
        grid.deleteSelections();
        fireCashDefrayalEvent();
      }
    }
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

  private void fireCashDefrayalEvent() {
    RActionEvent.fire(CollectionLineSingleLineEditGrid.this,
        ActionName.ACTION_LINEACCOUNTLINE_CASHDEFRAYALCHANGE);
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
      } else if (fieldName.equals(paymentTypeCol.getName())) {
        return new PaymentTypeRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(payTotalCol.getName())) {
        return new PayTotalRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(bankCol.getName())) {
        return new BankRenderer(grid, colDef, row, col, data);
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
      if (shouldCheckNull(line) && (field.getValue() == null || field.getValue().getUuid() == null)) {
        return Message.error(ReceiptMessages.M.notNullField(field.getFieldCaption()), field);
      }
      return null;
    }
  }

  /** 应收金额单元格渲染器 */
  private class ReceivableTotalRenderer extends EditGridCellWidgetRenderer implements ChangeHandler {
    private RNumberBox field;

    public ReceivableTotalRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col,
        Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RNumberBox(colDef.getFieldDef());
      field.setSelectAllOnFocus(true);
      field.setScale(2);
      field.setFormat(GWTFormat.fmt_money);
      field.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
      field.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2, false);
      field.setMinValue(0, true);
      field.setRequired(true);
      field.addChangeHandler(this);
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

  private class PaymentTypeRenderer extends EditGridCellWidgetRenderer implements ChangeHandler {

    private PaymentTypeComboBox paymentTypeField;

    public PaymentTypeRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      List<BUCN> paymentTypes = EPReceipt.getInstance().getPaymentTypes(false);
      paymentTypeField = new PaymentTypeComboBox(paymentTypes);
      paymentTypeField.setValue(paymentTypes.get(0), true);
      paymentTypeField.setEditable(false);
      paymentTypeField.addChangeHandler(this);
      return paymentTypeField;
    }

    @Override
    public void setValue(Object value) {
      BPaymentCollectionLine detail = lines.get(getRow());
      if (detail.getCashs().isEmpty()) {
        detail.getCashs().add(getDefaultCash());
      }
      BPaymentLineCash cash = detail.getCashs().get(0);
      paymentTypeField.setValue(cash.getPaymentType());
      paymentTypeField.clearValidResults();
    }

    public void onChange(ChangeEvent event) {
      BPaymentCollectionLine value = lines.get(getRow());
      value.getCashs().get(0).setPaymentType(paymentTypeField.getValue());
      fireCashDefrayalEvent();
    }

  }

  @Override
  public void onAction(RActionEvent event) {
    if (ActionName.ACTION_GENERALCREATE_REFRESH_BANKS.equals(event.getActionName())) {
      resetBankOptions();
    }
    if (event.getActionName() == ActionName.ACTION_SET_CONTRACT) {
      contract = (BContract) event.getParameters().get(0);
      for (BPaymentCollectionLine line : lines) {
        line.setContract(contract == null ? null : new BUCN(contract.getUuid(), contract
            .getBillNumber(), contract.getTitle()));
      }
      for (int i = 0; i < lines.size(); i++) {
        Widget widget = grid.getWidget(i, 3);
        if (widget instanceof ContractRenderer) {
          ((ContractRenderer) widget).field
              .setAccountUnitUuid(payment.getAccountUnit() == null ? null : payment
                  .getAccountUnit().getUuid());
          ((ContractRenderer) widget).field
              .setCounterpartUuid(payment.getCounterpart() == null ? null : payment
                  .getCounterpart().getUuid());
          if (((ContractRenderer) widget).field.getRawValue() != null)
            ((ContractRenderer) widget).field.getRawValue().getMessages().clear();
          ((ContractRenderer) widget).rawValue = contract;
        }
      }
      grid.refresh();
    }
  }

  /** 项目改变时需要重新改变银行的下拉值 */
  private void resetBankOptions() {
    for (int row = 0; row < grid.getRowCount(); row++) {
      Widget widget = grid.getWidget(row, bankCol.getIndex() + 1);
      if (widget instanceof BankRenderer) {
        BankRenderer bankField = (BankRenderer) widget;
        bankField.resetOptions();
      }
    }
  }

  private BPaymentLineCash getDefaultCash() {
    BPaymentLineCash cashDefrayal = new BPaymentLineCash();
    cashDefrayal.setPaymentType(EPReceipt.getInstance().getDefaultOption().getPaymentType());
    return cashDefrayal;
  }

  private class PayTotalRenderer extends EditGridCellWidgetRenderer implements ChangeHandler,
      RValidator {

    private RNumberBox field;

    public PayTotalRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RNumberBox(colDef.getFieldDef());
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
    public void onChange(ChangeEvent event) {
      BPaymentCollectionLine value = lines.get(getRow());
      if (value.getCashs().isEmpty()) {
        value.getCashs().add(new BPaymentLineCash());
      }
      BPaymentLineCash cash = value.getCashs().get(0);
      cash.setTotal(field.getValueAsBigDecimal());
      value.getTotal().setTotal(field.getValueAsBigDecimal());
      value.setDefrayalTotal(field.getValueAsBigDecimal());
      grid.refreshRow(getRow());
      fireCashDefrayalEvent();
    }

    @Override
    public void setValue(Object value) {
      BPaymentCollectionLine detail = lines.get(getRow());
      field.setValue(detail.getCashs().get(0).getTotal());
    }

    @Override
    public Message validate(Widget sender, String value) {
      BPaymentCollectionLine line = lines.get(getRow());
      BigDecimal cashTotal = line.getCashs().get(0).getTotal();
      if (cashTotal == null) {
        return Message.error(ReceiptMessages.M.notNull(field.getCaption()), field);
      }

      BigDecimal upayTotal = line.getUnpayedTotal().getTotal();
      if (upayTotal == null) {
        return null;
      }
      if (shouldCheckNull(line) && upayTotal.compareTo(cashTotal) != 0) {
        return Message.error(
            ReceiptMessages.M.mustBeEquals(field.getCaption(),
                ReceiptMessages.M.shouldReceiveTotal()), field);
      }
      return null;
    }
  }

  private class BankRenderer extends EditGridCellWidgetRenderer implements ChangeHandler,
      RValidator {
    private RComboBox<BBank> field;

    public BankRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RComboBox(PPaymentCashDefrayalDef.constants.bankCode());
      field.setNullOptionTextToDefault();
      field.setNullOptionText("[空]");
      field.setMaxDropdownRowCount(10);
      field.setEditable(false);
      field.setValidator(this);
      field.addChangeHandler(this);
      resetOptions();
      return field;
    }

    private void resetOptions() {
      BBank value = field.getValue();
      List<BBank> results = new ArrayList<BBank>();
      field.clearOptions();
      if (payment.getAccountUnit() == null || payment.getAccountUnit().getUuid() == null) {
        return;
      }
      for (BBank bank : EPReceipt.getInstance().getBanks()) {
        if (bank.getStore() == null) {
          continue;
        }
        if (payment.getAccountUnit().getUuid().equals(bank.getStore().getUuid())) {
          results.add(bank);
        }
      }
      field.addOptionList(results);

      if (value != null) {
        field.setValue(value);
      }
    }

    @Override
    public void setValue(Object value) {
      BPaymentCollectionLine detail = lines.get(getRow());
      field.setValue(detail.getCashs().get(0).getBank());
    }

    @Override
    public void onChange(ChangeEvent event) {
      BPaymentCollectionLine value = lines.get(getRow());
      if (value.getCashs().isEmpty()) {
        value.getCashs().add(new BPaymentLineCash());
      }
      BPaymentLineCash cash = value.getCashs().get(0);
      cash.setBank(field.getValue());
      fireCashDefrayalEvent();
    }

    @Override
    public Message validate(Widget sender, String value) {
      BPaymentCollectionLine line = lines.get(getRow());
      BPaymentLineCash cash = line.getCashs().get(0);

      if (shouldCheckNull(line) == false || ep.getConfig().isBankRequired() == false) {
        return null;
      }

      if (cash.getBank() == null) {
        return Message.error(ReceiptMessages.M.notNull(field.getCaption()), field);
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
      field.setAccountUnitUuid(payment == null || payment.getAccountUnit() == null ? null : payment
          .getAccountUnit().getUuid());
      field.setCounterpartUuid(payment == null || payment.getCounterpart() == null ? null : payment
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

  public void setContract(BContract contract) {
    this.contract = contract;
  }
}
