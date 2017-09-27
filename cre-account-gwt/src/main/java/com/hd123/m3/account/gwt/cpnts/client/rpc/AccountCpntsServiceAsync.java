/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	AccountCpntsServiceAsync.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-29 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.rpc;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;
import com.hd123.rumba.webframe.gwt.base.client.ModuleServiceAsync;

/**
 * @author chenrizhang
 * 
 */
public interface AccountCpntsServiceAsync extends ModuleServiceAsync {

  void queryEmployees(CodeNameFilter filter, List<String> excepts,
      AsyncCallback<RPageData<BEmployee>> callback);

  void getEmployeeByCode(String code, AsyncCallback<BEmployee> callback);

  void queryAccountUnits(CodeNameFilter filter, Boolean state,
      AsyncCallback<RPageData<BUCN>> callback);

  void getAccountUnitByCode(String code, Boolean state, AsyncCallback<BUCN> callback);

  void loadStoreNavigator(AsyncCallback<List<BStoreNavigator>> callback);

  void getCounterpartConfig(AsyncCallback<Map<String, String>> callback);

  void queryCounterparts(
      CodeNameFilter filter,
      AsyncCallback<RPageData<com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart>> callback);

  void getCounterpartByCode(String code, Map<String, Object> filter,
      AsyncCallback<com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart> callback);

  void queryCounterparts(CodeNameFilter filter, Boolean state,
      AsyncCallback<RPageData<BCounterpart>> callback);

  void getCounterpartByCode(String code, Boolean state, AsyncCallback<BCounterpart> callback);

  void querySubject(SubjectFilter filter, AsyncCallback<RPageData<BSubject>> callback);

  void getSubjectByCode(SubjectFilter filter, AsyncCallback callback);

  void queryContracts(ContractFilter filter, AsyncCallback<RPageData<BContract>> callback);

  void getContractByNumber(ContractFilter filter, AsyncCallback<BContract> callback);

  void querySettles(SettleNoFilter filter, AsyncCallback<RPageData<BMonthSettle>> callback);

  void getSettleByNumber(String number, AsyncCallback<String> callback);

  void queryFloor(CodeNameFilter filter, AsyncCallback<RPageData<BUCN>> callback);

  void getFloorByCode(String code, AsyncCallback<BUCN> callback);

  void queryPosition(String type, CodeNameFilter filter, AsyncCallback<RPageData<TypeBUCN>> callback);

  void getPositionByCode(String type, String code, AsyncCallback<TypeBUCN> callback);

  void queryPositions(PositionFilter filter, AsyncCallback<RPageData<SPosition>> callback);

  void getPositionSubTyoes(AsyncCallback<Map<String, List<String>>> callback);

  void getPositionByCode(PositionFilter filter, AsyncCallback<SPosition> callback);

  void queryCategories(CodeNameFilter filter, AsyncCallback<RPageData<BUCN>> callback);

  void getCategoryByCode(String code, AsyncCallback<BUCN> callback);

  void queryBuildings(String type, CodeNameFilter filter,
      AsyncCallback<RPageData<TypeBUCN>> callback);

  void getBuildingByCode(String type, String code, AsyncCallback<TypeBUCN> callback);

  void queryInvoiceStocks(CodeNameFilter filter, AsyncCallback<RPageData<BInvoiceStock>> callback);

  void getInvoiceStockByNumber(String number, Map<String, Object> filter,
      AsyncCallback<BInvoiceStock> callback);

  void queryStores(CodeNameFilter filter, AsyncCallback<RPageData<BUCN>> callback);

  void getStoreByCode(String code, Map<String, Object> filter, AsyncCallback<BUCN> callback);

  void queryTenants(CodeNameFilter filter, AsyncCallback<RPageData<BUCN>> callback);

  void getTenantByCode(String code, Map<String, Object> filter, AsyncCallback<BUCN> callback);
}
