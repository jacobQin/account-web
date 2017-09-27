/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	FreezeViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-3 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.freeze.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBill;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.freeze.client.EPFreeze;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreeze;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreezeLine;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreezeLogger;
import com.hd123.m3.account.gwt.freeze.client.biz.BFreezeState;
import com.hd123.m3.account.gwt.freeze.client.rpc.FreezeLoader;
import com.hd123.m3.account.gwt.freeze.client.rpc.FreezeService;
import com.hd123.m3.account.gwt.freeze.intf.client.FreezeMessages;
import com.hd123.m3.account.gwt.freeze.intf.client.FreezeUrlParams.View;
import com.hd123.m3.account.gwt.freeze.intf.client.dd.PFreezeDef;
import com.hd123.m3.account.gwt.freeze.intf.client.dd.PFreezeLineDef;
import com.hd123.m3.account.gwt.freeze.intf.client.perm.FreezePermDef;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementDef;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.dialog.InputBox;
import com.hd123.m3.commons.gwt.widget.client.biz.PrintingTemplate;
import com.hd123.m3.commons.gwt.widget.client.ui.navi.DefaultNavigateHandler;
import com.hd123.m3.commons.gwt.widget.client.ui.navi.EntityNavigator;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupViewField;
import com.hd123.m3.commons.gwt.widget.client.ui.print.PrintButton;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHyperlinkRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class FreezeViewPage extends BaseContentPage implements View {

  public static FreezeViewPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new FreezeViewPage();
    return instance;
  }

  public FreezeViewPage() throws ClientBizException {
    super();
    try {
      loader = new FreezeLoader();
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(FreezeMessages.M.cannotCreatePage("FreezeViewPage"), e);
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
  public void onShow(final JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    loader.decoderParams(params, new Command() {
      @Override
      public void execute() {
        entity = loader.getEntity();
        refreshTitle();
        refreshToolbar();
        refreshCommands();
        refreshPrinting();
        refreshGeneral();
        refreshGrid();
      }
    });
  }

  private void refreshToolbar() {
    navigator.start(ep.getObjectName(), entity.getUuid());
    refreshPrint();
    // 去除导航器消失时多余的分隔符
    getToolbar().minimizeSeparators();
  }

  private void refreshPrint() {
    if (printButton == null) {
      return;
    }
    if (entity != null) {
      printButton.clearParameters();
      printButton.setEnabled(ep.isPermitted(BAction.PRINT.getKey()));
      Map<String, String> map = new HashMap<String, String>();
      map.put(PrintingTemplate.KEY_UUID, entity.getUuid());
      map.put(PrintingTemplate.KEY_BILLNUMBER, entity.getBillNumber());
      printButton.addPrintObject(map);
      if (entity.getStore() != null
          && StringUtil.isNullOrBlank(entity.getStore().getUuid()) == false) {
        printButton.setStoreCodes(entity.getStore().getCode());
      }
    }
  }

  private static FreezeViewPage instance;
  private EPFreeze ep = EPFreeze.getInstance();
  private BFreeze entity;
  private Handler_click clickHandler = new Handler_click();
  private FreezeLoader loader;

  private RAction createAction;
  private RAction unfreezeAction;
  private RToolbarSeparator separator;
  private EntityNavigator navigator;
  private PrintButton printButton;

  private RViewStringField billNumberField;
  private RViewStringField accountUnitField;
  private RViewStringField counterpartField;

  private RViewStringField freezeReasonField;

  private RViewStringField unfreezeReasonField;

  private RForm operateForm;
  private RViewStringField stateField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RSimpleOperateInfoField freezeInfoField;
  private RSimpleOperateInfoField unfreezeInfoField;
  private RHyperlink moreInfoField;

  private RViewNumberField payTotalField;
  private RViewNumberField receiptTotalField;

  private PermGroupViewField permGroupField;

  private HTML resultTotalHtml;
  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef directionCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef statementNumCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef sourceBillNumCol;

  private void drawToolbar() {
    createAction = new RAction(RActionFacade.CREATE, clickHandler);
    getToolbar().add(new RToolbarButton(createAction));

    separator = getToolbar().addSeparator();

    unfreezeAction = new RAction(FreezeMessages.M.unfreeze(), clickHandler);
    getToolbar().add(new RToolbarButton(unfreezeAction));

    // 导航
    drawNaviButtons();
    // 打印
    drawPrintButton();
  }

  private void drawNaviButtons() {
    navigator = new EntityNavigator(ep.getModuleService());
    navigator.setNavigateHandler(new DefaultNavigateHandler(ep, getStartNode(), ep.getUrlBizKey()));
    getToolbar().addToRight(navigator);
  }

  private void drawPrintButton() {
    getToolbar().addToRight(new RToolbarSeparator());
    printButton = new PrintButton(ep.getPrintTemplate(), ep.getCurrentUser().getId());
    getToolbar().addToRight(printButton);
    // 刷新模板
    printButton.refresh();
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    panel.add(drawTop());
    panel.add(drawSecond());
  }

  private Widget drawTop() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    mvp.add(0, drawBasic());
    mvp.add(0, drawFreezeReason());
    mvp.add(0, drawUnfreezeReason());
    mvp.add(1, drawOperate());
    mvp.add(1, drawAggregate());
    permGroupField = new PermGroupViewField();
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(FreezeMessages.M.generalInfo());
    box.setWidth("100%");
    box.setContent(vp);
    return box;
  }

  private Widget drawBasic() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    billNumberField = new RViewStringField(PFreezeDef.billNumber);
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    form.addField(billNumberField);

    accountUnitField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    form.addField(accountUnitField);

    counterpartField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    form.addField(counterpartField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FreezeMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawFreezeReason() {
    freezeReasonField = new RViewStringField();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FreezeMessages.M.freezeReason());
    box.setWidth("100%");
    box.setContent(freezeReasonField);
    return box;
  }

  private Widget drawUnfreezeReason() {
    unfreezeReasonField = new RViewStringField();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FreezeMessages.M.unfreezeReason());
    box.setWidth("100%");
    box.setContent(unfreezeReasonField);
    return box;
  }

  private Widget drawOperate() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    stateField = new RViewStringField(PFreezeDef.state);
    stateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(stateField);

    createInfoField = new RSimpleOperateInfoField(PFreezeDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PFreezeDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    freezeInfoField = new RSimpleOperateInfoField(PFreezeDef.freezeInfo);
    operateForm.addField(freezeInfoField);

    unfreezeInfoField = new RSimpleOperateInfoField(PFreezeDef.unfreezeInfo);
    operateForm.addField(unfreezeInfoField);

    moreInfoField = new RHyperlink();
    moreInfoField.addClickHandler(clickHandler);
    moreInfoField.setHTML(FreezeMessages.M.moreInfo());
    RCombinedField moreField = new RCombinedField() {
      {
        addField(moreInfoField, 0.9f);
        addField(new HTML(), 0.1f);
      }
    };
    operateForm.addField(moreField);

    operateForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FreezeMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawAggregate() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    payTotalField = new RViewNumberField(PFreezeDef.paymentTotal_total);
    payTotalField.setFormat(M3Format.fmt_money);
    payTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
    form.addField(payTotalField);

    receiptTotalField = new RViewNumberField(PFreezeDef.receiptTotal_total);
    receiptTotalField.setFormat(M3Format.fmt_money);
    receiptTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
    form.addField(receiptTotalField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FreezeMessages.M.aggregate());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawSecond() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(5);

    panel.add(drawGrid());

    RCaptionBox box = new RCaptionBox();
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(panel);
    box.setCaption(FreezeMessages.M.accountLine());

    resultTotalHtml = new HTML(FreezeMessages.M.resultTotal(0));
    box.getCaptionBar().addButton(resultTotalHtml);

    return box;
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setProvider(new GridDateProvider());
    grid.addClickHandler(new Handler_grid());

    lineNumberCol = new RGridColumnDef(PFreezeLineDef.lineNumber);
    lineNumberCol.setWidth("40px");
    grid.addColumnDef(lineNumberCol);

    subjectCodeCol = new RGridColumnDef(PFreezeLineDef.acc1_subject_code);
    subjectCodeCol.setRendererFactory(new RHyperlinkRendererFactory());
    subjectCodeCol.setWidth("100px");
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PFreezeLineDef.acc1_subject_name);
    subjectNameCol.setWidth("100px");
    grid.addColumnDef(subjectNameCol);

    directionCol = new RGridColumnDef(PFreezeLineDef.acc1_direction);
    directionCol.setWidth("80px");
    grid.addColumnDef(directionCol);

    totalCol = new RGridColumnDef(PFreezeLineDef.total_total);
    totalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    totalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    totalCol.setWidth("80px");
    grid.addColumnDef(totalCol);

    taxCol = new RGridColumnDef(PFreezeLineDef.total_tax);
    taxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    taxCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    taxCol.setWidth("80px");
    grid.addColumnDef(taxCol);

    statementNumCol = new RGridColumnDef(PFreezeLineDef.acc2_statement_billUuid);
    statementNumCol.setRendererFactory(new RHyperlinkRendererFactory());
    statementNumCol.setWidth("160px");
    grid.addColumnDef(statementNumCol);

    sourceBillTypeCol = new RGridColumnDef(PFreezeLineDef.acc1_sourceBill_billType);
    sourceBillTypeCol.setWidth("160px");
    grid.addColumnDef(sourceBillTypeCol);

    sourceBillNumCol = new RGridColumnDef(PFreezeLineDef.acc1_sourceBill_billUuid);
    sourceBillNumCol.setWidth("160px");
    sourceBillNumCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    grid.addColumnDef(sourceBillNumCol);

    grid.setAllColumnsOverflowEllipsis(true);

    return grid;
  }

  private boolean checkIn() {
    if (ep.isPermitted(FreezePermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ep.getModuleCaption());
    if (entity != null)
      ep.getTitleBar().appendAttributeText(entity.getBillNumber());
  }

  private void refreshPrinting() {
    printButton.setEnabled(ep.isPermitted(FreezePermDef.PRINT));
    printButton.setObjectUuid(entity.getUuid());
    printButton.getParameters().put(PrintingTemplate.KEY_BILLNUMBER, entity.getBillNumber());
  }

  private void refreshCommands() {
    createAction.setEnabled(ep.isPermitted(FreezePermDef.CREATE));
    unfreezeAction.setEnabled(ep.isPermitted(FreezePermDef.UNFREEZE));

    boolean freeze = BFreezeState.froze.name().equals(entity.getState());

    separator.setVisible(freeze);
    unfreezeAction.setVisible(freeze);

    // printAction.setEnabled(ep.getReportFile() != null);
  }

  private void refreshGeneral() {
    if (entity == null)
      return;

    refreshLineNumber();

    billNumberField.setValue(entity.getBillNumber());
    accountUnitField.setValue(entity.getAccountUnit().getNameCode());
    counterpartField.setValue(entity.getCounterpart().toFriendlyStr(ep.getCounterpartTypeMap()));

    if (entity.getFreezeReason() != null)
      freezeReasonField.setValue(entity.getFreezeReason());
    else
      freezeReasonField.clearValue();

    if (entity.getUnfreezeReason() != null)
      unfreezeReasonField.setValue(entity.getUnfreezeReason());
    else
      unfreezeReasonField.clearValue();

    stateField.setValue(BFreezeState.valueOf(entity.getState()).getCaption());
    if (BFreezeState.froze.name().equals(entity.getState())) {
      freezeInfoField.setVisible(true);
      unfreezeInfoField.setVisible(false);
      freezeInfoField.setOperateInfo(entity.getFreezeInfo());
    } else if (BFreezeState.unfroze.name().equals(entity.getState())) {
      unfreezeInfoField.setVisible(true);
      freezeInfoField.setVisible(false);
      unfreezeInfoField.setOperateInfo(entity.getUnfreezeInfo());
    }
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());
    operateForm.rebuild();

    permGroupField.refresh(ep.isPermEnabled(), entity);

    refreshAggreGate();
  }

  private void refreshLineNumber() {
    if (entity.getLines().isEmpty() == false) {
      for (int i = 0; i < entity.getLines().size(); i++) {
        BFreezeLine line = entity.getLines().get(i);
        line.setLineNumber(i + 1);
      }
    }
  }

  private void refreshAggreGate() {
    entity.aggregate();
    payTotalField.setValue(entity.getFreezePayTotal());
    receiptTotalField.setValue(entity.getFreezeRecTotal());
  }

  private void refreshGrid() {
    resultTotalHtml.setText(FreezeMessages.M.resultTotal(entity.getLines().size()));
    grid.refresh();
  }

  private class Handler_click implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == createAction) {
        JumpParameters params = new JumpParameters(FreezeCreatePage.START_NODE);
        ep.jump(params);
      } else if (event.getSource() == unfreezeAction) {
        doUnfreeze();
      } else if (event.getSource() == moreInfoField) {
        doViewLog();
      }
      // else if (event.getSource() == printAction) {
      // ReportPrintUtil.print(ep.getReportFile(), entity.getUuid());
      // }
    }
  }

  private void doUnfreeze() {
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

  private void doUnfreeze(String text) {
    RLoadingDialog.show(FreezeMessages.M.actionDoing(FreezeMessages.M.unfreeze()));
    FreezeService.Locator.getService().unfreeze(entity.getUuid(), entity.getVersion(), text,
        new RBAsyncCallback2<Void>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = FreezeMessages.M.actionFailed2(FreezeMessages.M.unfreeze(),
                ep.getModuleCaption(), entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BFreezeLogger.getInstance().log(FreezeMessages.M.unfreeze(), entity);

            String msg = FreezeMessages.M.onSuccess(FreezeMessages.M.unfreeze(),
                ep.getModuleCaption(), entity.getBillNumber());
            ep.jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  private void doViewLog() {
    JumpParameters params = new JumpParameters(FreezeLogPage.START_NODE);
    params.getUrlRef().set(FreezeLogPage.PN_ENTITY_UUID, entity.getUuid());
    params.getExtend().put(EPFreeze.OPN_ENTITY, entity);
    ep.jump(params);
  }

  private class GridDateProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return entity == null ? 0 : entity.getLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (entity == null || entity.getLines().isEmpty())
        return null;

      BFreezeLine rowData = entity.getLines().get(row);
      if (col == lineNumberCol.getIndex())
        return rowData.getLineNumber();
      else if (col == subjectCodeCol.getIndex())
        return rowData.getAcc1() != null && rowData.getAcc1().getSubject() != null ? rowData
            .getAcc1().getSubject().getCode() : null;
      else if (col == subjectNameCol.getIndex())
        return rowData.getAcc1() != null && rowData.getAcc1().getSubject() != null ? rowData
            .getAcc1().getSubject().getName() : null;
      else if (col == directionCol.getIndex())
        return rowData.getAcc1() != null ? DirectionType.getCaptionByValue(rowData.getAcc1()
            .getDirection()) : null;
      else if (col == totalCol.getIndex())
        return rowData.getTotal().doubleValue();
      else if (col == taxCol.getIndex())
        return rowData.getTax().doubleValue();
      else if (col == statementNumCol.getIndex())
        return rowData.getAcc2() != null && rowData.getAcc2().getStatement() != null ? getStatementNumber(rowData
            .getAcc2().getStatement()) : null;
      else if (col == sourceBillTypeCol.getIndex())
        return rowData.getAcc1() != null && rowData.getAcc1().getSourceBill() != null
            && rowData.getAcc1().getSourceBill().getBillType() != null ? ep.getBillType().get(
            rowData.getAcc1().getSourceBill().getBillType()) : null;
      else if (col == sourceBillNumCol.getIndex()) {
        if (rowData.getAcc1() == null || rowData.getAcc1().getSourceBill() == null)
          return null;
        BDispatch dispatch = new BDispatch(rowData.getAcc1().getSourceBill().getBillType());
        dispatch
            .addParams(GRes.R.dispatch_key(), rowData.getAcc1().getSourceBill().getBillNumber());
        return dispatch;
      }
      return null;
    }
  }

  private String getStatementNumber(BBill bill) {
    if (FreezeMessages.M.noneBillNumber().equals(bill.getBillNumber()))
      return null;
    else
      return bill.getBillNumber();
  }

  private class Handler_grid implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      RGridColumnDef colDef = cell.getColumnDef();
      int row = cell.getRow();

      BFreezeLine line = entity.getLines().get(row);
      if (line == null)
        return;

      if (colDef.equals(subjectCodeCol)) {
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID,
            line.getAcc1().getSubject().getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
        } catch (Exception e) {
          String msg = FreezeMessages.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
          RMsgBox.showError(msg, e);
        }
      } else if (colDef.equals(statementNumCol)) {
        if (!"-".equals(line.getAcc2().getStatement().getBillNumber())) {
          GwtUrl url = StatementUrlParams.ENTRY_URL;
          url.getQuery().set(JumpParameters.PN_START, StatementUrlParams.View.START_NODE);
          url.getQuery().set(StatementUrlParams.View.PN_UUID,
              line.getAcc2().getStatement().getBillUuid());
          try {
            RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
          } catch (Exception e) {
            String msg = FreezeMessages.M.cannotNavigate(PStatementDef.TABLE_CAPTION);
            RMsgBox.showError(msg, e);
          }
        }
      }
    }
  }

}
