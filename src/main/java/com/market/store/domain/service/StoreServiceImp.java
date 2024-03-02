package com.market.store.domain.service;

import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.market.store.domain.entity.Outbox;
import com.market.store.domain.entity.StoreInfo;
import com.market.store.domain.repository.OutboxPrimaryRepo;
import com.market.store.domain.repository.ProductInfoPrimaryRepo;
import com.market.store.domain.repository.StoreInfoPrimaryRepo;
import com.market.store.domain.repository.StoreInfoSecondaryRepo;
import com.market.store.pkg.auth.AccessToken;
import com.market.store.pkg.tracing.SpanContext;
import com.market.store.server.error.StoreNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreServiceImp implements StoreService {
  private static final String AGGREGATE_TYPE_STORE = "Store";
  private static final String EVENT_TYPE_STORE_CREATED = "StoreCreated";
  private static final String EVENT_TYPE_STORE_DELETED = "StoreDeleted";

  @Autowired private StoreInfoPrimaryRepo storeInfoPrimaryRepo;
  @Autowired private StoreInfoSecondaryRepo storeInfoSecondaryRepo;
  @Autowired private ProductInfoPrimaryRepo productInfoPrimaryRepo;
  @Autowired private OutboxPrimaryRepo outboxPrimaryRepo;
  @Autowired private Tracer tracer;

  @Override
  @Transactional("secondaryTransactionManager")
  public List<StoreInfo> listStoreInfos(int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    return storeInfoSecondaryRepo.findAll(pageRequest).getContent();
  }

  @Override
  @Transactional("secondaryTransactionManager")
  public List<StoreInfo> listStoreInfosByName(String name, int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);

    return storeInfoSecondaryRepo.findByName(name, pageRequest);
  }

  @Override
  @Transactional
  public StoreInfo createStoreInfo(StoreInfo storeInfo) {
    // Get user ID form access token and set user ID to store info
    AccessToken accessToken = (AccessToken) SecurityContextHolder.getContext().getAuthentication();
    storeInfo.setUserId(accessToken.getUserId());

    // Create store
    storeInfo = storeInfoPrimaryRepo.save(storeInfo);

    try {
      // Get span context as JSON
      String spanContextJson = SpanContext.GetSpanContextAsJson(tracer.currentSpan());

      // Create outbox for store creation event
      Outbox outbox =
          new Outbox(
              null,
              AGGREGATE_TYPE_STORE,
              storeInfo.getId().toString(),
              EVENT_TYPE_STORE_CREATED,
              storeInfo.toJsonString(),
              spanContextJson);
      outboxPrimaryRepo.save(outbox);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    // Return
    return storeInfo;
  }

  @Override
  @Transactional("secondaryTransactionManager")
  public StoreInfo getStoreInfo(UUID storeId) {
    return storeInfoSecondaryRepo
        .findById(storeId)
        .orElseThrow(() -> new StoreNotFoundException(storeId));
  }

  @Override
  @Transactional
  public void updateStoreInfo(StoreInfo storeInfo) {
    storeInfoPrimaryRepo.save(storeInfo);
  }

  @Override
  @Transactional
  public void deleteStoreInfo(UUID storeId) {
    // Delete all products owned by store
    productInfoPrimaryRepo.deleteByStoreId(storeId);

    // Get and delete store
    StoreInfo storeInfo = storeInfoPrimaryRepo.getById(storeId);
    storeInfoPrimaryRepo.deleteById(storeId);

    try {
      // Get span context as JSON
      String spanContextJson = SpanContext.GetSpanContextAsJson(tracer.currentSpan());

      // Create outbox for store deletion event
      Outbox outbox =
          new Outbox(
              null,
              AGGREGATE_TYPE_STORE,
              storeInfo.getId().toString(),
              EVENT_TYPE_STORE_DELETED,
              storeInfo.toJsonString(),
              spanContextJson);
      outboxPrimaryRepo.save(outbox);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteStoreInfoByOwnerID(String msgId, UUID ownerId) {}
}
