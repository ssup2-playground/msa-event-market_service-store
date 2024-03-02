package com.market.store.pkg.auth;

import java.util.Collection;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class AccessToken extends AbstractAuthenticationToken {
  public static final String ROLE_ADMIN = "admin";
  public static final String ROLE_USER = "user";

  private String token;
  private UUID userId;
  private String userLoginId;
  private String userRole;

  public AccessToken(String token, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.token = token;
  }

  public AccessToken(
      String token,
      UUID userId,
      String userLoginId,
      String userRole,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.token = token;
    this.userId = userId;
    this.userLoginId = userLoginId;
    this.userRole = userRole;
  }

  @Override
  public String getPrincipal() {
    if (userId == null) {
      // When userId isn't set, return emptyId string for grpc authorization.
      return "emptyId";
    }
    return userId.toString();
  }

  @Override
  public Object getCredentials() {
    // No credential
    return null;
  }
}
