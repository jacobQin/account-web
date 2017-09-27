/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountOptionComponent.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.service.ivc.reg.InvoiceRegs;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.payment.PaymentDefrayalType;
import com.hd123.m3.account.service.paymenttype.PaymentType;
import com.hd123.m3.account.service.paymenttype.PaymentTypeService;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.cre.controller.account.common.cache.Cache;
import com.hd123.m3.cre.controller.account.common.cache.CacheBuilder;
import com.hd123.m3.cre.controller.account.common.cache.CacheMonitor;
import com.hd123.m3.cre.controller.account.common.model.ReceiptOverdueDefault;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 账务配置项组件
 * 
 * @author LiBin
 *
 */
@Component
public class AccountOptionComponent {
  @Autowired
  protected AccountOptionService accountOptionService;
  @Autowired
  protected PaymentTypeService paymentTypeService;
  @Autowired
  protected SubjectService subjectService;

  /** 账务配置缓存，key: 配置项键值 */
  private static Cache<String> optionsCache = new CacheBuilder().name("account-options").size(100)
      .timeout(1).build();

  /** 计算精度 */
  public int getScale() {
    try {
      String scale = optionsCache.get(AccOptions.OPTION_SCALE);
      if (scale == null) {
        scale = accountOptionService.getOption(AccOptions.OPTION_PATH, AccOptions.OPTION_SCALE);
        optionsCache.put(AccOptions.OPTION_SCALE, scale);
      }
      return StringUtil.isNullOrBlank(scale) ? 2 : Integer.valueOf(scale);
    } catch (Exception e) {
      return 2;
    }
  }

  /** 舍入模式 */
  public RoundingMode getRoundingMode() {
    try {
      String roundingMode = optionsCache.get(AccOptions.OPTION_ROUNDING_MODE);
      if (roundingMode == null) {
        roundingMode = accountOptionService.getOption(AccOptions.OPTION_PATH,
            AccOptions.OPTION_ROUNDING_MODE);
        optionsCache.put(AccOptions.OPTION_ROUNDING_MODE, roundingMode);
      }
      return RoundingMode.valueOf(roundingMode);
    } catch (Exception e) {
      return RoundingMode.HALF_UP;
    }
  }

