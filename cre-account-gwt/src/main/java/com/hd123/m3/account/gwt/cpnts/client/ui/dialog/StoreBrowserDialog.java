/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	m3-sales-web-common
 * 文件名：	StoreBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2014-5-28 - libin - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.dialog;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.base.client.BaseWidgetConstants;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.commons.gwt.base.client.biz.BBasicState;
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
 * 项目选择对话框
 * @author chenganbang
 * 
 */
public class StoreBrowserDialog extends RBrowserDialog {
  private RGridColumnDef codeCol;
  private RGridColumnDef nameCol;

  private PageDataProvider provider;

  public StoreBrowserDialog() {
    super(M.code(), M.name());
    setCaption(M.seleteData(M.defualtCaption()));

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

  public void setShowStateField(boolean show) {
    provider.stateField.setVisible(show);
  }

  private class PageDataProvider implements RPageDataProvider<BUCN>, RFilterCallback {
    private CodeNameFilter filter = new CodeNameFilter();
    private RForm form;
    private RTextBox codeField;
    private RTextBox nameField;
    private RComboBox<String> stateField;

    private PageDataProvider() {
      super();
      stateField = new RComboBox<String>(M.state());
    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BUCN>> callback) {
      if (form.validate() == false) {
        RPageData<BUCN> oldData = new RPageData<BUCN>();
        oldData.setPageCount(getPagingGrid().getPageCount());
        oldData.setCurrentPage(getPagingGrid().getCurrentPage());
        oldData.setTotalCount(getPagingGrid().getTotalCount());
        oldData.setValues(getPagingGrid().getValues());
        callback.onSuccess(oldData);
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
      AccountCpntsService.Locator.getService().queryStores(filter, callback);
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
      form.rebuild();
    }

    @Override
    public void buildConditions(RForm form) {
      this.form = form;
      form.setWidth("400px");

      codeField = new RTextBox(M.code());
      form.addField(codeField);

      nameField = new RTextBox(M.name());
      form.addField(nameField);

      stateField.setEditable(false);
      stateField.setNullOptionText("全部");
      stateField.addOption(BBasicState.USING, "使用中");
      stateField.addOption(BBasicState.DELETED, "已删除");
      stateField.setValue(BBasicState.USING);
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
      if (codeField != null) {
        filter.setCode(codeField.getValue());
      }
      if (nameField != null) {
        filter.setName(nameField.getValue());
      }
      if (stateField != null) {
        if (stateField.isVisible()) {
          filter.put(BaseWidgetConstants.KEY_FILTER_STATE, stateField.getValue());
        } else {
          filter.put(BaseWidgetConstants.KEY_FILTER_STATE, BBasicState.USING);
        }
      }
    }
  }

  private static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("项目")
    String defualtCaption();

    @DefaultMessage("代码")
    String code();

    @DefaultMessage("名称")
    String name();

    @DefaultMessage("状态")
    String state();

    @DefaultMessage("包含已删除")
    String containsDeleted();

    @DefaultMessage("请选择：{0}")
    String seleteData(String entityCaption);

  }

}
