/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	SelectContractPanel.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client.ui.gadget;

import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.contract.accountsettle.client.AccountSettleMessages;
import com.hd123.m3.account.gwt.contract.accountsettle.client.EPAccountSettle;
import com.hd123.m3.account.gwt.contract.accountsettle.client.rpc.AccountSettleService;
import com.hd123.m3.account.gwt.contract.accountsettle.client.ui.AccountSettlePage;
import com.hd123.m3.account.gwt.contract.accountsettle.intf.client.AccountSettleUrlParams;
import com.hd123.m3.account.gwt.contract.intf.client.dd.PSettlementDef;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.BuildingUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CategoryUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractBrowseBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.FloorUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.BPositionState;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.TypePositionBox;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.widget.client.ui.OptionComboBox;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.button.RButton;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidatable;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepPanel;
import com.hd123.rumba.gwt.widget2.client.navigate.roadmap.RRoadmapStepState;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 选择合同
 * 
 * @author huangjunxian
 * 
 */
public class AccSelectContractPanel extends Composite implements RRoadmapStepPanel, RValidatable {

  private AccountSettlePage page;
  private RForm form;
  private AccountUnitUCNBox accountUnitField;
  private RComboBox<String> counterpartTypeField;

  private RCombinedField countpartField;
  private CounterpartUCNBox countpartUCNBox;
  private RViewStringField countpartTypeViewField;
  private ContractBrowseBox contractField;
  private RTextBox contractTitleField;
  private CategoryUCNBox categoryUCNBox;
  private RComboBox<String> buildingTypeField;
  private BuildingUCNBox buildingField;
  private RTextBox settleNameField;
  private FloorUCNBox floorField;
  private TypePositionBox positionField;
  private RComboBox<String> coopModeField;
  private OptionComboBox contractCategoryField;
  private RComboBox<String> calcTypeField;
  private BContract bContract;

  private Handler_change changeHandler = new Handler_change();

  private EPAccountSettle ep = EPAccountSettle.getInstance();

  public AccSelectContractPanel(final AccountSettlePage page) {
    super();

    this.page = page;

    RMultiVerticalPanel rootPanel = new RMultiVerticalPanel(2);
    rootPanel.setWidth("100%");
    rootPanel.setColumnWidth(0, "50%");
    rootPanel.setColumnWidth(1, "50%");
    rootPanel.setSpacing(0);

    form = new RForm(1);

    accountUnitField = new AccountUnitUCNBox();
    accountUnitField.setCaption(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business())
        + AccountSettleMessages.M.captionEquals());
    accountUnitField.getBrowser().setCaption(
        WidgetRes.M.seleteData(ep.getFieldCaption(GRes.FIELDNAME_BUSINESS, GRes.R.business())));
    accountUnitField.addChangeHandler(changeHandler);
    form.addField(accountUnitField);

