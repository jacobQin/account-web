/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	m3-account-web-main
 * 文件名：	PaymentAccountLineComparator.java
 * 模块说明：	
 * 修改历史：
 * 2016年1月31日 - LiBin - 创建。
 */
package com.hd123.m3.account.gwt.payment.rec.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.hd123.m3.account.gwt.payment.commons.client.biz.BPaymentAccountLine;
import com.hd123.rumba.commons.gwt.mini.client.Assert;
import com.hd123.rumba.commons.lang.NormalComparator;
import com.hd123.rumba.gwt.base.client.PageSort.Order;

/**
 * 收付款单账款比较器
 * 
 * @author LiBin
 * 
 */
public class PaymentAccountLineComparator implements Comparator<BPaymentAccountLine>, Serializable {
  private static final long serialVersionUID = -6311452884426864655L;
  private List<Pair<ValueGetter, Comparator<Comparable>>> items = new ArrayList();

  private interface ValueGetter<T extends Comparable> {
    T getValue(BPaymentAccountLine line);
  }

  private static class SafeNormalComparator extends NormalComparator<Comparable> implements
      Serializable {
    private static final long serialVersionUID = -1395675944184641200L;
    boolean asc;

    public SafeNormalComparator(boolean asc) {
      super(asc);
      this.asc = asc;
    }

    @Override
    public int compare(Comparable o1, Comparable o2) {
      if (o1 == null) {
        return o2 == null ? 0 : asc ? -1 : 1;
      } else if (o2 == null) {
        return asc ? 1 : -1;
      } else {
        return super.compare(o1, o2);
      }
    }
  }

  /**
   * 根据查询排序定义创建品牌实体比较器。
   * 
   * @param orders
   *          查询排序定义，禁止传入null。
   * @throws IllegalArgumentException
   */
  public static PaymentAccountLineComparator create(List<Order> orders)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(orders, "orders");
    PaymentAccountLineComparator comparator = new PaymentAccountLineComparator();
    for (Order order : orders) {
      boolean asc = true;
      if (order.getDir() != null) {
        asc = order.getDir().isAsc();
      }
      if (BPaymentAccountLine.ORDER_BY_SOURCEBILL.equals(order.getFieldName())) {
        comparator.addField(new SourceBillNumberGetter(), new SafeNormalComparator(asc));
      } else if (BPaymentAccountLine.ORDER_BY_SUBJECT.equals(order.getFieldName())) {
        comparator.addField(new SubjectGetter(), new SafeNormalComparator(asc));
      } else if (BPaymentAccountLine.ORDER_BY_BEGINTIME.equals(order.getFieldName())) {
        comparator.addField(new BeginTimeGetter(), new SafeNormalComparator(asc));
      } else if (BPaymentAccountLine.ORDER_BY_ENDTIME.equals(order.getFieldName())) {
        comparator.addField(new EndTimeGetter(), new SafeNormalComparator(asc));
      }
    }
    return comparator;
  }

  @Override
  public int compare(BPaymentAccountLine o1, BPaymentAccountLine o2) {
    if (o1 == null) {
      return o2 == null ? 0 : -1;
    } else if (o2 == null) {
      return 1;
    }
    for (Pair<ValueGetter, Comparator<Comparable>> item : items) {
      Comparable value1 = item.getKey().getValue(o1);
      Comparable value2 = item.getKey().getValue(o2);
      int result = item.getValue().compare(value1, value2);
      if (result != 0) {
        return result;
      }
    }
    return 0;
  }

  private PaymentAccountLineComparator() {
    // Do Nothing
  }

  private void addField(ValueGetter valueGetter, Comparator<Comparable> valueComparator) {
    items.add(Pair.of(valueGetter, valueComparator));
  }

  private static class SubjectGetter implements ValueGetter<String> {

    @Override
    public String getValue(BPaymentAccountLine line) {
      return (line == null || line.getAcc1() == null) ? null : line.getAcc1().getSubject()
          .getCode();
    }
  }

  private static class SourceBillNumberGetter implements ValueGetter<String> {
    @Override
    public String getValue(BPaymentAccountLine line) {
      return (line == null || line.getAcc1() == null || line.getAcc1().getSourceBill() == null) ? null
          : line.getAcc1().getSourceBill().getBillNumber();
    }
  }

  private static class BeginTimeGetter implements ValueGetter<Long> {
    @Override
    public Long getValue(BPaymentAccountLine line) {
      return (line == null || line.getAcc1() == null || line.getAcc1().getBeginTime() == null) ? null
          : line.getAcc1().getBeginTime().getTime();
    }
  }

  private static class EndTimeGetter implements ValueGetter<Long> {
    @Override
    public Long getValue(BPaymentAccountLine line) {
      return (line == null || line.getAcc1() == null || line.getAcc1().getEndTime() == null) ? null
          : line.getAcc1().getEndTime().getTime();
    }
  }

}
