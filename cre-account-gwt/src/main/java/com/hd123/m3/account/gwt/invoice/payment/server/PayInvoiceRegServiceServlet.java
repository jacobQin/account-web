/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： RecInvoiceRegServiceServlet.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-27 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.invoice.payment.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.commons.AccOptions;
import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.gwt.acc.server.BizAcc1Converter;
import com.hd123.m3.account.gwt.acc.server.BizAcc2Converter;
import com.hd123.m3.account.gwt.base.server.AccBPMServiceServlet;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceType;
import com.hd123.m3.account.gwt.commons.client.biz.RoundingMode;
import com.hd123.m3.account.gwt.commons.server.BizTotalConverter;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegConfig;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegInvoice;
import com.hd123.m3.account.gwt.invoice.commons.client.biz.BInvoiceRegLine;
import com.hd123.m3.account.gwt.invoice.commons.server.converter.BizInvoiceRegConverter;
import com.hd123.m3.account.gwt.invoice.commons.server.converter.InvoiceRegBizConverter;
import com.hd123.m3.account.gwt.invoice.payment.client.rpc.PayInvoiceRegService;
import com.hd123.m3.account.gwt.invoice.payment.intf.client.perm.PayInvoiceRegPermDef;
import com.hd123.m3.account.gwt.invoice.payment.server.querybuilder.PayInvoiceRegQueryBuilder;
import com.hd123.m3.account.gwt.util.server.InvoiceTypeUtils;
import com.hd123.m3.account.service.acc.Accounts;
import com.hd123.m3.account.service.billtype.BillType;
import com.hd123.m3.account.service.billtype.BillTypeService;
import com.hd123.m3.account.service.invoice.InvoiceReg;
import com.hd123.m3.account.service.invoice.InvoiceRegInvoice;
import com.hd123.m3.account.service.invoice.InvoiceRegLine;
import com.hd123.m3.account.service.invoice.InvoiceRegService;
import com.hd123.m3.account.service.invoice.InvoiceRegs;
import com.hd123.m3.commons.biz.entity.BizActions;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.tax.TaxRate;
import com.hd123.m3.commons.biz.util.JsonUtil;
import com.hd123.m3.commons.gwt.base.server.tax.BizTaxRateConverter;
import com.hd123.m3.commons.gwt.base.server.tax.TaxRateBizConverter;
import com.hd123.m3.commons.gwt.util.server.RPageDataUtil;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.investment.rs.service.tenant.RSTenantService;
import com.hd123.m3.investment.service.tenant.tenant.Tenant;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.util.converter.ConverterUtil;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * @author chenpeisi
 * 
 */
