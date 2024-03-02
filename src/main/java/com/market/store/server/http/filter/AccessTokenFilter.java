package com.market.store.server.http.filter;

import com.market.store.pkg.auth.AccessToken;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AccessTokenFilter extends OncePerRequestFilter {
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // Get accessToken
    String accessToken = getAccessTokenFromHeader(request);
    if (accessToken != null) {
      // Set security context if access token exists
      SecurityContextHolder.getContext().setAuthentication(new AccessToken(accessToken, null));
    }

    filterChain.doFilter(request, response);
  }

  private String getAccessTokenFromHeader(HttpServletRequest request) {
    // Get JWT Token from header
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length()).trim();
    }
    return null;
  }
}
