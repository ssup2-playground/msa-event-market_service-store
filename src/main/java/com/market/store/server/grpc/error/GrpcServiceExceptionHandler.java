package com.market.store.server.grpc.error;

import com.market.store.server.error.ErrorCode;
import com.market.store.server.error.ProductForbiddenException;
import com.market.store.server.error.ProductNotFoundException;
import com.market.store.server.error.StoreForbiddenException;
import com.market.store.server.error.StoreNotFoundException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcServiceExceptionHandler {
  @GrpcExceptionHandler(StoreNotFoundException.class)
  public Status handleStoreNotFoundException(StoreNotFoundException e) {
    return Status.NOT_FOUND.withDescription(ErrorCode.NOT_FOUND_STORE);
  }

  @GrpcExceptionHandler(StoreForbiddenException.class)
  public Status handleStoreForbiddenException(StoreForbiddenException e) {
    return Status.NOT_FOUND.withDescription(ErrorCode.FORBIDDEN_STORE);
  }

  @GrpcExceptionHandler(ProductNotFoundException.class)
  public Status handleProductNotFoundException(ProductNotFoundException e) {
    return Status.NOT_FOUND.withDescription(ErrorCode.NOT_FOUND_PRODUCT);
  }

  @GrpcExceptionHandler(ProductForbiddenException.class)
  public Status handleProductForbiddenException(ProductForbiddenException e) {
    return Status.NOT_FOUND.withDescription(ErrorCode.FORBIDDEN_PRODUCT);
  }
}
