/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	LineEditGridGadget.java
 * 模块说明：	
 * 修改历史：
 * 2015-5-19 - liuguilin - 创建。
 */
package com.hd123.m3.account.gwt.internalfee.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.BTotal;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectStoreTaxRate;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BUsageType;
import com.hd123.m3.account.gwt.cpnts.client.rpc.AccountCpntsService;
import com.hd123.m3.account.gwt.cpnts.client.ui.filter.SubjectFilter;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.internalfee.client.EPInternalFee;
import com.hd123.m3.account.gwt.internalfee.client.InternalFeeMessages;
import com.hd123.m3.account.gwt.internalfee.client.biz.BInternalFeeLine;
import com.hd123.m3.account.gwt.internalfee.intf.client.dd.PInternalFeeLineDef;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.base.client.tax.BTaxCalculator;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridCellWidgetFactory;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridColumnDef;
import com.hd123.m3.commons.gwt.widget.client.ui.grid.EditGridDataProvider;
import com.hd123.rumba.commons.gwt.mini.client.StringUtil;
import com.hd123.rumba.gwt.base.client.BUCN;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.fielddef.FieldDef;
import com.hd123.rumba.gwt.util.client.fielddef.OtherFieldDef;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RCheckBox;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBoxBase;
import com.hd123.rumba.gwt.widget2.client.grid.RPageData;
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

/**
 * @author liuguilin
 * 
 */
public class LineEditGridGadget extends RCaptionBox implements HasRActionHandlers, RActionHandler {

  private OtherFieldDef lineNumber = new OtherFieldDef("lineNumber", "行号", true);

  private EPInternalFee ep = EPInternalFee.getInstance();

  private HTML resultTotalHtml;
  private RAction addAction;
  private RAction insertAction;
  private RAction batchAddAction;
  private RPopupMenu addMenu;
  private RToolbarSplitButton addButton;
  private RAction deleteAction;
  private SubjectBrowserDialog dialog;

  private EditGrid<BInternalFeeLine> editGrid;
  private EditGridColumnDef lineNumberCol;
  private EditGridColumnDef subjectCodeCol;
  private EditGridColumnDef subjectNameCol;
  private EditGridColumnDef totalCol;
  private EditGridColumnDef taxCol;
  private EditGridColumnDef taxRateCol;
  private EditGridColumnDef invoiceCol;
  private EditGridColumnDef remarkCol;

  private Handler_Click clickHandler = new Handler_Click();

  private int direction;
  private List<BInternalFeeLine> values = new ArrayList<BInternalFeeLine>();
  private List<Message> messages = new ArrayList<Message>();

  public LineEditGridGadget(int direction) {
    super();
    this.direction = direction;
    setContentSpacing(0);
    setWidth("100%");
    setCaption(PInternalFeeLineDef.constants.tableCaption());
    setEditing(true);
    getCaptionBar().setShowCollapse(true);

    RVerticalPanel panel = new RVerticalPanel();
    panel.setWidth("100%");
    panel.setSpacing(0);

    Widget w = drawGrid();
    panel.add(w);

    resultTotalHtml = new HTML(InternalFeeMessages.M.resultTotal(0));

    getCaptionBar().addButton(resultTotalHtml);
    addMenu = new RPopupMenu();

    batchAddAction = new RAction(InternalFeeMessages.M.batchAdd(), clickHandler);
    batchAddAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(batchAddAction));

