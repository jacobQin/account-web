/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-common
 * 文件名：	SubjectBrowserBox.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月14日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.Messages;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.contants.AccountCpntsContants.WidgetRes;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.SubjectBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.SubjectFilter;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.RPCExceptionDecoder;
import com.hd123.rumba.gwt.widget2e.client.form.RBrowseBox;
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * 科目浏览选择Box
 * 
 * @author LiBin
 * 
 */
public class SubjectBrowserBox extends RBrowseBox<BSubject> {

  private SubjectBrowserDialog dialog;

  private BSubject rawValue;
  private SubjectFilter filter = new SubjectFilter();

  public SubjectBrowserBox(String subjectType, Integer direction, Boolean state, String usageType,
      boolean multiSelect) {
    setCaption(WidgetRes.M.subject());
    dialog = new SubjectBrowserDialog(subjectType, direction, state, usageType, multiSelect);
    dialog.addSelectionHandler(new DialogSelectionHandler());
    setBrowser(dialog);
    
    filter.setSubjectType(subjectType);
    filter.setDirectionType(direction);
    filter.setState(state);
    filter.setUsageType(usageType);
  }

  public void clearConditions() {
    if (dialog != null) {
      dialog.clearConditions();
    }
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    clearConditions();
  }

  public SubjectFilter getFilter() {
    filter.setCode(getText());
    return filter;
  }

  public void setFilter(SubjectFilter filter) {
    this.filter = filter;
  }

  @Override
  public BSubject getRawValue() {
    if (StringUtil.isNullOrBlank(getValue())) {
      return null;
    }
    return rawValue;
  }

  protected void onValidate(String textToValidate) {
    if (getValue() == null || "".equals(getValue())) {
      clearMessages();
      return;
    }
    GWTUtil.enableSynchronousRPC();
    clearMessages();
    AccountCpntsService.Locator.getService().getSubjectByCode(getFilter(),
        new RBAsyncCallback2<BSubject>() {
          public void onException(Throwable caught) {
            addErrorMessage(RPCExceptionDecoder.decode(caught).getMessage());
          }

          public void onSuccess(BSubject result) {
            rawValue = result;
            if (result == null) {
              addErrorMessage(M.notFindInvoice(getCaption(), getText()));
            }
          }
        });
  }
  
  private class DialogSelectionHandler implements SelectionHandler<BSubject> {
    @Override
    public void onSelection(SelectionEvent<BSubject> event) {
      BSubject value = event.getSelectedItem();
      setValue(value.getCode());
      rawValue = value;
      ValueChangeEvent.fire(SubjectBrowserBox.this, getValue());
    }
  }
  
  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("未找到{0}{1}")
    String notFindInvoice(String caption, String code);

    @DefaultMessage("科目")
    String caption();

  }
}
