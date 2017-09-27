/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizDepositRepaymentConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014-2-21 - zhuhairui - 创建。
 */
package com.hd123.m3.account.gwt.depositrepayment.payment.server.converter;

import java.util.ArrayList;
import java.util.List;

import com.hd123.m3.account.commons.Direction;
import com.hd123.m3.account.gwt.commons.server.BizAccStandardBillConverter;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepayment;
import com.hd123.m3.account.gwt.depositrepayment.commons.client.biz.BDepositRepaymentLine;
import com.hd123.m3.account.service.depositrepayment.DepositRepayment;
import com.hd123.m3.account.service.depositrepayment.DepositRepaymentLine;
import com.hd123.m3.commons.gwt.util.server.converter.BizUCNConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * @author zhuhairui
 * 
 */
public class BizPayDepositRepaymentConverter extends
    BizAccStandardBillConverter<BDepositRepayment, DepositRepayment> {

  private static BizPayDepositRepaymentConverter instance = null;

  public static BizPayDepositRepaymentConverter getInstance() {
    if (instance == null)
      instance = new BizPayDepositRepaymentConverter();
    return instance;
  }

  @Override
  public DepositRepayment convert(BDepositRepayment source) throws ConversionException {
    if (source == null)
      return null;
    DepositRepayment target = new DepositRepayment();
    super.inject(source, target);
    target.setRemark(source.getRemark());
    target.setVersion(source.getVersion());
    target.setDirection(Direction.PAYMENT);
    target.setAccountUnit(BizUCNConverter.getInstance().convert(source.getAccountUnit()));
    target.setCounterpart(BizUCNConverter.getInstance().convert(source.getCounterpart()));
    target.setCounterpartType(source.getCounterpart() == null ? null : source.getCounterpart()
        .getCounterpartType());
    target.setDealer(source.getDealer() == null ? null : source.getDealer().getUuid());
    target.setCounterContact(source.getCounterContact());
    target.setRepaymentDate(source.getRepaymentDate());
    target.setRepaymentTotal(source.getRepaymentTotal());
    target.setPaymentType(BizUCNConverter.getInstance().convert(source.getPaymentType()));
    target.setContract(BizUCNConverter.getInstance().convert(source.getContract()));
    if (source.getBank() != null) {
      target.setBankAccount(source.getBank().getBankAccount());
      target.setBankCode(source.getBank().getCode());
      target.setBankName(source.getBank().getName());
    } else {
      target.setBankAccount(null);
      target.setBankCode(null);
      target.setBankName(null);
    }

    List<DepositRepaymentLine> lines = new ArrayList<DepositRepaymentLine>();
    for (BDepositRepaymentLine line : source.getLines()) {
      if (line.getSubject() == null || line.getSubject().getUuid() == null)
        continue;
      lines.add(BizPayDepositRepaymentLineConverter.getInstance().convert(line));
    }
    target.setLines(lines);

    return target;
  }
}
