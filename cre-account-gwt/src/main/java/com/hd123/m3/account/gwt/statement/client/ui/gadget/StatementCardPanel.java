/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementCardPanel.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import com.google.gwt.user.client.ui.FlexTable;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementDef;
import com.hd123.rumba.gwt.widget2.client.form.RFieldLabel;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.panel.RCardPanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;

/**
 * 账单卡片面板
 * 
 * @author huangjunxian
 * 
 */
public class StatementCardPanel extends RCardPanel {
  public StatementCardPanel() {
    super();
    drawSelf();
  }

  private EPStatement ep = EPStatement.getInstance();

  private RFieldLabel label;
  private RViewStringField counterpartField;

  private void drawSelf() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(8);

    label = new RFieldLabel();
    label.setStyleName(RTextStyles.STYLE_OVERVIEW_TITLE);
    vp.add(label);

    FlexTable table = new FlexTable();
    table.setWidth("100%");
    table.getColumnFormatter().setWidth(0, "50%");
    vp.add(table);

    counterpartField = new RViewStringField();
    table.setWidget(0, 0, counterpartField);

    setWidth("100%");
    setContent(vp);
  }

  public void setValue(BStatement entity) {
    if (entity == null) {
      label.setText(null);
      counterpartField.setValue(null);
    } else {
      label.setText(entity.getBillNumber() + ","
          + PStatementDef.bizState.getEnumCaption(entity.getBizState()));
      counterpartField.setValue(entity.getCounterpart() == null ? null : entity.getCounterpart()
          .toFriendlyStr(ep.getCounterpartTypeMap()));
    }

  }

}
