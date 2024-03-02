package com.market.store.domain.service;

import com.market.store.domain.entity.StoreInfo;
import com.market.store.domain.repository.StoreInfoPrimaryRepo;
import com.market.store.pkg.auth.AccessToken;
import com.market.store.server.error.StoreForbiddenException;
import com.market.store.server.error.StoreNotFoundException;
import java.util.UUID;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class StoreServiceAop {
  @Autowired private StoreInfoPrimaryRepo storeInfoPrimaryRepo;

  @Before(
      "execution(*"
          + " com.market.store.domain.service.StoreServiceImp.updateStoreInfo(..)) ||"
          + " execution(*"
          + " com.market.store.domain.service.StoreServiceImp.deleteStoreInfo(..))")
  private void validateStoreAndUserFromPrimaryDB(JoinPoint joinPoint) {
    // Get store ID
    UUID storeId;
    if (joinPoint.getArgs()[0] instanceof UUID) {
      storeId = (UUID) joinPoint.getArgs()[0];
    } else if (joinPoint.getArgs()[0] instanceof StoreInfo) {
      storeId = ((StoreInfo) joinPoint.getArgs()[0]).getId();
    } else {
      throw new IllegalArgumentException("Wrong store UUID");
    }

    // Get user ID form access token and set user ID to store info
    AccessToken accessToken = (AccessToken) SecurityContextHolder.getContext().getAuthentication();
    UUID userId = accessToken.getUserId();
    String userRole = accessToken.getUserRole();

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
}
