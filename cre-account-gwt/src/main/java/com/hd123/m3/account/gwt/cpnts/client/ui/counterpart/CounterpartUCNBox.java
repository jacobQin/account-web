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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.form.REditableField;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNQueryByCodeCommand;

/**
 * @author chenrizhang
 * 
 */
public class CounterpartUCNBox extends REditableField implements Focusable, HasValue<BCounterpart> {

  /** 样式 */
  private static final String STYLENAME = "rb-CombinedField";
  private static final String STYLENAME_CONTAINER = "container";
  private static final String STYLENAME_FIELD = "field";

  /** 内部控件间距，应为偶数，以便拆成间隙左右两个控件的padding */
  protected static final int FIELD_SPACING = 4;
  /** 内部控件padding */
  private static final int FIELD_PADDING = FIELD_SPACING / 2;

  private Map<String, Object> filter = new HashMap<String, Object>();
  private List<String> counterpartTypes;

  /** 根面板 */
  private FlexTable root;

  private CounterpartBrowserDialog dialog;
  private RUCNBox<BCounterpart> counterpartField;
  private RViewStringField counterpartTypeField;

  /**
   * valueChangeHandler初始化标志，用于确保初始化代码只在
   * {@link #addValueChangeHandler(ValueChangeHandler)}第一次被调用时执行}
   */
  private boolean valueChangeHandlerInitialized = false;

  /** 状态范围，未指定则包含全部状态。 */
  public CounterpartUCNBox() {
    super();
    CounterpartConfigLoader.getInstance().initial();

    dialog = new CounterpartBrowserDialog();
    counterpartField = new RUCNBox<BCounterpart>();
    counterpartField.setBrowser(dialog);
    counterpartField.addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        if (counterpartTypeField.isVisible() && counterpartField.getRawValue() != null) {
          String caption = CounterpartConfigLoader.getInstance().getCaption(
              counterpartField.getRawValue().getModule());
          counterpartTypeField.setValue(caption == null ? M.defualtCaption() : caption);
        }
        CounterpartUCNBox.this.fireChange();
      }
    });
    counterpartField.setQueryByCodeCommand(new RUCNQueryByCodeCommand<BCounterpart>() {

      @Override
      public void query(String code, AsyncCallback<BCounterpart> callback) {
        if (counterpartTypes == null) {
          counterpartTypes = CounterpartConfigLoader.getInstance().getCounterpartTypes();
        }

        filter.put(BCounterpart.KEY_FILTER_MODULE, counterpartTypes);
        AccountCpntsService.Locator.getService().getCounterpartByCode(code, filter, callback);
      }
    });

    counterpartTypeField = new RViewStringField();
    counterpartTypeField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

    // 根面板
    root = new FlexTable();
    root.setCellPadding(0);
    root.setCellSpacing(0);
    root.setStyleName(STYLENAME);
    initWidget(root);

    rebuild();
    setCaption(M.defualtCaption());
  }

  public void setStateCondition(String state) {
    dialog.setStateCondition(state);
  }

  public void setState(String state) {
    dialog.setState(state);
  }

  public void showStateField(boolean showStateFidld) {
    dialog.showStateField(showStateFidld);
  }

  public void setCounterpartTypes(List<String> counterpartTypes) {
    this.counterpartTypes = counterpartTypes;
    if (counterpartTypes.size() == 1) {
      setCaption(CounterpartConfigLoader.getInstance().getCaption(counterpartTypes.get(0)));
      rebuild();
    }
    dialog.setCounterpartTypes(counterpartTypes);
  }

  @Override
  public void setCaption(String caption) {
    super.setCaption(caption);
    counterpartField.setCaption(caption);
    counterpartField.getBrowser().setCaption(CommonsMessages.M.seleteData(caption));
  }

  public void setValue(BCounterpart value) {
    counterpartField.setRawValue(value);
    counterpartTypeField.setValue(value == null ? M.defualtCaption() : CounterpartConfigLoader
        .getInstance().getCaption(value.getModule()));
  }

  @Override
  public void setValue(BCounterpart value, boolean fireEvents) {
    setValue(value);
    if (fireEvents) {
      ValueChangeEvent.fire(this, value);
    }
  }

  /** 取得值 */
  public BCounterpart getValue() {
    return counterpartField.getRawValue();
  }

  /** 显示内容值 */
  public BCounterpart getDisplayValue() {
    BCounterpart counterpart = getValue();
    if (counterpart == null) {
      counterpart = new BCounterpart();
      counterpart.setCode(counterpartField.getCodeBox().getValue());
    }
    return counterpart;
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<BCounterpart> handler) {
    if (!valueChangeHandlerInitialized) {
      valueChangeHandlerInitialized = true;
      addChangeHandler(new ChangeHandler() {
        public void onChange(ChangeEvent event) {
          // 验证成功时触发valueChange事件
          if (isValid())
            ValueChangeEvent.fire(CounterpartUCNBox.this, getValue());
        }
      });
    }
    return addHandler(handler, ValueChangeEvent.getType());
  }

  public void clearConditions() {
    counterpartField.clearConditions();
  }

  @Override
  protected String getTextToValidate() {
    return null;
  }

  private void rebuild() {
    root.removeAllRows();
    if (counterpartTypes == null || counterpartTypes.size() != 1) {
      counterpartField.setCodeBoxWidth(0.46875f);
      addField(0, counterpartField, 0.8f);
      addField(1, counterpartTypeField, 0.2f);
    } else {
      counterpartField.setCodeBoxWidth(0.375f);
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

  public CounterpartBrowserDialog getDialog() {
    return dialog;
  }

  public void setDialog(CounterpartBrowserDialog dialog) {
    this.dialog = dialog;
  }

  @Override
  public void setRequired(boolean required) {
    super.setRequired(required);
    counterpartField.setRequired(required);
  }

  @Override
  public boolean validate() {
    return counterpartField.validate();
  }

  @Override
  public boolean isValid() {
    return counterpartField.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    return counterpartField.getInvalidMessages();
  }

  @Override
  public void clearValidResults() {
    counterpartField.clearValidResults();
  }

  @Override
  public int getTabIndex() {
    return counterpartField.getTabIndex();
  }

  @Override
  public void setAccessKey(char key) {
    counterpartField.setAccessKey(key);
  }

  @Override
  public void setFocus(boolean focused) {
    counterpartField.setFocus(focused);
  }

  @Override
  public void setTabIndex(int index) {
    counterpartField.setTabIndex(index);
  }

  public Map<String, Object> getFilter() {
    return filter;
  }

  public void setFilter(Map<String, Object> filter) {
    this.filter = filter;
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
