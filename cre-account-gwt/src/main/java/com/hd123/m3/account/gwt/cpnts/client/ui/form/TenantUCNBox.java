/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	TenantUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2014-1-16 - chenrizhang- 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.base.client.BaseWidgetConstants;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.TenantBrowserDialog;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * @author chenganbang
 * 
 */
public class TenantUCNBox extends RUCNBox implements RUCNQueryByCodeCommand {
  private List<String> states = new ArrayList<String>();
  private TenantBrowserDialog dialog;

  /** 状态范围，未指定则包含全部状态。 */
  public TenantUCNBox() {
    super();
    dialog = new TenantBrowserDialog();
    setBrowser(dialog);

    setCaption(R.defualtCaption());
    setQueryByCodeCommand(this);
  }

  public void setStates(String... states) {
    this.states.clear();
    for (String state : states) {
      this.states.add(state);
    }
    dialog.setStates(states);
  }

  @Override
  public void setCaption(String caption) {
    super.setCaption(caption);
    getBrowser().setCaption(CommonsMessages.M.seleteData(caption));
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    clearConditions();
  }

  @Override
  public void query(String code, AsyncCallback callback) {
    Map<String, Object> filter = new HashMap<String, Object>();
    filter.put(BaseWidgetConstants.KEY_FILTER_STATE, states);
    AccountCpntsService.Locator.getService().getTenantByCode(code, filter, callback);
  }

  public static R R = GWT.isClient() ? (R) GWT.create(R.class) : null;

  public static interface R extends ConstantsWithLookup {
    @DefaultStringValue("商户")
    String defualtCaption();
  }
}
