/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementAccSettleLogPage.java
 * 模块说明：	
 * 修改历史：
 * 2014年9月18日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.contract.intf.client.dd.BillCalculateTypeDef;
import com.hd123.m3.account.gwt.contract.intf.client.dd.CBillCalculateType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BAccountSettleLog;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementService;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams.AccSettleLog;
import com.hd123.m3.account.gwt.statement.intf.client.perm.StatementPermDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.client.flecs.BUCNCodec;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.codec.CodecUtils;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.flecs.client.DefaultOperandWidgetFactory;
import com.hd123.rumba.gwt.flecs.client.DefaultOperator;
import com.hd123.rumba.gwt.flecs.client.FlecsConfig;
import com.hd123.rumba.gwt.flecs.client.Operator;
import com.hd123.rumba.gwt.flecs.client.OperatorPolicy;
import com.hd123.rumba.gwt.flecs.client.RFlecsPanel;
import com.hd123.rumba.gwt.flecs.client.codec.DefaultOperatorCodec;
import com.hd123.rumba.gwt.flecs.client.codec.FlecsConfigCodec;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
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
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHyperlinkRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhr
 * 
 */
public class StatementAccSettleLogPage extends BaseContentPage implements AccSettleLog {
  private static String FIXTABLE_STYLE_NAME = "fixTable";

  /** 跳转类型 */
  public static final String DISPATCH = "store";
  /** URL参数：指定查看项目的uuid。 */
  public static final String PN_ENTITY_UUID = "_uuid";

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef(FIELD_ACCOUNTUNIT, EPStatement
      .getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(FIELD_COUNTERPARTTYPE,
      EPStatement.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE,
          GRes.R.counterpartType()));
  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef(FIELD_COUNTERPART, EPStatement
      .getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
  public static StringFieldDef contract_billNumber = new StringFieldDef(FIELD_CONTRACTNUMBER,
      StatementMessages.M.contractBillNumber(), true, 0, 32);
  public static StringFieldDef contract_title = new StringFieldDef(FIELD_CONTRACTTITLE,
      StatementMessages.M.contractTitle(), true, 0, 32);
  public static StringFieldDef floorCode = new StringFieldDef(FIELD_FLOOR_CODE,
      StatementMessages.M.floorCode(), true, 0, 32);
  public static StringFieldDef floorName = new StringFieldDef(FIELD_FLOOR_NAME,
      StatementMessages.M.floorName(), true, 0, 32);
  public static StringFieldDef positionCode = new StringFieldDef(FIELD_POSITION_CODE,
      StatementMessages.M.positionCode(), true, 0, 32);
  public static StringFieldDef positionName = new StringFieldDef(FIELD_POSITION_NAME,
      StatementMessages.M.positionName(), true, 0, 32);
  public static StringFieldDef coopMode = new StringFieldDef(FIELD_COOPMODE,
      StatementMessages.M.coopMode(), true, 0, 32);
  public static StringFieldDef settlementCaption = new StringFieldDef(FIELD_SETTLEMENTCAPTION,
      StatementMessages.M.settlementCaption(), true, 0, 32);
  public static DateFieldDef settlementBeginDate = new DateFieldDef(FIELD_SETTLEMENTBEGINDATE,
      StatementMessages.M.settlementBeginDate(), true, null, true, null, true, GWTFormat.fmt_yMd,
      false);
  public static DateFieldDef settlementEndDate = new DateFieldDef(FIELD_SETTLEMENTENDDATE,
      StatementMessages.M.settlementEndDate(), true, null, true, null, true, GWTFormat.fmt_yMd,
      false);
  public static DateFieldDef planDate = new DateFieldDef(FIELD_PLANDATE,
      StatementMessages.M.planDate(), true, null, true, null, true, GWTFormat.fmt_yMd, false);
  public static DateFieldDef accountTime = new DateFieldDef(FIELD_ACCOUNTTIME,
      StatementMessages.M.accountTime(), true, null, true, null, true, GWTFormat.fmt_yMdHms, true);
  public static EnumFieldDef billCalculateType = new EnumFieldDef(FIELD_BILLCALCULATETYPE,
      StatementMessages.M.billCalculateType(), true, new String[][] {
          {
              "auto", StatementMessages.M.auto() }, {
              "manul", StatementMessages.M.manul() } });
  public static EnumFieldDef isAccountSettle = new EnumFieldDef(FIELD_ISACCOUNTSETTLE,
      StatementMessages.M.isAccountSettel(), true, new String[][] {
          {
              "true", StatementMessages.M.yes() }, {
              "false", StatementMessages.M.no() } });
  public static StringFieldDef statementNumber = new StringFieldDef(FIELD_BILLNUMBER,
      StatementMessages.M.statementBillNumber(), false, 0, 32);

  /** 合同编号等于 */
  public static final String CONTRACTNUMBER_EQUALS = "contractNumber.EQUALS";
  /** 已出账等于 */
  public static final String ISACCOUNTSETTLE_EQUALS = "isAccountSettle.EQUALS";
  /** 计划出账日期小于等于 */
  public static final String PLANDATE_LESS_THAN_OR_EQUAL_TO = "planDate.LESS_THAN_OR_EQUAL_TO";

  public static StatementAccSettleLogPage getInstance() {
    if (instance == null)
      instance = new StatementAccSettleLogPage();
    return instance;
  }

  public StatementAccSettleLogPage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
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
    refreshFlecs();
  }

