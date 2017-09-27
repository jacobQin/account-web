/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.ui;

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
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.internalfee.client.EPInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.InternalFeeMessages;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFeeLogger;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeLoader;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeService;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeServiceAgent;
import com.hd123.m3.account.gwt.internalfee.client.ui.gadget.InternalFeeLineViewGrid;
import com.hd123.m3.account.gwt.internalfee.intf.client.InternalFeeUrlParams;
import com.hd123.m3.account.gwt.internalfee.intf.client.InternalFeeUrlParams.View;
import com.hd123.m3.account.gwt.internalfee.intf.client.dd.PInternalFeeDef;
import com.hd123.m3.account.gwt.internalfee.intf.client.perm.InternalFeePermDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.common.M3BaseOutgoingDialog;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmViewPage;
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
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author liuguilin
 * 
 */
public class InternalFeeViewPage extends BaseBpmViewPage implements View {

  private static InternalFeeViewPage instance = null;

  public static InternalFeeViewPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new InternalFeeViewPage();
    return instance;
  }

  public InternalFeeViewPage() throws ClientBizException {
    super();
    entityLoader = new InternalFeeLoader();
    drawToolbar();
    drawSelf();
  }

  private InternalFeeLoader entityLoader;
  private BInternalFee entity;

  private RAction editAction;
  private RAction deleteAction;
  private RAction effectAction;
  private RAction abortAction;

  private EntityNavigator navigator;
  private PrintButton printButton;

  private RViewStringField billNumberField;
  private RViewStringField storeField;
  private RViewStringField vendorField;
  private RViewDateField beginDateField;
  private RViewDateField endDateField;
  private RViewDateField accountDateField;
  private RViewStringField directionTypeField;

  private RForm operateForm;
  private RViewStringField bizStateField;
  private RViewStringField causeField;
  private RSimpleOperateInfoField createInfo;
  private RSimpleOperateInfoField lastModifyInfo;
  private RHyperlink moreInfo;

  private PermGroupViewField permGroupField;

  private RViewNumberField totalField;
  private RViewNumberField taxField;

  private InternalFeeLineViewGrid grid;

  private RTextArea remarkField;

  private Handler_click clickHandler = new Handler_click();

  private void drawToolbar() {
    getToolbar().add(getEP().getCreateWidget());

    editAction = new RAction(RActionFacade.EDIT, clickHandler);
    getToolbar().add(new RToolbarButton(editAction));

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    getToolbar().add(new RToolbarButton(deleteAction));

    getToolbar().addSeparator();

    injectBpmActions();

    getToolbar().addSeparator();

    effectAction = new RAction(InternalFeeMessages.M.effect(), clickHandler);
    getToolbar().add(new RToolbarButton(effectAction));

    abortAction = new RAction(InternalFeeMessages.M.abort() + "...", clickHandler);
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

    billNumberField = new RViewStringField(PInternalFeeDef.billNumber);
    billNumberField.addTextStyleName(RTextStyles.STYLE_BOLD);
    form.addField(billNumberField);

    storeField = new RViewStringField(PInternalFeeDef.store);
    form.addField(storeField);

    vendorField = new RViewStringField(PInternalFeeDef.vendor);
    form.addField(vendorField);

    beginDateField = new RViewDateField(PInternalFeeDef.beginDate);
    form.addField(beginDateField);

    endDateField = new RViewDateField(PInternalFeeDef.endDate);
    form.addField(endDateField);

    accountDateField = new RViewDateField(PInternalFeeDef.accountDate);
    form.addField(accountDateField);

    directionTypeField = new RViewStringField(PInternalFeeDef.constants.direction());
    form.addField(directionTypeField);

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
    taxField.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    form.addField(new RCombinedField() {
      {
        setCaption(PInternalFeeDef.constants.total());
        addField(totalField, 0.5f);
        addField(taxField, 0.5f);
      }
    });

    form.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InternalFeeMessages.M.aggregate());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private Widget drawOperate() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    bizStateField = new RViewStringField(PInternalFeeDef.bizState);
    bizStateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(bizStateField);

    causeField = new RViewStringField(InternalFeeMessages.M.cause());
    operateForm.addField(causeField);

    createInfo = new RSimpleOperateInfoField(PInternalFeeDef.constants.createInfo());
    operateForm.addField(createInfo);

    lastModifyInfo = new RSimpleOperateInfoField(PInternalFeeDef.constants.lastModifyInfo());
    operateForm.addField(lastModifyInfo);

    moreInfo = new RHyperlink();
    moreInfo.addClickHandler(clickHandler);
    moreInfo.setHTML(InternalFeeMessages.M.moreInfo());
    operateForm.addField(moreInfo);

    operateForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(InternalFeeMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawGrid() {
    grid = new InternalFeeLineViewGrid();
    return grid;
  }

  private Widget drawRemark() {
    remarkField = new RTextArea(PInternalFeeDef.remark);
    remarkField.setWidth("100%");
    remarkField.setHeight("60px");
    remarkField.setReadOnly(true);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PInternalFeeDef.constants.remark());
    box.setWidth("100%");
    box.setContent(remarkField);
    box.getCaptionBar().setShowCollapse(true);

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
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    entityLoader.decodeParams(params, new Command() {
      @Override
      public void execute() {
        entity = entityLoader.getEntity();
        refreshBpm(entity);
        refreshToolbar();
        refreshEntity();
      }
    });
  }

  private boolean checkIn() {
    if (getEP().isProcessMode())
      return true;

    if (getEP().isPermitted(InternalFeePermDef.READ) == false) {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
    return true;
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
    refreshGeneral();
    refreshOperateInfo();
    refreshAuthorize();
    refreshGrid();
  }

  private void refreshGeneral() {
    assert entity != null;
    billNumberField.setValue(entity.getBillNumber());
    storeField.setValue(entity.getStore() == null ? null : entity.getStore().toFriendlyStr());
    vendorField.setValue(entity.getVendor() == null ? null : entity.getVendor().toFriendlyStr());
    beginDateField.setValue(entity.getBeginDate());
    endDateField.setValue(entity.getEndDate());
    accountDateField.setValue(entity.getAccountDate());
    directionTypeField.setValue(DirectionType.getCaptionByValue(entity.getDirection()));
    refreshTotal();
    remarkField.setValue(entity.getRemark());
  }

  private void refreshTotal() {
    totalField.setValue(entity.getTotal().getTotal());
    taxField.setValue(entity.getTotal().getTax());
  }

  private void refreshOperateInfo() {
    bizStateField.setValue(PInternalFeeDef.bizState.getEnumCaption(entity.getBizState()));
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
    grid.refresh(entity);
  }

  @Override
  protected BaseOutgoingDialog getOutgoingDialog() {
    return new ProcessDialog();
  }

  @Override
  protected EPInternalFee getEP() {
    return EPInternalFee.getInstance();
  }

  @Override
  protected void refreshToolButtons() {
    boolean isInEffect = BBizStates.INEFFECT.equals(entity.getBizState());
    boolean isEffect = BBizStates.EFFECT.equals(entity.getBizState());

    boolean canEdit = getEP().isPermitted(InternalFeePermDef.UPDATE);
    boolean canDelete = getEP().isPermitted(InternalFeePermDef.DELETE);
    boolean canEffect = getEP().isPermitted(InternalFeePermDef.EFFECT);
    boolean canAbort = getEP().isPermitted(InternalFeePermDef.ABORT);

    editAction.setVisible(isInEffect && canEdit);
    deleteAction.setVisible(isInEffect && canDelete);
    effectAction.setVisible(isInEffect && canEffect);
    abortAction.setVisible(isEffect && canAbort);

    getToolbar().minimizeSeparators();
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
      InternalFeeServiceAgent.executeTask(operation, entity, processCtx, false, this);
    }
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
      } else if (event.getSource().equals(moreInfo)) {
        doViewLog();
      }
    }
  }

  private void doEdit() {
    JumpParameters params = new JumpParameters(InternalFeeEditPage.START_NODE);
    params.getUrlRef().set(InternalFeeEditPage.PN_UUID, entity.getUuid());
    getEP().jump(params);
  }

  private void doConfirmDelete() {
    getMessagePanel().clearMessages();

    String msg = InternalFeeMessages.M.actionComfirm2(InternalFeeMessages.M.delete(),
        PInternalFeeDef.TABLE_CAPTION, entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (!confirmed)
          return;
        doDelete();
      }
    });
  }

  private void doDelete() {
    RLoadingDialog.show(InternalFeeMessages.M.beDoing(InternalFeeMessages.M.delete(),
        entity.getBillNumber()));
    InternalFeeService.Locator.getService().delete(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InternalFeeMessages.M.processError2(InternalFeeMessages.M.delete(),
                PInternalFeeDef.TABLE_CAPTION, entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BInternalFeeLogger.getInstance().log(InternalFeeMessages.M.delete(), entity);

            JumpParameters params = InternalFeeSearchPage.getInstance().getLastParams();
            params.getMessages().add(
                Message.info(InternalFeeMessages.M.onSuccess(InternalFeeMessages.M.delete(),
                    PInternalFeeDef.TABLE_CAPTION, entity.getBillNumber())));
            getEP().jump(params);
          }
        });
  }

  private void doConfirmEffect() {
    getMessagePanel().clearMessages();

    String msg = InternalFeeMessages.M.actionComfirm2(InternalFeeMessages.M.effect(),
        PInternalFeeDef.TABLE_CAPTION, entity.getBillNumber());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEffect();
      }
    });
  }

  private void doEffect() {
    RLoadingDialog.show(InternalFeeMessages.M.beDoing(InternalFeeMessages.M.effect(),
        entity.getBillNumber()));
    InternalFeeService.Locator.getService().effect(entity.getUuid(), null, entity.getVersion(),
        new RBAsyncCallback2() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InternalFeeMessages.M.processError2(InternalFeeMessages.M.effect(),
                PInternalFeeDef.TABLE_CAPTION, entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BInternalFeeLogger.getInstance().log(InternalFeeMessages.M.effect(), entity);

            String msg = InternalFeeMessages.M.onSuccess(InternalFeeMessages.M.effect(),
                PInternalFeeDef.TABLE_CAPTION, entity.getBillNumber());
            getEP().jumpToViewPage(entity.getUuid(), Message.info(msg));
          }
        });
  }

  private void doConfirmAbort() {
    getMessagePanel().clearMessages();

    InputBox.show(InternalFeeMessages.M.abortReason(), null, true, PInternalFeeDef.remark,
        new InputBox.Callback() {
          public void onClosed(boolean ok, String text) {
            if (ok == false)
              return;
            doAbort(text);
          }
        });
  }

  private void doAbort(String comment) {
    RLoadingDialog.show(InternalFeeMessages.M.beDoing(InternalFeeMessages.M.abort(),
        entity.getBillNumber()));
    InternalFeeService.Locator.getService().abort(entity.getUuid(), comment, entity.getVersion(),
        new RBAsyncCallback2() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = InternalFeeMessages.M.processError2(InternalFeeMessages.M.abort(),
                PInternalFeeDef.TABLE_CAPTION, entity.getBillNumber());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Object result) {
            RLoadingDialog.hide();
            BInternalFeeLogger.getInstance().log(InternalFeeMessages.M.abort(), entity);

            String msg = InternalFeeMessages.M.onSuccess(InternalFeeMessages.M.abort(),
                PInternalFeeDef.TABLE_CAPTION, entity.getBillNumber());

            JumpParameters params = new JumpParameters(START_NODE);
            params.getUrlRef().set(PN_UUID, entity.getUuid());
            params.getMessages().add(Message.info(msg));
            getEP().jump(params);
          }
        });
  }

  private void doViewLog() {
    JumpParameters params = new JumpParameters(InternalFeeLogPage.START_NODE);
    params.getUrlRef().set(InternalFeeUrlParams.Log.PN_UUID, entity.getUuid());
    getEP().jump(params);
  }

}
