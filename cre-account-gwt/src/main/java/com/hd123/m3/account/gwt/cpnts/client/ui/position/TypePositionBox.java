package com.hd123.m3.account.gwt.cpnts.client.ui.position;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.ui.HasValue;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author chenrizhang
 * 
 */
public class TypePositionBox extends RCombinedField implements HasValue<SPosition> {
  private SPosition value = new SPosition();
  private PositionUCNBox positionField;
  private RComboBox<PositionSubType> positionTypeField;

  public TypePositionBox() {
    super();
    setCaption(M.defualtCaption());

    positionTypeField = new RComboBox<PositionSubType>();
    positionTypeField.setEditable(false);
    positionTypeField.setNullOptionText("全部");
    positionTypeField.addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        if (positionTypeField.getValue() != null && positionField.getRawValue() != null) {
          if (positionTypeField.getValue().getPositionType()
              .equals(positionField.getRawValue().getPositionType()) == false
              || (positionTypeField.getValue().getPositionSubType() != null && positionTypeField
                  .getValue().getPositionSubType()
                  .equals(positionField.getRawValue().getPositionSubType()) == false)) {
            positionField.clearValue();
            value.inject(new SPosition());
          }
        }
        value.setSearchType(positionTypeField.getValue());
        if (positionField.getValue().getUuid() == null) {
          value.setPositionType(value.getSearchType() == null ? null : value.getSearchType()
              .getPositionType());
          value.setPositionSubType(value.getSearchType() == null ? null : value.getSearchType()
              .getPositionSubType());
        }
        positionField.setPositionType(positionTypeField.getValue());
        if (positionField.validate() == false) {
          positionField.clearValue();
          positionField.clearValidResults();
        }
        fireChange();
      }
    });
    addField(positionTypeField, 0.375f);

    positionField = new PositionUCNBox();
    positionField.addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        value.inject(positionField.getRawValue() == null ? new SPosition() : positionField
            .getRawValue());
        value.setSearchType(positionTypeField.getValue());// 搜索类型不能改
        fireChange();
      }
    });
    addField(positionField, 0.625f);
    addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        if (validate()) {
          ValueChangeEvent.fire(TypePositionBox.this, value);
        }
      }
    });

    refreshPositionSubTypes(false);
  }

  public PositionUCNBox getPositionField() {
    return positionField;
  }

  /**
   * 设置铺位类型，会导致铺位类型的搜索条件隐藏。
   * 
   * @param positionType
   */
  public void setPositionType(BPositionType positionType) {
    if (positionType != null) {
      positionField.setPositionType(new PositionSubType(positionType.name()));
      positionTypeField.setVisible(false);
      setFieldWidth(positionTypeField, 0f);
      setFieldWidth(positionField, 1f);
    } else {
      positionField.setPositionType(positionType);
      positionTypeField.setVisible(true);
      setFieldWidth(positionTypeField, .375f);
      setFieldWidth(positionField, .625f);
    }
  }

  /**
   * 是否显示浏览框的状态搜索条件
   * 
   * @param showState
   */
  public void setShowDialogState(boolean showState) {
    positionField.setShowState(showState);
  }

  public RComboBox<PositionSubType> getPositionTypeField() {
    return positionTypeField;
  }

  public void refreshPositionSubTypes(boolean includeSubType) {
    positionField.refreshPositionSubTypes(includeSubType);
    positionTypeField.clearOptions();
    if (includeSubType == false) {
      positionTypeField.addOption(new PositionSubType(BPositionType.shoppe.name()),
          BPositionType.shoppe.getCaption());
      positionTypeField.addOption(new PositionSubType(BPositionType.booth.name()),
          BPositionType.booth.getCaption());
      positionTypeField.addOption(new PositionSubType(BPositionType.adPlace.name()),
          BPositionType.adPlace.getCaption());
      return;
    }

    GWTUtil.enableSynchronousRPC();
    AccountCpntsService.Locator.getService().getPositionSubTyoes(
        new RBAsyncCallback2<Map<String, List<String>>>() {

          @Override
          public void onException(Throwable caught) {
            RMsgBox.showError(caught);
          }

          @Override
          public void onSuccess(Map<String, List<String>> positionSubTyoes) {

            positionTypeField.addOption(new PositionSubType(BPositionType.shoppe.name()),
                BPositionType.shoppe.getCaption());
            if (positionSubTyoes.containsKey(BPositionType.shoppe.name())) {
              for (String subType : positionSubTyoes.get(BPositionType.shoppe.name())) {
                PositionSubType positionSubType = new PositionSubType(BPositionType.shoppe.name(),
                    subType);
                positionTypeField.addOption(positionSubType, positionSubType.toFriendlyStr());
              }
            }
            positionTypeField.addOption(new PositionSubType(BPositionType.booth.name()),
                BPositionType.booth.getCaption());
            if (positionSubTyoes.containsKey(BPositionType.booth.name())) {
              for (String subType : positionSubTyoes.get(BPositionType.booth.name())) {
                PositionSubType positionSubType = new PositionSubType(BPositionType.booth.name(),
                    subType);
                positionTypeField.addOption(positionSubType, positionSubType.toFriendlyStr());
              }
            }
            positionTypeField.addOption(new PositionSubType(BPositionType.adPlace.name()),
                BPositionType.adPlace.getCaption());
            if (positionSubTyoes.containsKey(BPositionType.adPlace.name())) {
              for (String subType : positionSubTyoes.get(BPositionType.adPlace.name())) {
                PositionSubType positionSubType = new PositionSubType(BPositionType.adPlace.name(),
                    subType);
                positionTypeField.addOption(positionSubType, positionSubType.toFriendlyStr());
              }
            }
          }
        });
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<SPosition> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /** 取得实际铺位 */
  public SPosition getPosition() {
    return positionField.getRawValue();
  }

  /** 注意：value中的positionType为具体选择的positionType。若要获取实际选择的铺位，请调用getPosition */
  @Override
  public SPosition getValue() {
    return value;
  }

  @Override
  public void setValue(SPosition value) {
    setValue(value, false);
  }

  @Override
  public void setValue(SPosition value, boolean fireEvents) {
    this.value = value == null ? new SPosition() : value;
    positionTypeField.setValue(this.value.getSearchType() == null ? null : this.value
        .getSearchType());
    if (positionTypeField.getValue() != null) {
      positionField.setPositionType(positionTypeField.getValue());
    }
    positionField.setRawValue(this.value);
    if (fireEvents) {
      ValueChangeEvent.fire(this, this.value);
    }
  }

  @Override
  public boolean validate() {
    boolean valid = positionTypeField.validate();
    valid &= positionField.validate();
    return valid;
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = new ArrayList<Message>();
    messages.addAll(positionTypeField.getInvalidMessages());
    messages.addAll(positionField.getInvalidMessages());
    return messages;
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("位置")
    String defualtCaption();

  }
}
