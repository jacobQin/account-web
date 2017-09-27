package com.hd123.m3.cre.controller.account.settle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.service.contract.AccountSettle;
import com.hd123.rumba.gwt.base.client.util.StringUtil;

/**
 * 
 * @author mengyinkun
 * 
 */
public class AccountSettleGroup implements Serializable {

  private static final long serialVersionUID = -5989319032533942030L;
  private String processDefKey;
  private String permGroupId;
  private String permGroupTitle;
  private String contractUuid;
  private List<AccountSettle> settles = new ArrayList<AccountSettle>();

  public String getProcessDefKey() {
    return processDefKey;
  }

  public void setProcessDefKey(String processDefKey) {
    this.processDefKey = processDefKey;
  }

  public String getPermGroupId() {
    return permGroupId;
  }

  public void setPermGroupId(String permGroupId) {
    this.permGroupId = permGroupId;
  }

  public String getPermGroupTitle() {
    return permGroupTitle;
  }

  public void setPermGroupTitle(String permGroupTitle) {
    this.permGroupTitle = permGroupTitle;
  }

  public String getContractUuid() {
    return contractUuid;
  }

  public void setContractUuid(String contractUuid) {
    this.contractUuid = contractUuid;
  }

  public List<AccountSettle> getSettles() {
    return settles;
  }

  public void setSettles(List<AccountSettle> settles) {
    if (settles == null || settles.isEmpty()) {
      this.settles.clear();
    } else {
      this.settles.clear();
      this.settles.addAll(settles);
    }
  }

  public String getCounterpartStr() {
    if (settles.isEmpty() == false && settles.get(0).getContract().getCounterpart() != null) {
      return settles.get(0).getContract().getCounterpart().getName() + "["
          + settles.get(0).getContract().getCounterpart().getCode() + "]";
    }
    return "";
  }

  public String getContractNumber() {
    if (settles.isEmpty() == false) {
      return settles.get(0).getContract().getBillNumber();
    }
    return "";
  }

  public String getSettleName() {
    StringBuilder sb = new StringBuilder();
    for (AccountSettle settle : settles) {
      if (StringUtil.isNullOrBlank(settle.getSettlement().getCaption())) {
        continue;
      }
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append(settle.getSettlement().getCaption());
    }
    return sb.toString();
  }

}
