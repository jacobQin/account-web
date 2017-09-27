/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	m3-sales-web-common
 * 文件名：	StoreUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2014-5-28 - libin - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.StoreBrowserDialog;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * 项目选择浏览框
 * 
 * @author libin
 * @since 1.0
 * 
 */
public class StoreUCNBox extends RUCNBox implements RUCNQueryByCodeCommand {
  private StoreBrowserDialog dialog;

  public StoreUCNBox() {
    super();
    dialog = new StoreBrowserDialog();
    setBrowser(dialog);
    setCaption(M.defualtCaption());
    setQueryByCodeCommand(this);
  }

  public StoreUCNBox(String caption) {
    this();
    setCaption(caption);
  }

  @Override
  public void setCaption(String caption) {
    super.setCaption(caption);
    getBrowser().setCaption(CommonsMessages.M.seleteData(caption));
  }

  /**
   * 设置是否显示状态选择框，默认为TRUE（显示），一般情况在需要显示全部数据的地方调用此方法。默认情况下只能选择使用中的数据
   * 
   * @param showStateCheckBox
   */
  public void setShowStateCheckBox(boolean showStateCheckBox) {
    dialog.setShowStateField(showStateCheckBox);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    clearConditions();
  }

  @Override
  public void query(String code, AsyncCallback callback) {
    Map<String, Object> filter = new HashMap<String, Object>();
    AccountCpntsService.Locator.getService().getStoreByCode(code, filter, callback);
  }

  private static M M = GWT.create(M.class);

  public static interface M extends Messages {

    @DefaultMessage("项目")
    String defualtCaption();

  }
}
