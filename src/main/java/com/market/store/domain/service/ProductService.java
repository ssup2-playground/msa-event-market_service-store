package com.market.store.domain.service;

import com.market.store.domain.entity.Inbox;
import com.market.store.domain.entity.ProductInfo;
import java.util.List;
import java.util.UUID;

public interface ProductService {
  public List<ProductInfo> listProductInfos(UUID storeId, int page, int size);

  public List<ProductInfo> listProductInfosByName(UUID storeId, String name, int page, int size);

  public ProductInfo createProductInfo(ProductInfo productInfo);

  public ProductInfo getProductInfo(UUID storeId, UUID productId);

  public void updateProductInfo(ProductInfo productInfo);

  public void deleteProductInfo(UUID storeId, UUID productId);

  public int increaseProductQuantity(UUID storeId, UUID productId, int increment);

  public void increaseProductQuantityMq(Inbox inbox);

  public int decreaseProductQuantity(UUID storeId, UUID productId, int decrement);

  public void decreaseProductQuantityMq(Inbox inbox);
}
