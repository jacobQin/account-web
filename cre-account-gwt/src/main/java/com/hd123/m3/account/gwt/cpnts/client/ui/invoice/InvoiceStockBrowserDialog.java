/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	InvoiceStockBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月8日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.invoice;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.CounterpartConfigLoader;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;

/**
 * @author chenrizhang
 *
 */
public class InvoiceStockBrowserDialog extends RBrowserDialog<BInvoiceStock> {
  private RGridColumnDef codeCol;
  private RGridColumnDef numberCol;

  private boolean accountUnitControl = false;
  private CodeNameFilter filter = new CodeNameFilter();
  private PageDataProvider provider;

  public InvoiceStockBrowserDialog() {
    super();
    CounterpartConfigLoader.getInstance().initial();

    codeCol = new RGridColumnDef(M.code());
    codeCol.setName(AccountCpntsContants.ORDER_BY_FIELD_CODE);
    codeCol.setSortable(true);
    numberCol = new RGridColumnDef(M.number());
    numberCol.setName(AccountCpntsContants.ORDER_BY_FIELD_NAME);
    numberCol.setSortable(true);
    setColumnDefs(codeCol, numberCol);

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
  }

  /** 票据状态 */
  public void setStates(String... states) {
    if (states != null) {
      filter.put(BInvoiceStock.KEY_FILTER_STATE, Arrays.asList(states));
    } else {
      filter.remove(BInvoiceStock.KEY_FILTER_STATE);
    }
  }

  /** 票据类型 */
  public void setInvoiceTypes(String... invoiceTypes) {
    if (invoiceTypes != null) {
      filter.put(BInvoiceStock.KEY_FILTER_INVOICE_TYPE, Arrays.asList(invoiceTypes));
    } else {
      filter.remove(BInvoiceStock.KEY_FILTER_INVOICE_TYPE);
    }
  }

  /** 发票使用方式常量定义.0-正常，1-被红冲，2-红冲，3-被交换，4-被交换和红冲，5-交换 */
  public void setUseType(Integer useType) {
    if (useType != null) {
      filter.put(BInvoiceStock.KEY_FILTER_USE_TYPE, useType);
    } else {
      filter.remove(BInvoiceStock.KEY_FILTER_USE_TYPE);
    }
  }

  /** 项目 */
  public void setAccountUnit(String accountUnit) {
    if (StringUtil.isNullOrBlank(accountUnit)) {
      filter.remove(BInvoiceStock.KEY_FILTER_ACCOUNTUNIT);
    } else {
      filter.put(BInvoiceStock.KEY_FILTER_ACCOUNTUNIT, accountUnit);
    }
  }

  /** 是否项目管控，若启用，则必须设置项目才能查询数据 */
  public void setAccountUnitControl(boolean accountUnitControl) {
    this.accountUnitControl = accountUnitControl;
  }

  /** 票据类型：0-发票，1-收据 */
  public void setSort(Integer sort) {
    if (sort != null) {
      filter.put(BInvoiceStock.KEY_FILTER_SORT, sort);
    } else {
      filter.remove(BInvoiceStock.KEY_FILTER_SORT);
    }
  }

  private class PageDataProvider implements RPageDataProvider<BInvoiceStock>, RFilterCallback {
    private RTextBox codeField;
    private RTextBox numberField;

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BInvoiceStock>> callback) {
      if (accountUnitControl && filter.get(BInvoiceStock.KEY_FILTER_ACCOUNTUNIT) == null) {
        callback.onSuccess(null);
        return;
      }
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(codeCol.getName(), OrderDir.asc);
      }
      AccountCpntsService.Locator.getService().queryInvoiceStocks(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BInvoiceStock rowData, List<BInvoiceStock> pageData) {
      if (col == codeCol.getIndex())
        return rowData.getInvoiceCode();
      if (col == numberCol.getIndex())
        return rowData.getInvoiceNumber();
      return null;
    }

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("400px");

      codeField = new RTextBox("代码 等于");
      form.addField(codeField);

      numberField = new RTextBox("号码 类似于");
      form.addField(numberField);
    }

    @Override
    public void clearConditions() {
      if (codeField != null) {
        codeField.clearValue();
      }
      if (numberField != null) {
        numberField.clearValue();
      }
    }

    @Override
    public void onQuery() {
      if (codeField != null) {
        filter.setCode(codeField.getValue());
      }
      if (numberField != null) {
        filter.setName(numberField.getValue());
      }
    }
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("发票库存")
    String defualtCaption();

    @DefaultMessage("代码")
    String code();

    @DefaultMessage("号码")
    String number();
  }

}
