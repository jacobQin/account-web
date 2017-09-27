/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BaseSearchPageForInvoiceStock.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.stock.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.bpm.widget.base.client.biz.BTaskState;
import com.hd123.bpm.widget.interaction.client.BProcessClient;
import com.hd123.bpm.widget.interaction.client.biz.BUserTask;
import com.hd123.m3.account.gwt.ivc.stock.client.biz.BInvoiceStock;
import com.hd123.m3.commons.gwt.base.client.biz.BStandardBill;
import com.hd123.m3.commons.gwt.base.client.biz.IsStandardBill;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizAction;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizActions;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizFlow;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizFlowUtils;
import com.hd123.m3.commons.gwt.base.client.log.EntityLogger;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.BpmModuleUrlParams;
import com.hd123.m3.commons.gwt.bpm.client.EPBpmModule2;
import com.hd123.m3.commons.gwt.bpm.client.common.FMenuItem;
import com.hd123.m3.commons.gwt.bpm.client.ui.BaseBpmSearchPage;
import com.hd123.m3.commons.gwt.util.client.flecs.BUCNCodec;
import com.hd123.m3.commons.gwt.util.client.grid.batch.PagingGridBatchProcessCallback;
import com.hd123.m3.commons.gwt.util.client.grid.batch.PagingGridBatchProcessor;
import com.hd123.m3.commons.gwt.widget.client.biz.PrintingTemplate;
import com.hd123.m3.commons.gwt.widget.client.ui.print.BeforePrintCallback;
import com.hd123.m3.commons.gwt.widget.client.ui.print.BeforePrintHandler;
import com.hd123.m3.commons.gwt.widget.client.ui.print.PrintButton;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.codec.CodecUtils;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.flecs.client.FlecsConfig;
import com.hd123.rumba.gwt.flecs.client.FlecsQueryDef;
import com.hd123.rumba.gwt.flecs.client.RFlecsPanel;
import com.hd123.rumba.gwt.flecs.client.codec.DefaultOperatorCodec;
import com.hd123.rumba.gwt.flecs.client.codec.FlecsConfigCodec;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.batchprocess.BatchProcesser;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RPagingGrid;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRenderer;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarMenuButton;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author lixiaohong
 *
 */
