package com.market.store.server.kafka.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DebezOutbox {
  private UUID id;
  private DebezPayload payload;

  public static DebezOutbox getDebezOutboxFromMsg(UUID msgId, String msg)
      throws JsonMappingException, JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    DebezOutbox outbox = objectMapper.readValue(msg, DebezOutbox.class);
    outbox.setId(msgId);
    return outbox;
  }
}
