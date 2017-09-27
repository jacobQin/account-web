/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeEviToInvAccountEditPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月21日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.ivc.common.client.ActionName;
import com.hd123.m3.account.gwt.ivc.common.client.CommonConstants;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.common.client.StartNumberBrowserBox;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BStockState;
import com.hd123.m3.account.gwt.ivc.exchange.client.EPInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.InvoiceExchangeMessages;
import com.hd123.m3.account.gwt.ivc.exchange.client.InvoiceExchangeUtil;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BExchangeType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchAccountLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchBalanceLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchangeAccountDetails;
import com.hd123.m3.account.gwt.ivc.exchange.client.rpc.InvoiceExchangeServiceAgent;
import com.hd123.m3.account.gwt.ivc.exchange.client.rpc.InvoiceExchangeServiceAgent.Callback;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd.PInvoiceExchBalanceLineDef;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridCellWidgetRenderer;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * 收据换发票账款明细编辑控件
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeEviToInvAccountEditWidget extends RCaptionBox implements RActionHandler {
  private BInvoiceExchange entity;
  private boolean checkEmpty;

  private HTML totalCount;

  private EditGrid<BInvoiceExchAccountLine> grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef beginEndDateCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef sourceBillCol;
  private RGridColumnDef oldReceiptCodeCol;
  private RGridColumnDef oldReceiptNumberCol;
  private RGridColumnDef newInvoiceCodeCol;
  private RGridColumnDef newInvoiceNumberCol;
  private RGridColumnDef amountCol;
  private RGridColumnDef remarkCol;
  private List<Message> messages = new ArrayList<Message>();

  public void setValue(BInvoiceExchange entity) {
    this.entity = entity;
    if (entity.getExchAccountLines() == null) {
      entity.setExchAccountLines(new ArrayList<BInvoiceExchAccountLine>());
    }
    grid.setDefaultDataRowCount(0);
    grid.setValues(entity.getExchAccountLines());
    invoiceStockBrowserBox.setConditionLimit(entity.getStore(), null);
    invoiceStockBrowserBox.setSort(CommonConstants.SORT_INVOICE);
    invoiceStockBrowserBox.clearValue();
    refreshCounterpartFieldValue();
    refreshTotalCount();

    grid.refresh();
  }

  private void refreshCounterpartFieldValue(){
    counterpartField.setValue(TenantStringUtil.toTenantsString(entity.getExchAccountLines()));
  }
  
  public void clearConditions() {
    invoiceStockBrowserBox.clearConditions();
    invoiceStockBrowserBox.setStoreLimit(null);
  }

  private void refreshTotalCount() {
    totalCount.setHTML(InvoiceCommonMessages.M.totalCount(entity.getExchAccountLines().size()));
  }

  public InvoiceExchangeEviToInvAccountEditWidget() {
    setCaption(InvoiceExchangeMessages.M.accountDetail());
    getCaptionBar().setShowCollapse(true);
    setEditing(true);
    setWidth("100%");
    drawSelf();
  }

  @Override
  public List getInvalidMessages() {
    messages.addAll(super.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    messages.clear();
    checkEmpty = true;
    boolean isValid = grid.validate();
    checkEmpty = false;
    return isValid;
  }

  private StartNumberBrowserBox invoiceStockBrowserBox;
  private RViewStringField counterpartField;

  private Widget drawNumberBrowserBox() {
    RForm numberForm = new RForm();
    numberForm.setWidth("800px");
    numberForm.setCellSpacing(10);

    counterpartField = new RViewStringField(InvoiceCommonMessages.M.tenant());
    numberForm.add(counterpartField);
    
    invoiceStockBrowserBox = new StartNumberBrowserBox(InvoiceExchangeMessages.M.invoiceNumber(),
        BStockState.received.name());
    invoiceStockBrowserBox.addValueChangeHandler(new StockBrowserBoxValueChangeHandler());

    numberForm.addField(invoiceStockBrowserBox);

    return numberForm;
  }

  private class StockBrowserBoxValueChangeHandler implements ValueChangeHandler<String> {
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
      BInvoiceStock value = invoiceStockBrowserBox.getRawValue();
      if (value == null) {
        return;
      }
      for (BInvoiceExchAccountLine line : entity.getExchAccountLines()) {
        line.setNewCode(value.getInvoiceCode());
        line.setNewNumber(value.getInvoiceNumber());
        line.setNewInvoiceType(value.getInvoiceType());
      }
      grid.refresh();
    }

  }

  private void drawSelf() {
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");

    root.add(drawNumberBrowserBox());
    root.add(drawGrid());

    setContent(root);

    totalCount = new HTML(InvoiceCommonMessages.M.totalCount(0));
    getCaptionBar().addButton(totalCount);

  }

  private Widget drawGrid() {
    grid = new EditGrid<BInvoiceExchAccountLine>();
    grid.setWidth("100%");
    grid.setShowCurrentRow(true);
    grid.setRequired(true);
    grid.setCaption(InvoiceExchangeMessages.M.accountDetail());

    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.setDataProducer(provider);

    CellRendererFactory rendererFactory = new CellRendererFactory();

    lineNumberCol = new RGridColumnDef(InvoiceCommonMessages.M.lineNo());
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.subject);
    subjectCol.setWidth("140px");
    grid.addColumnDef(subjectCol);

    beginEndDateCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.beginEndDate);
    beginEndDateCol.setWidth("150px");
    grid.addColumnDef(beginEndDateCol);

    sourceBillTypeCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.sourceBillType);
    sourceBillTypeCol.setWidth("100px");
    grid.addColumnDef(sourceBillTypeCol);

    sourceBillCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.sourceBill);
    sourceBillCol.setWidth("160px");
    sourceBillCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(sourceBillCol);

    oldReceiptCodeCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.oldReceiptCode);
    oldReceiptCodeCol.setWidth("80px");
    grid.addColumnDef(oldReceiptCodeCol);

    oldReceiptNumberCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.oldReceiptNumber);
    oldReceiptNumberCol.setWidth("100px");
    grid.addColumnDef(oldReceiptNumberCol);

    newInvoiceCodeCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.newInvoiceCode);
    newInvoiceCodeCol.setWidth("80px");
    grid.addColumnDef(newInvoiceCodeCol);

    newInvoiceNumberCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.newInvoiceNumber);
    newInvoiceNumberCol.setRendererFactory(rendererFactory);
    newInvoiceNumberCol.setWidth("100px");
    grid.addColumnDef(newInvoiceNumberCol);

    amountCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.amount);
    amountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    amountCol.setWidth("80px");
    grid.addColumnDef(amountCol);

    remarkCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.remark);
    remarkCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);

    return grid;
  }

  private class GridDataProvider implements RGridDataProvider,
      EditGridDataProduce<BInvoiceExchAccountLine> {

    @Override
    public BInvoiceExchAccountLine create() {
      return new BInvoiceExchAccountLine();
    }

    @Override
    public int getRowCount() {
      return entity == null || entity.getExchAccountLines() == null ? 0 : entity
          .getExchAccountLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      BInvoiceExchAccountLine rowData = entity.getExchAccountLines().get(row);
      if (col == lineNumberCol.getIndex()) {
        return row + 1;
      } else if (col == oldReceiptCodeCol.getIndex()) {
        return rowData.getOldCode();
      } else if (col == oldReceiptNumberCol.getIndex()) {
        return rowData.getOldNumber();
      } else if (col == newInvoiceCodeCol.getIndex()) {
        return rowData.getNewCode();
      } else if (col == newInvoiceNumberCol.getIndex()) {
        return rowData.getNewNumber();
      } else if (col == amountCol.getIndex()) {
        return InvoiceExchangeUtil.formatAmount(rowData.getAmount());
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      } else if (col == subjectCol.getIndex()) {
        return rowData.getSubject();
      } else if (col == beginEndDateCol.getIndex()) {
        return InvoiceExchangeUtil.getBeginEndDate(rowData.getAcc1());
      } else if (col == sourceBillCol.getIndex()) {
        return rowData.getSourceBill();
      } else if (col == sourceBillTypeCol.getIndex()) {
        return EPInvoiceExchange.getInstance()
            .getBillTypeCaptionByName(rowData.getSourceBillType());
      }
      return null;
    }
  }

  private class CellRendererFactory implements RCellRendererFactory {
    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        Object data) {
      if (colDef.getName().equals(newInvoiceNumberCol.getName())) {
        return new InvoiceNumberRenderer(grid, colDef, row, col, data);
      } else if (colDef.getName().equals(remarkCol.getName())) {
        return new RemarkTextRenderer(grid, colDef, row, col, data);
      } else if (colDef.getName().equals(sourceBillCol.getName())) {
        return new SourceBillNumberRenderer(grid, colDef, row, col, data);
      }
      return null;
    }
  }

  private class SourceBillNumberRenderer extends EditGridCellWidgetRenderer {
    private SourceBillDispatchLinkField sourceBillNumberField;

    public SourceBillNumberRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col,
        Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      sourceBillNumberField = new SourceBillDispatchLinkField();
      sourceBillNumberField.setLinkKey(GRes.R.dispatch_key());
      return sourceBillNumberField;
    }

    @Override
    public void setValue(Object value) {
      BInvoiceExchAccountLine detail = entity.getExchAccountLines().get(getRow());
      sourceBillNumberField.setValue(detail.getSourceBillType(), detail.getSourceBill());
    }
  }

  private class InvoiceNumberRenderer extends EditGridCellWidgetRenderer {
    private StartNumberBrowserBox field;
    private InvoiceStockFieldHandler fieldHandler;

    public InvoiceNumberRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      fieldHandler = new InvoiceStockFieldHandler();
      field = new StartNumberBrowserBox(InvoiceExchangeMessages.M.newInvoiceNumber(),
          BStockState.received.name());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.setRegex("[0-9]*");
      field.setRegexMessage(InvoiceCommonMessages.M.regexMessage());
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addValueChangeHandler(fieldHandler);
      field.setValidator(fieldHandler);

      field.setConditionLimit(entity.getStore(), null);
      field.setSort(CommonConstants.SORT_INVOICE);

      return field;
    }

    @Override
    public void setValue(Object value) {
      BInvoiceExchAccountLine detail = entity.getExchAccountLines().get(getRow());
      field.setValue(detail.getNewNumber());
    }

    private class InvoiceStockFieldHandler implements ValueChangeHandler<String>, RValidator {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        final BInvoiceExchAccountLine value = entity.getExchAccountLines().get(getRow());
        BInvoiceStock stock = field.getRawValue();
        value.setNewCode(stock.getInvoiceCode());
        value.setNewNumber(stock.getInvoiceNumber());
        value.setNewInvoiceType(stock.getInvoiceType());
        grid.refreshRow(getRow());
      }

      @Override
      public Message validate(Widget sender, String value) {
        if (checkEmpty && StringUtil.isNullOrBlank(value)) {
          return Message.error(
              InvoiceExchangeMessages.M.keyValue(field.getCaption(),
                  InvoiceCommonMessages.M.notNull()), field);
        }
        return null;
      }

    }
  }

  private class RemarkTextRenderer extends EditGridCellWidgetRenderer {
    private RTextBox field;

    public RemarkTextRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RTextBox(colDef.getFieldDef());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addChangeHandler(new RemarkChangeHandler());
      return field;
    }

    private class RemarkChangeHandler implements ChangeHandler {
      @Override
      public void onChange(ChangeEvent event) {
        BInvoiceExchAccountLine value = entity.getExchAccountLines().get(getRow());
        value.setRemark(field.getValue());
      }
    }

    @Override
    public void setValue(Object value) {
      BInvoiceExchAccountLine detail = entity.getExchAccountLines().get(getRow());
      field.setValue(detail.getRemark());
    }
  }

  @Override
  public void onAction(RActionEvent event) {
    if (ActionName.CHANGE_INVOICEDETAILS_ACTION.equals(event.getActionName())
        && BExchangeType.eviToInv.equals(entity.getType())) {
      refreshAccounts();
    }
  }

  /** 刷新账款明细 */
  private void refreshAccounts() {
    List<String> numbers = getInvoiceNumbers();
    InvoiceExchangeServiceAgent.getAccountLinesByInvoiceNumber(numbers, entity.getUuid(),
        new Callback<BInvoiceExchangeAccountDetails>() {
          @Override
          public void execute(BInvoiceExchangeAccountDetails result) {
            entity.setAccountAdjLines(result.getAccountAdjLines());
            entity.setExchAccountLines2(result.getExchAccountLines2());
            refreshShowNewAccountLines(result.getExchAccountLines());
            refreshCounterpartFieldValue();
          }
        });
  }

  private void refreshShowNewAccountLines(List<BInvoiceExchAccountLine> result) {
    GWTUtil.blurActiveElement();
    for (BInvoiceExchAccountLine oldLine : entity.getExchAccountLines()) {
      for (BInvoiceExchAccountLine newLine : result) {
        if (newLine.getOldNumber().equals(oldLine.getOldNumber())
            && newLine.getAcc1().getSubject().equals(oldLine.getAcc1().getSubject())) {
          newLine.setNewCode(oldLine.getNewCode());
          newLine.setNewNumber(oldLine.getNewNumber());
          newLine.setRemark(oldLine.getRemark());
        }
      }
    }
    entity.getExchAccountLines().clear();
    entity.getExchAccountLines().addAll(result);
    refreshTotalCount();
    grid.refresh();
  }

  private List<String> getInvoiceNumbers() {
    List<String> numbers = new ArrayList<String>();
    for (BInvoiceExchBalanceLine line : entity.getExchBalanceLines()) {
      if (StringUtil.isNullOrBlank(line.getOldNumber())) {
        continue;
      }
      numbers.add(line.getOldNumber());
    }
    return numbers;
  }
}
