/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	account-ls-web-lease
 * 文件名：	BizAttachmentConverter.java
 * 模块说明：	
 * 修改历史：
 * 2014年11月6日 - zhr - 创建。
 */
package com.hd123.m3.account.gwt.statement.server.converter;

import com.hd123.m3.account.gwt.statement.client.biz.BAttachment;
import com.hd123.m3.commons.biz.attach.Attachment;
import com.hd123.m3.commons.gwt.util.server.converter.BizOperateInfoConverter;
import com.hd123.rumba.commons.gwt.entity.server.EntityConverter;
import com.hd123.rumba.commons.util.converter.ConversionException;

/**
 * 附件转换器
 * <p>
 * BAttachment->Attachment
 * 
 * @author zhr
 * 
 */
public class BizAttachmentConverter extends EntityConverter<BAttachment, Attachment> {
  private static BizAttachmentConverter instance = null;

  public static BizAttachmentConverter getInstnce() {
    if (instance == null)
      instance = new BizAttachmentConverter();
    return instance;
  }

  @Override
  public Attachment convert(BAttachment source) throws ConversionException {
    if (source == null)
      return null;

    Attachment target = new Attachment();
    target.setUuid(source.getUuid());
    target.setId(source.getId());
    target.setName(source.getName());
    target.setFileType(source.getFileType());
    target.setSize(source.getSize());
    target.setCreateInfo(BizOperateInfoConverter.getInstance().convert(source.getCreateInfo()));
    return target;
  }

}
