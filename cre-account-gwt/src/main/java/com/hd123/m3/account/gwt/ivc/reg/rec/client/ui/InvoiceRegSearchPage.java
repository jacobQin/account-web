/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpartCodec;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.CounterpartUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.store.StoreNavigatorTreeGadget;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget.InvoiceRegOptionDialog;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegUrlParams;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.perm.BAction;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmSearchPage2;
import com.hd123.m3.commons.gwt.util.client.flecs.BUCNCodec;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.operate.OperateInfoUtil;
import com.hd123.m3.commons.gwt.widget.client.ui.search.FlecsSearchGadget;
import com.hd123.rumba.commons.gwt.mini.client.codec.CodecUtils;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.flecs.client.Condition;
import com.hd123.rumba.gwt.flecs.client.DefaultOperandWidgetFactory;
import com.hd123.rumba.gwt.flecs.client.DefaultOperator;
import com.hd123.rumba.gwt.flecs.client.FlecsConfig;
import com.hd123.rumba.gwt.flecs.client.FlecsQueryDef;
import com.hd123.rumba.gwt.flecs.client.Operator;
import com.hd123.rumba.gwt.flecs.client.OperatorPolicy;
import com.hd123.rumba.gwt.flecs.client.RFlecsPanel;
import com.hd123.rumba.gwt.flecs.client.codec.DefaultOperatorCodec;
import com.hd123.rumba.gwt.flecs.client.codec.FlecsConfigCodec;
import com.hd123.rumba.gwt.util.client.OrderDir;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RDateTimeRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RMultiVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票登记单搜索页面
 * 
 * @author chenrizhang
 * @since 1.7
 * 
 */
