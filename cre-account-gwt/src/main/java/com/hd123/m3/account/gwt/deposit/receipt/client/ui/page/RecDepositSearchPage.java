/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PaySearchPage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.receipt.client.ui.page;

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
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLogger;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositDef;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositLineDef;
import com.hd123.m3.account.gwt.deposit.receipt.client.EPRecDeposit;
import com.hd123.m3.account.gwt.deposit.receipt.client.rpc.RecDepositService;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.RecDepositUrlParams;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.RecDepositUrlParams.Search;
import com.hd123.m3.account.gwt.deposit.receipt.intf.client.perm.RecDepositPermDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.biz.IsStandardBill;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmSearchPage;
import com.hd123.m3.commons.gwt.util.client.flecs.BUCNCodec;
import com.hd123.m3.commons.gwt.util.client.grid.batch.PagingGridBatchProcessCallback;
import com.hd123.m3.commons.gwt.util.client.grid.batch.PagingGridBatchProcessor;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.style.PanelStyles;
import com.hd123.m3.commons.gwt.widget.client.biz.PrintingTemplate;
import com.hd123.m3.commons.gwt.widget.client.ui.print.BeforePrintCallback;
import com.hd123.m3.commons.gwt.widget.client.ui.print.BeforePrintHandler;
import com.hd123.m3.commons.gwt.widget.client.ui.print.PrintButton;
import com.hd123.rumba.commons.gwt.mini.client.codec.CodecUtils;
import com.hd123.rumba.gwt.base.client.BUCN;
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
public class RecDepositSearchPage extends BaseBpmSearchPage implements Search, BeforePrintHandler {
  private static RecDepositSearchPage instance = null;

  public static RecDepositSearchPage getInstance() {
    if (instance == null) {
      instance = new RecDepositSearchPage();
    }
    return instance;
  }

  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef(
      RecDepositUrlParams.Flecs.FIELD_COUNTERPART, EPRecDeposit.getInstance().getFieldCaption(
          GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(
      RecDepositUrlParams.Flecs.FIELD_COUNTERPARTTYPE, EPRecDeposit.getInstance().getFieldCaption(
          GRes.FIELDNAME_COUNTERPART_TYPE, GRes.R.counterpartType()));

  private JumpParameters params;
  private String keyword;
  private RFlecsPanel flecsPanel;
  private FlecsConfigCodec flecsCodec;
  private PageSort pageSort;

  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;
  private RToolbarMenuButton moreButton;
  protected PrintButton printButton = null;
  private VerticalPanel root;

  private RGrid grid;
  private RPagingGrid<BDeposit> pagingGrid;
  private PagingGridBatchProcessor<BDeposit> batchProcessor;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef contractNumberCol;
  private RGridColumnDef contractNameCol;
  private RGridColumnDef paymentTypeCol;
  private RGridColumnDef depositDateCol;
  private RGridColumnDef accountDateCol;
  private RGridColumnDef dealerCol;
  private RGridColumnDef depositTotalCol;
  private RGridColumnDef counterContactCol;
  private RGridColumnDef setterNoCol;

  private RMenuItem editMenuItem;
  private RMenuItem deleteMenuItem;
  private RMenuItem effectMenuItem;
  private RMenuItem abortMenuItem;

  public RecDepositSearchPage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());
    getToolbar().addSeparator();

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    effectAction = new RAction(DepositMessage.M.effect(), new Handler_effectAction());
    abortAction = new RAction(DepositMessage.M.abort(), new Handler_abortAction());
    moreButton = new RToolbarMenuButton(DepositMessage.M.operate(), moreMenu);
    getToolbar().add(moreButton);

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
    root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    root.setStyleName(PanelStyles.STYLE_WIDTH_FIXED);
    initWidget(root);
    root.add(drawFlecsAndGrid());
  }

  private Widget drawFlecsAndGrid() {
    drawLineMenu();
    drawGrid();
    pagingGrid = new RPagingGrid(grid, new GridDataProvider());
    pagingGrid.setWidth("100%");
    pagingGrid.addLoadDataHandler(this);
    drawFlecsPanel();

    return flecsPanel;
  }