    counterpartTypeField = new RComboBox<String>();
    counterpartTypeField.setCaption(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART_TYPE,
        GRes.R.counterpartType())
        + AccountSettleMessages.M.captionEquals());
    counterpartTypeField.setEditable(false);
    counterpartTypeField.setMaxDropdownRowCount(10);
    counterpartTypeField.setNullOptionText(CommonsMessages.M.all());
    for (Entry<String, String> entry : ep.getCounterpartTypeMap().entrySet()) {
      counterpartTypeField.addOption(entry.getKey(), entry.getValue());
    }
    counterpartTypeField.addChangeHandler(changeHandler);
    form.addField(counterpartTypeField);

    countpartUCNBox = new CounterpartUCNBox(Boolean.TRUE, Boolean.FALSE, ep.getCaptionMap());
    countpartUCNBox.setCodeBoxWidth(Float.parseFloat("0.44"));
    countpartUCNBox.getBrowser()
        .setCaption(
            WidgetRes.M.seleteData(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART,
                GRes.R.counterpart())));
    countpartUCNBox.setCounterTypeMap(ep.getCounterpartTypeMap());
    countpartUCNBox.addChangeHandler(changeHandler);

    countpartTypeViewField = new RViewStringField();

    countpartField = new RCombinedField() {
      {
        setCaption(ep.getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart())
            + AccountSettleMessages.M.captionEquals());
        if (ep.getCounterpartTypeMap().size() > 1) {
          addField(countpartUCNBox, 0.85f);
          addField(new HTML(), 0.05f);
          addField(countpartTypeViewField, 0.1f);
        } else {
          addField(countpartUCNBox, 1);
        }
      }

      @Override
      public boolean validate() {
        return countpartUCNBox.validate();
      }

      @Override
      public void clearMessages() {
        countpartUCNBox.clearMessages();
      }

      @Override
      public boolean isValid() {
        return countpartUCNBox.isValid();
      }

      @Override
      public List<Message> getInvalidMessages() {
        return countpartUCNBox.getInvalidMessages();
      }
    };
    form.addField(countpartField);

    contractField = new ContractBrowseBox(AccountSettleMessages.M.contract()
        + AccountSettleMessages.M.captionEquals(), true, new ContractBrowseBox.Callback() {
      @Override
      public void execute(BContract result) {
        if (result == null) {
          bContract = null;
          page.getFilter().setContractNumber(null);
        } else {
          bContract = result;
          contractField.setValue(result.getBillNumber());
          contractField.setRawValue(result);
          page.getFilter().setContractNumber(bContract.getBillNumber());
          accountUnitField.setValue(result.getAccountUnit());
          countpartUCNBox.setValue(result.getCounterpart());
          countpartTypeViewField.setValue(result.getCounterpart() == null ? null : ep
              .getCounterpartTypeMap().get(result.getCounterpart().getCounterpartType()));
          page.getFilter().setAccountUnitUuid(
              accountUnitField.getValue() == null ? null : accountUnitField.getValue().getUuid());
          page.getFilter().setCounterpartUuid(
              countpartUCNBox.getValue() == null ? null : countpartUCNBox.getValue().getUuid());

        }
        if (AccSelectContractPanel.this.isVisible() == false) {
          page.doRefresh();
        }
        refreshNextButton();
      }
    }, ep.getCaptionMap());
    contractField.setStateColVisible(true);
    contractField.setCounterTypeMap(ep.getCounterpartTypeMap());
    contractField.getBrowser().addOpenHandler(new OpenHandler<PopupPanel>() {
      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        contractField.setCounterpartLike(countpartUCNBox.getValue() == null ? null
            : countpartUCNBox.getValue().getCode());
        contractField.setAccountUnitLike(accountUnitField.getValue() == null ? null
            : accountUnitField.getValue().getCode());
        contractField.setCounterpartEqual(countpartUCNBox.getRawValue() == null ? null
            : countpartUCNBox.getRawValue().getCounterpartType());
      }
    });
    contractField.setUnlocked(false);
    contractField.addChangeHandler(changeHandler);
    form.addField(contractField);

    contractTitleField = new RTextBox(AccountSettleMessages.M.contract_title()
        + AccountSettleMessages.M.like());
    contractTitleField.addChangeHandler(changeHandler);
    form.addField(contractTitleField);

    categoryUCNBox = new CategoryUCNBox();
    categoryUCNBox.setCaption(AccountSettleMessages.M.category()
        + AccountSettleMessages.M.captionEquals());
    categoryUCNBox.getBrowser().setCaption(
        WidgetRes.M.seleteData(AccountSettleMessages.M.category()));
    categoryUCNBox.addChangeHandler(changeHandler);
    form.addField(categoryUCNBox);

    buildingTypeField = new RComboBox<String>();
    buildingTypeField.setEditable(false);
    buildingTypeField.setNullOptionText(AccountSettleMessages.M.all());
    for (String type : EPAccountSettle.getInstance().getBuildingTypes()) {
      buildingTypeField.addOption(type);
    }
    buildingTypeField.addValueChangeHandler(new ValueChangeHandler<String>() {
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        page.getFilter().setBuildingType(event.getValue());
        buildingField.setType(event.getValue());
        buildingField.clearValue();
        page.getFilter().setBuildingUuid(null);
      }
    });

    buildingField = new BuildingUCNBox(null);
    buildingField.setResetPage(true);
    buildingField.getBrowser().setCaption(
        WidgetRes.M.seleteData(AccountSettleMessages.M.building()));
    buildingField.addChangeHandler(changeHandler);

    RCombinedField buildingCombinField = new RCombinedField() {
      {
        setCaption(AccountSettleMessages.M.building() + AccountSettleMessages.M.captionEquals());
        addField(buildingTypeField, 0.2f);
        addField(buildingField, 0.8f);
        setValidator(buildingField.getValidator());
      }
    };
    form.addField(buildingCombinField);

    floorField = new FloorUCNBox();
    floorField
        .setCaption(AccountSettleMessages.M.floor() + AccountSettleMessages.M.captionEquals());
    floorField.getBrowser().setCaption(WidgetRes.M.seleteData(AccountSettleMessages.M.floor()));
    floorField.addChangeHandler(changeHandler);
    form.addField(floorField);

    positionField = new TypePositionBox();
    positionField.setCaption(AccountSettleMessages.M.positon()
        + AccountSettleMessages.M.captionEquals());
    positionField.getPositionField().setStates(BPositionState.using.name());
    positionField.refreshPositionSubTypes(true);
    positionField.addChangeHandler(changeHandler);
    form.addField(positionField);

    coopModeField = new RComboBox<String>(AccountSettleMessages.M.coopmode()
        + AccountSettleMessages.M.captionEquals());
    coopModeField.setEditable(false);
    coopModeField.addChangeHandler(changeHandler);
    coopModeField.setNullOptionText(AccountSettleMessages.M.all());
    coopModeField.setMaxDropdownRowCount(10);
    if (ep.getCoopModes() != null) {
      for (String value : ep.getCoopModes()) {
        coopModeField.addOption(value, value);
      }
    }
    form.addField(coopModeField);

    contractCategoryField = new OptionComboBox(AccountSettleUrlParams.KEY_CONTRACT_CATEGORY,
        AccountSettleMessages.M.contractCategory(), AccountSettleMessages.M.contractCategory()
            + AccountSettleMessages.M.captionEquals(), false);
    contractCategoryField.clearValue();
    contractCategoryField.setNullOptionText("全部");
    contractCategoryField.setConfigable(false);
    contractCategoryField.setRefreshWhenOpen(true);
    contractCategoryField.setMaxDropdownRowCount(10);
    contractCategoryField.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        page.getFilter().setContractCategory(contractCategoryField.getValue());
      }
    });
    form.addField(contractCategoryField);

    settleNameField = new RTextBox(AccountSettleMessages.M.settle_name()
        + AccountSettleMessages.M.like());
    settleNameField.addChangeHandler(changeHandler);
    form.addField(settleNameField);

    calcTypeField = new RComboBox<String>(PSettlementDef.billCalculateType);
    calcTypeField.setFieldCaption(AccountSettleMessages.M.calculate_type()
        + AccountSettleMessages.M.captionEquals());
    calcTypeField.setRequired(false);
    calcTypeField.setNullOptionText(AccountSettleMessages.M.nullOptionText());
    calcTypeField.addChangeHandler(changeHandler);
    form.addField(calcTypeField);

    RCaptionBox box = new RCaptionBox();
    box.setContentSpacing(0);
    box.setCaption(AccountSettleMessages.M.select_contract());
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setWidth("100%");
    box.setContent(form);

    rootPanel.add(0, box);

    initWidget(rootPanel);
  }

  @Override
  public void onShow() {
    ep.getTitleBar().clearStandardTitle();
    ep.getTitleBar().setTitleText(ep.getModuleCaption());
    ep.getTitleBar().appendAttributeText(AccountSettleMessages.M.select_contract());

    refreshNextButton();

    accountUnitField.setFocus(true);
  }

  public void setAccountUnit(BUCN accountUnit) {
    if (accountUnit != null) {
      accountUnitField.setValue(accountUnit);
    } else {
      accountUnitField.clearValue();
      accountUnitField.clearValidResults();
    }
  }

  public void refreshPreButton() {
    RButton preButton = AccountSettlePage.getRoadMapPreButton(page.getRoadMapPanel());
    if (preButton == null)
      return;
    preButton.setEnabled(true);
  }

  public void refreshNextButton() {
    RButton nextButton = AccountSettlePage.getRoadMapNextButton(page.getRoadMapPanel());
    if (nextButton == null)
      return;
    nextButton.setEnabled(validate());
    nextButton.setText(AccountSettleMessages.M.next());
    page.removeRoadMapNextButtonHandlers();
    page.getRoadMapPanel().getRoadmap().getStep(3).setState(RRoadmapStepState.DISABLED);
    page.getRoadMapPanel().getRoadmap().getStep(4).setState(RRoadmapStepState.DISABLED);
  }

  @Override
  public void onHide() {
    GWTUtil.blurActiveElement();
  }

  public void clear() {
    accountUnitField.clearValue();
    accountUnitField.clearValidResults();
    countpartUCNBox.clearValue();
    countpartUCNBox.clearValidResults();
    counterpartTypeField.clearValue();
    counterpartTypeField.clearValidResults();
    countpartTypeViewField.clearValue();
    contractField.clearValue();
    contractField.clearValidResults();
    settleNameField.clearValue();
    settleNameField.clearValidResults();
    calcTypeField.clearValue();
    calcTypeField.clearValidResults();
    categoryUCNBox.clearValue();
    categoryUCNBox.clearValidResults();
  }

  @Override
  public void clearValidResults() {
    form.clearValidResults();
  }

  @Override
  public boolean isValid() {
    return form.isValid() && positionField.isValid() && buildingField.isValid();
  }

  @Override
  public List<Message> getInvalidMessages() {
    return form.getInvalidMessages();
  }

  @Override
  public boolean validate() {
    form.validate();
    positionField.validate();
    buildingField.validate();
    return form.validate() && positionField.validate() && buildingField.validate();
  }

  /********************************/
  private class Handler_change implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      if (event.getSource() == accountUnitField) {
        page.getFilter().setAccountUnitUuid(
            accountUnitField.getValue() == null ? null : accountUnitField.getValue().getUuid());
        fetchContract();
      } else if (event.getSource() == counterpartTypeField) {
        page.getFilter().setCounterpartType(
            counterpartTypeField.getValue() == null ? null : counterpartTypeField.getValue());
      } else if (event.getSource() == countpartUCNBox) {
        page.getFilter().setCounterpartUuid(
            countpartUCNBox.getValue() == null ? null : countpartUCNBox.getValue().getUuid());
        if (ep.getCounterpartTypeMap().size() > 1) {
          String counterpatType = countpartUCNBox.getRawValue() == null ? null : countpartUCNBox
              .getRawValue().getCounterpartType();
          countpartTypeViewField.setValue(counterpatType == null ? null : ep
              .getCounterpartTypeMap().get(counterpatType));
        }
        fetchContract();
      } else if (event.getSource() == contractTitleField) {
        page.getFilter().setContractTitle(contractTitleField.getValue());
      } else if (event.getSource() == floorField) {
        page.getFilter().setFloorUuid(
            floorField.getValue() == null ? null : floorField.getValue().getUuid());
        if (AccSelectContractPanel.this.isVisible() == false) {
          page.doRefresh();
        }
      } else if (event.getSource() == positionField) {
        page.getFilter().setPostionUuid(
            positionField.getValue() == null ? null : positionField.getValue().getUuid());
        if (positionField.getValue() != null && positionField.getValue().getSearchType() != null) {
          page.getFilter().setPostionType(
              positionField.getValue().getSearchType().getPositionType());
          page.getFilter().setPostionSubType(
              positionField.getValue().getSearchType().getPositionSubType());
        } else {
          page.getFilter().setPostionType(null);
          page.getFilter().setPostionSubType(null);
        }
        if (AccSelectContractPanel.this.isVisible() == false) {
          page.doRefresh();
        }
      } else if (event.getSource() == coopModeField) {
        page.getFilter().setCoopMode(coopModeField.getValue());
      } else if (event.getSource() == settleNameField) {
        page.getFilter().setSettleName(settleNameField.getValue());
      } else if (event.getSource() == calcTypeField) {
        page.getFilter().setBillCalculateType(calcTypeField.getValue());
      } else if (event.getSource() == categoryUCNBox) {
        page.getFilter().setCategory(
            categoryUCNBox.getValue() == null ? null : categoryUCNBox.getValue().getUuid());
        if (AccSelectContractPanel.this.isVisible() == false) {
          page.doRefresh();
        }
      } else if (event.getSource() == buildingField) {
        page.getFilter().setBuildingUuid(
            buildingField.getValue() == null ? null : buildingField.getValue().getUuid());
        if (AccSelectContractPanel.this.isVisible() == false) {
          page.doRefresh();
        }
      }
      refreshNextButton();
    }
  }

  private void fetchContract() {
    if (countpartUCNBox.getValue() == null || accountUnitField.getValue() == null)
      return;

    GWTUtil.enableSynchronousRPC();
    AccountSettleService.Locator.getService().getOnlyOneContract(
        accountUnitField.getValue().getUuid(), countpartUCNBox.getValue().getUuid(), "_Tenant",// TODO
                                                                                               // 对方单位类型
        new RBAsyncCallback2<BContract>() {

          @Override
          public void onException(Throwable caught) {
          }

          @Override
          public void onSuccess(BContract result) {
            if (result != null) {
              bContract = result;
              contractField.setRawValue(result);
              contractField.setValue(result.getBillNumber());
              page.getFilter().setContractNumber(result.getBillNumber());
            } else {
              bContract = null;
              contractField.clearValue();
              contractField.clearRawValue();
              contractField.clearValidResults();
              page.getFilter().setContractNumber(null);
            }

          }
        });
    if (AccSelectContractPanel.this.isVisible() == false) {
      page.doRefresh();
    }
  }
}
