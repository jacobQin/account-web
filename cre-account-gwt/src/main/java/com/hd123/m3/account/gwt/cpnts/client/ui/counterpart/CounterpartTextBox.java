/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-common
 * 文件名： AccountUnitUCNBox.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-25 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.counterpart;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.REditableField;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;

/**
 * @author chenrizhang
 * 
 */
public class CounterpartTextBox extends REditableField {

  /** 样式 */
  private static final String STYLENAME = "rb-CombinedField";
  private static final String STYLENAME_CONTAINER = "container";
  private static final String STYLENAME_FIELD = "field";

  /** 内部控件间距，应为偶数，以便拆成间隙左右两个控件的padding */
  protected static final int FIELD_SPACING = 4;
  /** 内部控件padding */
  private static final int FIELD_PADDING = FIELD_SPACING / 2;

  private List<String> counterpartTypes;

  /** 根面板 */
  private FlexTable root;

  private RTextBox counterpartField;
  private RComboBox<String> counterpartTypeField;

  /** 状态范围，未指定则包含全部状态。 */
  public CounterpartTextBox() {
    super();
    CounterpartConfigLoader.getInstance().initial();

    counterpartTypeField = new RComboBox<String>();
    counterpartTypeField.setEditable(false);
    for (String counterpartType : CounterpartConfigLoader.getInstance().getCounterpartTypes()) {
      counterpartTypeField.addOption(counterpartType, CounterpartConfigLoader.getInstance()
          .getCaption(counterpartType));
    }
    counterpartField = new RTextBox();

    // 根面板
    root = new FlexTable();
    root.setCellPadding(0);
    root.setCellSpacing(0);
    root.setStyleName(STYLENAME);
    initWidget(root);

    rebuild();
    setCaption(M.defualtCaption());
  }

  public void setCounterpartTypes(List<String> counterpartTypes) {
    this.counterpartTypes = counterpartTypes;
    if (counterpartTypes != null && counterpartTypes.size() == 1) {
      setCaption(CounterpartConfigLoader.getInstance().getCaption(counterpartTypes.get(0)));
      counterpartTypeField.setValue(counterpartTypes.get(0));
    }
    if (counterpartTypeField != null && counterpartTypes != null) {
      counterpartTypeField.clearOptions();
      for (String counterpartType : counterpartTypes) {
        counterpartTypeField.addOption(counterpartType, CounterpartConfigLoader.getInstance()
            .getCaption(counterpartType));
      }
    }
    rebuild();
  }

  @Override
  public void setCaption(String caption) {
    super.setCaption(caption);
    counterpartField.setCaption(caption);
  }

  public void setCounterpart(String counterpart) {
    counterpartField.setValue(counterpart);
  }

  public String getCounterpart() {
    return counterpartField.getValue();
  }

  public void setCounterpartType(String counterpartType) {
    counterpartTypeField.setValue(counterpartType);
  }

  public String getCounterpartType() {
    return counterpartTypeField.getValue();
  }

  public void clearValue() {
    counterpartField.clearValue();
    counterpartTypeField.clearValue();
  }

  @Override
  protected String getTextToValidate() {
    return null;
  }

  private void rebuild() {
    root.removeAllRows();
    if (counterpartTypes == null || counterpartTypes.size() != 1) {
      addField(0, counterpartTypeField, 0.5f);
      addField(1, counterpartField, 0.5f);
    } else {
      addField(0, counterpartField, 1f);
    }
  }

  protected void addField(int index, Widget field, float width) {
    // 内部控件
    field.addStyleName(STYLENAME_FIELD);
    root.setWidget(0, index, field);

    // 内部控件容器
    root.getCellFormatter().setStyleName(0, index, STYLENAME_CONTAINER);
    root.getCellFormatter().setWidth(0, index, floatWidthToCSS(width));

    // 控件间距
    if (index > 0) {
      root.getCellFormatter().getElement(0, index - 1).getStyle()
          .setPaddingRight(FIELD_PADDING, Unit.PX);
      root.getCellFormatter().getElement(0, index).getStyle()
          .setPaddingLeft(FIELD_PADDING, Unit.PX);
    }
  }

  /**
   * float表示的宽度（取值范围[0,1]）转换成css格式的百分比宽度
   * 
   * @param width
   */
  private static String floatWidthToCSS(float width) {
    return new Float(Math.round(width * 10000) / 100.0f).toString() + "%";
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {
    @DefaultMessage("对方单位")
    String defualtCaption();
  }
}
