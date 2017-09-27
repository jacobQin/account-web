/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	SelectAccSettlePanel.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client.ui.gadget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.contract.accountsettle.client.AccountSettleMessages;
import com.hd123.m3.account.gwt.contract.accountsettle.client.EPAccountSettle;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BAccountSettle;
import com.hd123.m3.account.gwt.contract.accountsettle.client.rpc.AccountSettleFilter;
import com.hd123.m3.account.gwt.contract.accountsettle.client.rpc.AccountSettleService;
import com.hd123.m3.account.gwt.contract.accountsettle.client.ui.AccountSettlePage;
import com.hd123.m3.account.gwt.contract.intf.client.dd.PSettlementDef;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.button.RButton;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RPagingGrid;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepPanel;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepState;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 选择出账账单
 * 
 * @author huangjunxian
 * 
 */
public class AccSelectAccSettlePanel extends Composite implements RRoadmapStepPanel, RValidatable {

  private AccountSettlePage page;

  private EPAccountSettle ep = EPAccountSettle.getInstance();

  private List<Message> messages = new ArrayList<Message>();

  private AccountSettleFilter filter;

  private RGrid grid;
  private RPagingGrid<BAccountSettle> pagingGrid;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef settleNameCol;
  private RGridColumnDef settleDateRangeCol;
  private RGridColumnDef contractTitleCol;
  private RGridColumnDef contractNumberCol;
  private RGridColumnDef floorCol;
  private RGridColumnDef coopModeCol;
  private RGridColumnDef accountTimeCol;
  private RGridColumnDef calcTypeCol;

  public AccSelectAccSettlePanel(AccountSettlePage page) {
    super();
    this.page = page;
    this.filter = page.getFilter();

    RVerticalPanel rootPanel = new RVerticalPanel();
    rootPanel.setWidth("100%");
    rootPanel.setSpacing(5);

    RCaptionBox box = new RCaptionBox();
    box.setContentSpacing(0);
    box.setCaption(AccountSettleMessages.M.select_accSettle());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");

    Widget widget = drawGrid();
    ScrollPanel scrollPanel = new ScrollPanel(widget);
    scrollPanel.ensureVisible(widget);
    scrollPanel.setWidth("100%");
    scrollPanel.setAlwaysShowScrollBars(false);
    scrollPanel.setHeight("350px");
    box.setContent(scrollPanel);

    rootPanel.add(box);
    initWidget(rootPanel);
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setHoverRow(true);

    counterpartCol = new RGridColumnDef(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
        GRes.R.counterpart()));
    counterpartCol.setWidth("150px");
    counterpartCol.setSortable(true);
    counterpartCol.setName(BAccountSettle.FIELD_COUNTERPART);
    grid.addColumnDef(counterpartCol);

    settleNameCol = new RGridColumnDef(AccountSettleMessages.M.settle_name());
    settleNameCol.setWidth("120px");
    settleNameCol.setSortable(true);
    settleNameCol.setName(BAccountSettle.FIELD_SETTLENAME);
    grid.addColumnDef(settleNameCol);

    settleDateRangeCol = new RGridColumnDef(AccountSettleMessages.M.settle_dateRange());
    settleDateRangeCol.setWidth("150px");
    settleDateRangeCol.setSortable(true);
    settleDateRangeCol.setName(BAccountSettle.FIELD_BEGINDATE);
    grid.addColumnDef(settleDateRangeCol);

    contractTitleCol = new RGridColumnDef(AccountSettleMessages.M.contract_title());
    contractTitleCol.setWidth("120px");
    contractTitleCol.setSortable(true);
    contractTitleCol.setName(BAccountSettle.FIELD_CONTRACT_TITLE);
    grid.addColumnDef(contractTitleCol);

    contractNumberCol = new RGridColumnDef(AccountSettleMessages.M.contract_number());
    contractNumberCol.setWidth("160px");
    contractNumberCol.setSortable(true);
    contractNumberCol.setName(BAccountSettle.FIELD_CONTRACT_NUMBER);
    grid.addColumnDef(contractNumberCol);

    floorCol = new RGridColumnDef(AccountSettleMessages.M.floor());
    floorCol.setSortable(true);
    floorCol.setName(BAccountSettle.FIELD_FLOOR);
    grid.addColumnDef(floorCol);

    coopModeCol = new RGridColumnDef(AccountSettleMessages.M.coopmode());
    coopModeCol.setSortable(true);
    coopModeCol.setName(BAccountSettle.FIELD_COOPMODE);
    grid.addColumnDef(coopModeCol);

    accountTimeCol = new RGridColumnDef(AccountSettleMessages.M.accountTime());
    accountTimeCol.setWidth("100px");
    accountTimeCol.setSortable(true);
    accountTimeCol.setName(BAccountSettle.FIELD_ACCOUNTTIME);
    grid.addColumnDef(accountTimeCol);

    calcTypeCol = new RGridColumnDef(AccountSettleMessages.M.calculate_type());
    calcTypeCol.setSortable(true);
    calcTypeCol.setName(BAccountSettle.FIELD_BILLCALCTYPE);
    grid.addColumnDef(calcTypeCol);

