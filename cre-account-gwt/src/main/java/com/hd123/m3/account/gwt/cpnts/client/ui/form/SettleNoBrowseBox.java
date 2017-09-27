/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	SettleNoBrowseBox.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-18 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.commons.client.dd.PMonthSettleDef;
import com.hd123.m3.account.gwt.cpnts.client.biz.BMonthSettle;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.SettleNoFilter;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.RPCExceptionDecoder;
import com.hd123.rumba.gwt.widget2.client.form.RDateRangeField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;
import com.hd123.rumba.gwt.widget2e.client.form.RBrowseBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author subinzhu
 * 
 */
public class SettleNoBrowseBox extends RBrowseBox<BMonthSettle> {

  public SettleNoBrowseBox() {
    super();
    setFieldCaption(PMonthSettleDef.TABLE_CAPTION);
    PageDataProvider provider = new PageDataProvider();
    RBrowserDialog<BMonthSettle> browser = new RBrowserDialog(WidgetRes.M.seleteData(WidgetRes.M
        .settleNo()), provider, provider, new RGridColumnDef(PMonthSettleDef.constants.number(),
        BMonthSettle.FIELD_NUMBER), new RGridColumnDef(PMonthSettleDef.constants.beginTime(),
        BMonthSettle.FIELD_BEGINTIME), new RGridColumnDef(PMonthSettleDef.constants.endTime(),
        BMonthSettle.FIELD_ENDTIME), new RGridColumnDef(PMonthSettleDef.constants.createTime(),
        "createTime"));
    browser.addSelectionHandler(new SelectionHandler<BMonthSettle>() {
      public void onSelection(SelectionEvent<BMonthSettle> event) {
        setValue(event.getSelectedItem().getNumber(), true);
      }
    });
    browser.setFilterStyle(RBrowserDialog.FILTER_STYLE_PANEL);
    browser.setSingleSelection(true);
    setBrowser(browser);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    // 用于再次选择的时候能够清空对话框内容
    if (getBrowser() != null && getBrowser().getFilterCallback() != null) {
      getBrowser().getGrid().clearSort();
      getBrowser().getFilterCallback().clearConditions();
      getBrowser().getFilterCallback().onQuery();
    }
  }

  protected void onValidate(String textToValidate) {
    if (getValue() == null || "".equals(getValue())) {
      clearMessages();
      return;
    }
    GWTUtil.enableSynchronousRPC();
    AccountCpntsService.Locator.getService().getSettleByNumber(getText(),
        new RBAsyncCallback2<String>() {
          public void onException(Throwable caught) {
            clearMessages();
            addErrorMessage(RPCExceptionDecoder.decode(caught).getMessage());
          }

          public void onSuccess(String result) {
            if (result == null) {
              clearMessages();
              addErrorMessage(CommonsMessages.M.cannotFind2(PMonthSettleDef.TABLE_CAPTION,
                  getText()));
            } else {
              clearMessages();
            }
          }
        });
  }

  private class PageDataProvider implements RPageDataProvider<BMonthSettle>, RFilterCallback {
    private RForm form;
    private RTextBox numberField;
    private RDateRangeField beginTimedateRangeField;
    private RDateRangeField endTimedateRangeField;
    private SettleNoFilter filter = new SettleNoFilter();

    public void buildConditions(RForm form) {
      this.form = form;

      form.setWidth("450");
      numberField = new RTextBox(PMonthSettleDef.constants.number());
      form.addField(numberField);

      beginTimedateRangeField = new RDateRangeField(PMonthSettleDef.constants.beginTime());
      form.addField(beginTimedateRangeField);

      endTimedateRangeField = new RDateRangeField(PMonthSettleDef.constants.endTime());
      form.addField(endTimedateRangeField);

      form.rebuild();
    }

    public void clearConditions() {
      if (numberField != null)
        numberField.clearValue();
      if (beginTimedateRangeField != null)
        beginTimedateRangeField.clearValue();
      if (endTimedateRangeField != null)
        endTimedateRangeField.clearValue();
    }

    public void onQuery() {
      if (numberField != null && beginTimedateRangeField != null && endTimedateRangeField != null) {
        filter.setNumber(numberField.getValue());
        filter.setBeginTimeDateRange(beginTimedateRangeField.getValue() == null ? new BDateRange()
            : new BDateRange(beginTimedateRangeField.getValue().getStart(), beginTimedateRangeField
                .getValue().getEnd()));
        filter.setEndTimeDateRange(endTimedateRangeField.getValue() == null ? new BDateRange()
            : new BDateRange(endTimedateRangeField.getValue().getStart(), endTimedateRangeField
                .getValue().getEnd()));
      }
    }

    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BMonthSettle>> callback) {
      if (form.validate() == false || beginTimedateRangeField.getStartField().validate() == false
          || beginTimedateRangeField.getEndField().validate() == false
          || endTimedateRangeField.getStartField().validate() == false
          || endTimedateRangeField.getEndField().validate() == false) {
        RPageData<BMonthSettle> oldData = new RPageData<BMonthSettle>();
        oldData.setPageCount(getBrowser().getPagingGrid().getPageCount());
        oldData.setCurrentPage(getBrowser().getPagingGrid().getCurrentPage());
        oldData.setTotalCount(getBrowser().getPagingGrid().getTotalCount());
        oldData.setValues(getBrowser().getPagingGrid().getValues());
        callback.onSuccess(oldData);
        return;
      }
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(numberField.getName(), OrderDir.asc);
      }
      AccountCpntsService.Locator.getService().querySettles(filter, callback);
    }

    public Object getData(int row, int col, BMonthSettle rowData, List<BMonthSettle> pageData) {
      switch (col) {
      case 0:
        return rowData.getNumber();
      case 1:
        if (rowData.getBeginTime() == null)
          return null;
        return GWTFormat.fmt_yMd.format(rowData.getBeginTime());
      case 2:
        if (rowData.getEndTime() == null)
          return null;
        return GWTFormat.fmt_yMd.format(rowData.getEndTime());
      case 3:
        if (rowData.getCreateTime() == null)
          return null;
        return GWTFormat.fmt_yMd.format(rowData.getCreateTime());
      default:
        return null;
      }
    }
  }

}
