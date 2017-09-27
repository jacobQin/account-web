/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiptSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.ui.page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentLogger;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentOverdueLineDef;
import com.hd123.m3.account.gwt.payment.rec.client.EPReceipt;
import com.hd123.m3.account.gwt.payment.rec.client.ReceiptMessages;
import com.hd123.m3.account.gwt.payment.rec.client.rpc.ReceiptService;
import com.hd123.m3.account.gwt.payment.rec.client.ui.gadget.ReceiptConfigDialog;
import com.hd123.m3.account.gwt.payment.rec.intf.client.ReceiptUrlParams;
import com.hd123.m3.account.gwt.payment.rec.intf.client.ReceiptUrlParams.Search;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.biz.IsStandardBill;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmSearchPage;
import com.hd123.m3.commons.gwt.util.client.dialog.InputBox;
import com.hd123.m3.commons.gwt.util.client.flecs.BUCNCodec;
import com.hd123.m3.commons.gwt.util.client.grid.batch.PagingGridBatchProcessCallback;
import com.hd123.m3.commons.gwt.util.client.grid.batch.PagingGridBatchProcessor;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.widget.client.biz.PrintingTemplate;
import com.hd123.m3.commons.gwt.widget.client.ui.print.BeforePrintCallback;
import com.hd123.m3.commons.gwt.widget.client.ui.print.BeforePrintHandler;
import com.hd123.m3.commons.gwt.widget.client.ui.print.PrintButton;
import com.hd123.rumba.commons.gwt.mini.client.codec.CodecUtils;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.flecs.client.DefaultOperandWidgetFactory;
import com.hd123.rumba.gwt.flecs.client.DefaultOperator;
import com.hd123.rumba.gwt.flecs.client.FlecsConfig;
import com.hd123.rumba.gwt.flecs.client.Operator;
import com.hd123.rumba.gwt.flecs.client.OperatorPolicy;
import com.hd123.rumba.gwt.flecs.client.RFlecsPanel;
import com.hd123.rumba.gwt.flecs.client.codec.DefaultOperatorCodec;
import com.hd123.rumba.gwt.flecs.client.codec.FlecsConfigCodec;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.batchprocess.BatchProcesser;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RPagingGrid;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RDateTimeRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarMenuButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author subinzhu
 * 
 */
public class ReceiptSearchPage extends BaseBpmSearchPage implements Search, BeforePrintHandler {

  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef(FIELD_COUNTERPART, EPReceipt
      .getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef(FIELD_ACCOUNTUNIT, EPReceipt
      .getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
  public static DateFieldDef receiptDate = new DateFieldDef("receiptDate",
      PPaymentDef.constants.paymentDate(), false, null, true, null, true, GWTFormat.fmt_yMd, false);
  public static EmbeddedFieldDef permGroup = new EmbeddedFieldDef(FIELD_PERMGROUP,
      ReceiptMessages.M.permGroup());
  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(
      ReceiptUrlParams.Flecs.FIELD_COUNTERPARTTYPE, EPReceipt.getInstance().getFieldCaption(
          GRes.FIELDNAME_COUNTERPART_TYPE, GRes.R.counterpartType()));

  private EPReceipt ep = EPReceipt.getInstance();
  private static ReceiptSearchPage instance;

  public static ReceiptSearchPage getInstance() {
    if (instance == null) {
      instance = new ReceiptSearchPage();
    }
    return instance;
  }

  // flecs搜索
  private JumpParameters params;
  private String keyword;
  private RFlecsPanel flecsPanel;
  private FlecsConfigCodec flecsCodec;
  private PageSort pageSort;
  // 工具栏
  private RToolbarMenuButton moreButton;
  private RAction effectAction;
  private RAction deleteAction;
  private RAction abortAction;
  private RAction configAction;
  protected PrintButton printButton = null;
  // 结果表格
  private RGrid grid;
  private RPagingGrid pagingGrid;
  private PagingGridBatchProcessor<BPayment> batchProcessor;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef stateCol;
  private RGridColumnDef counterpartUnitCol;
  private RGridColumnDef contractNameCol;
  private RGridColumnDef paymentDatetCol;
  private RGridColumnDef unpayedTotalCol;
  // private RGridColumnDef totalCol;
  private RGridColumnDef defrayalTotalCol;
  private RGridColumnDef dealerCol;
  private RGridColumnDef settleNoCol;
  private RGridColumnDef remarkCol;
  // 下拉列表
  private RMenuItem editOneMenuItem;
  private RMenuItem effectOneMenuItem;
  private RMenuItem deleteOneMenuItem;
  private RMenuItem abortOneMenuItem;

  private Handler_click clickHandler = new Handler_click();
  private ReceiptConfigDialog configDialog;

  public ReceiptSearchPage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());
    getToolbar().addSeparator();

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    effectAction = new RAction(ReceiptMessages.M.effect(), clickHandler);
    abortAction = new RAction(ReceiptMessages.M.abort() + "...", clickHandler);

    moreButton = new RToolbarMenuButton(ReceiptMessages.M.operate(), moreMenu);
    getToolbar().add(moreButton);

    configAction = new RAction(RActionFacade.OPTION, clickHandler);
    RToolbarButton configButton = new RToolbarButton(configAction);
    configButton.setShowText(false);
    getToolbar().addToRight(configButton);

    // 打印
    drawPrintButton();
  }

