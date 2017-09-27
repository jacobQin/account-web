/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	DefrayalTypeComboBox.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.commons.client.gadget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.CPaymentDefrayalType;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PPaymentDef;
import com.hd123.m3.account.gwt.payment.commons.intf.client.dd.PaymentDefrayalTypeDef;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;

/**
 * 
 * 收付款方式控件
 * 
 * @author subinzhu
 * 
 */
public class DefrayalTypeComboBox extends RComboBox<String> {

  /** 根据direction的值，在初始化时判断caption以及options显示的值。 */
  public DefrayalTypeComboBox(int direction) {
    super();
    setEditable(false);
    init(direction);
  }

  private void init(int direction) {
    clearOptions();
    removeNullOption();
    setCaption(direction == 1 ? PPaymentDef.constants.defrayalType() : WidgetRes.R.defrayalType());
    addOption(CPaymentDefrayalType.bill, direction == 1 ? PaymentDefrayalTypeDef.constants.bill()
        : WidgetRes.R.bill());
    if (direction == 1) {
      addOption(CPaymentDefrayalType.lineSingle, PaymentDefrayalTypeDef.constants.lineSingle());
      addOption(CPaymentDefrayalType.line, PaymentDefrayalTypeDef.constants.line());
    } else {
      addOption(CPaymentDefrayalType.line, WidgetRes.R.line());
    }
  }

  public interface WidgetRes extends ConstantsWithLookup {
    public static WidgetRes R = GWT.create(WidgetRes.class);

    @DefaultStringValue("付款方式")
    String defrayalType();

    @DefaultStringValue("按总额付款")
    String bill();

    @DefaultStringValue("按科目付款")
    String line();
  }
}
