package com.market.store.server.error;

import java.util.UUID;

public class ProductForbiddenException extends RuntimeException {
  public ProductForbiddenException(UUID productUuid) {
    super("Product uuid " + productUuid.toString());
  }
}
