/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateInfoGadget.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月8日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui.gadget;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BDirection;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateLine;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesPayment;
import com.hd123.m3.account.gwt.rebate.intf.client.dd.PRebateBillDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.widget2.client.form.RCombinedField;
import com.hd123.rumba.gwt.widget2.client.form.RForm;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.form.RViewNumberField;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;

/**
 * 返款信息面板
 * 
 * @author chenganbang
 */
public class RebateInfoGadget extends RCaptionBox implements HasRActionHandlers, RActionHandler {

  public static final String ACTION_REFRESH_TOTAL = "refresh_total";

  private RHTMLField backTotalDirField;// 销售额返款收付方向
  private RViewNumberField backTotalField;// 返款合计->销售额返款
  private RHTMLField poundageTotalDirField;// 手续费收付方向
  private RViewNumberField poundageTotalField;// 返款合计->手续费
  private RHTMLField shouldBackTotalDirField;// 合计收付方向
  private RViewNumberField shouldBackTotalField;// 返款合计->合计

  private Map<String, RViewNumberField> salePaymentFields = new HashMap<String, RViewNumberField>();
  private RViewNumberField paymentTotalField;

  private BRebateBill entity;
  private EPRebateBill ep = EPRebateBill.getInstance();

  public RebateInfoGadget() {
    super();
    setCaption(PRebateBillDef.constants.rebateInfo());
    setWidth("100%");
    getCaptionBar().setShowCollapse(true);

    RMultiVerticalPanel mvp = new RMultiVerticalPanel(2);
    mvp.setWidth("100%");
    mvp.setSpacing(5);
    mvp.setColumnWidth(0, "50%");
    mvp.setColumnWidth(1, "50%");

    mvp.add(0, drawRebateAmount());
    mvp.add(1, drawPayDetails());

    setContent(mvp);
  }

  /**
   * 返款合计
   * 
   * @return
   */
  private Widget drawRebateAmount() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    backTotalDirField = new RHTMLField();
    backTotalDirField.addTextStyleName(RTextStyles.STYLE_RED);
    backTotalDirField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

    backTotalField = new RViewNumberField();
    backTotalField.setFormat(M3Format.fmt_money);
    backTotalField.addTextStyleName(RTextStyles.STYLE_RED);
    backTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    backTotalField.setValue(BigDecimal.ZERO);

    RCombinedField field1 = new RCombinedField() {
      {
        addField(backTotalDirField, 0.5f);
        addField(backTotalField, 0.5f);
      }
    };
    field1.setWidth("50%");
    field1.setCaption(PRebateBillDef.constants.backTotalInfo());
    form.addField(field1);

    poundageTotalDirField = new RHTMLField();
    poundageTotalDirField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

    poundageTotalField = new RViewNumberField();
    poundageTotalField.setFormat(M3Format.fmt_money);
    poundageTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    poundageTotalField.setValue(BigDecimal.ZERO);

    RCombinedField field2 = new RCombinedField() {
      {
        addField(poundageTotalDirField, 0.5f);
        addField(poundageTotalField, 0.5f);
      }
    };
    field2.setWidth("50%");
    field2.setCaption(PRebateBillDef.constants.poundageTotalInfo());
    form.addField(field2);

    shouldBackTotalDirField = new RHTMLField();
    shouldBackTotalDirField.addTextStyleName(RTextStyles.STYLE_BOLD);
    shouldBackTotalDirField.addTextStyleName(RTextStyles.STYLE_RED);
    shouldBackTotalDirField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

    shouldBackTotalField = new RViewNumberField();
    shouldBackTotalField.setFormat(M3Format.fmt_money);
    shouldBackTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    shouldBackTotalField.addTextStyleName(RTextStyles.STYLE_BOLD);
    shouldBackTotalField.addTextStyleName(RTextStyles.STYLE_RED);
    shouldBackTotalField.setValue(BigDecimal.ZERO);

