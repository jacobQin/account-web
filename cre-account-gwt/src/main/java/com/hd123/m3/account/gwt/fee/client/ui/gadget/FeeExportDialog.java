/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	ExportDialog.java
 * 模块说明：	
 * 修改历史：
 * 2015-9-11 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.ui.gadget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BUsageType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.fee.client.EPFee;
import com.hd123.m3.account.gwt.fee.client.FeeMessages;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeTemplate;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeService;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeDef;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeLineDef;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.util.client.message.MessageLevel;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.panel.RMessagePanel;

/**
 * @author liuguilin
 * 
 */
public class FeeExportDialog extends RDialog {

  public static final ButtonConfig BUTTON_EXPORT = new ButtonConfig(FeeMessages.M.export());

  private BFeeTemplate template;

  private RForm form;
  private AccountUnitUCNBox accountUnitBox;
  private RDateBox accountDateBox;
  private RDateBox beginDateBox;
  private RDateBox endDateBox;
  private SubjectUCNBox subjectBox;

  private RMessagePanel messagePanel;

  private Handler_change changeHandler = new Handler_change();

  public FeeExportDialog() {
    super();
    setCaptionText(FeeMessages.M.exportTemplate());
    setWorkingAreaPadding(5);
    setButtonsHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
    setWidth("450px");
    setHeight("250px");
    setWidget(drawSelf());
    setButtons(new ButtonConfig[] {
      BUTTON_EXPORT });
    BUTTON_EXPORT.setClickToClose(false);
    getButton(BUTTON_EXPORT).addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        doExport();
      }
    });
  }

  private Widget drawSelf() {
    VerticalPanel panel = new VerticalPanel();
    panel.setSpacing(8);
    panel.setWidth("100%");

    messagePanel = new RMessagePanel();
    panel.add(messagePanel);

    form = new RForm(1);
    form.setWidth("100%");
    form.setCellSpacing(5);
    form.setLabelWidth(0.2f);

    accountUnitBox = new AccountUnitUCNBox();
    accountUnitBox.setCaption(EPFee.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    accountUnitBox.setRequired(true);
    accountUnitBox.addChangeHandler(changeHandler);
    form.addField(accountUnitBox);

    subjectBox = new SubjectUCNBox(BSubjectType.credit.name(), null, Boolean.TRUE,
        BUsageType.tempFee.name());
    subjectBox.setCaption(PFeeLineDef.constants.subject());
    subjectBox.addChangeHandler(changeHandler);
    form.addField(subjectBox);

    accountDateBox = new RDateBox(PFeeDef.accountDate.getCaption());
    accountDateBox.addChangeHandler(changeHandler);
    form.addField(accountDateBox);

    beginDateBox = new RDateBox(PFeeDef.constants.dateRange_beginDate());
    beginDateBox.addChangeHandler(changeHandler);
    form.addField(beginDateBox);

    endDateBox = new RDateBox(PFeeDef.constants.dateRange_endDate());
    endDateBox.addChangeHandler(changeHandler);
    endDateBox.setValidator(new RValidator() {
      @Override
      public Message validate(Widget sender, String value) {
        if (beginDateBox.getValue() != null && endDateBox.getValue() != null
            && beginDateBox.getValue().after(endDateBox.getValue())) {
          return new Message(PFeeDef.constants.dateRange_endDate() + " : " + FeeMessages.M.cannot()
              + FeeMessages.M.lessThan(PFeeDef.constants.dateRange_beginDate()), MessageLevel.ERROR);
        }
        return null;
      }
    });
    form.addField(endDateBox);

    panel.add(form);
    return panel;
  }

  @Override
  public void center() {
    super.center();
    template = new BFeeTemplate();
    if (EPFee.getInstance().getUserStores().size() == 1) {
      template.setAccountUnit(new BUCN(EPFee.getInstance().getUserStores().get(0)));
    }
    accountUnitBox.setValue(template.getAccountUnit());
    form.clearValidResults();
    messagePanel.clearMessages();
  }

  @Override
  public void hide() {
    super.hide();
    clearValue();
  }

  private void clearValue() {
    accountUnitBox.clearValue();
    accountUnitBox.clearValidResults();
    subjectBox.clearValue();
    subjectBox.clearValidResults();
    accountDateBox.clearValue();
    accountDateBox.clearValidResults();
    beginDateBox.clearValue();
    beginDateBox.clearValidResults();
    endDateBox.clearValue();
    endDateBox.clearValidResults();
    messagePanel.clearMessages();
  }

  private void doExport() {
    GWTUtil.blurActiveElement();

    if (validate() == false) {
      return;
    }

    RLoadingDialog.show(FeeMessages.M.actionDoing(FeeMessages.M.export()));
    FeeService.Locator.getService().exportFile(template, new AsyncCallback<String>() {
      @Override
      public void onFailure(Throwable caught) {
        RLoadingDialog.hide();
        String msg = FeeMessages.M.actionFailed(FeeMessages.M.export(), PFeeDef.TABLE_CAPTION);
        RMsgBox.showError(msg, caught);
      }

      @Override
      public void onSuccess(String result) {
        RLoadingDialog.hide();
        hide();
        Window.open(result, "_blank", "");
      }
    });
  }

  private boolean validate() {
    messagePanel.clearMessages();
    boolean valid = form.validate();
    if (valid == false) {
      messagePanel.putMessages(form.getInvalidMessages());
    }
    return valid;
  }

  private class Handler_change implements ChangeHandler {
    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == accountUnitBox) {
        template.setAccountUnit(accountUnitBox.getValue());
      } else if (event.getSource() == subjectBox) {
        template.setSubject(subjectBox.getValue());
      } else if (event.getSource() == accountDateBox) {
        template.setAccountDate(accountDateBox.getValue());
      } else if (event.getSource() == beginDateBox) {
        template.setBeginDate(beginDateBox.getValue());
        endDateBox.validate();
      } else if (event.getSource() == endDateBox) {
        template.setEndDate(endDateBox.getValue());
      }
    }
  }
}
