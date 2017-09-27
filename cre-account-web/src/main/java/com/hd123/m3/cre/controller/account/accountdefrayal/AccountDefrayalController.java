/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountDefrayalController.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月30日 - chenganbang - 创建。
 */
package com.hd123.m3.cre.controller.account.accountdefrayal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.commons.CounterpartType;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.ContractService;
import com.hd123.m3.account.service.invoicetype.InvoiceType;
import com.hd123.m3.account.service.invoicetype.InvoiceTypeService;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.payment.PaymentService;
import com.hd123.m3.account.service.payment.Payments;
import com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayal;
import com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayalService;
import com.hd123.m3.account.service.report.accountdefrayal.AccountDefrayals;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.account.util.DateUtil;
import com.hd123.m3.commons.biz.option.PermOptionService;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.EntityUtil;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.servlet.biz.export.ExportColumnDef;
import com.hd123.m3.commons.servlet.biz.export.ExportDefinition;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.ModuleController;
import com.hd123.m3.commons.servlet.controllers.module.perm.Permission;
import com.hd123.m3.commons.servlet.permed.DataPermedHelper;
import com.hd123.m3.commons.servlet.permed.UserGroup;
import com.hd123.m3.commons.util.impex.poi.WorkbookUtil;
import com.hd123.m3.cre.controller.account.accountdefrayal.builder.AccountDefrayalQueryBuilder;
import com.hd123.m3.cre.controller.account.accountdefrayal.convert.BAccountConverter;
import com.hd123.m3.cre.controller.account.accountdefrayal.convert.BAccountDefrayalConverter;
import com.hd123.m3.cre.controller.account.accountdefrayal.convert.BigdecimalFormat;
import com.hd123.m3.cre.controller.account.accountdefrayal.convert.Date2StrUtil;
import com.hd123.m3.cre.controller.account.accountdefrayal.convert.UCN2StrUtil;
import com.hd123.m3.cre.controller.account.accountdefrayal.model.BAccount;
import com.hd123.m3.cre.controller.account.accountdefrayal.model.BAccountDefrayal;
import com.hd123.m3.cre.controller.account.common.AccountPermResourceDef;
import com.hd123.m3.cre.controller.account.common.util.CommonUtil;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.query.QueryDefinition;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 科目收付情况Controller
 * 
 * @author chenganbang
 *
 */
@Controller
@RequestMapping("account/defrayal/*")
public class AccountDefrayalController extends ModuleController {
  /** 单据类型 */
  private static final String KEY_BILLTYPES = "billTypes";
  /** 发票类型 */
  private static final String KEY_INVOICETYPES = "invoiceTypes";

  @Autowired
  private AccountDefrayalService accountDefrayalService;
  @Autowired
  private StatementService statementService;
  @Autowired
  private PermOptionService permOptionService;
  @Autowired
  private DataPermedHelper dataPermedHelper;
  @Autowired
  private ContractService contractService;
  @Autowired
  private AccountService accountService;
  @Autowired
  private PaymentService paymentService;
  @Autowired
  private InvoiceTypeService invoiceTypeService;

