/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名： cre-portal
 * 文件名： CommonComponent.java
 * 模块说明：    
 * 修改历史：
 * 2016年10月19日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.settle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.hd123.bpm.service.definition.ProcessDefinition;
import com.hd123.bpm.service.execution.Definition;
import com.hd123.bpm.service.execution.ProcessService;
import com.hd123.m3.account.service.statement.Statements;
import com.hd123.m3.commons.biz.entity.HasPerm;
import com.hd123.m3.commons.biz.option.PermOptionService;
import com.hd123.m3.commons.biz.store.MStore;
import com.hd123.m3.commons.biz.store.MStoreService;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;
import com.hd123.m3.commons.servlet.controllers.module.BpmConstants;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.query.QueryResult;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.rumba.webframe.session.Session;

/**
 * @author mengyinkun
 * 
 */
@Component
public class CommonComponent {
  @Autowired
  private ProcessService processService;
  @Autowired
  private MStoreService storeService;
  @Autowired
  private PermOptionService permOptionService;

  QueryResult<ProcessDefinition> queryProcess(QueryFilter filter) {
    QueryResult<ProcessDefinition> qr = new QueryResult<ProcessDefinition>();
    String keyword = filter.getKeyword();
    List<Definition> defs = processService.getDefinitionsByKeyPrefix(Statements.OBJECT_NAME);
    if (defs == null || defs.isEmpty()) {
      List<ProcessDefinition> list = new ArrayList<ProcessDefinition>();
      qr.setRecords(list);
      return qr;
    }
    Set<String> keys = new HashSet<String>();
    for (Definition def : defs) {
      keys.add(def.getDefinitionKey());
    }
    Collection<String> canStartList = processService.canStartProcesses(keys, getSessionUser()
        .getId());
    List<ProcessDefinition> result = new ArrayList<ProcessDefinition>();
    for (Definition def : defs) {
      if (canStartList.contains(def.getDefinitionKey()) == false) {
        continue;
      }
      ProcessDefinition process = new ProcessDefinition();
      process.setKey(def.getDefinitionKey());
      process.setName(def.getName());
      if (StringUtil.isNullOrBlank(keyword) || def.getName().contains(keyword))
        result.add(process);
    }
    qr.setRecords(result);
    return qr;
  }

  protected Map<String, Object> getBpmVariables(Object obj, Map<String, Object> variables) {
    if (variables == null) {
      variables = Maps.newHashMap();
    }
    if (obj == null) {
      return variables;
    } else if (obj instanceof com.hd123.m3.commons.biz.entity.IsStandardBill) {
      com.hd123.m3.commons.biz.entity.IsStandardBill bill = (com.hd123.m3.commons.biz.entity.IsStandardBill) obj;
      variables.put(BpmConstants.KEY_UUID, bill.getUuid());
      variables.put(BpmConstants.KEY_BILLNUMBER, bill.getBillNumber());
      variables.put(BpmConstants.KEY_BILLCAPTION, "账单" + ":" + bill.getBillNumber());
      // 授权组
      variables.put(BpmConstants.KEY_GROUPS, getBpmPermGroup(bill));
      injectOrgVariables(variables, bill);
    }
    return variables;
  }

  /**
   * 获取授权组
   * 
   * @param obj
   *          对象
   * @return 授权组字符串
   */
  protected String getBpmPermGroup(Object obj) {
    if (obj == null || !permEnabled()) {
      // 对象为空
      return null;
    } else if (obj instanceof com.hd123.m3.commons.biz.entity.HasPerm) {
      com.hd123.m3.commons.biz.entity.HasPerm bill = (com.hd123.m3.commons.biz.entity.HasPerm) obj;
      return HasPerm.DEFAULT_PERMGROUP.equals(bill.getPermGroupId()) ? null : bill.getPermGroupId();
    }
    return null;
  }

  /** 是否启用授权组 */
  public boolean permEnabled() {
    return permEnabled(Statements.OBJECT_NAME);
  }

  protected boolean permEnabled(String objectName) {
    if (objectName == null) {
      return false;
    } else {
      return permOptionService.isPermEnabled(objectName);
    }
  }

  /**
   * 计入组织变量
   * 
   * @param variables
   *          变量map
   * @param bill
   *          对象
   */
  private void injectOrgVariables(Map<String, Object> variables, Object bill) {
    if (variables == null || bill == null) {
      return;
    }
    String uuid;
    if (bill instanceof com.hd123.m3.commons.biz.entity.HasStore) {
      com.hd123.rumba.commons.biz.entity.HasUCN store = ((com.hd123.m3.commons.biz.entity.HasStore) bill)
          .getStore();
      if (store == null) {
        return;
      }
      uuid = store.getUuid();
    } else {
      return;
    }
    if (uuid != null) {
      MStore store = storeService.get(uuid);
      if (store != null) {
        variables.put(BpmConstants.KEY_STORE, store.getUuid());
        variables.put(BpmConstants.KEY_ORGANIZATION, store.getOrgId());
        variables.put(BpmConstants.KEY_ORGNAME, store.getName());
      }
    }
  }

  /** 获取当前用户 */
  protected IsOperator getSessionUser() {
    Session session = Session.getInstance();
    return session != null ? session.getCurrentUser() : null;
  }
}
