/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	m3-investment-web-w
 * 文件名：	BPMServerUtil.java
 * 模块说明：	
 * 修改历史：
 * 2014-1-20 - chenrizhang- 创建。
 */
package com.hd123.m3.account.gwt.util.server;

import java.util.HashMap;
import java.util.Map;

import com.hd123.bpm.service.BpmOperateContext;
import com.hd123.bpm.widget.interaction.client.biz.BVariableValue;
import com.hd123.rumba.commons.biz.entity.IsOperator;
import com.hd123.rumba.commons.biz.entity.OperateContext;
import com.hd123.rumba.commons.codec.CodecUtils;
import com.hd123.rumba.commons.codec.DecodingException;

/**
 * @author chenrizhang
 * 
 */
public class BPMServerUtil {

  public static BpmOperateContext createBPMOperCtx(IsOperator operator, String comment) {
    BpmOperateContext bpmOperCtx = new BpmOperateContext();
    bpmOperCtx.setOperator(operator);
    bpmOperCtx.setComment(comment);
    return bpmOperCtx;
  }

  public static BpmOperateContext createBPMOperCtx(OperateContext operCtx, String comment) {
    BpmOperateContext bpmOperCtx = new BpmOperateContext();
    bpmOperCtx.setOperator(operCtx.getOperator());
    bpmOperCtx.setTime(operCtx.getTime());
    bpmOperCtx.setComment(comment);
    return bpmOperCtx;
  }

  public static Map convertBVariableValue(Map<String, BVariableValue> variables)
      throws DecodingException, IllegalArgumentException, ClassNotFoundException {
    Map<String, Object> result = new HashMap<String, Object>();
    for (String key : variables.keySet()) {
      result.put(
          key,
          CodecUtils.decode(variables.get(key).getValue(),
              Class.forName(variables.get(key).getType())));
    }
    return result;
  }
}
