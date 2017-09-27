/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceAbortSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.abort.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.ivc.abort.client.EPInvoiceAbort;
import com.hd123.m3.account.gwt.ivc.abort.client.biz.BInvoiceAbort;
import com.hd123.m3.account.gwt.ivc.abort.intf.client.InvoiceAbortUrlParams.Search;
import com.hd123.m3.account.gwt.ivc.abort.intf.client.dd.PInvoiceAbortDef;
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
 * @author lixiaohong
 * 
 */
public class InvoiceAbortSearchPage extends BaseBpmSearchPage2<BInvoiceAbort> implements Search {
  private static InvoiceAbortSearchPage instance;

  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef invoiceTypeCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef aborterCol;
  private RGridColumnDef abortDateCol;
  private RGridColumnDef remarkCol;

  @Override
  protected EPInvoiceAbort getEP() {
    return EPInvoiceAbort.getInstance();
  }

  public static InvoiceAbortSearchPage getInstance() {
    if (instance == null) {
      instance = new InvoiceAbortSearchPage();
    }
    return instance;
  }

  @Override
  protected List<RGridColumnDef> createGridColumns() {
    List<RGridColumnDef> columnDefs = new ArrayList<RGridColumnDef>();

    billNumberCol = new RGridColumnDef(PInvoiceAbortDef.billNumber);
    billNumberCol.setWidth("180px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setSortable(true);
    billNumberCol.setName(PN_BILLNUMBER);
    billNumberCol.setAllowHiding(false);
    columnDefs.add(billNumberCol);

    bizStateCol = new RGridColumnDef(PInvoiceAbortDef.bizState);
    bizStateCol.setWidth("100px");
    bizStateCol.setSortable(true);
    bizStateCol.setName(FIELD_BIZSTATE);
    columnDefs.add(bizStateCol);

    invoiceTypeCol = new RGridColumnDef(PInvoiceAbortDef.invoiceType);
    invoiceTypeCol.setWidth("100px");
    invoiceTypeCol.setSortable(true);
    invoiceTypeCol.setName(FIELD_INVOICE_TYPE);
    columnDefs.add(invoiceTypeCol);

    accountUnitCol = new RGridColumnDef(PInvoiceAbortDef.accountUnit);
    accountUnitCol.setWidth("120px");
    accountUnitCol.setSortable(true);
    accountUnitCol.setName(FIELD_ACCOUNT_UNIT);
    columnDefs.add(accountUnitCol);

    aborterCol = new RGridColumnDef(PInvoiceAbortDef.aborter);
    aborterCol.setWidth("150px");
    aborterCol.setSortable(true);
    aborterCol.setName(FIELD_ABORTER);
    columnDefs.add(aborterCol);

    abortDateCol = new RGridColumnDef(PInvoiceAbortDef.abortDate);
    abortDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    abortDateCol.setWidth("120px");
    abortDateCol.setSortable(true);
    abortDateCol.setName(FIELD_ABORTDATE);
    columnDefs.add(abortDateCol);

    remarkCol = new RGridColumnDef(PInvoiceAbortDef.remark);
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

    flecsPanel.addField(PInvoiceAbortDef.billNumber);
    flecsPanel.addField(PInvoiceAbortDef.bizState);
    flecsPanel.addField(PInvoiceAbortDef.accountUnit);
    flecsPanel.addField(PInvoiceAbortDef.aborter);
    flecsPanel.addField(PInvoiceAbortDef.invoice_code);
    flecsPanel.addField(PInvoiceAbortDef.invoice_number);
    flecsPanel.addField(PInvoiceAbortDef.abortDate);
    flecsPanel.addField(PInvoiceAbortDef.invoiceType);

    if (getEP().isPermEnabled()) {
      flecsPanel.addField(PInvoiceAbortDef.permGroupId);
    }
    flecsPanel.addField(PInvoiceAbortDef.createInfo_operator_id);
    flecsPanel.addField(PInvoiceAbortDef.createInfo_operator_fullName);
    flecsPanel.addField(PInvoiceAbortDef.createInfo_time);
    flecsPanel.addField(PInvoiceAbortDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PInvoiceAbortDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PInvoiceAbortDef.lastModifyInfo_time);

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
      conditions.add(new Condition(PInvoiceAbortDef.billNumber, DefaultOperator.STARTS_WITH,
          getSearchKeyWork(params)));
      return conditions;
    }

    conditions.add(new Condition(PInvoiceAbortDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceAbortDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceAbortDef.accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceAbortDef.aborter, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceAbortDef.invoice_code, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceAbortDef.invoice_number, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PInvoiceAbortDef.abortDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceAbortDef.invoiceType, DefaultOperator.EQUALS, null));
    return conditions;
  }

  @Override
  protected Object getGridData(int row, int col, BInvoiceAbort rowData, List<BInvoiceAbort> pageData) {
    if (col == billNumberCol.getIndex()) {
      return rowData.getBillNumber();
    } else if (col == bizStateCol.getIndex()) {
      return BBizStates.getCaption(rowData.getBizState());
    } else if (col == invoiceTypeCol.getIndex()) {
      return rowData.getInvoiceType() == null ? null : getEP().getInvoiceTypes().get(
          rowData.getInvoiceType());
    } else if (col == accountUnitCol.getIndex()) {
      return BUCN.toFriendlyStr(rowData.getAccountUnit(), null);
    } else if (col == aborterCol.getIndex()) {
      return rowData.getAborter() == null ? null : rowData.getAborter().toFriendlyStr();
    } else if (col == abortDateCol.getIndex()) {
      return rowData.getAbortDate();
    } else if (col == remarkCol.getIndex()) {
      return rowData.getRemark();
    }
    return null;
  }

  @Override
  protected BInvoiceAbort[] newEntityArray() {
    return new BInvoiceAbort[] {};
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
      if (field == PInvoiceAbortDef.accountUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == PInvoiceAbortDef.permGroupId) {
        return new PermGroupField(field);
      } else if (field == PInvoiceAbortDef.aborter) {
        return DefaultOperator.EQUALS == operator ? new EmployeeUCNBox() : null;
      } else if (field == PInvoiceAbortDef.invoiceType) {
        RComboBox<String> invoiceTypeBox = new RComboBox<String>(PInvoiceAbortDef.invoiceType);
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
      if (PInvoiceAbortDef.accountUnit == field || PInvoiceAbortDef.aborter == field
          || field == PInvoiceAbortDef.invoice_code) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PInvoiceAbortDef.bizState == field || PInvoiceAbortDef.invoiceType == field
          || PInvoiceAbortDef.permGroupId == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PInvoiceAbortDef.invoice_number == field) {
        result.add(DefaultOperator.CONTAINS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }
}
