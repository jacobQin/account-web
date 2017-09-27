/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	FloorUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2014-10-22 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.FloorBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * 楼层
 * 
 * @author huangjunxian
 * 
 */
public class FloorUCNBox extends RUCNBox implements RUCNQueryByCodeCommand {

  public FloorUCNBox() {
    super();
    setBrowser(new FloorBrowserDialog());
    setCaption(WidgetRes.M.floor());
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
    AccountCpntsService.Locator.getService().getFloorByCode(code, callback);
  }


}
