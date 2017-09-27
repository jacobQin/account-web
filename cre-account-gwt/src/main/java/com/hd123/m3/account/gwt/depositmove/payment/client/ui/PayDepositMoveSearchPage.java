/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PayDepositMoveSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.payment.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMoveLogger;
import com.hd123.m3.account.gwt.depositmove.intf.client.DepositMoveMessage;
import com.hd123.m3.account.gwt.depositmove.intf.client.dd.PDepositMoveDef;
import com.hd123.m3.account.gwt.depositmove.payment.client.EPPayDepositMove;
import com.hd123.m3.account.gwt.depositmove.payment.client.rpc.PayDepositMoveService;
import com.hd123.m3.account.gwt.depositmove.payment.intf.client.PayDepositMoveUrlParams;
import com.hd123.m3.account.gwt.depositmove.payment.intf.client.PayDepositMoveUrlParams.Search;
import com.hd123.m3.account.gwt.depositmove.payment.intf.client.perm.PayDepositMovePermDef;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.biz.IsStandardBill;
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
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RDateTimeRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHyperlinkFieldRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarMenuButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class PayDepositMoveSearchPage extends BaseBpmSearchPage implements Search,
    BeforePrintHandler {

  private static String FIXTABLE_STYLE_NAME = "fixTable";

  private static EmbeddedFieldDef businessUnit = new EmbeddedFieldDef("businessUnit",
      EPPayDepositMove.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
  private static EmbeddedFieldDef permGroup = new EmbeddedFieldDef(FIELD_PERMGROUP,
      DepositMoveMessage.M.permGroup());
  private static StringFieldDef outContract_name = new StringFieldDef("outContract.name",
      WidgetRes.M.outContractName(), true, 0, 64);
  private static StringFieldDef inContract_name = new StringFieldDef("inContract.name",
      WidgetRes.M.inContractName(), true, 0, 64);

  private static EmbeddedFieldDef outCounterpartType = new EmbeddedFieldDef(
      PayDepositMoveUrlParams.Flecs.FIELD_OUTCOUNTERPARTTYPE, "转出"
          + EPPayDepositMove.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE,
              GRes.R.counterpartType()));
  private static EmbeddedFieldDef inCounterpartType = new EmbeddedFieldDef(
      PayDepositMoveUrlParams.Flecs.FIELD_INCOUNTERPARTTYPE, "转入"
          + EPPayDepositMove.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE,
              GRes.R.counterpartType()));

  private static PayDepositMoveSearchPage instance = null;

  public static PayDepositMoveSearchPage getInstance() {
    if (instance == null) {
      instance = new PayDepositMoveSearchPage();
    }
    return instance;
  }

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

  private RGrid grid;
  private RPagingGrid<BDepositMove> pagingGrid;
  private PagingGridBatchProcessor<BDepositMove> batchProcessor;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef outCountPartCol;
  private RGridColumnDef outContractNumberCol;
  private RGridColumnDef outContractNameCol;
  private RGridColumnDef outSubjectCol;
  private RGridColumnDef accountDateCol;
  private RGridColumnDef amountCol;
  private RGridColumnDef inCountPartCol;
  private RGridColumnDef inContractNumberCol;
  private RGridColumnDef inContractNameCol;
  private RGridColumnDef inSubjectCol;
  private RGridColumnDef remarkCol;
  private RGridColumnDef setterNoCol;

  private RMenuItem editMenuItem;
  private RMenuItem deleteMenuItem;
  private RMenuItem effectMenuItem;
  private RMenuItem abortMenuItem;

  public PayDepositMoveSearchPage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());
    getToolbar().addSeparator();

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    effectAction = new RAction(DepositMoveMessage.M.effect(), new Handler_effectAction());
    abortAction = new RAction(DepositMoveMessage.M.abort(), new Handler_abortAction());

    moreButton = new RToolbarMenuButton(DepositMoveMessage.M.operate(), moreMenu);
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
    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    root.setStyleName(FIXTABLE_STYLE_NAME);
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
    editMenuItem = new RMenuItem(RActionFacade.EDIT, new Handler_editMenuItem());
    editMenuItem.setHotKey(null);

    deleteMenuItem = new RMenuItem(RActionFacade.DELETE, new Handler_deleteMenuItem());
    deleteMenuItem.setHotKey(null);

    effectMenuItem = new RMenuItem(DepositMoveMessage.M.effect(), new Handler_effectMenuItem());
    effectMenuItem.setHotKey(null);

    abortMenuItem = new RMenuItem(DepositMoveMessage.M.abort(), new Handler_abortMenuItem());
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
        for (BDepositMove o : pagingGrid.getSelections()) {
          if (StringUtil.isNullOrBlank(o.getStore().getUuid()) == false) {
            storeCodes.add(o.getStore().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    billNumberCol = new RGridColumnDef(PDepositMoveDef.billNumber);
    billNumberCol.setSortable(true);
    billNumberCol.setName(PayDepositMoveUrlParams.Search.FIELD_BILLNUMBER);
    billNumberCol.setWidth("180px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setAllowHiding(false);
    grid.addColumnDef(billNumberCol);

    bizStateCol = new RGridColumnDef(PDepositMoveDef.constants.bizState());
    bizStateCol.setSortable(true);
    bizStateCol.setName(PayDepositMoveUrlParams.Search.FIELD_BIZSTATE);
    bizStateCol.setWidth("60px");
    grid.addColumnDef(bizStateCol);

    outCountPartCol = new RGridColumnDef(DepositMoveMessage.M.out()
        + EPPayDepositMove.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
            GRes.R.counterpart()));
    outCountPartCol.setSortable(true);
    outCountPartCol.setName(PayDepositMoveUrlParams.Search.FIELD_OUTCOUNTERPART);
    outCountPartCol.setWidth("120px");
    grid.addColumnDef(outCountPartCol);

    outContractNumberCol = new RGridColumnDef(DepositMoveMessage.M.out()
        + PDepositMoveDef.constants.outContract_code());
    outContractNumberCol.setSortable(true);
    outContractNumberCol.setName(PayDepositMoveUrlParams.Search.FIELD_OUTCONTRACTNUMBER);
    outContractNumberCol.setWidth("120px");
    grid.addColumnDef(outContractNumberCol);

    outContractNameCol = new RGridColumnDef(DepositMoveMessage.M.out()
        + PDepositMoveDef.constants.outContract_name());
    outContractNameCol.setSortable(true);
    outContractNameCol.setName(PayDepositMoveUrlParams.Search.FIELD_OUTCONTRACTNAME);
    outContractNameCol.setWidth("120px");
    grid.addColumnDef(outContractNameCol);

    outSubjectCol = new RGridColumnDef(DepositMoveMessage.M.out()
        + PDepositMoveDef.constants.outSubject());
    outSubjectCol.setRendererFactory(new RHyperlinkFieldRendererFactory());
    outSubjectCol.setSortable(true);
    outSubjectCol.setName(PayDepositMoveUrlParams.Search.FIELD_OUTSUBJECT);
    outSubjectCol.setWidth("120px");
    grid.addColumnDef(outSubjectCol);

    amountCol = new RGridColumnDef(PDepositMoveDef.constants.amount());
    amountCol.setSortable(true);
    amountCol.setName(PayDepositMoveUrlParams.Search.FIELD_AMOUNT);
    amountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    amountCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    amountCol.setWidth("90px");
    grid.addColumnDef(amountCol);

    accountDateCol = new RGridColumnDef(PDepositMoveDef.accountDate);
    accountDateCol.setSortable(true);
    accountDateCol.setName(PayDepositMoveUrlParams.Search.FIELD_ACCOUNTDATE);
    accountDateCol.setRendererFactory(new RDateTimeRendererFactory(GWTFormat.fmt_yMd));
    accountDateCol.setWidth("90px");
    grid.addColumnDef(accountDateCol);

    inCountPartCol = new RGridColumnDef(DepositMoveMessage.M.in()
        + EPPayDepositMove.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
            GRes.R.counterpart()));
    inCountPartCol.setSortable(true);
    inCountPartCol.setName(PayDepositMoveUrlParams.Search.FIELD_INCOUNTERPART);
    inCountPartCol.setWidth("120px");
    grid.addColumnDef(inCountPartCol);

    inContractNumberCol = new RGridColumnDef(DepositMoveMessage.M.in()
        + PDepositMoveDef.constants.inContract_code());
    inContractNumberCol.setSortable(true);
    inContractNumberCol.setName(PayDepositMoveUrlParams.Search.FIELD_INCONTRACTNUMBER);
    inContractNumberCol.setWidth("120px");
    grid.addColumnDef(inContractNumberCol);

    inContractNameCol = new RGridColumnDef(DepositMoveMessage.M.in()
        + PDepositMoveDef.constants.inContract_name());
    inContractNameCol.setSortable(true);
    inContractNameCol.setName(PayDepositMoveUrlParams.Search.FIELD_INCONTRACTNAME);
    inContractNameCol.setWidth("120px");
    grid.addColumnDef(inContractNameCol);

    inSubjectCol = new RGridColumnDef(DepositMoveMessage.M.in()
        + PDepositMoveDef.constants.inSubject());
    inSubjectCol.setRendererFactory(new RHyperlinkFieldRendererFactory());
    inSubjectCol.setSortable(true);
    inSubjectCol.setName(PayDepositMoveUrlParams.Search.FIELD_INSUBJECT);
    inSubjectCol.setWidth("120px");
    grid.addColumnDef(inSubjectCol);

    remarkCol = new RGridColumnDef(PDepositMoveDef.constants.remark());
    remarkCol.setSortable(true);
    remarkCol.setName(PayDepositMoveUrlParams.Search.FIELD_REMARK);
    remarkCol.setWidth("80px");
    grid.addColumnDef(remarkCol);

    setterNoCol = new RGridColumnDef(PDepositMoveDef.constants.settleNo());
    setterNoCol.setSortable(true);
    setterNoCol.setWidth("90px");
    setterNoCol.setName(PayDepositMoveUrlParams.Search.FIELD_SETTLENO);
    grid.addColumnDef(setterNoCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private void drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());

    flecsPanel.addField(PDepositMoveDef.billNumber);
    flecsPanel.addField(PDepositMoveDef.bizState);
    flecsPanel.addField(outCounterpartType);
    flecsPanel.addField(PDepositMoveDef.outCounterpart);
    flecsPanel.addField(PDepositMoveDef.outContract_code);
    flecsPanel.addField(outContract_name);
    flecsPanel.addField(PDepositMoveDef.outSubject);
    flecsPanel.addField(PDepositMoveDef.accountDate);
    flecsPanel.addField(inCounterpartType);
    flecsPanel.addField(PDepositMoveDef.inCounterpart);
    flecsPanel.addField(PDepositMoveDef.inContract_code);
    flecsPanel.addField(inContract_name);
    flecsPanel.addField(PDepositMoveDef.inSubject);
    flecsPanel.addField(businessUnit);
    flecsPanel.addField(PDepositMoveDef.settleNo);
    if (getEP().isPermEnabled()) {
      flecsPanel.addField(permGroup);
    }
    flecsPanel.addField(PDepositMoveDef.createInfo_operator_id);
    flecsPanel.addField(PDepositMoveDef.createInfo_operator_fullName);
    flecsPanel.addField(PDepositMoveDef.createInfo_time);
    flecsPanel.addField(PDepositMoveDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PDepositMoveDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PDepositMoveDef.lastModifyInfo_time);
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
    batchProcessor = new PagingGridBatchProcessor<BDepositMove>(pagingGrid, getEP()
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
  public void onShow(JumpParameters params) {
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
    deleteAction.setVisible(getEP().isPermitted(PayDepositMovePermDef.DELETE));
    effectAction.setVisible(getEP().isPermitted(PayDepositMovePermDef.EFFECT));
    abortAction.setVisible(getEP().isPermitted(PayDepositMovePermDef.ABORT));
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
    if (getEP().isPermitted(PayDepositMovePermDef.READ) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    getEP().getTitleBar().clearStandardTitle();
    getEP().getTitleBar().setTitleText(DepositMoveMessage.M.search());
    getEP().getTitleBar().appendAttributeText(getEP().getModuleCaption());
  }

  private void decodeParams(JumpParameters params) {
    keyword = params.getUrlRef().get(PN_KEYWORD);
  }

  private void refreshFlecs() {
    grid.setSort(billNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel.addCondition(new Condition(PDepositMoveDef.billNumber,
          DefaultOperator.STARTS_WITH, keyword));
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
    conditions.add(new Condition(PDepositMoveDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositMoveDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(outCounterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositMoveDef.outCounterpart, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositMoveDef.outContract_code, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(outContract_name, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PDepositMoveDef.outSubject, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositMoveDef.accountDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(inCounterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositMoveDef.inCounterpart, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositMoveDef.inContract_code, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(inContract_name, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PDepositMoveDef.inSubject, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(businessUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositMoveDef.settleNo, DefaultOperator.EQUALS, SettleNoField
        .getCurrentSettleNo()));

    if (getEP().isPermEnabled())
      conditions.add(new Condition(permGroup, DefaultOperator.EQUALS, null));
    return conditions;
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

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      BDepositMove entity = pagingGrid.getRowData(cell.getRow());
      if (entity == null)
        return;

      if (cell.getColumnDef().equals(billNumberCol)) {
        getEP().jumpToViewPage(entity.getUuid());
      } else if (cell.getColumnDef().equals(outSubjectCol)) {
        if (entity.getOutSubject() == null || entity.getOutSubject().getUuid() == null)
          return;
        doJumpSubject(entity.getOutSubject().getUuid());
      } else if (cell.getColumnDef().equals(inSubjectCol)) {
        if (entity.getInSubject() == null || entity.getInSubject().getUuid() == null)
          return;
        doJumpSubject(entity.getInSubject().getUuid());
      }
    }
  }

  private void doJumpSubject(String subjectUuid) {
    GwtUrl url = SubjectUrlParams.ENTRY_URL;
    url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
    url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, subjectUuid);
    try {
      RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
    } catch (Exception e) {
      String msg = DepositMoveMessage.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
      RMsgBox.showError(msg, e);
    }
  }

  private class Handler_effectAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (pagingGrid.getSelections().isEmpty()) {
        RMsgBox.showError(DepositMoveMessage.M.seleteDataToAction(DepositMoveMessage.M.effect(),
            getEP().getModuleCaption()));
        return;
      }

      batchProcessor.batchProcess(DepositMoveMessage.M.effect(), new BDepositMove[] {},
          new PagingGridBatchProcessCallback<BDepositMove>() {
            @Override
            public void execute(BDepositMove entity, BatchProcesser<BDepositMove> processer,
                AsyncCallback callback) {
              if (!BBizStates.INEFFECT.equals(entity.getBizState())) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              PayDepositMoveService.Locator.getService().effect(entity.getUuid(), null,
                  entity.getVersion(), callback);
              BDepositMoveLogger.getInstance().log(DepositMoveMessage.M.effect(), entity);
            }
          });

    }
  }

  private class Handler_abortAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (pagingGrid.getSelections().isEmpty()) {
        RMsgBox.showError(DepositMoveMessage.M.seleteDataToAction(DepositMoveMessage.M.abort(),
            getEP().getModuleCaption()));
        return;
      }

      batchProcessor.batchProcess(DepositMoveMessage.M.abort(), new BDepositMove[] {},
          new PagingGridBatchProcessCallback<BDepositMove>() {
            @Override
            public void execute(BDepositMove entity, BatchProcesser<BDepositMove> processer,
                AsyncCallback callback) {
              if (!BBizStates.EFFECT.equals(entity.getBizState())) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              PayDepositMoveService.Locator.getService().abort(entity.getUuid(), null,
                  entity.getVersion(), callback);
              BDepositMoveLogger.getInstance().log(DepositMoveMessage.M.abort(), entity);
            }
          });

    }
  }

  private class Handler_deleteAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      batchProcessor.batchProcess(DepositMoveMessage.M.delete(), new BDepositMove[] {},
          new PagingGridBatchProcessCallback<BDepositMove>() {
            @Override
            public void execute(BDepositMove entity, BatchProcesser<BDepositMove> processer,
                AsyncCallback callback) {
              if (!BBizStates.INEFFECT.equals(entity.getBizState())) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              PayDepositMoveService.Locator.getService().remove(entity.getUuid(),
                  entity.getVersion(), callback);
              BDepositMoveLogger.getInstance().log(DepositMoveMessage.M.delete(), entity);
            }
          });
    }
  }

  private class Handler_editMenuItem implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BDepositMove pay = pagingGrid.getPageData().get(hotItem.getGridRow());

      JumpParameters jumParams = new JumpParameters(PayDepositMoveEditPage.START_NODE);
      jumParams.getUrlRef().set(PayDepositMoveEditPage.PN_ENTITY_UUID, pay.getUuid());
      getEP().jump(jumParams);
    }
  }

  private class Handler_deleteMenuItem implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BDepositMove entity = pagingGrid.getPageData().get(hotItem.getGridRow());

      doConfirmDeleteOne(entity);
    }
  }

  private void doConfirmDeleteOne(final BDepositMove entity) {
    String msg = DepositMoveMessage.M.actionComfirm2(DepositMoveMessage.M.delete(),
        PayDepositMoveUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doDeleteOne(entity);
      }
    });
  }

  private void doDeleteOne(final BDepositMove pay) {
    RLoadingDialog.show(DepositMoveMessage.M.actionDoing(DepositMoveMessage.M.delete()));
    PayDepositMoveService.Locator.getService().remove(pay.getUuid(), pay.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.actionFailed(DepositMoveMessage.M.delete(),
                PayDepositMoveUrlParams.MODULE_CAPTION);
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositMoveLogger.getInstance().log(DepositMoveMessage.M.delete(), pay);

            getMessagePanel().clearMessages();
            getMessagePanel().putInfoMessage(
                DepositMoveMessage.M.onSuccess(DepositMoveMessage.M.delete(),
                    PayDepositMoveUrlParams.MODULE_CAPTION, pay.getBillNumber()));
            pagingGrid.refresh();
          }
        });
  }

  private class Handler_effectMenuItem implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BDepositMove entity = pagingGrid.getPageData().get(hotItem.getGridRow());

      doConfirmEffectOne(entity);
    }
  }

  private void doConfirmEffectOne(final BDepositMove bill) {
    getMessagePanel().clearMessages();

    String msg = DepositMoveMessage.M.actionComfirm2(DepositMoveMessage.M.effect(), getEP()
        .getModuleCaption(), bill.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        effectOne(bill);
      }
    });
  }

  private void effectOne(final BDepositMove bill) {
    RLoadingDialog.show(DepositMoveMessage.M.beDoing(DepositMoveMessage.M.effect(),
        bill.getBillNumber()));
    PayDepositMoveService.Locator.getService().effect(bill.getUuid(), null, bill.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.actionFailed2(DepositMoveMessage.M.effect(), getEP()
                .getModuleCaption(), bill.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositMoveLogger.getInstance().log(DepositMoveMessage.M.effect(), bill);

            String msg = DepositMoveMessage.M.onSuccess(DepositMoveMessage.M.effect(), getEP()
                .getModuleCaption(), bill.getBillNumber());
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
      BDepositMove entity = pagingGrid.getPageData().get(hotItem.getGridRow());

      doConfirmAbortOne(entity);
    }
  }

  private void doConfirmAbortOne(final BDepositMove bill) {
    getMessagePanel().clearMessages();

    String msg = DepositMoveMessage.M.actionComfirm2(DepositMoveMessage.M.abort(), getEP()
        .getModuleCaption(), bill.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        abortOne(bill);
      }
    });
  }

  private void abortOne(final BDepositMove bill) {
    RLoadingDialog.show(DepositMoveMessage.M.beDoing(DepositMoveMessage.M.abort(),
        bill.getBillNumber()));
    PayDepositMoveService.Locator.getService().abort(bill.getUuid(), null, bill.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.actionFailed2(DepositMoveMessage.M.abort(), getEP()
                .getModuleCaption(), bill.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositMoveLogger.getInstance().log(DepositMoveMessage.M.abort(), bill);

            String msg = DepositMoveMessage.M.onSuccess(DepositMoveMessage.M.abort(), getEP()
                .getModuleCaption(), bill.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private class MyOperatorPolicy implements OperatorPolicy {
    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (PDepositMoveDef.bizState == field || permGroup == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PDepositMoveDef.settleNo == field || PDepositMoveDef.outCounterpart == field
          || PDepositMoveDef.outSubject == field || PDepositMoveDef.inCounterpart == field
          || PDepositMoveDef.inSubject == field || businessUnit == field
          || outCounterpartType == field || inCounterpartType == field) {
        result.add(DefaultOperator.EQUALS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (field == PDepositMoveDef.outCounterpart) {
        field.setCaption(DepositMoveMessage.M.out()
            + getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
        return DefaultOperator.EQUALS == operator || DefaultOperator.NOT_EQUALS == operator ? buildCounterpart(true)
            : null;
      } else if (field == PDepositMoveDef.inCounterpart) {
        field.setCaption(DepositMoveMessage.M.in()
            + getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
        return DefaultOperator.EQUALS == operator || DefaultOperator.NOT_EQUALS == operator ? buildCounterpart(true)
            : null;
      } else if (field == PDepositMoveDef.outSubject) {
        field.setCaption(DepositMoveMessage.M.out() + PDepositMoveDef.constants.outSubject());
        return DefaultOperator.EQUALS == operator ? new SubjectUCNBox(
            BSubjectType.predeposit.name(), DirectionType.payment.getDirectionValue()) : null;
      } else if (field == PDepositMoveDef.inSubject) {
        field.setCaption(DepositMoveMessage.M.in() + PDepositMoveDef.constants.inSubject());
        return DefaultOperator.EQUALS == operator ? new SubjectUCNBox(
            BSubjectType.predeposit.name(), DirectionType.payment.getDirectionValue()) : null;
      } else if (field == PDepositMoveDef.settleNo) {
        SettleNoField settleNoField = new SettleNoField(PDepositMoveDef.settleNo);
        settleNoField.refreshOption(12);
        settleNoField.setMaxLength(6);
        return settleNoField;
      } else if (field == PDepositMoveDef.outContract_code) {
        field.setCaption(DepositMoveMessage.M.out() + PDepositMoveDef.constants.outContract_code());
      } else if (field == PDepositMoveDef.inContract_code) {
        field.setCaption(DepositMoveMessage.M.in() + PDepositMoveDef.constants.inContract_code());
      } else if (field == businessUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (permGroup == field) {
        return new PermGroupField(permGroup);
      } else if (outCounterpartType == field) {
        return new CounterpartTypeField(outCounterpartType);
      } else if (inCounterpartType == field) {
        return new CounterpartTypeField(inCounterpartType);
      }
      return super.createOperandWidget(field, operator);
    }
  }

  private CounterpartUCNBox buildCounterpart(boolean isIn) {
    CounterpartUCNBox counterpart = null;
    if (isIn) {
      counterpart = new CounterpartUCNBox(null, true, getEP().getCaptionMap());
    } else {
      counterpart = new CounterpartUCNBox(getEP().getCaptionMap());
    }
    counterpart.setCounterTypeMap(getEP().getCounterpartTypeMap());
    return counterpart;
  }

  private class CounterpartTypeField extends RComboBox<String> {

    public CounterpartTypeField(FieldDef fieldDef) {
      super();
      setFieldDef(fieldDef);
      setMaxDropdownRowCount(10);
      setNullOptionText(DepositMoveMessage.M.all());
      setEditable(false);
      for (Entry<String, String> entry : getEP().getCounterpartTypeMap().entrySet()) {
        this.addOption(entry.getKey(), entry.getValue());
      }
    }
  }

  private class GridDataProvider implements RPageDataProvider<BDepositMove> {
    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BDepositMove>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);

      if (sortField != null)
        pageSort.appendOrder(sortField, sortDir);
      else
        pageSort.appendOrder(PayDepositMoveUrlParams.Search.FIELD_BILLNUMBER, "desc");

      PayDepositMoveService.Locator.getService().query(
          flecsPanel.getCurrentConfig().getConditions(),
          flecsPanel.getCurrentConfig().getVisibleColumnNames(), pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BDepositMove rowData, List<BDepositMove> pageData) {
      if (col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      if (col == bizStateCol.getIndex())
        return PDepositMoveDef.bizState.getEnumCaption(rowData.getBizState());
      if (col == outCountPartCol.getIndex())
        return rowData.getOutCounterpart() == null ? null : rowData.getOutCounterpart()
            .toFriendlyStr(getEP().getCounterpartTypeMap());
      if (col == outContractNumberCol.getIndex())
        return rowData.getOutContract() == null ? null : rowData.getOutContract().getCode();
      if (col == outContractNameCol.getIndex())
        return rowData.getOutContract() == null ? null : rowData.getOutContract().getName();
      if (col == outSubjectCol.getIndex())
        return rowData.getOutSubject().toFriendlyStr();
      if (col == amountCol.getIndex())
        return rowData.getAmount() == null ? null : rowData.getAmount().doubleValue();
      if (col == accountDateCol.getIndex())
        return rowData.getAccountDate();
      if (col == inCountPartCol.getIndex())
        return rowData.getInCounterpart() == null ? null : rowData.getInCounterpart()
            .toFriendlyStr(getEP().getCounterpartTypeMap());
      if (col == inContractNumberCol.getIndex())
        return rowData.getInContract() == null ? null : rowData.getInContract().getCode();
      if (col == inContractNameCol.getIndex())
        return rowData.getInContract() == null ? null : rowData.getInContract().getName();
      if (col == inSubjectCol.getIndex())
        return rowData.getInSubject().toFriendlyStr();
      if (col == remarkCol.getIndex())
        return rowData.getRemark();
      if (col == setterNoCol.getIndex())
        return rowData.getSettleNo();
      return null;
    }
  }

  @Override
  protected RPagingGrid getPagingGrid() {
    return pagingGrid;
  }

  @Override
  protected EPPayDepositMove getEP() {
    return EPPayDepositMove.getInstance();
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
    BDepositMove entity = (BDepositMove) bill;
    menu.addSeparator();
    menu.addItem(editMenuItem);
    menu.addItem(deleteMenuItem);
    menu.addSeparator();
    menu.addItem(effectMenuItem);
    menu.addItem(abortMenuItem);

    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffected = BBizStates.EFFECT.equals(entity.getBizState());

    boolean canEdit = getEP().isPermitted(PayDepositMovePermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(PayDepositMovePermDef.DELETE);
    boolean canEffect = getEP().isPermitted(PayDepositMovePermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(PayDepositMovePermDef.ABORT);

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

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes M = GWT.create(WidgetRes.class);

    @DefaultStringValue("转出店招")
    String outContractName();

    @DefaultStringValue("转入店招")
    String inContractName();
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
