/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	EmployeeUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-29 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.biz.BEmployee;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.EmployeeBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * @author chenrizhang
 * 
 */
public class EmployeeUCNBox extends RUCNBox<BEmployee> implements RUCNQueryByCodeCommand {
  private EmployeeBrowserDialog dialog;

  public EmployeeUCNBox() {
    this(null);
  }

  public EmployeeUCNBox(Integer state) {
    super();
    dialog = new EmployeeBrowserDialog(state);
    setBrowser(dialog);

    setCaption(WidgetRes.M.employee());
    setQueryByCodeCommand(this);
  }

  @Override
  public void setCaption(String caption) {
    super.setCaption(caption);
    getBrowser().setCaption(WidgetRes.M.seleteData(caption));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    clearConditions();
  }

  @Override
  public void query(String code, AsyncCallback callback) {
    AccountCpntsService.Locator.getService().getEmployeeByCode(code, callback);
  }


}
