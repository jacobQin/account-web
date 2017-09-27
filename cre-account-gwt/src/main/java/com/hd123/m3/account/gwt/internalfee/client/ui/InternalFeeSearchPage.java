/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.ui;

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
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BUsageType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.internalfee.client.EPInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.InternalFeeMessages;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFeeLogger;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeService;
import com.hd123.m3.account.gwt.internalfee.client.ui.gadget.VendorUCNBox;
import com.hd123.m3.account.gwt.internalfee.intf.client.InternalFeeUrlParams.Search;
import com.hd123.m3.account.gwt.internalfee.intf.client.dd.PInternalFeeDef;
import com.hd123.m3.account.gwt.internalfee.intf.client.perm.InternalFeePermDef;
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
 * @author liuguilin
 * 
 */
public class InternalFeeSearchPage extends BaseBpmSearchPage implements Search, BeforePrintHandler {

  public static EmbeddedFieldDef subject = new EmbeddedFieldDef(FIELD_SUBJECT,
      InternalFeeMessages.M.subject());
  public static EmbeddedFieldDef permGroup = new EmbeddedFieldDef(FIELD_PERMGROUP,
      InternalFeeMessages.M.permGroup());

  private EPInternalFee ep = EPInternalFee.getInstance();
  private static InternalFeeSearchPage instance;

  public static InternalFeeSearchPage getInstance() {
    if (instance == null) {
      instance = new InternalFeeSearchPage();
    }
    return instance;
  }

  public InternalFeeSearchPage() {
    super();
    drawToolbar();
    drawSelf();
    afterDraw();
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
  protected PrintButton printButton = null;

  // 结果表格
  private RGrid grid;
  private RPagingGrid pagingGrid;
  private PagingGridBatchProcessor<BInternalFee> batchProcessor;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef vendorCol;
  private RGridColumnDef directionTypeCol;
  private RGridColumnDef accountDateCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef storeCol;
  private RGridColumnDef remarkCol;
  // 下拉列表
  private RMenuItem editOneMenuItem;
  private RMenuItem deleteOneMenuItem;
  private RMenuItem effectOneMenuItem;
  private RMenuItem abortOneMenuItem;

  private Handler_click clickHandler = new Handler_click();

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());
    getToolbar().addSeparator();

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    effectAction = new RAction(InternalFeeMessages.M.effect(), clickHandler);
    abortAction = new RAction(InternalFeeMessages.M.abort() + "...", clickHandler);

    moreButton = new RToolbarMenuButton(InternalFeeMessages.M.operate(), moreMenu);
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
    pagingGrid = new RPagingGrid<BInternalFee>(grid, new GridDataProvider());
    pagingGrid.addLoadDataHandler(this);
    drawFlecsPanel();

