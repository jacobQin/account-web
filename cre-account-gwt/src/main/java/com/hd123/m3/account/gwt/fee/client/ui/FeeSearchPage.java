/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeSearchPage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BUsageType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.fee.client.EPFee;
import com.hd123.m3.account.gwt.fee.client.FeeMessages;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeLogger;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeService;
import com.hd123.m3.account.gwt.fee.client.ui.gadget.FeeExportDialog;
import com.hd123.m3.account.gwt.fee.client.ui.gadget.FeeImportFactory;
import com.hd123.m3.account.gwt.fee.intf.client.FeeUrlParams;
import com.hd123.m3.account.gwt.fee.intf.client.FeeUrlParams.Search;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeDef;
import com.hd123.m3.account.gwt.fee.intf.client.perm.FeePermDef;
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
import com.hd123.m3.commons.gwt.widget.client.biz.BImpParams;
import com.hd123.m3.commons.gwt.widget.client.biz.PrintingTemplate;
import com.hd123.m3.commons.gwt.widget.client.ui.imp.ImpHandler;
import com.hd123.m3.commons.gwt.widget.client.ui.print.BeforePrintCallback;
import com.hd123.m3.commons.gwt.widget.client.ui.print.BeforePrintHandler;
import com.hd123.m3.commons.gwt.widget.client.ui.print.PrintButton;
import com.hd123.m3.commons.gwt.widget.impex.client.dialog.ImpMonDialog;
import com.hd123.m3.commons.gwt.widget.impex.client.dialog.gadget.job.RJobMonitorListener;
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
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarMenuButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author subinzhu
 * 
 */
public class FeeSearchPage extends BaseBpmSearchPage implements Search, BeforePrintHandler {

  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef(FIELD_COUNTERPART, EPFee
      .getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
  public static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef(FIELD_ACCOUNTUNIT, EPFee
      .getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
  public static EmbeddedFieldDef subject = new EmbeddedFieldDef(FIELD_SUBJECT,
      WidgetRes.R.subject());
  public static EmbeddedFieldDef permGroup = new EmbeddedFieldDef(FIELD_PERMGROUP,
      FeeMessages.M.permGroup());

  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(
      FeeUrlParams.Flecs.FIELD_COUNTERPARTTYPE, EPFee.getInstance().getFieldCaption(
          GRes.FIELDNAME_COUNTERPART_TYPE, GRes.R.counterpartType()));

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes R = GWT.create(WidgetRes.class);

    @DefaultStringValue("科目")
    String subject();
  }

  private EPFee ep = EPFee.getInstance();
  private static FeeSearchPage instance;

  public static FeeSearchPage getInstance() {
    if (instance == null) {
      instance = new FeeSearchPage();
    }
    return instance;
  }

  public FeeSearchPage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  private void afterDraw() {
    batchProcessor = new PagingGridBatchProcessor<BFee>(pagingGrid, PFeeDef.TABLE_CAPTION);
    flecsConfigCodec = new FlecsConfigCodec();
    flecsConfigCodec.addOperatorCodec(new DefaultOperatorCodec());
    flecsConfigCodec.addOperandCodec(new BUCNCodec());
  }

  // Flecs
  private JumpParameters params;
  private FlecsConfigCodec flecsConfigCodec;
  private RFlecsPanel flecsPanel;
  private String keyword;
  private PageSort pageSort;
  // 工具栏
  private RToolbarMenuButton moreButton;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;
  private RAction exportAction;
  private RAction importAction;
  protected PrintButton printButton = null;

  // 结果表格
  private RGrid grid;
  private RPagingGrid pagingGrid;
  private PagingGridBatchProcessor<BFee> batchProcessor;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef counterpartUnitCol;
  private RGridColumnDef contractCodeCol;
  private RGridColumnDef contractNameCol;
  private RGridColumnDef directionTypeCol;
  private RGridColumnDef accountDateCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef remarkCol;
  // 下拉列表
  private RMenuItem editOneMenuItem;
  private RMenuItem deleteOneMenuItem;
  private RMenuItem effectOneMenuItem;
  private RMenuItem abortOneMenuItem;

  private Handler_click clickHandler = new Handler_click();

  private FeeExportDialog exportDialog;
  private ImpMonDialog importDialog;
  private FeeImportFactory importFactory;

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());
    getToolbar().addSeparator();

