/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AdvanceSubjectUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-21 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.commons.client.ui.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.rpc.DepositRepaymentService;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.ui.dialog.AdvanceSubjectBrowserDialog;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * 从指定对方结算中心取出所有的预存款账户科目。对方结算中心uuid为空，返回的预存款科目也为null。
 * 
 * @author chenpeisi
 * 
 */
public class AdvanceSubjectUCNBox extends RUCNBox implements RUCNQueryByCodeCommand {

  private AdvanceSubjectBrowserDialog dialog;
  /** 项目 */
  private String accountUnitUuid;
  /** 商户 */
  private String counterpartUuid;
  /** 合同uuid */
  private String contractUuid;
  private int direction;

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
    DepositRepaymentService.Locator.getService().getSubjetByCode(code, direction, accountUnitUuid,
        counterpartUuid, contractUuid, callback);
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
}
