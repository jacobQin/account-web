/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StatementAdjustViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.AccountDefrayalUrlParams;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.dd.PAccountDefrayalDef;
import com.hd123.m3.account.gwt.statement.adjust.client.EPStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLine;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjustLogger;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustLoader;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustService;
import com.hd123.m3.account.gwt.statement.adjust.client.rpc.StatementAdjustServiceAgent;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustMessages;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustUrlParams;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.StatementAdjustUrlParams.View;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.dd.PStatementAdjustDef;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.dd.PStatementAdjustLineDef;
import com.hd123.m3.account.gwt.statement.adjust.intf.client.perm.StatementAdjustPermDef;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementDef;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmViewPage;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
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
import com.hd123.rumba.gwt.widget2.client.form.RHyperlinkField;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RBooleanRendererFactory;
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
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class StatementAdjustViewPage extends BaseBpmViewPage implements View {

  public static StatementAdjustViewPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new StatementAdjustViewPage();
    return instance;
  }

  public StatementAdjustViewPage() throws ClientBizException {
    super();
    try {
      loader = new StatementAdjustLoader();
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(
          StatementAdjustMessages.M.cannotCreatePage("StatementAdjustViewPage"), e);
    }
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
  public void onShow(final JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    loader.decoderParams(params, new Command() {

      @Override
      public void execute() {
        entity = loader.getEntity();
        refreshGeneral();
        refreshGrid();
        refreshBpm(entity);
        refreshToolbar();
      }
    });
  }

  private void refreshToolbar() {
    getEP().appendSearchBox();
    navigator.start(getEP().getObjectName(), entity.getUuid());
    refreshPrint();
    // 去除导航器消失时多余的分隔符
    getToolbar().minimizeSeparators();
  }

  private static StatementAdjustViewPage instance;

  private BStatementAdjust entity;
  private StatementAdjustLoader loader;

  private RAction editAction;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;
  private RAction accDefrayalAction;

  private EntityNavigator navigator;
  private PrintButton printButton;

  private RViewStringField billNumberField;
  private RHyperlinkField statementNumberField;
  private RViewStringField accountUnitField;
  private DispatchLinkField contractBillNumberField;
  private RViewStringField contractNameField;
  private RViewStringField counterpartField;
  private RViewStringField settleNoField;

  private RViewNumberField receiptTotalField;
  private RViewNumberField receiptTaxField;
  private RViewNumberField paymentTotalField;
  private RViewNumberField paymentTaxField;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField reasonField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RHyperlink moreInfoField;

  private PermGroupViewField permGroupField;

  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef directionCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef invoiceCol;
  private RGridColumnDef accountDateCol;
  private RGridColumnDef beginDateCol;
  private RGridColumnDef endDateCol;
  private RGridColumnDef lastPayDateCol;
  private RGridColumnDef remarkCol;
  private HTML resultTotalHtml;

  private RCaptionBox remarkBox;
  private RTextArea remarkField;

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());

    editAction = new RAction(RActionFacade.EDIT, new Handler_editAction());
    RToolbarButton editButton = new RToolbarButton(editAction);
    getToolbar().add(editButton);

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deletection());
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    getToolbar().add(deleteButton);

    getToolbar().addSeparator();

    injectBpmActions();

    getToolbar().addSeparator();

    effectAction = new RAction(StatementAdjustMessages.M.effect(), new Handler_effectAction());
    RToolbarButton effectButton = new RToolbarButton(effectAction);
    getToolbar().add(effectButton);

    abortAction = new RAction(
        StatementAdjustMessages.M.abort() + StatementAdjustMessages.M.doing(),
        new Handler_abortAction());
    RToolbarButton abortButton = new RToolbarButton(abortAction);
    getToolbar().add(abortButton);

    getToolbar().addSeparator();

    accDefrayalAction = new RAction(StatementAdjustMessages.M.accDefrayal(),
        new Handler_accDefrayalAction());
    RToolbarButton accDefrayalButton = new RToolbarButton(accDefrayalAction);
    getToolbar().add(accDefrayalButton);

    // 导航
    drawNaviButtons();
    // 打印
    drawPrintButton();
  }

  private void drawNaviButtons() {
    navigator = new EntityNavigator(getEP().getModuleService());
    navigator.setNavigateHandler(new DefaultNavigateHandler(getEP(), getStartNode(), getEP()
        .getUrlBizKey()));
    getToolbar().addToRight(navigator);
  }

  private void drawPrintButton() {
    getToolbar().addToRight(new RToolbarSeparator());
    printButton = new PrintButton(getEP().getPrintTemplate(), getEP().getCurrentUser().getId());
    getToolbar().addToRight(printButton);
    // 刷新模板
    printButton.refresh();
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    panel.add(drawTopPanel());
    panel.add(drawMiddlePanel());
    panel.add(drawRemarkPanel());
  }

  private Widget drawTopPanel() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    mvp.add(0, drawBasicInfo());
    mvp.add(0, drawTotalPanel());
    mvp.add(1, drawOperatePanel());
    permGroupField = new PermGroupViewField();
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(StatementAdjustMessages.M.generalInfo());
    box.setWidth("100%");
    box.setContent(vp);

    return box;
  }

  private Widget drawBasicInfo() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    billNumberField = new RViewStringField(PStatementAdjustDef.billNumber);
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    form.addField(billNumberField);

    statementNumberField = new RHyperlinkField(PStatementAdjustDef.constants.statement_billNumber());
    statementNumberField.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        if (entity == null || entity.getStatement() == null
            || entity.getStatement().getBillUuid() == null)
          return;

        GwtUrl url = StatementUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, StatementUrlParams.View.START_NODE);
        url.getQuery().set(StatementUrlParams.View.PN_UUID, entity.getStatement().getBillUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
        } catch (Exception e) {
          String msg = StatementAdjustMessages.M.cannotNavigate(PStatementDef.TABLE_CAPTION);
          RMsgBox.showError(msg, e);
        }
      }
    });
    form.addField(statementNumberField);

    accountUnitField = new RViewStringField(EPStatementAdjust.getInstance().getFieldCaption(
        GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    form.addField(accountUnitField);

    contractBillNumberField = new DispatchLinkField(PStatementAdjustDef.constants.contract_code());
    contractBillNumberField.setLinkKey(GRes.R.dispatch_key());
    form.addField(contractBillNumberField);

    contractNameField = new RViewStringField(PStatementAdjustDef.constants.contract_name());
    form.addField(contractNameField);

    counterpartField = new RViewStringField(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    form.addField(counterpartField);

    settleNoField = new RViewStringField(PStatementAdjustDef.settleNo);
    form.addField(settleNoField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(StatementAdjustMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawTotalPanel() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    receiptTotalField = new RViewNumberField();
    receiptTotalField.setFormat(M3Format.fmt_money);
    receiptTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    receiptTaxField = new RViewNumberField();
    receiptTaxField.setFormat(M3Format.fmt_money);
    receiptTaxField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(new RCombinedField() {
      {
        setCaption(PStatementAdjustDef.constants.receiptTotal_total() + "/"
            + PStatementAdjustDef.constants.receiptTotal_tax());
        addField(receiptTotalField, 0.5f);
        addField(receiptTaxField, 0.5f);
      }
    });

    paymentTotalField = new RViewNumberField();
    paymentTotalField.setFormat(M3Format.fmt_money);
    paymentTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    paymentTaxField = new RViewNumberField();
    paymentTaxField.setFormat(M3Format.fmt_money);
    paymentTaxField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(new RCombinedField() {
      {
        setCaption(PStatementAdjustDef.constants.paymentTotal_total() + "/"
            + PStatementAdjustDef.constants.paymentTotal_tax());
        addField(paymentTotalField, 0.5f);
        addField(paymentTaxField, 0.5f);
      }
    });

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(StatementAdjustMessages.M.aggregate());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawOperatePanel() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PStatementAdjustDef.bizState);
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    reasonField = new RViewStringField();
    reasonField.setCaption(StatementAdjustMessages.M.reason());
    operateForm.addField(reasonField);

    createInfoField = new RSimpleOperateInfoField(PStatementAdjustDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PStatementAdjustDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    moreInfoField = new RHyperlink();
    moreInfoField.addClickHandler(new Handler_logAction());
    moreInfoField.setHTML(StatementAdjustMessages.M.moreInfo());
    RCombinedField moreField = new RCombinedField() {
      {
        addField(new HTML(), 0.1f);
        addField(moreInfoField, 0.9f);
      }
    };
    operateForm.addField(moreField);

    operateForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(StatementAdjustMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawMiddlePanel() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    panel.add(drawGrid());

    RCaptionBox box = new RCaptionBox();
    box.getCaptionBar().setShowCollapse(true);
    box.setContentSpacing(0);
    box.setCaption(StatementAdjustMessages.M.statementAdjust());
    box.setContent(panel);
    box.setWidth("100%");

    resultTotalHtml = new HTML(StatementAdjustMessages.M.resultTotal(0));
    box.getCaptionBar().addButton(resultTotalHtml);

    return box;
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setProvider(new GridDataProvider());
    grid.addClickHandler(new Handler_grid());

    lineNumberCol = new RGridColumnDef(PStatementAdjustLineDef.lineNumber);
    lineNumberCol.setWidth("20px");
    grid.addColumnDef(lineNumberCol);

    subjectCodeCol = new RGridColumnDef(PStatementAdjustLineDef.subject_code);
    subjectCodeCol.setRendererFactory(new RHyperlinkRendererFactory());
    subjectCodeCol.setWidth("50px");
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PStatementAdjustLineDef.subject_name);
    subjectNameCol.setWidth("80px");
    grid.addColumnDef(subjectNameCol);

    directionCol = new RGridColumnDef(PStatementAdjustLineDef.direction);
    directionCol.setWidth("50px");
    grid.addColumnDef(directionCol);

    totalCol = new RGridColumnDef(PStatementAdjustLineDef.amount_total);
    totalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    totalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    totalCol.setWidth("50px");
    grid.addColumnDef(totalCol);

    taxCol = new RGridColumnDef(PStatementAdjustLineDef.amount_tax);
    taxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    taxCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    taxCol.setWidth("50px");
    grid.addColumnDef(taxCol);

    taxRateCol = new RGridColumnDef(PStatementAdjustLineDef.taxRate);
    taxRateCol.setWidth("80px");
    grid.addColumnDef(taxRateCol);

    invoiceCol = new RGridColumnDef(PStatementAdjustLineDef.invoice);
    invoiceCol.setRendererFactory(new RBooleanRendererFactory());
    invoiceCol.setHorizontalAlign(HasAlignment.ALIGN_CENTER);
    invoiceCol.setWidth("70px");
    grid.addColumnDef(invoiceCol);

    accountDateCol = new RGridColumnDef(PStatementAdjustLineDef.accountDate);
    accountDateCol.setWidth("60px");
    grid.addColumnDef(accountDateCol);

    beginDateCol = new RGridColumnDef(PStatementAdjustLineDef.dateRange_beginDate);
    beginDateCol.setWidth("60px");
    grid.addColumnDef(beginDateCol);

    endDateCol = new RGridColumnDef(PStatementAdjustLineDef.dateRange_endDate);
    endDateCol.setWidth("60px");
    grid.addColumnDef(endDateCol);

    lastPayDateCol = new RGridColumnDef(PStatementAdjustLineDef.lastPayDate);
    lastPayDateCol.setWidth("60px");
    grid.addColumnDef(lastPayDateCol);

    remarkCol = new RGridColumnDef(PStatementAdjustLineDef.remark);
    remarkCol.setWidth("100px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);

    return grid;
  }

  private Widget drawRemarkPanel() {
    remarkField = new RTextArea(PStatementAdjustDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.setReadOnly(true);

    remarkBox = new RCaptionBox();
    remarkBox.setCaption(PStatementAdjustDef.constants.remark());
    remarkBox.setWidth("100%");
    remarkBox.setContent(remarkField);
    remarkBox.getCaptionBar().setShowCollapse(true);

    return remarkBox;
  }

  private boolean checkIn() {
    if (getEP().isProcessMode()) {
      return true;
    }

    if (getEP().isPermitted(StatementAdjustPermDef.READ) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshPrint() {
    if (printButton == null) {
      return;
    }
    if (entity != null) {
      printButton.clearParameters();
      printButton.setEnabled(getEP().isPermitted(BAction.PRINT.getKey()));
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

  private void refreshGeneral() {
    if (entity == null)
      return;

    billNumberField.setValue(entity.getBillNumber());
    statementNumberField.setValue(entity.getStatement() == null ? null : entity.getStatement()
        .getBillNumber());
    accountUnitField.setValue(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .toFriendlyStr());
    if (entity.getBcontract() == null) {
      contractBillNumberField.clearValue();
    } else {
      if (entity.getCounterpart() != null
          && BCounterpart.COUNPERPART_PROPRIETOR.equals(entity.getCounterpart()
              .getCounterpartType())) {
        contractBillNumberField.setValue(GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE, entity
            .getBcontract().getCode());
      } else {
        contractBillNumberField.setValue(GRes.FIELDNAME_CONTRACT_BILLTYPE, entity.getBcontract()
            .getCode());
      }
    }
    contractNameField.setValue(entity.getBcontract() == null ? null : entity.getBcontract()
        .getName());
    counterpartField.setValue(entity.getCounterpart()
        .toFriendlyStr(getEP().getCounterpartTypeMap()));
    settleNoField.setValue(entity.getSettleNo());
    if (entity.getReceiptTotal() != null && entity.getReceiptTax() != null) {
      receiptTotalField.setValue(entity.getReceiptTotal());
      receiptTaxField.setValue(entity.getReceiptTax());
    } else {
      receiptTotalField.clearValue();
      receiptTaxField.clearValue();
    }

    if (entity.getPaymentTotal() != null && entity.getPaymentTax() != null) {
      paymentTotalField.setValue(entity.getPaymentTotal());
      paymentTaxField.setValue(entity.getPaymentTax());
    } else {
      paymentTotalField.clearValue();
      paymentTaxField.clearValue();
    }

    bizStateField.setValue(PStatementAdjustDef.bizState.getEnumCaption(entity.getBizState()));
    if (entity.getBpmMessage() != null) {
      reasonField.setVisible(true);
      reasonField.setValue(entity.getBpmMessage());
    } else {
      reasonField.setVisible(false);
    }

    permGroupField.refresh(getEP().isPermEnabled(), entity);

    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());
    operateForm.rebuild();

    remarkField.setValue(entity.getRemark());
    remarkBox.expand();
  }

  private void refreshGrid() {
    resultTotalHtml.setText(StatementAdjustMessages.M.resultTotal(entity.getLines().size()));
    grid.refresh();
  }

  private class Handler_editAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      JumpParameters params = new JumpParameters(StatementAdjustEditPage.START_NODE);
      params.getUrlRef().set(StatementAdjustEditPage.PN_ENTITY_UUID, entity.getUuid());
      params.getUrlRef().set(StatementAdjustUrlParams.Base.SCENCE,
          StatementAdjustMessages.M.editScence());
      getEP().jump(params);
    }
  }

  private class Handler_effectAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();

      String msg = StatementAdjustMessages.M.actionComfirm2(StatementAdjustMessages.M.effect(),
          getEP().getModuleCaption(), entity.getBillNumber());
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        public void onClosed(boolean confirmed) {
          if (confirmed == false)
            return;
          doEffect();
        }
      });
    }
  }

  private void doEffect() {
    RLoadingDialog.show(StatementAdjustMessages.M.beDoing(StatementAdjustMessages.M.effect(),
        entity.getBillNumber()));
    StatementAdjustService.Locator.getService().effect(entity.getUuid(), null, entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementAdjustMessages.M.processError2(
                StatementAdjustMessages.M.effect(), getEP().getModuleCaption(),
                entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BStatementAdjustLogger.getInstance().log(StatementAdjustMessages.M.effect(), entity);

            String msg = StatementAdjustMessages.M.onSuccess(StatementAdjustMessages.M.effect(),
                getEP().getModuleCaption(), entity.getBillNumber());

            JumpParameters params = new JumpParameters(START_NODE);
            params.getUrlRef().set(PN_ENTITY_UUID, entity.getUuid());
            params.getMessages().add(Message.info(msg));
            getEP().jump(params);
          }
        });
  }

  private class Handler_deletection implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();
      String msg = StatementAdjustMessages.M.actionComfirm2(StatementAdjustMessages.M.delete(),
          getEP().getModuleCaption(), entity.getBillNumber());
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        public void onClosed(boolean confirmed) {
          if (confirmed == false)
            return;
          doDelete();
        }
      });
    }
  }

  private void doDelete() {
    RLoadingDialog.show(StatementAdjustMessages.M.actionDoing(StatementAdjustMessages.M.delete()));
    final String billNumber = entity.getBillNumber();
    StatementAdjustService.Locator.getService().delete(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementAdjustMessages.M.actionFailed(StatementAdjustMessages.M.delete(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            try {
              BStatementAdjustLogger.getInstance().log(StatementAdjustMessages.M.delete(), entity);

              JumpParameters params = StatementAdjustSearchPage.getInstance().getLastJumpParams();
              params.getMessages().add(
                  Message.info(StatementAdjustMessages.M.onSuccess(
                      StatementAdjustMessages.M.delete(), getEP().getModuleCaption(), billNumber)));
              getEP().jump(params);
            } catch (Exception e) {
            }
          }
        });
  }

  private class Handler_abortAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();
      InputBox.show(StatementAdjustMessages.M.abortReason(), null, true,
          PStatementAdjustDef.remark, new InputBox.Callback() {

            @Override
            public void onClosed(boolean ok, String text) {
              if (!ok)
                return;
              doAbort(ok, text);
            }
          });
    }
  }

  private void doAbort(boolean ok, String text) {
    RLoadingDialog.show(StatementAdjustMessages.M.actionDoing(StatementAdjustMessages.M.abort()));
    StatementAdjustService.Locator.getService().abort(entity.getUuid(), entity.getVersion(), ok,
        text, new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = StatementAdjustMessages.M.actionFailed(StatementAdjustMessages.M.abort(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BStatementAdjustLogger.getInstance().log(StatementAdjustMessages.M.abort(), entity);

            String msg = StatementAdjustMessages.M.onSuccess(StatementAdjustMessages.M.abort(),
                getEP().getModuleCaption(), entity.getBillNumber());
            getEP().jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  private class Handler_accDefrayalAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      try {
        GwtUrl url = AccountDefrayalUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, AccountDefrayalUrlParams.Search.START_NODE);
        url.getQuery().set(AccountDefrayalUrlParams.Search.KEY_SOURCEBILLNUMBER,
            entity.getBillNumber());
        url.getQuery().set(AccountDefrayalUrlParams.Search.KEY_SOURCEBILLTYPE,
            getEP().getAdjBillType().getName());
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
      } catch (Exception e) {
        String msg = StatementAdjustMessages.M.cannotNavigate(PAccountDefrayalDef.TABLE_CAPTION);
        RMsgBox.showError(msg, e);
      }
    }
  }

  private class Handler_logAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      JumpParameters params = new JumpParameters(StatementAdjustLogViewPage.START_NODE);
      params.getUrlRef().set(StatementAdjustLogViewPage.PN_ENTITY_UUID, entity.getUuid());
      params.getExtend().put(EPStatementAdjust.OPN_ENTITY, entity);
      getEP().jump(params);
    }
  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return entity == null ? 0 : entity.getLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (entity == null || entity.getLines().isEmpty())
        return null;

      BStatementAdjustLine line = entity.getLines().get(row);
      if (col == lineNumberCol.getIndex())
        return line.getLineNumber();
      else if (col == subjectCodeCol.getIndex())
        return line.getSubject().getCode();
      else if (col == subjectNameCol.getIndex())
        return line.getSubject().getName();
      else if (col == directionCol.getIndex())
        return DirectionType.getCaptionByValue(line.getDirection());
      else if (col == totalCol.getIndex())
        return Double.valueOf(line.getTotal().getTotal().doubleValue());
      else if (col == taxCol.getIndex())
        return Double.valueOf(line.getTotal().getTax().doubleValue());
      else if (col == taxRateCol.getIndex())
        return line.getTaxRate().toString();
      else if (col == invoiceCol.getIndex())
        return line.isInvoice();
      else if (col == accountDateCol.getIndex())
        return line.getAccountDate() == null ? null : M3Format.fmt_yMd
            .format(line.getAccountDate());
      else if (col == beginDateCol.getIndex())
        return line.getBeginDate() == null ? null : M3Format.fmt_yMd.format(line.getBeginDate());
      else if (col == endDateCol.getIndex())
        return line.getEndDate() == null ? null : M3Format.fmt_yMd.format(line.getEndDate());
      else if (col == lastPayDateCol.getIndex())
        return line.getLastPayDate() == null ? null : M3Format.fmt_yMd
            .format(line.getLastPayDate());
      else if (col == remarkCol.getIndex())
        return line.getRemark();
      else
        return "";
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

      BStatementAdjustLine line = entity.getLines().get(row);
      if (line == null)
        return;

      if (colDef.equals(subjectCodeCol)) {
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, line.getSubject().getUuid());
        try {
          RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
        } catch (Exception e) {
          String msg = StatementAdjustMessages.M.cannotNavigate(url.toString());
          RMsgBox.showError(msg, e);
        }
      }
    }
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected EPStatementAdjust getEP() {
    return EPStatementAdjust.getInstance();
  }

  @Override
  protected void refreshToolButtons() {
    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());

    editAction.setVisible(isInEffect && getEP().isPermitted(StatementAdjustPermDef.UPDATE));
    deleteAction.setVisible(isInEffect && getEP().isPermitted(StatementAdjustPermDef.DELETE));
    effectAction.setVisible(isInEffect && getEP().isPermitted(StatementAdjustPermDef.EFFECT));
    abortAction.setVisible(isEffect && getEP().isPermitted(StatementAdjustPermDef.ABORT));
    accDefrayalAction.setVisible(isEffect);

    getToolbar().minimizeSeparators();
  }

  @Override
  protected List<Widget> getRelateWidgets() {
    List<Widget> list = new ArrayList<Widget>();
    list.add(editAction);
    list.add(deleteAction);
    list.add(effectAction);
    list.add(abortAction);
    list.add(accDefrayalAction);
    return list;
  }

  private class ProcessDialog extends M3BaseOutgoingDialog {

    @Override
    protected void doExcute(BOperation operation, AsyncCallback<Object> callback) {
      final BProcessContext processCtx = getEP().getFormBuilder().getProcessCtx();
      StatementAdjustServiceAgent.executeTask(operation, entity, processCtx, false, this);
    }
  }
}
