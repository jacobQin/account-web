/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	PositionUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2014-10-22 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.cpnts.client.biz.TypeBUCN;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.PositionBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.BPositionType;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * 位置
 * 
 * @author huangjunxian
 * 
 */
public class PositionUCNBox extends RUCNBox<TypeBUCN> implements RUCNQueryByCodeCommand {

  private String type = BPositionType.shoppe.name();
  private PositionBrowserDialog dialog;

  public PositionUCNBox(String type) {
    super();
    this.type = type;

    dialog = new PositionBrowserDialog(type);
    setBrowser(dialog);
    setCaption(WidgetRes.M.position());
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
    AccountCpntsService.Locator.getService().getPositionByCode(type, code, callback);
  }


  public void setType(String type) {
    this.type = type;
    if (dialog != null) {
      dialog.setType(type);
    }
  }
  
  public String getType() {
    return type;
  }

}