    addAction = new RAction(RActionFacade.APPEND, clickHandler);
    addAction.setCaption(InternalFeeMessages.M.addLine());
    addAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(addAction));

    insertAction = new RAction(InternalFeeMessages.M.insertLine(), clickHandler);
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

  /**
   * @param uuid The uuid to set.
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  private Widget drawGrid() {
    editGrid = new EditGrid<BInternalFeeLine>();
    editGrid.setWidth("100%");
    editGrid.setDisplayWidgetStyle(true);
    editGrid.setShowRowSelector(true);
    editGrid.addColumnDefs(createColumnDef());
    editGrid.setShowAddRemoveColumn(false);
    editGrid.setDefaultDataRowCount(1);
    editGrid.setProvider(new DataProvider());
    editGrid.setCellWidgetFactory(new CellWidgetFactory());

    return editGrid;
  }

  private List<EditGridColumnDef> createColumnDef() {
    lineNumberCol = new EditGridColumnDef(lineNumber, "20px");
    subjectCodeCol = new EditGridColumnDef(PInternalFeeLineDef.subject_code, true, "150px");
    subjectNameCol = new EditGridColumnDef(PInternalFeeLineDef.subject_name, "150px");
    totalCol = new EditGridColumnDef(PInternalFeeLineDef.total_total, true, "80px");
    taxCol = new EditGridColumnDef(PInternalFeeLineDef.total_tax, false, "60px",
        HasHorizontalAlignment.ALIGN_RIGHT);
    taxRateCol = new EditGridColumnDef(PInternalFeeLineDef.taxRate, "60px");
    invoiceCol = new EditGridColumnDef(PInternalFeeLineDef.issueInvoice, true, "80px");
    remarkCol = new EditGridColumnDef(PInternalFeeLineDef.remark, true, "100px");

    return Arrays.asList(lineNumberCol, subjectCodeCol, subjectNameCol, totalCol, taxCol,
        taxRateCol, invoiceCol, remarkCol);
  }

  public EditGrid<BInternalFeeLine> getEditGrid() {
    return editGrid;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  public void setValue(List<BInternalFeeLine> values, int direction) {
    this.values = values;
    this.direction = direction;
    editGrid.rebuild();
    if (values.size() > 0)
      editGrid.setCurrentRow(0);
  }

  public void clear() {
    values.clear();
    if (dialog != null) {
      dialog.clearConditions();
    }
    editGrid.refresh();
  }

  @Override
  public List getInvalidMessages() {
    List<Message> result = super.getInvalidMessages();
    result.addAll(messages);
    return result;
  }

  @Override
  public boolean validate() {
    messages.clear();

    boolean valid = super.validate();
    boolean noRecord = true;
    boolean subjectValid = true;

    for (int i = 0; i < values.size(); i++) {
      BInternalFeeLine line = values.get(i);
      subjectValid = ((SubjectUCNBox) editGrid.getWidget(i + 1, 2)).validate();

      if (subjectValid && line.isEmpty())
        continue;

      noRecord = false;

      if (((SubjectUCNBox) editGrid.getWidget(i + 1, 2)).isValid()) {
        if (line.getSubject() == null || line.getSubject().getCode() == null) {
          String msg = InternalFeeMessages.M.belongToline(i + 1)
              + PInternalFeeLineDef.constants.subject() + InternalFeeMessages.M.cannot()
              + InternalFeeMessages.M.beBblank() + "。";
          Message message = Message.error(msg, editGrid.getWidget(i + 1, 2));
          messages.add(message);
          valid = false;
        }
      } else {
        valid = false;
      }

      boolean noTotal = line.getTotal() == null || line.getTotal().getTotal() == null
          || line.getTotal().getTotal().compareTo(BigDecimal.ZERO) == 0;
      if (noTotal) {
        String msg = InternalFeeMessages.M.belongToline(i + 1)
            + PInternalFeeLineDef.constants.total_total() + InternalFeeMessages.M.cannot()
            + InternalFeeMessages.M.beBblank() + InternalFeeMessages.M.or()
            + InternalFeeMessages.M.valueEquals("0") + "。";
        Message message = Message.error(msg, editGrid.getWidget(i + 1, 4));
        messages.add(message);
        valid = false;
      }
    }

    if (noRecord) {
      Message message = Message.error(InternalFeeMessages.M.detailLines()
          + InternalFeeMessages.M.cannot() + InternalFeeMessages.M.beBblank() + "。");
      messages.add(message);
      valid = false;
    }
    return valid;
  }

  private class DataProvider implements EditGridDataProvider<BInternalFeeLine> {
    @Override
    public List<BInternalFeeLine> getData() {
      return values;
    }

    @Override
    public Object getData(int row, String colName) {
      if (values == null || row < 0 || row >= values.size())
        return null;

      resultTotalHtml.setText(InternalFeeMessages.M.resultTotal(values.size()));

      BInternalFeeLine rowData = values.get(row);
      if (colName.equals(lineNumberCol.getName())) {
        return row + 1;
      } else if (colName.equals(subjectCodeCol.getName())) {
        return rowData.getSubject();
      } else if (colName.equals(subjectNameCol.getName())) {
        return rowData.getSubject() == null ? null : rowData.getSubject().getName();
      } else if (colName.equals(totalCol.getName())) {
        return rowData.getTotal() == null || rowData.getTotal().getTotal() == null ? null : rowData
            .getTotal().getTotal();
      } else if (colName.equals(taxCol.getName())) {
        return rowData.getTotal() == null || rowData.getTotal().getTax() == null ? null
            : M3Format.fmt_money.format(rowData.getTotal().getTax().doubleValue());
      } else if (colName.equals(taxRateCol.getName())) {
        return rowData.getTaxRate() == null ? null : rowData.getTaxRate().caption();
      } else if (colName.equals(invoiceCol.getName())) {
        return rowData.isIssueInvoice();
      } else if (colName.equals(remarkCol.getName())) {
        return rowData.getRemark();
      }
      return null;
    }

    @Override
    public BInternalFeeLine create() {
      return new BInternalFeeLine();
    }
  }

  private class CellWidgetFactory implements EditGridCellWidgetFactory<BInternalFeeLine> {
    @Override
    public Widget createEditWidget(FieldDef field, int dataRowIdx, BInternalFeeLine rowData) {
      if (field.getName().equals(subjectCodeCol.getName())) {
        SubjectUCNBox subjectField = new SubjectUCNBox(BSubjectType.credit.name(), direction,
            Boolean.TRUE, BUsageType.tempFee.name());
        subjectField.setCodeBoxWidth(1f);
        subjectField.getNameBox().setVisible(false);
        subjectField.setRequired(false);
        subjectField.addChangeHandler(new SubjectChange(dataRowIdx));
        return subjectField;
      } else if (field.getName().equals(totalCol.getName())) {
        final RNumberBox totalField = new RNumberBox();
        totalField.setSelectAllOnFocus(true);
        totalField.setCaption(M.total());
        totalField.setRequired(false);
        totalField.setTextAlignment(RTextBoxBase.ALIGN_RIGHT);
        totalField.setFormat(M3Format.fmt_money);
        totalField.setScale(2);
        totalField.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2);
        totalField.setMinValue(0);
        totalField.addChangeHandler(new TotalChange(dataRowIdx));
        return totalField;
      } else if (field.getName().equals(invoiceCol.getName())) {
        RCheckBox invoiceField = new RCheckBox();
        invoiceField.addChangeHandler(new InvoiceChange(dataRowIdx));
        return invoiceField;
      } else if (field.getName().equals(remarkCol.getName())) {
        RTextBox remarkField = new RTextBox(PInternalFeeLineDef.remark);
        remarkField.addChangeHandler(new RemarkChange(dataRowIdx));
        return remarkField;
      }
      return null;
    }
  }

  private class SubjectChange implements ChangeHandler {

    private int row;

    public SubjectChange(int row) {
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BInternalFeeLine value = values.get(row);
      SubjectUCNBox box = (SubjectUCNBox) event.getSource();
      if (value == null) {
        return;
      }

      value.setSubject(box.getValue());
      BSubject subject = box.getRawValue();
      List<BSubjectStoreTaxRate> storeTaxRates = subject.getStoreTaxRates();
      if (storeTaxRates.size() == 0) {
        value.setTaxRate(subject == null ? null : subject.getTaxRate());
      } else {
        for (BSubjectStoreTaxRate bSubjectStoreTaxRate : storeTaxRates) {
          if (bSubjectStoreTaxRate.getStore().getUuid().equals(uuid)) {
            value.setTaxRate(bSubjectStoreTaxRate.getTaxRate());
            break;
          } else {
            value.setTaxRate(subject == null ? null : subject.getTaxRate());
          }
        }
      }

      if (value.getTotal() != null && value.getTotal().getTotal() != null) {
        value.getTotal().setTax(
            BTaxCalculator.tax(value.getTotal().getTotal(), value.getTaxRate(), ep.getScale(),
                ep.getRoundingMode()));
      }

      RActionEvent.fire(LineEditGridGadget.this, ActionName.REFRESH, "");
      if (box.isValid())
        editGrid.refresh(row);
    }
  }

  private class TotalChange implements ChangeHandler {

    private int row;

    public TotalChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BInternalFeeLine value = values.get(row);
      if (value == null)
        return;

      RNumberBox box = (RNumberBox) event.getSource();
      box.setSelectAllOnFocus(true);
      BigDecimal totalLocal = box.getValueAsBigDecimal();
      value.setTotal(new BTotal(totalLocal, BTaxCalculator.tax(totalLocal, value.getTaxRate(),
          ep.getScale(), ep.getRoundingMode())));

      RActionEvent.fire(LineEditGridGadget.this, ActionName.REFRESH, "");
      editGrid.refresh(row);
    }
  }

  private class InvoiceChange implements ChangeHandler {

    private int row;

    public InvoiceChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BInternalFeeLine value = values.get(row);
      if (value == null)
        return;

      RCheckBox box = (RCheckBox) event.getSource();
      value.setIssueInvoice(box.isChecked());
    }
  }

  private class RemarkChange implements ChangeHandler {

    private int row;

    public RemarkChange(int row) {
      super();
      this.row = row;
    }

    @Override
    public void onChange(ChangeEvent event) {
      BInternalFeeLine value = values.get(row);
      if (value == null)
        return;
      RTextBox box = (RTextBox) event.getSource();
      value.setRemark(box.getValue());
    }
  }

  private class Handler_Click implements ClickHandler {
    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == batchAddAction) {
        doBatchAdd();
      } else if (event.getSource() == addAction) {
        doAdd();
      } else if (event.getSource() == insertAction) {
        doInsert();
      } else if (event.getSource() == deleteAction) {
        doDelete();
      }
    }
  }

  private void doBatchAdd() {
    if (dialog == null) {
      dialog = new SubjectBrowserDialog(BSubjectType.credit.name(), direction, Boolean.TRUE,
          BUsageType.tempFee.name());
      dialog.addValueChangeHandler(new ValueChangeHandler<List<BSubject>>() {
        @Override
        public void onValueChange(ValueChangeEvent<List<BSubject>> event) {
          if (values.size() == 1
              && (values.get(0).getSubject() == null || values.get(0).getSubject().getCode() == null)) {
            values.clear();
          }

          List<BSubject> subjects = event.getValue();

          if (subjects.isEmpty() == false) {
            List<String> subjectCodes = new ArrayList<String>();
            for (BInternalFeeLine value : values) {
              if (value.getSubject() != null && value.getSubject().getCode() != null) {
                subjectCodes.add(value.getSubject().getCode());
              }
            }

            for (BSubject subject : subjects) {
              BInternalFeeLine line = new BInternalFeeLine();

              List<BSubjectStoreTaxRate> storeTaxRates = subject.getStoreTaxRates();
              if (storeTaxRates.size() == 0) {
                line.setTaxRate(subject.getTaxRate());
              } else {
                for (BSubjectStoreTaxRate bSubjectStoreTaxRate : storeTaxRates) {
                  if (bSubjectStoreTaxRate.getStore().getUuid().equals(uuid)) {
                    line.setTaxRate(bSubjectStoreTaxRate.getTaxRate());
                    break;
                  } else {
                    line.setTaxRate(subject.getTaxRate());
                  }
                }
              }

              line.setSubject(new BUCN(subject));
              values.add(line);
            }
            doRefresh();
          }
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

  private void doAdd() {
    GWTUtil.blurActiveElement();
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        doAddLine();
        doRefresh();
        editGrid.setCurrentRow(values.size() - 1);
      }
    });
  }

  private void doAddLine() {
    values.add(new BInternalFeeLine());
  }

  private void doInsert() {
    int index = editGrid.getCurrentRow();
    values.add(index, new BInternalFeeLine());
    doRefresh();
    editGrid.setCurrentRow(index);
  }

  private void doDelete() {
    List list = editGrid.getSelections();
    if (list == null || list.size() == 0) {
      RMsgBox.show(M.seleteDataToAction(M.delete(), M.detailLines()));
      return;
    }
    doDeleteRows();
    doRefresh();
  }

  private void doDeleteRows() {
    List<Integer> selections = editGrid.getSelections();
    for (int i = selections.size() - 1; i >= 0; i--) {
      Integer row = selections.get(i);
      int dataRow = row.intValue();
      values.remove(dataRow);
    }

    if (values.size() <= 0) {
      doAddLine();
    }
  }

  private void doRefresh() {
    editGrid.refresh();
    resultTotalHtml.setText(InternalFeeMessages.M.resultTotal(values.size()));
    RActionEvent.fire(LineEditGridGadget.this, ActionName.REFRESH,
        new Integer(editGrid.getCurrentRow()));
  }

  public static class ActionName {
    /** 表格刷新事件 */
    public static final String REFRESH = "refresh";
  }

  // 接收页面传来的值
  private String uuid = null;

  @Override
  public void onAction(RActionEvent event) {
    if (event.getActionName().equals("storeChangeEent")) {
      uuid = event.getParameters().get(0).toString();

      Set<String> subjectUuids = new HashSet<String>();
      for (BInternalFeeLine internalFeeLine : values) {
        if (internalFeeLine.getSubject() == null || internalFeeLine.getSubject().getUuid() == null) {
          continue;
        }
        subjectUuids.add(internalFeeLine.getSubject().getUuid());
      }

      if (subjectUuids.isEmpty()) {
        return;
      }

      SubjectFilter filter = new SubjectFilter();
      filter.setUuids(subjectUuids);
      AccountCpntsService.Locator.getService().querySubject(filter,
          new AsyncCallback<RPageData<BSubject>>() {

            @Override
            public void onSuccess(RPageData<BSubject> result) {

              for (BInternalFeeLine internalFeeLine : values) {
                if (internalFeeLine.getSubject() == null
                    || internalFeeLine.getSubject().getUuid() == null) {
                  internalFeeLine.setTaxRate(null);
                  continue;
                }
                
                BSubject subject = getSubject(internalFeeLine.getSubject().getUuid(), result.getValues());
                if(subject == null){
                  internalFeeLine.setTaxRate(null);
                  continue;
                }
                
                BSubjectStoreTaxRate taxRate = null;
                for(BSubjectStoreTaxRate storeTaxRate:subject.getStoreTaxRates()){
                  if(storeTaxRate.getStore().getUuid().equals(uuid)){
                    taxRate = storeTaxRate;
                    break;
                  }
                }
                
                if(taxRate !=null){
                  internalFeeLine.setTaxRate(taxRate.getTaxRate());
                }else {
                  internalFeeLine.setTaxRate(subject.getTaxRate());
                }
                
                if (internalFeeLine.getTotal() != null && internalFeeLine.getTotal().getTotal() != null) {
                  internalFeeLine.getTotal().setTax(
                      BTaxCalculator.tax(internalFeeLine.getTotal().getTotal(), internalFeeLine.getTaxRate(), ep.getScale(),
                          ep.getRoundingMode()));
                }
              }
              RActionEvent.fire(LineEditGridGadget.this, ActionName.REFRESH, "");
              editGrid.refresh();
            }

            @Override
            public void onFailure(Throwable caught) {
              RMsgBox.showError(caught);
            }
          });

    }
  }

  private BSubject getSubject(String subjectUuid, List<BSubject> subjects) {
    if (StringUtil.isNullOrBlank(subjectUuid)) {
      return null;
    }
    for (BSubject subject : subjects) {
      if (subjectUuid.equals(subject.getSubject().getUuid())) {
        return subject;
      }
    }

    return null;
  }

  public static M M = GWT.create(M.class);

  public static interface M extends Messages {
    @DefaultMessage("含税金额")
    String total();

    @DefaultMessage("删除")
    String delete();

    @DefaultMessage("明细行")
    String detailLines();

    @DefaultMessage("请先选择需要{0}的{1}。")
    String seleteDataToAction(String action, String entityCaption);

    @DefaultMessage("账款明细：第{0}的含税金额不能为0。")
    String totalPatch(int i);

    @DefaultMessage("账款明细：第{0}行的{1}不能为空。")
    String isNull(int i, String caption);

    @DefaultMessage("账款明细：第{0}行与第{1}行重复。")
    String duplicateGift(int i, int j);

    @DefaultMessage("账单明细行不能为空。")
    String emptyLine();
  }

}
