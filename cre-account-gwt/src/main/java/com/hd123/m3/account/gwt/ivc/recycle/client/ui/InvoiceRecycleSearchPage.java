/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRecycleSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年4月8日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.recycle.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.ivc.recycle.client.EPInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.client.biz.BInvoiceRecycle;
import com.hd123.m3.account.gwt.ivc.recycle.intf.client.InvoiceRecycleUrlParams.Flecs;
import com.hd123.m3.account.gwt.ivc.recycle.intf.client.dd.PInvoiceRecycleDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmSearchPage2;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.flecs.client.DefaultOperandWidgetFactory;
import com.hd123.rumba.gwt.flecs.client.DefaultOperator;
import com.hd123.rumba.gwt.flecs.client.FlecsConfig;
import com.hd123.rumba.gwt.flecs.client.Operator;
import com.hd123.rumba.gwt.flecs.client.OperatorPolicy;
import com.hd123.rumba.gwt.flecs.client.RFlecsPanel;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RDateTimeRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRendererFactory;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票回收单搜索页面
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class InvoiceRecycleSearchPage extends BaseBpmSearchPage2<BInvoiceRecycle> implements Flecs {
  private static InvoiceRecycleSearchPage instance;

  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef invoiceTypeCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef receiverCol;
  private RGridColumnDef returnorCol;
  private RGridColumnDef recycleDateCol;
  private RGridColumnDef remarkCol;

  @Override
  protected EPInvoiceRecycle getEP() {
    return EPInvoiceRecycle.getInstance();
  }

  public static InvoiceRecycleSearchPage getInstance() {
    if (instance == null) {
      instance = new InvoiceRecycleSearchPage();
    }
    return instance;
  }

  @Override
  protected List<RGridColumnDef> createGridColumns() {
    List<RGridColumnDef> columnDefs = new ArrayList<RGridColumnDef>();

    billNumberCol = new RGridColumnDef(PInvoiceRecycleDef.billNumber);
    billNumberCol.setWidth("180px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setSortable(true);
    billNumberCol.setName(PN_BILLNUMBER);
    billNumberCol.setAllowHiding(false);
    columnDefs.add(billNumberCol);

    bizStateCol = new RGridColumnDef(PInvoiceRecycleDef.bizState);
    bizStateCol.setWidth("100px");
    bizStateCol.setSortable(true);
    bizStateCol.setName(FIELD_BIZSTATE);
    columnDefs.add(bizStateCol);

    invoiceTypeCol = new RGridColumnDef(PInvoiceRecycleDef.invoiceType);
    invoiceTypeCol.setWidth("100px");
    invoiceTypeCol.setSortable(true);
    invoiceTypeCol.setName(FIELD_INVOICE_TYPE);
    columnDefs.add(invoiceTypeCol);

    accountUnitCol = new RGridColumnDef(PInvoiceRecycleDef.accountUnit);
    accountUnitCol.setWidth("160px");
    accountUnitCol.setSortable(true);
    accountUnitCol.setName(FIELD_ACCOUNT_UNIT);
    columnDefs.add(accountUnitCol);

    receiverCol = new RGridColumnDef(PInvoiceRecycleDef.receiver);
    receiverCol.setWidth("150px");
    receiverCol.setSortable(true);
    receiverCol.setName(FIELD_RECEIVER);
    columnDefs.add(receiverCol);

    returnorCol = new RGridColumnDef(PInvoiceRecycleDef.returnor);
    returnorCol.setWidth("150px");
    returnorCol.setSortable(true);
    returnorCol.setName(FIELD_RETURNOR);
    columnDefs.add(returnorCol);

    recycleDateCol = new RGridColumnDef(PInvoiceRecycleDef.recycleDate);
    recycleDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    recycleDateCol.setWidth("120px");
    recycleDateCol.setSortable(true);
    recycleDateCol.setName(FIELD_RECYCLEDATE);
    columnDefs.add(recycleDateCol);

    remarkCol = new RGridColumnDef(PInvoiceRecycleDef.remark);
    remarkCol.setSortable(true);
    remarkCol.setName(FIELD_REMARK);
    columnDefs.add(remarkCol);

    return columnDefs;
  }

  @Override
  protected RFlecsPanel drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());

    flecsPanel.addField(PInvoiceRecycleDef.billNumber);
    flecsPanel.addField(PInvoiceRecycleDef.bizState);
    flecsPanel.addField(PInvoiceRecycleDef.accountUnit);
    flecsPanel.addField(PInvoiceRecycleDef.receiver);
    flecsPanel.addField(PInvoiceRecycleDef.returnor);
    flecsPanel.addField(PInvoiceRecycleDef.invoice_code);
    flecsPanel.addField(PInvoiceRecycleDef.invoice_number);
    flecsPanel.addField(PInvoiceRecycleDef.recycleDate);
    flecsPanel.addField(PInvoiceRecycleDef.invoiceType);

    if (getEP().isPermEnabled()) {
      flecsPanel.addField(PInvoiceRecycleDef.permGroupId);
    }
    flecsPanel.addField(PInvoiceRecycleDef.createInfo_operator_id);
    flecsPanel.addField(PInvoiceRecycleDef.createInfo_operator_fullName);
    flecsPanel.addField(PInvoiceRecycleDef.createInfo_time);
    flecsPanel.addField(PInvoiceRecycleDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PInvoiceRecycleDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PInvoiceRecycleDef.lastModifyInfo_time);

    flecsPanel.addBeforeLoadDataHandler(new BeforeLoadDataHandler<Integer>() {

      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent<Integer> event) {
        JumpParameters jumpParam = new JumpParameters(getStartNode());
        FlecsConfig flecsConfig = flecsPanel.getCurrentConfig();
        flecsConfig.setShowConditions(flecsPanel.isShowConditions());
        jumpParam.getUrlRef().set(PN_FLECSCONFIG, flecsCodec.encode(flecsConfig));
        jumpParam.getUrlRef().set(PN_PAGE, String.valueOf(event.getParameter()));
        getEP().jump(jumpParam);
        event.cancel();
      }
    });

    getToolbar().add(flecsPanel.getMenuButton(), Alignment.right);
    return flecsPanel;
  }

  @Override
  protected RGridColumnDef getIdentifyColumn() {
    return billNumberCol;
  }

  @Override
  protected List<Condition> getDefaultConditions(JumpParameters params) {
    List<Condition> conditions = new ArrayList<Condition>();
    if (StringUtil.isNullOrBlank(getSearchKeyWork(params)) == false) {
      conditions.add(new Condition(PInvoiceRecycleDef.billNumber, DefaultOperator.STARTS_WITH,
          getSearchKeyWork(params)));
      return conditions;
    }

    conditions.add(new Condition(PInvoiceRecycleDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRecycleDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRecycleDef.accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRecycleDef.receiver, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRecycleDef.returnor, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRecycleDef.invoice_code, DefaultOperator.EQUALS, null));
    conditions
        .add(new Condition(PInvoiceRecycleDef.invoice_number, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PInvoiceRecycleDef.recycleDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRecycleDef.invoiceType, DefaultOperator.EQUALS, null));
    return conditions;
  }

  @Override
  protected Object getGridData(int row, int col, BInvoiceRecycle rowData,
      List<BInvoiceRecycle> pageData) {
    if (col == billNumberCol.getIndex()) {
      return rowData.getBillNumber();
    } else if (col == bizStateCol.getIndex()) {
      return BBizStates.getCaption(rowData.getBizState());
    } else if (col == invoiceTypeCol.getIndex()) {
      return rowData.getInvoiceType() == null ? null : getEP().getInvoiceTypes().get(
          rowData.getInvoiceType());
    } else if (col == accountUnitCol.getIndex()) {
      return BUCN.toFriendlyStr(rowData.getAccountUnit(), null);
    } else if (col == receiverCol.getIndex()) {
      return rowData.getReceiver() == null ? null : rowData.getReceiver().toFriendlyStr();
    } else if (col == returnorCol.getIndex()) {
      return rowData.getReturnor() == null ? null : rowData.getReturnor().toFriendlyStr();
    } else if (col == recycleDateCol.getIndex()) {
      return rowData.getRecycleDate();
    } else if (col == remarkCol.getIndex()) {
      return rowData.getRemark();
    }
    return null;
  }

  @Override
  protected BInvoiceRecycle[] newEntityArray() {
    return new BInvoiceRecycle[] {};
  }

  private class PermGroupField extends RComboBox<String> {
    public PermGroupField(FieldDef fieldDef) {
      super();
      setFieldDef(fieldDef);
      setEditable(false);
      setMaxDropdownRowCount(10);
      for (Entry<String, String> entry : getEP().getUserGroups().entrySet()) {
        this.addOption(entry.getKey(), entry.getValue());
      }
    }
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (field == PInvoiceRecycleDef.accountUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == PInvoiceRecycleDef.permGroupId) {
        return new PermGroupField(field);
      } else if (field == PInvoiceRecycleDef.receiver) {
        return DefaultOperator.EQUALS == operator ? new EmployeeUCNBox() : null;
      } else if (field == PInvoiceRecycleDef.returnor) {
        return DefaultOperator.EQUALS == operator ? new EmployeeUCNBox() : null;
      } else if (field == PInvoiceRecycleDef.invoiceType) {
        RComboBox<String> invoiceTypeBox = new RComboBox<String>(PInvoiceRecycleDef.invoiceType);
        for (Map.Entry<String, String> item : getEP().getInvoiceTypes().entrySet()) {
          invoiceTypeBox.addOption(item.getKey(), item.getValue());
        }
        invoiceTypeBox.setEditable(false);
        return invoiceTypeBox;
      }
      return super.createOperandWidget(field, operator);
    }
  }

  private class MyOperatorPolicy implements OperatorPolicy {

    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (PInvoiceRecycleDef.accountUnit == field || PInvoiceRecycleDef.receiver == field
          || PInvoiceRecycleDef.returnor == field || field == PInvoiceRecycleDef.invoice_code) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PInvoiceRecycleDef.bizState == field || PInvoiceRecycleDef.invoiceType == field
          || PInvoiceRecycleDef.permGroupId == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PInvoiceRecycleDef.invoice_number == field) {
        result.add(DefaultOperator.CONTAINS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }
}
