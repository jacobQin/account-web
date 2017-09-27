/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	DepositSubjectBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-15 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.ui.gadget;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.SubjectFilter;
import com.hd123.rumba.gwt.util.client.OrderDir;
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
public class SubjectBrowserDialog extends RBrowserDialog<BSubject> {

  private RGridColumnDef codeCol = new RGridColumnDef(WidgetRes.M.code(),
      AccountCpntsContants.ORDER_BY_FIELD_CODE);
  private RGridColumnDef nameCol = new RGridColumnDef(WidgetRes.M.name(),
      AccountCpntsContants.ORDER_BY_FIELD_NAME);

  private PageDataProvider provider;

  public SubjectBrowserDialog(String subjectType, Integer direction, Boolean state, String usageType) {
    super(WidgetRes.M.code(), WidgetRes.M.name());
    setColumnDefs(codeCol, nameCol);

    filter.setSubjectType(subjectType);
    filter.setDirectionType(direction);
    filter.setState(state);
    filter.setUsageType(usageType);

    if (subjectType == null || direction == null) {
      setCaption(WidgetRes.M.seleteData(WidgetRes.M.subject()));
    } else if (BSubjectType.credit.name().equals(subjectType)) {
      setCaption(WidgetRes.M.seleteData(WidgetRes.M.subject_account()));
    } else if (BSubjectType.predeposit.name().equals(subjectType)) {
      if (DirectionType.payment.getDirectionValue() == direction) {
        setCaption(WidgetRes.M.seleteData(WidgetRes.M.subject_paymentDeposit()));
      } else if (DirectionType.receipt.getDirectionValue() == direction) {
        setCaption(WidgetRes.M.seleteData(WidgetRes.M.subject_receiptDeposit()));
      }
    }

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
    getGrid().setAllColumnsOverflowEllipsis(true);
    getGrid().setSingleSelection(false);

  
  }


  public void setDirection(int direction) {
    if (filter != null)
      filter.setDirectionType(direction);
  }

  private SubjectFilter filter = new SubjectFilter();

  private class PageDataProvider implements RPageDataProvider<BSubject>, RFilterCallback {
    private RTextBox codeField;
    private RTextBox nameField;

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BSubject>> callback) {
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(codeCol.getName(), OrderDir.asc);
      }
      AccountCpntsService.Locator.getService().querySubject(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BSubject rowData, List<BSubject> pageData) {
      switch (col) {
      case 0:
        return rowData.getSubject().getCode();
      case 1:
        return rowData.getSubject().getName();
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
      if (filter != null)
        filter.clear();
    }

    @Override
    public void onQuery() {
      if (codeField != null && nameField != null) {
        filter.setCode(codeField.getValue());
        filter.setName(nameField.getValue());
      }
    }
  }
}
