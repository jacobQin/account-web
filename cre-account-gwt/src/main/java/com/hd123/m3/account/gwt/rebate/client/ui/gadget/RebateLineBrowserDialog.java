/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	SaleDetailsBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月9日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui.gadget;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesBill;
import com.hd123.m3.account.gwt.rebate.client.rpc.RebateBillService;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.sales.RebateLineFilter;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseHandler;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;

/**
 * 销售明细浏览框
 * 
 * @author chenganbang
 */
public class RebateLineBrowserDialog extends RBrowserDialog<BSalesBill> {
  private RGridColumnDef billNumberCol = new RGridColumnDef(RebateLineContants.M.billNumberText(),
      BSalesBill.FIELD_BILLNUMBER);
  private RGridColumnDef accountDateCol = new RGridColumnDef(
      RebateLineContants.M.accountDateText(), BSalesBill.FIELD_ACCOUNTDATE);
  private RGridColumnDef amountCol = new RGridColumnDef(RebateLineContants.M.amountText(),
      BSalesBill.FIELD_AMOUNT);

  private PageDataProvider provider;
  private RebateLineFilter filter;

  public RebateLineBrowserDialog() {
    // 设置表格列名定义
    super(RebateLineContants.M.billNumberText(), RebateLineContants.M.accountDateText(),
        RebateLineContants.M.amountText());
    amountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    // 设置表格列
    setColumnDefs(billNumberCol, accountDateCol, amountCol);
    accountDateCol.setSortable(false);
    // 设置浏览框标题
    setCaption(RebateLineContants.M.selectData(RebateLineContants.M.caption()));

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setSingleSelection(false);
    setFilterStyle(FILTER_STYLE_PANEL);

    addBeforeCloseHandler(new BeforeCloseHandler<RDialog>() {

      @Override
      public void onBeforeClose(BeforeCloseEvent<RDialog> event) {
        if (getFilterCallback() != null && getGrid() != null) {
          getFilterCallback().onQuery();
        }
      }
    });
  }

  private class PageDataProvider implements RPageDataProvider<BSalesBill>, RFilterCallback {
    private RTextBox saleNumberField;
    private RDateBox receiverDateField;

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BSalesBill>> callback) {

      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(billNumberCol.getName(), OrderDir.asc);
      }
      RebateBillService.Locator.getService().queryProductRpts(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BSalesBill rowData, List<BSalesBill> pageData) {
      if (rowData != null) {
        if (col == billNumberCol.getIndex()) {
          return rowData.getBillnumber();
        } else if (col == accountDateCol.getIndex()) {
          if (rowData.getAccountDate().getBeginDate().compareTo(rowData.getAccountDate().getEndDate()) != 0) {
            return rowData.getAccountDate().format(M3Format.fmt_yMd);
          } else {
            return M3Format.fmt_yMd.format(rowData.getAccountDate().getBeginDate());
          }
        } else if (col == amountCol.getIndex()) {
          return M3Format.fmt_money.format(rowData.getAmount());
        }
      }
      return null;
    }

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("100%");

      saleNumberField = new RTextBox(RebateLineContants.M.billNumberText());
      form.addField(saleNumberField);

      receiverDateField = new RDateBox(RebateLineContants.M.accountDateText());
      form.addField(receiverDateField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      clearCondition();
    }

    @Override
    public void onQuery() {
      if (saleNumberField != null) {
        filter.setSaleNumber(saleNumberField.getValue());
      }
      if (receiverDateField != null) {
        filter.setOrcTime(receiverDateField.getValue());
      }
    }

  }
  
  private void clearCondition(){
    if (provider != null) {
      if (provider.saleNumberField != null) {
        provider.saleNumberField.clearValue();
      }
      if (provider.receiverDateField != null) {
        provider.receiverDateField.clearValue();
      }
    }
  }
  
  public void setFilter(RebateLineFilter filter){
    this.filter = filter;
  }

  public interface RebateLineContants extends Messages {
    public static RebateLineContants M = GWT.create(RebateLineContants.class);

    @DefaultMessage("单号")
    String billNumberText();

    @DefaultMessage("收款日期")
    String accountDateText();

    @DefaultMessage("总销售额")
    String amountText();

    @DefaultMessage("销售明细")
    String caption();

    @DefaultMessage("请选择-{0}")
    String selectData(String data);
  }

}
