/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	emposale-web-main-w
 * 文件名：	TextDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-8-22 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.dialog;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.form.RTextArea;

/**
 * @author chenpeisi
 * 
 */
public class TextDialog extends RDialog {

  /** 输入框的默认标题 */
  public static final String DEFAULT_CAPTION = WidgetRes.M.remark();

  /** 提示用户输入的说明文字 */
  private HTML message;

  /** 文本框 */
  private RTextArea textArea;

  /** 是否允许输入空串 */
  private boolean nullable = true;

  /** OK按钮定义 */
  private ButtonConfig okButton;

  public TextDialog() {
    super(false, true);
    setWorkingAreaPadding(25);
    setShowCloseButton(false);
    setEscKeyToHide(true);

    VerticalPanel root = new VerticalPanel();
    root.setWidth("260px");

    message = new HTML();
    message.setWidth("100%");
    root.add(message);

    textArea = new RTextArea();
    textArea.setWidth("260px");
    textArea.setHeight("60px");
    textArea.setEnterToTab(false);
    textArea.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          textArea.setFocus(false);
          getButton(okButton).click();
        }
      }
    });
    root.add(textArea);
    root.setCellHeight(textArea, "26px");
    root.setCellVerticalAlignment(textArea, HasAlignment.ALIGN_BOTTOM);

    setWidget(root);

    okButton = new ButtonConfig(WidgetRes.M.ok(), false);
    setButtons(new ButtonConfig[] {
        okButton, BUTTON_CANCEL });
    getButton(okButton).addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (textArea.getFieldDef() == null) {
          if (!nullable && getText().equals("")) {
            Window.alert(WidgetRes.M.notNull());
            textArea.setFocus(true);
          } else
            hide();
        } else {
          if (textArea.validate()) {
            hide();
          } else {
            textArea.setFocus(true);
            return;
          }
        }
      }
    });
  }

  public TextDialog(FieldDef fieldDef) {
    this();
    textArea.setFieldDef(fieldDef);
    textArea.setCaption(null);
    if (fieldDef != null)
      this.setNullable(fieldDef.isNullable());
  }

  /**
   * 显示输入框，允许输入空串
   * 
   * @param message
   *          提示用户输入的说明文字。允许null。
   * @param defaultValue
   *          文本框的默认内容。允许null。
   * @param callback
   *          回调
   */
  public static void show(String message, String defaultValue, Callback callback) {
    show(null, message, defaultValue, true, callback);
  }

  /**
   * 显示输入框
   * 
   * @param message
   *          提示用户输入的说明文字。允许null。
   * @param defaultValue
   *          文本框的默认内容。允许null。
   * @param nullable
   *          是否允许输入空串
   * @param callback
   *          回调
   */
  public static void show(String message, String defaultValue, boolean nullable,
      final Callback callback) {
    show(null, message, defaultValue, nullable, callback);
  }

  /**
   * 显示输入框
   * 
   * @param title
   *          输入对话框的标题。允许null，表示使用默认的{@link #DEFAULT_CAPTION} 。
   * @param message
   *          提示用户输入的说明文字。允许null。
   * @param defaultValue
   *          文本框的默认内容。允许null。
   * @param nullable
   *          是否允许输入空串
   * @param callback
   *          回调
   */
  public static void show(String title, String message, String defaultValue, boolean nullable,
      final Callback callback) {
    show(title, message, defaultValue, nullable, null, callback);
  }

  /**
   * 显示输入框
   * 
   * @param title
   *          输入对话框的标题。允许null，表示使用默认的{@link #DEFAULT_CAPTION} 。
   * @param message
   *          提示用户输入的说明文字。允许null。
   * @param defaultValue
   *          文本框的默认内容。允许null。
   * @param fieldDef
   *          字段定义
   * @param callback
   *          回调
   */
  public static void show(String title, String message, String defaultValue, FieldDef fieldDef,
      final Callback callback) {
    show(title, message, defaultValue, fieldDef == null ? true : fieldDef.isNullable(), fieldDef,
        callback);
  }

  /**
   * 显示输入框
   * 
   * @param title
   *          输入对话框的标题。允许null，表示使用默认的{@link #DEFAULT_CAPTION} 。
   * @param message
   *          提示用户输入的说明文字。允许null。
   * @param defaultValue
   *          文本框的默认内容。允许null。
   * @param nullable
   *          是否允许输入空串
   * @param fieldDef
   *          字段定义
   * @param callback
   *          回调
   */
  private static void show(String title, String message, String defaultValue, boolean nullable,
      FieldDef fieldDef, final Callback callback) {
    TextDialog textDialog = new TextDialog(fieldDef);

    if (title == null)
      title = DEFAULT_CAPTION;
    textDialog.setCaptionText(title);

    textDialog.setMessage(message);
    textDialog.setText(defaultValue);
    textDialog.setNullable(nullable);

    if (callback != null) {
      textDialog.addCloseHandler(new CloseHandler<PopupPanel>() {
        public void onClose(CloseEvent<PopupPanel> event) {
          TextDialog textDialog = ((TextDialog) event.getSource());
          boolean ok = textDialog.getClickedButton() != null
              && textDialog.getClickedButton().equals(textDialog.okButton);
          String text = ok ? textDialog.getText() : null;
          callback.onClosed(ok, text);
        }
      });
    }

    textDialog.center();
  }

  /**
   * 提示用户输入的说明文字
   * 
   * @param message
   */
  public void setMessage(String message) {
    this.message.setHTML(message);
    this.textArea.getFieldDef().setCaption(message);
  }

  /**
   * 文本框内容
   * 
   */
  public String getText() {
    return textArea.getValue();
  }

  /**
   * 设置文本框内容
   * 
   * @param text
   */
  public void setText(String text) {
    textArea.setValue(text);
  }

  /**
   * 是否允许输入空串
   * 
   * @return
   */
  public boolean isNullable() {
    return nullable;
  }

  /**
   * 设置是否允许输入空串
   * 
   * @param nullable
   */
  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }

  public void show() {
    super.show();

    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

      @Override
      public void execute() {
        textArea.setFocus(true);
        textArea.selectAll();
      }
    });

  }

  /**
   * 输入框回调接口
   * 
   * @author LiQi
   * 
   */
  public static interface Callback {

    /**
     * 当输入框关闭时触发
     * 
     * @param ok
     *          是否点击了OK按钮
     * @param text
     *          输入的文本，如果ok==false，则text为null
     */
    void onClosed(boolean ok, String text);
  }

}
