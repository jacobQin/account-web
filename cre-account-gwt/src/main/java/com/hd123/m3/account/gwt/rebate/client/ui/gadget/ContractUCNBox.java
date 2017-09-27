/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-sales-web-common
 * 文件名：	_ContractUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2015-2-4 -chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui.gadget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.ContractFilter;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.base.client.BWithUCN;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * 合同浏览框
 * 
 * @author chenganbang
 * 
 */
public class ContractUCNBox extends RUCNBox<BContract> implements RUCNQueryByCodeCommand {

  private ContractBrowserDialog dialog;

  public ContractUCNBox() {
    super();
    dialog = new ContractBrowserDialog();
    setBrowser(dialog);

    setCaption(M.defaultCaption());
    setQueryByCodeCommand(this);
  }

  @Override
  public void setCaption(String caption) {
    super.setCaption(caption);
    getBrowser().setCaption(CommonsMessages.M.seleteData(caption));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    clearConditions();
  }

  /**
   * 设置商户，限制合同为该商户签订的合同
   * 
   * @param tenant
   */
  public void setTenantLimit(BWithUCN tenant) {
    if (dialog != null) {
      dialog.setTenantLimit(tenant);
    }
  }

  /**
   * 设置项目，限制合同为该项目签订的合同
   * 
   * @param store
   */
  public void setStoreLimit(BWithUCN store) {
    if (dialog != null) {
      dialog.setStoreLimit(store);
    }
  }

  @Override
  public void query(final String code, final AsyncCallback callback) {
    ContractFilter filter = new ContractFilter();
    filter.setBillNumber(code);
    filter.setRequiresAccountUnitAndCountpart(false);
    filter.setAccountUnitUuid(null);
    filter.setCounterpartUuid(null);
    filter.setUnlocked(false);
    filter.setTenant(true);
    AccountCpntsService.Locator.getService().getContractByNumber(filter, callback);
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("合同")
    String defaultCaption();

    @DefaultMessage("合同：未找到{0}")
    String noResult(String code);
  }
}
