/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	Word2Pdf.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月7日 - mengyinkun - 创建。
 */
package com.hd123.m3.cre.controller.account.statement.email.word;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hd123.m3.commons.biz.M3ServiceException;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * @author mengyinkun
 * 
 */
public class Word2Pdf {
  private static final int wdFormatPDF = 17;
  Logger logger = LoggerFactory.getLogger(Word2Pdf.class);

  public Word2Pdf() {
  }

  public boolean convert2PDF(String inputFile, String pdfFile) throws M3ServiceException {
    String suffix = getFileSufix(inputFile);
    File file = new File(inputFile);
    if (!file.exists()) {
      throw new M3ServiceException("文件不存在");
    }
    if (suffix.equals("pdf")) {
      throw new M3ServiceException("pdf不需要转换");
    }
    if (suffix.equals("doc") || suffix.equals("docx") || suffix.equals("txt")) {
      return word2PDF(inputFile, pdfFile);
    } else {
      throw new M3ServiceException("文件格式不支持转换");
    }
  }

  public static String getFileSufix(String fileName) {
    int splitIndex = fileName.lastIndexOf(".");
    return fileName.substring(splitIndex + 1);
  }

  public boolean word2PDF(String inputFile, String pdfFile) {
    Dispatch doc = null;
    ActiveXComponent app = null;
    try {
      ComThread.InitSTA();
      // 打开word应用程序
      app = new ActiveXComponent("Word.Application");
      // 设置word不可见
      app.setProperty("Visible", false);
      // 获得word中所有打开的文档,返回Documents对象
      Dispatch docs = app.getProperty("Documents").toDispatch();
      // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
      logger.info("======================转换开始==================");
      // Dispatch doc = Dispatch.call(docs, "Open", inputFile, false,
      // true).toDispatch();

      doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[] {
          inputFile, new Variant(false), new Variant(false),// 是否只读
          new Variant(false), new Variant("pwd") }, new int[1]).toDispatch();
      Dispatch.call(doc, "ExportAsFixedFormat", pdfFile, wdFormatPDF // word保存为pdf格式宏，值为17
          );
      return true;
    } catch (Exception e) {
      logger.info("=====================转换失败==================");
      logger.error(e.getMessage(), e);
      return false;
    } finally {
      if (doc != null) {
        Dispatch.call(doc, "Close", false);
      }
      if (app != null) {
        app.invoke("Quit", 0);
      }
      ComThread.Release();
    }
  }
}
