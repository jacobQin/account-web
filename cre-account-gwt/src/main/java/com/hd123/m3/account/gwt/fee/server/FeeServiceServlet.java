/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeServiceImpl.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-16 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.fee.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.ClientException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alex.csvhelper.CsvWriter;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.gwt.base.server.AccBPMServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.RoundingMode;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectUsage;
import com.hd123.m3.account.gwt.cpnts.server.converter.ContractBizConverter;
import com.hd123.m3.account.gwt.fee.client.biz.BAccSettle;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeLine;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeTemplate;
import com.hd123.m3.account.gwt.fee.client.rpc.FeeService;
import com.hd123.m3.account.gwt.fee.intf.client.perm.FeePermDef;
import com.hd123.m3.account.gwt.fee.server.exp.CSVFeeExportItem;
import com.hd123.m3.account.gwt.payment.rec.intf.client.perm.ReceiptPermDef;
import com.hd123.m3.account.gwt.util.server.SubjectUsageUtils;
import com.hd123.m3.account.gwt.widget.server.UploadConfig;
import com.hd123.m3.account.rs.service.settle.AccSettle;
import com.hd123.m3.account.service.acc.Account;
import com.hd123.m3.account.service.acc.AccountService;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.m3.account.service.contract.Contract;
import com.hd123.m3.account.service.contract.Contracts;
import com.hd123.m3.account.service.fee.Fee;
import com.hd123.m3.account.service.fee.FeeJobService;
import com.hd123.m3.account.service.fee.Fees;
import com.hd123.m3.account.service.statement.Statement;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.entity.BizActions;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.query.QueryDefinitionUtil;
import com.hd123.m3.commons.gwt.bpm.client.BpmConstants;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.gwt.widget.client.biz.BImpParams;
import com.hd123.m3.commons.gwt.widget.client.ui.imp.ImpexConstants;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryOrderDirection;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtils;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.quartz.gwt.client.rpc.BJobScheduleHandler;
import com.hd123.rumba.quartz.gwt.server.rpc.BJobScheduleHandlerConverter;
import com.hd123.rumba.quartz.service.JobScheduleHandler;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author subinzhu
 * 
 */
