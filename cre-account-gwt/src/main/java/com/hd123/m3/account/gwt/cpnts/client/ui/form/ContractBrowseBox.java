/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-common
 * 文件名：	ContractBrowseBox.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-18 - subinzhu - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.biz.BContract;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.ContractBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.ContractFilter;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.util.client.message.MessageLevel;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2e.client.form.RBrowseBox;
import com.hd123.rumba.gwt.widget2e.client.util.MessageHelper;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author subinzhu
 * 
 */
public class ContractBrowseBox extends RBrowseBox<BContract> {

  private Callback callback;
  private ContractBrowserDialog dialog;
  private String accountUnitUuid;
  private String counterpartUuid;
  private boolean unlocked;
  private Map<String, String> counterpartTypeMap;
  private boolean requiresAccountUnitAndCountpart;

  public Callback getCallback() {
    return callback;
  }

  public ContractBrowseBox(Callback callback, boolean showAllFields, Map<String, String> captionMap) {
    super();
    this.callback = callback;

    dialog = new ContractBrowserDialog(showAllFields, captionMap);
    dialog.addSelectionHandler(new SelectionHandler<BContract>() {
      @Override
      public void onSelection(SelectionEvent<BContract> event) {
        BContract contract = event.getSelectedItem();
        setRawValue(contract);
        setValue(contract.getBillNumber(), true);
        getCallback().execute(contract);
      }
    });
    setBrowser(dialog);
    setValidator(new MessageValidator());
    addChangeHandler(new Handler_textField());
  }

  /**
   * 设置状态列是否可见，默认不可见
   * 
   * @param visible
   */
  public void setStateColVisible(boolean visible) {
    dialog.setStateColVisible(visible);
  }

  public void setClearState(boolean clearState) {
    dialog.setClearState(clearState);
  }

  public Map<String, String> getCounterTypeMap() {
    return counterpartTypeMap;
  }

  public void setCounterTypeMap(Map<String, String> counterTypeMap) {
    this.counterpartTypeMap = counterTypeMap;
    dialog.setCounterTypeMap(counterTypeMap);
  }

  /**
   * 设置项目及对方单位是否必须，为是时未传不返回数据
   * 
   * @param require
   *          默认FALSE true表示一定需要才能返回，false表示可以查询合同及对方单位为空的数据
   */
  public void setRequiresAccountUnitAndCountpart(boolean require) {
    this.requiresAccountUnitAndCountpart = require;
    if (dialog != null)
      dialog.setRequiresAccountUnitAndCountpart(require);
  }

  public ContractBrowseBox(String caption, boolean showAllFields, Callback callback,
      Map<String, String> captionMap) {
    this(callback, showAllFields, captionMap);
    setCaption(caption);
  }

  public ContractBrowseBox(StringFieldDef field, boolean showAllFields, Callback callback,
      Map<String, String> captionMap) {
    this(callback, showAllFields, captionMap);
    setFieldDef(field);
  }

  @Override
  public ContractBrowserDialog getBrowser() {
    return dialog;
  }

  private class MessageValidator implements RValidator {
    @Override
    public Message validate(Widget sender, String value) {
      return MessageHelper.toHighPriorityMessage(getRawValue(), MessageLevel.ERROR);
    }
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      clearRawValue();
      clearValidResults();

      if (StringUtil.isNullOrBlank(getValue())) {
        clearValue();
        getCallback().execute(null);
      } else if (getRawValue() == null || getValue().equals(getRawValue().getBillNumber()) == false) {
        validatorContract(getValue());
      }
    }
  }

  private void validatorContract(final String billNumber) {
    getContractByNumber(billNumber);
  }

  private void getContractByNumber(final String billNumber) {
    ContractFilter filter = new ContractFilter();
    filter.setBillNumber(billNumber);
    filter.setRequiresAccountUnitAndCountpart(requiresAccountUnitAndCountpart);
    filter.setAccountUnitUuid(accountUnitUuid);
    filter.setCounterpartUuid(counterpartUuid);
    filter.setUnlocked(unlocked);

    AccountCpntsService.Locator.getService().getContractByNumber(filter,
        new RBAsyncCallback2<BContract>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = CommonsMessages.M.actionFailed(
                CommonsMessages.M.load() + WidgetRes.M.contract(), ":" + billNumber);
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BContract result) {
            RLoadingDialog.hide();
            if (result == null) {
              result = new BContract();
              result.setBillNumber(billNumber);
              result.addMessage(Message.error(WidgetRes.M.cannotFindContract() + ":" + billNumber));
            }
            setRawValue(result);
            setValue(billNumber);
            setTitle(result.getTitle());
            getCallback().execute(result);
          }
        });
  }

  /** 回调接口 */
  public static interface Callback {
    void execute(BContract result);
  }

  /** 用来给该box筛选面板中添加项目类似于 */
  public void setAccountUnitLike(String accountUnitCode) {
    if (dialog != null)
      dialog.setAccountUnitLike(accountUnitCode);
  }

  /** 用来给该box筛选面板中添加商户类似于 */
  public void setCounterpartLike(String counterpart) {
    if (dialog != null)
      dialog.setCounterpartLike(counterpart);
  }

  /** 用来给该box筛选面板中添加对方单位类型等于 */
  public void setCounterpartEqual(String counterpartType) {
    if (dialog != null)
      dialog.setCounterpartEqual(counterpartType);
  }

  /** 当前结算中心uuid */
  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
    if (dialog != null)
      dialog.setAccountUnitUuid(accountUnitUuid);
  }

  /** 对方结算中心uuid 提供给编辑页面用 */
  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
    if (dialog != null)
      dialog.setCounterpartUuid(counterpartUuid);
  }

  /** 合同是否需要是未锁定 */
  public void setUnlocked(boolean unlocked) {
    this.unlocked = unlocked;
    if (dialog != null) {
      dialog.setUnlocked(unlocked);
    }
  }

  public boolean isUnlocked() {
    return unlocked;
  }
}
