package com.market.store.server.grpc.interceptor;

import brave.Span;
import brave.Tracer;
import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@GrpcGlobalServerInterceptor
public class SpanIdResponseInterceptor implements ServerInterceptor {
  private final Tracer tracer;

  public SpanIdResponseInterceptor(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata requestHeaders, ServerCallHandler<ReqT, RespT> next) {
    return next.startCall(
        new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
          @Override
          public void sendHeaders(Metadata responseHeaders) {
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
              responseHeaders.put(
                  Metadata.Key.of("X-B3-TraceId", Metadata.ASCII_STRING_MARSHALLER),
                  currentSpan.context().traceIdString());
            }
            super.sendHeaders(responseHeaders);
          }
        },
        requestHeaders);
  }
}
