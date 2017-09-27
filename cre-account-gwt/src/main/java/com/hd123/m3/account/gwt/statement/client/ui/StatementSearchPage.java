/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： StatementSearchPage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.contract.accountsettle.intf.client.AccountSettleUrlParams;
import com.hd123.m3.account.gwt.contract.accountsettle.intf.client.perm.AccountSettlePermDef;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.SPositionCodec;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.TypePositionBox;
import com.hd123.m3.account.gwt.invoice.payment.intf.client.perm.PayInvoiceRegPermDef;
import com.hd123.m3.account.gwt.invoice.receipt.intf.client.perm.RecInvoiceRegPermDef;
import com.hd123.m3.account.gwt.payment.pay.intf.client.perm.PaymentPermDef;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.perm.AccountDefrayalPermDef;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.perm.StatementAdjustPermDef;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLogger;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementService;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.SourceBillTypeBox;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.TypeBuildingBox;
import com.hd123.m3.account.gwt.statement.client.ui.gadget.TypeUCNCodec;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.dd.CStatementType;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementDef;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementLineDef;
import com.hd123.m3.account.gwt.statement.intf.client.dd.StatementTypeDef;
import com.hd123.m3.account.gwt.statement.intf.client.perm.StatementPermDef;
import com.hd123.m3.account.gwt.subject.intf.client.dd.CSubjectType;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.biz.IsStandardBill;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmSearchPage;
import com.hd123.m3.commons.gwt.util.client.flecs.BUCNCodec;
import com.hd123.m3.commons.gwt.util.client.grid.batch.PagingGridBatchProcessCallback;
import com.hd123.m3.commons.gwt.util.client.grid.batch.PagingGridBatchProcessor;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.widget.client.biz.PrintingTemplate;
import com.hd123.m3.commons.gwt.widget.client.ui.OptionComboBox;
import com.hd123.m3.commons.gwt.widget.client.ui.print.BeforePrintCallback;
import com.hd123.m3.commons.gwt.widget.client.ui.print.BeforePrintHandler;
import com.hd123.m3.commons.gwt.widget.client.ui.print.PrintButton;
import com.hd123.rumba.commons.gwt.mini.client.codec.CodecUtils;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
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
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
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
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarMenuButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * 搜索页面
 * 
 * @author huangjunxian
 * 
 */
