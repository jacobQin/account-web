/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	SelectCounterpartGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-26 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui.gadget.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.FloorUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.PositionUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.BPositionType;
import com.hd123.m3.account.gwt.paymentnotice.client.EPPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.PaymentNoticeMessages;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.QBatchStatement;
import com.hd123.m3.account.gwt.paymentnotice.client.rpc.PaymentNoticeService;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.PaymentNoticeBatchPage;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.filter.BatchFilter;
import com.hd123.m3.account.gwt.paymentnotice.intf.client.PaymentNoticeUrlParams;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.button.RButton;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RPagingGrid;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepPanel;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepState;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;

/**
 * 选择对方单位
 * 
 * @author huangjunxian
 * 
 */
public class PNSelectCounterpartGadget extends Composite implements RRoadmapStepPanel, RValidatable {

  private PaymentNoticeBatchPage page;
  private EPPaymentNotice ep = EPPaymentNotice.getInstance();

  private Handler_action actionHandler = new Handler_action();
  private List<Message> messages = new ArrayList<Message>();

  private BatchFilter filter;
  private RCaptionBox filterBox;
  private RForm filterForm;
  private AccountUnitUCNBox accountUnitField;

  private RCombinedField countpartField;
  private CounterpartUCNBox counterpartUCNBox;
  private RViewStringField countpartTypeViewField;

  private RComboBox<String> positionTypeField;
  private PositionUCNBox positionField;
  private RTextBox contractTitleField;
  private FloorUCNBox floorField;
  private RComboBox<String> coopModeField;
  private RComboBox<String> counterpartTypeField;

  private RAction searchAction;
  private RAction clearAction;

  private HTML resultTotal;
  private RGrid grid;
  private RPagingGrid<QBatchStatement> pagingGrid;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef contractBillNumberCol;
  private RGridColumnDef contractTitleCol;
  private RGridColumnDef floorCol;
  private RGridColumnDef coopModeCol;

  public PNSelectCounterpartGadget(PaymentNoticeBatchPage page) {
    super();
    this.page = page;
    filter = new BatchFilter();

    RVerticalPanel rootPanel = new RVerticalPanel();
    rootPanel.setWidth("100%");
    rootPanel.setSpacing(5);

    rootPanel.add(drawFilterGadget());
    rootPanel.add(drawGrid());
    initWidget(rootPanel);
  }

