/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositRepaymentSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-25 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui;

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
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLogger;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentDef;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentLineDef;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.EPRecDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc.RecDepositRepaymentService;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.RecDepositRepaymentUrlParams;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.RecDepositRepaymentUrlParams.Search;
import com.hd123.m3.account.gwt.depositrepayment.receipt.intf.client.perm.RecDepositRepaymentPermDef;
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
 * @author zhuhairui
 * 
 */
public class RecDepositRepaymentSearchPage extends BaseBpmSearchPage implements Search,
    BeforePrintHandler {
  public static EmbeddedFieldDef businessUnit = new EmbeddedFieldDef(FIELD_ACCOUNTUNIT,
      EPRecDepositRepayment.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS,
          GRes.R.business()));
  public static EmbeddedFieldDef permGroup = new EmbeddedFieldDef(FIELD_PERMGROUP,
      DepositRepaymentMessage.M.permGroup());

  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(
      RecDepositRepaymentUrlParams.Flecs.FIELD_COUNTERPARTTYPE, EPRecDepositRepayment.getInstance()
          .getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE, GRes.R.counterpartType()));
  private static RecDepositRepaymentSearchPage instance = null;

  public static RecDepositRepaymentSearchPage getInstance() {
    if (instance == null) {
      instance = new RecDepositRepaymentSearchPage();
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
  private RPagingGrid<BDepositRepayment> pagingGrid;
  private PagingGridBatchProcessor<BDepositRepayment> batchProcessor;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef stateCol;
  private RGridColumnDef countPartCol;
  private RGridColumnDef contractNumberCol;
  private RGridColumnDef contractNameCol;
  private RGridColumnDef paymentTypeCol;
  private RGridColumnDef repaymentDateCol;
  private RGridColumnDef accountDateCol;
  private RGridColumnDef dealerCol;
  private RGridColumnDef repaymentTotalCol;
  private RGridColumnDef counterContactCol;
  private RGridColumnDef setterNoCol;

  private RMenuItem editMenuItem;
  private RMenuItem deleteMenuItem;
  private RMenuItem effectMenuItem;
  private RMenuItem abortMenuItem;

  public RecDepositRepaymentSearchPage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());
    getToolbar().addSeparator();

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    effectAction = new RAction(DepositRepaymentMessage.M.effect(), new Handler_effectAction());
    abortAction = new RAction(DepositRepaymentMessage.M.abort(), new Handler_abortAction());

    moreButton = new RToolbarMenuButton(DepositRepaymentMessage.M.operate(), moreMenu);
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

    effectMenuItem = new RMenuItem(DepositRepaymentMessage.M.effect(), new Handler_effectMenuItem());
    effectMenuItem.setHotKey(null);

    abortMenuItem = new RMenuItem(DepositRepaymentMessage.M.abort(), new Handler_abortMenuItem());
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
        for (BDepositRepayment o : pagingGrid.getSelections()) {
          if (StringUtil.isNullOrBlank(o.getStore().getUuid()) == false) {
            storeCodes.add(o.getStore().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    billNumberCol = new RGridColumnDef(PDepositRepaymentDef.billNumber);
    billNumberCol.setSortable(true);
    billNumberCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_BILLNUMBER);
    billNumberCol.setWidth("180px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setAllowHiding(false);
    grid.addColumnDef(billNumberCol);

    stateCol = new RGridColumnDef(PDepositRepaymentDef.bizState);
    stateCol.setSortable(true);
    stateCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_BIZSTATE);
    stateCol.setWidth("60px");
    grid.addColumnDef(stateCol);

    countPartCol = new RGridColumnDef(EPRecDepositRepayment.getInstance().getFieldCaption(
        GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
    countPartCol.setSortable(true);
    countPartCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_COUNTERPART);
    countPartCol.setWidth("120px");
    grid.addColumnDef(countPartCol);

    contractNumberCol = new RGridColumnDef(PDepositRepaymentDef.contract_code);
    contractNumberCol.setSortable(true);
    contractNumberCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_CONTRACTNUMBER);
    contractNumberCol.setWidth("120px");
    grid.addColumnDef(contractNumberCol);

    contractNameCol = new RGridColumnDef(PDepositRepaymentDef.contract_name);
    contractNameCol.setSortable(true);
    contractNameCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_CONTRACTNAME);
    contractNameCol.setWidth("120px");
    grid.addColumnDef(contractNameCol);

    repaymentTotalCol = new RGridColumnDef(PDepositRepaymentDef.repaymentTotal);
    repaymentTotalCol.setSortable(true);
    repaymentTotalCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_REPAYMENTTOTAL);
    repaymentTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    repaymentTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    repaymentTotalCol.setWidth("90px");
    grid.addColumnDef(repaymentTotalCol);

    paymentTypeCol = new RGridColumnDef(PDepositRepaymentDef.paymentType);
    paymentTypeCol.setWidth("90px");
    paymentTypeCol.setSortable(true);
    paymentTypeCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_PAYMENTTYPE);
    grid.addColumnDef(paymentTypeCol);

    repaymentDateCol = new RGridColumnDef(PDepositRepaymentDef.repaymentDate);
    repaymentDateCol.setSortable(true);
    repaymentDateCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_REPAYMENTDATE);
    repaymentDateCol.setRendererFactory(new RDateTimeRendererFactory(GWTFormat.fmt_yMd));
    repaymentDateCol.setWidth("90px");
    grid.addColumnDef(repaymentDateCol);

    accountDateCol = new RGridColumnDef(PDepositRepaymentDef.accountDate);
    accountDateCol.setSortable(true);
    accountDateCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_ACCOUNTDATE);
    accountDateCol.setRendererFactory(new RDateTimeRendererFactory(GWTFormat.fmt_yMd));
    accountDateCol.setWidth("90px");
    grid.addColumnDef(accountDateCol);

    dealerCol = new RGridColumnDef(PDepositRepaymentDef.dealer);
    dealerCol.setSortable(true);
    dealerCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_DEALER);
    dealerCol.setWidth("90px");
    grid.addColumnDef(dealerCol);

    counterContactCol = new RGridColumnDef(PDepositRepaymentDef.counterContact);
    counterContactCol.setWidth("90px");
    counterContactCol.setSortable(true);
    counterContactCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_COUNTERCONTACT);
    grid.addColumnDef(counterContactCol);

    setterNoCol = new RGridColumnDef(PDepositRepaymentDef.settleNo);
    setterNoCol.setSortable(true);
    setterNoCol.setName(RecDepositRepaymentUrlParams.Search.FIELD_SETTLENO);
    grid.addColumnDef(setterNoCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private void drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());

    flecsPanel.addField(PDepositRepaymentDef.billNumber);
    flecsPanel.addField(counterpartType);
    flecsPanel.addField(PDepositRepaymentDef.counterpart);
    flecsPanel.addField(PDepositRepaymentDef.contract_code);
    flecsPanel.addField(PDepositRepaymentDef.contract_name);
    flecsPanel.addField(PDepositRepaymentDef.bizState);
    flecsPanel.addField(PDepositRepaymentDef.repaymentDate);
    flecsPanel.addField(PDepositRepaymentDef.accountDate);
    flecsPanel.addField(PDepositRepaymentLineDef.subject);
    flecsPanel.addField(businessUnit);
    flecsPanel.addField(PDepositRepaymentDef.paymentType);
    flecsPanel.addField(PDepositRepaymentDef.settleNo);
    flecsPanel.addField(PDepositRepaymentDef.dealer);
    if (getEP().isPermEnabled()) {
      flecsPanel.addField(permGroup);
    }
    flecsPanel.addField(PDepositRepaymentDef.createInfo_operator_id);
    flecsPanel.addField(PDepositRepaymentDef.createInfo_operator_fullName);
    flecsPanel.addField(PDepositRepaymentDef.createInfo_time);
    flecsPanel.addField(PDepositRepaymentDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PDepositRepaymentDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PDepositRepaymentDef.lastModifyInfo_time);
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
    batchProcessor = new PagingGridBatchProcessor<BDepositRepayment>(pagingGrid, getEP()
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

    if (checkIn() == false)
      return;

    refreshTitle();
    refreshCommands();
    decodeParams(params);
    refreshFlecs();
    refreshCommonds();
  }

  private void refreshCommonds() {
    deleteAction.setVisible(getEP().isPermitted(RecDepositRepaymentPermDef.DELETE));
    effectAction.setVisible(getEP().isPermitted(RecDepositRepaymentPermDef.EFFECT));
    abortAction.setVisible(getEP().isPermitted(RecDepositRepaymentPermDef.ABORT));
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
    if (getEP().isPermitted(RecDepositRepaymentPermDef.READ) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    getEP().getTitleBar().clearStandardTitle();
    getEP().getTitleBar().setTitleText(DepositRepaymentMessage.M.search());
    getEP().getTitleBar().appendAttributeText(getEP().getModuleCaption());
  }

  private void refreshCommands() {
    deleteAction.setEnabled(getEP().isPermitted(RecDepositRepaymentPermDef.DELETE));
    effectAction.setEnabled(getEP().isPermitted(RecDepositRepaymentPermDef.EFFECT));
    abortAction.setEnabled(getEP().isPermitted(RecDepositRepaymentPermDef.ABORT));
    getToolbar().rebuild();
  }

  private void decodeParams(JumpParameters params) {
    keyword = params.getUrlRef().get(PN_KEYWORD);
  }

  private void refreshFlecs() {
    grid.setSort(billNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel.addCondition(new Condition(PDepositRepaymentDef.billNumber,
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
    conditions.add(new Condition(PDepositRepaymentDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositRepaymentDef.counterpart, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositRepaymentDef.contract_code, DefaultOperator.EQUALS, null));
    conditions
        .add(new Condition(PDepositRepaymentDef.contract_name, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PDepositRepaymentDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositRepaymentDef.repaymentDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositRepaymentDef.accountDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositRepaymentLineDef.subject, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(businessUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositRepaymentDef.paymentType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PDepositRepaymentDef.settleNo, DefaultOperator.EQUALS,
        SettleNoField.getCurrentSettleNo()));
    conditions.add(new Condition(PDepositRepaymentDef.dealer, DefaultOperator.EQUALS, null));

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

      if (cell.getColumnDef().equals(billNumberCol)) {
        BDepositRepayment entity = pagingGrid.getRowData(cell.getRow());
        if (entity == null)
          return;

        getEP().jumpToViewPage(entity.getUuid());
      }
    }
  }

  private class Handler_deleteAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (pagingGrid.getSelections().isEmpty()) {
        RMsgBox.showError(DepositRepaymentMessage.M.seleteDataToAction(
            DepositRepaymentMessage.M.delete(), DepositRepaymentMessage.M.recDepositRepayment()));
        return;
      }

      batchProcessor.batchProcess(DepositRepaymentMessage.M.delete(), new BDepositRepayment[] {},
          new PagingGridBatchProcessCallback<BDepositRepayment>() {
            @Override
            public void execute(BDepositRepayment entity,
                BatchProcesser<BDepositRepayment> processer, AsyncCallback callback) {
              if (BBizStates.INEFFECT.equals(entity.getBizState()) == false) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              RecDepositRepaymentService.Locator.getService().remove(entity.getUuid(),
                  entity.getVersion(), callback);
              BDepositRepaymentLogger.getInstance().log(DepositRepaymentMessage.M.delete(), entity);
            }
          });
    }
  }

  private class Handler_effectAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (pagingGrid.getSelections().isEmpty()) {
        RMsgBox.showError(DepositRepaymentMessage.M.seleteDataToAction(
            DepositRepaymentMessage.M.effect(), getEP().getModuleCaption()));
        return;
      }

      batchProcessor.batchProcess(DepositRepaymentMessage.M.effect(), new BDepositRepayment[] {},
          new PagingGridBatchProcessCallback<BDepositRepayment>() {
            @Override
            public void execute(BDepositRepayment bill,
                BatchProcesser<BDepositRepayment> processer, AsyncCallback callback) {
              if (!BBizStates.INEFFECT.equals(bill.getBizState())) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              RecDepositRepaymentService.Locator.getService().effect(bill.getUuid(), null,
                  bill.getVersion(), callback);
              BDepositRepaymentLogger.getInstance().log(DepositRepaymentMessage.M.effect(), bill);
            }
          });

    }
  }

  private class Handler_abortAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (pagingGrid.getSelections().isEmpty()) {
        RMsgBox.showError(DepositRepaymentMessage.M.seleteDataToAction(
            DepositRepaymentMessage.M.abort(), getEP().getModuleCaption()));
        return;
      }

      batchProcessor.batchProcess(DepositRepaymentMessage.M.abort(), new BDepositRepayment[] {},
          new PagingGridBatchProcessCallback<BDepositRepayment>() {
            @Override
            public void execute(BDepositRepayment bill,
                BatchProcesser<BDepositRepayment> processer, AsyncCallback callback) {
              if (!BBizStates.EFFECT.equals(bill.getBizState())) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              RecDepositRepaymentService.Locator.getService().abort(bill.getUuid(), null,
                  bill.getVersion(), callback);
              BDepositRepaymentLogger.getInstance().log(DepositRepaymentMessage.M.abort(), bill);
            }
          });

    }
  }

  private class MyOperatorPolicy implements OperatorPolicy {
    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (PDepositRepaymentDef.bizState == field || PDepositRepaymentDef.paymentType == field
          || permGroup == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PDepositRepaymentDef.settleNo == field
          || PDepositRepaymentDef.counterpart == field || PDepositRepaymentDef.dealer == field
          || businessUnit == field || counterpartType == field) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PDepositRepaymentLineDef.subject == field) {
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
      if (field == PDepositRepaymentDef.counterpart) {
        field.setCaption(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
        return DefaultOperator.EQUALS == operator || DefaultOperator.NOT_EQUALS == operator ? buildCounterpart()
            : null;
      } else if (field == PDepositRepaymentLineDef.subject) {
        return DefaultOperator.CONTAINS == operator ? new SubjectUCNBox(
            BSubjectType.predeposit.name(), DirectionType.receipt.getDirectionValue()) : null;
      } else if (field == PDepositRepaymentDef.settleNo) {
        SettleNoField settleNoField = new SettleNoField(PDepositRepaymentDef.settleNo);
        settleNoField.refreshOption(12);
        settleNoField.setMaxLength(6);
        return settleNoField;
      } else if (field == PDepositRepaymentDef.dealer) {
        return DefaultOperator.EQUALS == operator ? new EmployeeUCNBox() : null;
      } else if (field == businessUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == PDepositRepaymentDef.paymentType) {
        return DefaultOperator.EQUALS == operator || DefaultOperator.NOT_EQUALS == operator ? new PaymentTypeComboBox()
            : null;
      } else if (permGroup == field) {
        return new PermGroupField(permGroup);
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
      setNullOptionText(DepositRepaymentMessage.M.all());
      setEditable(false);
      for (Entry<String, String> entry : getEP().getCounterpartTypeMap().entrySet()) {
        this.addOption(entry.getKey(), entry.getValue());
      }
    }
  }

  private class PaymentTypeComboBox extends RComboBox<BUCN> {

    public PaymentTypeComboBox() {
      setEditable(false);

      setFieldDef(PDepositRepaymentDef.paymentType);
      setMaxDropdownRowCount(10);
      clearOptions();

      for (BUCN paymentType : getEP().getPaymentTypes()) {
        addOption(paymentType, paymentType.toFriendlyStr());
      }
    }
  }

  private class Handler_editMenuItem implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BDepositRepayment pay = pagingGrid.getPageData().get(hotItem.getGridRow());

      JumpParameters jumParams = new JumpParameters(RecDepositRepaymentEditPage.START_NODE);
      jumParams.getUrlRef().set(RecDepositRepaymentEditPage.PN_ENTITY_UUID, pay.getUuid());
      getEP().jump(jumParams);
    }
  }

  private class Handler_deleteMenuItem implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BDepositRepayment pay = pagingGrid.getPageData().get(hotItem.getGridRow());

      doConfirmDeleteOne(pay);
    }
  }

  private void doConfirmDeleteOne(final BDepositRepayment pay) {
    String msg = DepositRepaymentMessage.M.actionComfirm2(DepositRepaymentMessage.M.delete(),
        RecDepositRepaymentUrlParams.MODULE_CAPTION, pay.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doDeleteOne(pay);
      }
    });
  }

  private void doDeleteOne(final BDepositRepayment pay) {
    RLoadingDialog.show(DepositRepaymentMessage.M.actionDoing(DepositRepaymentMessage.M.delete()));
    RecDepositRepaymentService.Locator.getService().remove(pay.getUuid(), pay.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositRepaymentMessage.M.actionFailed(DepositRepaymentMessage.M.delete(),
                RecDepositRepaymentUrlParams.MODULE_CAPTION);
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositRepaymentLogger.getInstance().log(DepositRepaymentMessage.M.delete(), pay);

            getMessagePanel().clearMessages();
            getMessagePanel().putInfoMessage(
                DepositRepaymentMessage.M.onSuccess(DepositRepaymentMessage.M.delete(),
                    RecDepositRepaymentUrlParams.MODULE_CAPTION, pay.getBillNumber()));
            pagingGrid.refresh();
          }
        });
  }

  private class Handler_effectMenuItem implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BDepositRepayment bill = pagingGrid.getPageData().get(hotItem.getGridRow());

      doConfirmEffectOne(bill);
    }
  }

  private void doConfirmEffectOne(final BDepositRepayment bill) {
    getMessagePanel().clearMessages();

    String msg = DepositRepaymentMessage.M.actionComfirm2(DepositRepaymentMessage.M.effect(),
        getEP().getModuleCaption(), bill.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        effectOne(bill);
      }
    });
  }

  private void effectOne(final BDepositRepayment bill) {
    RLoadingDialog.show(DepositRepaymentMessage.M.beDoing(DepositRepaymentMessage.M.effect(),
        bill.getBillNumber()));
    RecDepositRepaymentService.Locator.getService().effect(bill.getUuid(), null, bill.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositRepaymentMessage.M.actionFailed2(
                DepositRepaymentMessage.M.effect(), getEP().getModuleCaption(),
                bill.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositRepaymentLogger.getInstance().log(DepositRepaymentMessage.M.effect(), bill);

            String msg = DepositRepaymentMessage.M.onSuccess(DepositRepaymentMessage.M.effect(),
                getEP().getModuleCaption(), bill.getBillNumber());
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
      BDepositRepayment bill = pagingGrid.getPageData().get(hotItem.getGridRow());

      doConfirmAbortOne(bill);
    }
  }

  private void doConfirmAbortOne(final BDepositRepayment bill) {
    getMessagePanel().clearMessages();

    String msg = DepositRepaymentMessage.M.actionComfirm2(DepositRepaymentMessage.M.abort(),
        getEP().getModuleCaption(), bill.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        abortOne(bill);
      }
    });
  }

  private void abortOne(final BDepositRepayment bill) {
    RLoadingDialog.show(DepositRepaymentMessage.M.beDoing(DepositRepaymentMessage.M.abort(),
        bill.getBillNumber()));
    RecDepositRepaymentService.Locator.getService().abort(bill.getUuid(), null, bill.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositRepaymentMessage.M.actionFailed2(DepositRepaymentMessage.M.abort(),
                getEP().getModuleCaption(), bill.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositRepaymentLogger.getInstance().log(DepositRepaymentMessage.M.abort(), bill);

            String msg = DepositRepaymentMessage.M.onSuccess(DepositRepaymentMessage.M.abort(),
                getEP().getModuleCaption(), bill.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private class GridDataProvider implements RPageDataProvider<BDepositRepayment> {
    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BDepositRepayment>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);

      if (sortField != null)
        pageSort.appendOrder(sortField, sortDir);
      else
        pageSort.appendOrder(RecDepositRepaymentUrlParams.Search.FIELD_BILLNUMBER, "desc");

      RecDepositRepaymentService.Locator.getService().query(
          flecsPanel.getCurrentConfig().getConditions(),
          flecsPanel.getCurrentConfig().getVisibleColumnNames(), pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BDepositRepayment rowData,
        List<BDepositRepayment> pageData) {
      if (col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      if (col == stateCol.getIndex())
        return PDepositRepaymentDef.bizState.getEnumCaption(rowData.getBizState());
      if (col == countPartCol.getIndex())
        return rowData.getCounterpart() == null ? null : rowData.getCounterpart().toFriendlyStr(
            getEP().getCounterpartTypeMap());
      if (col == setterNoCol.getIndex())
        return rowData.getSettleNo();
      if (col == paymentTypeCol.getIndex())
        return rowData.getPaymentType() == null ? null : rowData.getPaymentType().toFriendlyStr();
      if (col == repaymentDateCol.getIndex())
        return rowData.getRepaymentDate();
      if (col == accountDateCol.getIndex())
        return rowData.getAccountDate();
      if (col == dealerCol.getIndex())
        return rowData.getDealer() == null ? null : rowData.getDealer().toFriendlyStr();
      if (col == repaymentTotalCol.getIndex())
        return rowData.getRepaymentTotal() == null ? null : Double.valueOf(rowData
            .getRepaymentTotal().doubleValue());
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
  protected EPRecDepositRepayment getEP() {
    return EPRecDepositRepayment.getInstance();
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
    BDepositRepayment entity = (BDepositRepayment) bill;
    menu.addSeparator();
    menu.addItem(editMenuItem);
    menu.addItem(deleteMenuItem);
    menu.addSeparator();
    menu.addItem(effectMenuItem);
    menu.addItem(abortMenuItem);

    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffected = BBizStates.EFFECT.equals(entity.getBizState());

    editMenuItem.setVisible(isInEffect);
    deleteMenuItem.setVisible(isInEffect);
    effectMenuItem.setVisible(isInEffect);
    abortMenuItem.setVisible(isEffected);

    boolean canEdit = getEP().isPermitted(RecDepositRepaymentPermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(RecDepositRepaymentPermDef.DELETE);
    boolean canEffect = getEP().isPermitted(RecDepositRepaymentPermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(RecDepositRepaymentPermDef.ABORT);

    if (editMenuItem.isVisible())
      editMenuItem.setEnabled(canEdit);
    if (deleteMenuItem.isVisible())
      deleteMenuItem.setEnabled(canDelete);
    if (effectMenuItem.isVisible())
      effectMenuItem.setEnabled(canEffect);
    if (abortMenuItem.isVisible())
      abortMenuItem.setEnabled(canAbort);
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
