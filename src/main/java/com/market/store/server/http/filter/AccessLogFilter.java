package com.market.store.server.http.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class AccessLogFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // Logging access
    long start = System.currentTimeMillis();
    filterChain.doFilter(request, response);
    long end = System.currentTimeMillis();
    log.info(
        "method:{} url:{} client_ip:{} status:{} duration:{}ms",
        request.getMethod(),
        request.getRequestURI(),
        request.getRemoteHost(),
        response.getStatus(),
        end - start);
  }
}
