package com.market.store.pkg.tracing;

import brave.Span;
import brave.propagation.Propagation;
import brave.propagation.TraceContext.Extractor;
import brave.propagation.TraceContext.Injector;
import brave.propagation.TraceContextOrSamplingFlags;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.TreeMap;

public class SpanContext {
  public static String GetSpanContextAsJson(Span span) throws JsonProcessingException {
    /*
    int sampled = 0;
    if (span.context().sampled()) {
      sampled = 1;
    }

    Map<String, Object> spanContextMap = new TreeMap<>();
    spanContextMap.put("x-b3-parentspanid", span.context().parentIdString());
    spanContextMap.put("x-b3-traceid", span.context().traceIdString());
    spanContextMap.put("x-b3-spanid", span.context().spanIdString());
    spanContextMap.put("x-b3-sampled", sampled);
    */
    // Inject span context to map
    Map<String, String> spanContextMap = new TreeMap<>();
    Propagation<String> b3Propagator = Propagation.B3_STRING;
    Injector<Map<String, String>> b3Injector = b3Propagator.injector(Map::put);
    b3Injector.inject(span.context(), spanContextMap);

    // Serialize map to JSON
    String spanContextJson = new ObjectMapper().writeValueAsString(spanContextMap);
    return spanContextJson;
  }

  public static TraceContextOrSamplingFlags GetSpanContextFromJson(String spanContextJson)
      throws JsonProcessingException {
    // Deserialize JSON to map
    Map<String, String> spanContexMap =
        new ObjectMapper()
            .readValue(spanContextJson, new TypeReference<TreeMap<String, String>>() {});

    // Extract span context from map
    Propagation<String> b3Propagator = Propagation.B3_STRING;
    Extractor<Map<String, String>> b3Extractor = b3Propagator.extractor(Map::get);
    return b3Extractor.extract(spanContexMap);
  }
}
