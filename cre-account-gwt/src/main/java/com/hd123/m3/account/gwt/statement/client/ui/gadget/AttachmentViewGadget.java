/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AttachmentViewGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014年11月6日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BAttachment;
import com.hd123.m3.account.gwt.statement.intf.client.perm.StatementPermDef;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;

/**
 * 附件查看面板
 * 
 * @author zhr
 * 
 */
public class AttachmentViewGadget extends RCaptionBox {

  public AttachmentViewGadget() {
    super();
    setCaption(M.defaultCaption());
    setStyleName(RCaptionBox.STYLENAME_CONCISE);

    setContent(drawFilePanel());
  }

  public void setValue(List<BAttachment> values) {
    rebuildLineGadget(values);
  }

  private RVerticalPanel root;
  private EPStatement ep = EPStatement.getInstance();

  private Widget drawFilePanel() {
    root = new RVerticalPanel();
    root.setSpacing(5);
    root.setWidth("100%");
    return root;
  }

  private void rebuildLineGadget(List<BAttachment> values) {
    root.clear();
    for (BAttachment value : values) {
      buildLine(value);
    }
  }

  private void buildLine(BAttachment attachment) {
    final RHTMLField fileField = new RHTMLField();

    if (!ep.isPermitted(StatementPermDef.READ) || !ep.isPermitted(StatementPermDef.UPDATE))
      fileField.setHTML(attachment.getName());
    else
      fileField.setHTML("<a href ='" + attachment.getUrl() + "&fileName=" + attachment.getName()
          + "'>" + attachment.getName() + "</a>");

    fileField.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        if (!ep.isPermitted(StatementPermDef.READ))
          RMsgBox.show("无查看权，请申请权限。");
        else if (!ep.isPermitted(StatementPermDef.UPDATE))
          RMsgBox.show("无编辑权，请申请权限。");
        else if (!ep.isPermitted(StatementPermDef.READ) && !ep.isPermitted(StatementPermDef.UPDATE))
          RMsgBox.show("无查看权与编辑权，请申请权限。");
      }
    });
    root.add(fileField);
  }

  public static M M = GWT.create(M.class);

  public static interface M extends Messages {
    @DefaultMessage("附件")
    String defaultCaption();
  }

}
