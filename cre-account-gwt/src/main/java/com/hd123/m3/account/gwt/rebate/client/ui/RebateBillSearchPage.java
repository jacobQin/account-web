/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateBillSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpartCodec;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.BPositionType;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.SPositionCodec;
import com.hd123.m3.account.gwt.cpnts.client.ui.position.TypePositionBox;
import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.intf.client.RebateBillUrlParams.Flecs;
import com.hd123.m3.account.gwt.rebate.intf.client.dd.PRebateBillDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmSearchPage2;
import com.hd123.m3.commons.gwt.util.client.flecs.BUCNCodec;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.widget.client.ui.search.FlecsSearchGadget;
import com.hd123.rumba.commons.gwt.mini.client.codec.CodecUtils;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.flecs.client.DefaultOperandWidgetFactory;
import com.hd123.rumba.gwt.flecs.client.DefaultOperator;
import com.hd123.rumba.gwt.flecs.client.FlecsConfig;
import com.hd123.rumba.gwt.flecs.client.Operator;
import com.hd123.rumba.gwt.flecs.client.OperatorPolicy;
import com.hd123.rumba.gwt.flecs.client.RFlecsPanel;
import com.hd123.rumba.gwt.flecs.client.codec.DefaultOperatorCodec;
import com.hd123.rumba.gwt.flecs.client.codec.FlecsConfigCodec;
import com.hd123.rumba.gwt.util.client.OrderDir;
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
 * 销售额返款单搜索页面
 * 
 * @author lixiaohong
 * @since 1.12
 *
 */
public class RebateBillSearchPage extends BaseBpmSearchPage2<BRebateBill> implements Flecs {
  private static RebateBillSearchPage instance;
  public static final String PN_SEARCH_MODE = "search_mode";

  private FlecsSearchGadget searchGadget;

  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef storeCol;
  private RGridColumnDef tenantCol;
  private RGridColumnDef contractCol;
  private RGridColumnDef positionsCol;
  private RGridColumnDef accountDateCol;
  private RGridColumnDef beginDateCol;
  private RGridColumnDef endDateCol;
  private RGridColumnDef backTotalCol;
  private RGridColumnDef poundageTotalCol;
  private RGridColumnDef shouldBackTotalCol;

  @Override
  protected void afterDraw() {
    super.afterDraw();
    getRoot().setSpacing(0);
  }

  @Override
  protected EPRebateBill getEP() {
    return EPRebateBill.getInstance();
  }

  public static RebateBillSearchPage getInstance() {
    if (instance == null) {
      instance = new RebateBillSearchPage();
    }
    return instance;
  }

  @Override
  protected void drawSelf() {
    super.drawSelf();
    getRoot().add(drawSearchGadget());
  }

  @Override
  protected FlecsConfigCodec createFlecsConfigCodec() {
    FlecsConfigCodec codec = new FlecsConfigCodec();
    codec.addOperatorCodec(new DefaultOperatorCodec());
    codec.addOperandCodec(new BCounterpartCodec());
    codec.addOperandCodec(new SPositionCodec());
    codec.addOperandCodec(new BUCNCodec());
    return codec;
  }

