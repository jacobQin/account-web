/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	SettleNoField.java
 * 模块说明：	
 * 修改历史：
 * 2015-4-21 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;

/**
 * 结转期控件
 * 
 * @author huangjunxian
 * 
 */
public class SettleNoField extends RComboBox<String> {

  public SettleNoField() {
    super();
    this.setMaxDropdownRowCount(10);
  }

  public SettleNoField(String caption) {
    this();
    setCaption(caption);
  }

  public SettleNoField(FieldDef fieldDef) {
    this();
    setFieldDef(fieldDef);
    setMaxLength(6);
  }

  /**
   * 根据过去的N个月份构造选项值。
   * 
   * @param count
   *          过去几个月的数据。
   */
  public void refreshOption(int count) {
    this.clearOptions();
    for (int i = 0; i < count; i++) {
      Date date = RDateUtil.addMonth(new Date(), -i);
      String value = fmt_YM.format(date);
      this.addOption(value, value);
    }
  }

  public static final String yyyyMM = "yyyyMM";
  public static final DateTimeFormat fmt_YM = DateTimeFormat.getFormat(yyyyMM);

  /**
   * 获取当前结转期
   * 
   * @return
   */
  public static String getCurrentSettleNo() {
    return fmt_YM.format(new Date());
  }
}
