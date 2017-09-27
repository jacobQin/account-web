/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceRegDetailGadget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月13日 - chenrizhang - 创建。
 */
package com.hd123.m3.account.gwt.ivc.reg.rec.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.acc.client.BAcc1;
import com.hd123.m3.account.gwt.acc.client.BAccount;
import com.hd123.m3.account.gwt.base.client.GRes;
import com.hd123.m3.account.gwt.commons.client.biz.BBillType;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.ui.counterpart.BCounterpart;
import com.hd123.m3.account.gwt.cpnts.client.ui.invoice.BInvoiceStockState;
import com.hd123.m3.account.gwt.cpnts.client.ui.invoice.InvoiceStockBrowserBox;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.EPInvoiceRegReceipt;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.InvoiceRegMessages;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BInvoiceReg;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.BIvcRegLine;
import com.hd123.m3.account.gwt.ivc.reg.rec.client.biz.InvoiceRegContains;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.InvoiceRegPermDef;
import com.hd123.m3.account.gwt.ivc.reg.rec.intf.client.dd.PInvoiceRegDef;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountNotice;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountPayment;
import com.hd123.m3.account.gwt.settle.client.biz.BAccountStatement;
import com.hd123.m3.account.gwt.subject.intf.client.SubjectUrlParams;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.BDispatch;
import com.hd123.m3.commons.gwt.cpnt.dispatch.intf.client.widget.renderer.DispatchLinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.grid.renderer.HyperlinkRendererFactory;
import com.hd123.m3.commons.gwt.util.client.widget.FocusOnFirstField;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridCellWidgetRenderer;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.fielddef.BigDecimalFieldDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.LoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarMenuButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.webframe.gwt.base.client.RWindow;
import com.hd123.rumba.webframe.gwt.base.client.http.GwtUrl;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 发票领用单|发票明细编辑页面
 * 
 * @author chenrizhang
 * @since 1.7
 * 
 */
