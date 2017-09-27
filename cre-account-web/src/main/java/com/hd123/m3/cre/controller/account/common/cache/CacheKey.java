package com.hd123.m3.cre.controller.account.common.cache;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class CacheKey {

  private Object[] keys;

  public CacheKey(Object... keys) {
    this.keys = keys;
  }

  @Override
  public String toString() {
    if (keys != null) {
      return StringUtils.join(keys, "##");
    } else {
      return "";
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (obj instanceof CacheKey == false)
      return false;
    CacheKey other = (CacheKey) obj;
    if (keys == null && other.keys == null)
      return true;
    else if (keys == null && other.keys != null)
      return false;
    else if (keys != null && other.keys == null)
      return false;
    if (keys.length != other.keys.length)
      return false;
    for (int index = 0; index < keys.length; index++)
      if (ObjectUtils.equals(keys[index], other.keys[index]) == false)
        return false;
    return true;
  }

  @Override
  public int hashCode() {
    return ObjectUtils.hashCodeMulti(keys);
  }

}
