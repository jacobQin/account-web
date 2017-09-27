/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	DepositSubjectUCNBox.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-15 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.SubjectBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.SubjectFilter;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * @author chenpeisi
 * 
 */
public class SubjectUCNBox extends RUCNBox<BSubject> implements RUCNQueryByCodeCommand {

  private Integer direction;
  private SubjectFilter filter = new SubjectFilter();
  private SubjectBrowserDialog dialog;

  public SubjectUCNBox(String subjectType) {
    this(subjectType, null, null, null);
  }

  public SubjectUCNBox(String subjectType, Integer direction) {
    this(subjectType, direction, Boolean.TRUE, null);
  }

  public SubjectUCNBox(String subjectType, Integer direction, Boolean state, String usageType) {
    super();
    this.direction = direction;

    filter.setSubjectType(subjectType);
    filter.setDirectionType(direction);
    filter.setState(state);
    filter.setUsageType(usageType);
    
    dialog = new SubjectBrowserDialog(subjectType, direction, state, usageType,false);
    setBrowser(dialog);
    String caption = null;
    if (subjectType == null || direction == null) {
      caption = WidgetRes.M.subject();
    } else if (BSubjectType.credit.name().equals(subjectType)) {
      caption = WidgetRes.M.subject_account();
    } else if (BSubjectType.predeposit.name().equals(subjectType)) {
      if (DirectionType.payment.getDirectionValue() == direction) {
        caption = WidgetRes.M.subject_paymentDeposit();
      } else if (DirectionType.receipt.getDirectionValue() == direction) {
        caption = WidgetRes.M.subject_receiptDeposit();
      }
    }
    setCaption(caption);
    setQueryByCodeCommand(this);
  }

  @Override
  public void setCaption(String caption) {
    super.setCaption(caption);
    getBrowser().setCaption(WidgetRes.M.seleteData(caption));
  }

  @Override
  public void setFieldCaption(String fieldCaption) {
    super.setFieldCaption(fieldCaption);
    getBrowser().setCaption(WidgetRes.M.seleteData(fieldCaption));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    clearConditions();
  }


  @Override
  public void query(String code, AsyncCallback callback) {
    filter.setCode(code);
    AccountCpntsService.Locator.getService().getSubjectByCode(filter, callback);
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
    dialog.setDirection(direction);
  }

}