public class InvoiceRegDetailEditGadget extends RCaptionBox implements RActionHandler,
    FocusOnFirstField, HasRActionHandlers {
  private EPInvoiceRegReceipt ep = EPInvoiceRegReceipt.getInstance();

  private BInvoiceReg entity;
  private ActionHandler clickHandler = new ActionHandler();
  private boolean checkEmpty;
  private List<Message> messages = new ArrayList<Message>();

  private IvcRegAccountBrowserDialog accountDialog;
  private IvcRegAccPaymentBrowserDialog accPaymentDialog;
  private IvcRegAccStatementBrowserDialog accStatementDialog;
  private IvcRegAccNoticeBrowserDialog accNoticeDialog;

  private HTML totalCount;
  private RAction importFromPaymentAction;
  private RAction importFromStatementAction;
  private RAction importFromAccountAction;
  private RAction importFromNoticeAction;
  private RAction deleteAction;

  private EditGrid<BIvcRegLine> grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef periodCol;
  private RGridColumnDef sourceBillNumberCol;
  private RGridColumnDef sourceBillTypeCol;
  private RGridColumnDef originTotalCol;
  private RGridColumnDef unregTotalCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef invoiceCodeCol;
  private RGridColumnDef invoiceNumberCol;
  private RGridColumnDef remarkCol;

  public InvoiceRegDetailEditGadget() {
    super();
    setCaption(InvoiceRegMessages.M.regDetails());
    getCaptionBar().setShowCollapse(true);
    setEditing(true);
    setWidth("100%");
    drawSelf();
  }

  private void drawSelf() {
    drawGrid();

    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.add(grid);
    setContent(root);

    totalCount = new HTML(InvoiceRegMessages.M.totalCount(0));
    getCaptionBar().addButton(totalCount);

    RPopupMenu appendMenu = new RPopupMenu();

    importFromStatementAction = new RAction("从账单导入", clickHandler);
    appendMenu.addItem(new RMenuItem(importFromStatementAction));

    importFromPaymentAction = new RAction("从收款单导入", clickHandler);
    appendMenu.addItem(new RMenuItem(importFromPaymentAction));

    importFromNoticeAction = new RAction("从收付款通知单导入", clickHandler);
    appendMenu.addItem(new RMenuItem(importFromNoticeAction));

    importFromAccountAction = new RAction("从账款导入", clickHandler);
    appendMenu.addItem(new RMenuItem(importFromAccountAction));

    RToolbarMenuButton appendButton = new RToolbarMenuButton(RActionFacade.APPEND, appendMenu);
    appendButton.setShowText(false);
    getCaptionBar().addButton(appendButton);

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    deleteAction.clearHotKey();
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);

  }

  private void drawGrid() {
    grid = new EditGrid<BIvcRegLine>();
    grid.setWidth("100%");
    grid.setDefaultDataRowCount(0);
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setRequired(true);
    grid.setCaption(InvoiceRegMessages.M.regDetails());
    grid.addClickHandler(new GridClickHandler());
    grid.addLoadDataHandler(new LoadDataHandler() {

      @Override
      public void onLoadData(LoadDataEvent event) {
        totalCount.setHTML(InvoiceRegMessages.M.totalCount(entity == null ? 0 : entity
            .getRegLines().size()));
        RActionEvent.fire(InvoiceRegDetailEditGadget.this,
            InvoiceRegContains.ACTION_REFRESH_SUMMARY);
      }
    });

    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.setDataProducer(provider);

    CellRendererFactory rendererFactory = new CellRendererFactory();
    lineNumberCol = new RGridColumnDef(InvoiceRegMessages.M.lineNo());
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(InvoiceRegMessages.M.regDetail_subject());
    subjectCol.setWidth("140px");
    subjectCol.setRendererFactory(new HyperlinkRendererFactory("120px"));
    grid.addColumnDef(subjectCol);

    periodCol = new RGridColumnDef(InvoiceRegMessages.M.regDetail_period());
    periodCol.setWidth("160px");
    grid.addColumnDef(periodCol);

    sourceBillNumberCol = new RGridColumnDef(InvoiceRegMessages.M.regDetail_sourceBillNumber());
    sourceBillNumberCol.setRendererFactory(new DispatchLinkRendererFactory(GRes.R.dispatch_key()));
    sourceBillNumberCol.setWidth("160px");
    grid.addColumnDef(sourceBillNumberCol);

    sourceBillTypeCol = new RGridColumnDef(InvoiceRegMessages.M.regDetail_sourceBillType());
    sourceBillTypeCol.setWidth("80px");
    grid.addColumnDef(sourceBillTypeCol);

    originTotalCol = new RGridColumnDef(PInvoiceRegDef.originTotal);
    originTotalCol.setWidth("90px");
    originTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    originTotalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    grid.addColumnDef(originTotalCol);

    unregTotalCol = new RGridColumnDef(PInvoiceRegDef.unregTotal);
    unregTotalCol.setWidth("90px");
    unregTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unregTotalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    grid.addColumnDef(unregTotalCol);

    totalCol = new RGridColumnDef(PInvoiceRegDef.total);
    totalCol.setWidth("100px");
    totalCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(totalCol);

    taxCol = new RGridColumnDef(PInvoiceRegDef.taxTotal);
    taxCol.setWidth("90px");
    taxCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    taxCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    grid.addColumnDef(taxCol);

    invoiceNumberCol = new RGridColumnDef(PInvoiceRegDef.invoice_number);
    invoiceNumberCol.setWidth("120px");
    invoiceNumberCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(invoiceNumberCol);

    invoiceCodeCol = new RGridColumnDef(PInvoiceRegDef.invoice_code);
    invoiceCodeCol.setWidth("120px");
    invoiceCodeCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(invoiceCodeCol);

    remarkCol = new RGridColumnDef(PInvoiceRegDef.remark);
    remarkCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(remarkCol);

    if (ep.getEnabledExtInvoiceSystem()) {
      invoiceCodeCol.setVisible(false);
      invoiceNumberCol.setVisible(false);
    }

    grid.setAllColumnsOverflowEllipsis(true);
  }

  public void setValue(BInvoiceReg entity) {
    this.entity = entity;
    unregTotalCol.setVisible(entity.isAllowSplitReg());

    importFromStatementAction.setVisible(ep
        .isPermitted(InvoiceRegPermDef.ACTION_ADD_FROM_STATEMENT));
    importFromPaymentAction.setVisible(ep.isPermitted(InvoiceRegPermDef.ACTION_ADD_FROM_PAYMENT));
    importFromNoticeAction.setVisible(ep.isPermitted(InvoiceRegPermDef.ACTION_ADD_FROM_PAYNOTICE));
    importFromAccountAction.setVisible(ep.isPermitted(InvoiceRegPermDef.ACTION_ADD_FROM_ACCOUNT));

    grid.setValues(entity.getRegLines());

  }

  @Override
  public boolean focusOnFirstField() {
    grid.focusOnFirstField();
    return true;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (InvoiceRegContains.ACTION_REFRESH_DETAILS.equals(event.getActionName())) {
      grid.refresh();
    }
  }

  @Override
  public boolean validate() {
    checkEmpty = true;
    boolean isValid = grid.validate();
    checkEmpty = false;

    List<String> invoices = new ArrayList<String>();// 记录一下顺序
    Map<String, BigDecimal> invoiceTotals = new HashMap<String, BigDecimal>();
    for (BIvcRegLine line : entity.getRegLines()) {
      if (StringUtil.isNullOrBlank(line.getInvoiceNumber()) == false
          && line.getTotal().getTotal() != null) {
        if (invoices.contains(line.getInvoiceNumber())) {
          invoiceTotals.put(line.getInvoiceNumber(),
              line.getTotal().getTotal().add(invoiceTotals.get(line.getInvoiceNumber())));
        } else {
          invoices.add(line.getInvoiceNumber());
          invoiceTotals.put(line.getInvoiceNumber(), line.getTotal().getTotal());
        }
      }
    }
    for (String invoice : invoices) {
      if (invoiceTotals.get(invoice).compareTo(BigDecimal.ZERO) <= 0) {
        messages.add(Message.error(InvoiceRegMessages.M.regInvoiceTotalLessZero(invoice)));
      }
    }
    return isValid && messages.isEmpty();
  }

  @Override
  public void clearValidResults() {
    super.clearValidResults();
    messages.clear();
  }

  @Override
  public boolean isValid() {
    return super.isValid() && messages.isEmpty();
  }

  @Override
  public List getInvalidMessages() {
    List<Message> result = new ArrayList<Message>();
    result.addAll(super.getInvalidMessages());
    result.addAll(messages);
    return result;
  }

  public void clearConditions() {
    if (accStatementDialog != null) {
      accStatementDialog.clearConditions();
    }
    if (accPaymentDialog != null) {
      accPaymentDialog.clearConditions();
    }
    if (accNoticeDialog != null) {
      accNoticeDialog.clearConditions();
    }
    if (accountDialog != null) {
      accountDialog.clearConditions();
    }
  }

  private class ActionHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (importFromPaymentAction == event.getSource()) {
        doImportFromPayments();
      } else if (importFromStatementAction == event.getSource()) {
        doImportFromStatements();
      } else if (importFromNoticeAction == event.getSource()) {
        doImportFromNotices();
      } else if (importFromAccountAction == event.getSource()) {
        doImportFromAccounts();
      } else if (deleteAction == event.getSource()) {
        grid.deleteSelections();
      }
    }

    /**
     * 从收款单导入
     */
    private void doImportFromPayments() {
      if (accPaymentDialog == null) {
        accPaymentDialog = new IvcRegAccPaymentBrowserDialog();
        accPaymentDialog.setAccountUnitControl(true);
        accPaymentDialog.setCounterpartControl(true);
        accPaymentDialog.setCounterpartTypes(Arrays.asList(BCounterpart.COUNTERPART_TYPE_TENANT));
        accPaymentDialog.addValueChangeHandler(new ValueChangeHandler<List<BAccountPayment>>() {

          @Override
          public void onValueChange(ValueChangeEvent<List<BAccountPayment>> event) {
            List<BIvcRegLine> lines = new ArrayList<BIvcRegLine>();
            for (BAccountPayment payment : event.getValue()) {
              for (BAccount account : payment.getAccounts()) {
                lines.add(createRegLine(account));
              }
            }
            grid.appendValues(lines.toArray());
          }
        });
      }
      accPaymentDialog.setDirection(DirectionType.receipt.getDirectionValue());
      accPaymentDialog.setAccountUnitUuid(entity.getAccountUnit() == null ? null : entity
          .getAccountUnit().getUuid());
      accPaymentDialog.setCounterpartUuid(entity.getCounterpart() == null ? null : entity
          .getCounterpart().getUuid());
      accPaymentDialog.setInvoiceRegUuid(entity.getUuid());
      accPaymentDialog.setAccExcepts(entity.getAccIds());
      accPaymentDialog.center();
    }

    /**
     * 从账单导入
     */
    private void doImportFromStatements() {
      if (accStatementDialog == null) {
        accStatementDialog = new IvcRegAccStatementBrowserDialog();
        accStatementDialog.setAccountUnitControl(true);
        accStatementDialog.setCounterpartControl(true);
        accStatementDialog.setCounterpartTypes(Arrays.asList(BCounterpart.COUNTERPART_TYPE_TENANT));
        accStatementDialog.addValueChangeHandler(new ValueChangeHandler<List<BAccountStatement>>() {

          @Override
          public void onValueChange(ValueChangeEvent<List<BAccountStatement>> event) {
            List<BIvcRegLine> lines = new ArrayList<BIvcRegLine>();
            for (BAccountStatement payment : event.getValue()) {
              for (BAccount account : payment.getAccounts()) {
                lines.add(createRegLine(account));
              }
            }
            grid.appendValues(lines.toArray());
          }
        });
      }
      accStatementDialog.setDirection(DirectionType.receipt.getDirectionValue());
      accStatementDialog.setAccountUnitUuid(entity.getAccountUnit() == null ? null : entity
          .getAccountUnit().getUuid());
      accStatementDialog.setCounterpartUuid(entity.getCounterpart() == null ? null : entity
          .getCounterpart().getUuid());
      accStatementDialog.setAccExcepts(entity.getAccIds());
      accStatementDialog.setInvoiceRegUuid(entity.getUuid());
      accStatementDialog.center();
    }

    /**
     * 从收款通知单导入
     */
    private void doImportFromNotices() {
      if (accNoticeDialog == null) {
        accNoticeDialog = new IvcRegAccNoticeBrowserDialog();
        accNoticeDialog.setAccountUnitControl(true);
        accNoticeDialog.setCounterpartControl(true);
        accNoticeDialog.setCounterpartTypes(Arrays.asList(BCounterpart.COUNTERPART_TYPE_TENANT));
        accNoticeDialog.addValueChangeHandler(new ValueChangeHandler<List<BAccountNotice>>() {

          @Override
          public void onValueChange(ValueChangeEvent<List<BAccountNotice>> event) {
            List<BIvcRegLine> lines = new ArrayList<BIvcRegLine>();
            for (BAccountNotice payment : event.getValue()) {
              for (BAccount account : payment.getAccounts()) {
                lines.add(createRegLine(account));
              }
            }
            grid.appendValues(lines.toArray());
          }
        });
      }
      accNoticeDialog.setDirection(DirectionType.receipt.getDirectionValue());
      accNoticeDialog.setAccountUnitUuid(entity.getAccountUnit() == null ? null : entity
          .getAccountUnit().getUuid());
      accNoticeDialog.setCounterpartUuid(entity.getCounterpart() == null ? null : entity
          .getCounterpart().getUuid());
      accNoticeDialog.setAccExcepts(entity.getAccIds());
      accNoticeDialog.setInvoiceRegUuid(entity.getUuid());
      accNoticeDialog.center();
    }

    /**
     * 从账款导入
     */
    private void doImportFromAccounts() {
      if (accountDialog == null) {
        accountDialog = new IvcRegAccountBrowserDialog();
        accountDialog.setAccountUnitControl(true);
        accountDialog.setCounterpartControl(true);
        accountDialog.setCounterpartTypes(Arrays.asList(BCounterpart.COUNTERPART_TYPE_TENANT));
        accountDialog.addValueChangeHandler(new ValueChangeHandler<List<BAccount>>() {

          @Override
          public void onValueChange(ValueChangeEvent<List<BAccount>> event) {
            List<BIvcRegLine> lines = new ArrayList<BIvcRegLine>();
            for (BAccount account : event.getValue()) {
              lines.add(createRegLine(account));
            }
            grid.appendValues(lines.toArray());
          }
        });
      }
      accountDialog.setDirection(DirectionType.receipt.getDirectionValue());
      accountDialog.setAccountUnitUuid(entity.getAccountUnit() == null ? null : entity
          .getAccountUnit().getUuid());
      accountDialog.setCounterpartUuid(entity.getCounterpart() == null ? null : entity
          .getCounterpart().getUuid());
      accountDialog.setAccExcepts(entity.getAccIds());
      accountDialog.setInvoiceRegUuid(entity.getUuid());
      accountDialog.center();
    }
  }

  private BIvcRegLine createRegLine(BAccount account) {
    BIvcRegLine line = new BIvcRegLine();
    line.setInvoiceType(entity.getInvoiceType());
    line.setInvoiceCode(entity.getInvoiceCode());
    line.setInvoiceNumber(entity.getInvoiceNumber());
    line.setAcc1(account.getAcc1());
    line.setAcc2(account.getAcc2());
    line.setOriginTotal(account.getOriginTotal() == null ? BTotal.zero() : account.getOriginTotal()
        .clone());
    line.setUnregTotal(account.getTotal() == null ? BTotal.zero() : account.getTotal().clone());
    line.setTotal(account.getTotal() == null ? BTotal.zero() : account.getTotal().clone());
    line.getTotal().setTax(
        BTaxCalculator.tax(line.getTotal().getTotal(), line.getAcc1().getTaxRate(), ep.getScale(),
            ep.getRoundingMode()));
    line.setTotalDiff(BigDecimal.ZERO);
    line.setTaxDiff(BigDecimal.ZERO);
    return line;
  }

  private class GridClickHandler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      if (cell.getColumnDef().equals(subjectCol)) {
        BIvcRegLine line = entity.getRegLines().get(grid.getCurrentRow());
        if (line == null)
          return;
        if (line.getAcc1().getSubject() == null
            || StringUtil.isNullOrBlank(line.getAcc1().getSubject().getUuid()))
          return;
        GwtUrl url = SubjectUrlParams.ENTRY_URL;
        url.getQuery().set(JumpParameters.PN_START, SubjectUrlParams.View.START_NODE);
        url.getQuery().set(SubjectUrlParams.View.PN_ENTITY_UUID,
            line.getAcc1().getSubject().getUuid());
        RWindow.navigate(url.getUrl(), RWindow.WINDOWNAME_NEW);
      }
    }

  }

  private class GridDataProvider implements RGridDataProvider, EditGridDataProduce<BIvcRegLine> {

    @Override
    public BIvcRegLine create() {
      BIvcRegLine value = new BIvcRegLine();
      value.setInvoiceType(entity.getInvoiceType());
      value.setInvoiceCode(entity.getInvoiceCode());
      value.setInvoiceNumber(entity.getInvoiceNumber());
      value.setTotalDiff(BigDecimal.ZERO);
      value.setTaxDiff(BigDecimal.ZERO);
      return value;
    }

    @Override
    public int getRowCount() {
      return entity == null || entity.getRegLines() == null ? 0 : entity.getRegLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      BIvcRegLine rowData = entity.getRegLines().get(row);
      if (col == lineNumberCol.getIndex()) {
        return row + 1;
      } else if (col == originTotalCol.getIndex()) {
        if (entity.isAllowSplitReg()) {
          return rowData.getOriginTotal().getTotal();
        } else {
          return rowData.getUnregTotal().getTotal();
        }
      } else if (col == unregTotalCol.getIndex()) {
        return rowData.getUnregTotal().getTotal();
      } else if (col == totalCol.getIndex()) {
        return rowData.getTotal().getTotal();
      } else if (col == taxCol.getIndex()) {
        return rowData.getTotal().getTax();
      } else if (col == invoiceCodeCol.getIndex()) {
        return rowData.getInvoiceCode();
      } else if (col == invoiceNumberCol.getIndex()) {
        return rowData.getInvoiceNumber();
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      }
      if (rowData.getAcc1() != null) {
        BAcc1 acc = rowData.getAcc1();
        if (col == subjectCol.getIndex()) {
          return acc.getSubject().toFriendlyStr();
        } else if (col == periodCol.getIndex()) {
          if (acc.getBeginTime() == null && acc.getEndTime() == null)
            return "";
          return InvoiceRegMessages.M.period(
              acc.getBeginTime() == null ? "" : M3Format.fmt_yMd.format(acc.getBeginTime()),
              acc.getEndTime() == null ? "" : M3Format.fmt_yMd.format(acc.getEndTime()));
        } else if (col == sourceBillNumberCol.getIndex()) {
          if (rowData.getAcc1().getSourceBill() == null) {
            return null;
          }
          BDispatch dispatch = new BDispatch(rowData.getAcc1().getSourceBill().getBillType());
          dispatch.addParams(GRes.R.dispatch_key(), rowData.getAcc1().getSourceBill()
              .getBillNumber());
          return dispatch;
        } else if (col == sourceBillTypeCol.getIndex()) {
          BBillType billType = ep.getBillTypes().get(acc.getSourceBill().getBillType());
          return billType == null ? null : billType.getCaption();
        }
      }
      return null;
    }
  }

  private class CellRendererFactory implements RCellRendererFactory {

    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        Object data) {
      String fieldName = colDef.getName();
      if (fieldName.equals(totalCol.getName())) {
        return new TotalRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(invoiceCodeCol.getName())) {
        return new InvoiceCodeRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(invoiceNumberCol.getName())) {
        return new InvoiceNumberRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(remarkCol.getName())) {
        return new RemarkTextRenderer(grid, colDef, row, col, data);
      }
      return null;
    }
  }

  private class TotalRenderer extends EditGridCellWidgetRenderer {
    private BigDecimalFieldDef fieldDef;
    private RNumberBox field;

    public TotalRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      fieldDef = (BigDecimalFieldDef) PInvoiceRegDef.total.clone();

      field = new RNumberBox<Number>(fieldDef);
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.setFormat(GWTFormat.fmt_money);
      field.setSelectAllOnFocus(true);
      field.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
      field.addKeyDownHandler(new DefaultFocusNextHandler(false));
      field.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          BIvcRegLine value = entity.getRegLines().get(getRow());
          value.getTotal().setTotal(field.getValueAsBigDecimal());
          value.getTotal().setTax(
              BTaxCalculator.tax(value.getTotal().getTotal(), value.getAcc1().getTaxRate(),
                  ep.getScale(), ep.getRoundingMode()));
          value.setTotalDiff(value.getUnregTotal().subtract(value.getTotal()).getTotal());
          value.setTaxDiff(value.getUnregTotal().subtract(value.getTotal()).getTax());
          grid.refresh();
        }
      });

      field.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String text) {
          StringBuffer sb = new StringBuffer();
          if (fieldDef.validateText(text, sb, checkEmpty) == false) {
            if (StringUtil.isNullOrBlank(sb.toString()) == false) {
              return Message.error(sb.toString(), field);
            }
          }
          return null;
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BIvcRegLine detail = entity.getRegLines().get(getRow());
      if (detail.getUnregTotal().getTotal().compareTo(BigDecimal.ZERO) < 0) {
        fieldDef.setMinValue(BoundaryValue.BIGDECIMAL_MINVALUE_S2);
        fieldDef.setMaxValue(BigDecimal.ZERO);
      } else {
        fieldDef.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2);
        fieldDef.setMinValue(BigDecimal.ZERO);
      }
      if (ObjectUtil.equals(field.getValueAsBigDecimal(), detail.getTotal().getTotal()) == false) {
        field.setValue(detail.getTotal().getTotal());
      } else {
        field.validate();
      }
    }
  }

  private class InvoiceNumberRenderer extends EditGridCellWidgetRenderer {
    private InvoiceStockBrowserBox field;

    public InvoiceNumberRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new InvoiceStockBrowserBox();
      field.setFieldDef(PInvoiceRegDef.invoice_number);
      field.setWidth("100%");
      field.setAccountUnitControl(true);
      field.setStates(BInvoiceStockState.received.name());
      field.addKeyDownHandler(new DefaultFocusNextHandler(false));
      field.addValueChangeHandler(new ValueChangeHandler<String>() {

        @Override
        public void onValueChange(ValueChangeEvent<String> event) {
          BIvcRegLine value = entity.getRegLines().get(getRow());
          if (field.getRawValue() != null) {
            value.setInvoiceNumber(field.getRawValue().getInvoiceNumber());
            value.setInvoiceCode(field.getRawValue().getInvoiceCode());
          } else {
            value.setInvoiceNumber(field.getValue());
            if (entity.isUseInvoiceStock()) {
              value.setInvoiceCode(null);
            }
          }
          grid.refresh();
        }
      });
      field.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String text) {
          StringBuffer sb = new StringBuffer();
          if (PInvoiceRegDef.invoice_number.validateText(text, sb, checkEmpty) == false) {
            if (StringUtil.isNullOrBlank(sb.toString()) == false) {
              return Message.error(sb.toString(), field);
            }
          }
          return null;
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      field.setStockControl(entity.isUseInvoiceStock());
      field.setInvoiceTypes(entity.getInvoiceType());
      field.setAccountUnit(entity.getStore() == null ? null : entity.getStore().getUuid());
      BIvcRegLine detail = entity.getRegLines().get(getRow());
      if (ObjectUtil.equals(field.getValue(), detail.getInvoiceNumber()) == false) {
        field.setValue(detail.getInvoiceNumber());
      }
    }
  }

  private class InvoiceCodeRenderer extends EditGridCellWidgetRenderer {
    private RTextBox field;

    public InvoiceCodeRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RTextBox(PInvoiceRegDef.invoice_code);
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.setReadOnly(entity.isUseInvoiceStock());
      field.addKeyDownHandler(new DefaultFocusNextHandler(false));
      field.addChangeHandler(new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          BIvcRegLine value = entity.getRegLines().get(getRow());
          value.setInvoiceCode(field.getValue());
        }
      });
      field.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String text) {
          StringBuffer sb = new StringBuffer();
          if (PInvoiceRegDef.invoice_code.validateText(text, sb, checkEmpty) == false) {
            if (StringUtil.isNullOrBlank(sb.toString()) == false) {
              return Message.error(sb.toString(), field);
            }
          }
          return null;
        }
      });

      return field;
    }

    @Override
    public void setValue(Object value) {
      BIvcRegLine detail = entity.getRegLines().get(getRow());
      if (ObjectUtil.equals(field.getValue(), detail.getInvoiceCode()) == false) {
        field.setValue(detail.getInvoiceCode());
      }
    }
  }

  private class RemarkTextRenderer extends EditGridCellWidgetRenderer {
    private RTextBox field;

    public RemarkTextRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RTextBox(colDef.getFieldDef());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler(false));
      field.addChangeHandler(new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          BIvcRegLine value = entity.getRegLines().get(getRow());
          value.setRemark(field.getValue());
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BIvcRegLine detail = entity.getRegLines().get(getRow());
      if (ObjectUtil.equals(field.getValue(), detail.getRemark()) == false) {
        field.setValue(detail.getRemark());
      }
    }
  }
}
