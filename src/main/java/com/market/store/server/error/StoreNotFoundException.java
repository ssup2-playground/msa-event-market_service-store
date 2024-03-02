package com.market.store.server.error;

import java.util.UUID;

public class StoreNotFoundException extends RuntimeException {
  public StoreNotFoundException(UUID storeUuid) {
    super("Store uuid " + storeUuid.toString());
  }
}
