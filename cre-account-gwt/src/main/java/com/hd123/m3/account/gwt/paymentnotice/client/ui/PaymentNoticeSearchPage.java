/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-11 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SettleNoField;
import com.hd123.m3.account.gwt.paymentnotice.client.EPPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.PaymentNoticeMessages;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNoticeLogger;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeService;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.PaymentNoticeUrlParams;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.PaymentNoticeUrlParams.Search;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.dd.PPaymentNoticeDef;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.dd.PPaymentNoticeLineDef;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.perm.PaymentNoticePermDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.biz.IsStandardBill;
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
public class PaymentNoticeSearchPage extends BaseBpmSearchPage implements Search,
    BeforePrintHandler {

  public static PaymentNoticeSearchPage getInstance() {
    if (instance == null)
      instance = new PaymentNoticeSearchPage();
    return instance;
  }

  public PaymentNoticeSearchPage() {
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
    // do nothing
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
    deleteAction.setVisible(getEP().isPermitted(PaymentNoticePermDef.DELETE));
    effectAction.setVisible(getEP().isPermitted(PaymentNoticePermDef.EFFECT));
    abortAction.setVisible(getEP().isPermitted(PaymentNoticePermDef.ABORT));
    batchAction.setVisible(getEP().isPermitted(PaymentNoticePermDef.CREATE));
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
          || batchAction.isVisible();
    }
    moreButton.setVisible(visible);
    getToolbar().rebuild();
  }

  public JumpParameters getLastParams() {
    if (params != null)
      params.getMessages().clear();
    return params;
  }

  private static EmbeddedFieldDef counterpart = new EmbeddedFieldDef(FIELD_COUNTERPART,
      EPPaymentNotice.getInstance().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
          GRes.R.counterpart()));
  private static EmbeddedFieldDef businessUnit = new EmbeddedFieldDef(FIELD_BUSINESSUNIT,
      EPPaymentNotice.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
  private static EmbeddedFieldDef permGroup = new EmbeddedFieldDef(FIELD_PERMGROUP,
      PaymentNoticeMessages.M.permGroup());

  public static EmbeddedFieldDef counterpartType = new EmbeddedFieldDef(
      PaymentNoticeUrlParams.Flecs.FIELD_COUNTERPARTTYPE, EPPaymentNotice.getInstance()
          .getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE, GRes.R.counterpartType()));

  private static PaymentNoticeSearchPage instance;
  private Handler_clickAction clickHandler = new Handler_clickAction();
  private PagingGridBatchProcessor<BPaymentNotice> batchProcessor;

  private RToolbarMenuButton moreButton;
  private RAction effectAction;
  private RAction abortAction;
  private RAction deleteAction;
  private RAction batchAction;
  protected PrintButton printButton = null;

  private RMenuItem editOneMenuItem;
  private RMenuItem deleteOneMenuItem;
  private RMenuItem effectOneMenuItem;
  private RMenuItem abortOneMenuItem;

  private RGrid grid;
  private RPagingGrid<BPaymentNotice> pagingGrid;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef settleNoCol;
  private RGridColumnDef receiptTotalCol;
  private RGridColumnDef receiptIvcTotalCol;
  private RGridColumnDef paymentTotalCol;
  private RGridColumnDef paymentIvcTotalCol;
  private RGridColumnDef remarkCol;

  private JumpParameters params;
  private String keyword;
  private RFlecsPanel flecsPanel;
  private FlecsConfigCodec flecsCodec;
  private PageSort pageSort;

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());
    getToolbar().addSeparator();

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    effectAction = new RAction(PaymentNoticeMessages.M.effect(), clickHandler);
    abortAction = new RAction(PaymentNoticeMessages.M.abort() + PaymentNoticeMessages.M.doing(),
        clickHandler);
    batchAction = new RAction(PaymentNoticeMessages.M.batch_caption(getEP().getModuleCaption()),
        clickHandler);

    moreButton = new RToolbarMenuButton(PaymentNoticeMessages.M.operate(), moreMenu);
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

  private void afterDraw() {
    batchProcessor = new PagingGridBatchProcessor<BPaymentNotice>(pagingGrid, getEP()
        .getModuleCaption());
    flecsCodec = new FlecsConfigCodec();
    flecsCodec.addOperatorCodec(new DefaultOperatorCodec());
    flecsCodec.addOperandCodec(new BUCNCodec());
  }

  private Widget drawFlecsAndGrid() {

    drawLineMenu();
    drawGrid();

    pagingGrid = new RPagingGrid<BPaymentNotice>(grid, new GridDataProvider());
    pagingGrid.addLoadDataHandler(this);
    drawFlecsPanel();

    return flecsPanel;
  }

  private void drawLineMenu() {
    editOneMenuItem = new RMenuItem(RActionFacade.EDIT, clickHandler);
    editOneMenuItem.setHotKey(null);

    deleteOneMenuItem = new RMenuItem(RActionFacade.DELETE, clickHandler);
    deleteOneMenuItem.setHotKey(null);

    effectOneMenuItem = new RMenuItem(PaymentNoticeMessages.M.effect(), clickHandler);
    effectOneMenuItem.setHotKey(null);

    abortOneMenuItem = new RMenuItem(PaymentNoticeMessages.M.abort()
        + PaymentNoticeMessages.M.doing(), clickHandler);
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
        for (BPaymentNotice o : pagingGrid.getSelections()) {
          if (StringUtil.isNullOrBlank(o.getStore().getUuid()) == false) {
            storeCodes.add(o.getStore().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    billNumberCol = new RGridColumnDef(PPaymentNoticeDef.billNumber);
    billNumberCol.setWidth("180px");
    billNumberCol.setName(FIELD_BILLNUMBER);
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setSortable(true);
    billNumberCol.setAllowHiding(false);
    grid.addColumnDef(billNumberCol);

    bizStateCol = new RGridColumnDef(PPaymentNoticeDef.bizState);
    bizStateCol.setWidth("100px");
    bizStateCol.setName(FIELD_BIZSTATE);
    bizStateCol.setSortable(true);
    grid.addColumnDef(bizStateCol);

    counterpartCol = new RGridColumnDef(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    counterpartCol.setWidth("160px");
    counterpartCol.setSortable(true);
    counterpartCol.setName(FIELD_COUNTERPART);
    grid.addColumnDef(counterpartCol);

    settleNoCol = new RGridColumnDef(PPaymentNoticeDef.settleNo);
    settleNoCol.setWidth("100px");
    settleNoCol.setName(FIELD_SETTLENO);
    settleNoCol.setSortable(true);
    grid.addColumnDef(settleNoCol);

    receiptTotalCol = new RGridColumnDef(PPaymentNoticeDef.receiptTotal_total);
    receiptTotalCol.setWidth("100px");
    receiptTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    receiptTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    receiptTotalCol.setName(FIELD_RECEIPTTOTAL);
    receiptTotalCol.setSortable(true);
    grid.addColumnDef(receiptTotalCol);

    receiptIvcTotalCol = new RGridColumnDef(PPaymentNoticeDef.receiptIvcTotal_total);
    receiptIvcTotalCol.setWidth("120px");
    receiptIvcTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    receiptIvcTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    receiptIvcTotalCol.setName(FIELD_RECEIPTIVCTOTAL);
    receiptIvcTotalCol.setSortable(true);
    grid.addColumnDef(receiptIvcTotalCol);

    paymentTotalCol = new RGridColumnDef(PPaymentNoticeDef.paymentTotal_total);
    paymentTotalCol.setWidth("100px");
    paymentTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    paymentTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    paymentTotalCol.setName(FIELD_PAYMENTTOTAL);
    paymentTotalCol.setSortable(true);
    grid.addColumnDef(paymentTotalCol);

    paymentIvcTotalCol = new RGridColumnDef(PPaymentNoticeDef.paymentIvcTotal_total);
    paymentIvcTotalCol.setWidth("120px");
    paymentIvcTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    paymentIvcTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    paymentIvcTotalCol.setName(FIELD_PAYMENTIVCTOTAL);
    paymentIvcTotalCol.setSortable(true);
    grid.addColumnDef(paymentIvcTotalCol);

    remarkCol = new RGridColumnDef(PPaymentNoticeDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  private void drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());
    flecsPanel.addField(PPaymentNoticeDef.billNumber);
    flecsPanel.addField(PPaymentNoticeDef.bizState);
    flecsPanel.addField(businessUnit);
    if (getEP().isPermEnabled())
      flecsPanel.addField(permGroup);
    flecsPanel.addField(counterpartType);
    flecsPanel.addField(counterpart);
    flecsPanel.addField(PPaymentNoticeLineDef.contract_code);
    flecsPanel.addField(PPaymentNoticeLineDef.statement_billNumber);
    flecsPanel.addField(PPaymentNoticeDef.settleNo);
    flecsPanel.addField(PPaymentNoticeDef.createInfo_operator_id);
    flecsPanel.addField(PPaymentNoticeDef.createInfo_operator_fullName);
    flecsPanel.addField(PPaymentNoticeDef.createInfo_time);
    flecsPanel.addField(PPaymentNoticeDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PPaymentNoticeDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PPaymentNoticeDef.lastModifyInfo_time);
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
    if (getEP().isPermitted(PaymentNoticePermDef.READ) == false) {
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
    getEP().getTitleBar().setTitleText(PaymentNoticeMessages.M.search());
    getEP().getTitleBar().appendAttributeText(getEP().getModuleCaption());
  }

  private void refreshFlecs() {
    grid.setSort(billNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel.addCondition(new Condition(PPaymentNoticeDef.billNumber,
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
    conditions.add(new Condition(PPaymentNoticeDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PPaymentNoticeDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(businessUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpartType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(counterpart, DefaultOperator.EQUALS, null));
    conditions
        .add(new Condition(PPaymentNoticeLineDef.contract_code, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PPaymentNoticeLineDef.statement_billNumber,
        DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PPaymentNoticeDef.settleNo, DefaultOperator.EQUALS, SettleNoField
        .getCurrentSettleNo()));
    return conditions;
  }

  private void doDelete() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(PaymentNoticeMessages.M.seleteDataToAction(
          PaymentNoticeMessages.M.delete(), getEP().getModuleCaption()));
      return;
    }

    batchProcessor.batchProcess(PaymentNoticeMessages.M.delete(), new BPaymentNotice[] {},
        new PagingGridBatchProcessCallback<BPaymentNotice>() {
          @Override
          public void execute(BPaymentNotice entity, BatchProcesser<BPaymentNotice> processer,
              AsyncCallback callback) {
            if (!BBizStates.INEFFECT.equals(entity.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            PaymentNoticeService.Locator.getService().delete(entity.getUuid(), entity.getVersion(),
                callback);
            BPaymentNoticeLogger.getInstance().log(PaymentNoticeMessages.M.delete(), entity);
          }
        });
  }

  private void doEffect() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(PaymentNoticeMessages.M.seleteDataToAction(
          PaymentNoticeMessages.M.effect(), getEP().getModuleCaption()));
      return;
    }

    batchProcessor.batchProcess(PaymentNoticeMessages.M.effect(), new BPaymentNotice[] {},
        new PagingGridBatchProcessCallback<BPaymentNotice>() {

          @Override
          public void execute(BPaymentNotice entity, BatchProcesser<BPaymentNotice> processer,
              AsyncCallback callback) {
            if (!BBizStates.INEFFECT.equals(entity.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            PaymentNoticeService.Locator.getService().effect(entity.getUuid(), null,
                entity.getVersion(), callback);
            BPaymentNoticeLogger.getInstance().log(PaymentNoticeMessages.M.effect(), entity);
          }
        });
  }

  private void doAbortConfirm() {
    if (pagingGrid.getSelections().isEmpty()) {
      RMsgBox.showError(PaymentNoticeMessages.M.seleteDataToAction(PaymentNoticeMessages.M.abort(),
          getEP().getModuleCaption()));
      return;
    }

    InputBox.show(PaymentNoticeMessages.M.abortReason(), null, true,
        PPaymentNoticeDef.latestComment, new InputBox.Callback() {
          @Override
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            doAbort(text);
          }
        });
  }

  private void doAbort(final String text) {
    batchProcessor.batchProcess(PaymentNoticeMessages.M.abort(), new BPaymentNotice[] {}, true,
        false, new PagingGridBatchProcessCallback<BPaymentNotice>() {
          @Override
          public void execute(BPaymentNotice entity, BatchProcesser<BPaymentNotice> processer,
              AsyncCallback callback) {
            if (!BBizStates.EFFECT.equals(entity.getBizState())) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            PaymentNoticeService.Locator.getService().abort(entity.getUuid(), entity.getVersion(),
                text, callback);
            BPaymentNoticeLogger.getInstance().log(PaymentNoticeMessages.M.abort(), entity);
          }
        });
  }

  private void doBatch() {
    JumpParameters jumParams = new JumpParameters(PaymentNoticeBatchPage.START_NODE);
    getEP().jump(jumParams);
  }

  private void doEditOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BPaymentNotice paymentNotice = pagingGrid.getPageData().get(hotItem.getGridRow());

    JumpParameters jumParams = new JumpParameters(PaymentNoticeEditPage.START_NODE);
    jumParams.getUrlRef().set(PaymentNoticeEditPage.PN_ENTITY_UUID, paymentNotice.getUuid());
    getEP().jump(jumParams);
  }

  private void doDeleteOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BPaymentNotice paymentNotice = pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmDeleteOne(paymentNotice);
  }

  private void doConfirmDeleteOne(final BPaymentNotice paymentNotice) {
    getMessagePanel().clearMessages();

    String msg = PaymentNoticeMessages.M.actionComfirm2(PaymentNoticeMessages.M.delete(), getEP()
        .getModuleCaption(), paymentNotice.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        deleteOne(paymentNotice);
      }
    });
  }

  private void deleteOne(final BPaymentNotice paymentNotice) {
    RLoadingDialog.show(PaymentNoticeMessages.M.actionDoing(PaymentNoticeMessages.M.delete()));
    PaymentNoticeService.Locator.getService().delete(paymentNotice.getUuid(),
        paymentNotice.getVersion(), new RBAsyncCallback2<Void>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentNoticeMessages.M.actionFailed2(PaymentNoticeMessages.M.delete(),
                getEP().getModuleCaption(), paymentNotice.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BPaymentNoticeLogger.getInstance().log(PaymentNoticeMessages.M.delete(), paymentNotice);

            String msg = PaymentNoticeMessages.M.onSuccess(PaymentNoticeMessages.M.delete(),
                getEP().getModuleCaption(), paymentNotice.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doEffectOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BPaymentNotice paymentNotice = pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmEffectOne(paymentNotice);
  }

  private void doConfirmEffectOne(final BPaymentNotice paymentNotice) {
    getMessagePanel().clearMessages();

    String msg = PaymentNoticeMessages.M.actionComfirm2(PaymentNoticeMessages.M.effect(), getEP()
        .getModuleCaption(), paymentNotice.getBillNumber());

    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        effectOne(paymentNotice);
      }
    });
  }

  private void effectOne(final BPaymentNotice paymentNotice) {
    RLoadingDialog.show(PaymentNoticeMessages.M.actionDoing(PaymentNoticeMessages.M.effect()));
    PaymentNoticeService.Locator.getService().effect(paymentNotice.getUuid(), null,
        paymentNotice.getVersion(), new RBAsyncCallback2<Void>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentNoticeMessages.M.actionFailed2(PaymentNoticeMessages.M.effect(),
                getEP().getModuleCaption(), paymentNotice.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BPaymentNoticeLogger.getInstance().log(PaymentNoticeMessages.M.effect(), paymentNotice);

            String msg = PaymentNoticeMessages.M.onSuccess(PaymentNoticeMessages.M.effect(),
                getEP().getModuleCaption(), paymentNotice.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private void doAbortOne(ClickEvent event) {
    RMenuItem menuItem = (RMenuItem) event.getSource();
    RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
    BPaymentNotice paymentNotice = pagingGrid.getPageData().get(hotItem.getGridRow());

    doConfirmAbortOne(paymentNotice);
  }

  private void doConfirmAbortOne(final BPaymentNotice paymentNotice) {
    getMessagePanel().clearMessages();

    InputBox.show(PaymentNoticeMessages.M.abortReason(), null, true,
        PPaymentNoticeDef.latestComment, new InputBox.Callback() {
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            abortOne(paymentNotice, text);
          }
        });
  }

  private void abortOne(final BPaymentNotice paymentNotice, String text) {
    RLoadingDialog.show(PaymentNoticeMessages.M.actionDoing(PaymentNoticeMessages.M.abort()));
    PaymentNoticeService.Locator.getService().abort(paymentNotice.getUuid(),
        paymentNotice.getVersion(), text, new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentNoticeMessages.M.actionFailed2(PaymentNoticeMessages.M.abort(),
                getEP().getModuleCaption(), paymentNotice.getBillNumber());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BPaymentNoticeLogger.getInstance().log(PaymentNoticeMessages.M.abort(), paymentNotice);

            String msg = PaymentNoticeMessages.M.onSuccess(PaymentNoticeMessages.M.abort(), getEP()
                .getModuleCaption(), paymentNotice.getBillNumber());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

  private class Handler_clickAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(deleteAction)) {
        doDelete();
      } else if (event.getSource().equals(effectAction)) {
        doEffect();
      } else if (event.getSource().equals(abortAction)) {
        doAbortConfirm();
      } else if (event.getSource().equals(batchAction)) {
        doBatch();
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

  private class Handler_grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(billNumberCol)) {
        BPaymentNotice entity = pagingGrid.getRowData(cell.getRow());

        if (entity == null)
          return;
        getEP().jumpToViewPage(entity.getUuid());
      }
    }
  }

  private class GridDataProvider implements RPageDataProvider<BPaymentNotice> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BPaymentNotice>> callback) {
      pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);
      if (sortField != null)
        pageSort.appendOrder(sortField, sortDir);
      else
        pageSort.appendOrder(FIELD_BILLNUMBER, OrderDir.desc);

      PaymentNoticeService.Locator.getService().query(
          flecsPanel.getCurrentConfig().getConditions(), pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BPaymentNotice rowData, List<BPaymentNotice> pageData) {
      if (col == billNumberCol.getIndex())
        return rowData.getBillNumber();
      else if (col == bizStateCol.getIndex())
        return rowData.getBizState() == null ? null : PPaymentNoticeDef.bizState
            .getEnumCaption(rowData.getBizState());
      else if (col == counterpartCol.getIndex())
        return rowData.getCounterpart() == null ? null : rowData.getCounterpart().toFriendlyStr(
            getEP().getCounterpartTypeMap());
      else if (col == settleNoCol.getIndex())
        return rowData.getSettleNo();
      else if (col == receiptTotalCol.getIndex())
        return rowData.getReceiptTotal() == null ? BigDecimal.ZERO : rowData.getReceiptTotal()
            .doubleValue();
      else if (col == receiptIvcTotalCol.getIndex())
        return rowData.getReceiptIvcTotal() == null ? BigDecimal.ZERO : rowData
            .getReceiptIvcTotal().doubleValue();
      else if (col == paymentTotalCol.getIndex())
        return rowData.getPaymentTotal() == null ? BigDecimal.ZERO : rowData.getPaymentTotal()
            .doubleValue();
      else if (col == paymentIvcTotalCol.getIndex())
        return rowData.getPaymentIvcTotal() == null ? BigDecimal.ZERO : rowData
            .getPaymentIvcTotal().doubleValue();
      else if (col == remarkCol.getIndex())
        return rowData.getRemark();
      return null;
    }
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (permGroup == field)
        return new PermGroupField(permGroup);
      else if (counterpart == field)
        return DefaultOperator.EQUALS == operator ? buildCounterpart() : null;
      else if (PPaymentNoticeDef.settleNo == field) {
        SettleNoField settleNoField = new SettleNoField(PPaymentNoticeDef.settleNo);
        settleNoField.refreshOption(12);
        settleNoField.setMaxLength(6);
        return settleNoField;
      } else if (businessUnit == field)
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      else if (counterpartType == field) {
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
      setNullOptionText(PaymentNoticeMessages.M.all());
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

      if (PPaymentNoticeDef.bizState == field || permGroup == field) {
        result.clear();
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (counterpart == field || PPaymentNoticeDef.settleNo == field
          || businessUnit == field || counterpartType == field) {
        result.clear();
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PPaymentNoticeDef.createInfo_time == field
          || PPaymentNoticeDef.lastModifyInfo_time == field) {
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

  @Override
  protected RPagingGrid getPagingGrid() {
    return pagingGrid;
  }

  @Override
  protected EPPaymentNotice getEP() {
    return EPPaymentNotice.getInstance();
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
    menu.addSeparator();
    menu.addItem(new RMenuItem(batchAction));
  }

  @Override
  protected void appendLineMenu(RPopupMenu menu, Object bill) {
    BPaymentNotice entity = (BPaymentNotice) bill;
    menu.addSeparator();
    menu.addItem(editOneMenuItem);
    menu.addItem(deleteOneMenuItem);
    menu.addSeparator();
    menu.addItem(effectOneMenuItem);
    menu.addItem(abortOneMenuItem);

    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());

    editOneMenuItem.setVisible(isInEffect && getEP().isPermitted(PaymentNoticePermDef.UPDATE));
    deleteOneMenuItem.setVisible(isInEffect && getEP().isPermitted(PaymentNoticePermDef.DELETE));
    effectOneMenuItem.setVisible(isInEffect && getEP().isPermitted(PaymentNoticePermDef.EFFECT));
    abortOneMenuItem.setVisible(isEffect && getEP().isPermitted(PaymentNoticePermDef.ABORT));
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