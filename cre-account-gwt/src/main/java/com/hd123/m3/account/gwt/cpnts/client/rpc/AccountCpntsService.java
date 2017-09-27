/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	AccountCpntsService.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-29 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.rpc;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.commons.client.biz.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.biz.BEmployee;
import com.hd123.m3.account.gwt.cpnts.client.biz.BMonthSettle;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.TypeBUCN;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.ContractFilter;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.SettleNoFilter;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.SubjectFilter;
import com.hd123.m3.account.gwt.cpnts.client.ui.invoice.BInvoiceStock;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.PositionFilter;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.SPosition;
import com.hd123.m3.account.gwt.cpnts.client.ui.store.BStoreNavigator;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;
import com.hd123.rumba.webframe.gwt.base.client.ModuleService;

/**
 * @author chenrizhang
 * 
 */
@RemoteServiceRelativePath("account/common/cpnts")
public interface AccountCpntsService extends ModuleService {
  public static class Locator {
    private static AccountCpntsServiceAsync service = null;

    public static AccountCpntsServiceAsync getService() {
      if (service == null)
        service = GWT.create(AccountCpntsService.class);
      return service;
    }
  }

  /**
   * 查询员工
   * 
   * @param filter
   * @param state
   * @return 员工列表
   * @throws ClientBizException
   */
  RPageData<BEmployee> queryEmployees(CodeNameFilter filter, List<String> excepts)
      throws ClientBizException;

  /**
   * 根据代码查找员工。
   * 
   * @param code
   * @param state
   * @return 员工。
   * @throws ClientBizException
   */
  BEmployee getEmployeeByCode(String code) throws ClientBizException;

  /**
   * 查询结算单位
   * 
   * @param filter
   * @return 项目列表
   * @throws ClientBizException
   */
  RPageData<BUCN> queryAccountUnits(CodeNameFilter filter, Boolean state) throws ClientBizException;

  /**
   * 根据代码查找结算单位。
   * 
   * @param code
   * @return 项目。
   * @throws ClientBizException
   */
  BUCN getAccountUnitByCode(String code, Boolean state) throws ClientBizException;

  /** 加载项目导航 */
  List<BStoreNavigator> loadStoreNavigator() throws Exception;

  /** 取得对方单位配置项 */
  Map<String, String> getCounterpartConfig() throws Exception;

  /**
   * 查询对方单位
   * 
   * @param filter
   * @return 商户列表
   * @throws Exception
   */
  RPageData<com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart> queryCounterparts(
      CodeNameFilter filter) throws Exception;

  /**
   * 根据代码查找对方单位。
   * 
   * @param code
   * @return 商户。
   * @throws Exception
   */
  com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart getCounterpartByCode(
      String code, Map<String, Object> filter) throws Exception;

  /**
   * 查询对方单位
   * 
   * @param filter
   * @return 商户列表
   * @throws ClientBizException
   */
  RPageData<BCounterpart> queryCounterparts(CodeNameFilter filter, Boolean state)
      throws ClientBizException;

  /**
   * 根据代码查找对方单位。
   * 
   * @param code
   * @return 商户。
   * @throws ClientBizException
   */
  BCounterpart getCounterpartByCode(String code, Boolean state) throws ClientBizException;

  /**
   * 获取预存款科目。
   * 
   * @param filter
   * @return
   * @throws ClientBizException
   */
  RPageData<BSubject> querySubject(SubjectFilter filter) throws ClientBizException;

  /**
   * 通过代码取得预存款科目。
   * 
   * @param filter
   * @return 科目
   * @throws ClientBizException
   */
  BSubject getSubjectByCode(SubjectFilter filter) throws ClientBizException;

  /**
   * 获取合同。
   * 
   * @param filter
   * @return
   * @throws ClientBizException
   */
  RPageData<BContract> queryContracts(ContractFilter filter) throws ClientBizException;

  /**
   * 根据单号获取合同。
   * 
   * @param filter
   * @return
   * @throws ClientBizException
   */
  BContract getContractByNumber(ContractFilter filter) throws ClientBizException;

