/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeViewPage.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.fee.client.EPFee;
import com.hd123.m3.account.gwt.fee.client.FeeMessages;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeLogger;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeLoader;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeService;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeServiceAgent;
import com.hd123.m3.account.gwt.fee.client.ui.gadget.FeeLineViewGrid;
import com.hd123.m3.account.gwt.fee.intf.client.FeeUrlParams;
import com.hd123.m3.account.gwt.fee.intf.client.FeeUrlParams.View;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeDef;
import com.hd123.m3.account.gwt.fee.intf.client.perm.FeePermDef;
import com.hd123.m3.account.gwt.payment.rec.client.ui.page.ReceiptCreatePage;
import com.hd123.m3.account.gwt.payment.rec.intf.client.ReceiptUrlParams;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.AccountDefrayalUrlParams;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
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
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RViewDateField;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
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
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author subinzhu
 * 
 */
public class FeeViewPage extends BaseBpmViewPage implements View {
  private static FeeViewPage instance = null;

  public static FeeViewPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new FeeViewPage();
    return instance;
  }

  public FeeViewPage() throws ClientBizException {
    super();
    entityLoader = new FeeLoader();
    drawToolbar();
    drawSelf();
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
  }

  private BFee entity;

  private FeeLoader entityLoader;

  private RAction editAction;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;
  private RAction viewSubjectRAndPInfoAction;
  private RAction quickReceiveAction;

  private EntityNavigator navigator;
  private PrintButton printButton;

  private RViewStringField billNumberField;
  private RViewStringField accountUnitField;
  private DispatchLinkField contractField;
  private RViewStringField contractNameField;
  private RViewStringField counterpartField;
  private RViewDateField accountDateField;
  private RViewDateField beginDateField;
  private RViewDateField endDateField;
  private RViewStringField directionTypeField;
  private HTML captionHtml;
  private RViewStringField generateStatementField;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField causeField;
  private RSimpleOperateInfoField createInfo;
  private RSimpleOperateInfoField lastModifyInfo;
  private RHyperlink moreInfo;

  private PermGroupViewField permGroupField;

  private RViewNumberField totalField;
  private RViewNumberField taxField;

  private FeeLineViewGrid grid;

  private RTextArea remarkField;

  private Handler_click clickHandler = new Handler_click();

  @Override
  public void onShow(final JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    entityLoader.decodeParams(params, new Command() {
      public void execute() {
        entity = entityLoader.getEntity();
        refreshBpm(entity);
        refreshToolbar();
        refreshEntity();
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

  private void refreshEntity() {
    entity.initialize();

    refreshGeneral();
    refreshOperateInfo();
    refreshAuthorize();
    refreshGrid();
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;

    if (getEP().isPermitted(FeePermDef.READ) == false) {
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
    assert entity != null;
    billNumberField.setValue(entity.getBillNumber());
    accountUnitField.setValue(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .toFriendlyStr());
    if (entity.getContract() == null) {
      contractField.setValue(entity.getContract() == null ? null : entity.getContract()
          .getBillNumber());
    } else {
      if (entity.getCounterpart() != null
          && BCounterpart.COUNPERPART_PROPRIETOR.equals(entity.getCounterpart()
              .getCounterpartType())) {
        contractField.setValue(GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE, entity.getContract()
            .getCode());
      } else {
        contractField.setValue(GRes.FIELDNAME_CONTRACT_BILLTYPE, entity.getContract().getCode());
      }
    }
    contractNameField
        .setValue(entity.getContract() == null ? null : entity.getContract().getName());
    counterpartField.setValue(entity.getCounterpart() == null ? null : entity.getCounterpart()
        .toFriendlyStr(getEP().getCounterpartTypeMap()));
    accountDateField.setValue(entity.getAccountDate());
    directionTypeField.setValue(DirectionType.getCaptionByValue(entity.getDirection()));
    generateStatementField.setValue(entity.isGenerateStatement() ? FeeMessages.M.yes()
        : FeeMessages.M.no());

    beginDateField.setValue(entity.getDateRange() == null ? null : entity.getDateRange()
        .getBeginDate());
    endDateField
        .setValue(entity.getDateRange() == null ? null : entity.getDateRange().getEndDate());
    refreshTotal();
    remarkField.setValue(entity.getRemark());
  }

  private void refreshTotal() {
    totalField.setValue(entity.getTotal().getTotal());
    taxField.setValue(entity.getTotal().getTax());
  }

  private void refreshOperateInfo() {
    bizStateField.setValue(PFeeDef.bizState.getEnumCaption(entity.getBizState()));
    causeField.setValue(entity.getBpmMessage());
    causeField.setVisible(!StringUtil.isNullOrBlank(entity.getBpmMessage()));
    createInfo.setOperateInfo(entity.getCreateInfo());
    lastModifyInfo.setOperateInfo(entity.getLastModifyInfo());
    operateForm.rebuild();
  }

  private void refreshAuthorize() {
    permGroupField.refresh(getEP().isPermEnabled(), entity);
  }

  private void refreshGrid() {
    grid.setBill(entity);
    grid.refresh();
  }

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());

    editAction = new RAction(RActionFacade.EDIT, clickHandler);
    getToolbar().add(new RToolbarButton(editAction));

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    getToolbar().add(new RToolbarButton(deleteAction));

    getToolbar().addSeparator();

    injectBpmActions();

    getToolbar().addSeparator();

    effectAction = new RAction(FeeMessages.M.effect(), clickHandler);
    getToolbar().add(new RToolbarButton(effectAction));

    abortAction = new RAction(FeeMessages.M.abort() + "...", clickHandler);
    getToolbar().add(new RToolbarButton(abortAction));

    getToolbar().addSeparator();

    viewSubjectRAndPInfoAction = new RAction(FeeMessages.M.subjectRAndPInfo(), clickHandler);
    getToolbar().add(new RToolbarButton(viewSubjectRAndPInfoAction));

    quickReceiveAction = new RAction(FeeMessages.M.quickReceive(), clickHandler);
    getToolbar().add(new RToolbarButton(quickReceiveAction));

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

    panel.add(drawTop());
    panel.add(drawGrid());
    panel.add(drawRemark());
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
    mvp.add(0, drawAggregate());
    mvp.add(1, drawOperate());

    permGroupField = new PermGroupViewField();
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(CommonsMessages.M.generalInfo());
    box.setWidth("100%");
    box.setContent(vp);

    return box;
  }

  private Widget drawBasic() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    billNumberField = new RViewStringField(PFeeDef.billNumber);
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    form.addField(billNumberField);

    accountUnitField = new RViewStringField(EPFee.getInstance().getFieldCaption(
        GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    form.addField(accountUnitField);

    contractField = new DispatchLinkField(PFeeDef.constants.contract_code());
    contractField.setLinkKey(GRes.R.dispatch_key());
    form.addField(contractField);

    contractNameField = new RViewStringField(PFeeDef.constants.contract_name());
    form.addField(contractNameField);

    counterpartField = new RViewStringField(EPFee.getInstance().getFieldCaption(
        GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
    form.addField(counterpartField);

    accountDateField = new RViewDateField();
    accountDateField.setFieldCaption(PFeeDef.accountDate.getCaption());
    form.addField(accountDateField);

    beginDateField = new RViewDateField();
    beginDateField.setFieldCaption(PFeeDef.dateRange_beginDate.getCaption());
    form.addField(beginDateField);

    endDateField = new RViewDateField();
    endDateField.setFieldCaption(PFeeDef.dateRange_endDate.getCaption());
    form.addField(endDateField);

    directionTypeField = new RViewStringField(PFeeDef.constants.direction());

    captionHtml = new HTML();
    captionHtml.setHTML(PFeeDef.constants.generateStatement());
    captionHtml.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    generateStatementField = new RViewStringField();
    RCombinedField combinedField = new RCombinedField() {
      {
        addField(directionTypeField, 0.1f);
        addField(captionHtml, 0.4f);
        addField(generateStatementField, 0.5f);
      }
    };

    combinedField.setCaption(directionTypeField.getCaption());
    form.addField(combinedField, true);

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(CommonsMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawAggregate() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    totalField = new RViewNumberField();
    totalField.setFormat(M3Format.fmt_money);
    totalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    taxField = new RViewNumberField();
    taxField.setFormat(M3Format.fmt_money);
    taxField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

    RCombinedField combinField = new RCombinedField() {
      {
        setCaption(PFeeDef.constants.total_total() + "/" + PFeeDef.constants.total_tax());
        addField(totalField, 0.5f);
        addField(taxField, 0.5f);
      }
    };

    form.add(combinField);

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FeeMessages.M.aggregate());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawOperate() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PFeeDef.bizState);
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    causeField = new RViewStringField(PFeeDef.constants.latestComment());
    operateForm.addField(causeField);

    createInfo = new RSimpleOperateInfoField(PFeeDef.constants.createInfo());
    operateForm.addField(createInfo);

    lastModifyInfo = new RSimpleOperateInfoField(PFeeDef.constants.lastModifyInfo());
    operateForm.addField(lastModifyInfo);

    moreInfo = new RHyperlink();
    moreInfo.addClickHandler(clickHandler);
    moreInfo.setHTML(FeeMessages.M.moreInfo());
    operateForm.addField(moreInfo);

    operateForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(FeeMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawGrid() {
    grid = new FeeLineViewGrid();
    return grid;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PFeeDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.setReadOnly(true);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PFeeDef.constants.remark());
    box.setWidth("100%");
    box.setContent(remarkField);
    box.getCaptionBar().setShowCollapse(true);

    return box;
  }

  private class Handler_click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource().equals(editAction)) {
        doEdit();
      } else if (event.getSource().equals(deleteAction)) {
        doConfirmDelete();
      } else if (event.getSource().equals(effectAction)) {
        doConfirmEffect();
      } else if (event.getSource().equals(abortAction)) {
        doConfirmAbort();
      } else if (event.getSource().equals(viewSubjectRAndPInfoAction)) {
        doViewSubjectRAndP();
      } else if (event.getSource().equals(moreInfo)) {
        doViewLog();
      } else if (event.getSource().equals(quickReceiveAction)) {
        doQuickReceive();
      }
    }

    private void doEdit() {
      JumpParameters params = new JumpParameters(FeeEditPage.START_NODE);
      params.getUrlRef().set(FeeEditPage.PN_UUID, entity.getUuid());
      getEP().jump(params);
    }

    private void doConfirmDelete() {
      getMessagePanel().clearMessages();

      String msg = FeeMessages.M.actionComfirm2(FeeMessages.M.delete(), PFeeDef.TABLE_CAPTION,
          entity.getBillNumber());
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        public void onClosed(boolean confirmed) {
          if (!confirmed)
            return;
          doDelete();
        }
      });
    }

    private void doDelete() {
      RLoadingDialog.show(FeeMessages.M.beDoing(FeeMessages.M.delete(), entity.getBillNumber()));
      FeeService.Locator.getService().delete(entity.getUuid(), entity.getVersion(),
          new RBAsyncCallback2() {

            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = FeeMessages.M.processError2(FeeMessages.M.delete(),
                  PFeeDef.TABLE_CAPTION, entity.getBillNumber());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(Object result) {
              RLoadingDialog.hide();
              // 记录日志
              BFeeLogger.getInstance().log(FeeMessages.M.delete(), entity);
              JumpParameters params = FeeSearchPage.getInstance().getLastParams();
              params.getMessages().add(
                  Message.info(FeeMessages.M.onSuccess(FeeMessages.M.delete(),
                      PFeeDef.TABLE_CAPTION, entity.getBillNumber())));
              getEP().jump(params);
            }
          });
    }

    private void doConfirmEffect() {
      getMessagePanel().clearMessages();

      String msg = FeeMessages.M.actionComfirm2(FeeMessages.M.effect(), PFeeDef.TABLE_CAPTION,
          entity.getBillNumber());
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        public void onClosed(boolean confirmed) {
          if (confirmed == false)
            return;
          doEffect();
        }
      });
    }

    private void doEffect() {
      RLoadingDialog.show(FeeMessages.M.beDoing(FeeMessages.M.effect(), entity.getBillNumber()));
      FeeService.Locator.getService().effect(entity.getUuid(), null, entity.getVersion(),
          new RBAsyncCallback2() {

            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = FeeMessages.M.processError2(FeeMessages.M.effect(),
                  PFeeDef.TABLE_CAPTION, entity.getBillNumber());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(Object result) {
              RLoadingDialog.hide();
              // 记录日志
              BFeeLogger.getInstance().log(FeeMessages.M.effect(), entity);
              String msg = FeeMessages.M.onSuccess(FeeMessages.M.effect(), PFeeDef.TABLE_CAPTION,
                  entity.getBillNumber());

              getEP().jumpToViewPage(entity.getUuid(), Message.info(msg));
            }
          });
    }

    private void doConfirmAbort() {
      getMessagePanel().clearMessages();

      InputBox.show(FeeMessages.M.abortReason(), null, true, PFeeDef.latestComment,
          new InputBox.Callback() {
            public void onClosed(boolean ok, String text) {
              if (ok == false)
                return;
              doAbort(text);
            }
          });
    }

    private void doAbort(String comment) {
      RLoadingDialog.show(FeeMessages.M.beDoing(FeeMessages.M.abort(), entity.getBillNumber()));
      FeeService.Locator.getService().abort(entity.getUuid(), comment, entity.getVersion(),
          new RBAsyncCallback2() {

            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              String msg = FeeMessages.M.processError2(FeeMessages.M.abort(),
                  PFeeDef.TABLE_CAPTION, entity.getBillNumber());
              RMsgBox.showError(msg, caught);
            }

            @Override
            public void onSuccess(Object result) {
              RLoadingDialog.hide();
              // 记录日志
              BFeeLogger.getInstance().log(FeeMessages.M.abort(), entity);
              String msg = FeeMessages.M.onSuccess(FeeMessages.M.abort(), PFeeDef.TABLE_CAPTION,
                  entity.getBillNumber());

              JumpParameters params = new JumpParameters(START_NODE);
              params.getUrlRef().set(PN_UUID, entity.getUuid());
              params.getMessages().add(Message.info(msg));
              getEP().jump(params);
            }
          });
    }

    private void doViewSubjectRAndP() {
      GwtUrl url = AccountDefrayalUrlParams.ENTRY_URL;
      url.getRef().set(JumpParameters.PN_START, AccountDefrayalUrlParams.Search.START_NODE);
      url.getRef().set(AccountDefrayalUrlParams.Search.KEY_SOURCEBILLTYPE, "fee");
      url.getRef()
          .set(AccountDefrayalUrlParams.Search.KEY_SOURCEBILLNUMBER, entity.getBillNumber());
      try {
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
      } catch (Exception e) {
        String msg = "无法导航到指定模块(" + url + ")。";
        RMsgBox.showError(msg, e);
      }
    }

    private void doViewLog() {
      JumpParameters params = new JumpParameters(FeeLogPage.START_NODE);
      params.getUrlRef().set(FeeUrlParams.Log.PN_UUID, entity.getUuid());
      getEP().jump(params);
    }

    private void doQuickReceive() {
      GwtUrl url = ReceiptUrlParams.ENTRY_URL;
      url.getRef().set(JumpParameters.PN_START, ReceiptUrlParams.Create.START_NODE);
      url.getRef().set(ReceiptCreatePage.PN_CREATEBYSOURCEBILL, entity.getUuid());
      try {
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_BYMODULE);
      } catch (Exception e) {
        String msg = "无法导航到指定模块(" + url + ")。";
        RMsgBox.showError(msg, e);
      }
    }
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected EPFee getEP() {
    return EPFee.getInstance();
  }

  @Override
  protected void refreshToolButtons() {
    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());

    boolean canEdit = getEP().isPermitted(FeePermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(FeePermDef.DELETE);
    boolean canEffect = getEP().isPermitted(FeePermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(FeePermDef.ABORT);
    boolean canQuick = getEP().isPermitted(ReceiptPermDef.CREATE);

    editAction.setVisible(isInEffect && canEdit);
    deleteAction.setVisible(isInEffect && canDelete);
    effectAction.setVisible(isInEffect && canEffect);
    abortAction.setVisible(isEffect && canAbort);
    viewSubjectRAndPInfoAction.setVisible(isEffect && canEffect);
    quickReceiveAction.setVisible(isEffect && canQuick && entity.isHasAccounts());

    getToolbar().minimizeSeparators();
  }

  @Override
  protected List<Widget> getRelateWidgets() {
    List<Widget> list = new ArrayList<Widget>();
    list.add(editAction);
    list.add(deleteAction);
    list.add(effectAction);
    list.add(abortAction);
    list.add(viewSubjectRAndPInfoAction);
    return list;
  }

  private class ProcessDialog extends M3BaseOutgoingDialog {

    @Override
    protected void doExcute(BOperation operation, AsyncCallback<Object> callback) {
      final BProcessContext processCtx = getEP().getFormBuilder().getProcessCtx();
      FeeServiceAgent.executeTask(operation, entity, processCtx, false, this);
    }
  }
}