  private Widget drawFilterGadget() {
    RVerticalPanel contentPanel = new RVerticalPanel();
    contentPanel.setWidth("100%");

    filterForm = new RForm(2);
    filterForm.setWidth("100%");
    contentPanel.add(filterForm);

    accountUnitField = new AccountUnitUCNBox();
    accountUnitField.setCaption(PaymentNoticeMessages.M.valueEquals(ep.getFieldCaption(
        GRes.FIELDNAME_BUSINESS, GRes.R.business())));
    filterForm.addField(accountUnitField);

    counterpartUCNBox = new CounterpartUCNBox(ep.getCaptionMap());
    counterpartUCNBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    counterpartUCNBox.addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        if (ep.getCounterpartTypeMap().size() > 1)
          countpartTypeViewField.setValue(counterpartUCNBox.getRawValue() == null ? null : ep
              .getCounterpartTypeMap().get(counterpartUCNBox.getRawValue().getCounterpartType()));

      }
    });
    countpartTypeViewField = new RViewStringField();

    countpartField = new RCombinedField() {
      {
        setCaption(PaymentNoticeMessages.M.valueEquals(ep.getFieldCaption(
            GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart())));
        if (ep.getCounterpartTypeMap().size() > 1) {
          addField(counterpartUCNBox, 0.85f);
          addField(new HTML(), 0.05f);
          addField(countpartTypeViewField, 0.1f);
        } else {
          addField(counterpartUCNBox, 1);

        }
      }
    };
    filterForm.addField(countpartField);

    positionTypeField = new RComboBox<String>();
    positionTypeField.setEditable(false);
    positionTypeField.setNullOptionText(PaymentNoticeMessages.M.all());
    positionTypeField.addOption(BPositionType.shoppe.name(), BPositionType.shoppe.getCaption());
    positionTypeField.addOption(BPositionType.booth.name(), BPositionType.booth.getCaption());
    positionTypeField.addOption(BPositionType.adPlace.name(), BPositionType.adPlace.getCaption());
    positionTypeField.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        filter.setPositionType(event.getValue());
        positionField.setType(event.getValue());
        positionField.clearValue();
        filter.setPositionUuid(null);
      }
    });

    positionField = new PositionUCNBox(null);
    positionField.getBrowser().setCaption(
        PaymentNoticeMessages.M.seleteData(PaymentNoticeMessages.M.positon()));

    RCombinedField combinField = new RCombinedField() {
      {
        setCaption(PaymentNoticeMessages.M.valueEquals(PaymentNoticeMessages.M.positon()));
        addField(positionTypeField, 0.2f);
        addField(positionField, 0.8f);
        setValidator(positionField.getValidator());
      }
    };
    filterForm.addField(combinField);

    contractTitleField = new RTextBox(PaymentNoticeMessages.M.like(PaymentNoticeMessages.M
        .contractTitle()));
    filterForm.addField(contractTitleField);

    floorField = new FloorUCNBox();
    floorField.setCaption(PaymentNoticeMessages.M.valueEquals(PaymentNoticeMessages.M.floor()));
    floorField.getBrowser().setCaption(
        PaymentNoticeMessages.M.seleteData(PaymentNoticeMessages.M.floor()));
    filterForm.addField(floorField);

    coopModeField = new RComboBox<String>(
        PaymentNoticeMessages.M.valueEquals(PaymentNoticeMessages.M.coopMode()));
    coopModeField.setEditable(false);
    coopModeField.setNullOptionText(PaymentNoticeMessages.M.all());
    coopModeField.setMaxDropdownRowCount(10);
    if (ep.getCoopModes() != null) {
      for (String value : ep.getCoopModes()) {
        coopModeField.addOption(value, value);
      }
    }
    filterForm.addField(coopModeField);

    counterpartTypeField = new RComboBox<String>(ep.getFieldCaption(
        GRes.FIELDNAME_COUNTERPART_TYPE, GRes.R.counterpartType())
        + PaymentNoticeMessages.M.equal());
    counterpartTypeField.setEditable(false);
    counterpartTypeField.setNullOptionText(PaymentNoticeMessages.M.all());
    counterpartTypeField.setMaxDropdownRowCount(10);
    for (Map.Entry<String, String> entry : ep.getCounterpartTypeMap().entrySet()) {
      counterpartTypeField.addOption(entry.getKey(), entry.getValue());
    }
    filterForm.addField(counterpartTypeField);

    HorizontalPanel hp = new HorizontalPanel();
    hp.setSpacing(5);
    contentPanel.add(hp);

    searchAction = new RAction(RActionFacade.SEARCH, actionHandler);
    RButton searchButton = new RButton(searchAction);
    hp.add(searchButton);

    clearAction = new RAction(PaymentNoticeMessages.M.clear(), actionHandler);
    RButton clearButton = new RButton(clearAction);
    hp.add(clearButton);

    filterBox = new RCaptionBox();
    filterBox.setWidth("100%");
    filterBox.setCaption(PaymentNoticeMessages.M.searchCondition());
    filterBox.setStyleName(RCaptionBox.STYLENAME_STANDARD);
    filterBox.setContent(contentPanel);
    filterBox.getCaptionBar().setShowCollapse(true);
    return filterBox;
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setHoverRow(true);

    accountUnitCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    accountUnitCol.setWidth("200px");
    accountUnitCol.setSortable(true);
    accountUnitCol.setName(PaymentNoticeUrlParams.ORDER_BY_ACCOUNTUNIT_CODE);
    grid.addColumnDef(accountUnitCol);

    counterpartCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    counterpartCol.setWidth("200px");
    counterpartCol.setSortable(true);
    counterpartCol.setName(PaymentNoticeUrlParams.ORDER_BY_COUNTERPART_CODE);
    grid.addColumnDef(counterpartCol);

    contractBillNumberCol = new RGridColumnDef(PaymentNoticeMessages.M.contract_number());
    contractBillNumberCol.setWidth("200px");
    contractBillNumberCol.setSortable(true);
    contractBillNumberCol.setName(PaymentNoticeUrlParams.ORDER_BY_CONTRACTBILLNUMBER);
    grid.addColumnDef(contractBillNumberCol);

    contractTitleCol = new RGridColumnDef(PaymentNoticeMessages.M.contractTitle());
    contractTitleCol.setWidth("100px");
    grid.addColumnDef(contractTitleCol);

    floorCol = new RGridColumnDef(PaymentNoticeMessages.M.floor());
    floorCol.setWidth("200px");
    grid.addColumnDef(floorCol);

    coopModeCol = new RGridColumnDef(PaymentNoticeMessages.M.coopMode());
    coopModeCol.setWidth("200px");
    grid.addColumnDef(coopModeCol);

    grid.setAllColumnsOverflowEllipsis(true);

    grid.addSelectionHandler(new SelectionHandler<Integer>() {

      @Override
      public void onSelection(SelectionEvent<Integer> event) {
        refreshNextButton();
      }
    });
    grid.addLoadDataHandler(new LoadDataHandler() {

      @Override
      public void onLoadData(LoadDataEvent event) {
        refreshNextButton();
      }
    });

    pagingGrid = new RPagingGrid<QBatchStatement>(grid, new GridDataProvider());
    pagingGrid.getPagingBar().setVisible(false);

    RCaptionBox box = new RCaptionBox();

    resultTotal = new HTML(PaymentNoticeMessages.M.resultTotal(0));
    box.getCaptionBar().addButton(resultTotal);

    box.setCaption(PaymentNoticeMessages.M.searchResult());
    box.setStyleName(RCaptionBox.STYLENAME_STANDARD);
    box.setContent(pagingGrid);
    return box;
  }

  @Override
  public void onShow() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(PaymentNoticeMessages.M.batch_caption(ep.getModuleCaption()));
    ep.getTitleBar().appendAttributeText(
        PaymentNoticeMessages.M.batch_select()
            + ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));

    refresh();
    page.removeRoadMapNextButtonHandlers();
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

      @Override
      public void execute() {
        refreshNextButton();
      }
    });
  }

  public void refreshNextButton() {
    RButton nextButton = PaymentNoticeBatchPage.getRoadMapNextButton(page.getRoadMapPanel());
    if (nextButton == null)
      return;

    nextButton.setText(PaymentNoticeMessages.M.batch_next());
    nextButton.setEnabled(validate());
    page.getRoadMapPanel().getRoadmap().getStep(2)
        .setState(nextButton.isEnabled() ? RRoadmapStepState.ENABLED : RRoadmapStepState.DISABLED);
    page.getRoadMapPanel().getRoadmap().getStep(3).setState(RRoadmapStepState.DISABLED);
  }

  private void refresh() {
    filterBox.expand();
    grid.setSort(accountUnitCol, OrderDir.asc, false);

    refreshGrid();
    grid.selectAllRows();
  }

  public void clearFilter() {
    filter.setCounterpartUuid(null);
    filter.setAccountUnitUuid(null);
    filter.setPositionType(null);
    filter.setPositionUuid(null);
    filter.setContractTitle(null);
    filter.setFloor(null);
    filter.setCoopMode(null);
    filter.setCounterpartType(null);

    counterpartUCNBox.clearValue();
    countpartTypeViewField.clearValue();
    accountUnitField.clearValue();
    positionTypeField.clearValue();
    positionField.clearValue();
    contractTitleField.clearValue();
    floorField.clearValue();
    counterpartTypeField.clearValue();
    coopModeField.clearValue();
  }

  @Override
  public void onHide() {
    page.setCounterparts(pagingGrid.getSelections());
  }

  @Override
  public void clearValidResults() {
    messages.clear();

  }

  @Override
  public boolean isValid() {
    return pagingGrid.getSelections().isEmpty();
  }

  @Override
  public List<Message> getInvalidMessages() {
    messages.clear();
    if (pagingGrid.getSelections().isEmpty())
      messages.add(Message.error(PaymentNoticeMessages.M.batch_msg_emptyCounterparts(ep
          .getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()))));
    return messages;
  }

  @Override
  public boolean validate() {
    if (pagingGrid.getSelections().isEmpty())
      return false;
    return true;
  }

  /*************************************************/
  private class Handler_action implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == searchAction) {
        doSearch();
      } else if (event.getSource() == clearAction) {
        doClear();
      }
    }

  }

  private void doSearch() {
    filter.setCounterpartUuid(counterpartUCNBox.getValue().getUuid());
    filter.setAccountUnitUuid(accountUnitField.getValue().getUuid());
    filter.setPositionType(positionTypeField.getValue());
    filter.setPositionUuid(positionField.getValue().getUuid());
    filter.setContractTitle(contractTitleField.getValue());
    filter.setFloor(floorField.getValue().getUuid());
    filter.setCoopMode(coopModeField.getValue());
    filter.setCounterpartType(counterpartTypeField.getValue());
    refreshGrid();
  }

  private void refreshGrid() {
    GWTUtil.enableSynchronousRPC();
    pagingGrid.refresh();
    resultTotal.setHTML(PaymentNoticeMessages.M.resultTotal(pagingGrid.getValues().size()));
  }

  private void doClear() {
    accountUnitField.clearValue();
    counterpartUCNBox.clearValue();
    countpartTypeViewField.clearValue();
    positionTypeField.clearValue();
    positionField.clearValue();
    contractTitleField.clearValue();
    floorField.clearValue();
    coopModeField.clearValue();
    counterpartTypeField.clearValue();

    filter.setCounterpartUuid(null);
    filter.setAccountUnitUuid(null);
    filter.setPositionType(null);
    filter.setPositionUuid(null);
    filter.setContractTitle(null);
    filter.setFloor(null);
    filter.setCoopMode(null);
    filter.setCounterpartType(null);
    accountUnitField.setFocus(true);
  }

  private class GridDataProvider implements RPageDataProvider<QBatchStatement> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<QBatchStatement>> callback) {
      filter.setPage(0);
      filter.setPageSize(0);
      filter.clearOrders();
      if (sortField != null)
        filter.appendOrder(sortField, sortDir);
      else
        filter.appendOrder(PaymentNoticeUrlParams.ORDER_BY_ACCOUNTUNIT_CODE, OrderDir.asc);

      PaymentNoticeService.Locator.getService().queryBatchStatement(filter, callback);
    }

    @Override
    public Object getData(int row, int col, QBatchStatement rowData, List<QBatchStatement> pageData) {
      resultTotal.setHTML(PaymentNoticeMessages.M.resultTotal(pageData.size()));
      if (col == counterpartCol.getIndex())
        return rowData.getCounterpart() == null ? null : rowData.getCounterpart().toFriendlyStr(
            ep.getCounterpartTypeMap());
      else if (col == accountUnitCol.getIndex())
        return rowData.getAccountUnit() == null ? null : rowData.getAccountUnit().getNameCode();
      else if (col == contractBillNumberCol.getIndex())
        return rowData.getContractBillNumber();
      else if (col == contractTitleCol.getIndex())
        return rowData.getContractTitle();
      else if (col == floorCol.getIndex())
        return rowData.getFloor() == null ? null : rowData.getFloor().getNameCode();
      else if (col == coopModeCol.getIndex())
        return rowData.getCoopMode();
      else
        return null;
    }

  }

}
