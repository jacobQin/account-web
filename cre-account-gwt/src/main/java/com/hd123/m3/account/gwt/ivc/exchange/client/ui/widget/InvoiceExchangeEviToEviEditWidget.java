/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeInvToInvEditWidget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月19日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.widget;

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
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.common.client.ActionName;
import com.hd123.m3.account.gwt.ivc.common.client.CommonConstants;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.common.client.StartNumberBrowserBox;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BStockState;
import com.hd123.m3.account.gwt.ivc.exchange.client.InvoiceExchangeMessages;
import com.hd123.m3.account.gwt.ivc.exchange.client.InvoiceExchangeUtil;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchBalanceLine;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd.PInvoiceExchBalanceLineDef;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridCellWidgetRenderer;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.hotkey.RHotKey;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSplitButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * 收据换收据（发票）明细编辑控件
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeEviToEviEditWidget extends RCaptionBox implements HasRActionHandlers {
  private BInvoiceExchange entity;
  private ClickActionHandler clickHandler = new ClickActionHandler();

  private HTML totalCount;
  private RAction appendAction;
  private RAction insertAction;
  private RAction deleteAction;

  private EditGrid<BInvoiceExchBalanceLine> grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef receiptCodeCol;
  private RGridColumnDef receiptNumberCol;
  private RGridColumnDef amountCol;
  private RGridColumnDef remarkCol;
  private List<Message> messages = new ArrayList<Message>();

  public void setValue(BInvoiceExchange entity) {
    this.entity = entity;
    if (entity.getExchBalanceLines() == null) {
      entity.setExchBalanceLines(new ArrayList<BInvoiceExchBalanceLine>());
    }
    grid.setValues(entity.getExchBalanceLines());

    totalCount.setHTML(InvoiceCommonMessages.M.totalCount(entity.getExchBalanceLines().size()));

    grid.refresh();
  }

  public InvoiceExchangeEviToEviEditWidget() {
    setCaption(InvoiceExchangeMessages.M.invoiceDetail());
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
    boolean isValid = grid.validate();
    return isValid;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  private void drawSelf() {
    drawGrid();

    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.add(grid);
    setContent(root);

    totalCount = new HTML(InvoiceCommonMessages.M.totalCount(0));
    getCaptionBar().addButton(totalCount);

    RPopupMenu addMenu = new RPopupMenu();

    appendAction = new RAction(RActionFacade.APPEND, clickHandler);
    appendAction.setCaption(InvoiceCommonMessages.M.append());
    appendAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(appendAction));

    insertAction = new RAction(InvoiceCommonMessages.M.insertBefore(), clickHandler);
    insertAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(insertAction));

    RToolbarSplitButton appendButton = new RToolbarSplitButton(appendAction);
    appendButton.setMenu(addMenu);
    appendButton.setShowText(false);
    getCaptionBar().addButton(appendButton);

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    deleteAction.clearHotKey();
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);

  }

  private void drawGrid() {
    grid = new EditGrid<BInvoiceExchBalanceLine>();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setRequired(true);
    grid.setCaption(InvoiceExchangeMessages.M.invoiceDetail());

    grid.addActionHandler(new GridActionHandler());

    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.setDataProducer(provider);

    CellRendererFactory rendererFactory = new CellRendererFactory();
    lineNumberCol = new RGridColumnDef(InvoiceCommonMessages.M.lineNo());
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    receiptCodeCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.oldReceiptCode);
    receiptCodeCol.setWidth("180px");
    grid.addColumnDef(receiptCodeCol);

    receiptNumberCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.oldReceiptNumber);
    receiptNumberCol.setRendererFactory(rendererFactory);
    receiptNumberCol.setWidth("180px");
    grid.addColumnDef(receiptNumberCol);

    amountCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.amount);
    amountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    amountCol.setWidth("120px");
    grid.addColumnDef(amountCol);

    remarkCol = new RGridColumnDef(PInvoiceExchBalanceLineDef.remark);
    remarkCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  private void fireInvoiceDetailsChangeAction() {
    RActionEvent.fire(InvoiceExchangeEviToEviEditWidget.this,
        ActionName.CHANGE_INVOICEDETAILS_ACTION, entity.getExchAccountLines());
  }

  private class GridActionHandler implements RActionHandler {
    @Override
    public void onAction(RActionEvent event) {
      if (EditGrid.ACTION_APPEND.equals(event.getActionName())
          || EditGrid.ACTION_REMOVE.equals(event.getActionName())) {
        totalCount.setHTML(InvoiceCommonMessages.M.totalCount(entity == null ? 0 : entity
            .getExchBalanceLines().size()));
      }
      if (EditGrid.ACTION_REMOVE.equals(event.getActionName())) {
        fireInvoiceDetailsChangeAction();
      }
    }
  }

  private class GridDataProvider implements RGridDataProvider,
      EditGridDataProduce<BInvoiceExchBalanceLine> {

    @Override
    public BInvoiceExchBalanceLine create() {
      BInvoiceExchBalanceLine detail = new BInvoiceExchBalanceLine();
      return detail;
    }

    @Override
    public int getRowCount() {
      return entity == null || entity.getExchBalanceLines() == null ? 0 : entity
          .getExchBalanceLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      BInvoiceExchBalanceLine rowData = entity.getExchBalanceLines().get(row);
      if (col == lineNumberCol.getIndex()) {
        return row + 1;
      } else if (col == receiptCodeCol.getIndex()) {
        return rowData.getOldCode();
      } else if (col == receiptNumberCol.getIndex()) {
        return rowData.getOldNumber();
      } else if (col == amountCol.getIndex()) {
        return InvoiceExchangeUtil.formatAmount(rowData.getAmount());
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      }
      return null;
    }
  }

  private class CellRendererFactory implements RCellRendererFactory {

    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        Object data) {
      String fieldName = colDef.getName();

      if (fieldName.equals(receiptNumberCol.getName())) {
        return new InvoiceNumberRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(remarkCol.getName())) {
        return new RemarkTextRenderer(grid, colDef, row, col, data);
      }
      return null;
    }
  }

  private class InvoiceNumberRenderer extends EditGridCellWidgetRenderer implements RValidator {
    private StartNumberBrowserBox field;
    private InvoiceStockFieldHandler fieldHandler;

    public InvoiceNumberRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      fieldHandler = new InvoiceStockFieldHandler();
      field = new StartNumberBrowserBox(InvoiceExchangeMessages.M.oldReceiptNumber(),
          BStockState.used.name());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.setRegex("[0-9]*");
      field.setRegexMessage(InvoiceCommonMessages.M.regexMessage());
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addValueChangeHandler(fieldHandler);
      field.addChangeHandler(fieldHandler);
      field.setValidator(this);

      field.setConditionLimit(entity.getStore(), null);
      field.setSort(CommonConstants.SORT_RECEIPT);

      field.setUseTypes(CommonConstants.UseType.NORMAL, CommonConstants.UseType.EXCHANGING);

      return field;
    }

    @Override
    public void setValue(Object value) {
      BInvoiceExchBalanceLine detail = entity.getExchBalanceLines().get(getRow());
      field.setValue(detail.getOldNumber());
    }

    private class InvoiceStockFieldHandler implements ValueChangeHandler<String>, ChangeHandler {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        BInvoiceExchBalanceLine value = entity.getExchBalanceLines().get(getRow());
        BInvoiceStock stock = field.getRawValue();

        if (StringUtil.isNullOrBlank(field.getValue())) {
          value.setOldNumber(null);
          value.setOldCode(null);
          value.setAmount(null);
          grid.refreshRow(getRow());
          fireInvoiceDetailsChangeAction();
          return;
        }
        if (stock.getInvoiceNumber() != null
            && stock.getInvoiceNumber().equals(value.getOldNumber())) {
          return;
        }

        if (containsCount((stock)) > 1) {
          return;
        }
        value.setOldNumber(stock.getInvoiceNumber());
        value.setOldCode(stock.getInvoiceCode());
        value.setAmount(stock.getAmount());
        grid.refreshRow(getRow());
        fireInvoiceDetailsChangeAction();
      }

      @Override
      public void onChange(ChangeEvent event) {
        BInvoiceExchBalanceLine value = entity.getExchBalanceLines().get(getRow());
        value.setOldNumber(field.getValue());
      }

    }

    @Override
    public Message validate(Widget sender, String value) {
      BInvoiceStock stock = field.getRawValue();

      if (StringUtil.isNullOrBlank(field.getValue())) {
        return null;
      }

      if (containsCount(stock) > 1) {
        return Message.error("已经存在相同代码", field);
      }
      return null;
    }
  }

  public int containsCount(BInvoiceStock stock) {
    if (stock == null || StringUtil.isNullOrBlank(stock.getInvoiceNumber())) {
      return 0;
    }

    int count = 0;

    for (BInvoiceExchBalanceLine line : entity.getExchBalanceLines()) {
      if (stock.getInvoiceNumber().equals(line.getOldNumber())) {
        count++;
      }
    }

    return count;
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
        BInvoiceExchBalanceLine value = entity.getExchBalanceLines().get(getRow());
        value.setRemark(field.getValue());
      }
    }

    @Override
    public void setValue(Object value) {
      BInvoiceExchBalanceLine detail = entity.getExchBalanceLines().get(getRow());
      field.setValue(detail.getRemark());
    }
  }

  private class ClickActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (appendAction == event.getSource()) {
        grid.appendValue(entity.getExchBalanceLines().size());
      } else if (event.getSource() == insertAction) {
        int index = grid.getCurrentRow() > 0 ? grid.getCurrentRow() : 0;
        grid.appendValue(index);
      } else if (deleteAction == event.getSource()) {
        grid.deleteSelections();
        grid.refresh();
      }
    }
  }
}