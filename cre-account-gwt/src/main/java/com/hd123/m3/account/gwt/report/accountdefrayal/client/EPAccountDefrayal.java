/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	EPAccountDefrayal.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-14 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.report.accountdefrayal.client;

import java.util.Map;

import com.hd123.m3.account.gwt.base.client.AccEPTitleModule;
import com.hd123.m3.account.gwt.report.accountdefrayal.client.rpc.AccountDefrayalService;
import com.hd123.m3.account.gwt.report.accountdefrayal.client.ui.AccountDefrayalSearchPage;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.AccountDefrayalMessages;
import com.hd123.m3.account.gwt.report.accountdefrayal.intf.client.dd.PAccountDefrayalDef;
import com.hd123.m3.commons.gwt.base.client.M3EPTitleModule;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;

/**
 * @author zhuhairui
 * 
 */
public class EPAccountDefrayal extends AccEPTitleModule {

  public static EPAccountDefrayal getInstance() {
    return M3EPTitleModule.getInstance(EPAccountDefrayal.class);
  }

  @Override
  protected void dispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = AccountDefrayalSearchPage.START_NODE;
      if (AccountDefrayalSearchPage.START_NODE.equals(start))
        showContentPage(AccountDefrayalSearchPage.getInstance(), urlParams);
    } catch (Exception e) {
      RMsgBox.showError(AccountDefrayalMessages.M.showPageError(getModuleCaption()), e);
    }
  }

  @Override
  protected String getModuleCaption() {
    return PAccountDefrayalDef.TABLE_CAPTION;
  }

  @Override
  protected ModuleServiceAsync getModuleService() {
    return AccountDefrayalService.Locator.getService();
  }

  /** 单据类型 */
  public Map<String, String> getBillType() {
    String billTypeString = getModuleContext().get(AccountDefrayalService.KEY_BILLTYPES);
    return CollectionUtil.toMap(billTypeString);
  }

}
