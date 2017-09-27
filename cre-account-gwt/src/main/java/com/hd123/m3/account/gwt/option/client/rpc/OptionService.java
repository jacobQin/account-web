/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-basic
 * 文件名：	OptionService.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-13 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.option.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hd123.m3.account.gwt.commons.client.biz.BConfigOption;
import com.hd123.m3.account.gwt.commons.client.biz.BDefaultOption;
import com.hd123.m3.account.gwt.commons.client.biz.BOption;
import com.hd123.m3.account.gwt.commons.client.biz.BProcessDefinition;
import com.hd123.m3.account.gwt.option.client.BIncompleteMonthByRealDaysOption;
import com.hd123.m3.account.gwt.option.client.BRebateByBillOption;
import com.hd123.m3.account.gwt.option.client.BStoreMonthDaysOption;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.exception.ClientBizException;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
import com.hd123.rumba.gwt.widget2e.client.dialog.CodeNameFilter;
import com.hd123.rumba.webframe.gwt.base.client.ModuleService;

/**
 * @author zhuhairui
 * 
 */
@RemoteServiceRelativePath("account/option")
public interface OptionService extends ModuleService {
  
  public static final String KEY_INVOICE_TYPE = "invoiceType";

  /** 账单流程定义路径 */
  public static final String KEY_STATEMENTBPMKEYPREFIX = "_statementBpmKeyPrefix";
  /** 账单默认流程Key */
  public static final String KEY_STATEMENTDEFAULTBPMKEY = "_statementDefaultBpmKey";
  /** 收付款通知单流程定义路径 */
  public static final String KEY_PAYMENTNOTICEBPMKEYPREFIX = "_paymentNoticeBpmKeyPrefix";
  /** 收付款通知单默认流程Key */
  public static final String KEY_PAYMENTNOTICEDEFAULTBPMKEY = "_paymentNoticeDefaultBpmKey";

  public static class Locator {
    private static OptionServiceAsync service = null;

    public static OptionServiceAsync getService() {
      if (service == null)
        service = GWT.create(OptionService.class);
      return service;
    }
  }

  /**
   * 获取结算配置。
   * 
   * @return 结算配置
   * @throws Exception
   */
  public BOption getOption() throws Exception;

  /**
   * 获取结算配置的配置项。
   * 
   * @return 结算配置配置项
   * @throws Exception
   */
  public BConfigOption getConfigOption() throws Exception;

  /**
   * 获取结算配置的默认值。
   * 
   * @return 结算配置默认值
   * @throws Exception
   */
  public BDefaultOption getDefaultOption() throws Exception;

  /**
   * 保存配置项。
   * 
   * @param configOption
   * @throws Exception
   */
  public void setConfigOption(BConfigOption configOption) throws Exception;

  /**
   * 保存默认值。
   * 
   * @param defaultOption
   * @throws Exception
   */
  public void setDefaultOption(BDefaultOption defaultOption) throws Exception;

  /**
   * 保存项目对应的财务月天数配置
   * 
   * @param options
   *          配置项集合
   * @throws Exception
   */
  public void saveStoreMonthDaysOptions(List<BStoreMonthDaysOption> options) throws Exception;

  /**
   * 获取项目对应的财务月天数配置集合。
   * 
   * @return 至少返回一个空集合。
   * 
   * @throws Exception
   */
  public List<BStoreMonthDaysOption> getStoreMonthDaysOptions() throws Exception;
  
  
  /**
   * 获取项目对应的非整月月末是否按实际天数计算配置
   * 
   * @return 至少返回一个空集合。
   * 
   * @throws Exception
   */
  public List<BIncompleteMonthByRealDaysOption> getIncompleteMonthByRealDaysOptions()
      throws Exception;

  /**
   * 保存项目对应的非整月月末是否按实际天数计算配置
   * 
   * @param options
   *          配置项集合
   * @throws Exception
   */
  public void saveIncompleteMonthByRealDaysOption(List<BIncompleteMonthByRealDaysOption> options)
      throws Exception;
  
  /**
   * 保存返款单是否按笔返款配置
   * @param options 配置项集合
   * @throws
   */
  public void saveRebateByBillOption(List<BRebateByBillOption> options) throws Exception;
  
  /**
   * 获取返款单是否按笔返款配置
   * @return 返回配置集合，至少返回一个空集合
   * @throws Exception
   */
  public List<BRebateByBillOption> getRebateByBillOptions() throws Exception;

  /**
   * 根据代码获取科目。
   * 
   * @param code
   * @return 科目
   * @throws Exception
   */
  public BUCN getSubjectByCode(String code) throws Exception;

  /**
   * 获取科目列表。
   * 
   * @param filter
   * @return 科目列表
   * @throws Exception
   */
  public RPageData<BUCN> querySubjects(CodeNameFilter filter) throws Exception;

  /**
   * 根据代码获取付款方式。
   * 
   * @param code
   * @return 付款方式
   * @throws Exception
   */
  public BUCN getPaymentTypeByCode(String code) throws Exception;

  /**
   * 获取付款方式列表。
   * 
   * @param filter
   * @return 付款方式列表
   * @throws Exception
   */
  public RPageData<BUCN> queryPaymentTypes(CodeNameFilter filter) throws Exception;

  /**
   * 查询流程定义。
   * 
   * @param filter
   *          流程定义的筛选条件。
   * @return 流程定义的分页数据。
   * @throws Exception
   */
  public List<BProcessDefinition> queryProcessDefinition(String keyPrefix) throws Exception;

  /**
   * 获取指定的流程定义
   * 
   * @param key
   *          流程定义key
   * @return
   * @throws Exception
   */
  public BProcessDefinition getProcessDefinition(String key) throws ClientBizException;

  /**
   * 获取付款方式列表
   * 
   * @return 付款方式列表
   * @throws Exception
   */
  public List<BUCN> getPaymentTypes() throws Exception;
}
