/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AdvcaneContractBrowserDialog.java
 * 模块说明：	
 * 修改历史：
 *  2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.client.ui.dialog;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContractState;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BAdvanceContract;
import com.hd123.m3.account.gwt.depositmove.commons.client.rpc.DepositMoveService;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.filter.AdvanceContractFilter;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.fielddef.EnumFieldDef;
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
 * @author zhuhairui
 * 
 */
public class AdvanceContractBrowserDialog extends RBrowserDialog<BAdvanceContract> {

  private RGridColumnDef billNumberCol = new RGridColumnDef(WidgetRes.M.contract_number(),
      BAdvanceContract.FIELD_BILLNUMBER);
  private RGridColumnDef businessUnitCol = new RGridColumnDef(getFieldCaption(
      GRes.FIELDNAME_BUSINESS, WidgetRes.M.accountUnit()), BContract.FIELD_ACCOUNTUNIT);
  private RGridColumnDef counterpartCol = new RGridColumnDef(getFieldCaption(
      BAdvanceContract.FIELD_COUNTERPART, WidgetRes.M.counterpart()),
      BAdvanceContract.FIELD_COUNTERPART);
  private RGridColumnDef titleCol = new RGridColumnDef(WidgetRes.M.signboard(),
      BContract.FIELD_TITLE);
  private RGridColumnDef floorCol = new RGridColumnDef(WidgetRes.M.floor(), BContract.FIELD_FLOOR);

  public static EnumFieldDef state = new EnumFieldDef("state", WidgetRes.M.state(), true,
      new String[][] {
          {
              BContractState.INEFFECT, BContractState.R.R.ineffect() }, {
              BContractState.EFFECTED, BContractState.R.R.effected() }, {
              BContractState.FINISHED, BContractState.R.R.finished() }, });

  private PageDataProvider provider;
  private boolean isShowAccountUnit;
  private boolean isShowState;
  private Map<String, String> captionMap;
  private boolean isCounterpartTypeEnable = true;
  private Map<String, String> counterTypeMap;
  private String counterpartType;

  public AdvanceContractBrowserDialog(boolean isShowAccountUnit, boolean isShowState,
      boolean isCounterpartTypeEnable, Map<String, String> captionMap) {
    super(WidgetRes.M.contract_number(), WidgetRes.M.counterpart());
    this.captionMap = captionMap;
    this.isShowAccountUnit = isShowAccountUnit;
    this.isShowState = isShowState;
    this.isCounterpartTypeEnable = isCounterpartTypeEnable;
    if (isShowAccountUnit)
      setColumnDefs(billNumberCol, businessUnitCol, titleCol, counterpartCol);
    else
      setColumnDefs(billNumberCol, counterpartCol, titleCol, floorCol);

    setWidth("760px");

    setCaption(WidgetRes.M.seleteData(WidgetRes.M.contract()));

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
  }

  /** 设置对方单位等于 */
  public void setCounterpartEqual(String counterpartType) {
    this.counterpartType = counterpartType;
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

  /** 设置项目uuid */
  public void setAccountUnitUuid(String accountUnitUuid) {
    if (provider != null)
      provider.setAccountUnitUuid(accountUnitUuid);
  }

  /** 设置收付方向 */
  public void setDirection(int direction) {
    if (provider != null)
      provider.setDirection(direction);
  }

  /** 设置是否从预存款账户中查询合同 */
  public void setQueryAdvance(boolean isQueryAdvance) {
    if (provider != null) {
      provider.setQueryAdvance(isQueryAdvance);
    }
  }

  public void setState(String state) {
    if (provider != null) {
      provider.setState(state);
    }
  }

  public Map<String, String> getCounterTypeMap() {
    return counterTypeMap;
  }

  public void setCounterTypeMap(Map<String, String> counterTypeMap) {
    this.counterTypeMap = counterTypeMap;
  }

  private String getFieldCaption(String field, String defaultCaption) {
    if (field == null)
      return defaultCaption;
    if (captionMap == null) {
      return defaultCaption;
    } else {
      String caption = captionMap.get(field);
      return caption == null ? defaultCaption : caption;
    }
  }

  private class PageDataProvider implements RPageDataProvider<BAdvanceContract>, RFilterCallback {

    private RTextBox billNumberField;
    private RTextBox accountUnitField;
    private RCombinedField countpartField;
    private RTextBox counterpartField;
    private RTextBox titleField;
    private RTextBox positionField;
    private RTextBox brandField;
    private RTextBox floorField;
    private RComboBox<String> stateField;
    private RComboBox<String> counterpartTypeField;

    AdvanceContractFilter filter = new AdvanceContractFilter();

    @Override
    public void buildConditions(RForm form) {
      form.setWidth("100%");

      billNumberField = new RTextBox(WidgetRes.M.contractNnumberStartWith());
      form.addField(billNumberField);

      accountUnitField = new RTextBox(getFieldCaption(GRes.FIELDNAME_BUSINESS,
          WidgetRes.M.accountUnit())
          + WidgetRes.M.like());
      accountUnitField.setVisible(isShowAccountUnit);
      accountUnitField.setValue(getAccountUnitValue());
      form.addField(accountUnitField);

      counterpartField = new RTextBox();
      counterpartField.setValue(getCounterpartValue());

      counterpartTypeField = new RComboBox<String>();
      counterpartTypeField.setEditable(false);
      counterpartTypeField.setEnabled(isCounterpartTypeEnable);
      counterpartTypeField.setNullOptionText(WidgetRes.M.all());
      counterpartTypeField.setMaxDropdownRowCount(10);
      for (Map.Entry<String, String> entry : counterTypeMap.entrySet()) {
        counterpartTypeField.addOption(entry.getKey(), entry.getValue());
      }
      counterpartTypeField.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          counterpartType = counterpartTypeField.getValue();
        }
      });

