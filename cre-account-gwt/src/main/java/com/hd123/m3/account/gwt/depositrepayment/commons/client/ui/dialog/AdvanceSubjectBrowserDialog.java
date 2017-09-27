/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AdvanceSubjectBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-21 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.commons.client.ui.dialog;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.rpc.DepositRepaymentService;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.ui.filter.AdvanceSubjectFilter;
import com.hd123.rumba.gwt.base.client.BUCN;
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
 * @author chenpeisi
 * 
 */
public class AdvanceSubjectBrowserDialog extends RBrowserDialog<BUCN> {

  private RGridColumnDef codeCol = new RGridColumnDef(WidgetRes.M.code(),
      AccountCpntsContants.ORDER_BY_FIELD_CODE);
  private RGridColumnDef nameCol = new RGridColumnDef(WidgetRes.M.name(),
      AccountCpntsContants.ORDER_BY_FIELD_NAME);

  private PageDataProvider provider;

  public AdvanceSubjectBrowserDialog() {
    super(WidgetRes.M.code(), WidgetRes.M.name());
    setColumnDefs(codeCol, nameCol);

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);

    addBeforeCloseHandler(new BeforeCloseHandler<RDialog>() {

      @Override
      public void onBeforeClose(BeforeCloseEvent<RDialog> event) {
        if (getFilterCallback() != null) {
          getFilterCallback().onQuery();
        }
      }
    });
  }


  /** 项目uuid */
  public void setAccountUnitUuid(String accountUnitUuid) {
    if (provider != null)
      provider.setAccountUnitUuid(accountUnitUuid);
  }

  /** 商户uuid */
  public void setCounterpartUuid(String counterpartUuid) {
    if (provider != null)
      provider.setCounterpartUuid(counterpartUuid);
  }

  public void setContractUuid(String contractUuid) {
    if (provider != null)
      provider.setContractUuid(contractUuid);
  }

  /** 收付方向 */
  public void setDirection(int direction) {
    if (provider != null)
      provider.setDirection(direction);
  }

  private class PageDataProvider implements RPageDataProvider<BUCN>, RFilterCallback {

    private AdvanceSubjectFilter filter = new AdvanceSubjectFilter();
    private RTextBox codeField;
    private RTextBox nameField;

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("400px");

      codeField = new RTextBox(WidgetRes.M.code());
      form.addField(codeField);

      nameField = new RTextBox(WidgetRes.M.name());
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

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BUCN>> callback) {
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(codeCol.getName(), OrderDir.asc);
      }
      DepositRepaymentService.Locator.getService().querySubject(filter, callback);
    }

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

    public void setAccountUnitUuid(String accountUnitUuid) {
      filter.setAccountUnitUuid(accountUnitUuid);
    }

    public void setCounterpartUuid(String counterpartUuid) {
      filter.setCounterpartUuid(counterpartUuid);
    }

    public void setContractUuid(String contractUuid) {
      filter.setContractUuid(contractUuid);
    }

    public void setDirection(int direction) {
      filter.setDirectionType(direction);
    }
  }

}
