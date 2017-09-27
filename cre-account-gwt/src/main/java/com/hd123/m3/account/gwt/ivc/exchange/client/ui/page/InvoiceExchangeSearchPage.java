/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceExchangeSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月17日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.ivc.exchange.client.ui.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.ivc.exchange.client.EPInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BExchangeType;
import com.hd123.m3.account.gwt.ivc.exchange.client.biz.BInvoiceExchange;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.InvoiceExchangeUrlParams;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.InvoiceExchangeUrlParams.Search;
import com.hd123.m3.account.gwt.ivc.exchange.intf.client.dd.PInvoiceExchangeDef;
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
 * 发票交换单|搜索页面
 * 
 * @author LiBin
 * @since 1.7
 * 
 */
public class InvoiceExchangeSearchPage extends BaseBpmSearchPage2<BInvoiceExchange> implements
    Search {

  private static InvoiceExchangeSearchPage instance = null;

  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef exchangeTypeCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef exchangerCol;
  private RGridColumnDef exchangeDateCol;
  private RGridColumnDef remarkCol;

  @Override
  protected EPInvoiceExchange getEP() {
    return EPInvoiceExchange.getInstance();
  }

  public static InvoiceExchangeSearchPage getInstance() {
    if (instance == null) {
      instance = new InvoiceExchangeSearchPage();
    }
    return instance;
  }

  @Override
  protected List<RGridColumnDef> createGridColumns() {
    List<RGridColumnDef> columnDefs = new ArrayList<RGridColumnDef>();

    billNumberCol = new RGridColumnDef(PInvoiceExchangeDef.billNumber);
    billNumberCol.setWidth("180px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setSortable(true);
    billNumberCol.setName(InvoiceExchangeUrlParams.PN_BILLNUMBER);
    billNumberCol.setAllowHiding(false);
    columnDefs.add(billNumberCol);

    bizStateCol = new RGridColumnDef(PInvoiceExchangeDef.bizState);
    bizStateCol.setWidth("100px");
    bizStateCol.setSortable(true);
    bizStateCol.setName(FIELD_BIZSTATE);
    columnDefs.add(bizStateCol);

    exchangeTypeCol = new RGridColumnDef(PInvoiceExchangeDef.exchangeType);
    exchangeTypeCol.setWidth("150px");
    exchangeTypeCol.setSortable(true);
    exchangeTypeCol.setName(FIELD_EXCHANGE_TYPE);
    columnDefs.add(exchangeTypeCol);

    accountUnitCol = new RGridColumnDef(PInvoiceExchangeDef.accountUnit);
    accountUnitCol.setWidth("120px");
    accountUnitCol.setSortable(true);
    accountUnitCol.setName(FIELD_ACCOUNT_UNIT);
    columnDefs.add(accountUnitCol);

    exchangerCol = new RGridColumnDef(PInvoiceExchangeDef.exchanger);
    exchangerCol.setWidth("150px");
    exchangerCol.setSortable(true);
    exchangerCol.setName(FIELD_EXCHANGER);
    columnDefs.add(exchangerCol);

    exchangeDateCol = new RGridColumnDef(PInvoiceExchangeDef.exchangeDate);
    exchangeDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    exchangeDateCol.setWidth("150px");
    exchangeDateCol.setSortable(true);
    exchangeDateCol.setName(FIELD_EXCHANGE_DATE);
    columnDefs.add(exchangeDateCol);

    remarkCol = new RGridColumnDef(PInvoiceExchangeDef.remark);
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

    flecsPanel.addField(PInvoiceExchangeDef.billNumber);
    flecsPanel.addField(PInvoiceExchangeDef.bizState);
    flecsPanel.addField(PInvoiceExchangeDef.accountUnit);
    flecsPanel.addField(PInvoiceExchangeDef.exchanger);
    flecsPanel.addField(PInvoiceExchangeDef.invoiceCode);
    flecsPanel.addField(PInvoiceExchangeDef.invoiceNumber);
    flecsPanel.addField(PInvoiceExchangeDef.exchangeDate);
    flecsPanel.addField(PInvoiceExchangeDef.exchangeType);

    if (getEP().isPermEnabled()) {
      flecsPanel.addField(PInvoiceExchangeDef.permGroupId);
    }
    flecsPanel.addField(PInvoiceExchangeDef.createInfo_operator_id);
    flecsPanel.addField(PInvoiceExchangeDef.createInfo_operator_fullName);
    flecsPanel.addField(PInvoiceExchangeDef.createInfo_time);
    flecsPanel.addField(PInvoiceExchangeDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PInvoiceExchangeDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PInvoiceExchangeDef.lastModifyInfo_time);

    flecsPanel.addBeforeLoadDataHandler(new FlecsBeforeLoadDataHandler());

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
      conditions.add(new Condition(PInvoiceExchangeDef.billNumber, DefaultOperator.STARTS_WITH,
          getSearchKeyWork(params)));
      return conditions;
    }

    conditions.add(new Condition(PInvoiceExchangeDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceExchangeDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceExchangeDef.accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceExchangeDef.exchanger, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceExchangeDef.invoiceCode, DefaultOperator.CONTAINS, null));
    conditions
        .add(new Condition(PInvoiceExchangeDef.invoiceNumber, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PInvoiceExchangeDef.exchangeDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceExchangeDef.exchangeType, DefaultOperator.EQUALS, null));
    return conditions;
  }

  @Override
  protected Object getGridData(int row, int col, BInvoiceExchange rowData,
      List<BInvoiceExchange> pageData) {
    if (col == billNumberCol.getIndex()) {
      return rowData.getBillNumber();
    } else if (col == bizStateCol.getIndex()) {
      return BBizStates.getCaption(rowData.getBizState());
    } else if (col == exchangeTypeCol.getIndex()) {
      return rowData.getType() == null ? null : rowData.getType().getCaption();
    } else if (col == accountUnitCol.getIndex()) {
      return BUCN.toFriendlyStr(rowData.getAccountUnit(), null);
    } else if (col == exchangerCol.getIndex()) {
      return BUCN.toFriendlyStr(rowData.getExchanger(), null);
    } else if (col == exchangeDateCol.getIndex()) {
      return rowData.getExchangeDate();
    } else if (col == remarkCol.getIndex()) {
      return rowData.getRemark();
    }
    return null;
  }

  @Override
  protected BInvoiceExchange[] newEntityArray() {
    return new BInvoiceExchange[] {};
  }

  private class FlecsBeforeLoadDataHandler implements BeforeLoadDataHandler<Integer> {
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
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (field == PInvoiceExchangeDef.accountUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == PInvoiceExchangeDef.permGroupId) {
        return buildPermGroupBox(field);
      } else if (field == PInvoiceExchangeDef.exchanger) {
        return DefaultOperator.EQUALS == operator ? new EmployeeUCNBox() : null;
      } else if (field == PInvoiceExchangeDef.exchangeType) {
        return buidExchangeTypeBox();
      }
      return super.createOperandWidget(field, operator);
    }

    private RComboBox<String> buidExchangeTypeBox() {
      RComboBox<String> exchangeTypeBox = new RComboBox<String>(PInvoiceExchangeDef.exchangeType);
      exchangeTypeBox.setEditable(false);

      exchangeTypeBox.addOption(BExchangeType.eviToEvi.name(), BExchangeType.eviToEvi.getCaption());
      exchangeTypeBox.addOption(BExchangeType.eviToInv.name(), BExchangeType.eviToInv.getCaption());
      exchangeTypeBox.addOption(BExchangeType.invToInv.name(), BExchangeType.invToInv.getCaption());

      return exchangeTypeBox;
    }

    public RComboBox<String> buildPermGroupBox(FieldDef fieldDef) {
      RComboBox<String> permGroupBox = new RComboBox<String>();

      permGroupBox.setFieldDef(fieldDef);
      permGroupBox.setEditable(false);
      permGroupBox.setMaxDropdownRowCount(10);
      for (Entry<String, String> entry : getEP().getUserGroups().entrySet()) {
        permGroupBox.addOption(entry.getKey(), entry.getValue());
      }

      return permGroupBox;
    }
  }

  private class MyOperatorPolicy implements OperatorPolicy {

    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      List<Operator> result = new ArrayList<Operator>();
      if (PInvoiceExchangeDef.accountUnit == field || PInvoiceExchangeDef.exchanger == field) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PInvoiceExchangeDef.bizState == field || PInvoiceExchangeDef.exchangeType == field
          || PInvoiceExchangeDef.permGroupId == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;

      } else if (PInvoiceExchangeDef.invoiceCode == field
          || PInvoiceExchangeDef.invoiceNumber == field) {
        result.add(DefaultOperator.CONTAINS);
        return result;

      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }

}
