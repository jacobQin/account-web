/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： RecInvoiceRegSearchPage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-23 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.receipt.client.ui;

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
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegLogger;
import com.hd123.m3.account.gwt.invoice.commons.client.ui.dialog.InvoiceRegConfigDialog;
import com.hd123.m3.account.gwt.invoice.intf.client.InvoiceRegMessage;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegInvoiceDef;
import com.hd123.m3.account.gwt.invoice.intf.client.dd.PInvoiceRegLineDef;
import com.hd123.m3.account.gwt.invoice.receipt.client.EPRecInvoiceReg;
import com.hd123.m3.account.gwt.invoice.receipt.client.rpc.RecInvoiceRegService;
import com.hd123.m3.account.gwt.invoice.receipt.intf.client.RecInvoiceRegUrlParams;
import com.hd123.m3.account.gwt.invoice.receipt.intf.client.RecInvoiceRegUrlParams.Search;
import com.hd123.m3.account.gwt.invoice.receipt.intf.client.perm.RecInvoiceRegPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.biz.IsStandardBill;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmSearchPage;
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
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
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
 * @author chenpeisi
 * 
 */
public class RecInvoiceRegSearchPage extends BaseBpmSearchPage implements Search,
    BeforePrintHandler {
  private static RecInvoiceRegSearchPage instance = null;

  public static EmbeddedFieldDef permGroup = new EmbeddedFieldDef(FIELD_PERMGROUP,
      InvoiceRegMessage.M.permGroup());
  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(
      RecInvoiceRegUrlParams.Flecs.FIELD_COUNTERPARTTYPE, EPRecInvoiceReg.getInstance()
          .getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE, GRes.R.counterpartType()));

  public static RecInvoiceRegSearchPage getInstance() {
    if (instance == null) {
      instance = new RecInvoiceRegSearchPage();
    }
    return instance;
  }

  private JumpParameters params;
  private FlecsConfigCodec flecsCodec;
  private String keyword;
  private RFlecsPanel flecsPanel;
  private PageSort pageSort;

  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;
  private RToolbarMenuButton moreButton;
  protected PrintButton printButton = null;

  private RAction configAction;

  private InvoiceRegConfigDialog configDialog;

  private RGrid grid;
  private RPagingGrid<BInvoiceReg> pagingGrid;
  private PagingGridBatchProcessor<BInvoiceReg> batchProcessor;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef countpartCol;
  private RGridColumnDef setterNoCol;
  private RGridColumnDef regDateCol;
  private RGridColumnDef invoiceTotalCol;
  private RGridColumnDef invoiceTaxCol;
  private RGridColumnDef accountTotalCol;
  private RGridColumnDef accountTaxCol;
  private RGridColumnDef totalDiffCol;
  private RGridColumnDef taxDiffCol;

  private RMenuItem editMenuItem;
  private RMenuItem deleteMenuItem;
  private RMenuItem effectMenuItem;
  private RMenuItem abortMenuItem;

  private ActionHandler actionHandler = new ActionHandler();

  public RecInvoiceRegSearchPage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());
    getToolbar().addSeparator();

    deleteAction = new RAction(RActionFacade.DELETE, actionHandler);
    effectAction = new RAction(CommonsMessages.M.effect(), actionHandler);
    abortAction = new RAction(CommonsMessages.M.abort(), actionHandler);

    moreButton = new RToolbarMenuButton(InvoiceRegMessage.M.operate(), moreMenu);
    getToolbar().add(moreButton);

    configAction = new RAction(RActionFacade.OPTION, actionHandler);
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
    pagingGrid = new RPagingGrid(grid, new GridDataProvider());
    pagingGrid.addLoadDataHandler(this);
    drawFlecsPanel();

    return flecsPanel;
  }

  private void drawLineMenu() {
    editMenuItem = new RMenuItem(RActionFacade.EDIT, actionHandler);
    editMenuItem.setHotKey(null);

    deleteMenuItem = new RMenuItem(RActionFacade.DELETE, actionHandler);
    deleteMenuItem.setHotKey(null);

    effectMenuItem = new RMenuItem(InvoiceRegMessage.M.effect(), actionHandler);
    effectMenuItem.setHotKey(null);

    abortMenuItem = new RMenuItem(InvoiceRegMessage.M.abort(), actionHandler);
    abortMenuItem.setHotKey(null);
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
        for (BInvoiceReg o : pagingGrid.getSelections()) {
          if (StringUtil.isNullOrBlank(o.getStore().getUuid()) == false) {
            storeCodes.add(o.getStore().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    billNumberCol = new RGridColumnDef(PInvoiceRegDef.billNumber);
    billNumberCol.setSortable(true);
    billNumberCol.setName(RecInvoiceRegUrlParams.Search.FIELD_BILLNUMBER);
    billNumberCol.setWidth("180px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setAllowHiding(false);
    grid.addColumnDef(billNumberCol);

    bizStateCol = new RGridColumnDef(PInvoiceRegDef.bizState);
    bizStateCol.setSortable(true);
    bizStateCol.setName(RecInvoiceRegUrlParams.Search.FIELD_BIZSTATE);
    bizStateCol.setWidth("80px");
    grid.addColumnDef(bizStateCol);

    accountUnitCol = new RGridColumnDef(getEP().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    accountUnitCol.setSortable(true);
    accountUnitCol.setName(RecInvoiceRegUrlParams.Search.FIELD_ACCOUNTUNIT);
    accountUnitCol.setWidth("120px");
    grid.addColumnDef(accountUnitCol);

    countpartCol = new RGridColumnDef(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    countpartCol.setSortable(true);
    countpartCol.setName(RecInvoiceRegUrlParams.Search.FIELD_COUNTERPART);
    countpartCol.setWidth("120px");
    grid.addColumnDef(countpartCol);

    setterNoCol = new RGridColumnDef(PInvoiceRegDef.settleNo);
    setterNoCol.setWidth("90px");
    setterNoCol.setSortable(true);
    setterNoCol.setName(RecInvoiceRegUrlParams.Search.FIELD_SETTLENO);
    grid.addColumnDef(setterNoCol);

    regDateCol = new RGridColumnDef(PInvoiceRegDef.regDate);
    regDateCol.setWidth("90px");
    regDateCol.setSortable(true);
    regDateCol.setName(RecInvoiceRegUrlParams.Search.FIELD_REGDATE);
    regDateCol.setRendererFactory(new RDateTimeRendererFactory(GWTFormat.fmt_yMd));
    grid.addColumnDef(regDateCol);

    invoiceTotalCol = new RGridColumnDef(PInvoiceRegDef.invoiceTotal_total);
    invoiceTotalCol.setWidth("90px");
    invoiceTotalCol.setSortable(true);
    invoiceTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    invoiceTotalCol.setName(RecInvoiceRegUrlParams.Search.FIELD_INVOICETOTAL_TOTAL);
    invoiceTotalCol.setRendererFactory(new RNumberRendererFactory<Number>(GWTFormat.fmt_money));
    grid.addColumnDef(invoiceTotalCol);

    invoiceTaxCol = new RGridColumnDef(PInvoiceRegDef.invoiceTotal_tax);
    invoiceTaxCol.setWidth("90px");
    invoiceTaxCol.setSortable(true);
    invoiceTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    invoiceTaxCol.setName(RecInvoiceRegUrlParams.Search.FIELD_INVOICETOTAL_TAX);
    invoiceTaxCol.setRendererFactory(new RNumberRendererFactory<Number>(GWTFormat.fmt_money));
    grid.addColumnDef(invoiceTaxCol);

    accountTotalCol = new RGridColumnDef(PInvoiceRegDef.accountTotal_total);
    accountTotalCol.setWidth("90px");
    accountTotalCol.setSortable(true);
    accountTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    accountTotalCol.setName(RecInvoiceRegUrlParams.Search.FIELD_ACCOUNTTOTAL_TOTAL);
    accountTotalCol.setRendererFactory(new RNumberRendererFactory<Number>(GWTFormat.fmt_money));
    grid.addColumnDef(accountTotalCol);

    accountTaxCol = new RGridColumnDef(PInvoiceRegDef.accountTotal_tax);
    accountTaxCol.setWidth("90px");
    accountTaxCol.setSortable(true);
    accountTaxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    accountTaxCol.setName(RecInvoiceRegUrlParams.Search.FIELD_ACCOUNTTOTAL_TAX);
    accountTaxCol.setRendererFactory(new RNumberRendererFactory<Number>(GWTFormat.fmt_money));
    grid.addColumnDef(accountTaxCol);

    totalDiffCol = new RGridColumnDef(PInvoiceRegDef.totalDiff);
    totalDiffCol.setWidth("90px");
    totalDiffCol.setSortable(true);
    totalDiffCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalDiffCol.setName(RecInvoiceRegUrlParams.Search.FIELD_TOTALDIFF);
    totalDiffCol.setRendererFactory(new RNumberRendererFactory<Number>(GWTFormat.fmt_money));
    grid.addColumnDef(totalDiffCol);

    taxDiffCol = new RGridColumnDef(PInvoiceRegDef.taxDiff);
    taxDiffCol.setSortable(true);
    taxDiffCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    taxDiffCol.setName(RecInvoiceRegUrlParams.Search.FIELD_TAXDIFF);
    taxDiffCol.setRendererFactory(new RNumberRendererFactory<Number>(GWTFormat.fmt_money));
    grid.addColumnDef(taxDiffCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private void drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());

    flecsPanel.addField(PInvoiceRegDef.billNumber);
    flecsPanel.addField(PInvoiceRegDef.bizState);
    flecsPanel.addField(counterpartType);
    flecsPanel.addField(PInvoiceRegDef.accountUnit);
    flecsPanel.addField(PInvoiceRegDef.counterpart);
    flecsPanel.addField(PInvoiceRegInvoiceDef.invoiceNumber);
    flecsPanel.addField(PInvoiceRegLineDef.acc1_sourceBill_billNumber);
    flecsPanel.addField(PInvoiceRegDef.settleNo);
    flecsPanel.addField(PInvoiceRegDef.regDate);
    if (getEP().isPermEnabled()) {
      flecsPanel.addField(permGroup);
    }
    flecsPanel.addField(PInvoiceRegDef.createInfo_operator_id);
    flecsPanel.addField(PInvoiceRegDef.createInfo_operator_fullName);
    flecsPanel.addField(PInvoiceRegDef.createInfo_time);
    flecsPanel.addField(PInvoiceRegDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PInvoiceRegDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PInvoiceRegDef.lastModifyInfo_time);
    flecsPanel.addBeforeLoadDataHandler(new BeforeLoadDataHandler<Integer>() {

      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent<Integer> event) {
        JumpParameters jumParams = new JumpParameters(START_NODE);
        FlecsConfig curConfig = flecsPanel.getCurrentConfig();
        curConfig.setShowConditions(flecsPanel.isShowConditions());
        jumParams.getUrlRef().set(PN_FLECSCONFIG, flecsCodec.encode(curConfig));
        jumParams.getUrlRef().set(PN_PAGE, String.valueOf(event.getParameter()));
        getEP().jump(jumParams);
        event.cancel();
      }
    });

    getToolbar().add(flecsPanel.getMenuButton(), Alignment.right);

  }

  private void afterDraw() {
    batchProcessor = new PagingGridBatchProcessor<BInvoiceReg>(pagingGrid, getEP()
        .getModuleCaption());
    flecsCodec = new FlecsConfigCodec();
    flecsCodec.addOperatorCodec(new DefaultOperatorCodec());
    flecsCodec.addOperandCodec(new BUCNCodec());
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
  }

  @Override
  public void onShow(final JumpParameters params) {
    super.onShow(params);

    this.params = params;
    // 权限检查
    if (checkIn() == false)
      return;

    refreshTitle();
    decodeParams(params);
    refreshFlecs();
    refreshCommands();
  }

  public JumpParameters getLastParams() {
    if (params != null)
      params.getMessages().clear();
    return params;
  }

  private boolean checkIn() {
    if (getEP().isPermitted(RecInvoiceRegPermDef.READ) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    getEP().getTitleBar().clearStandardTitle();
    getEP().getTitleBar().setTitleText(InvoiceRegMessage.M.search());
    getEP().getTitleBar().appendAttributeText(getEP().getModuleCaption());
  }

  private void refreshCommands() {
    configAction.setEnabled(getEP().isPermitted(RecInvoiceRegPermDef.CONFIG));
    deleteAction.setVisible(getEP().isPermitted(RecInvoiceRegPermDef.DELETE));
    effectAction.setVisible(getEP().isPermitted(RecInvoiceRegPermDef.EFFECT));
    abortAction.setVisible(getEP().isPermitted(RecInvoiceRegPermDef.ABORT));
    printButton.setEnabled(getEP().isPermitted(BAction.PRINT.getKey()));

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

  private void decodeParams(JumpParameters params) {
    keyword = params.getUrlRef().get(PN_KEYWORD);
  }

  private void refreshFlecs() {
    grid.setSort(billNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel.addCondition(new Condition(PInvoiceRegDef.billNumber, DefaultOperator.STARTS_WITH,
          keyword));
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
    conditions.add(new Condition(PInvoiceRegDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRegDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRegDef.accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRegDef.counterpart, DefaultOperator.EQUALS, null));
    conditions
        .add(new Condition(PInvoiceRegInvoiceDef.invoiceNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRegLineDef.acc1_sourceBill_billNumber,
        DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRegDef.settleNo, DefaultOperator.EQUALS, SettleNoField
        .getCurrentSettleNo()));
    conditions.add(new Condition(PInvoiceRegDef.regDate, DefaultOperator.EQUALS, null));
    if (getEP().isPermEnabled())
      conditions.add(new Condition(permGroup, DefaultOperator.EQUALS, null));

    return conditions;
  }

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      BInvoiceReg entity = pagingGrid.getRowData(cell.getRow());
      if (entity == null)
        return;

      // 本模块内的跳转
      if (cell.getColumnDef().equals(billNumberCol)) {
        getEP().jumpToViewPage(entity.getUuid());
      }
    }
  }

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == effectAction) {
        doEffect();
      } else if (event.getSource() == abortAction) {
        doAbort();
      } else if (event.getSource() == deleteAction) {
        doDelete();
      } else if (event.getSource() == configAction) {
        doConfig();
      } else if (event.getSource() == editMenuItem) {
        doEditOne();
      } else if (event.getSource() == effectMenuItem) {
        doEffectOne();
      } else if (event.getSource() == abortMenuItem) {
        doAbortOne();
      } else if (event.getSource() == deleteMenuItem) {
        doDeleteOne();
      }
    }
  }

  public void doEffect() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(InvoiceRegMessage.M.seleteDataToAction(InvoiceRegMessage.M.effect(),
          getEP().getModuleCaption()));
      return;
    }
    batchProcessor.batchProcess(InvoiceRegMessage.M.effect(), new BInvoiceReg[] {},
        new PagingGridBatchProcessCallback<BInvoiceReg>() {
          @Override
          public void execute(BInvoiceReg entity, BatchProcesser<BInvoiceReg> processer,
              AsyncCallback callback) {
            if (!BBizStates.INEFFECT.equals(entity.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            RecInvoiceRegService.Locator.getService().effect(entity.getUuid(), entity.getVersion(),
                callback);
            BInvoiceRegLogger.getInstance().log(InvoiceRegMessage.M.effect(), entity);
          }
        });
  }

  public void doAbort() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(InvoiceRegMessage.M.seleteDataToAction(InvoiceRegMessage.M.abort(), getEP()
          .getModuleCaption()));
      return;
    }
    batchProcessor.batchProcess(InvoiceRegMessage.M.abort(), new BInvoiceReg[] {},
        new PagingGridBatchProcessCallback<BInvoiceReg>() {
          @Override
          public void execute(BInvoiceReg entity, BatchProcesser<BInvoiceReg> processer,
              AsyncCallback callback) {
            if (!BBizStates.EFFECT.equals(entity.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            RecInvoiceRegService.Locator.getService().abort(entity.getUuid(), entity.getVersion(),
                callback);
            BInvoiceRegLogger.getInstance().log(InvoiceRegMessage.M.abort(), entity);
          }
        });
  }

  public void doDelete() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(InvoiceRegMessage.M.seleteDataToAction(InvoiceRegMessage.M.delete(),
          getEP().getModuleCaption()));
      return;
    }
    batchProcessor.batchProcess(InvoiceRegMessage.M.delete(), new BInvoiceReg[] {},
        new PagingGridBatchProcessCallback<BInvoiceReg>() {
          @Override
          public void execute(BInvoiceReg entity, BatchProcesser<BInvoiceReg> processer,
              AsyncCallback callback) {
            if (BBizStates.INEFFECT.equals(entity.getBizState()) == false) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            RecInvoiceRegService.Locator.getService().remove(entity.getUuid(), entity.getVersion(),
                callback);
            BInvoiceRegLogger.getInstance().log(InvoiceRegMessage.M.delete(), entity);
          }
        });

  }

  private void doConfig() {
    if (configDialog == null)
      configDialog = new InvoiceRegConfigDialog(DirectionType.receipt.getDirectionValue(),
          new InvoiceRegConfigDialog.Callback() {

            @Override
            public void execute(BInvoiceRegConfig result) {
              if (result != null)
                getEP().setConfig(result);
            }
          });
    configDialog.refresh(getEP().getConfig());
    configDialog.center();
  }

  private void doEditOne() {
    RHotItemRenderer hotItem = (RHotItemRenderer) editMenuItem.getContextWidget();
    BInvoiceReg entity = pagingGrid.getPageData().get(hotItem.getGridRow());

    JumpParameters jumParams = new JumpParameters(RecInvoiceRegEditPage.START_NODE);
    jumParams.getUrlRef().set(RecInvoiceRegEditPage.PN_UUID, entity.getUuid());
    getEP().jump(jumParams);
  }

  private void doEffectOne() {
    RHotItemRenderer hotItem = (RHotItemRenderer) effectMenuItem.getContextWidget();
    BInvoiceReg entity = pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmEffect(entity);
  }

  private void doConfirmEffect(final BInvoiceReg entity) {
    getMessagePanel().clearMessages();
    String msg = InvoiceRegMessage.M.actionComfirm2(InvoiceRegMessage.M.effect(), getEP()
        .getModuleCaption(), entity.getBillNumber());

    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEffectOne(entity);
      }
    });
  }

  private void doEffectOne(final BInvoiceReg entity) {
    RLoadingDialog.show(InvoiceRegMessage.M.actionDoing(InvoiceRegMessage.M.effect()));

    RecInvoiceRegService.Locator.getService().effect(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.effect(), getEP()
                .getModuleCaption());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BInvoiceRegLogger.getInstance().log(InvoiceRegMessage.M.effect(), entity);

            String msg = InvoiceRegMessage.M.onSuccess(InvoiceRegMessage.M.effect(), getEP()
                .getModuleCaption(), entity.toFriendlyStr());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doAbortOne() {
    RHotItemRenderer hotItem = (RHotItemRenderer) abortMenuItem.getContextWidget();
    BInvoiceReg entity = pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmAbort(entity);
  }

  private void doConfirmAbort(final BInvoiceReg entity) {
    getMessagePanel().clearMessages();
    String msg = InvoiceRegMessage.M.actionComfirm2(InvoiceRegMessage.M.effect(), getEP()
        .getModuleCaption(), entity.getBillNumber());

    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doAbortOne(entity);
      }
    });
  }

  private void doAbortOne(final BInvoiceReg entity) {
    RLoadingDialog.show(InvoiceRegMessage.M.actionDoing(InvoiceRegMessage.M.abort()));
    RecInvoiceRegService.Locator.getService().abort(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.abort(), getEP()
                .getModuleCaption());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BInvoiceRegLogger.getInstance().log(InvoiceRegMessage.M.abort(), entity);

            String msg = InvoiceRegMessage.M.onSuccess(InvoiceRegMessage.M.abort(), getEP()
                .getModuleCaption(), entity.toFriendlyStr());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doDeleteOne() {
    RHotItemRenderer hotItem = (RHotItemRenderer) deleteMenuItem.getContextWidget();
    BInvoiceReg entity = pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmDeleteOne(entity);
  }

  private void doConfirmDeleteOne(final BInvoiceReg entity) {
    String msg = InvoiceRegMessage.M.actionComfirm2(InvoiceRegMessage.M.delete(),
        RecInvoiceRegUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doDeleteOne(entity);
      }
    });
  }

  private void doDeleteOne(final BInvoiceReg entity) {
    RLoadingDialog.show(InvoiceRegMessage.M.actionDoing(InvoiceRegMessage.M.delete()));
    RecInvoiceRegService.Locator.getService().remove(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InvoiceRegMessage.M.actionFailed(InvoiceRegMessage.M.delete(),
                RecInvoiceRegUrlParams.MODULE_CAPTION);
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BInvoiceRegLogger.getInstance().log(InvoiceRegMessage.M.delete(), entity);

            getMessagePanel().clearMessages();
            getMessagePanel().putInfoMessage(
                InvoiceRegMessage.M.onSuccess(InvoiceRegMessage.M.delete(),
                    RecInvoiceRegUrlParams.MODULE_CAPTION, ""));
            pagingGrid.refresh();
          }
        });
  }

  private class MyOperatorPolicy implements OperatorPolicy {

    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (PInvoiceRegDef.bizState == field || permGroup == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PInvoiceRegDef.settleNo == field || PInvoiceRegDef.counterpart == field
          || PInvoiceRegDef.accountUnit == field || PInvoiceRegInvoiceDef.invoiceNumber == field
          || PInvoiceRegLineDef.acc1_sourceBill_billNumber == field || counterpartType == field) {
        result.add(DefaultOperator.EQUALS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {

    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (field == PInvoiceRegDef.counterpart) {
        field.setCaption(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
        return DefaultOperator.EQUALS == operator || DefaultOperator.NOT_EQUALS == operator ? buildCounterpart()
            : null;
      } else if (field == PInvoiceRegDef.settleNo) {
        SettleNoField settleNoField = new SettleNoField(PInvoiceRegDef.settleNo);
        settleNoField.refreshOption(12);
        settleNoField.setMaxLength(6);
        return settleNoField;
      } else if (permGroup == field) {
        return new PermGroupField(permGroup);
      } else if (field == PInvoiceRegDef.accountUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (counterpartType == field) {
        return new CounterpartTypeField(counterpartType);
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
      setNullOptionText(InvoiceRegMessage.M.all());
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
      for (Entry<String, String> entry : getEP().getUserGroups().entrySet()) {
        this.addOption(entry.getKey(), entry.getValue());
      }
    }
  }

  private class GridDataProvider implements RPageDataProvider<BInvoiceReg> {
    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BInvoiceReg>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);

      if (sortField != null)
        pageSort.appendOrder(sortField, sortDir);
      else
        pageSort.appendOrder(RecInvoiceRegUrlParams.Search.FIELD_BILLNUMBER, "desc");

      RecInvoiceRegService.Locator.getService().query(
          flecsPanel.getCurrentConfig().getConditions(),
          flecsPanel.getCurrentConfig().getVisibleColumnNames(), pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BInvoiceReg rowData, List<BInvoiceReg> pageData) {
      if (col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      if (col == bizStateCol.getIndex())
        return PInvoiceRegDef.bizState.getEnumCaption(rowData.getBizState());
      if (col == accountUnitCol.getIndex())
        return rowData.getAccountUnit().toFriendlyStr();
      if (col == countpartCol.getIndex())
        return rowData.getCounterpart().toFriendlyStr(getEP().getCounterpartTypeMap());
      if (col == setterNoCol.getIndex())
        return rowData.getSettleNo();
      if (col == regDateCol.getIndex())
        return rowData.getRegDate();
      if (col == invoiceTotalCol.getIndex())
        return rowData.getInvoiceTotal() == null ? null : rowData.getInvoiceTotal().getTotal()
            .doubleValue();
      if (col == invoiceTaxCol.getIndex())
        return rowData.getInvoiceTotal() == null ? null : rowData.getInvoiceTotal().getTax()
            .doubleValue();
      if (col == accountTotalCol.getIndex())
        return rowData.getAccountTotal() == null ? null : rowData.getAccountTotal().getTotal()
            .doubleValue();
      if (col == accountTaxCol.getIndex())
        return rowData.getAccountTotal() == null ? null : rowData.getAccountTotal().getTax()
            .doubleValue();
      if (col == totalDiffCol.getIndex())
        return rowData.getTotalDiff() == null ? null : rowData.getTotalDiff().doubleValue();
      if (col == taxDiffCol.getIndex())
        return rowData.getTaxDiff() == null ? null : rowData.getTaxDiff().doubleValue();
      return null;
    }
  }

  @Override
  protected RPagingGrid getPagingGrid() {
    return pagingGrid;
  }

  @Override
  protected EPRecInvoiceReg getEP() {
    return EPRecInvoiceReg.getInstance();
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
    BInvoiceReg entity = (BInvoiceReg) bill;
    menu.addSeparator();
    menu.addItem(editMenuItem);
    menu.addItem(deleteMenuItem);
    menu.addSeparator();
    menu.addItem(effectMenuItem);
    menu.addItem(abortMenuItem);

    boolean isIneffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());

    boolean canEdit = getEP().isPermitted(RecInvoiceRegPermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(RecInvoiceRegPermDef.DELETE);
    boolean canEffect = getEP().isPermitted(RecInvoiceRegPermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(RecInvoiceRegPermDef.ABORT);

    editMenuItem.setVisible(isIneffect && canEdit);
    deleteMenuItem.setVisible(isIneffect && canDelete);
    effectMenuItem.setVisible(isIneffect && canEffect);
    abortMenuItem.setVisible(isEffect && canAbort);
  }

  @Override
  public void beforeShow(JumpParameters params) {
    super.beforeShow(params);
    getEP().appendSearchBox();
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