public class PayInvoiceRegServiceServlet extends AccBPMServiceServlet implements
    PayInvoiceRegService {
  private static final long serialVersionUID = 300200L;

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    try {
      moduleContext.put(KEY_INVOICE_TYPE, CollectionUtil.toString(getInvoiceTypes()));
      moduleContext.put(KEY_BILL_TYPES, JsonUtil.objectToJson(getBillTypes()));
      moduleContext.put(SCALE, getScale());
      moduleContext.put(ROUNDING_MODE, getRoundingMode());
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public String getObjectName() {
    return InvoiceRegs.OBJECT_NAME_PAY;
  }

  @Override
  protected String getObjectCaption() {
    return R.R.moduleCaption();
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    Set<BPermission> permissions = helper.getAuthorizedPermissions(sessionUser.getQualifiedId(),
        PayInvoiceRegPermDef.RESOURCE_PAYINVOICEREG_SET);
    return permissions;
  }

  @Override
  public BDefaultOption getDefaultValue() throws ClientBizException {
    try {
      BDefaultOption defaultOption = new BDefaultOption();

      TaxRate taxRate = JsonUtil.jsonToObject(
          getOptionService().getOption(AccOptions.DEFAULT_PATH, AccOptions.DEFAULT_TAXRATE),
          TaxRate.class);

      defaultOption.setTaxRate(TaxRateBizConverter.getInstance().convert(taxRate));

      return defaultOption;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BInvoiceRegConfig getConfig() throws ClientBizException {
    try {
      BInvoiceRegConfig config = new BInvoiceRegConfig();

      String regTotalWriteable = getOptionService().getOption(InvoiceRegs.OPTION_PATH,
          InvoiceRegs.KEY_PAY_REGTOTAL_WRITABLE);
      config.setRegTotalWritable(regTotalWriteable == null ? false : Boolean
          .valueOf(regTotalWriteable));

      String taxDiffHi = getOptionService().getOption(InvoiceRegs.OPTION_PATH,
          InvoiceRegs.KEY_PAY_TAX_DIFF_HI);
      config.setTaxDiffHi(taxDiffHi == null ? BigDecimal.ZERO : new BigDecimal(taxDiffHi));

      String taxDiffLo = getOptionService().getOption(InvoiceRegs.OPTION_PATH,
          InvoiceRegs.KEY_PAY_TAX_DIFF_LO);
      config.setTaxDiffLo(taxDiffLo == null ? BigDecimal.ZERO : new BigDecimal(taxDiffLo));

      String totalDiffHi = getOptionService().getOption(InvoiceRegs.OPTION_PATH,
          InvoiceRegs.KEY_PAY_TOTAL_DIFF_HI);
      config.setTotalDiffHi(totalDiffHi == null ? BigDecimal.ZERO : new BigDecimal(totalDiffHi));

      String totalDiffLo = getOptionService().getOption(InvoiceRegs.OPTION_PATH,
          InvoiceRegs.KEY_PAY_TOTAL_DIFF_LO);
      config.setTotalDiffLo(totalDiffLo == null ? BigDecimal.ZERO : new BigDecimal(totalDiffLo));

      return config;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  public List<BillType> getBillTypes() throws ClientBizException {
    List<BillType> result = new ArrayList<BillType>();
    List<BillType> types = getBillTypeService().getAccountTypes();
    for (BillType type : types) {
      if (type.getDirection() == 0 || type.getDirection() == Direction.PAYMENT) {
        result.add(type);
      }
    }
    return result;
  }

  public Map<String, String> getInvoiceTypes() {
    Map<String, String> mapInvoiceTypes = new HashMap<String, String>();
    try {
      for (BInvoiceType type : InvoiceTypeUtils.getAll()) {
        mapInvoiceTypes.put(type.getCode(), type.getName());
      }
      return mapInvoiceTypes;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public BInvoiceReg load(String uuid) throws ClientBizException {
    try {

      InvoiceReg source = getInvoiceRegService().get(uuid, InvoiceReg.PART_WHOLE);

      BInvoiceReg result = InvoiceRegBizConverter.getInstance().convert(source);
      if (validateEntity(result) == false)
        return null;

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BInvoiceReg loadByNumber(String billNumber) throws ClientBizException {
    try {

      InvoiceReg source = getInvoiceRegService().getByNumber(billNumber, InvoiceReg.PART_WHOLE);

      BInvoiceReg result = InvoiceRegBizConverter.getInstance().convert(source);
      if (validateEntity(result) == false)
        return null;

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private boolean validateEntity(BInvoiceReg source) throws Exception {
    if (source == null)
      return false;
    if (source.getDirection() == Direction.RECEIPT)
      return false;

    validEntityPerm(source);

    return true;
  }

  @Override
  public RPageData<BInvoiceReg> query(List<Condition> conditions, List<String> visibleColumnNames,
      PageSort pageSort) throws ClientBizException {
    try {

      FlecsQueryDefinition queryDefinition = buildQueryDefinition(conditions, pageSort);

      if (queryDefinition == null)
        return new RPageData<BInvoiceReg>();

      QueryResult<InvoiceReg> queryResult = getInvoiceRegService().query(queryDefinition);

      RPageData<BInvoiceReg> result = new RPageData<BInvoiceReg>();

      RPageDataUtil.copyPagingInfo(queryResult, result);

      result.setValues(ConverterUtil.convert(queryResult.getRecords(),
          InvoiceRegBizConverter.getInstance()));

      // 更新导航数据
      updateNaviEntities(getObjectName(), result.getValues());

      return result;
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions, PageSort pageSort)
      throws ClientBizException {
    FlecsQueryDefinition quertDef = buildQueryDefinition(conditions, pageSort.getSortOrders());
    if (quertDef == null)
      return null;

    quertDef.setPage(pageSort.getPage());
    quertDef.setPageSize(pageSort.getPageSize());
    return quertDef;
  }

  private FlecsQueryDefinition buildQueryDefinition(List<Condition> conditions,
      List<Order> sortOrders) throws ClientBizException {
    FlecsQueryDefinition queryDef = buildDefaultQueryDefinition();

    if (queryDef == null)
      return null;

    queryDef.getFlecsConditions().addAll(
        PayInvoiceRegQueryBuilder.getInstance().buildFlecsConditions(conditions));
    queryDef.setOrders(PayInvoiceRegQueryBuilder.getInstance().buildQueryOrders(sortOrders));

    return queryDef;
  }

  private FlecsQueryDefinition buildDefaultQueryDefinition() throws ClientBizException {
    FlecsQueryDefinition queryDef = new FlecsQueryDefinition();

    queryDef.addCondition(InvoiceRegs.CONDITION_DIRECTION_EQUALS, Direction.PAYMENT);
    queryDef.getConditions().addAll(getPermConditions());

    return queryDef;
  }

  @Override
  public void remove(String uuid, long version) throws ClientBizException {
    try {

      getInvoiceRegService().remove(uuid, version,
          ConvertHelper.getOperateContext(getSessionUser()));

    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BInvoiceReg create() throws ClientBizException {
    try {
      InvoiceReg source = getInvoiceRegService().create(Direction.PAYMENT);
      return InvoiceRegBizConverter.getInstance().convert(source);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BInvoiceReg createByStatement(String statementUuid, int direction)
      throws ClientBizException {
    try {
      InvoiceReg source = getInvoiceRegService().createByStatement(statementUuid, direction);
      return InvoiceRegBizConverter.getInstance().convert(source);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BInvoiceReg createByPayment(String paymentUuid) throws ClientBizException {
    try {

      InvoiceReg source = getInvoiceRegService().createByPayment(paymentUuid);
      return InvoiceRegBizConverter.getInstance().convert(source);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void effect(String uuid, long version) throws ClientBizException {
    try {

      getInvoiceRegService().effect(uuid, version,
          ConvertHelper.getOperateContext(getSessionUser()));

    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public void abort(String uuid, long version) throws ClientBizException {
    try {

      getInvoiceRegService()
          .abort(uuid, version, ConvertHelper.getOperateContext(getSessionUser()));

    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public BInvoiceReg save(BInvoiceReg entity, BProcessContext task) throws ClientBizException {
    try {

      // 先判断流程权限
      doBeforeExecute(entity, task);

      InvoiceReg target = null;
      if (entity.getUuid() == null)
        target = getInvoiceRegService().create(Direction.PAYMENT);
      else
        target = getInvoiceRegService().get(entity.getUuid(), InvoiceReg.PART_WHOLE);

      write(entity, target);

      String uuid = getInvoiceRegService().save(target,
          ConvertHelper.getOperateContext(getSessionUser()));

      target = getInvoiceRegService().get(uuid);

      doAfterSave(target, task);

      return InvoiceRegBizConverter.getInstance().convert(target);
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String getInvoiceCode(String uuid) throws ClientBizException {
    try {

      Tenant tenat = getTenantService().get(uuid);
      if (tenat == null)
        return null;
      return tenat.getInvoiceCode();
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  public String executeTask(BOperation operation, BInvoiceReg entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException {
    try {
      Assert.assertArgumentNotNull(operation, "operation");
      Assert.assertArgumentNotNull(entity, "entity");
      // 先判断流程权限
      doBeforeExecuteTask(entity, operation, task);

      if (saveBeforeAction
          && !BizActions.DELETE.equals(operation.getOutgoingDef().getBusinessAction())) {
        String uuid = getInvoiceRegService().save(
            BizInvoiceRegConverter.getInstance().convert(entity),
            ConvertHelper.getOperateContext(getSessionUser()));
        InvoiceReg result = getInvoiceRegService().get(uuid);
        doExecuteTask(result, operation, task);
        return result.getUuid();
      } else {
        doExecuteTask(operation, task);
        return entity == null ? null : entity.getUuid();
      }
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  private void write(BInvoiceReg source, InvoiceReg target)  {
    assert source != null;
    assert target != null;

    target.setPermGroupId(source.getPermGroupId());
    target.setPermGroupTitle(source.getPermGroupTitle());
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setAccountTotal(BizTotalConverter.getInstance().convert(source.getAccountTotal()));
    target.setCounterpart(BizUCNConverter.getInstance().convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
        .getCounterpartType());
    target.setDirection(source.getDirection());
    target.setInvoiceCode(source.getInvoiceCode());
    target.setInvoiceTotal(BizTotalConverter.getInstance().convert(source.getInvoiceTotal()));
    target.setRegDate(source.getRegDate());
    target.setRemark(source.getRemark());
    target.setSettleNo(source.getSettleNo());
    target.setTaxDiff(source.getTaxDiff());
    target.setTotalDiff(source.getTotalDiff());
    target.setVersion(source.getVersion());

    List<InvoiceRegLine> lines = new ArrayList<InvoiceRegLine>();
    for (BInvoiceRegLine bLine : source.getLines()) {
      InvoiceRegLine line = new InvoiceRegLine();

      line.setLineNumber(bLine.getLineNumber());
      line.setAcc1(BizAcc1Converter.getInstance().convert(bLine.getAcc1()));
      line.setAcc2(BizAcc2Converter.getInstance().convert(bLine.getAcc2()));
      line.getAcc2().setLocker(Accounts.NONE_LOCKER);
      line.getAcc2().setId(line.getAcc2().bizId().toId());

      line.setRemark(bLine.getRemark());
      line.setTotal(BizTotalConverter.getInstance().convert(bLine.getRegTotal()));
      line.setUnregTotal(BizTotalConverter.getInstance().convert(bLine.getUnregTotal()));
      lines.add(line);
    }
    target.setLines(lines);

    List<InvoiceRegInvoice> invoices = new ArrayList<InvoiceRegInvoice>();
    for (BInvoiceRegInvoice bInvoice : source.getInvoices()) {
      if (StringUtil.isNullOrBlank(bInvoice.getInvoiceNumber()))
        continue;
      InvoiceRegInvoice invoice = new InvoiceRegInvoice();

      invoice.setLineNumber(bInvoice.getLineNumber());
      invoice.setInvoiceDate(bInvoice.getInvoiceDate());
      invoice.setInvoiceNumber(bInvoice.getInvoiceNumber());
      invoice.setInvoiceType(bInvoice.getInvoiceType());
      invoice.setRemark(bInvoice.getRemark());
      invoice.setTaxRate(BizTaxRateConverter.getInstance().convert(bInvoice.getTaxRate()));
      invoice.setTotal(BizTotalConverter.getInstance().convert(bInvoice.getTotal()));

      invoices.add(invoice);
    }
    target.setInvoices(invoices);
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

  private InvoiceRegService getInvoiceRegService()  {
    return M3ServiceFactory.getService(InvoiceRegService.class);
  }

  private RSTenantService getTenantService() {
    return M3ServiceFactory.getService(RSTenantService.class);
  }

  private BillTypeService getBillTypeService() {
    return getAppCtx().getBean(BillTypeService.class);
  }

  public interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("付款发票登记单")
    String moduleCaption();

    @DefaultStringValue("付款发票登记：{0}")
    String billCaption();

    @DefaultStringValue("找不到任务出口({0})对应的操作。")
    String nullTask();

    @DefaultStringValue("获取单据类型出错：")
    String findBillTypeError();

    @DefaultStringValue("指定的\"付款发票登记单\"不存在。")
    String nullObject();
  }
}
