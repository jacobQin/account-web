/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： SubjectSearchPage.java
 * 模块说明：
 * 修改历史：
 * 2013-99 - cRazy - 创建。
 */
package com.hd123.m3.account.gwt.subject.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.subject.client.EPSubject;
import com.hd123.m3.account.gwt.subject.client.SubjectMessages;
import com.hd123.m3.account.gwt.subject.client.biz.BSubject;
import com.hd123.m3.account.gwt.subject.client.biz.BSubjectLogger;
import com.hd123.m3.account.gwt.subject.client.rpc.SubjectService;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectDef;
import com.hd123.m3.account.gwt.subject.intf.client.dd.PSubjectUsageDef;
import com.hd123.m3.account.gwt.subject.intf.client.perm.SubjectPermDef;
import com.hd123.m3.commons.gwt.util.client.flecs.BUCNCodec;
import com.hd123.m3.commons.gwt.util.client.grid.batch.PagingGridBatchProcessCallback;
import com.hd123.m3.commons.gwt.util.client.grid.batch.PagingGridBatchProcessor;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.operate.OperateInfoUtil;
import com.hd123.m3.commons.gwt.widget.client.ui.OptionComboBox;
import com.hd123.rumba.commons.gwt.mini.client.codec.CodecUtils;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
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
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author cRazy
 * 
 */
public class SubjectSearchPage extends BaseContentPage implements SubjectUrlParams.Search {
  private static SubjectSearchPage instance;
  private static EPSubject ep = EPSubject.getInstance();

  public static SubjectSearchPage getInstance() throws ClientBizException {
    if (instance == null) {
      instance = new SubjectSearchPage();
    }
    return instance;
  }

