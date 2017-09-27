package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.hd123.m3.account.gwt.cpnts.client.biz.TypeBUCN;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.BuildingUCNBox;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;

/**
 * 类型-楼宇选择框
 * 
 * @author LiBin
 * 
 */
public class TypeBuildingBox extends RCombinedField implements HasValue<TypeBUCN> {
  private TypeBUCN value;
  private BuildingUCNBox buildingField;
  private RComboBox<String> buildingTypeField;

  private String oldType;

  public TypeBuildingBox() {
    super();

    buildingTypeField = new RComboBox<String>();
    buildingTypeField.setEditable(false);
    buildingTypeField.setNullOptionText("全部");
    for (String type : EPStatement.getInstance().getBuildingTypes()) {
      buildingTypeField.addOption(type);
    }
    buildingTypeField.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        if (oldType != null && oldType.equals(buildingTypeField.getValue())) {
          return;
        }
        oldType = buildingTypeField.getValue();

        if (buildingTypeField.getValue() == null
            || (buildingField.getRawValue() != null && buildingTypeField.getValue().equals(
                buildingField.getRawValue().getType()) == false)) {
          buildingField.clearValue();
          value.inject(new TypeBUCN());
        }
        value.setType(buildingTypeField.getValue());
        buildingField.setType(buildingTypeField.getValue());

        buildingField.clearConditions();
        if (buildingField.validate() == false) {
          buildingField.clearValue();
          buildingField.clearValidResults();
        }
        ValueChangeEvent.fire(TypeBuildingBox.this, value);
      }
    });
    addField(buildingTypeField, 0.375f);

    buildingField = new BuildingUCNBox(null);
    buildingField.setResetPage(true);
    buildingField.addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        value.inject(buildingField.getRawValue() == null ? new TypeBUCN() : buildingField
            .getRawValue());
        fireChange();
      }
    });
    addField(buildingField, 0.625f);
    addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        ValueChangeEvent.fire(TypeBuildingBox.this, value);
      }
    });
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<TypeBUCN> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /** 取得实际楼宇 */
  public TypeBUCN getBuilding() {
    return buildingField.getRawValue();
  }

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
    buildingField.setType(buildingTypeField.getValue());
    buildingField.setRawValue(this.value);
    if (fireEvents) {
      ValueChangeEvent.fire(this, this.value);
    }
  }
}
