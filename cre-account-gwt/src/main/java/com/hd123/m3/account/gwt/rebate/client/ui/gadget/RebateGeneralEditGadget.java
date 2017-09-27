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

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.ContractLinkField;
import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BContract;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesBill;
import com.hd123.m3.account.gwt.rebate.client.rpc.RebateBillService;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.sales.RebateLineFilter;
import com.hd123.m3.account.gwt.rebate.intf.client.dd.PRebateBillDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.widget.client.ui.perm.PermGroupEditField;
import com.hd123.rumba.gwt.util.client.RDateUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
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
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 销售额返款单新建页面的概要面板
 * 
 * @author chenganbang
 */
public class RebateGeneralEditGadget extends RCaptionBox implements HasRActionHandlers,
    RActionHandler {
  private BRebateBill entity;
  private EPRebateBill ep = EPRebateBill.getInstance();
  private FieldChangeHandler handler = new FieldChangeHandler();

  private RViewStringField codeField;// 单号
  private RViewStringField storeField;// 项目
  private RViewStringField counterpartField;// 商户
  private ContractLinkField contractField;// 合同
  private RViewStringField positionField;// 铺位编号
  private RDateBox accountDateField;// 记账日期
  private RDateBox beginDateField;// 起始日期
  private RDateBox endDateField;// 截止日期

  private RebateOperateGadget operateGadget;// 状态与操作
  private PermGroupEditField permGroupField;// 授权组

  public RebateGeneralEditGadget() {
    super();
    setCaption(CommonsMessages.M.generalInfo());
    setWidth("100%");
    setEditing(true);
    getCaptionBar().setShowCollapse(true);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");

    mvp.add(0, drawLeftInfo());
    mvp.add(1, drawRightInfo());

    setContent(mvp);
  }

  /**
   * 状态与操作+授权
   * 
   * @return
   */
  private Widget drawRightInfo() {
    VerticalPanel root = new VerticalPanel();
    root.setWidth("100%");
    root.setSpacing(8);

    operateGadget = new RebateOperateGadget(false);
    root.add(operateGadget);

    permGroupField = new PermGroupEditField(ep.isPermEnabled(), ep.getUserGroupList());
    permGroupField.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    root.add(permGroupField);

    return root;
  }

  /**
   * 基本信息
   * 
   * @return
   */
  private Widget drawLeftInfo() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    codeField = new RViewStringField(PRebateBillDef.constants.billNumber());
    codeField.addTextStyleName(RTextStyles.STYLE_BOLD);
    form.addField(codeField);

    storeField = new RViewStringField(PRebateBillDef.constants.store());
    form.addField(storeField);

    counterpartField = new RViewStringField(PRebateBillDef.constants.tenant());
    form.addField(counterpartField);

    contractField = new ContractLinkField();
    form.addField(contractField);

    positionField = new RViewStringField(PRebateBillDef.constants.positions());
    form.addField(positionField);

    accountDateField = new RDateBox();
    accountDateField.setRequired(true);
    accountDateField.setCaption(PRebateBillDef.constants.accountDate());
    accountDateField.addChangeHandler(handler);

    form.addField(accountDateField);

    beginDateField = new RDateBox();
    beginDateField.setRequired(true);
    beginDateField.setCaption(PRebateBillDef.constants.beginDate());
    beginDateField.addChangeHandler(handler);
    beginDateField.setValidator(new BeginDateValidator());
    form.addField(beginDateField);

    endDateField = new RDateBox();
    endDateField.setRequired(true);
    endDateField.setCaption(PRebateBillDef.constants.endDate());
    endDateField.addChangeHandler(handler);
    endDateField.setValidator(new EndDateValidator());
    form.addField(endDateField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(CommonsMessages.M.basicInfo());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  public void setValue(BRebateBill entity) {
    this.entity = entity;
    if (entity != null) {
      codeField.setValue(entity.getBillNumber());
      storeField.setValue(entity.getStore().toFriendlyStr());
      counterpartField.setValue(entity.getTenant().toFriendlyStr());
      contractField.setRawValue(entity.getContract());
      positionField.setValue(entity.getPositions());
      accountDateField.setValue(entity.getAccountDate());
      beginDateField.setValue(entity.getBeginDate());
      endDateField.setValue(entity.getEndDate());
      permGroupField.setPerm(entity);

      operateGadget.setValue(entity);

      RebateBillService.Locator.getService().getContractDoc(entity.getContract().getCode(),
          new ContractDocCallback());
    }
  }

  private class ContractDocCallback extends RBAsyncCallback2<BContract> {

    @Override
    public void onException(Throwable caught) {
      RMsgBox.showError(caught);
    }

    @Override
    public void onSuccess(BContract result) {
      entity.setContract(result);
      entity.setRebateInvoice(result.isRebateInvoice());
      entity.setPoundageInvoice(result.isPoundageInvoice());
      entity.setRebateSubject(result.getRebateSubject());
      entity.setPoundageSubject(result.getPoundageSubject());
      entity.setPoundageTaxRate(entity.getContract().getPoundageTaxRate());
      entity.setBackTaxRate(entity.getContract().getBackTaxRate());

      RActionEvent.fire(RebateGeneralEditGadget.this, RebateLineGadget.ACTION_READY_TO_LOAD_PRO,
          beginDateField.isValid() && endDateField.isValid());
    }
  }

  /**
   * 起始日期验证器
   */
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

  /**
   * 结束时间验证器
   */
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
      if (source == accountDateField) {
        entity.setAccountDate(accountDateField.getValue());
      } else if (source == beginDateField) {
        entity.setBeginDate(beginDateField.getValue());
        if (beginDateField.validate() && endDateField.getValue() != null) {
          endDateField.validate();
        }
        Boolean isReadyToLoad = beginDateField.isValid() && endDateField.isValid();
        RActionEvent.fire(RebateGeneralEditGadget.this, RebateLineGadget.ACTION_READY_TO_LOAD_PRO,
            isReadyToLoad);
        queryProductRps();
      } else if (source == endDateField) {
        entity.setEndDate(endDateField.getValue());
        if (endDateField.validate() && beginDateField.getValue() != null) {
          beginDateField.validate();
        }
        Boolean isReadyToLoad = beginDateField.isValid() && endDateField.isValid();
        RActionEvent.fire(RebateGeneralEditGadget.this, RebateLineGadget.ACTION_READY_TO_LOAD_PRO,
            isReadyToLoad);
        queryProductRps();
      }
    }

  }

  /**
   * 获取返款明细
   */
  public void queryProductRps() {
    // 验证是否选择合同
    if (entity.getContract() == null) {
      return;
    }
    // 首先验证起始时间和截止时间是否符合规范
    if (!beginDateField.isValid() || !endDateField.isValid()) {
      return;
    }

    RebateLineFilter filter = new RebateLineFilter();
    filter.setContractUuid(entity.getContract().getUuid());
    filter.setBeginDate(beginDateField.getValue());
    filter.setEndDate(endDateField.getValue());
    filter.setBankRate(entity.getContract().isBankRate());
    filter.setShouldBack(entity.getContract().isRebateBack());
    filter.setPosMode(entity.getContract().getPosMode());

    RebateBillService.Locator.getService().queryProductRpts(filter,
        new AsyncCallback<RPageData<BSalesBill>>() {

          @Override
          public void onSuccess(RPageData<BSalesBill> result) {
            List<BSalesBill> bills = result.getValues();
            entity.getLines().clear();
            for (BSalesBill bill : bills) {
              entity.getLines().addAll(bill.getLines());
            }
            RActionEvent.fire(RebateGeneralEditGadget.this, RebateLineGadget.ACTION_REFRESH_LINES);
          }

          @Override
          public void onFailure(Throwable caught) {
            RMsgBox.showError(caught);
          }
        });
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
  }
}
