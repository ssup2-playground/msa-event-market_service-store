package com.market.store.server.http.error;

import com.market.store.server.error.ErrorCode;
import com.market.store.server.error.ErrorMessage;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class HttpDefaultErrorResponse extends DefaultErrorAttributes {
  // Override for BaseExecptionHandler
  @Override
  public Map<String, Object> getErrorAttributes(WebRequest request, ErrorAttributeOptions options) {
    return HttpErrorResponseBodyBuilder.getResponseAsMap(
        ErrorCode.INTERNAl_SERVER_ERROR, ErrorMessage.INTERNAl_SERVER_ERROR);
  }
}
