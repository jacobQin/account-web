/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceInstockSearchPage.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月1日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.instock.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.AccountUnitUCNBox;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.EmployeeUCNBox;
import com.hd123.m3.account.gwt.ivc.instock.client.EPInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.client.biz.BInvoiceInstock;
import com.hd123.m3.account.gwt.ivc.instock.intf.client.InvoiceInstockUrlParams.Search;
import com.hd123.m3.account.gwt.ivc.instock.intf.client.dd.PInvoiceInstockDef;
import com.hd123.m3.commons.gwt.base.client.biz.BBizStates;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizAction;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizFlow;
import com.hd123.m3.commons.gwt.base.client.biz.flow.BBizFlowUtils;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.log.EntityLogger;
import com.hd123.m3.commons.gwt.bpm.client.common.FMenuItem;
import com.hd123.m3.commons.gwt.bpm.client.ui2.BaseBpmSearchPage2;
import com.hd123.m3.commons.gwt.util.client.grid.batch.PagingGridBatchProcessCallback;
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
import com.hd123.rumba.gwt.util.client.batchprocess.BatchProcesser;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.form.RComboBox;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RBooleanRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RDateTimeRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHotItemRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbar.Alignment;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票入库单搜索页面
 * 
 * @author lixiaohong
 * @since 1.0
 * 
 */
public class InvoiceInstockSearchPage extends BaseBpmSearchPage2<BInvoiceInstock> implements Search {
  private static InvoiceInstockSearchPage instance;
  private List<FMenuItem> oneMenus;

  private RGridColumnDef billNumberCol;
  private RGridColumnDef bizStateCol;
  private RGridColumnDef invoiceTypeCol;
  private RGridColumnDef accountUnitCol;
  private RGridColumnDef isReceiveCol;
  private RGridColumnDef instockorCol;
  private RGridColumnDef instockDateCol;
  private RGridColumnDef remarkCol;

  @Override
  protected EPInvoiceInstock getEP() {
    return EPInvoiceInstock.getInstance();
  }

  public static InvoiceInstockSearchPage getInstance() {
    if (instance == null) {
      instance = new InvoiceInstockSearchPage();
    }
    return instance;
  }

  @Override
  protected List<RGridColumnDef> createGridColumns() {
    List<RGridColumnDef> columnDefs = new ArrayList<RGridColumnDef>();

    billNumberCol = new RGridColumnDef(PInvoiceInstockDef.billNumber);
    billNumberCol.setWidth("180px");
    billNumberCol.setRendererFactory(new RHotItemRendererFactory(lineMenu,
        new HyperlinkRendererFactory("140px")));
    billNumberCol.setSortable(true);
    billNumberCol.setName(PN_BILLNUMBER);
    billNumberCol.setAllowHiding(false);
    columnDefs.add(billNumberCol);

    bizStateCol = new RGridColumnDef(PInvoiceInstockDef.bizState);
    bizStateCol.setWidth("100px");
    bizStateCol.setSortable(true);
    bizStateCol.setName(FIELD_BIZSTATE);
    columnDefs.add(bizStateCol);

    invoiceTypeCol = new RGridColumnDef(PInvoiceInstockDef.invoiceType);
    invoiceTypeCol.setWidth("100px");
    invoiceTypeCol.setSortable(true);
    invoiceTypeCol.setName(FIELD_INVOICE_TYPE);
    columnDefs.add(invoiceTypeCol);

    accountUnitCol = new RGridColumnDef(PInvoiceInstockDef.accountUnit);
    accountUnitCol.setWidth("120px");
    accountUnitCol.setSortable(true);
    accountUnitCol.setName(FIELD_ACCOUNT_UNIT);
    columnDefs.add(accountUnitCol);

    isReceiveCol = new RGridColumnDef(PInvoiceInstockDef.isReceive);
    isReceiveCol.setRendererFactory(new RBooleanRendererFactory());
    isReceiveCol.setWidth("150px");
    isReceiveCol.setSortable(true);
    isReceiveCol.setName(FIELD_ISRECEIVE);
    columnDefs.add(isReceiveCol);

    instockorCol = new RGridColumnDef(PInvoiceInstockDef.instockor);
    instockorCol.setWidth("150px");
    instockorCol.setSortable(true);
    instockorCol.setName(FIELD_INSTOCKOR);
    columnDefs.add(instockorCol);

    instockDateCol = new RGridColumnDef(PInvoiceInstockDef.instockDate);
    instockDateCol.setRendererFactory(new RDateTimeRendererFactory(M3Format.fmt_yMd));
    instockDateCol.setWidth("120px");
    instockDateCol.setSortable(true);
    instockDateCol.setName(FIELD_INSTOCKDATE);
    columnDefs.add(instockDateCol);

    remarkCol = new RGridColumnDef(PInvoiceInstockDef.remark);
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

    flecsPanel.addField(PInvoiceInstockDef.billNumber);
    flecsPanel.addField(PInvoiceInstockDef.bizState);
    flecsPanel.addField(PInvoiceInstockDef.accountUnit);
    flecsPanel.addField(PInvoiceInstockDef.instockor);
    flecsPanel.addField(PInvoiceInstockDef.invoice_code);
    flecsPanel.addField(PInvoiceInstockDef.invoice_number);
    flecsPanel.addField(PInvoiceInstockDef.instockDate);
    flecsPanel.addField(PInvoiceInstockDef.invoiceType);

    if (getEP().isPermEnabled()) {
      flecsPanel.addField(PInvoiceInstockDef.permGroupId);
    }
    flecsPanel.addField(PInvoiceInstockDef.createInfo_operator_id);
    flecsPanel.addField(PInvoiceInstockDef.createInfo_operator_fullName);
    flecsPanel.addField(PInvoiceInstockDef.createInfo_time);
    flecsPanel.addField(PInvoiceInstockDef.lastModifyInfo_operator_id);
    flecsPanel.addField(PInvoiceInstockDef.lastModifyInfo_operator_fullName);
    flecsPanel.addField(PInvoiceInstockDef.lastModifyInfo_time);

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
      conditions.add(new Condition(PInvoiceInstockDef.billNumber, DefaultOperator.STARTS_WITH,
          getSearchKeyWork(params)));
      return conditions;
    }

