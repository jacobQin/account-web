/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-web
 * 文件名：	FeeController.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月11日 - lizongyi - 创建。
 */
package com.hd123.m3.cre.controller.account.fee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.rs.service.settle.AccSettle;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.ContractService;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.fee.Fee;
import com.hd123.m3.account.service.fee.FeeDetail;
import com.hd123.m3.account.service.fee.FeeLine;
import com.hd123.m3.account.service.fee.FeeService;
import com.hd123.m3.account.service.fee.Fees;
import com.hd123.m3.account.service.option.AccountOptionService;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.SubjectUsage;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.flow.BizFlow;
import com.hd123.m3.commons.biz.flow.BizState;
import com.hd123.m3.commons.biz.option.BasicConfigService;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.query.QueryDefinitionUtil;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.export.ExportColumnDef;
import com.hd123.m3.commons.servlet.biz.export.ExportDefinition;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.audit.AuditActions;
import com.hd123.m3.commons.servlet.controllers.module.BizFlowModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.commons.util.impex.poi.WorkbookUtil;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.ExceptionUtil;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author lizongyi
 *
 */
@Controller
@RequestMapping("account/fee/*")
public class FeeController extends BizFlowModuleController {

  /** 计算精度,小数位数 */
  public static final String KEY_SCALE = "scale";
  /** 计算精度,舍入算法 */
  public static final String KEY_ROUNDING_MODE = "roundingMode";

  public static final String RESOURCE_PATH = "account/fee";

  public static final String KEY_VALIDATE_CONTRACT = "contract";
  public static final String KEY_VALIDATE_ACCOUNTDATE = "accountDate";

  @Autowired
  private FeeService feeService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private ContractService contractService;

  @Autowired
  private SubjectService subjectService;

  @Autowired
  private AccountOptionService accountOptionService;

  @Autowired
  private BasicConfigService basicConfigService;

