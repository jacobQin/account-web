/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	ContractBrowserDialog2.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月11日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui.gadget;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hd123.m3.account.gwt.base.client.BaseWidgetConstants;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContractState;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.ContractFilter;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.TenantUCNBox;
import com.hd123.m3.commons.gwt.base.client.biz.BBasicState;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.widget.client.ui.OptionComboBox;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.BWithUCN;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.widget2.client.dialog.RDialog;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeCloseHandler;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.grid.RPageDataProvider;
import com.hd123.rumba.gwt.widget2e.client.dialog.RBrowserDialog;
import com.hd123.rumba.gwt.widget2e.client.dialog.RFilterCallback;

/**
 * 对应合同浏览框(ContractUCNBox)
 * 
 * @author chenganbang
 */
public class ContractBrowserDialog extends RBrowserDialog<BContract> {

  private RGridColumnDef billNumberCol = new RGridColumnDef(M.contract(),
      BContract.FIELD_BILLNUMBER);
  private RGridColumnDef accountUuitCol = new RGridColumnDef(M.accountUuit(),
      BContract.FIELD_ACCOUNTUNIT);
  private RGridColumnDef counterpartCol = new RGridColumnDef(M.counterpart(),
      BContract.FIELD_COUNTERPART);
  private RGridColumnDef positionCol = new RGridColumnDef(M.position(), BContract.FIELD_POSITION);
  private RGridColumnDef contractCategoryCol = new RGridColumnDef("合同类型",
      BContract.FIELD_CONTRACT_CATEGORY);

  private PageDataProvider provider;

  public ContractBrowserDialog() {
    super(M.contract(), M.accountUuit(), M.counterpart(), M.position());
    setColumnDefs(billNumberCol, accountUuitCol, counterpartCol, positionCol, contractCategoryCol);
    setWidth("860px");

    positionCol.setSortable(false);

    provider = new PageDataProvider();
    setProvider(provider);
    setFilterCallback(provider);
    setFilterStyle(FILTER_STYLE_PANEL);
    setCaption(CommonsMessages.M.seleteData(M.defaultCaption()));

    addBeforeCloseHandler(new BeforeCloseHandler<RDialog>() {

      @Override
      public void onBeforeClose(BeforeCloseEvent<RDialog> event) {
        if (getFilterCallback() != null) {
          getFilterCallback().onQuery();
        }
      }
    });
    getGrid().setAllColumnsOverflowEllipsis(true);
  }

  /**
   * 设置商户，限制合同为该商户签订的合同
   */
  public void setTenantLimit(BWithUCN tenant) {
    if (provider != null) {
      provider.setTenant(tenant);
    }
  }

  /**
   * 设置项目，限制合同为该项目签订的合同
   * 
   */
  public void setStoreLimit(BWithUCN store) {
    if (provider != null) {
      provider.setStore(store);
    }
  }

  private class PageDataProvider implements RPageDataProvider<BContract>, RFilterCallback {

    private RTextBox serialNumberField;
    private AccountUnitUCNBox storeField;
    private TenantUCNBox tenantField;
    private RTextBox signboardField;
    private RComboBox<String> stateField;
    private OptionComboBox contractCategoryField;
    private RTextBox positionField;

    public void setStore(BWithUCN store) {
      this.storeField.setValue(store);
    }

    public void setTenant(BWithUCN tenant) {
      this.tenantField.setValue(tenant);
    }

    private ContractFilter filter = new ContractFilter();

    public PageDataProvider() {
      serialNumberField = new RTextBox(M.billNumberStartWith());

      storeField = new AccountUnitUCNBox();
      storeField.setCaption(M.storeEquals());
      storeField.getBrowser().setCaption(CommonsMessages.M.seleteData(M.accountUuit()));

      tenantField = new TenantUCNBox();
      tenantField.setStates(BBasicState.USING);
      tenantField.setCaption(M.tenantEquals());
      tenantField.getBrowser().setCaption(CommonsMessages.M.seleteData(M.counterpart()));

      signboardField = new RTextBox(M.signBoardLike());

      stateField = new RComboBox<String>(M.stateEquals());
      stateField.setRequired(false);
      stateField.setEditable(false);
      stateField.addOption(BContractState.INEFFECT, BContractState.R.R.ineffect());
      stateField.addOption(BContractState.EFFECTED, BContractState.R.R.effected());
      stateField.addOption(BContractState.FINISHED, BContractState.R.R.finished());
      stateField.setNullOptionText(M.stateAll());
      stateField.setValue(BContractState.EFFECTED);

      contractCategoryField = new OptionComboBox(AccountCpntsContants.KEY_CONTRACT_CATEGORY,
          WidgetRes.M.contractCategory(), WidgetRes.M.contractCategoryEqual(), false);
      contractCategoryField.clearValue();
      contractCategoryField.setNullOptionText("全部");
      contractCategoryField.setConfigable(false);
      contractCategoryField.setRefreshWhenOpen(true);
      contractCategoryField.setMaxDropdownRowCount(10);

      positionField = new RTextBox("铺位 类似于");

    }