    conditions.add(new Condition(PInvoiceInstockDef.billNumber, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceInstockDef.bizState, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceInstockDef.accountUnit, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceInstockDef.instockor, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceInstockDef.invoice_code, DefaultOperator.EQUALS, null));
    conditions
        .add(new Condition(PInvoiceInstockDef.invoice_number, DefaultOperator.CONTAINS, null));
    conditions.add(new Condition(PInvoiceInstockDef.instockDate, DefaultOperator.EQUALS, null));
    conditions.add(new Condition(PInvoiceInstockDef.invoiceType, DefaultOperator.EQUALS, null));
    return conditions;
  }

  @Override
  protected Object getGridData(int row, int col, BInvoiceInstock rowData,
      List<BInvoiceInstock> pageData) {
    if (col == billNumberCol.getIndex()) {
      return rowData.getBillNumber();
    } else if (col == bizStateCol.getIndex()) {
      return BBizStates.getCaption(rowData.getBizState());
    } else if (col == invoiceTypeCol.getIndex()) {
      return rowData.getInvoiceType() == null ? null : getEP().getInvoiceTypes().get(
          rowData.getInvoiceType());
    } else if (col == accountUnitCol.getIndex()) {
      return BUCN.toFriendlyStr(rowData.getAccountUnit(), null);
    } else if (col == isReceiveCol.getIndex()) {
      return rowData.isReceive();
    } else if (col == instockorCol.getIndex()) {
      return rowData.getInstockor() == null ? null : rowData.getInstockor().toFriendlyStr();
    } else if (col == instockDateCol.getIndex()) {
      return rowData.getInstockDate();
    } else if (col == remarkCol.getIndex()) {
      return rowData.getRemark();
    }
    return null;
  }

  @Override
  protected BInvoiceInstock[] newEntityArray() {
    return new BInvoiceInstock[] {};
  }

  @Override
  protected void drawLineMenu() {
    oneMenus = new ArrayList<FMenuItem>();
    BBizFlow flow = getEP().getBizFlow();
    for (BBizAction act : flow.getActions()) {
      FMenuItem item = createMenuItem(act, false);
      oneMenus.add(item);
    }
  }

  @Override
  protected void appendLineMenu(RPopupMenu menu, Object bill) {
    BInvoiceInstock entity = (BInvoiceInstock) bill;
    lineMenu.addSeparator();

    for (FMenuItem item : oneMenus) {
      BBizAction action = item.getBizAction();
      if (isPermitted(action, entity)) {
        item.setEnabled(true);
        if (action.getSrcStates().indexOf(entity.getBizState()) >= 0) {
          lineMenu.addItem(item);
        }
      }
    }
  }

  @Override
  protected void doBatchChangeBizState(final String state, final String actionName) {
    batchProcessor.batchProcess(actionName, newEntityArray(),
        new PagingGridBatchProcessCallback<BInvoiceInstock>() {

          @Override
          public void execute(BInvoiceInstock entity, BatchProcesser<BInvoiceInstock> processer,
              AsyncCallback callback) {
            if (BBizFlowUtils.needChangeState(getEP().getBizFlow(), entity.getBizState(), state) == false) {
              processer.getReport().reportSkipped();
              processer.next();
              return;
            }
            getEP().getModuleService().changeBizState(entity.getUuid(), entity.getVersion(), state,
                callback);
          }

          @Override
          public void onSuccess(BInvoiceInstock entity, Object result) {
            EntityLogger.getInstance().log(actionName, entity);
            super.onSuccess(entity, result);
          }
        });
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
      if (field == PInvoiceInstockDef.accountUnit) {
        return DefaultOperator.EQUALS == operator ? new AccountUnitUCNBox(null, true) : null;
      } else if (field == PInvoiceInstockDef.permGroupId) {
        return new PermGroupField(field);
      } else if (field == PInvoiceInstockDef.instockor) {
        return DefaultOperator.EQUALS == operator ? new EmployeeUCNBox() : null;
      } else if (field == PInvoiceInstockDef.invoiceType) {
        RComboBox<String> invoiceTypeBox = new RComboBox<String>(PInvoiceInstockDef.invoiceType);
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
      if (PInvoiceInstockDef.accountUnit == field || PInvoiceInstockDef.instockor == field
          || field == PInvoiceInstockDef.invoice_code) {
        result.add(DefaultOperator.EQUALS);
        return result;
      } else if (PInvoiceInstockDef.bizState == field || PInvoiceInstockDef.invoiceType == field
          || PInvoiceInstockDef.permGroupId == field) {
        result.add(DefaultOperator.EQUALS);
        result.add(DefaultOperator.NOT_EQUALS);
        return result;
      } else if (PInvoiceInstockDef.invoice_number == field) {
        result.add(DefaultOperator.CONTAINS);
        return result;
      }
      defaultOperators.remove(DefaultOperator.IS_NOT_NULL);
      defaultOperators.remove(DefaultOperator.IS_NULL);
      return defaultOperators;
    }
  }

}