    exportAction = new RAction(FeeMessages.M.exportTemplate() + "...", clickHandler);
    importAction = new RAction(FeeMessages.M.imp() + "...", clickHandler);
    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    effectAction = new RAction(FeeMessages.M.effect(), clickHandler);
    abortAction = new RAction(FeeMessages.M.abort() + "...", clickHandler);

    moreButton = new RToolbarMenuButton(FeeMessages.M.operate(), moreMenu);
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
    pagingGrid = new RPagingGrid<BFee>(grid, new GridDataProvider());
    pagingGrid.addLoadDataHandler(this);
    drawFlecsPanel();

    return flecsPanel;
  }

  private Widget drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setWidth("100%");
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());
    flecsPanel.addField(PFeeDef.billNumber);
    flecsPanel.addField(PFeeDef.bizState);
    flecsPanel.addField(counterpartType);
    flecsPanel.addField(counterpart);
    flecsPanel.addField(PFeeDef.contract_code);
    flecsPanel.addField(PFeeDef.contract_name);
    flecsPanel.addField(PFeeDef.accountDate);
    flecsPanel.addField(subject);
    flecsPanel.addField(PFeeDef.settleNo);
    flecsPanel.addField(PFeeDef.coopMode);
    if (ep.isPermEnabled()) {
      flecsPanel.addField(permGroup);
    }
    flecsPanel.addField(accountUnit);
    flecsPanel.addField(PFeeDef.createInfo_operator_id);
    flecsPanel.addField(PFeeDef.createInfo_operator_fullName);
    flecsPanel.addField(PFeeDef.createInfo_time);
    flecsPanel.addField(PFeeDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PFeeDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PFeeDef.lastModifyInfo_time);
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

  /** 定制操作数 */
  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (PFeeDef.settleNo == field) {
        SettleNoField settleNoField = new SettleNoField(PFeeDef.settleNo);
        settleNoField.refreshOption(12);
        settleNoField.setMaxLength(6);
        return settleNoField;
      } else if (counterpart == field) {
        return DefaultOperator.EQUALS == operator ? buildCounterpart() : null;
      } else if (subject == field) {
        return DefaultOperator.CONTAINS == operator ? new SubjectUCNBox(BSubjectType.credit.name(),
            null, null, BUsageType.tempFee.name()) : null;
      } else if (accountUnit == field) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (permGroup == field) {
        return new PermGroupField(permGroup);
      } else if (counterpartType == field) {
        return new CounterpartTypeField(counterpartType);
      } else if (field == PFeeDef.coopMode) {
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
      setNullOptionText(FeeMessages.M.all());
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
      if (PFeeDef.settleNo == field || counterpart == field || accountUnit == field
          || counterpartType == field) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (permGroup == field || PFeeDef.coopMode.equals(field)) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (subject == field) {
        result.add(DefaultOperator.CONTAINS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }

  }

  private void drawLineMenu() {
    editOneMenuItem = new RMenuItem(RActionFacade.EDIT, clickHandler);
    editOneMenuItem.setHotKey(null);

    deleteOneMenuItem = new RMenuItem(RActionFacade.DELETE, clickHandler);
    deleteOneMenuItem.setHotKey(null);

    effectOneMenuItem = new RMenuItem(FeeMessages.M.effect(), clickHandler);
    effectOneMenuItem.setHotKey(null);

    abortOneMenuItem = new RMenuItem(FeeMessages.M.abort() + "...", clickHandler);
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
          if (o instanceof BFee
              && StringUtil.isNullOrBlank(((BFee) o).getStore().getUuid()) == false) {
            storeCodes.add(((BFee) o).getStore().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    billNumberCol = new RGridColumnDef(PFeeDef.billNumber);
    billNumberCol.setWidth("170px");
    billNumberCol.setName(FIELD_BILLNUMBER);
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("130px")));
    billNumberCol.setSortable(true);
    billNumberCol.setAllowHiding(false);
    grid.addColumnDef(billNumberCol);

    bizStateCol = new RGridColumnDef(PFeeDef.bizState);
    bizStateCol.setWidth("80px");
    bizStateCol.setName(FIELD_BIZSTATE);
    bizStateCol.setSortable(true);
    grid.addColumnDef(bizStateCol);

    counterpartUnitCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    counterpartUnitCol.setWidth("160px");
    counterpartUnitCol.setSortable(true);
    counterpartUnitCol.setName(FIELD_COUNTERPART);
    grid.addColumnDef(counterpartUnitCol);

    contractCodeCol = new RGridColumnDef(PFeeDef.contract_code);
    contractCodeCol.setWidth("160px");
    contractCodeCol.setName(FIELD_CONTRACT_CODE);
    contractCodeCol.setSortable(true);
    grid.addColumnDef(contractCodeCol);

    contractNameCol = new RGridColumnDef(PFeeDef.contract_name);
    contractNameCol.setWidth("160px");
    contractNameCol.setName(FIELD_CONTRACT_NAME);
    contractNameCol.setSortable(true);
    grid.addColumnDef(contractNameCol);

    directionTypeCol = new RGridColumnDef(PFeeDef.constants.direction());
    directionTypeCol.setWidth("80px");
    directionTypeCol.setSortable(true);
    directionTypeCol.setName(FIELD_DIRECTION);
    grid.addColumnDef(directionTypeCol);

    accountDateCol = new RGridColumnDef(PFeeDef.accountDate);
    accountDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    accountDateCol.setWidth("80px");
    accountDateCol.setSortable(true);
    accountDateCol.setName(FIELD_ACCOUNTDATE);
    grid.addColumnDef(accountDateCol);

    totalCol = new RGridColumnDef(PFeeDef.total_total);
    totalCol.setWidth("120px");
    totalCol.setSortable(true);
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(totalCol);

    accountUnitCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    accountUnitCol.setWidth("160px");
    accountUnitCol.setSortable(true);
    accountUnitCol.setName(FIELD_ACCOUNTUNIT);
    grid.addColumnDef(accountUnitCol);

    remarkCol = new RGridColumnDef(PFeeDef.remark);
    remarkCol.setSortable(true);
    remarkCol.setName(FIELD_REMARK);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private class GridDataProvider implements RPageDataProvider<BFee> {
    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BFee>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);
      if (sortField != null) {
        pageSort.appendOrder(sortField, sortDir);
      } else {
        pageSort.appendOrder(FIELD_BILLNUMBER, OrderDir.desc);
      }

      FeeService.Locator.getService().query(flecsPanel.getCurrentConfig().getConditions(),
          pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BFee rowData, List<BFee> pageData) {
      if (col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      else if (col == bizStateCol.getIndex())
        return PFeeDef.bizState.getEnumCaption(rowData.getBizState());
      else if (col == counterpartUnitCol.getIndex())
        return rowData.getCounterpart().toFriendlyStr(getEP().getCounterpartTypeMap());
      else if (col == contractCodeCol.getIndex())
        return rowData.getContract().getBillNumber();
      else if (col == contractNameCol.getIndex())
        return rowData.getContract().getName();
      else if (col == directionTypeCol.getIndex())
        return DirectionType.getCaptionByValue(rowData.getDirection());
      else if (col == accountDateCol.getIndex())
        return rowData.getAccountDate();
      else if (col == totalCol.getIndex()) {
        BigDecimal total = (rowData.getTotal() == null || rowData.getTotal().getTotal() == null) ? BigDecimal.ZERO
            : rowData.getTotal().getTotal();
        return M3Format.fmt_money.format(total.doubleValue());
      } else if (col == accountUnitCol.getIndex()) {
        return rowData.getAccountUnit() == null ? null : rowData.getAccountUnit().toFriendlyStr();
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      }
      return null;
    }
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

    if (!checkIn())
      return;

    refreshTitle();
    decodeParams(params);
    refreshFlecs();
    refreshCommonds();
  }

  private void refreshCommonds() {
    deleteAction.setVisible(ep.isPermitted(FeePermDef.DELETE));
    effectAction.setVisible(ep.isPermitted(FeePermDef.EFFECT));
    abortAction.setVisible(ep.isPermitted(FeePermDef.ABORT));
    exportAction.setVisible(ep.isPermitted(FeePermDef.IMPORT));
    importAction.setVisible(ep.isPermitted(FeePermDef.IMPORT));
    printButton.setEnabled(ep.isPermitted(BAction.PRINT.getKey()));

    boolean visible = false;
    for (Widget item : moreMenu.getItems()) {
      if (item.isVisible()) {
        visible = true;
        break;
      }
    }
    if (visible == false) {
      visible = deleteAction.isVisible() || effectAction.isVisible() || abortAction.isVisible()
          || exportAction.isVisible() || importAction.isVisible();
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
    if (ep.isPermitted(FeePermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void decodeParams(JumpParameters params) {
    keyword = params.getUrlRef().get(PN_KEYWORD);
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(FeeMessages.M.search());
    ep.getTitleBar().appendAttributeText(PFeeDef.constants.tableCaption());
  }

  private void refreshFlecs() {
    grid.setSort(billNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel.addCondition(new Condition(PFeeDef.billNumber, DefaultOperator.STARTS_WITH,
          keyword));
      flecsPanel.refresh();
    } else {
      String flecsStr = params.getUrlRef().get(PN_FLECSCONFIG);
      String pageInt = params.getUrlRef().get(PN_PAGE);
      int startPage = CodecUtils.decodeInt(pageInt, 0);
      if (flecsStr != null) {
        final FlecsConfig fc = flecsConfigCodec.decode(flecsStr);
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
    conditions.add(new Condition(PFeeDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PFeeDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpart, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PFeeDef.contract_code, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PFeeDef.contract_name, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PFeeDef.accountDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(subject, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PFeeDef.settleNo, DefaultOperator.EQUALS, SettleNoField
        .getCurrentSettleNo()));
    return conditions;
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
      } else if (event.getSource().equals(editOneMenuItem)) {
        doEditOne(event);
      } else if (event.getSource().equals(deleteOneMenuItem)) {
        doDeleteOne(event);
      } else if (event.getSource().equals(effectOneMenuItem)) {
        doEffectOne(event);
      } else if (event.getSource().equals(abortOneMenuItem)) {
        doAbortOne(event);
      } else if (event.getSource().equals(exportAction)) {
        doExport();
      } else if (event.getSource().equals(importAction)) {
        doImport();
      }
    }

  }

  private void doDelete() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(FeeMessages.M.seleteDataToAction(FeeMessages.M.delete(),
          PFeeDef.TABLE_CAPTION));
      return;
    }

    batchProcessor.batchProcess(FeeMessages.M.delete(), new BFee[] {},
        new PagingGridBatchProcessCallback<BFee>() {
          @Override
          public void execute(BFee bill, BatchProcesser<BFee> processer, AsyncCallback callback) {
            if (!BBizStates.INEFFECT.equals(bill.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            FeeService.Locator.getService().delete(bill.getUuid(), bill.getVersion(), callback);
          }

          @Override
          public void onSuccess(BFee entity, Object result) {
            // 记录日志
            super.onSuccess(entity, result);
          }
        });
  }

  private void doEffect() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(FeeMessages.M.seleteDataToAction(FeeMessages.M.effect(),
          PFeeDef.TABLE_CAPTION));
      return;
    }

    batchProcessor.batchProcess(FeeMessages.M.effect(), new BFee[] {},
        new PagingGridBatchProcessCallback<BFee>() {
          @Override
          public void execute(BFee bill, BatchProcesser<BFee> processer, AsyncCallback callback) {
            if (!BBizStates.INEFFECT.equals(bill.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            FeeService.Locator.getService().effect(bill.getUuid(), null, bill.getVersion(),
                callback);
            BFeeLogger.getInstance().log(FeeMessages.M.effect(), bill);
          }

          @Override
          public void onSuccess(BFee entity, Object result) {
            // 记录日志
            super.onSuccess(entity, result);
          }
        });
  }

  private void doAbortConfirm() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(FeeMessages.M.seleteDataToAction(FeeMessages.M.abort(),
          PFeeDef.TABLE_CAPTION));
      return;
    }

    InputBox.show(FeeMessages.M.abortReason(), null, true, PFeeDef.latestComment,
        new InputBox.Callback() {
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            doAbort(text);
          }
        });
  }

  private void doAbort(final String reason) {
    batchProcessor.batchProcess(FeeMessages.M.abort(), new BFee[] {}, true, false,
        new PagingGridBatchProcessCallback<BFee>() {
          @Override
          public void execute(BFee bill, BatchProcesser<BFee> processer, AsyncCallback callback) {
            if (!BBizStates.EFFECT.equals(bill.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            FeeService.Locator.getService().abort(bill.getUuid(), reason, bill.getVersion(),
                callback);
            BFeeLogger.getInstance().log(FeeMessages.M.abort(), bill);
          }

          @Override
          public void onSuccess(BFee entity, Object result) {
            // 记录日志
            super.onSuccess(entity, result);
          }
        });
  }

  private void doEditOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BFee bill = (BFee) pagingGrid.getPageData().get(hotItem.getGridRow());

    JumpParameters jumParams = new JumpParameters(FeeEditPage.START_NODE);
    jumParams.getUrlRef().set(FeeEditPage.PN_UUID, bill.getUuid());
    ep.jump(jumParams);
  }

  private void doDeleteOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BFee bill = (BFee) pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmDeleteOne(bill);
  }

  private void doConfirmDeleteOne(final BFee bill) {
    getMessagePanel().clearMessages();

    String msg = FeeMessages.M.actionComfirm2(FeeMessages.M.delete(), PFeeDef.TABLE_CAPTION,
        bill.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        deleteOne(bill);
      }
    });
  }

  private void deleteOne(final BFee bill) {
    RLoadingDialog.show(FeeMessages.M.beDoing(FeeMessages.M.delete(), bill.getBillNumber()));
    FeeService.Locator.getService().delete(bill.getUuid(), bill.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = FeeMessages.M.actionFailed2(FeeMessages.M.delete(), PFeeDef.TABLE_CAPTION,
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
            // 记录日志
            String msg = FeeMessages.M.onSuccess(FeeMessages.M.delete(), PFeeDef.TABLE_CAPTION,
                bill.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doEffectOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BFee bill = (BFee) pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmEffectOne(bill);
  }

  private void doConfirmEffectOne(final BFee bill) {
    getMessagePanel().clearMessages();

    String msg = FeeMessages.M.actionComfirm2(FeeMessages.M.effect(), PFeeDef.TABLE_CAPTION,
        bill.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        effectOne(bill);
      }
    });
  }

  private void effectOne(final BFee bill) {
    RLoadingDialog.show(FeeMessages.M.beDoing(FeeMessages.M.effect(), bill.getBillNumber()));
    FeeService.Locator.getService().effect(bill.getUuid(), null, bill.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            BFeeLogger.getInstance().log(FeeMessages.M.effect(), bill);
            String msg = FeeMessages.M.actionFailed2(FeeMessages.M.effect(), PFeeDef.TABLE_CAPTION,
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
            // 记录日志
            String msg = FeeMessages.M.onSuccess(FeeMessages.M.effect(), PFeeDef.TABLE_CAPTION,
                bill.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doAbortOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BFee bill = (BFee) pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmAbortOne(bill);
  }

  private void doConfirmAbortOne(final BFee bill) {
    getMessagePanel().clearMessages();

    InputBox.show(FeeMessages.M.abortReason(), null, true, PFeeDef.latestComment,
        new InputBox.Callback() {
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            abortOne(bill, text);
          }
        });
  }

  private void abortOne(final BFee bill, String comment) {
    RLoadingDialog.show(FeeMessages.M.beDoing(FeeMessages.M.abort(), bill.getBillNumber()));
    FeeService.Locator.getService().abort(bill.getUuid(), comment, bill.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = FeeMessages.M.actionFailed2(FeeMessages.M.abort(), PFeeDef.TABLE_CAPTION,
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
            BFeeLogger.getInstance().log(FeeMessages.M.abort(), bill);
            // 记录日志
            String msg = FeeMessages.M.onSuccess(FeeMessages.M.abort(), PFeeDef.TABLE_CAPTION,
                bill.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doExport() {
    if (exportDialog == null) {
      exportDialog = new FeeExportDialog();
    }
    exportDialog.center();
  }

  private void doImport() {
    if (importFactory == null) {
      importFactory = new FeeImportFactory();
    }

    String fileDir = ep.getModuleContext().get(FeeService.EXPORTDIR);
    String downloadUrl = ep.getModuleContext().get(FeeService.DOWANLOAD_URL);
    importDialog = new ImpMonDialog(importFactory, 1);
    importDialog.setJobTitle("批量导入费用单");
    importDialog.getContentPanel().setImportDir(fileDir);
    importDialog.getContentPanel().setImpSuffix("csv", "xls", "xlsx");
    importDialog.getContentPanel().setShowStrategy(false);
    importDialog.getContentPanel().setDownloadURL(downloadUrl);
    importDialog.getContentPanel().getImpParams().setPluginName("导入费用单");
    importDialog.getContentPanel().getImpParams().setMaxCount(500);
    importDialog.setImpHandler(new ImpHandler() {
      @Override
      public void onImport(BImpParams params) {
        if (params.getFile() != null) {
          importDialog.start();
        }
      }
    });
    importDialog.setMoniterListener(new RJobMonitorListener() {
      @Override
      public void onSchedule(Map params, AsyncCallback callback) {
        FeeService.Locator.getService().importFile(importDialog.getContentPanel().getImpParams(),
            importFactory.getPermGroupId(), importFactory.getPermGroupTitle(), callback);
      }
    });
    importDialog.addCloseHandler(new CloseHandler<PopupPanel>() {
      @Override
      public void onClose(CloseEvent<PopupPanel> event) {
        if (importDialog.getContentPanel() != null
            && importDialog.getContentPanel().getImpResult() != null
            && importDialog.getContentPanel().getImpResult().getSuccessNum() > 0) {
          importDialog.getContentPanel().getImpResult().setSuccessNum(-1);
        }
        importFactory.hideResultPanel();
        pagingGrid.gotoFirstPage();
      }
    });
    importDialog.show();
  }

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      RGridColumnDef colDef = cell.getColumnDef();
      int row = cell.getRow();

      BFee bill = (BFee) pagingGrid.getPageData().get(row);
      if (bill == null)
        return;
      if (colDef.equals(billNumberCol)) {
        ep.jumpToViewPage(bill.getUuid());
      }
    }
  }

  @Override
  protected RPagingGrid getPagingGrid() {
    return pagingGrid;
  }

  @Override
  protected EPFee getEP() {
    return EPFee.getInstance();
  }

  @Override
  public void beforeShow(JumpParameters params) {
    super.beforeShow(params);
    getEP().appendSearchBox();
  }

  @Override
  protected void appendBatchMenu(RPopupMenu menu) {
    menu.addItem(new RMenuItem(exportAction));
    menu.addItem(new RMenuItem(importAction));
    menu.addSeparator();
    menu.addItem(new RMenuItem(deleteAction));
    menu.addSeparator();
    menu.addItem(new RMenuItem(effectAction));
    menu.addItem(new RMenuItem(abortAction));
  }

  @Override
  protected void appendLineMenu(RPopupMenu menu, Object bill) {
    BFee entity = (BFee) bill;
    menu.addSeparator();
    menu.addItem(editOneMenuItem);
    menu.addItem(deleteOneMenuItem);
    menu.addSeparator();
    menu.addItem(effectOneMenuItem);
    menu.addItem(abortOneMenuItem);

    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());

    boolean canEdit = ep.isPermitted(FeePermDef.UPDATE);
    boolean canDelete = ep.isPermitted(FeePermDef.DELETE);
    boolean canEffect = ep.isPermitted(FeePermDef.EFFECT);
    boolean canAbort = ep.isPermitted(FeePermDef.ABORT);

    editOneMenuItem.setVisible(isInEffect && canEdit);
    deleteOneMenuItem.setVisible(isInEffect && canDelete);
    effectOneMenuItem.setVisible(isInEffect && canEffect);
    abortOneMenuItem.setVisible(isEffect && canAbort);
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
