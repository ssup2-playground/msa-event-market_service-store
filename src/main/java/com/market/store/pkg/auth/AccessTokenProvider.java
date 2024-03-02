package com.market.store.pkg.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccessTokenProvider implements AuthenticationProvider {
  public static final String TOKEN_KEY =
      "cyQsIQ6RSE1CqTARl8pWeM7br9qp1Don57Pd18uDCwoBaiUPEXWe15pYMP4D9WKc";
  public static final String USER_ID = "UserID";
  public static final String USER_LOGIN_ID = "UserLoginID";
  public static final String USER_ROLE = "UserRole";

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // Parsing access token
    AccessToken accessToken = (AccessToken) authentication;
    Claims claims;
    try {
      claims =
          Jwts.parserBuilder()
              .setSigningKey(TOKEN_KEY.getBytes())
              .build()
              .parseClaimsJws(accessToken.getToken())
              .getBody();
    } catch (Exception e) {
      log.error("wrong access token");
      throw new BadCredentialsException("wrong access token");
    }
    String userId = (String) claims.get(USER_ID);
    String userLoginId = (String) claims.get(USER_LOGIN_ID);
    String userRole = (String) claims.get(USER_ROLE);

    // Get access token with authorities
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    grantedAuthorities.add(new SimpleGrantedAuthority(userRole));
    accessToken =
        new AccessToken(
            accessToken.getToken(),
            UUID.fromString(userId),
            userLoginId,
            userRole,
            grantedAuthorities);
    accessToken.setAuthenticated(true);
    return accessToken;
  }

  // For reflection
  @Override
  public boolean supports(Class<?> authentication) {
    return AccessToken.class.isAssignableFrom(authentication);
  }
}
