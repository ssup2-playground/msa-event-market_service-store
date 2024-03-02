package com.market.store.domain.service;

import com.market.store.domain.entity.Inbox;

public interface ManagementService {
  public void deleteStoreProudctByDeletedUserMq(Inbox inbox);
}