  @Autowired
  private FeeImportProcessor importProcessor;

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        AccountPermResourceDef.RESOURCE_FEE, AccountPermResourceDef.RESOURCE_RECEIPT,
        AccountPermResourceDef.RESOURCE_ACCOUNTDEFRAYAL));
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    moduleContext.put(KEY_SCALE,
        accountOptionService.getOption(AccOptions.OPTION_PATH, AccOptions.OPTION_SCALE));

    String roundingMode = accountOptionService.getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_ROUNDING_MODE);
    moduleContext.put(KEY_ROUNDING_MODE,
        StringUtil.toEnum(roundingMode, RoundingMode.class, RoundingMode.HALF_UP).ordinal());
  }

  @Override
  protected String doSave(String json) throws M3ServiceException {
    if (json == null)
      return null;

    Fee entity = JsonUtil.jsonToObject(json, Fee.class);

    Iterator<FeeLine> detailIterator = entity.getLines().iterator();
    while (detailIterator.hasNext()) {
      FeeLine detail = detailIterator.next();
      if (isEmpty(detail)) {// 过滤空行
        detailIterator.remove();
      }
    }

    return feeService.save(entity, new BeanOperateContext(getSessionUser()));
  }

  @RequestMapping(value = "getAllTaxRates", method = RequestMethod.GET)
  public @ResponseBody List<TaxRate> getAllTaxRates() {
    List<String> list = basicConfigService.getByCategory(TaxRate.class.getName());
    List<TaxRate> rates = new ArrayList<TaxRate>();

    for (String s : list) {
      rates.add(TaxRate.decode(s));
    }
    return rates;
  }

  @RequestMapping("validateContract")
  public @ResponseBody Map<String, String> validateContract(
      @RequestParam("contractId") String contractId,
      @RequestParam("accountDateStr") String accountDateStr) throws Exception {
    if (StringUtil.isNullOrBlank(contractId)) {
      return null;
    }
    Map<String, String> result = new HashMap<String, String>();
    Contract contract = contractService.get(contractId);
    if (contract == null) {
      result.put(KEY_VALIDATE_CONTRACT, "合同：找不到对应合同。");
      return result;
    }

    AccSettle settle = getLastAccountSettle(contractId, false);
    if (settle == null) {
      result.put(KEY_VALIDATE_CONTRACT, "合同：不存在临时费用类型的结算周期。");
    }

    if (StringUtil.isNullOrBlank(accountDateStr)) {
      return result;
    }

    Date accountDate = StringUtil.toDate(accountDateStr);
    if (accountDate == null) {
      return result;
    }
    settle = getLastAccountSettle(contractId, true);
    if (settle != null && accountDate.after(settle.getEndDate()) == false) {
      result.put(
          KEY_VALIDATE_ACCOUNTDATE,
          "记账日期：记账日期必须大于所属结算周期的最大已出账账期的截止日期"
              + StringUtil.dateToString(settle.getEndDate(), StringUtil.DATE_FORMATS[4]));
    }

    if (result.get(KEY_VALIDATE_ACCOUNTDATE) == null
        && accountDate.after(DateUtils.addMonths(contract.getEndDate(), 6))) {
      result.put(
          KEY_VALIDATE_ACCOUNTDATE,
          "记账日期必须小于等于合同结算周期的截止日期"
              + StringUtil.dateToString(DateUtils.addMonths(contract.getEndDate(), 6),
                  StringUtil.DATE_FORMATS[4]));
    }

    if (result.get(KEY_VALIDATE_ACCOUNTDATE) == null && accountDate.before(contract.getBeginDate())) {
      result.put(
          KEY_VALIDATE_ACCOUNTDATE,
          "记账日期必须大于等于合同的起始日期"
              + StringUtil.dateToString(contract.getBeginDate(), StringUtil.DATE_FORMATS[4]));
    }

    if (result.get(KEY_VALIDATE_ACCOUNTDATE) == null && accountDate.after(contract.getEndDate())) {
      result.put(
          KEY_VALIDATE_ACCOUNTDATE,
          "记账日期必须小于等于合同的截止日期"
              + StringUtil.dateToString(contract.getEndDate(), StringUtil.DATE_FORMATS[4]));
    }

    return result;
  }

  @RequestMapping("save")
  public @ResponseBody Fee save(@RequestBody Fee entity) throws Exception {
    Iterator<FeeLine> detailIterator = entity.getLines().iterator();
    while (detailIterator.hasNext()) {
      FeeLine detail = detailIterator.next();
      if (isEmpty(detail)) {// 过滤空行
        detailIterator.remove();
      }
    }

    String uuid = feeService.save(entity, new BeanOperateContext(getSessionUser()));

    Fee target = load(uuid);
    log(StringUtil.isNullOrBlank(entity.getUuid()) ? AuditActions.R.create()
        : AuditActions.R.modify(), uuid);
    return target;
  }

  @RequestMapping(value = "import")
  public @ResponseBody List<BFee> importFile(@RequestBody Map<String, String> impexParams)
      throws Exception {
    String filePath = impexParams.get("filePath");
    if (StringUtil.isNullOrBlank(filePath)) {
      throw ExceptionUtil.nullArgument("filePath");
    }

    String permGroupId = impexParams.get("permGroupId");
    String permGroupTitle = impexParams.get("permGroupTitle");

    List<BFee> entities = importProcessor.importFile(filePath, getSessionUserId(),
        accountOptionService.getOption(AccOptions.OPTION_PATH, AccOptions.OPTION_SCALE),
        accountOptionService.getOption(AccOptions.OPTION_PATH, AccOptions.OPTION_ROUNDING_MODE));

    for (BFee bf : entities) {
      if (StringUtil.isNullOrBlank(permGroupId) == false) {
        bf.setPermGroupId(permGroupId);
      }
      if (StringUtil.isNullOrBlank(permGroupTitle) == false) {
        bf.setPermGroupTitle(permGroupTitle);
      }
      try {
        log(AuditActions.R.create(), feeService.save(bf, new BeanOperateContext(getSessionUser())));
      } catch (Exception e) {
        throw new Exception(e.getMessage());
      }
    }

    return entities;
  }

  @RequestMapping(value = "load", method = RequestMethod.GET)
  public @ResponseBody BFee load(@RequestParam("uuid") String uuid) throws M3ServiceException {
    Fee entity = feeService.get(uuid, Fees.FETCH_LINES);
    if (entity == null) {
      entity = feeService.getByNumber(uuid, Fees.FETCH_LINES);
    }
    if (entity == null) {
      return null;
    }

    BFee target = BFee.newInstance(entity);

    if (BizStates.EFFECT.equals(entity.getBizState())) {
      target.setHasAccounts(hasAccount(entity.getUuid()).isEmpty() == false);
    }

    if (entity.getContract() != null && entity.getContract().getUuid() != null) {
      Contract contract = contractService.get(entity.getContract().getUuid());
      target.setContractEntity(contract);
    }

    return target;
  }

  @Override
  protected void decorateRecords(SummaryQueryResult queryResult) {
    super.decorateRecords(queryResult);

    List<Fee> result = queryResult.getRecords();

    List<String> strs = new ArrayList<String>();
    for (Fee fee : result) {
      if (BizStates.EFFECT.equals(fee.getBizState())) {
        strs.add(fee.getUuid());
      }
    }

    if (strs.isEmpty()) {
      return;
    }

    List<Account> accounts = hasAccount(strs.toArray(new String[] {}));
    Map<String, Account> cache = new HashMap<String, Account>();
    for (Account account : accounts) {
      cache.put(account.getAcc1().getSourceBill().getBillUuid(), account);
    }

    List<BFee> bfees = new ArrayList<BFee>();
    for (Fee fee : result) {
      BFee target = BFee.newInstance(fee);
      if (BizStates.EFFECT.equals(target.getBizState())) {
        target.setHasAccounts(cache.get(fee.getUuid()) != null);
      }
      bfees.add(target);
    }
    queryResult.setRecords(bfees);
  }

  @RequestMapping("queryDetail")
  public @ResponseBody SummaryQueryResult queryDetail(@RequestBody QueryFilter queryFilter) {
    if (queryFilter == null) {
      return new SummaryQueryResult();
    }

    FlecsQueryDefinition queryDef = getDetailQueryBuilder().build(queryFilter, getObjectName(),
        getSessionUserId());
    queryDef.getFetchParts().add(FeeDetail.FETCH_PART_FEE);
    queryDef.getFetchParts().add(FeeDetail.FETCH_PART_ACCINFO);

    QueryResult<FeeDetail> queryResult = feeService.queryDetail(queryDef);

    SummaryQueryResult result = SummaryQueryResult.newInstance(queryResult);
    buildDetailSummary(result, queryFilter);
    decorateDetails(result);
    return result;
  }

  private void decorateDetails(SummaryQueryResult result) {
    List<FeeDetail> details = result.getRecords();
    List<String> accountIds = new ArrayList<String>();
    for (FeeDetail detail : details) {
      accountIds.add(detail.getAccountId());
    }

    if (accountIds.isEmpty()) {
      return;
    }

    QueryDefinition definition = new QueryDefinition();
    definition.addCondition(Accounts.CONDITION_ACCID_IN, accountIds.toArray());
    definition.addCondition(Accounts.CONDITION_CAN_PAYMENT);
    definition.addCondition(Accounts.CONDITION_ACTIVE);

    List<Account> accounts = accountService.query(definition).getRecords();

    Map<String, Account> cache = new HashMap<String, Account>();
    for (Account account : accounts) {
      cache.put(account.getAcc1().getId(), account);
    }

    List<BFeeDetail> bizs = new ArrayList<BFeeDetail>();
    for (FeeDetail detail : details) {
      BFeeDetail biz = BFeeDetail.newInstance(detail);
      if (StringUtil.isNullOrBlank(detail.getAccountId()) == false) {
        biz.setLocked(cache.get(detail.getAccountId()) == null);
      }
      bizs.add(biz);
    }
    result.setRecords(bizs);
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

      QueryResult<AccountSettle> qry = contractService.queryAccountSettle(def);
      if (qry.getRecords().size() == 0) {
        return null;
      } else {
        return convertAccSettle(qry.getRecords().get(0));
      }
    } catch (Exception e) {
      throw new M3ServiceException(e);
    }
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

  /**
   * 处理合计
   */
  protected void buildDetailSummary(SummaryQueryResult result, QueryFilter queryFilter) {
    BizFlow flow = getBizFlowService().getBizFlow(getObjectName());
    if (flow == null)
      return;

    queryFilter.setPageSize(1);
    queryFilter.getFilter().put(FILTER_BIZSTATE, null);

    FlecsQueryDefinition queryDef = getDetailQueryBuilder().build(queryFilter, getObjectName(),
        getSessionUserId());
    result.getSummary().put("all", feeService.queryDetail(queryDef).getRecordCount());

    Map<String, Object> filter = queryFilter.getFilter() == null ? new HashMap<String, Object>()
        : queryFilter.getFilter();
    for (BizState bizState : flow.getStates()) {
      filter.put(FILTER_BIZSTATE, bizState.getName());

      queryDef = getDetailQueryBuilder().build(queryFilter, getObjectName(), getSessionUserId());
      result.getSummary()
          .put(bizState.getName(), feeService.queryDetail(queryDef).getRecordCount());
    }
  }

  private List<Account> hasAccount(String... sourceBillUuids) {
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addCondition(Accounts.CONDITION_ACTIVE);
    definition.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_SOURCEBILL_UUID_IN,
            sourceBillUuids));
    definition.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_CAN_PAYMENT));
    definition.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS,
            Accounts.NONE_LOCK_UUID));

    return accountService.query(definition).getRecords();
  }

  @RequestMapping("export")
  public @ResponseBody String export(@RequestBody ExportDefinition definition) throws Exception {
    if (definition == null)
      return null;

    String fileURL = "/static/temp/FeeDetail"
        + StringUtil.dateToString(new Date(), "yyyyMdHHmmssSSS") + ".xlsx";

    Workbook book = new XSSFWorkbook();
    Sheet sheet = book.createSheet("Sheet1");

    QueryFilter filter = new QueryFilter();
    filter.setFilter(definition.getFilter());
    filter.setSorts(definition.getSorts());
    FlecsQueryDefinition queryDef = getDetailQueryBuilder().build(filter, getObjectName(),
        getSessionUserId());
    queryDef.getFetchParts().add(FeeDetail.FETCH_PART_FEE);
    queryDef.getFetchParts().add(FeeDetail.FETCH_PART_ACCINFO);
    queryDef.setPage(0);
    queryDef.setPageSize(0);
    QueryResult<FeeDetail> queryResult = feeService.queryDetail(queryDef);

    int rowIdx = 0;
    Row row = sheet.createRow(rowIdx++);
    for (int index = 0; index < definition.getColumns().size(); index++) {
      sheet.setColumnWidth(index, 30000);
      Cell cell = row.createCell(index);
      cell.setCellValue(definition.getColumns().get(index).getText());
    }

    CellStyle cellStyle = book.createCellStyle();
    cellStyle.setWrapText(true);
    for (FeeDetail detail : queryResult.getRecords()) {
      row = sheet.createRow(rowIdx++);
      for (int index = 0; index < definition.getColumns().size(); index++) {
        ExportColumnDef columnDef = definition.getColumns().get(index);
        Cell cell = row.createCell(index);
        cell.setCellStyle(cellStyle);
        if ("bizState".equals(columnDef.getDataIndex())) {
          cell.setCellValue(BizStates.getCaption(detail.getBizState()));
        } else if ("store".equals(columnDef.getDataIndex())) {
          cell.setCellValue(detail.getStore().getName() + "[" + detail.getStore().getCode() + "]");
        } else if ("contract".equals(columnDef.getDataIndex())) {
          cell.setCellValue(detail.getContract().getName() + "[" + detail.getContract().getCode()
              + "]");
        } else if ("counterpart".equals(columnDef.getDataIndex())) {
          cell.setCellValue(detail.getCounterpart().getName() + "["
              + detail.getCounterpart().getCode() + "]"
              + ("_Tenant".equals(detail.getCounterpartType()) ? "[商户]" : "[业主]"));
        } else if ("subject".equals(columnDef.getDataIndex())) {
          cell.setCellValue(detail.getSubject().getName() + "[" + detail.getSubject().getCode()
              + "]");
        } else if ("beginDate".equals(columnDef.getDataIndex())) {
          cell.setCellValue(buildDateRangeStr(detail));
        } else if ("total".equals(columnDef.getDataIndex())) {
          cell.setCellValue(detail.getTotal() == null ? 0 : detail.getTotal().doubleValue());
        } else if ("payTotal".equals(columnDef.getDataIndex())) {
          cell.setCellValue(detail.getPayTotal() == null ? 0 : detail.getPayTotal().doubleValue());
        } else if ("unPayTotal".equals(columnDef.getDataIndex())) {
          cell.setCellValue(detail.getUnPayTotal() == null ? 0 : detail.getUnPayTotal()
              .doubleValue());
        }
      }
    }

    for (int index = 0; index < definition.getColumns().size(); index++) {
      sheet.autoSizeColumn(index); // 调整第一列宽度
      sheet.setColumnWidth(index, sheet.getColumnWidth(index) + 1500);// 自动调整的列宽过小，再加点
    }

    WorkbookUtil.writeWorkbook(book, getServletContext().getRealPath(fileURL));
    return fileURL;
  }

  private String buildDateRangeStr(FeeDetail detail) {
    String beginStr = "";
    if (detail.getBeginDate() != null) {
      beginStr = StringUtil.dateToString(detail.getBeginDate(), StringUtil.DATE_FORMATS[4]);
    }

    String endStr = "";
    if (detail.getEndDate() != null) {
      endStr = StringUtil.dateToString(detail.getEndDate(), StringUtil.DATE_FORMATS[4]);
    }

    if (StringUtil.isNullOrBlank(beginStr) && StringUtil.isNullOrBlank(endStr))
      return "";

    return beginStr + "~" + endStr;
  }

  @Override
  protected FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(FeeQueryBuilder.class);
  }

  private FlecsQueryDefinitionBuilder getDetailQueryBuilder() {
    return getAppCtx().getBean(FeeDetailQueryBuilder.class);
  }

  @Override
  protected String getObjectName() {
    return Fees.OBJECT_NAME;
  }

  @Override
  protected String getObjectCaption() {
    return "费用单";
  }

  @Override
  protected String getServiceBeanId() {
    return FeeService.DEFAULT_CONTEXT_ID;
  }

  private boolean isEmpty(FeeLine detail) {
    if (detail.getSubject() != null && detail.getSubject().getUuid() != null) {
      return false;
    } else if (detail.getTotal() != null && detail.getTotal().getTotal() != null
        && BigDecimal.ZERO.compareTo(detail.getTotal().getTotal()) != 0) {
      // 有科目时项目税率带出会自动计算出金额
      return false;
    }

    if (detail.getBeginDate() != null || detail.getEndDate() != null) {
      return false;
    }

    return true;
  }
}
