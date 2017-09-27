/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 *
 * 项目名： M3
 * 文件名： BankSearchPage.java
 * 模块说明：
 * 修改历史：
 * 2013-817 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.bank.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.bank.client.BankMessages;
import com.hd123.m3.account.gwt.bank.client.EPBank;
import com.hd123.m3.account.gwt.bank.client.biz.BBank;
import com.hd123.m3.account.gwt.bank.client.biz.BBankLogger;
import com.hd123.m3.account.gwt.bank.client.rpc.BankService;
import com.hd123.m3.account.gwt.bank.intf.client.BankUrlParams;
import com.hd123.m3.account.gwt.bank.intf.client.BankUrlParams.Search;
import com.hd123.m3.account.gwt.bank.intf.client.dd.PBankDef;
import com.hd123.m3.account.gwt.bank.intf.client.perm.BankPermDef;
import com.hd123.m3.commons.gwt.util.client.batchprocess.SimpleBatchProcesser;
import com.hd123.m3.commons.gwt.util.client.batchprocess.SimpleBatchProcesser.SimpleBatchProcesserCallback;
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
 * @author chenrizhang
 * 
 */
public class BankSearchPage extends BaseContentPage implements Search {
  private static EPBank ep = EPBank.getInstance(EPBank.class);

  private static BankSearchPage instance;

  public static BankSearchPage getInstance() throws ClientBizException {
    if (instance == null) {
      instance = new BankSearchPage();
    }
    return instance;
  }

