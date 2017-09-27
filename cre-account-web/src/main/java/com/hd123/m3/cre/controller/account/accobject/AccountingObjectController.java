/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccountingObjectController.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月1日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.accobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hd123.m3.account.service.accobject.AccountingObject;
import com.hd123.m3.account.service.accobject.AccountingObjectBill;
import com.hd123.m3.account.service.accobject.AccountingObjectBillService;
import com.hd123.m3.account.service.accobject.AccountingObjectLine;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.commons.biz.query.FlecsQueryDefinition;
import com.hd123.m3.commons.biz.util.QueryPagingUtil;
import com.hd123.m3.commons.servlet.biz.query.FlecsQueryDefinitionBuilder;
import com.hd123.m3.commons.servlet.biz.query.OrderSort;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.biz.query.SummaryQueryResult;
import com.hd123.m3.commons.servlet.controllers.module.ModuleController;
import com.hd123.m3.cre.controller.account.accobject.common.AccObjectSortUtil;
import com.hd123.m3.cre.controller.account.accobject.common.AccountingObjectQueryBuilder;
import com.hd123.m3.cre.controller.account.accobject.model.BAccObjectDetail;
import com.hd123.m3.cre.controller.account.common.util.CollectionUtil;
import com.hd123.rumba.commons.biz.entity.BeanOperateContext;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.gwt.base.client.QueryFilter.Order;
import com.hd123.rumba.gwt.util.client.OrderDir;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * 核算主体维护控制器
 * 
 * @author LiBin
 * 
 */
@Controller
@RequestMapping("account/accobject/*")
public class AccountingObjectController extends ModuleController {
  /** 默认项目 */
  private static final String KEY_DEFAULT_STORE = "defaultStore";

  @Autowired
  AccountingObjectBillService accObjectBillService;

  /** 构建模块操作上下文 */
  protected void buildModuleContext(Map<String, Object> moduleContext) {
    super.buildModuleContext(moduleContext);

    List<UCN> stores = getUserStoreNames(getSessionUser().getId());
    Collections.sort(stores, new Comparator<UCN>() {

      @Override
      public int compare(UCN o1, UCN o2) {
        return (o1.getCode().compareTo(o2.getCode()));
      }
    });
    if (stores.size() > 0) {
      moduleContext.put(KEY_DEFAULT_STORE, stores.get(0));
    }
  }

  /** 查询核算主体明细 */
  @RequestMapping("query")
  public @ResponseBody
  SummaryQueryResult queryAccObjects(@RequestBody
  QueryFilter queryFilter) {
    if (queryFilter == null
        || queryFilter.getFilter() == null
        || StringUtil.isNullOrBlank((String) queryFilter.getFilter().get(
            AccountingObjectQueryBuilder.FILTER_STORE_UUID))) {
      return new SummaryQueryResult();
    }

    FlecsQueryDefinition queryDef = getQueryBuilder().build(queryFilter,
        AccountingObjectBill.OBJECT_NAME, getSessionUserId());

    queryDef.setPage(0);
    queryDef.setPageSize(0);

    QueryResult<AccountingObjectLine> qr = accObjectBillService.queryAccObjects(queryDef);

    // 将按项目及按合同设置明细分开
    List<AccountingObjectLine> storeLines = new ArrayList<AccountingObjectLine>();
    List<AccountingObjectLine> contractLines = new ArrayList<AccountingObjectLine>();
    for (AccountingObjectLine line : qr.getRecords()) {
      if (line.getContract() == null) {
        storeLines.add(line);
      } else {
        contractLines.add(line);
      }
    }

    List<BAccObjectDetail> storeDetails = new ArrayList<BAccObjectDetail>();
    // 按项目设置（按核算主体分组）
    Collection<List<AccountingObjectLine>> storeLineGroups = CollectionUtil.group(storeLines,
        new Comparator<AccountingObjectLine>() {
          @Override
          public int compare(AccountingObjectLine o1, AccountingObjectLine o2) {
            if (o1.getAccObject().equals(o2.getAccObject())) {
              return 0;
            }
            return 1;
          }
        });

    for (List<AccountingObjectLine> group : storeLineGroups) {
      BAccObjectDetail detail = new BAccObjectDetail();
      detail.setStore(group.get(0).getStore());
      detail.setAccObject(group.get(0).getAccObject());
      detail.setType(BAccObjectDetail.BY_STORE);
      for (AccountingObjectLine line : group) {
        detail.getSubjects().add(line.getSubject());
      }
      storeDetails.add(detail);
    }

    List<BAccObjectDetail> contractDetails = new ArrayList<BAccObjectDetail>();
    // 按合同设置（按合同+核算主体分组）
    Collection<List<AccountingObjectLine>> contractLineGroups = CollectionUtil.group(contractLines,
        new Comparator<AccountingObjectLine>() {
          @Override
          public int compare(AccountingObjectLine o1, AccountingObjectLine o2) {
            if (o1.getContract().equals(o2.getContract())
                && o1.getAccObject().equals(o2.getAccObject())) {
              return 0;
            }
            return 1;
          }
        });

    for (List<AccountingObjectLine> group : contractLineGroups) {
      BAccObjectDetail detail = new BAccObjectDetail();
      detail.setStore(group.get(0).getStore());
      detail.setContract(group.get(0).getContract());
      detail.setAccObject(group.get(0).getAccObject());
      detail.setType(BAccObjectDetail.BY_CONTRACT);
      for (AccountingObjectLine line : group) {
        detail.getSubjects().add(line.getSubject());
      }
      contractDetails.add(detail);
    }

    // 排序
    storeDetails = AccObjectSortUtil.sortAccObjectDetails(storeDetails,
        getOrder(queryFilter, AccObjectSortUtil.ORDER_BY_FIELD_CONTRACT, OrderDir.desc));
    contractDetails = AccObjectSortUtil.sortAccObjectDetails(contractDetails,
        getOrder(queryFilter, AccObjectSortUtil.ORDER_BY_FIELD_CONTRACT, OrderDir.desc));

    List<BAccObjectDetail> details = new ArrayList<BAccObjectDetail>();
    details.addAll(storeDetails);
    details.addAll(contractDetails);

    // 分页
    QueryResult<BAccObjectDetail> result = QueryPagingUtil.flip(details, queryFilter.getPage() - 1,
        queryFilter.getPageSize());

    SummaryQueryResult queryResult = SummaryQueryResult.newInstance(result);

    return queryResult;
  }

