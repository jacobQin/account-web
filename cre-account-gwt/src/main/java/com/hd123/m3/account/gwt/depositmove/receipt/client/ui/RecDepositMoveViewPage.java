/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositMoveViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.receipt.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.bpm.widget.interaction.client.ui.navi.BaseOutgoingDialog;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMove;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BDepositMoveLogger;
import com.hd123.m3.account.gwt.depositmove.intf.client.DepositMoveMessage;
import com.hd123.m3.account.gwt.depositmove.intf.client.dd.PDepositMoveDef;
import com.hd123.m3.account.gwt.depositmove.receipt.client.EPRecDepositMove;
import com.hd123.m3.account.gwt.depositmove.receipt.client.rpc.RecDepositMoveLoader;
import com.hd123.m3.account.gwt.depositmove.receipt.client.rpc.RecDepositMoveService;
import com.hd123.m3.account.gwt.depositmove.receipt.client.rpc.RecDepositMoveServiceAgent;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.RecDepositMoveUrlParams;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.RecDepositMoveUrlParams.View;
import com.hd123.m3.account.gwt.depositmove.receipt.intf.client.perm.RecDepositMovePermDef;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmViewPage;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchLinkField;
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
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class RecDepositMoveViewPage extends BaseBpmViewPage implements View {
  private static EPRecDepositMove ep = EPRecDepositMove.getInstance();
  private static RecDepositMoveViewPage instance = null;

  public static RecDepositMoveViewPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new RecDepositMoveViewPage();
    return instance;
  }

  public RecDepositMoveViewPage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(DepositMoveMessage.M.cannotCreatePage("RecDepositMoveViewPage"),
          e);
    }
  }

  private BDepositMove entity;

  private RAction editAction;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;

  private EntityNavigator navigator;
  private PrintButton printButton;

  private RViewStringField billNumberField;

  private RForm outInfoForm;
  private RViewStringField outAccountUnitField;
  private RViewStringField outCounterpartField;
  private DispatchLinkField outContrarctField;
  private RViewStringField outContractNameField;
  private RHyperlinkField outSubjectField;
  private RViewNumberField totalField;
  private RCombinedField totalComField;
  private RViewNumberField amountField;
  private RViewNumberField outBalanceField;
  private RViewDateField accountDateField;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField reasonField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RHyperlink moreInfoField;

  private PermGroupViewField permGroupField;

  private RForm inInfoForm;
  private RViewStringField inAccountUnitField;
  private RViewStringField inCounterpartField;
  private DispatchLinkField inContrarctField;
  private RViewStringField inContractNameField;
  private RHyperlinkField inSubjectField;
  private RViewNumberField inBalanceField;

  private RTextArea remarkField;

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());

    editAction = new RAction(RActionFacade.EDIT, new Handler_editAction());
    getToolbar().add(new RToolbarButton(editAction));

    deleteAction = new RAction(RActionFacade.DELETE, new Handler_deleteAction());
    getToolbar().add(new RToolbarButton(deleteAction));

    getToolbar().addSeparator();

    injectBpmActions();

    getToolbar().addSeparator();

    effectAction = new RAction(DepositMoveMessage.M.effect(), new Handler_effectAction());
    getToolbar().add(new RToolbarButton(effectAction));

    abortAction = new RAction(DepositMoveMessage.M.abort(), new Handler_abortAction());
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

  private void drawSelf() throws ClientBizException {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    panel.add(drawGeneralGadget());

    panel.add(drawRemark());
  }

  private Widget drawGeneralGadget() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");
    vp.add(mvp);

    Widget w = drawBasicPanel();
    mvp.add(0, w);

    w = drawOutInfoPanel();
    mvp.add(0, w);

    w = drawInInfoPanel();
    mvp.add(0, w);

    w = drawOperatePanel();
    mvp.add(1, w);

    permGroupField = new PermGroupViewField();
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(DepositMoveMessage.M.generalInfo());
    box.setWidth("100%");
    box.setContent(vp);

    return box;
  }

  private Widget drawBasicPanel() {
    RForm basicForm = new RForm(1);
    basicForm.setWidth("100%");

    billNumberField = new RViewStringField(PDepositMoveDef.billNumber);
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    basicForm.addField(billNumberField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(DepositMoveMessage.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicForm);
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);

    return box;
  }

  private Widget drawOutInfoPanel() {
    outInfoForm = new RForm(1);
    outInfoForm.setWidth("100%");

    outAccountUnitField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    outInfoForm.addField(outAccountUnitField);

    outCounterpartField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    outInfoForm.addField(outCounterpartField);

    outContrarctField = new DispatchLinkField(PDepositMoveDef.constants.outContract_code());
    outContrarctField.setLinkKey(GRes.R.dispatch_key());
    outInfoForm.addField(outContrarctField);

    outContractNameField = new RViewStringField(PDepositMoveDef.constants.outContract_name());
    outInfoForm.addField(outContractNameField);

    outSubjectField = new RHyperlinkField(PDepositMoveDef.constants.outSubject());
    outSubjectField.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        if (entity == null || entity.getOutSubject() == null
            || entity.getOutSubject().getUuid() == null)
          return;
        doJumeSubject(entity.getOutSubject().getUuid());
      }
    });
    outInfoForm.addField(outSubjectField);

    totalField = new RViewNumberField(PDepositMoveDef.constants.amount());
    totalField.setWidth("50%");
    totalField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    totalField.setFormat(GWTFormat.fmt_money);
    outInfoForm.addField(totalField);

    amountField = new RViewNumberField();
    amountField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    amountField.setFormat(GWTFormat.fmt_money);

    outBalanceField = new RViewNumberField();
    outBalanceField.setFormat(GWTFormat.fmt_money);
    outBalanceField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    totalComField = new RCombinedField() {
      {
        setCaption(PDepositMoveDef.constants.amount() + "/" + DepositMoveMessage.M.balance());
        addField(amountField, 0.5f);
        addField(outBalanceField, 0.5f);
      }
    };
    outInfoForm.addField(totalComField);

    accountDateField = new RViewDateField(PDepositMoveDef.accountDate);
    outInfoForm.addField(accountDateField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(DepositMoveMessage.M.out() + DepositMoveMessage.M.info());
    box.setWidth("100%");
    box.setContent(outInfoForm);
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);

    return box;
  }

  private Widget drawOperatePanel() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PDepositMoveDef.constants.bizState());
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    reasonField = new RViewStringField(DepositMoveMessage.M.reason());
    operateForm.addField(reasonField);

    createInfoField = new RSimpleOperateInfoField(PDepositMoveDef.constants.createInfo());
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PDepositMoveDef.constants.lastModifyInfo());
    operateForm.addField(lastModifyInfoField);

    moreInfoField = new RHyperlink();
    moreInfoField.addClickHandler(new Handler_viewLogAction());
    moreInfoField.setHTML(DepositMoveMessage.M.moreInfo());
    operateForm.addField(moreInfoField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(DepositMoveMessage.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawInInfoPanel() {
    inInfoForm = new RForm(1);
    inInfoForm.setWidth("100%");

    inAccountUnitField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    inInfoForm.addField(inAccountUnitField);

    inCounterpartField = new RViewStringField(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    inInfoForm.addField(inCounterpartField);

    inContrarctField = new DispatchLinkField(PDepositMoveDef.constants.outContract_code());
    inContrarctField.setLinkKey(GRes.R.dispatch_key());
    inInfoForm.addField(inContrarctField);

    inContractNameField = new RViewStringField(PDepositMoveDef.constants.outContract_name());
    inInfoForm.addField(inContractNameField);

    inSubjectField = new RHyperlinkField(PDepositMoveDef.constants.outSubject());
    inSubjectField.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        if (entity == null || entity.getInSubject() == null
            || entity.getInSubject().getUuid() == null)
          return;
        doJumeSubject(entity.getInSubject().getUuid());
      }
    });
    inInfoForm.addField(inSubjectField);

    inBalanceField = new RViewNumberField(DepositMoveMessage.M.balance());
    inBalanceField.setWidth("50%");
    inBalanceField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    inBalanceField.setFormat(GWTFormat.fmt_money);
    inInfoForm.addField(inBalanceField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(DepositMoveMessage.M.in() + DepositMoveMessage.M.info());
    box.setWidth("100%");
    box.setContent(inInfoForm);
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);

    return box;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PDepositMoveDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.setReadOnly(true);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PDepositMoveDef.constants.remark());
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
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

    if (checkIn() == false)
      return;

    RecDepositMoveLoader.decodeParams(params, new Command() {

      @Override
      public void execute() {
        entity = RecDepositMoveLoader.getEntity();
        refresh();
      }
    });
  }

  private boolean checkIn() {
    if (ep.isProcessMode())
      return true;
    if (ep.isPermitted(RecDepositMovePermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refresh() {
    refreshBpm(entity);
    refreshToolbar();
    refreshEntity();
  }

  private void refreshToolbar() {
    getEP().appendSearchBox();
    navigator.start(getEP().getObjectName(), entity.getUuid());
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

  private void refreshEntity() {
    assert entity != null;

    billNumberField.setValue(entity.getBillNumber());

    outAccountUnitField.setValue(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .getNameCode());
    outCounterpartField.setValue(entity.getOutCounterpart() == null ? null : entity
        .getOutCounterpart().toFriendlyStr(getEP().getCounterpartTypeMap()));
    if (entity.getOutContract() == null) {
      outContrarctField.setValue(null);
    } else {
      if (entity.getOutCounterpart() != null
          && BCounterpart.COUNPERPART_PROPRIETOR.equals(entity.getOutCounterpart()
              .getCounterpartType())) {
        outContrarctField.setValue(GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE, entity
            .getOutContract().getCode());
      } else {
        outContrarctField.setValue(GRes.FIELDNAME_CONTRACT_BILLTYPE, entity.getOutContract()
            .getCode());
      }
    }
    outContractNameField.setValue(entity.getOutContract() == null ? null : entity.getOutContract()
        .getName());
    outSubjectField.setValue(entity.getOutSubject().toFriendlyStr());
    totalField.setValue(entity.getAmount().doubleValue());
    amountField.setValue(entity.getAmount().doubleValue());
    outBalanceField.setValue(entity.getOutBalance() == null ? null : entity.getOutBalance()
        .doubleValue());
    accountDateField.setValue(entity.getAccountDate());

    bizStateField.setValue(PDepositMoveDef.bizState.getEnumCaption(entity.getBizState()));
    reasonField.setVisible(entity.getBpmMessage() != null);
    reasonField.setValue(entity.getBpmMessage());
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());

    permGroupField.refresh(ep.isPermEnabled(), entity);

    inAccountUnitField.setValue(entity.getAccountUnit() == null ? null : entity.getAccountUnit()
        .getNameCode());
    inCounterpartField.setValue(entity.getInCounterpart() == null ? null : entity
        .getInCounterpart().toFriendlyStr(getEP().getCounterpartTypeMap()));
    if (entity.getInContract() == null) {
      inContrarctField.setValue(null);
    } else {
      if (entity.getInCounterpart() != null
          && BCounterpart.COUNPERPART_PROPRIETOR.equals(entity.getInCounterpart()
              .getCounterpartType())) {
        inContrarctField.setValue(GRes.FIELDNAME_LEASEBACKCONTRACT_BILLTYPE, entity.getInContract()
            .getCode());
      } else {
        inContrarctField.setValue(GRes.FIELDNAME_CONTRACT_BILLTYPE, entity.getInContract()
            .getCode());
      }
    }
    inContractNameField.setValue(entity.getInContract() == null ? null : entity.getInContract()
        .getName());
    inSubjectField.setValue(entity.getInSubject().toFriendlyStr());
    inBalanceField.setValue(entity.getInBalance() == null ? null : entity.getInBalance()
        .doubleValue());

    remarkField.setValue(entity.getRemark());

    outInfoForm.rebuild();
    operateForm.rebuild();
    inInfoForm.rebuild();
  }

  private class Handler_editAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      JumpParameters params = new JumpParameters(RecDepositMoveUrlParams.Edit.START_NODE);
      params.getUrlRef().set(RecDepositMoveUrlParams.Edit.PN_ENTITY_UUID, entity.getUuid());
      ep.jump(params);
    }
  }

  private class Handler_deleteAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();
      String msg = DepositMoveMessage.M.actionComfirm2(DepositMoveMessage.M.delete(),
          RecDepositMoveUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
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
    RLoadingDialog.show(DepositMoveMessage.M.actionDoing(DepositMoveMessage.M.delete()));
    RecDepositMoveService.Locator.getService().remove(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.actionFailed(DepositMoveMessage.M.delete(),
                RecDepositMoveUrlParams.MODULE_CAPTION);
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositMoveLogger.getInstance().log(DepositMoveMessage.M.delete(), entity);

            JumpParameters params = RecDepositMoveSearchPage.getInstance().getLastParams();
            params.getMessages().add(
                Message.info(DepositMoveMessage.M.onSuccess(DepositMoveMessage.M.delete(),
                    RecDepositMoveUrlParams.MODULE_CAPTION, entity.toFriendlyStr())));
            ep.jump(params);
          }
        });
  }

  private class Handler_effectAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();
      String msg = DepositMoveMessage.M.actionComfirm2(DepositMoveMessage.M.effect(),
          RecDepositMoveUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
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
    RLoadingDialog.show(DepositMoveMessage.M.actionDoing(DepositMoveMessage.M.effect()));

    RecDepositMoveService.Locator.getService().effect(entity.getUuid(), null, entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.actionFailed(DepositMoveMessage.M.effect(),
                ep.getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositMoveLogger.getInstance().log(DepositMoveMessage.M.effect(), entity);

            String msg = DepositMoveMessage.M.onSuccess(DepositMoveMessage.M.effect(),
                RecDepositMoveUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
            ep.jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });

  }

  private class Handler_abortAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      getMessagePanel().clearMessages();
      String msg = DepositMoveMessage.M.actionComfirm2(DepositMoveMessage.M.abort(),
          RecDepositMoveUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
      RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
        public void onClosed(boolean confirmed) {
          if (confirmed == false)
            return;
          doAbort();
        }
      });
    }
  }

  private void doAbort() {
    RLoadingDialog.show(DepositMoveMessage.M.actionDoing(DepositMoveMessage.M.abort()));

    RecDepositMoveService.Locator.getService().abort(entity.getUuid(), null, entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMoveMessage.M.actionFailed(DepositMoveMessage.M.abort(),
                ep.getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BDepositMoveLogger.getInstance().log(DepositMoveMessage.M.abort(), entity);

            String msg = DepositMoveMessage.M.onSuccess(DepositMoveMessage.M.abort(),
                RecDepositMoveUrlParams.MODULE_CAPTION, entity.toFriendlyStr());
            ep.jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });

  }

  private class Handler_viewLogAction implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      JumpParameters params = new JumpParameters(RecDepositMoveLogViewPage.START_NODE);
      params.getUrlRef().set(RecDepositMoveLogViewPage.PN_ENTITY_UUID, entity.getUuid());
      ep.jump(params);
    }
  }

  private void doJumeSubject(String subjectUuid) {
    GwtUrl url = SubjectUrlParams.ENTRY_URL;
    url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
    url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID, subjectUuid);
    try {
      RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
    } catch (Exception e) {
      String msg = DepositMoveMessage.M.cannotNavigate(PSubjectDef.TABLE_CAPTION);
      RMsgBox.showError(msg, e);
    }
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  private class ProcessDialog extends M3BaseOutgoingDialog {

    @Override
    protected void doExcute(BOperation operation, AsyncCallback<Object> callback) {
      final BProcessContext processCtx = getEP().getFormBuilder().getProcessCtx();
      RecDepositMoveServiceAgent.executeTask(processCtx, operation, entity, false, this);
    }
  }

  @Override
  protected EPRecDepositMove getEP() {
    return EPRecDepositMove.getInstance();
  }

  @Override
  protected void refreshToolButtons() {
    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());
    boolean isAbort = BBizStates.ABORTED.equals(entity.getBizState());

    boolean canEdit = getEP().isPermitted(RecDepositMovePermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(RecDepositMovePermDef.DELETE);
    boolean canEffect = getEP().isPermitted(RecDepositMovePermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(RecDepositMovePermDef.ABORT);

    editAction.setVisible(isInEffect && canEdit);
    deleteAction.setVisible(isInEffect && canDelete);
    effectAction.setVisible(isInEffect && canEffect);
    abortAction.setVisible(isEffect && canAbort);

    totalField.setVisible(isEffect || isAbort);
    totalComField.setVisible(isInEffect);
    inBalanceField.setVisible(isInEffect);

    outInfoForm.rebuild();
    inInfoForm.rebuild();

    getToolbar().minimizeSeparators();
  }

  @Override
  protected List<Widget> getRelateWidgets() {
    List<Widget> list = new ArrayList<Widget>();
    list.add(editAction);
    list.add(effectAction);
    list.add(deleteAction);
    list.add(abortAction);

    return list;
  }
}