public abstract class BaseSearchPageForInvoiceStock<T extends BStandardBill> extends
    BaseBpmSearchPage implements BeforePrintHandler {
  /** URL参数名：搜索条件。 */
  public static final String PN_FLECSCONFIG = "flecsconfig";
  /** URL参数名：页码 */
  public static final String PN_PAGE = "page";
  public static final String FIELD_BILLNUMBER = "billNumber";

  private RVerticalPanel root;
  protected RGrid grid;
  protected RPagingGrid<T> pagingGrid;
  protected PagingGridBatchProcessor<T> batchProcessor;
  protected RFlecsPanel flecsPanel;
  protected FlecsConfigCodec flecsCodec;
  private ActionHandler actionHandler;

  protected RToolbarMenuButton moreButton;
  protected PrintButton printButton = null;

  private List<FMenuItem> oneMenus;
  private List<FMenuItem> batchMenus;

  @Override
  protected abstract EPBpmModule2 getEP();

  /** 返回表格列 */
  protected abstract List<RGridColumnDef> createGridColumns();

  /** 绘制flecs panel */
  protected abstract RFlecsPanel drawFlecsPanel();

  /** 标识列定义, 一般是单号列 */
  protected abstract RGridColumnDef getIdentifyColumn();

  /** 默认排序列 */
  protected abstract RGridColumnDef getOrderColumn();

  /**
   * 默认查询条件
   *
   * @param params
   *          跳转参数
   * @return 条件列表
   */
  protected abstract List<Condition> getDefaultConditions(JumpParameters params);

  /** 读取表格数据 */
  protected abstract Object getGridData(int row, int col, T rowData, List<T> pageData);

  /** 返回一个空对象数组, 用于批量操作 */
  protected abstract T[] newEntityArray();

  /** root panel */
  protected VerticalPanel getRoot() {
    return root;
  }

  protected BaseSearchPageForInvoiceStock() {
    super();
    actionHandler = new ActionHandler();
    drawToolbar();
    drawSelf();
    afterDraw();
  }

  @Override
  protected RPagingGrid getPagingGrid() {
    return pagingGrid;
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);
    getEP().setCurrentParams(params);

    if (checkIn() == false) {
      return;
    }

    decodeParams(params);
    refreshCommands();
    refreshFlecs(params);
  }

  /**
   * 搜索之前调用，可以覆盖增加自定义的搜索条件
   *
   * @param definition
   */
  protected void beforeQuery(FlecsQueryDef definition) {

  }

  /** 授权检查 */
  protected boolean checkIn() {
    if (getEP().isPermitted(BAction.READ.getKey())) {
      return true;
    } else {
      NoAuthorized.open(getEP().getModuleCaption());
      return false;
    }
  }

  protected void decodeParams(JumpParameters params) {

  }

  protected void refreshCommands() {
    for (FMenuItem item : batchMenus) {
      BBizAction action = item.getBizAction();
      item.setEnabled(getEP().isPermitted(action.getAction()));
      item.setVisible(item.isEnabled());
    }
    getToolbar().rebuild();
  }

  protected String getSearchKeyWork(JumpParameters params) {
    return params.getUrlRef().get(getIdentifyColumn().getName());
  }

  protected void refreshFlecs(JumpParameters params) {
    grid.setSort(getOrderColumn(), OrderDir.asc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(getSearchKeyWork(params)) == false) {
      flecsPanel.addConditions(getDefaultConditions(params));
      flecsPanel.refresh();
    } else {
      String flecsStr = params.getUrlRef().get(PN_FLECSCONFIG);
      String pageInt = params.getUrlRef().get(PN_PAGE);
      int startPage = CodecUtils.decodeInt(pageInt, 0);
      if (flecsStr != null) {
        final FlecsConfig fc = flecsCodec.decode(flecsStr);
        flecsPanel.setCurrentConfig(fc, startPage);
      } else if (flecsPanel.getDefaultConfig() == null) {
        flecsPanel.addConditions(getDefaultConditions(params));
        flecsPanel.refresh();
      } else {
        flecsPanel.setCurrentConfig(flecsPanel.getDefaultConfig());
      }
    }
  }

  @Override
  protected void appendBatchMenu(RPopupMenu menu) {
    menu.addSeparator();
    for (FMenuItem item : batchMenus) {
      menu.addItem(item);
    }
  }

  @Override
  protected void appendLineMenu(RPopupMenu menu, Object bill) {
    T entity = (T) bill;
    lineMenu.addSeparator();

    for (FMenuItem item : oneMenus) {
      BBizAction action = item.getBizAction();
      if (isPermitted(action, entity)) {
        item.setEnabled(true);
        if (action.getSrcStates().indexOf(entity.getBizState()) >= 0) {
          lineMenu.addItem(item);
        }
      }
    }
  }

  /** 判断权限 */
  protected boolean isPermitted(BBizAction action, T entity) {
    if (action == null) {
      return false;
    }
    return getEP().isPermitted(action.getAction());
  }

  private void drawToolbar() {
    addCreateButton();

    getToolbar().addSeparator(Alignment.left);

    batchMenus = new ArrayList<FMenuItem>();
    BBizFlow flow = getEP().getBizFlow();
    for (BBizAction act : flow.getActions()) {
      if (!act.isBatch()) {
        continue;
      }
      FMenuItem item = createMenuItem(act, true);
      batchMenus.add(item);
    }
    moreButton = new RToolbarMenuButton(CommonsMessages.M.operate(), moreMenu);
    getToolbar().add(moreButton);

    // 打印
    drawPrintButton();
  }

  /** 打印模板（相对路径），返回空表示无打印功能 */
  protected String getPrintTemplate() {
    return getEP().getPrintTemplate();
  }

  protected void drawPrintButton() {
    if (getPrintTemplate() != null) {
      printButton = new PrintButton(getPrintTemplate(), getEP().getCurrentUser().getId());
      printButton.setEnabled(getEP().isPermitted(BAction.PRINT.getKey()));
      printButton.setBeforePrintHandler(this);
      getToolbar().add(printButton, Alignment.right);
      getToolbar().addSeparator(Alignment.right);
      // 刷新模板
      printButton.refresh();
    }
  }

  @Override
  public void beforePrint(final PrintingTemplate template, final String action,
      List<Map<String, String>> parameters, BeforePrintCallback callback) {
    if (template == null || template.getTemplate() == null) {
      return;
    }
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    for (Object o : getPagingGrid().getSelections()) {
      if (!(o instanceof IsStandardBill)) {
        continue;
      }
      IsStandardBill bill = (IsStandardBill) o;
      Map<String, String> map = new HashMap<String, String>();
      list.add(map);
      map.put(PrintingTemplate.KEY_UUID, bill.getUuid());
      map.put(PrintingTemplate.KEY_BILLNUMBER, bill.getBillNumber());
    }

    callback.execute(template, action, list);
  }

  protected void addCreateButton() {
    Widget widget = createCreateButton();
    if (widget != null) {
      getToolbar().add(widget);
    }
  }

  /** 创建按钮，有个性化需求可覆盖这个方法 ,如果不需要新建功能则返回null */
  protected Widget createCreateButton() {
    return getEP().getCreateWidget();
  }

  protected void drawSelf() {
    root = new RVerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    initWidget(root);

    drawLineMenu();
    drawGrid();
    flecsPanel = drawFlecsPanel();
    root.add(flecsPanel);
  }

  private void afterDraw() {
    batchProcessor = new PagingGridBatchProcessor<T>(pagingGrid, getEP().getModuleCaption());
    flecsCodec = createFlecsConfigCodec();
  }

  protected FlecsConfigCodec createFlecsConfigCodec() {
    FlecsConfigCodec codec = new FlecsConfigCodec();
    codec.addOperatorCodec(new DefaultOperatorCodec());
    codec.addOperandCodec(new BUCNCodec());
    return codec;
  }

  protected Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.addClickHandler(new GridClickHandler());
    grid.addSelectionHandler(new SelectionHandler<Integer>() {

      @Override
      public void onSelection(SelectionEvent<Integer> event) {
        if (printButton == null) {
          return;
        }
        Set<String> storeCodes = new HashSet<String>();
        for (Object o : pagingGrid.getSelections()) {
          if (o instanceof BInvoiceStock
              && StringUtil.isNullOrBlank(((BInvoiceStock) o).getAccountUnit().getUuid()) == false) {
            storeCodes.add(((BInvoiceStock) o).getAccountUnit().getCode());
          }
        }
        printButton.setStoreCodes(storeCodes.toArray(new String[] {}));
      }
    });

    List<RGridColumnDef> columns = createGridColumns();
    for (RGridColumnDef column : columns) {
      grid.addColumnDef(column);
    }

    grid.setAllColumnsOverflowEllipsis(true);

    pagingGrid = new RPagingGrid<T>(grid, new GridDataProvider());
    pagingGrid.setWidth("100%");
    pagingGrid.addLoadDataHandler(this);
    return grid;
  }

  protected void drawLineMenu() {
    oneMenus = new ArrayList<FMenuItem>();
    BBizFlow flow = getEP().getBizFlow();
    for (BBizAction act : flow.getActions()) {
      FMenuItem item = createMenuItem(act, false);
      oneMenus.add(item);
    }
  }

  protected FMenuItem createMenuItem(BBizAction act, boolean batch) {
    FMenuItem item;
    if (BBizActions.UPDATE.equals(act.getAction())) {
      item = new FMenuItem(RActionFacade.EDIT, actionHandler);
    } else if (BBizActions.DELETE.equals(act.getAction())) {
      item = new FMenuItem(RActionFacade.DELETE, actionHandler);
    } else {
      item = new FMenuItem(act.getActionName(), actionHandler);
    }
    item.setBizAction(act);
    item.setHotKey(null);
    item.setBatch(batch);
    return item;
  }

  protected void doChangeState(FMenuItem menuItem) {
    getMessagePanel().clearMessages();
    BBizAction action = menuItem.getBizAction();
    if (menuItem.isBatch()) {
      doBatchChangeBizState(action.getState(), action.getActionName());
    } else {
      RHotItemRenderer hotItem = (RHotItemRenderer) menuItem.getContextWidget();
      T entity = pagingGrid.getPageData().get(hotItem.getGridRow());
      if (action == null) {
        return;
      }
      if (BBizActions.UPDATE.equals(action.getAction())) {
        doEdit(entity);
      } else {
        doChangeOneBizState(entity, action.getState(), action.getActionName());
      }
    }
  }

  protected void doEdit(T entity) {
    JumpParameters params = new JumpParameters(BpmModuleUrlParams.START_NODE_EDIT);
    params.getUrlRef().set(getEP().getUrlBizKey(), entity.getUuid());
    getEP().jump(params);
  }

  protected void doChangeOneBizState(final T entity, final String state, final String actionName) {

    getEP().getServiceAgent().changeBizState(entity, state, actionName, new Command() {

      @Override
      public void execute() {
        String msg = CommonsMessages.M.onSuccess(actionName, getEP().getModuleCaption(),
            entity.getBillNumber());
        getMessagePanel().putInfoMessage(msg);
        pagingGrid.refresh();
      }
    });
  }

  protected void doBatchChangeBizState(final String state, final String actionName) {
    batchProcessor.batchProcess(actionName, newEntityArray(),
        new PagingGridBatchProcessCallback<T>() {

          @Override
          public void execute(T entity, BatchProcesser<T> processer, AsyncCallback callback) {
            if (BBizFlowUtils.needChangeState(getEP().getBizFlow(), entity.getBizState(), state) == false) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            getEP().getModuleService().changeBizState(entity.getUuid(), entity.getVersion(), state,
                callback);
          }

          @Override
          public void onSuccess(T entity, Object result) {
            EntityLogger.getInstance().log(actionName, entity);
            super.onSuccess(entity, result);
          }
        });
  }

  private class GridDataProvider implements RPageDataProvider<T> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<T>> callback) {
      FlecsQueryDef definition = buildQueryDef(page, pageSize, sortField, sortDir);

      beforeQuery(definition);
      getEP().getServiceAgent().query(definition, callback);
    }

    @Override
    public Object getData(int row, int col, T rowData, List<T> pageData) {
      return getGridData(row, col, rowData, pageData);
    }
  }

  protected FlecsQueryDef buildQueryDef(int page, int pageSize, String sortField, OrderDir sortDir) {
    FlecsQueryDef queryDef = flecsPanel.getQueryDef(page);
    queryDef.getConditions();
    queryDef.setPageSize(pageSize);
    if (StringUtil.isNullOrBlank(sortField)) {
      queryDef.setSortField(getIdentifyColumn().getName());
      queryDef.setSortDir(OrderDir.asc);
    } else {
      queryDef.setSortField(sortField);
      queryDef.setSortDir(sortDir);
    }
    return queryDef;
  }

  private class GridClickHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null) {
        return;
      }
      T entity = pagingGrid.getRowData(cell.getRow());
      if (entity == null) {
        return;
      }
      // 跳转处理
      if (cell.getColumnDef().equals(getIdentifyColumn())) {
        BUserTask task = getUserTasks().get(entity.getUuid());
        if (task == null) {
          getEP().jumpToViewPage(false, entity.getUuid(), null, false);
        } else {
          // 直接跳转任务页面
          BProcessClient client = BProcessClient.getInstance();
          if (BTaskState.claimed.equals(task.getState())
              || BTaskState.pending.equals(task.getState())
              || BTaskState.resolved.equals(task.getState())) {
            client.gotoExecutePage(task);
          } else {
            client.gotoViewPage(task);
          }
        }
      }
    }
  }

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() instanceof FMenuItem) {
        doChangeState((FMenuItem) event.getSource());
      }
    }
  }

}
