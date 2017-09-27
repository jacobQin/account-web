/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	RecDepositRepaymentLineEditGrid.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-26 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.receipt.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.actionname.DepositRepaymentActionName;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLine;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.ui.form.AdvanceSubjectUCNBox;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.DepositRepaymentMessage;
import com.hd123.m3.account.gwt.depositrepayment.intf.client.dd.PDepositRepaymentLineDef;
import com.hd123.m3.account.gwt.depositrepayment.receipt.client.rpc.RecDepositRepaymentService;
import com.hd123.m3.commons.gwt.base.client.contants.BoundaryValue;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGrid;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridCellWidgetRenderer;
import com.hd123.m3.commons.gwt.widget.client.ui.grid2.EditGridDataProduce;
import com.hd123.rumba.gwt.base.client.util.StringUtil;
import com.hd123.rumba.gwt.util.client.GWTUtil;
import com.hd123.rumba.gwt.util.client.commandqueue.CommandQueue;
import com.hd123.rumba.gwt.util.client.commandqueue.LocalCommand;
import com.hd123.rumba.gwt.util.client.commandqueue.RPCCommand;
import com.hd123.rumba.gwt.util.client.message.Message;
import com.hd123.rumba.gwt.widget2.client.action.RAction;
import com.hd123.rumba.gwt.widget2.client.action.RActionFacade;
import com.hd123.rumba.gwt.widget2.client.dialog.RLoadingDialog;
import com.hd123.rumba.gwt.widget2.client.dialog.RMsgBox;
import com.hd123.rumba.gwt.widget2.client.form.RNumberBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBox;
import com.hd123.rumba.gwt.widget2.client.form.RTextBoxBase;
import com.hd123.rumba.gwt.widget2.client.form.RValidator;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
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
import com.hd123.rumba.webframe.gwt.base.client.i18n.GWTFormat;

/**
 * @author zhuhairui
 * 
 */
