package com.market.store.server.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerController {
  @RequestMapping(path = "/v1")
  public String redirectToSwagger() {
    return "redirect:/v1/swagger";
  }
}
