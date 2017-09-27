/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-basic
 * 文件名：	StoreMonthDaysField.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月6日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.option.client.ui.settle;

import java.math.BigDecimal;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.hd123.m3.account.gwt.option.client.BStoreMonthDaysOption;
import com.hd123.m3.account.gwt.option.client.OptionMessages;
import com.hd123.m3.account.gwt.option.client.ui.OptionConstants;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedFlowField;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.misc.RHyperlink;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * 项目财务月天数字段控件
 * 
 * @author LiBin
 * 
 */
public class StoreMonthDaysField extends RCombinedFlowField implements HasRActionHandlers {

  private RNumberBox monthDaysField;
  private RCheckBox actualMonthDaysField;
  private RViewStringField captionField;
  private RHyperlink deleteLink;

  private BStoreMonthDaysOption value;

  public StoreMonthDaysField(BStoreMonthDaysOption value) {
    assert value != null;
    this.value = value;
    setCaption("");
    drawSelf();
    addField(captionField);
    addHorizontalSpacing(4);
    addField(monthDaysField);
    addHorizontalSpacing(4);
    addField(actualMonthDaysField);
    addHorizontalSpacing(4);
    addField(deleteLink);
  }

  private void drawSelf() {
    FieldHandlerChange handler = new FieldHandlerChange();

    captionField = new RViewStringField("");
    captionField.setWidth("100px");
    captionField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    captionField.setOverflowEllipsis(true);
    captionField.setValue(value.getStore().toFriendlyStr());

    monthDaysField = new RNumberBox(value.getStore().getName());
    monthDaysField.addValueChangeHandler(handler);
    monthDaysField.setMinValue(0, true);
    monthDaysField.setScale(6);
    monthDaysField.selectAll();
    monthDaysField.addChangeHandler(handler);
    monthDaysField.setTextAlignment(RNumberBox.ALIGN_RIGHT);
    monthDaysField.setWidth("100px");
    monthDaysField.setValue(value.getMonthDays());

    actualMonthDaysField = new RCheckBox();
    actualMonthDaysField.addValueChangeHandler(handler);
    actualMonthDaysField.setText(OptionMessages.M.actualMonthDays());
    actualMonthDaysField.setValue(value.isActualMonthDays(), true);

    deleteLink = new RHyperlink();
    deleteLink.setHTML(OptionMessages.M.remove());
    deleteLink.addClickHandler(handler);
  }

  private class FieldHandlerChange implements ValueChangeHandler, ClickHandler, ChangeHandler {
    @Override
    public void onValueChange(ValueChangeEvent event) {
      if (event.getSource() == monthDaysField) {
        value.setMonthDays(monthDaysField.getValueAsBigDecimal());
      } else if (event.getSource() == actualMonthDaysField) {
        value.setActualMonthDays(actualMonthDaysField.getValue());
        if (value.isActualMonthDays()) {
          monthDaysField.clearValidResults();
          monthDaysField.setEnabled(false);
        } else {
          monthDaysField.setEnabled(true);
          monthDaysField.validate();
        }
      }
    }

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == deleteLink) {
        RActionEvent.fire(StoreMonthDaysField.this, OptionConstants.ACTION_REMOVE_LINE, value);
      }
    }

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == monthDaysField) {
        if (monthDaysField.getValueAsBigDecimal() == null
            || monthDaysField.getValueAsBigDecimal().compareTo(BigDecimal.ZERO) < 0) {
          monthDaysField.setValue(BigDecimal.ZERO, true);
        }
      }

    }

  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

}
