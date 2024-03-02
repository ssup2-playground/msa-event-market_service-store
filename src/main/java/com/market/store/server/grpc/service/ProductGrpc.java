package com.market.store.server.grpc.service;

import com.market.store.domain.entity.ProductInfo;
import com.market.store.domain.service.ProductService;
import com.market.store.proto.*;
import com.market.store.proto.ProductGrpc.ProductImplBase;
import io.grpc.stub.StreamObserver;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class ProductGrpc extends ProductImplBase {
  @Autowired ProductService productService;

  @Override
  public void listProduct(ProductListReq request, StreamObserver<ProductListRes> responseObserver) {
    ProductListRes res =
        convertProductInfoListResToRes(
            productService.listProductInfos(
                UUID.fromString(request.getStoreId()),
                request.getOffset() / request.getLimit(),
                request.getLimit()));
    responseObserver.onNext(res);
    responseObserver.onCompleted();
  }

  @Override
  public void createProduct(
      ProductCreateReq request, StreamObserver<ProductInfoRes> responseObserver) {
    ProductInfo req = convertProductCreateReqToModel(request);
    ProductInfo res = productService.createProductInfo(req);
    responseObserver.onNext(convertProductInfoToRes(res));
    responseObserver.onCompleted();
  }

  @Override
  public void getProduct(ProductIdReq request, StreamObserver<ProductInfoRes> responseObserver) {
    ProductInfo storeInfo =
        productService.getProductInfo(
            UUID.fromString(request.getStoreId()), UUID.fromString(request.getId()));
    responseObserver.onNext(convertProductInfoToRes(storeInfo));
    responseObserver.onCompleted();
  }

  @Override
  public void updateProduct(ProductUpdateReq request, StreamObserver<Empty> responseObserver) {
    ProductInfo reqProductInfo = convertProductUpdateReqToModel(request);
    productService.updateProductInfo(reqProductInfo);
    responseObserver.onNext(Empty.newBuilder().build());
    responseObserver.onCompleted();
  }

  @Override
  public void deleteProduct(ProductIdReq request, StreamObserver<Empty> responseObserver) {
    productService.deleteProductInfo(
        UUID.fromString(request.getStoreId()), UUID.fromString(request.getId()));
    responseObserver.onNext(Empty.newBuilder().build());
    responseObserver.onCompleted();
  }

  @Override
  public void increaseQuantityProduct(
      ProductQuantityReq request, StreamObserver<ProductQuantityRes> responseObserver) {
    int res =
        productService.increaseProductQuantity(
            UUID.fromString(request.getStoreId()),
            UUID.fromString(request.getId()),
            request.getQuantity());
    responseObserver.onNext(convertQuantityToRes(request.getId(), res));
    responseObserver.onCompleted();
  }

  @Override
  public void decreaseQuantityProduct(
      ProductQuantityReq request, StreamObserver<ProductQuantityRes> responseObserver) {
    int res =
        productService.decreaseProductQuantity(
            UUID.fromString(request.getStoreId()),
            UUID.fromString(request.getId()),
            request.getQuantity());
    responseObserver.onNext(convertQuantityToRes(request.getId(), res));
    responseObserver.onCompleted();
  }

  public ProductListRes convertProductInfoListResToRes(List<ProductInfo> storeInfoList) {
    // Make up storetory info response list
    ProductListRes.Builder builder = ProductListRes.newBuilder();
    Iterator<ProductInfo> iter = storeInfoList.iterator();
    while (iter.hasNext()) {
      builder.addProducts(convertProductInfoToRes(iter.next()));
    }
    return builder.build();
  }

  public ProductInfo convertProductCreateReqToModel(ProductCreateReq request) {
    return new ProductInfo(
        null,
        UUID.fromString(request.getStoreId()),
        request.getName(),
        request.getDescription(),
        request.getQuantity());
  }

  public ProductInfo convertProductUpdateReqToModel(ProductUpdateReq request) {
    return new ProductInfo(
        UUID.fromString(request.getId()),
        UUID.fromString(request.getStoreId()),
        request.getName(),
        request.getDescription(),
        request.getQuantity());
  }

  public ProductInfoRes convertProductInfoToRes(ProductInfo storeInfo) {
    ProductInfoRes.Builder builder =
        ProductInfoRes.newBuilder()
            .setId(storeInfo.getId().toString())
            .setName(storeInfo.getName())
            .setDescription(storeInfo.getDescription())
            .setStoreId(storeInfo.getStoreId().toString())
            .setQuantity(storeInfo.getQuantity());
    return builder.build();
  }

  public ProductQuantityRes convertQuantityToRes(String productId, int quantity) {
    ProductQuantityRes.Builder builder =
        ProductQuantityRes.newBuilder().setId(productId).setQuantity(quantity);
    return builder.build();
  }
}
