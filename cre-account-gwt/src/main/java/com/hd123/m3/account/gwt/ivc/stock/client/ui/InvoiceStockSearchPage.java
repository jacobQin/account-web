/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceStockPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月10日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BStockState;
import com.hd123.m3.account.gwt.ivc.stock.client.EPInvoiceStock;
import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.stock.client.biz.BUseType;
import com.hd123.m3.account.gwt.ivc.stock.intf.client.InvoiceStockUrlParams.Search;
import com.hd123.m3.account.gwt.ivc.stock.intf.client.dd.PInvoiceStockDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.flecs.client.DefaultOperandWidgetFactory;
import com.hd123.rumba.gwt.flecs.client.DefaultOperator;
import com.hd123.rumba.gwt.flecs.client.FlecsConfig;
import com.hd123.rumba.gwt.flecs.client.FlecsQueryDef;
import com.hd123.rumba.gwt.flecs.client.Operator;
import com.hd123.rumba.gwt.flecs.client.OperatorPolicy;
import com.hd123.rumba.gwt.flecs.client.RFlecsPanel;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RPagingGrid;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票库存查询页面
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class InvoiceStockSearchPage extends BaseSearchPageForInvoiceStock<BInvoiceStock> implements
    Search {
  public static final String FIELD_BILLNUMBER = "invoiceCode";
  private static InvoiceStockSearchPage instance;

  private RGridColumnDef invoiceTypeCol;
  private RGridColumnDef invoiceCodeCol;
  private RGridColumnDef invoiceNumberCol;
  private RGridColumnDef stateCol;
  private RGridColumnDef remarkCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef holderCol;
  private RGridColumnDef amountCol;

  @Override
  protected EPInvoiceStock getEP() {
    return EPInvoiceStock.getInstance();
  }

  public static InvoiceStockSearchPage getInstance() throws ClientBizException {
    if (instance == null) {
      instance = new InvoiceStockSearchPage();
    }
    return instance;
  }

  @Override
  protected void addCreateButton() {
  }

  @Override
  protected Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);

    List<RGridColumnDef> columns = createGridColumns();
    for (RGridColumnDef column : columns) {
      grid.addColumnDef(column);
    }

    grid.setAllColumnsOverflowEllipsis(true);

    pagingGrid = new RPagingGrid<BInvoiceStock>(grid, new GridDataProvider());
    pagingGrid.setWidth("100%");
    pagingGrid.addLoadDataHandler(this);
    return grid;
  }

  @Override
  protected List<RGridColumnDef> createGridColumns() {
    List<RGridColumnDef> columnDefs = new ArrayList<RGridColumnDef>();

    invoiceTypeCol = new RGridColumnDef(PInvoiceStockDef.invoiceType);
    invoiceTypeCol.setWidth("100px");
    invoiceTypeCol.setSortable(true);
    invoiceTypeCol.setName(FIELD_INVOICE_TYPE);
    columnDefs.add(invoiceTypeCol);

    invoiceCodeCol = new RGridColumnDef(PInvoiceStockDef.invoiceCode);
    invoiceCodeCol.setWidth("100px");
    invoiceCodeCol.setSortable(true);
    invoiceCodeCol.setName(FIELD_INVOICE_CODE);
    columnDefs.add(invoiceCodeCol);

    invoiceNumberCol = new RGridColumnDef(PInvoiceStockDef.invoiceNumber);
    invoiceNumberCol.setRendererFactory(new HyperlinkRendererFactory("120px"));
    invoiceNumberCol.setOverflowEllipsis(true);
    invoiceNumberCol.setWidth("140px");
    invoiceNumberCol.setSortable(true);
    invoiceNumberCol.setName(FIELD_INVOICE_NUNBER);
    columnDefs.add(invoiceNumberCol);

    stateCol = new RGridColumnDef(PInvoiceStockDef.state);
    stateCol.setWidth("80px");
    stateCol.setSortable(true);
    stateCol.setName(FIELD_STATE);
    columnDefs.add(stateCol);

    remarkCol = new RGridColumnDef(PInvoiceStockDef.remark);
    remarkCol.setWidth("150px");
    remarkCol.setSortable(true);
    remarkCol.setName(FIELD_REMARK);
    columnDefs.add(remarkCol);

    accountUnitCol = new RGridColumnDef(PInvoiceStockDef.accountUnit);
    accountUnitCol.setWidth("120px");
    accountUnitCol.setSortable(true);
    accountUnitCol.setName(FIELD_ACCOUNT_UNIT);
    columnDefs.add(accountUnitCol);

    holderCol = new RGridColumnDef(PInvoiceStockDef.holder);
    holderCol.setWidth("120px");
    holderCol.setSortable(true);
    holderCol.setName(FIELD_HOLDER);
    columnDefs.add(holderCol);

    amountCol = new RGridColumnDef(PInvoiceStockDef.amount);
    amountCol.setWidth("120px");
    amountCol.setSortable(true);
    amountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    amountCol.setName(FIELD_AMOUNT);
    columnDefs.add(amountCol);

    return columnDefs;
  }

  @Override
  protected RFlecsPanel drawFlecsPanel() {
    getToolbar().remove(moreButton);
    grid.addClickHandler(new GridClickHandler());
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());

    flecsPanel.addField(PInvoiceStockDef.invoiceCode);
    flecsPanel.addField(PInvoiceStockDef.invoiceNumber);
    flecsPanel.addField(PInvoiceStockDef.accountUnit);
    flecsPanel.addField(PInvoiceStockDef.abortDate);
    flecsPanel.addField(PInvoiceStockDef.state);
    flecsPanel.addField(PInvoiceStockDef.invoiceType);

    flecsPanel.addBeforeLoadDataHandler(new BeforeLoadDataHandler<Integer>() {

      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent<Integer> event) {
        JumpParameters jumpParam = new JumpParameters(getStartNode());
        FlecsConfig flecsConfig = flecsPanel.getCurrentConfig();
        flecsConfig.setShowConditions(flecsPanel.isShowConditions());
        jumpParam.getUrlRef().set(PN_FLECSCONFIG, flecsCodec.encode(flecsConfig));
        jumpParam.getUrlRef().set(PN_PAGE, String.valueOf(event.getParameter()));
        getEP().jump(jumpParam);
        event.cancel();
      }
    });

    getToolbar().add(flecsPanel.getMenuButton(), Alignment.right);
    return flecsPanel;
  }

  @Override
  protected RGridColumnDef getIdentifyColumn() {
    return invoiceNumberCol;
  }

  @Override
  protected RGridColumnDef getOrderColumn() {
    return invoiceCodeCol;
  }

  @Override
  protected void refreshFlecs(JumpParameters params) {
    super.refreshFlecs(params);
  }

  @Override
  protected List<Condition> getDefaultConditions(JumpParameters params) {
    List<Condition> conditions = new ArrayList<Condition>();
    if (StringUtil.isNullOrBlank(getSearchKeyWork(params)) == false) {
      conditions.add(new Condition(PInvoiceStockDef.invoiceNumber, DefaultOperator.STARTS_WITH,
          getSearchKeyWork(params)));
      return conditions;
    }

    conditions.add(new Condition(PInvoiceStockDef.invoiceCode, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceStockDef.invoiceNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceStockDef.accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceStockDef.abortDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceStockDef.state, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceStockDef.invoiceType, DefaultOperator.EQUALS, null));
    return conditions;
  }

  @Override
  protected Object getGridData(int row, int col, BInvoiceStock rowData, List<BInvoiceStock> pageData) {
    if (col == invoiceTypeCol.getIndex()) {
      return rowData.getInvoiceType() == null ? null : getEP().getInvoiceTypes().get(
          rowData.getInvoiceType());
    } else if (col == invoiceCodeCol.getIndex()) {
      return rowData.getInvoiceCode();
    } else if (col == invoiceNumberCol.getIndex()) {
      return rowData.getInvoiceNumber();
    } else if (col == stateCol.getIndex()) {
      return rowData.getState() == null ? null : BStockState.valueOf(rowData.getState())
          .getCaption();
    } else if (col == remarkCol.getIndex()) {
      return rowData.getRemark();
    } else if (col == accountUnitCol.getIndex()) {
      return rowData.getAccountUnit() == null ? null : rowData.getAccountUnit().toFriendlyStr();
    } else if (col == holderCol.getIndex()) {
      return rowData.getHolder() == null ? null : rowData.getHolder().toFriendlyStr();
    } else if (col == amountCol.getIndex()) {
      return rowData.getAmount() == null ? null : M3Format.fmt_money.format(rowData.getAmount());
    }
    return null;
  }

  @Override
  protected BInvoiceStock[] newEntityArray() {
    return new BInvoiceStock[] {};
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {

    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (field == PInvoiceStockDef.accountUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == PInvoiceStockDef.invoiceType) {
        RComboBox<String> invoiceTypeBox = new RComboBox<String>(PInvoiceStockDef.invoiceType);
        invoiceTypeBox.setEditable(false);
        for (Map.Entry<String, String> item : getEP().getInvoiceTypes().entrySet())
          invoiceTypeBox.addOption(item.getKey(), item.getValue());
        return invoiceTypeBox;
      }
      return super.createOperandWidget(field, operator);
    }
  }

  private class MyOperatorPolicy implements OperatorPolicy {
    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (PInvoiceStockDef.state.equals(field) || PInvoiceStockDef.invoiceType.equals(field)) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PInvoiceStockDef.accountUnit.equals(field)) {
        result.add(DefaultOperator.EQUALS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }

  private class GridClickHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null) {
        return;
      }
      BInvoiceStock entity = pagingGrid.getRowData(cell.getRow());

      if (cell.getColumnDef().equals(invoiceNumberCol)) {
        if (BStockState.used.name().equals(entity.getState()) == false&&BStockState.usedRecovered.name().equals(entity.getState()) == false) {
          return;
        }

        if ((BUseType.NORMAL != entity.getUseType()) && (BUseType.EXCHANGING != entity.getUseType())) {
          return;
        }

        if (regDetailsDialog == null) {
          regDetailsDialog = new InvoiceStockRegDetailsDialog();
        }
        regDetailsDialog.show(entity.getInvoiceNumber());

      }

    }

  }

  private InvoiceStockRegDetailsDialog regDetailsDialog;

  private class GridDataProvider implements RPageDataProvider<BInvoiceStock> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BInvoiceStock>> callback) {
      FlecsQueryDef definition = buildQueryDef(page, pageSize, sortField, sortDir);

      beforeQuery(definition);
      getEP().getServiceAgent().query(definition, callback);
    }

    @Override
    public Object getData(int row, int col, BInvoiceStock rowData, List<BInvoiceStock> pageData) {
      return getGridData(row, col, rowData, pageData);
    }
  }
}
