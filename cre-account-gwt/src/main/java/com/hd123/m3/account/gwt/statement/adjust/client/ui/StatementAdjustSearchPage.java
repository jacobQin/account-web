/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementAdjustSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.ui;

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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.statement.adjust.client.EPStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLogger;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustService;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustMessages;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustUrlParams;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustUrlParams.Search;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.dd.PStatementAdjustDef;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.perm.StatementAdjustPermDef;
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
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarMenuButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class StatementAdjustSearchPage extends BaseBpmSearchPage implements Search,
    BeforePrintHandler {

  public static EmbeddedFieldDef counterpart = new EmbeddedFieldDef(FIELD_COUNTERPART,
      EPStatementAdjust.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
          GRes.R.counterpart()));
  public static EmbeddedFieldDef businessUnit = new EmbeddedFieldDef(FIELD_ACCOUNTUNIT,
      EPStatementAdjust.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
  public static EmbeddedFieldDef subject = new EmbeddedFieldDef("subject", WidgetRes.R.subject());
  public static EmbeddedFieldDef permGroup = new EmbeddedFieldDef(FIELD_PERMGROUP,
      StatementAdjustMessages.M.permGroup());

  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(
      StatementAdjustUrlParams.Flecs.FIELD_COUNTERPARTTYPE, EPStatementAdjust.getInstance()
          .getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE, GRes.R.counterpartType()));

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes R = GWT.create(WidgetRes.class);

    @DefaultStringValue("对账组")
    String accountGroup();

    @DefaultStringValue("科目")
    String subject();
  }

  public static StatementAdjustSearchPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new StatementAdjustSearchPage();
    return instance;
  }

  public StatementAdjustSearchPage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
      afterDraw();
    } catch (Exception e) {
      throw new ClientBizException(
          StatementAdjustMessages.M.cannotCreatePage("StatementAdjustSearchPage"), e);
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
    refreshCommonds();
  }

  private void refreshCommonds() {
    deleteAction.setVisible(getEP().isPermitted(StatementAdjustPermDef.DELETE));
    abortAction.setVisible(getEP().isPermitted(StatementAdjustPermDef.ABORT));
    effectAction.setVisible(getEP().isPermitted(StatementAdjustPermDef.EFFECT));
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

  public JumpParameters getLastJumpParams() {
    if (params != null)
      params.getMessages().clear();
    return params;
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
  }

  private static StatementAdjustSearchPage instance;

  private RToolbarMenuButton moreButton;
  private RAction deleteAction;
  private RAction abortAction;
  private RAction effectAction;
  protected PrintButton printButton = null;

  private RGrid grid;
  private RPagingGrid<BStatementAdjust> pagingGrid;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef counterpartUnitCol;
  private RGridColumnDef contractBillNumCol;
  private RGridColumnDef contractNameCol;
  private RGridColumnDef statementBillNumCol;
  private RGridColumnDef settleNoCol;
  private RGridColumnDef receiptTotalCol;
  private RGridColumnDef payTotalCol;
  private RGridColumnDef remarkCol;

  private RMenuItem editOneMenuItem;
  private RMenuItem deleteOneMenuItem;
  private RMenuItem effectOneMenuItem;
  private RMenuItem abortOneMenuItem;

  private JumpParameters params;
  private String keyword;
  private RFlecsPanel flecsPanel;
  private FlecsConfigCodec flecsCodec;
  private PageSort pageSort;

  private PagingGridBatchProcessor<BStatementAdjust> batchProcessor;

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());
    getToolbar().addSeparator();

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deletection());
    effectAction = new RAction(StatementAdjustMessages.M.effect(), new Handler_effectAction());
    abortAction = new RAction(StatementAdjustMessages.M.abort() + "...", new Handler_abortAction());

    moreButton = new RToolbarMenuButton(StatementAdjustMessages.M.operate(), moreMenu);
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
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    initWidget(root);

    root.add(drawFlecsAndGrid());
  }

  private void afterDraw() {
    batchProcessor = new PagingGridBatchProcessor<BStatementAdjust>(pagingGrid, getEP()
        .getModuleCaption());
    flecsCodec = new FlecsConfigCodec();
    flecsCodec.addOperatorCodec(new DefaultOperatorCodec());
    flecsCodec.addOperandCodec(new BUCNCodec());
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
    editOneMenuItem = new RMenuItem(RActionFacade.EDIT, new Handler_editOneMenuItem());
    editOneMenuItem.setHotKey(null);

    deleteOneMenuItem = new RMenuItem(RActionFacade.DELETE, new Handler_deleteOneMenuItem());
    deleteOneMenuItem.setHotKey(null);

    effectOneMenuItem = new RMenuItem(StatementAdjustMessages.M.effect(),
        new Handler_effectOneMenuItem());
    effectOneMenuItem.setHotKey(null);

    abortOneMenuItem = new RMenuItem(StatementAdjustMessages.M.abort()
        + StatementAdjustMessages.M.doing(), new Handler_abortOneMenuItem());
    abortOneMenuItem.setHotKey(null);
  }

  private void drawGrid() {
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
        for (BStatementAdjust o : pagingGrid.getSelections()) {
          if (StringUtil.isNullOrBlank(o.getStore().getUuid()) == false) {
            storeCodes.add(o.getStore().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    billNumberCol = new RGridColumnDef(PStatementAdjustDef.billNumber);
    billNumberCol.setWidth("180px");
    billNumberCol.setName(FIELD_BILLNUMBER);
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setSortable(true);
    billNumberCol.setAllowHiding(false);
    grid.addColumnDef(billNumberCol);

    bizStateCol = new RGridColumnDef(PStatementAdjustDef.bizState);
    bizStateCol.setWidth("70px");
    bizStateCol.setName(FIELD_BIZSTATE);
    bizStateCol.setSortable(true);
    grid.addColumnDef(bizStateCol);

    counterpartUnitCol = new RGridColumnDef(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    counterpartUnitCol.setWidth("160px");
    counterpartUnitCol.setName(FIELD_COUNTERPART);
    counterpartUnitCol.setSortable(true);
    grid.addColumnDef(counterpartUnitCol);

    contractBillNumCol = new RGridColumnDef(PStatementAdjustDef.contract_code);
    contractBillNumCol.setWidth("150px");
    contractBillNumCol.setName(FIELD_CONTRACTBILLNUMBER);
    contractBillNumCol.setSortable(true);
    grid.addColumnDef(contractBillNumCol);

    contractNameCol = new RGridColumnDef(PStatementAdjustDef.contract_name);
    contractNameCol.setWidth("150px");
    contractNameCol.setName(FIELD_CONTRACTNAME);
    contractNameCol.setSortable(true);
    grid.addColumnDef(contractNameCol);

    statementBillNumCol = new RGridColumnDef(PStatementAdjustDef.statement_billNumber);
    statementBillNumCol.setWidth("150px");
    statementBillNumCol.setName(FIELD_STATEMENTBILLNUMBER);
    statementBillNumCol.setSortable(true);
    grid.addColumnDef(statementBillNumCol);

    settleNoCol = new RGridColumnDef(PStatementAdjustDef.settleNo);
    settleNoCol.setWidth("80px");
    settleNoCol.setName(FIELD_SETTLENO);
    settleNoCol.setSortable(true);
    grid.addColumnDef(settleNoCol);

    receiptTotalCol = new RGridColumnDef(PStatementAdjustDef.receiptTotal_total);
    receiptTotalCol.setCaption(StatementAdjustMessages.M.receiptTotal_total());
    receiptTotalCol.setWidth("100px");
    receiptTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    receiptTotalCol.setSortable(false);
    grid.addColumnDef(receiptTotalCol);

    payTotalCol = new RGridColumnDef(PStatementAdjustDef.paymentTotal_total);
    payTotalCol.setCaption(StatementAdjustMessages.M.paymentTotal_total());
    payTotalCol.setWidth("100px");
    payTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    payTotalCol.setSortable(false);
    grid.addColumnDef(payTotalCol);

    remarkCol = new RGridColumnDef(PStatementAdjustDef.remark);
    remarkCol.setSortable(false);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  private void drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());
    flecsPanel.addField(PStatementAdjustDef.billNumber);
    flecsPanel.addField(PStatementAdjustDef.bizState);
    flecsPanel.addField(PStatementAdjustDef.settleNo);
    flecsPanel.addField(businessUnit);
    flecsPanel.addField(counterpartType);
    flecsPanel.addField(counterpart);
    flecsPanel.addField(PStatementAdjustDef.contract_code);
    flecsPanel.addField(PStatementAdjustDef.contract_name);
    flecsPanel.addField(PStatementAdjustDef.statement_billNumber);
    flecsPanel.addField(subject);
    if (getEP().isPermEnabled()) {
      flecsPanel.addField(permGroup);
    }
    flecsPanel.addField(PStatementAdjustDef.createInfo_operator_id);
    flecsPanel.addField(PStatementAdjustDef.createInfo_operator_fullName);
    flecsPanel.addField(PStatementAdjustDef.createInfo_time);
    flecsPanel.addField(PStatementAdjustDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PStatementAdjustDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PStatementAdjustDef.lastModifyInfo_time);
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

  private boolean checkIn() {
    if (getEP().isPermitted(StatementAdjustPermDef.READ) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private List<Condition> getDefaultConditions() {
    List<Condition> conditions = new ArrayList<Condition>();
    conditions.add(new Condition(PStatementAdjustDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PStatementAdjustDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PStatementAdjustDef.settleNo, DefaultOperator.EQUALS,
        SettleNoField.getCurrentSettleNo()));
    conditions.add(new Condition(businessUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpart, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PStatementAdjustDef.contract_code, DefaultOperator.EQUALS, null));
    conditions
        .add(new Condition(PStatementAdjustDef.contract_name, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PStatementAdjustDef.statement_billNumber, DefaultOperator.EQUALS,
        null));
    conditions.add(new Condition(subject, DefaultOperator.CONTAINS, null));
    return conditions;
  }

  private void decodeParams(JumpParameters params) {
    keyword = params.getUrlRef().get(PN_KEYWORD);
  }

  private void refreshTitle() {
    getEP().getTitleBar().clearStandardTitle();
    getEP().getTitleBar().setTitleText(StatementAdjustMessages.M.search());
    getEP().getTitleBar().appendAttributeText(PStatementAdjustDef.TABLE_CAPTION);
  }

  private void refreshFlecs() {
    grid.setSort(billNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel.addCondition(new Condition(PStatementAdjustDef.billNumber,
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

  private class Handler_effectAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (pagingGrid.getSelections().isEmpty()) {
        RMsgBox.showError(StatementAdjustMessages.M.seleteDataToAction(
            StatementAdjustMessages.M.effect(), getEP().getModuleCaption()));
        return;
      }

      batchProcessor.batchProcess(StatementAdjustMessages.M.effect(), new BStatementAdjust[] {},
          new PagingGridBatchProcessCallback<BStatementAdjust>() {
            @Override
            public void execute(BStatementAdjust bill, BatchProcesser<BStatementAdjust> processer,
                AsyncCallback callback) {
              if (!BBizStates.INEFFECT.equals(bill.getBizState())) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              StatementAdjustService.Locator.getService().effect(bill.getUuid(), null,
                  bill.getVersion(), callback);
              BStatementAdjustLogger.getInstance().log(StatementAdjustMessages.M.effect(), bill);
            }
          });
    }
  }

  private class Handler_deletection implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (pagingGrid.getSelections().isEmpty()) {
        RMsgBox.showError(StatementAdjustMessages.M.seleteDataToAction(
            StatementAdjustMessages.M.delete(), getEP().getModuleCaption()));
        return;
      }

      batchProcessor.batchProcess(StatementAdjustMessages.M.delete(), new BStatementAdjust[] {},
          new PagingGridBatchProcessCallback<BStatementAdjust>() {

            @Override
            public void execute(BStatementAdjust entity,
                BatchProcesser<BStatementAdjust> processer, AsyncCallback callback) {
              if (!BBizStates.INEFFECT.equals(entity.getBizState())) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              StatementAdjustService.Locator.getService().delete(entity.getUuid(),
                  entity.getVersion(), callback);
              BStatementAdjustLogger.getInstance().log(StatementAdjustMessages.M.delete(), entity);
            }
          });
    }
  }

  private class Handler_abortAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (pagingGrid.getSelections().isEmpty()) {
        RMsgBox.showError(StatementAdjustMessages.M.seleteDataToAction(
            StatementAdjustMessages.M.abort(), getEP().getModuleCaption()));
        return;
      }
      getMessagePanel().clearMessages();
      InputBox.show(StatementAdjustMessages.M.abortReason(), null, true,
          PStatementAdjustDef.remark, new InputBox.Callback() {

            @Override
            public void onClosed(boolean ok, String text) {
              if (ok == false)
                return;
              batchAbort(ok, text);
            }
          });
    }
  }

  private void batchAbort(final boolean ok, final String text) {
    batchProcessor.batchProcess(StatementAdjustMessages.M.abort(), new BStatementAdjust[] {}, true,
        false, new PagingGridBatchProcessCallback<BStatementAdjust>() {

          @Override
          public void execute(BStatementAdjust entity, BatchProcesser<BStatementAdjust> processer,
              AsyncCallback callback) {
            if (!BBizStates.EFFECT.equals(entity.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            StatementAdjustService.Locator.getService().abort(entity.getUuid(),
                entity.getVersion(), ok, text, callback);
            BStatementAdjustLogger.getInstance().log(StatementAdjustMessages.M.abort(), entity);
          }
        });
  }

  private class GridDataProvider implements RPageDataProvider<BStatementAdjust> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BStatementAdjust>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);
      if (sortField != null)
        pageSort.appendOrder(sortField, sortDir);
      else
        pageSort.appendOrder(FIELD_BILLNUMBER, OrderDir.desc);

      StatementAdjustService.Locator.getService().query(
          flecsPanel.getCurrentConfig().getConditions(),
          flecsPanel.getCurrentConfig().getVisibleColumnNames(), pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BStatementAdjust rowData,
        List<BStatementAdjust> pageData) {
      if (col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      else if (col == bizStateCol.getIndex())
        return PStatementAdjustDef.bizState.getEnumCaption(rowData.getBizState());
      else if (col == counterpartUnitCol.getIndex())
        return rowData.getCounterpart().toFriendlyStr(getEP().getCounterpartTypeMap());
      else if (col == contractBillNumCol.getIndex())
        return rowData.getBcontract().getBillNumber();
      else if (col == contractNameCol.getIndex())
        return rowData.getBcontract().getName();
      else if (col == statementBillNumCol.getIndex())
        return rowData.getStatement().getBillNumber();
      else if (col == settleNoCol.getIndex())
        return rowData.getSettleNo();
      else if (col == receiptTotalCol.getIndex())
        return rowData.getReceiptTotal() == null ? BigDecimal.ZERO : M3Format.fmt_money
            .format(rowData.getReceiptTotal().doubleValue());
      else if (col == payTotalCol.getIndex())
        return rowData.getPaymentTotal() == null ? BigDecimal.ZERO : M3Format.fmt_money
            .format(rowData.getPaymentTotal().doubleValue());
      else if (col == remarkCol.getIndex())
        return rowData.getRemark();
      return null;
    }
  }

  private class Handler_editOneMenuItem implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BStatementAdjust entity = pagingGrid.getPageData().get(hotItem.getGridRow());

      JumpParameters jumParams = new JumpParameters(StatementAdjustEditPage.START_NODE);
      jumParams.getUrlRef().set(StatementAdjustEditPage.PN_ENTITY_UUID, entity.getUuid());
      jumParams.getUrlRef().set(StatementAdjustUrlParams.Base.SCENCE,
          StatementAdjustMessages.M.editScence());
      getEP().jump(jumParams);
    }
  }

  private class Handler_effectOneMenuItem implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BStatementAdjust entity = pagingGrid.getPageData().get(hotItem.getGridRow());
      doConfirmeffectOne(entity);
    }
  }

  private void doConfirmeffectOne(final BStatementAdjust entity) {
    getMessagePanel().clearMessages();

    String msg = StatementAdjustMessages.M.actionComfirm2(StatementAdjustMessages.M.effect(),
        PStatementAdjustDef.TABLE_CAPTION, entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEffectOne(entity);
      }
    });
  }

  private void doEffectOne(final BStatementAdjust entity) {
    RLoadingDialog.show(StatementAdjustMessages.M.actionDoing(StatementAdjustMessages.M.effect()));
    StatementAdjustService.Locator.getService().effect(entity.getUuid(), null, entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementAdjustMessages.M.actionFailed(StatementAdjustMessages.M.effect(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BStatementAdjustLogger.getInstance().log(StatementAdjustMessages.M.effect(), entity);

            String msg = StatementAdjustMessages.M.onSuccess(StatementAdjustMessages.M.effect(),
                getEP().getModuleCaption(), entity.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private class Handler_deleteOneMenuItem implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BStatementAdjust entity = pagingGrid.getPageData().get(hotItem.getGridRow());
      doConfirmDeleteOne(entity);
    }
  }

  private void doConfirmDeleteOne(final BStatementAdjust entity) {
    String msg = StatementAdjustMessages.M.actionComfirm2(StatementAdjustMessages.M.delete(),
        getEP().getModuleCaption(), entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doDeleteOne(entity);
      }
    });
  }

  private void doDeleteOne(final BStatementAdjust entity) {
    RLoadingDialog.show(StatementAdjustMessages.M.actionDoing(StatementAdjustMessages.M.delete()));
    StatementAdjustService.Locator.getService().delete(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementAdjustMessages.M.actionFailed(StatementAdjustMessages.M.delete(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BStatementAdjustLogger.getInstance().log(StatementAdjustMessages.M.delete(), entity);

            getMessagePanel().clearMessages();
            getMessagePanel().putInfoMessage(
                StatementAdjustMessages.M.onSuccess(StatementAdjustMessages.M.delete(), getEP()
                    .getModuleCaption(), ""));
            pagingGrid.refresh();
          }
        });
  }

  private class Handler_abortOneMenuItem implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMenuItem menuItem = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      BStatementAdjust entity = pagingGrid.getPageData().get(hotItem.getGridRow());

      doConfirmAbort(entity);
    }
  }

  private void doConfirmAbort(final BStatementAdjust entity) {
    getMessagePanel().clearMessages();
    InputBox.show(StatementAdjustMessages.M.abortReason(), null, true, PStatementAdjustDef.remark,
        new InputBox.Callback() {
          @Override
          public void onClosed(boolean ok, String text) {
            if (!ok)
              return;
            doAbortOne(entity, ok, text);
          }
        });
  }

  private void doAbortOne(final BStatementAdjust entity, boolean ok, String text) {
    RLoadingDialog.show(StatementAdjustMessages.M.actionDoing(StatementAdjustMessages.M.abort()));
    StatementAdjustService.Locator.getService().abort(entity.getUuid(), entity.getVersion(), ok,
        text, new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementAdjustMessages.M.actionFailed(StatementAdjustMessages.M.abort(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BStatementAdjustLogger.getInstance().log(StatementAdjustMessages.M.abort(), entity);

            String msg = StatementAdjustMessages.M.onSuccess(StatementAdjustMessages.M.abort(),
                getEP().getModuleCaption(), entity.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private class Handler_grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(billNumberCol)) {
        BStatementAdjust entity = pagingGrid.getRowData(cell.getRow());
        if (entity == null)
          return;
        getEP().jumpToViewPage(entity.getUuid());
      }
    }
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {

    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (field == PStatementAdjustDef.settleNo) {
        SettleNoField settleNoField = new SettleNoField(PStatementAdjustDef.settleNo);
        settleNoField.setRequired(false);
        settleNoField.refreshOption(12);
        settleNoField.setMaxLength(6);
        return settleNoField;
      } else if (field == counterpart)
        return DefaultOperator.EQUALS == operator ? buildCounterpart() : null;
      else if (field == subject)
        return new SubjectUCNBox(BSubjectType.credit.name());
      else if (field == businessUnit)
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      else if (permGroup == field) {
        return new PermGroupField(permGroup);
      } else if (counterpartType == field) {
        return new CounterpartTypeField(counterpartType);
      } else
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
      setNullOptionText(StatementAdjustMessages.M.all());
      setEditable(false);
      for (Entry<String, String> entry : getEP().getCounterpartTypeMap().entrySet()) {
        this.addOption(entry.getKey(), entry.getValue());
      }
    }
  }

  private class MyOperatorPolicy implements OperatorPolicy {

    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      result.add(DefaultOperator.EQUALS);
      if (PStatementAdjustDef.billNumber == field || PStatementAdjustDef.contract_code == field
          || PStatementAdjustDef.statement_billNumber == field
          || PStatementAdjustDef.createInfo_operator_id == field
          || PStatementAdjustDef.createInfo_operator_fullName == field
          || PStatementAdjustDef.lastModifyInfo_operator_id == field
          || PStatementAdjustDef.lastModifyInfo_operator_fullName == field
          || PStatementAdjustDef.contract_name == field) {
        result.add(DefaultOperator.NOT_EQUALS);
        result.add(DefaultOperator.STARTS_WITH);
        result.add(DefaultOperator.ENDS_WITH);
        result.add(DefaultOperator.CONTAINS);
        result.add(DefaultOperator.NOT_CONTAINS);
      } else if (PStatementAdjustDef.createInfo_time == field
          || PStatementAdjustDef.lastModifyInfo_time == field) {
        result.add(DefaultOperator.NOT_EQUALS);
        result.add(DefaultOperator.GREATER_THAN);
        result.add(DefaultOperator.GREATER_THAN_OR_EQUAL_TO);
        result.add(DefaultOperator.LESS_THAN);
        result.add(DefaultOperator.LESS_THAN_OR_EQUAL_TO);
        result.add(DefaultOperator.BETWEEN);
      } else if (PStatementAdjustDef.bizState == field) {
        result.add(DefaultOperator.NOT_EQUALS);
      } else if (subject == field) {
        result.clear();
        result.add(DefaultOperator.CONTAINS);
      }
      return result;
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

  @Override
  protected RPagingGrid getPagingGrid() {
    return pagingGrid;
  }

  @Override
  protected EPStatementAdjust getEP() {
    return EPStatementAdjust.getInstance();
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
  public void beforeShow(JumpParameters params) {
    super.beforeShow(params);
    getEP().appendSearchBox();
  }

  @Override
  protected void appendLineMenu(RPopupMenu menu, Object bill) {
    BStatementAdjust adjust = (BStatementAdjust) bill;
    menu.addSeparator();
    menu.addItem(editOneMenuItem);
    menu.addItem(deleteOneMenuItem);
    menu.addSeparator();
    menu.addItem(effectOneMenuItem);
    menu.addItem(abortOneMenuItem);

    boolean isInEffect = BBizStates.INEFFECT.equals(adjust.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(adjust.getBizState());

    editOneMenuItem.setVisible(isInEffect && getEP().isPermitted(StatementAdjustPermDef.UPDATE));
    deleteOneMenuItem.setVisible(isInEffect && getEP().isPermitted(StatementAdjustPermDef.DELETE));
    effectOneMenuItem.setVisible(isInEffect && getEP().isPermitted(StatementAdjustPermDef.EFFECT));
    abortOneMenuItem.setVisible(isEffect && getEP().isPermitted(StatementAdjustPermDef.ABORT));
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
