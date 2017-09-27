/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementAdjust.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-10 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.statement.adjust.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.BStatementAdjust;
import com.hd123.m3.account.gwt.statement.adjust.client.biz.QStatement;
import com.hd123.m3.account.gwt.statement.adjust.client.ui.gadget.StatementBrowseFilter;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviService;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 同步接口
 * 
 * @author zhuhairui
 * 
 */
@RemoteServiceRelativePath("account/statementAdjust")
public interface StatementAdjustService extends M3ModuleService, M3ModuleNaviService {

  /** 持久化单元 */
  public static final String PU_ACCOUNT_STATEMENTADJUST = "account_statementAdjust";
  /** 键值: 是否启用对账组 */
  public static final String KEY_ENABLE_ACCOUNTGROUP = "useAccGroup";
  /** 键值：单据类型 */
  public static final String KEY_BILLTYPES = "billTypes";
  /** 键值：打印模板文件 */
  public static final String KEY_BQFILE = "_BQFile";
  /** 键值：账单调整单单据类型值 */
  public static final String KEY_STATEMENTADJUST_BILLTYPE = "statementAdjust_billType";
  /** 计算精度,小数位数 */
  public static final String SCALE = "scale";
  /** 计算精度,舍入算法 */
  public static final String ROUNDING_MODE = "RoundingMode";

  public static class Locator {
    private static StatementAdjustServiceAsync service = null;

    public static StatementAdjustServiceAsync getService() {
      if (service == null)
        service = GWT.create(StatementAdjustService.class);
      return service;
    }
  }

  /**
   * 按搜索条件查找账单调整单。
   * 
   * @param conditions
   *          搜索条件，not null。
   * @param visibleColumnNames
   *          显示的结果列集合。
   * @param pageSort
   *          分页和排序条件。
   * @return 账单调整单的分页数据。
   * @throws Exception
   */
  public RPageData<BStatementAdjust> query(List<Condition> conditions,
      List<String> visibleColumnNames, PageSort pageSort) throws Exception;

  /**
   * 查询账单。
   * 
   * @param filter
   *          账单的筛选条件。
   * @return 账单的分页数据。
   * @throws Exception
   */
  public RPageData<QStatement> queryStatement(StatementBrowseFilter filter) throws Exception;

  /**
   * 根据账单号,查询账单。
   * 
   * @param billNumber
   *          账单号，not null。
   * @return 账单的Q对象。
   * @throws Exception
   */
  public QStatement queryStatementByBillNum(String billNumber) throws Exception;

  /**
   * 创建账单调整单。
   * 
   * @return 账单调整单。
   * @throws Exception
   */
  public BStatementAdjust create() throws Exception;

  /**
   * 根据账单标识新建账款调整单。
   * 
   * @param statementUuid
   *          账单标识。not null。
   * @return 账款调整单。
   * @throws ClientBizException
   */
  public BStatementAdjust createByStatement(String statementUuid) throws ClientBizException;

  /**
   * 保存账单调整单。
   * 
   * @param statementAdjust
   *          账单调整单B对象。not null。
   * @return 账单调整单。
   * @throws Exception
   */
  public BStatementAdjust save(BStatementAdjust statementAdjust, BProcessContext task)
      throws Exception;

  /**
   * 加载账单调整单。
   * 
   * @param uuid
   *          账单调整单标识。not null 。
   * @param scence
   *          应用场景。not null 。
   * @return 账单调整单B对象。
   * @throws Exception
   */
  public BStatementAdjust load(String uuid, String scence) throws Exception;

  /**
   * 加载账单调整单。
   * 
   * @param billNumber
   *          账单调整单单号。not null。
   * @param scence
   *          应用场景。not null 。
   * @return 账单调整单。
   * @throws Exception
   */
  public BStatementAdjust loadByNumber(String billNumber, String scence) throws Exception;

  /**
   * 删除账单调整单。
   * 
   * @param uuid
   *          账单调整单标识。
   * @param version
   *          账单调整单版本号。
   * @throws Exception
   */
  public void delete(String uuid, long version) throws Exception;

  /**
   * 作废账单调整单。
   * 
   * @param uuid
   *          账单调整单标识。
   * @param version
   *          账单调整单版本号。
   * @param effectStatement
   *          账单调整单业务状态。
   * @param comment
   *          作废说明。
   * @throws Exception
   */
  public void abort(String uuid, long version, boolean effectStatement, String comment)
      throws Exception;

  /**
   * 根据商户uuid取其仅有的一份合同。如果商户只有一份有效合同，那么返回该合同，否则返回空。
   * 
   * @param counterpartUuid
   * @return
   * @throws Exception
   */
  public BContract getOnlyOneContractByCounterpart(String counterpartUuid, String counterpartType)
      throws Exception;

  /**
   * 根据账单标识取得账单。
   * 
   * @param uuid
   *          账单标识。not null。
   * @return 账单。
   * @throws Exception
   */
  public QStatement getStatementByUuid(String uuid) throws Exception;

  /**
   * 获取单据类型
   * 
   * @return 单据类型
   * @throws ClientBizException
   */
  public BBillType getBillType() throws ClientBizException;

  /**
   * 账单调整单生效
   * 
   * @param uuid
   *          账单调整单uuid。not null。
   * @param latestComment
   *          审核说明。
   * @param oca
   *          版本号。
   * @throws Exception
   */
  public void effect(String uuid, String comment, long oca) throws Exception;

  /**
   * 执行BPM任务。
   * 
   * @param operation
   *          任务操作参数。not null。
   * @param detailedTask
   *          任务详情。
   * @param entity
   *          账单调整单。not null。
   * @throws ClientBizException
   */
  public String executeTask(BOperation operation, BStatementAdjust entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException;

  /**
   * 验证结算单位、对方单位、合同数据合法性。
   * 
   * @param statement
   *          账单信息，not null。
   * @return 验证信息。
   * @throws ClientBizException
   */
  public String validateAccountUnitAndCounterpartAndContractByStatement(QStatement statement)
      throws ClientBizException;

}
