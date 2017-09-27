/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web
 * 文件名：	RebateLineGadget.java
 * 模块说明：	
 * 修改历史：
 * 2016年7月8日 - chenganbang - 创建。
 */
package com.hd123.m3.account.gwt.rebate.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.rebate.client.EPRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BRebateLine;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesBill;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesDetail;
import com.hd123.m3.account.gwt.rebate.client.biz.BSalesPayment;
import com.hd123.m3.account.gwt.rebate.client.ui.gadget.sales.RebateLineFilter;
import com.hd123.m3.account.gwt.rebate.intf.client.dd.PRebateBillDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRenderer;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.date.BDateRange;
import com.hd123.m3.commons.gwt.util.client.style.PanelStyles;
import com.hd123.rumba.commons.gwt.entity.client.HasUCN;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * 返款明细
 * 
 * @author chenganbang
 */
public class RebateLineGadget extends RCaptionBox implements HasRActionHandlers, RActionHandler {

  public static final String ACTION_REFRESH_LINES = "refresh_lines";
  public static final String ACTION_REFRESH_ACTIONS = "refresh_actions";
  public static final String ACTION_READY_TO_LOAD_PRO = "ready_to_load_pro";
  private static final String KEY_PARAM = "_uuid";
  private boolean isReadyToLoadPro = true;

  private ActionHandler actionHandler = new ActionHandler();
  private BRebateBill entity;
  private List<BSalesDetail> values = new ArrayList<BSalesDetail>();

  private EPRebateBill ep = EPRebateBill.getInstance();

  private RGrid grid;
  private RGridColumnDef lineNumberCol;// 行号
  private RGridColumnDef billCodeCol;// 销售单号
  private RGridColumnDef orcTimeCol;// 返款时间
  private RGridColumnDef shouldBackAmountCol;// 应返金额
  private RGridColumnDef amountCol;// 销售额
  private RGridColumnDef poundageAmountCol;// 手续费金额

  private HTML totalCount;
  private RAction addAction;
  private RAction deleteAction;
  private RebateLineBrowserDialog dialog;
  private String billType;

  public RebateLineGadget() {
    super();
    setContentSpacing(0);
    setWidth("100%");
    getCaptionBar().setShowCollapse(true);
    setCaption("返款明细");

    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.addStyleName(PanelStyles.STYLE_WIDTH_FIXED);
    panel.setSpacing(0);

    totalCount = new HTML(CommonsMessages.M.resultTotal(0));
    getCaptionBar().addButton(totalCount);

    panel.add(drawGrid());
    setContent(panel);

    drawTools();
  }

  private Widget drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setHoverRow(true);
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setAllowHorizontalScrollBar(true);

    grid.setProvider(new GridDateProvider());
    grid.addLoadDataHandler(new LoadDataHandler() {

      @Override
      public void onLoadData(LoadDataEvent event) {
        totalCount.setText(CommonsMessages.M.resultTotal(values.size()));
      }
    });

    grid.addRefreshHandler(new RefreshHandler<RGridCellInfo>() {

      @Override
      public void onRefresh(RefreshEvent<RGridCellInfo> event) {
        if (event.getTarget().getWidget() instanceof DispatchLinkRenderer) {
          DispatchLinkRenderer field = (DispatchLinkRenderer) event.getTarget().getWidget();
          if ("SalesInput".equals(billType) || "moonstar.order".equals(billType)) {
            field.setEnabled(true);
          } else {
            field.setEnabled(false);
          }
        }
      }
    });

    lineNumberCol = new RGridColumnDef("行号");
    lineNumberCol.setWidth("50px");
    lineNumberCol.setResizable(true);

    billCodeCol = new RGridColumnDef("销售单号");
    billCodeCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    billCodeCol.setWidth("160px");
    grid.addColumnDef(billCodeCol);

    orcTimeCol = new RGridColumnDef("收款日期");
    orcTimeCol.setWidth("150px");
    grid.addColumnDef(orcTimeCol);

    shouldBackAmountCol = new RGridColumnDef(PRebateBillDef.constants.shouldBackTotal());
    shouldBackAmountCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    shouldBackAmountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    shouldBackAmountCol.addTextStyleName(RTextStyles.STYLE_BLUE);
    shouldBackAmountCol.setWidth("100px");
    grid.addColumnDef(shouldBackAmountCol);

    amountCol = new RGridColumnDef(PRebateBillDef.constants.amount());
    amountCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    amountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    amountCol.setWidth("100px");
    grid.addColumnDef(amountCol);

    poundageAmountCol = new RGridColumnDef(PRebateBillDef.constants.poundageTotal());
    poundageAmountCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    poundageAmountCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    poundageAmountCol.setWidth("100px");
    grid.addColumnDef(poundageAmountCol);

    for (HasUCN payment : ep.getPaymentTypes()) {
      RGridColumnDef col = new RGridColumnDef(payment.getName(), payment.getUuid());
      col.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
      col.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
      col.setWidth("100px");
      grid.addColumnDef(col);
    }

