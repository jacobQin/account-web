/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceInstockServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月7日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.instock.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hd123.ia.authen.service.user.User;
import com.hd123.ia.authen.service.user.UserService;
import com.hd123.m3.account.gwt.commons.client.biz.BInvoiceType;
import com.hd123.m3.account.gwt.ivc.instock.client.EPInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.biz.BInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.rpc.InvoiceInstockService;
import com.hd123.m3.account.gwt.ivc.instock.server.converter.BizInvoiceInstockConverter;
import com.hd123.m3.account.gwt.ivc.instock.server.converter.InvoiceInstockBizConverter;
import com.hd123.m3.account.gwt.util.server.InvoiceTypeUtils;
import com.hd123.m3.account.service.ivc.instock.InvoiceInstock;
import com.hd123.m3.account.service.ivc.instock.InvoiceInstocks;
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
 * 发票入库单|客户端服务。
 * 
 * @author lixiaohong
 * @since 1.7
 */
public class InvoiceInstockServiceServlet extends M3BpmService2Servlet<BInvoiceInstock> implements
    InvoiceInstockService {
  private static final long serialVersionUID = 8704653242527220996L;

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
        Arrays.asList(EPInvoiceInstock.RESOURCE_KEY)));
    return permissions;
  }

  public Map<String, String> getInvoiceTypes() {
    Map<String,String> mapInvoiceTypes = new HashMap<String, String>();
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
  protected String doSave(BInvoiceInstock entity) throws Exception {
    try {
      entity.setSort(InvoiceTypeUtils.getInvoiceTypeByCode(entity.getInvoiceType()).getSort());
      removeEmptyLines(entity);
      return getInvoiceInstockService().save(
          BizInvoiceInstockConverter.getInstance().convert(entity),
          ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  /** 去除空明细行 */
  private void removeEmptyLines(BInvoiceInstock entity) {
    for (int i = entity.getInstockLines().size() - 1; i >= 0; i--) {
      if (entity.getInstockLines().get(i).isEmpty()) {
        entity.getInstockLines().remove(i);
      }
    }
  }

  @Override
  protected BInvoiceInstock doGet(String id) throws Exception {
    Assert.assertArgumentNotNull(id, "id");
    try {
      InvoiceInstock bill = getInvoiceInstockService().get(id);
      BInvoiceInstock bBill = InvoiceInstockBizConverter.getInstance().convert(bill);
      return bBill;
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  protected void doChangeBizState(String uuid, long version, String state) throws Exception {
    try {
      getInvoiceInstockService().changeBizState(uuid, version, state,
          ConvertHelper.getOperateContext(getSessionUser()));
    } catch (Exception e) {
      throw new ClientBizException(e.getMessage());
    }
  }

  @Override
  protected String getServiceBeanId() {
    return com.hd123.m3.account.service.ivc.instock.InvoiceInstockService.DEFAULT_CONTEXT_ID;
  }

  @Override
  protected RPageData<BInvoiceInstock> doQuery(FlecsQueryDef definition) throws Exception {
    try {
      
      FlecsQueryDefinition queryDef = buildQueryDefinition(definition,
          InvoiceInstockQueryBuilder.getInstance());
      QueryResult<InvoiceInstock> qr = getInvoiceInstockService().query(queryDef);
      return RPageDataConverter.convert(qr, InvoiceInstockBizConverter.getInstance());
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
    return InvoiceInstocks.OBJECT_NAME;
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

  protected UserService getUserService() {
    return M3ServiceFactory.getBean(UserService.DEFAULT_CONTEXT_ID, UserService.class);
  }

  private com.hd123.m3.account.service.ivc.instock.InvoiceInstockService getInvoiceInstockService() {
    return M3ServiceFactory
        .getService(com.hd123.m3.account.service.ivc.instock.InvoiceInstockService.class);
  }

  public static interface R {
    public static final R R = Resources.create(R.class);

    @DefaultStringValue("发票入库单")
    String billCaption();
  }

}
