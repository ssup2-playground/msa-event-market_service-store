package com.market.store.domain.service;

import brave.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.store.domain.entity.Inbox;
import com.market.store.domain.entity.Outbox;
import com.market.store.domain.entity.ProductInfo;
import com.market.store.domain.repository.InboxPrimaryRepo;
import com.market.store.domain.repository.OutboxPrimaryRepo;
import com.market.store.domain.repository.ProductInfoPrimaryRepo;
import com.market.store.domain.repository.ProductInfoSecondaryRepo;
import com.market.store.domain.vo.ProductOrder;
import com.market.store.pkg.tracing.SpanContext;
import com.market.store.server.error.ProductNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ProductServiceImp implements ProductService {
  private static final String AGGREGATE_TYPE_PRODUCT = "Product";
  private static final String EVENT_TYPE_PRODUCT_CREATED = "ProductCreated";
  private static final String EVENT_TYPE_PRODUCT_DELETED = "ProductDeleted";
  private static final String EVENT_TYPE_PRODUCT_INCREASED = "ProductIncreased";
  private static final String EVENT_TYPE_PRODUCT_INCREASED_ERROR = "ProductIncreasedError";
  private static final String EVENT_TYPE_PRODUCT_DECREASED = "ProductDecreased";
  private static final String EVENT_TYPE_PRODUCT_DECREASED_ERROR = "ProductDecreasedError";

  @Autowired private ProductInfoPrimaryRepo productInfoPrimaryRepo;
  @Autowired private ProductInfoSecondaryRepo productInfoSecondaryRepo;
  @Autowired private OutboxPrimaryRepo outboxPrimaryRepo;
  @Autowired private InboxPrimaryRepo inboxPrimaryRepo;
  @Autowired private Tracer tracer;

  @Override
  @Transactional("secondaryTransactionManager")
  public List<ProductInfo> listProductInfos(UUID storeId, int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    return productInfoSecondaryRepo.findAll(pageRequest).getContent();
  }

  @Override
  @Transactional("secondaryTransactionManager")
  public List<ProductInfo> listProductInfosByName(UUID storeId, String name, int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    return productInfoSecondaryRepo.findByName(name, pageRequest);
  }

  @Override
  @Transactional
  public ProductInfo createProductInfo(ProductInfo productInfo) {
    // Create product
    productInfo = productInfoPrimaryRepo.save(productInfo);

    try {
      // Get span context as JSON
      String spanContextJson = SpanContext.GetSpanContextAsJson(tracer.currentSpan());

      // Create outbox for product creation event
      ObjectMapper jsonMapper = new ObjectMapper();
      String productInfoJson = jsonMapper.writeValueAsString(productInfo);
      Outbox outbox =
          new Outbox(
              null,
              AGGREGATE_TYPE_PRODUCT,
              productInfo.getId().toString(),
              EVENT_TYPE_PRODUCT_CREATED,
              productInfoJson,
              spanContextJson);
      outboxPrimaryRepo.save(outbox);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // return
    return productInfo;
  }

  @Override
  @Transactional("secondaryTransactionManager")
  public ProductInfo getProductInfo(UUID storeId, UUID productId) {
    return productInfoSecondaryRepo
        .findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
  }

  @Override
  @Transactional
  public void updateProductInfo(ProductInfo productInfo) {
    productInfoPrimaryRepo.save(productInfo);
  }

  @Override
  @Transactional
  public void deleteProductInfo(UUID storeId, UUID productId) {
    // Get and delete product
    ProductInfo productInfo = productInfoPrimaryRepo.getById(productId);
    productInfoPrimaryRepo.deleteById(productId);

    try {
      // Get span context as JSON
      String spanContextJson = SpanContext.GetSpanContextAsJson(tracer.currentSpan());

      // Create outbox for product deletion event
      Outbox outbox =
          new Outbox(
              null,
              AGGREGATE_TYPE_PRODUCT,
              productInfo.getId().toString(),
              EVENT_TYPE_PRODUCT_DELETED,
              productInfo.toJsonString(),
              spanContextJson);
      outboxPrimaryRepo.save(outbox);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @Transactional
  public int increaseProductQuantity(UUID storeId, UUID productId, int increment) {
    ProductInfo productInfo =
        productInfoPrimaryRepo
            .findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
    productInfoPrimaryRepo.increaseQuantity(productId, increment);
    return productInfo.getQuantity() + increment;
  }

  @Override
  @Transactional
  public void increaseProductQuantityMq(Inbox inbox) {
    // Get order info from inbox
    ProductOrder productOrder = new ProductOrder(inbox.getPayload());

    // Save inbox to prevent duplicate action
    if (inboxPrimaryRepo.findById(productOrder.getId()).isPresent()) {
      log.warn("Duplicated inbox");
      return;
    }
    inboxPrimaryRepo.save(inbox);

    try {
      // Get span context as JSON
      String spanContextJson = SpanContext.GetSpanContextAsJson(tracer.currentSpan());

      // Increment
      ProductInfo productInfo =
          productInfoPrimaryRepo
              .findById(productOrder.getId())
              .orElseThrow(() -> new ProductNotFoundException(productOrder.getId()));
      productInfoPrimaryRepo.increaseQuantity(productOrder.getId(), productOrder.getCount());
      productInfo.setQuantity(productInfo.getQuantity() + productOrder.getCount());

      // Create outbox to publish product increment event
      Outbox outbox =
          new Outbox(
              null,
              AGGREGATE_TYPE_PRODUCT,
              productInfo.getId().toString(),
              EVENT_TYPE_PRODUCT_INCREASED,
              productInfo.toJsonString(),
              spanContextJson);
      outboxPrimaryRepo.save(outbox);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @Transactional
  public int decreaseProductQuantity(UUID storeId, UUID productId, int decrement) {
    ProductInfo productInfo =
        productInfoPrimaryRepo
            .findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
    productInfoPrimaryRepo.decreaseQuantity(productId, decrement);
    if (productInfo.getQuantity() - decrement < 0) {
      throw new RuntimeException("insufficient product quantity");
    }
    return productInfo.getQuantity() - decrement;
  }

  @Override
  @Transactional
  public void decreaseProductQuantityMq(Inbox inbox) {
    // Get order info from inbox
    ProductOrder productOrder = new ProductOrder(inbox.getPayload());

    // Check and Save inbox to prevent duplicate action
    if (inboxPrimaryRepo.findById(inbox.getId()).isPresent()) {
      log.warn("Duplicated inbox");
      return;
    }
    inboxPrimaryRepo.save(inbox);

    // Decrement
    ProductInfo productInfo =
        productInfoPrimaryRepo
            .findById(productOrder.getId())
            .orElseThrow(() -> new ProductNotFoundException(productOrder.getId()));
    productInfoPrimaryRepo.decreaseQuantity(productOrder.getId(), productOrder.getCount());
    productInfo.setQuantity(productInfo.getQuantity() - productOrder.getCount());

    try {
      // Get span context as JSON
      String spanContextJson = SpanContext.GetSpanContextAsJson(tracer.currentSpan());

      if (productInfo.getQuantity() < 0) {
        // If lack of quantity, increase it and send decrease error
        log.warn("Lack of quantity");
        productInfoPrimaryRepo.increaseQuantity(productOrder.getId(), productOrder.getCount());

        // Create outbox to publish product decrement event
        Outbox outbox =
            new Outbox(
                null,
                AGGREGATE_TYPE_PRODUCT,
                productOrder.getId().toString(),
                EVENT_TYPE_PRODUCT_DECREASED_ERROR,
                "",
                spanContextJson);
        outboxPrimaryRepo.save(outbox);
        return;
      } else {
        // Create outbox to publish product decrement event
        Outbox outbox =
            new Outbox(
                null,
                AGGREGATE_TYPE_PRODUCT,
                productInfo.getId().toString(),
                EVENT_TYPE_PRODUCT_DECREASED,
                productInfo.toJsonString(),
                spanContextJson);
        outboxPrimaryRepo.save(outbox);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