    RCombinedField field3 = new RCombinedField() {
      {
        addField(shouldBackTotalDirField, 0.5f);
        addField(shouldBackTotalField, 0.5f);
      }
    };
    field3.setWidth("50%");
    field3.setCaption(PRebateBillDef.constants.shouldBackTotalInfo());
    form.addField(field3);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(PRebateBillDef.constants.rebateTotal());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  /**
   * 销售付款明细
   * 
   * @return
   */
  private Widget drawPayDetails() {
    RForm form = new RForm(1);
    form.setWidth("100%");

    for (HasUCN payment : ep.getPaymentTypes()) {
      RViewNumberField field = new RViewNumberField(payment.getName());
      field.setFormat(M3Format.fmt_money);
      field.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      field.setWidth("50%");
      field.setValue(BigDecimal.ZERO);
      form.addField(field);
      salePaymentFields.put(payment.getUuid(), field);
    }
    paymentTotalField = new RViewNumberField("合计");
    paymentTotalField.setFormat(M3Format.fmt_money);
    paymentTotalField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    paymentTotalField.setWidth("50%");
    paymentTotalField.setStyleName(RTextStyles.STYLE_BOLD);
    paymentTotalField.setValue(BigDecimal.ZERO);
    form.addField(paymentTotalField);

    RCaptionBox box = new RCaptionBox();
    box.setStyleName(RCaptionBox.STYLENAME_CONCISE);
    box.setCaption(PRebateBillDef.constants.rebateLine());
    box.setWidth("100%");
    box.setContent(form);
    return box;
  }

  /**
   * 设置值
   * 
   * @param lines
   */
  public void setValue(BRebateBill entity) {
    this.entity = entity;
    refresh();
    refreshDir();
  }

  private void refresh() {
    if (entity == null)
      return;

    BigDecimal rebateAmount = BigDecimal.ZERO;// 销售额返款
    BigDecimal poundageTotal = BigDecimal.ZERO;// 手续费

    BigDecimal paymentTotal = BigDecimal.ZERO;
    Map<String, BigDecimal> paymentAmountMap = new HashMap<String, BigDecimal>();

    if (entity.getLines() != null) {
      for (BRebateLine line : entity.getLines()) {
        rebateAmount = rebateAmount.add(line.getRebateAmount());
        poundageTotal = poundageTotal.add(line.getPoundageAmount());
        // 设置销售付款明细中的值
        for (BSalesPayment payment : line.getPayments()) {
          BigDecimal total = paymentAmountMap.get(payment.getPayment().getUuid());
          paymentAmountMap.put(payment.getPayment().getUuid(), total == null ? payment.getTotal()
              : payment.getTotal().add(total));
          paymentTotal = paymentTotal.add(payment.getTotal());
        }
      }
    }

    entity.setBackTotal(rebateAmount);
    entity.setPoundageTotal(poundageTotal);
    entity.setShouldBackTotal(rebateAmount.subtract(poundageTotal));

    backTotalField.setValue(entity.getBackTotal());
    poundageTotalField.setValue(entity.getPoundageTotal());
    for (String key : salePaymentFields.keySet()) {
      if (paymentAmountMap.get(key) != null) {
        salePaymentFields.get(key).setValue(paymentAmountMap.get(key));
      } else {
        salePaymentFields.get(key).setValue(BigDecimal.ZERO);
      }
    }
    paymentTotalField.setValue(paymentTotal);
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (ACTION_REFRESH_TOTAL.equals(event.getActionName())) {
      refresh();
      refreshDir();
    }
  }

  private void refreshDir() {
    if (entity != null && entity.getContract() != null) {
      String rebateDir = BDirection.MAP.get(entity.getContract().getRebateDirection());
      String poundageDir = BDirection.MAP.get(entity.getContract().getPoundageDirection());
      if (!StringUtil.isNullOrBlank(rebateDir)) {
        backTotalDirField.setHTML(rebateDir);
      }
      if (!StringUtil.isNullOrBlank(poundageDir)) {
        poundageTotalDirField.setHTML(poundageDir);
      }
      if (entity.getBackTotal().compareTo(entity.getPoundageTotal()) > 0) {
        shouldBackTotalDirField.setHTML(rebateDir);
      } else {
        shouldBackTotalDirField.setHTML(poundageDir);
      }
      if (!StringUtil.isNullOrBlank(rebateDir) && !StringUtil.isNullOrBlank(poundageDir)
          && rebateDir.equals(poundageDir)) {
        entity.setShouldBackTotal(entity.getBackTotal().add(entity.getPoundageTotal()));
        shouldBackTotalField.setValue(entity.getShouldBackTotal());
      } else {
        shouldBackTotalField.setValue(entity.getShouldBackTotal().abs());
      }
    }
  }
}
