/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	ModelAccObjectBillConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月6日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.accobject.converter;

import java.util.List;

import com.hd123.m3.account.service.accobject.AccountingObjectBill;
import com.hd123.m3.account.service.accobject.AccountingObjectLine;
import com.hd123.m3.cre.controller.account.accobject.model.BAccObjectBillContractLine;
import com.hd123.m3.cre.controller.account.accobject.model.BAccObjectBillStoreLine;
import com.hd123.m3.cre.controller.account.accobject.model.BAccountingObjectBill;
import com.hd123.rumba.commons.biz.entity.UCN;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * BAccountingObjectBill -> AccountingObjectBill
 * 
 * @author LiBin
 *
 */
public class ModelAccObjectBillConverter implements
    Converter<BAccountingObjectBill, AccountingObjectBill> {

  private static ModelAccObjectBillConverter instance = null;

  public static ModelAccObjectBillConverter getInstance() {
    if (instance == null) {
      instance = new ModelAccObjectBillConverter();
    }
    return instance;
  }

  @Override
  public AccountingObjectBill convert(BAccountingObjectBill source) throws ConversionException {
    if (source == null) {
      return null;
    }

    AccountingObjectBill target = new AccountingObjectBill();
    target.inject(source);

    target.setStore(source.getStore());
    target.setRemark(source.getRemark());

    for (BAccObjectBillStoreLine line : source.getStoreLines()) {
      for (UCN subject : line.getSubjects()) {
        if (getStoreLineBySubject(target.getLines(), subject) != null) {
          continue;
        }
        AccountingObjectLine targetLine = new AccountingObjectLine();
        targetLine.setStore(source.getStore());
        targetLine.setAccObject(line.getAccObject());
        targetLine.setSubject(subject);

        target.getLines().add(targetLine);
      }
    }

    for (BAccObjectBillContractLine line : source.getContractLines()) {
      for (UCN subject : line.getSubjects()) {
        if (getContractLineBySubject(target.getLines(), line.getContract(), subject) != null) {
          continue;
        }
        AccountingObjectLine targetLine = new AccountingObjectLine();
        targetLine.setStore(source.getStore());
        targetLine.setContract(line.getContract());
        targetLine.setAccObject(line.getAccObject());
        targetLine.setSubject(subject);

        target.getLines().add(targetLine);
      }

    }

    return target;
  }

  /** 从集合中取出已存在的明细（按项目） */
  private AccountingObjectLine getStoreLineBySubject(List<AccountingObjectLine> lines, UCN subject) {
    for (AccountingObjectLine objLine : lines) {
      if (subject.equals(objLine.getSubject())) {
        return objLine;
      }
    }
    return null;
  }

  /** 从集合中取出已存在的明细（按合同） */
  private AccountingObjectLine getContractLineBySubject(List<AccountingObjectLine> lines,
      UCN contract, UCN subject) {
    for (AccountingObjectLine objLine : lines) {
      if(objLine.getContract() == null){
        continue;
      }
      if (subject.equals(objLine.getSubject()) && contract.equals(objLine.getContract())) {
        return objLine;
      }
    }
    return null;
  }

}
