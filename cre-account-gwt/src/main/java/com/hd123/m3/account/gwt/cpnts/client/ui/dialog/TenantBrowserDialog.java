/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	PositionBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-5-10 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.base.client.BaseWidgetConstants;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.commons.gwt.base.client.biz.BBasicState;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.mres.client.dd.rt.CBasicState;
import com.hd123.rumba.gwt.base.client.BUCN;
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
 * @author chenganbang
 * 
 */
public class TenantBrowserDialog extends RBrowserDialog {
  private RGridColumnDef codeCol;
  private RGridColumnDef nameCol;

  private List<String> states = new ArrayList<String>();
  private PageDataProvider provider;

  /** 状态范围，未指定则包含全部状态。 */
  public TenantBrowserDialog(String... states) {
    super(M.code(), M.name());

    for (String state : states) {
      this.states.add(state);
    }

    codeCol = getGrid().getColumnDef(0);
    codeCol.setName(BaseWidgetConstants.ORDER_BY_FIELD_CODE);
    codeCol.setSortable(true);
    nameCol = getGrid().getColumnDef(1);
    nameCol.setName(BaseWidgetConstants.ORDER_BY_FIELD_NAME);
    nameCol.setSortable(true);
    setColumnDefs(codeCol, nameCol);

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
  }

  public void setStates(String... states) {
    this.states.clear();
    for (String state : states) {
      this.states.add(state);
    }
    provider.filterRebuild();
  }

  private class PageDataProvider implements RPageDataProvider<BUCN>, RFilterCallback {
    private CodeNameFilter filter = new CodeNameFilter();
    private RForm form;
    private RTextBox codeField;
    private RTextBox nameField;
    private RComboBox<String> stateField;

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
      filter.put(BaseWidgetConstants.KEY_FILTER_STATE, states.isEmpty() ? stateField.getValue()
          : states);
      AccountCpntsService.Locator.getService().queryTenants(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BUCN rowData, List<BUCN> pageData) {
      if (col == codeCol.getIndex())
        return rowData.getCode();
      if (col == nameCol.getIndex())
        return rowData.getName();
      return null;
    }

    public void filterRebuild() {
      if (stateField != null) {
        stateField.setVisible(states.isEmpty());
      }

      if (form != null) {
        form.rebuild();
      }
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
      stateField.addOption(BBasicState.USING, BBasicState.getCaption(BBasicState.USING));
      stateField.addOption(BBasicState.DELETED, BBasicState.getCaption(BBasicState.DELETED));
      stateField.setNullOptionText(CommonsMessages.M.all());
      stateField.setValue(CBasicState.using);
      form.addField(stateField);

      filterRebuild();
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
        stateField.clearValue();
      }
    }

    @Override
    public void onQuery() {
      if (codeField != null && nameField != null) {
        filter.setCode(codeField.getValue());
        filter.setName(nameField.getValue());
      }
    }
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("商户")
    String defualtCaption();

    @DefaultMessage("代码")
    String code();

    @DefaultMessage("名称")
    String name();

    @DefaultMessage("状态")
    String state();
  }

}