  /** 收款单滞纳金默认值配置项 */
  public ReceiptOverdueDefault getReceiptOverdueDefault() {
    String receiptOverdueDefault = optionsCache.get(AccOptions.OPTION_RECEIPT_OVERDUE_DEFAULT);
    if (receiptOverdueDefault == null) {
      receiptOverdueDefault = accountOptionService.getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_OVERDUE_DEFAULT);
      optionsCache.put(AccOptions.OPTION_RECEIPT_OVERDUE_DEFAULT, receiptOverdueDefault);
    }
    try {
      return ReceiptOverdueDefault.valueOf(receiptOverdueDefault);
    } catch (Exception e) {
      return ReceiptOverdueDefault.calcValue;
    }
  }

  /**
   * 取得配置项“长短款付款方式”，如果所配置的付款方式被禁用则返回null。
   * 
   * @return 返回配置的长短款付款方式，找不到或者被禁用将返回null。
   */
  public PaymentType getShortLongPaymentType() {

    String paymentTypeStr = optionsCache.get(AccOptions.OPTION_RECEIPT_PAYMENTTYPE);

    if (paymentTypeStr == null) {
      String receiptPaymentType = accountOptionService.getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_PAYMENTTYPE);

      PaymentType paymentType = paymentTypeService.getByCode(receiptPaymentType);
      if (paymentType != null && paymentType.isEnabled() == false) {
        return null;
      }

      optionsCache.put(AccOptions.OPTION_RECEIPT_PAYMENTTYPE, JsonUtil.objectToJson(paymentType));

      return paymentType;
    }

    return JsonUtil.jsonToObject(paymentTypeStr, PaymentType.class);

  }

  /**
   * 取得配置项“长短款付款方式限额”，如果没配置或者转换出错将直接返回0。
   */
  public BigDecimal getShortLongPayLimit() {
    String receiptPaymentTypeLimit = optionsCache.get(AccOptions.OPTION_RECEIPT_PAYMENTTYPE_LIMITS);
    if (receiptPaymentTypeLimit == null) {
      receiptPaymentTypeLimit = accountOptionService.getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_PAYMENTTYPE_LIMITS);
      optionsCache.put(AccOptions.OPTION_RECEIPT_PAYMENTTYPE_LIMITS, receiptPaymentTypeLimit);
    }
    try {
      return new BigDecimal(receiptPaymentTypeLimit);
    } catch (Exception e) {
      return BigDecimal.ZERO;
    }
  }

  /** 银行是否必填 */
  public boolean isReceiptBankRequired() {
    String bankRequired = optionsCache.get(AccOptions.OPTION_RECEIPT_BANK_REQUIRED);
    if (bankRequired == null) {
      bankRequired = accountOptionService.getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_BANK_REQUIRED);
      optionsCache.put(AccOptions.OPTION_RECEIPT_BANK_REQUIRED, bankRequired);
    }

    return Boolean.valueOf(bankRequired);
  }

  // 默认收款方式
  public String getDefaultDefrayalType() {
    String defaultDefrayalType = optionsCache.get(AccOptions.OPTION_DEFAULT_RECEIPT_PAYMENTTYPE);
    if (defaultDefrayalType == null) {
      defaultDefrayalType = accountOptionService.getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_DEFAULT_RECEIPT_PAYMENTTYPE);
    }

    if (PaymentDefrayalType.line.name().equals(defaultDefrayalType)) {
      // 兼容现有而存在，处理好配置后将去掉
      defaultDefrayalType = PaymentDefrayalType.lineSingle.name();
    }

    if (defaultDefrayalType == null) {
      defaultDefrayalType = PaymentDefrayalType.bill.name();
    }

    optionsCache.put(AccOptions.OPTION_DEFAULT_RECEIPT_PAYMENTTYPE, defaultDefrayalType);

    return defaultDefrayalType;
  }

  /** 默认预存款科目 */
  public Subject getDefaultPreSubject() {
    String subjectstr = optionsCache.get(AccOptions.DEFAULT_PRERECEIVE_SUBJECT);
    if (subjectstr == null) {
      String defaultPreRecSubjectUuid = accountOptionService.getOption(AccOptions.DEFAULT_PATH,
          AccOptions.DEFAULT_PRERECEIVE_SUBJECT);

      Subject subject = subjectService.get(defaultPreRecSubjectUuid);
      if (subject != null && subject.isEnabled() == false) {
        return null;
      }

      optionsCache.put(AccOptions.DEFAULT_PRERECEIVE_SUBJECT, JsonUtil.objectToJson(subject));

      return subject;
    }

    return JsonUtil.jsonToObject(subjectstr, Subject.class);

  }

  public PaymentType getDefaltPaymentType() {
    String defaultPaymentTypeStr = optionsCache.get(AccOptions.DEFAULT_PAYMENT);
    if (defaultPaymentTypeStr == null) {
      String defaultPaymentTypeUuid = accountOptionService.getOption(AccOptions.DEFAULT_PATH,
          AccOptions.DEFAULT_PAYMENT);

      PaymentType paymentType = paymentTypeService.get(defaultPaymentTypeUuid);
      if (paymentType == null || paymentType.isEnabled() == false) {
        return null;
      }
      optionsCache.put(AccOptions.DEFAULT_PAYMENT, JsonUtil.objectToJson(paymentType));
      return paymentType;
    }
    return JsonUtil.jsonToObject(defaultPaymentTypeStr, PaymentType.class);

  }

  /** 收款金额默认等于未收金额 */
  public boolean isReceiptEqualsUnpayed() {
    String receiptEqualsUnpayed = optionsCache.get(AccOptions.OPTION_RECEIPT_EQUALS_UNPAYED);
    if (receiptEqualsUnpayed == null) {
      receiptEqualsUnpayed = accountOptionService.getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_RECEIPT_EQUALS_UNPAYED);
      optionsCache.put(AccOptions.OPTION_RECEIPT_EQUALS_UNPAYED, receiptEqualsUnpayed);
    }

    return Boolean.valueOf(receiptEqualsUnpayed);
  }

  // 是否启用核算主体
  public boolean isAccObjectEnabled() {
    String accObjectEnabled = optionsCache.get(AccOptions.OPTION_ACCOBJECT_ENABLED);
    if (accObjectEnabled == null) {
      accObjectEnabled = accountOptionService.getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_ACCOBJECT_ENABLED);
      optionsCache.put(AccOptions.OPTION_ACCOBJECT_ENABLED, accObjectEnabled);
    }

    return Boolean.valueOf(accObjectEnabled);
  }

  // 是否启用保证金按单管理
  public boolean isByDepositEnabled() {
    String byDepositEnabled = optionsCache.get(AccOptions.OPTION_BYDEPOSIT_ENABLED);
    if (byDepositEnabled == null) {
      byDepositEnabled = accountOptionService.getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_BYDEPOSIT_ENABLED);
      optionsCache.put(AccOptions.OPTION_BYDEPOSIT_ENABLED, byDepositEnabled);
    }

    return Boolean.valueOf(byDepositEnabled);
  }
  
  // 是否启用外部发票系统
  public boolean isExtinvSystemEnabled() {
    String extinvSystemEnabled = optionsCache.get(AccOptions.EXTINVSYSTEM_ENABLED);
    if (extinvSystemEnabled == null) {
      extinvSystemEnabled = accountOptionService.getOption(AccOptions.OPTION_PATH,
          AccOptions.EXTINVSYSTEM_ENABLED);
      optionsCache.put(AccOptions.EXTINVSYSTEM_ENABLED, extinvSystemEnabled);
    }

    return Boolean.valueOf(extinvSystemEnabled);
  }
  
  /** 收款发票登记单默认发票类型 */
  public String getDefalutInvoiceType() {
    String defalutInvoiceType = optionsCache.get(AccOptions.OPTION_DEFAULT_INVOICETYPE);
    if (defalutInvoiceType == null) {
      defalutInvoiceType = accountOptionService.getOption(AccOptions.OPTION_PATH,
          AccOptions.OPTION_DEFAULT_INVOICETYPE);
      
      if ("null".equals(defalutInvoiceType)) {
        defalutInvoiceType = null;
      }
      optionsCache.put(AccOptions.OPTION_DEFAULT_INVOICETYPE, defalutInvoiceType);
    }
    
    return defalutInvoiceType;
  }
  
  public boolean isAllowSplitReg(){
    String allowSplitReg = optionsCache.get(InvoiceRegs.OPTION_REC_ALLOW_SPLIT_REG);
    if (allowSplitReg == null) {
      allowSplitReg = accountOptionService.getOption(InvoiceRegs.OBJECT_NAME_RECEIPT,
          InvoiceRegs.OPTION_REC_ALLOW_SPLIT_REG);
      optionsCache.put(InvoiceRegs.OPTION_REC_ALLOW_SPLIT_REG, allowSplitReg);
    }
    
    return StringUtil.toBoolean(allowSplitReg, false);
  }
  
  public boolean isUseInvoiceStock(){
    String useInvoiceStock = optionsCache.get(InvoiceRegs.OPTION_REC_USEINVOICESTOCK);
    if (useInvoiceStock == null) {
      useInvoiceStock = accountOptionService.getOption(InvoiceRegs.OBJECT_NAME_RECEIPT,
          InvoiceRegs.OPTION_REC_USEINVOICESTOCK);
      optionsCache.put(InvoiceRegs.OPTION_REC_ALLOW_SPLIT_REG, useInvoiceStock);
    }
    return StringUtil.toBoolean(useInvoiceStock, false);
  }
  
  static {
    CacheMonitor.get().monitor(optionsCache);
  }

}