    @Override
    public void fetchData(int page, int pageSize, String sortField, OrderDir sortDir,
        AsyncCallback<RPageData<BContract>> callback) {
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.clearOrders();
      if (sortField != null) {
        filter.appendOrder(sortField, sortDir);
      } else {
        filter.appendOrder(BaseWidgetConstants.ORDER_BY_FIELD_CODE);
      }
      filter.setCouterpartType(BCounterpart.COUNPERPART_TENANT);
      AccountCpntsService.Locator.getService().queryContracts(filter, callback);
    }

    @Override
    public Object getData(int row, int col, BContract rowData, List<BContract> pageData) {
      switch (col) {
      case 0:
        return rowData.getTitle() == null ? "-[" + rowData.getCode() + "]" : rowData.getTitle()
            + "[" + rowData.getCode() + "]";
      case 1:
        return rowData.getAccountUnit() == null ? null : rowData.getAccountUnit().toFriendlyStr();
      case 2:
        return rowData.getCounterpart() == null ? null : rowData.getCounterpart().toFriendlyStr();
      case 3: {
        if (rowData.getPositions() == null || rowData.getPositions().isEmpty()) {
          return null;
        }
        StringBuffer sb = new StringBuffer();
        for (BUCN p : rowData.getPositions()) {
          if (sb.length() > 0) {
            sb.append(",");
          }
          sb.append(p.getName()).append("[").append(p.getCode()).append("]");
        }
        return sb.toString();
      }
      case 4:
        return rowData.getContractCategory();
      default:
        return null;
      }
    }

    @Override
    public void buildConditions(RForm form) {

      form.addField(serialNumberField);
      form.addField(storeField);
      form.addField(tenantField);
      form.addField(signboardField);
      form.addField(stateField);
      form.addField(contractCategoryField);
      form.addField(positionField);

      form.rebuild();
    }

    @Override
    public void clearConditions() {
      clearCondition();
    }

    @Override
    public void onQuery() {
      if (serialNumberField != null) {
        filter.setBillNumber(serialNumberField.getValue());
      }
      if (tenantField != null) {
        filter.setCounterpartUuid(tenantField.getValue().getUuid());
      }
      if (storeField != null) {
        filter.setAccountUnitUuid(storeField.getValue().getUuid());
      }
      if (signboardField != null) {
        filter.setTitle(signboardField.getValue());
      }
      if (stateField != null) {
        filter.setState(stateField.getValue());
      }
      if (contractCategoryField != null) {
        filter.setContractCategory(contractCategoryField.getValue());
      }
      if (positionField != null) {
        filter.setPosition(positionField.getValue());
      }
    }
  }

  private void clearCondition() {
    if (provider != null) {
      if (provider.serialNumberField != null) {
        provider.serialNumberField.clearValue();
      }
      if (provider.tenantField != null) {
        provider.tenantField.clearValue();
      }
      if (provider.storeField != null) {
        provider.storeField.clearValue();
      }
      if (provider.signboardField != null) {
        provider.signboardField.clearValue();
      }
      if (provider.stateField != null) {
        provider.stateField.clearValue();
      }
      if (provider.positionField != null) {
        provider.positionField.clearValue();
      }
      if (provider.contractCategoryField != null) {
        provider.contractCategoryField.clearValue();
      }
    }
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("合同")
    String defaultCaption();

    @DefaultMessage("合同编号 起始于")
    String billNumberStartWith();

    @DefaultMessage("合同")
    String contract();

    @DefaultMessage("项目 等于")
    String storeEquals();

    @DefaultMessage("项目")
    String accountUuit();

    @DefaultMessage("商户 等于")
    String tenantEquals();

    @DefaultMessage("商户 ")
    String counterpart();

    @DefaultMessage("店招 类似于")
    String signBoardLike();

    @DefaultMessage("铺位")
    String position();

    @DefaultMessage("状态")
    String state();

    @DefaultMessage("状态 等于")
    String stateEquals();

    @DefaultMessage("未生效")
    String stateIneffect();

    @DefaultMessage("已生效")
    String stateEffect();

    @DefaultMessage("已终止")
    String stateStopped();

    @DefaultMessage("全部")
    String stateAll();
  }
}
