/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： SubjectEditPage.java
 * 模块说明：
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.subject.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.subject.client.EPSubject;
import com.hd123.m3.account.gwt.subject.client.SubjectMessages;
import com.hd123.m3.account.gwt.subject.client.biz.BSubject;
import com.hd123.m3.account.gwt.subject.client.biz.BSubjectLogger;
import com.hd123.m3.account.gwt.subject.client.biz.BSubjectStoreTaxRate;
import com.hd123.m3.account.gwt.subject.client.rpc.BSubjectLoader;
import com.hd123.m3.account.gwt.subject.client.rpc.SubjectService;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectUsageDef;
import com.hd123.m3.account.gwt.subject.intf.client.perm.SubjectPermDef;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author chenrizhang
 * 
 */
public class SubjectViewPage extends BaseContentPage implements SubjectUrlParams.View {
  private static SubjectViewPage instance;
  private static EPSubject ep = EPSubject.getInstance();

  public static SubjectViewPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new SubjectViewPage();
    return instance;
  }

  public SubjectViewPage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(SubjectMessages.M.cannotCreatePage("SubjectViewPage"), e);
    }
  }

  private BSubject entity;

  private RAction createAction;
  private RAction editAction;
  private RAction enableAction;
  private RAction disableAction;

  private RForm basicForm;
  private RViewStringField codeField;
  private RViewStringField nameField;
  private RViewStringField directionField;
  private RViewStringField usageField;
  private RViewStringField taxRateField;
  private RViewStringField typeField;

  private RForm operateForm;
  private RViewStringField stateField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;
  private RHyperlink moreInfo;

  private RGrid grid;
  private RGridColumnDef ordinalCol;
  private RGridColumnDef storeCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef remarkCol;
  private List<BSubjectStoreTaxRate> lines = new ArrayList<BSubjectStoreTaxRate>();

  // 组件
  private RTextArea remarkField;

  // 句柄
  private ActionHandler actionHandler = new ActionHandler();

  private void drawToolbar() {
    createAction = new RAction(RActionFacade.CREATE, actionHandler);
    getToolbar().add(new RToolbarButton(createAction));

    editAction = new RAction(RActionFacade.EDIT, actionHandler);
    getToolbar().add(new RToolbarButton(editAction));

    getToolbar().addSeparator();

    enableAction = new RAction(SubjectMessages.M.enable(), actionHandler);
    getToolbar().add(new RToolbarButton(enableAction));

    disableAction = new RAction(SubjectMessages.M.disable(), actionHandler);
    getToolbar().add(new RToolbarButton(disableAction));
  }

  private void drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    initWidget(panel);

    Widget w = drawGeneralGadget();
    panel.add(w);

    w = drawDetailBoxGadget();
    panel.add(w);

    w = drawRemarkGadget();
    panel.add(w);
  }

  private Widget drawGeneralGadget() {
    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");

    Widget w = drawBasicGadget();
    mvp.add(0, w);

    w = drawOperateGadget();
    mvp.add(1, w);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(SubjectMessages.M.generalInfo());
    box.getCaptionBar().setShowCollapse(true);
    box.setWidth("100%");
    box.setContent(mvp);
    return box;
  }

  private Widget drawBasicGadget() {
    basicForm = new RForm(1);
    basicForm.setWidth("100%");

    codeField = new RViewStringField(PSubjectDef.code);
    codeField.addStyleName(RTextStyles.STYLE_BOLD);
    codeField.setWidth("100%");
    basicForm.addField(codeField);

    nameField = new RViewStringField(PSubjectDef.name);
    nameField.setWidth("100%");
    basicForm.addField(nameField);

    directionField = new RViewStringField(PSubjectDef.direction);
    directionField.setWidth("100%");
    basicForm.addField(directionField);

    usageField = new RViewStringField(PSubjectUsageDef.usage);
    usageField.setWidth("100%");
    basicForm.addField(usageField);

    taxRateField = new RViewStringField(PSubjectDef.taxRate);
    taxRateField.setWidth("100%");
    basicForm.addField(taxRateField);

    typeField = new RViewStringField(PSubjectDef.constants.customType());
    typeField.setWidth("100%");
    basicForm.addField(typeField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(SubjectMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicForm);
    return box;
  }

  // TODO
  private Widget drawDetailBoxGadget() {
    drawDetailGadget();

    RCaptionBox box = new RCaptionBox();
    box.setCaption(SubjectMessages.M.storeSubjectTaxRate());
    box.setWidth("100%");
    box.setEditing(false);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(grid);
    return box;
  }

  private Widget drawDetailGadget() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.setProvider(new GridDataProvider());

    ordinalCol = new RGridColumnDef(PSubjectDef.ordinal);
    ordinalCol.setWidth("50px");
    grid.addColumnDef(ordinalCol);

    storeCol = new RGridColumnDef(PSubjectDef.store);
    storeCol.setWidth("220px");
    grid.addColumnDef(storeCol);

    taxRateCol = new RGridColumnDef(PSubjectDef.taxRate_rate);
    taxRateCol.setWidth("180px");
    grid.addColumnDef(taxRateCol);

    remarkCol = new RGridColumnDef(PSubjectDef.remark);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private Widget drawOperateGadget() {
    operateForm = new RForm(1);
    operateForm.setWidth("100%");

    stateField = new RViewStringField(PSubjectDef.state);
    stateField.addTextStyleName(RTextStyles.STYLE_BOLD);
    operateForm.addField(stateField);

    createInfoField = new RSimpleOperateInfoField(PSubjectDef.createInfo);
    operateForm.addField(createInfoField);

    lastModifyInfoField = new RSimpleOperateInfoField(PSubjectDef.lastModifyInfo);
    operateForm.addField(lastModifyInfoField);

    moreInfo = new RHyperlink();
    moreInfo.addClickHandler(actionHandler);
    moreInfo.setEnabled(true);
    moreInfo.setHTML("详情>>");
    operateForm.addField(moreInfo);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(SubjectMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea(PSubjectDef.remark);
    remarkField.setWidth("100%");
    remarkField.setReadOnly(true);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PSubjectDef.constants.remark());
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
  public void onShow(JumpParameters params) {
    super.onShow(params);
    if (checkIn() == false)
      return;
    BSubjectLoader.decodeParams(params, new BSubjectLoader.Callback() {

      @Override
      public void execute(BSubject result) {
        entity = result;
        refreshTitle();
        refreshCommand();
        refreshEntity();
      }
    });
  }

  private boolean checkIn() {
    if (ep.isPermitted(SubjectPermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ep.getModuleCaption());
    if (entity != null) {
      ep.getTitleBar().appendAttributeText(entity.getCode());
      ep.getTitleBar().appendAttributeText(entity.getName());
    }
  }

  private void refreshCommand() {
    createAction.setEnabled(ep.isPermitted(SubjectPermDef.CREATE));
    editAction.setEnabled(ep.isPermitted(SubjectPermDef.UPDATE));
    enableAction.setEnabled(ep.isPermitted(SubjectPermDef.ENABLE));
    disableAction.setEnabled(ep.isPermitted(SubjectPermDef.DISABLE));

    editAction.setVisible(entity.isEnabled());
    disableAction.setVisible(entity.isEnabled());
    enableAction.setVisible(entity.isEnabled() == false);
  }

  private void refreshEntity() {
    codeField.setValue(entity.getCode());
    nameField.setValue(entity.getName());
    directionField.setValue(DirectionType.getCaptionByValue(entity.getDirection()));
    List<String> usages = new ArrayList<String>();
    for (String usageCode : entity.getUsages()) {
      BSubjectUsage subjectUsage = ep.getUsage(usageCode);
      if (subjectUsage != null) {
        usages.add(subjectUsage.getName());
      }
    }
    usageField.setValue(CollectionUtil.toString(usages, ','));
    taxRateField.setValue(entity.getTaxRate().caption());
    typeField.setValue(entity.getCustomType());

    stateField.setValue(entity.isEnabled() ? SubjectMessages.M.enabled() : SubjectMessages.M
        .disabled());
    if (entity.isEnabled() == false) {
      stateField.removeTextStyleName(RTextStyles.STYLE_STATE_USING);
      stateField.addTextStyleName(RTextStyles.STYLE_STATE_DELETED);
    } else {
      stateField.removeTextStyleName(RTextStyles.STYLE_STATE_DELETED);
      stateField.addTextStyleName(RTextStyles.STYLE_STATE_USING);
    }
    createInfoField.setOperateInfo(entity.getCreateInfo());
    lastModifyInfoField.setOperateInfo(entity.getLastModifyInfo());
    remarkField.setValue(entity.getRemark());
    lines.clear();
    lines.addAll(entity.getStoreTaxRates());
    grid.refresh();
  }

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == createAction) {
        doCreate();
      } else if (event.getSource() == editAction) {
        doEdit();
      } else if (event.getSource() == enableAction) {
        doEnableConfirm(true);
      } else if (event.getSource() == disableAction) {
        doEnableConfirm(false);
      } else if (event.getSource() == moreInfo) {
        doViewDetail();
      }
    }
  }

  private void doCreate() {
    JumpParameters params = new JumpParameters(SubjectUrlParams.Create.START_NODE);
    ep.jump(params);
  }

  private void doEdit() {
    JumpParameters params = new JumpParameters(SubjectEditPage.START_NODE);
    params.getUrlRef().set(SubjectEditPage.PN_ENTITY_UUID, entity.getUuid());
    ep.jump(params);
  }

  private void doViewDetail() {
    JumpParameters params = new JumpParameters(SubjectUrlParams.Log.START_NODE);
    params.getUrlRef().set(SubjectUrlParams.Log.PN_ENTITY_UUID, entity.getUuid());
    ep.jump(params);
  }

  private void doEnableConfirm(final boolean enable) {
    getMessagePanel().clearMessages();

    String actionText = enable ? SubjectMessages.M.enable() : SubjectMessages.M.disable();
    String msg = SubjectMessages.M.actionComfirm2(actionText, ep.getModuleCaption(),
        entity.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEnable(enable);
      }
    });
  }

  private void doEnable(final boolean enable) {
    final String actionText = enable ? SubjectMessages.M.enable() : SubjectMessages.M.disable();
    RLoadingDialog.show(SubjectMessages.M.actionDoing(actionText));
    SubjectService.Locator.getService().enable(entity.getUuid(), entity.getVersion(), enable,
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = SubjectMessages.M.actionFailed(actionText, ep.getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BSubjectLogger.getInstatnce().log(
                enable ? SubjectMessages.M.enable() : SubjectMessages.M.disable(), entity);

            String msg = SubjectMessages.M.onSuccess(actionText, ep.getModuleCaption(),
                entity.toFriendlyStr());
            JumpParameters params = new JumpParameters(SubjectViewPage.START_NODE);
            params.getUrlRef().set(SubjectViewPage.PN_ENTITY_UUID, entity.getUuid());
            params.getMessages().add(Message.info(msg));
            ep.jump(params);
          }
        });
  }

  // TODO
  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return lines == null ? 0 : lines.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (lines == null)
        return null;
      BSubjectStoreTaxRate value = lines.get(row);
      if (col == ordinalCol.getIndex()) {
        return (row + 1) + "";
      } else if (col == storeCol.getIndex()) {
        return value.getStore().toFriendlyStr();
      } else if (col == taxRateCol.getIndex()) {
        return value.getTaxRate();
      } else if (col == remarkCol.getIndex()) {
        return value.getRemark();
      }
      return null;
    }
  }

}