  private EPStatement ep = EPStatement.getInstance();
  private static StatementAccSettleLogPage instance;

  private RAction backAction;

  private JumpParameters params;
  private RFlecsPanel flecsPanel;
  private FlecsConfigCodec flecsCodec;
  private PageSort pageSort;

  private RPopupMenu lineMenu;
  private RMenuItem cleanItem;
  private RGrid grid;
  private RPagingGrid<BAccountSettleLog> pagingGrid;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef contractTitleCol;
  private RGridColumnDef contractBillNumberCol;
  private RGridColumnDef floorCol;
  private RGridColumnDef coopModeCol;
  private RGridColumnDef settlementCaptionCol;
  private RGridColumnDef settlementRangeDateCol;
  private RGridColumnDef planDateCol;
  private RGridColumnDef accountTimeCol;
  private RGridColumnDef billCalculateTypeCol;
  private RGridColumnDef statementBillNumberCol;

  private void drawToolbar() {
    backAction = new RAction(RActionFacade.BACK_TO_VIEW, new Handler_cancleAction());
    getToolbar().add(new RToolbarButton(backAction));
  }

  private void drawSelf() {
    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    root.setStyleName(FIXTABLE_STYLE_NAME);
    initWidget(root);

    root.add(drawFlecsAndGrid());
  }

  private void afterDraw() {
    flecsCodec = new FlecsConfigCodec();
    flecsCodec.addOperatorCodec(new DefaultOperatorCodec());
    flecsCodec.addOperandCodec(new BUCNCodec());
  }

  private Widget drawFlecsAndGrid() {
    drawGrid();
    pagingGrid = new RPagingGrid<BAccountSettleLog>(grid, new GridDataProvider());
    drawFlecsPanel();

    return flecsPanel;
  }

