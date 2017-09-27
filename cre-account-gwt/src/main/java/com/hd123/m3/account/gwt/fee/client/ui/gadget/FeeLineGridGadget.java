/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： FeeLineEditGadget.java
 * 模块说明：    
 * 修改历史：
 * 2015-9-23 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.fee.client.ui.gadget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BUsageType;
import com.hd123.m3.account.gwt.cpnts.client.ui.dialog.SubjectBrowserDialog;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.fee.client.EPFee;
import com.hd123.m3.account.gwt.fee.client.FeeMessages;
import com.hd123.m3.account.gwt.fee.client.biz.ActionName;
import com.hd123.m3.account.gwt.fee.client.biz.BFee;
import com.hd123.m3.account.gwt.fee.client.biz.BFeeLine;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeDef;
import com.hd123.m3.account.gwt.fee.intf.client.dd.PFeeLineDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.message.CommonsMessages;
import com.hd123.m3.commons.gwt.widget.client.ui.TaxRateComboBox;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridCellWidgetRenderer;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.commons.gwt.mini.client.ObjectUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataEvent;
import com.hd123.rumba.gwt.widget2.client.event.BeforeLoadDataHandler;
import com.hd123.rumba.gwt.widget2.client.event.RefreshEvent;
import com.hd123.rumba.gwt.widget2.client.event.RefreshHandler;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RDateBox;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBoxBase;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RNumberRendererFactory;
import com.hd123.rumba.gwt.widget2.client.hotkey.RHotKey;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSeparator;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSplitButton;
import com.hd123.rumba.gwt.widget2e.client.event.HasRActionHandlers;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;
import com.hd123.rumba.gwt.widget2e.client.form.RUCNBox;
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author lixiaohong
 * 
 */
public class FeeLineGridGadget extends RCaptionBox implements HasRActionHandlers, RActionHandler {
  private EPFee ep = EPFee.getInstance();

  public static final Double MAX_TOTAL = new BigDecimal(9999999999.99)
      .setScale(2, RoundingMode.HALF_UP).doubleValue();
  public static final Double MIN_TOTAL = new BigDecimal(-9999999999.99)
      .setScale(2, RoundingMode.HALF_UP).doubleValue();

  private HTML resultTotalHtml;
  private RPopupMenu addMenu;
  private RAction addAction;
  private RAction insertAction;
  private RAction batchAddAction;
  private RToolbarSplitButton addButton;
  private RAction deleteAction;
  private SubjectBrowserDialog dialog;

  private EditGrid<BFeeLine> editGrid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef beginDateCol;
  private RGridColumnDef endDateCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef taxRateCol;
  private RGridColumnDef taxCol;
  private RGridColumnDef issueInvoiceCol;
  private RGridColumnDef remarkCol;

  private boolean checkEmpty;

  private int direction;
  private List<BFeeLine> values = new ArrayList<BFeeLine>();
  private BFee bill;
  private Handler_Click clickHandler = new Handler_Click();

  public FeeLineGridGadget(int direction) {
    super();
    this.direction = direction;
    setContentSpacing(0);
    setWidth("100%");
    setCaption(PFeeLineDef.constants.tableCaption());
    setEditing(true);
    getCaptionBar().setShowCollapse(true);

    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    Widget w = drawGrid();
    panel.add(w);

    resultTotalHtml = new HTML(CommonsMessages.M.resultTotal(0));
    getCaptionBar().addButton(resultTotalHtml);
    addMenu = new RPopupMenu();

    batchAddAction = new RAction(FeeMessages.M.batchAdd(), clickHandler);
    batchAddAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(batchAddAction));

