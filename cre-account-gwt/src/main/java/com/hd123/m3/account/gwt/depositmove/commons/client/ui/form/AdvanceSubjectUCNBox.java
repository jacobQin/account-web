/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AdvanceSubjectUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.client.ui.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.depositmove.commons.client.rpc.DepositMoveService;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.dialog.AdvanceSubjectBrowserDialog;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * 从指定对方结算中心取出所有的预存款账户科目。对方结算中心uuid为空，返回的预存款科目也为null。
 * 
 * @author zhuhairui
 * 
 */
public class AdvanceSubjectUCNBox extends RUCNBox implements RUCNQueryByCodeCommand {

  private AdvanceSubjectBrowserDialog dialog;
  /** 项目uuid */
  private String accountUnitUuid;
  /** 对方结算中心uuid */
  private String counterpartUuid;
  /** 合同uuid */
  private String contractUuid;
  /** 收付方向 */
  private int direction;
  /** 是否从预存款账户中取合同 */
  private boolean isQueryAdvance = false;

  public AdvanceSubjectUCNBox() {
    super();

    dialog = new AdvanceSubjectBrowserDialog();
    setBrowser(dialog);

    setQueryByCodeCommand(this);
  }

  public AdvanceSubjectUCNBox(String caption) {
    this();
    setCaption(caption);
    dialog.setCaption(caption);
  }

  public AdvanceSubjectUCNBox(StringFieldDef field) {
    this();
    setFieldDef(field);
    dialog.setCaption(field.getCaption());
  }

  @Override
  public void query(String code, AsyncCallback callback) {
    DepositMoveService.Locator.getService().getSubjectByCode(code, direction, accountUnitUuid,
        counterpartUuid, contractUuid, isQueryAdvance, callback);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    clearConditions();
  }

  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
    if (dialog != null)
      dialog.setAccountUnitUuid(accountUnitUuid);
  }

  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
    if (dialog != null)
      dialog.setCounterpartUuid(counterpartUuid);
  }

  public void setContractUuid(String contractUuid) {
    this.contractUuid = contractUuid;
    if (dialog != null)
      dialog.setContractUuid(contractUuid);
  }

  public void setDirection(int direction) {
    this.direction = direction;
    if (dialog != null)
      dialog.setDirection(direction);
  }

  public void setQueryAdvance(boolean isQueryAdvance) {
    this.isQueryAdvance = isQueryAdvance;
    if (dialog != null)
      dialog.setQueryAdvance(isQueryAdvance);
  }

}
