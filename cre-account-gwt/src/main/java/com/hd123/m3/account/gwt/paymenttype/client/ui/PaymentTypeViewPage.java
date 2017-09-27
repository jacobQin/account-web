/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	PaymentTypeViewPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.paymenttype.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.grid.batch.GridBatchProcessCallback;
import com.hd123.m3.account.gwt.base.client.grid.batch.GridBatchProcessor;
import com.hd123.m3.account.gwt.paymenttype.client.EPPaymentType;
import com.hd123.m3.account.gwt.paymenttype.client.PaymentTypeMessages;
import com.hd123.m3.account.gwt.paymenttype.client.biz.BPaymentType;
import com.hd123.m3.account.gwt.paymenttype.client.biz.BPaymentTypeLogger;
import com.hd123.m3.account.gwt.paymenttype.client.rpc.PaymentTypeService;
import com.hd123.m3.account.gwt.paymenttype.client.ui.gadget.PaymentTypeModifyDialog;
import com.hd123.m3.account.gwt.paymenttype.intf.client.PaymentTypeUrlParams.View;
import com.hd123.m3.account.gwt.paymenttype.intf.client.dd.PPaymentTypeDef;
import com.hd123.m3.account.gwt.paymenttype.intf.client.perm.PaymentTypePermDef;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.operate.OperateInfoUtil;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.batchprocess.BatchProcesser;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog.ButtonConfig;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class PaymentTypeViewPage extends BaseContentPage implements View {

  public static PaymentTypeViewPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new PaymentTypeViewPage();
    return instance;
  }

  public PaymentTypeViewPage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(PaymentTypeMessages.M.cannotCreatePage("PaymentTypeViewPage"), e);
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
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (!checkIn()) {
      return;
    }

    refreshTitle();
    refresh();
    refreshCommand();
  }

  private static PaymentTypeViewPage instance;
  private EPPaymentType ep = EPPaymentType.getInstance();

  private List<BPaymentType> entityList;
  private RAction createAction;
  private RAction enableAction;
  private RAction disableAction;
  private RToolbarButton createButton;
  private RToolbarButton enableButton;
  private RToolbarButton disableButton;

  private RPopupMenu lineMenu;
  private RMenuItem editMenuItem;
  private RMenuItem enableMenuItem;
  private RMenuItem disableMenuItem;

  private RGrid grid;
  private RGridColumnDef codeCol;
  private RGridColumnDef nameCol;
  private RGridColumnDef stateCol;
  private RGridColumnDef remarkCol;
  private RGridColumnDef createInfoCol;
  private RGridColumnDef lastModifyInfoCol;

  private PaymentTypeModifyDialog createDialog;
  private PaymentTypeModifyDialog editDialog;

  private GridBatchProcessor<BPaymentType> batchProcess;

  private void drawToolbar() {
    createAction = new RAction(RActionFacade.CREATE, new Handler_createAction());
    createButton = new RToolbarButton(createAction);
    getToolbar().add(createButton);
    
    enableAction = new RAction(PaymentTypeMessages.M.enable(), new Handler_enableAction());
    enableButton = new RToolbarButton(enableAction);
    getToolbar().add(enableButton);

    disableAction = new RAction(PaymentTypeMessages.M.disable(), new Handler_disableAction());
    disableButton = new RToolbarButton(disableAction);
    getToolbar().add(disableButton);

  }

  private void drawSelf() {
    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(0);
    initWidget(root);

    drawLineMenu();

    root.add(drawGrid());

  }

  private void drawLineMenu() {

    lineMenu = new RPopupMenu();
    lineMenu.addOpenHandler(new Handler_lineMenu());

    editMenuItem = new RMenuItem(RActionFacade.EDIT, new Handler_editMenuItem());
    editMenuItem.setHotKey(null);
    lineMenu.addItem(editMenuItem);

    lineMenu.addSeparator();

    enableMenuItem = new RMenuItem(PaymentTypeMessages.M.enable(), new Handler_enableMenuItme());
    enableMenuItem.setHotKey(null);
    lineMenu.addItem(enableMenuItem);

    disableMenuItem = new RMenuItem(PaymentTypeMessages.M.disable(), new Handler_disableMenuItem());
    disableMenuItem.setHotKey(null);
    lineMenu.addItem(disableMenuItem);

  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setProvider(new GridDataProvider());
    grid.addClickHandler(new Handler_grid());

    codeCol = new RGridColumnDef(PPaymentTypeDef.code);
    codeCol.setRendererFactory(new RHotItemRendererFactory(lineMenu, new HyperlinkRendererFactory(
        "160px")));
    codeCol.setWidth("200px");
    grid.addColumnDef(codeCol);

    nameCol = new RGridColumnDef(PPaymentTypeDef.name);
    nameCol.setWidth("200px");
    nameCol.setOverflowEllipsis(true);
    grid.addColumnDef(nameCol);

    stateCol = new RGridColumnDef(PPaymentTypeDef.enabled);
    stateCol.setWidth("50px");
    stateCol.setOverflowEllipsis(true);
    grid.addColumnDef(stateCol);

    remarkCol = new RGridColumnDef(PPaymentTypeDef.remark);
    remarkCol.setWidth("250px");
    remarkCol.setOverflowEllipsis(true);
    grid.addColumnDef(remarkCol);

    createInfoCol = new RGridColumnDef(PPaymentTypeDef.createInfo);
    createInfoCol.setWidth("250px");
    createInfoCol.setOverflowEllipsis(true);
    grid.addColumnDef(createInfoCol);

    lastModifyInfoCol = new RGridColumnDef(PPaymentTypeDef.lastModifyInfo);
    lastModifyInfoCol.setOverflowEllipsis(true);
    grid.addColumnDef(lastModifyInfoCol);

    return grid;
  }

  private void refresh() {
    RLoadingDialog.show(CommonsMessages.M.loading());
    PaymentTypeService.Locator.getService().getAll(new RBAsyncCallback2<List<BPaymentType>>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = CommonsMessages.M.processError(CommonsMessages.M.load()
            + PPaymentTypeDef.TABLE_CAPTION);
        RMsgBox.showErrorAndBack(msg, caught);
      }

      @Override
      public void onSuccess(List<BPaymentType> result) {
        RLoadingDialog.hide();
        entityList = result;
        try {
          batchProcess = new GridBatchProcessor<BPaymentType>(entityList, grid,
              PPaymentTypeDef.TABLE_CAPTION);
        } catch (Exception e) {
          RMsgBox.showError(e.getMessage());
        }
        grid.refresh();
      }
    });
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(PaymentTypeMessages.M.moduleCaption());
  }

  private void refreshCommand() {
    createButton.setEnabled(ep.isPermitted(PaymentTypePermDef.CREATE));
    enableButton.setEnabled(ep.isPermitted(PaymentTypePermDef.ENABLE));
    disableButton.setEnabled(ep.isPermitted(PaymentTypePermDef.DISABLE));
  }

  private void refreshLineMenu(BPaymentType paymentType) {
    boolean isEnable = paymentType.isEnabled();

    enableMenuItem.setEnabled(ep.isPermitted(PaymentTypePermDef.ENABLE));
    disableMenuItem.setEnabled(ep.isPermitted(PaymentTypePermDef.DISABLE));
    editMenuItem.setEnabled(ep.isPermitted(PaymentTypePermDef.UPDATE));

    enableMenuItem.setVisible(!isEnable);
    disableMenuItem.setVisible(isEnable);
    editMenuItem.setVisible(isEnable);
  }

  private boolean checkIn() {
    if (!ep.isPermitted(PaymentTypePermDef.READ)) {
      NoAuthorized.open(PaymentTypeMessages.M.moduleCaption());
      return false;
    }
    return true;
  }

  /** -----------------------私有类定义 --------------------------- */

  private class Handler_createAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (createDialog == null)
        createDialog = new PaymentTypeModifyDialog();
      createDialog.doCreate();
      createDialog.center();
    }
  }

  private class Handler_enableAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      batchProcess.batchProcess(PaymentTypeMessages.M.enable(), new BPaymentType[] {},
          new GridBatchProcessCallback<BPaymentType>() {

            @Override
            public void execute(BPaymentType entity, BatchProcesser<BPaymentType> processer,
                AsyncCallback callback) {
              if (entity.isEnabled()) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              PaymentTypeService.Locator.getService().enable(entity.getUuid(), entity.getVersion(),
                  callback);
              BPaymentTypeLogger.getInstance().log(PaymentTypeMessages.M.enable(), entity);
            }

            @Override
            public void onOver(RGrid grid, boolean interrupted) {
              refresh();
            }

          });
    }
  }

  private class Handler_disableAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      batchProcess.batchProcess(PaymentTypeMessages.M.disable(), new BPaymentType[] {},
          new GridBatchProcessCallback<BPaymentType>() {

            @Override
            public void execute(BPaymentType entity, BatchProcesser<BPaymentType> processer,
                AsyncCallback callback) {
              if (entity.isEnabled() == false) {
                processer.getReport().reportSkipped();
                processer.next();
                return;
              }
              PaymentTypeService.Locator.getService().disable(entity.getUuid(),
                  entity.getVersion(), callback);
              BPaymentTypeLogger.getInstance().log(PaymentTypeMessages.M.disable(), entity);
            }

            @Override
            public void onOver(RGrid grid, boolean interrupted) {
              refresh();
            }
          });
    }
  }

  private class Handler_grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      BPaymentType entity = entityList.get(cell.getRow());
      if (entity == null)
        return;
      if (cell.getColumnDef().equals(codeCol)) {
        if (entity.isEnabled() && ep.isPermitted(PaymentTypePermDef.UPDATE)) {
          if (editDialog == null)
            editDialog = new PaymentTypeModifyDialog();

          editDialog.doLoad(entity.getUuid());
          editDialog.center();
        }
      }
    }
  }

  private class Handler_lineMenu implements OpenHandler<RPopupMenu> {

    @Override
    public void onOpen(OpenEvent<RPopupMenu> event) {
      RHotItemRenderer hotItme = (RHotItemRenderer) event.getTarget().getContextWidget();
      BPaymentType paymentType = entityList.get(hotItme.getGridRow());
      refreshLineMenu(paymentType);
    }
  }

  private class Handler_editMenuItem implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (editDialog == null)
        editDialog = new PaymentTypeModifyDialog();

      RMenuItem item = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) item.getContextWidget();
      BPaymentType paymentType = entityList.get(hotItem.getGridRow());

      editDialog.doLoad(paymentType.getUuid());
      editDialog.center();
    }
  }

  private class Handler_enableMenuItme implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMenuItem item = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) item.getContextWidget();
      BPaymentType paymentType = entityList.get(hotItem.getGridRow());

      doConfirmEnableOne(paymentType);
    }
  }

  private void doConfirmEnableOne(final BPaymentType paymentType) {
    getMessagePanel().clearMessages();

    String msg = PaymentTypeMessages.M.actionComfirm2(PaymentTypeMessages.M.enable(),
        PPaymentTypeDef.TABLE_CAPTION, paymentType.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doEnableOne(paymentType);
      }
    });
  }

  private void doEnableOne(BPaymentType paymentType) {
    RLoadingDialog.show(PaymentTypeMessages.M.enabling());
    PaymentTypeService.Locator.getService().enable(paymentType.getUuid(), paymentType.getVersion(),
        new RBAsyncCallback2<BPaymentType>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = CommonsMessages.M.processError(PaymentTypeMessages.M.enabed()
                + PPaymentTypeDef.TABLE_CAPTION);
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                grid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(BPaymentType result) {
            RLoadingDialog.hide();
            BPaymentTypeLogger.getInstance().log(PaymentTypeMessages.M.enable(), result);

            String msg = CommonsMessages.M.onSuccess(PaymentTypeMessages.M.enabed(),
                PPaymentTypeDef.TABLE_CAPTION, result.toFriendlyStr());
            getMessagePanel().putInfoMessage(msg);
            refresh();
          }
        });
  }

  private class Handler_disableMenuItem implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RMenuItem item = (RMenuItem) event.getSource();
      RHotItemRenderer hotItem = (RHotItemRenderer) item.getContextWidget();
      BPaymentType paymentType = entityList.get(hotItem.getGridRow());

      doConfirmDisableOne(paymentType);
    }
  }

  private void doConfirmDisableOne(final BPaymentType paymentType) {
    getMessagePanel().clearMessages();

    String msg = CommonsMessages.M.actionComfirm2(PaymentTypeMessages.M.disable(),
        PPaymentTypeDef.TABLE_CAPTION, paymentType.toFriendlyStr());
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doDisableOne(paymentType);
      }
    });
  }

  private void doDisableOne(BPaymentType paymentType) {
    RLoadingDialog.show(PaymentTypeMessages.M.disabling());
    PaymentTypeService.Locator.getService().disable(paymentType.getUuid(),
        paymentType.getVersion(), new RBAsyncCallback2<BPaymentType>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = CommonsMessages.M.processError(PaymentTypeMessages.M.disable()
                + PPaymentTypeDef.TABLE_CAPTION);
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                grid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(BPaymentType result) {
            RLoadingDialog.hide();
            BPaymentTypeLogger.getInstance().log(PaymentTypeMessages.M.disable(), result);

            String msg = CommonsMessages.M.onSuccess(PaymentTypeMessages.M.disable(),
                PPaymentTypeDef.TABLE_CAPTION, result.toFriendlyStr());
            getMessagePanel().putInfoMessage(msg);
            refresh();
          }
        });
  }

  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      if (entityList == null)
        return 0;
      return entityList.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (entityList == null || entityList.isEmpty())
        return null;

      BPaymentType entity = entityList.get(row);

      if (col == codeCol.getIndex())
        return entity.getCode();
      else if (col == nameCol.getIndex())
        return entity.getName();
      else if (col == stateCol.getIndex())
        return entity.isEnabled() ? PaymentTypeMessages.M.isUsing() : PaymentTypeMessages.M
            .disabled();
      else if (col == remarkCol.getIndex())
        return entity.getRemark();
      else if (col == createInfoCol.getIndex())
        return OperateInfoUtil.getOperateInfo(entity.getCreateInfo());
      else if (col == lastModifyInfoCol.getIndex())
        return OperateInfoUtil.getOperateInfo(entity.getLastModifyInfo());
      else
        return null;
    }
  }

}
