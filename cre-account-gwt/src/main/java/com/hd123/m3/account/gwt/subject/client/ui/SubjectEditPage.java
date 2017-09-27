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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
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
import com.hd123.m3.commons.gwt.widget.client.ui.OptionComboBox;
import com.hd123.m3.commons.gwt.widget.client.ui.TaxRateComboBox;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridCellWidgetRenderer;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.history.RHistory;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RCheckGroup;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RRadioGroup;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.form.RSimpleOperateInfoField;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author chenrizhang
 * 
 */
public class SubjectEditPage extends BaseContentPage implements SubjectUrlParams.Edit, RValidatable {
  private static SubjectEditPage instance;
  private static EPSubject ep = EPSubject.getInstance();

  public static SubjectEditPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new SubjectEditPage();
    return instance;
  }

  public SubjectEditPage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(SubjectMessages.M.cannotCreatePage("SubjectEditPage"), e);
    }
  }

  private BSubject entity;

  private RAction saveAction;
  private RAction cancelAction;

  private RForm basicForm;
  private RViewStringField codeField;
  private RTextBox nameField;
  private RRadioGroup<Integer> directionField;
  private RCheckGroup<String> usageField;
  private TaxRateComboBox taxRateField;
  private OptionComboBox typeField;

  private RForm operateForm;
  private RViewStringField stateField;
  private RSimpleOperateInfoField createInfoField;
  private RSimpleOperateInfoField lastModifyInfoField;

  private EditGrid<BSubjectStoreTaxRate> grid;
  private RGridColumnDef ordinalCol;
  private RGridColumnDef storeCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef remarkCol;
  private List<BSubjectStoreTaxRate> lines = new ArrayList<BSubjectStoreTaxRate>();

  // 组件
  private RTextArea remarkField;

  // 句柄
  private ValueHandler valueHandler = new ValueHandler();
  private ActionHandler actionHandler = new ActionHandler();

  private void drawToolbar() {
    saveAction = new RAction(RActionFacade.SAVE, actionHandler);
    RToolbarButton saveButton = new RToolbarButton(saveAction);
    getToolbar().add(saveButton);

    cancelAction = new RAction(RActionFacade.CANCEL, actionHandler);
    RToolbarButton cancelButton = new RToolbarButton(cancelAction);
    getToolbar().add(cancelButton);
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
    box.setEditing(true);
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

    nameField = new RTextBox(PSubjectDef.name);
    nameField.setWidth("100%");
    nameField.addChangeHandler(valueHandler);
    basicForm.addField(nameField);

    directionField = new RRadioGroup<Integer>(PSubjectDef.direction);
    directionField.add(DirectionType.receipt.getCaption(),
        DirectionType.receipt.getDirectionValue());
    directionField.add(DirectionType.payment.getCaption(),
        DirectionType.payment.getDirectionValue());
    directionField.addChangeHandler(valueHandler);
    directionField.setReadOnly(true);
    basicForm.add(directionField);

    usageField = new RCheckGroup<String>(PSubjectUsageDef.constants.usage());
    usageField.setRequired(true);
    usageField.setColumnCount(3);
    for (BSubjectUsage u : ep.getUsages()) {
      usageField.add(u.getName(), u.getCode());
    }
    usageField.addChangeHandler(valueHandler);
    usageField.setReadOnly(true);
    basicForm.add(usageField);

    taxRateField = new TaxRateComboBox();
    taxRateField.setCaption(PSubjectDef.constants.taxRate());
    taxRateField.addChangeHandler(valueHandler);
    taxRateField.setRequired(true);
    taxRateField.refreshOptions();
    basicForm.add(taxRateField);

    typeField = new OptionComboBox(EPSubject.KEY_CUSTOM_TYPE, PSubjectDef.constants.customType(),
        PSubjectDef.constants.customType());
    typeField.setWidth("100%");
    typeField.setMaxDropdownRowCount(10);
    typeField.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        entity.setCustomType(typeField.getValue());
      }
    });
    typeField.setNullOptionTextToDefault();
    basicForm.add(typeField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(SubjectMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(basicForm);
    return box;
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

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(SubjectMessages.M.operateInfo());
    box.setWidth("100%");
    box.setContent(operateForm);
    return box;
  }

  /** 项目科目税率 */
  private Widget drawDetailBoxGadget() {
    drawDetailGadget();

    RCaptionBox box = new RCaptionBox();
    box.setCaption(SubjectMessages.M.storeSubjectTaxRate());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(grid);
    return box;
  }

  private Widget drawDetailGadget() {
    grid = new EditGrid<BSubjectStoreTaxRate>();
    grid.setWidth("100%");
    grid.setRequired(true);
    grid.setCaption(SubjectMessages.M.storeSubjectTaxRate());
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.setDefaultDataRowCount(1);
    grid.setActionModes(EditGrid.ActionMode.append, EditGrid.ActionMode.remove);

    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.setDataProducer(provider);

    CellRendererFactory rendererFactory = new CellRendererFactory();

    ordinalCol = new RGridColumnDef(PSubjectDef.ordinal);
    ordinalCol.setWidth("50px");
    grid.addColumnDef(ordinalCol);

    storeCol = new RGridColumnDef(PSubjectDef.store);
    storeCol.setRendererFactory(rendererFactory);
    storeCol.setWidth("220px");
    grid.addColumnDef(storeCol);

    taxRateCol = new RGridColumnDef(PSubjectDef.taxRate_rate);
    taxRateCol.setRendererFactory(rendererFactory);
    taxRateCol.setWidth("180px");
    grid.addColumnDef(taxRateCol);

    remarkCol = new RGridColumnDef(PSubjectDef.remark);
    remarkCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private Widget drawRemarkGadget() {
    remarkField = new RTextArea(PSubjectDef.remark);
    remarkField.setWidth("100%");
    remarkField.addChangeHandler(valueHandler);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PSubjectDef.constants.remark());
    box.setWidth("100%");
    box.setEditing(true);
    box.getCaptionBar().setShowCollapse(true);
    box.setContent(remarkField);
    return box;
  }

  @Override
  public void clearValidResults() {
    getMessagePanel().clearMessages();
    basicForm.clearValidResults();
    remarkField.clearValidResults();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    list.addAll(basicForm.getInvalidMessages());
    list.addAll(grid.getInvalidMessages());
    list.addAll(remarkField.getInvalidMessages());
    return list;
  }

  @Override
  public boolean isValid() {
    return basicForm.isValid() && remarkField.isValid();
  }

  @Override
  public boolean validate() {
    getMessagePanel().clearMessages();
    boolean isValid = basicForm.validate();
    isValid &= grid.validate();
    isValid &= remarkField.validate();
    if (isValid == false)
      getMessagePanel().putMessages(getInvalidMessages());
    return isValid;
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
    GWTUtil.enableSynchronousRPC();
    typeField.refreshOptions(false);
    BSubjectLoader.decodeParams(params, new BSubjectLoader.Callback() {

      @Override
      public void execute(BSubject result) {
        entity = result;
        refreshTitle();
        refreshCommand();
        refreshEntity();
        basicForm.focusOnFirstField();
      }
    });
  }

  private boolean checkIn() {
    if (ep.isPermitted(SubjectPermDef.UPDATE) == false) {
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
  }

  private void refreshEntity() {
    codeField.setValue(entity.getCode());
    nameField.setValue(entity.getName());
    directionField.setValue(entity.getDirection());
    usageField.setValue(entity.getUsages());
    refreshUsages();

    taxRateField.setValue(entity.getTaxRate());
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
    lines = entity.getStoreTaxRates();
    grid.setValues(lines);
  }

  private void refreshUsages() {
    if (entity.getUsages().isEmpty()) {
      entity.setType(null);
      for (RCheckBox box : usageField.getCheckBoxes()) {
        box.setEnabled(true);
      }
    } else {
      BSubjectUsage subjectUsage = ep.getUsage(entity.getUsages().get(0));

      entity.setType(subjectUsage.getType().name());
      for (BSubjectUsage u : ep.getUsages()) {
        if (entity.getType().equals(u.getType().name()) == false) {
          usageField.getCheckBoxByValue(u.getCode()).setEnabled(false);
        } else {
          if (BSubjectType.credit.name().equals(entity.getType())
              && subjectUsage.getCode().equals(u.getCode()) == false)
            usageField.getCheckBoxByValue(u.getCode()).setEnabled(false);
          else
            usageField.getCheckBoxByValue(u.getCode()).setEnabled(true);
        }
      }
      usageField.clearValidResults();
    }
  }

  private class ValueHandler implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == nameField) {
        entity.setName(nameField.getValue());
      } else if (event.getSource() == directionField) {
        entity.setDirection(directionField.getValue());
      } else if (event.getSource() == usageField) {
        entity.setUsages(usageField.getValue());
        refreshUsages();
      } else if (event.getSource() == taxRateField) {
        entity.setTaxRate(taxRateField.getValue());
        grid.rebuild();
      } else if (event.getSource() == remarkField) {
        entity.setRemark(remarkField.getValue());
      }
    }
  }

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == cancelAction) {
        doCancel();
      } else if (event.getSource() == saveAction) {
        doSave();
      }
    }
  }

  private void doSave() {
    GWTUtil.blurActiveElement();
    if (!validate())
      return;
    RLoadingDialog.show(SubjectMessages.M.actionDoing(SubjectMessages.M.save()));
    SubjectService.Locator.getService().save(entity, new RBAsyncCallback2<BSubject>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = SubjectMessages.M.actionFailed(SubjectMessages.M.save(), ep.getModuleCaption());
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(BSubject result) {
        RLoadingDialog.hide();
        BSubjectLogger.getInstatnce().log(SubjectMessages.M.modify(), result);

        JumpParameters params = new JumpParameters(SubjectViewPage.START_NODE);
        params.getUrlRef().set(SubjectViewPage.PN_ENTITY_UUID, result.getUuid());
        params.getExtend().put(EPSubject.OPN_ENTITY, result);
        params.getMessages().add(
            Message.info(SubjectMessages.M.onSuccess(SubjectMessages.M.save(),
                ep.getModuleCaption(), result.toFriendlyStr())));
        ep.jump(params);
      }
    });
  }

  private void doCancel() {
    String msg = SubjectMessages.M.actionComfirm(SubjectMessages.M.cancel(),
        SubjectMessages.M.edit());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed)
          RHistory.back();
      }
    });
  }

  private class GridDataProvider implements RGridDataProvider, EditGridDataProduce {

    @Override
    public Object create() {
      BSubjectStoreTaxRate value = new BSubjectStoreTaxRate();
      value.setTaxRate(taxRateField.getValue());
      return value;
    }

    @Override
    public int getRowCount() {
      return lines == null ? 0 : lines.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (lines == null || row < 0 || row >= lines.size())
        return null;
      BSubjectStoreTaxRate value = lines.get(row);
      if (col == ordinalCol.getIndex()) {
        return (row + 1);
      } else if (col == storeCol.getIndex()) {
        return value.getStore();
      } else if (col == taxRateCol.getIndex()) {
        return value.getTaxRate();
      } else if (col == remarkCol.getIndex()) {
        return value.getRemark();
      }
      return null;
    }
  }

  private class CellRendererFactory implements RCellRendererFactory {

    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        Object data) {
      BSubjectStoreTaxRate value = lines.get(row);
      if (PSubjectDef.store.getName().equals(colDef.getName())) {
        return new StoreRenderer(grid, colDef, row, col, value);
      } else if (PSubjectDef.remark.getName().equals(colDef.getName())) {
        return new RemarkRenderer(grid, colDef, row, col, value);
      } else if (PSubjectDef.taxRate_rate.getName().equals(colDef.getName())) {
        return new TaxRateRenderer(grid, colDef, row, col, value);
      }
      return null;
    }

  }

  private class StoreRenderer extends EditGridCellWidgetRenderer<RUCNBox> {

    public StoreRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private AccountUnitUCNBox field;

    @Override
    protected RUCNBox drawField(RGridColumnDef colDef, Object data) {
      field = new AccountUnitUCNBox();
      field.getNameBox().setVisible(true);
      field.setRequired(false);
      field.setEnterToTab(false);

      StoreHandler handler = new StoreHandler();
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addChangeHandler(handler);
      field.setValidator(handler);
      return field;
    }

    @Override
    public void setValue(Object value) {
      BSubjectStoreTaxRate line = lines.get(getRow());
      field.setRawValue(line.getStore());
      field.setValue(line.getStore(), true);
    }

    private class StoreHandler implements ChangeHandler, RValidator {

      @Override
      public void onChange(ChangeEvent event) {
        BSubjectStoreTaxRate detail = lines.get(getRow());
        detail.setStore(field.getValue());
        detail.getStore().setCode(field.getRawValue().getCode());
        detail.getStore().setName(field.getRawValue().getName());
        detail.getStore().setUuid(field.getRawValue().getUuid());
        grid.refresh();
      }

      @Override
      public Message validate(Widget sender, String str) {
        BSubjectStoreTaxRate subjectStore = lines.get(getRow());
        if (subjectStore.isEmpty()) {
          return null;
        }
        if (StringUtil.isNullOrBlank(field.getValue().getUuid())
            && StringUtil.isNullOrBlank(field.getValue().getCode()) == false) {
          field.clearMessages();
          return Message.error(
              SubjectMessages.M.cannotFindCode(field.getCaption(), field.getValue().getCode()),
              field);
        }
        for (int i = 0; i < getRow(); i++) {
          BSubjectStoreTaxRate detail = lines.get(i);
          if (detail != null && field.getRawValue() != null
              && StringUtil.isNullOrBlank(field.getRawValue().getUuid()) == false
              && field.getRawValue().equals(detail.getStore())) {
            return Message.error(SubjectMessages.M.storeRepeat(field.getValue().getCode()));
          }
        }
        return null;
      }
    }
  }

  private class TaxRateRenderer extends EditGridCellWidgetRenderer<TaxRateComboBox> {

    public TaxRateRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private TaxRateComboBox field;

    @Override
    protected TaxRateComboBox drawField(RGridColumnDef colDef, Object data) {
      field = new TaxRateComboBox();
      field.setConfigable(false);
      field.setWidth("100%");
      field.setCaption(PSubjectDef.constants.taxRate_rate());
      field.addChangeHandler(new ChangeHandler(){
        @Override
        public void onChange(ChangeEvent arg0) {
          BSubjectStoreTaxRate value = lines.get(getRow());
          value.setTaxRate(field.getValue());
          grid.refreshRow(getRow());
          taxRateField.addOption(field.getValue());
        }
      });
      field.setMaxDropdownRowCount(10);
      field.refreshOptions();
      return field;
    }

    @Override
    public void setValue(Object value) {
      BSubjectStoreTaxRate detail = lines.get(getRow());
      field.setValue(detail.getTaxRate());
    }
  }

  private class RemarkRenderer extends EditGridCellWidgetRenderer<RTextBox> {

    public RemarkRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private RTextBox field;

    @Override
    protected RTextBox drawField(RGridColumnDef colDef, Object data) {
      field = new RTextBox(colDef.getFieldDef());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addValueChangeHandler(new ValueChangeHandler<String>() {
        @Override
        public void onValueChange(ValueChangeEvent<String> arg0) {
          BSubjectStoreTaxRate line = lines.get(getRow());
          line.setRemark(field.getValue());
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BSubjectStoreTaxRate line = lines.get(getRow());
      field.setValue(line.getRemark());
    }

  }
}
