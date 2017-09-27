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

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.CounterpartBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * @author subinzhu
 * 
 */
public class CounterpartUCNBox extends RUCNBox<BCounterpart> implements RUCNQueryByCodeCommand {

  private CounterpartBrowserDialog dialog;
  private Boolean state;
  private Map<String, String> counterTypeMap;

  public CounterpartUCNBox(Map<String, String> captionMap) {
    this(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, captionMap);
  }

  public CounterpartUCNBox(Boolean state, Boolean isStateVisible, Map<String, String> captionMap) {
    this(state, isStateVisible, Boolean.TRUE, captionMap);
  }

  public CounterpartUCNBox(Boolean state, Boolean isStateVisible, Boolean isCounterpartTypeEnable,
      Map<String, String> captionMap) {
    this.state = state;
    dialog = new CounterpartBrowserDialog(isStateVisible, isCounterpartTypeEnable, captionMap);
    setBrowser(dialog);

    setCaption(dialog.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
    setQueryByCodeCommand(this);
  }

  @Override
  public void setCaption(String caption) {
    super.setCaption(caption);
    getBrowser().setCaption(WidgetRes.M.seleteData(caption));
  }

  public void setCounterTypeMap(Map<String, String> counterTypeMap) {
    this.counterTypeMap = counterTypeMap;
    dialog.setCounterTypeMap(counterTypeMap);

  }
  
  public void setCounterpartType(String counterpartType){
    dialog.setCounterpartType(counterpartType);
  }

  public Map<String, String> getCounterTypeMap() {
    return counterTypeMap;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    clearConditions();
  }

  @Override
  public void query(String code, AsyncCallback callback) {
    AccountCpntsService.Locator.getService().getCounterpartByCode(code, state, callback);
  }

}
