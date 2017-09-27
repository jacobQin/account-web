/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	InportFromStatementImportProcessor.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月7日 - renjingzhan - 创建。
 */
package com.hd123.m3.cre.controller.account.invoice.reg.batchInput;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.payment.Payment;
import com.hd123.m3.account.service.payment.PaymentService;
import com.hd123.m3.account.service.payment.Payments;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.query.QueryDefinitionUtil;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.util.impex.poi.WorkbookUtil;
import com.hd123.rumba.commons.biz.query.QueryResult;

/**
 * @author renjingzhan
 *
 */
@Component
public class InvoiceRegImportProcessor {

  public static final String COL_BILLNUMBER = "单号";
  public static final String COL_INVOICETYPE = "发票类型";
  public static final String COL_INVOICECODE = "发票代码";
  public static final String COL_INVOICENUMBER = "发票号码";
  public static final String COL_REGDATE = "开票日期";
  public static final String COL_REMARK = "说明";

  /** 代码正则表达式 */
  private static final String CODE_REGEXP = "^[\\w\\.\\-\\+]+$";
  /** 代码正则提示信息 */
  private static final String CODE_REGMSG = "只能包含字母、数字、\"_\"、\".\"、\"-\"、\"+\"。";

  
  @Autowired
  private AccountService accountService;
  @Autowired
  private StatementService statementService;
  @Autowired
  private PaymentService paymentService;

  public BInvoiceRegBatchInput importFile(String filePath, String type, String userId)
      throws Exception {
    BInvoiceRegBatchInput result = new BInvoiceRegBatchInput();
    List<String> errors = new ArrayList();
    List<BIvcRegLine> bIvcRegLines = new ArrayList<BIvcRegLine>();

    Workbook book = WorkbookUtil.getWorkbook(filePath);
    Sheet sheet = book.getSheetAt(0);
    List<String> billNumbers = new ArrayList<String>();
    List<String> invoicetypes = new ArrayList<String>();
    List<String> invoicecodes = new ArrayList<String>();
    List<String> invoicenumbers = new ArrayList<String>();
    List<Date> regdates = new ArrayList<Date>();
    List<String> remarks = new ArrayList<String>();

    for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
      Row row = sheet.getRow(rowIdx);
      if (WorkbookUtil.isRowEmpty(row))
        continue;
      try {
        billNumbers.add(WorkbookUtil.getStringCellValue(row, COL_BILLNUMBER, false));
        invoicetypes.add(WorkbookUtil.getStringCellValue(row, COL_INVOICETYPE, false));
        invoicecodes.add(WorkbookUtil.getStringCellValue(row, COL_INVOICECODE, false, 0, 32,CODE_REGEXP,CODE_REGMSG));
        invoicenumbers.add(WorkbookUtil.getStringCellValue(row, COL_INVOICENUMBER, false, 0, 32,CODE_REGEXP,CODE_REGMSG));
        regdates.add(WorkbookUtil.getDateCellValue(row, COL_REGDATE, false));
        remarks.add(WorkbookUtil.getStringCellValue(row, COL_REMARK, true, 0, 128));
      } catch (Exception e) {
        errors.add("第" + rowIdx + "行：" + e.getMessage());
      }
    }
    if (errors.isEmpty() == false)
      throw new Exception(JsonUtil.objectToJson(errors));
    // 检查单号是否重复
    checkBillNumbersIsDuplicate(billNumbers, errors);
    if (errors.isEmpty() == false)
      throw new Exception(JsonUtil.objectToJson(errors));

    // 检查发票类型是否否和要求
    checkInvoiceTypeIsInSystem(invoicetypes, errors);
    if (errors.isEmpty() == false)
      throw new Exception(JsonUtil.objectToJson(errors));

    // 检查单号是否存在并赋值
    List<BIvcRegLine> lines = safeCheckBills(type, billNumbers, errors, bIvcRegLines);
    if (errors.isEmpty() == false)
      throw new Exception(JsonUtil.objectToJson(errors));
    if (type.equals("statement")) {
      for (BIvcRegLine bIvcRegLine : lines) {
        for (int i = 0; i < billNumbers.size(); i++) {
          if (bIvcRegLine.getAcc2().getStatement().getBillNumber().equals(billNumbers.get(i))) {
            bIvcRegLine.setInvoiceCode(invoicecodes.get(i));
            bIvcRegLine.setInvoiceNumber(invoicenumbers.get(i));
            bIvcRegLine.setInvoiceType(invoicetypes.get(i));
            bIvcRegLine.setRegDate(regdates.get(i));
            bIvcRegLine.setRemark(remarks.get(i));
          }
        }
      }
    } else {
      for (BIvcRegLine bIvcRegLine : lines) {
        for (int i = 0; i < billNumbers.size(); i++) {
          if (bIvcRegLine.getAcc2().getPayment().getBillNumber().equals(billNumbers.get(i))) {
            bIvcRegLine.setInvoiceCode(invoicecodes.get(i));
            bIvcRegLine.setInvoiceNumber(invoicenumbers.get(i));
            bIvcRegLine.setInvoiceType(invoicetypes.get(i));
            bIvcRegLine.setRegDate(regdates.get(i));
            bIvcRegLine.setRemark(remarks.get(i));
          }
        }
      }
    }