    addAction = new RAction(RActionFacade.APPEND, clickHandler);
    addAction.setCaption(FeeMessages.M.addLine());
    addAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(addAction));

    insertAction = new RAction(FeeMessages.M.insertAboveLine(), clickHandler);
    insertAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(insertAction));

    addButton = new RToolbarSplitButton(addAction);
    addButton.setMenu(addMenu);
    addButton.setShowText(false);
    getCaptionBar().addButton(addButton);

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteAction.setHotKey(null);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);

    getCaptionBar().addButton(new RToolbarSeparator());

    setContent(panel);
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName() == ActionName.CHANGE_BEGIN_DATE) {
      for (BFeeLine line : bill.getLines()) {
        line.getDateRange().setBeginDate(bill.getDateRange().getBeginDate());
        Widget widget = editGrid.getWidget(bill.getLines().indexOf(line), 4);
        Widget endDateWidget = editGrid.getWidget(bill.getLines().indexOf(line), 5);
        if ((widget instanceof BeginDateRenderer) && (endDateWidget instanceof EndDateRenderer)) {
          ((BeginDateRenderer) widget).field.setValue(bill.getDateRange().getBeginDate());
          ((EndDateRenderer) endDateWidget).field.validate();
        }
      }
    }
    if (event.getActionName() == ActionName.CHANGE_END_DATE) {
      for (BFeeLine line : bill.getLines()) {
        line.getDateRange().setEndDate(bill.getDateRange().getEndDate());
        Widget widget = editGrid.getWidget(bill.getLines().indexOf(line), 5);
        if (widget instanceof EndDateRenderer) {
          ((EndDateRenderer) widget).field.setValue(bill.getDateRange().getEndDate());
        }
      }
    }
    if (event.getActionName() == ActionName.CHANGE_STORE) {
      for (BFeeLine value : bill.getLines()) {
        value.setTaxRate(value.getbSubject() == null ? value.getTaxRate()
            : (value.getbSubject()
                .getTaxRate(bill.getStore() == null ? null : bill.getStore().getUuid())));
        value.changeTotal(ep.getScale(), ep.getRoundingMode());
      }
      editGrid.refresh();
    }
  }

  public void setValue(BFee bill, int direction) {
    this.bill = bill;
    this.values = bill.getLines();
    this.direction = direction;

    editGrid.setValues(values);
  }

  public void clear() {
    values.clear();
    if (dialog != null) {
      dialog.clearConditions();
    }
    editGrid.refresh();
  }

  @Override
  public List<Message> getInvalidMessages() {
    return editGrid.getInvalidMessages();
  }

  @Override
  public boolean validate() {
    checkEmpty = true;
    boolean valid = editGrid.validate();
    checkEmpty = false;
    return valid;
  }

  private Widget drawGrid() {
    editGrid = new EditGrid<BFeeLine>();
    editGrid.setWidth("100%");
    editGrid.setRequired(true);
    editGrid.setCaption("账款明细 ");
    editGrid.setShowRowSelector(true);
    editGrid.setShowCurrentRow(true);
    editGrid.setHoverRow(true);
    editGrid.setDefaultDataRowCount(1);

    GridDataProvider provider = new GridDataProvider();
    editGrid.setProvider(provider);
    editGrid.setDataProducer(provider);
    editGrid.addRefreshHandler(new RefreshHandler<RGridCellInfo>() {
      @Override
      public void onRefresh(RefreshEvent<RGridCellInfo> event) {
        doRefresh();
      }
    });

    CellRendererFactory rendererFactory = new CellRendererFactory();

    lineNumberCol = new RGridColumnDef(PFeeLineDef.constants.lineNumber());
    lineNumberCol.setWidth("20px");
    editGrid.addColumnDef(lineNumberCol);

    subjectCodeCol = new RGridColumnDef(PFeeLineDef.subject_code);
    subjectCodeCol.setWidth("150px");
    subjectCodeCol.setRendererFactory(rendererFactory);
    editGrid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PFeeLineDef.subject_name);
    subjectNameCol.setWidth("150px");
    editGrid.addColumnDef(subjectNameCol);

    beginDateCol = new RGridColumnDef(PFeeDef.dateRange_beginDate);
    beginDateCol.setWidth("100px");
    beginDateCol.setRendererFactory(rendererFactory);
    editGrid.addColumnDef(beginDateCol);

    endDateCol = new RGridColumnDef(PFeeDef.dateRange_endDate);
    endDateCol.setWidth("100px");
    endDateCol.setRendererFactory(rendererFactory);
    editGrid.addColumnDef(endDateCol);

    totalCol = new RGridColumnDef(PFeeLineDef.total_total);
    totalCol.setWidth("80px");
    totalCol.setRendererFactory(rendererFactory);
    editGrid.addColumnDef(totalCol);

    taxRateCol = new RGridColumnDef(PFeeLineDef.taxRate);
    taxRateCol.setWidth("80px");
    taxRateCol.setRendererFactory(rendererFactory);
    editGrid.addColumnDef(taxRateCol);

    taxCol = new RGridColumnDef(PFeeLineDef.total_tax);
    taxCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    taxCol.setHorizontalAlign(HorizontalPanel.ALIGN_RIGHT);
    taxCol.setWidth("80px");
    editGrid.addColumnDef(taxCol);

    issueInvoiceCol = new RGridColumnDef(PFeeLineDef.issueInvoice);
    issueInvoiceCol.setWidth("80px");
    issueInvoiceCol.setRendererFactory(rendererFactory);
    editGrid.addColumnDef(issueInvoiceCol);

    remarkCol = new RGridColumnDef(PFeeLineDef.remark);
    remarkCol.setWidth("100px");
    remarkCol.setRendererFactory(rendererFactory);
    editGrid.addColumnDef(remarkCol);

    editGrid.setAllColumnsOverflowEllipsis(true);
    return editGrid;
  }

  private class CellRendererFactory implements RCellRendererFactory {

    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        final Object data) {

      BFeeLine value = values.get(row);

      if (PFeeLineDef.subject_code.getName().equals(colDef.getName())) {
        return new SubjectRenderer(editGrid, colDef, row, col, value);
      } else if (PFeeDef.dateRange_beginDate.getName().equals(colDef.getName())) {
        return new BeginDateRenderer(editGrid, colDef, row, col, value);
      } else if (PFeeDef.dateRange_endDate.getName().equals(colDef.getName())) {
        return new EndDateRenderer(editGrid, colDef, row, col, value);
      } else if (PFeeLineDef.total_total.getName().equals(colDef.getName())) {
        return new TotalRenderer(editGrid, colDef, row, col, value);
      } else if (PFeeLineDef.taxRate.getName().equals(colDef.getName())) {
        return new TaxRateRenderer(editGrid, colDef, row, col, value);
      } else if (PFeeLineDef.issueInvoice.getName().equals(colDef.getName())) {
        return new InvoiceRenderer(editGrid, colDef, row, col, value);
      } else if (PFeeLineDef.remark.getName().equals(colDef.getName())) {
        return new RemarkRenderer(editGrid, colDef, row, col, value);
      }
      return null;
    }
  }

  private class SubjectRenderer extends EditGridCellWidgetRenderer<RUCNBox> {

    public SubjectRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private RUCNBox field;

    @Override
    protected RUCNBox drawField(RGridColumnDef colDef, Object data) {
      field = new SubjectUCNBox(BSubjectType.credit.name(), direction, Boolean.TRUE,
          BUsageType.tempFee.name());
      field.setCodeBoxWidth(1f);
      field.getNameBox().setVisible(false);
      field.setRequired(false);
      field.setEnterToTab(false);
      SubjectHandler handler = new SubjectHandler();
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addBeforeLoadDataHandler(handler);
      field.setValidator(handler);
      field.addChangeHandler(handler);
      return field;
    }

    @Override
    public void setValue(Object value) {
      BFeeLine line = values.get(getRow());
      field.setRawValue(line.getSubject());
      field.setValue(line.getSubject(), true);
    }

    private class SubjectHandler
        implements ChangeHandler, RValidator, BeforeLoadDataHandler<String> {
      @Override
      public void onBeforeLoadData(BeforeLoadDataEvent<String> event) {
        if (bill.getContract() == null || StringUtil.isNullOrBlank(bill.getContract().getUuid())) {
          RMsgBox.showError(FeeMessages.M.pleaseSelect(PFeeDef.constants.contract()));
          field.clearValidResults();
          event.cancel();
          return;
        }
      }

      @Override
      public void onChange(ChangeEvent event) {
        BFeeLine value = values.get(getRow());
        BSubject subject = (BSubject) field.getRawValue();
        value.setTaxRate(subject == null ? null
            : subject.getTaxRate(bill.getStore() == null ? null : bill.getStore().getUuid()));
        value.setSubject(field.getValue());
        value.getSubject().setCode(field.getCodeBox().getValue());
        value.setbSubject(subject);
        value.changeTotal(ep.getScale(), ep.getRoundingMode());
        editGrid.refreshRow(getRow());
      }

      @Override
      public Message validate(Widget sender, String value) {
        BFeeLine entity = values.get(getRow());
        if (entity.isEmpty() && ObjectUtil.equals(bill.getDateRange(), entity.getDateRange())) {
          return null;
        }
        if (StringUtil.isNullOrBlank(field.getValue().getUuid())
            && StringUtil.isNullOrBlank(field.getValue().getCode()) == false) {
          field.clearMessages();
          entity.setSubject(new BUCN(null, field.getValue().getCode(), null));
          return Message.error(
              FeeMessages.M.cannotFindCode(field.getCaption(), field.getValue().getCode()), field);
        }
        if (checkEmpty && (entity.getSubject() == null || entity.getSubject().getUuid() == null)) {
          return Message.error(FeeMessages.M.notNull(field.getCaption()), field);
        }

        return null;
      }
    }
  }

  private class TotalRenderer extends EditGridCellWidgetRenderer<RNumberBox> {

    public TotalRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private RNumberBox field;

    @Override
    protected RNumberBox drawField(RGridColumnDef colDef, Object data) {
      field = new RNumberBox();
      field.setWidth("100%");
      field.setSelectAllOnFocus(true);
      field.setCaption(PFeeLineDef.total_total.getCaption());
      field.setMaxValue(MAX_TOTAL);
      field.setMinValue(MIN_TOTAL);
      field.setRequired(false);
      field.setTextAlignment(RTextBoxBase.ALIGN_RIGHT);
      field.setFormat(M3Format.fmt_money);
      field.setScale(2);
      field.addChangeHandler(new TotalChangeHandler());
      field.setValidator(new NumberBoxValidator());
      return field;
    }

    @Override
    public void setValue(Object value) {
      BFeeLine detail = values.get(getRow());
      field.setText(detail.getTotal().getText());
    }

    private class TotalChangeHandler implements ChangeHandler {
      @Override
      public void onChange(ChangeEvent arg0) {
        BFeeLine value = values.get(getRow());
        value.getTotal().setValue(field.getValueAsBigDecimal());
        value.getTotal().setText(field.getText());
        value.changeTotal(ep.getScale(), ep.getRoundingMode());
        editGrid.refreshRow(getRow());
      }
    }

    private class NumberBoxValidator implements RValidator {
      @Override
      public Message validate(Widget sender, String value) {
        BFeeLine detail = values.get(getRow());
        if (detail.isEmpty() && ObjectUtil.equals(bill.getDateRange(), detail.getDateRange())) {
          return null;
        }
        if (checkEmpty) {
          if (StringUtil.isNullOrBlank(detail.getTotal().getText())
              || detail.getTotal().getValue() == null) {
            return Message.error(FeeMessages.M.notNull(field.getCaption()));
          } else if (detail.getTotal().getValue().compareTo(BigDecimal.ZERO) == 0) {
            return Message.error(FeeMessages.M.notZero(field.getCaption()));
          }
        }

        return null;
      }
    }
  }

  private class BeginDateRenderer extends EditGridCellWidgetRenderer<RDateBox> {
    public BeginDateRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private RDateBox field;

    @Override
    protected RDateBox drawField(RGridColumnDef colDef, Object data) {
      field = new RDateBox();
      field.setWidth("100%");
      field.setCaption(PFeeDef.dateRange_beginDate.getCaption());
      field.setRequired(false);
      field.addChangeHandler(new BeginDateChangeHandler());
      return field;
    }

    @Override
    public void setValue(Object value) {
      BFeeLine detail = values.get(getRow());
      field.setValue(detail.getDateRange().getBeginDate());
    }

    private class BeginDateChangeHandler implements ChangeHandler {
      @Override
      public void onChange(ChangeEvent arg0) {
        BFeeLine value = values.get(getRow());
        value.getDateRange().setBeginDate(field.getValue());
        Widget widget = editGrid.getWidget(getRow(), 5);
        if (widget instanceof EndDateRenderer) {
          ((EndDateRenderer) widget).field.validate();
        }
      }
    }
  }

  private class EndDateRenderer extends EditGridCellWidgetRenderer<RDateBox> {
    public EndDateRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private RDateBox field;

    @Override
    protected RDateBox drawField(RGridColumnDef colDef, Object data) {
      field = new RDateBox();
      field.setWidth("100%");
      field.setCaption(PFeeDef.dateRange_endDate.getCaption());
      field.setRequired(false);
      field.addChangeHandler(new EndDateChangeHandler());
      field.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String value) {
          BFeeLine line = values.get(getRow());
          if (field.getValue() == null)
            return null;
          if (line.getDateRange().getBeginDate() != null) {
            if (line.getDateRange().getBeginDate().after(field.getValue())) {
              return Message.error(field.getCaption() + " : "
                  + PFeeDef.constants.dateRange_endDate() + FeeMessages.M.cannot()
                  + FeeMessages.M.lessThan(PFeeDef.constants.dateRange_beginDate()));
            }
          }
          return null;
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BFeeLine detail = values.get(getRow());
      field.setValue(detail.getDateRange().getEndDate());
    }

    private class EndDateChangeHandler implements ChangeHandler {
      @Override
      public void onChange(ChangeEvent arg0) {
        BFeeLine value = values.get(getRow());
        value.getDateRange().setEndDate(field.getValue());
      }
    }
  }

  private class TaxRateRenderer extends EditGridCellWidgetRenderer<TaxRateComboBox> {

    public TaxRateRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private TaxRateComboBox field;

    @Override
    protected TaxRateComboBox drawField(RGridColumnDef colDef, Object data) {
      field = new TaxRateComboBox();
      field.setWidth("100%");
      field.setCaption(PFeeLineDef.constants.taxRate());
      field.addChangeHandler(new TaxRateChangeHandler());
      field.setValidator(new RValidator() {
        
        @Override
        public Message validate(Widget sender, String value) {
          BFeeLine entity = values.get(getRow());
          if (entity.isEmpty() && ObjectUtil.equals(bill.getDateRange(), entity.getDateRange())) {
            return null;
          }
          if (checkEmpty && (entity.getTaxRate()==null)) {
            return Message.error(FeeMessages.M.notNull(field.getCaption()), field);
          }
          return null;
        }
      });
      field.setMaxDropdownRowCount(10);
      field.refreshOptions();
      return field;
    }

    @Override
    public void setValue(Object value) {
      BFeeLine detail = values.get(getRow());
      field.setValue(detail.getTaxRate());
    }

    private class TaxRateChangeHandler implements ChangeHandler {
      @Override
      public void onChange(ChangeEvent arg0) {
        BFeeLine value = values.get(getRow());
        value.setTaxRate(field.getValue());
        value.changeTotal(ep.getScale(), ep.getRoundingMode());
        editGrid.refreshRow(getRow());
      }
    }
  }

  private class GridDataProvider implements RGridDataProvider, EditGridDataProduce {

    @Override
    public BFeeLine create() {
      BFeeLine line = new BFeeLine();
      if (bill != null) {
        line.getDateRange().setBeginDate(bill.getDateRange().getBeginDate());
        line.getDateRange().setEndDate(bill.getDateRange().getEndDate());
      }
      return line;
    }

    @Override
    public int getRowCount() {
      return values == null ? 0 : values.size();
    }

    @Override
    public Object getData(int row, int col) {
      if (values == null || row < 0 || row >= values.size())
        return null;
      // resultTotalHtml.setText(FeeMessages.M.resultTotal(values.size()));
      BFeeLine value = values.get(row);
      if (col == lineNumberCol.getIndex()) {
        value.setLineNumber(row);
        return (row + 1);
      } else if (col == subjectCodeCol.getIndex()) {
        return value.getSubject();
      } else if (col == subjectNameCol.getIndex()) {
        return value.getSubject() == null ? null : value.getSubject().getName();
      } else if (col == beginDateCol.getIndex()) {
        return value.getDateRange().getBeginDate();
      } else if (col == endDateCol.getIndex()) {
        return value.getDateRange().getEndDate();
      } else if (col == totalCol.getIndex()) {
        return value.getTotal().getValue();
      } else if (col == taxRateCol.getIndex()) {
        return value.getTaxRate();
      } else if (col == taxCol.getIndex()) {
        return value.getTax();
      } else if (col == issueInvoiceCol.getIndex()) {
        return value.isIssueInvoice();
      } else if (col == remarkCol.getIndex()) {
        return value.getRemark();
      }
      return null;
    }
  }

  private class InvoiceRenderer extends EditGridCellWidgetRenderer<RCheckBox> {
    public InvoiceRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private RCheckBox field;

    @Override
    protected RCheckBox drawField(RGridColumnDef colDef, Object data) {
      field = new RCheckBox();
      field.addValueChangeHandler(new ValueChangeHandler() {
        @Override
        public void onValueChange(ValueChangeEvent arg0) {
          BFeeLine line = values.get(getRow());
          line.setIssueInvoice(field.isChecked());
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BFeeLine line = values.get(getRow());
      field.setValue(line.isIssueInvoice());
    }
  }

  private class RemarkRenderer extends EditGridCellWidgetRenderer<RTextBox> {
    public RemarkRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    private RTextBox field;

    @Override
    protected RTextBox drawField(RGridColumnDef colDef, Object data) {
      field = new RTextBox(colDef.getFieldDef());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addValueChangeHandler(new ValueChangeHandler<String>() {
        @Override
        public void onValueChange(ValueChangeEvent<String> arg0) {
          BFeeLine line = values.get(getRow());
          line.setRemark(field.getValue());
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BFeeLine line = values.get(getRow());
      field.setValue(line.getRemark());
    }
  }

  private void doBatchAdd() {
    if (dialog == null) {
      dialog = new SubjectBrowserDialog(BSubjectType.credit.name(), direction, Boolean.TRUE,
          BUsageType.tempFee.name(), true);
      dialog.addValueChangeHandler(new ValueChangeHandler<List<BSubject>>() {
        @Override
        public void onValueChange(ValueChangeEvent<List<BSubject>> event) {
          List<BFeeLine> values = new ArrayList<BFeeLine>();
          for (BSubject subject : event.getValue()) {
            BFeeLine detail = new BFeeLine();
            detail.setSubject(BUCN.getBUCN(subject));
            detail.setbSubject(subject);
            detail.setTaxRate(subject == null ? null
                : subject.getTaxRate(bill.getStore() == null ? null : bill.getStore().getUuid()));
            if (bill != null) {
              detail.getDateRange().setBeginDate(bill.getDateRange().getBeginDate());
              detail.getDateRange().setEndDate(bill.getDateRange().getEndDate());
            }
            values.add(detail);
          }

          editGrid.appendValues(values.toArray());
        }
      });
      dialog.addOpenHandler(new OpenHandler<PopupPanel>() {
        @Override
        public void onOpen(OpenEvent<PopupPanel> event) {
          dialog.setDirection(direction);
        }
      });
    }
    dialog.center();
  }

  private void doRefresh() {
    resultTotalHtml.setText(FeeMessages.M.resultTotal(values.size()));
    RActionEvent.fire(FeeLineGridGadget.this, ActionName.REFRESH,
        new Integer(editGrid.getCurrentRow()));
  }

  private class Handler_Click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == batchAddAction) {
        doBatchAdd();
      } else if (event.getSource() == addAction) {
        editGrid.appendValue();
      } else if (event.getSource() == insertAction) {
        editGrid.appendValue(editGrid.getCurrentRow() > 0 ? editGrid.getCurrentRow() : 0);
      } else if (event.getSource() == deleteAction) {
        editGrid.deleteSelections();
      }
    }
  }
}
