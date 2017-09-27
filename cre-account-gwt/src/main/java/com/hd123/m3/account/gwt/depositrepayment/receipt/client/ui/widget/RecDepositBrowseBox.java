/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RecDepositRepaymentBrowseBox.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月28日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc.RecDepositRepaymentService;
import com.hd123.m3.commons.biz.entity.BizStates;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.util.client.message.MessageLevel;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2e.client.form.RBrowseBox;
import com.hd123.rumba.gwt.widget2e.client.util.MessageHelper;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 预存款组件
 * 
 * @author chenganbang
 *
 */
public class RecDepositBrowseBox extends RBrowseBox<BDeposit> {
  private Callback callback;
  private RecDepositBrowseDialog dialog;
  private String accountUnitUuid;
  private String counterpartUuid;
  private String contractUuid;

  public Callback getCallback() {
    return callback;
  }

  public RecDepositBrowseBox(Callback callback) {
    super();
    this.callback = callback;

    dialog = new RecDepositBrowseDialog();
    dialog.addSelectionHandler(new SelectionHandler<BDeposit>() {
      @Override
      public void onSelection(SelectionEvent<BDeposit> event) {
        BDeposit entity = event.getSelectedItem();
        setRawValue(entity);
        setValue(entity.getBillNumber(), true);
        getCallback().execute(entity);
      }
    });
    setBrowser(dialog);
    setCaption(M.defaultCaption());
    setValidator(new MessageValidator());
    addChangeHandler(new Handler_textField());
  }

  @Override
  public RecDepositBrowseDialog getBrowser() {
    return dialog;
  }

  private class Handler_textField implements ChangeHandler {

    @Override
    public void onChange(ChangeEvent event) {
      clearRawValue();
      clearValidResults();

      if (StringUtil.isNullOrBlank(getValue())) {
        clearValue();
        getCallback().execute(null);
      } else {
        validatorDeposit(getValue());
      }
    }
  }

  private void validatorDeposit(final String billNumber) {
    RecDepositFilter filter = new RecDepositFilter();
    filter.setBillNumber(billNumber);
    filter.setAccountUnitUuid(accountUnitUuid);
    filter.setCounterpartUuid(counterpartUuid);
    filter.setContractUuid(contractUuid);
    filter.setBizState(BizStates.EFFECT);

    RecDepositRepaymentService.Locator.getService().getDepositByBillNumber(filter,
        new RBAsyncCallback2<BDeposit>() {

          @Override
          public void onException(Throwable caught) {
            String msg = CommonsMessages.M.actionFailed(
                CommonsMessages.M.load() + M.defaultCaption(), ":" + billNumber);
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(BDeposit result) {
            if (result.getUuid() == null) {
              result.addMessage(Message.error(M.cannotFindDeposit(billNumber)));
            }
            setRawValue(result);
            setValue(billNumber);
            getCallback().execute(result);
          }
        });
  }

  private class MessageValidator implements RValidator {
    @Override
    public Message validate(Widget sender, String value) {
      return MessageHelper.toHighPriorityMessage(getRawValue(), MessageLevel.ERROR);
    }
  }

  /** 回调接口 */
  public static interface Callback {
    void execute(BDeposit result);
  }

  /** 当前项目 */
  public void setAccountUnitUuid(String accountUnitUuid) {
    this.accountUnitUuid = accountUnitUuid;
    if (dialog != null) {
      dialog.setAccountUnitUuid(accountUnitUuid);
    }
  }

  /** 当前对方单位 */
  public void setCounterpartUuid(String counterpartUuid) {
    this.counterpartUuid = counterpartUuid;
    if (dialog != null) {
      dialog.setCounterpartUuid(counterpartUuid);
    }
  }

  /** 当前合同 */
  public void setContractUuid(String contractUuid) {
    this.contractUuid = contractUuid;
    if (dialog != null) {
      dialog.setContractUuid(contractUuid);
    }
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("预存款单")
    String defaultCaption();

    @DefaultMessage("预存款单：未找到{0}")
    String cannotFindDeposit(String billNumber);
  }
}
