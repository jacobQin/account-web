/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	cre-portal
 * 文件名：	AccObjectBillModelConverter.java
 * 模块说明：	
 * 修改历史：
 * 2016年9月6日 - LiBin - 创建。
 */
package com.hd123.m3.cre.controller.account.accobject.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.hd123.m3.account.service.accobject.AccountingObjectBill;
import com.hd123.m3.account.service.accobject.AccountingObjectLine;
import com.hd123.m3.cre.controller.account.accobject.model.BAccObjectBillContractLine;
import com.hd123.m3.cre.controller.account.accobject.model.BAccObjectBillStoreLine;
import com.hd123.m3.cre.controller.account.accobject.model.BAccountingObjectBill;
import com.hd123.m3.cre.controller.account.common.util.CollectionUtil;
import com.hd123.rumba.commons.util.converter.ConversionException;
import com.hd123.rumba.commons.util.converter.Converter;

/**
 * AccountingObjectBill -> BAccountingObjectBill
 * 
 * @author LiBin
 *
 */
public class AccObjectBillModelConverter implements
    Converter<AccountingObjectBill, BAccountingObjectBill> {
  
  private static AccObjectBillModelConverter instance = null;
  
  public static AccObjectBillModelConverter getInstance(){
    if(instance == null){
      instance = new AccObjectBillModelConverter();
    }
    return instance;
  }
  
  @Override
  public BAccountingObjectBill convert(AccountingObjectBill source) throws ConversionException {
    if (source == null) {
      return null;
    }

    BAccountingObjectBill target = new BAccountingObjectBill();
    target.inject(source);

    target.setStore(source.getStore());
    target.setRemark(source.getRemark());

    List<AccountingObjectLine> storeLines = new ArrayList<AccountingObjectLine>();
    List<AccountingObjectLine> contractLines = new ArrayList<AccountingObjectLine>();

    for (AccountingObjectLine line : source.getLines()) {
      if (line.getContract() == null) {
        storeLines.add(line);
      } else {
        contractLines.add(line);
      }
    }

    // 按项目设置（按核算主体分组）
    Collection<List<AccountingObjectLine>> storeLineGroups = CollectionUtil.group(storeLines,
        new Comparator<AccountingObjectLine>() {
          @Override
          public int compare(AccountingObjectLine o1, AccountingObjectLine o2) {
            if (o1.getAccObject().equals(o2.getAccObject())) {
              return 0;
            }
            return 1;
          }
        });

    for (List<AccountingObjectLine> group : storeLineGroups) {
      BAccObjectBillStoreLine storeLine = new BAccObjectBillStoreLine();
      storeLine.setAccObject(group.get(0).getAccObject());
      for (AccountingObjectLine line : group) {
        storeLine.getSubjects().add(line.getSubject());
      }
      target.getStoreLines().add(storeLine);
    }

    // 按合同设置（按合同+核算主体分组）
    Collection<List<AccountingObjectLine>> contractLineGroups = CollectionUtil.group(contractLines,
        new Comparator<AccountingObjectLine>() {
          @Override
          public int compare(AccountingObjectLine o1, AccountingObjectLine o2) {
            if (o1.getContract().equals(o2.getContract())
                && o1.getAccObject().equals(o2.getAccObject())) {
              return 0;
            }
            return 1;
          }
        });

    for (List<AccountingObjectLine> group : contractLineGroups) {
      BAccObjectBillContractLine contractLine = new BAccObjectBillContractLine();
      contractLine.setAccObject(group.get(0).getAccObject());
      contractLine.setContract(group.get(0).getContract());
      for (AccountingObjectLine line : group) {
        contractLine.getSubjects().add(line.getSubject());
      }
      target.getContractLines().add(contractLine);
    }

    return target;
  }

}