  private void drawLineMenu() {
    editMenuItem = new RMenuItem(RActionFacade.EDIT, new Handler_editMenuItem());
    editMenuItem.setHotKey(null);

    deleteMenuItem = new RMenuItem(RActionFacade.DELETE, new Handler_deleteMenuItem());
    deleteMenuItem.setHotKey(null);

    effectMenuItem = new RMenuItem(DepositMessage.M.effect(), new Handler_effectMenuItem());
    effectMenuItem.setHotKey(null);

    abortMenuItem = new RMenuItem(DepositMessage.M.abort(), new Handler_abortMenuItem());
    abortMenuItem.setHotKey(null);
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setAllowHorizontalScrollBar(true);
    grid.addClickHandler(new Handler_grid());
    grid.addSelectionHandler(new SelectionHandler<Integer>() {

      @Override
      public void onSelection(SelectionEvent<Integer> event) {
        if (printButton == null) {
          return;
        }
        Set<String> storeCodes = new HashSet<String>();
        for (BDeposit o : pagingGrid.getSelections()) {
          if (StringUtil.isNullOrBlank(o.getStore().getUuid()) == false) {
            storeCodes.add(o.getStore().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    billNumberCol = new RGridColumnDef(PDepositDef.billNumber);
    billNumberCol.setSortable(true);
    billNumberCol.setName(RecDepositUrlParams.Search.FIELD_BILLNUMBER);
    billNumberCol.setWidth("160px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("120px")));
    billNumberCol.setAllowHiding(false);
    grid.addColumnDef(billNumberCol);

    bizStateCol = new RGridColumnDef(PDepositDef.bizState);
    bizStateCol.setSortable(true);
    bizStateCol.setName(RecDepositUrlParams.Search.FIELD_BIZSTATE);
    bizStateCol.setWidth("80px");
    grid.addColumnDef(bizStateCol);

    accountUnitCol = new RGridColumnDef(getEP().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    accountUnitCol.setSortable(true);
    accountUnitCol.setName(RecDepositUrlParams.Search.FIELD_ACCOUNTUNIT);
    accountUnitCol.setWidth("120px");
    grid.addColumnDef(accountUnitCol);

    counterpartCol = new RGridColumnDef(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    counterpartCol.setSortable(true);
    counterpartCol.setName(RecDepositUrlParams.Search.FIELD_COUNTERPART);
    counterpartCol.setWidth("160px");
    grid.addColumnDef(counterpartCol);

    contractNumberCol = new RGridColumnDef(PDepositDef.contract_code);
    contractNumberCol.setSortable(true);
    contractNumberCol.setName(RecDepositUrlParams.Search.FIELD_SERIALNUMBER);
    contractNumberCol.setWidth("140px");
    grid.addColumnDef(contractNumberCol);

    contractNameCol = new RGridColumnDef(PDepositDef.contract_name);
    contractNameCol.setSortable(true);
    contractNameCol.setName(RecDepositUrlParams.Search.FIELD_SERIALNAME);
    contractNameCol.setWidth("90px");
    grid.addColumnDef(contractNameCol);

    depositTotalCol = new RGridColumnDef(PDepositDef.depositTotal);
    depositTotalCol.setSortable(true);
    depositTotalCol.setName(RecDepositUrlParams.Search.FIELD_PAYTOTAL);
    depositTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    depositTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    depositTotalCol.setWidth("90px");
    grid.addColumnDef(depositTotalCol);

    paymentTypeCol = new RGridColumnDef(PDepositDef.paymentType);
    paymentTypeCol.setWidth("90px");
    paymentTypeCol.setSortable(true);
    paymentTypeCol.setName(RecDepositUrlParams.Search.FIELD_PAYMENTTYPE);
    grid.addColumnDef(paymentTypeCol);

    depositDateCol = new RGridColumnDef(DepositMessage.M.receiptDate());
    depositDateCol.setSortable(true);
    depositDateCol.setName(RecDepositUrlParams.Search.FIELD_DEPOSITDATE);
    depositDateCol.setRendererFactory(new RDateTimeRendererFactory(GWTFormat.fmt_yMd));
    depositDateCol.setWidth("90px");
    grid.addColumnDef(depositDateCol);

    accountDateCol = new RGridColumnDef(PDepositDef.accountDate);
    accountDateCol.setSortable(true);
    accountDateCol.setName(RecDepositUrlParams.Search.FIELD_ACCOUNTDATE);
    accountDateCol.setRendererFactory(new RDateTimeRendererFactory(GWTFormat.fmt_yMd));
    accountDateCol.setWidth("90px");
    grid.addColumnDef(accountDateCol);

    dealerCol = new RGridColumnDef(PDepositDef.dealer);
    dealerCol.setSortable(true);
    dealerCol.setName(RecDepositUrlParams.Search.FIELD_DEALER);
    dealerCol.setWidth("90px");
    grid.addColumnDef(dealerCol);

    counterContactCol = new RGridColumnDef(PDepositDef.counterContact);
    counterContactCol.setWidth("90px");
    counterContactCol.setSortable(true);
    counterContactCol.setName(RecDepositUrlParams.Search.FIELD_COUNTERCONTACT);
    grid.addColumnDef(counterContactCol);

    setterNoCol = new RGridColumnDef(PDepositDef.settleNo);
    setterNoCol.setSortable(true);
    setterNoCol.setWidth("100px");
    setterNoCol.setName(RecDepositUrlParams.Search.FIELD_SETTLENO);
    grid.addColumnDef(setterNoCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private void drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());

    flecsPanel.addField(PDepositDef.billNumber);
    flecsPanel.addField(PDepositDef.bizState);
    flecsPanel.addField(PDepositDef.accountUnit);
    flecsPanel.addField(counterpart);
    flecsPanel.addField(counterpartType);
    flecsPanel.addField(PDepositDef.contract_code);
    flecsPanel.addField(PDepositDef.contract_name);
    flecsPanel.addField(EPRecDeposit.depositDate);
    flecsPanel.addField(PDepositDef.accountDate);
    flecsPanel.addField(PDepositLineDef.subject);
    flecsPanel.addField(PDepositDef.paymentType);
    flecsPanel.addField(PDepositDef.settleNo);
    flecsPanel.addField(PDepositDef.dealer);
    if (getEP().isPermEnabled()) {
      flecsPanel.addField(EPRecDeposit.permGroup);
    }
    flecsPanel.addField(PDepositDef.createInfo_operator_id);
    flecsPanel.addField(PDepositDef.createInfo_operator_fullName);
    flecsPanel.addField(PDepositDef.createInfo_time);
    flecsPanel.addField(PDepositDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PDepositDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PDepositDef.lastModifyInfo_time);
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
    batchProcessor = new PagingGridBatchProcessor<BDeposit>(pagingGrid, getEP().getModuleCaption());
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

  public void onShow(final JumpParameters params) {
    super.onShow(params);
    this.params = params;
    // 权限检查
    if (checkIn() == false)
      return;

    refreshTitle();
    decodeParams(params);
    refreshFlecs();
    refreshCommonds();
  }

  private void refreshCommonds() {
    deleteAction.setVisible(getEP().isPermitted(RecDepositPermDef.DELETE));
    effectAction.setVisible(getEP().isPermitted(RecDepositPermDef.EFFECT));
    abortAction.setVisible(getEP().isPermitted(RecDepositPermDef.ABORT));
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

  public JumpParameters getLastParams() {
    if (params != null)
      params.getMessages().clear();
    return params;
  }

  private boolean checkIn() {
    if (getEP().isPermitted(RecDepositPermDef.READ) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    getEP().getTitleBar().clearStandardTitle();
    getEP().getTitleBar().setTitleText(DepositMessage.M.search());
    getEP().getTitleBar().appendAttributeText(getEP().getModuleCaption());
  }

  private void decodeParams(JumpParameters params) {
    keyword = params.getUrlRef().get(PN_KEYWORD);
  }

  private void refreshFlecs() {
    root.add(flecsPanel);
    grid.setSort(billNumberCol, OrderDir.desc, false);
    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel.addCondition(new Condition(PDepositDef.billNumber, DefaultOperator.STARTS_WITH,
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
    conditions.add(new Condition(PDepositDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositDef.accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpart, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositDef.contract_code, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositDef.contract_name, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(EPRecDeposit.depositDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositDef.accountDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositLineDef.subject, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PDepositDef.paymentType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositDef.settleNo, DefaultOperator.EQUALS, SettleNoField
        .getCurrentSettleNo()));
    conditions.add(new Condition(PDepositDef.dealer, DefaultOperator.EQUALS, null));
    if (getEP().isPermEnabled())
      conditions.add(new Condition(EPRecDeposit.permGroup, DefaultOperator.EQUALS, null));

    return conditions;
  }

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      BDeposit entity = pagingGrid.getRowData(cell.getRow());
      if (entity == null)
        return;

      // 本模块内的跳转
      if (cell.getColumnDef().equals(billNumberCol)) {
        getEP().jumpToViewPage(entity.getUuid());
      }
    }
  }

  private class Handler_deleteAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      batchProcessor.batchProcess(DepositMessage.M.delete(), new BDeposit[] {},
          new PagingGridBatchProcessCallback<BDeposit>() {
            @Override
            public void execute(BDeposit entity, BatchProcesser<BDeposit> processer,
                AsyncCallback callback) {
              if (BBizStates.INEFFECT.equals(entity.getBizState()) == false) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              RecDepositService.Locator.getService().remove(entity.getUuid(), entity.getVersion(),
                  callback);
              BDepositLogger.getInstance().log(DepositMessage.M.delete(), entity);
            }
          });
    }
  }

  private class Handler_effectAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (pagingGrid.getSelections().isEmpty()) {
        RMsgBox.showError(DepositMessage.M.seleteDataToAction(DepositMessage.M.effect(), getEP()
            .getModuleCaption()));
        return;
      }
      batchProcessor.batchProcess(DepositMessage.M.effect(), new BDeposit[] {},
          new PagingGridBatchProcessCallback<BDeposit>() {
            @Override
            public void execute(BDeposit entity, BatchProcesser<BDeposit> processer,
                AsyncCallback callback) {
              if (!BBizStates.INEFFECT.equals(entity.getBizState())) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              RecDepositService.Locator.getService().effect(entity.getUuid(), entity.getVersion(),
                  callback);
              BDepositLogger.getInstance().log(DepositMessage.M.effect(), entity);
            }
          });
    }
  }

  private class Handler_abortAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (pagingGrid.getSelections().isEmpty()) {
        RMsgBox.showError(DepositMessage.M.seleteDataToAction(DepositMessage.M.abort(), getEP()
            .getModuleCaption()));
        return;
      }
      batchProcessor.batchProcess(DepositMessage.M.abort(), new BDeposit[] {},
          new PagingGridBatchProcessCallback<BDeposit>() {
            @Override
            public void execute(BDeposit entity, BatchProcesser<BDeposit> processer,
                AsyncCallback callback) {
              if (!BBizStates.EFFECT.equals(entity.getBizState())) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              RecDepositService.Locator.getService().abort(entity.getUuid(), entity.getVersion(),
                  callback);
              BDepositLogger.getInstance().log(DepositMessage.M.abort(), entity);
            }
          });
    }
  }

  private class Handler_editMenuItem implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BDeposit pay = pagingGrid.getPageData().get(hotItem.getGridRow());

      JumpParameters jumParams = new JumpParameters(RecDepositEditPage.START_NODE);
      jumParams.getUrlRef().set(RecDepositEditPage.PN_UUID, pay.getUuid());
      getEP().jump(jumParams);
    }
  }

  private class Handler_deleteMenuItem implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BDeposit entity = pagingGrid.getPageData().get(hotItem.getGridRow());

      doConfirmDeleteOne(entity);
    }
  }

  private void doConfirmDeleteOne(final BDeposit entity) {
    String msg = DepositMessage.M.actionComfirm2(DepositMessage.M.delete(),
        RecDepositUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doDeleteOne(entity);
      }
    });
  }

  private void doDeleteOne(final BDeposit entity) {
    RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.delete()));
    RecDepositService.Locator.getService().remove(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.delete(),
                RecDepositUrlParams.MODULE_CAPTION);
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositLogger.getInstance().log(DepositMessage.M.delete(), entity);

