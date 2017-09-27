/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	InvoiceAbortDetailGadget.java
 * 模块说明：	
 * 修改历史：
 * 2015年12月16日 - lixiaohong - 创建。
 */
package com.hd123.m3.account.gwt.ivc.abort.client.ui.gadget;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.ivc.abort.client.biz.BInvoiceAbort;
import com.hd123.m3.account.gwt.ivc.common.client.InvoiceCommonMessages;
import com.hd123.m3.account.gwt.ivc.common.client.StartNumberBrowserBox;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceLine;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BInvoiceStock;
import com.hd123.m3.account.gwt.ivc.common.client.biz.BStockState;
import com.hd123.m3.account.gwt.ivc.intf.client.dd.PInvoiceLineDef;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridCellWidgetRenderer;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBoxBase;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.RGridSummaryMethod;
import com.hd123.rumba.gwt.widget2.client.grid.RGridSummaryProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRenderer;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RCellRendererFactory;
import com.hd123.rumba.gwt.widget2.client.hotkey.RHotKey;
import com.hd123.rumba.gwt.widget2.client.hotkey.RKey;
import com.hd123.rumba.gwt.widget2.client.menu.RMenuItem;
import com.hd123.rumba.gwt.widget2.client.menu.RPopupMenu;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2.client.panel.RVerticalPanel;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarButton;
import com.hd123.rumba.gwt.widget2.client.toolbar.RToolbarSplitButton;
import com.hd123.rumba.gwt.widget2e.client.event.RActionEvent;
import com.hd123.rumba.gwt.widget2e.client.event.RActionHandler;

/**
 * 发票作废单|发票明细编辑页面
 * 
 * @author lixiaohong
 * @since 1.7
 * 
 */
public class InvoiceAbortDetailGadget extends RCaptionBox implements RActionHandler {
  private BInvoiceAbort entity;
  private Click_Handler clickHandler = new Click_Handler();
  private boolean checkEmpty;

  private HTML totalCount;
  private RAction appendAction;
  private RAction insertAction;
  private RAction deleteAction;

  private EditGrid<BInvoiceLine> grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef invoiceCodeCol;
  private RGridColumnDef startNumberCol;
  private RGridColumnDef quantityCol;
  private RGridColumnDef endNumberCol;
  private RGridColumnDef remarkCol;
  private List<Message> messages = new ArrayList<Message>();

  public void setValue(BInvoiceAbort entity) {
    this.entity = entity;
    if (entity.getAbortLines() == null) {
      entity.setAbortLines(new ArrayList<BInvoiceLine>());
    }
    grid.setValues(entity.getAbortLines());

    totalCount.setHTML(InvoiceCommonMessages.M.totalCount(entity.getAbortLines().size()));

    grid.refresh();
  }

  public InvoiceAbortDetailGadget() {
    super();
    setCaption(InvoiceCommonMessages.M.invoiceDetail());
    getCaptionBar().setShowCollapse(true);
    setEditing(true);
    setWidth("100%");
    drawSelf();
  }

  @Override
  public List<Message> getInvalidMessages() {
    messages.addAll(super.getInvalidMessages());
    return messages;
  }

  @Override
  public boolean validate() {
    messages.clear();
    checkEmpty = true;
    boolean isValid = grid.validate();
    checkEmpty = false;
    isValid = isValid && validateContain();
    return isValid;
  }

  private boolean validateContain() {
    boolean isValid = true;
    boolean result = true;
    for (int i = 0; i < entity.getAbortLines().size(); i++) {
      for (int j = i + 1; j < entity.getAbortLines().size(); j++) {
        if ((!StringUtil.isNullOrBlank(entity.getAbortLines().get(i).getStartNumber()))
            && (!StringUtil.isNullOrBlank(entity.getAbortLines().get(i).getEndNumber()))
            && (!StringUtil.isNullOrBlank(entity.getAbortLines().get(j).getStartNumber()))
            && (!StringUtil.isNullOrBlank(entity.getAbortLines().get(j).getEndNumber()))
            && isNumeric(entity.getAbortLines().get(i).getStartNumber())
            && isNumeric(entity.getAbortLines().get(i).getEndNumber())
            && isNumeric(entity.getAbortLines().get(j).getStartNumber())
            && isNumeric(entity.getAbortLines().get(j).getEndNumber())) {
          isValid = !isInclude(entity.getAbortLines().get(i), entity.getAbortLines().get(j));
          if (isValid == false) {
            messages.add(Message.error(InvoiceCommonMessages.M.contained(i + 1, j + 1)));
            result = false;
          }
        }
      }
    }
    return result;
  }

