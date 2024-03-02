package com.market.store.server.grpc.interceptor;

import io.grpc.ForwardingServerCall;
import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@Slf4j
@GrpcGlobalServerInterceptor
public class AccessLogInterceptor implements ServerInterceptor {

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata requestHeaders, ServerCallHandler<ReqT, RespT> next) {
    long start = System.currentTimeMillis();
    return next.startCall(
        new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
          @Override
          public void close(Status status, Metadata trailers) {
            super.close(status, trailers);
            long end = System.currentTimeMillis();
            log.info(
                "method:{} client:{} status:{} duration:{}ms",
                call.getMethodDescriptor().getFullMethodName(),
                call.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR),
                status.getCode(),
                end - start);
          }
        },
        requestHeaders);
  }
}
