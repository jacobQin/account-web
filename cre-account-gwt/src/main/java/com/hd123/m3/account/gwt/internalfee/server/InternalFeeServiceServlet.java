/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InternalFeeServiceServlet.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.server;

import java.util.List;

import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.base.server.AccBPMServiceServlet;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.rpc.InternalFeeService;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;

/**
 * @author liuguilin
 * 
 */
public class InternalFeeServiceServlet extends AccBPMServiceServlet implements InternalFeeService {

  @Override
  public RPageData<BInternalFee> query(List<Condition> conditions, PageSort pageSort)
      throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BInternalFee load(String uuid) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BInternalFee loadByNumber(String number) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BInternalFee create() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BInternalFee save(BInternalFee bill, BProcessContext task) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(String uuid, long version) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void effect(String uuid, String comment, long version) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void abort(String uuid, String comment, long version) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public String executeTask(BOperation operation, BInternalFee entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public RPageData<BUCN> queryVendors(CodeNameFilter filter, Boolean state, Boolean freezed)
      throws ClientBizException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BUCN getVendorByCode(String code, Boolean state, Boolean freezed)
      throws ClientBizException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String getObjectCaption() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getObjectName() {
    // TODO Auto-generated method stub
    return null;
  }
}
