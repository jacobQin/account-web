/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountDataFilter.java
 * 模块说明：	
 * 修改历史：
 * 2016年8月26日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.commons.biz.date.DateRange;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.cre.controller.account.common.model.AccountId;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 账款数据查询条件
 * 
 * @author LiBin
 *
 */
public class AccountDataFilter extends QueryFilter {

  /** 账单 */
  public static final String STATEMENT = "statement";
  /** 收付款单*/
  public static final String PAYMENT = "payment";
  /** 项目uuid */
  public static final String ACCOUNTUNIT_UUID = "accountUnitUuid";
  /** 对方单位uuid */
  public static final String COUNTERPART_UUID = "counterpartUuid";
  /** 对方单位名称 */
  public static final String COUNTERPART_NAME = "counterpartName";
  /** 对方单位类型 */
  public static final String COUNTERPART_TYPE = "counterpartType";
  /** 合同Id */
  public static final String CONTRACT_ID = "contractId";
  /** 合同编号 */
  public static final String CONTRACT_NUMBER = "contractNumber";
  /** 店招 */
  public static final String CONTRACT_NAME = "contractName";
  /** 来源单据（包含来源单据类型及单号） */
  public static final String SOURCEBILL = "sourceBill";
  /** 来源单据号 */
  public static final String SOURCEBILL_NUMBER = "sourceBillNumber";
  /** 来源单据类型 */
  public static final String SOURCEBILL_TYPE = "sourceBillType";
  /** 科目uuid */
  public static final String SUBJECT_UUID = "subjectUuid";
  /** 发票（包含发票代码及发票号码） */
  public static final String INVOICE = "invoice";
  /** 发票代码 */
  public static final String INVOICE_CODE = "invoiceCode";
  /** 发票号码 */
  public static final String INVOICE_NUMBER = "invoiceNumber";
  /** 账款类型 */
  public static final String DIRECTION_TYPE = "directionType";
  /** 忽略账款方向 */
  public static final String IGNORE_DIRECTIONTYPE = "ignoreDirectionType";
  /** 锁定账款单据uuid， 允许为空。 用于查询账款时，将被当前单据（收付款单、收款发票登记单）锁定的账款查询出来。 */
  public static final String LOCKER_UUID = "lockerUuid";

  /** 发票登记号 */
  public static final String INVOICEREG_NUMBER = "invoiceRegNumber";
  /** 出账日期区间 */
  public static final String ACCOUNTDATE_PERIOD = "accountDatePeriod";
  /** 发票登记日期区间 */
  public static final String REGDATE_PERIOD = "regDatePeriod";
  /** 已导入的账款id集合 */
  public static final String HASADDED_ACCIDS = "hasAddedAccIds";
  /** 查询正负账款时是否需要取反账款 */
  public static final String OPPOSITE_DIRECTION = "oppositeDirection";

  public String getSourceBillNumber() {
    Map<String, String> sourceBill = (Map) getFilter().get(SOURCEBILL);
    if (sourceBill == null) {
      return null;
    }
    return sourceBill.get(SOURCEBILL_NUMBER);
  }

  public String getAccountUnitUuid() {
    return (String) getFilter().get(ACCOUNTUNIT_UUID);
  }

  public String getCounterpartUuid() {
    return (String) getFilter().get(COUNTERPART_UUID);
  }

  public String getCounterpartName() {
    return (String) getFilter().get(COUNTERPART_NAME);
  }

  public String getContractId() {
    return (String) getFilter().get(CONTRACT_ID);
  }

  public String getContractNumber() {
    return (String) getFilter().get(CONTRACT_NUMBER);
  }

  public String getContractName() {
    return (String) getFilter().get(CONTRACT_NAME);
  }

