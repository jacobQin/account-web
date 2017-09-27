/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	ShiMaoEmailWordCreator.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月10日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.email.creator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.service.subject.Subject;
import com.hd123.m3.account.service.subject.UsageType;
import com.hd123.m3.commons.biz.M3ServiceException;
import com.hd123.m3.cre.controller.account.statement.email.EmailConstants;
import com.hd123.m3.cre.controller.account.statement.email.word.Word2Pdf;
import com.hd123.m3.cre.controller.account.statement.email.word.WordTemplate;
import com.hd123.m3.cre.controller.account.statement.model.BStatement;
import com.hd123.m3.cre.controller.account.statement.model.BStatementDetail;
import com.hd123.m3.investment.service.tenant.tenant.Tenant;
import com.hd123.rumba.commons.biz.entity.UCN;

/**
 * @author mengyinkun
 * 
 */
public class ShiMaoEmailWordCreator {
  private static final ShiMaoEmailWordCreator instance = null;
  private WordTemplate template;
  private Word2Pdf word2Pdf = new Word2Pdf();

  public static ShiMaoEmailWordCreator getInstance() {
    if (instance == null) {
      return new ShiMaoEmailWordCreator();
    }
    return instance;
  }

  /**
   * 根据模板 再次生成word文档
   * 
   * @param billNumber
   * @throws Exception
   */
  public void exportDoc(Map<String, Subject> subjects, Tenant tenant, String positions,
      BStatement statement, String billNumber, String srcFile, String descFile, int tableIndex)
      throws Exception {

    Map<String, String> map = fillContentFromStatement(subjects, statement, tenant, positions);
    makeModel(tableIndex, srcFile, descFile, statement);

    InputStream is = new FileInputStream(descFile);
    template = new WordTemplate(is);
    template.replaceTag(map);
    File file = new File(descFile);
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(file);
      BufferedOutputStream bos = new BufferedOutputStream(out);
      template.write(bos);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 生成模板文件
   * 
   * @param descFile
   *          目标文件
   * @param srcFile
   *          源文件
   * @param rowCount
   *          明细行数
   * @throws Exception
   */
  private void makeModel(int tableIndex, String descFile, String srcFile, BStatement statement)
      throws Exception {
    InputStream is = ShiMaoEmailWordCreator.class.getClassLoader().getResourceAsStream(descFile);
    XWPFDocument document = new XWPFDocument(is);

    XWPFTable tb = document.getTables().get(tableIndex);
    XWPFTableRow oldRow = tb.getRow(3);
    int count = statement.getLines().size();
    if (count > 0) {
      for (int i = 0; i <= count - 1; i++) {
        XWPFTableRow newTableRow = tb.insertNewTableRow(2);
        newTableRow.addNewTableCell().setText("${" + EmailConstants.MODEL_SUBJECT + (i + 1) + "}");
        newTableRow.addNewTableCell().setText(
            "${" + EmailConstants.MODEL_WITHOUTTAX + (i + 1) + "}");
        newTableRow.addNewTableCell().setText("${" + EmailConstants.MODEL_TAX + (i + 1) + "}");
        newTableRow.addNewTableCell().setText("${" + EmailConstants.MODEL_TOTAL + (i + 1) + "}");
        newTableRow.addNewTableCell();
        newTableRow.addNewTableCell().setText("${" + EmailConstants.MODEL_FEEDATE + (i + 1) + "}");

      }
    }
    tb.getRows().add(3, oldRow);
    File file = new File("c://temp/statement.pdf");
    if (file.isFile() && file.exists()) {
      file.delete();
    }
    OutputStream os = new FileOutputStream(srcFile);
    document.write(os);
    os.close();
    is.close();
  }

  /**
   * 设置指定表格的字体大小与字体样式
   * 
   * @param srcFile
   * @param fontFamily
   * @param fontSize
   * @param tableIndex
   * @throws IOException
   */
  public void setFont(String srcFile, String fontFamily, int fontSize, int tableIndex)
      throws IOException {
    InputStream is = new FileInputStream(srcFile);
    XWPFDocument doc = new XWPFDocument(is);
    XWPFTable table = doc.getTables().get(tableIndex);
    for (int i = 0; i < table.getNumberOfRows(); i++) {
      XWPFTableRow row = table.getRow(i);
      List<XWPFTableCell> tableCells = row.getTableCells();
      for (XWPFTableCell xwpfTableCell : tableCells) {
        List<XWPFRun> runs = xwpfTableCell.getParagraphs().get(0).getRuns();
        for (XWPFRun xwpfRun : runs) {
          xwpfRun.setFontFamily(fontFamily);
          xwpfRun.setFontSize(fontSize);
        }
      }
    }

    OutputStream os = new FileOutputStream(srcFile);
    doc.write(os);
    is.close();
    os.close();
  }

  /**
   * 合并单元格
   * 
   * @param table
   * @param col
   * @param fromRow
   * @param toRow
   */
  public void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
    for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
      XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
      if (rowIndex == fromRow) {
        cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
      } else {
        cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
      }
    }
  }

  /**
   * 指定表格垂直对齐
   * 
   * @param tableIndex
   *          默认操作索引为1 的表格
   * @param srcFile
   * @throws Exception
   */
  public void setValign(int tableIndex, String srcFile) throws Exception {
    InputStream is = new FileInputStream(srcFile);
    XWPFDocument doc = new XWPFDocument(is);
    XWPFTable table = doc.getTables().get(tableIndex);

    cellValign(table, table.getNumberOfRows());

    table = doc.getTables().get(2);

    cellValign(table, table.getNumberOfRows() - 2);

    OutputStream os = new FileOutputStream(srcFile);
    doc.write(os);
    is.close();
    os.close();
  }

  /**
   * 将指定表格的指定行数对齐
   * 
   * @param table
   * @param NumberOfRows
   */
  private void cellValign(XWPFTable table, int NumberOfRows) {
    for (int i = 2; i < NumberOfRows; i++) {
      XWPFTableRow row = table.getRow(i);
      List<XWPFTableCell> tableCells = row.getTableCells();
      for (XWPFTableCell xwpfTableCell : tableCells) {
        CTTc ctTc = xwpfTableCell.getCTTc();
        CTTcPr ctTcPr = ctTc.isSetTcPr() ? ctTc.getTcPr() : ctTc.addNewTcPr();
        CTVerticalJc vJc = ctTcPr.isSetVAlign() ? ctTcPr.getVAlign() : ctTcPr.addNewVAlign();
        vJc.setVal(STVerticalJc.CENTER);

      }
    }

  }

  /**
   * 将数据填充到模板
   * 
   * @param source
   * @return
   */
  public Map<String, String> fillContentFromStatement(Map<String, Subject> subjects,
      BStatement source, Tenant tenant, String positions) {
    SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, String> descMap = new HashMap<String, String>();
    descMap.put("billnumber", source.getBillNumber());
    descMap.put("contract", source.getContract().getCode());
    descMap.put("settlement", source.getSettleNo());
    descMap.put("printDate", dateFormater.format(new Date()));
    descMap.put("tenantCode", source.getCounterpart().getCode());
    descMap.put("tenantName", source.getCounterpart().getName());
    descMap.put("storeName", source.getAccountUnit().getName());
    descMap.put("tenant", tenant.getShortName());
    descMap.put("monthTotal", source.getSaleTotal().toString());

    descMap.put("position", positions);
    List<BStatementDetail> details = source.getDetails();
    // 科目信息
    if (details != null && details.size() > 0) {
      fillDetails(source, descMap, details);
    }

    return descMap;
  }

  private void fillDetails(BStatement source, Map<String, String> descMap,
      List<BStatementDetail> details) {

    // 总金额
    BigDecimal totalMoney = new BigDecimal(0);

    SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");

    for (int i = 0; i <= details.size() - 1; i++) {
      BStatementDetail line = details.get(i);
      // 税额
      BigDecimal tax = line.getTax();
      // 价税总金额
      BigDecimal total = line.getTotal();

      // 科目
      UCN ucn = line.getSubject();

      if (line.getDirection() == Direction.PAYMENT) {
        total = total.negate();
        tax = tax.negate();
      }

      descMap.put("subject" + (i + 1), ucn.getName());
      descMap.put("tax" + (i + 1), tax.toString());
      descMap.put("total" + (i + 1), total.toString());
      descMap.put("withoutTax" + (i + 1), (total.subtract(tax)).toString());
      descMap.put("feeDate" + (i + 1), dateFormater.format(line.getBeginDate()) + "到"
          + dateFormater.format(line.getEndDate()));

      Date lastDate = line.getLastPayDate();
      if (lastDate == null) {
        descMap.put("lastDate" + (i + 1), "");
      } else {
        descMap.put("lastDate" + (i + 1), dateFormater.format(lastDate));
      }

      totalMoney = totalMoney.add(total);

    }
    descMap.put("total", totalMoney.toString());
  }

  /**
   * 根据情况合并单元格
   * 
   * @param srcFile
   * @throws Exception
   */
  private void collspanCell(String srcFile) throws Exception {
    InputStream is = new FileInputStream(srcFile);
    XWPFDocument doc = new XWPFDocument(is);
    List<String> datas = getMutiplyData(doc);

    XWPFTable table = doc.getTables().get(1);
    for (String string : datas) {
      int firstIndex = datas.indexOf(string);
      int lastIndex = datas.lastIndexOf(string);
      // 若存在重复的费用明细，则合并；同时将已结算金额合并
      if (lastIndex != firstIndex) {
        mergeCellsVertically(table, 0, firstIndex + 2, lastIndex + 2);
        mergeCellsVertically(table, 4, firstIndex + 2, lastIndex + 2);
      }

    }
    OutputStream os = new FileOutputStream(srcFile);
    doc.write(os);
    is.close();
    os.close();
  }

  /**
   * 拿到本月缴费明细的第一列单元格数据
   * 
   * @param doc
   * @return
   */
  public List<String> getMutiplyData(XWPFDocument doc) {

    XWPFTable tb = doc.getTables().get(1);
    List<String> datas = new ArrayList<String>();
    // 费用明细行下标起始于2，结束于总行数-2
    for (int i = 2; i < tb.getNumberOfRows() - 2; i++) {
      XWPFTableRow row = tb.getRow(i);
      XWPFTableCell tableCell = row.getTableCells().get(0);
      datas.add(tableCell.getText());
    }
    return datas;
  }

  /**
   * 填充已结算金额数据
   * 
   * @throws Exception
   */
  public void filleRceipted(Map<String, Subject> subjects, int tableIndex, BStatement statement,
      String srcFile, Map<String, BigDecimal> receiptedMap) throws Exception {
    InputStream is = new FileInputStream(srcFile);
    XWPFDocument doc = new XWPFDocument(is);
    XWPFTable table = doc.getTables().get(tableIndex);

    for (int i = 2; i < table.getNumberOfRows() - 2; i++) {
      XWPFTableRow row = table.getRow(i);
      XWPFTableCell cell = row.getCell(0);
      if (receiptedMap.containsKey(cell.getText())) {
        row.getCell(4).setText(receiptedMap.get(cell.getText()).toString());
      }
    }
    // 总 已收付金额
    BigDecimal totalReceipted = new BigDecimal(0);
    Set<Entry<String, BigDecimal>> entrySet = receiptedMap.entrySet();
    for (Entry<String, BigDecimal> entry : entrySet) {
      totalReceipted = totalReceipted.add(entry.getValue());
    }

    XWPFTableRow row = table.getRow(table.getNumberOfRows() - 2);
    // 设置总已收付金额
    row.getCell(3).setText(totalReceipted.toString());

    BigDecimal total = new BigDecimal(row.getCell(2).getText());

    row = table.getRow(table.getNumberOfRows() - 1);
    BigDecimal unReceiptedTotal = total.subtract(totalReceipted);
    // 设置总应收付金额
    row.getCell(1).setText(unReceiptedTotal.toString());
    // 银行资料表格
    XWPFTable bankTable = doc.getTables().get(2);
    // 设置应收金额 总应收金额-租金金额
    BigDecimal rentTotal = new BigDecimal(0);
    for (BStatementDetail bStatementDetail : statement.getDetails()) {
      UCN ucn = bStatementDetail.getSubject();
      if (subjects.containsKey(ucn.getUuid())
          && subjects.get(ucn.getUuid()).getUsages().contains(UsageType.fixedRent.name())) {
        BigDecimal receipted = receiptedMap.get(ucn.getName());
        rentTotal = rentTotal.add(bStatementDetail.getTotal().subtract(receipted));
      }
    }
    bankTable.getRow(2).getCell(0).setText("应收金额：" + unReceiptedTotal.subtract(rentTotal));
    bankTable.getRow(2).getCell(1).setText("应收金额：" + rentTotal);

    OutputStream os = new FileOutputStream(srcFile);
    doc.write(os);
    os.close();
    is.close();
  }

  /**
   * 操作word文档
   * 
   * @param subjects
   *          科目明细列表
   * @param tenant
   *          商户
   * @param positions
   *          铺位号
   * @param statement
   *          账单
   * @param billNumber
   *          账单号
   * @param modelFile
   *          模板文件
   * @param descFile
   *          目标文件
   * @param tableIndex
   *          word中表格的索引
   * @param receiptedMap
   *          已结算金额
   * @param descPdf
   *          生成的目标pdf 路径
   * @throws Exception
   */
  public void workWithDocument(Map<String, Subject> subjects, Tenant tenant, String positions,
      BStatement statement, String billNumber, String modelFile, String descFile, int tableIndex,
      Map<String, BigDecimal> receiptedMap, String descPdf) throws Exception {

    ClassLoader classLoader = ShiMaoEmailWordCreator.class.getClassLoader();

    InputStream in = classLoader.getResourceAsStream(modelFile);

    XWPFDocument doc = new XWPFDocument(in);
    if (tableIndex > doc.getTables().size()) {
      throw new M3ServiceException("模板文件中无该表格");
    }

    exportDoc(subjects, tenant, positions, statement, billNumber, modelFile, descFile, tableIndex);

    setFont(descFile, "Microsoft Yahei", 10, tableIndex);

    collspanCell(descFile);

    filleRceipted(subjects, tableIndex, statement, descFile, receiptedMap);

    setValign(tableIndex, descFile);

    boolean isConvertSuccess = word2Pdf.convert2PDF(descFile, descPdf);

    if (isConvertSuccess == false) {
      throw new M3ServiceException("word-->pdf转换失败");
    }
  }
}
