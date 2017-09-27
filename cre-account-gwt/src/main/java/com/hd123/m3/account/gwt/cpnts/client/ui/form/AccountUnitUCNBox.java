/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-common
 * 文件名： AccountUnitUCNBox.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-25 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.AccountUnitBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * @author subinzhu
 * 
 */
public class AccountUnitUCNBox extends RUCNBox implements RUCNQueryByCodeCommand {

  private Boolean state;
  private AccountUnitBrowserDialog dialog;

  public AccountUnitUCNBox() {
    this(Boolean.TRUE, Boolean.FALSE);
  }

  public AccountUnitUCNBox(Boolean state, Boolean isStateVisible) {
    this.state = state;
    dialog = new AccountUnitBrowserDialog(state, isStateVisible);
    setBrowser(dialog);

    setCaption(GRes.R.business());
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
    AccountCpntsService.Locator.getService().getAccountUnitByCode(code, state, callback);
  }


}