  private SubjectSearchPage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
      afterDraw();
    } catch (Exception e) {
      throw new ClientBizException(SubjectMessages.M.cannotCreatePage("SubjectSearchPage"), e);
    }
  }

  private RAction createAction;
  private RAction enableAction;
  private RAction disableAction;

  private RPopupMenu lineMenu;
  private RMenuItem editOneMenuItem;
  private RMenuItem enableOneMenuItem;
  private RMenuItem disableOneMenuItem;

  private Action_handler actionHandler = new Action_handler();

  private JumpParameters params;
  private String keyword;
  private RFlecsPanel flecsPanel;
  private FlecsConfigCodec flecsCodec;

  private RGrid grid;
  private RPagingGrid<BSubject> pagingGrid;
  private RGridColumnDef codeCol;
  private RGridColumnDef nameCol;
  private RGridColumnDef stateCol;
  private RGridColumnDef usageCol;
  private RGridColumnDef directionCol;
  private RGridColumnDef customTypeCol;
  private RGridColumnDef createInfoCol;
  private RGridColumnDef lastModifyInfoCol;

  private PagingGridBatchProcessor<BSubject> batchProcessor;

  private void drawToolbar() {
    createAction = new RAction(RActionFacade.CREATE, actionHandler);
    RToolbarButton createButton = new RToolbarButton(createAction);
    getToolbar().add(createButton);

    getToolbar().addSeparator();

    enableAction = new RAction(SubjectMessages.M.enable(), actionHandler);
    getToolbar().add(new RToolbarButton(enableAction));

    disableAction = new RAction(SubjectMessages.M.disable(), actionHandler);
    getToolbar().add(new RToolbarButton(disableAction));
  }

  private void drawSelf() {
    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    initWidget(root);

    root.add(drawFlecsAndGrid());
  }

  private Widget drawFlecsAndGrid() {
    drawLineMenu();

    drawGrid();
    pagingGrid = new RPagingGrid<BSubject>(grid, new GridDataProvider());

    drawFlecsPanel();

    return flecsPanel;
  }

  private void drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());

    flecsPanel.addField(PSubjectDef.code);
    flecsPanel.addField(PSubjectDef.name);
    flecsPanel.addField(PSubjectDef.state);
    flecsPanel.addField(PSubjectDef.customType);
    flecsPanel.addField(PSubjectUsageDef.usage);
    flecsPanel.addField(PSubjectDef.direction);
    flecsPanel.addField(PSubjectDef.createInfo_operator_id);
    flecsPanel.addField(PSubjectDef.createInfo_operator_fullName);
    flecsPanel.addField(PSubjectDef.createInfo_time);
    flecsPanel.addField(PSubjectDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PSubjectDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PSubjectDef.lastModifyInfo_time);
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

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.addClickHandler(new GridHandler());

    codeCol = new RGridColumnDef(PSubjectDef.code);
    codeCol.setRendererFactory(new RHotItemRendererFactory(lineMenu, new HyperlinkRendererFactory(
        "80px")));
    codeCol.setWidth("120px");
    codeCol.setSortable(true);
    codeCol.setAllowHiding(false);
    grid.addColumnDef(codeCol);

    nameCol = new RGridColumnDef(PSubjectDef.name);
    nameCol.setWidth("180px");
    nameCol.setSortable(true);
    grid.addColumnDef(nameCol);

    stateCol = new RGridColumnDef(PSubjectDef.state);
    stateCol.setWidth("80px");
    stateCol.setSortable(true);
    grid.addColumnDef(stateCol);

    usageCol = new RGridColumnDef(PSubjectUsageDef.usage);
    usageCol.setName(FIELD_USAGE);
    usageCol.setSortable(false);
    usageCol.setWidth("160px");
    grid.addColumnDef(usageCol);

    directionCol = new RGridColumnDef(PSubjectDef.direction);
    directionCol.setName(FIELD_DIRECTION);
    directionCol.setWidth("90px");
    directionCol.setSortable(true);
    grid.addColumnDef(directionCol);

    customTypeCol = new RGridColumnDef(PSubjectDef.constants.customType());
    customTypeCol.setName(FIELD_CUSTOMTYPE);
    customTypeCol.setWidth("120px");
    customTypeCol.setSortable(true);
    grid.addColumnDef(customTypeCol);

    createInfoCol = new RGridColumnDef(PSubjectDef.createInfo);
    createInfoCol.setName(FIELD_CREATE_INFO_TIME);
    createInfoCol.setSortable(true);
    createInfoCol.setWidth("250px");
    grid.addColumnDef(createInfoCol);

    lastModifyInfoCol = new RGridColumnDef(PSubjectDef.lastModifyInfo);
    lastModifyInfoCol.setName(FIELD_LASTMODIFY_INFO_TIME);
    lastModifyInfoCol.setSortable(true);
    grid.addColumnDef(lastModifyInfoCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  private void drawLineMenu() {
    lineMenu = new RPopupMenu();
    lineMenu.addOpenHandler(new Handler_lineMenu());

    editOneMenuItem = new RMenuItem(RActionFacade.EDIT, actionHandler);
    editOneMenuItem.setHotKey(null);
    lineMenu.addItem(editOneMenuItem);

    lineMenu.addSeparator();

    enableOneMenuItem = new RMenuItem(SubjectMessages.M.enable(), actionHandler);
    enableOneMenuItem.setHotKey(null);
    lineMenu.addItem(enableOneMenuItem);

    disableOneMenuItem = new RMenuItem(SubjectMessages.M.disable(), actionHandler);
    disableOneMenuItem.setHotKey(null);
    lineMenu.addItem(disableOneMenuItem);
  }

  private void afterDraw() throws ClientBizException {
    batchProcessor = new PagingGridBatchProcessor<BSubject>(pagingGrid, ep.getModuleCaption());
    flecsCodec = new FlecsConfigCodec();
    flecsCodec.addOperatorCodec(new DefaultOperatorCodec());
    flecsCodec.addOperandCodec(new BUCNCodec());
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
  }

  public JumpParameters getParams() {
    return params;
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);
    this.params = params;

    if (checkIn() == false)
      return;

    refreshTitle();
    refreshCommands();
    decodeParams(params);
    refreshFlecs();
  }

  private void decodeParams(JumpParameters params) {
    keyword = params.getUrlRef().get(PN_KEYWORD);
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
    ep.getTitleBar().setTitleText(SubjectMessages.M.search());
    ep.getTitleBar().appendAttributeText(ep.getModuleCaption());
  }

  private void refreshCommands() {
    createAction.setEnabled(ep.isPermitted(SubjectPermDef.CREATE));
    enableAction.setEnabled(ep.isPermitted(SubjectPermDef.ENABLE));
    disableAction.setEnabled(ep.isPermitted(SubjectPermDef.DISABLE));

    getToolbar().rebuild();
  }

  private void refreshFlecs() {
    grid.setSort(codeCol, OrderDir.asc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(keyword) == false) {
      flecsPanel
          .addCondition(new Condition(PSubjectDef.code, DefaultOperator.STARTS_WITH, keyword));
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
    conditions.add(new Condition(PSubjectDef.code, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PSubjectDef.name, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PSubjectDef.state, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PSubjectUsageDef.usage, DefaultOperator.EQUALS, null));
    return conditions;
  }

  private void refreshLineMenu(BSubject entity) {
    editOneMenuItem.setVisible(entity.isEnabled());
    enableOneMenuItem.setVisible(entity.isEnabled() == false);
    disableOneMenuItem.setVisible(entity.isEnabled());

    editOneMenuItem.setEnabled(ep.isPermitted(SubjectPermDef.UPDATE));
    enableOneMenuItem.setEnabled(ep.isPermitted(SubjectPermDef.ENABLE));
    disableOneMenuItem.setEnabled(ep.isPermitted(SubjectPermDef.DISABLE));
  }

  /*********************** private class ******************************/
  private class MyOperatorPolicy implements OperatorPolicy {
    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (field == PSubjectDef.state || field == PSubjectUsageDef.usage
          || field == PSubjectDef.direction || field == PSubjectDef.customType) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (field == PSubjectDef.state) {
        RComboBox<Boolean> stateField = new RComboBox<Boolean>(PSubjectDef.state);
        stateField.addOption(Boolean.TRUE, SubjectMessages.M.enabled());
        stateField.addOption(Boolean.FALSE, SubjectMessages.M.disabled());
        stateField.setEditable(false);
        return stateField;
      } else if (field == PSubjectUsageDef.usage) {
        RComboBox<String> usageField = new RComboBox<String>(PSubjectUsageDef.usage);
        usageField.setEditable(false);
        for (BSubjectUsage usage : ep.getUsages()) {
          usageField.addOption(usage.getCode(), usage.getName());
        }
        return usageField;
      } else if (field == PSubjectDef.direction) {
        return new DirectionField();
      } else if (field == PSubjectDef.customType) {
        final OptionComboBox unitBox = new OptionComboBox(EPSubject.KEY_CUSTOM_TYPE,
            PSubjectDef.constants.customType(), PSubjectDef.constants.customType(), false);
        unitBox.clearValue();
        unitBox.setMaxDropdownRowCount(10);
        unitBox.setRefreshWhenOpen(true);
        return unitBox;
      }
      return super.createOperandWidget(field, operator);
    }
  }

  private class DirectionField extends RComboBox<Integer> {
    public DirectionField() {
      super();
      addOption(DirectionType.receipt.getDirectionValue(), DirectionType.receipt.getCaption());
      addOption(DirectionType.payment.getDirectionValue(), DirectionType.payment.getCaption());
      setEditable(false);
      setRequired(false);
    }

    public void setFieldDef(FieldDef fieldDef) {
      super.setFieldDef(fieldDef);
    }
  }

  private class GridHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      BSubject entity = pagingGrid.getRowData(cell.getRow());
      if (entity == null)
        return;

      // 本模块内的跳转
      if (cell.getColumnDef().equals(codeCol)) {
        JumpParameters jumParams = new JumpParameters(SubjectViewPage.START_NODE);
        jumParams.getUrlRef().set(SubjectViewPage.PN_ENTITY_UUID, entity.getUuid());
        ep.jump(jumParams);
      }
    }
  }

  private class GridDataProvider implements RPageDataProvider<BSubject> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BSubject>> callback) {
      PageSort pageSort = new PageSort();
      pageSort.setPage(page);
      pageSort.setPageSize(pageSize);
      if (sortField != null)
        pageSort.appendOrder(sortField, sortDir);
      else
        pageSort.appendOrder(codeCol.getName(), OrderDir.asc);

      SubjectService.Locator.getService().query(flecsPanel.getCurrentConfig().getConditions(),
          pageSort, callback);
    }

    @Override
    public Object getData(int row, int col, BSubject rowData, List<BSubject> pageData) {
      if (col == codeCol.getIndex())
        return rowData.getCode();
      else if (col == nameCol.getIndex())
        return rowData.getName();
      else if (col == stateCol.getIndex())
        return rowData.isEnabled() ? SubjectMessages.M.enabled() : SubjectMessages.M.disabled();
      else if (col == usageCol.getIndex()) {
        if (rowData.getUsages() == null || rowData.getUsages().isEmpty())
          return null;

        List<String> usages = new ArrayList<String>();
        for (String usageCode : rowData.getUsages()) {
          BSubjectUsage subjectUsage = ep.getUsage(usageCode);
          if (subjectUsage != null) {
            usages.add(subjectUsage.getName());
          }
        }
        return CollectionUtil.toString(usages, '、');
      } else if (col == directionCol.getIndex())
        return DirectionType.getCaptionByValue(rowData.getDirection());
      else if (col == customTypeCol.getIndex()) {
        return rowData.getCustomType();
      } else if (col == createInfoCol.getIndex())
        return OperateInfoUtil.getOperateInfo(rowData.getCreateInfo());
      else if (col == lastModifyInfoCol.getIndex())
        return OperateInfoUtil.getOperateInfo(rowData.getLastModifyInfo());
      return null;
    }
  }

  private class Handler_lineMenu implements OpenHandler<RPopupMenu> {
    public void onOpen(OpenEvent<RPopupMenu> event) {
      RHotItemRenderer hotItem = (RHotItemRenderer) event.getTarget().getContextWidget();
      BSubject entity = pagingGrid.getPageData().get(hotItem.getGridRow());
      refreshLineMenu(entity);
    }
  }

  private class Action_handler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == createAction) {
        doCreate();
      } else if (event.getSource() == enableAction) {
        doEnable(true);
      } else if (event.getSource() == disableAction) {
        doEnable(false);
      } else if (event.getSource() == editOneMenuItem) {
        doEditOne();
      } else if (event.getSource() == enableOneMenuItem) {
        doEnableOneConfirm(true);
      } else if (event.getSource() == disableOneMenuItem) {
        doEnableOneConfirm(false);
      }
    }
  }

  private void doCreate() {
    JumpParameters jumParams = new JumpParameters(SubjectUrlParams.Create.START_NODE);
    ep.jump(jumParams);
  }

  private void doEditOne() {
    RHotItemRenderer hotItem = (RHotItemRenderer) editOneMenuItem.getContextWidget();
    BSubject entity = pagingGrid.getPageData().get(hotItem.getGridRow());

    JumpParameters jumParams = new JumpParameters(SubjectEditPage.START_NODE);
    jumParams.getUrlRef().set(SubjectEditPage.PN_ENTITY_UUID, entity.getUuid());
    ep.jump(jumParams);
  }

  private void doEnable(final boolean enable) {
    getMessagePanel().clear();
    String actionText = enable ? SubjectMessages.M.enable() : SubjectMessages.M.disable();
    batchProcessor.batchProcess(actionText, new BSubject[] {},
        new PagingGridBatchProcessCallback<BSubject>() {
          @Override
          public void execute(BSubject entity, BatchProcesser<BSubject> processer,
              AsyncCallback callback) {
            if (entity.isEnabled() == enable) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }

            SubjectService.Locator.getService().enable(entity.getUuid(), entity.getVersion(),
                enable, callback);
            BSubjectLogger.getInstatnce().log(
                enable ? SubjectMessages.M.enable() : SubjectMessages.M.disable(), entity);
          }
        });
  }

  private void doEnableOneConfirm(final boolean enable) {
    String actionText = enable ? SubjectMessages.M.enable() : SubjectMessages.M.disable();
    RHotItemRenderer hotItem = (RHotItemRenderer) enableOneMenuItem.getContextWidget();
    final BSubject entity = pagingGrid.getPageData().get(hotItem.getGridRow());

    getMessagePanel().clearMessages();

    String msg = SubjectMessages.M.actionComfirm2(actionText, ep.getModuleCaption(),
        entity.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEnableOne(entity, enable);
      }
    });
  }

  private void doEnableOne(final BSubject entity, final boolean enable) {
    final String actionText = enable ? SubjectMessages.M.enable() : SubjectMessages.M.disable();
    RLoadingDialog.show(SubjectMessages.M.actionDoing(actionText));
    SubjectService.Locator.getService().enable(entity.getUuid(), entity.getVersion(), enable,
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = SubjectMessages.M.actionFailed(actionText, ep.getModuleCaption());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                pagingGrid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            BSubjectLogger.getInstatnce().log(
                enable ? SubjectMessages.M.enable() : SubjectMessages.M.disable(), entity);

            String msg = SubjectMessages.M.onSuccess(actionText, ep.getModuleCaption(),
                entity.toFriendlyStr());
            getMessagePanel().putInfoMessage(msg);
            pagingGrid.refresh();
          }
        });
  }

}
