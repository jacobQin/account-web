/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	ContractBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-17 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.dialog;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContractState;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.ContractFilter;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.commons.gwt.widget.client.ui.OptionComboBox;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseHandler;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;

/**
 * @author subinzhu
 * 
 */
public class ContractBrowserDialog extends RBrowserDialog<BContract> {

  private RGridColumnDef billNumberCol = new RGridColumnDef(WidgetRes.M.contract_number(),
      BContract.FIELD_BILLNUMBER);
  private RGridColumnDef businessUnitCol = new RGridColumnDef(getFieldCaption(
      GRes.FIELDNAME_BUSINESS, WidgetRes.M.accountUnit()), BContract.FIELD_ACCOUNTUNIT);
  private RGridColumnDef counterpartCol = new RGridColumnDef(getFieldCaption(
      BContract.FIELD_COUNTERPART, WidgetRes.M.counterpart()), BContract.FIELD_COUNTERPART);
  private RGridColumnDef titleCol = new RGridColumnDef(WidgetRes.M.signboard(),
      BContract.FIELD_TITLE);
  private RGridColumnDef floorCol = new RGridColumnDef(WidgetRes.M.floor(), BContract.FIELD_FLOOR);
  private RGridColumnDef coopModeCol = new RGridColumnDef(WidgetRes.M.coopMode(),
      BContract.FIELD_COOPMODE);
  private RGridColumnDef contractCategoryCol = new RGridColumnDef(WidgetRes.M.contractCategory(),
      BContract.FIELD_CONTRACT_CATEGORY);
  private RGridColumnDef stateCol = new RGridColumnDef(WidgetRes.M.state(), BContract.FIELD_STATE);

  private boolean showAllFields;
  private boolean clearState = true;
  private Map<String, String> captionMap;
  private Map<String, String> counterTypeMap;
  private boolean requiresAccountUnitAndCountpart;
  public static EnumFieldDef state = new EnumFieldDef("state", WidgetRes.M.stateEqual(), true,
      new String[][] {
          {
              BContractState.INEFFECT, BContractState.R.R.ineffect() }, {
              BContractState.EFFECTED, BContractState.R.R.effected() }, {
              BContractState.FINISHED, BContractState.R.R.finished() }, });

  private PageDataProvider provider;

  public ContractBrowserDialog(boolean showAllFields, Map<String, String> captionMap) {
    super(WidgetRes.M.contract_number(), WidgetRes.M.accountUnit(), WidgetRes.M.counterpart(),
        WidgetRes.M.signboard(), WidgetRes.M.floor(), WidgetRes.M.coopMode(), WidgetRes.M
            .contractCategory());
    this.captionMap = captionMap;
    this.showAllFields = showAllFields;
    setColumnDefs(billNumberCol, businessUnitCol, counterpartCol, titleCol, floorCol, coopModeCol,
        contractCategoryCol, stateCol);
    if (showAllFields == false) {
      businessUnitCol.setVisible(false);
      counterpartCol.setVisible(false);
    }
    stateCol.setVisible(false);
    stateCol.setSortable(false);
    setWidth("860px");
    setCaption(WidgetRes.M.seleteData(WidgetRes.M.contract()));
    getGrid().setAllColumnsOverflowEllipsis(true);

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);

