/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-common
 * 文件名： AccountUnitBrowserDialog.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-25 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.counterpart;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.commons.gwt.base.client.biz.BBasicState;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.mres.client.dd.rt.BasicStateDef;
import com.hd123.m3.commons.gwt.mres.client.dd.rt.CBasicState;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.OrderDir;
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
 * @author chenrizhang
 * 
 */
public class CounterpartBrowserDialog extends RBrowserDialog<BCounterpart> {
  private CodeNameFilter filter = new CodeNameFilter();

  private RGridColumnDef codeCol;
  private RGridColumnDef nameCol;

  private List<String> counterpartTypes;
  private String state;

  private PageDataProvider provider;

  public CounterpartBrowserDialog() {
    super();
    CounterpartConfigLoader.getInstance().initial();

    codeCol = new RGridColumnDef(M.code());
    codeCol.setName(AccountCpntsContants.ORDER_BY_FIELD_CODE);
    codeCol.setSortable(true);
    nameCol = new RGridColumnDef(M.name());
    nameCol.setName(AccountCpntsContants.ORDER_BY_FIELD_NAME);
    nameCol.setSortable(true);
    setColumnDefs(codeCol, nameCol);

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
  }
  
  public void setStateCondition(String state){
    this.state = state;
    filter.put(BCounterpart.KEY_FILTER_STATE, state);
  }

  public void setState(String state) {
    this.state = state;
    setStateCondition(state);
    if (provider.stateField != null) {
      provider.stateField.setVisible(state != null);
    }
    if (provider.form != null) {
      provider.form.rebuild();
    }
  }

  public void setCounterpartTypes(List<String> counterpartTypes) {
    this.counterpartTypes = counterpartTypes != null ? counterpartTypes : CounterpartConfigLoader
        .getInstance().getCounterpartTypes();
    if (this.counterpartTypes.size() == 1) {
      setCaption(CommonsMessages.M.seleteData(CounterpartConfigLoader.getInstance().getCaption(
          this.counterpartTypes.get(0), M.defualtCaption())));
    }
    filter.put(BCounterpart.KEY_FILTER_MODULE, counterpartTypes);
    if (provider.counterpartTypeField != null && counterpartTypes != null) {
      provider.counterpartTypeField.setVisible(counterpartTypes.size() != 1);

      provider.counterpartTypeField.clearOptions();
      for (String counterpartType : counterpartTypes) {
        provider.counterpartTypeField.addOption(counterpartType, CounterpartConfigLoader
            .getInstance().getCaption(counterpartType));
      }
    }
    if (provider.form != null) {
      provider.form.rebuild();
    }
  }

  @Override
  public void show() {
    if (counterpartTypes == null) {
      setCounterpartTypes(null);
    }
    if (!showStateField) {
      setState(null);
    }
    super.show();
  }

  @Override
  public void center() {
    if (counterpartTypes == null) {
      setCounterpartTypes(null);
    }
    if (!showStateField) {
      setState(null);
    }
    super.center();
  }

  private boolean showStateField = false;
  public void showStateField(boolean showStateField) {
    this.showStateField = showStateField;
    if (provider != null && provider.stateField != null) {
      provider.stateField.setVisible(showStateField);
    }
  }
  
  private class PageDataProvider implements RPageDataProvider<BCounterpart>, RFilterCallback {
    private RForm form;
    private RTextBox codeField;
    private RTextBox nameField;
    private RComboBox<String> counterpartTypeField;
    private RComboBox<String> stateField;

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
      if (stateField != null && stateField.isVisible()) {
        filter.put(BCounterpart.KEY_FILTER_STATE, stateField.getValue());
      } else if (!StringUtil.isNullOrBlank(state)) {
        filter.put(BCounterpart.KEY_FILTER_STATE, state);
      } else {
        filter.put(BCounterpart.KEY_FILTER_STATE, BBasicState.USING);
      }
      AccountCpntsService.Locator.getService().queryCounterparts(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BCounterpart rowData, List<BCounterpart> pageData) {
      if (col == codeCol.getIndex())
        return rowData.getCode();
      if (col == nameCol.getIndex())
        return rowData.getName();
      return null;
    }

    @Override
    public void buildConditions(RForm form) {
      this.form = form;
      form.setWidth("400px");

      codeField = new RTextBox(M.code());
      form.addField(codeField);

      nameField = new RTextBox(M.name());
      form.addField(nameField);

      stateField = new RComboBox<String>(M.state());
      stateField.setRequired(false);
      stateField.setEditable(false);
      stateField.addOption(CBasicState.using, BasicStateDef.constants.using());
      stateField.addOption(CBasicState.deleted, BasicStateDef.constants.deleted());
      stateField.setNullOptionText(CommonsMessages.M.all());
      stateField.setValue(CBasicState.using);
      stateField.setVisible(state == null);
      form.addField(stateField);

      counterpartTypeField = new RComboBox<String>(M.counterpartType());
      counterpartTypeField.setNullOptionText(CommonsMessages.M.all());
      counterpartTypeField.setRequired(false);
      counterpartTypeField.setEditable(false);
      if (counterpartTypes != null) {
        counterpartTypeField.setVisible(counterpartTypes.size() != 1);
      }
      form.addField(counterpartTypeField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      clearCondition();
    }

    @Override
    public void onQuery() {
      if (codeField != null && codeField.isVisible()) {
        filter.setCode(codeField.getValue());
      }
      if (nameField != null && nameField.isVisible()) {
        filter.setName(nameField.getValue());
      }
      
      if (counterpartTypeField != null && counterpartTypeField.isVisible()) {
        List<String> list = counterpartTypeField.getValue() == null ? counterpartTypes : Arrays
            .asList(counterpartTypeField.getValue());
        filter.put(BCounterpart.KEY_FILTER_MODULE, list);
      }
      if (stateField != null && stateField.isVisible()) {
        filter.put(BCounterpart.KEY_FILTER_STATE, stateField.getValue());
      } else if (!StringUtil.isNullOrBlank(state)) {
        filter.put(BCounterpart.KEY_FILTER_STATE, state);
      } else {
        filter.put(BCounterpart.KEY_FILTER_STATE, BBasicState.USING);
      }
    }
  }
  
  private void clearCondition(){
    if (provider != null) {
      if (provider.codeField != null && provider.codeField.isVisible()) {
        provider.codeField.clearValue();
      }
      if (provider.nameField != null && provider.nameField.isVisible()) {
        provider.nameField.clearValue();
      }
      if (provider.stateField != null && provider.stateField.isVisible()) {
        provider.stateField.clearValue();
      }
      if (provider.counterpartTypeField != null && provider.counterpartTypeField.isVisible()) {
        provider.counterpartTypeField.clearValue();
      }
    }
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("对方单位")
    String defualtCaption();

    @DefaultMessage("代码")
    String code();

    @DefaultMessage("名称")
    String name();

    @DefaultMessage("状态")
    String state();

    @DefaultMessage("对方单位类型")
    String counterpartType();
  }

}
