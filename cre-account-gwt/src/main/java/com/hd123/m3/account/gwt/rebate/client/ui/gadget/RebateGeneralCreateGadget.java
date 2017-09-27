/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateGeneralCreateGadget.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月8日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui.gadget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.ContractFilter;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BContract;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesBill;
import com.hd123.m3.account.gwt.rebate.client.rpc.RebateBillService;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.sales.RebateLineFilter;
import com.hd123.m3.account.gwt.rebate.intf.client.dd.PRebateBillDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBasicState;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.form.RViewStringField;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 销售额返款单新建页面的概要面板
 * 
 * @author chenganbang
 */
public class RebateGeneralCreateGadget extends RCaptionBox implements HasRActionHandlers,
    RActionHandler {
  private EPRebateBill ep = EPRebateBill.getInstance();
  private BRebateBill entity;

  private FieldChangeHandler fieldHandler = new FieldChangeHandler();

  private AccountUnitUCNBox storeField;
  private CounterpartUCNBox tenantField;
  private ContractUCNBox contractField;
  private RViewStringField positionField;
  private RDateBox accountDateField;
  private RDateBox beginDateField;
  private RDateBox endDateField;
  private PermGroupEditField permGroupField;

  public RebateGeneralCreateGadget() {
    super();
    setCaption(CommonsMessages.M.generalInfo());
    setWidth("100%");
    setEditing(true);
    getCaptionBar().setShowCollapse(true);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setSpacing(5);
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");

    mvp.add(0, drawBaseInfo());

    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    mvp.add(1, permGroupField);
    setContent(mvp);
  }

  public void setValue(BRebateBill entity) {
    this.entity = entity;
    refresh();
  }

  private void refresh() {
    storeField.setValue(entity.getStore());
    tenantField
        .setValue(new BCounterpart(entity.getTenant(), BCounterpart.COUNTERPART_TYPE_TENANT));
    contractField.setValue(entity.getContract());
    positionField.setValue(entity.getPositions());
    accountDateField.setValue(entity.getAccountDate());
    beginDateField.setValue(entity.getBeginDate());
    endDateField.setValue(entity.getEndDate());
    permGroupField.setPerm(entity);
  }

  private Widget drawBaseInfo() {
    RForm form = new RForm(1);
    form.setWidth("100%");
    
    storeField = new AccountUnitUCNBox();
    storeField.setCaption(EPRebateBill.getInstance().getFieldCaption(GRes.FIELDNAME_BUSINESS,
        GRes.R.business()));
    storeField.setRequired(true);
    storeField.addChangeHandler(fieldHandler);
    form.addField(storeField);
    
    tenantField = new CounterpartUCNBox();
    tenantField.setCounterpartTypes(Arrays.asList(BCounterpart.COUNTERPART_TYPE_TENANT));
    tenantField.setRequired(true);
    tenantField.addChangeHandler(fieldHandler);
    tenantField.showStateField(false);// 隐藏商户的状态搜索条件
    tenantField.setStateCondition(BBasicState.USING);// 添加使用中的搜索条件
    List<String> states = new ArrayList<String>();
    states.add(BBasicState.USING);
    tenantField.getFilter().put(BCounterpart.KEY_FILTER_STATE, states);
    form.addField(tenantField);
    
    contractField = new ContractUCNBox();
    contractField.setRequired(true);
    contractField.setValidator(new RValidator() {
      @Override
      public Message validate(Widget sender, String value) {
        if (entity.getContract() == null) {
          return Message.error("合同不能为空。", contractField);
        }
        if (entity.getContract().isRebateBack() == false) {
          return Message.error("指定的合同不存在销售额返款条款。", contractField);
        }
        return null;
      }
    });
    contractField.getBrowser().addOpenHandler(new OpenHandler<PopupPanel>() {
      @Override
      public void onOpen(OpenEvent<PopupPanel> event) {
        contractField.setStoreLimit(entity == null ? null : entity.getStore());
        contractField.setTenantLimit(entity == null ? null : entity.getTenant());
      }
    });
    contractField.addChangeHandler(fieldHandler);
    form.addField(contractField);

    positionField = new RViewStringField(PRebateBillDef.positions);
    form.addField(positionField);

    accountDateField = new RDateBox();
    accountDateField.setRequired(true);
    accountDateField.setCaption(PRebateBillDef.constants.accountDate());
    accountDateField.addChangeHandler(fieldHandler);
    form.addField(accountDateField);

    beginDateField = new RDateBox();
    beginDateField.setName(PRebateBillDef.constants.beginDate());
    beginDateField.setRequired(true);
    beginDateField.setCaption(PRebateBillDef.constants.beginDate());
    beginDateField.addChangeHandler(fieldHandler);
    beginDateField.setValidator(new BeginDateValidator());
    form.addField(beginDateField);

    endDateField = new RDateBox();
    endDateField.setRequired(true);
    endDateField.setName(PRebateBillDef.constants.endDate());
    endDateField.setCaption(PRebateBillDef.constants.endDate());
    endDateField.addChangeHandler(fieldHandler);
    endDateField.setValidator(new EndDateValidator());
    form.addField(endDateField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(CommonsMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  private class BeginDateValidator implements RValidator {

    @Override
    public Message validate(Widget sender, String value) {
      if (beginDateField.getValue() == null) {
        return null;
      }
      BContract contract = entity.getContract();
      if (contract == null)
        return null;
      // 起始日期不能小于合同起始日期
      if (contract.getBeginDate() != null
          && contract.getBeginDate().after(beginDateField.getValue())) {
        return Message.error(
            M.beginIsBeforeContractBeginDate(M3Format.fmt_yMd.format(RDateUtil.truncate(
                contract.getBeginDate(), RDateUtil.DAY_OF_MONTH))), beginDateField);
      }
      // 起始日期必须大于最近出账的截止日期
      if (contract.getLastestAccountDate() != null
          && beginDateField.getValue().before(contract.getLastestAccountDate())) {
        return Message.error(
            M.beginDateIsLess(M3Format.fmt_yMd.format(RDateUtil.truncate(
                contract.getLastestAccountDate(), RDateUtil.DAY_OF_MONTH))), beginDateField);
      }
      return null;
    }
  }

  private class EndDateValidator implements RValidator {

    @Override
    public Message validate(Widget sender, String value) {
      if (endDateField.getValue() == null) {
        return null;
      }

      // 截止时间不能超过当前时间(after不包含等于)
      if (endDateField.getValue().after(new Date())) {
        return Message.error(M.forbidAfterCurretnDate(endDateField.getCaption()), endDateField);
      }

      BContract contract = entity.getContract();
      if (contract == null)
        return null;

      // 截止日期不能大于合同截止日期
      if (contract.getEndDate() != null && endDateField.getValue().after(contract.getEndDate())) {
        return Message.error(
            M.endDateIsAfterContractEndDate(M3Format.fmt_yMd.format(RDateUtil.truncate(
                contract.getEndDate(), RDateUtil.DAY_OF_MONTH))), endDateField);
      }

      if (beginDateField.getValue() != null) {
        // 起始日期不能大于截止日期
        if (beginDateField.getValue().after(endDateField.getValue())) {
          return Message.error(M.beginDateIsAfterEndDate(), endDateField);
        }

        BDateRange period = new BDateRange(beginDateField.getValue(), endDateField.getValue());
        // 起止日期必须落在同一个账期内
        if (contract != null && contract.getRanges() != null && contract.getRanges().size() != 0) {
          List<BDateRange> ranges = contract.getRanges();
          for (BDateRange range : ranges) {
            if (range.overlap(period) != null && range.include(period) == false)
              return Message.error(M.betweenOnePaymentDays(), endDateField);
          }
        }
      }
      return null;
    }

  }

  private class FieldChangeHandler implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      Object source = event.getSource();
      if (source == storeField) {
        entity.setStore(storeField.getValue());
        RActionEvent.fire(RebateGeneralCreateGadget.this, RebateLineGadget.ACTION_REFRESH_ACTIONS);
        fetchContract();
      } else if (source == tenantField) {
        entity.setTenant(tenantField.getValue());
        fetchContract();
      } else if (source == contractField) {
        loadContractDoc(contractField.getValue().getUuid());
      } else if (source == accountDateField) {
        entity.setAccountDate(accountDateField.getValue());
      } else if (source == beginDateField) {
        entity.setBeginDate(beginDateField.getValue());
        if (beginDateField.validate() && endDateField.getValue() != null) {
          endDateField.validate();
        }
        Boolean isReadyToLoad = beginDateField.isValid() && endDateField.isValid();
        RActionEvent.fire(RebateGeneralCreateGadget.this,
            RebateLineGadget.ACTION_READY_TO_LOAD_PRO, isReadyToLoad);
        queryProductRpts();
      } else if (source == endDateField) {
        entity.setEndDate(endDateField.getValue());
        if (endDateField.validate() && beginDateField.getValue() != null) {
          beginDateField.validate();
        }
        Boolean isReadyToLoad = beginDateField.isValid() && endDateField.isValid();
        RActionEvent.fire(RebateGeneralCreateGadget.this,
            RebateLineGadget.ACTION_READY_TO_LOAD_PRO, isReadyToLoad);
        queryProductRpts();
      }
    }

    public void fetchContract() {
      if (entity.getStore() == null || entity.getStore().getUuid() == null
          || entity.getTenant() == null || entity.getTenant().getUuid() == null) {
        return;
      }
      RLoadingDialog.show();

      ContractFilter filter = new ContractFilter();
      filter.setAccountUnitUuid(entity.getStore().getUuid());
      filter.setCounterpartUuid(entity.getTenant().getUuid());

      GWTUtil.enableSynchronousRPC();
      AccountCpntsService.Locator.getService().queryContracts(filter,
          new RBAsyncCallback2<RPageData<com.hd123.m3.account.gwt.cpnts.client.biz.BContract>>() {

            @Override
            public void onException(Throwable caught) {
              RLoadingDialog.hide();
              RMsgBox.showError(caught);
            }

            @Override
            public void onSuccess(
                RPageData<com.hd123.m3.account.gwt.cpnts.client.biz.BContract> result) {
              RLoadingDialog.hide();
              if (result != null && result.getValues().size() == 1) {// 只返回一条结果，则设置合同的相关信息
                loadContractDoc(result.getValues().get(0).getUuid());
              } else {// 返回0条
                contractField.clearValue();
                contractField.clearValidResults();
                positionField.setValue(null);

                entity.setContract(null);
                entity.setPositions(null);
                entity.getLines().clear();
                RActionEvent.fire(RebateGeneralCreateGadget.this,
                    RebateLineGadget.ACTION_REFRESH_LINES);
              }
            }

          });
    }
  }

  private void loadContractDoc(String contractId) {
    if (contractId == null) {
      positionField.setValue(null);
      entity.setPositions(null);
      entity.getLines().clear();
      RActionEvent.fire(RebateGeneralCreateGadget.this, RebateLineGadget.ACTION_REFRESH_LINES);
      return;
    }

    RLoadingDialog.show();
    RebateBillService.Locator.getService().getContractDoc(contractId,
        new RBAsyncCallback2<BContract>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            RMsgBox.showError(caught);
          }

          @Override
          public void onSuccess(BContract contract) {
            RLoadingDialog.hide();
            entity.setContract(contract);
            entity.setStore(contract.getAccountUnit());
            entity.setTenant(contract.getCounterpart());
            entity.setPositions(contract.getPositions());

            entity.setRebateInvoice(contract.isRebateInvoice());
            entity.setPoundageInvoice(contract.isPoundageInvoice());
            entity.setRebateSubject(contract.getRebateSubject());
            entity.setPoundageSubject(contract.getPoundageSubject());
            entity.setPoundageTaxRate(contract.getPoundageTaxRate());
            entity.setBackTaxRate(contract.getBackTaxRate());
            // 设置未开的最小销售额开始时间
            entity.setBeginDate(contract.getRebateBeginDate());
            entity.setEndDate(null);
            
            entity.getLines().clear();
            
            contractField.validate();

            if (contract.isRebateBack() == false) {
              contractField.addErrorMessage("指定的合同不存在销售额返款条款。");
              return;
            }

            refresh();
            clearValidResults();
            queryProductRpts();
            RActionEvent
                .fire(RebateGeneralCreateGadget.this, RebateLineGadget.ACTION_REFRESH_LINES);
          }
        });
  }

  /**
   * 加载销售数据。
   * <p>
   * 1、若不包含销售额返款条款且不包含银行手续费扣率条款，则不需要加载销售数据；否则通过计算器计算销售额返款和手续费。
   */
  private void queryProductRpts() {
    // 验证是否选择合同
    if (entity == null || entity.getContract() == null) {
      return;
    }
    BContract contract = entity.getContract();
    // 首先验证起始日期和截止日期是否符合规范
    if (beginDateField.getValue() == null || endDateField.getValue() == null
        || !beginDateField.isValid() || !endDateField.isValid()) {
      return;
    }
    // 是否包含银行手续费条款和销售额返款条款
    if (!contract.isBankRate() && !contract.isRebateBack()) {
      entity.getLines().clear();// 销售数据为空

      RActionEvent.fire(RebateGeneralCreateGadget.this, RebateLineGadget.ACTION_REFRESH_LINES);
      return;
    }

    RebateLineFilter filter = new RebateLineFilter();
    filter.setContractUuid(contract.getUuid());
    filter.setBeginDate(beginDateField.getValue());
    filter.setEndDate(endDateField.getValue());
    filter.setBankRate(contract.isBankRate());
    filter.setShouldBack(contract.isRebateBack());
    filter.setPosMode(contract.getPosMode());

    RebateBillService.Locator.getService().queryProductRpts(filter,
        new AsyncCallback<RPageData<BSalesBill>>() {

          @Override
          public void onSuccess(RPageData<BSalesBill> result) {
            List<BSalesBill> bills = result.getValues();
            entity.getLines().clear();
            for (BSalesBill bill : bills) {
              entity.getLines().addAll(bill.getLines());
            }
            RActionEvent
                .fire(RebateGeneralCreateGadget.this, RebateLineGadget.ACTION_REFRESH_LINES);
          }

          @Override
          public void onFailure(Throwable caught) {
            RMsgBox.showError(caught);
          }
        });
  }

  public void clearConditions() {
    storeField.getBrowser().clearConditions();
    contractField.getBrowser().clearConditions();
    tenantField.getDialog().clearConditions();
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
  }

  public static M M = GWT.create(M.class);

  public static interface M extends Messages {
    @DefaultMessage("{0}：不能大于当前日期。")
    String forbidAfterCurretnDate(String caption);

    @DefaultMessage("起始日期不能大于截止日期")
    String beginDateIsAfterEndDate();

    @DefaultMessage("起始日期必须大于等于最近出账的截止日期：{0}")
    String beginDateIsLess(String dateStr);

    @DefaultMessage("起止日期必须落在同一个账期内")
    String betweenOnePaymentDays();

    @DefaultMessage("{0}：不能小于当前日期")
    String accountDateBeforeCurDate(String caption);

    @DefaultMessage("起始日期不能小于合同起始日期：{0}")
    String beginIsBeforeContractBeginDate(String dateStr);

    @DefaultMessage("截止日期不能大于合同截止日期：{0}")
    String endDateIsAfterContractEndDate(String dateStr);

    @DefaultMessage("请输入正确的合同编号")
    String noResult();
  }
}
