/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-common
 * 文件名： AccountUnitBrowserDialog.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-25 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.dialog;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.commons.gwt.base.client.biz.BBasicState;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseHandler;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;

/**
 * @author subinzhu
 * 
 */
public class AccountUnitBrowserDialog extends RBrowserDialog<BUCN> {
  private RGridColumnDef codeCol = new RGridColumnDef(WidgetRes.M.code(),
      AccountCpntsContants.ORDER_BY_FIELD_CODE);
  private RGridColumnDef nameCol = new RGridColumnDef(WidgetRes.M.name(),
      AccountCpntsContants.ORDER_BY_FIELD_NAME);

  private Boolean state;
  private PageDataProvider provider;

  public AccountUnitBrowserDialog(Boolean state, Boolean isStateVisible) {
    super(WidgetRes.M.code(), WidgetRes.M.name());
    this.state = state;
    setColumnDefs(codeCol, nameCol);
    getGrid().setAllColumnsOverflowEllipsis(true);
    setCaption(WidgetRes.M.seleteData(GRes.R.business()));

    provider = new PageDataProvider();
    provider.setIsStateVisible(isStateVisible);
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
    getGrid().setAllColumnsOverflowEllipsis(true);

    addBeforeCloseHandler(new BeforeCloseHandler<RDialog>() {

      @Override
      public void onBeforeClose(BeforeCloseEvent<RDialog> event) {
        if (getFilterCallback() != null) {
          getFilterCallback().onQuery();
        }
      }
    });
  }


  private class PageDataProvider implements RPageDataProvider<BUCN>, RFilterCallback {
    private CodeNameFilter filter = new CodeNameFilter();
    private RTextBox codeField;
    private RTextBox nameField;
    private RComboBox<String> stateField;
    private boolean isStateVisible;

    public void setIsStateVisible(boolean isStateVisible) {
      this.isStateVisible = isStateVisible;
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
      if (isStateVisible) {
        state = stateField.getValue() == null ? state : ("all" == stateField.getValue() ? null
            : BBasicState.USING == stateField.getValue() ? true : false);
      }
      AccountCpntsService.Locator.getService().queryAccountUnits(filter, state, callback);
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

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("400px");

      codeField = new RTextBox(WidgetRes.M.code());
      form.addField(codeField);

      nameField = new RTextBox(WidgetRes.M.name());
      form.addField(nameField);

      stateField = new RComboBox<String>(WidgetRes.M.state());
      stateField.setEditable(false);
      stateField.addOption("all", "全部");
      stateField.addOption(BBasicState.USING, "使用中");
      stateField.addOption(BBasicState.DELETED, "已删除");
      stateField.setVisible(isStateVisible);
      stateField.setValue(state == null ? "all" : state == Boolean.TRUE ? BBasicState.USING
          : BBasicState.DELETED);
      form.addField(stateField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      clearCodition();
    }
    
    @Override
    public void onQuery() {
      if (codeField != null) {
        filter.setCode(codeField.getValue());
      }
      if (nameField != null) {
        filter.setName(nameField.getValue());
      }
    }

  }
  
  private void clearCodition(){
    if (provider != null) {
      if (provider.codeField != null) {
        provider.codeField.clearValue();
      }
      if (provider.nameField != null) {
        provider.nameField.clearValue();
      }
      if (provider.stateField != null) {
        provider.stateField.setValue("all");
      }
      if (provider.filter != null) {
        provider.filter.clear();
      }
    }
  }
}
