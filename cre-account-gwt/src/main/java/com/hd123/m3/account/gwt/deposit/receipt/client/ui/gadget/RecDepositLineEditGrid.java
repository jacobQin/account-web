/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名： account-web-lease
 * 文件名： PayLineEditGrid.java
 * 模块说明：    
 * 修改历史：
 * 2013-10-14 - chenpeisi - 创建。
 */
package com.hd123.m3.account.gwt.deposit.receipt.client.ui.gadget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubject;
import com.hd123.m3.account.gwt.cpnts.client.biz.BSubjectType;
import com.hd123.m3.account.gwt.cpnts.client.ui.form.SubjectUCNBox;
import com.hd123.m3.account.gwt.deposit.commons.client.actionname.DepositActionName;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDeposit;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositLine;
import com.hd123.m3.account.gwt.deposit.commons.client.biz.BDepositTotal;
import com.hd123.m3.account.gwt.deposit.intf.client.DepositMessage;
import com.hd123.m3.account.gwt.deposit.intf.client.dd.PDepositLineDef;
import com.hd123.m3.account.gwt.deposit.receipt.client.EPRecDeposit;
import com.hd123.m3.account.gwt.deposit.receipt.client.rpc.RecDepositService;
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
import com.hd123.rumba.webframe.gwt.base.client.RBAsyncCallback2;

/**
 * @author chenpeisi
 * 
 */
