/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	SubjectComboBox.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-29 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.gadget;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;

/**
 * 预收付款科目下拉控件
 * 
 * @author subinzhu
 * 
 */
public class SubjectComboBox extends RComboBox<BUCN> {

  public SubjectComboBox() {
    setEditable(false);
    setMaxDropdownRowCount(10);

    clear();
  }

  private void clear() {
    clearValidResults();
    clearOptions();
    setNullOptionTextToDefault();
    setNullOptionText("[空]");
  }

  public void refreshOptions(List<BUCN> result) {
    clear();
    if (result == null || result.isEmpty())
      return;
    for (BUCN ucn : result) {
      addOption(ucn, ucn.toFriendlyStr());
    }
  }

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes R = GWT.create(WidgetRes.class);

    @DefaultStringValue("预存款科目")
    String collectionSubject();

    @DefaultStringValue("预付款科目")
    String paymentSubject();
  }
}
