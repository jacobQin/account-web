/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	VendorUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.ui.gadget;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.internalfee.client.InternalFeeMessages;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeService;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * @author liuguilin
 * 
 */
public class VendorUCNBox extends RUCNBox implements RUCNQueryByCodeCommand {

  private VendorBrowserDialog dialog;

  private Boolean using;
  private Boolean freezed;

  public VendorUCNBox(Boolean state, Boolean freezed) {
    this.using = state;
    this.freezed = freezed;
    dialog = new VendorBrowserDialog(state, freezed);
    setBrowser(dialog);
    setCaption(InternalFeeMessages.M.vendor());
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
    InternalFeeService.Locator.getService().getVendorByCode(code, using, freezed, callback);
  }

}