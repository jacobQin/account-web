/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	AccountCpntsContants.java
 * 模块说明：	
 * 修改历史：
 * 2013-9-29 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.contants;

import com.google.gwt.core.client.GWT;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;

/**
 * @author chenrizhang
 * 
 */
public class AccountCpntsContants {

  /** 通用排序定义 */
  public static final String ORDER_BY_FIELD_CODE = "code";
  public static final String ORDER_BY_FIELD_NAME = "name";
  /** 查询条件定义 */
  public static final String CONDITION_COUNTERPARTTYPE = "counterpartType";

  /** 合同类型 */
  public static final String KEY_CONTRACT_CATEGORY = "investment/contract/category";

  public interface WidgetRes extends CommonsMessages {
    public static WidgetRes M = GWT.create(WidgetRes.class);

    @DefaultMessage("代码")
    String code();

    @DefaultMessage("名称")
    String name();

    @DefaultMessage("合同")
    String contract();

    @DefaultMessage("合同编号")
    String contract_number();

    @DefaultMessage("项目")
    String accountUnit();

    @DefaultMessage("商户")
    String counterpart();

    @DefaultMessage("对方单位类型")
    String counterpartType();

    @DefaultMessage("店招")
    String signboard();

    @DefaultMessage("品牌")
    String brand();

    @DefaultMessage("业态")
    String category();

    @DefaultMessage("楼层")
    String floor();

    @DefaultMessage("位置")
    String position();

    @DefaultMessage("楼宇")
    String building();

    @DefaultMessage(" 起始于")
    String startWith();

    @DefaultMessage(" 类似于")
    String like();

    @DefaultMessage("合同编号 起始于")
    String contractNnumberStartWith();

    @DefaultMessage("项目 类似于")
    String accountUnitLike();

    @DefaultMessage("商户 类似于")
    String counterpartLike();

    @DefaultMessage("店招 类似于")
    String signboardLike();

    @DefaultMessage("位置 类似于")
    String positionLike();

    @DefaultMessage("品牌 类似于")
    String brandLike();

    @DefaultMessage("楼层 类似于")
    String floorLike();

    @DefaultMessage("员工")
    String employee();

    @DefaultMessage("状态")
    String state();

    @DefaultMessage("科目")
    String subject();

    @DefaultMessage("账款科目")
    String subject_account();

    @DefaultMessage("预存款科目")
    String subject_deposit();

    @DefaultMessage("预付款科目")
    String subject_paymentDeposit();

    @DefaultMessage("预存款科目")
    String subject_receiptDeposit();

    @DefaultMessage("输入内容不能为空")
    String notNull();

    @DefaultMessage("说明")
    String remark();

    @DefaultMessage(" 等于")
    String equal();

    @DefaultMessage("对账组")
    String accountGroup();

    @DefaultMessage("银行资料")
    String bank();

    @DefaultMessage("付款方式")
    String paymentType();

    @DefaultMessage("结转期")
    String settleNo();

    @DefaultMessage("合同编号：未找到合同")
    String cannotFindContract();

    @DefaultMessage("合作方式")
    String coopMode();

    @DefaultMessage("合作方式 类似于")
    String coopModeLike();

    @DefaultMessage("合同类型")
    String contractCategory();

    @DefaultMessage("合同类型 等于")
    String contractCategoryEqual();

    @DefaultMessage("状态 等于")
    String stateEqual();
  }
}