public class InvoiceRegSearchPage extends BaseBpmSearchPage2<BInvoiceReg> implements
    InvoiceRegUrlParams.Flecs {

  public static final String PN_SEARCH_MODE = "search_mode";
  public static final String PN_STORE_ID = "storeId";

  private static InvoiceRegSearchPage instance;

  private RAction optionsAction;
  private InvoiceRegOptionDialog optionDialog;

  private StoreNavigatorTreeGadget storeNavigator;

  private FlecsSearchGadget searchGadget;

  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef counterpartCol;
  private RGridColumnDef invoiceTypeCol;
  private RGridColumnDef regDateCol;
  private RGridColumnDef createInfoCol;
  private RGridColumnDef remarkCol;

  @Override
  protected EPInvoiceRegReceipt getEP() {
    return EPInvoiceRegReceipt.getInstance();
  }

  public static InvoiceRegSearchPage getInstance() {
    if (instance == null) {
      instance = new InvoiceRegSearchPage();
    }
    return instance;
  }

  @Override
  protected void drawToolbar() {
    super.drawToolbar();

    optionsAction = new RAction(RActionFacade.OPTION, new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        if (optionDialog == null) {
          optionDialog = new InvoiceRegOptionDialog();
        }
        optionDialog.showDialog();
      }
    });
    getToolbar().add(new RToolbarButton(optionsAction), Alignment.right);
  }

  @Override
  protected FlecsConfigCodec createFlecsConfigCodec() {
    FlecsConfigCodec codec = new FlecsConfigCodec();
    codec.addOperatorCodec(new DefaultOperatorCodec());
    codec.addOperandCodec(new BCounterpartCodec());
    codec.addOperandCodec(new BUCNCodec());
    return codec;
  }

  @Override
  protected void drawSelf() {
    super.drawSelf();

    RMultiVerticalPanel panel = new RMultiVerticalPanel();
    panel.setWidth("100%");
    panel.setColumnWidth(0, "200px");

    panel.add(0, drawNavigatorGadget());
    panel.add(1, drawSearchGadget());
    getRoot().add(panel);
    getRoot().setSpacing(0);
  }

  private Widget drawNavigatorGadget() {
    VerticalPanel panel = new VerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(8);
    storeNavigator = new StoreNavigatorTreeGadget();
    storeNavigator.addActionHandler(new RActionHandler() {

      @Override
      public void onAction(RActionEvent event) {
        JumpParameters params = new JumpParameters(getStartNode());
        FlecsConfig flecsConfig = searchGadget.getCurrentConfig();
        params.getUrlRef().set(PN_FLECSCONFIG, flecsCodec.encode(flecsConfig));
        params.getUrlRef().set(PN_SEARCH_MODE, searchGadget.getSearchMode());
        params.getUrlRef().set(PN_STORE_ID, storeNavigator.getSelected());
        getEP().jump(params);
      }
    });
    panel.add(storeNavigator);
    return panel;
  }

  private Widget drawSearchGadget() {
    searchGadget = new FlecsSearchGadget(flecsPanel);
    searchGadget.setColumnCount(2);
    searchGadget.setSimplifySearch(getDefaultConditions(new JumpParameters()).toArray(
        new Condition[] {}));
    searchGadget.addBeforeLoadDataHandler(new BeforeLoadDataHandler<Integer>() {

      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent<Integer> event) {
        JumpParameters params = new JumpParameters(getStartNode());
        FlecsConfig flecsConfig = searchGadget.getCurrentConfig();
        params.getUrlRef().set(PN_FLECSCONFIG, flecsCodec.encode(flecsConfig));
        params.getUrlRef().set(PN_PAGE, String.valueOf(event.getParameter()));
        params.getUrlRef().set(PN_SEARCH_MODE, searchGadget.getSearchMode());
        params.getUrlRef().set(PN_STORE_ID, storeNavigator.getSelected());
        getEP().jump(params);
        event.cancel();
      }
    });
    return searchGadget;
  }

  @Override
  protected List<RGridColumnDef> createGridColumns() {
    List<RGridColumnDef> columnDefs = new ArrayList<RGridColumnDef>();

    billNumberCol = new RGridColumnDef(PInvoiceRegDef.billNumber);
    billNumberCol.setWidth("180px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setSortable(true);
    billNumberCol.setName(FIELD_BILLNUMBER);
    billNumberCol.setAllowHiding(false);
    columnDefs.add(billNumberCol);

    bizStateCol = new RGridColumnDef(PInvoiceRegDef.bizState);
    bizStateCol.setWidth("100px");
    bizStateCol.setSortable(true);
    bizStateCol.setName(FIELD_BIZSTATE);
    columnDefs.add(bizStateCol);

    counterpartCol = new RGridColumnDef(EPInvoiceRegReceipt.PInvoiceRegDef_counterpart);
    counterpartCol.setWidth("120px");
    counterpartCol.setSortable(true);
    counterpartCol.setName(FIELD_COUNTERPART);
    columnDefs.add(counterpartCol);

    invoiceTypeCol = new RGridColumnDef(PInvoiceRegDef.invoiceType);
    invoiceTypeCol.setWidth("100px");
    invoiceTypeCol.setSortable(true);
    invoiceTypeCol.setName(FIELD_INVOICE_TYPE);
    columnDefs.add(invoiceTypeCol);

    regDateCol = new RGridColumnDef(PInvoiceRegDef.regDate);
    regDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    regDateCol.setWidth("120px");
    regDateCol.setSortable(true);
    regDateCol.setName(FIELD_REGDATE);
    columnDefs.add(regDateCol);

    createInfoCol = new RGridColumnDef(PInvoiceRegDef.createInfo_time);
    createInfoCol.setSortable(true);
    createInfoCol.setName(FIELD_CREATE_INFO_TIME);
    columnDefs.add(createInfoCol);

    remarkCol = new RGridColumnDef(PInvoiceRegDef.remark);
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

    flecsPanel.addField(PInvoiceRegDef.billNumber);
    flecsPanel.addField(PInvoiceRegDef.bizState);
    flecsPanel.addField(EPInvoiceRegReceipt.PInvoiceRegDef_counterpart);
    flecsPanel.addField(PInvoiceRegDef.invoice_code);
    flecsPanel.addField(PInvoiceRegDef.invoice_number);
    flecsPanel.addField(PInvoiceRegDef.regDate);
    flecsPanel.addField(PInvoiceRegDef.invoiceType);
    flecsPanel.addField(PInvoiceRegDef.sourceBill_billNumber);
    if (getEP().isPermEnabled()) {
      flecsPanel.addField(PInvoiceRegDef.permGroupId);
    }
    flecsPanel.addField(PInvoiceRegDef.createInfo_operator_id);
    flecsPanel.addField(PInvoiceRegDef.createInfo_operator_fullName);
    flecsPanel.addField(PInvoiceRegDef.createInfo_time);
    flecsPanel.addField(PInvoiceRegDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PInvoiceRegDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PInvoiceRegDef.lastModifyInfo_time);

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
      conditions.add(new Condition(PInvoiceRegDef.billNumber, DefaultOperator.STARTS_WITH,
          getSearchKeyWork(params)));
      return conditions;
    }

    conditions.add(new Condition(PInvoiceRegDef.invoice_code, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRegDef.invoice_number, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PInvoiceRegDef.regDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(EPInvoiceRegReceipt.PInvoiceRegDef_counterpart,
        DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRegDef.invoiceType, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceRegDef.bizState, DefaultOperator.EQUALS, null));
    return conditions;
  }

  @Override
  protected Object getGridData(int row, int col, BInvoiceReg rowData, List<BInvoiceReg> pageData) {
    if (col == billNumberCol.getIndex()) {
      return rowData.getBillNumber();
    } else if (col == bizStateCol.getIndex()) {
      return BBizStates.getCaption(rowData.getBizState());
    } else if (col == counterpartCol.getIndex()) {
      return BUCN.toFriendlyStr(rowData.getCounterpart(), "");
    } else if (col == invoiceTypeCol.getIndex()) {
      return rowData.getInvoiceType() == null ? null : getEP().getInvoiceTypes().get(
          rowData.getInvoiceType());
    } else if (col == regDateCol.getIndex()) {
      return rowData.getRegDate();
    } else if (col == regDateCol.getIndex()) {
      return rowData.getRegDate();
    } else if (col == createInfoCol.getIndex()) {
      return OperateInfoUtil.toString(rowData.getCreateInfo());
    } else if (col == remarkCol.getIndex()) {
      return rowData.getRemark();
    }
    return null;
  }

  @Override
  protected void refreshCommands() {
    super.refreshCommands();
    optionsAction.setVisible(getEP().isPermitted(BAction.CONFIG.getKey()));
  }

  @Override
  protected void refreshFlecs(JumpParameters params) {
    if (params.getUrlRef().get(PN_STORE_ID) != null) {
      storeNavigator.setCurrent(params.getUrlRef().get(PN_STORE_ID));
    }

    grid.setSort(billNumberCol, OrderDir.desc, false);

    flecsPanel.clearConditions();
    if (StringUtil.isNullOrBlank(getSearchKeyWork(params)) == false) {
      searchGadget.search(new Condition(PInvoiceRegDef.billNumber, DefaultOperator.STARTS_WITH,
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
    if (StringUtil.isNullOrBlank(storeNavigator.getSelected()))// 没有选中则需要选择第一个
      storeNavigator.selectFirst();
  }

  @Override
  protected BInvoiceReg[] newEntityArray() {
    return new BInvoiceReg[] {};
  }

  @Override
  protected FlecsQueryDef buildQueryDef(int page, int pageSize, String sortField, OrderDir sortDir) {
    FlecsQueryDef queryDef = super.buildQueryDef(page, pageSize, sortField, sortDir);
    if (StringUtil.isNullOrBlank(storeNavigator.getSelected())) {
      queryDef.getConditions().add(
          new FlecsQueryDef.Condition(FIELD_ACCOUNT_UNIT, DefaultOperator.EQUALS, "-"));// 如此，查不到数据
    } else {
      queryDef.getConditions().add(
          new FlecsQueryDef.Condition(FIELD_ACCOUNT_UNIT, DefaultOperator.EQUALS, storeNavigator
              .getSelected()));
    }
    return queryDef;
  }

  public String getCurrentStoreUuid() {
    return storeNavigator.getSelected();
  }

  private class MyOperandWidgetFactory extends DefaultOperandWidgetFactory {
    @Override
    public Widget createOperandWidget(FieldDef field, Operator operator) {
      if (field == PInvoiceRegDef.accountUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == PInvoiceRegDef.permGroupId) {
        RComboBox<String> permGroupField = new RComboBox<String>();
        permGroupField.setEditable(false);
        permGroupField.setMaxDropdownRowCount(10);
        if (getEP().getUserGroups() != null) {
          for (String key : getEP().getUserGroups().keySet()) {
            permGroupField.addOption(key, getEP().getUserGroups().get(key));
          }
        }
        return permGroupField;
      } else if (field == EPInvoiceRegReceipt.PInvoiceRegDef_counterpart) {
        CounterpartUCNBox widget = new CounterpartUCNBox();
        widget
            .setCaption(getEP().getFieldCaption(GRes.FIELDNAME_COUNTERPART, GRes.R.counterpart()));
        widget.setCounterpartTypes(Arrays.asList(BCounterpart.COUNTERPART_TYPE_TENANT));
        return widget;
      } else if (field == PInvoiceRegDef.invoiceType) {
        RComboBox<String> invoiceTypeBox = new RComboBox<String>(PInvoiceRegDef.invoiceType);
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
      if (PInvoiceRegDef.accountUnit == field
          || EPInvoiceRegReceipt.PInvoiceRegDef_counterpart == field
          || field == PInvoiceRegDef.invoice_code) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PInvoiceRegDef.bizState == field || PInvoiceRegDef.invoiceType == field
          || PInvoiceRegDef.permGroupId == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PInvoiceRegDef.invoice_number == field) {
        result.add(DefaultOperator.CONTAINS);
        return result;
      } else if (PInvoiceRegDef.sourceBill_billNumber == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.STARTS_WITH);
        result.add(DefaultOperator.ENDS_WITH);
        result.add(DefaultOperator.CONTAINS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }
}
