/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	PageDataProvider.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.client.ui.gadget.dataprovider;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;

/**
 * @author zhuhairui
 * 
 */
public abstract class PageDataProvider implements RFilterCallback, RPageDataProvider<BUCN> {
  protected CodeNameFilter filter = new CodeNameFilter();
  protected RForm form;
  protected RTextBox codeField;
  protected RTextBox nameField;

  @Override
  public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
      AsyncCallback<RPageData<BUCN>> callback) {
    filter.setPage(page);
    filter.setPageSize(pageSize);
    filter.clearOrders();
    if (sortField != null) {
      filter.appendOrder(sortField, sortDir);
    } else {
      filter.appendOrder(AccountCpntsContants.ORDER_BY_FIELD_CODE, OrderDir.asc);
    }
    queryData(filter, callback);
  }

  protected abstract void queryData(CodeNameFilter tilter, AsyncCallback<RPageData<BUCN>> callback);

  @Override
  public Object getData(int row, int col, BUCN rowData, List<BUCN> pageData) {
    switch (col) {
    case 0:
      return rowData.getCode();
    case 1:
      return rowData.getName();
    default:
      return null;
    }
  }

  public void filterRebuild() {
    form.rebuild();
  }

  @Override
  public void buildConditions(RForm form) {
    this.form = form;
    form.setWidth("400px");

    codeField = new RTextBox(WidgetRes.R.code());
    form.addField(codeField);

    nameField = new RTextBox(WidgetRes.R.name());
    form.addField(nameField);

    form.rebuild();
  }

  @Override
  public void clearConditions() {
    if (codeField != null) {
      codeField.clearValue();
    }
    if (nameField != null) {
      nameField.clearValue();
    }
    if (filter != null) {
      filter.clear();
    }
  }

  @Override
  public void onQuery() {
    if (codeField != null && nameField != null) {
      filter.setCode(codeField.getValue());
      filter.setName(nameField.getValue());
    }
  }

  public CodeNameFilter getFilter() {
    return filter;
  }

  public void setFilter(CodeNameFilter filter) {
    this.filter = filter;
  }

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes R = GWT.create(WidgetRes.class);

    @DefaultStringValue("代码")
    String code();

    @DefaultStringValue("名称")
    String name();
  }
}
