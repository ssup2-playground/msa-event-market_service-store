package com.market.store.server.http.error;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HttpErrorResponseBodyBuilder {
  public static Map<String, Object> getResponseAsMap(String code, String message) {
    Map<String, Object> result = new HashMap<>();
    result.put("code", code);
    result.put("message", message);
    return result;
  }

  public static String getResponseAsJson(String code, String message) {
    return String.format("{\"code\":\"%s\",\"message\":\"%s\"}", code, message);
  }
}
