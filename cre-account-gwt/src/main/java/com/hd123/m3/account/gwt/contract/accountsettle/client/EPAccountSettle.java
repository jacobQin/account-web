/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	EPAccountSettle.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.gwt.base.client.AccEPTitleModule;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.contract.accountsettle.client.rpc.AccountSettleService;
import com.hd123.m3.account.gwt.contract.accountsettle.client.ui.AccountSettlePage;
import com.hd123.m3.account.gwt.contract.accountsettle.intf.client.AccountSettleUrlParams;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.gwt.mini.client.http.UrlParameters;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author huangjunxian
 * 
 */
public class EPAccountSettle extends AccEPTitleModule {

  public static EPAccountSettle getInstance() {
    return EPAccountSettle.getInstance(EPAccountSettle.class);
  }

  @Override
  protected void dispatch(String start, UrlParameters urlParams) {
    try {
      if (start == null)
        start = AccountSettlePage.START_NODE;

      if (AccountSettlePage.START_NODE.equals(start))
        showContentPage(AccountSettlePage.getInstance(), urlParams);
    } catch (Exception e) {
      String msg = CommonsMessages.M.processError(CommonsMessages.M.show());
      RMsgBox.showError(msg, e);
    }

  }
  
  public List<String> getBuildingTypes(){
    String types = getModuleContext().get(AccountSettleService.KEY_BUILDING_TYPE);
    if(types==null){
     return new ArrayList<String>();
    }
    return CollectionUtil.toList(types);
  }

  @Override
  protected ModuleServiceAsync getModuleService() {
    return AccountSettleService.Locator.getService();
  }

  @Override
  public String getModuleCaption() {
    return AccountSettleUrlParams.MODULE_CAPTION;
  }

  /** 获取默认流程定义，当前用户必须拥有发起权，否则返回空。 */
  public String getDefaultDef() {
    String key = getModuleContext().get(AccountSettleService.KEY_DEFAULTBPMKEY);
    if (StringUtil.isNullOrBlank(key)) {
      return null;
    }
    for (BProcessDefinition def : getDefs()) {
      if (key.equals(def.getKey())) {
        return key;
      }
    }
    return null;
  }

  private List<BProcessDefinition> defs;

  /** 获取当前用户有发起权的账单流程定义 */
  public List<BProcessDefinition> getDefs() {
    if (defs == null) {
      GWTUtil.enableSynchronousRPC();
      AccountSettleService.Locator.getService().queryProcessDefinition(
          new RBAsyncCallback2<List<BProcessDefinition>>() {

            @Override
            public void onException(Throwable caught) {
              defs = new ArrayList<BProcessDefinition>();
            }

            @Override
            public void onSuccess(List<BProcessDefinition> result) {
              defs = result;

            }
          });
    }
    return defs;
  }

  List<String> coopModes = null;

  /** 获取合作方式列表 */
  public List<String> getCoopModes() {
    if (coopModes == null) {
      coopModes = new ArrayList<String>();
      GWTUtil.enableSynchronousRPC();
      AccountSettleService.Locator.getService().getCoopModes(new RBAsyncCallback2<List<String>>() {

        @Override
        public void onException(Throwable caught) {
        }

        @Override
        public void onSuccess(List<String> result) {
          if (result != null && result.isEmpty() == false) {
            coopModes = result;
          }
        }
      });
    }
    return coopModes;
  }

}
