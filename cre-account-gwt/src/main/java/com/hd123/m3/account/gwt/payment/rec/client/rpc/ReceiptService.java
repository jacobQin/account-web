/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	ReceiveService.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-4 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.client.rpc;

import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.bpm.widget.interaction.client.BProcessContext;
import com.hd123.bpm.widget.interaction.client.BTaskContext;
import com.hd123.bpm.widget.interaction.client.biz.BOperation;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPayment;
import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.m3.account.gwt.payment.rec.client.biz.BReceiptConfig;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleNaviService;
import com.hd123.m3.commons.gwt.base.client.rpc.M3ModuleService;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.PageSort;
import com.hd123.rumba.gwt.base.client.PageSort.Order;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;

/**
 * @author subinzhu
 * 
 */
@RemoteServiceRelativePath("account/receipt")
public interface ReceiptService extends M3ModuleService, M3ModuleNaviService {

  /** 持久化单元 */
  public static final String PU_ACCOUNT_RECEIPT = "account_receipt";
  /** 单据类型 */
  public static final String KEY_BILL_TYPE = "billType";
  /** 键值: 默认配置 */
  public static final String KEY_DEFAULTOPTION = "defaultOption";
  /** 键值：付款方式 */
  public static final String KEY_PAYMENTTYPES = "paymentTypes";
  /** 键值：银行 */
  public static final String KEY_BANKS = "banks";
  /** 计算精度,小数位数 */
  public static final String SCALE = "scale";
  /** 计算精度,舍入算法 */
  public static final String ROUNDING_MODE = "roundingMode";
  /** 合作方式 */
  public static final String KEY_COOPMODES = "_value_CoopModes";
  /** 收款单滞纳金默认值 */
  public static final String KEY_RECEIPT_OVERDUE_DEFAULTS = "_value_ReceiptOverdueDefaults";

  public static class Locator {
    private static ReceiptServiceAsync service = null;

    public static ReceiptServiceAsync getService() {
      if (service == null)
        service = GWT.create(ReceiptService.class);
      return service;
    }
  }

  /**
   * 查询收付款单
   * 
   * @param conditions
   * @param pageSort
   * @return
   * @throws Exception
   */
  public RPageData<BPayment> query(List<Condition> conditions, PageSort pageSort) throws Exception;

  /**
   * 新建收款单
   * 
   * @return
   * @throws Exception
   */
  public BPayment create() throws Exception;

  /**
   * 根据账单创建收款单。
   * 
   * @param statementUuids
   *          账单uuid集合，not null。
   * @return 收款单。
   * @throws Exception
   */
  BPayment createByStatements(Collection<String> statementUuids) throws Exception;

  /**
   * 根据发票登记单创建收款单。
   * 
   * @param invoiceUuids
   *          登记单uuid集合，not null。
   * @return 收款单。
   * @throws Exception
   */
  BPayment createByInvoices(Collection<String> invoiceUuids) throws Exception;

  /**
   * 根据通知单创建收款单。
   * 
   * @param noticeUuids
   *          通知单uuid集合，not null。
   * @return 收款单。
   * @throws Exception
   */
  BPayment createByPaymentNotices(Collection<String> noticeUuids) throws Exception;

  /**
   * 根据来源单据创建收款单。
   * 
   * @param sourceBillUuids
   *          来源单据uuid集合，not null。
   * @return 收款单。
   * @throws Exception
   */
  BPayment createBySourceBills(Collection<String> sourceBillUuids) throws Exception;

  /**
   * 根据账款创建收款单。
   * 
   * @param accountUuids
   *          账款uuid集合，not null。
   * @return 收款单。
   * @throws Exception
   */
  BPayment createByAccounts(Collection<String> accountUuids) throws Exception;
  
  /**
   * 根据账款accId创建收款单
   * 
   * @param accountIds
   */
  BPayment createByAccountIds(Collection<String> accountIds) throws Exception;

