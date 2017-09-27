/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	EmailSenderService.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月10日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.email;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.ContractService;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.statement.StatementService;
import com.hd123.m3.account.service.statement.StatementSubjectAccount;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.SubjectService;
import com.hd123.m3.account.service.subject.Subjects;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.cre.controller.account.statement.convert.BStatementConverter;
import com.hd123.m3.cre.controller.account.statement.email.creator.ShiMaoEmailWordCreator;
import com.hd123.m3.cre.controller.account.statement.model.BStatement;
import com.hd123.m3.cre.controller.account.statement.model.BStatementDetail;
import com.hd123.m3.investment.service.tenant.tenant.Tenant;
import com.hd123.m3.investment.service.tenant.tenant.TenantService;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author mengyinkun
 * 
 */
@Component
public class EmailSenderService {

  @Autowired
  private SubjectService subjectService;
  @Autowired
  private TenantService tenantService;
  @Autowired
  private StatementService statementService;
  @Autowired
  private ContractService contractService;
  private ShiMaoEmailWordCreator emailWordCreator = new ShiMaoEmailWordCreator();

  /**
   * 
   * @param billNumber
   * @return
   * @throws Exception
   */
  public BStatement loadByBillNumber(String billNumber) throws Exception {
    BStatement result = new BStatement();
    Statement statement = statementService.getByNumber(billNumber, Statement.FETCH_PART_WHOLE);
    if (statement != null) {
      result = BStatementConverter.getInstance().convert(statement, true);
    }
    return result;
  }

  /**
   * 取得科目uuid
   * 
   * @param subjectUuids
   * @return
   */
  private Map<String, Subject> getSubjectByUuids(List<String> subjectUuids) {

    FlecsQueryDefinition definition = new FlecsQueryDefinition();
    definition.addCondition(Subjects.CONDITION_UUID_IN, subjectUuids.toArray());
    QueryResult<Subject> query = subjectService.query(definition, Subject.PART_USAGE);
    Map<String, Subject> map = new HashMap<String, Subject>();
    for (Subject subject : query.getRecords()) {
      map.put(subject.getUuid(), subject);
    }
    return map;
  }

  private List<String> getSubjectUuid(String billNumber) throws Exception {
    BStatement statement = loadByBillNumber(billNumber);
    List<BStatementDetail> details = statement.getDetails();
    List<String> subjectUuids = new ArrayList<String>();
    for (BStatementDetail bStatementDetail : details) {
      subjectUuids.add(bStatementDetail.getSubject().getUuid());
    }
    return subjectUuids;
  }

  /**
   * 取到商户对象
   * 
   * @param uuid
   * @return
   */
  private Tenant getTenant(String uuid) {
    if (StringUtil.isNullOrBlank(uuid)) {
      return new Tenant();
    }
    Tenant tenant = tenantService.get(uuid);
    return tenant == null ? null : tenant;
  }

  /**
   * 取得铺位号
   * 
   * @param uuid
   * @return
   */
  private String getPositions(String uuid) {
    if (StringUtil.isNullOrBlank(uuid)) {
      return null;
    }
    Contract contract = contractService.get(uuid, Contracts.FETCH_POSITIONS);
    if (contract != null) {
      StringBuilder sb = new StringBuilder();
      List<UCN> positions = contract.getPositions();
      for (int i = 0; i < positions.size(); i++) {
        if (i != positions.size() - 1) {
          sb.append(positions.get(i).getCode() + ",");
        }
        sb.append(positions.get(i).getCode());
      }
      return sb.toString();
    } else {
      return null;
    }

  }

  /**
   * 取得已结算金额
   * 
   * @param uuid
   * @return
   */
  private Map<String, BigDecimal> getReceipted(String uuid) {
    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    List<StatementSubjectAccount> accounts = statementService.getStatementAccount(uuid);
    for (int i = 0; i <= accounts.size() - 1; i++) {
      StatementSubjectAccount account = accounts.get(i);
      String subject = getSubject(account.getSubject()).getName();
      BigDecimal receipted = new BigDecimal(0);
      if(account.getDirection()==Direction.PAYMENT){
        receipted=account.getSettled().getTotal().negate();
      }else{
        receipted=account.getSettled().getTotal();
      }
      map.put(subject, receipted);
    }
    return map;
  }

  /**
   * 根据uuid取得科目
   * 
   * @param uuid
   * @return
   */
  private Subject getSubject(String uuid) {
    return subjectService.get(uuid, Subject.PART_USAGE);
  }

  /**
   * 发送邮件前的准备工作，将word-->pdf
   * 
   * @param billNumber
   *          账单号
   * @param modelFile
   *          模板文件路径
   * @param descFile
   *          目标文件 路径
   * @param tableIndex
   *          表格索引
   * @param descPdf
   *          目标pdf路径
   * @throws Exception
   */
  public void beforeSendEmail(String billNumber, String modelFile, String descFile, int tableIndex,
      String descPdf) throws Exception {
    BStatement statement = loadByBillNumber(billNumber);
    List<String> subjectUuids = getSubjectUuid(billNumber);
    Map<String, Subject> subjects = getSubjectByUuids(subjectUuids);
    Tenant tenant = getTenant(statement.getCounterpart().getUuid());
    String positions = getPositions(statement.getContract().getUuid());
    Map<String, BigDecimal> receiptedMap = getReceipted(statement.getUuid());

    emailWordCreator.workWithDocument(subjects, tenant, positions, statement, billNumber,
        modelFile, descFile, tableIndex, receiptedMap, descPdf);
  }

}
