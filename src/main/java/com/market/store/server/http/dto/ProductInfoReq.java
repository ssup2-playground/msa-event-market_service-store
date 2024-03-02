package com.market.store.server.http.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoReq {
  @NotBlank
  @Size(max = 50)
  private String name;

  @NotBlank
  @Size(max = 100)
  private String description;

  @Min(0)
  private int quantity;
}