  private BankSearchPage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
      afterDraw();
    } catch (Exception e) {
      throw new ClientBizException(BankMessages.M.cannotCreatePage("BankSearchPage"), e);
    }
  }

  private RAction createAction;
  private RAction deleteAction;

  private RPopupMenu lineMenu;
  private RMenuItem editOneMenuItem;
  private RMenuItem deleteOneMenuItem;

  private Action_handler actionHandler = new Action_handler();

  private RGrid grid;
  private RGridColumnDef codeCol;
  private RGridColumnDef nameCol;
  private RGridColumnDef storeCol;
  private RGridColumnDef bankCol;
  private RGridColumnDef accountCol;
  private RGridColumnDef remarkCol;
  private RGridColumnDef createInfoCol;
  private RGridColumnDef lastModifyInfoCol;

  private List<BBank> entities = new ArrayList<BBank>();
  private SimpleBatchProcesser batchProcessor;

  private void drawToolbar() {
    createAction = new RAction(RActionFacade.CREATE, actionHandler);
    getToolbar().add(new RToolbarButton(createAction));

    deleteAction = new RAction(RActionFacade.DELETE, actionHandler);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getToolbar().add(deleteButton);
  }

  private void drawSelf() {
    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    initWidget(root);

    drawLineMenu();
    root.add(drawGrid());
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.addClickHandler(new GridHandler());
    grid.setProvider(new GridDataProvider());

    codeCol = new RGridColumnDef(PBankDef.code);
    codeCol.setRendererFactory(new RHotItemRendererFactory(lineMenu, new HyperlinkRendererFactory(
        "80px")));
    codeCol.setWidth("120px");
    grid.addColumnDef(codeCol);

    nameCol = new RGridColumnDef(PBankDef.name);
    nameCol.setWidth("160px");
    grid.addColumnDef(nameCol);

    storeCol = new RGridColumnDef(PBankDef.store);
    storeCol.setWidth("160px");
    grid.addColumnDef(storeCol);

    bankCol = new RGridColumnDef(PBankDef.bank);
    bankCol.setWidth("120px");
    grid.addColumnDef(bankCol);

    accountCol = new RGridColumnDef(PBankDef.account);
    accountCol.setWidth("120px");
    grid.addColumnDef(accountCol);

    remarkCol = new RGridColumnDef(PBankDef.remark);
    remarkCol.setWidth("160px");
    grid.addColumnDef(remarkCol);

    createInfoCol = new RGridColumnDef(PBankDef.createInfo);
    createInfoCol.setWidth("240px");
    grid.addColumnDef(createInfoCol);

    lastModifyInfoCol = new RGridColumnDef(PBankDef.lastModifyInfo);
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

    deleteOneMenuItem = new RMenuItem(RActionFacade.DELETE, actionHandler);
    deleteOneMenuItem.setHotKey(null);
    lineMenu.addItem(deleteOneMenuItem);
  }

  private void afterDraw() throws ClientBizException {
    batchProcessor = new SimpleBatchProcesser(ep, PBankDef.TABLE_CAPTION);
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

    refreshTitle();
    refreshCommands();
    refreshGrid();
  }

  private boolean checkIn() {
    if (ep.isPermitted(BankPermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(BankMessages.M.search());
    ep.getTitleBar().appendAttributeText(ep.getModuleCaption());
  }

  private void refreshCommands() {
    createAction.setEnabled(ep.isPermitted(BankPermDef.CREATE));
    deleteAction.setEnabled(ep.isPermitted(BankPermDef.DELETE));

    getToolbar().rebuild();
  }

  private void refreshGrid() {
    RLoadingDialog.show();
    BankService.Locator.getService().getAll(new RBAsyncCallback2<List<BBank>>() {

      @Override
      public void onException(Throwable caught) {
        RLoadingDialog.hide();
        String msg = BankMessages.M.actionFailed(BankMessages.M.load(), ep.getModuleCaption());
        RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
          public void onClosed(ButtonConfig clickedButton) {
            grid.refresh();
          }
        });
      }

      @Override
      public void onSuccess(List<BBank> result) {
        RLoadingDialog.hide();
        entities = result;
        grid.refresh();
      }
    });
  }

  /*********************** private class ******************************/
  private class GridDataProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return entities == null ? 0 : entities.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (entities == null || row >= entities.size())
        return null;

      BBank bank = entities.get(row);
      if (col == codeCol.getIndex())
        return bank.getCode();
      if (col == nameCol.getIndex())
        return bank.getName();
      if (col == storeCol.getIndex())
        return bank.getStore() == null ? null : bank.getStore().toFriendlyStr();
      if (col == bankCol.getIndex())
        return bank.getBank();
      if (col == accountCol.getIndex())
        return bank.getAccount();
      if (col == remarkCol.getIndex())
        return bank.getRemark();
      if (col == createInfoCol.getIndex())
        return OperateInfoUtil.getOperateInfo(bank.getCreateInfo());
      if (col == lastModifyInfoCol.getIndex())
        return OperateInfoUtil.getOperateInfo(bank.getLastModifyInfo());

      return null;
    }
  }

  private class GridHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      BBank entity = entities.get(cell.getRow());
      if (entity == null)
        return;

      // 本模块内的跳转
      if (cell.getColumnDef().equals(codeCol)) {
        JumpParameters params = new JumpParameters(BankViewPage.START_NODE);
        params.getUrlRef().set(BankViewPage.PN_ENTITY_UUID, entity.getUuid());
        ep.jump(params);
      }
    }
  }

  private class Handler_lineMenu implements OpenHandler<RPopupMenu> {
    public void onOpen(OpenEvent<RPopupMenu> event) {
      editOneMenuItem.setEnabled(ep.isPermitted(BankPermDef.UPDATE));
      deleteOneMenuItem.setEnabled(ep.isPermitted(BankPermDef.DELETE));
    }
  }

  private class Action_handler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == createAction) {
        doCreate();
      } else if (event.getSource() == deleteAction) {
        doDelete();
      } else if (event.getSource() == editOneMenuItem) {
        doEditOne();
      } else if (event.getSource() == deleteOneMenuItem) {
        doDeleteOneConfirm();
      }
    }
  }

  private void doCreate() {
    JumpParameters params = new JumpParameters(BankUrlParams.Create.START_NODE);
    ep.jump(params);
  }

  private void doEditOne() {
    RHotItemRenderer hotItem = (RHotItemRenderer) editOneMenuItem.getContextWidget();
    BBank entity = entities.get(hotItem.getGridRow());

    JumpParameters params = new JumpParameters(BankEditPage.START_NODE);
    params.getUrlRef().set(BankEditPage.PN_ENTITY_UUID, entity.getUuid());
    ep.jump(params);
  }

  private void doDelete() {
    if (grid.getSelections().isEmpty()) {
      RMsgBox.showError("请先选择要删除的" + PBankDef.TABLE_CAPTION);
      return;
    }

    List<BBank> cond = new ArrayList<BBank>();
    for (Integer index : grid.getSelections()) {
      cond.add(entities.get(index));
    }

    batchProcessor.batchProcess("删除", cond.toArray(), new SimpleBatchProcesserCallback<BBank>() {

      @Override
      public void onAborted(String message, Throwable caught) {
        refreshGrid();
      }

      @Override
      public void onOver(boolean interrupted) {
        refreshGrid();
      }

      @Override
      public void execute(BBank entity, BatchProcesser<BBank> processer, AsyncCallback callback) {
        BankService.Locator.getService().delete(entity.getUuid(), entity.getVersion(), callback);
        // 记日志
        BBankLogger.getInstance().log(BankMessages.M.delete(), entity);
      }

    });

  }

  private void doDeleteOneConfirm() {
    RHotItemRenderer hotItem = (RHotItemRenderer) deleteOneMenuItem.getContextWidget();
    final BBank entity = entities.get(hotItem.getGridRow());

    getMessagePanel().clearMessages();

    String msg = "确定要删除" + ep.getModuleCaption() + "“" + entity.toFriendlyStr() + "”吗？";
    RMsgBox.showConfirm(msg, new RMsgBox.ConfirmCallback() {
      public void onClosed(boolean confirmed) {
        if (confirmed == false)
          return;
        doDeleteOne(entity);
      }
    });
  }

  private void doDeleteOne(final BBank entity) {
    RLoadingDialog.show(BankMessages.M.actionDoing(BankMessages.M.delete()));
    BankService.Locator.getService().delete(entity.getUuid(), entity.getVersion(),
        new RBAsyncCallback2<Void>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = BankMessages.M.actionFailed(BankMessages.M.delete(), ep.getModuleCaption());
            RMsgBox.showError(msg, caught, new RMsgBox.Callback() {
              public void onClosed(ButtonConfig clickedButton) {
                grid.refresh();
              }
            });
          }

          @Override
          public void onSuccess(Void result) {
            RLoadingDialog.hide();
            // 记日志
            BBankLogger.getInstance().log(BankMessages.M.delete(), entity);
            String msg = "删除" + ep.getModuleCaption() + "“" + entity.toFriendlyStr() + "”成功。";
            getMessagePanel().putInfoMessage(msg);
            refreshGrid();
          }
        });
  }
}
