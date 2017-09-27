/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	FeeImportProcessor.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月12日 - lizongyi - 创建。
 */
package com.hd123.m3.cre.controller.account.fee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hd123.m3.account.commons.Total;
import com.hd123.m3.account.rs.service.settle.AccSettle;
import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.fee.FeeLine;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.SubjectStoreTaxRate;
import com.hd123.m3.account.service.subject.SubjectType;
import com.hd123.m3.account.service.subject.SubjectUsage;
import com.hd123.m3.account.service.subject.Subjects;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.tax.TaxCalculator;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.util.impex.poi.WorkbookUtil;
import com.hd123.m3.investment.service.contract.contract.ContractService;
import com.hd123.m3.investment.service.contract.contract.ContractSummary;
import com.hd123.m3.investment.service.contract.model.ContractType;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author lizongyi
 *
 */
@Component
public class FeeImportProcessor {

  public static final String COL_STORE_CODE = "项目代码";
  public static final String COL_STORE_NAME = "项目名称";
  public static final String COL_CONTRACT = "合同号";
  public static final String COL_CONTRACT_SIGNBOARD = "店招";
  public static final String COL_ACCOUNTDATE = "记账日期";
  public static final String COL_GENERATESTATEMENT = "进账单";
  public static final String COL_BEGINDATE = "开始日期";
  public static final String COL_ENDDATE = "截止日期";
  public static final String COL_SUBJECT_CODE = "科目代码";
  public static final String COL_SUBJECT_NAME = "科目名称";
  public static final String COL_TOTAL = "金额";
  public static final String COL_ISSUEINVOICE = "是否开票";

  @Autowired
  private ContractService contractService;

  @Autowired
  private com.hd123.m3.account.service.contract.ContractService accContractService;

  @Autowired
  private SubjectService subjectService;

  public List<BFee> importFile(String filePath, String userId, String scale, String roundingMode)
      throws Exception {
    List<BFee> result = new ArrayList<BFee>();
    List<String> errors = new ArrayList<String>();

    Workbook book = WorkbookUtil.getWorkbook(filePath);
    Sheet sheet = book.getSheetAt(0);

    Map<String, Subject> subjectMap = querySubjects();

    for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
      Row row = sheet.getRow(rowIdx);
      if (WorkbookUtil.isRowEmpty(row))
        continue;

      BFee bFee = new BFee();
      bFee.setBizState(BBizStates.INEFFECT);
      bFee.setDirection(1);

      bFee.setAccountUnit(new UCN());
      try {
        bFee.getAccountUnit().setCode(WorkbookUtil.getStringCellValue(row, COL_STORE_CODE, true));
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }

      try {
        bFee.getAccountUnit().setName(WorkbookUtil.getStringCellValue(row, COL_STORE_NAME, true));
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }

      bFee.setContract(new UCN());
      try {
        bFee.getContract().setCode(WorkbookUtil.getStringCellValue(row, COL_CONTRACT, false));
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }

      ContractSummary contract = null;
      if (StringUtil.isNullOrBlank(bFee.getContract().getCode()) == false) {
        contract = contractService.getSummaryByNumber(bFee.getContract().getCode());
        if (contract == null) {
          errors.add("第" + rowIdx + "行：找不到对应的合同号" + bFee.getContract().getCode());
        }
      }

      try {
        bFee.getContract().setName(
            WorkbookUtil.getStringCellValue(row, COL_CONTRACT_SIGNBOARD, true));
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }

      if (contract != null) {
        bFee.setAccountUnit(contract.getStore());
        bFee.setCounterpart(contract.getTenant());
        bFee.setCounterpartType(ContractType.proprietor == contract.getContractType() ? "_Proprietor"
            : "_Tenant");
        bFee.getContract().setUuid(contract.getId());
        bFee.getContract().setName(contract.getSignboard());
      }

      try {
        bFee.setAccountDate(WorkbookUtil.getDateCellValue(row, COL_ACCOUNTDATE, false));
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }

      try {
        if ("是".equals(WorkbookUtil.getStringCellValue(row, COL_GENERATESTATEMENT))) {
          bFee.setGenerateStatement(true);
        } else if ("否".equals(WorkbookUtil.getStringCellValue(row, COL_GENERATESTATEMENT))) {
          bFee.setGenerateStatement(false);
        } else {
          throw new Exception("进账单值输入错误");
        }
        if (bFee.isGenerateStatement() && contract != null) {
          validateContract(contract.getId(), bFee.getAccountDate());
        }
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }

      bFee.setLines(new ArrayList<FeeLine>());
      FeeLine line = new FeeLine();

      line.setSubject(new UCN());
      try {
        line.getSubject().setCode((WorkbookUtil.getStringCellValue(row, COL_SUBJECT_CODE, false)));
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }
      try {
        line.getSubject().setName((WorkbookUtil.getStringCellValue(row, COL_SUBJECT_NAME, true)));
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }

      if (StringUtil.isNullOrBlank(line.getSubject().getCode()) == false) {
        Subject sub = subjectMap.get(line.getSubject().getCode());
        if (sub == null) {
          errors.add("第" + rowIdx + "行：找不到对应的科目（" + line.getSubject().getCode() + "）");
        } else {
          line.setSubject(UCN.newInstance(sub));
          line.setTaxRate(getStoreTexRate(bFee.getStore() == null ? null : bFee.getStore()
              .getUuid(), sub));
        }
      }

      try {
        line.setBeginDate(WorkbookUtil.getDateCellValue(row, COL_BEGINDATE, false));
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }

      try {
        line.setEndDate(WorkbookUtil.getDateCellValue(row, COL_ENDDATE, false));

        if (line.getBeginDate() != null && line.getEndDate() != null
            && line.getBeginDate().after(line.getEndDate())) {
          throw new Exception("截止日期必须大于等于起始日期。");
        }
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }

      line.setTotal(Total.zero());

      try {
        line.getTotal().setTotal(
            WorkbookUtil.getBigDecimalCellValue(row, COL_TOTAL, false,
                WorkbookUtil.BIGDECIMAL_MINVALUE_S2, true, WorkbookUtil.BIGDECIMAL_MAXVALUE_S2,
                true, 2));

        if (BigDecimal.ZERO.compareTo(line.getTotal().getTotal()) == 0) {
          throw new Exception("金额不能等于0。");
        }
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }

      // 税额
      line.getTotal().setTax(
          TaxCalculator.tax(line.getTotal().getTotal(), line.getTaxRate(),
              StringUtil.toInteger(scale),
              StringUtil.toEnum(roundingMode, RoundingMode.class, RoundingMode.HALF_UP)));

      try {
        if ("是".equals(WorkbookUtil.getStringCellValue(row, COL_ISSUEINVOICE))) {
          line.setIssueInvoice(true);
        } else if ("否".equals(WorkbookUtil.getStringCellValue(row, COL_ISSUEINVOICE))) {
          line.setIssueInvoice(false);
        } else {
          throw new Exception("是否开票值输入错误。");
        }
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }

      bFee.getLines().add(line);
      bFee.resetTotal();
      result.add(bFee);
    }