public class RecDepositRepaymentLineEditGrid extends RCaptionBox implements RActionHandler,
    HasRActionHandlers {

  private BDepositRepayment entity;
  private RAction addAction;
  private RAction insertAction;
  private RAction deleteAction;
  private RToolbarSplitButton addButton;
  private HTML totalNumber;

  private EditGrid<BDepositRepaymentLine> grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef remainTotalCol;
  private RGridColumnDef remarkCol;

  private Click_Handler clickHandler = new Click_Handler();
  private List<Message> messages = new ArrayList<Message>();

  private SubjectRenderer subjectRenderer;
  private TotalRenderer totalRenderer;

  private boolean checkEmpty;

  public RecDepositRepaymentLineEditGrid() {
    super();
    setCaption(DepositRepaymentMessage.M.repaymentLines());
    getCaptionBar().setShowCollapse(true);
    setEditing(true);
    setWidth("100%");
    drawSelf();
  }

  @Override
  public boolean validate() {
    messages.clear();
    checkEmpty = true;
    boolean isValid = grid.validate();
    checkEmpty = false;
    return isValid;
  }

  private void drawSelf() {
    drawGrid();
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.add(grid);
    setContent(root);

    totalNumber = new HTML(DepositRepaymentMessage.M.resultTotal(0));
    getCaptionBar().addButton(totalNumber);

    RPopupMenu addMenu = new RPopupMenu();

    addMenu = new RPopupMenu();
    addAction = new RAction(RActionFacade.CREATE, clickHandler);
    addAction.setCaption(DepositRepaymentMessage.M.addLine());
    addAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(addAction));
    insertAction = new RAction(DepositRepaymentMessage.M.insertLine(), clickHandler);
    insertAction.setHotKey(new RHotKey(true, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(insertAction));

    addButton = new RToolbarSplitButton(addAction);
    addButton.setMenu(addMenu);
    addButton.setShowText(false);
    getCaptionBar().addButton(addButton);

    deleteAction = new RAction(RActionFacade.DELETE, clickHandler);
    deleteAction.setHotKey(null);
    RToolbarButton deleteButton = new RToolbarButton(deleteAction);
    deleteButton.setShowText(false);
    getCaptionBar().addButton(deleteButton);

    getCaptionBar().addButton(new RToolbarSeparator());
  }

  public void setEntity(BDepositRepayment entity) {
    this.entity = entity;
    grid.setValues(entity.getLines());
    totalNumber.setHTML(DepositRepaymentMessage.M.resultTotal(entity.getLines().size()));
    grid.refresh();
  }

  private Widget drawGrid() {
    grid = new EditGrid<BDepositRepaymentLine>();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setRequired(true);
    grid.setCaption(DepositRepaymentMessage.M.repaymentLines());
    grid.addActionHandler(new RActionHandler() {

      @Override
      public void onAction(RActionEvent event) {
        if (EditGrid.ACTION_APPEND.equals(event.getActionName())
            || EditGrid.ACTION_REMOVE.equals(event.getActionName())) {
          totalNumber.setHTML(DepositRepaymentMessage.M.resultTotal(entity == null ? 0 : entity
              .getLines().size()));
        }
      }
    });
    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.setDataProducer(provider);

    CellRendererFactory rendererFactory = new CellRendererFactory();

    lineNumberCol = new RGridColumnDef(PDepositRepaymentLineDef.lineNumber);
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PDepositRepaymentLineDef.subject);
    subjectCol.setRendererFactory(rendererFactory);
    subjectCol.setWidth("180px");
    grid.addColumnDef(subjectCol);

    totalCol = new RGridColumnDef(PDepositRepaymentLineDef.amount);
    totalCol.setRendererFactory(rendererFactory);
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("120px");
    grid.addColumnDef(totalCol);

    remainTotalCol = new RGridColumnDef(DepositRepaymentMessage.M.accountBalance());
    remainTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    remainTotalCol.setRendererFactory(new RNumberRendererFactory(GWTFormat.fmt_money));
    remainTotalCol.setWidth("120px");
    grid.addColumnDef(remainTotalCol);

    remarkCol = new RGridColumnDef(PDepositRepaymentLineDef.remark);
    remarkCol.setRendererFactory(rendererFactory);
    remarkCol.setWidth("160px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
    return grid;
  }

  @Override
  public HandlerRegistration addActionHandler(RActionHandler handler) {
    return addHandler(handler, RActionEvent.getType());
  }

  @Override
  public void onAction(final RActionEvent event) {
    GWTUtil.blurActiveElement();
    CommandQueue.offer(new LocalCommand() {

      @Override
      public void onCall(CommandQueue queue) {
        queue.goon();
        if (event.getActionName() == DepositRepaymentActionName.CHANGE_REMAINTOTAL) {
          doChangeRemainTotal();
        } else if (event.getActionName() == DepositRepaymentActionName.REFRESH) {
          grid.refresh();
        } else {
          grid.refresh();
          if (event.getActionName() == DepositRepaymentActionName.CHANGE_ACCOUNTUNIT) {
            String accountUnitUuid = (String) event.getParameters().get(0);
            for (int i = 0; i < entity.getLines().size(); i++) {
              Widget widget = grid.getWidget(i, 2);
              if (widget instanceof SubjectRenderer) {
                ((SubjectRenderer) widget).field.setAccountUnitUuid(accountUnitUuid);
              }
            }
          } else if (event.getActionName() == DepositRepaymentActionName.CHANGE_COUNTERPART) {
            String counterpartUuid = (String) event.getParameters().get(0);
            for (int i = 0; i < entity.getLines().size(); i++) {
              Widget widget = grid.getWidget(i, 2);
              if (widget instanceof SubjectRenderer) {
                ((SubjectRenderer) widget).field.setCounterpartUuid(counterpartUuid);
              }
            }
          } else if (event.getActionName() == DepositRepaymentActionName.CHANGE_CONTRACT) {
            String contractUuid = (String) event.getParameters().get(0);
            for (int i = 0; i < entity.getLines().size(); i++) {
              Widget widget = grid.getWidget(i, 2);
              if (widget instanceof SubjectRenderer) {
                ((SubjectRenderer) widget).field.setContractUuid(contractUuid);
              }
            }
          } else if (event.getActionName() == DepositRepaymentActionName.CHANGE_DEPOSIT) {
            String depositUuid = (String) event.getParameters().get(0);
            Boolean isEnableByDeposit = (Boolean) event.getParameters().get(1);
            boolean readOnly = false;
            if (isEnableByDeposit) {
              if (StringUtil.isNullOrBlank(depositUuid)) {
                grid.setShowRowSelector(true);
                getCaptionBar().getToolbar().setVisible(true);
                readOnly = false;
              } else {
                grid.setShowRowSelector(false);
                getCaptionBar().getToolbar().setVisible(false);
                readOnly = true;
                List<BDepositRepaymentLine> lines = entity.getLines();
                for (BDepositRepaymentLine line : lines) {
                  fetchLineExt(line);
                }
              }
            } else {
              grid.setShowRowSelector(true);
              getCaptionBar().getToolbar().setVisible(true);
              readOnly = false;
            }
            grid.refresh();

            int col = !readOnly ? 2 : 1;
            for (int i = 0; i < entity.getLines().size(); i++) {
              Widget subjectWidget = grid.getWidget(i, col);
              if (subjectWidget instanceof SubjectRenderer) {
                ((SubjectRenderer) subjectWidget).getField().getCodeBox().setReadOnly(readOnly);
              }
              Widget totalWidget = grid.getWidget(i, col + 1);
              if (totalWidget instanceof TotalRenderer) {
                ((TotalRenderer) totalWidget).getField().setReadOnly(readOnly);
              }
            }
          }
        }
      }
    });
    CommandQueue.awake();
  }

  private void doChangeRemainTotal() {
    for (BDepositRepaymentLine line : entity.getLines()) {
      line.setRemainAmount(BigDecimal.ZERO);
    }
  }

  private void fetchLineExt(final BDepositRepaymentLine line) {
    if (line.getSubject() != null && entity.getCounterpart() != null
        && line.getSubject().getUuid() != null && entity.getCounterpart().getUuid() != null
        && entity.getAccountUnit() != null && entity.getAccountUnit().getUuid() != null) {
      CommandQueue.offer(new RPCCommand<BigDecimal>() {
        @Override
        public void onCall(CommandQueue queue, AsyncCallback<BigDecimal> callback) {
          RLoadingDialog.show();
          String billUuid = entity.getContract() == null ? "-" : StringUtil.isNullOrBlank(entity
              .getContract().getUuid()) ? "-" : entity.getContract().getUuid();
          RecDepositRepaymentService.Locator.getService().getAdvance(
              entity.getAccountUnit().getUuid(), entity.getCounterpart().getUuid(),
              line.getSubject().getUuid(), billUuid, callback);
        }

        @Override
        public void onFailure(CommandQueue queue, Throwable t) {
          RLoadingDialog.hide();
          String msg = "查找过程中发生错误。";
          RMsgBox.showError(msg, t);
          queue.abort();
        }

        @Override
        public void onSuccess(CommandQueue queue, BigDecimal result) {
          RLoadingDialog.hide();
          line.setRemainAmount(result);
          grid.refresh();
          queue.goon();
        }
      });
      CommandQueue.awake();
    }
  }

  private class GridDataProvider implements RGridDataProvider,
      EditGridDataProduce<BDepositRepaymentLine> {
    @Override
    public int getRowCount() {
      return entity == null ? 0 : entity.getLines().size();
    }

    @Override
    public BDepositRepaymentLine create() {
      BDepositRepaymentLine detail = new BDepositRepaymentLine();
      return detail;
    }

    @Override
    public Object getData(int row, int col) {
      if (entity == null || entity.getLines().size() == 0)
        return null;
      if (col == lineNumberCol.getIndex())
        return row + 1;
      if (col == subjectCol.getIndex())
        return entity.getLines().get(row).getSubject();
      if (col == totalCol.getIndex())
        return entity.getLines().get(row).getAmount() == null ? null : entity.getLines().get(row)
            .getAmount().doubleValue();
      if (col == remainTotalCol.getIndex())
        return entity.getLines().get(row).getRemainAmount() == null ? null : entity.getLines()
            .get(row).getRemainAmount().doubleValue();
      if (col == remarkCol.getIndex())
        return entity.getLines().get(row).getRemark();
      return null;
    }
  }

  private class CellRendererFactory implements RCellRendererFactory {

    @Override
    public RCellRenderer makeRenderer(RGrid rGrid, RGridColumnDef colDef, int row, int col,
        Object data) {
      String fieldName = colDef.getName();
      BDepositRepaymentLine value = entity.getLines().get(row);
      if (fieldName.equals(subjectCol.getName())) {
        subjectRenderer = new SubjectRenderer(grid, colDef, row, col, value);
        return subjectRenderer;
      } else if (fieldName.equals(totalCol.getName())) {
        totalRenderer = new TotalRenderer(grid, colDef, row, col, data);
        return totalRenderer;
      } else if (fieldName.equals(remarkCol.getName())) {
        return new RemarkTextRenderer(grid, colDef, row, col, data);
      }
      return null;
    }
  }

  private class SubjectRenderer extends EditGridCellWidgetRenderer {
    private AdvanceSubjectUCNBox field;

    public SubjectRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new AdvanceSubjectUCNBox(PDepositRepaymentLineDef.constants.subject());
      field.setContractUuid(entity != null ? (entity.getContract() != null ? entity.getContract()
          .getUuid() : null) : null);
      field.setCounterpartUuid(entity != null ? (entity.getCounterpart() != null ? entity
          .getCounterpart().getUuid() : null) : null);
      field.setAccountUnitUuid(entity != null ? (entity.getAccountUnit() != null ? entity
          .getAccountUnit().getUuid() : null) : null);
      field.setDirection(DirectionType.receipt.getDirectionValue());
      field.setEnterToTab(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          BDepositRepaymentLine value = entity.getLines().get(getRow());
          value.setSubject(field.getValue());
          if (field.validate())
            fetchLineExt(value);
        }
      });
      field.setValidator(new RValidator() {
        @Override
        public Message validate(Widget sender, String value) {
          if (checkEmpty && StringUtil.isNullOrBlank(value)) {
            return Message.error(field.getCaption() + ":" + DepositRepaymentMessage.M.notNull2(),
                field);
          }
          for (int i = 0; i < getRow(); i++) {
            BDepositRepaymentLine detail = entity.getLines().get(i);
            if (checkEmpty && detail != null && field.getValue() != null
                && StringUtil.isNullOrBlank(field.getValue().getUuid()) == false
                && field.getValue().equals(detail.getSubject())) {
              return Message.error(DepositRepaymentMessage.M.duplicateSubject2(i + 1));
            }
          }
          return null;
        }
      });
      return field;
    }

    public AdvanceSubjectUCNBox getField() {
      return field;
    }

    @Override
    public void setValue(Object value) {
      BDepositRepaymentLine detail = entity.getLines().get(getRow());
      field.setValue(detail.getSubject());
    }
  }

  private class TotalRenderer extends EditGridCellWidgetRenderer {
    private RNumberBox<BigDecimal> field;

    public TotalRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new RNumberBox<BigDecimal>(colDef.getFieldDef());
      field.setSelectAllOnFocus(true);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.setScale(2);
      field.setTextAlignment(RTextBoxBase.ALIGN_RIGHT);
      field.setMaxValue(BoundaryValue.BIGDECIMAL_MAXVALUE_S2);
      field.setFormat(M3Format.fmt_money);
      field.addValueChangeHandler(new ValueChangeHandler<BigDecimal>() {

        @Override
        public void onValueChange(ValueChangeEvent<BigDecimal> event) {

          BDepositRepaymentLine value = entity.getLines().get(getRow());
          value.setAmount(field.getValueAsBigDecimal() == null ? BigDecimal.ZERO : field
              .getValueAsBigDecimal());
          grid.refreshRow(getRow());
          RActionEvent.fire(RecDepositRepaymentLineEditGrid.this,
              DepositRepaymentActionName.CHANGE_REPAYMENTTOTAL);
        }
      });

      field.setValidator(new RValidator() {
        @Override
        public Message validate(Widget sender, String value) {
          BDepositRepaymentLine line = entity.getLines().get(getRow());
          if (checkEmpty && field.getValueAsBigDecimal() != null
              && field.getValueAsBigDecimal().compareTo(BigDecimal.ZERO) <= 0) {
            return Message.error(DepositRepaymentMessage.M.min(field.getCaption()));
          }
          if (field.isValid() && field.getValueAsBigDecimal() != null
              && field.getValueAsBigDecimal().compareTo(line.getRemainAmount()) > 0) {
            return Message.error(DepositRepaymentMessage.M.max(
                PDepositRepaymentLineDef.constants.amount(),
                DepositRepaymentMessage.M.accountBalance()));
          }
          return null;
        }
      });
      return field;
    }

    public RNumberBox<BigDecimal> getField() {
      return field;
    }

    @Override
    public void setValue(Object value) {
      BDepositRepaymentLine detail = entity.getLines().get(getRow());
      field.setValue(detail.getAmount());
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
          BDepositRepaymentLine value = entity.getLines().get(getRow());
          value.setRemark(field.getValue());
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BDepositRepaymentLine detail = entity.getLines().get(getRow());
      field.setValue(detail.getRemark());
    }
  }

  private class Click_Handler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (event.getSource() == addAction) {
        grid.appendValue(entity.getLines().size());
      } else if (event.getSource() == insertAction) {
        int index = grid.getCurrentRow() > 0 ? grid.getCurrentRow() : 0;
        grid.appendValue(index);
      } else if (event.getSource() == deleteAction) {
        grid.deleteSelections();
        grid.refresh();
        RActionEvent.fire(RecDepositRepaymentLineEditGrid.this,
            DepositRepaymentActionName.CHANGE_REPAYMENTTOTAL);
      }
    }
  }

}