    grid.setAllColumnsResizable(true);
    return grid;
  }

  private void drawTools() {
    addAction = new RAction(RActionFacade.APPEND, actionHandler);
    addAction.setHotKey(null);
    RToolbarButton addButton = new RToolbarButton(addAction);
    addButton.setShowText(false);
    getCaptionBar().addButton(addButton);

    deleteAction = new RAction(RActionFacade.DELETE, actionHandler);
    deleteAction.setHotKey(null);
    RToolbarButton delButton = new RToolbarButton(deleteAction);
    delButton.setShowText(false);
    getCaptionBar().addButton(delButton);
  }

  public void setValue(BRebateBill entity, boolean isViewPage) {
    this.entity = entity;
    refresh();
    setFieldVisible(false);
    if (!isViewPage) {
      refreshActions();
    }
  }

  private void refresh() {
    values.clear();
    if (entity == null || entity.getLines() == null) {
      grid.refresh();
      return;
    }

    Map<String, BSalesDetail> map = new HashMap<String, BSalesDetail>();
    for (BRebateLine line : entity.getLines()) {
      BSalesDetail detail = map.get(line.getSrcBill().getUuid());
      if (detail == null) {
        detail = new BSalesDetail();
        detail.setUuid(line.getSrcBill().getUuid());
        detail.setBillnumber(line.getSrcBill().getCode());
        detail.setBillType(line.getSrcBill().getName());
        detail.setPeriod(new BDateRange(line.getSalesDate(), line.getSalesDate()));
        values.add(detail);
        map.put(detail.getUuid(), detail);
      }

      if (detail.getPeriod().getBeginDate().after(line.getSalesDate())) {
        detail.getPeriod().setBeginDate(line.getSalesDate());
      } else if (detail.getPeriod().getEndDate().before(line.getSalesDate())) {
        detail.getPeriod().setEndDate(line.getSalesDate());
      }

      detail.setAmount(detail.getAmount().add(line.getRebateAmount()));
      detail.setPoundageAmount(detail.getPoundageAmount().add(line.getPoundageAmount()));
      // 界面显示的应返金额等于返款金额减去(或者加上)手续费
      BigDecimal shouldBackAmount = BigDecimal.ZERO;
      if (entity.getContract() != null) {
        int rebateDir = entity.getContract().getRebateDirection();
        int poundageDir = entity.getContract().getPoundageDirection();

        if (rebateDir != 0 && poundageDir != 0 && rebateDir == poundageDir) {
          shouldBackAmount = line.getRebateAmount().add(line.getPoundageAmount());
        } else {
          shouldBackAmount = line.getRebateAmount().subtract(line.getPoundageAmount());
        }
      }
      detail.setShouldBackAmount(detail.getShouldBackAmount().add(shouldBackAmount));

      for (BSalesPayment payment : line.getPayments()) {
        BigDecimal total = detail.getPaymentTotals().get(payment.getPayment().getUuid());
        detail.getPaymentTotals().put(payment.getPayment().getUuid(),
            total == null ? payment.getTotal() : payment.getTotal().add(total));
      }
    }
    grid.refresh();

    RActionEvent.fire(this, RebateInfoGadget.ACTION_REFRESH_TOTAL);
  }

  private void refreshActions() {
    boolean isRebateByBill = ep.isRebateByBill(entity.getStore() == null ? null : entity.getStore()
        .getUuid());
    entity.setSingle(isRebateByBill);
    setFieldVisible(isRebateByBill);
  }

  /**
   * 设置返款明细添加和删除按钮的可见性
   * 
   * @param visible
   */
  private void setFieldVisible(boolean visible) {
    addAction.setVisible(visible);
    deleteAction.setVisible(visible);
    grid.setShowRowSelector(visible);
  }

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == addAction) {
        doAppend();
      } else if (event.getSource() == deleteAction) {
        doDelete();
      }
    }

    private void doAppend() {
      if (entity == null || entity.getContract() == null
          || StringUtil.isNullOrBlank(entity.getContract().getUuid())) {
        RMsgBox.showError(M.M.pleaseSelect(PRebateBillDef.constants.contract()));
        return;
      } else if (entity.getBeginDate() == null || entity.getEndDate() == null || !isReadyToLoadPro) {
        RMsgBox.showError(M.M.pleaseInputDate());
        return;
      }
      // 弹出选择对话框，并添加相应的行
      RebateLineFilter filter = new RebateLineFilter();
      filter.setContractUuid(entity.getContract().getUuid());
      filter.setPosMode(entity.getContract().getPosMode());
      filter.setBeginDate(entity.getBeginDate());
      filter.setEndDate(entity.getEndDate());
      if (dialog == null) {
        dialog = new RebateLineBrowserDialog();
        dialog.addValueChangeHandler(new ValueChangeHandler<List<BSalesBill>>() {

          @Override
          public void onValueChange(ValueChangeEvent<List<BSalesBill>> event) {
            List<BSalesBill> bills = event.getValue();
            // 当前返款明细中已经存在的明细行的单号集合
            Set<String> list = new HashSet<String>();
            for (BRebateLine line : entity.getLines()) {
              list.add(line.getSrcBill().getUuid());
            }
            for (BSalesBill bill : bills) {
              // 根据单号或者收款日期来判断是否添加
              if (list.contains(bill.getUuid()) == false) {
                entity.getLines().addAll(bill.getLines());
              } else {
                for (BRebateLine line : bill.getLines()) {
                  boolean hasAdded = false;
                  for (BRebateLine brl : entity.getLines()) {
                    boolean codeBoolean = brl.getSrcBill().getCode()
                        .equals(line.getSrcBill().getCode());
                    boolean dateBoolean = brl.getSalesDate().getTime() == line.getSalesDate()
                        .getTime();
                    if (codeBoolean && dateBoolean) {
                      hasAdded = true;
                      break;
                    }
                  }
                  if (!hasAdded) {
                    entity.getLines().add(line);
                  }
                }
              }
            }
            refresh();
          }
        });
      }
      dialog.setFilter(filter);
      dialog.center();
    }

    private void doDelete() {
      List<Integer> selections = grid.getSelections();
      if (selections == null || selections.isEmpty()) {
        RMsgBox.show(M.M.selectToDelete());
      }
      List<String> deleteList = new ArrayList<String>();
      for (int i = 0; i < selections.size(); i++) {
        deleteList.add(values.get(selections.get(i)).getUuid());
      }
      Iterator<BRebateLine> iterator = entity.getLines().iterator();
      while (iterator.hasNext()) {
        BRebateLine line = iterator.next();
        if (deleteList.contains(line.getSrcBill().getUuid())) {
          iterator.remove();
        }
      }
      refresh();
    }

  }

  /**
   * 表格数据提供
   */
  private class GridDateProvider implements RGridDataProvider {

    @Override
    public int getRowCount() {
      return values == null ? 0 : values.size();
    }

    @Override
    public Object getData(int row, int col) {
      BSalesDetail value = values.get(row);
      if (col == lineNumberCol.getIndex()) {
        return row + 1;
      } else if (col == billCodeCol.getIndex()) {
        billType = value.getBillType();
        BDispatch dispatch = new BDispatch(billType);
        dispatch.addParams(KEY_PARAM, value.getUuid());
        dispatch.addParams(GRes.R.dispatch_key(), value.getBillnumber());
        return dispatch;
      } else if (col == orcTimeCol.getIndex()) {
        if (value.getPeriod().getBeginDate().compareTo(value.getPeriod().getEndDate()) != 0) {
          return value.getPeriod().format(M3Format.fmt_yMd);
        } else {
          return M3Format.fmt_yMd.format(value.getPeriod().getBeginDate());
        }
      } else if (col == amountCol.getIndex()) {
        ep.log("销售金额为："+value.getAmount());
        return value.getAmount();
      } else if (col == shouldBackAmountCol.getIndex()) {
        ep.log("返款金额金额为："+value.getAmount());
        return value.getShouldBackAmount();
      } else if (col == poundageAmountCol.getIndex()) {
        return value.getPoundageAmount();
      } else {
        BigDecimal val = value.getPaymentTotals().get(grid.getColumnDef(col).getName());
        if (val == null) {
          val = BigDecimal.ZERO;
        }
        return val;
      }
    }

  }

  @Override
  public boolean validate() {
    return values.isEmpty() == false;
  }

  @Override
  public List<Message> getInvalidMessages() {
    List<Message> list = new ArrayList<Message>();
    if (values.isEmpty()) {
      list.add(Message.error(M.M.linesIsNull()));
    }
    return list;
  }

  @Override
  public boolean isValid() {
    return values.isEmpty() == false;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (ACTION_REFRESH_ACTIONS.equals(event.getActionName())) {
      refreshActions();
    } else if (ACTION_REFRESH_LINES.equals(event.getActionName())) {
      refresh();
      refreshActions();
    } else if (ACTION_READY_TO_LOAD_PRO.equals(event.getActionName())) {
      this.isReadyToLoadPro = (Boolean) event.getParameters().get(0);
    }
  }

  public RebateLineBrowserDialog getDialog() {
    return dialog;
  }

  public void setDialog(RebateLineBrowserDialog dialog) {
    this.dialog = dialog;
  }

  public interface M extends Messages {
    public static M M = GWT.create(M.class);

    @DefaultMessage("请选择需要删除的数据！")
    String selectToDelete();

    @DefaultMessage("请先选择{0}")
    String pleaseSelect(String caption);

    @DefaultMessage("返款明细不能为空")
    String linesIsNull();

    @DefaultMessage("请填写正确的日期信息")
    String pleaseInputDate();
  }
}