    if (errors.isEmpty() == false)
      throw new Exception(JsonUtil.objectToJson(errors));

    List<BFee> generateStatements = new ArrayList<BFee>();
    List<BFee> notGenerateStatements = new ArrayList<BFee>();

    for (BFee bf : result) {
      if (bf.isGenerateStatement()) {
        generateStatements.add(bf);
      } else {
        notGenerateStatements.add(bf);
      }
    }

    result.clear();
    mergeLines(generateStatements, result);
    mergeLines(notGenerateStatements, result);
    return result;
  }

  private void mergeLines(List<BFee> list, List<BFee> result) {
    // 将导入行按合同id分类
    Map<String, List<BFee>> cache = new HashMap<String, List<BFee>>();
    for (BFee bf : list) {
      List<BFee> ls = cache.get(bf.getContract().getUuid());
      if (ls == null) {
        cache.put(bf.getContract().getUuid(), new ArrayList<BFee>());
        ls = cache.get(bf.getContract().getUuid());
      }
      ls.add(bf);
    }

    for (List<BFee> ls : cache.values()) {
      if (ls == null || ls.isEmpty()) {
        continue;
      }
      // 合同一致且记账日期一致的，合并明细行
      Map<Date, BFee> map = new HashMap<Date, BFee>();
      for (BFee bf : ls) {
        BFee entity = map.get(bf.getAccountDate());
        if (entity == null) {
          map.put(bf.getAccountDate(), bf);
          entity = map.get(bf.getAccountDate());
          continue;
        }
        entity.getLines().addAll(bf.getLines());
        entity.resetTotal();
      }

      result.addAll(map.values());
    }
  }

  private Map<String, Subject> querySubjects() {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.addCondition(Subjects.CONDITION_STATE_EQUALS, Boolean.TRUE);
    queryDef.addCondition(Subjects.CONDITION_DIRECTION_EQUALS, 1);
    queryDef.addCondition(Subjects.CONDITION_SUBJECT_TYPE_EQUALS, SubjectType.credit.name());
    queryDef.addCondition(Subjects.CONDITION_USAGE_EQUALS, UsageType.tempFee.name());

    QueryResult<Subject> qr = subjectService.query(queryDef, Subject.PART_STORE_TAXRATE);

    Map<String, Subject> result = new HashMap<String, Subject>();
    for (Subject sub : qr.getRecords()) {
      result.put(sub.getCode(), sub);
    }
    return result;
  }

  private TaxRate getStoreTexRate(String store, Subject subject) {
    if (subject.getStoreTaxRates() == null || subject.getStoreTaxRates().isEmpty()) {
      return subject.getTaxRate();
    }

    for (SubjectStoreTaxRate rate : subject.getStoreTaxRates()) {
      if (rate.getStore() != null && rate.getStore().getUuid().equals(store)) {
        return rate.getTaxRate();
      }
    }

    return subject.getTaxRate();
  }

  public void validateContract(String contractId, Date accountDate) throws Exception {
    if (StringUtil.isNullOrBlank(contractId)) {
      return;
    }
    Contract contract = accContractService.get(contractId);
    if (contract == null) {
      throw new Exception("找不到对应合同。");
    }

    AccSettle settle = getLastAccountSettle(contractId, false);
    if (settle == null) {
      throw new Exception("合同不存在临时费用类型的结算周期。");
    }

    if (accountDate == null) {
      return;
    }
    settle = getLastAccountSettle(contractId, true);
    if (settle != null && accountDate.after(settle.getEndDate()) == false) {
      throw new Exception("记账日期必须大于所属结算周期的最大已出账账期的截止日期"
          + StringUtil.dateToString(settle.getEndDate(), StringUtil.DATE_FORMATS[4]));
    }

    if (accountDate.after(DateUtils.addMonths(contract.getEndDate(), 6))) {
      throw new Exception("记账日期必须小于等于合同结算周期的截止日期"
          + StringUtil.dateToString(DateUtils.addMonths(contract.getEndDate(), 6),
              StringUtil.DATE_FORMATS[4]));
    }

    if (accountDate.before(contract.getBeginDate())) {
      throw new Exception("记账日期必须大于等于合同的起始日期"
          + StringUtil.dateToString(contract.getBeginDate(), StringUtil.DATE_FORMATS[4]));
    }

  }

  private AccSettle getLastAccountSettle(String contractUuid, boolean accounted)
      throws M3ServiceException {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.addCondition(Contracts.CONDITION_ID_EQUALS, contractUuid);
      if (accounted) {
        def.addCondition(Contracts.CONDITION_ACCOUNTED);
      }

      List<SubjectUsage> usages = getSubjectUsages(UsageType.tempFee);
      Set<String> usageCodes = new HashSet<String>();
      for (SubjectUsage usage : usages) {
        usageCodes.add(usage.getCode());
      }
      def.addCondition(Contracts.CONDITION_SUBJECTUSAGE_IN, usageCodes.toArray());

      def.addOrder(Contracts.ORDER_BY_ENDDATE, QueryOrderDirection.desc);

      def.setPageSize(1);

      QueryResult<AccountSettle> qry = accContractService.queryAccountSettle(def);
      if (qry.getRecords().size() == 0) {
        return null;
      } else {
        return convertAccSettle(qry.getRecords().get(0));
      }
    } catch (Exception e) {
      throw new M3ServiceException(e);
    }
  }

  private AccSettle convertAccSettle(AccountSettle source) {
    if (source == null)
      return null;

    AccSettle t = new AccSettle();
    if (source.getBill() != null
        && !Contracts.NONE_BILL_UUID.equals(source.getBill().getBillUuid())) {
      t.setAccountBill(source.getBill().getBillUuid());
      t.setAccountNumber(source.getBill().getBillNumber());
    }
    t.setAccountTime(source.getAccountTime());
    t.setBeginDate(source.getBeginDate());
    t.setContract(source.getContract().getUuid());
    t.setEndDate(source.getEndDate());
    t.setPlanDate(source.getPlanDate());
    t.setSettlement(source.getSettlement().getUuid());
    t.setUuid(source.getUuid());

    return t;
  }

  private List<SubjectUsage> getSubjectUsages(UsageType type) {
    if (type == null) {
      return new ArrayList<SubjectUsage>();
    }

    List<SubjectUsage> result = new ArrayList<SubjectUsage>();

    List<SubjectUsage> usages = subjectService.getAllUsages();
    for (SubjectUsage usage : usages) {
      if (type.name().equals(usage.getUsageType().name())) {
        result.add(usage);
      }
    }
    return result;
  }
}