    grid.setAllColumnsOverflowEllipsis(true);

    grid.addSelectionHandler(new SelectionHandler<Integer>() {

      @Override
      public void onSelection(SelectionEvent<Integer> arg0) {
        refreshNextButton();
      }
    });
    grid.addLoadDataHandler(new LoadDataHandler() {

      @Override
      public void onLoadData(LoadDataEvent event) {
        refreshNextButton();
      }
    });

    pagingGrid = new RPagingGrid<BAccountSettle>(grid, new GridDataProvider());
    pagingGrid.getPagingBar().setVisible(false);
    return pagingGrid;
  }

  public void refreshPreButton() {
    RButton preButton = AccountSettlePage.getRoadMapPreButton(page.getRoadMapPanel());
    if (preButton == null)
      return;

    preButton.setEnabled(true);
  }

  public void refreshNextButton() {
    RButton nextButton = AccountSettlePage.getRoadMapNextButton(page.getRoadMapPanel());
    if (nextButton == null)
      return;

    nextButton.setText(AccountSettleMessages.M.next());
    nextButton.setEnabled(validate());
    page.getRoadMapPanel().getRoadmap().getStep(3)
        .setState(nextButton.isEnabled() ? RRoadmapStepState.ENABLED : RRoadmapStepState.DISABLED);
    page.getRoadMapPanel().getRoadmap().getStep(4).setState(RRoadmapStepState.DISABLED);
  }

  private List<BAccountSettle> records = new ArrayList<BAccountSettle>();

  public void refresh() {
    doQuery();
    pagingGrid.clearSelections();
    pagingGrid.refresh();
  }

  private void doQuery() {
    records.clear();
    filter.setPage(0);
    filter.setPageSize(0);
    filter.clearOrders();
    GWTUtil.enableSynchronousRPC();
    RLoadingDialog.show();
    AccountSettleService.Locator.getService().query(filter,
        new RBAsyncCallback2<RPageData<BAccountSettle>>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            RMsgBox.showError(AccountSettleMessages.M.query_error(), caught);
          }

          @Override
          public void onSuccess(RPageData<BAccountSettle> result) {
            RLoadingDialog.hide();
            records.addAll(result.getValues());
          }
        });
  }

  @Override
  public void onShow() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ep.getModuleCaption());
    ep.getTitleBar().appendAttributeText(AccountSettleMessages.M.select_accSettle());

    clear();
    refresh();
    page.removeRoadMapNextButtonHandlers();
    refreshNextButton();

  }

  @Override
  public void onHide() {
    page.setRecords(pagingGrid.getSelections());
  }

  public void clear() {
    records.clear();
    pagingGrid.refresh();
  }

  @Override
  public void clearValidResults() {
    messages.clear();

  }

  @Override
  public boolean isValid() {
    return grid.getSelections().isEmpty() == false;
  }

  @Override
  public List<Message> getInvalidMessages() {
    messages.clear();
    if (grid.getSelections().isEmpty())
      messages.add(Message.error(AccountSettleMessages.M.msg_emptyRecords()));
    return messages;
  }

  @Override
  public boolean validate() {
    if (this.isVisible() == false) {
      return true;
    }
    if (grid.getSelections().isEmpty())
      return false;
    return true;
  }

  /**************************************************/
  private class GridDataProvider implements RPageDataProvider<BAccountSettle> {

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAccountSettle>> callback) {
      // 排序
      sortRecords(sortField, sortDir);
      RPageData<BAccountSettle> result = new RPageData<BAccountSettle>();
      result.setValues(records);
      result.setTotalCount(records.size());
      // 分页
      // int pageSized = pageSize;
      // int paged = page;
      // result.setPageCount(new Double(
      // Math.ceil(records.size() / new
      // Double(pageSized).doubleValue())).intValue());
      // result.setTotalCount(records.size());
      // paged = paged >= result.getPageCount() ? result.getPageCount() - 1 :
      // paged;
      // result.setCurrentPage(paged);
      //
      // if (records.size() > 0) {
      // for (int i = pageSized * paged; i < Math.min(pageSized * (paged + 1),
      // records.size()); i++) {
      // result.getValues().add(records.get(i));
      // }
      // }
      callback.onSuccess(result);
    }

    @Override
    public Object getData(int row, int col, BAccountSettle rowData, List<BAccountSettle> pageData) {
      if (col == counterpartCol.getIndex())
        return rowData.getCounterpart() == null ? null : rowData.getCounterpart().toFriendlyStr(
            ep.getCounterpartTypeMap());
      else if (col == settleNameCol.getIndex())
        return rowData.getSettleName();
      else if (col == settleDateRangeCol.getIndex())
        return toDateRangeStr(rowData.getBeginDate(), rowData.getEndDate());
      else if (col == contractTitleCol.getIndex())
        return rowData.getContractTitle();
      else if (col == contractNumberCol.getIndex())
        return rowData.getContractNumber();
      else if (col == accountTimeCol.getIndex())
        return rowData.getAccountTime() == null ? null : GWTFormat.fmt_yMd.format(rowData
            .getAccountTime());
      else if (col == calcTypeCol.getIndex())
        return rowData.getBillCalcType() == null ? null : PSettlementDef.billCalculateType
            .getEnumCaption(rowData.getBillCalcType());
      else if (col == floorCol.getIndex())
        return rowData.getFloor() == null || rowData.getFloor().getUuid() == null ? "" : rowData
            .getFloor().toFriendlyStr();
      else if (col == coopModeCol.getIndex())
        return rowData.getCoopMode() == null ? "" : rowData.getCoopMode();
      return null;
    }

    private String toDateRangeStr(Date beginDate, Date endDate) {
      if (beginDate == null && endDate == null)
        return null;
      StringBuffer sb = new StringBuffer();
      if (beginDate != null)
        sb.append(GWTFormat.fmt_yMd.format(beginDate));
      sb.append("~");
      if (endDate != null)
        sb.append(GWTFormat.fmt_yMd.format(endDate));
      return sb.toString();
    }

  }

  private void sortRecords(final String sortField, OrderDir sortDir) {
    if (records.isEmpty()) {
      return;
    }
    if (StringUtil.isNullOrBlank(sortField)) {
      // 按照商户+合同+出账日期+费用类型升序排序
      Collections.sort(records, new Comparator<BAccountSettle>() {

        @Override
        public int compare(BAccountSettle o1, BAccountSettle o2) {
          if (compareField(o1.getCounterpart().getCode(), o2.getCounterpart().getCode(), 1) == 0) {
            if (compareField(o1.getContractNumber(), o2.getContractNumber(), 1) == 0) {
              if (compareField(o1.getAccountTime(), o2.getAccountTime(), 1) == 0) {
                if (compareField(o1.getSettleName(), o2.getSettleName(), 1) == 0) {
                  return compareField(o1.getBeginDate(), o2.getBeginDate(), 1);
                } else {
                  return compareField(o1.getSettleName(), o2.getSettleName(), 1);
                }
              } else {
                return compareField(o1.getAccountTime(), o2.getAccountTime(), 1);
              }
            } else {
              return compareField(o1.getCounterpart().getCode(), o2.getCounterpart().getCode(), 1);
            }
          } else {
            return compareField(o1.getCounterpart().getCode(), o2.getCounterpart().getCode(), 1);
          }
        }

      });
    } else {
      final int direction = OrderDir.asc.equals(sortDir) ? 1 : -1;
      Collections.sort(records, new Comparator<BAccountSettle>() {

        @Override
        public int compare(BAccountSettle o1, BAccountSettle o2) {
          if (BAccountSettle.FIELD_COUNTERPART.equals(sortField)) {
            return compareField(o1.getCounterpart().getCode(), o2.getCounterpart().getCode(),
                direction);
          } else if (BAccountSettle.FIELD_SETTLENAME.equals(sortField)) {
            return compareField(o1.getSettleName(), o2.getSettleName(), direction);
          } else if (BAccountSettle.FIELD_BEGINDATE.equals(sortField)) {
            return compareField(o1.getBeginDate(), o2.getBeginDate(), direction);
          } else if (BAccountSettle.FIELD_CONTRACT_TITLE.equals(sortField)) {
            return compareField(o1.getContractTitle(), o2.getContractTitle(), direction);
          } else if (BAccountSettle.FIELD_CONTRACT_NUMBER.equals(sortField)) {
            return compareField(o1.getContractNumber(), o2.getContractNumber(), direction);
          } else if (BAccountSettle.FIELD_FLOOR.equals(sortField)) {
            return compareField(o1.getFloor() == null ? null : o1.getFloor().getCode(),
                o2.getFloor() == null ? null : o2.getFloor().getCode(), direction);
          } else if (BAccountSettle.FIELD_COOPMODE.equals(sortField)) {
            return compareField(o1.getCoopMode(), o2.getCoopMode(), direction);
          } else if (BAccountSettle.FIELD_ACCOUNTTIME.equals(sortField)) {
            return compareField(o1.getAccountTime(), o2.getAccountTime(), direction);
          } else if (BAccountSettle.FIELD_BILLCALCTYPE.equals(sortField)) {
            return compareField(o1.getBillCalcType(), o2.getBillCalcType(), direction);
          }
          return 0;
        }
      });
    }
  }

  private int compareField(Object field1, Object field2, int direction) {
    if (field1 == null && field2 == null) {
      return 0;
    } else if (field1 == null && field2 != null) {
      return 1 * direction;
    } else if (field1 != null && field2 == null) {
      return -1 * direction;
    } else {
      if (field1 instanceof String && field2 instanceof String) {
        return ((String) field1).compareTo((String) field2) * direction;
      } else if (field1 instanceof Date && field2 instanceof Date) {
        return ((Date) field1).compareTo((Date) field2) * direction;
      } else {
        return field1.toString().compareTo(field2.toString()) * direction;
      }
    }
  }

}