    addBeforeCloseHandler(new BeforeCloseHandler<RDialog>() {

      @Override
      public void onBeforeClose(BeforeCloseEvent<RDialog> event) {
        if (getFilterCallback() != null && getGrid() != null) {
          getFilterCallback().onQuery();
        }
      }
    });
  }

  /**
   * 设置状态列是否可见，默认不可见
   * 
   * @param visible
   */
  public void setStateColVisible(boolean visible) {
    stateCol.setVisible(visible);
    getGrid().rebuild();
  }

  /** 设置项目类似于 */
  public void setAccountUnitLike(String accountUnitCode) {
    if (provider != null)
      provider.setAccountUnitLike(accountUnitCode);
  }

  /** 设置商户类似于 */
  public void setCounterpartLike(String ounterpart) {
    if (provider != null)
      provider.setCounterpartLike(ounterpart);
  }

  /** 设置对方单位等于 */
  public void setCounterpartEqual(String counterpartType) {
    if (provider != null)
      provider.setCounterpartType(counterpartType);
  }

  /** 设置结算单位uuid */
  public void setAccountUnitUuid(String accountUnitUuid) {
    if (provider != null)
      provider.setAccountUnitUuid(accountUnitUuid);
  }

  /** 设置对方单位uuid */
  public void setCounterpartUuid(String counterpartUuid) {
    if (provider != null)
      provider.setCounterpartUuid(counterpartUuid);
  }

  /** 设置合同未锁定 */
  public void setUnlocked(boolean unlocked) {
    if (provider != null) {
      provider.setUnlocked(unlocked);
    }
  }

  public void setState(String state) {
    if (provider != null) {
      provider.setState(state);
    }
  }

  public void setClearState(boolean isclear) {
    clearState = isclear;
  }

  public Map<String, String> getCounterTypeMap() {
    return counterTypeMap;
  }

  public void setCounterTypeMap(Map<String, String> counterTypeMap) {
    this.counterTypeMap = counterTypeMap;
  }

  /**
   * 设置项目及对方单位是否必须，为是时未传不返回数据
   * 
   * @param require
   *          默认FALSE true表示一定需要才能返回，false表示可以查询合同及对方单位为空的数据
   */
  public void setRequiresAccountUnitAndCountpart(boolean require) {
    this.requiresAccountUnitAndCountpart = require;
  }

  public String getFieldCaption(String field, String defaultCaption) {
    if (field == null)
      return defaultCaption;
    if (captionMap == null) {
      return defaultCaption;
    } else {
      String caption = captionMap.get(field);
      return caption == null ? defaultCaption : caption;
    }
  }

  private class PageDataProvider implements RPageDataProvider<BContract>, RFilterCallback {

    private RTextBox billNumberField;
    private RTextBox accountUnitField;
    private RCombinedField countpartField;
    private RTextBox counterpartField;
    private RTextBox titleField;
    private RTextBox positionField;
    private RTextBox brandField;
    private RTextBox floorField;
    private RTextBox coopModeField;
    private OptionComboBox contractCategoryField;
    private RComboBox<String> stateField;
    private RComboBox<String> counterpartTypeField;

    private ContractFilter filter = new ContractFilter();

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BContract>> callback) {
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(billNumberCol.getName(), OrderDir.asc);
      }
      filter.getParams().put(Contracts.CONDITION_ACCOUNT_COUNTPART_REQUIRE,
          requiresAccountUnitAndCountpart);

      AccountCpntsService.Locator.getService().queryContracts(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BContract rowData, List<BContract> pageData) {
      switch (col) {
      case 0:
        return rowData.getBillNumber();
      case 1:
        return rowData.getAccountUnit() == null ? null : rowData.getAccountUnit().toFriendlyStr();
      case 2: {
        return rowData.getCounterpart() == null ? null : rowData.getCounterpart().toFriendlyStr(
            counterTypeMap);
      }
      case 3:
        return rowData.getTitle();
      case 4:
        return rowData.getFloor() == null ? null : rowData.getFloor().toFriendlyStr();
      case 5:
        return rowData.getCoopMode();
      case 6:
        return rowData.getContractCategory();
      case 7:
        return rowData.getState();
      default:
        return null;
      }
    }

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("100%");

      billNumberField = new RTextBox(WidgetRes.M.contractNnumberStartWith());
      form.addField(billNumberField);

      accountUnitField = new RTextBox(getFieldCaption(GRes.FIELDNAME_BUSINESS,
          WidgetRes.M.accountUnit())
          + WidgetRes.M.like());
      accountUnitField.setVisible(showAllFields);
      accountUnitField.setValue(getAccountUnitValue());
      form.addField(accountUnitField);

      titleField = new RTextBox(WidgetRes.M.signboardLike());
      form.addField(titleField);

      counterpartField = new RTextBox(getFieldCaption(GRes.FIELDNAME_COUNTERPART,
          WidgetRes.M.counterpart())
          + WidgetRes.M.like());
      counterpartField.setValue(getCounterpartValue());

      counterpartTypeField = new RComboBox<String>();
      counterpartTypeField.setEditable(false);
      counterpartTypeField.setNullOptionText(WidgetRes.M.all());
      counterpartTypeField.setMaxDropdownRowCount(10);
      for (Map.Entry<String, String> entry : counterTypeMap.entrySet()) {
        counterpartTypeField.addOption(entry.getKey(), entry.getValue());
      }
      final String countpartFieldCaption = getFieldCaption(GRes.FIELDNAME_COUNTERPART,
          WidgetRes.M.counterpart())
          + WidgetRes.M.like();
      countpartField = new RCombinedField() {
        {
          setCaption(countpartFieldCaption);
          addField(counterpartTypeField, 0.4f);
          addField(counterpartField, 0.6f);

        }
      };
      countpartField.setVisible(showAllFields);
      form.addField(countpartField);

      positionField = new RTextBox(WidgetRes.M.positionLike());
      form.addField(positionField);

      brandField = new RTextBox(WidgetRes.M.brandLike());
      form.addField(brandField);

      floorField = new RTextBox(WidgetRes.M.floorLike());
      floorField.setValue(getFloorValue());
      form.addField(floorField);

      coopModeField = new RTextBox(WidgetRes.M.coopModeLike());
      form.addField(coopModeField);

      contractCategoryField = new OptionComboBox(AccountCpntsContants.KEY_CONTRACT_CATEGORY,
          WidgetRes.M.contractCategory(), WidgetRes.M.contractCategoryEqual(), false);
      contractCategoryField.clearValue();
      contractCategoryField.setNullOptionText("全部");
      contractCategoryField.setConfigable(false);
      contractCategoryField.setRefreshWhenOpen(true);
      contractCategoryField.setMaxDropdownRowCount(10);
      form.addField(contractCategoryField);

      stateField = new RComboBox<String>(state);
      stateField.setEditable(false);
      stateField.setValue(BContractState.EFFECTED);
      form.addField(stateField);

      form.rebuild();
    }

    private String getAccountUnitValue() {
      if (filter == null)
        return null;
      if (filter.getAccountUnit() != null) {
        accountUnitField.setValue(filter.getAccountUnit());
        return filter.getAccountUnit();
      } else
        return null;
    }

    private String getCounterpartValue() {
      if (filter == null)
        return null;
      if (filter.getCounterpart() != null) {
        counterpartField.setValue(filter.getCounterpart());
        return filter.getCounterpart();
      } else
        return null;
    }

    private String getFloorValue() {
      if (filter == null)
        return null;
      if (filter.getFloor() != null) {
        floorField.setValue(filter.getFloor());
        return filter.getFloor();
      } else
        return null;
    }

    @Override
    public void clearConditions() {
      if (billNumberField != null) {
        billNumberField.clearValue();
      }
      if (accountUnitField != null) {
        accountUnitField.clearValue();
      }
      if (counterpartField != null) {
        counterpartField.clearValue();
      }
      if (titleField != null) {
        titleField.clearValue();
      }
      if (positionField != null) {
        positionField.clearValue();
      }
      if (brandField != null) {
        brandField.clearValue();
      }
      if (floorField != null) {
        floorField.clearValue();
      }
      if (coopModeField != null) {
        coopModeField.clearValue();
      }
      if (contractCategoryField != null) {
        contractCategoryField.clearValue();
      }
      if (counterpartTypeField != null) {
        counterpartTypeField.clearValue();
      }
      if (stateField != null) {
        if (clearState) {
          stateField.setValue(null);
        } else {
          stateField.setValue(BContractState.EFFECTED);
          clearState = true;
        }
      }
      if (filter != null) {
        filter.clear();
      }
    }

    @Override
    public void onQuery() {
      if (billNumberField != null && accountUnitField != null && counterpartField != null
          && titleField != null && positionField != null && brandField != null
          && floorField != null && coopModeField != null && stateField != null
          && counterpartTypeField != null && contractCategoryField != null) {
        filter.setBillNumber(billNumberField.getValue());
        filter.setAccountUnit(accountUnitField.getValue());
        filter.setTitle(titleField.getValue());
        filter.setCounterpart(counterpartField.getValue());
        filter.setPosition(positionField.getValue());
        filter.setBrand(brandField.getValue());
        filter.setFloor(floorField.getValue());
        filter.setCoopMode(coopModeField.getValue());
        filter.setContractCategory(contractCategoryField.getValue());
        filter.setState(stateField.getValue());
        filter.setCouterpartType(counterpartTypeField.getValue());
      }
      filter.setUnlocked(unlocked);
    }

    /** 设置项目类似于 */
    public void setAccountUnitLike(String accountUnit) {
      filter.setAccountUnit(accountUnit);
      if (accountUnitField != null)
        accountUnitField.setValue(accountUnit);
    }

    /** 设置商户类似于 */
    public void setCounterpartLike(String counterpart) {
      filter.setCounterpart(counterpart);
      if (counterpartField != null)
        counterpartField.setValue(counterpart);
    }

    /** 设置商户类似于 */
    public void setCounterpartType(String counterparType) {
      filter.setCouterpartType(counterparType);
      if (counterpartTypeField != null)
        counterpartTypeField.setValue(counterparType);
    }

    /** 设置结算单位uuid */
    public void setAccountUnitUuid(String accountUnitUuid) {
      filter.setAccountUnitUuid(accountUnitUuid);
    }

    /** 设置对方单位uuid */
    public void setCounterpartUuid(String counterpartUuid) {
      filter.setCounterpartUuid(counterpartUuid);
    }

    private boolean unlocked;

    /** 设置未锁定状态条件 */
    public void setUnlocked(boolean unlocked) {
      this.unlocked = unlocked;
      filter.setUnlocked(unlocked);
    }

    private void setState(String state) {
      filter.setState(state);
      if (stateField != null)
        stateField.setValue(state);
    }
  }
}
