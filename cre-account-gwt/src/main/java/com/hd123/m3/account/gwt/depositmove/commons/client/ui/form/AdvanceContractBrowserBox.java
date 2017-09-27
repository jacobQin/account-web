/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	AdvanceContractBrowserBox.java
 * 模块说明：	
 * 修改历史：
 * 2014-02-27 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositmove.commons.client.ui.form;

import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.depositmove.commons.client.biz.BAdvanceContract;
import com.hd123.m3.account.gwt.depositmove.commons.client.rpc.DepositMoveService;
import com.hd123.m3.account.gwt.depositmove.commons.client.ui.dialog.AdvanceContractBrowserDialog;
import com.hd123.m3.account.gwt.depositmove.intf.client.DepositMoveMessage;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.fielddef.StringFieldDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.util.client.message.MessageLevel;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.event.HasBeforeLoadDataHandlers;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2e.client.form.RBrowseBox;
import com.hd123.rumba.gwt.widget2e.client.util.MessageHelper;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author zhuhairui
 * 
 */
public class AdvanceContractBrowserBox extends RBrowseBox<BAdvanceContract> implements
    HasBeforeLoadDataHandlers<String> {
  private Callback callback;
  private AdvanceContractBrowserDialog dialog;
  private int direction;
  private boolean isQueryAdvance;
  private String accountUnitUuid;

  public AdvanceContractBrowserBox(boolean isShowAccountUnit, boolean isShowState,
      boolean isCounterpartTypeEnable, Callback callback, Map<String, String> captionMap) {
    super();
    this.callback = callback;

    dialog = new AdvanceContractBrowserDialog(isShowAccountUnit, isShowState,
        isCounterpartTypeEnable, captionMap);
    dialog.addSelectionHandler(new SelectionHandler<BAdvanceContract>() {
      @Override
      public void onSelection(SelectionEvent<BAdvanceContract> event) {
        BAdvanceContract contract = event.getSelectedItem();
        setRawValue(contract);
        setValue(contract.getContract().getCode(), true);
        AdvanceContractBrowserBox.this.callback.execute(contract);
      }
    });
    setBrowser(dialog);
    setValidator(new MessageValidator());
    addChangeHandler(new Handler_textField());
  }

  public AdvanceContractBrowserBox(String caption, boolean isShowAccountUnit, boolean isShowState,
      boolean isCounterpartTypeEnable, Callback callback, Map<String, String> captionMap) {
    this(isShowAccountUnit, isShowState, isCounterpartTypeEnable, callback, captionMap);
    setCaption(caption);
  }

  public AdvanceContractBrowserBox(StringFieldDef fieldDef, boolean isShowAccountUnit,
      boolean isShowState, boolean isCounterpartTypeEnable, Callback callback,
      Map<String, String> captionMap) {
    this(isShowAccountUnit, isShowState, isCounterpartTypeEnable, callback, captionMap);
    setFieldDef(fieldDef);
  }

  public void setCounterTypeMap(Map<String, String> counterTypeMap) {
    dialog.setCounterTypeMap(counterTypeMap);
  }

  @Override
  public AdvanceContractBrowserDialog getBrowser() {
    return dialog;
  }
  
  @Override
  protected void onBrowseButtonClick() {
    BeforeLoadDataEvent be = BeforeLoadDataEvent.fire(AdvanceContractBrowserBox.this, "");
    if(be != null && be.isCanceled())
      return;
    super.onBrowseButtonClick();
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
      BeforeLoadDataEvent be = BeforeLoadDataEvent.fire(AdvanceContractBrowserBox.this, "");
      if(be != null && be.isCanceled()){
        clearRawValue();
        clearValue();
        clearValidResults();
        return;
      }
      
      clearRawValue();
      clearValidResults();

      if (StringUtil.isNullOrBlank(getValue())) {
        clearValue();
        AdvanceContractBrowserBox.this.callback.execute(null);
      } else if (getRawValue() == null || getRawValue().getContract() == null
          || getValue().equals(getRawValue().getContract().getCode()) == false) {
        validatorContract(getValue());
      }
    }
  }

  private void validatorContract(final String billNumber) {
    RLoadingDialog.show(DepositMoveMessage.M.loading());
    DepositMoveService.Locator.getService().getContractByBillNumber(billNumber, direction,
        accountUnitUuid, isQueryAdvance, new RBAsyncCallback2<BAdvanceContract>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = CommonsMessages.M.actionFailed(
                CommonsMessages.M.load() + WidgetRes.M.contract(), ":" + billNumber);
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BAdvanceContract result) {
            RLoadingDialog.hide();
            if (result == null) {
              result = new BAdvanceContract();
              result.addMessage(Message.error(WidgetRes.M.cannotFindContract() + ":" + billNumber));
            }
            setRawValue(result);
            setValue(billNumber);
            AdvanceContractBrowserBox.this.callback.execute(result);
          }
        });
  }

  /**
   * 回调接口
   */
  public static interface Callback {
    void execute(BAdvanceContract result);
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

  /** 设置项目uuid,提供给转入信息用。 */
  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
    if (dialog != null)
      dialog.setAccountUnitUuid(accountUnitUuid);
  }

  /** 用来给该box筛选面板中添加对方单位类型等于 */
  public void setCounterpartEqual(String counterpartType) {
    if (dialog != null)
      dialog.setCounterpartEqual(counterpartType);
  }

  /** 设置收付方向 */
  public void setDirection(int direction) {
    this.direction = direction;
    if (dialog != null)
      dialog.setDirection(direction);
  }

  /** 设置是否从预存款账务取得合同 */
  public void setQueryAdvance(boolean isQueryAdvance) {
    this.isQueryAdvance = isQueryAdvance;
    if (dialog != null)
      dialog.setQueryAdvance(isQueryAdvance);
  }

  @Override
  public HandlerRegistration addBeforeLoadDataHandler(BeforeLoadDataHandler<String> handler) {
    return addHandler(handler, BeforeLoadDataEvent.getType());
  }
}
