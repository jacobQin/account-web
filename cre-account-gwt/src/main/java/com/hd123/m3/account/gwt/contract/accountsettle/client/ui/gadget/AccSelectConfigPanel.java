/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	AccSelectConfigPanel.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-21 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client.ui.gadget;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.hd123.m3.account.gwt.contract.accountsettle.client.AccountSettleMessages;
import com.hd123.m3.account.gwt.contract.accountsettle.client.EPAccountSettle;
import com.hd123.m3.account.gwt.contract.accountsettle.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.contract.accountsettle.client.ui.AccountSettlePage;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.commons.gwt.mini.client.util.Pair;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.button.RButton;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepPanel;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepState;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;

/**
 * 配置
 * 
 * @author huangjunxian
 * 
 */
public class AccSelectConfigPanel extends Composite implements RRoadmapStepPanel, RValidatable {

  private EPAccountSettle ep = EPAccountSettle.getInstance();
  private AccountSettlePage page;
  private RForm form;
  private RComboBox<String> statementField;
  private RComboBox<String> permGroupField;

  public AccSelectConfigPanel(AccountSettlePage page) {
    super();

    this.page = page;

    RMultiVerticalPanel rootPanel = new RMultiVerticalPanel(2);
    rootPanel.setWidth("100%");
    rootPanel.setColumnWidth(0, "50%");
    rootPanel.setColumnWidth(1, "50%");
    rootPanel.setSpacing(0);

    form = new RForm(1);

    statementField = new RComboBox<String>();
    statementField.setFieldCaption(AccountSettleMessages.M.statement_process());
    statementField.setMaxDropdownRowCount(10);
    statementField.setEditable(false);
    statementField.setNullOptionText(AccountSettleMessages.M.empty_text());
    for (BProcessDefinition def : ep.getDefs()) {
      statementField.addOption(def.getKey(), def.getName());
    }
    statementField.setValue(ep.getDefaultDef());
    form.addField(statementField);

    permGroupField = new RComboBox<String>(CommonsMessages.M.permGroup());
    if (ep.isPermEnabled()) {
      permGroupField.setRequired(true);
      permGroupField.addChangeHandler(new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent arg0) {
          refreshNextButton();
        }
      });
    }
    permGroupField.setNullOptionText(AccountSettleMessages.M.empty_text());
    permGroupField.setEditable(false);
    permGroupField.setMaxDropdownRowCount(10);
    for (Pair<String, String> pair : ep.getUserGroupList()) {
      permGroupField.addOption(pair.getLeft(), pair.getRight());
    }
    form.addField(permGroupField);

    RCaptionBox box = new RCaptionBox();
    box.setContentSpacing(0);
    box.setCaption(AccountSettleMessages.M.config());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    box.setContent(form);

    rootPanel.add(0, box);

    initWidget(rootPanel);
  }

  @Override
  public void clearValidResults() {
    statementField.clearValidResults();
    permGroupField.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return statementField.isValid() && permGroupField.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> messages = statementField.getInvalidMessages();
    messages.addAll(permGroupField.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    return form.validate();
  }

  @Override
  public void onShow() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ep.getModuleCaption());
    ep.getTitleBar().appendAttributeText(AccountSettleMessages.M.config());

    page.removeRoadMapNextButtonHandlers();
    refreshNextButton();
    form.clearValidResults();
    statementField.setFocus(true);
  }

  public void refreshNextButton() {
    RButton nextButton = AccountSettlePage.getRoadMapNextButton(page.getRoadMapPanel());
    if (nextButton == null)
      return;
    nextButton.setEnabled(validate());
    nextButton.setText(AccountSettleMessages.M.calculate());
    if (page.getRoadMapPanel().getRoadmap().getStepCount() == 4)
      page.getRoadMapPanel()
          .getRoadmap()
          .getStep(4)
          .setState(nextButton.isEnabled() ? RRoadmapStepState.ENABLED : RRoadmapStepState.DISABLED);
  }

  @Override
  public void onHide() {
    page.setStatementProcesserKey(statementField.getValue());
    page.setPermGroupId(permGroupField.getValue());
    page.setPermGroupTitle(permGroupField.getText());
  }
}
