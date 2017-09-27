/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AdvanceLogPage.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-5 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.adv.client.ui;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.adv.client.EPAdvance;
import com.hd123.m3.account.gwt.adv.client.biz.BAdvanceLog;
import com.hd123.m3.account.gwt.adv.client.filter.AdvanceFilter;
import com.hd123.m3.account.gwt.adv.client.filter.AdvanceLogFilter;
import com.hd123.m3.account.gwt.adv.client.rpc.AdvanceService;
import com.hd123.m3.account.gwt.adv.intf.client.AdvanceMessages;
import com.hd123.m3.account.gwt.adv.intf.client.AdvanceUrlParams;
import com.hd123.m3.account.gwt.adv.intf.client.AdvanceUrlParams.Log;
import com.hd123.m3.account.gwt.adv.intf.client.dd.PAdvanceLogDef;
import com.hd123.m3.account.gwt.adv.intf.client.perm.AdvancePermDef;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.RPCExceptionDecoder;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.button.RButton;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateRangeField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RCustomPagingGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RPagingGrid;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RDateTimeRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RSimplePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.entrypoint.client.BaseContentPage;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;
import com.hd123.rumba.webframe.gwt.noauthorized.intf.client.NoAuthorized;

/**
 * @author zhuhairui
 * 
 */
public class AdvanceLogPage extends BaseContentPage implements Log {

  public static AdvanceLogPage getInstance() throws ClientBizException {
    if (instance == null)
      instance = new AdvanceLogPage();
    return instance;
  }

  public AdvanceLogPage() throws ClientBizException {
    super();
    try {
      drawToolbar();
      drawSelf();
    } catch (Exception e) {
      throw new ClientBizException(AdvanceMessages.M.cannotCreatePage("AdvanceLogPage"), e);
    }
  }

  @Override
  public String getStartNode() {
    return START_NODE;
  }

  @Override
  public void onHide() {
    if (subjectField != null) {
      subjectField.getBrowser().getFilterCallback().clearConditions();
      subjectField.getBrowser().getFilterCallback().onQuery();
    }
  }

  @Override
  public void onShow(JumpParameters params) {
    super.onShow(params);

    if (!checkIn())
      return;

    decodeParams(params);
    refreshFilter();
    refreshTitle();
    refreshGrid();
    accountUnitField.setFocus(true);
  }

  private static AdvanceLogPage instance;
  private EPAdvance ep = EPAdvance.getInstance();
  private AdvanceFilter filter;
  private AdvanceLogFilter logFilter;
  private boolean isRec;

  private RAction backAction;

  private AccountUnitUCNBox accountUnitField;
  private RCombinedField countpartField;
  private RTextBox counterpartUnitField;
  private RComboBox<String> directionField;
  private SubjectUCNBox subjectField;
  private RDateRangeField logTimeField;
  private RComboBox<String> counterpartTypeField;

  private RAction searchAction;
  private RAction clearFilterAction;

  private Label countLabel;
  private RGrid grid;
  private RPagingGrid<BAdvanceLog> pagingGrid;
  private RGridColumnDef logTimeCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef contractBillNumCol;
  private RGridColumnDef billTypeCol;
  private RGridColumnDef billNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef beforeTotalCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef afterTotal;

  private void drawToolbar() {
    backAction = new RAction(RActionFacade.BACK_TO_VIEW, new Handler_backAction());
    RToolbarButton createButton = new RToolbarButton(backAction);
    getToolbar().add(createButton);
  }

  private void drawSelf() {
    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);
    initWidget(root);

