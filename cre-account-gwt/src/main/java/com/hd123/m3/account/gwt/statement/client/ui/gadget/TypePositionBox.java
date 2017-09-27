package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.hd123.m3.account.gwt.cpnts.client.biz.TypeBUCN;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.PositionUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.BPositionType;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;

/**
 * 类型-位置选择框
 * 
 * @author Libin
 * 
 */
public class TypePositionBox extends RCombinedField implements HasValue<TypeBUCN> {
  private TypeBUCN value;
  private PositionUCNBox positionField;
  private RComboBox<String> positionTypeField;

  private String oldType;

  public TypePositionBox() {
    super();

    positionTypeField = new RComboBox<String>();
    positionTypeField.setEditable(false);
    positionTypeField.setNullOptionText("全部");
    positionTypeField.addOption(BPositionType.shoppe.name(), BPositionType.shoppe.getCaption());
    positionTypeField.addOption(BPositionType.booth.name(), BPositionType.booth.getCaption());
    positionTypeField.addOption(BPositionType.adPlace.name(), BPositionType.adPlace.getCaption());
    positionTypeField.addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        if (oldType != null && oldType.equals(positionTypeField.getValue())) {
          return;
        }
        oldType = positionTypeField.getValue();

        if (positionTypeField.getValue() == null
            || (positionField.getRawValue() != null && positionTypeField.getValue().equals(
                positionField.getRawValue().getType()) == false)) {
          positionField.clearValue();
          value.inject(new TypeBUCN());
        }
        value.setType(positionTypeField.getValue());
        positionField.setType(positionTypeField.getValue());

        positionField.clearConditions();
        if (positionField.validate() == false) {
          positionField.clearValue();
          positionField.clearValidResults();
        }
        ValueChangeEvent.fire(TypePositionBox.this, value);
      }
    });
    addField(positionTypeField, 0.375f);

    positionField = new PositionUCNBox(null);
    positionField.addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        value.inject(positionField.getRawValue() == null ? new TypeBUCN() : positionField
            .getRawValue());
        value.setType(positionTypeField.getValue());
        fireChange();
      }
    });
    addField(positionField, 0.625f);
    addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        ValueChangeEvent.fire(TypePositionBox.this, value);
      }
    });
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<TypeBUCN> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /** 取得实际铺位 */
  public TypeBUCN getPosition() {
    return positionField.getRawValue();
  }

  /** 注意：value中的positionType为具体选择的positionType。若要获取实际选择的铺位，请调用getPosition */
  @Override
  public TypeBUCN getValue() {
    return value;
  }

  @Override
  public void setValue(TypeBUCN value) {
    setValue(value, false);
  }

  @Override
  public void setValue(TypeBUCN value, boolean fireEvents) {
    this.value = value == null ? new TypeBUCN() : value;
    positionField.setValue(this.value);
    positionField.setRawValue(this.value);
    positionTypeField.setValue(this.getValue().getType());
    if (fireEvents) {
      ValueChangeEvent.fire(this, this.value);
    }
  }
}
