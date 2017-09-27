/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	StoreLinkField.java
 * 模块说明：	
 * 修改历史：
 * 2014-1-17 - chenrizhang- 创建。
 */
package com.hd123.m3.account.gwt.cpnts.client.ui.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.DispatchClickHandler;
import com.hd123.rumba.gwt.base.client.BWithUCN;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.widget2.client.form.RHyperlinkField;

/**
 * @author chenrizhang
 * 
 */
public class ContractLinkField extends RHyperlinkField {
  /** 跳转类型 */
  public static final String DISPATCH = "contract";
  /** URL参数：指定查看合同的uuid。 */
  public static final String PN_ENTITY_UUID = "_uuid";

  private BWithUCN rowValue;
  private BDispatch dispatch = new BDispatch(DISPATCH);

  public ContractLinkField() {
    setCaption(M.defaultCaption());
    addClickHandler(new DispatchClickHandler(dispatch));
  }

  public ContractLinkField(String caption) {
    this();
    setCaption(caption);
  }

  public ContractLinkField(FieldDef fieldDef) {
    this();
    setFieldDef(fieldDef);
  }

  public void setRawValue(BWithUCN rowValue) {
    this.rowValue = rowValue;
    if (rowValue != null) {
      setValue(rowValue.toFriendlyStr());
      dispatch.addParams(PN_ENTITY_UUID, rowValue.getUuid());
    } else {
      clearValue();
      dispatch.getParams().clear();
    }
  }

  public BWithUCN getRawValue() {
    return rowValue;
  }

  public static M M = GWT.create(M.class);

  public interface M extends Messages {

    @DefaultMessage("合同")
    String defaultCaption();
  }
}
