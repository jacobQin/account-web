/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	account-web-lease
 * 文件名：	StatementSubjectViewGridBox.java
 * 模块说明：	
 * 修改历史：
 * 2013-10-28 - huangjunxian- 创建。
 */
package com.hd123.m3.account.gwt.statement.client.ui.gadget;

import java.math.BigDecimal;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.hd123.m3.account.gwt.commons.client.biz.DirectionType;
import com.hd123.m3.account.gwt.statement.client.EPStatement;
import com.hd123.m3.account.gwt.statement.client.StatementMessages;
import com.hd123.m3.account.gwt.statement.client.biz.BStatement;
import com.hd123.m3.account.gwt.statement.client.biz.BStatementSumLine;
import com.hd123.m3.account.gwt.statement.intf.client.StatementUrlParams;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementLineDef;
import com.hd123.m3.account.gwt.statement.intf.client.dd.PStatementSubjectAccountDef;
import com.hd123.m3.commons.gwt.base.client.format.M3Format;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchBox;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchEvent;
import com.hd123.m3.commons.gwt.widget.client.ui.search.RSearchHandler;
import com.hd123.rumba.gwt.widget2.client.form.RHTMLField;
import com.hd123.rumba.gwt.widget2.client.grid.RGrid;
import com.hd123.rumba.gwt.widget2.client.grid.RGridCellInfo;
import com.hd123.rumba.gwt.widget2.client.grid.RGridColumnDef;
import com.hd123.rumba.gwt.widget2.client.grid.RGridDataProvider;
import com.hd123.rumba.gwt.widget2.client.grid.renderer.RHyperlinkFieldRendererFactory;
import com.hd123.rumba.gwt.widget2.client.panel.RCaptionBox;
import com.hd123.rumba.gwt.widget2e.client.util.RTextStyles;
import com.hd123.rumba.webframe.gwt.entrypoint.client.jump.JumpParameters;

/**
 * 账款信息查看Box
 * 
 * @author huangjunxian
 * 
 */
public class StatementAccInfoViewBox extends RCaptionBox {

  public StatementAccInfoViewBox() {
    super();
    drawSelf();
  }

  private BStatement entity;

  private RSearchBox searchBox;
  private RHTMLField htmlField;
  private RGrid grid;
  private RGridColumnDef lineNumberCol;
  private RGridColumnDef subjectCodeCol;
  private RGridColumnDef subjectNameCol;
  private RGridColumnDef directionCol;
  private RGridColumnDef totalCol;
  private RGridColumnDef owedAmountCol;
  private RGridColumnDef needInvoiceCol;
  private RGridColumnDef settledCol;
  private RGridColumnDef invoicedCol;

  private void drawSelf() {
    setContentSpacing(0);
    setCaption(StatementMessages.M.accountInfo());
    getCaptionBar().setShowCollapse(true);
    setWidth("100%");

    drawGrid();
    setContent(grid);

    searchBox = new RSearchBox();
    searchBox.setEmptyText(StatementMessages.M.search_subject());
    searchBox.setShowBack(false);
    searchBox.addSearchHandler(new Handler_search());
    getCaptionBar().addButton(searchBox);

    HTML spaceField = new HTML("");
    spaceField.setWidth("20px");
    getCaptionBar().addButton(spaceField);
    htmlField = new RHTMLField();
    htmlField.addTextStyleName(RTextStyles.STYLE_RED);
    htmlField.addTextStyleName(RTextStyles.STYLE_BOLD);
    htmlField.setText(StatementMessages.M.exists_desActiveAcc());
    getCaptionBar().addButton(htmlField);
  }

