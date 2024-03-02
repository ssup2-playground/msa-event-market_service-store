package com.market.store.domain.vo;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder {
  private UUID id;
  private UUID storeId;
  private int count;

  public ProductOrder(String inboxPayload) {
    ProductOrder productOrder;
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      productOrder = objectMapper.readValue(inboxPayload, ProductOrder.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    this.id = productOrder.getId();
    this.storeId = productOrder.getStoreId();
    this.count = productOrder.getCount();
  }
}
