/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StringPairGroupGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014-7-25 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.hd123.m3.account.gwt.commons.client.biz.BStringPairGroup;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;

/**
 * @author huangjunxian
 * 
 */
public class StringPairGroupGadget extends Composite implements HasValue<BStringPairGroup> {

  public StringPairGroupGadget() {
    super();
    drawSelf();
  }

  private BStringPairGroup value;

  private RVerticalPanel rootPanel;
  private HTML captionField;
  private StringPairGroupLineGrid lineGrid;
  private StringPairGroupLineGadget lineGadget;

  private void drawSelf() {
    rootPanel = new RVerticalPanel();
    rootPanel.setWidth("100%");

    captionField = new HTML();
    rootPanel.add(captionField);

    lineGrid = new StringPairGroupLineGrid();
    rootPanel.add(lineGrid);

    lineGadget = new StringPairGroupLineGadget();
    rootPanel.add(lineGadget);

    lineGrid.addActionHandler(lineGadget);
    lineGadget.addActionHandler(lineGrid);
    initWidget(rootPanel);
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<BStringPairGroup> arg0) {
    return null;
  }

  @Override
  public BStringPairGroup getValue() {
    return value;
  }

  @Override
  public void setValue(BStringPairGroup value) {
    setValue(value, false);

  }

  @Override
  public void setValue(BStringPairGroup value, boolean fireEvent) {
    this.value = value;
    if (value != null) {
      captionField.setHTML(value.getCaption());
      captionField.setVisible(value.getCaption() != null);
      lineGadget.setValue(value);
      lineGadget.setVisible(value.isShowDetail());
      lineGrid.setValue(value);
      lineGrid.setVisible(true);
    } else {
      captionField.setVisible(false);
      lineGadget.setVisible(false);
      lineGrid.setVisible(false);
    }
  }

}