  /**
   * 查询结转期。
   * 
   * @param filter
   * @return
   * @throws ClientBizException
   */
  public RPageData<BMonthSettle> querySettles(SettleNoFilter filter) throws ClientBizException;

  /**
   * 根据结转期号读取结转期。
   * 
   * @param number
   *          指定结转期号。
   * @return 结转期号。
   * @throws ClientBizException
   */
  public String getSettleByNumber(String number) throws ClientBizException;

  /**
   * 查询楼层
   * 
   * @param filter
   * @return
   * @throws ClientBizException
   */
  RPageData<BUCN> queryFloor(CodeNameFilter filter) throws ClientBizException;

  /**
   * 根据代码获取楼层
   * 
   * @param code
   * @return
   * @throws ClientBizException
   */
  BUCN getFloorByCode(String code) throws ClientBizException;

  /**
   * 查询位置
   * 
   * @param type
   *          位置类型
   * @param filter
   *          查询定义
   * @return
   * @throws ClientBizException
   */
  RPageData<TypeBUCN> queryPosition(String type, CodeNameFilter filter) throws ClientBizException;

  /**
   * 根据代码获取位置
   * 
   * @param type
   *          位置类型
   * @param code
   *          位置代码
   * @return
   * @throws ClientBizException
   */
  TypeBUCN getPositionByCode(String type, String code) throws ClientBizException;

  /**
   * 取得铺位子类型定义
   * 
   * @return 子类型
   * @throws ClientBizException
   */
  Map<String, List<String>> getPositionSubTyoes() throws ClientBizException;

  /**
   * 查询位置
   * 
   * @param type
   *          位置类型
   * @param filter
   *          查询定义
   * @return
   * @throws ClientBizException
   */
  RPageData<SPosition> queryPositions(PositionFilter filter) throws ClientBizException;

  /**
   * 根据代码获取位置
   * 
   * @param type
   *          位置类型
   * @param code
   *          位置代码
   * @return
   * @throws ClientBizException
   */
  SPosition getPositionByCode(PositionFilter filter) throws ClientBizException;

  /**
   * 查询业态
   * 
   * @param filter
   *          查询定义
   * @throws ClientBizException
   */
  RPageData<BUCN> queryCategories(CodeNameFilter filter) throws ClientBizException;

  /**
   * 根据代码获取业态
   * 
   * @param code
   *          业态代码
   * @throws ClientBizException
   */
  BUCN getCategoryByCode(String code) throws ClientBizException;

  /**
   * 查询楼宇
   * 
   * @param type
   *          楼宇类型
   * @param filter
   *          查询定义
   * @throws ClientBizException
   */
  RPageData<TypeBUCN> queryBuildings(String type, CodeNameFilter filter) throws ClientBizException;

  /**
   * 根据代码获取楼宇
   * 
   * @param type
   *          楼宇类型
   * @param code
   *          楼宇代码
   * @throws ClientBizException
   */
  TypeBUCN getBuildingByCode(String type, String code) throws ClientBizException;

  /**
   * 查询发票
   * 
   * @param filter
   *          查询定义
   * @throws ClientBizException
   */
  RPageData<BInvoiceStock> queryInvoiceStocks(CodeNameFilter filter) throws ClientBizException;

  /**
   * 根据号码获取发票
   * 
   * @param number
   *          号码
   * @param filter
   *          查询定义
   * @throws ClientBizException
   */
  BInvoiceStock getInvoiceStockByNumber(String number, Map<String, Object> filter)
      throws ClientBizException;

  /**
   * 查询项目
   * @param filter
   * @return
   * @throws Exception
   */
  RPageData<BUCN> queryStores(CodeNameFilter filter) throws Exception;
  
  /**
   * 查询商户
   * @param filter
   * @return
   * @throws Exception
   */
  RPageData<BUCN> queryTenants(CodeNameFilter filter) throws Exception;
  
  BUCN getStoreByCode(String code, Map<String, Object> filter) throws Exception;
  
  BUCN getTenantByCode(String code, Map<String, Object> filter) throws Exception;
  
}
