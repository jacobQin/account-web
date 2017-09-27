/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RecDepositRepaymentBrowseDialog.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月28日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.widget;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc.RecDepositRepaymentService;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseHandler;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;

/**
 * 预存款浏览框
 * 
 * @author chenganbang
 *
 */
public class RecDepositBrowseDialog extends RBrowserDialog<BDeposit> {
  private RGridColumnDef billNumberCol = new RGridColumnDef(M.columnCaption(), "billNumber");

  private PageDataProvider provider;

  public RecDepositBrowseDialog() {
    super(M.columnCaption());
    setColumnDefs(billNumberCol);

    setWidth("500px");
    setCaption(M.selectDate(M.defaultCaption()));
    getGrid().setAllColumnsOverflowEllipsis(true);

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
    setSingleSelection(true);

    addBeforeCloseHandler(new BeforeCloseHandler<RDialog>() {

      @Override
      public void onBeforeClose(BeforeCloseEvent<RDialog> event) {
        if (getFilterCallback() != null && getGrid() != null) {
          getFilterCallback().onQuery();
        }
      }
    });
  }

  /** 设置项目 */
  public void setAccountUnitUuid(String accountUnitUuid) {
    if (provider != null) {
      provider.setAccountUnitUuid(accountUnitUuid);
    }
  }

  /** 设置对方单位 */
  public void setCounterpartUuid(String counterpartUuid) {
    if (provider != null) {
      provider.setCounterpartUuid(counterpartUuid);
    }
  }

  /** 设置合同 */
  public void setContractUuid(String contractUuid) {
    if (provider != null) {
      provider.setContractUuid(contractUuid);
    }
  }

  private class PageDataProvider implements RPageDataProvider<BDeposit>, RFilterCallback {

    private RTextBox billNumberField;

    private RecDepositFilter filter = new RecDepositFilter();

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BDeposit>> callback) {
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(billNumberCol.getName(), OrderDir.asc);
      }
      // 默认行为
      filter.setBizState(BizStates.EFFECT);

      RecDepositRepaymentService.Locator.getService().queryDeposit(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BDeposit rowData, List<BDeposit> pageData) {
      switch (col) {
      case 0:
        return rowData.getBillNumber();
      default:
        return null;
      }
    }

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("100%");

      billNumberField = new RTextBox(M.billNumberStartWith());
      form.addField(billNumberField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      if (billNumberField != null) {
        billNumberField.clearValue();
      }

      if (filter != null) {
        filter.clear();
      }
    }

    @Override
    public void onQuery() {
      if (billNumberField != null) {
        filter.setBillNumber(billNumberField.getValue());
      }
    }

    /** 设置项目 */
    public void setAccountUnitUuid(String accountUnitUuid) {
      filter.setAccountUnitUuid(accountUnitUuid);
    }

    /** 设置对方单位 */
    public void setCounterpartUuid(String counterpartUuid) {
      filter.setCounterpartUuid(counterpartUuid);
    }

    /** 设置合同 */
    public void setContractUuid(String contractUuid) {
      filter.setContractUuid(contractUuid);
    }
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("预存款单")
    String defaultCaption();

    @DefaultMessage("单号")
    String columnCaption();

    @DefaultMessage("单号 起始于")
    String billNumberStartWith();

    @DefaultMessage("请选择：{0}")
    String selectDate(String caption);
  }
}
