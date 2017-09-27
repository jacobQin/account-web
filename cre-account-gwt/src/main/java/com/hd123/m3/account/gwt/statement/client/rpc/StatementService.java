/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementService.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-24 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.rpc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.statement.client.biz.BAccountSettleLog;
import com.hd123.m3.account.gwt.statement.client.biz.BAttachment;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementAccDetail;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementLine;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviService;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * 同步接口
 * 
 * @author huangjunxian
 * 
 */
@RemoteServiceRelativePath("account/statement")
public interface StatementService extends M3ModuleNaviService, M3ModuleService {

  /** 楼宇类型Key */
  public static final String KEY_BUILDING_TYPE = "buildingType";
  
  public static final String KEY_STATEMENT_TYPE = "statment_type";
  /** 计算精度,小数位数 */
  public static final String SCALE = "scale";
  /** 计算精度,舍入算法 */
  public static final String ROUNDING_MODE = "RoundingMode";
  /** 合作方式 */
  public static final String KEY_COOPMODES = "_value_CoopModes";

  public static class Locator {
    private static StatementServiceAsync service = null;

    public static StatementServiceAsync getService() {
      if (service == null)
        service = GWT.create(StatementService.class);
      return service;
    }
  }

  /**
   * 查询账单
   * 
   * @param conditions
   * @param visibleColumnNames
   * @param pageSort
   * @return
   * @throws ClientBizException
   */
  RPageData<BStatement> query(List<Condition> conditions, List<String> visibleColumnNames,
      PageSort pageSort) throws ClientBizException;

  /**
   * 根据uuid获取账单
   * 
   * @param uuid
   * @param startNode
   * @return
   * @throws ClientBizException
   */
  BStatement load(String uuid, String startNode) throws ClientBizException;

  /**
   * 根据单号获取账单
   * 
   * @param billNumber
   * @param startNode
   * @return
   * @throws ClientBizException
   */
  BStatement loadByNumber(String billNumber, String startNode) throws ClientBizException;

  /**
   * 删除账单
   * 
   * @param uuid
   * @param version
   * @throws ClientBizException
   */
  void remove(String uuid, long version) throws ClientBizException;

  /**
   * 生效账单
   * 
   * @param uuid
   * @param version
   * @throws ClientBizException
   */
  void effect(String uuid, long version) throws ClientBizException;

  /**
   * 作废账单
   * 
   * @param uuid
   * @param version
   * @throws ClientBizException
   */
  void abort(String uuid, long version) throws ClientBizException;

  /**
   * 查询未出帐账款
   * 
   * @param filter
   * @return
   * @throws ClientBizException
   */
  RPageData<BStatementLine> queryAccount(StatementAccFilter filter, Collection<String> excludedUuids)
      throws ClientBizException;

  /**
   * 根据id取得账款明细
   * 
   * @param id
   *          账款明细id
   * @return
   * @throws ClientBizException
   */
  BStatementAccDetail getAccountDetail(String id, boolean active) throws ClientBizException;

  /**
   * 
   * @return
   * @throws ClientBizException
   */
  BBillType getBillType() throws ClientBizException;

  /**
   * 获取单据类型
   * 
   * @return
   * @throws ClientBizException
   */
  Map<String, String> getBillTypeMap() throws ClientBizException;

  /**
   * 执行BPM任务。
   * 
   * @param operation
   *          任务操作参数。not null。
   * @param detailedTask
   *          任务详情。
   * @param entity
   *          预付款单。not null。
   * @throws ClientBizException
   */
  public String executeTask(BOperation operation, BStatement entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException;

  /**
   * 查询账单出账日志。
   * 
   * @param conditions
   * @param pageSort
   * @return
   * @throws ClientBizException
   */
  RPageData<BAccountSettleLog> queryAccountSettleLog(List<Condition> conditions, PageSort pageSort)
      throws ClientBizException;

  /**
   * 添加附件。
   * 
   * @param fileName
   *          文件名。
   * @return 附件。
   * @throws ClientBizException
   */
  public BAttachment uploadFile(String fileName) throws ClientBizException;

  /**
   * 新建账单(type=补录单)。
   * 
   * @return 账单(type=补录单)。
   * @throws ClientBizException
   */
  public BStatement create() throws ClientBizException;

  /**
   * 保存账单。
   * <p/>
   * 新建时，type=normal为正常单据。
   * <p/>
   * 编辑时，type=patch为补录单。
   * 
   * 
   * @param entity
   *          账单。
   * @param task
   *          BPM任务
   * @return 账单。
   * @throws ClientBizException
   */
  public BStatement save(BStatement entity, BProcessContext task) throws ClientBizException;

  /**
   * 根据商户uuid取其仅有的一份合同。如果商户只有一份有效合同，那么返回该合同，否则返回空。
   * 
   * @param counterpartUuid
   * @return
   * @throws Exception
   */
  BContract getOnlyOneContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws ClientBizException;

  /**
   * 查询账单有效明细
   * 
   * @param filter
   * @return
   * @throws ClientBizException
   */
  List<BStatementLine> queryLines(StatementAccFilter filter, boolean includeSelf)
      throws ClientBizException;

  /**
   * 查询账单无效明细
   * 
   * @param filter
   * @return
   * @throws ClientBizException
   */
  List<BStatementLine> queryDesLines(StatementAccFilter filter, boolean includeSelf)
      throws ClientBizException;

  /**
   * 查询账单账款详情
   * 
   * @param filter
   * @param includeSelf
   * @return
   */
  List<BStatementAccDetail> queryAccDetails(StatementAccFilter filter, boolean includeSelf)
      throws ClientBizException;

  /**
   * 根据代码获取科目。
   * 
   * @param code
   * @return
   */
  BUCN getSubjectByCode(String code);

  /**
   * 账单删除前的数据验证。
   * 
   * @param uuid
   *          账单uuid，not null。
   * @return 验证错误消息。
   */
  String validateBeforeAction(String uuid, String action);

  /**
   * 恢复出账
   * 
   * @param uuid
   *          出账记录uuid,not null。
   */
  void cleanSettle(String uuid) throws ClientBizException;

  Boolean isMaxSettled(String accountSettleUuid);
}
