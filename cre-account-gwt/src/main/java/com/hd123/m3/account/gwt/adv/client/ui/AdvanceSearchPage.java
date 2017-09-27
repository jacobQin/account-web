/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AdvanceSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.client.ui;

import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.adv.client.EPAdvance;
import com.hd123.m3.account.gwt.adv.client.biz.BAdvanceData;
import com.hd123.m3.account.gwt.adv.client.filter.AdvanceFilter;
import com.hd123.m3.account.gwt.adv.client.rpc.AdvanceService;
import com.hd123.m3.account.gwt.adv.client.ui.gadget.AdvanceGadget;
import com.hd123.m3.account.gwt.adv.intf.client.AdvanceMessages;
import com.hd123.m3.account.gwt.adv.intf.client.AdvanceUrlParams.Search;
import com.hd123.m3.account.gwt.adv.intf.client.dd.PAdvanceDef;
import com.hd123.m3.account.gwt.adv.intf.client.perm.AdvancePermDef;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.layout.BillLayoutManager;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.button.RButton;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedFlowField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RCustomPagingGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RPagingGrid;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class AdvanceSearchPage extends BaseContentPage implements Search {

  public static AdvanceSearchPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new AdvanceSearchPage();
    return instance;
  }

  public AdvanceSearchPage() throws ClientBizException {
    super();
    try {
      filter = EPAdvance.getFilter();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(AdvanceMessages.M.cannotCreatePage("AdvanceSearchPage"), e);
    }
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

    decodeParams(params);
    if (checkIn() == false)
      return;

    refreshFilter();
    refreshTitle();
    refresh();
    refreshAdvanceGadget();
    if (ep.getUserStores().size() == 1) {
      accountUnitUCNBoxField.setValue(new BUCN(ep.getUserStores().get(0)));
      refreshAccountUnit(ep.getUserStores().get(0).getUuid());
    }
    counterpartUnitLikeField.setFocus(true);
  }

  private static AdvanceSearchPage instance;

  private AdvanceFilter filter;
  private EPAdvance ep = EPAdvance.getInstance();
  private BillLayoutManager layoutMgr;
  private boolean hasPayViewPerm;
  private boolean hasRecViewPerm;

  private RForm leftFilterForm;
  private RTextBox counterpartUnitLikeField;
  private RComboBox<String> counterpartTypeField;
  private RAction searchAction;

  private RGrid grid;
  private RPagingGrid<BUCN> pagingGrid;
  private RGridColumnDef codeCol;
  private RGridColumnDef nameCol;

  private void drawSelf() {
    VerticalPanel p = new VerticalPanel();
    p.setSpacing(8);
    p.setWidth("100%");
    p.setHeight("100%");
    initWidget(p);

    layoutMgr = new BillLayoutManager("650px", "300px");
    layoutMgr.setHeight("100%");
    layoutMgr.setMainWidget(drawLeftGadget());
    layoutMgr.setSecondaryWidget(drawRightGadget());
    layoutMgr.switchLayout(BillLayoutManager.LayoutType.LEFT_RIGTH);

    p.add(layoutMgr);
  }

  /**
   * 画左筛选面板
   * 
   */
  private Widget drawLeftGadget() {
    RVerticalPanel leftPanel = new RVerticalPanel();
    leftPanel.setWidth("100%");
    leftPanel.setSpacing(0);

    leftPanel.add(drawLeftFilterGadget());
    leftPanel.add(drawLeftResultGadget());

    return leftPanel;
  }

  private Widget drawLeftFilterGadget() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    leftFilterForm = new RForm(1);
    leftFilterForm.setWidth("300px");
    panel.add(leftFilterForm);

    counterpartUnitLikeField = new RTextBox(PAdvanceDef.counterpart_code);
    counterpartUnitLikeField.setWidth("150px");
    counterpartUnitLikeField.setFieldCaption(AdvanceMessages.M.like(ep.getFieldCaption(
        GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart())));
    leftFilterForm.addField(counterpartUnitLikeField);

    counterpartTypeField = new RComboBox<String>();
    counterpartTypeField.setCaption(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE,
        GRes.R.counterpartType())
        + AdvanceMessages.M.equal(""));
    counterpartTypeField.setEditable(false);
    counterpartTypeField.setWidth("150px");
    counterpartTypeField.setNullOptionText(CommonsMessages.M.all());
    for (Entry<String, String> entry : ep.getCounterpartTypeMap().entrySet()) {
      counterpartTypeField.addOption(entry.getKey(), entry.getValue());
    }
    leftFilterForm.addField(counterpartTypeField);

    leftFilterForm.addField(new RCombinedFlowField() {
      {
        setWidth("150px");
        searchAction = new RAction(RActionFacade.SEARCH, new Handler_searchAction());
        RButton queryButton = new RButton(searchAction);
        addFieldToRight(queryButton);
      }
    });

    leftFilterForm.rebuild();

    RCaptionBox box = new RCaptionBox();
    box.setCaption(AdvanceMessages.M.list(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart())));
    box.setContent(panel);
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    return box;
  }

  /**
   * 画表格界面
   * 
   */
  private Widget drawLeftResultGadget() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    panel.add(drawGrid());

    pagingGrid = new PagingGrid(grid, new GridDataProvider());
    panel.add(pagingGrid);

    RCaptionBox box = new RCaptionBox();
    box.setContentSpacing(0);
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setContent(panel);
    box.setWidth("100%");
    return box;
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(false);
    grid.setShowCurrentRow(true);
    grid.setHoverRow(true);
    grid.addClickHandler(new Handler_grid());

    codeCol = new RGridColumnDef(PAdvanceDef.counterpart_code);
    codeCol.setRendererFactory(new HyperlinkRendererFactory("60px"));
    codeCol.setWidth("80px");
    codeCol.setSortable(true);
    codeCol.setName(ORDER_BY_CODE);
    grid.addColumnDef(codeCol);

    nameCol = new RGridColumnDef(PAdvanceDef.counterpart_name);
    nameCol.setWidth("150px");
    grid.addColumnDef(nameCol);

    grid.setAllColumnsOverflowEllipsis(true);

    return grid;
  }

  private void decodeParams(JumpParameters params) {
    filter = EPAdvance.getFilter();
    filter.decodeUrlParams(params.getUrlRef());

    if (filter.getOrders().isEmpty()) {
      filter.appendOrder(codeCol.getName(), OrderDir.asc);
    }
  }

  private boolean checkIn() {
    hasPayViewPerm = ep.isPermitted(AdvancePermDef.READ);
    hasRecViewPerm = ep.isPermitted(AdvancePermDef.READ);
    if (ep.isPermitted(AdvancePermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void refreshFilter() {
    counterpartUnitLikeField.setValue(filter.getCounterpartUnitLike());
    leftFilterForm.rebuild();
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ep.getModuleCaption());
  }

  private void refresh() {
    grid.refresh();
    pagingGrid.refresh();
  }

  private void refreshAdvanceGadget() {
    recAdvanceGadget.setVisible(hasRecViewPerm);
    recAdvanceGadget.refresh();
    payAdvanceGadget.setVisible(hasPayViewPerm);
    payAdvanceGadget.refresh();
  }

  private class Handler_searchAction implements ClickHandler {
    public void onClick(ClickEvent event) {
      filter.setCounterpartUnitLike(counterpartUnitLikeField.getValue());
      filter.setCounterpartType(counterpartTypeField.getValue());
      filter.setPage(0);

      refresh();
    }
  }

  private class Handler_grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      RGridColumnDef colDef = cell.getColumnDef();
      int row = cell.getRow();

      if (colDef.equals(codeCol)) {
        BUCN counterpartUnit = pagingGrid.getPageData().get(row);
        if (counterpartUnit == null)
          return;
        refreshCounterpartUnit(counterpartUnit);
      }
    }
  }

  private class PagingGrid extends RCustomPagingGrid {

    public PagingGrid(RGrid grid, RPageDataProvider provider) {
      super(grid, provider);
      this.getPagingBar().setShowButtonText(false);
      this.getPagingBar().setShowPageSize(false);
    }

    @Override
    protected boolean onGotoPage(int page, int pageSize) {
      filter.setPage(page);

      JumpParameters params = new JumpParameters(START_NODE);
      filter.encodeUrlParams(params.getUrlRef());
      ep.jump(params);
      return false;
    }

    @Override
    protected boolean onSort(RGridColumnDef sortColumn, OrderDir sortDir) {
      filter.clearOrders();
      filter.appendOrder(sortColumn.getName(), sortDir);

      JumpParameters params = new JumpParameters(START_NODE);
      filter.encodeUrlParams(params.getUrlRef());
      ep.jump(params);
      return false;
    }
  }

  private class GridDataProvider implements RPageDataProvider<BCounterpart> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BCounterpart>> callback) {

      AdvanceService.Locator.getService().queryCounterpartUnit(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BCounterpart rowData, List<BCounterpart> pageData) {
      if (col == codeCol.getIndex())
        return rowData.getCode();
      else if (col == nameCol.getIndex())
        return rowData.toNameStr(ep.getCounterpartTypeMap());
      return null;
    }
  }

  // rightPage
  private AccountUnitUCNBox accountUnitUCNBoxField;
  private AdvanceGadget recAdvanceGadget;
  private AdvanceGadget payAdvanceGadget;

  private Widget drawRightGadget() {
    RVerticalPanel rightPanel = new RVerticalPanel();
    rightPanel.setWidth("100%");
    rightPanel.setSpacing(8);

    recAdvanceGadget = new AdvanceGadget(DEPOSITREC, true);
    payAdvanceGadget = new AdvanceGadget(DEPOSITPAY, false);

    rightPanel.add(drawRightFilterGadget());
    rightPanel.add(recAdvanceGadget);
    rightPanel.add(payAdvanceGadget);

    return rightPanel;
  }

  private Widget drawRightFilterGadget() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(5);

    RForm rightForm = new RForm();
    panel.add(rightForm);

    accountUnitUCNBoxField = new AccountUnitUCNBox();
    accountUnitUCNBoxField
        .setCaption(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business()));
    accountUnitUCNBoxField.addValueChangeHandler(new AccountUnitValuechange());
    rightForm.addField(accountUnitUCNBoxField);

    return panel;
  }

  private void refreshCounterpartUnit(final BUCN counterpartUnit) {
    AdvanceService.Locator.getService().getAdvanceDataByCounterpartUnitUuid(
        counterpartUnit.getUuid(), new RBAsyncCallback2<BAdvanceData>() {

          @Override
          public void onException(Throwable caught) {
            recAdvanceGadget.setAdvancesforProvider(null);
            payAdvanceGadget.setAdvancesforProvider(null);
            gadgetRefresh();
          }

          @Override
          public void onSuccess(BAdvanceData result) {
            accountUnitUCNBoxField.clearValue();
            recAdvanceGadget.setAdvancesforProvider(result.getRecAdvance());
            payAdvanceGadget.setAdvancesforProvider(result.getPayAdvance());
            gadgetRefresh();
          }
        });
  }

  private class AccountUnitValuechange implements ValueChangeHandler {

    @Override
    public void onValueChange(ValueChangeEvent event) {
      String accountUnitUuid = ((AccountUnitUCNBox) event.getSource()).getValue().getUuid();
      refreshAccountUnit(accountUnitUuid);
    }
  }

  private void refreshAccountUnit(String accountUnitUuid) {
    AdvanceService.Locator.getService().getAdvanceDataByAccountUnitUuid(accountUnitUuid,
        new RBAsyncCallback2<BAdvanceData>() {

          @Override
          public void onException(Throwable caught) {
            recAdvanceGadget.setAdvancesforProvider(null);
            payAdvanceGadget.setAdvancesforProvider(null);
            gadgetRefresh();
          }

          @Override
          public void onSuccess(BAdvanceData result) {
            recAdvanceGadget.setAdvancesforProvider(result.getRecAdvance());
            payAdvanceGadget.setAdvancesforProvider(result.getPayAdvance());
            gadgetRefresh();
          }
        });
  }

  private void gadgetRefresh() {
    recAdvanceGadget.refresh();
    payAdvanceGadget.refresh();
  }

}
