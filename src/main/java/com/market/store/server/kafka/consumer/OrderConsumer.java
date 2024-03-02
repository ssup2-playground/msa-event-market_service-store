package com.market.store.server.kafka.consumer;

import brave.Tracer;
import brave.propagation.TraceContextOrSamplingFlags;
import com.market.store.domain.entity.Inbox;
import com.market.store.domain.service.ProductService;
import com.market.store.pkg.message.Converter;
import com.market.store.pkg.tracing.SpanContext;
import com.market.store.server.error.ProductNotFoundException;
import com.market.store.server.error.StoreNotFoundException;
import com.market.store.server.kafka.message.DebezOutbox;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderConsumer {
  private static final String AGGREGATE_TYPE_ORDER = "Order";
  private static final String EVENT_TYPE_ORDER_PAID = "OrderPaid";
  private static final String EVENT_TYPE_ORDER_CANCELLED = "OrderCancelled";

  @Autowired private ProductService productService;
  @Autowired private Tracer tracer;

  @KafkaListener(
      topics = "#{${spring.kafka.topic.prefix}}-market-order-outbox-Order",
      groupId = "#{${spring.kafka.groupid.prefix}}-market-store-order")
  public void consume(
      @Header("id") String msgIdBase64,
      @Header("spanContext") String spanContextJson,
      @Payload String msg,
      Acknowledgment ack) {

    try {
      // Get msg info
      UUID msgId = Converter.getUuidFromBase64(msgIdBase64);
      DebezOutbox outbox = DebezOutbox.getDebezOutboxFromMsg(msgId, msg);
      log.info(
          "Order Consumer id:{} eventType:{} event:{}",
          outbox.getId().toString(),
          outbox.getPayload().getEventType(),
          outbox.getPayload().getEvent());

      if (outbox.getPayload().getEventType().equals(EVENT_TYPE_ORDER_PAID)) {
        // Set span context
        TraceContextOrSamplingFlags spanContext =
            SpanContext.GetSpanContextFromJson(spanContextJson);
        tracer.nextSpan(spanContext);

        // Call decrese product quantity service
        Inbox inbox =
            new Inbox(
                msgId,
                AGGREGATE_TYPE_ORDER,
                EVENT_TYPE_ORDER_PAID,
                outbox.getPayload().getEvent(),
                spanContextJson);
        productService.decreaseProductQuantityMq(inbox);

      } else if (outbox.getPayload().getEventType().equals(EVENT_TYPE_ORDER_CANCELLED)) {
        // Set span context
        TraceContextOrSamplingFlags spanContext =
            SpanContext.GetSpanContextFromJson(spanContextJson);
        tracer.nextSpan(spanContext);

        // Call increase product quantity service
        Inbox inbox =
            new Inbox(
                msgId,
                AGGREGATE_TYPE_ORDER,
                EVENT_TYPE_ORDER_PAID,
                outbox.getPayload().getEvent(),
                spanContextJson);
        productService.increaseProductQuantityMq(inbox);
      }
    } catch (StoreNotFoundException | ProductNotFoundException e) {
      log.error("Resource not found. Ignore it", e);
      ack.acknowledge();
      return;
    } catch (Exception e) {
      log.error("Order consumer exception", e);
      throw new RuntimeException(e);
    }
    ack.acknowledge();
  }
}
