/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AccountDefrayalSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-14 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.report.accountdefrayal.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.report.accountdefrayal.client.EPAccountDefrayal;
import com.hd123.m3.account.gwt.report.accountdefrayal.client.biz.BAccountDefrayal;
import com.hd123.m3.account.gwt.report.accountdefrayal.client.rpc.AccountDefrayalService;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.AccountDefrayalMessages;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.AccountDefrayalUrlParams;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.AccountDefrayalUrlParams.Search;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.dd.PAccountDefrayalDef;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.perm.AccountDefrayalPermDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.flecs.BUCNCodec;
import com.hd123.rumba.commons.gwt.mini.client.codec.CodecUtils;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
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
import com.hd123.rumba.gwt.util.client.fielddef.DateFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.EmbeddedFieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RPagingGrid;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class AccountDefrayalSearchPage extends BaseContentPage implements Search {
  private static String FIXTABLE_STYLE_NAME = "fixTable";

  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef(FIELD_ACCOUNTUNIT,
      EPAccountDefrayal.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef(FIELD_COUNTERPART,
      EPAccountDefrayal.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
          GRes.R.counterpart()));
  public static StringFieldDef statementSettleNo = new StringFieldDef(FIELD_STATEMENTSETTLENO,
      AccountDefrayalMessages.M.statementSettleNo());
  public static EmbeddedFieldDef dateRange = new EmbeddedFieldDef(FIELD_DATERANGE,
      AccountDefrayalMessages.M.dateRange());
  public static DateFieldDef lastReceiptDate = new DateFieldDef(FIELD_LASTRECEIPTDATE,
      AccountDefrayalMessages.M.lastReceiptDate(), true, null, true, null, true, GWTFormat.fmt_yMd,
      false);
  public static StringFieldDef direction = new StringFieldDef(FIELD_DIRECTION,
      AccountDefrayalMessages.M.direction());
  public static DateFieldDef accountTime = new DateFieldDef(FIELD_ACCOUNTTIME,
      AccountDefrayalMessages.M.accountTime(), true, null, true, null, true, GWTFormat.fmt_yMd,
      false);
  public static StringFieldDef settled = new StringFieldDef(FIELD_SETTLED,
      AccountDefrayalMessages.M.showSubject());
  public static StringFieldDef invoiced = new StringFieldDef(FIELD_INVOICED,
      AccountDefrayalMessages.M.showInvoice());
  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(
      AccountDefrayalUrlParams.Flecs.FIELD_COUNTERPARTTYPE, EPAccountDefrayal.getInstance()
          .getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE, GRes.R.counterpartType()));

  public static AccountDefrayalSearchPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new AccountDefrayalSearchPage();
    return instance;
  }

  public AccountDefrayalSearchPage() throws ClientBizException {
    super();
    try {
      drawSelf();
      afterDraw();
    } catch (Exception e) {
      throw new ClientBizException(
          AccountDefrayalMessages.M.cannotCreatePage("AccountDefrayalSearchPage"), e);
    }
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    if (subjectUCNBox != null) {
      subjectUCNBox.getBrowser().getFilterCallback().clearConditions();
      subjectUCNBox.getBrowser().getFilterCallback().onQuery();
    }

    if (accountUnitUCNBox != null) {
      accountUnitUCNBox.getBrowser().getFilterCallback().clearConditions();
      accountUnitUCNBox.getBrowser().getFilterCallback().onQuery();
    }
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);
    this.params = params;

    if (!checkIn())
      return;

    decodeParams(params);
    refreshTitle();
    refreshFlecs();
  }

  private EPAccountDefrayal ep = EPAccountDefrayal.getInstance();
  private static AccountDefrayalSearchPage instance;

  private JumpParameters params;
  private RFlecsPanel flecsPanel;
  private FlecsConfigCodec flecsConfigCodec;
  private PageSort pageSort;

  private String sourceBillType;
  private String sourceBillNumber;
  private String statementNumber;

  private RGrid grid;
  private RPagingGrid<BAccountDefrayal> pagingGrid;
  private RGridColumnDef sourceBillNumberCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef directionCol;
  private RGridColumnDef needSettleCol;
  private RGridColumnDef settleAdjCol;
  private RGridColumnDef settledCol;
  private RGridColumnDef needInvoiceCol;
  private RGridColumnDef invoiceAdjCol;
  private RGridColumnDef invoicedCol;
  private RGridColumnDef statementNumberCol;
  private RGridColumnDef statementSettleNoCol;
  private RGridColumnDef dateRangeCol;
  private RGridColumnDef lastReceiptDateCol;
  private RGridColumnDef contractNumberCol;
  private RGridColumnDef contractNameCol;
  private RGridColumnDef counterpartCol;

  private SubjectUCNBox subjectUCNBox;
  private AccountUnitUCNBox accountUnitUCNBox;

  private void afterDraw() {
    flecsConfigCodec = new FlecsConfigCodec();
    flecsConfigCodec.addOperatorCodec(new DefaultOperatorCodec());
    flecsConfigCodec.addOperandCodec(new BUCNCodec());
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
    drawGrid();
    pagingGrid = new RPagingGrid<BAccountDefrayal>(grid, new GridDataProvider());
    drawFlecsPanel();

    return flecsPanel;
  }

  private void drawGrid() {
    grid = new RGrid();
    grid.setAllowHorizontalScrollBar(true);
    grid.addClickHandler(new Handler_Grid());

    sourceBillNumberCol = new RGridColumnDef(PAccountDefrayalDef.acc1_sourceBill_billNumber);
    sourceBillNumberCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    sourceBillNumberCol.setWidth("160px");
    sourceBillNumberCol.setName(FIELD_SOURCEBILLNUMBER);
    sourceBillNumberCol.setSortable(true);
    sourceBillNumberCol.setAllowHiding(false);
    grid.addColumnDef(sourceBillNumberCol);

    sourceBillTypeCol = new RGridColumnDef(PAccountDefrayalDef.acc1_sourceBill_billType);
    sourceBillTypeCol.setWidth("80px");
    sourceBillTypeCol.setName(FIELD_SOURCEBILLTYPE);
    sourceBillTypeCol.setSortable(true);
    grid.addColumnDef(sourceBillTypeCol);

    subjectCol = new RGridColumnDef(PAccountDefrayalDef.acc1_subject_uuid);
    subjectCol.setWidth("120px");
    subjectCol.setName(FIELD_SUBJECT);
    subjectCol.setSortable(true);
    grid.addColumnDef(subjectCol);

    directionCol = new RGridColumnDef(PAccountDefrayalDef.acc1_direction);
    directionCol.setWidth("80px");
    directionCol.setName(FIELD_DIRECTION);
    directionCol.setSortable(true);
    grid.addColumnDef(directionCol);

    needSettleCol = new RGridColumnDef(PAccountDefrayalDef.needSettle_total);
    needSettleCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    needSettleCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    needSettleCol.setWidth("100px");
    needSettleCol.setName(FIELD_NEEDSETTLE);
    needSettleCol.setSortable(true);
    grid.addColumnDef(needSettleCol);

    settleAdjCol = new RGridColumnDef(PAccountDefrayalDef.settleAdj_total);
    settleAdjCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    settleAdjCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    settleAdjCol.setWidth("90px");
    settleAdjCol.setName(FIELD_SETTLEADJ);
    settleAdjCol.setSortable(true);
    grid.addColumnDef(settleAdjCol);

    settledCol = new RGridColumnDef(PAccountDefrayalDef.settled_total);
    settledCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    settledCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    settledCol.setWidth("90px");
    settledCol.setName(FIELD_SETTLETOTAL);
    settledCol.setSortable(true);
    grid.addColumnDef(settledCol);

    needInvoiceCol = new RGridColumnDef(PAccountDefrayalDef.needInvoice_total);
    needInvoiceCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    needInvoiceCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    needInvoiceCol.setWidth("110px");
    needInvoiceCol.setName(FIELD_NEEDINVOICE);
    needInvoiceCol.setSortable(true);
    grid.addColumnDef(needInvoiceCol);

    invoiceAdjCol = new RGridColumnDef(PAccountDefrayalDef.invoiceAdj_total);
    invoiceAdjCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    invoiceAdjCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    invoiceAdjCol.setWidth("100px");
    invoiceAdjCol.setName(FIELD_INVOICEADJ);
    invoiceAdjCol.setSortable(true);
    grid.addColumnDef(invoiceAdjCol);

    invoicedCol = new RGridColumnDef(PAccountDefrayalDef.invoiced_total);
    invoicedCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    invoicedCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    invoicedCol.setWidth("110px");
    invoicedCol.setName(FIELD_INVOICETOTAL);
    invoicedCol.setSortable(true);
    grid.addColumnDef(invoicedCol);

    statementNumberCol = new RGridColumnDef(PAccountDefrayalDef.statement_billNumber);
    statementNumberCol.setWidth("160px");
    statementNumberCol.setName(FIELD_STATEMENTNUM);
    statementNumberCol.setSortable(true);
    grid.addColumnDef(statementNumberCol);

    statementSettleNoCol = new RGridColumnDef(statementSettleNo);
    statementSettleNoCol.setWidth("150px");
    grid.addColumnDef(statementSettleNoCol);

    dateRangeCol = new RGridColumnDef(dateRange);
    dateRangeCol.setWidth("150px");
    dateRangeCol.setName(FIELD_DATERANGE);
    dateRangeCol.setSortable(true);
    grid.addColumnDef(dateRangeCol);

    lastReceiptDateCol = new RGridColumnDef(lastReceiptDate);
    lastReceiptDateCol.setWidth("100px");
    lastReceiptDateCol.setName(FIELD_LASTRECEIPTDATE);
    lastReceiptDateCol.setSortable(true);
    grid.addColumnDef(lastReceiptDateCol);

    contractNumberCol = new RGridColumnDef(PAccountDefrayalDef.acc1_contract);
    contractNumberCol.setWidth("160px");
    contractNumberCol.setName(FIELD_CONTRACT_BILLNUMBER);
    contractNumberCol.setSortable(true);
    grid.addColumnDef(contractNumberCol);

    contractNameCol = new RGridColumnDef(PAccountDefrayalDef.acc1_contract_name);
    contractNameCol.setWidth("160px");
    contractNameCol.setName(FIELD_CONTRACT_NAME);
    contractNameCol.setSortable(true);
    grid.addColumnDef(contractNameCol);

    counterpartCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    counterpartCol.setWidth("150px");
    counterpartCol.setName(FIELD_COUNTERPART);
    counterpartCol.setSortable(true);
    grid.addColumnDef(counterpartCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  private Widget drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setWidth("100%");
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());
    flecsPanel.addField(PAccountDefrayalDef.acc1_sourceBill_billType);
    flecsPanel.addField(PAccountDefrayalDef.acc1_sourceBill_billNumber);
    flecsPanel.addField(PAccountDefrayalDef.acc1_subject);
    flecsPanel.addField(direction);
    flecsPanel.addField(accountUnit);
    flecsPanel.addField(counterpartType);
    flecsPanel.addField(counterpart);
    flecsPanel.addField(PAccountDefrayalDef.acc1_contract_code);
    flecsPanel.addField(PAccountDefrayalDef.acc1_contract_name);
    flecsPanel.addField(PAccountDefrayalDef.statement_billNumber);
    flecsPanel.addField(accountTime);
    flecsPanel.addField(statementSettleNo);
    flecsPanel.addField(settled);
    flecsPanel.addField(invoiced);
    flecsPanel.addBeforeLoadDataHandler(new BeforeLoadDataHandler<Integer>() {

      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent<Integer> event) {
        JumpParameters jumParams = new JumpParameters(START_NODE);
        FlecsConfig curConfig = flecsPanel.getCurrentConfig();
        curConfig.setShowConditions(flecsPanel.isShowConditions());
        jumParams.getUrlRef().set(PN_FLECSCONFIG, flecsConfigCodec.encode(curConfig));
        jumParams.getUrlRef().set(PN_PAGE, String.valueOf(event.getParameter()));
        ep.jump(jumParams);
        event.cancel();
      }
    });

    getToolbar().add(flecsPanel.getMenuButton(), Alignment.right);
    return flecsPanel;
  }

  private boolean checkIn() {
    if (!ep.isPermitted(AccountDefrayalPermDef.READ)) {
      NoAuthorized.open(AccountDefrayalMessages.M.moduleCaption());
      return false;
    }
    return true;
  }

  private void decodeParams(JumpParameters params) {
    sourceBillType = params.getUrlRef().get(KEY_SOURCEBILLTYPE);
    sourceBillNumber = params.getUrlRef().get(KEY_SOURCEBILLNUMBER);
    statementNumber = params.getUrlRef().get(KEY_STATEMENTNUMBER);
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(AccountDefrayalMessages.M.search());
    ep.getTitleBar().appendAttributeText(AccountDefrayalMessages.M.moduleCaption());
  }

  private FlecsConfigCodec getFlecsConfigCodec() {
    FlecsConfigCodec result = new FlecsConfigCodec();
    result.addOperandCodec(new BUCNCodec());
    return result;
  }

  private void refreshFlecs() {
    grid.setSort(sourceBillNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    String flecsStr = params.getUrlRef().get(PN_FLECSCONFIG);
    String pageInt = params.getUrlRef().get(PN_PAGE);
    int startPage = CodecUtils.decodeInt(pageInt, 0);
    if (flecsStr != null) {
      final FlecsConfig fc = getFlecsConfigCodec().decode(flecsStr);
      flecsPanel.setCurrentConfig(fc, startPage);
    } else if (StringUtil.isNullOrBlank(sourceBillNumber) == false
        && StringUtil.isNullOrBlank(sourceBillType) == false) {
      flecsPanel.clearConditions();
      flecsPanel.addCondition(new Condition(PAccountDefrayalDef.acc1_sourceBill_billType,
          DefaultOperator.EQUALS, sourceBillType));
      flecsPanel.addCondition(new Condition(PAccountDefrayalDef.acc1_sourceBill_billNumber,
          DefaultOperator.EQUALS, StringUtil.trim(sourceBillNumber)));
      flecsPanel.refresh();
      flecsPanel.setShowConditions(false);
    } else if (StringUtil.isNullOrBlank(statementNumber) == false) {
      flecsPanel.clearConditions();
      flecsPanel.addCondition(new Condition(PAccountDefrayalDef.statement_billNumber,
          DefaultOperator.EQUALS, StringUtil.trim(statementNumber)));
      flecsPanel.refresh();
      flecsPanel.setShowConditions(false);
    } else {
      if (flecsPanel.getDefaultConfig() == null) {
        flecsPanel.clearConditions();
        flecsPanel.addConditions(getDefaultConditions());
        flecsPanel.refresh();
      } else {
        flecsPanel.setCurrentConfig(flecsPanel.getDefaultConfig());
      }
    }
  }

  private List<Condition> getDefaultConditions() {
    List<Condition> conditions = new ArrayList<Condition>();
    conditions.add(new Condition(PAccountDefrayalDef.acc1_sourceBill_billType,
        DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PAccountDefrayalDef.acc1_sourceBill_billNumber,
        DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PAccountDefrayalDef.acc1_subject, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(direction, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpart, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PAccountDefrayalDef.acc1_contract_code, DefaultOperator.EQUALS,
        null));
    conditions.add(new Condition(PAccountDefrayalDef.acc1_contract_name, DefaultOperator.CONTAINS,
        null));
    conditions.add(new Condition(PAccountDefrayalDef.statement_billNumber, DefaultOperator.EQUALS,
        null));
    conditions.add(new Condition(accountTime, DefaultOperator.EQUALS, null));
    return conditions;
  }

  private class GridDataProvider implements RPageDataProvider<BAccountDefrayal> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAccountDefrayal>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);
      if (sortField != null)
        pageSort.appendOrder(sortField, sortDir);
      else
        pageSort.appendOrder(FIELD_SOURCEBILLNUMBER, OrderDir.desc);
      AccountDefrayalService.Locator.getService().query(
          flecsPanel.getCurrentConfig().getConditions(),
          flecsPanel.getCurrentConfig().getVisibleColumnNames(), pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BAccountDefrayal rowData,
        List<BAccountDefrayal> pageData) {
      if (col == sourceBillNumberCol.getIndex()) {
        if (rowData.getSourceBill() == null) {
          return null;
        }

        BDispatch dispatch = new BDispatch(rowData.getSourceBill().getBillType());
        dispatch.addParams(GRes.R.dispatch_key(), rowData.getSourceBill().getBillNumber());
        return dispatch;
      } else if (col == sourceBillTypeCol.getIndex())
        return rowData.getSourceBill() != null && rowData.getSourceBill().getBillType() != null ? ep
            .getBillType().get(rowData.getSourceBill().getBillType()) : null;
      else if (col == subjectCol.getIndex())
        return rowData.getSubject().getNameCode();
      else if (col == directionCol.getIndex())
        return DirectionType.getCaptionByValue(rowData.getDirection());
      else if (col == needSettleCol.getIndex())
        return rowData.getNeedSettle().doubleValue();
      else if (col == settleAdjCol.getIndex())
        return rowData.getSettleAdj().doubleValue();
      else if (col == settledCol.getIndex())
        return rowData.getSettled().doubleValue();
      else if (col == needInvoiceCol.getIndex())
        return rowData.getNeedInvoice().doubleValue();
      else if (col == invoiceAdjCol.getIndex())
        return rowData.getInvoiceAdj().doubleValue();
      else if (col == invoicedCol.getIndex())
        return rowData.getInvoiced().doubleValue();
      else if (col == statementNumberCol.getIndex())
        return getStatementNumber(rowData.getStatement());
      else if (col == statementSettleNoCol.getIndex())
        return rowData.getSettleNo();
      else if (col == dateRangeCol.getIndex())
        return formatDate(rowData.getBeginDate(), rowData.getEndDate());
      else if (col == lastReceiptDateCol.getIndex())
        return rowData.getLastReceiptDate() != null ? M3Format.fmt_yMd.format(rowData
            .getLastReceiptDate()) : null;
      else if (col == contractNumberCol.getIndex())
        return rowData.getContract().getCode();
      else if (col == contractNameCol.getIndex())
        return rowData.getContract().getName();
      else if (col == counterpartCol.getIndex())
        return rowData.getCounterpart().toFriendlyStr(ep.getCounterpartTypeMap());
      return null;
    }
  }

  private String getStatementNumber(BBill bill) {
    if (AccountDefrayalMessages.M.noneBillNumber().equals(bill.getBillNumber()))
      return null;
    else
      return bill.getBillNumber();
  }

  private String formatDate(Date beginDate, Date endDate) {
    if (beginDate != null && endDate != null)
      return M3Format.fmt_yMd.format(beginDate) + AccountDefrayalMessages.M.waveLine()
          + M3Format.fmt_yMd.format(endDate);
    else if (beginDate != null && endDate == null)
      return M3Format.fmt_yMd.format(beginDate) + AccountDefrayalMessages.M.waveLine();
    else if (beginDate == null && endDate != null)
      return AccountDefrayalMessages.M.waveLine() + M3Format.fmt_yMd.format(endDate);
    return null;
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (PAccountDefrayalDef.acc1_sourceBill_billType == field) {
        return DefaultOperator.EQUALS == operator ? new SourceBillTypeField() : null;
      } else if (PAccountDefrayalDef.acc1_subject == field) {
        if (subjectUCNBox == null)
          subjectUCNBox = new SubjectUCNBox(BSubjectType.credit.toString(), null, Boolean.TRUE,
              null);
        return DefaultOperator.CONTAINS == operator ? new SubjectUCNBox(
            BSubjectType.credit.toString(), null, Boolean.TRUE, null) : null;
      } else if (direction == field) {
        return new DirectionField();
      } else if (counterpart == field) {
        return DefaultOperator.EQUALS == operator ? buildCounterpart() : null;
      } else if (accountUnit == field) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (settled == field) {
        return DefaultOperator.EQUALS == operator ? new SettledField() : null;
      } else if (invoiced == field) {
        return DefaultOperator.EQUALS == operator ? new InvoicedField() : null;
      } else if (statementSettleNo == field) {
        SettleNoField settleNoField = new SettleNoField(statementSettleNo);
        settleNoField.refreshOption(12);
        settleNoField.setMaxLength(6);
        return settleNoField;
      } else if (counterpartType == field) {
        return new counterpartTypeField(counterpartType);
      }
      return super.createOperandWidget(field, operator);
    }
  }

  private class MyOperatorPolicy implements OperatorPolicy {

    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (PAccountDefrayalDef.acc1_sourceBill_billType == field || counterpart == field
          || accountUnit == field || settled == field || invoiced == field
          || counterpartType == field) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PAccountDefrayalDef.acc1_subject == field) {
        result.add(DefaultOperator.CONTAINS);
        return result;
      } else if (direction == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (statementSettleNo == field) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (accountTime == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.GREATER_THAN);
        result.add(DefaultOperator.GREATER_THAN_OR_EQUAL_TO);
        result.add(DefaultOperator.LESS_THAN);
        result.add(DefaultOperator.LESS_THAN_OR_EQUAL_TO);
        result.add(DefaultOperator.BETWEEN);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }

  private CounterpartUCNBox buildCounterpart() {
    CounterpartUCNBox counterpart = null;
    counterpart = new CounterpartUCNBox(null, true, ep.getCaptionMap());
    counterpart.setCounterTypeMap(ep.getCounterpartTypeMap());
    return counterpart;
  }

  private class counterpartTypeField extends RComboBox<String> {

    public counterpartTypeField(FieldDef fieldDef) {
      super();
      setFieldDef(fieldDef);
      setMaxDropdownRowCount(10);
      setNullOptionText(AccountDefrayalMessages.M.all());
      setEditable(false);
      for (Entry<String, String> entry : ep.getCounterpartTypeMap().entrySet()) {
        this.addOption(entry.getKey(), entry.getValue());
      }
    }
  }

  private class Handler_Grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(statementNumberCol)) {
        // BAccountDefrayal entity = pagingGrid.getRowData(cell.getRow());
        // if (!AccountDefrayalMessages.M.noneBillNumber().equals(
        // entity.getStatement().getBillNumber())) {
        // GwtUrl url = new GwtUrl(StatementUrlParams.ENTRY_FILE,
        // StatementUrlParams.View.START_NODE);
        // url.getParams().put(StatementUrlParams.View.PN_ENTITY_UUID,
        // entity.getStatement().getBillNumber());
        // try {
        // RWindow.navigate(url.toString(), RWindow.WINDOWNAME_BYMODULE);
        // } catch (Exception e) {
        // String msg =
        // AccountDefrayalMessages.M.cannotNavigate(url.toString());
        // RMsgBox.showError(msg, e);
        // }
        // }
      }
    }
  }

  private class SourceBillTypeField extends RComboBox<String> {
    private SourceBillTypeField() {
      super();
      setEditable(false);
      setRequired(false);
      setMaxDropdownRowCount(10);
      for (String key : ep.getBillType().keySet()) {
        addOption(key, ep.getBillType().get(key));
      }
    }
  }

  private class DirectionField extends RComboBox<Integer> {
    private DirectionField() {
      super();
      setEditable(false);
      setRequired(false);
      addOption(DirectionType.receipt.getDirectionValue(), AccountDefrayalMessages.M.receipet());
      addOption(DirectionType.payment.getDirectionValue(), AccountDefrayalMessages.M.payment());
    }
  }

  private class SettledField extends RComboBox<Boolean> {
    public SettledField() {
      super();
      setEditable(false);
      setRequired(false);
      addOption(Boolean.FALSE, AccountDefrayalMessages.M.no());
      addOption(Boolean.TRUE, AccountDefrayalMessages.M.yes());
    }
  }

  private class InvoicedField extends RComboBox<Boolean> {
    public InvoicedField() {
      super();
      setEditable(false);
      setRequired(false);
      addOption(Boolean.FALSE, AccountDefrayalMessages.M.no());
      addOption(Boolean.TRUE, AccountDefrayalMessages.M.yes());
    }
  }

}
