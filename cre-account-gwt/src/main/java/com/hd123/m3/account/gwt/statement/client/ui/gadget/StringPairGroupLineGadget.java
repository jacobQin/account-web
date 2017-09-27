/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	StringPairGroupLineGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014-7-25 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.hd123.m3.account.gwt.commons.client.biz.BStringPairGroup;
import com.hd123.m3.account.gwt.commons.client.biz.BStringPairGroupItem;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.rumba.commons.gwt.event.client.ActionEvent;
import com.hd123.rumba.commons.gwt.event.client.ActionHandler;
import com.hd123.rumba.commons.gwt.event.client.HasActionHandlers;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.LocalCommand;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.hotkey.RHotKey;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;

/**
 * @author huangjunxian
 * 
 */
public class StringPairGroupLineGadget extends Composite implements HasValue<BStringPairGroup>,
    ActionHandler, HasActionHandlers {

  public StringPairGroupLineGadget() {
    super();
    drawSelf();
  }

  private BStringPairGroup value;
  private BStringPairGroupItem line;

  private RCaptionBox box;

  private RAction prevAction;
  private RAction nextAction;
  private RVerticalPanel mainPanel;

  private Handler_action actionHandler = new Handler_action();

  private void drawSelf() {
    mainPanel = new RVerticalPanel();
    mainPanel.setWidth("100%");
    mainPanel.setSpacing(5);

    box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    box.setContent(mainPanel);

    box.getCaptionBar().addButton(new RToolbarSeparator());

    prevAction = new RAction(RActionFacade.PREVIOUS_LINE, actionHandler);
    prevAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_UP));
    RToolbarButton prevButton = new RToolbarButton(prevAction);
    prevButton.setShowText(false);
    box.getCaptionBar().addButton(prevButton);

    nextAction = new RAction(RActionFacade.NEXT_LINE, actionHandler);
    nextAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_DOWN));
    RToolbarButton nextButton = new RToolbarButton(nextAction);
    nextButton.setShowText(false);
    box.getCaptionBar().addButton(nextButton);

    initWidget(box);
  }

  private class Handler_action implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == prevAction) {
        ActionEvent.fire(StringPairGroupLineGadget.this,
            StringPairGroupLineGrid.ActionName.PREVLINE);
      } else if (event.getSource() == nextAction) {
        ActionEvent.fire(StringPairGroupLineGadget.this,
            StringPairGroupLineGrid.ActionName.NEXTLINE);
      }
    }

  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<BStringPairGroup> handler) {
    return null;
  }

  @Override
  public HandlerRegistration addActionHandler(ActionHandler handler) {
    return addHandler(handler, ActionEvent.getType());
  }

  @Override
  public void onAction(final ActionEvent event) {
    if (event.getAction() == StringPairGroupLineGrid.ActionName.CHANGE_LINE) {
      CommandQueue.offer(new LocalCommand() {
        public void onCall(CommandQueue queue) {
          int row = ((Integer) event.getParameter()).intValue();
          box.setCaption(StatementMessages.M.caption_line_no(String.valueOf(row + 1)));
          line = value.getItems().isEmpty() ? null : value.getItems().get(row);
          refresh();
          queue.goon();
        }
      });
      CommandQueue.awake();
    }
  }

  @Override
  public BStringPairGroup getValue() {
    return value;
  }

  @Override
  public void setValue(BStringPairGroup value) {
    setValue(value, false);
  }

  @Override
  public void setValue(BStringPairGroup value, boolean fireEvent) {
    this.value = value;
    mainPanel.clear();
  }

  private void refresh() {
    mainPanel.clear();
    if (line == null || line.getKeys().isEmpty()) {
      return;
    }

    boolean prevDisable = value.getItems().get(0) == line;
    prevAction.setEnabled(!prevDisable);
    boolean nextDisable = value.getItems().get(value.getItems().size() - 1) == line;
    nextAction.setEnabled(!nextDisable);

    for (String detail : line.getDetails()) {
      HTML contentField = new HTML(detail);
      mainPanel.add(contentField);
    }

  }
}