public class RecDepositLineEditGrid extends RCaptionBox implements RActionHandler,
    HasRActionHandlers {
  private RAction importFromContractAction;
  private RAction addAction;
  private RAction insertAction;
  private RAction deleteAction;
  private RToolbarSplitButton addButton;
  private HTML totalNumber;

  private BDeposit entity;

  private EditGrid<BDepositLine> grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef remainTotalCol;
  private RGridColumnDef contractTotalCol;
  private RGridColumnDef unDepositTotalCol;
  private RGridColumnDef remarkCol;
  private Click_Handler clickHandler = new Click_Handler();
  private List<Message> messages = new ArrayList<Message>();

  private boolean checkEmpty;

  public RecDepositLineEditGrid() {
    super();
    setCaption(DepositMessage.M.receiptLine());
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

  public BDeposit getEntity() {
    return entity;
  }

  public void setEntity(BDeposit entity) {
    this.entity = entity;
    grid.setValues(entity.getLines());
    totalNumber.setHTML(DepositMessage.M.resultTotal(entity.getLines().size()));
    grid.refresh();
  }

  private void drawSelf() {
    drawGrid();
    RVerticalPanel root = new RVerticalPanel();
    root.setWidth("100%");
    root.add(grid);
    setContent(root);

    totalNumber = new HTML(DepositMessage.M.resultTotal(0));
    getCaptionBar().addButton(totalNumber);

    RPopupMenu addMenu = new RPopupMenu();

    addMenu = new RPopupMenu();
    importFromContractAction = new RAction(DepositMessage.M.importFromContract(), clickHandler);
    importFromContractAction.setHotKey(null);
    addMenu.addItem(new RMenuItem(importFromContractAction));
    addAction = new RAction(RActionFacade.CREATE, clickHandler);
    addAction.setCaption(DepositMessage.M.addLine());
    addAction.setHotKey(new RHotKey(false, false, false, RKey.KEY_CODE_INSERT));
    addMenu.addItem(new RMenuItem(addAction));
    insertAction = new RAction(DepositMessage.M.insertLine(), clickHandler);
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

  private void drawGrid() {
    grid = new EditGrid<BDepositLine>();
    grid.setWidth("100%");
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setRequired(true);
    grid.setCaption(DepositMessage.M.receiptLine());
    grid.addActionHandler(new RActionHandler() {

      @Override
      public void onAction(RActionEvent event) {
        if (EditGrid.ACTION_APPEND.equals(event.getActionName())
            || EditGrid.ACTION_REMOVE.equals(event.getActionName())) {
          totalNumber.setHTML(DepositMessage.M.resultTotal(entity == null ? 0 : entity.getLines()
              .size()));
        }
      }
    });

    GridDataProvider provider = new GridDataProvider();
    grid.setProvider(provider);
    grid.setDataProducer(provider);

    CellRendererFactory rendererFactory = new CellRendererFactory();

    grid.setHoverRow(true);
    grid.setShowRowSelector(true);
    grid.setShowCurrentRow(true);
    grid.setAllowHorizontalScrollBar(true);
    grid.setProvider(new GridDataProvider());

    lineNumberCol = new RGridColumnDef(PDepositLineDef.lineNumber);
    lineNumberCol.setWidth("30px");
    grid.addColumnDef(lineNumberCol);

    subjectCol = new RGridColumnDef(PDepositLineDef.subject);
    subjectCol.setRendererFactory(rendererFactory);
    subjectCol.setWidth("180px");
    grid.addColumnDef(subjectCol);

    totalCol = new RGridColumnDef(PDepositLineDef.amount);
    totalCol.setRendererFactory(rendererFactory);
    totalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    totalCol.setWidth("100px");
    grid.addColumnDef(totalCol);

    remainTotalCol = new RGridColumnDef(PDepositLineDef.remainTotal);
    remainTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    remainTotalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    remainTotalCol.setWidth("100px");
    grid.addColumnDef(remainTotalCol);

    contractTotalCol = new RGridColumnDef(DepositMessage.M.contractTotal());
    contractTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    contractTotalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    contractTotalCol.setWidth("100px");
    grid.addColumnDef(contractTotalCol);

    unDepositTotalCol = new RGridColumnDef(DepositMessage.M.unDepositTotal());
    unDepositTotalCol.setHorizontalAlign(HasHorizontalAlignment.ALIGN_RIGHT);
    unDepositTotalCol.setRendererFactory(new RNumberRendererFactory(M3Format.fmt_money));
    unDepositTotalCol.setWidth("100px");
    grid.addColumnDef(unDepositTotalCol);

    remarkCol = new RGridColumnDef(PDepositLineDef.remark);
    remarkCol.setRendererFactory(rendererFactory);
    remarkCol.setWidth("160px");
    grid.addColumnDef(remarkCol);

    grid.setAllColumnsOverflowEllipsis(true);
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
        if (event.getActionName() == DepositActionName.CHANGE_REMAINTOTAL) {
          doChangeRemainTotal();
          fetchLineExts();
        }
      }
    });
    CommandQueue.awake();
  }

  private void doImportFromContract() {
    // 基本信息
    if (entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null
        || entity.getCounterpart() == null || entity.getCounterpart().getUuid() == null) {
      RMsgBox.show(DepositMessage.M.fillBasicInfo());
      return;
    }
    // 先选择合同
    if (entity.getContract() == null || entity.getContract().getUuid() == null) {
      RMsgBox.show(DepositMessage.M.selectContractFirst());
      return;
    }

    RLoadingDialog.show();
    RecDepositService.Locator.getService().importFromContract(entity.getContract().getUuid(),
        entity.getAccountUnit().getUuid(), entity.getCounterpart().getUuid(),
        new RBAsyncCallback2<List<BDepositLine>>() {
          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.importFromContract(),
                EPRecDeposit.getInstance().getModuleCaption());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(List<BDepositLine> result) {
            RLoadingDialog.hide();
            if (result.isEmpty()) {
              RMsgBox.show(DepositMessage.M.noDepositTerm(entity.getContract().getCode()));
              return;
            }
            // 删除空行
            Set<String> subjectUuids = new HashSet<String>();
            for (int i = entity.getLines().size() - 1; i >= 0; i--) {
              BDepositLine line = entity.getLines().get(i);
              if (line.getSubject() == null || line.getSubject().getUuid() == null) {
                entity.getLines().remove(i);
              } else {
                subjectUuids.add(line.getSubject().getUuid());
              }
            }
            // 过滤重复科目，添加导入科目
            for (BDepositLine line : result) {
              if (subjectUuids.contains(line.getSubject().getUuid())) {
                continue;
              }
              entity.getLines().add(line);
            }
            grid.refresh();
            RActionEvent.fire(RecDepositLineEditGrid.this, DepositActionName.CHANGE_TOTAL);
          }
        });
  }

  private void doChangeRemainTotal() {
    for (BDepositLine line : entity.getLines()) {
      line.setRemainTotal(BigDecimal.ZERO);
      line.setContractTotal(BigDecimal.ZERO);
    }
  }

  private void fetchLineExts() {
    if (entity.getAccountUnit() == null || entity.getAccountUnit().getUuid() == null
        || entity.getCounterpart() == null || entity.getCounterpart().getUuid() == null
        || entity.getLines().isEmpty())
      return;

    Set<String> subjectUuids = new HashSet<String>();
    for (BDepositLine line : entity.getLines()) {
      if (line.getSubject() == null || line.getSubject().getUuid() == null)
        continue;
      subjectUuids.add(line.getSubject().getUuid());
    }

    if (subjectUuids.isEmpty())
      return;

    String contractUuid = null;
    if (entity.getContract() != null)
      contractUuid = entity.getContract().getUuid();

    RLoadingDialog.show();
    GWTUtil.enableSynchronousRPC();
    RecDepositService.Locator.getService().getAdvances(entity.getAccountUnit().getUuid(),
        entity.getCounterpart().getUuid(), subjectUuids, contractUuid,
        new RBAsyncCallback2<Map<String, BDepositTotal>>() {

          @Override
          public void onException(Throwable caught) {
            RLoadingDialog.hide();
            String msg = DepositMessage.M.actionFailed(DepositMessage.M.fetch(),
                DepositMessage.M.referenceInfo());
            RMsgBox.showError(msg, caught);
          }

          @Override
          public void onSuccess(Map<String, BDepositTotal> result) {
            RLoadingDialog.hide();
            for (BDepositLine line : entity.getLines()) {
              if (line.getSubject() == null || line.getSubject().getUuid() == null)
                continue;
              BDepositTotal value = result.get(line.getSubject().getUuid());
              line.setRemainTotal(value.getAdvanceTotal());
              if (value.getContractTotal() != null) {
                line.setContractTotal(value.getContractTotal());
                if (line.getTotal() == null || BigDecimal.ZERO.compareTo(line.getTotal()) == 0) {
                  line.setTotal(line.getUnDepositTotal());
                }
              }
            }
            grid.refresh();
          }
        });
  }

  private void fetchLineExt(final BDepositLine line, final int row) {
    if (line.getSubject() != null && entity.getCounterpart() != null
        && entity.getAccountUnit() != null && line.getSubject().getUuid() != null
        && entity.getCounterpart().getUuid() != null && entity.getAccountUnit().getUuid() != null) {
      CommandQueue.offer(new RPCCommand<BDepositTotal>() {
        @Override
        public void onCall(CommandQueue queue, AsyncCallback<BDepositTotal> callback) {
          RLoadingDialog.show();

          String contractUuid = null;
          if (entity.getContract() != null)
            contractUuid = entity.getContract().getUuid();

          RecDepositService.Locator.getService().getAdvance(entity.getAccountUnit().getUuid(),
              entity.getCounterpart().getUuid(), line.getSubject().getUuid(), contractUuid,
              callback);
        }

        @Override
        public void onFailure(CommandQueue queue, Throwable t) {
          RLoadingDialog.hide();
          String msg = DepositMessage.M.actionFailed(DepositMessage.M.fetch(),
              DepositMessage.M.referenceInfo());
          RMsgBox.showError(msg, t);
          queue.abort();
        }

        @Override
        public void onSuccess(CommandQueue queue, BDepositTotal result) {
          RLoadingDialog.hide();
          line.setRemainTotal(result.getAdvanceTotal());
          if (result.getContractTotal() != null) {
            line.setContractTotal(result.getContractTotal());
            if (line.getTotal() == null || BigDecimal.ZERO.compareTo(line.getTotal()) == 0) {
              line.setTotal(line.getUnDepositTotal());
            }
          }
          RActionEvent.fire(RecDepositLineEditGrid.this, DepositActionName.CHANGE_TOTAL);
          
          grid.refreshRow(row);
          queue.goon();
        }
      });
      CommandQueue.awake();
    } else {
    }
  }

  private class GridDataProvider implements RGridDataProvider, EditGridDataProduce<BDepositLine> {
    @Override
    public BDepositLine create() {
      BDepositLine detail = new BDepositLine();
      return detail;
    }

    @Override
    public int getRowCount() {
      return entity == null || entity.getLines() == null ? 0 : entity.getLines().size();
    }

    @Override
    public Object getData(int row, int col) {
      if (entity == null || entity.getLines().size() == 0)
        return null;
      if (col == lineNumberCol.getIndex())
        return row+1;
      if (col == subjectCol.getIndex())
        return entity.getLines().get(row).getSubject();
      if (col == totalCol.getIndex())
        return entity.getLines().get(row).getTotal() == null ? null : entity.getLines().get(row)
            .getTotal().doubleValue();
      if (col == remainTotalCol.getIndex())
        return entity.getLines().get(row).getRemainTotal() == null ? null : entity.getLines()
            .get(row).getRemainTotal().doubleValue();
      if (col == contractTotalCol.getIndex())
        return entity.getLines().get(row).getContractTotal() == null ? null : entity.getLines()
            .get(row).getContractTotal().doubleValue();
      if (col == unDepositTotalCol.getIndex())
        return entity.getLines().get(row).getUnDepositTotal() == null ? null : entity.getLines()
            .get(row).getUnDepositTotal().doubleValue();
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
      BDepositLine value = entity.getLines().get(row);
      if (fieldName.equals(subjectCol.getName())) {
        return new SubjectRenderer(grid, colDef, row, col, value);
      } else if (fieldName.equals(totalCol.getName())) {
        return new TotalRenderer(grid, colDef, row, col, data);
      } else if (fieldName.equals(remarkCol.getName())) {
        return new RemarkTextRenderer(grid, colDef, row, col, data);
      }
      return null;
    }
  }

  private class SubjectRenderer extends EditGridCellWidgetRenderer {
    private SubjectUCNBox field;

    public SubjectRenderer(EditGrid grid, RGridColumnDef colDef, int row, int col, Object data) {
      super(grid, colDef, row, col, data);
    }

    @Override
    protected Widget drawField(RGridColumnDef colDef, Object data) {
      field = new SubjectUCNBox(BSubjectType.predeposit.name(), new Integer(
          DirectionType.receipt.getDirectionValue()));
      field.setCaption(PDepositLineDef.constants.subject());
      field.getBrowser().addSelectionHandler(new SelectionHandler<BSubject>() {
        @Override
        public void onSelection(SelectionEvent<BSubject> event) {
          BSubject item = event.getSelectedItem();
          field.setRawValue(item);
          field.setValue(item.getSubject(), true);
        }
      });
      field.setEnterToTab(false);
      field.addKeyDownHandler(new DefaultFocusNextHandler());
      field.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          BDepositLine value = entity.getLines().get(getRow());
          value.setSubject(field.getValue());
          if (field.validate())
            fetchLineExt(value, getRow());
        }
      });
      field.setValidator(new RValidator() {
        @Override
        public Message validate(Widget sender, String value) {
          if (checkEmpty && StringUtil.isNullOrBlank(value)) {
            return Message.error(field.getCaption() + ":" + DepositMessage.M.notNull2(), field);
          }
          for (int i = 0; i < getRow(); i++) {
            BDepositLine detail = entity.getLines().get(i);
            if (checkEmpty&&detail != null && field.getValue() != null
                && StringUtil.isNullOrBlank(field.getValue().getUuid()) == false
                && field.getValue().equals(detail.getSubject())) {
              return Message.error(DepositMessage.M.duplicateSubject2(i + 1));
            }
          }
          return null;
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BDepositLine detail = entity.getLines().get(getRow());
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
      field.addChangeHandler(new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
          BDepositLine value = entity.getLines().get(getRow());
          value.setTotal(field.getValueAsBigDecimal());
          RActionEvent.fire(RecDepositLineEditGrid.this, DepositActionName.CHANGE_TOTAL);
        }
      });

      field.setValidator(new RValidator() {
        @Override
        public Message validate(Widget sender, String value) {
          if (checkEmpty && field.getValueAsBigDecimal()!=null&&field.getValueAsBigDecimal().compareTo(BigDecimal.ZERO) <= 0) {
            return Message.error(DepositMessage.M.min(field.getCaption()));
          }
          return null;
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BDepositLine detail = entity.getLines().get(getRow());
      field.setValue(detail.getTotal());
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
          BDepositLine value = entity.getLines().get(getRow());
          value.setRemark(field.getValue());
        }
      });
      return field;
    }

    @Override
    public void setValue(Object value) {
      BDepositLine detail = entity.getLines().get(getRow());
      field.setValue(detail.getRemark());
    }
  }

  private class Click_Handler implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      if (importFromContractAction == event.getSource()) {
        doImportFromContract();
      } else if (event.getSource() == addAction) {
        grid.appendValue(entity.getLines().size());
      } else if (event.getSource() == insertAction) {
        int index = grid.getCurrentRow() > 0 ? grid.getCurrentRow() : 0;
        grid.appendValue(index);
      } else if (event.getSource() == deleteAction) {
        grid.deleteSelections();
        grid.refresh();
        RActionEvent.fire(RecDepositLineEditGrid.this, DepositActionName.CHANGE_TOTAL);
      }
    }
  }

}