  public boolean isInclude(BInvoiceLine line1, BInvoiceLine line2) {
    if (line1.getStartNumber().length() != line2.getStartNumber().length()) {
      return false;
    }
    if ((new BigInteger(line1.getStartNumber()).compareTo(new BigInteger(line2.getEndNumber())) > 0)
        || (new BigInteger(line2.getStartNumber()).compareTo(new BigInteger(line1.getEndNumber())) > 0)) {
      return false;
    }
    return true;
  }

  private void drawSelf() {
    drawGrid();

    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.add(grid);
    setContent(root);

    totalCount = new HTML(InvoiceCommonMessages.M.totalCount(0));
    getCaptionBar().addButton(totalCount);

    RPopupMenu addMenu = new RPopupMenu();

    appendAction = new RAction(RActionFacade.APPEND, clickHandler);
    appendAction.setCaption(InvoiceCommonMessages.M.append());
    appendAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(appendAction));

    insertAction = new RAction(InvoiceCommonMessages.M.insertBefore(), clickHandler);
    insertAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(insertAction));

    RToolbarSplitButton appendButton = new RToolbarSplitButton(appendAction);
    appendButton.setMenu(addMenu);
    appendButton.setShowText(false);
    getCaptionBar().addButton(appendButton);

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    deleteAction.clearHotKey();
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);

  }

  private void drawGrid() {
    grid = new EditGrid<BInvoiceLine>();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setRequired(true);
    grid.setCaption(InvoiceCommonMessages.M.invoiceDetail());

    grid.addActionHandler(new RActionHandler() {

      @Override
      public void onAction(RActionEvent event) {
        if (EditGrid.ACTION_APPEND.equals(event.getActionName())
            || EditGrid.ACTION_REMOVE.equals(event.getActionName())) {
          totalCount.setHTML(InvoiceCommonMessages.M.totalCount(entity == null ? 0 : entity
              .getAbortLines().size()));
        }
      }
    });

    grid.setCustomSummaryProvider(new RGridSummaryProvider() {

      @Override
      public String getSummary(int col) {
        if (col == startNumberCol.getIndex()) {
          return "合计：";
        }
        if (col == quantityCol.getIndex()) {
          int total = 0;
          for (BInvoiceLine line : entity.getAbortLines()) {
            total = total + line.getQuantity();
          }
          return total + "";
        }
        return null;
      }

    });

    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.setDataProducer(provider);

    CellRendererFactory rendererFactory = new CellRendererFactory();
    lineNumberCol = new RGridColumnDef(InvoiceCommonMessages.M.lineNo());
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    invoiceCodeCol = new RGridColumnDef(PInvoiceLineDef.invoiceCode);
    invoiceCodeCol.setWidth("180px");
    grid.addColumnDef(invoiceCodeCol);

    startNumberCol = new RGridColumnDef(PInvoiceLineDef.startNumber);
    startNumberCol.setRendererFactory(rendererFactory);
    startNumberCol.setSummaryMethod(RGridSummaryMethod.CUSTOM);
    startNumberCol.setWidth("180px");
    grid.addColumnDef(startNumberCol);

    quantityCol = new RGridColumnDef(PInvoiceLineDef.abort_quantity);
    quantityCol.setRendererFactory(rendererFactory);
    quantityCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    quantityCol.setSummaryMethod(RGridSummaryMethod.CUSTOM);
    quantityCol.setWidth("120px");
    grid.addColumnDef(quantityCol);

    endNumberCol = new RGridColumnDef(PInvoiceLineDef.endNumber);
    endNumberCol.setRendererFactory(rendererFactory);
    endNumberCol.setWidth("180px");
    grid.addColumnDef(endNumberCol);

    remarkCol = new RGridColumnDef(PInvoiceLineDef.remark);
    remarkCol.setRendererFactory(rendererFactory);
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  private String getEndNumber(String startNumber, int quantity) {
    BigInteger endNo = BigInteger.ZERO;
    String endNumber = "";
    if (startNumber != null && isNumeric(startNumber)) {
      endNo = (new BigInteger(startNumber).add(new BigInteger(quantity + "")))
          .subtract(BigInteger.ONE);
      endNumber = endNo + "";
      if (startNumber.length() > endNumber.length()) {
        endNumber = getSizeZero(startNumber.length() - endNumber.length()) + endNumber;
      }
    }
    return endNumber;
  }

  private String getSizeZero(int size) {
    String s = "";
    for (int i = 0; i < size; i++) {
      s = s + "0";
    }
    return s;
  }

  private class GridDataProvider implements RGridDataProvider, EditGridDataProduce<BInvoiceLine> {

    @Override
    public BInvoiceLine create() {
      BInvoiceLine detail = new BInvoiceLine();
      return detail;
    }

    @Override
    public int getRowCount() {
      return entity == null || entity.getAbortLines() == null ? 0 : entity.getAbortLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      BInvoiceLine rowData = entity.getAbortLines().get(row);
      if (col == lineNumberCol.getIndex()) {
        return row + 1;
      } else if (col == invoiceCodeCol.getIndex()) {
        return rowData.getInvoiceCode();
      } else if (col == startNumberCol.getIndex()) {
        return rowData.getStartNumber();
      } else if (col == quantityCol.getIndex()) {
        return rowData.getQuantity();
      } else if (col == endNumberCol.getIndex()) {
        return rowData.getEndNumber();
      } else if (col == remarkCol.getIndex()) {
        return rowData.getRemark();
      }
      return null;
    }
  }

  private class CellRendererFactory implements RCellRendererFactory {

    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        Object data) {
      String fieldName = colDef.getName();
      if (fieldName.equals(startNumberCol.getName())) {
        return new StartNumberRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(quantityCol.getName())) {
        return new QuantityRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(endNumberCol.getName())) {
        return new EndNumberRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(remarkCol.getName())) {
        return new RemarkTextRenderer(grid, colDef, row, col, data);
      }
      return null;
    }
  }

  private class StartNumberRenderer extends EditGridCellWidgetRenderer {
    private StartNumberBrowserBox field;

    public StartNumberRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new StartNumberBrowserBox(colDef.getFieldDef().getCaption(),
          BStockState.received.name());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.setRegex("[0-9]*");
      field.setRegexMessage(InvoiceCommonMessages.M.regexMessage());
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.setConditionLimit(entity.getAccountUnit(), entity.getInvoiceType());
      field.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          BInvoiceLine value = entity.getAbortLines().get(getRow());
          value.setStartNumber(field.getValue());
          if (value.getQuantity() > 0) {
            value.setEndNumber(getEndNumber(value.getStartNumber(), value.getQuantity()));
          }
          grid.refreshRow(getRow());
        }
      });

      field.addValueChangeHandler(new ValueChangeHandler<String>() {
        @Override
        public void onValueChange(ValueChangeEvent event) {
          BInvoiceLine value = entity.getAbortLines().get(getRow());
          value.setStartNumber(field.getValue());
          BInvoiceStock stock = field.getRawValue();
          value.setInvoiceCode(stock == null ? null : stock.getInvoiceCode());
          if (value.getQuantity() > 0) {
            value.setEndNumber(getEndNumber(value.getStartNumber(), value.getQuantity()));
          }
          grid.refreshRow(getRow());
        }
      });

      field.setValidator(new RValidator() {

        @Override
        public Message validate(Widget sender, String value) {
          if (checkEmpty && StringUtil.isNullOrBlank(value)) {
            return Message.error(field.getCaption() + ":" + InvoiceCommonMessages.M.notNull(),
                field);
          }
          return null;
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BInvoiceLine detail = entity.getAbortLines().get(getRow());
      field.setValue(detail.getStartNumber());
    }
  }

  private class QuantityRenderer extends EditGridCellWidgetRenderer {
    private RNumberBox<Integer> field;

    public QuantityRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RNumberBox<Integer>(colDef.getFieldDef());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.setTextAlignment(RTextBoxBase.ALIGN_RIGHT);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addChangeHandler(new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          BInvoiceLine value = entity.getAbortLines().get(getRow());
          if (field.getValue() == null) {
            value.setQuantity(0);
            grid.refresh();
            return;
          }
          if (field.getValueAsBigDecimal().compareTo(new BigDecimal(Integer.MAX_VALUE)) < 0) {
            value.setQuantity(field.getValueAsInteger());
            grid.refresh();
          }
        }
      });

      field.setValidator(new RValidator() {
        @Override
        public Message validate(Widget sender, String value) {
          BInvoiceLine line = entity.getAbortLines().get(getRow());
          if (StringUtil.isNullOrBlank(line.getStartNumber())) {
            return null;
          }
          String endNumber = getEndNumber(line.getStartNumber(), field.getValueAsInteger());
          if (field.getValueAsInteger() > 0)
            line.setEndNumber(endNumber);
          if (endNumber.length() > line.getStartNumber().length()) {
            return Message.error(
                field.getCaption() + ":" + InvoiceCommonMessages.M.overQuantitySize(), field);
          }
          if (checkEmpty && field.getValueAsBigDecimal().compareTo(BigDecimal.ZERO) <= 0) {
            return Message.error(field.getCaption() + ":" + InvoiceCommonMessages.M.needOne(),
                field);
          }
          if (field.getValueAsBigDecimal().compareTo(new BigDecimal(Integer.MAX_VALUE)) > 0) {
            return Message.error(field.getCaption() + ":"
                + InvoiceCommonMessages.M.maxNotGreater(Integer.MAX_VALUE + ""));
          }
          return null;
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BInvoiceLine detail = entity.getAbortLines().get(getRow());
      field.setValue(detail.getQuantity());
    }
  }

  private class EndNumberRenderer extends EditGridCellWidgetRenderer {
    private RTextBox field;

    public EndNumberRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RTextBox(colDef.getFieldDef());
      field.setWidth("100%");
      field.setEnterToTab(false);
      field.setRegex("[0-9]*");
      field.setRegexMessage(InvoiceCommonMessages.M.regexMessage());
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addChangeHandler(new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          BInvoiceLine value = entity.getAbortLines().get(getRow());
          value.setEndNumber(field.getValue());
          if (field.isValid()&&isNumeric(value.getStartNumber()) && isNumeric(value.getEndNumber())
              && !StringUtil.isNullOrBlank(value.getStartNumber())) {
            value.setQuantity((new BigInteger(value.getEndNumber()).subtract(new BigInteger(value
                .getStartNumber())).add(BigInteger.ONE)).intValue());
            grid.refresh();
          }
        }
      });
      field.setValidator(new RValidator() {
        @Override
        public Message validate(Widget sender, String value) {
          BInvoiceLine line = entity.getAbortLines().get(getRow());
          if (checkEmpty && StringUtil.isNullOrBlank(field.getValue())) {
            return Message.error(field.getCaption() + ":" + InvoiceCommonMessages.M.notNull(),
                field);
          }
          if(checkEmpty &&!isValid()){
            return field.getMessages().get(0);
          }
          if (isValid()
              && !StringUtil.isNullOrBlank(line.getStartNumber())
              && field.getValue() != null
              && isNumeric(line.getStartNumber())
              && isNumeric(field.getValue())
              && (new BigInteger(field.getValue()).compareTo(new BigInteger(line.getStartNumber())) < 0)) {
            return Message.error(
                field.getCaption() + ":" + InvoiceCommonMessages.M.endNumberLessError(), field);
          }
          if (isValid() && !StringUtil.isNullOrBlank(line.getStartNumber())
              && !StringUtil.isNullOrBlank(field.getValue())
              && field.getValue().length() != line.getStartNumber().length()) {
            return Message.error(field.getCaption() + ":" + InvoiceCommonMessages.M.notEqualSize(),
                field);
          }
          if (!StringUtil.isNullOrBlank(field.getValue())&&!StringUtil.isNullOrBlank(line.getStartNumber())) {
            BigInteger end = new BigInteger(field.getValue());
            BigInteger start = new BigInteger(line.getStartNumber());
            BigInteger qty = end.subtract(start);
            int maxInt = Integer.MAX_VALUE;
            if (isValid() && qty.compareTo(new BigInteger(maxInt + "")) > 0) {
              return Message.error(
                  field.getCaption() + ":" + InvoiceCommonMessages.M.canNotGreaterMax(maxInt + ""),
                  field);
            }
          }
          return null;
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BInvoiceLine detail = entity.getAbortLines().get(getRow());
      field.setValue(detail.getEndNumber());
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
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addChangeHandler(new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          BInvoiceLine value = entity.getAbortLines().get(getRow());
          value.setRemark(field.getValue());
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BInvoiceLine detail = entity.getAbortLines().get(getRow());
      field.setValue(detail.getRemark());
    }
  }

  private class Click_Handler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (appendAction == event.getSource()) {
        grid.appendValue(entity.getAbortLines().size());
      } else if (event.getSource() == insertAction) {
        int index = grid.getCurrentRow() > 0 ? grid.getCurrentRow() : 0;
        grid.appendValue(index);
      } else if (deleteAction == event.getSource()) {
        grid.deleteSelections();
        grid.refresh();
      }
    }
  }

  public boolean isNumeric(String str) {
    if (StringUtil.isNullOrBlank(str)) {
      return false;
    }
    if (str.matches("[0-9]*")) {
      return true;
    }
    return false;
  }

  @Override
  public void onAction(RActionEvent event) {
    entity.getAbortLines().clear();
    grid.refresh();
    for (int i = 0; i < entity.getAbortLines().size(); i++) {
      Widget widget = grid.getWidget(i, 3);
      if (widget instanceof StartNumberRenderer) {
        ((StartNumberRenderer) widget).field.setConditionLimit(entity.getStore(),
            entity.getInvoiceType());
      }
    }
  }
}