            getMessagePanel().clearMessages();
            getMessagePanel().putInfoMessage(
                DepositMessage.M.onSuccess(DepositMessage.M.delete(),
                    RecDepositUrlParams.MODULE_CAPTION, entity.getBillNumber()));
            pagingGrid.refresh();
          }
        });
  }

  private class Handler_effectMenuItem implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BDeposit entity = pagingGrid.getPageData().get(hotItem.getGridRow());

      doConfirmEffect(entity);
    }
  }

  private void doConfirmEffect(final BDeposit entity) {
    getMessagePanel().clearMessages();
    String msg = DepositMessage.M.actionComfirm2(DepositMessage.M.effect(), getEP()
        .getModuleCaption(), entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEffectOne(entity);
      }
    });
  }

  private void doEffectOne(final BDeposit entity) {
    RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.effect()));
    RecDepositService.Locator.getService().effect(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.effect(), getEP()
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
            BDepositLogger.getInstance().log(DepositMessage.M.effect(), entity);

            String msg = DepositMessage.M.onSuccess(DepositMessage.M.effect(), getEP()
                .getModuleCaption(), entity.toFriendlyStr());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private class Handler_abortMenuItem implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BDeposit entity = pagingGrid.getPageData().get(hotItem.getGridRow());

      doConfirmAbort(entity);
    }
  }

  private void doConfirmAbort(final BDeposit entity) {
    getMessagePanel().clearMessages();
    String msg = DepositMessage.M.actionComfirm2(DepositMessage.M.abort(), getEP()
        .getModuleCaption(), entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doAbortOne(entity);
      }
    });
  }

  private void doAbortOne(final BDeposit entity) {
    RLoadingDialog.show(DepositMessage.M.actionDoing(DepositMessage.M.abort()));
    RecDepositService.Locator.getService().abort(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.abort(), getEP()
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
            BDepositLogger.getInstance().log(DepositMessage.M.abort(), entity);

            String msg = DepositMessage.M.onSuccess(DepositMessage.M.abort(), getEP()
                .getModuleCaption(), entity.toFriendlyStr());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private class MyOperatorPolicy implements OperatorPolicy {
    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (PDepositDef.paymentType == field || EPRecDeposit.permGroup == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PDepositDef.settleNo == field || counterpart == field
          || PDepositDef.dealer == field || PDepositDef.accountUnit == field
          || counterpartType == field) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PDepositLineDef.subject == field) {
        result.add(DefaultOperator.CONTAINS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (field == counterpart) {
        return DefaultOperator.EQUALS == operator || DefaultOperator.NOT_EQUALS == operator ? buildCounterpart()
            : null;
      } else if (field == PDepositLineDef.subject) {
        return DefaultOperator.CONTAINS == operator ? new SubjectUCNBox(
            BSubjectType.predeposit.name(), DirectionType.receipt.getDirectionValue()) : null;
      } else if (field == PDepositDef.settleNo) {
        SettleNoField settleNoField = new SettleNoField(PDepositDef.settleNo);
        settleNoField.refreshOption(12);
        settleNoField.setMaxLength(6);
        return settleNoField;
      } else if (field == PDepositDef.dealer) {
        return DefaultOperator.EQUALS == operator ? new EmployeeUCNBox() : null;
      } else if (field == PDepositDef.paymentType) {
        return DefaultOperator.EQUALS == operator || DefaultOperator.NOT_EQUALS == operator ? new PaymentTypeComboBox()
            : null;
      } else if (field == PDepositDef.accountUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == EPRecDeposit.permGroup) {
        RComboBox<String> permGroupField = new RComboBox<String>();
        permGroupField.setEditable(false);
        permGroupField.setMaxDropdownRowCount(10);
        if (getEP().getUserGroups() != null) {
          for (String key : getEP().getUserGroups().keySet()) {
            permGroupField.addOption(key, getEP().getUserGroups().get(key));
          }
        }
        return permGroupField;
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
      setNullOptionText(DepositMessage.M.all());
      setEditable(false);
      for (Entry<String, String> entry : getEP().getCounterpartTypeMap().entrySet()) {
        this.addOption(entry.getKey(), entry.getValue());
      }
    }
  }

  private class PaymentTypeComboBox extends RComboBox<BUCN> {

    public PaymentTypeComboBox() {
      setEditable(false);
      setMaxDropdownRowCount(10);
      setFieldDef(PDepositDef.paymentType);

      clearOptions();

      for (BUCN paymentType : getEP().getPaymentTypes()) {
        addOption(paymentType, paymentType.toFriendlyStr());
      }
    }
  }

  private class GridDataProvider implements RPageDataProvider<BDeposit> {
    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BDeposit>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);

      if (sortField != null)
        pageSort.appendOrder(sortField, sortDir);
      else
        pageSort.appendOrder(RecDepositUrlParams.Search.FIELD_BILLNUMBER, "desc");

      RecDepositService.Locator.getService().query(flecsPanel.getCurrentConfig().getConditions(),
          flecsPanel.getCurrentConfig().getVisibleColumnNames(), pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BDeposit rowData, List<BDeposit> pageData) {
      if (col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      if (col == bizStateCol.getIndex())
        return PDepositDef.bizState.getEnumCaption(rowData.getBizState());
      if (col == accountUnitCol.getIndex())
        return rowData.getAccountUnit().toFriendlyStr();
      if (col == counterpartCol.getIndex())
        return rowData.getCounterpart().toFriendlyStr(getEP().getCounterpartTypeMap());
      if (col == setterNoCol.getIndex())
        return rowData.getSettleNo();
      if (col == paymentTypeCol.getIndex())
        return rowData.getPaymentType() == null ? null : rowData.getPaymentType().toFriendlyStr();
      if (col == depositDateCol.getIndex())
        return rowData.getDepositDate();
      if (col == accountDateCol.getIndex())
        return rowData.getAccountDate();
      if (col == dealerCol.getIndex())
        return rowData.getDealer() == null ? null : rowData.getDealer().toFriendlyStr();
      if (col == depositTotalCol.getIndex())
        return rowData.getDepositTotal() == null ? null : Double.valueOf(rowData.getDepositTotal()
            .doubleValue());
      if (col == counterContactCol.getIndex())
        return rowData.getCounterContact();
      if (col == contractNumberCol.getIndex())
        return rowData.getContract() == null ? null : rowData.getContract().getCode();
      if (col == contractNameCol.getIndex())
        return rowData.getContract() == null ? null : rowData.getContract().getName();
      return null;
    }
  }

  @Override
  protected RPagingGrid getPagingGrid() {
    return pagingGrid;
  }

  @Override
  protected EPRecDeposit getEP() {
    return EPRecDeposit.getInstance();
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
    BDeposit entity = (BDeposit) bill;
    menu.addSeparator();
    menu.addItem(editMenuItem);
    menu.addItem(deleteMenuItem);
    menu.addSeparator();
    menu.addItem(effectMenuItem);
    menu.addItem(abortMenuItem);

    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffected = BBizStates.EFFECT.equals(entity.getBizState());
    boolean canEdit = getEP().isPermitted(RecDepositPermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(RecDepositPermDef.DELETE);
    boolean canEffect = getEP().isPermitted(RecDepositPermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(RecDepositPermDef.ABORT);

    editMenuItem.setVisible(isInEffect && canEdit);
    deleteMenuItem.setVisible(isInEffect && canDelete);
    effectMenuItem.setVisible(isInEffect && canEffect);
    abortMenuItem.setVisible(isEffected && canAbort);

    menu.minimizeSeparators();
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
