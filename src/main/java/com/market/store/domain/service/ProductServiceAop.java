package com.market.store.domain.service;

import com.market.store.domain.entity.Inbox;
import com.market.store.domain.entity.ProductInfo;
import com.market.store.domain.entity.StoreInfo;
import com.market.store.domain.repository.ProductInfoPrimaryRepo;
import com.market.store.domain.repository.StoreInfoPrimaryRepo;
import com.market.store.domain.vo.ProductOrder;
import com.market.store.pkg.auth.AccessToken;
import com.market.store.server.error.ProductNotFoundException;
import com.market.store.server.error.StoreForbiddenException;
import com.market.store.server.error.StoreNotFoundException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
public class ProductServiceAop {
  @Autowired private StoreInfoPrimaryRepo storeInfoPrimaryRepo;
  @Autowired private StoreInfoPrimaryRepo storeInfoSecondaryRepo;
  @Autowired private ProductInfoPrimaryRepo productInfoPrimaryRepo;

  @Before(
      "execution(*"
          + " com.market.store.domain.service.ProductServiceImp.updateProductInfo(..)) ||"
          + " execution(*"
          + " com.market.store.domain.service.ProductServiceImp.deleteProductInfo(..)) ||"
          + " execution(*"
          + " com.market.store.domain.service.ProductServiceImp.increase*(..)) ||"
          + " execution(*"
          + " com.market.store.domain.service.ProductServiceImp.decrease*(..))")
  private void validateStoreAndUserFromPrimaryDB(JoinPoint joinPoint) {
    // Get store ID
    UUID storeId;
    if (joinPoint.getArgs()[0] instanceof UUID) {
      storeId = (UUID) joinPoint.getArgs()[0];
    } else if (joinPoint.getArgs()[0] instanceof ProductInfo) {
      storeId = ((ProductInfo) joinPoint.getArgs()[0]).getStoreId();
    } else if (joinPoint.getArgs()[0] instanceof Inbox) {
      Inbox inbox = ((Inbox) joinPoint.getArgs()[0]);
      storeId = new ProductOrder(inbox.getPayload()).getStoreId();
    } else {
      throw new IllegalArgumentException("Wrong store ID");
    }

    // Get user ID form access token and set user ID to store info
    AccessToken accessToken = (AccessToken) SecurityContextHolder.getContext().getAuthentication();
    UUID userId = accessToken.getUserId();
    String userRole = accessToken.getUserRole();
    log.info("-- user Id --" + userId.toString());
    log.info("-- user Role --" + userRole);

    // Get store info
    StoreInfo storeInfo =
        storeInfoPrimaryRepo
            .findById(storeId)
            .orElseThrow(() -> new StoreNotFoundException(storeId));

    // Check whether the user is the admin user or
    // the same user as the user stored in the storeInfo.
    if (userRole.equals("admin")) {
      return;
    } else {
      if (!userId.equals(storeInfo.getUserId())) {
        throw new StoreForbiddenException(storeId);
      }
    }
  }

  @Before("execution(* com.market.store.domain.service.ProductServiceImp.getProductInfo(..))")
  private void validateStoreFromSecondaryDB(JoinPoint joinPoint) {
    // Get store ID
    UUID storeId;
    if (joinPoint.getArgs()[0] instanceof UUID) {
      storeId = (UUID) joinPoint.getArgs()[0];
    } else {
      throw new IllegalArgumentException("Wrong store ID");
    }

    // Get store info
    storeInfoSecondaryRepo.findById(storeId).orElseThrow(() -> new StoreNotFoundException(storeId));
  }

  @Before(
      "execution(*"
          + " com.market.store.domain.service.ProductServiceImp.updateProductInfo(..)) ||"
          + " execution(*"
          + " com.market.store.domain.service.ProductServiceImp.deleteProductInfo(..))")
  private void validateProductFromPrimaryDB(JoinPoint joinPoint) {
    // Get product ID
    UUID productId;
    if (joinPoint.getArgs()[0] instanceof ProductInfo) {
      productId = ((ProductInfo) joinPoint.getArgs()[0]).getId();
    } else if (joinPoint.getArgs()[0] instanceof Inbox) {
      Inbox inbox = (Inbox) joinPoint.getArgs()[0];
      productId = new ProductOrder(inbox.getPayload()).getId();
    } else if (joinPoint.getArgs()[1] instanceof UUID) {
      productId = (UUID) joinPoint.getArgs()[1];
    } else {
      throw new IllegalArgumentException("Wrong product UUID");
    }

    // Get product info
    productInfoPrimaryRepo
        .findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
  }
}