  /**
   * 保存收款单
   * 
   * @param bill
   * @return 返回保存后收款单的uuid
   * @throws Exception
   */
  public BPayment save(BPayment bill, BProcessContext task) throws Exception;

  /**
   * 删除收款单
   * 
   * @param uuid
   * @param oca
   * @throws Exception
   */
  public void delete(String uuid, long oca) throws Exception;

  /**
   * 作废收款单
   * 
   * @param uuid
   *          收款单uuid。not null。
   * @param comment
   *          作废说明。
   * @param oca
   * @throws Exception
   */
  public void abort(String uuid, String comment, long oca) throws Exception;

  /**
   * 生效收款单
   * 
   * @param uuid
   * @param comment
   *          说明。
   * @param oca
   * @throws Exception
   */
  public void effect(String uuid, String comment, long oca) throws Exception;

  /**
   * 执行BPM任务。
   * 
   * @param outgoing
   *          任务出口。not null。
   * @param detailedTask
   *          任务详情。
   * @param entity
   *          付款单。not null。
   * @throws ClientBizException
   */
  public String executeTask(BOperation operation, BPayment entity, BTaskContext task,
      boolean saveBeforeAction) throws ClientBizException;

  /**
   * 根据uuid获取收付款单
   * 
   * @param uuid
   *          指定收付款单的uuid。not null。
   * @param isForEdit
   *          是否是编辑界面加载，是的话会加载账款的滞纳金条款。
   * @return 返回收付款单
   * @throws Exception
   */
  public BPayment load(String uuid, boolean isForEdit) throws Exception;

  /**
   * 根据单号获取收付款单
   * 
   * @param billNumber
   *          指定收付款单的单号。not null。
   * @param isForEdit
   *          是否是编辑界面加载，是的话会加载账款的滞纳金条款。
   * @return 返回收付款单
   * @throws Exception
   */
  public BPayment loadByNumber(String billNumber, boolean isForEdit) throws Exception;

  /**
   * 加载收款配置
   * 
   * @return 返回收款配置
   * @throws Exception
   */
  public BReceiptConfig loadConfig() throws Exception;

  /**
   * 保存收款配置
   * 
   * @param 待保存的收款配置
   * @throws Exception
   */
  public void saveConfig(BReceiptConfig config) throws Exception;

  /**
   * 加载待发票登记的收款单
   * 
   * @param uuid
   * @return
   * @throws Exception
   */
  public BPayment loadRegiste(String uuid) throws Exception;

  /**
   * 对明细进行排序
   * 
   * @param lines
   *          原明细。
   * @param orders
   *          排序定义
   * @return 排序后的明细
   * @throws Exception
   */
  public List<BPaymentAccountLine> sortLines(List<BPaymentAccountLine> lines, List<Order> orders)
      throws Exception;

  /**
   * 根据商户uuid取其仅有的一份合同。如果商户只有一份有效合同，那么返回该合同，否则均返回空。
   * 
   * @param accountUnitUuid
   * @param counterpartUuid
   * @return
   * @throws ClientBizException
   */
  public BContract getContract(String accountUnitUuid, String counterpartUuid,
      String counterpartType) throws ClientBizException;

  /**
   * 根据指定的合同-科目对应的核算主体对传入的科目进行过滤，过滤规则如下：
   * <p>
   * <li>如果指定的合同-科目对应的核算主体对象为空，则直接返回原列表；
   * <li>如果列表中科目-合同对应的核算主体为空，则保留该科目；
   * <li>如果比较科目与被比较科目与合同对应的核算主体皆存在，不相等则从列表中移除。
   * 
   * @param subjects
   *          被比较科目，传入空或者空列表直接返回空列表。
   * @param store
   *        项目，不允许为空。
   * @param subject
   *          比较科目，传入空直接返回原列表。
   * @param contract
   *          合同，传入空直接返回原列表。
   * @throws ClientBizException
   */
  public List<BUCN> filterSubjects(List<BUCN> subjects, BUCN store, BUCN subject, BUCN contract)
      throws ClientBizException;
}
