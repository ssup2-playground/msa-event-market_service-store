package com.market.store.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  public static final String SECURITY_SCHEME_ACCESS_TOKEN = "AccessToken";

  @Bean
  public OpenAPI postsApi() {
    return new OpenAPI()
        // .addSecurityItem(new SecurityRequirement().addList("AccessToken"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "AccessToken",
                    new SecurityScheme()
                        .name(SECURITY_SCHEME_ACCESS_TOKEN)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .info(getInfo());
  }

  private Info getInfo() {
    return new Info()
        .title("market store service")
        .description("market store service")
        .version("1.0.0");
  }
}