  private void drawGrid() {
    drawLineMenu();

    grid = new RGrid();
    grid.setAllowHorizontalScrollBar(true);
    grid.addClickHandler(new Handler_Grid());

    accountUnitCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));

    accountUnitCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new DispatchLinkRendererFactory(DISPATCH, true, "110px")));
    accountUnitCol.setWidth("150px");
    accountUnitCol.setName(FIELD_ACCOUNTUNIT);
    accountUnitCol.setSortable(true);
    accountUnitCol.setOverflowEllipsis(true);
    grid.addColumnDef(accountUnitCol);

    counterpartCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    counterpartCol.setWidth("150px");
    counterpartCol.setName(FIELD_COUNTERPART);
    counterpartCol.setSortable(true);
    grid.addColumnDef(counterpartCol);

    contractTitleCol = new RGridColumnDef(StatementMessages.M.contractTitle());
    contractTitleCol.setWidth("80px");
    contractTitleCol.setName(FIELD_CONTRACTTITLE);
    contractTitleCol.setSortable(true);
    grid.addColumnDef(contractTitleCol);

    contractBillNumberCol = new RGridColumnDef(StatementMessages.M.contractBillNumber());
    contractBillNumberCol.setWidth("160px");
    contractBillNumberCol.setName(FIELD_CONTRACTNUMBER);
    contractBillNumberCol.setSortable(true);
    grid.addColumnDef(contractBillNumberCol);

    floorCol = new RGridColumnDef(StatementMessages.M.floor());
    floorCol.setWidth("130px");
    floorCol.setName(FIELD_FLOOR);
    floorCol.setSortable(true);
    grid.addColumnDef(floorCol);

    coopModeCol = new RGridColumnDef(StatementMessages.M.coopMode());
    coopModeCol.setWidth("80px");
    coopModeCol.setName(FIELD_COOPMODE);
    coopModeCol.setSortable(true);
    grid.addColumnDef(coopModeCol);

    settlementCaptionCol = new RGridColumnDef(StatementMessages.M.settlementCaption());
    settlementCaptionCol.setWidth("120px");
    settlementCaptionCol.setName(FIELD_SETTLEMENTCAPTION);
    settlementCaptionCol.setSortable(true);
    grid.addColumnDef(settlementCaptionCol);

    settlementRangeDateCol = new RGridColumnDef(StatementMessages.M.settlementRangeDate());
    settlementRangeDateCol.setWidth("210px");
    settlementRangeDateCol.setName(FIELD_SETTLEMENTRANGEDATE);
    settlementRangeDateCol.setSortable(true);
    grid.addColumnDef(settlementRangeDateCol);

    planDateCol = new RGridColumnDef(StatementMessages.M.planDate());
    planDateCol.setWidth("120px");
    planDateCol.setName(FIELD_PLANDATE);
    planDateCol.setSortable(true);
    grid.addColumnDef(planDateCol);

    accountTimeCol = new RGridColumnDef(StatementMessages.M.accountTime());
    accountTimeCol.setWidth("120px");
    accountTimeCol.setName(FIELD_ACCOUNTTIME);
    accountTimeCol.setSortable(true);
    grid.addColumnDef(accountTimeCol);

    billCalculateTypeCol = new RGridColumnDef(StatementMessages.M.billCalculateType());
    billCalculateTypeCol.setWidth("100px");
    billCalculateTypeCol.setName(FIELD_BILLCALCULATETYPE);
    billCalculateTypeCol.setSortable(true);
    grid.addColumnDef(billCalculateTypeCol);

    statementBillNumberCol = new RGridColumnDef(StatementMessages.M.statement());
    statementBillNumberCol.setWidth("160px");
    statementBillNumberCol.setRendererFactory(new RHyperlinkRendererFactory());
    statementBillNumberCol.setName(FIELD_BILLNUMBER);
    statementBillNumberCol.setSortable(true);
    grid.addColumnDef(statementBillNumberCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  private void drawLineMenu() {
    lineMenu = new RPopupMenu();
    lineMenu.addOpenHandler(new LineMenuOpenHandler());

    cleanItem = new RMenuItem(StatementMessages.M.cleanSettle(), new Handler_menuItem());
    cleanItem.setHotKey(null);
    lineMenu.addItem(cleanItem);
  }

  private void drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setWidth("100%");
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());
    flecsPanel.addField(accountUnit);
    flecsPanel.addField(counterpartType);
    flecsPanel.addField(counterpart);
    flecsPanel.addField(contract_billNumber);
    flecsPanel.addField(contract_title);
    flecsPanel.addField(floorCode);
    flecsPanel.addField(floorName);
    flecsPanel.addField(positionCode);
    flecsPanel.addField(positionName);
    flecsPanel.addField(coopMode);
    flecsPanel.addField(settlementCaption);
    flecsPanel.addField(settlementBeginDate);
    flecsPanel.addField(settlementEndDate);
    flecsPanel.addField(planDate);
    flecsPanel.addField(accountTime);
    flecsPanel.addField(billCalculateType);
    flecsPanel.addField(isAccountSettle);
    flecsPanel.addField(statementNumber);
    flecsPanel.addBeforeLoadDataHandler(new BeforeLoadDataHandler<Integer>() {

      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent<Integer> event) {
        JumpParameters jumParams = new JumpParameters(START_NODE);
        FlecsConfig curConfig = flecsPanel.getCurrentConfig();
        jumParams.getUrlRef().set(PN_FLECSCONFIG, flecsCodec.encode(curConfig));
        jumParams.getUrlRef().set(PN_PAGE, String.valueOf(event.getParameter()));
        ep.jump(jumParams);
        event.cancel();
      }
    });

    getToolbar().add(flecsPanel.getMenuButton(), Alignment.right);
  }

  private boolean checkIn() {
    if (ep.isProcessMode()) {
      return true;
    }
    if (ep.isPermitted(StatementPermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(StatementMessages.M.statementAccountSettelLog());
  }

  private void refreshFlecs() {
    grid.setSort(accountUnitCol, OrderDir.asc, false);

    flecsPanel.clearConditions();

    String flecsStr = params.getUrlRef().get(PN_FLECSCONFIG);
    String pageInt = params.getUrlRef().get(PN_PAGE);
    int startPage = CodecUtils.decodeInt(pageInt, 0);
    // url条件解析
    String contractNumber = params.getUrlRef().get(CONTRACTNUMBER_EQUALS);
    Boolean isAccountSettleLocal = params.getUrlRef().get(ISACCOUNTSETTLE_EQUALS) == null ? null
        : CodecUtils.decodeBoolean(params.getUrlRef().get(ISACCOUNTSETTLE_EQUALS));
    Date planDateLocal = params.getUrlRef().get(PLANDATE_LESS_THAN_OR_EQUAL_TO) == null ? null
        : StringUtil.toDate(params.getUrlRef().get(PLANDATE_LESS_THAN_OR_EQUAL_TO),
            StringUtil.DATE_FORMATS[4]);
    if (flecsStr != null) {
      final FlecsConfig fc = flecsCodec.decode(flecsStr);
      flecsPanel.setCurrentConfig(fc, startPage);
    } else if (contractNumber != null || isAccountSettleLocal != null || planDateLocal != null) {
      flecsPanel.addConditions(getDefaultConditions(contractNumber, planDateLocal,
          isAccountSettleLocal));
      flecsPanel.refresh();
    } else if (flecsPanel.getDefaultConfig() == null) {
      flecsPanel.addConditions(getDefaultConditions(null, null, null));
      flecsPanel.refresh();
    } else {
      flecsPanel.setCurrentConfig(flecsPanel.getDefaultConfig());
    }
  }

  private List<Condition> getDefaultConditions(String contractNumber, Date planDateValue,
      Boolean settled) {
    List<Condition> conditions = new ArrayList<Condition>();
    conditions.add(new Condition(accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpart, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(contract_billNumber, DefaultOperator.EQUALS, contractNumber));
    conditions.add(new Condition(contract_title, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(planDate, DefaultOperator.LESS_THAN_OR_EQUAL_TO,
        planDateValue == null ? new Date() : planDateValue));
    if (settled != null) {
      conditions.add(new Condition(isAccountSettle, DefaultOperator.EQUALS, settled ? "true"
          : "false"));
    }
    return conditions;
  }

  private class GridDataProvider implements RPageDataProvider<BAccountSettleLog> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAccountSettleLog>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);
      if (sortField != null)
        pageSort.appendOrder(sortField, sortDir);
      else
        pageSort.appendOrder(FIELD_ACCOUNTUNIT, OrderDir.desc);

      StatementService.Locator.getService().queryAccountSettleLog(
          flecsPanel.getCurrentConfig().getConditions(), pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BAccountSettleLog rowData,
        List<BAccountSettleLog> pageData) {
      if (col == accountUnitCol.getIndex()) {
        if (rowData.getAccountUnit() == null) {
          return null;
        } else {
          BDispatch store = new BDispatch(DISPATCH);
          store.addParams(PN_ENTITY_UUID, rowData.getAccountUnit().getUuid());
          store.addParams(DISPATCH, rowData.getAccountUnit().toFriendlyStr());
          return store;
        }
      } else if (col == counterpartCol.getIndex())
        return rowData.getCountpart() == null ? null : rowData.getCountpart().toFriendlyStr(
            ep.getCounterpartTypeMap());
      else if (col == contractTitleCol.getIndex())
        return rowData.getContractTitle();
      else if (col == contractBillNumberCol.getIndex())
        return rowData.getContractBillNumber();
      else if (col == floorCol.getIndex())
        return rowData.getFloor() == null ? null : rowData.getFloor().toFriendlyStr();
      else if (col == coopModeCol.getIndex())
        return rowData.getCoopMode();
      else if (col == settlementCaptionCol.getIndex())
        return rowData.getSettlementCaption();
      else if (col == settlementRangeDateCol.getIndex())
        return buildDateRangeStr(rowData.getSettlementDateRange());
      else if (col == planDateCol.getIndex())
        return rowData.getPlanDate() == null ? null : M3Format.fmt_yMd
            .format(rowData.getPlanDate());
      else if (col == accountTimeCol.getIndex())
        return rowData.getAccountTime() == null ? null : M3Format.fmt_yMdHms.format(rowData
            .getAccountTime());
      else if (col == billCalculateTypeCol.getIndex())
        return rowData.getBillCalculateType() == null ? null : CBillCalculateType.auto
            .equals(rowData.getBillCalculateType().trim()) ? BillCalculateTypeDef.constants.auto()
            : BillCalculateTypeDef.constants.manul();
      else if (col == statementBillNumberCol.getIndex())
        return rowData.getStatementBillNumber();
      return null;
    }
  }

  private String buildDateRangeStr(BDateRange dateRange) {
    if (dateRange == null)
      return null;
    StringBuffer sb = new StringBuffer();
    if (dateRange.getBeginDate() != null)
      sb.append(M3Format.fmt_yMd.format(dateRange.getBeginDate()));
    sb.append("~");
    if (dateRange.getEndDate() != null)
      sb.append(M3Format.fmt_yMd.format(dateRange.getEndDate()));
    return sb.toString();
  }

  private class Handler_Grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      BAccountSettleLog entity = pagingGrid.getRowData(cell.getRow());
      if (entity == null)
        return;

      if (cell.getColumnDef().equals(statementBillNumberCol)) {
        if (!"-".equals(entity.getStatementBillNumber())) {
          GwtUrl url = StatementUrlParams.ENTRY_URL;
          url.getQuery().set(JumpParameters.PN_START, StatementUrlParams.View.START_NODE);
          url.getQuery()
              .set(StatementUrlParams.View.PN_BILLNUMBER, entity.getStatementBillNumber());
          try {
            RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
          } catch (Exception e) {
            String msg = StatementMessages.M.cannotNavigate(url.getUrl());
            RMsgBox.showError(msg, e);
          }
        }
      }
    }
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (field == accountUnit)
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox() : null;
      else if (field == counterpart)
        return DefaultOperator.EQUALS == operator ? buildCounterpart() : null;
      else if (counterpartType == field) {
        return new CounterpartTypeField(counterpartType);
      } else if (field == coopMode) {
        RComboBox<String> coopModeField = new RComboBox<String>();
        coopModeField.setMaxDropdownRowCount(15);
        coopModeField.setEditable(false);
        for (String coopMode : EPStatement.getInstance().getCoopModes()) {
          coopModeField.addOption(coopMode);
        }
        return coopModeField;
      }
      return super.createOperandWidget(field, operator);
    }
  }

  private CounterpartUCNBox buildCounterpart() {
    CounterpartUCNBox counterpart = new CounterpartUCNBox(null, true, ep.getCaptionMap());
    counterpart.setCounterTypeMap(ep.getCounterpartTypeMap());
    return counterpart;
  }

  private class CounterpartTypeField extends RComboBox<String> {

    public CounterpartTypeField(FieldDef fieldDef) {
      super();
      setFieldDef(fieldDef);
      setMaxDropdownRowCount(10);
      setNullOptionText(StatementMessages.M.all());
      setEditable(false);
      for (Entry<String, String> entry : ep.getCounterpartTypeMap().entrySet()) {
        this.addOption(entry.getKey(), entry.getValue());
      }
    }
  }

  private class MyOperatorPolicy implements OperatorPolicy {

    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (accountUnit == field || counterpart == field || billCalculateType == field
          || isAccountSettle == field || counterpartType == field) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (coopMode.equals(field)) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }

  private class Handler_cancleAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      JumpParameters jumParams = new JumpParameters(StatementSearchPage.START_NODE);
      ep.jump(jumParams);
    }
  }

  private class Handler_menuItem implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BAccountSettleLog entity = pagingGrid.getPageData().get(hotItem.getGridRow());

      doConfirmClean(entity);
    }
  }

  private void doConfirmClean(final BAccountSettleLog entity) {
    getMessagePanel().clearMessages();
    String msg = StatementMessages.M.actionComfirm(StatementMessages.M.cleanSettle(), "");

    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doClean(entity);
      }
    });
  }

  private void doClean(BAccountSettleLog entity) {
    RLoadingDialog.show(StatementMessages.M.cleanSettle() + "...");
    StatementService.Locator.getService().cleanSettle(entity.getUuid(),
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementMessages.M.actionFailed(StatementMessages.M.cleanSettle(), "");
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            String msg = StatementMessages.M.onSuccess2(StatementMessages.M.cleanSettle(), "");
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private class LineMenuOpenHandler implements OpenHandler<RPopupMenu> {

    @Override
    public void onOpen(OpenEvent<RPopupMenu> event) {
      RHotItemRenderer hotItem = (RHotItemRenderer) event.getTarget().getContextWidget();
      BAccountSettleLog entity = pagingGrid.getPageData().get(hotItem.getGridRow());

      if (entity.isEmpty()) {
        GWTUtil.enableSynchronousRPC();
        StatementService.Locator.getService().isMaxSettled(entity.getUuid(),
            new RBAsyncCallback2<Boolean>() {

              @Override
              public void onException(Throwable caught) {
                cleanItem.setVisible(true);
              }

              @Override
              public void onSuccess(Boolean result) {
                cleanItem.setVisible(result);
              }
            });
      } else {
        cleanItem.setVisible(false);
      }
    }
  }

}
