/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentNoticeViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-14 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.paymentnotice.client.EPPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.PaymentNoticeMessages;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNoticeLine;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BPaymentNoticeLogger;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeLoader;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeService;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeServiceAgent;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.PaymentNoticeUrlParams.View;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.dd.PPaymentNoticeDef;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.dd.PPaymentNoticeLineDef;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.perm.PaymentNoticePermDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmViewPage;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.dialog.InputBox;
import com.hd123.m3.commons.gwt.widget.client.biz.PrintingTemplate;
import com.hd123.m3.commons.gwt.widget.client.ui.navi.DefaultNavigateHandler;
import com.hd123.m3.commons.gwt.widget.client.ui.navi.EntityNavigator;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupViewField;
import com.hd123.m3.commons.gwt.widget.client.ui.print.PrintButton;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
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
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class PaymentNoticeViewPage extends BaseBpmViewPage implements View {

  public static PaymentNoticeViewPage getInstance() {
    if (instance == null)
      instance = new PaymentNoticeViewPage();
    return instance;
  }

  public PaymentNoticeViewPage() {
    super();
    loader = new PaymentNoticeLoader();
    drawToolbar();
    drawSelf();
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

  private static PaymentNoticeViewPage instance;
  private BPaymentNotice entity;
  private PaymentNoticeLoader loader;
  private Handler_click clickHandler = new Handler_click();

  private RAction editAction;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;
  private EntityNavigator navigator;
  private PrintButton printButton;

  private RViewStringField billNumberField;
  private RViewStringField accountCenterField;
  private RViewStringField counterPartField;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField reasonField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RHyperlink moreInfoField;

  private PermGroupViewField permGroupField;

  private RViewNumberField receiptTotalField;
  private RViewNumberField receiptTaxField;
  private RViewNumberField receiptIvcTotalField;
  private RViewNumberField paymentTotalField;
  private RViewNumberField paymentTaxField;
  private RViewNumberField paymentIvcTotalField;

  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef statementNumCol;
  private RGridColumnDef contractNumCol;
  private RGridColumnDef contractNameCol;
  private RGridColumnDef receiptTotalCol;
  private RGridColumnDef receiptTaxCol;
  private RGridColumnDef receiptIvcTotalCol;
  private RGridColumnDef payTotalCol;
  private RGridColumnDef payTaxCol;
  private RGridColumnDef payIvcTotalCol;
  private RGridColumnDef remarkCol;
  private HTML resultTotalHtml;

  private RCaptionBox remarkBox;
  private RTextArea remarkField;

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());

    editAction = new RAction(RActionFacade.EDIT, clickHandler);
    getToolbar().add(new RToolbarButton(editAction));

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    getToolbar().add(new RToolbarButton(deleteAction));

    getToolbar().addSeparator();

    injectBpmActions();

    getToolbar().addSeparator();

    effectAction = new RAction(PaymentNoticeMessages.M.effect(), clickHandler);
    getToolbar().add(new RToolbarButton(effectAction));

    abortAction = new RAction(PaymentNoticeMessages.M.abort() + PaymentNoticeMessages.M.doing(),
        clickHandler);
    getToolbar().add(new RToolbarButton(abortAction));

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
    mvp.add(0, drawAggregatePanel());
    mvp.add(1, drawOperatePanel());

    permGroupField = new PermGroupViewField();
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PaymentNoticeMessages.M.generalInfo());
    box.setWidth("100%");
    box.setContent(vp);

    return box;
  }

  private Widget drawBasicInfo() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    billNumberField = new RViewStringField(PPaymentNoticeDef.billNumber);
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    form.addField(billNumberField);

    accountCenterField = new RViewStringField(PPaymentNoticeDef.accountUnit);
    accountCenterField.setCaption(getEP().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    form.addField(accountCenterField);

    counterPartField = new RViewStringField(PPaymentNoticeDef.counterpart);
    counterPartField.setCaption(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    form.addField(counterPartField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(PaymentNoticeMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawAggregatePanel() {
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
        setCaption(PPaymentNoticeDef.constants.receiptTotal_total() + "/"
            + PPaymentNoticeDef.constants.receiptTotal_tax());
        addField(receiptTotalField, 0.5f);
        addField(receiptTaxField, 0.5f);
      }
    });

    receiptIvcTotalField = new RViewNumberField();
    receiptIvcTotalField.setFormat(M3Format.fmt_money);
    receiptIvcTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(new RCombinedField() {
      {
        setCaption(PPaymentNoticeDef.constants.receiptIvcTotal_total());
        addField(receiptIvcTotalField, 0.5f);
        addField(new HTML(), 0.5f);
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
        setCaption(PPaymentNoticeDef.constants.paymentTotal_total() + "/"
            + PPaymentNoticeDef.constants.paymentTotal_tax());
        addField(paymentTotalField, 0.5f);
        addField(paymentTaxField, 0.5f);
      }
    });

    paymentIvcTotalField = new RViewNumberField();
    paymentIvcTotalField.setFormat(M3Format.fmt_money);
    paymentIvcTotalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(new RCombinedField() {
      {
        setCaption(PPaymentNoticeDef.constants.paymentIvcTotal_total());
        addField(paymentIvcTotalField, 0.5f);
        addField(new HTML(), 0.5f);
      }
    });

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(PaymentNoticeMessages.M.aggregate());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawOperatePanel() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PPaymentNoticeDef.bizState);
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    reasonField = new RViewStringField();
    reasonField.setCaption(PaymentNoticeMessages.M.reason());
    operateForm.addField(reasonField);

    createInfoField = new RSimpleOperateInfoField(PPaymentNoticeDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PPaymentNoticeDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    moreInfoField = new RHyperlink();
    moreInfoField.addClickHandler(clickHandler);
    moreInfoField.setHTML(PaymentNoticeMessages.M.moreInfo());
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
    box.setCaption(PaymentNoticeMessages.M.operateInfo());
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
    box.setCaption(PaymentNoticeMessages.M.statementLine());
    box.setContent(panel);
    box.setWidth("100%");

    resultTotalHtml = new HTML(PaymentNoticeMessages.M.resultTotal(0));
    box.getCaptionBar().addButton(resultTotalHtml);

    return box;
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setProvider(new GridDataProvider());

    lineNumberCol = new RGridColumnDef(PPaymentNoticeLineDef.lineNumber);
    lineNumberCol.setWidth("40px");
    grid.addColumnDef(lineNumberCol);

    statementNumCol = new RGridColumnDef(PPaymentNoticeLineDef.statement_billNumber);
    statementNumCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    statementNumCol.setWidth("160px");
    grid.addColumnDef(statementNumCol);

    contractNumCol = new RGridColumnDef(PPaymentNoticeLineDef.contract_code);
    contractNumCol.setWidth("160px");
    grid.addColumnDef(contractNumCol);

    contractNameCol = new RGridColumnDef(PPaymentNoticeLineDef.contract_name);
    contractNameCol.setWidth("160px");
    grid.addColumnDef(contractNameCol);

    receiptTotalCol = new RGridColumnDef(PPaymentNoticeLineDef.receiptTotal_total);
    receiptTotalCol.setWidth("70px");
    receiptTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    receiptTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(receiptTotalCol);

    receiptTaxCol = new RGridColumnDef(PPaymentNoticeLineDef.receiptTotal_tax);
    receiptTaxCol.setWidth("70px");
    receiptTaxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    receiptTaxCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(receiptTaxCol);

    receiptIvcTotalCol = new RGridColumnDef(PPaymentNoticeLineDef.receiptIvcTotal_total);
    receiptIvcTotalCol.setWidth("90px");
    receiptIvcTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    receiptIvcTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(receiptIvcTotalCol);

    payTotalCol = new RGridColumnDef(PPaymentNoticeLineDef.paymentTotal_total);
    payTotalCol.setWidth("70px");
    payTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    payTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(payTotalCol);

    payTaxCol = new RGridColumnDef(PPaymentNoticeLineDef.paymentTotal_tax);
    payTaxCol.setWidth("70px");
    payTaxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    payTaxCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(payTaxCol);

    payIvcTotalCol = new RGridColumnDef(PPaymentNoticeLineDef.paymentIvcTotal_total);
    payIvcTotalCol.setWidth("90px");
    payIvcTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    payIvcTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    grid.addColumnDef(payIvcTotalCol);

    remarkCol = new RGridColumnDef(PPaymentNoticeLineDef.remark);
    remarkCol.setWidth("100px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);

    return grid;

  }

  private Widget drawRemarkPanel() {
    remarkField = new RTextArea(PPaymentNoticeDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.setReadOnly(true);

    remarkBox = new RCaptionBox();
    remarkBox.setCaption(PPaymentNoticeDef.constants.remark());
    remarkBox.setWidth("100%");
    remarkBox.setContent(remarkField);
    remarkBox.getCaptionBar().setShowCollapse(true);

    return remarkBox;
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;

    if (getEP().isPermitted(PaymentNoticePermDef.READ) == false) {
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
    accountCenterField.setValue(entity.getAccountUnit() != null ? entity.getAccountUnit()
        .getNameCode() : null);
    counterPartField.setValue(entity.getCounterpart() == null ? null : entity.getCounterpart()
        .toFriendlyStr(getEP().getCounterpartTypeMap()));
    receiptTotalField.setValue(entity.getReceiptTotal());
    receiptTaxField.setValue(entity.getReceiptTax());
    receiptIvcTotalField.setValue(entity.getReceiptIvcTotal());
    paymentTotalField.setValue(entity.getPaymentTotal());
    paymentTaxField.setValue(entity.getPaymentTax());
    paymentIvcTotalField.setValue(entity.getPaymentIvcTotal());

    bizStateField.setValue(PPaymentNoticeDef.bizState.getEnumCaption(entity.getBizState()));
    if (entity.getBpmMessage() != null) {
      reasonField.setVisible(true);
      reasonField.setValue(entity.getBpmMessage());
    } else {
      reasonField.setVisible(false);
    }

    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());
    operateForm.rebuild();

    permGroupField.refresh(getEP().isPermEnabled(), entity);

    remarkField.setValue(entity.getRemark());
    remarkBox.expand();

    getToolbar().minimizeSeparators();
  }

  private void refreshGrid() {
    resultTotalHtml.setText(PaymentNoticeMessages.M.resultTotal(entity.getLines().size()));
    grid.refresh();
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == editAction) {
        doEdit();
      } else if (event.getSource() == deleteAction) {
        doConfirmDelete();
      } else if (event.getSource() == effectAction) {
        doConfirmEffect();
      } else if (event.getSource() == abortAction) {
        doConfirmAbort();
      } else if (event.getSource() == moreInfoField) {
        doViewLog();
      }
    }
  }

  private void doEdit() {
    JumpParameters params = new JumpParameters(PaymentNoticeEditPage.START_NODE);
    params.getUrlRef().set(PaymentNoticeEditPage.PN_ENTITY_UUID, entity.getUuid());
    params.getExtend().put(EPPaymentNotice.OPN_ENTITY, entity);
    getEP().jump(params);
  }

  private void doConfirmDelete() {
    getMessagePanel().clearMessages();
    String msg = PaymentNoticeMessages.M.actionComfirm2(PaymentNoticeMessages.M.delete(), getEP()
        .getModuleCaption(), entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (!confirmed)
          return;
        doDelete();
      }
    });
  }

  private void doDelete() {
    RLoadingDialog.show(PaymentNoticeMessages.M.actionDoing(PaymentNoticeMessages.M.delete()));
    PaymentNoticeService.Locator.getService().delete(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentNoticeMessages.M.actionFailed(PaymentNoticeMessages.M.delete(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BPaymentNoticeLogger.getInstance().log(PaymentNoticeMessages.M.delete(), entity);

            JumpParameters params = PaymentNoticeSearchPage.getInstance().getLastParams();
            params.getMessages().add(
                Message.info(PaymentNoticeMessages.M.onSuccess(PaymentNoticeMessages.M.delete(),
                    getEP().getModuleCaption(), entity.getBillNumber())));
            getEP().jump(params);
          }
        });
  }

  private void doConfirmEffect() {
    getMessagePanel().clearMessages();

    String msg = PaymentNoticeMessages.M.actionComfirm2(PaymentNoticeMessages.M.effect(), getEP()
        .getModuleCaption(), entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEffect();
      }
    });
  }

  private void doEffect() {
    RLoadingDialog.show(PaymentNoticeMessages.M.actionDoing(PaymentNoticeMessages.M.effect()));
    PaymentNoticeService.Locator.getService().effect(entity.getUuid(), null, entity.getVersion(),
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentNoticeMessages.M.actionFailed(PaymentNoticeMessages.M.effect(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BPaymentNoticeLogger.getInstance().log(PaymentNoticeMessages.M.effect(), entity);

            String msg = PaymentNoticeMessages.M.onSuccess(PaymentNoticeMessages.M.effect(),
                getEP().getModuleCaption(), entity.getBillNumber());

            JumpParameters params = new JumpParameters(START_NODE);
            params.getUrlRef().set(PN_ENTITY_UUID, entity.getUuid());
            params.getMessages().add(Message.info(msg));
            getEP().jump(params);
          }
        });
  }

  private void doConfirmAbort() {
    getMessagePanel().clearMessages();

    InputBox.show(PaymentNoticeMessages.M.abortReason(), null, true,
        PPaymentNoticeDef.latestComment, new InputBox.Callback() {
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            doAbort(text);
          }
        });
  }

  private void doAbort(String text) {
    RLoadingDialog.show(PaymentNoticeMessages.M.actionDoing(PaymentNoticeMessages.M.abort()));
    PaymentNoticeService.Locator.getService().abort(entity.getUuid(), entity.getVersion(), text,
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = PaymentNoticeMessages.M.actionFailed(PaymentNoticeMessages.M.abort(),
                getEP().getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BPaymentNoticeLogger.getInstance().log(PaymentNoticeMessages.M.abort(), entity);

            String msg = PaymentNoticeMessages.M.onSuccess(PaymentNoticeMessages.M.abort(), getEP()
                .getModuleCaption(), entity.getBillNumber());

            JumpParameters params = new JumpParameters(START_NODE);
            params.getUrlRef().set(PN_ENTITY_UUID, entity.getUuid());
            params.getMessages().add(Message.info(msg));
            getEP().jump(params);
          }
        });
  }

  private void doViewLog() {
    JumpParameters params = new JumpParameters(PaymentNoticeLogPage.START_NODE);
    params.getUrlRef().set(PaymentNoticeLogPage.PN_ENTITY_UUID, entity.getUuid());
    params.getExtend().put(EPPaymentNotice.OPN_ENTITY, entity);
    getEP().jump(params);
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

      BPaymentNoticeLine rowData = entity.getLines().get(row);
      if (col == lineNumberCol.getIndex())
        return rowData.getLineNumber();
      else if (col == statementNumCol.getIndex()) {
        BDispatch dispatch = new BDispatch("statement");
        dispatch.addParams(GRes.R.dispatch_key(), rowData.getStatement().getBillNumber());
        return dispatch;
      } else if (col == contractNumCol.getIndex()) {
        return rowData.getContract() == null ? null : rowData.getContract().getCode();
      } else if (col == contractNameCol.getIndex()) {
        return rowData.getContract() == null ? null : rowData.getContract().getName();
      } else if (col == receiptTotalCol.getIndex())
        return rowData.getReceiptTotal() == null ? BigDecimal.ZERO : rowData.getReceiptTotal()
            .doubleValue();
      else if (col == receiptTaxCol.getIndex())
        return rowData.getReceiptTax() == null ? BigDecimal.ZERO : rowData.getReceiptTax()
            .doubleValue();
      else if (col == receiptIvcTotalCol.getIndex())
        return rowData.getReceiptIvcTotal() == null ? BigDecimal.ZERO : rowData
            .getReceiptIvcTotal().doubleValue();
      else if (col == payTotalCol.getIndex())
        return rowData.getPaymentTotal() == null ? BigDecimal.ZERO : rowData.getPaymentTotal()
            .doubleValue();
      else if (col == payTaxCol.getIndex())
        return rowData.getPaymentTax() == null ? BigDecimal.ZERO : rowData.getPaymentTax()
            .doubleValue();
      else if (col == payIvcTotalCol.getIndex())
        return rowData.getPaymentIvcTotal() == null ? BigDecimal.ZERO : rowData
            .getPaymentIvcTotal().doubleValue();
      else if (col == remarkCol.getIndex())
        return rowData.getRemark();
      return null;
    }
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected EPPaymentNotice getEP() {
    return EPPaymentNotice.getInstance();
  }

  @Override
  protected void refreshToolButtons() {
    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());

    editAction.setVisible(isInEffect && getEP().isPermitted(PaymentNoticePermDef.UPDATE));
    deleteAction.setVisible(isInEffect && getEP().isPermitted(PaymentNoticePermDef.DELETE));
    effectAction.setVisible(isInEffect && getEP().isPermitted(PaymentNoticePermDef.EFFECT));
    abortAction.setVisible(isEffect && getEP().isPermitted(PaymentNoticePermDef.ABORT));
  }

  @Override
  protected List<Widget> getRelateWidgets() {
    List<Widget> list = new ArrayList<Widget>();
    list.add(editAction);
    list.add(deleteAction);
    list.add(effectAction);
    list.add(abortAction);
    return list;
  }

  private class ProcessDialog extends M3BaseOutgoingDialog {

    @Override
    protected void doExcute(BOperation operation, AsyncCallback<Object> callback) {
      final BProcessContext processCtx = getEP().getFormBuilder().getProcessCtx();
      PaymentNoticeServiceAgent.executeTask(operation, entity, processCtx, false, this);
    }
  }
}