    root.add(drawFilterGadget());
    root.add(drawResultGadget());
  }

  private Widget drawFilterGadget() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(5);

    RForm filterForm = new RForm();
    panel.add(filterForm);

    accountUnitField = new AccountUnitUCNBox();
    accountUnitField.setCaption(AdvanceMessages.M.like(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business())));
    filterForm.addField(accountUnitField);

    counterpartUnitField = new RTextBox();

    counterpartTypeField = new RComboBox<String>();
    counterpartTypeField.setWidth("200px");
    counterpartTypeField.setEditable(false);
    counterpartTypeField.setNullOptionText(WidgetRes.M.all());
    counterpartTypeField.setMaxDropdownRowCount(10);
    for (Map.Entry<String, String> entry : ep.getCounterpartTypeMap().entrySet()) {
      counterpartTypeField.addOption(entry.getKey(), entry.getValue());
    }
    countpartField = new RCombinedField() {
      {

        setCaption(AdvanceMessages.M.like(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
            GRes.R.counterpart())));
        addField(counterpartTypeField, 0.4f);
        addField(counterpartUnitField, 0.6f);

      }
    };
    filterForm.addField(countpartField);

    directionField = new RComboBox();
    directionField.setWidth("200px");
    directionField.setEditable(false);
    directionField.setFieldCaption(AdvanceMessages.M.equal(PAdvanceLogDef.constants.direction()));
    directionField.addOption(DEPOSITREC);
    directionField.addOption(DEPOSITPAY);
    filterForm.addField(directionField);

    subjectField = new SubjectUCNBox(BSubjectType.predeposit.toString(),
        DirectionType.receipt.getDirectionValue());
    subjectField.setWidth("200px");
    subjectField.setFieldCaption(AdvanceMessages.M.equal(PAdvanceLogDef.constants.subject()));
    filterForm.addField(subjectField);

    logTimeField = new RDateRangeField();
    logTimeField.setWidth("200px");
    logTimeField.setFieldCaption(AdvanceMessages.M.between(AdvanceMessages.M.time()));
    filterForm.addField(logTimeField);

    filterForm.rebuild();

    HorizontalPanel commandPanel = new HorizontalPanel();
    commandPanel.setSpacing(3);

    searchAction = new RAction(RActionFacade.SEARCH, new Handler_searchAction());
    RButton queryButton = new RButton(searchAction);
    commandPanel.add(queryButton);

    clearFilterAction = new RAction(AdvanceMessages.M.clear(), new Handler_clearFilterAction());
    RButton clearFilterButton = new RButton(clearFilterAction);
    commandPanel.add(clearFilterButton);

    panel.add(RSimplePanel.decoratePadding(commandPanel, 0, 0, 0, 5));

    RCaptionBox box = new RCaptionBox();
    box.setCaption(AdvanceMessages.M.searchFilter());
    box.setContent(panel);
    box.setWidth("100%");
    box.getCaptionBar().setShowCollapse(true);
    return box;
  }

  private Widget drawResultGadget() {
    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    panel.add(drawGrid());
    pagingGrid = new PagingGrid(grid, new GridDataProvider());
    pagingGrid.addLoadDataHandler(new Handler_pagingGrid());
    panel.add(pagingGrid);

    RCaptionBox box = new RCaptionBox();
    box.setContentSpacing(3);
    box.setCaption(AdvanceMessages.M.resultSearch());
    box.setContent(panel);
    box.setWidth("100%");

    countLabel = new HTML(AdvanceMessages.M.resultTotal(0));
    box.getCaptionBar().addButton(RSimplePanel.decorateMargin(countLabel, 0, 0, 0, 10));
    return box;
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");

    logTimeCol = new RGridColumnDef(PAdvanceLogDef.time);
    logTimeCol.setWidth("90px");
    logTimeCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    grid.addColumnDef(logTimeCol);

    accountUnitCol = new RGridColumnDef(PAdvanceLogDef.accountUnit);
    accountUnitCol.setWidth("120px");
    grid.addColumnDef(accountUnitCol);

    counterpartCol = new RGridColumnDef(PAdvanceLogDef.counterpart);
    counterpartCol.setWidth("120px");
    grid.addColumnDef(counterpartCol);

    contractBillNumCol = new RGridColumnDef(PAdvanceLogDef.bill_billNumber);
    contractBillNumCol.setWidth("150px");
    grid.addColumnDef(contractBillNumCol);

    billTypeCol = new RGridColumnDef(PAdvanceLogDef.sourceBill_billType);
    billTypeCol.setWidth("100px");
    grid.addColumnDef(billTypeCol);

    billNumberCol = new RGridColumnDef(PAdvanceLogDef.sourceBill_billNumber);
    billNumberCol.setWidth("150px");
    billNumberCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    grid.addColumnDef(billNumberCol);

    subjectCol = new RGridColumnDef(PAdvanceLogDef.subject);
    subjectCol.setWidth("150px");
    grid.addColumnDef(subjectCol);

    beforeTotalCol = new RGridColumnDef(PAdvanceLogDef.beforeTotal);
    beforeTotalCol.setWidth("100px");
    grid.addColumnDef(beforeTotalCol);

    totalCol = new RGridColumnDef(PAdvanceLogDef.total);
    totalCol.setWidth("100px");
    grid.addColumnDef(totalCol);

    afterTotal = new RGridColumnDef(PAdvanceLogDef.afterTotal);
    afterTotal.setWidth("100px");
    grid.addColumnDef(afterTotal);

    grid.setAllColumnsOverflowEllipsis(true);

    return grid;
  }

  private boolean checkIn() {
    if (ep.isPermitted(AdvancePermDef.READ) == false) {
      NoAuthorized.open(ep.getModuleCaption());
      return false;
    }
    return true;
  }

  private void decodeParams(JumpParameters params) {
    isRec = Boolean.valueOf(params.getUrlRef().get(KEY_IS_REC)).booleanValue();

    logFilter = new AdvanceLogFilter();
    logFilter.decodeUrlParams(params.getUrlRef());
    logFilter.appendOrder(ORDER_BY_TIME);

    if (StringUtil.isNullOrBlank(logFilter.getAdvanceType()))
      logFilter.setAdvanceType(isRec ? DEPOSITREC : DEPOSITPAY);

    if (filter == null) {
      filter = new AdvanceFilter();
      filter.decodeUrlParams(params.getUrlRef());
    }
  }

  private void refreshFilter() {
    if (logFilter.getAccountUnitCode() != null)
      validateAccountUnit();
    else
      accountUnitField.clearValue();

    if (logFilter.getCounterpartUnit() != null)
      counterpartUnitField.setValue(logFilter.getCounterpartUnit());
    else
      counterpartUnitField.clearValue();

    if (logFilter.getAdvanceType() != null)
      directionField.setValue(logFilter.getAdvanceType());
    else
      directionField.setValue(DEPOSITREC);

    if (logFilter.getCounterpartType() != null)
      counterpartTypeField.setValue(logFilter.getCounterpartType());
    else
      counterpartTypeField.clearValue();

    if (logFilter.getAdvanceType().equals(DEPOSITREC)) {
      subjectField.setDirection(DirectionType.receipt.getDirectionValue());
    } else {
      subjectField.setDirection(DirectionType.payment.getDirectionValue());
    }
    validateSubjectCode();

    if (logFilter.getBeginLogTime() == null && logFilter.getEndLogTime() != null) {
      logTimeField.getStartField().clearValue();
      logTimeField.setEndDate(logFilter.getEndLogTime());
    } else if (logFilter.getBeginLogTime() != null && logFilter.getEndLogTime() == null) {
      logTimeField.setStartDate(logFilter.getBeginLogTime());
      logTimeField.getEndField().clearValue();
    } else if (logFilter.getBeginLogTime() != null && logFilter.getEndLogTime() != null) {
      logTimeField.setStartDate(logFilter.getBeginLogTime());
      logTimeField.setEndDate(logFilter.getEndLogTime());
    } else {
      logTimeField.clearValue();
    }

  }

  private void validateAccountUnit() {
    if (accountUnitField != null) {
      accountUnitField.query(logFilter.getAccountUnitCode(), new RBAsyncCallback2<BUCN>() {

        @Override
        public void onException(Throwable caught) {
          accountUnitField.clearMessages();
          accountUnitField.addErrorMessage(RPCExceptionDecoder.decode(caught).getMessage());
        }

        @Override
        public void onSuccess(BUCN result) {
          if (result == null) {
            accountUnitField.clearMessages();
          } else {
            accountUnitField.clearMessages();
            accountUnitField.setValue(result);
          }
        }
      });
    }
  }

  private void validateSubjectCode() {
    if (subjectField != null) {
      subjectField.query(logFilter.getSubject(), new RBAsyncCallback2<BSubject>() {

        @Override
        public void onException(Throwable caught) {
          subjectField.clearMessages();
          subjectField.addErrorMessage(RPCExceptionDecoder.decode(caught).getMessage());
        }

        @Override
        public void onSuccess(BSubject result) {
          if (result == null) {
            subjectField.clearMessages();
            // subjectField.addErrorMessage(AdvanceMessages.M.cannotFind(AdvanceMessages.M.subject()));
          } else {
            subjectField.clearMessages();
            subjectField.setValue(new BUCN(result.getUuid(), result.getCode(), result.getName()));
          }
        }
      });
    }
  }

  private void refreshTitle() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(AdvanceMessages.M.advance());
    ep.getTitleBar().appendAttributeText(AdvanceMessages.M.useLog());
  }

  private void refreshGrid() {
    grid.rebuild();

    // 设置每页记录数
    pagingGrid.setPageSize(logFilter.getPageSize());
    pagingGrid.gotoPage(logFilter.getPage());
  }

  private class Handler_backAction implements ClickHandler {
    public void onClick(ClickEvent event) {
      JumpParameters params = new JumpParameters(AdvanceUrlParams.Search.START_NODE);
      if (filter != null) {
        filter.encodeUrlParams(params.getUrlRef());
        filter = null;
      }
      ep.jump(params);
    }
  }

  private class Handler_searchAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      setValueToFilter();
      logFilter.setPage(0);

      JumpParameters params = new JumpParameters(START_NODE);
      logFilter.encodeUrlParams(params.getUrlRef());
      params.getUrlRef().set(KEY_IS_REC, isRec ? "true" : "false");
      ep.jump(params);
    }
  }

  private class Handler_clearFilterAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      logFilter.clear();
      clearFilter();
      counterpartUnitField.setFocus(true);
    }
  }

  /** 将筛选面板的值设置到过滤器中 */
  private void setValueToFilter() {
    logFilter.setAccountUnitCode(accountUnitField.getValue().getCode());
    logFilter.setCounterpartUnit(counterpartUnitField.getValue());
    logFilter.setCounterpartType(counterpartTypeField.getValue());
    logFilter.setAdvanceType(directionField.getValue());
    logFilter.setSubject(subjectField.getValue().getCode());
    logFilter.setBeginLogTime(logTimeField.getStartDate());
    logFilter.setEndLogTime(logTimeField.getEndDate());
  }

  private void clearFilter() {
    accountUnitField.clearValue();
    counterpartUnitField.clearValue();
    counterpartTypeField.clearValue();
    subjectField.clearValue();
    directionField.setValue(DEPOSITREC);
    logTimeField.clearValue();
  }

  private class PagingGrid extends RCustomPagingGrid<BAdvanceLog> {

    public PagingGrid(RGrid grid, RPageDataProvider<BAdvanceLog> provider) {
      super(grid, provider);
    }

    @Override
    protected boolean onGotoPage(int page, int pageSize) {
      logFilter.setPage(page);
      logFilter.setPageSize(pageSize);

      JumpParameters params = new JumpParameters(START_NODE);
      logFilter.encodeUrlParams(params.getUrlRef());
      params.getUrlRef().set(KEY_IS_REC, isRec ? "true" : "false");
      ep.jump(params);
      return false;
    }

    @Override
    protected boolean onSort(RGridColumnDef sortColumn, OrderDir sortDir) {
      logFilter.clearOrders();
      logFilter.appendOrder(sortColumn.getName(), sortDir);

      JumpParameters params = new JumpParameters(START_NODE);
      logFilter.encodeUrlParams(params.getUrlRef());
      params.getUrlRef().set(KEY_IS_REC, isRec ? "true" : "false");
      ep.jump(params);
      return false;
    }
  }

  private class GridDataProvider implements RPageDataProvider<BAdvanceLog> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAdvanceLog>> callback) {
      AdvanceService.Locator.getService().queryLog(logFilter, callback);
    }

    @Override
    public Object getData(int row, int col, BAdvanceLog rowData, List<BAdvanceLog> pageData) {
      if (col == logTimeCol.getIndex())
        return rowData.getTime();
      else if (col == accountUnitCol.getIndex())
        return rowData.getAccountUnit() == null ? null : rowData.getAccountUnit().getNameCode();
      else if (col == counterpartCol.getIndex())
        return rowData.getCounterpart().toFriendlyStr(ep.getCounterpartTypeMap());
      else if (col == contractBillNumCol.getIndex())
        return rowData.getContractBillNum();
      else if (col == billTypeCol.getIndex())
        return rowData.getSourceBill() != null ? rowData.getSourceBill().getBillType() != null ? ep
            .getBillType().get(rowData.getSourceBill().getBillType()) : null : null;
      else if (col == billNumberCol.getIndex()) {
        if (rowData.getSourceBill() == null)
          return null;
        BDispatch dispatch = new BDispatch(rowData.getSourceBill().getBillType());
        dispatch.addParams(GRes.R.dispatch_key(), rowData.getSourceBill().getBillNumber());
        return dispatch;
      } else if (col == subjectCol.getIndex())
        return rowData.getSubject().getNameCode();
      else if (col == beforeTotalCol.getIndex())
        return M3Format.fmt_money.format(rowData.getBeforeTotal().doubleValue());
      else if (col == totalCol.getIndex())
        return M3Format.fmt_money.format(rowData.getTotal().doubleValue());
      else if (col == afterTotal.getIndex())
        return M3Format.fmt_money.format(rowData.getAfterTotal().doubleValue());
      return null;
    }
  }

  private class Handler_pagingGrid implements LoadDataHandler {
    public void onLoadData(LoadDataEvent event) {
      countLabel.setText(AdvanceMessages.M.resultTotal(pagingGrid.getTotalCount()));
    }
  }

}
