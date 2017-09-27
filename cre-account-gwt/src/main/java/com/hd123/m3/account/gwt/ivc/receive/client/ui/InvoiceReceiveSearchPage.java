/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceReceiveSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.receive.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.ivc.receive.client.EPInvoiceReceive;
import com.hd123.m3.account.gwt.ivc.receive.client.biz.BInvoiceReceive;
import com.hd123.m3.account.gwt.ivc.receive.intf.client.InvoiceReceiveUrlParams.Search;
import com.hd123.m3.account.gwt.ivc.receive.intf.client.dd.PInvoiceReceiveDef;
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
 * 发票领用单搜索页面
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class InvoiceReceiveSearchPage extends BaseBpmSearchPage2<BInvoiceReceive> implements Search {
  private static InvoiceReceiveSearchPage instance;

  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef invoiceTypeCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef issuerCol;
  private RGridColumnDef receiverCol;
  private RGridColumnDef receiveDateCol;
  private RGridColumnDef remarkCol;

  @Override
  protected EPInvoiceReceive getEP() {
    return EPInvoiceReceive.getInstance();
  }

  public static InvoiceReceiveSearchPage getInstance() {
    if (instance == null) {
      instance = new InvoiceReceiveSearchPage();
    }
    return instance;
  }

  @Override
  protected List<RGridColumnDef> createGridColumns() {
    List<RGridColumnDef> columnDefs = new ArrayList<RGridColumnDef>();

    billNumberCol = new RGridColumnDef(PInvoiceReceiveDef.billNumber);
    billNumberCol.setWidth("180px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setSortable(true);
    billNumberCol.setName(PN_BILLNUMBER);
    billNumberCol.setAllowHiding(false);
    columnDefs.add(billNumberCol);

    bizStateCol = new RGridColumnDef(PInvoiceReceiveDef.bizState);
    bizStateCol.setWidth("100px");
    bizStateCol.setSortable(true);
    bizStateCol.setName(FIELD_BIZSTATE);
    columnDefs.add(bizStateCol);

    invoiceTypeCol = new RGridColumnDef(PInvoiceReceiveDef.invoiceType);
    invoiceTypeCol.setWidth("100px");
    invoiceTypeCol.setSortable(true);
    invoiceTypeCol.setName(FIELD_INVOICE_TYPE);
    columnDefs.add(invoiceTypeCol);

    accountUnitCol = new RGridColumnDef(PInvoiceReceiveDef.accountUnit);
    accountUnitCol.setWidth("120px");
    accountUnitCol.setSortable(true);
    accountUnitCol.setName(FIELD_ACCOUNT_UNIT);
    columnDefs.add(accountUnitCol);

    issuerCol = new RGridColumnDef(PInvoiceReceiveDef.issuer);
    issuerCol.setWidth("150px");
    issuerCol.setSortable(true);
    issuerCol.setName(FIELD_ISSUER);
    columnDefs.add(issuerCol);

    receiverCol = new RGridColumnDef(PInvoiceReceiveDef.receiver);
    receiverCol.setWidth("150px");
    receiverCol.setSortable(true);
    receiverCol.setName(FIELD_RECEIVER);
    columnDefs.add(receiverCol);

    receiveDateCol = new RGridColumnDef(PInvoiceReceiveDef.receiveDate);
    receiveDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    receiveDateCol.setWidth("120px");
    receiveDateCol.setSortable(true);
    receiveDateCol.setName(FIELD_RECEIVEDATE);
    columnDefs.add(receiveDateCol);

    remarkCol = new RGridColumnDef(PInvoiceReceiveDef.remark);
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

    flecsPanel.addField(PInvoiceReceiveDef.billNumber);
    flecsPanel.addField(PInvoiceReceiveDef.bizState);
    flecsPanel.addField(PInvoiceReceiveDef.accountUnit);
    flecsPanel.addField(PInvoiceReceiveDef.issuer);
    flecsPanel.addField(PInvoiceReceiveDef.receiver);
    flecsPanel.addField(PInvoiceReceiveDef.invoice_code);
    flecsPanel.addField(PInvoiceReceiveDef.invoice_number);
    flecsPanel.addField(PInvoiceReceiveDef.receiveDate);
    flecsPanel.addField(PInvoiceReceiveDef.invoiceType);

    if (getEP().isPermEnabled()) {
      flecsPanel.addField(PInvoiceReceiveDef.permGroupId);
    }
    flecsPanel.addField(PInvoiceReceiveDef.createInfo_operator_id);
    flecsPanel.addField(PInvoiceReceiveDef.createInfo_operator_fullName);
    flecsPanel.addField(PInvoiceReceiveDef.createInfo_time);
    flecsPanel.addField(PInvoiceReceiveDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PInvoiceReceiveDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PInvoiceReceiveDef.lastModifyInfo_time);

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
      conditions.add(new Condition(PInvoiceReceiveDef.billNumber, DefaultOperator.STARTS_WITH,
          getSearchKeyWork(params)));
      return conditions;
    }

    conditions.add(new Condition(PInvoiceReceiveDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceReceiveDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceReceiveDef.accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceReceiveDef.issuer, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceReceiveDef.receiver, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceReceiveDef.invoice_code, DefaultOperator.EQUALS, null));
    conditions
        .add(new Condition(PInvoiceReceiveDef.invoice_number, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PInvoiceReceiveDef.receiveDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceReceiveDef.invoiceType, DefaultOperator.EQUALS, null));
    return conditions;
  }

  @Override
  protected Object getGridData(int row, int col, BInvoiceReceive rowData,
      List<BInvoiceReceive> pageData) {
    if (col == billNumberCol.getIndex()) {
      return rowData.getBillNumber();
    } else if (col == bizStateCol.getIndex()) {
      return BBizStates.getCaption(rowData.getBizState());
    } else if (col == invoiceTypeCol.getIndex()) {
      return rowData.getInvoiceType() == null ? null : getEP().getInvoiceTypes().get(
          rowData.getInvoiceType());
    } else if (col == accountUnitCol.getIndex()) {
      return BUCN.toFriendlyStr(rowData.getAccountUnit(), null);
    } else if (col == issuerCol.getIndex()) {
      return rowData.getIssuer() == null ? null : rowData.getIssuer().toFriendlyStr();
    } else if (col == receiverCol.getIndex()) {
      return rowData.getReceiver() == null ? null : rowData.getReceiver().toFriendlyStr();
    } else if (col == receiveDateCol.getIndex()) {
      return rowData.getReceiveDate();
    } else if (col == remarkCol.getIndex()) {
      return rowData.getRemark();
    }
    return null;
  }

  @Override
  protected BInvoiceReceive[] newEntityArray() {
    return new BInvoiceReceive[] {};
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
      if (field == PInvoiceReceiveDef.accountUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == PInvoiceReceiveDef.permGroupId) {
        return new PermGroupField(field);
      } else if (field == PInvoiceReceiveDef.issuer) {
        return DefaultOperator.EQUALS == operator ? new EmployeeUCNBox() : null;
      } else if (field == PInvoiceReceiveDef.receiver) {
        return DefaultOperator.EQUALS == operator ? new EmployeeUCNBox() : null;
      } else if (field == PInvoiceReceiveDef.invoiceType) {
          RComboBox<String> invoiceTypeBox = new RComboBox<String>(PInvoiceReceiveDef.invoiceType);
          invoiceTypeBox.setEditable(false);
          for (Map.Entry<String, String> item : getEP().getInvoiceTypes().entrySet())
            invoiceTypeBox.addOption(item.getKey(), item.getValue());
        return invoiceTypeBox;
      }
      return super.createOperandWidget(field, operator);
    }
  }

  private class MyOperatorPolicy implements OperatorPolicy {

    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (PInvoiceReceiveDef.accountUnit == field || PInvoiceReceiveDef.issuer == field
          || PInvoiceReceiveDef.receiver == field || field == PInvoiceReceiveDef.invoice_code) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PInvoiceReceiveDef.bizState == field || PInvoiceReceiveDef.invoiceType == field
          || PInvoiceReceiveDef.permGroupId == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PInvoiceReceiveDef.invoice_number == field) {
        result.add(DefaultOperator.CONTAINS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }
}
