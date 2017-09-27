/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	AccountAction.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-11 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.base.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.hd123.rumba.webframe.gwt.base.client.permission.BAction;

/**
 * 账务系统操作
 * 
 * @author huangjunxian
 * 
 */
public class AccountAction extends BAction {

  private static final long serialVersionUID = 3266671950420594794L;
  public static final M M;

  /** 启用操作。 */
  public static final AccountAction ENABLE;
  /** 停用操作。 */
  public static final AccountAction DISABLE;
  /** 生效操作。 */
  public static final AccountAction EFFECT;
  /** 作废操作。 */
  public static final AccountAction ABORT;
  /** 导入操作 */
  public static final AccountAction IMPORT;
  /** 冻结操作。 */
  public static final AccountAction FREEZE;
  /** 解冻操作。 */
  public static final AccountAction UNFREEZE;
  /** 出账权 */
  public static final AccountAction ACCSETTLE;
  /** 选项权 */
  public static final AccountAction CONFIG;
  /** 账款修改权 */
  public static final AccountAction MODIFYACCOUNT;
  /** 账单添加权 */
  public static final AccountAction ADDFROMSTATEMENT;
  /** 发票登记单添加权 */
  public static final AccountAction ADDFROMINVOICE;
  /** 收付款通知单添加权 */
  public static final AccountAction ADDFROMPAYNOTICE;
  /** 来源单据添加权 */
  public static final AccountAction ADDFROMSRCBILL;
  /** 账款添加权 */
  public static final AccountAction ADDFROMACCOUNT;
  /** 附件编辑权 */
  public static final AccountAction ATTACHMENTEDIT;
  /** 发票登记 */
  public static final AccountAction IVCREGISTE;

  /** 打印操作。 */
  public static final BAction PRINT;
  /** 打印导出操作。 */
  public static final BAction PRINTEXPORT;
  static {
    if (GWT.isClient()) {
      M = GWT.create(M.class);
    } else {
      M = new MImpl();
    }
    ENABLE = new AccountAction("enable", M.enalbe());
    DISABLE = new AccountAction("disable", M.disable());
    EFFECT = new AccountAction("effect", M.effect());
    ABORT = new AccountAction("abort", M.abort());
    IMPORT = new AccountAction("import", M.imp());
    FREEZE = new AccountAction("freeze", M.freeze());
    UNFREEZE = new AccountAction("unfreeze", M.unfreeze());
    ACCSETTLE = new AccountAction("accSettle", M.accSettle());
    CONFIG = new AccountAction("config", M.config());
    MODIFYACCOUNT = new AccountAction("modifyAccount", M.modifyAccount());
    ADDFROMSTATEMENT = new AccountAction("addFromStatement", M.addFromStatement());
    ADDFROMINVOICE = new AccountAction("addFromInvoice", M.addFromInvoice());
    ADDFROMPAYNOTICE = new AccountAction("addFromPayNotice", M.addFromPayNotice());
    ADDFROMSRCBILL = new AccountAction("addFromSrcBill", M.addFromSrcBill());
    ADDFROMACCOUNT = new AccountAction("addFromAccount", M.addFromAccount());
    ATTACHMENTEDIT = new AccountAction("attachmentEdit", M.attachmentEdit());
    IVCREGISTE = new AccountAction("ivcRegiste", M.ivcRegiste());
    PRINT = new BAction("print", M.print());
    PRINTEXPORT = new BAction("printExport", M.printExport());
  }

  public AccountAction() {
    // do nothing
  }

  /**
   * 创建对象。
   * 
   * @param key
   *          键值。
   * @param caption
   *          显示标题。
   */
  public AccountAction(String key, String caption) {
    this.key = key;
    this.caption = caption;
  }

  private String key;
  private String caption;

  /** 键值。 */
  public String getKey() {
    return key;
  }

  /** 显示标题。 */
  public String getCaption() {
    return caption;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AccountAction other = (AccountAction) obj;
    if (key == null) {
      if (other.key != null)
        return false;
    } else if (!key.equals(other.key))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return key;
  }

  static interface M extends Messages {

    @DefaultMessage("启用")
    String enalbe();

    @DefaultMessage("禁用")
    String disable();

    @DefaultMessage("生效")
    String effect();

    @DefaultMessage("作废")
    String abort();

    @DefaultMessage("导入")
    String imp();

    @DefaultMessage("冻结")
    String freeze();

    @DefaultMessage("解冻")
    String unfreeze();

    @DefaultMessage("出账")
    String accSettle();

    @DefaultMessage("选项")
    String config();

    @DefaultMessage("账款修改")
    String modifyAccount();

    @DefaultMessage("账单添加权")
    String addFromStatement();

    @DefaultMessage("发票登记单添加权  ")
    String addFromInvoice();

    @DefaultMessage("收付款通知单添加权 ")
    String addFromPayNotice();

    @DefaultMessage("来源单据添加权")
    String addFromSrcBill();

    @DefaultMessage("账款添加权")
    String addFromAccount();

    @DefaultMessage("附件编辑权")
    String attachmentEdit();

    @DefaultMessage("发票登记权")
    String ivcRegiste();

    @DefaultMessage("打印")
    String print();

    @DefaultMessage("打印导出")
    String printExport();
  }

  static class MImpl implements M {
    @Override
    public String enalbe() {
      return "Enable";
    }

    @Override
    public String disable() {
      return "Disable";
    }

    @Override
    public String effect() {
      return "Effect";
    }

    @Override
    public String abort() {
      return "abort";
    }

    @Override
    public String imp(){
      return "import";
    }
    
    @Override
    public String freeze() {
      return "freeze";
    }

    @Override
    public String unfreeze() {
      return "unfreeze";
    }

    @Override
    public String accSettle() {
      return "accSettle";
    }

    @Override
    public String print() {
      return "print";
    }

    @Override
    public String printExport() {
      return "printExport";
    }

    @Override
    public String config() {
      return "config";
    }

    @Override
    public String modifyAccount() {
      return "modifyAccount";
    }

    @Override
    public String addFromStatement() {
      return "addFromStatement";
    }

    @Override
    public String addFromInvoice() {
      return "addFromInvoice";
    }

    @Override
    public String addFromPayNotice() {
      return "addFromPayNotice";
    }

    @Override
    public String addFromSrcBill() {
      return "addFromSrcBill";
    }

    @Override
    public String addFromAccount() {
      return "addFromAccount";
    }

    @Override
    public String attachmentEdit() {
      return "attachmentEdit";
    }

    @Override
    public String ivcRegiste() {
      return "ivcRegiste";
    }
  }

}