public class FeeServiceServlet extends AccBPMServiceServlet implements FeeService {
  private static final long serialVersionUID = 300200L;

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        FeePermDef.RESOURCE_FEE_SET);
    Set<BPermission> receiptPermissions = helper.getAuthorizedPermissions(
        sessionUser.getQualifiedId(), ReceiptPermDef.RESOURCE_RECEIPT_SET);
    permissions.addAll(receiptPermissions);
    return permissions;
  }

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    try {
      moduleContext.put(SCALE, getScale());
      moduleContext.put(ROUNDING_MODE, getRoundingMode());
      moduleContext.put(EXPORTDIR, UploadConfig.getInstance().getFolderRoot());
      moduleContext.put(DOWANLOAD_URL, "/static/import/fee."
          + UploadConfig.getInstance().getDownloadSuffix());
      moduleContext.put(KEY_COOPMODES, CollectionUtil.toString(getAllEnabledCoopModes()));
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public RPageData<BFee> query(List<Condition> conditions, PageSort pageSort) throws Exception {
    try {

      FlecsQueryDefinition def = buildQueryDefinition(conditions, pageSort);
      if (def == null)
        return new RPageData<BFee>();

      QueryResult<Fee> queryResult = getFeeService().query(def);

      RPageData<BFee> pd = new RPageData<BFee>();
      RPageDataUtil.copyPagingInfo(queryResult, pd);
      pd.setValues(ConverterUtil.convert(queryResult.getRecords(), new FeeBizConverter(true)));

      // 更新导航数据
      updateNaviEntities(getObjectName(), pd.getValues());
      return pd;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions, PageSort pageSort)
      throws ClientBizException {
    FlecsQueryDefinition queryDef = buildQueryDefinition(conditions, pageSort.getSortOrders());
    if (queryDef == null)
      return null;

    queryDef.setPage(pageSort.getPage());
    queryDef.setPageSize(pageSort.getPageSize());
    return queryDef;
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions,
      List<Order> sortOrders) throws ClientBizException {
    FlecsQueryDefinition queryDef = buildDefaultQueryDefinition();
    if (queryDef == null)
      return null;
    queryDef.getFlecsConditions().addAll(
        FeeQueryBuilder.getInstance().buildFlecsConditions(conditions));
    queryDef.setOrders(FeeQueryBuilder.getInstance().buildQueryOrders(sortOrders));
    return queryDef;
  }

  private FlecsQueryDefinition buildDefaultQueryDefinition() throws ClientBizException {
    FlecsQueryDefinition queryDefinition = new FlecsQueryDefinition();
    queryDefinition.getConditions().addAll(getPermConditions());

    return queryDefinition;
  }

  @Override
  public BFee load(String uuid) throws Exception {
    try {
      if (uuid == null)
        return null;

      Fee bill = getFeeService().get(uuid, Fees.FETCH_LINES);
      if (bill == null)
        return null;

      FeeBizConverter converter = new FeeBizConverter(true);
      BFee result = converter.convert(bill);
      if (BizStates.EFFECT.equals(result.getBizState())) {
        result.setHasAccounts(hasAccount(result.getUuid()));
      }
      validEntityPerm(result);

      fetchContract(result);
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BFee loadByNumber(String number) throws Exception {
    try {
      if (number == null)
        return null;

      Fee bill = getFeeService().getByNumber(number, Fees.FETCH_LINES);
      if (bill == null)
        return null;

      FeeBizConverter converter = new FeeBizConverter(true);
      BFee result = converter.convert(bill);
      if (BizStates.EFFECT.equals(result.getBizState())) {
        result.setHasAccounts(hasAccount(result.getUuid()));
      }
      validEntityPerm(result);

      fetchContract(result);
      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  /**
   * 获取合同信息
   * 
   * @param fee
   * @throws M3ServiceException
   *           @
   */
  private void fetchContract(BFee fee) throws M3ServiceException {
    Contract contract = getContractService().get(fee.getContract().getUuid());
    if (contract == null)
      throw new M3ServiceException("费用单的合同[" + fee.getContract().getBillNumber() + "]不存在。");
    fee.setContract(ContractBizConverter.getInstance().convert(contract));
  }

  @Override
  public BFee create() throws Exception {
    try {
      Fee fee = new Fee();
      fee.setBizState(BizStates.INEFFECT);
      BFee bill = new FeeBizConverter().convert(fee);
      return bill;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BFee save(BFee bill, BProcessContext task) throws Exception {
    try {

      // 先判断流程权限
      doBeforeExecute(bill, task);

      for (int i = bill.getLines().size() - 1; i >= 0; i--) {
        BFeeLine line = bill.getLines().get(i);
        if (line.getSubject() == null || line.getSubject().getUuid() == null)
          bill.getLines().remove(line);
      }
      Fee po = new BizFeeConverter().convert(bill);
      String uuid = getFeeService().save(po, ConvertHelper.getOperateContext(getSessionUser()));

      po = getFeeService().get(uuid);

      doAfterSave(po, task);

      return FeeBizConverter.getInstance().convert(po);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void delete(String uuid, long oca) throws Exception {
    try {

      getFeeService().remove(uuid, oca, ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void effect(String uuid, String comment, long oca) throws Exception {
    try {

      BeanOperateContext operCtx = ConvertHelper.getOperateContext(getSessionUser());
      operCtx.setAttribute(Fees.PROPERTY_MESSAGE, comment);

      getFeeService().effect(uuid, oca, operCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void abort(String uuid, String comment, long oca) throws Exception {
    try {

      BeanOperateContext operCtx = ConvertHelper.getOperateContext(getSessionUser());
      operCtx.setAttribute(Fees.PROPERTY_MESSAGE, comment);

      getFeeService().abort(uuid, oca, operCtx);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String exportFile(BFeeTemplate template) throws ClientBizException {
    try {

      String filePath = getServletContext().getRealPath("");
      filePath = filePath + "/static/import/fee." + UploadConfig.getInstance().getDownloadSuffix();

      File srcFile = new File(filePath);
      if (srcFile.exists() == false) {
        throw new ClientBizException("导出模板不存在。");
      }

      String file = generalFile();
      File tempFile = new File(file.substring(0, file.lastIndexOf("\\")));
      if (!tempFile.exists()) {
        tempFile.mkdirs();
      }

      // 如果是.xls或.xlsx，保留源模板列格式
      if (file.endsWith(BImpParams.FILE_TYPE_XLS) || file.endsWith(BImpParams.FILE_TYPE_XLSX))
        FileUtils.copyFileToDirectory(srcFile, tempFile);

      List<Contract> contracts = getContracts(template.getAccountUnit().getUuid());
      List<CSVFeeExportItem> records = new ArrayList<CSVFeeExportItem>();
      if (contracts.isEmpty()) {
        records.add(fillRecord(CSVFeeExportItem.COLUMNS.length, template, null));
      } else {
        for (Contract contract : contracts) {
          records.add(fillRecord(CSVFeeExportItem.COLUMNS.length, template, contract));
        }
      }
      if (file.endsWith(BImpParams.FILE_TYPE_CSV)) {
        CsvWriter cw = new CsvWriter(file, CSVFeeExportItem.class);
        cw.start();
        cw.append(records);
        cw.end();
      } else if (file.endsWith(BImpParams.FILE_TYPE_XLS)
          || file.endsWith(BImpParams.FILE_TYPE_XLSX)) {
        FileInputStream inputStream = new FileInputStream(file);
        Workbook book = file.endsWith(BImpParams.FILE_TYPE_XLS) ? new HSSFWorkbook(inputStream)
            : new XSSFWorkbook(inputStream);
        Sheet sheet = book.getSheetAt(0);

        for (int i = 0; i < records.size(); i++) {
          Row recordRow = sheet.createRow(i + 1);
          CSVFeeExportItem record = records.get(i);
          recordRow.createCell(0).setCellValue(record.getAccountUnitCode());
          recordRow.createCell(1).setCellValue(record.getAccountUnitName());
          recordRow.createCell(2).setCellValue(record.getContractNumber());
          recordRow.createCell(3).setCellValue(record.getContractName());
          recordRow.createCell(4).setCellValue(record.getAccountDate());
          recordRow.createCell(5).setCellValue(record.getSubjectCode());
          recordRow.createCell(6).setCellValue(record.getSubjectName());
          recordRow.createCell(7).setCellValue(record.getTotal());
          recordRow.createCell(8).setCellValue(record.getBeginDate());
          recordRow.createCell(9).setCellValue(record.getEndDate());
          recordRow.createCell(10).setCellValue(record.getInvoice());
        }

        FileOutputStream outputStream = new FileOutputStream(file);
        book.write(outputStream);
        outputStream.flush();
        outputStream.close();
        inputStream.close();
      }

      return buildUrl(file);
    } catch (Exception e) {
      throw new ClientException(e);
    }
  }

  private String buildUrl(String file) {
    String url = getRequest().getRequestURL().toString();
    String path = getServletContext().getContextPath();

    url = url.substring(0, url.indexOf(path) + 1);

    String realPath = getServletContext().getRealPath("").substring(0,
        getServletContext().getRealPath("").lastIndexOf("\\") + 1);
    String fileName = file.replace(realPath, "");

    return url + fileName;
  }

  @Override
  public BJobScheduleHandler importFile(BImpParams params, String permGroupId, String permGroupTitle)
      throws ClientBizException {
    try {
      Map<String, Object> paramsMap = new HashMap<String, Object>();
      paramsMap.put(ImpexConstants.PN_CHARSET, "GBK");
      paramsMap.put(ImpexConstants.PN_CAPTION, params.getPluginName());
      paramsMap.put(ImpexConstants.PN_FILE, params.getFile());
      paramsMap.put(ImpexConstants.PN_MAXCOUNT, params.getMaxCount());
      paramsMap.put("scale", getScale());
      paramsMap.put("roundingMode", getRoundingMode());
      if (permGroupId != null) {
        paramsMap.put("permGroupId", permGroupId);
      }
      if (permGroupTitle != null) {
        paramsMap.put("permGroupTitle", permGroupTitle);
      }
      boolean useContractPermT = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
      boolean useContractPermP = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR);
      paramsMap.put("contractTPermEnabled", useContractPermT);
      paramsMap.put("contractPPermEnabled", useContractPermP);
      paramsMap.put("userGroups", getUserGroups(getSessionUser().getId()));
      paramsMap.put("usageCodes", getSubjectUsageCodes());
      List<String> stores = getUserStores(getSessionUser().getId());
      paramsMap.put("stores", stores);

      JobScheduleHandler handler = getFeeJobService().scheduleJob(paramsMap,
          ConvertHelper.getOperateContext(getSessionUser()));
      return new BJobScheduleHandlerConverter().convert(handler);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private List<String> getSubjectUsageCodes() throws Exception {
    List<BSubjectUsage> usages = SubjectUsageUtils.getSubjectUsage(UsageType
        .valueOf(UsageType.tempFee.name()));
    List<String> usageCodes = new ArrayList<String>();
    for (BSubjectUsage usage : usages) {
      if (usageCodes.contains(usage.getCode()) == false)
        usageCodes.add(usage.getCode());
    }
    return usageCodes;
  }

  private String generalFile() {
    String suffix = "csv";
    String downloadUrl = UploadConfig.getInstance().getDownloadSuffix();
    if (StringUtil.isNullOrBlank(downloadUrl) == false) {
      suffix = downloadUrl;
    }
    String realPath = getServletContext().getRealPath("");
    realPath = realPath + "\\template\\";

    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
    String time = fmt.format(new Date());

    String fileName = time + "\\" + "fee." + suffix;
    return realPath + fileName;
  }

  private CSVFeeExportItem fillRecord(int length, BFeeTemplate template, Contract contract)
      throws Exception {
    CSVFeeExportItem item = new CSVFeeExportItem();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    item.setAccountUnitCode(template.getAccountUnit().getCode());
    item.setAccountUnitName(template.getAccountUnit().getName());
    item.setContractNumber(contract == null ? "" : contract.getBillNumber());
    item.setContractName(contract == null ? "" : contract.getTitle());
    item.setAccountDate(template.getAccountDate() == null ? "" : format.format(template
        .getAccountDate()));
    item.setBeginDate(template.getBeginDate() == null ? "" : format.format(template.getBeginDate()));
    item.setEndDate(template.getEndDate() == null ? "" : format.format(template.getEndDate()));
    item.setSubjectCode(template.getSubject() == null || template.getSubject().getCode() == null ? ""
        : template.getSubject().getCode());
    item.setSubjectName(template.getSubject() == null || template.getSubject().getCode() == null ? ""
        : template.getSubject().getName());
    item.setTotal("");
    item.setInvoice("是");
    return item;
  }

  private List<Contract> getContracts(String accountUnitUuid) throws Exception {
    try {
      // 受项目限制
      List<String> stores = getUserStores(getSessionUser().getId());
      if (stores.contains(accountUnitUuid) == false) {
        return new ArrayList<Contract>();
      }
      FlecsQueryDefinition queryDef = new FlecsQueryDefinition();
      queryDef.addCondition(Contracts.CONDITION_BUSINESSUNIT_UUID_IN, stores.toArray());
      queryDef.addCondition(Contracts.CONDITION_CONTRACT_USING);
      queryDef.addCondition(Contracts.CONDITION_BUSINESSUNIT_UUID_EQUALS, accountUnitUuid);
      queryDef.addCondition(Contracts.CONDITION_EFFECT_STATE_EQUALS,
          Contracts.EFFECT_STATE_EFFECTED);
      boolean useContractPermT = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_TENANT);
      boolean useContractPermP = permEnabled(com.hd123.m3.investment.service.contract.contract.Contracts.OBJECT_NAME_FOR_PROPRIETOR);
      if (useContractPermT || useContractPermP) {
        // 启用授权组
        List<String> userGroups = getUserGroups(getSessionUser().getId());
        if (userGroups != null && userGroups.isEmpty() == false) {
          queryDef.addCondition(Contracts.CONDITION_CUSTOM_PERM_GROUP_ID_IN,
              userGroups.toArray());
        } else {
          return new ArrayList<Contract>();
        }
      }

      QueryResult<Contract> qr = getContractService().query(queryDef);
      return qr.getRecords();
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BContract getOnlyOneContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws Exception {
    try {
      return getSingleContract(accountUnitUuid, counterpartUuid, counterpartType);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String executeTask(BOperation operation, BFee entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException {
    try {
      Assert.assertArgumentNotNull(operation, "operation");
      Assert.assertArgumentNotNull(entity, "entity");

      // 先判断流程权限
      doBeforeExecuteTask(entity, operation, task);

      if (BizActions.DELETE.equals(operation.getOutgoingDef().getBusinessAction()) == false
          && saveBeforeAction) {
        String uuid = getFeeService().save(new BizFeeConverter().convert(entity),
            ConvertHelper.getOperateContext(getSessionUser()));
        Fee result = getFeeService().get(uuid);
        doExecuteTask(result, operation, task);
        return result.getUuid();
      } else {
        doExecuteTask(operation, task);
        return entity == null ? null : entity.getUuid();
      }
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  protected Map<String, Object> getBpmVariables(Object obj, BTaskContext ctx) {
    Map<String, Object> variables = super.getBpmVariables(obj, ctx);
    if (obj instanceof Statement) {
      Fee bill = (Fee) obj;
      variables.put(
          BpmConstants.KEY_BILLCAPTION,
          CodecUtils.encode(MessageFormat.format(R.R.billCaption(), bill.getBillNumber(),
              bill.getContract() == null ? "" : bill.getContract().getCode())));
    }
    return variables;
  }

  @Override
  public BAccSettle getAccSettle(String contractUuid) throws ClientBizException {
    if (contractUuid == null)
      return null;

    BAccSettle result = new BAccSettle();

    AccSettle settle = getLastAccountSettle(contractUuid, false);
    result.setExists(settle != null);

    if (result.isExists()) {
      settle = getLastAccountSettle(contractUuid, true);
      result.setLstCalcEndDate(settle == null ? null : settle.getEndDate());
    }

    return result;
  }

  private AccSettle getLastAccountSettle(String contractUuid, boolean accounted)
      throws ClientBizException {
    try {
      FlecsQueryDefinition def = new FlecsQueryDefinition();
      def.addCondition(Contracts.CONDITION_ID_EQUALS, contractUuid);
      List<BSubjectUsage> usages = SubjectUsageUtils.getSubjectUsage(UsageType.tempFee);
      Set<String> usageCodes = new HashSet<String>();
      for (BSubjectUsage usage : usages) {
        usageCodes.add(usage.getCode());
      }
      def.addCondition(Contracts.CONDITION_SUBJECTUSAGE_IN, usageCodes.toArray());
      if (accounted) {
        def.addCondition(Contracts.CONDITION_ACCOUNTED);
      }

      def.addOrder(Contracts.ORDER_BY_ENDDATE, QueryOrderDirection.desc);

      def.setPageSize(1);

      QueryResult<AccountSettle> qry = getContractService().queryAccountSettle(def);
      if (qry.getRecords().size() == 0)
        return null;
      else
        return convertAccSettle(qry.getRecords().get(0));
    } catch (Exception e) {
      throw new ClientBizException(e);
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

  public boolean hasAccount(String... sourceBillUuids) {
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

    List<Account> accounts = getAccountService().query(definition).getRecords();
    if (accounts.isEmpty()) {
      return false;
    }
    return true;
  }

  private String getScale() {
    String scale = getOptionService().getOption(AccOptions.OPTION_PATH, AccOptions.OPTION_SCALE);
    return scale == null ? "2" : scale;
  }

  private String getRoundingMode() {
    String roundingMode = getOptionService().getOption(AccOptions.OPTION_PATH,
        AccOptions.OPTION_ROUNDING_MODE);
    return roundingMode == null ? RoundingMode.HALF_UP.name() : roundingMode;
  }

  private com.hd123.m3.account.service.fee.FeeService getFeeService() {
    return M3ServiceFactory.getService(com.hd123.m3.account.service.fee.FeeService.class);
  }

  private FeeJobService getFeeJobService() {
    return M3ServiceFactory.getService(FeeJobService.DEFAULT_CONTEXT_ID, FeeJobService.class);
  }

  private AccountService getAccountService() {
    return M3ServiceFactory.getService(AccountService.class);
  }

  @Override
  public String getObjectName() {
    return Fees.OBJECT_NAME;
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("费用单")
    String moduleCaption();

    @DefaultStringValue("费用单：{0},合同编号：{1}")
    String billCaption();

    @DefaultStringValue("找不到任务出口({0})对应的操作。")
    String nullTask();

    @DefaultStringValue("{0},{1}不是有效的数据，或者缺少授权组权限。")
    String accountUnitAndCounterpart();

    @DefaultStringValue("{0}不是有效的数据，或者缺少授权组权限。")
    String lackOfAuthorizatioGroups();

    @DefaultStringValue("指定的\"费用单\"不存在。")
    String nullObject();
  }

}
