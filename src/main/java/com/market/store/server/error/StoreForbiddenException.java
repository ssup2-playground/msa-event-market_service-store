package com.market.store.server.error;

import java.util.UUID;

public class StoreForbiddenException extends RuntimeException {
  public StoreForbiddenException(UUID storeUuid) {
    super("Store uuid " + storeUuid.toString());
  }
}
