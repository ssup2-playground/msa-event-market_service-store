package com.market.store.domain.vo;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
  private UUID id;
  private String loginId;
  private String role;

  public UserInfo(String inboxPayload) {
    UserInfo userInfo;
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      userInfo = objectMapper.readValue(inboxPayload, UserInfo.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    this.id = userInfo.getId();
    this.loginId = userInfo.getLoginId();
    this.role = userInfo.getRole();
  }
}