      final String counterpartFieldCaption = getFieldCaption(GRes.FIELDNAME_COUNTERPART,
          WidgetRes.M.counterpart()) + WidgetRes.M.like();
      countpartField = new RCombinedField() {
        {
          setCaption(counterpartFieldCaption);
          addField(counterpartTypeField, 0.4f);
          addField(counterpartField, 0.6f);
        }
      };
      form.addField(countpartField);

      titleField = new RTextBox(WidgetRes.M.signboardLike());
      form.addField(titleField);

      positionField = new RTextBox(WidgetRes.M.positionLike());
      positionField.setVisible(!isShowAccountUnit);
      form.addField(positionField);

      brandField = new RTextBox(WidgetRes.M.brandLike());
      brandField.setVisible(!isShowAccountUnit);
      form.addField(brandField);

      floorField = new RTextBox(WidgetRes.M.floorLike());
      floorField.setVisible(!isShowAccountUnit);
      floorField.setValue(getFloorValue());
      form.addField(floorField);

      stateField = new RComboBox<String>(state);
      stateField.setCaption(state.getCaption() + WidgetRes.M.equal());
      stateField.setVisible(isShowState);
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
      if (billNumberField != null)
        billNumberField.clearValue();
      if (accountUnitField != null)
        accountUnitField.clearValue();
      if (counterpartField != null)
        counterpartField.clearValue();
      if (titleField != null)
        titleField.clearValue();
      if (positionField != null)
        positionField.clearValue();
      if (brandField != null)
        brandField.clearValue();
      if (floorField != null)
        floorField.clearValue();
      if (stateField != null)
        stateField.clearValue();
      if (isCounterpartTypeEnable && counterpartTypeField != null) {
        counterpartTypeField.clearValue();
      }
      if (filter != null)
        filter.clear();
      if (isCounterpartTypeEnable) {
        counterpartType = null;
      }
    }

    @Override
    public void onQuery() {
      setCounterpartType(counterpartType);
      if (billNumberField != null && counterpartField != null && counterpartTypeField != null) {
        filter.setBillNumber(billNumberField.getValue());
        filter.setAccountUnit(accountUnitField.getValue());
        filter.setCounterpart(counterpartField.getValue());
        filter.setTitle(titleField.getValue());
        filter.setPosition(positionField.getValue());
        filter.setBrand(brandField.getValue());
        filter.setFloor(floorField.getValue());
        filter.setState(stateField.getValue());
        filter.setCounterpartType(counterpartTypeField.getValue());
      }
    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BAdvanceContract>> callback) {
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(billNumberCol.getName(), OrderDir.asc);
      }
      DepositMoveService.Locator.getService().queryContract(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BAdvanceContract rowData,
        List<BAdvanceContract> pageData) {
      if (isShowAccountUnit) {
        switch (col) {
        case 0:
          return rowData.getContract() == null ? null : rowData.getContract().getCode();
        case 1:
          return rowData.getAccountUnit() == null ? null : rowData.getAccountUnit().toFriendlyStr();
        case 2:
          return rowData.getContract() == null ? null : rowData.getContract().getName();
        case 3:
          return rowData.getCounterpart() == null ? null : rowData.getCounterpart().toFriendlyStr(
              counterTypeMap);
        default:
          return null;
        }
      } else {
        switch (col) {
        case 0:
          return rowData.getContract() == null ? null : rowData.getContract().getCode();
        case 1:
          return rowData.getCounterpart() == null ? null : rowData.getCounterpart().toFriendlyStr(
              counterTypeMap);
        case 2:
          return rowData.getTitle();
        case 3:
          return rowData.getFloor() == null ? null : rowData.getFloor().toFriendlyStr();
        default:
          return null;
        }
      }
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

    /** 设置商户类型 */
    public void setCounterpartType(String counterparType) {
      filter.setCounterpartType(counterparType);
      if (counterpartTypeField != null)
        counterpartTypeField.setValue(counterparType);
    }

    /** 设置项目uuid */
    public void setAccountUnitUuid(String accountUnitUuid) {
      filter.setAccountUnitUuid(accountUnitUuid);
    }

    /** 设置收付方向。 */
    public void setDirection(int direction) {
      filter.setDirectionType(direction);
    }

    /** 设置是否从预存款账户中查询合同。 */
    public void setQueryAdvance(boolean isQueryAdvance) {
      filter.setQueryAdvance(isQueryAdvance);
    }

    public void setState(String state) {
      filter.setState(state);
      if (stateField != null)
        stateField.setValue(state);
    }

  }
}
