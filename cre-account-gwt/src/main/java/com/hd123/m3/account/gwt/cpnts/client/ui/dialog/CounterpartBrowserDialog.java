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
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.commons.gwt.base.client.biz.BBasicState;
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
public class CounterpartBrowserDialog extends RBrowserDialog<BCounterpart> {
  private RGridColumnDef codeCol = new RGridColumnDef(WidgetRes.M.code(),
      AccountCpntsContants.ORDER_BY_FIELD_CODE);
  private RGridColumnDef nameCol = new RGridColumnDef(WidgetRes.M.name(),
      AccountCpntsContants.ORDER_BY_FIELD_NAME);
  private Map<String, String> captionMap;

  private PageDataProvider provider;
  private Boolean state = Boolean.TRUE;
  private Map<String, String> counterTypeMap;
  private String counterpartType;

  public CounterpartBrowserDialog(Boolean isStateVisible, Boolean isCounterpartTypeEnable,
      Map<String, String> captionMap) {
    super(WidgetRes.M.code(), WidgetRes.M.name());
    this.captionMap = captionMap;
    setColumnDefs(codeCol, nameCol);
    setCaption(WidgetRes.M.seleteData(GRes.R.counterpart()));

    provider = new PageDataProvider();
    provider.setIsStateVisible(isStateVisible);
    provider.setCounterpartTypeEnable(isCounterpartTypeEnable);
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

  public Map<String, String> getCounterTypeMap() {
    return counterTypeMap;
  }

  public void setCounterTypeMap(Map<String, String> counterTypeMap) {
    this.counterTypeMap = counterTypeMap;
  }

  public void setCounterpartType(String counterpartType) {
    this.counterpartType = counterpartType;
  }

  public String getFieldCaption(String field, String defaultCaption) {
    if (field == null)
      return defaultCaption;
    if (captionMap == null) {
      return defaultCaption;
    } else {
      String caption = captionMap.get(field);
      return caption == null ? defaultCaption : caption;
    }
  }

  private class PageDataProvider implements RPageDataProvider<BCounterpart>, RFilterCallback {
    private CodeNameFilter filter = new CodeNameFilter();
    private RTextBox codeField;
    private RTextBox nameField;
    private RComboBox<String> counterpartTypeField;
    private RComboBox<String> stateField;
    private boolean isStateVisible;
    private boolean isCounterpartTypeEnable;

    public void setIsStateVisible(boolean isStateVisible) {
      this.isStateVisible = isStateVisible;
    }

    public void setCounterpartTypeEnable(boolean isCounterpartTypeEnable) {
      this.isCounterpartTypeEnable = isCounterpartTypeEnable;
    }

    public void setCounterpartType(String counterpartType) {
      if (counterpartTypeField != null)
        counterpartTypeField.setValue(counterpartType);
    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BCounterpart>> callback) {
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

      AccountCpntsService.Locator.getService().queryCounterparts(filter, state, callback);
    }

    @Override
    public Object getData(int row, int col, BCounterpart rowData, List<BCounterpart> pageData) {
      switch (col) {
      case 0:
        return rowData.getCode();
      case 1:
        return rowData.toNameStr(counterTypeMap);
      default:
        return null;
      }
    }

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("450px");

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
      stateField.setValue("all");
      form.addField(stateField);

      counterpartTypeField = new RComboBox<String>(getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE,
          GRes.R.counterpartType()));
      counterpartTypeField.setVisible(isCounterpartTypeEnable);
      counterpartTypeField.setEditable(false);
      counterpartTypeField.setNullOptionText(WidgetRes.M.all());
      counterpartTypeField.setMaxDropdownRowCount(10);
      for (Map.Entry<String, String> entry : counterTypeMap.entrySet()) {
        counterpartTypeField.addOption(entry.getKey(), entry.getValue());
      }
      form.addField(counterpartTypeField);
      counterpartTypeField.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          counterpartType = counterpartTypeField.getValue();
        }
      });
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
      if (stateField != null) {
        stateField.setValue("all");
      }
      if (isCounterpartTypeEnable && counterpartTypeField != null) {
        counterpartTypeField.clearValue();
      }
      if (filter != null) {
        filter.clear();
      }
      if (isCounterpartTypeEnable) {
        counterpartType = null;
      }
    }

    @Override
    public void onQuery() {
      setCounterpartType(counterpartType);
      if (codeField != null && nameField != null && counterpartTypeField != null) {
        filter.setCode(codeField.getValue());
        filter.setName(nameField.getValue());
        filter.put(AccountCpntsContants.CONDITION_COUNTERPARTTYPE, counterpartTypeField.getValue());
      }
    }
  }

}