  private void drawGrid() {
    grid = new RGrid();
    grid.setWidth("100%");
    grid.setShowRowSelector(false);
    grid.setProvider(new GridDataProvider());
    grid.addClickHandler(new Handler_grid());

    lineNumberCol = new RGridColumnDef(StatementMessages.M.lineNumber());
    lineNumberCol.setWidth("40px");
    lineNumberCol.setResizable(true);
    grid.addColumnDef(lineNumberCol);

    subjectCodeCol = new RGridColumnDef(PStatementLineDef.acc1_subject_code);
    subjectCodeCol.setRendererFactory(new RHyperlinkFieldRendererFactory());
    subjectCodeCol.setWidth("140px");
    subjectCodeCol.setResizable(true);
    grid.addColumnDef(subjectCodeCol);

    subjectNameCol = new RGridColumnDef(PStatementLineDef.acc1_subject_name);
    subjectNameCol.setWidth("160px");
    subjectNameCol.setResizable(true);
    grid.addColumnDef(subjectNameCol);

    directionCol = new RGridColumnDef(PStatementLineDef.acc1_direction);
    directionCol.setWidth("120px");
    directionCol.setResizable(true);
    grid.addColumnDef(directionCol);

    totalCol = new RGridColumnDef(PStatementLineDef.total_total);
    totalCol.setWidth("120px");
    totalCol.setResizable(true);
    totalCol.setHorizontalAlign(HasAlignment.ALIGN_RIGHT);
    grid.addColumnDef(totalCol);

    needInvoiceCol = new RGridColumnDef(PStatementSubjectAccountDef.needInvoice_total);
    needInvoiceCol.setWidth("120px");
    needInvoiceCol.setResizable(true);
    needInvoiceCol.setHorizontalAlign(HasAlignment.ALIGN_RIGHT);
    grid.addColumnDef(needInvoiceCol);

    settledCol = new RGridColumnDef(PStatementSubjectAccountDef.settled_total);
    settledCol.setWidth("120px");
    settledCol.setResizable(true);
    settledCol.setHorizontalAlign(HasAlignment.ALIGN_RIGHT);
    grid.addColumnDef(settledCol);

    invoicedCol = new RGridColumnDef(PStatementSubjectAccountDef.invoiced_total);
    invoicedCol.setHorizontalAlign(HasAlignment.ALIGN_RIGHT);
    invoicedCol.setWidth("120px");
    invoicedCol.setResizable(true);
    grid.addColumnDef(invoicedCol);
    
    owedAmountCol = new RGridColumnDef(PStatementLineDef.OWED_AMOUNT);
    owedAmountCol.setResizable(true);
    owedAmountCol.setHorizontalAlign(HasAlignment.ALIGN_RIGHT);
    grid.addColumnDef(owedAmountCol);

    grid.setAllColumnsOverflowEllipsis(true);
  }

  private String buildTotalStr(BigDecimal value) {
    if (value == null)
      return M3Format.fmt_money.format(0);
    return M3Format.fmt_money.format(value.doubleValue());
  }

  private class GridDataProvider implements RGridDataProvider {
    public Object getData(int row, int col) {
      if (entity == null)
        return null;
      BStatementSumLine line = entity.getSumLines().get(row);
      if (line == null)
        return null;
      if (col == lineNumberCol.getIndex())
        return row + 1;
      else if (col == subjectCodeCol.getIndex())
        return line.getSubjectCode();
      else if (col == subjectNameCol.getIndex())
        return line.getSubjectName();
      else if (col == directionCol.getIndex())
        return DirectionType.getCaptionByValue(line.getDirection());
      else if (col == totalCol.getIndex())
        return line.getTotal() == null ? null : buildTotalStr(line.getTotal().getTotal());
      else if (col == owedAmountCol.getIndex())
        return buildOwedAmountValueStr(line);
      else if (col == needInvoiceCol.getIndex())
        return buildTotalStr(line.getNeedInvoice());
      else if (col == settledCol.getIndex())
        return buildTotalStr(line.getSettled());
      else if (col == invoicedCol.getIndex())
        return buildTotalStr(line.getInvoiced());
      else
        return null;
    }
    
    private String buildOwedAmountValueStr(BStatementSumLine line){
      if(line.getTotal()==null || line.getTotal().getTotal()==null){
        return buildTotalStr(line.getSettled());
      }
      
      if(line.getSettled() == null){
         return buildTotalStr(line.getTotal().getTotal());
      }
      return buildTotalStr(line.getTotal().getTotal().subtract(line.getSettled()));
    }

    public int getRowCount() {
      if (entity == null)
        return 0;
      else
        return entity.getSumLines().size();
    }
  }

  public void setValue(BStatement entity) {
    this.entity = entity;
    htmlField.setVisible(entity.isShowDesLineInfo());
    grid.refresh();
  }

  private class Handler_grid implements ClickHandler {

    @Override
    public void onClick(ClickEvent event) {
      RGridCellInfo cell = grid.getCellForEvent(event.getNativeEvent());
      if (cell == null)
        return;

      BStatementSumLine line = entity.getSumLines().get(cell.getRow());
      if (line == null)
        return;

      // 本模块内的跳转
      if (cell.getColumnDef().equals(subjectCodeCol)) {
        JumpParameters params = new JumpParameters(StatementUrlParams.AccView.START_NODE);
        params.getUrlRef().set(StatementUrlParams.AccView.PN_UUID, entity.getUuid());
        params.getUrlRef().set(StatementUrlParams.AccView.KEY_SUBJECT_UUID, line.getSubjectUuid());
        params.getUrlRef().set(StatementUrlParams.AccView.KEY_SUBJECT_CODE, line.getSubjectCode());
        params.getUrlRef().set(StatementUrlParams.AccView.KEY_SUBJECT_NAME, line.getSubjectName());
        EPStatement.getInstance().jump(params);
      }
    }
  }

  private class Handler_search implements RSearchHandler {

    @Override
    public void onSearch(RSearchEvent event) {
      JumpParameters params = new JumpParameters(StatementUrlParams.AccView.START_NODE);
      params.getUrlRef().set(StatementUrlParams.AccView.PN_UUID, entity.getUuid());
      params.getUrlRef().set(StatementUrlParams.AccView.KEY_SUBJECT_CODE, searchBox.getKeyword());
      EPStatement.getInstance().jump(params);
    }

  }
}
