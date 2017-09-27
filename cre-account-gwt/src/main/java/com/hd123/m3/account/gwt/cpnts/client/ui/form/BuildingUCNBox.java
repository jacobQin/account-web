/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	BuildingUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2016年1月28日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.biz.TypeBUCN;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.BuildingBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * 楼宇浏览框
 * 
 * @author LiBin
 * 
 */
public class BuildingUCNBox extends RUCNBox<TypeBUCN> implements RUCNQueryByCodeCommand {

  private String type;
  private boolean resetPage = false;
  private BuildingBrowserDialog dialog;

  public BuildingUCNBox(String type) {
    super();
    this.type = type;

    dialog = new BuildingBrowserDialog(type);
    setBrowser(dialog);
    setCaption(WidgetRes.M.building());
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
    AccountCpntsService.Locator.getService().getBuildingByCode(type, code, callback);
  }

  public void setType(String type) {
    this.type = type;
    if (dialog != null) {
      dialog.setType(type);
    }
  }

  /**
   * 重新构建filter的page和pageSize信息，用于出账
   * 
   * @param resetPage
   */
  public void setResetPage(boolean resetPage) {
    this.resetPage = resetPage;
    if (dialog != null) {
      dialog.setResetPage(this.resetPage);
    }
  }

}