    result.setRegLines(lines);
    return result;
  }

  // 检查发票类型在系统中是否定义
  private void checkInvoiceTypeIsInSystem(List<String> invoicetypes, List<String> errors) {
    for (int i = 0; i < invoicetypes.size(); i++) {
      try {
        boolean flag = false;
        if (invoicetypes.get(i).equals("普通发票") || invoicetypes.get(i).equals("凭据")
            || invoicetypes.get(i).equals("增值税发票")) {
          flag = true;
        }
        if (flag == false) {
          throw new Exception("发票类型在系统中没有定义");
        }
      } catch (Exception e) {
        errors.add("第" + (i + 1) + "行：" + e.getMessage());
      }
    }
  }

  // 检查单号是否重复
  private void checkBillNumbersIsDuplicate(List<String> billNumbers, List<String> errors) {
    for (int i = 0; i < billNumbers.size(); i++) {
      for (int j = i + 1; j < billNumbers.size(); j++) {
        try {
          if (billNumbers.get(i).equals(billNumbers.get(j))) {
            throw new Exception("单号重复");
          }
        } catch (Exception e) {
          errors.add("第" + (i + 1) + "行与第" + (j + 1) + "行" + e.getMessage());
        }
      }
    }
  }

  // 检查单据安全性并赋值
  private List<BIvcRegLine> safeCheckBills(String type, List<String> billNumbers,
      List<String> errors, List<BIvcRegLine> bIvcRegLines) throws RuntimeException, Exception {
    List<BIvcRegLine> result = new ArrayList<BIvcRegLine>();
    if (type.equals("statement")) {
      // 查询账单信息
      List<Statement> statements = queryStatementInfo(billNumbers);

      // 检查账单单号是否不存在
      List<String> uuids = checkStatementBillNumberIsExist(billNumbers, statements, errors);
      if (errors.isEmpty() == false)
        throw new Exception(JsonUtil.objectToJson(errors));

      // 如果单号都存在，查询账款信息
      QueryResult<Account> accounts = queryAccountsByStatementUuids(uuids);

      // 判断账单单号对应账款存在
      checkAccountFromStatementBillNumberIsExist(billNumbers, accounts, errors);
      if (errors.isEmpty() == false)
        throw new Exception(JsonUtil.objectToJson(errors));

      // 给账款lines赋值赋值
      result = assignBIvcRegLineByAccounts(result, accounts);

    } else if (type.equals("payment")) {

      // 查询付款单信息
      List<Payment> payments = queryPaymentInfo(billNumbers);

      // 检查付款单号单号是否不存在
      List<String> uuids = checkPaymentBillNumberIsExist(billNumbers, payments, errors);
      if (errors.isEmpty() == false)
        throw new Exception(JsonUtil.objectToJson(errors));

      // 如果单号都存在，查询付款单信息
      QueryResult<Account> accounts = queryAccountsByPaymentUuids(uuids);

      // 判断付款单号对应账款是否存在
      checkAccountFromPaymentBillNumberIsExist(billNumbers, accounts, errors);
      if (errors.isEmpty() == false)
        throw new Exception(JsonUtil.objectToJson(errors));

      // 给账款lines赋值赋值
      result = assignBIvcRegLineByAccounts(result, accounts);
    }
    return result;
  }

  // 判断付款单号对应账款是否存在
  private void checkAccountFromPaymentBillNumberIsExist(List<String> billNumbers,
      QueryResult<Account> accounts, List<String> errors) {
    for (int i = 0; i < billNumbers.size(); i++) {
      try {
        boolean flag = false;
        for (Account account : accounts.getRecords()) {
          if (account.getAcc2().getPayment().getBillNumber().equals(billNumbers.get(i))) {
            flag = true;
          }
        }
        if (flag == false) {
          throw new Exception("不存在可登记的账款或者账款被锁定");
        }
      } catch (Exception e) {
        errors.add("第" + (i + 1) + "行" + e.getMessage());
      }
    }
  }

  // 如果单号都存在，查询付款单信息
  private QueryResult<Account> queryAccountsByPaymentUuids(List<String> uuids) {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_ACTIVE);
    queryDef.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_PAYMENT_UUID_IN,
            uuids.toArray()));

    queryDef.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_CAN_INVOICE));

    queryDef.getConditions().add(
        QueryDefinitionUtil.createQueryCondition(Accounts.CONDITION_DIRECTION_EQUALS, 1));
    queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_BILL_UUID);
    return accountService.query(queryDef);
  }

  // 检查付款单号单号是否不存在
  private List<String> checkPaymentBillNumberIsExist(List<String> billNumbers,
      List<Payment> payments, List<String> errors) {
    List<String> uuids = new ArrayList<String>();

    for (int i = 0; i < billNumbers.size(); i++) {
      try {
        boolean flag = false;
        for (Payment payment : payments) {
          if (payment.getBillNumber().equals(billNumbers.get(i))) {
            flag = true;
            uuids.add(payment.getUuid());
          }
        }
        if (flag == false) {
          throw new Exception("对应的单据不存在");
        }
      } catch (Exception e) {
        errors.add("第" + (i + 1) + "行单号(" + billNumbers.get(i) + ")" + e.getMessage());
      }
    }
    return uuids;
  }

  // 查询付款单信息
  private List<Payment> queryPaymentInfo(List<String> billNumbers) {
    FlecsQueryDefinition paymentDef = new FlecsQueryDefinition();
    paymentDef.addCondition(Payments.CONDITION_BILLNUMBER_IN, billNumbers.toArray());
    paymentDef.addCondition(Payments.CONDITION_BIZSTATE_NOTEQUALS, BizStates.ABORTED);
    paymentDef.addCondition(Payments.CONDITION_DIRECTION_EQUALS, 1);
    return paymentService.query(paymentDef).getRecords();
  }

  // 给账款lines赋值赋值
  private List<BIvcRegLine> assignBIvcRegLineByAccounts(List<BIvcRegLine> result,
      QueryResult<Account> accounts) {
    for (Account account : accounts.getRecords()) {
      BIvcRegLine line = new BIvcRegLine();
      line.setAcc1(account.getAcc1());
      line.setAcc2(account.getAcc2());
      line.setOriginTotal(account.getOriginTotal());
      line.setTotal(account.getTotal());
      line.setUnregTotal(account.getTotal());
      result.add(line);
    }
    return result;
  }

  // 如果单号都存在，查询账款信息
  private QueryResult<Account> queryAccountsByStatementUuids(List<String> uuids) {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
    queryDef.addCondition(Accounts.CONDITION_ACTIVE);
    queryDef.addCondition(Accounts.CONDITION_STATEMENT_UUID_IN, uuids.toArray());
    queryDef.addCondition(Accounts.CONDITION_CAN_INVOICE);
    queryDef.addCondition(Accounts.CONDITION_DIRECTION_EQUALS, 1);
    queryDef.addCondition(Accounts.CONDITION_LOCKER_UUID_EQUALS, Accounts.NONE_BILL_UUID);
    return accountService.query(queryDef);
  }

  // 查询账单信息
  private List<Statement> queryStatementInfo(List<String> billNumbers) {
    FlecsQueryDefinition statementDef = new FlecsQueryDefinition();
    statementDef.addCondition(Statements.CONDITION_BILLNUMBER_IN, billNumbers.toArray());
    statementDef.addCondition(Statements.CONDITION_BIZSTATE_NOT_EQUALS, BizStates.ABORTED);

    return statementService.query(statementDef).getRecords();
  }

  // 检查账单单号是否不存在
  private List<String> checkStatementBillNumberIsExist(List<String> billNumbers,
      List<Statement> statements, List<String> errors) {
    List<String> uuids = new ArrayList<String>();

    for (int i = 0; i < billNumbers.size(); i++) {
      try {
        boolean flag = false;
        for (Statement statement : statements) {
          if (statement.getBillNumber().equals(billNumbers.get(i))) {
            flag = true;
            uuids.add(statement.getUuid());
          }
        }
        if (flag == false) {
          throw new Exception("对应的单据不存在");
        }
      } catch (Exception e) {
        errors.add("第" + (i + 1) + "行单号(" + billNumbers.get(i) + ")" + e.getMessage());
      }
    }
    return uuids;
  }

  // 判断账单单号对应账款存在
  private void checkAccountFromStatementBillNumberIsExist(List<String> billNumbers,
      QueryResult<Account> accounts, List<String> errors) {
    for (int i = 0; i < billNumbers.size(); i++) {
      try {
        boolean flag = false;
        for (Account account : accounts.getRecords()) {
          if (account.getAcc2().getStatement().getBillNumber().equals(billNumbers.get(i))) {
            flag = true;
          }
        }
        if (flag == false) {
          throw new Exception("不存在可登记的账款或者账款被锁定");
        }
      } catch (Exception e) {
        errors.add("第" + (i + 1) + "行" + e.getMessage());
      }
    }
  }
}
