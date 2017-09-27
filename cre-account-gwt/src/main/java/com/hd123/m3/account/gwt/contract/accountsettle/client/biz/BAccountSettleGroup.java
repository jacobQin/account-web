/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	BAccountSettleGroup.java
 * 模块说明：	
 * 修改历史：
 * 2015-6-18 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.contract.accountsettle.client.biz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * @author huangjunxian
 * 
 */
public class BAccountSettleGroup implements Serializable {
  private static final long serialVersionUID = -6268210613808096808L;

  private String contractUuid;
  private List<BAccountSettle> settles = new ArrayList<BAccountSettle>();

  public String getContractUuid() {
    return contractUuid;
  }

  public void setContractUuid(String contractUuid) {
    this.contractUuid = contractUuid;
  }

  public List<BAccountSettle> getSettles() {
    return settles;
  }

  public void setSettles(List<BAccountSettle> settles) {
    if (settles == null || settles.isEmpty()) {
      this.settles.clear();
    } else {
      this.settles.clear();
      this.settles.addAll(settles);
    }
  }

  public String toCounterpartStr() {
    if (settles.isEmpty() == false && settles.get(0).getCounterpart() != null) {
      return settles.get(0).getCounterpart().toFriendlyStr();
    }
    return "";
  }

  public String toContractNumber() {
    if (settles.isEmpty() == false) {
      return settles.get(0).getContractNumber();
    }
    return "";
  }

  public String toSettleName() {
    StringBuilder sb = new StringBuilder();
    for (BAccountSettle settle : settles) {
      if (StringUtil.isNullOrBlank(settle.getSettleName())) {
        continue;
      }
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append(settle.getSettleName());
    }
    return sb.toString();
  }

}
