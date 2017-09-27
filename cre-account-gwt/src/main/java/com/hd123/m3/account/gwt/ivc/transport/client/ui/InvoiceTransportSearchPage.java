/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceTransportSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年3月15日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.transport.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.ivc.transport.client.EPInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.client.biz.BInvoiceTransport;
import com.hd123.m3.account.gwt.ivc.transport.intf.client.InvoiceTransportUrlParams.Flecs;
import com.hd123.m3.account.gwt.ivc.transport.intf.client.dd.PInvoiceTransportDef;
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
 * 发票调拨单搜索页面
 * 
 * @author lixiaohong
 * @since 1.9
 * 
 */
public class InvoiceTransportSearchPage extends BaseBpmSearchPage2<BInvoiceTransport> implements
    Flecs {
  private static InvoiceTransportSearchPage instance;

  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef invoiceTypeCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef transporterCol;
  private RGridColumnDef transportDateCol;
  private RGridColumnDef remarkCol;

  @Override
  protected EPInvoiceTransport getEP() {
    return EPInvoiceTransport.getInstance();
  }

  public static InvoiceTransportSearchPage getInstance() {
    if (instance == null) {
      instance = new InvoiceTransportSearchPage();
    }
    return instance;
  }

  @Override
  protected List<RGridColumnDef> createGridColumns() {
    List<RGridColumnDef> columnDefs = new ArrayList<RGridColumnDef>();

    billNumberCol = new RGridColumnDef(PInvoiceTransportDef.billNumber);
    billNumberCol.setWidth("180px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setSortable(true);
    billNumberCol.setName(PN_BILLNUMBER);
    billNumberCol.setAllowHiding(false);
    columnDefs.add(billNumberCol);

    bizStateCol = new RGridColumnDef(PInvoiceTransportDef.bizState);
    bizStateCol.setWidth("100px");
    bizStateCol.setSortable(true);
    bizStateCol.setName(FIELD_BIZSTATE);
    columnDefs.add(bizStateCol);

    invoiceTypeCol = new RGridColumnDef(PInvoiceTransportDef.invoiceType);
    invoiceTypeCol.setWidth("100px");
    invoiceTypeCol.setSortable(true);
    invoiceTypeCol.setName(FIELD_INVOICE_TYPE);
    columnDefs.add(invoiceTypeCol);

    accountUnitCol = new RGridColumnDef(PInvoiceTransportDef.accountUnit);
    accountUnitCol.setWidth("160px");
    accountUnitCol.setSortable(true);
    accountUnitCol.setName(FIELD_ACCOUNT_UNIT);
    columnDefs.add(accountUnitCol);

    transporterCol = new RGridColumnDef(PInvoiceTransportDef.transportor);
    transporterCol.setWidth("150px");
    transporterCol.setSortable(true);
    transporterCol.setName(FIELD_TRANSPORTOR);
    columnDefs.add(transporterCol);

    transportDateCol = new RGridColumnDef(PInvoiceTransportDef.transportDate);
    transportDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    transportDateCol.setWidth("120px");
    transportDateCol.setSortable(true);
    transportDateCol.setName(FIELD_TRANSPORTDATE);
    columnDefs.add(transportDateCol);

    remarkCol = new RGridColumnDef(PInvoiceTransportDef.remark);
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

    flecsPanel.addField(PInvoiceTransportDef.billNumber);
    flecsPanel.addField(PInvoiceTransportDef.bizState);
    flecsPanel.addField(PInvoiceTransportDef.accountUnit);
    flecsPanel.addField(PInvoiceTransportDef.transportor);
    flecsPanel.addField(PInvoiceTransportDef.invoice_code);
    flecsPanel.addField(PInvoiceTransportDef.invoice_number);
    flecsPanel.addField(PInvoiceTransportDef.transportDate);
    flecsPanel.addField(PInvoiceTransportDef.invoiceType);
    flecsPanel.addField(PInvoiceTransportDef.inAccountUnit);
    flecsPanel.addField(PInvoiceTransportDef.receiver);

    if (getEP().isPermEnabled()) {
      flecsPanel.addField(PInvoiceTransportDef.permGroupId);
    }
    flecsPanel.addField(PInvoiceTransportDef.createInfo_operator_id);
    flecsPanel.addField(PInvoiceTransportDef.createInfo_operator_fullName);
    flecsPanel.addField(PInvoiceTransportDef.createInfo_time);
    flecsPanel.addField(PInvoiceTransportDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PInvoiceTransportDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PInvoiceTransportDef.lastModifyInfo_time);

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
      conditions.add(new Condition(PInvoiceTransportDef.billNumber, DefaultOperator.STARTS_WITH,
          getSearchKeyWork(params)));
      return conditions;
    }

    conditions.add(new Condition(PInvoiceTransportDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceTransportDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceTransportDef.accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceTransportDef.transportor, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceTransportDef.invoice_code, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceTransportDef.invoice_number, DefaultOperator.CONTAINS,
        null));
    conditions.add(new Condition(PInvoiceTransportDef.transportDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceTransportDef.invoiceType, DefaultOperator.EQUALS, null));
    conditions
        .add(new Condition(PInvoiceTransportDef.inAccountUnit, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PInvoiceTransportDef.receiver, DefaultOperator.CONTAINS, null));
    return conditions;
  }

  @Override
  protected Object getGridData(int row, int col, BInvoiceTransport rowData,
      List<BInvoiceTransport> pageData) {
    if (col == billNumberCol.getIndex()) {
      return rowData.getBillNumber();
    } else if (col == bizStateCol.getIndex()) {
      return BBizStates.getCaption(rowData.getBizState());
    } else if (col == invoiceTypeCol.getIndex()) {
      return rowData.getInvoiceType() == null ? null : getEP().getInvoiceTypes().get(
          rowData.getInvoiceType());
    } else if (col == accountUnitCol.getIndex()) {
      return BUCN.toFriendlyStr(rowData.getAccountUnit(), null);
    } else if (col == transporterCol.getIndex()) {
      return rowData.getTransportor() == null ? null : rowData.getTransportor().toFriendlyStr();
    } else if (col == transportDateCol.getIndex()) {
      return rowData.getTransportDate();
    } else if (col == remarkCol.getIndex()) {
      return rowData.getRemark();
    }
    return null;
  }

  @Override
  protected BInvoiceTransport[] newEntityArray() {
    return new BInvoiceTransport[] {};
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
      if (field == PInvoiceTransportDef.accountUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == PInvoiceTransportDef.inAccountUnit) {
        return DefaultOperator.CONTAINS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == PInvoiceTransportDef.permGroupId) {
        return new PermGroupField(field);
      } else if (field == PInvoiceTransportDef.transportor) {
        return DefaultOperator.EQUALS == operator ? new EmployeeUCNBox() : null;
      } else if (field == PInvoiceTransportDef.receiver) {
        return DefaultOperator.CONTAINS == operator ? new EmployeeUCNBox() : null;
      } else if (field == PInvoiceTransportDef.invoiceType) {
        RComboBox<String> invoiceTypeBox = new RComboBox<String>(PInvoiceTransportDef.invoiceType);
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
      if (PInvoiceTransportDef.accountUnit == field || PInvoiceTransportDef.transportor == field
          || field == PInvoiceTransportDef.invoice_code) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PInvoiceTransportDef.bizState == field
          || PInvoiceTransportDef.invoiceType == field || PInvoiceTransportDef.permGroupId == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PInvoiceTransportDef.invoice_number == field
          || PInvoiceTransportDef.inAccountUnit == field || PInvoiceTransportDef.receiver == field) {
        result.add(DefaultOperator.CONTAINS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }
}
