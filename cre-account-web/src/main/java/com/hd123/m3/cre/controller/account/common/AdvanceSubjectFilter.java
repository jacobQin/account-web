/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	cre-account-web
 * 文件名：	AdvanceSubjectFilter.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月19日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.common;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.commons.servlet.biz.query.QueryFilter;

/**
 * 预存款科目查询参数
 * 
 * @author LiBin
 *
 */
public class AdvanceSubjectFilter extends QueryFilter {
  
  // 收付方向
  public static final String DIRECTION_TYPE = "directionType";
  // 对方单位uuid
  public static final String COUNTERPART_UUID = "counterpartUuid";
  // 合同uuid
  public static final String CONTRACT_UUID = "contractUuid";
  // 项目uuid
  public static final String ACCOUNTUNIT_UUID = "storeUuid";

  public int getDirectionType() {
    Integer direction = (Integer) getFilter().get(DIRECTION_TYPE);
    return direction == null ? Direction.RECEIPT : direction.intValue();
  }

  public String getCounterpartUuid() {
    return (String) getFilter().get(COUNTERPART_UUID);
  }

  public String getContractUuid() {
    return (String) getFilter().get(CONTRACT_UUID);
  }

  public String getAccountUnitUuid() {
    return (String) getFilter().get(ACCOUNTUNIT_UUID);
  }

}
