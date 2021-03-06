/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	PositionQueryBuilder.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-18 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.server.query;

import java.util.ArrayList;
import java.util.List;

import com.hd123.ia.author.service.group.GroupGroupMemberService;
import com.hd123.ia.author.service.group.GroupUserMemberService;
import com.hd123.m3.commons.biz.Basices;
import com.hd123.m3.commons.biz.entity.HasPerm;
import com.hd123.m3.commons.biz.option.PermOptionService;
import com.hd123.m3.commons.biz.store.MUserStoreService;
import com.hd123.m3.commons.rpc.M3ServiceFactory;
import com.hd123.m3.investment.service.res.building.BuildingService;
import com.hd123.m3.investment.service.res.floor.FloorService;
import com.hd123.rumba.commons.biz.query.QueryCondition;

/**
 * @author chenrizhang
 * 
 */
public class CommonQueryBuilder {

  public static List<QueryCondition> getPermConditions(String objectName, String userId) {
    List<QueryCondition> conditions = new ArrayList<QueryCondition>();

    if (isPermEnabled(objectName)) {
      QueryCondition c = new QueryCondition();
      c.setOperation(Basices.CONDITION_PERM_GROUP_ID_IN);
      List<String> list = getUserGroups(userId);
      if (list.size() > 0) {
        c.addParameter(list.toArray());
      }
      if (!list.contains(HasPerm.DEFAULT_PERMGROUP)) {
        c.addParameter(HasPerm.DEFAULT_PERMGROUP);
      }
      conditions.add(c);
    }

    QueryCondition c = new QueryCondition();
    c.setOperation(Basices.CONDITION_PERM_STORE_ID_IN);
    List<String> list = getUserStores(userId);
    if (list.size() > 0) {
      c.addParameter(list.toArray());
    }
    conditions.add(c);
    return conditions;
  }

  /**
   * 当前用户有权限的项目
   * 
   * @param userId
   *          用户id
   * @return 项目uuid列表
   */
  public static List<String> getUserStores(String userId) {
    if (userId == null) {
      return new ArrayList<String>();
    }
    return getUserStoreService().getUserStoreIds(userId);
  }

  public static List<String> getUserGroups(String userId) {
    List<String> groups = new ArrayList<String>();
    if (userId == null) {
      return groups;
    }

    List<String> list = getGroupUserMemberService().getOwners(userId, true, false);
    for (String id : list) {
      groups.add(id);
      List<String> childs = getGroupGroupMemberService().getMembers(id, true);
      for (String cid : childs) {
        groups.add(cid);
      }
    }

    return groups;
  }

  /** 是否启用授权组 */
  public static boolean isPermEnabled(String objectName) {
    if (objectName == null) {
      return true;
    }
    return getPermOptionService().isPermEnabled(objectName);
  }

  private static PermOptionService getPermOptionService() {
    return M3ServiceFactory.getBean(PermOptionService.DEFAULT_CONTEXT_ID, PermOptionService.class);
  }

  private static GroupUserMemberService getGroupUserMemberService() {
    return M3ServiceFactory.getBean(GroupUserMemberService.DEFAULT_CONTEXT_ID,
        GroupUserMemberService.class);
  }

  private static GroupGroupMemberService getGroupGroupMemberService() {
    return M3ServiceFactory.getBean(GroupGroupMemberService.DEFAULT_CONTEXT_ID,
        GroupGroupMemberService.class);
  }

  public static MUserStoreService getUserStoreService() {
    return M3ServiceFactory.getBean(MUserStoreService.DEFAULT_CONTEXT_ID, MUserStoreService.class);
  }
  
  public static BuildingService getBuildingService(){
    return M3ServiceFactory.getBean(BuildingService.DEFAULT_CONTEXT_ID, BuildingService.class);
  }
  
  public static FloorService getFloorService() {
    return M3ServiceFactory.getBean(FloorService.DEFAULT_CONTEXT_ID, FloorService.class);
  }
}
