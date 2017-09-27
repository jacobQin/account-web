/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	SelectRulesGadget.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-26 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.paymentnotice.client.ui.gadget.batch;

import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.hd123.m3.account.gwt.paymentnotice.client.EPPaymentNotice;
import com.hd123.m3.account.gwt.paymentnotice.client.PaymentNoticeMessages;
import com.hd123.m3.account.gwt.paymentnotice.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.paymentnotice.client.ui.PaymentNoticeBatchPage;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.button.RButton;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepPanel;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepState;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;

/**
 * 设置规则
 * 
 * @author huangjunxian
 * 
 */
public class PNSelectRulesGadget extends Composite implements RRoadmapStepPanel, RValidatable {

  private PaymentNoticeBatchPage page;
  private EPPaymentNotice ep = EPPaymentNotice.getInstance();

  private RForm form;
  private RCheckBox splitCheckBox;
  private RComboBox<String> processBoxField;
  private PermGroupEditField permGroupField;

  public PNSelectRulesGadget(PaymentNoticeBatchPage page) {
    super();
    this.page = page;

    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("50%");

    form = new RForm(1);
    form.setWidth("50%");
    panel.add(form);

    splitCheckBox = new RCheckBox();
    splitCheckBox.addValueChangeHandler(new Handler_checkBox());
    splitCheckBox.setCaption(PaymentNoticeMessages.M.batch_splitByContract());
    form.addField(splitCheckBox);

    processBoxField = new RComboBox<String>();
    processBoxField.setFieldCaption(PaymentNoticeMessages.M.paymentNoticeBPM());
    processBoxField.setMaxDropdownRowCount(10);
    processBoxField.setEditable(false);
    processBoxField.setNullOptionText(PaymentNoticeMessages.M.empty_text());
    processBoxField.addValueChangeHandler(new Handler_processBox());
    for (BProcessDefinition def : ep.getDefs()) {
      processBoxField.addOption(def.getKey(), def.getName());
    }
    processBoxField.setValue(ep.getDefaultDef());
    form.addField(processBoxField);

    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    form.addField(permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setCaption(PaymentNoticeMessages.M.batch_generateRule());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("50%");
    box.setContent(panel);
    initWidget(box);
  }

  @Override
  public void onShow() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(PaymentNoticeMessages.M.batch_caption(ep.getModuleCaption()));
    ep.getTitleBar().appendAttributeText(PaymentNoticeMessages.M.batch_selectRules());

    refresh();
    page.removeRoadMapNextButtonHandlers();
    refreshNextButton();
    page.getRoadMapPanel().getRoadmap().getStep(3).setState(RRoadmapStepState.ENABLED);
  }

  private void refresh() {
    if (ep.isPermEnabled()) {
      permGroupField.setVisible(true);
      page.setPerm(permGroupField.getPerm());
    } else
      permGroupField.setVisible(false);
    splitCheckBox.setValue(page.isSplitBill());
  }

  public void refreshNextButton() {
    RButton nextButton = PaymentNoticeBatchPage.getRoadMapNextButton(page.getRoadMapPanel());
    if (nextButton == null)
      return;
    nextButton.setEnabled(true);
    nextButton.setText(PaymentNoticeMessages.M.batch_generate(""));
  }

  @Override
  public void onHide() {
    page.setProDefinition(processBoxField.getValue());
    page.setPerm(permGroupField.getPerm());
  }

  /**************************************/
  private class Handler_checkBox implements ValueChangeHandler {

    @Override
    public void onValueChange(ValueChangeEvent event) {
      page.setSplitBill(splitCheckBox.getValue());
    }
  }

  private class Handler_processBox implements ValueChangeHandler {

    @Override
    public void onValueChange(ValueChangeEvent event) {
      refreshNextButton();
    }
  }

  @Override
  public void clearValidResults() {
    processBoxField.clearValidResults();
    permGroupField.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return processBoxField.isValid() && permGroupField.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = processBoxField.getInvalidMessages();
    messages.addAll(permGroupField.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    return form.validate();
  }

}
