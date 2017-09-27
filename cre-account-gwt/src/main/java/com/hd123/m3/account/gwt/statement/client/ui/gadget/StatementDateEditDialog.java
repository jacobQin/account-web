/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementDateEditDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementLineDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.panel.RMessagePanel;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;

/**
 * 日期编辑Dialog
 * 
 * @author huangjunxian
 * 
 */
public class StatementDateEditDialog extends RDialog implements RValidatable {

  private static final ButtonConfig MY_BUTTON_OK = new ButtonConfig(StatementMessages.M.ok(), false);
  private static final ButtonConfig[] BUTTONS = new ButtonConfig[] {
      MY_BUTTON_OK, BUTTON_CANCEL };

  public StatementDateEditDialog(Callback callback) {
    super();
    this.callback = callback;
    setCaptionText(StatementMessages.M.setLastPayDate());
    setWidth("400px");
    setWorkingAreaPadding(2);

    setWidget(drawSelf());
    setButtons(BUTTONS);
    getButton(MY_BUTTON_OK).addClickHandler(new Handler_okAction());
    setButtonsHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
  }

  private RDateBox dateField;
  private RMessagePanel messagePanel;

  private Widget drawSelf() {
    RVerticalPanel root = new RVerticalPanel();
    root.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    root.setWidth("100%");
    root.setSpacing(0);

    messagePanel = new RMessagePanel();
    root.add(messagePanel);

    RForm form = new RForm(1);
    form.setWidth("98%");
    form.setLabelAlign(HasHorizontalAlignment.ALIGN_LEFT);
    form.setLabelWidth(0.25f);
    dateField = new RDateBox(PStatementLineDef.acc2_lastPayDate);
    dateField.setFormat(M3Format.fmt_yMd);
    form.addField(dateField);
    root.add(form);

    return root;
  }

  private Callback callback;

  public void onShow() {
    dateField.clearValue();
    clearValidResults();
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

      @Override
      public void execute() {
        dateField.setFocus(true);
      }
    });
    super.center();
  }

  @Override
  public void clearValidResults() {
    messagePanel.clearMessages();
    dateField.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return dateField.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    return dateField.getInvalidMessages();
  }

  @Override
  public boolean validate() {
    boolean isValid = dateField.validate();
    if (isValid == false)
      messagePanel.putMessages(getInvalidMessages());
    return isValid;
  }

  private class Handler_okAction implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (validate() == false)
        return;
      if (callback != null) {
        callback.execute(dateField.getValue());
      }
      StatementDateEditDialog.this.hide();
    }
  }

  public interface Callback {
    public void execute(Date date);
  }

}
