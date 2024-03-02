package com.market.store.server.http.filter;

import brave.Span;
import brave.Tracer;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.cloud.sleuth.autoconfig.instrument.web.SleuthWebProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(SleuthWebProperties.TRACING_FILTER_ORDER + 1)
public class SpanIdResponseFilter extends OncePerRequestFilter {
  private final Tracer tracer;

  SpanIdResponseFilter(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // Copy trace ID to response
    Span currentSpan = tracer.currentSpan();
    if (currentSpan != null) {
      ((HttpServletResponse) response)
          .addHeader("X-B3-TraceId", currentSpan.context().traceIdString());
    }

    filterChain.doFilter(request, response);
  }
}
