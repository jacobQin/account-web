/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	SourceBillTypeBox.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-30 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;

/**
 * @author huangjunxian
 * 
 */
public class SourceBillTypeBox extends RComboBox<String> {
  private EPStatement ep = EPStatement.getInstance();

  public SourceBillTypeBox() {
    super();
    init();
  }

  public SourceBillTypeBox(FieldDef fieldDef) {
    super(fieldDef);
    init();
  }

  private void init() {
    setEditable(false);
    setRequired(false);
    setMaxDropdownRowCount(10);
    for (String key : ep.getBillType().keySet()) {
      addOption(key, ep.getBillType().get(key));
    }
  }
}
