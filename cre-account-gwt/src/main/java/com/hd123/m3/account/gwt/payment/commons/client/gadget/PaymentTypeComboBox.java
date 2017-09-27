/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	PaymentTypeComboBox.java
 * 模块说明：	
 * 修改历史：
 * 2013-12-25 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.gadget;

import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RComboBoxOptions.Option;

/**
 * @author subinzhu
 * 
 */
public class PaymentTypeComboBox extends RComboBox<BUCN> {

  public PaymentTypeComboBox(List<BUCN> paymentTypes) {
    setEditable(false);
    clearValidResults();
    clearOptions();
    removeNullOption();
    setMaxDropdownRowCount(10);
    if (paymentTypes == null || paymentTypes.isEmpty())
      return;
    for (BUCN ucn : paymentTypes) {
      addOption(ucn, ucn.toFriendlyStr());
    }
  }

  @Override
  public void setValue(BUCN value, boolean fireEvents) {
    // 旧值
    BUCN oldValue = getValue();

    // 查找
    Option<BUCN> option = getOptions().getByValue(value);

    if (option != null) {
      // 找到，则接受value，并显示相应的文本
      super.setValue(value,false);
      super.setText(option.getText());
    } else {
      // 没找到，如果不可编辑且value!=null，则抛异常
      if (!isEditable() && value != null) {
        addErrorMessage(formatMessage("指定的值不在选项范围内(" + value.toFriendlyStr() + ")。"));
        return;
      }

      // 接受value，value==null时也同样接受
      super.setValue(value,false);
      super.setText(value == null ? "" : value.toString());
    }

    // 触发valueChange事件
    if (fireEvents && isValid())
      ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
  }
}