  /**
   * 批量保存核算主体，服务需要对传入的核算主体对象逐个进行合法性检查，如版本信息检查等。
   * 
   * @param accObjects
   *          核算主体对象列表，传入空或者空列表时将忽略此操作。
   * @throws M3ServiceException
   *           当数据不合法及服务端异常时抛出。
   */
  @RequestMapping(value = "saveAccObjects")
  public @ResponseBody
  void saveAccObjects(@RequestBody
  List<AccountingObject> accObjects) throws M3ServiceException {
    if (accObjects == null || accObjects.isEmpty()) {
      return;
    }

    // 过滤核算主体列表
    filterAccObjects(accObjects);
    // 处理删除的核算主体
    processShouldRemoveAccObjects(accObjects);

    if (accObjects.isEmpty()) {
      return;
    }

    accObjectBillService.saveAccObjects(accObjects, new BeanOperateContext(getSessionUser()));
  }

  /**
   * 取得所有核算主体对象列表。
   * 
   * @return 已保存的核算主体对象列表，至少会返回一个空集合。
   */
  @RequestMapping(value = "allAccObjects", method = RequestMethod.POST)
  public @ResponseBody
  List<AccountingObject> getAllAccObjects(@RequestBody
  QueryFilter queryFilter) {
    List<AccountingObject> objects = accObjectBillService.getAllAccObjects();
    if (StringUtil.isNullOrBlank(queryFilter.getKeyword())) {
      return objects;
    }

    String keyword = queryFilter.getKeyword();
    List<AccountingObject> objectsRexsult = new ArrayList<AccountingObject>();
    for (AccountingObject object : objects) {
      if (object.getCode().toUpperCase().startsWith(keyword.toUpperCase())
          || object.getName().contains(keyword)) {
        objectsRexsult.add(object);
      }
    }
    return objectsRexsult;
  }

  /**
   * 取得所有核算主体对象列表。
   * 
   * @return 已保存的核算主体对象列表，至少会返回一个空集合。
   */
  @RequestMapping(value = "allAccObjects", method = RequestMethod.GET)
  public @ResponseBody
  List<AccountingObject> getAllAccObjects() {
    return getAllAccObjects(new QueryFilter());
  }

  private Order getOrder(QueryFilter filter, String defaultFieldName, OrderDir defaultOrderDir) {
    Order order = new Order();
    if (filter.getSorts() != null && filter.getSorts().size() > 0) {
      OrderSort orderSort = filter.getSorts().get(0);
      order.setFieldName(orderSort.getProperty());
      order.setDir("DESC".equals(orderSort.getDirection()) ? OrderDir.desc : OrderDir.asc);
    } else if (StringUtil.isNullOrBlank(defaultFieldName) == false) {
      order.setFieldName(defaultFieldName);
      order.setDir(defaultOrderDir);
    }
    return order;
  }

  private FlecsQueryDefinitionBuilder getQueryBuilder() {
    return getAppCtx().getBean(AccountingObjectQueryBuilder.class);
  }

  /** 过滤核算主体列表，去掉代码为空的行以及代码重复的行 */
  private void filterAccObjects(List<AccountingObject> accObjects) {
    for (int index = accObjects.size() - 1; index >= 0; index--) {
      AccountingObject obj = accObjects.get(index);
      // 过滤掉代码为空的核算主体
      if (StringUtil.isNullOrBlank(obj.getCode())) {
        accObjects.remove(index);
        continue;
      }

      // 过滤掉代码重复的核算主体
      for (int j = index - 1; j >= 0; j--) {
        AccountingObject nextobj = accObjects.get(j);
        if (obj.getCode().equals(nextobj.getCode())) {
          accObjects.remove(index);
          break;
        }
      }
    }

  }

  /** 处理删除的核算主体 */
  private void processShouldRemoveAccObjects(List<AccountingObject> accObjects)
      throws M3ServiceException {
    List<AccountingObject> oldAccObjects = accObjectBillService.getAllAccObjects();
    for (AccountingObject oldAccObject : oldAccObjects) {
      boolean shouldRemove = true;
      for (AccountingObject accObject : accObjects) {
        if (oldAccObject.getUuid().equals(accObject.getUuid())) {
          shouldRemove = false;
          break;
        }
      }
      if (shouldRemove) {
        accObjectBillService.removeAccObject(oldAccObject.getUuid(), oldAccObject.getVersion());
      }
    }
  }

}
