package com.hd123.m3.account.gwt.cpnts.client.ui.store;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree.Resources;

public interface StoreNavigatorResources extends Resources {

  public static final StoreNavigatorResources INSTANCE = GWT.create(StoreNavigatorResources.class);

  @Source("open.gif")
  ImageResource treeOpen();

  @Source("close.gif")
  ImageResource treeClosed();

  @Source("cb-arrow-hide.gif")
  ImageResource treeLeaf();

  @Source("store.gif")
  ImageResource store();
}
