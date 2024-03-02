package com.market.store.server.grpc.service;

import com.market.store.domain.entity.StoreInfo;
import com.market.store.domain.service.StoreService;
import com.market.store.proto.*;
import com.market.store.proto.StoreGrpc.StoreImplBase;
import io.grpc.stub.StreamObserver;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class StoreGrpc extends StoreImplBase {
  @Autowired StoreService storeService;

  @Override
  public void listStore(StoreListReq request, StreamObserver<StoreListRes> responseObserver) {
    StoreListRes res =
        convertStoreInfoListResToRes(
            storeService.listStoreInfos(
                request.getOffset() / request.getLimit(), request.getLimit()));
    responseObserver.onNext(res);
    responseObserver.onCompleted();
  }

  @Override
  public void createStore(StoreCreateReq request, StreamObserver<StoreInfoRes> responseObserver) {
    StoreInfo req = convertStoreCreateReqToModel(request);
    StoreInfo res = storeService.createStoreInfo(req);
    responseObserver.onNext(convertStoreInfoToRes(res));
    responseObserver.onCompleted();
  }

  @Override
  public void getStore(StoreIdReq request, StreamObserver<StoreInfoRes> responseObserver) {
    StoreInfo storeInfo = storeService.getStoreInfo(UUID.fromString(request.getId()));
    responseObserver.onNext(convertStoreInfoToRes(storeInfo));
    responseObserver.onCompleted();
  }

  @Override
  public void updateStore(StoreUpdateReq request, StreamObserver<Empty> responseObserver) {
    StoreInfo reqStoreInfo = convertStoreUpdateReqToModel(request);
    storeService.updateStoreInfo(reqStoreInfo);
    responseObserver.onNext(Empty.newBuilder().build());
    responseObserver.onCompleted();
  }

  @Override
  public void deleteStore(StoreIdReq request, StreamObserver<Empty> responseObserver) {
    storeService.deleteStoreInfo(UUID.fromString(request.getId()));
    responseObserver.onNext(Empty.newBuilder().build());
    responseObserver.onCompleted();
  }

  public StoreListRes convertStoreInfoListResToRes(List<StoreInfo> storeInfoList) {
    // Make up store info response list
    StoreListRes.Builder builder = StoreListRes.newBuilder();
    Iterator<StoreInfo> iter = storeInfoList.iterator();
    while (iter.hasNext()) {
      builder.addStores(convertStoreInfoToRes(iter.next()));
    }
    return builder.build();
  }

  public StoreInfo convertStoreCreateReqToModel(StoreCreateReq request) {
    return new StoreInfo(null, request.getName(), request.getDescription(), null);
  }

  public StoreInfo convertStoreUpdateReqToModel(StoreUpdateReq request) {
    return new StoreInfo(
        UUID.fromString(request.getId()), request.getName(), request.getDescription(), null);
  }

  public StoreInfoRes convertStoreInfoToRes(StoreInfo storeInfo) {
    StoreInfoRes.Builder builder =
        StoreInfoRes.newBuilder()
            .setId(storeInfo.getId().toString())
            .setName(storeInfo.getName())
            .setDescription(storeInfo.getDescription());
    return builder.build();
  }
}
