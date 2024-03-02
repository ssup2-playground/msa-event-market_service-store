package com.market.store.server.http.error;

import com.market.store.server.error.ErrorCode;
import com.market.store.server.error.ErrorMessage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class HttpAuthExceptionHandler implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    response.setStatus(400);
    response.setContentType("application/json; charset=utf-8");
    response
        .getWriter()
        .write(
            HttpErrorResponseBodyBuilder.getResponseAsJson(
                ErrorCode.UNAUTHORIZED, ErrorMessage.UNAUTHORIZED));
  }
}
