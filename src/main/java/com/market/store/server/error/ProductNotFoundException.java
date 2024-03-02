package com.market.store.server.error;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(UUID productUuid) {
    super("Product uuid " + productUuid.toString());
  }
}