  @Override
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);
    try {
      moduleContext.put(KEY_BILLTYPES, JsonUtil.objectToJson(CommonUtil.getBillTypes()));
      moduleContext.put(KEY_INVOICETYPES, invoiceTypeService.getAllTypes());
    } catch (Exception e) {
      Logger.getLogger(AccountDefrayalController.class).error("buildModuleContext error", e);
    }
  }

  @Override
  protected Set<Permission> getAuthorizedPermissions(IsOperator sessionUser) {
    Set<Permission> permissions = super.getAuthorizedPermissions(sessionUser);
    permissions.addAll(getUserAuthorizedHelper().getAuthorizedPermissions(sessionUser.getId(),
        AccountPermResourceDef.RESOURCE_RECEIPT, AccountPermResourceDef.RESOURCE_ACCOUNTDEFRAYAL));
    return permissions;
  }

  @RequestMapping("queryPayments")
  @ResponseBody
  public List<BAccount> queryPayments(@RequestParam(value = "accId", required = true) String accId)
      throws InterruptedException {
    Thread.sleep(1000);// 延时一秒，让页面有足够时间显示toast
    if (StringUtil.isNullOrBlank(accId)) {
      return new ArrayList<BAccount>();
    }
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addCondition(Accounts.CONDITION_ACCID_EQUALS, accId);
    QueryResult<Account> accounts = accountService.query(definition);
    List<Account> sources = accounts.getRecords();
    List<BAccount> result = new ArrayList<BAccount>(sources.size());
    List<InvoiceType> types = invoiceTypeService.getAllTypes();
    for (Account source : sources) {
      if ("-".equals(source.getAcc2().getPayment().getBillNumber())) {
        continue;
      }
      BAccount bp = BAccountConverter.getInstance().convert(source);
      for (InvoiceType invoiceType : types) {
        if (invoiceType.getCode().equals(bp.getPivcType())) {
          bp.setPivcType(invoiceType.getName());
          break;
        }
      }
      result.add(bp);
    }
    // 取收付款单的日期
    FlecsQueryDefinition definition1 = new FlecsQueryDefinition();
    definition1.addCondition(Payments.CONDITION_ACCID_EQUALS, accId);
    List<Payment> payments = paymentService.query(definition).getRecords();
    for (BAccount account : result) {
      for (Payment payment : payments) {
        if (payment.getBillNumber().equals(account.getBillNumber())) {
          account.setPaymentDate(payment.getPaymentDate());
        }
      }
    }
    return result;
  }

  /**
   * 查询
   * 
   * @param filter
   * @return
   */
  @RequestMapping("query")
  @ResponseBody
  public SummaryQueryResult<BAccountDefrayal> query(@RequestBody QueryFilter filter) {
    SummaryQueryResult<BAccountDefrayal> result = new SummaryQueryResult<BAccountDefrayal>();
    FlecsQueryDefinition definition = buildDefinition(filter, false);
    if (definition == null) {
      return result;
    }
    buildSummary(result, filter);

    definition.setPageSize(filter.getPageSize());
    definition.setPage(filter.getCurrentPage());// 数据库分页从第0页开始

    QueryResult<AccountDefrayal> defrayals = accountDefrayalService.query(definition);
    List<AccountDefrayal> records = defrayals.getRecords();
    if (records.isEmpty()) {
      return result;
    }
    List<BAccountDefrayal> defrayalsList = decorate(records);
    result.setRecords(defrayalsList);
    result.setPageCount(defrayals.getPageCount());
    result.setRecordCount(defrayals.getRecordCount());
    result.setPageSize(defrayals.getPageSize());
    result.setPage(defrayals.getPage());

    return result;
  }

  @RequestMapping("export")
  public @ResponseBody String export(@RequestBody ExportDefinition definition) throws Exception {

    String fileURL = "/static/temp/AccountDefrayal"
        + StringUtil.dateToString(new Date(), "yyyyMdHHmmssSSS") + ".xlsx";

    Workbook book = new XSSFWorkbook();
    Sheet sheet = book.createSheet("Sheet1");

    QueryFilter filter = new QueryFilter();
    filter.setFilter(definition.getFilter());
    filter.setSorts(definition.getSorts());
    FlecsQueryDefinition queryDef = buildDefinition(filter, false);
    QueryResult<AccountDefrayal> defrayals = accountDefrayalService.query(queryDef);

    List<BAccountDefrayal> defrayalsList = decorate(defrayals.getRecords());

    int rowIdx = 0;
    Row row = sheet.createRow(rowIdx++);
    for (int index = 0; index < definition.getColumns().size(); index++) {
      sheet.setColumnWidth(index, 30000);
      Cell cell = row.createCell(index);
      cell.setCellValue(definition.getColumns().get(index).getText());
    }

    CellStyle cellStyle = book.createCellStyle();
    cellStyle.setWrapText(true);
    for (BAccountDefrayal defrayal : defrayalsList) {
      row = sheet.createRow(rowIdx++);
      for (int index = 0; index < definition.getColumns().size(); index++) {
        ExportColumnDef columnDef = definition.getColumns().get(index);
        Cell cell = row.createCell(index);
        cell.setCellStyle(cellStyle);
        String colName = columnDef.getDataIndex();
        if ("row".equals(colName)) {
          cell.setCellValue(defrayal.getLight() == 1 ? "绿灯" : "红灯");
        } else if ("settleState".equals(colName)) {
          cell.setCellValue(defrayal.getSettleState());
        } else if ("bizType".equals(colName)) {
          cell.setCellValue(UCN2StrUtil.getInstance().toFriendString(defrayal.getBizType()));
        } else if ("contract".equals(colName)) {
          cell.setCellValue(UCN2StrUtil.getInstance().toFriendString(defrayal.getContract()));
        } else if ("counterpart".equals(colName)) {
          cell.setCellValue(UCN2StrUtil.getInstance().toFriendString(defrayal.getCounterpart())
              + (CounterpartType.TENANT.equals(defrayal.getCounterpartType()) ? "[商户]"
                  : CounterpartType.PROPRIETOR.equals(defrayal.getCounterpartType()) ? "[业主]" : ""));
        } else if ("position".equals(colName)) {
          cell.setCellValue(defrayal.getPosition());
        } else if ("subject".equals(colName)) {
          cell.setCellValue(UCN2StrUtil.getInstance().toFriendString(defrayal.getSubject()));
        } else if ("direction".equals(colName)) {
          cell.setCellValue(defrayal.getDirection() == 1 ? "收"
              : defrayal.getDirection() == -1 ? "付" : "");
        } else if ("settlement".equals(colName)) {
          cell.setCellValue(defrayal.getSettlement());
        } else if ("needSettle".equals(colName)) {
          cell.setCellValue(BigdecimalFormat.getInstance().format(defrayal.getNeedSettle()));
        } else if ("settled".equals(colName)) {
          cell.setCellValue(BigdecimalFormat.getInstance().format(defrayal.getSettled()));
        } else if ("owedAmount".equals(colName)) {
          cell.setCellValue(BigdecimalFormat.getInstance().format(defrayal.getOwedAmount()));
        } else if ("invoiced".equals(colName)) {
          cell.setCellValue(BigdecimalFormat.getInstance().format(defrayal.getInvoiced()));
        } else if ("ivcCode".equals(colName)) {
          cell.setCellValue(defrayal.getIvcNumber());
        } else if ("settleNo".equals(colName)) {
          cell.setCellValue(defrayal.getSettleNo());
        } else if ("lastPayDate".equals(colName)) {
          cell.setCellValue(Date2StrUtil.getInstance().convert(defrayal.getLastPayDate()));
        } else if ("coopMode".equals(colName)) {
          cell.setCellValue(defrayal.getCoopMode());
        } else if ("srcBill".equals(colName)) {
          if (defrayal.getSrcBill() == null) {
            cell.setCellValue("");
          } else {
            List<BillType> types = CommonUtil.getBillTypes();
            for (BillType type : types) {
              if (type.getName().equals(defrayal.getSrcBill().getBillType())) {
                cell.setCellValue(type.getCaption() + "[" + defrayal.getSrcBill().getBillNumber()
                    + "]");
                break;
              }
            }
          }
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

  private List<BAccountDefrayal> decorate(List<AccountDefrayal> records) {
    List<BAccountDefrayal> defrayalsList = new ArrayList<BAccountDefrayal>(records.size());
    List<String> contractUuids = new ArrayList<String>(records.size());
    List<String> acc1Ids = new ArrayList<String>(records.size());
    for (AccountDefrayal accountDefrayal : records) {
      BAccountDefrayal defrayal = BAccountDefrayalConverter.getInstance().convert(accountDefrayal);
      // 初始化状态
      initSettleState(defrayal, accountDefrayal);
      // 初始化亮灯情况
      initLight(defrayal, accountDefrayal);

      contractUuids.add(accountDefrayal.getAcc1().getContract().getUuid());
      acc1Ids.add(accountDefrayal.getAcc1().getId());

      defrayalsList.add(defrayal);
    }
    // 初始化账务合同相关信息
    initContractInfo(contractUuids, defrayalsList);
    // 初始化发票代码
    initIvcCode(acc1Ids, defrayalsList);
    // 查账单
    queryStatement(defrayalsList);
    isLocked(defrayalsList, acc1Ids);
    return defrayalsList;
  }

  /**
   * 是否被锁定
   * 
   * @param defrayalsList
   * @param acc1Ids
   */
  private void isLocked(List<BAccountDefrayal> defrayalsList, List<String> acc1Ids) {
    if (acc1Ids.isEmpty()) {
      return;
    }

    QueryDefinition definition = new QueryDefinition();
    definition.addCondition(Accounts.CONDITION_ACCID_IN, acc1Ids.toArray());
    definition.addCondition(Accounts.CONDITION_CAN_PAYMENT);
    definition.addCondition(Accounts.CONDITION_ACTIVE);

    List<Account> accounts = accountService.query(definition).getRecords();

    Map<String, Account> cache = new HashMap<String, Account>();
    for (Account account : accounts) {
      cache.put(account.getAcc1().getId(), account);
    }
    for (BAccountDefrayal defrayal : defrayalsList) {
      if (!StringUtil.isNullOrBlank(defrayal.getUuid())) {
        defrayal.setLocked(cache.get(defrayal.getUuid()) == null);
      }
    }
  }

  /**
   * 初始化账务合同相关信息
   * 
   * @param contractUuids
   * @param defrayals
   */
  private void initContractInfo(List<String> contractUuids, List<BAccountDefrayal> defrayals) {
    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addCondition(com.hd123.m3.account.service.contract.Contracts.CONDITION_ID_IN,
        contractUuids.toArray());
    List<Contract> contracts = contractService.query(definition,
        com.hd123.m3.account.service.contract.Contracts.FETCH_POSITIONS).getRecords();
    for (BAccountDefrayal defrayal : defrayals) {
      for (Contract contract : contracts) {
        if (defrayal.getContract().getUuid().equals(contract.getUuid())) {
          defrayal.setCoopMode(contract.getCoopMode());
          defrayal.setBizType(contract.getBizType());
          defrayal.setPosition(contract.getPositions());
        }
      }
    }
  }

  /**
   * 查账单
   * 
   * @param defrayalsList
   * @return
   */
  private void queryStatement(List<BAccountDefrayal> defrayalsList) {
    Map<String, List<BAccountDefrayal>> defrayalsMap = new HashMap<String, List<BAccountDefrayal>>();
    for (BAccountDefrayal defrayal : defrayalsList) {
      if (Accounts.NONE_BILL_UUID.equals(defrayal.getStatement().getBillUuid())) {
        continue;
      }
      List<BAccountDefrayal> defrayalList = defrayalsMap.get(defrayal.getStatement().getBillUuid());
      if (defrayalList == null) {
        defrayalList = new ArrayList<BAccountDefrayal>();
        defrayalsMap.put(defrayal.getStatement().getBillUuid(), defrayalList);
      }
      defrayalList.add(defrayal);
    }

    if (defrayalsMap.isEmpty()) {
      return;
    }

    FlecsQueryDefinition statementQueryDef = new FlecsQueryDefinition();
    statementQueryDef.addCondition(Statements.CONDITION_UUID_IN, defrayalsMap.keySet().toArray());
    List<Statement> statements = statementService.query(statementQueryDef).getRecords();
    for (Statement statement : statements) {
      List<BAccountDefrayal> defrayalList = defrayalsMap.get(statement.getUuid());
      for (BAccountDefrayal defrayal : defrayalList) {
        defrayal.setSettleNo(statement.getSettleNo());
      }
    }
  }

  /**
   * 判断状态
   * 
   * @param defrayal
   * @param accountDefrayal
   */
  private void initSettleState(BAccountDefrayal defrayal, AccountDefrayal accountDefrayal) {
    if (accountDefrayal
        .getNeedSettle()
        .getTotal()
        .compareTo(
            accountDefrayal.getSettled().getTotal()
                .subtract(accountDefrayal.getSettleAdj().getTotal())) == 0) {
      defrayal.setSettleState("已结款");
    } else if (accountDefrayal.getSettled().getTotal().compareTo(BigDecimal.ZERO) == 0) {
      defrayal.setSettleState("未结款");
    } else if (accountDefrayal.getSettled().getTotal().compareTo(BigDecimal.ZERO) != 0
        && accountDefrayal
            .getNeedSettle()
            .getTotal()
            .compareTo(
                accountDefrayal.getSettled().getTotal()
                    .subtract(accountDefrayal.getSettleAdj().getTotal())) != 0) {
      defrayal.setSettleState("部分结款");
    }
  }

  /**
   * 判断亮灯情况
   * 
   * @param defrayal
   * @param accountDefrayal
   */
  private void initLight(BAccountDefrayal defrayal, AccountDefrayal accountDefrayal) {
    Date now = new Date();
    if (!"已结款".equals(defrayal.getSettleState())
        && defrayal.getLastPayDate() != null
        && DateUtil.beginOfTheDate(now).getTime() > DateUtil.beginOfTheDate(
            defrayal.getLastPayDate()).getTime()) {
      defrayal.setLight(-1);
    } else {
      defrayal.setLight(1);
    }
  }

  /**
   * 初始化发票代码
   * 
   * @param acc1Ids
   * @param defrayals
   */
  private void initIvcCode(List<String> acc1Ids, List<BAccountDefrayal> defrayals) {
    FlecsQueryDefinition def = new FlecsQueryDefinition();
    def.addCondition(Accounts.CONDITION_ACCID_IN, acc1Ids.toArray());
    List<Account> acconts = accountService.query(def).getRecords();

    List<String> ivcNumbers = new ArrayList<String>();

    for (BAccountDefrayal defrayal : defrayals) {
      ivcNumbers.clear();
      for (int i = 0; i < acconts.size(); i++) {
        Account account = acconts.get(i);
        if ("-".equals(account.getAcc2().getPayment().getBillNumber())) {
          continue;
        }
        String number = account.getAcc2().getInvoice().getInvoiceNumber();
        if (!"-".equals(number) && account.getAcc1().getId().equals(defrayal.getUuid())) {
          ivcNumbers.add(number);
        }
      }
      defrayal.setIvcNumber("");
      if (ivcNumbers.size() == 1) {
        defrayal.setIvcNumber(ivcNumbers.get(0));
      } else {
        for (int j = 0; j < ivcNumbers.size(); j++) {
          defrayal.setIvcNumber(defrayal.getIvcNumber()
              + (j == ivcNumbers.size() - 1 ? ivcNumbers.get(j) : ivcNumbers.get(j) + ","));
        }
      }
    }
  }

  /**
   * 构建查询定义
   * 
   * @param filter
   * @param ignoreSettleState
   * @return
   */
  private FlecsQueryDefinition buildDefinition(QueryFilter filter, boolean ignoreSettleState) {
    FlecsQueryDefinition definition = AccountDefrayalQueryBuilder.getInstance().buildDefinition(
        filter, ignoreSettleState);

    // 启用授权组
    List<UserGroup> userGroups = dataPermedHelper.getUserGroups(getSessionUserId());
    definition.addCondition(AccountDefrayals.CONDITION_PERM_CONTRACT_GROUP_ID_IN, EntityUtil
        .toUuids(userGroups).toArray());

    // 受项目限制
    List<String> storeList = getUserStores(getSessionUser().getId());
    if (storeList.isEmpty()) {
      return null;
    }
    definition.addCondition(AccountDefrayals.CONDITION_ACCOUNTUNIT_IN, storeList.toArray());
    if (!ignoreSettleState) {
      AccountDefrayalQueryBuilder.getInstance().buildSort(definition, filter);
    }
    return definition;
  }

  /**
   * 计数
   * 
   * @param result
   * @param filter
   */
  private void buildSummary(SummaryQueryResult result, QueryFilter filter) {
    FlecsQueryDefinition definition = buildDefinition(filter, true);
    if (definition == null) {
      result.getSummary().put("all", 0);
      result.getSummary().put("unsettled", 0);
      result.getSummary().put("partSettled", 0);
      result.getSummary().put("settled", 0);
      return;
    }
    // 不限状态
    result.getSummary().put("all", accountDefrayalService.query(definition).getRecordCount());
    // 未结款
    FlecsQueryDefinition unsettledDef = buildDefinition(filter, true);
    unsettledDef.addCondition(AccountDefrayals.CONDITION_UNSETTLED, true);
    result.getSummary().put("unsettled",
        accountDefrayalService.query(unsettledDef).getRecordCount());
    // 部分借款
    FlecsQueryDefinition partSettledDef = buildDefinition(filter, true);
    partSettledDef.addCondition(AccountDefrayals.CONDITION_PARTSETTLED, true);
    result.getSummary().put("partSettled",
        accountDefrayalService.query(partSettledDef).getRecordCount());
    // 已结款
    FlecsQueryDefinition settledDef = buildDefinition(filter, true);
    settledDef.addCondition(AccountDefrayals.CONDITION_SETTLED, true);
    result.getSummary().put("settled", accountDefrayalService.query(settledDef).getRecordCount());
  }

}