public class StatementSearchPage extends BaseBpmSearchPage implements StatementUrlParams.Search,
    BeforePrintHandler {
  private static String FIXTABLE_STYLE_NAME = "fixTable";

  public static StringFieldDef permGroup = new StringFieldDef(FIELD_PERMGROUP,
      StatementMessages.M.permGroup());
  public static StringFieldDef invoiceReg = new StringFieldDef("invoiceReg",
      StatementMessages.M.invoiceReg());
  public static DateFieldDef accountRange = new DateFieldDef("accountRange",
      PStatementDef.constants.accountRange(), false, null, true, null, true, GWTFormat.fmt_yMd,
      false);
  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(FIELD_COUNTERPARTTYPE,
      EPStatement.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE,
          GRes.R.counterpartType()));

  public static EmbeddedFieldDef typePosition = new EmbeddedFieldDef(
      StatementUrlParams.Flecs.FIELD_POSITION, "位置");
  public static EmbeddedFieldDef typeBuilding = new EmbeddedFieldDef(
      StatementUrlParams.Flecs.FIELD_BUILDING, "楼宇");

  private static StatementSearchPage instance;

  public static StatementSearchPage getInstance() {
    if (instance == null)
      instance = new StatementSearchPage();
    return instance;
  }

  public StatementSearchPage() {
    super();
    PStatementDef.counterpart.setCaption(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    PStatementDef.accountUnit.setCaption(getEP().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private Handler_action actionHandler = new Handler_action();
  private Handler_menuItem menuItemHandler = new Handler_menuItem();

  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;
  private RAction calcAction;
  private RAction accountSettleAction;
  private RToolbarMenuButton moreButton;
  protected PrintButton printButton = null;

  private JumpParameters params;
  private String keyword;
  private RFlecsPanel flecsPanel;
  private FlecsConfigCodec flecsCodec;
  private PageSort pageSort;

  private RVerticalPanel root;
  private RGrid grid;
  private RPagingGrid<BStatement> pagingGrid;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef contractCodeCol;
  private RGridColumnDef contractNameCol;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef settleStateCol;
  private RGridColumnDef settleNoCol;
  private RGridColumnDef settlementCol;// 费用周期
  private RGridColumnDef accountTimeCol;
  private RGridColumnDef receiptTotalCol;
  private RGridColumnDef receiptedCol;
  private RGridColumnDef owedAmountCol;
  private RGridColumnDef payTotalCol;
  private RGridColumnDef payedCol;
  private RGridColumnDef typeCol;

  private RMenuItem editMenuItem;
  private RMenuItem deleteMenuItem;
  private RMenuItem effectMenuItem;
  private RMenuItem abortMenuItem;
  private RMenuItem payIvcMenuItem;
  private RMenuItem payMenuItem;
  private RMenuItem receiptIvcMenuItem;
  private RMenuItem receiptMenuItem;
  private RMenuItem adjMenuItem;
  private RMenuItem subjectMenuItem;

  private PagingGridBatchProcessor<BStatement> batchProcessor;

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());

    calcAction = new RAction(StatementMessages.M.calc(), actionHandler);
    RToolbarButton calcButton = new RToolbarButton(calcAction);
    getToolbar().add(calcButton);

    getToolbar().addSeparator();

    deleteAction = new RAction(RActionFacade.DELETE, actionHandler);
    effectAction = new RAction(StatementMessages.M.effect(), actionHandler);
    abortAction = new RAction(StatementMessages.M.abort(), actionHandler);
    accountSettleAction = new RAction(StatementMessages.M.accountSettelLog(), actionHandler);

    moreButton = new RToolbarMenuButton(StatementMessages.M.operate(), moreMenu);
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
    root = new RVerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    root.setStyleName(FIXTABLE_STYLE_NAME);
    initWidget(root);

    root.add(drawFlecsAndGrid());
  }

  private void afterDraw() {
    batchProcessor = new PagingGridBatchProcessor<BStatement>(pagingGrid, getEP()
        .getModuleCaption());
    flecsCodec = new FlecsConfigCodec();
    flecsCodec.addOperatorCodec(new DefaultOperatorCodec());
    flecsCodec.addOperandCodec(new SPositionCodec());
    flecsCodec.addOperandCodec(new TypeUCNCodec());
    flecsCodec.addOperandCodec(new BUCNCodec());
  }

  private Widget drawFlecsAndGrid() {
    drawLineMenu();

    drawGrid();
    pagingGrid = new RPagingGrid<BStatement>(grid, new GridDataProvider());
    pagingGrid.addLoadDataHandler(this);
    pagingGrid.setWidth("100%");

    drawFlecsPanel();

    return flecsPanel;
  }

  private void drawGrid() {
    grid = new RGrid();
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
        for (BStatement o : pagingGrid.getSelections()) {
          if (StringUtil.isNullOrBlank(o.getStore().getUuid()) == false) {
            storeCodes.add(o.getStore().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    billNumberCol = new RGridColumnDef(PStatementDef.billNumber);
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setWidth("180px");
    billNumberCol.setSortable(true);
    billNumberCol.setResizable(true);
    billNumberCol.setName(FIELD_BILLNUMBER);
    billNumberCol.setAllowHiding(false);
    grid.addColumnDef(billNumberCol);

    bizStateCol = new RGridColumnDef(PStatementDef.bizState);
    bizStateCol.setWidth("80px");
    bizStateCol.setSortable(true);
    bizStateCol.setResizable(true);
    bizStateCol.setName(FIELD_BIZSTATE);
    grid.addColumnDef(bizStateCol);

    contractCodeCol = new RGridColumnDef(PStatementDef.contract);
    contractCodeCol.setWidth("160px");
    contractCodeCol.setSortable(true);
    contractCodeCol.setResizable(true);
    contractCodeCol.setName(FIELD_CONTRACTNUMBER);
    grid.addColumnDef(contractCodeCol);

    contractNameCol = new RGridColumnDef(PStatementDef.contract_name);
    contractNameCol.setWidth("160px");
    contractNameCol.setSortable(true);
    contractNameCol.setResizable(true);
    contractNameCol.setName(FIELD_CONTRACTNAME);
    grid.addColumnDef(contractNameCol);

    counterpartCol = new RGridColumnDef(PStatementDef.counterpart);
    counterpartCol.setWidth("120px");
    counterpartCol.setSortable(true);
    counterpartCol.setResizable(true);
    counterpartCol.setName(FIELD_COUNTERPART);
    grid.addColumnDef(counterpartCol);

    settleStateCol = new RGridColumnDef(PStatementDef.settleState);
    settleStateCol.setWidth("120px");
    settleStateCol.setSortable(true);
    settleStateCol.setResizable(true);
    settleStateCol.setName(FIELD_SETTLESTATE);
    grid.addColumnDef(settleStateCol);

    settleNoCol = new RGridColumnDef(PStatementDef.settleNo);
    settleNoCol.setWidth("100px");
    settleNoCol.setSortable(true);
    settleNoCol.setResizable(true);
    settleNoCol.setName(FIELD_SETTLENO);
    grid.addColumnDef(settleNoCol);

    settlementCol = new RGridColumnDef(PStatementDef.settlement);
    settlementCol.setWidth("246px");
    settlementCol.setResizable(true);
    grid.addColumnDef(settlementCol);

    accountTimeCol = new RGridColumnDef(PStatementDef.accountTime);
    accountTimeCol.setWidth("100px");
    accountTimeCol.setSortable(true);
    accountTimeCol.setResizable(true);
    accountTimeCol.setName(FIELD_ACCOUNTTIME);
    grid.addColumnDef(accountTimeCol);

    receiptTotalCol = new RGridColumnDef(PStatementDef.receiptTotal);
    receiptTotalCol.setWidth("100px");
    receiptTotalCol.setSortable(true);
    receiptTotalCol.setResizable(true);
    receiptTotalCol.setName(FIELD_RECEIPTTOTOAL);
    receiptTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(receiptTotalCol);

    receiptedCol = new RGridColumnDef(PStatementDef.receipted);
    receiptedCol.setWidth("100px");
    receiptedCol.setSortable(true);
    receiptedCol.setResizable(true);
    receiptedCol.setName(FIELD_RECEIPTED);
    receiptedCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(receiptedCol);

    payTotalCol = new RGridColumnDef(PStatementDef.payTotal);
    payTotalCol.setWidth("100px");
    payTotalCol.setSortable(true);
    payTotalCol.setResizable(true);
    payTotalCol.setName(FIELD_PAYTOTAL);
    payTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(payTotalCol);

    payedCol = new RGridColumnDef(PStatementDef.payed);
    payedCol.setWidth("100px");
    payedCol.setSortable(true);
    payedCol.setResizable(true);
    payedCol.setName(FIELD_PAYED);
    payedCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(payedCol);

    owedAmountCol = new RGridColumnDef(StatementMessages.M.owedAmount());
    owedAmountCol.setWidth("100px");
    owedAmountCol.setResizable(true);
    owedAmountCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(owedAmountCol);

    typeCol = new RGridColumnDef(PStatementDef.type);
    typeCol.setWidth("150px");
    typeCol.setSortable(true);
    typeCol.setResizable(true);
    typeCol.setName(FIELD_TYPE);
    grid.addColumnDef(typeCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  private void drawLineMenu() {
    editMenuItem = new RMenuItem(RActionFacade.EDIT, menuItemHandler);
    editMenuItem.setHotKey(null);

    deleteMenuItem = new RMenuItem(RActionFacade.DELETE, menuItemHandler);
    deleteMenuItem.setHotKey(null);

    effectMenuItem = new RMenuItem(StatementMessages.M.effect(), menuItemHandler);
    effectMenuItem.setHotKey(null);

    abortMenuItem = new RMenuItem(StatementMessages.M.abort(), menuItemHandler);
    abortMenuItem.setHotKey(null);

    payIvcMenuItem = new RMenuItem(StatementMessages.M.payIvc(), menuItemHandler);
    payIvcMenuItem.setHotKey(null);

    payMenuItem = new RMenuItem(StatementMessages.M.pay(), menuItemHandler);
    payMenuItem.setHotKey(null);

    receiptIvcMenuItem = new RMenuItem(StatementMessages.M.receiptIvc(), menuItemHandler);
    receiptIvcMenuItem.setHotKey(null);

    receiptMenuItem = new RMenuItem(StatementMessages.M.receipt(), menuItemHandler);
    receiptMenuItem.setHotKey(null);

    adjMenuItem = new RMenuItem(StatementMessages.M.adjust(), menuItemHandler);
    adjMenuItem.setHotKey(null);

    subjectMenuItem = new RMenuItem(StatementMessages.M.subjectInfo(), menuItemHandler);
    subjectMenuItem.setHotKey(null);
  }

  private void drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());

    flecsPanel.addField(PStatementDef.billNumber);
    flecsPanel.addField(PStatementDef.bizState);
    if (getEP().isPermEnabled()) {
      flecsPanel.addField(permGroup);
    }
    flecsPanel.addField(PStatementDef.payTotalTotal);
    flecsPanel.addField(PStatementDef.receiptTotalTotal);
    flecsPanel.addField(counterpartType);
    flecsPanel.addField(PStatementDef.counterpart);
    flecsPanel.addField(PStatementDef.contract_code);
    flecsPanel.addField(PStatementDef.contract_name);
    flecsPanel.addField(PStatementDef.settleNo);
    flecsPanel.addField(PStatementDef.accountTime);
    flecsPanel.addField(accountRange);
    flecsPanel.addField(PStatementDef.settleState);
    flecsPanel.addField(typePosition);
    flecsPanel.addField(typeBuilding);
    flecsPanel.addField(PStatementLineDef.acc1_subject);
    flecsPanel.addField(PStatementDef.type);
    flecsPanel.addField(PStatementLineDef.acc1_sourceBill_billType);
    flecsPanel.addField(PStatementLineDef.acc1_sourceBill_billNumber);
    flecsPanel.addField(invoiceReg);
    flecsPanel.addField(PStatementDef.accountUnit);
    flecsPanel.addField(PStatementDef.coopMode);
    flecsPanel.addField(PStatementDef.contractCategory);
    flecsPanel.addField(PStatementDef.createInfo_operator_id);
    flecsPanel.addField(PStatementDef.createInfo_operator_fullName);
    flecsPanel.addField(PStatementDef.createInfo_time);
    flecsPanel.addField(PStatementDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PStatementDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PStatementDef.lastModifyInfo_time);
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

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);
    this.params = params;

    if (checkIn() == false) {
      return;
    }
    EPStatement.getInstance().log(
        "进入模块:" + EPStatement.getInstance().getModuleCaption() + "搜索页面,时间"
            + GWTFormat.fmt_yMdHmsS.format(new Date()));

    root.add(flecsPanel);
    decodeParams(params);
    refreshTitle();
    refreshFlecs();
    refreshCommonds();
  }

  private void refreshCommonds() {
    deleteAction.setVisible(getEP().isPermitted(StatementPermDef.DELETE));
    effectAction.setVisible(getEP().isPermitted(StatementPermDef.EFFECT));
    abortAction.setVisible(getEP().isPermitted(StatementPermDef.ABORT));
    accountSettleAction.setVisible(getEP().isPermitted(AccountSettlePermDef.ACCSETTLE));
    printButton.setEnabled(getEP().isPermitted(BAction.PRINT.getKey()));

    boolean visible = false;
    for (Widget item : moreMenu.getItems()) {
      if (item.isVisible()) {
        visible = true;
        break;
      }
    }
    if (visible == false) {
      visible = deleteAction.isVisible() || effectAction.isVisible() || abortAction.isVisible()
          || accountSettleAction.isVisible();
    }
    moreButton.setVisible(visible);
    getToolbar().rebuild();
  }

  private boolean checkIn() {
    if (getEP().isPermitted(StatementPermDef.READ) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void decodeParams(JumpParameters params) {
    keyword = params.getUrlRef().get(PN_KEYWORD);
  }

  private void refreshTitle() {
    getEP().getTitleBar().clearStandardTitle();
    getEP().getTitleBar().setTitleText(StatementMessages.M.search());
    getEP().getTitleBar().appendAttributeText(getEP().getModuleCaption());
  }

  private void refreshFlecs() {
    grid.setSort(billNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel.addCondition(new Condition(PStatementDef.billNumber, DefaultOperator.STARTS_WITH,
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
    conditions.add(new Condition(PStatementDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PStatementDef.bizState, DefaultOperator.EQUALS, null));
    if (getEP().isPermEnabled()) {
      conditions.add(new Condition(permGroup, DefaultOperator.EQUALS, null));
    }
    conditions.add(new Condition(counterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PStatementDef.counterpart, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PStatementDef.contract_code, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PStatementDef.contract_name, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PStatementDef.settleNo, DefaultOperator.EQUALS, SettleNoField
        .getCurrentSettleNo()));
    return conditions;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {

  }

  public JumpParameters getLastParams() {
    if (params != null)
      params.getMessages().clear();
    return params;
  }

  private String buildTotalStr(BigDecimal value) {
    if (value == null)
      return M3Format.fmt_money.format(0);
    return M3Format.fmt_money.format(value.doubleValue());
  }

  private class GridDataProvider implements RPageDataProvider<BStatement> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BStatement>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);
      if (sortField != null)
        pageSort.appendOrder(sortField, sortDir);
      else
        pageSort.appendOrder(billNumberCol.getName(), OrderDir.desc);

      StatementService.Locator.getService().query(flecsPanel.getCurrentConfig().getConditions(),
          flecsPanel.getCurrentConfig().getVisibleColumnNames(), pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BStatement rowData, List<BStatement> pageData) {
      if (row == 0 && col == 0) {
        EPStatement.getInstance()
            .log("渲染第一行数据:" + ",时间" + GWTFormat.fmt_yMdHmsS.format(new Date()));
      }
      if (row == pageData.size() - 1 && col == 0) {
        EPStatement.getInstance().log(
            "渲染最后一行数据:" + ",时间" + GWTFormat.fmt_yMdHmsS.format(new Date()));
      }

      if (col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      else if (col == bizStateCol.getIndex())
        return rowData.getBizState() == null ? null : PStatementDef.bizState.getEnumCaption(rowData
            .getBizState());
      else if (col == contractCodeCol.getIndex())
        return rowData.getContract() == null ? null : rowData.getContract().getCode();
      else if (col == contractNameCol.getIndex())
        return rowData.getContract() == null ? null : rowData.getContract().getName();
      else if (col == counterpartCol.getIndex())
        return rowData == null ? null : rowData.getCounterpart().toFriendlyStr(
            getEP().getCounterpartTypeMap());
      else if (col == settleStateCol.getIndex())
        return rowData.getSettleState() == null ? null : PStatementDef.settleState
            .getEnumCaption(rowData.getSettleState());
      else if (col == settleNoCol.getIndex())
        return rowData.getSettleNo();
      else if (col == settlementCol.getIndex()) {
        return CollectionUtil.toString(rowData.getRanges(), '，', ' ');
      } else if (col == accountTimeCol.getIndex())
        return rowData.getAccountTime() == null ? null : M3Format.fmt_yMd.format(rowData
            .getAccountTime());
      else if (col == payTotalCol.getIndex())
        return rowData.getPayTotal() == null ? null : buildTotalStr(rowData.getPayTotal()
            .getTotal());
      else if (col == payedCol.getIndex())
        return buildTotalStr(rowData.getPayed());
      else if (col == receiptTotalCol.getIndex())
        return rowData.getReceiptTotal() == null ? null : buildTotalStr(rowData.getReceiptTotal()
            .getTotal());
      else if (col == receiptedCol.getIndex())
        return buildTotalStr(rowData.getReceipted());
      else if (col == typeCol.getIndex())
        return CStatementType.patch.equals(rowData.getType()) ? StatementTypeDef.constants.patch()
            : StatementTypeDef.constants.normal();
      else if (col == owedAmountCol.getIndex()) {
        return buildOwedAmountStr(rowData);
      } else
        return null;
    }
  }

  /*** 构造欠款金额：应收-实收-（应付-实付） */
  private String buildOwedAmountStr(BStatement statement) {
    BigDecimal owedAmount = BigDecimal.ZERO;
    if (statement.getReceiptTotal() != null && statement.getReceiptTotal().getTotal() != null) {
      owedAmount = owedAmount.add(statement.getReceiptTotal().getTotal());
    }

    if (statement.getReceipted() != null) {
      owedAmount = owedAmount.subtract(statement.getReceipted());
    }

    if (statement.getPayTotal() != null && statement.getPayTotal().getTotal() != null) {
      owedAmount = owedAmount.subtract(statement.getPayTotal().getTotal());
    }

    if (statement.getPayed() != null) {
      owedAmount = owedAmount.add(statement.getPayed());
    }

    return M3Format.fmt_money.format(owedAmount.doubleValue());

  }

  private class Handler_action implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == deleteAction) {
        doBatchDelete();
      } else if (event.getSource() == effectAction) {
        doEffect();
      } else if (event.getSource() == abortAction) {
        doBatchAbort();
      } else if (event.getSource() == calcAction) {
        doCalc();
      } else if (event.getSource() == accountSettleAction) {
        doAccountSettle();
      }
    }
  }

  private class Handler_menuItem implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BStatement entity = pagingGrid.getPageData().get(hotItem.getGridRow());

      if (event.getSource() == editMenuItem) {
        doEdit(entity);
      } else if (event.getSource() == deleteMenuItem) {
        doConfirmDeleteOne(entity);
      } else if (event.getSource() == effectMenuItem) {
        doConfirmEffectOne(entity);
      } else if (event.getSource() == abortMenuItem) {
        doConfirmAbortOne(entity);
      } else if (event.getSource() == payIvcMenuItem) {
        getEP().doPaymentIvc(entity);
      } else if (event.getSource() == payMenuItem) {
        getEP().doPayment(entity);
      } else if (event.getSource() == receiptIvcMenuItem) {
        getEP().doReceiptIvc(entity);
      } else if (event.getSource() == receiptMenuItem) {
        getEP().doReceipt(entity);
      } else if (event.getSource() == adjMenuItem) {
        getEP().doAdjust(entity);
      } else if (event.getSource() == subjectMenuItem) {
        getEP().doSubjectInfo(entity);
      }
    }
  }

  private class Handler_grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      BStatement entity = pagingGrid.getRowData(cell.getRow());
      if (entity == null)
        return;

      // 本模块内的跳转
      if (cell.getColumnDef().equals(billNumberCol)) {
        getEP().jumpToViewPage(entity.getUuid());
      }
    }
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (PStatementDef.settleNo == field) {
        SettleNoField settleNoField = new SettleNoField(PStatementDef.settleNo);
        settleNoField.setMaxLength(6);
        settleNoField.setRequired(false);
        settleNoField.refreshOption(12);
        return settleNoField;
      } else if (PStatementDef.counterpart == field) {
        return buildCounterpart();
      } else if (permGroup == field) {
        return new PermGroupField(permGroup);
      } else if (PStatementLineDef.acc1_subject == field) {
        return new SubjectUCNBox(CSubjectType.credit, null);
      } else if (PStatementLineDef.acc1_sourceBill_billType == field) {
        SourceBillTypeBox box = new SourceBillTypeBox();
        box.getOptions().removeByValue("statement");
        box.getOptions().removeByValue("invoiceExchange");
        return box;
      } else if (PStatementDef.accountUnit == field) {
        return new AccountUnitUCNBox(null, true);
      } else if (invoiceReg == field) {
        return new InvoiceField();
      } else if (counterpartType == field) {
        return new CounterpartTypeField(counterpartType);
      } else if (field == PStatementDef.coopMode) {
        RComboBox<String> coopModeField = new RComboBox<String>();
        coopModeField.setMaxDropdownRowCount(15);
        coopModeField.setEditable(false);
        for (String coopMode : getEP().getCoopModes()) {
          coopModeField.addOption(coopMode);
        }
        return coopModeField;
      } else if (field == typePosition) {
        TypePositionBox widget = new TypePositionBox();
        widget.refreshPositionSubTypes(true);
        return widget;
      } else if (field == typeBuilding) {
        return new TypeBuildingBox();
      } else if (field == PStatementDef.contractCategory) {
        OptionComboBox unitBox = new OptionComboBox(StatementUrlParams.KEY_CONTRACT_CATEGORY,
            PStatementDef.constants.contractCategory(), PStatementDef.constants.contractCategory(),
            false);
        unitBox.clearValue();
        unitBox.setNullOptionText("全部");
        unitBox.setConfigable(false);
        unitBox.setRefreshWhenOpen(true);
        unitBox.setMaxDropdownRowCount(10);
        return unitBox;
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
      setNullOptionText(StatementMessages.M.all());
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

  private class InvoiceField extends RComboBox<Boolean> {
    public InvoiceField() {
      super();
      setEditable(false);
      setRequired(false);
      addOption(Boolean.FALSE, StatementMessages.M.unFinished());
      addOption(Boolean.TRUE, StatementMessages.M.finished());
    }
  }

  private class MyOperatorPolicy implements OperatorPolicy {
    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      if (PStatementDef.counterpart == field || PStatementLineDef.acc1_subject == field
          || PStatementDef.accountUnit == field || PStatementDef.settleNo == field
          || PStatementLineDef.acc1_sourceBill_billType == field || invoiceReg == field
          || PStatementDef.type == field || counterpartType == field || typePosition == field
          || typeBuilding == field) {
        List<Operator> result = new ArrayList<Operator>();
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (permGroup == field || PStatementDef.coopMode.equals(field)
          || PStatementDef.contractCategory.equals(field)) {
        List<Operator> result = new ArrayList<Operator>();
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (accountRange == field) {
        List<Operator> result = new ArrayList<Operator>();
        result.add(DefaultOperator.CONTAINS);
        return result;
      } else if (PStatementLineDef.acc1_sourceBill_billNumber == field) {
        List<Operator> result = new ArrayList<Operator>();
        result.add(DefaultOperator.CONTAINS);
        return result;
      }
      return defaultOperators;
    }
  }

  private void doBatchDelete() {
    getMessagePanel().clearMessages();
    batchProcessor.batchProcess(StatementMessages.M.delete(), new BStatement[] {},
        new PagingGridBatchProcessCallback<BStatement>() {

          @Override
          public void execute(final BStatement entity, final BatchProcesser<BStatement> processer,
              final AsyncCallback callback) {
            if (BBizStates.INEFFECT.equals(entity.getBizState()) == false) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            if (CStatementType.normal.equals(entity.getType())) {
              StatementService.Locator.getService().validateBeforeAction(entity.getUuid(),
                  BBizActions.DELETE, new RBAsyncCallback2<String>() {

                    @Override
                    public void onException(Throwable caught) {
                      String msg = StatementMessages.M.actionFailed(StatementMessages.M.delete(),
                          StatementUrlParams.MODULE_CAPTION);
                      RMsgBox.showError(msg, caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                      if (result != null) {
                        String msg = (result == null ? "" : result)
                            + StatementMessages.M.actionComfirm2(StatementMessages.M.delete(),
                                StatementUrlParams.MODULE_CAPTION, entity.getBillNumber());
                        RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

                          @Override
                          public void onClosed(boolean confirmed) {
                            if (confirmed == false) {
                              processer.getReport().reportSkipped();
                              processer.next();
                              return;
                            }
                            StatementService.Locator.getService().remove(entity.getUuid(),
                                entity.getVersion(), callback);
                            BStatementLogger.getInstance()
                                .log(StatementMessages.M.delete(), entity);
                          }
                        });
                      } else {
                        StatementService.Locator.getService().remove(entity.getUuid(),
                            entity.getVersion(), callback);
                        BStatementLogger.getInstance().log(StatementMessages.M.delete(), entity);
                      }
                    }
                  });
            } else {
              StatementService.Locator.getService().remove(entity.getUuid(), entity.getVersion(),
                  callback);
              BStatementLogger.getInstance().log(StatementMessages.M.delete(), entity);
            }
          }
        });
  }

  private void doEffect() {
    getMessagePanel().clearMessages();
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(StatementMessages.M.seleteDataToAction(StatementMessages.M.effect(),
          getEP().getModuleCaption()));
      return;
    }
    batchProcessor.batchProcess(StatementMessages.M.effect(), new BStatement[] {},
        new PagingGridBatchProcessCallback<BStatement>() {
          @Override
          public void execute(BStatement entity, BatchProcesser<BStatement> processer,
              AsyncCallback callback) {
            if (!BBizStates.INEFFECT.equals(entity.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            StatementService.Locator.getService().effect(entity.getUuid(), entity.getVersion(),
                callback);
            BStatementLogger.getInstance().log(StatementMessages.M.effect(), entity);
          }
        });

  }

  private void doBatchAbort() {
    getMessagePanel().clearMessages();
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(StatementMessages.M.seleteDataToAction(StatementMessages.M.abort(), getEP()
          .getModuleCaption()));
      return;
    }
    batchProcessor.batchProcess(StatementMessages.M.abort(), new BStatement[] {},
        new PagingGridBatchProcessCallback<BStatement>() {
          @Override
          public void execute(final BStatement entity, final BatchProcesser<BStatement> processer,
              final AsyncCallback callback) {
            if (!BBizStates.EFFECT.equals(entity.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            if (CStatementType.normal.equals(entity.getType())) {
              StatementService.Locator.getService().validateBeforeAction(entity.getUuid(),
                  BBizActions.ABORT, new RBAsyncCallback2<String>() {

                    @Override
                    public void onException(Throwable caught) {
                      String msg = StatementMessages.M.actionFailed(StatementMessages.M.abort(),
                          StatementUrlParams.MODULE_CAPTION);
                      RMsgBox.showError(msg, caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                      if (result != null) {
                        String msg = (result == null ? "" : result)
                            + StatementMessages.M.actionComfirm2(StatementMessages.M.abort(),
                                StatementUrlParams.MODULE_CAPTION, entity.getBillNumber());
                        RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

                          @Override
                          public void onClosed(boolean confirmed) {
                            if (confirmed == false) {
                              processer.getReport().reportSkipped();
                              processer.next();
                              return;
                            }
                            StatementService.Locator.getService().abort(entity.getUuid(),
                                entity.getVersion(), callback);
                            BStatementLogger.getInstance().log(StatementMessages.M.abort(), entity);
                          }
                        });
                      } else {
                        StatementService.Locator.getService().abort(entity.getUuid(),
                            entity.getVersion(), callback);
                        BStatementLogger.getInstance().log(StatementMessages.M.abort(), entity);
                      }
                    }
                  });
            } else {
              StatementService.Locator.getService().abort(entity.getUuid(), entity.getVersion(),
                  callback);
              BStatementLogger.getInstance().log(StatementMessages.M.abort(), entity);
            }
          }
        });
  }

  private void doCalc() {
    GwtUrl url = AccountSettleUrlParams.ENTRY_URL;
    url.getQuery().set(JumpParameters.PN_START, AccountSettleUrlParams.AccountSettle.START_NODE);
    try {
      RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
    } catch (Exception e) {
      String msg = StatementMessages.M.cannotNavigate(url.toString());
      RMsgBox.showError(msg, e);
    }
  }

  private void doAccountSettle() {
    JumpParameters jumParams = new JumpParameters(StatementAccSettleLogPage.START_NODE);
    getEP().jump(jumParams);
  }

  private void doEdit(BStatement entity) {
    JumpParameters jumParams = null;
    if (CStatementType.patch.equals(entity.getType())) {
      jumParams = new JumpParameters(StatementEditPatchPage.START_NODE);
    } else {
      jumParams = new JumpParameters(StatementEditPage.START_NODE);
    }
    jumParams.getUrlRef().set(StatementEditPatchPage.PN_UUID, entity.getUuid());
    getEP().jump(jumParams);
  }

  private void doConfirmDeleteOne(final BStatement entity) {
    getMessagePanel().clearMessages();
    if (CStatementType.normal.equals(entity.getType())) {
      StatementService.Locator.getService().validateBeforeAction(entity.getUuid(),
          BBizActions.DELETE, new RBAsyncCallback2<String>() {

            @Override
            public void onException(Throwable caught) {
              String msg = StatementMessages.M.actionFailed(StatementMessages.M.delete(),
                  StatementUrlParams.MODULE_CAPTION);
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(String result) {
              String msg = (result == null ? "" : result)
                  + StatementMessages.M.actionComfirm2(StatementMessages.M.delete(),
                      StatementUrlParams.MODULE_CAPTION, entity.getBillNumber());
              RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

                @Override
                public void onClosed(boolean confirmed) {
                  if (confirmed == false)
                    return;
                  doDeleteOne(entity);
                }
              });
            }
          });
    } else {
      String msg = StatementMessages.M.actionComfirm2(StatementMessages.M.delete(),
          StatementUrlParams.MODULE_CAPTION, entity.getBillNumber());
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

        @Override
        public void onClosed(boolean confirmed) {
          if (confirmed == false)
            return;
          doDeleteOne(entity);
        }
      });
    }

  }

  private void doDeleteOne(final BStatement entity) {
    RLoadingDialog.show(StatementMessages.M.actionDoing(StatementMessages.M.delete()));
    StatementService.Locator.getService().remove(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementMessages.M.actionFailed(StatementMessages.M.delete(),
                StatementUrlParams.MODULE_CAPTION);
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {

              @Override
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BStatementLogger.getInstance().log(StatementMessages.M.delete(), entity);

            getMessagePanel().clearMessages();
            getMessagePanel().putInfoMessage(
                StatementMessages.M.onSuccess(StatementMessages.M.delete(),
                    StatementUrlParams.MODULE_CAPTION, entity.getBillNumber()));
            pagingGrid.refresh();
          }
        });
  }

  private void doConfirmEffectOne(final BStatement entity) {
    getMessagePanel().clearMessages();
    String msg = StatementMessages.M.actionComfirm2(StatementMessages.M.effect(), getEP()
        .getModuleCaption(), entity.getBillNumber());

    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEffectOne(entity);
      }
    });
  }

  private void doEffectOne(final BStatement entity) {
    RLoadingDialog.show(StatementMessages.M.actionDoing(StatementMessages.M.effect()));
    StatementService.Locator.getService().effect(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementMessages.M.actionFailed(StatementMessages.M.effect(), getEP()
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
            BStatementLogger.getInstance().log(StatementMessages.M.effect(), entity);

            String msg = StatementMessages.M.onSuccess(StatementMessages.M.effect(), getEP()
                .getModuleCaption(), entity.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doConfirmAbortOne(final BStatement entity) {
    getMessagePanel().clearMessages();
    if (CStatementType.normal.equals(entity.getType())) {
      StatementService.Locator.getService().validateBeforeAction(entity.getUuid(),
          BBizActions.ABORT, new RBAsyncCallback2<String>() {

            @Override
            public void onException(Throwable caught) {
              String msg = StatementMessages.M.actionFailed(StatementMessages.M.abort(),
                  StatementUrlParams.MODULE_CAPTION);
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(String result) {
              String msg = (result == null ? "" : result)
                  + StatementMessages.M.actionComfirm2(StatementMessages.M.abort(),
                      StatementUrlParams.MODULE_CAPTION, entity.getBillNumber());
              RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {

                @Override
                public void onClosed(boolean confirmed) {
                  if (confirmed == false)
                    return;
                  doAbortOne(entity);
                }
              });
            }
          });
    } else {
      String msg = StatementMessages.M.actionComfirm2(StatementMessages.M.abort(), getEP()
          .getModuleCaption(), entity.getBillNumber());

      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        public void onClosed(boolean confirmed) {
          if (confirmed == false)
            return;
          doAbortOne(entity);
        }
      });
    }
  }

  private void doAbortOne(final BStatement entity) {
    RLoadingDialog.show(StatementMessages.M.actionDoing(StatementMessages.M.abort()));
    StatementService.Locator.getService().abort(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementMessages.M.actionFailed(StatementMessages.M.abort(), getEP()
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
            BStatementLogger.getInstance().log(StatementMessages.M.abort(), entity);

            String msg = StatementMessages.M.onSuccess(StatementMessages.M.abort(), getEP()
                .getModuleCaption(), entity.getBillNumber());
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
  protected EPStatement getEP() {
    return EPStatement.getInstance();
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
    menu.addItem(new RMenuItem(accountSettleAction));
  }

  @Override
  protected void appendLineMenu(RPopupMenu menu, Object bill) {
    BStatement entity = (BStatement) bill;
    menu.addSeparator();
    menu.addItem(editMenuItem);
    menu.addItem(deleteMenuItem);
    menu.addSeparator();
    menu.addItem(effectMenuItem);
    menu.addItem(abortMenuItem);
    menu.addSeparator();
    menu.addItem(payIvcMenuItem);
    menu.addItem(payMenuItem);
    menu.addItem(receiptIvcMenuItem);
    menu.addItem(receiptMenuItem);
    menu.addSeparator();
    menu.addItem(adjMenuItem);
    menu.addItem(subjectMenuItem);

    boolean canEdit = getEP().isPermitted(StatementPermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(StatementPermDef.DELETE);
    boolean canEffect = getEP().isPermitted(StatementPermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(StatementPermDef.ABORT);

    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());

    editMenuItem.setVisible(isInEffect && canEdit);
    deleteMenuItem.setVisible(isInEffect && canDelete);
    effectMenuItem.setVisible(isInEffect && canEffect);
    abortMenuItem.setVisible(isEffect && canAbort);

    // 其它按钮
    payIvcMenuItem.setVisible(isEffect && getEP().isPermitted(PayInvoiceRegPermDef.CREATE));
    payMenuItem.setVisible(isEffect && getEP().isPermitted(PaymentPermDef.CREATE));
    receiptIvcMenuItem.setVisible(isEffect && getEP().isPermitted(RecInvoiceRegPermDef.CREATE));
    receiptMenuItem.setVisible(isEffect && getEP().isPermitted(ReceiptPermDef.CREATE));
    adjMenuItem.setVisible(isInEffect && CStatementType.normal.equals(entity.getType())
        && getEP().isPermitted(StatementAdjustPermDef.CREATE));
    subjectMenuItem.setVisible(isEffect && getEP().isPermitted(AccountDefrayalPermDef.READ));

    menu.minimizeSeparators();
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
