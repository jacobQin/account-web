/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AttachmentEditGadget.java
 * 模块说明：	
 * 修改历史：
 * 2014年11月5日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BAttachment;
import com.hd123.m3.account.gwt.statement.client.rpc.StatementService;
import com.hd123.m3.account.gwt.statement.intf.client.perm.StatementPermDef;
import com.hd123.m3.commons.gwt.util.client.i18n.StringFormatter;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.misc.RHoveringBind;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.upload.RUploadField;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 附件编辑面板
 * 
 * @author zhr
 * 
 */
public class AttachmentEditGadget extends RCaptionBox implements HasValue<List<BAttachment>> {

  private static final String WIDTH_COL0 = "200px";
  private static final String WIDTH_COL1 = "50px";

  private EPStatement ep = EPStatement.getInstance();
  private List<BAttachment> attachments = new ArrayList<BAttachment>();

  private RUploadField uploadField;

  private RVerticalPanel filePanel;

  public AttachmentEditGadget() {
    setCaption(M.M.defaultCaption());
    setStyleName(RCaptionBox.STYLENAME_CONCISE);

    RVerticalPanel vp = new RVerticalPanel();
    vp.setWidth("100%");
    vp.setSpacing(5);

    vp.add(drawFilePanel());

    uploadField = new RUploadField() {
      @Override
      public boolean validate() {
        boolean isValid = uploadField.getUploadingFiles().isEmpty();
        if (isValid == false) {
          uploadField.addWarningMessage(StringFormatter.format(M.M.uploadingWarn(),
              CollectionUtil.toString(uploadField.getUploadingFiles())));
        }
        return isValid && super.validate();
      }
    };
    uploadField.setFieldCaption(M.M.addButton());
    uploadField.setMaxFiles(1);
    uploadField.addChangeHandler(new Handler_attachChange());
    vp.add(uploadField);

    if (!ep.isProcessMode())
      uploadField.setEnabled(ep.isPermitted(StatementPermDef.ATTACHMENTEDIT));
    setContent(vp);
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<BAttachment>> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  @Override
  public List<BAttachment> getValue() {
    return attachments;
  }

  @Override
  public void setValue(List<BAttachment> attachments) {
    setValue(attachments, false);
  }

  @Override
  public void setValue(List<BAttachment> attachments, boolean fireEvent) {
    this.attachments = attachments;

    rebuildLineGadget(attachments);
  }

  private Widget drawFilePanel() {
    RVerticalPanel vp = new RVerticalPanel();
    vp.setSpacing(8);
    vp.setWidth("100%");

    filePanel = new RVerticalPanel();
    filePanel.setSpacing(5);
    filePanel.setWidth("100%");
    vp.add(filePanel);

    return vp;
  }

  private void rebuildLineGadget(List<BAttachment> values) {
    filePanel.clear();
    for (BAttachment value : values) {
      buildLine(value);
    }
  }

  private void buildLine(BAttachment attachment) {
    LineGadget line = new LineGadget();
    line.setBAttachment(attachment);
    filePanel.add(line);
  }

  private class LineGadget extends Composite {

    private BAttachment attachment;

    private FlexTable table;
    private RHTMLField fileNameField;
    private RToolbarButton removeButton;

    public LineGadget() {
      table = new FlexTable();
      initWidget(table);

      fileNameField = new RHTMLField();
      table.setWidget(0, 0, fileNameField);
      table.getColumnFormatter().setWidth(0, WIDTH_COL0);

      removeButton = new RToolbarButton(RActionFacade.DELETE, new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
          Iterator<BAttachment> iterator = attachments.iterator();
          while (iterator.hasNext()) {
            BAttachment attach = iterator.next();
            if (attachment.getId().equals(attach.getId())) {
              iterator.remove();
              rebuildLineGadget(attachments);
              ValueChangeEvent.fire(AttachmentEditGadget.this, AttachmentEditGadget.this.getValue());
              return;
            }
          }
        }
      });
      removeButton.setShowText(false);
      removeButton.clearHotKey();

      RCombinedField field = new RCombinedField() {
        {
          setWidth("100%");
          addField(removeButton, 1);
        }
      };

      RHoveringBind hoveringBind = new RHoveringBind();
      hoveringBind.addTrigger(fileNameField);
      hoveringBind.addTrigger(field);
      hoveringBind.addContent(removeButton);

      table.setWidget(0, 1, field);
      table.getColumnFormatter().setWidth(1, WIDTH_COL1);
    }

    public void setBAttachment(BAttachment attachment) {
      this.attachment = attachment;
      fileNameField.setHTML("<a href ='" + attachment.getUrl() + "&fileName="
          + attachment.getName() + "'>" + attachment.getName() + "</a>");
      removeButton.setEnabled(ep.isPermitted(StatementPermDef.ATTACHMENTEDIT));
    }

  }

  private class Handler_attachChange implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() != uploadField)
        return;
      if (uploadField.isUploading() == false) {
        uploadField.clearValidResults();
        upload2MediaServer();
      }
    }
  }

  private void upload2MediaServer() {
    List<String> list = uploadField.getUploadedFilesWithRoot();
    String fileName = list.isEmpty() ? null : list.get(0);
    if (StringUtil.isNullOrBlank(fileName))
      return;

    StatementService.Locator.getService().uploadFile(fileName, new RBAsyncCallback2<BAttachment>() {

      @Override
      public void onException(Throwable caught) {
        uploadField.clearMessages();
        RMsgBox.showError(StringFormatter.format(M.M.uploadFailed(), caught.getMessage()));
        uploadField.clearFiles();
      }

      @Override
      public void onSuccess(BAttachment result) {
        uploadField.clearMessages();
        addAttachment(result);
        uploadField.clearFiles();
      }
    });
  }

  private void addAttachment(BAttachment attach) {
    if (attach == null || attach.getId() == null)
      return;

    for (BAttachment a : attachments) {
      if (attach.getId().equals(a.getId())) {
        RMsgBox.show(M.M.sameAttachment());
        return;
      }
    }
    attachments.add(attach);
    rebuildLineGadget(attachments);
    ValueChangeEvent.fire(AttachmentEditGadget.this, AttachmentEditGadget.this.getValue());
  }

  public interface M extends ConstantsWithLookup {
    public static M M = GWT.create(M.class);

    @DefaultStringValue("附件")
    String defaultCaption();

    @DefaultStringValue("添加...")
    String addButton();

    @DefaultStringValue("取消")
    String cancle();

    @DefaultStringValue("上传文件失败：{0}")
    String uploadFailed();

    @DefaultStringValue("已经存在相同的附件。")
    String sameAttachment();

    @DefaultStringValue("附件：{0}正在上传;")
    String uploadingWarn();
  }

}