    return flecsPanel;
  }

  private void drawLineMenu() {
    editOneMenuItem = new RMenuItem(RActionFacade.EDIT, clickHandler);
    editOneMenuItem.setHotKey(null);

    deleteOneMenuItem = new RMenuItem(RActionFacade.DELETE, clickHandler);
    deleteOneMenuItem.setHotKey(null);

    effectOneMenuItem = new RMenuItem(InternalFeeMessages.M.effect(), clickHandler);
    effectOneMenuItem.setHotKey(null);

    abortOneMenuItem = new RMenuItem(InternalFeeMessages.M.abort() + "...", clickHandler);
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
          if (o instanceof BInternalFee
              && StringUtil.isNullOrBlank(((BInternalFee) o).getStore().getUuid()) == false) {
            storeCodes.add(((BInternalFee) o).getStore().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    billNumberCol = new RGridColumnDef(PInternalFeeDef.billNumber);
    billNumberCol.setWidth("170px");
    billNumberCol.setName(FIELD_BILLNUMBER);
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("130px")));
    billNumberCol.setSortable(true);
    billNumberCol.setAllowHiding(false);
    grid.addColumnDef(billNumberCol);

    bizStateCol = new RGridColumnDef(PInternalFeeDef.bizState);
    bizStateCol.setWidth("80px");
    bizStateCol.setName(FIELD_BIZSTATE);
    bizStateCol.setSortable(true);
    grid.addColumnDef(bizStateCol);

    vendorCol = new RGridColumnDef(PInternalFeeDef.vendor);
    vendorCol.setWidth("180px");
    vendorCol.setSortable(true);
    vendorCol.setName(FIELD_VENDOR);
    grid.addColumnDef(vendorCol);

    directionTypeCol = new RGridColumnDef(PInternalFeeDef.constants.direction());
    directionTypeCol.setWidth("80px");
    directionTypeCol.setSortable(true);
    directionTypeCol.setName(FIELD_DIRECTION);
    grid.addColumnDef(directionTypeCol);

    accountDateCol = new RGridColumnDef(PInternalFeeDef.accountDate);
    accountDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    accountDateCol.setWidth("80px");
    accountDateCol.setSortable(true);
    accountDateCol.setName(FIELD_ACCOUNTDATE);
    grid.addColumnDef(accountDateCol);

    totalCol = new RGridColumnDef(PInternalFeeDef.total_total);
    totalCol.setWidth("120px");
    totalCol.setSortable(true);
    totalCol.setName(FIELD_TOTAL);
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    grid.addColumnDef(totalCol);

    storeCol = new RGridColumnDef(PInternalFeeDef.store);
    storeCol.setWidth("160px");
    storeCol.setSortable(true);
    storeCol.setName(FIELD_STORE);
    grid.addColumnDef(storeCol);

    remarkCol = new RGridColumnDef(PInternalFeeDef.remark);
    remarkCol.setSortable(true);
    remarkCol.setName(FIELD_REMARK);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private Widget drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setWidth("100%");
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());
    flecsPanel.addField(PInternalFeeDef.billNumber);
    flecsPanel.addField(PInternalFeeDef.bizState);
    flecsPanel.addField(PInternalFeeDef.store);
    flecsPanel.addField(PInternalFeeDef.vendor);
    flecsPanel.addField(PInternalFeeDef.accountDate);
    flecsPanel.addField(subject);
    flecsPanel.addField(PInternalFeeDef.settleNo);
    if (ep.isPermEnabled()) {
      flecsPanel.addField(permGroup);
    }
    flecsPanel.addField(PInternalFeeDef.createInfo_operator_id);
    flecsPanel.addField(PInternalFeeDef.createInfo_operator_fullName);
    flecsPanel.addField(PInternalFeeDef.createInfo_time);
    flecsPanel.addField(PInternalFeeDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PInternalFeeDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PInternalFeeDef.lastModifyInfo_time);
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
      if (PInternalFeeDef.settleNo == field) {
        SettleNoField settleNoField = new SettleNoField(PInternalFeeDef.settleNo);
        settleNoField.refreshOption(12);
        settleNoField.setMaxLength(6);
        return settleNoField;
      } else if (PInternalFeeDef.vendor == field) {
        return DefaultOperator.EQUALS == operator ? new VendorUCNBox(false, null) : null;
      } else if (subject == field) {
        return DefaultOperator.CONTAINS == operator ? new SubjectUCNBox(BSubjectType.credit.name(),
            null, null, BUsageType.tempFee.name()) : null;
      } else if (PInternalFeeDef.store == field) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (permGroup == field) {
        return new PermGroupField(permGroup);
      }
      return super.createOperandWidget(field, operator);
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
      if (PInternalFeeDef.settleNo == field || PInternalFeeDef.vendor == field
          || PInternalFeeDef.store == field) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (subject == field) {
        result.add(DefaultOperator.CONTAINS);
        return result;
      } else if (permGroup == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }

  private void afterDraw() {
    batchProcessor = new PagingGridBatchProcessor<BInternalFee>(pagingGrid,
        PInternalFeeDef.TABLE_CAPTION);
    flecsConfigCodec = new FlecsConfigCodec();
    flecsConfigCodec.addOperatorCodec(new DefaultOperatorCodec());
    flecsConfigCodec.addOperandCodec(new BUCNCodec());
  }

  @Override
  protected RPagingGrid getPagingGrid() {
    return pagingGrid;
  }

  @Override
  protected EPInternalFee getEP() {
    return EPInternalFee.getInstance();
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
    BInternalFee entity = (BInternalFee) bill;
    menu.addSeparator();
    menu.addItem(editOneMenuItem);
    menu.addItem(deleteOneMenuItem);
    menu.addSeparator();
    menu.addItem(effectOneMenuItem);
    menu.addItem(abortOneMenuItem);

    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());

    boolean canEdit = ep.isPermitted(InternalFeePermDef.UPDATE);
    boolean canDelete = ep.isPermitted(InternalFeePermDef.DELETE);
    boolean canEffect = ep.isPermitted(InternalFeePermDef.EFFECT);
    boolean canAbort = ep.isPermitted(InternalFeePermDef.ABORT);

    editOneMenuItem.setVisible(isInEffect && canEdit);
    deleteOneMenuItem.setVisible(isInEffect && canDelete);
    effectOneMenuItem.setVisible(isInEffect && canEffect);
    abortOneMenuItem.setVisible(isEffect && canAbort);
  }

  @Override
  public void onHide() {
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void beforeShow(JumpParameters params) {
    super.beforeShow(params);
    getEP().appendSearchBox();
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

  public JumpParameters getLastParams() {
    if (params != null)
      params.getMessages().clear();
    return params;
  }

  private boolean checkIn() {
    if (ep.isPermitted(InternalFeePermDef.READ) == false) {
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
    ep.getTitleBar().setTitleText(InternalFeeMessages.M.search());
    ep.getTitleBar().appendAttributeText(PInternalFeeDef.constants.tableCaption());
  }

  private void refreshFlecs() {
    grid.setSort(billNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel.addCondition(new Condition(PInternalFeeDef.billNumber,
          DefaultOperator.STARTS_WITH, keyword));
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
    conditions.add(new Condition(PInternalFeeDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInternalFeeDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInternalFeeDef.store, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInternalFeeDef.vendor, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInternalFeeDef.accountDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(subject, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PInternalFeeDef.settleNo, DefaultOperator.EQUALS, SettleNoField
        .getCurrentSettleNo()));
    return conditions;
  }

  private void refreshCommonds() {
    deleteAction.setVisible(ep.isPermitted(InternalFeePermDef.DELETE));
    effectAction.setVisible(ep.isPermitted(InternalFeePermDef.EFFECT));
    abortAction.setVisible(ep.isPermitted(InternalFeePermDef.ABORT));
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

  private class GridDataProvider implements RPageDataProvider<BInternalFee> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BInternalFee>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);
      if (sortField != null) {
        pageSort.appendOrder(sortField, sortDir);
      } else {
        pageSort.appendOrder(FIELD_BILLNUMBER, OrderDir.desc);
      }

      InternalFeeService.Locator.getService().query(flecsPanel.getCurrentConfig().getConditions(),
          pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BInternalFee rowData, List<BInternalFee> pageData) {
      if (col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      else if (col == bizStateCol.getIndex())
        return PInternalFeeDef.bizState.getEnumCaption(rowData.getBizState());
      else if (col == vendorCol.getIndex())
        return rowData.getVendor() == null ? null : rowData.getVendor().toFriendlyStr();
      else if (col == directionTypeCol.getIndex())
        return DirectionType.getCaptionByValue(rowData.getDirection());
      else if (col == accountDateCol.getIndex())
        return rowData.getAccountDate();
      else if (col == totalCol.getIndex()) {
        BigDecimal total = (rowData.getTotal() == null || rowData.getTotal().getTotal() == null) ? BigDecimal.ZERO
            : rowData.getTotal().getTotal();
        return M3Format.fmt_money.format(total.doubleValue());
      } else if (col == storeCol.getIndex()) {
        return rowData.getStore() == null ? null : rowData.getStore().toFriendlyStr();
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      }
      return null;
    }
  }

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      RGridColumnDef colDef = cell.getColumnDef();
      int row = cell.getRow();

      BInternalFee bill = (BInternalFee) pagingGrid.getPageData().get(row);
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
      RMsgBox.showError(InternalFeeMessages.M.seleteDataToAction(InternalFeeMessages.M.delete(),
          PInternalFeeDef.TABLE_CAPTION));
      return;
    }

    batchProcessor.batchProcess(InternalFeeMessages.M.delete(), new BInternalFee[] {},
        new PagingGridBatchProcessCallback<BInternalFee>() {
          @Override
          public void execute(BInternalFee bill, BatchProcesser<BInternalFee> processer,
              AsyncCallback callback) {
            if (!BBizStates.INEFFECT.equals(bill.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            InternalFeeService.Locator.getService().delete(bill.getUuid(), bill.getVersion(),
                callback);
            BInternalFeeLogger.getInstance().log(InternalFeeMessages.M.delete(), bill);
          }
        });
  }

  private void doEffect() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(InternalFeeMessages.M.seleteDataToAction(InternalFeeMessages.M.effect(),
          PInternalFeeDef.TABLE_CAPTION));
      return;
    }

    batchProcessor.batchProcess(InternalFeeMessages.M.effect(), new BInternalFee[] {},
        new PagingGridBatchProcessCallback<BInternalFee>() {
          @Override
          public void execute(BInternalFee bill, BatchProcesser<BInternalFee> processer,
              AsyncCallback callback) {
            if (!BBizStates.INEFFECT.equals(bill.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            InternalFeeService.Locator.getService().effect(bill.getUuid(), null, bill.getVersion(),
                callback);
            BInternalFeeLogger.getInstance().log(InternalFeeMessages.M.effect(), bill);
          }
        });
  }

  private void doAbortConfirm() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(InternalFeeMessages.M.seleteDataToAction(InternalFeeMessages.M.abort(),
          PInternalFeeDef.TABLE_CAPTION));
      return;
    }

    InputBox.show(InternalFeeMessages.M.abortReason(), null, true, PInternalFeeDef.remark,
        new InputBox.Callback() {
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            doAbort(text);
          }
        });
  }

  private void doAbort(final String reason) {
    batchProcessor.batchProcess(InternalFeeMessages.M.abort(), new BInternalFee[] {}, true, false,
        new PagingGridBatchProcessCallback<BInternalFee>() {
          @Override
          public void execute(BInternalFee bill, BatchProcesser<BInternalFee> processer,
              AsyncCallback callback) {
            if (!BBizStates.EFFECT.equals(bill.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            InternalFeeService.Locator.getService().abort(bill.getUuid(), reason,
                bill.getVersion(), callback);
            BInternalFeeLogger.getInstance().log(InternalFeeMessages.M.abort(), bill);
          }
        });
  }

  private void doEditOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BInternalFee bill = (BInternalFee) pagingGrid.getPageData().get(hotItem.getGridRow());

    JumpParameters jumParams = new JumpParameters(InternalFeeEditPage.START_NODE);
    jumParams.getUrlRef().set(InternalFeeEditPage.PN_UUID, bill.getUuid());
    ep.jump(jumParams);
  }

  private void doDeleteOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BInternalFee bill = (BInternalFee) pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmDeleteOne(bill);
  }

  private void doConfirmDeleteOne(final BInternalFee bill) {
    getMessagePanel().clearMessages();

    String msg = InternalFeeMessages.M.actionComfirm2(InternalFeeMessages.M.delete(),
        PInternalFeeDef.TABLE_CAPTION, bill.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        deleteOne(bill);
      }
    });
  }

  private void deleteOne(final BInternalFee bill) {
    RLoadingDialog.show(InternalFeeMessages.M.beDoing(InternalFeeMessages.M.delete(),
        bill.getBillNumber()));
    InternalFeeService.Locator.getService().delete(bill.getUuid(), bill.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InternalFeeMessages.M.actionFailed2(InternalFeeMessages.M.delete(),
                PInternalFeeDef.TABLE_CAPTION, bill.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BInternalFeeLogger.getInstance().log(InternalFeeMessages.M.delete(), bill);

            String msg = InternalFeeMessages.M.onSuccess(InternalFeeMessages.M.delete(),
                PInternalFeeDef.TABLE_CAPTION, bill.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doEffectOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BInternalFee bill = (BInternalFee) pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmEffectOne(bill);
  }

  private void doConfirmEffectOne(final BInternalFee bill) {
    getMessagePanel().clearMessages();

    String msg = InternalFeeMessages.M.actionComfirm2(InternalFeeMessages.M.effect(),
        PInternalFeeDef.TABLE_CAPTION, bill.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        effectOne(bill);
      }
    });
  }

  private void effectOne(final BInternalFee bill) {
    RLoadingDialog.show(InternalFeeMessages.M.beDoing(InternalFeeMessages.M.effect(),
        bill.getBillNumber()));
    InternalFeeService.Locator.getService().effect(bill.getUuid(), null, bill.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InternalFeeMessages.M.actionFailed2(InternalFeeMessages.M.effect(),
                PInternalFeeDef.TABLE_CAPTION, bill.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BInternalFeeLogger.getInstance().log(InternalFeeMessages.M.effect(), bill);

            String msg = InternalFeeMessages.M.onSuccess(InternalFeeMessages.M.effect(),
                PInternalFeeDef.TABLE_CAPTION, bill.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doAbortOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BInternalFee bill = (BInternalFee) pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmAbortOne(bill);
  }

  private void doConfirmAbortOne(final BInternalFee bill) {
    getMessagePanel().clearMessages();

    InputBox.show(InternalFeeMessages.M.abortReason(), null, true, PInternalFeeDef.remark,
        new InputBox.Callback() {
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            abortOne(bill, text);
          }
        });
  }

  private void abortOne(final BInternalFee bill, String comment) {
    RLoadingDialog.show(InternalFeeMessages.M.beDoing(InternalFeeMessages.M.abort(),
        bill.getBillNumber()));
    InternalFeeService.Locator.getService().abort(bill.getUuid(), comment, bill.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InternalFeeMessages.M.actionFailed2(InternalFeeMessages.M.abort(),
                PInternalFeeDef.TABLE_CAPTION, bill.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BInternalFeeLogger.getInstance().log(InternalFeeMessages.M.abort(), bill);

            String msg = InternalFeeMessages.M.onSuccess(InternalFeeMessages.M.abort(),
                PInternalFeeDef.TABLE_CAPTION, bill.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
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
