/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	CategoryUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2016年1月28日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.CategoryBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * 业态浏览框
 * 
 * @author LiBin
 * 
 */
public class CategoryUCNBox extends RUCNBox implements RUCNQueryByCodeCommand {

  private CategoryBrowserDialog dialog;

  public CategoryUCNBox() {
    super();
    dialog = new CategoryBrowserDialog();
    setBrowser(dialog);
    setCaption(WidgetRes.M.category());
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
    AccountCpntsService.Locator.getService().getCategoryByCode(code, callback);
  }

}
