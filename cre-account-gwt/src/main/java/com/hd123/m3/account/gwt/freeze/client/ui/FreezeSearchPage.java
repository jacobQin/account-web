/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	FreezeSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-3 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.client.ui;

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
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.freeze.client.EPFreeze;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreeze;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreezeLogger;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreezeState;
import com.hd123.m3.account.gwt.freeze.client.rpc.FreezeService;
import com.hd123.m3.account.gwt.freeze.intf.client.FreezeMessages;
import com.hd123.m3.account.gwt.freeze.intf.client.FreezeUrlParams;
import com.hd123.m3.account.gwt.freeze.intf.client.FreezeUrlParams.Search;
import com.hd123.m3.account.gwt.freeze.intf.client.dd.PFreezeDef;
import com.hd123.m3.account.gwt.freeze.intf.client.perm.FreezePermDef;
import com.hd123.m3.commons.gwt.base.client.biz.IsStandardBill;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
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
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class FreezeSearchPage extends BaseContentPage implements Search, BeforePrintHandler {

  public static EmbeddedFieldDef permGroup = new EmbeddedFieldDef(FIELD_PERMGROUP,
      FreezeMessages.M.permGroup());
  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(
      FreezeUrlParams.Flecs.FIELD_COUNTERPARTTYPE, EPFreeze.getInstance().getFieldCaption(
          GRes.FIELDNAME_COUNTERPART_TYPE, GRes.R.counterpartType()));

  public static FreezeSearchPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new FreezeSearchPage();
    return instance;
  }

  public FreezeSearchPage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
      afterDraw();
    } catch (Exception e) {
      throw new ClientBizException(FreezeMessages.M.cannotCreatePage("FreezeSearchPage"), e);
    }
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
  }

  public JumpParameters getLastJumpParams() {
    if (params != null)
      params.getMessages().clear();
    return params;
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);
    this.params = params;

    if (!checkIn())
      return;

    decodeParams(params);
    refreshTitle();
    refreshCommands();
    refreshFlecs();
  }

  private static FreezeSearchPage instance;
  private EPFreeze ep = EPFreeze.getInstance();
  private Handler_clickAction clickHandler = new Handler_clickAction();
  private PagingGridBatchProcessor<BFreeze> batchProcessor;

  private RAction createAction;
  private RAction unfreezeAction;
  protected PrintButton printButton = null;

  private RPopupMenu lineMenu;
  private RMenuItem unfreezeOneMenuItem;

  private RGrid grid;
  private RPagingGrid<BFreeze> pagingGrid;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef stateCol;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef freezePayTotalCol;
  private RGridColumnDef freezeRecTotalCol;
  private RGridColumnDef freezeDateCol;
  private RGridColumnDef unfreezeDateCol;
  private RGridColumnDef freezeReasonCol;
  private RGridColumnDef unfreezeReasonCol;

  private JumpParameters params;
  private String keyword;
  private RFlecsPanel flecsPanel;
  private FlecsConfigCodec flecsCodec;
  private PageSort pageSort;

  private static EmbeddedFieldDef counterpart = new EmbeddedFieldDef(FIELD_COUNTERPART, EPFreeze
      .getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
  private static StringFieldDef contractNum = new StringFieldDef(FIELD_CONTRACT_BILLNUMBER,
      FreezeMessages.M.contractNum(), true, 0, 32);

  private static EmbeddedFieldDef accountUnit = new EmbeddedFieldDef(FIELD_BUSINESSUNIT, EPFreeze
      .getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));

  private void drawToolbar() {
    createAction = new RAction(RActionFacade.CREATE, clickHandler);
    getToolbar().add(new RToolbarButton(createAction));

    getToolbar().addSeparator();

    unfreezeAction = new RAction(FreezeMessages.M.unfreeze(), clickHandler);
    getToolbar().add(new RToolbarButton(unfreezeAction));

    // 打印
    drawPrintButton();
  }

  // 构造打印按钮， 需要在构造flecs panel之前调用
  protected void drawPrintButton() {
    getToolbar().addToRight(new RToolbarSeparator());
    printButton = new PrintButton(ep.getPrintTemplate(), ep.getCurrentUser().getId());
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

  private void afterDraw() {
    batchProcessor = new PagingGridBatchProcessor<BFreeze>(pagingGrid, ep.getModuleCaption());
    flecsCodec = new FlecsConfigCodec();
    flecsCodec.addOperatorCodec(new DefaultOperatorCodec());
    flecsCodec.addOperandCodec(new BUCNCodec());
  }

  private Widget drawFlecsAndGrid() {
    drawLineMenu();
    drawGrid();

    pagingGrid = new RPagingGrid<BFreeze>(grid, new GridDataProvider());

    drawFlecsPanel();

    return flecsPanel;
  }

  private void drawLineMenu() {
    lineMenu = new RPopupMenu();
    lineMenu.addOpenHandler(new Handler_lineMenu());

    unfreezeOneMenuItem = new RMenuItem(FreezeMessages.M.unfreeze() + FreezeMessages.M.doing(),
        clickHandler);
    unfreezeOneMenuItem.setHotKey(null);
    lineMenu.addItem(unfreezeOneMenuItem);
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
        for (BFreeze o : pagingGrid.getSelections()) {
          if (StringUtil.isNullOrBlank(o.getStore().getUuid()) == false) {
            storeCodes.add(o.getStore().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    billNumberCol = new RGridColumnDef(PFreezeDef.billNumber);
    billNumberCol.setWidth("180px");
    billNumberCol.setName(FIELD_BILLNUMBER);
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setSortable(true);
    billNumberCol.setAllowHiding(false);
    grid.addColumnDef(billNumberCol);

    stateCol = new RGridColumnDef(PFreezeDef.state);
    stateCol.setWidth("100px");
    stateCol.setName(FIELD_STATE);
    stateCol.setSortable(true);
    grid.addColumnDef(stateCol);

    counterpartCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    counterpartCol.setWidth("120px");
    counterpartCol.setSortable(true);
    counterpartCol.setName(FIELD_COUNTERPART);
    grid.addColumnDef(counterpartCol);

    freezePayTotalCol = new RGridColumnDef(PFreezeDef.paymentTotal_total);
    freezePayTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    freezePayTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    freezePayTotalCol.setName(FIELD_CONTRACT_PAYMENTTOTAL);
    freezePayTotalCol.setSortable(true);
    freezePayTotalCol.setWidth("100px");
    grid.addColumnDef(freezePayTotalCol);

    freezeRecTotalCol = new RGridColumnDef(PFreezeDef.receiptTotal_total);
    freezeRecTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    freezeRecTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    freezeRecTotalCol.setName(FIELD_CONTRACT_RECEIPTTOTAL);
    freezeRecTotalCol.setSortable(true);
    freezeRecTotalCol.setWidth("100px");
    grid.addColumnDef(freezeRecTotalCol);

    freezeDateCol = new RGridColumnDef(PFreezeDef.freezeInfo_time);
    freezeDateCol.setName(FIELD_FREEZE_DATE);
    freezeDateCol.setSortable(true);
    freezeDateCol.setWidth("100px");
    grid.addColumnDef(freezeDateCol);

    unfreezeDateCol = new RGridColumnDef(PFreezeDef.unfreezeInfo_time);
    unfreezeDateCol.setName(FIELD_UNFREEZE_DATE);
    unfreezeDateCol.setSortable(true);
    unfreezeDateCol.setWidth("100px");
    grid.addColumnDef(unfreezeDateCol);

    freezeReasonCol = new RGridColumnDef(PFreezeDef.freezeReason);
    freezeReasonCol.setWidth("130px");
    grid.addColumnDef(freezeReasonCol);

    unfreezeReasonCol = new RGridColumnDef(PFreezeDef.unfreezeReason);
    grid.addColumnDef(unfreezeReasonCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  private void drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());
    flecsPanel.addField(PFreezeDef.billNumber);
    flecsPanel.addField(counterpartType);
    flecsPanel.addField(PFreezeDef.state);
    flecsPanel.addField(accountUnit);
    flecsPanel.addField(counterpart);
    flecsPanel.addField(contractNum);
    flecsPanel.addField(PFreezeDef.freezeInfo_time);
    flecsPanel.addField(PFreezeDef.unfreezeInfo_time);
    if (ep.isPermEnabled()) {
      flecsPanel.addField(permGroup);
    }
    flecsPanel.addField(PFreezeDef.createInfo_operator_id);
    flecsPanel.addField(PFreezeDef.createInfo_operator_fullName);
    flecsPanel.addField(PFreezeDef.createInfo_time);
    flecsPanel.addField(PFreezeDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PFreezeDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PFreezeDef.lastModifyInfo_time);
    flecsPanel.addBeforeLoadDataHandler(new BeforeLoadDataHandler<Integer>() {

      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent<Integer> event) {
        JumpParameters jumParams = new JumpParameters(START_NODE);
        FlecsConfig curConfig = flecsPanel.getCurrentConfig();
        curConfig.setShowConditions(flecsPanel.isShowConditions());
        jumParams.getUrlRef().set(PN_FLECSCONFIG, flecsCodec.encode(curConfig));
        jumParams.getUrlRef().set(PN_PAGE, String.valueOf(event.getParameter()));
        ep.jump(jumParams);
        event.cancel();
      }
    });

    getToolbar().add(flecsPanel.getMenuButton(), Alignment.right);
  }

  private boolean checkIn() {
    if (ep.isPermitted(FreezePermDef.READ) == false) {
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
    ep.getTitleBar().setTitleText(FreezeMessages.M.search());
    ep.getTitleBar().appendAttributeText(ep.getModuleCaption());
  }

  private void refreshCommands() {
    createAction.setEnabled(ep.isPermitted(FreezePermDef.CREATE));
    unfreezeAction.setEnabled(ep.isPermitted(FreezePermDef.UNFREEZE));
    printButton.setEnabled(ep.isPermitted(FreezePermDef.PRINT));
  }

  private void refreshFlecs() {
    grid.setSort(billNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel.addCondition(new Condition(PFreezeDef.billNumber, DefaultOperator.STARTS_WITH,
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
    conditions.add(new Condition(PFreezeDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PFreezeDef.state, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpart, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(contractNum, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PFreezeDef.freezeInfo_time, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PFreezeDef.unfreezeInfo_time, DefaultOperator.EQUALS, null));
    return conditions;
  }

  private class Handler_clickAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == createAction) {
        JumpParameters jumParams = new JumpParameters(FreezeCreatePage.START_NODE);
        ep.jump(jumParams);
      } else if (event.getSource() == unfreezeAction) {
        doUnfreezeConfirm();
      } else if (event.getSource() == unfreezeOneMenuItem) {
        doUnfreezeOne(event);
      }
    }
  }

  private void doUnfreezeConfirm() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(FreezeMessages.M.seleteDataToAction(FreezeMessages.M.unfreeze(),
          ep.getModuleCaption()));
      return;
    }

    InputBox.show(FreezeMessages.M.unfreezeReason(), null, true, PFreezeDef.unfreezeReason,
        new InputBox.Callback() {
          @Override
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            doUnfreeze(text);
          }
        });
  }

  private void doUnfreeze(final String text) {
    batchProcessor.batchProcess(FreezeMessages.M.unfreeze(), new BFreeze[] {}, true, false,
        new PagingGridBatchProcessCallback<BFreeze>() {
          @Override
          public void execute(BFreeze entity, BatchProcesser<BFreeze> processer,
              AsyncCallback callback) {
            if (BFreezeState.unfroze.name().equals(entity.getState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            FreezeService.Locator.getService().unfreeze(entity.getUuid(), entity.getVersion(),
                text, callback);
            BFreezeLogger.getInstance().log(FreezeMessages.M.unfreeze(), entity);
          }
        });
  }

  private void doUnfreezeOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BFreeze freeze = pagingGrid.getPageData().get(hotItem.getGridRow());
    doConfirmUnfreezeOne(freeze);
  }

  private void doConfirmUnfreezeOne(final BFreeze freeze) {
    getMessagePanel().clearMessages();

    InputBox.show(FreezeMessages.M.unfreezeReason(), null, true, PFreezeDef.unfreezeReason,
        new InputBox.Callback() {
          @Override
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            unfreezeOne(freeze, text);
          }
        });
  }

  private void unfreezeOne(final BFreeze freeze, String text) {
    RLoadingDialog.show(FreezeMessages.M.actionDoing(FreezeMessages.M.unfreeze()));
    FreezeService.Locator.getService().unfreeze(freeze.getUuid(), freeze.getVersion(), text,
        new RBAsyncCallback2<Void>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = FreezeMessages.M.actionFailed2(FreezeMessages.M.unfreeze(),
                ep.getModuleCaption(), freeze.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BFreezeLogger.getInstance().log(FreezeMessages.M.unfreeze(), freeze);

            String msg = FreezeMessages.M.onSuccess(FreezeMessages.M.unfreeze(),
                ep.getModuleCaption(), freeze.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private class GridDataProvider implements RPageDataProvider<BFreeze> {
    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BFreeze>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);
      if (sortField != null)
        pageSort.appendOrder(sortField, sortDir);
      else
        pageSort.appendOrder(FIELD_BILLNUMBER, OrderDir.desc);

      FreezeService.Locator.getService().query(flecsPanel.getCurrentConfig().getConditions(),
          pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BFreeze rowData, List<BFreeze> pageData) {
      if (col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      else if (col == stateCol.getIndex())
        return rowData.getState() != null ? BFreezeState.valueOf(rowData.getState()).getCaption()
            : null;
      else if (col == counterpartCol.getIndex())
        return rowData.getCounterpart() != null ? rowData.getCounterpart().toFriendlyStr(
            ep.getCounterpartTypeMap()) : null;
      else if (col == freezePayTotalCol.getIndex())
        return rowData.getFreezePayTotal() != null ? rowData.getFreezePayTotal().doubleValue()
            : BigDecimal.ZERO.doubleValue();
      else if (col == freezeRecTotalCol.getIndex())
        return rowData.getFreezeRecTotal() != null ? rowData.getFreezeRecTotal().doubleValue()
            : BigDecimal.ZERO.doubleValue();
      else if (col == freezeDateCol.getIndex())
        return rowData.getFreezeInfo() != null ? rowData.getFreezeInfo().getTime() != null ? M3Format.fmt_yMd
            .format(rowData.getFreezeInfo().getTime()) : null
            : null;
      else if (col == unfreezeDateCol.getIndex())
        return rowData.getUnfreezeInfo() != null ? rowData.getUnfreezeInfo().getTime() != null ? M3Format.fmt_yMd
            .format(rowData.getUnfreezeInfo().getTime()) : null
            : null;
      else if (col == freezeReasonCol.getIndex())
        return rowData.getFreezeReason();
      else if (col == unfreezeReasonCol.getIndex())
        return rowData.getUnfreezeReason();
      return null;
    }
  }

  private class Handler_lineMenu implements OpenHandler<RPopupMenu> {

    @Override
    public void onOpen(OpenEvent<RPopupMenu> event) {
      RHotItemRenderer hotItem = (RHotItemRenderer) event.getTarget().getContextWidget();
      BFreeze bFreeze = pagingGrid.getPageData().get(hotItem.getGridRow());
      refreshLineMenu(bFreeze);
    }
  }

  private void refreshLineMenu(BFreeze bFreeze) {
    unfreezeOneMenuItem.setEnabled(ep.isPermitted(FreezePermDef.UNFREEZE));

    boolean froze = BFreezeState.froze.name().equals(bFreeze.getState());

    unfreezeOneMenuItem.setVisible(froze);
  }

  private class Handler_grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(billNumberCol)) {
        BFreeze rowData = pagingGrid.getRowData(cell.getRow());
        if (rowData == null)
          return;
        ep.jumpToViewPage(rowData.getUuid());
      }
    }
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {

    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (counterpart == field && DefaultOperator.EQUALS == operator) {
        return buildCounterpart();
      } else if (accountUnit == field && DefaultOperator.EQUALS == operator) {
        return new AccountUnitUCNBox(null, true);
      } else if (permGroup == field) {
        return new PermGroupField(permGroup);
      } else if (counterpartType == field) {
        return new CounterpartTypeField(counterpartType);
      } else {
        return super.createOperandWidget(field, operator);
      }
    }
  }

  private class MyOperatorPolicy implements OperatorPolicy {

    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      result.add(DefaultOperator.EQUALS);
      result.add(DefaultOperator.NOT_EQUALS);
      result.add(DefaultOperator.STARTS_WITH);
      result.add(DefaultOperator.ENDS_WITH);
      result.add(DefaultOperator.CONTAINS);
      result.add(DefaultOperator.NOT_CONTAINS);

      if (counterpart == field || accountUnit == field || counterpartType == field) {
        result.clear();
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PFreezeDef.state == field || permGroup == field) {
        result.clear();
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PFreezeDef.freezeInfo_time == field || PFreezeDef.unfreezeInfo_time == field
          || PFreezeDef.createInfo_time == field || PFreezeDef.lastModifyInfo_time == field) {
        defaultOperators.remove(DefaultOperator.STARTS_WITH);
        defaultOperators.remove(DefaultOperator.ENDS_WITH);
        defaultOperators.remove(DefaultOperator.CONTAINS);
        defaultOperators.remove(DefaultOperator.NOT_CONTAINS);
        defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
        defaultOperators.remove(DefaultOperator.IS_NULL);
        return defaultOperators;
      } else {
        return result;
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
      setNullOptionText(FreezeMessages.M.all());
      setEditable(false);
      for (Entry<String, String> entry : ep.getCounterpartTypeMap().entrySet()) {
        this.addOption(entry.getKey(), entry.getValue());
      }
    }
  }

  @Override
  public void beforePrint(PrintingTemplate template, String action,
      List<Map<String, String>> parameters, BeforePrintCallback callback) {
    if (template == null || template.getTemplate() == null) {
      return;
    }
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    for (Object o : pagingGrid.getSelections()) {
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