  // 构造打印按钮， 需要在构造flecs panel之前调用
  protected void drawPrintButton() {
    getToolbar().addToRight(new RToolbarSeparator());
    printButton = new PrintButton(getEP().getPrintTemplate(), getEP().getCurrentUser().getId());
    printButton.setBeforePrintHandler(this);
    getToolbar().add(printButton, Alignment.right);
    getToolbar().addSeparator(Alignment.right);
    // 刷新模板
    printButton.refresh();
  }

  private void drawSelf() {
    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    initWidget(root);

    root.add(drawFlecsAndGrid());
  }

  private Widget drawFlecsAndGrid() {
    drawLineMenu();
    drawGrid();
    pagingGrid = new RPagingGrid<BPayment>(grid, new GridDataProvider());
    pagingGrid.addLoadDataHandler(this);
    drawFlecsPanel();

    return flecsPanel;
  }

  private void drawLineMenu() {
    editOneMenuItem = new RMenuItem(RActionFacade.EDIT, clickHandler);
    editOneMenuItem.setHotKey(null);

    deleteOneMenuItem = new RMenuItem(RActionFacade.DELETE, clickHandler);
    deleteOneMenuItem.setHotKey(null);

    effectOneMenuItem = new RMenuItem(ReceiptMessages.M.effect(), clickHandler);
    effectOneMenuItem.setHotKey(null);

    abortOneMenuItem = new RMenuItem(ReceiptMessages.M.abort() + "...", clickHandler);
    abortOneMenuItem.setHotKey(null);
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.addClickHandler(new Handler_grid());
    grid.addSelectionHandler(new SelectionHandler<Integer>() {

      @Override
      public void onSelection(SelectionEvent<Integer> event) {
        if (printButton == null) {
          return;
        }
        Set<String> storeCodes = new HashSet<String>();
        for (Object o : pagingGrid.getSelections()) {
          if (o instanceof BPayment
              && StringUtil.isNullOrBlank(((BPayment) o).getStore().getUuid()) == false) {
            storeCodes.add(((BPayment) o).getStore().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    billNumberCol = new RGridColumnDef(PPaymentDef.billNumber);
    billNumberCol.setWidth("180px");
    billNumberCol.setName(FIELD_BILLNUMBER);
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setSortable(true);
    billNumberCol.setResizable(true);
    billNumberCol.setAllowHiding(false);
    grid.addColumnDef(billNumberCol);

    stateCol = new RGridColumnDef(PPaymentDef.bizState);
    stateCol.setWidth("80px");
    stateCol.setName(FIELD_BIZSTATE);
    stateCol.setSortable(true);
    stateCol.setResizable(true);
    grid.addColumnDef(stateCol);

    counterpartUnitCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    counterpartUnitCol.setWidth("160px");
    counterpartUnitCol.setSortable(true);
    counterpartUnitCol.setResizable(true);
    counterpartUnitCol.setName(FIELD_COUNTERPART);
    grid.addColumnDef(counterpartUnitCol);

    contractNameCol = new RGridColumnDef(PPaymentOverdueLineDef.contract_name);
    contractNameCol.setWidth("160px");
    contractNameCol.setResizable(true);
    contractNameCol.setName(FIELD_CONTRACT_NAME);
    grid.addColumnDef(contractNameCol);

    paymentDatetCol = new RGridColumnDef(PPaymentDef.paymentDate);
    paymentDatetCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    paymentDatetCol.setWidth("80px");
    paymentDatetCol.setName(FIELD_RECEIPTDATE);
    paymentDatetCol.setSortable(true);
    paymentDatetCol.setResizable(true);
    grid.addColumnDef(paymentDatetCol);

    unpayedTotalCol = new RGridColumnDef(PPaymentDef.unpayedTotal_total);
    unpayedTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unpayedTotalCol.setWidth("120px");
    unpayedTotalCol.setSortable(true);
    unpayedTotalCol.setResizable(true);
    unpayedTotalCol.setName(FIELD_UNPAYEDTOTAL);
    grid.addColumnDef(unpayedTotalCol);

    /*
     * totalCol = new RGridColumnDef(PPaymentDef.total_total);
     * totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
     * totalCol.setWidth("120px"); totalCol.setSortable(true);
     * totalCol.setResizable(true); totalCol.setName(FIELD_TOTAL);
     * grid.addColumnDef(totalCol);
     */
    defrayalTotalCol = new RGridColumnDef(PPaymentDef.defrayalTotal);
    defrayalTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    defrayalTotalCol.setWidth("120px");
    defrayalTotalCol.setSortable(true);
    defrayalTotalCol.setResizable(true);
    defrayalTotalCol.setName(FIELD_DEFRAYALTOTAL);
    grid.addColumnDef(defrayalTotalCol);

    dealerCol = new RGridColumnDef(PPaymentDef.dealer);
    dealerCol.setWidth("100px");
    dealerCol.setResizable(true);
    grid.addColumnDef(dealerCol);

    settleNoCol = new RGridColumnDef(PPaymentDef.settleNo);
    settleNoCol.setWidth("100px");
    settleNoCol.setSortable(true);
    settleNoCol.setResizable(true);
    settleNoCol.setName(FIELD_SETTLENO);
    grid.addColumnDef(settleNoCol);

    remarkCol = new RGridColumnDef(PPaymentDef.remark);
    remarkCol.setSortable(true);
    remarkCol.setName(FIELD_REMARK);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private void drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setWidth("100%");
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());

    flecsPanel.addField(PPaymentDef.billNumber);
    flecsPanel.addField(PPaymentDef.bizState);
    flecsPanel.addField(PPaymentOverdueLineDef.contract_billNumber);
    flecsPanel.addField(accountUnit);
    flecsPanel.addField(counterpartType);
    flecsPanel.addField(counterpart);
    flecsPanel.addField(receiptDate);
    flecsPanel.addField(PPaymentDef.settleNo);
    flecsPanel.addField(PPaymentDef.coopMode);
    flecsPanel.addField(PPaymentOverdueLineDef.contract_name);
    if (ep.isPermEnabled()) {
      flecsPanel.addField(permGroup);
    }
    flecsPanel.addField(PPaymentDef.createInfo_operator_id);
    flecsPanel.addField(PPaymentDef.createInfo_operator_fullName);
    flecsPanel.addField(PPaymentDef.createInfo_time);
    flecsPanel.addField(PPaymentDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PPaymentDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PPaymentDef.lastModifyInfo_time);
    flecsPanel.addBeforeLoadDataHandler(new BeforeLoadDataHandler<Integer>() {

      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent<Integer> event) {
        JumpParameters jumParams = new JumpParameters(START_NODE);
        FlecsConfig curConfig = flecsPanel.getCurrentConfig();
        curConfig.setShowConditions(flecsPanel.isShowConditions());
        jumParams.getUrlRef().set(PN_FLECSCONFIG, flecsCodec.encode(curConfig));
        jumParams.getUrlRef().set(PN_PAGE, String.valueOf(event.getParameter()));
        ep.jump(jumParams);
        event.cancel();
      }
    });

    getToolbar().add(flecsPanel.getMenuButton(), Alignment.right);
  }

  private void afterDraw() {
    batchProcessor = new PagingGridBatchProcessor<BPayment>(pagingGrid, ep.getModuleCaption());
    flecsCodec = new FlecsConfigCodec();
    flecsCodec.addOperatorCodec(new DefaultOperatorCodec());
    flecsCodec.addOperandCodec(new BUCNCodec());
  }

  /** 定制操作数 */
  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (counterpart == field) {
        return DefaultOperator.EQUALS == operator ? buildCounterpart() : null;
      } else if (PPaymentDef.settleNo == field) {
        SettleNoField settleNoField = new SettleNoField(PPaymentDef.settleNo);
        settleNoField.refreshOption(12);
        settleNoField.setMaxLength(6);
        return settleNoField;
      } else if (accountUnit == field) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (permGroup == field) {
        return new PermGroupField(permGroup);
      } else if (counterpartType == field) {
        return new CounterpartTypeField(counterpartType);
      } else if (field == PPaymentDef.coopMode) {
        RComboBox<String> coopModeField = new RComboBox<String>();
        coopModeField.setMaxDropdownRowCount(15);
        coopModeField.setEditable(false);
        for (String coopMode : ep.getCoopModes()) {
          coopModeField.addOption(coopMode);
        }
        return coopModeField;
      }
      return super.createOperandWidget(field, operator);
    }
  }

  private CounterpartUCNBox buildCounterpart() {
    CounterpartUCNBox counterpart = new CounterpartUCNBox(null, true, getEP().getCaptionMap());
    counterpart.setCounterTypeMap(getEP().getCounterpartTypeMap());
    return counterpart;
  }

  private class CounterpartTypeField extends RComboBox<String> {

    public CounterpartTypeField(FieldDef fieldDef) {
      super();
      setFieldDef(fieldDef);
      setMaxDropdownRowCount(10);
      setNullOptionText(ReceiptMessages.M.all());
      setEditable(false);
      for (Entry<String, String> entry : getEP().getCounterpartTypeMap().entrySet()) {
        this.addOption(entry.getKey(), entry.getValue());
      }
    }
  }

  private class PermGroupField extends RComboBox<String> {

    public PermGroupField(FieldDef fieldDef) {
      super();
      setFieldDef(fieldDef);
      setMaxDropdownRowCount(10);
      setEditable(false);
      for (Entry<String, String> entry : ep.getUserGroups().entrySet()) {
        this.addOption(entry.getKey(), entry.getValue());
      }
    }
  }

  /** 定制操作符 */
  private class MyOperatorPolicy implements OperatorPolicy {
    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (PPaymentDef.settleNo == field || counterpart == field || accountUnit == field
          || counterpartType == field) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (permGroup == field || PPaymentDef.coopMode.equals(field)) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }

  private class GridDataProvider implements RPageDataProvider<BPayment> {
    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BPayment>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);
      if (sortField != null) {
        pageSort.appendOrder(sortField, sortDir);
      } else {
        pageSort.appendOrder(FIELD_BILLNUMBER, OrderDir.desc);
      }

      ReceiptService.Locator.getService().query(flecsPanel.getCurrentConfig().getConditions(),
          pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BPayment rowData, List<BPayment> pageData) {
      if (col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      else if (col == stateCol.getIndex())
        return PPaymentDef.finishBizState.getEnumCaption(rowData.getBizState());
      else if (col == counterpartUnitCol.getIndex())
        return rowData.getCounterpart().toFriendlyStr(getEP().getCounterpartTypeMap());
      else if (col == contractNameCol.getIndex())
        return rowData.getContractNameToStr();
      else if (col == paymentDatetCol.getIndex())
        return rowData.getPaymentDate();
      else if (col == unpayedTotalCol.getIndex()) {
        BigDecimal unpayedTotal = (rowData.getUnpayedTotal() == null || rowData.getUnpayedTotal()
            .getTotal() == null) ? BigDecimal.ZERO : rowData.getUnpayedTotal().getTotal();
        return M3Format.fmt_money.format(unpayedTotal.doubleValue());
        /*
         * } else if (col == totalCol.getIndex()) { BigDecimal total =
         * (rowData.getTotal() == null || rowData.getTotal().getTotal() == null)
         * ? BigDecimal.ZERO : rowData.getTotal().getTotal(); return
         * M3Format.fmt_money.format(total.doubleValue());
         */
      } else if (col == defrayalTotalCol.getIndex()) {
        BigDecimal defrayalTotal = rowData.getDefrayalTotal() == null ? BigDecimal.ZERO : rowData
            .getDefrayalTotal();
        return M3Format.fmt_money.format(defrayalTotal.doubleValue());
      } else if (col == dealerCol.getIndex()) {
        return rowData.getDealer() == null ? null : rowData.getDealer().toFriendlyStr();
      } else if (col == settleNoCol.getIndex()) {
        return rowData.getSettleNo();
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      }
      return null;
    }
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);
    this.params = params;

    if (checkIn() == false)
      return;

    refreshTitle();
    decodeParams(params);
    refreshFlecs();
    refreshCommonds();
  }

  private void refreshCommonds() {
    deleteAction.setVisible(ep.isPermitted(ReceiptPermDef.DELETE));
    effectAction.setVisible(ep.isPermitted(ReceiptPermDef.EFFECT));
    abortAction.setVisible(ep.isPermitted(ReceiptPermDef.ABORT));
    configAction.setVisible(ep.isPermitted(ReceiptPermDef.CONFIG));
    printButton.setEnabled(ep.isPermitted(BAction.PRINT.getKey()));

    boolean visible = false;
    for (Widget item : moreMenu.getItems()) {
      if (item.isVisible()) {
        visible = true;
        break;
      }
    }
    if (visible == false) {
      visible = deleteAction.isVisible() || effectAction.isVisible() || abortAction.isVisible();
    }
    moreButton.setVisible(visible);
    getToolbar().rebuild();
  }

  public JumpParameters getLastParams() {
    if (params != null)
      params.getMessages().clear();
    return params;
  }

  private boolean checkIn() {
    if (ep.isPermitted(ReceiptPermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ReceiptMessages.M.search());
    ep.getTitleBar().appendAttributeText(ep.getModuleCaption());
  }

  private void decodeParams(JumpParameters params) {
    pageSort = new PageSort();
    keyword = params.getUrlRef().get(PN_KEYWORD);
  }

  private void refreshFlecs() {
    grid.setSort(billNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel.addCondition(new Condition(PPaymentDef.billNumber, DefaultOperator.STARTS_WITH,
          keyword));
      flecsPanel.setShowConditions(true);
      flecsPanel.refresh();
    } else {
      String flecsStr = params.getUrlRef().get(PN_FLECSCONFIG);
      String pageInt = params.getUrlRef().get(PN_PAGE);
      int startPage = CodecUtils.decodeInt(pageInt, 0);
      if (flecsStr != null) {
        final FlecsConfig fc = flecsCodec.decode(flecsStr);
        flecsPanel.setCurrentConfig(fc, startPage);
      } else if (flecsPanel.getDefaultConfig() == null) {
        flecsPanel.addConditions(getDefaultConditions());
        flecsPanel.refresh();
      } else {
        flecsPanel.setCurrentConfig(flecsPanel.getDefaultConfig());
      }
    }
  }

  private List<Condition> getDefaultConditions() {
    List<Condition> conditions = new ArrayList<Condition>();
    conditions.add(new Condition(PPaymentDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PPaymentDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PPaymentOverdueLineDef.contract_billNumber,
        DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PPaymentOverdueLineDef.contract_name, DefaultOperator.CONTAINS,
        null));
    conditions.add(new Condition(accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpart, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(receiptDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PPaymentDef.settleNo, DefaultOperator.EQUALS, SettleNoField
        .getCurrentSettleNo()));
    return conditions;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    getEP().clearConditions();
  }

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      RGridColumnDef colDef = cell.getColumnDef();
      int row = cell.getRow();

      BPayment bill = (BPayment) pagingGrid.getPageData().get(row);
      if (bill == null)
        return;
      if (colDef.equals(billNumberCol)) {
        ep.jumpToViewPage(bill.getUuid());
      }
    }
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(deleteAction)) {
        doDelete();
      } else if (event.getSource().equals(effectAction)) {
        doEffect();
      } else if (event.getSource().equals(abortAction)) {
        doAbortConfirm();
      } else if (event.getSource().equals(configAction)) {
        doConfig();
      } else if (event.getSource().equals(editOneMenuItem)) {
        doEditOne(event);
      } else if (event.getSource().equals(deleteOneMenuItem)) {
        doDeleteOne(event);
      } else if (event.getSource().equals(effectOneMenuItem)) {
        doEffectOne(event);
      } else if (event.getSource().equals(abortOneMenuItem)) {
        doAbortOne(event);
      }
    }
  }

  private void doDelete() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(ReceiptMessages.M.seleteDataToAction(ReceiptMessages.M.delete(),
          PPaymentDef.TABLE_CAPTION));
      return;
    }

    batchProcessor.batchProcess(ReceiptMessages.M.delete(), new BPayment[] {},
        new PagingGridBatchProcessCallback<BPayment>() {
          @Override
          public void execute(BPayment bill, BatchProcesser<BPayment> processer,
              AsyncCallback callback) {
            if (!BBizStates.INEFFECT.equals(bill.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            ReceiptService.Locator.getService().delete(bill.getUuid(), bill.getVersion(), callback);
            BPaymentLogger.getInstance().log(ReceiptMessages.M.delete(), bill);
          }
        });
  }

  private void doEffect() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(ReceiptMessages.M.seleteDataToAction(ReceiptMessages.M.effect(),
          PPaymentDef.TABLE_CAPTION));
      return;
    }

    batchProcessor.batchProcess(ReceiptMessages.M.effect(), new BPayment[] {},
        new PagingGridBatchProcessCallback<BPayment>() {
          @Override
          public void execute(BPayment bill, BatchProcesser<BPayment> processer,
              AsyncCallback callback) {
            if (!BBizStates.INEFFECT.equals(bill.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            ReceiptService.Locator.getService().effect(bill.getUuid(), null, bill.getVersion(),
                callback);
            BPaymentLogger.getInstance().log(ReceiptMessages.M.effect(), bill);
          }
        });
  }

  private void doAbortConfirm() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(ReceiptMessages.M.seleteDataToAction(ReceiptMessages.M.abort(),
          PPaymentDef.TABLE_CAPTION));
      return;
    }

    InputBox.show(ReceiptMessages.M.abortReason(), null, true, PPaymentDef.latestComment,
        new InputBox.Callback() {
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            doAbort(text);
          }
        });
  }

  private void doConfig() {
    if (configDialog == null) {
      configDialog = new ReceiptConfigDialog();
    }
    configDialog.center();
  }

  private void doAbort(final String reason) {
    batchProcessor.batchProcess(ReceiptMessages.M.abort(), new BPayment[] {}, true, false,
        new PagingGridBatchProcessCallback<BPayment>() {
          @Override
          public void execute(BPayment bill, BatchProcesser<BPayment> processer,
              AsyncCallback callback) {
            boolean isEffect = BBizStates.EFFECT.equals(bill.getBizState());
            if (!isEffect) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            ReceiptService.Locator.getService().abort(bill.getUuid(), reason, bill.getVersion(),
                callback);
            BPaymentLogger.getInstance().log(ReceiptMessages.M.abort(), bill);
          }
        });
  }

  private void doEditOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BPayment bill = (BPayment) pagingGrid.getPageData().get(hotItem.getGridRow());

    JumpParameters jumParams = new JumpParameters(ReceiptEditPage.START_NODE);
    jumParams.getUrlRef().set(ReceiptEditPage.PN_UUID, bill.getUuid());
    ep.jump(jumParams);
  }

  private void doDeleteOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BPayment bill = (BPayment) pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmDeleteOne(bill);
  }

  private void doConfirmDeleteOne(final BPayment bill) {
    getMessagePanel().clearMessages();

    String msg = ReceiptMessages.M.actionComfirm2(ReceiptMessages.M.delete(),
        PPaymentDef.TABLE_CAPTION, bill.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        deleteOne(bill);
      }
    });
  }

  private void deleteOne(final BPayment bill) {
    RLoadingDialog
        .show(ReceiptMessages.M.beDoing(ReceiptMessages.M.delete(), bill.getBillNumber()));
    ReceiptService.Locator.getService().delete(bill.getUuid(), bill.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed2(ReceiptMessages.M.delete(),
                PPaymentDef.TABLE_CAPTION, bill.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BPaymentLogger.getInstance().log(ReceiptMessages.M.delete(), bill);

            String msg = ReceiptMessages.M.onSuccess(ReceiptMessages.M.delete(),
                PPaymentDef.TABLE_CAPTION, bill.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doEffectOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BPayment bill = (BPayment) pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmEffectOne(bill);
  }

  private void doConfirmEffectOne(final BPayment bill) {
    getMessagePanel().clearMessages();

    String msg = ReceiptMessages.M.actionComfirm2(ReceiptMessages.M.effect(),
        PPaymentDef.TABLE_CAPTION, bill.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        effectOne(bill);
      }
    });
  }

  private void effectOne(final BPayment bill) {
    RLoadingDialog
        .show(ReceiptMessages.M.beDoing(ReceiptMessages.M.effect(), bill.getBillNumber()));
    ReceiptService.Locator.getService().effect(bill.getUuid(), null, bill.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();

            String msg = ReceiptMessages.M.actionFailed2(ReceiptMessages.M.effect(),
                PPaymentDef.TABLE_CAPTION, bill.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BPaymentLogger.getInstance().log(ReceiptMessages.M.effect(), bill);

            String msg = ReceiptMessages.M.onSuccess(ReceiptMessages.M.effect(),
                PPaymentDef.TABLE_CAPTION, bill.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doAbortOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BPayment bill = (BPayment) pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmAbortOne(bill);
  }

  private void doConfirmAbortOne(final BPayment bill) {
    getMessagePanel().clearMessages();

    InputBox.show(ReceiptMessages.M.abortReason(), null, true, PPaymentDef.latestComment,
        new InputBox.Callback() {
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            abortOne(bill, text);
          }
        });
  }

  private void abortOne(final BPayment bill, String comment) {
    RLoadingDialog.show(ReceiptMessages.M.beDoing(ReceiptMessages.M.abort(), bill.getBillNumber()));
    ReceiptService.Locator.getService().abort(bill.getUuid(), comment, bill.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = ReceiptMessages.M.actionFailed2(ReceiptMessages.M.abort(),
                PPaymentDef.TABLE_CAPTION, bill.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BPaymentLogger.getInstance().log(ReceiptMessages.M.abort(), bill);

            String msg = ReceiptMessages.M.onSuccess(ReceiptMessages.M.abort(),
                PPaymentDef.TABLE_CAPTION, bill.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  @Override
  protected RPagingGrid getPagingGrid() {
    return pagingGrid;
  }

  @Override
  protected EPReceipt getEP() {
    return EPReceipt.getInstance();
  }

  @Override
  public void beforeShow(JumpParameters params) {
    super.beforeShow(params);
    getEP().appendSearchBox();
  }

  @Override
  protected void appendBatchMenu(RPopupMenu menu) {
    menu.addSeparator();
    menu.addItem(new RMenuItem(deleteAction));
    menu.addSeparator();
    menu.addItem(new RMenuItem(effectAction));
    menu.addItem(new RMenuItem(abortAction));
  }

  @Override
  protected void appendLineMenu(RPopupMenu menu, Object bill) {
    BPayment entity = (BPayment) bill;
    menu.addSeparator();
    menu.addItem(editOneMenuItem);
    menu.addItem(deleteOneMenuItem);
    menu.addSeparator();
    menu.addItem(effectOneMenuItem);
    menu.addItem(abortOneMenuItem);

    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());
    boolean canEdit = ep.isPermitted(ReceiptPermDef.UPDATE);
    boolean canDelete = ep.isPermitted(ReceiptPermDef.DELETE);
    boolean canEffect = ep.isPermitted(ReceiptPermDef.EFFECT);
    boolean canAbort = ep.isPermitted(ReceiptPermDef.ABORT);

    editOneMenuItem.setVisible(isInEffect && canEdit);
    deleteOneMenuItem.setVisible(isInEffect && canDelete);
    effectOneMenuItem.setVisible(isInEffect && canEffect);
    abortOneMenuItem.setVisible(isEffect && canAbort);

    lineMenu.minimizeSeparators();
  }

  @Override
  public void beforePrint(PrintingTemplate template, String action,
      List<Map<String, String>> parameters, BeforePrintCallback callback) {
    if (template == null || template.getTemplate() == null) {
      return;
    }
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    for (Object o : getPagingGrid().getSelections()) {
      if (!(o instanceof IsStandardBill)) {
        continue;
      }
      IsStandardBill bill = (IsStandardBill) o;
      Map<String, String> map = new HashMap<String, String>();
      list.add(map);
      map.put(PrintingTemplate.KEY_UUID, bill.getUuid());
      map.put(PrintingTemplate.KEY_BILLNUMBER, bill.getBillNumber());
    }

    callback.execute(template, action, list);
  }
}