  /** 出账日期区间 */
  public DateRange getAccountDatePeriod() {
    Map<String, String> period = (Map) getFilter().get(ACCOUNTDATE_PERIOD);
    if (period == null) {
      return null;
    }
    String beginDate = period.get("beginDate");
    String endDate = period.get("endDate");
    if (StringUtil.isNullOrBlank(beginDate) && StringUtil.isNullOrBlank(endDate)) {
      return null;
    }

    DateRange accountDatePeriod = new DateRange();
    try {
      if (StringUtil.isNullOrBlank(beginDate) == false) {
        accountDatePeriod.setBeginDate(StringUtil.toDate(beginDate));
      }
      if (StringUtil.isNullOrBlank(endDate) == false) {
        accountDatePeriod.setEndDate(StringUtil.toDate(endDate));
      }
    } catch (Exception e) {
      // Do Nothing
    }

    return accountDatePeriod;
  }

  /** 登记日期区间 */
  public DateRange getRegDatePeriod() {
    Map<String, String> period = (Map) getFilter().get(REGDATE_PERIOD);
    if (period == null) {
      return null;
    }
    String beginDate = period.get("beginDate");
    String endDate = period.get("endDate");
    if (StringUtil.isNullOrBlank(beginDate) && StringUtil.isNullOrBlank(endDate)) {
      return null;
    }

    DateRange regDatePeriod = new DateRange();
    try {
      if (StringUtil.isNullOrBlank(beginDate) == false) {
        regDatePeriod.setBeginDate(StringUtil.toDate(beginDate));
      }
      if (StringUtil.isNullOrBlank(endDate) == false) {
        regDatePeriod.setEndDate(StringUtil.toDate(endDate));
      }
    } catch (Exception e) {
      // Do Nothing
    }

    return regDatePeriod;
  }

  public String getSourceBillType() {
    Map<String, String> sourceBill = (Map) getFilter().get(SOURCEBILL);
    if (sourceBill == null) {
      return null;
    }
    return sourceBill.get(SOURCEBILL_TYPE);
  }

  public int getDirectionType() {
    Integer direction = (Integer) getFilter().get(DIRECTION_TYPE);
    return direction == null ? Direction.RECEIPT : direction.intValue();
  }

  public String getSubjectUuid() {
    return (String) getFilter().get(SUBJECT_UUID);
  }

  public String getStatement() {
    return (String) getFilter().get(STATEMENT);
  }

  public String getPayment(){
    return (String) getFilter().get(PAYMENT);
  }
  
  public String getInvoiceCode() {
    Map<String, String> sourceBill = (Map) getFilter().get(INVOICE);
    if (sourceBill == null) {
      return null;
    }
    return sourceBill.get(INVOICE_CODE);
  }

  public String getInvoiceNumber() {
    Map<String, String> sourceBill = (Map) getFilter().get(INVOICE);
    if (sourceBill == null) {
      return null;
    }
    return sourceBill.get(INVOICE_NUMBER);
  }

  public String getLockerUuid() {
    return (String) getFilter().get(LOCKER_UUID);
  }

  public String getCounterpartType() {
    return (String) getFilter().get(COUNTERPART_TYPE);
  }

  public Boolean isIgnoreDirectionType() {
    Boolean ignoreDirectionType = (Boolean) getFilter().get(IGNORE_DIRECTIONTYPE);
    return ignoreDirectionType == null ? false : ignoreDirectionType;
  }

  public boolean isOppositeDirection() {
    Boolean isOppositeDirection = (Boolean) getFilter().get(OPPOSITE_DIRECTION);
    return isOppositeDirection == null ? true : isOppositeDirection;
  }

  public String getInvoiceRegNumber() {
    return (String) getFilter().get(INVOICEREG_NUMBER);
  }

  public List<AccountId> getHasAddedAccIds() {
    List<AccountId> accountIds = new ArrayList<AccountId>();
    List accIds = (List) getFilter().get(HASADDED_ACCIDS);
    if (accIds == null) {
      return accountIds;
    }
    for (Object obj : accIds) {
      Map<String, String> accountIdMap = (Map<String, String>) obj;
      AccountId accountId = new AccountId();
      accountId.setAccId(accountIdMap.get("accId"));
      accountId.setBizId(accountIdMap.get("bizId"));

      accountIds.add(accountId);
    }
    return accountIds;
  }

}