  private Widget drawSearchGadget() {
    searchGadget = new FlecsSearchGadget(flecsPanel);
    searchGadget.setColumnCount(3);
    searchGadget.setSimplifySearch(getDefaultConditions(new JumpParameters()).toArray(
        new Condition[] {}));
    searchGadget.addBeforeLoadDataHandler(new BeforeLoadDataHandler<Integer>() {

      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent<Integer> event) {
        JumpParameters params = new JumpParameters(getStartNode());
        FlecsConfig flecsConfig = searchGadget.getCurrentConfig();
        params.getUrlRef().set(PN_FLECSCONFIG, flecsCodec.encode(flecsConfig));
        params.getUrlRef().set(PN_PAGE, String.valueOf(event.getParameter()));
        params.getUrlRef().set(PN_SEARCH_MODE, searchGadget.getSearchMode());// 精简搜索
        getEP().jump(params);
        event.cancel();
      }
    });
    return searchGadget;
  }

  @Override
  protected List<RGridColumnDef> createGridColumns() {
    List<RGridColumnDef> columnDefs = new ArrayList<RGridColumnDef>();

    billNumberCol = new RGridColumnDef(PRebateBillDef.billNumber);
    billNumberCol.setWidth("160px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("120px")));
    billNumberCol.setSortable(true);
    billNumberCol.setResizable(true);
    billNumberCol.setName(PN_BILLNUMBER);
    billNumberCol.setAllowHiding(false);
    columnDefs.add(billNumberCol);

    bizStateCol = new RGridColumnDef(PRebateBillDef.bizState);
    bizStateCol.setWidth("80px");
    bizStateCol.setSortable(true);
    bizStateCol.setResizable(true);
    bizStateCol.setName(FIELD_BIZSTATE);
    columnDefs.add(bizStateCol);

    storeCol = new RGridColumnDef(PRebateBillDef.store);
    storeCol.setWidth("120px");
    storeCol.setSortable(true);
    storeCol.setResizable(true);
    storeCol.setName(FIELD_STORE);
    columnDefs.add(storeCol);

    tenantCol = new RGridColumnDef(PRebateBillDef.tenant);
    tenantCol.setWidth("120px");
    tenantCol.setSortable(true);
    tenantCol.setResizable(true);
    tenantCol.setName(FIELD_TENANT);
    columnDefs.add(tenantCol);

    contractCol = new RGridColumnDef(PRebateBillDef.contract);
    contractCol.setWidth("120px");
    contractCol.setSortable(true);
    contractCol.setResizable(true);
    contractCol.setName(FIELD_CONTRACT);
    columnDefs.add(contractCol);

    positionsCol = new RGridColumnDef(PRebateBillDef.positions);
    positionsCol.setWidth("150px");
    positionsCol.setSortable(true);
    positionsCol.setResizable(true);
    positionsCol.setName(FIELD_POSITIONS);
    columnDefs.add(positionsCol);

    accountDateCol = new RGridColumnDef(PRebateBillDef.accountDate);
    accountDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    accountDateCol.setWidth("100px");
    accountDateCol.setSortable(true);
    accountDateCol.setResizable(true);
    accountDateCol.setName(FIELD_ACCOUNT_DATE);
    columnDefs.add(accountDateCol);

    beginDateCol = new RGridColumnDef(PRebateBillDef.beginDate);
    beginDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    beginDateCol.setWidth("100px");
    beginDateCol.setSortable(true);
    beginDateCol.setResizable(true);
    beginDateCol.setName(FIELD_BEGINDATE);
    columnDefs.add(beginDateCol);

    endDateCol = new RGridColumnDef(PRebateBillDef.endDate);
    endDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    endDateCol.setWidth("100px");
    endDateCol.setSortable(true);
    endDateCol.setResizable(true);
    endDateCol.setName(FIELD_ENDDATE);
    columnDefs.add(endDateCol);

    backTotalCol = new RGridColumnDef(PRebateBillDef.backTotal);
    backTotalCol.setWidth("100px");
    backTotalCol.setSortable(true);
    backTotalCol.setResizable(true);
    backTotalCol.setName(FIELD_BACKTOTAL);
    backTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    columnDefs.add(backTotalCol);

    poundageTotalCol = new RGridColumnDef(PRebateBillDef.poundageTotal);
    poundageTotalCol.setWidth("100px");
    poundageTotalCol.setSortable(true);
    poundageTotalCol.setResizable(true);
    poundageTotalCol.setName(FIELD_POUNDAGETOTAL);
    poundageTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    columnDefs.add(poundageTotalCol);

    shouldBackTotalCol = new RGridColumnDef(PRebateBillDef.shouldBackTotal);
    shouldBackTotalCol.setSortable(true);
    shouldBackTotalCol.setResizable(true);
    shouldBackTotalCol.setWidth("100px");
    shouldBackTotalCol.setName(FIELD_SHOULDBACKTOTAL);
    shouldBackTotalCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    columnDefs.add(shouldBackTotalCol);

    return columnDefs;
  }

  @Override
  protected RFlecsPanel drawFlecsPanel() {
    flecsPanel = new RFlecsPanel(GROUP_ID, pagingGrid);
    flecsPanel.setShowToolbar(false);
    flecsPanel.setOperandWidgetFactory(new MyOperandWidgetFactory());
    flecsPanel.setOperatorPolicy(new MyOperatorPolicy());

    flecsPanel.addField(PRebateBillDef.billNumber);
    flecsPanel.addField(PRebateBillDef.bizState);
    flecsPanel.addField(PRebateBillDef.contract_name);
    flecsPanel.addField(PRebateBillDef.store);
    flecsPanel.addField(PRebateBillDef.tenant);
    flecsPanel.addField(PRebateBillDef.position);
    flecsPanel.addField(PRebateBillDef.accountDate);

    if (getEP().isPermEnabled()) {
      flecsPanel.addField(PRebateBillDef.permGroupId);
    }
    flecsPanel.addField(PRebateBillDef.createInfo_operator_id);
    flecsPanel.addField(PRebateBillDef.createInfo_operator_fullName);
    flecsPanel.addField(PRebateBillDef.createInfo_time);
    flecsPanel.addField(PRebateBillDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PRebateBillDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PRebateBillDef.lastModifyInfo_time);

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
      conditions.add(new Condition(PRebateBillDef.billNumber, DefaultOperator.STARTS_WITH,
          getSearchKeyWork(params)));
      return conditions;
    }

    conditions.add(new Condition(PRebateBillDef.billNumber, DefaultOperator.STARTS_WITH, null));
    conditions.add(new Condition(PRebateBillDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PRebateBillDef.contract_name, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PRebateBillDef.store, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PRebateBillDef.tenant, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PRebateBillDef.position, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PRebateBillDef.accountDate, DefaultOperator.EQUALS, null));
    return conditions;
  }

  @Override
  protected Object getGridData(int row, int col, BRebateBill rowData, List<BRebateBill> pageData) {
    if (col == billNumberCol.getIndex()) {
      return rowData.getBillNumber();
    } else if (col == bizStateCol.getIndex()) {
      return BBizStates.getCaption(rowData.getBizState());
    } else if (col == storeCol.getIndex()) {
      return BUCN.toFriendlyStr(rowData.getStore(), null);
    } else if (col == tenantCol.getIndex()) {
      return BUCN.toFriendlyStr(rowData.getTenant(), null);
    } else if (col == contractCol.getIndex()) {
      return BUCN.toFriendlyStr(rowData.getContract(), null);
    } else if (col == positionsCol.getIndex()) {
      return rowData.getPositions();
    } else if (col == accountDateCol.getIndex()) {
      return rowData.getAccountDate();
    } else if (col == beginDateCol.getIndex()) {
      return rowData.getBeginDate();
    } else if (col == endDateCol.getIndex()) {
      return rowData.getEndDate();
    } else if (col == shouldBackTotalCol.getIndex()) {
      BigDecimal total = (rowData.getShouldBackTotal() == null) ? BigDecimal.ZERO : rowData
          .getShouldBackTotal();
      return M3Format.fmt_money.format(total.doubleValue());
    } else if (col == poundageTotalCol.getIndex()) {
      BigDecimal total = (rowData.getPoundageTotal() == null) ? BigDecimal.ZERO : rowData
          .getPoundageTotal();
      return M3Format.fmt_money.format(total.doubleValue());
    } else if (col == backTotalCol.getIndex()) {
      BigDecimal total = (rowData.getBackTotal() == null) ? BigDecimal.ZERO : rowData
          .getBackTotal();
      return M3Format.fmt_money.format(total.doubleValue());
    }
    return null;
  }

  @Override
  protected BRebateBill[] newEntityArray() {
    return new BRebateBill[] {};
  }

  @Override
  protected void refreshFlecs(JumpParameters params) {
    grid.setSort(getIdentifyColumn(), OrderDir.desc, false);
    grid.setAllowHorizontalScrollBar(true);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(getSearchKeyWork(params)) == false) {
      searchGadget.search(new Condition(PRebateBillDef.billNumber, DefaultOperator.STARTS_WITH,
          getSearchKeyWork(params)));
    } else {
      String flecsStr = params.getUrlRef().get(PN_FLECSCONFIG);
      String pageInt = params.getUrlRef().get(PN_PAGE);
      int startPage = CodecUtils.decodeInt(pageInt, 0);
      if (flecsStr != null) {
        searchGadget.search(params.getUrlRef().get(PN_SEARCH_MODE), flecsCodec.decode(flecsStr),
            startPage);
      } else {
        searchGadget.resetDefaultSearch();
      }
    }
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (field == PRebateBillDef.store) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == PRebateBillDef.tenant) {
        CounterpartUCNBox widget = new CounterpartUCNBox();
        widget
            .setCaption(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
        widget.setCounterpartTypes(Arrays.asList(BCounterpart.COUNTERPART_TYPE_TENANT));
        widget.showStateField(true);
        return DefaultOperator.EQUALS == operator ? widget : null;
      } else if (field == PRebateBillDef.permGroupId) {
        RComboBox<String> permGroupField = new RComboBox<String>();
        permGroupField.setEditable(false);
        permGroupField.setMaxDropdownRowCount(10);
        if (getEP().getUserGroups() != null) {
          for (String key : getEP().getUserGroups().keySet()) {
            permGroupField.addOption(key, getEP().getUserGroups().get(key));
          }
        }
        return permGroupField;
      } else if (field == PRebateBillDef.position) {
        TypePositionBox widget = new TypePositionBox();
        widget.setPositionType(BPositionType.shoppe);
        widget.setShowDialogState(true);
        return widget;
      }
      return super.createOperandWidget(field, operator);
    }
  }

  private class MyOperatorPolicy implements OperatorPolicy {
    @Override
    public List<Operator> getOperatorsForField(FieldDef field, List<Operator> defaultOperators) {
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      if (PRebateBillDef.store == field || PRebateBillDef.tenant == field) {
        List<Operator> result = new ArrayList<Operator>();
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PRebateBillDef.permGroupId == field) {
        List<Operator> result = new ArrayList<Operator>();
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PRebateBillDef.position == field) {
        List<Operator> result = new ArrayList<Operator>();
        result.add(DefaultOperator.CONTAINS);
        return result;
      }
      return defaultOperators;
    }
  }
}
