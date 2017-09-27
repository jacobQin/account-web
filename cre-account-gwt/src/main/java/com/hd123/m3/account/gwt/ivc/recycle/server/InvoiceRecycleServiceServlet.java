/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceType;
import com.hd123.m3.account.gwt.ivc.recycle.client.EPInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.rpc.InvoiceRecycleService;
import com.hd123.m3.account.gwt.ivc.recycle.server.converter.BizInvoiceRecycleConverter;
import com.hd123.m3.account.gwt.ivc.recycle.server.converter.InvoiceRecycleBizConverter;
import com.hd123.m3.account.gwt.util.server.InvoiceTypeUtils;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycle;
import com.hd123.m3.account.service.ivc.recycle.InvoiceRecycles;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.gwt.bpm.server.M3BpmService2Servlet;
import com.hd123.m3.commons.gwt.util.server.converter.ConvertHelper;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.rumba.commons.biz.entity.Operator;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.codec.CodecUtilsBean;
import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.base.client.util.CollectionUtil;
import com.hd123.rumba.gwt.flecs.client.FlecsQueryDef;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2.server.RPageDataConverter;
import com.hd123.rumba.webframe.gwt.base.client.permission.BPermission;
import com.hd123.rumba.webframe.gwt.base.server.PermHelper;

/**
 * 发票回收单|客户端服务。
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class InvoiceRecycleServiceServlet extends M3BpmService2Servlet<BInvoiceRecycle> implements
    InvoiceRecycleService {
  private static final long serialVersionUID = -8223429971295631698L;

  @Override
  protected void buildModuleContext(Map<String, String> moduleContext, CodecUtilsBean codecUtils) {
    super.buildModuleContext(moduleContext, codecUtils);
    moduleContext.put(INVOICE_TYPE, CollectionUtil.toString(getInvoiceTypes()));
  }

  @Override
  protected Set<BPermission> getAuthorizedPermissions(Operator sessionUser) {
    Set<BPermission> permissions = new HashSet<BPermission>();
    PermHelper helper = M3ServiceFactory.getBean(PermHelper.DEFAULT_CONTEXT_ID, PermHelper.class);
    permissions.addAll(helper.getAuthorizedPermissions(sessionUser.getId(),
        Arrays.asList(EPInvoiceRecycle.RESOURCE_KEY)));
    return permissions;
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
  public BUCN getCurrentEmployee(String id) throws ClientBizException {
    try {

      User user = getUserService().get(id);
      return new BUCN(user.getUuid(), user.getId(), user.getFullName());
    } catch (Exception e) {
      throw new ClientBizException(e);
    }
  }

  @Override
  protected String doSave(BInvoiceRecycle entity) throws Exception {
    try {
      entity.setSort(InvoiceTypeUtils.getInvoiceTypeByCode(entity.getInvoiceType()).getSort());
      removeEmptyLines(entity);
      return getInvoiceRecycleService().save(
          BizInvoiceRecycleConverter.getInstance().convert(entity),
          ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  /** 去除空明细行 */
  private void removeEmptyLines(BInvoiceRecycle entity) {
    for (int i = entity.getLines().size() - 1; i >= 0; i--) {
      if (entity.getLines().get(i).isEmpty()) {
        entity.getLines().remove(i);
      }
    }
  }

  @Override
  protected BInvoiceRecycle doGet(String id) throws Exception {
    Assert.assertArgumentNotNull(id, "id");
    try {
      InvoiceRecycle bill = getInvoiceRecycleService().get(id);
      BInvoiceRecycle bBill = InvoiceRecycleBizConverter.getInstance().convert(bill);
      return bBill;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  protected void doChangeBizState(String uuid, long version, String state) throws Exception {
    getInvoiceRecycleService().changeBizState(uuid, version, state,
        ConvertHelper.getOperateContext(getSessionUser()));
  }

  @Override
  protected String getServiceBeanId() {
    return com.hd123.m3.account.service.ivc.recycle.InvoiceRecycleService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected RPageData<BInvoiceRecycle> doQuery(FlecsQueryDef definition) throws Exception {
    try {

      FlecsQueryDefinition queryDef = buildQueryDefinition(definition,
          InvoiceRecycleQueryBuilder.getInstance());
      QueryResult<InvoiceRecycle> qr = getInvoiceRecycleService().query(queryDef);
      return RPageDataConverter.convert(qr, InvoiceRecycleBizConverter.getInstance());
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  protected String getObjectCaption() {
    return R.R.billCaption();
  }

  @Override
  public String getObjectName() {
    return InvoiceRecycles.OBJECT_NAME;
  }

  private com.hd123.m3.account.service.ivc.recycle.InvoiceRecycleService getInvoiceRecycleService() {
    return M3ServiceFactory
        .getService(com.hd123.m3.account.service.ivc.recycle.InvoiceRecycleService.class);
  }

  protected UserService getUserService() {
    return M3ServiceFactory.getBean(UserService.DEFAULT_CONTEXT_ID, UserService.class);
  }

  public static interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("发票调拨单")
    String billCaption();
  }
}
