# msa-event-market_service-store

service-store is the service responsible for store and product management in the [msa-event-market](https://github.com/ssup2-playground/project_msa-event-market) Project. service-store follows this [considerations](https://github.com/ssup2-playground/project_msa-event-market?tab=readme-ov-file#service-considerations)

* Architecture

<img src="/images/architecture.png" width="600"/>

* ER Diagram

<img src="/images/er-diagram.png" width="700"/>

* [Swagger](https://ssup2-playground.github.io/msa-event-market_service-store/api/openapi/swagger.html)

## Used main maven repos and tools

service-store uses following maven repos and tools

* **HTTP Server** : [Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* **GRPC Server** : [gRPC Spring Starter](https://yidongnan.github.io/grpc-spring-boot-starter/en/)
* **MySQL** : [Spring Data JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* **Redis** : 
* **Kafka** : [Spring Kafka](https://spring.io/projects/spring-kafka), [Debezium Outbox](https://debezium.io/documentation/reference/1.8/transformations/outbox-event-router.html)
* **Logging** : [Logback](https://logback.qos.ch/), [Spring Cloud Slueth/Zipkin](https://spring.io/projects/spring-cloud-sleuth/), 
* **Continuous Integration** : [Spring Test](https://spring.io/guides/gs/testing-web/), [Github Actions](https://github.com/features/actions)
* **Continuous Deployment** : [K8s](https://kubernetes.io/), [ArgoCD](https://argo-cd.readthedocs.io/en/stable/), [ArgoCD Image Updater](https://github.com/argoproj-labs/argocd-image-updater/), [Kustomize](https://kustomize.io/)

## Reference

* Multiple Datasource 
  * https://medium.com/swlh/a-complete-guide-to-setting-up-multiple-datasources-in-spring-8296d4ff0935
  * https://www.baeldung.com/spring-data-jpa-multiple-databases
  * https://gigas-blog.tistory.com/122
  * https://stackoverflow.com/questions/28275448/multiple-data-source-and-schema-creation-in-spring-boot

* Spring Data JPA 
  * https://www.baeldung.com/spring-data-jpa-query
  * https://stackoverflow.com/questions/40605834/spring-jparepositroy-save-does-not-appear-to-throw-exception-on-duplicate-save

* GRPC
  * https://github.com/LogNet/grpc-spring-boot-starter
  * https://tech.lattechiffon.com/2021/06/27/grpc-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B5%AC%EC%84%B1-%EB%B0%A9%EB%B2%95%EA%B3%BC-unary-rpc-%EA%B5%AC%ED%98%84-java/
  * https://github.com/dconnelly/grpc-error-example

* Kafka
  * https://howtodoinjava.com/kafka/spring-boot-jsonserializer-example/
  * https://www.baeldung.com/spring-kafka
  * https://www.quora.com/What-is-the-difference-between-acknowledgement-and-commit-in-Kafka
  * https://stackoverflow.com/questions/40872520/whats-the-purpose-of-kafkas-key-value-pair-based-messaging
  * https://docs.confluent.io/platform/current/schema-registry/serdes-develop/serdes-json.html
  * https://stackoverflow.com/questions/47427948/how-to-acknowledge-current-offset-in-spring-kafka-for-manual-commit

* Application Properties
  * https://lejewk.github.io/springboot-gradle-spring-profiles-active/

* Spring Security 
  * https://lion-king.tistory.com/entry/Spring-Security-what-is
  * https://derekpark.tistory.com/101
  * https://gregor77.github.io/2021/05/18/spring-security-03/
  * https://brunch.co.kr/@springboot/491
  * https://lemontia.tistory.com/655

* Spring Actuator
  * https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html

* OpenTracing
  * https://opentracing.io/guides/java/
  * https://github.com/opentracing-contrib/java-spring-cloud
  * https://stackoverflow.com/questions/41222405/adding-the-traceid-from-spring-cloud-sleuth-to-response
  * https://faun.pub/enhance-istio-distributed-tracing-with-opentracing-part-1-3c4f3ee918d
  * https://medium.com/@zhaohuabing/enhance-istio-distributed-tracing-with-opentracing-part-2-3d2643b571a2
  * https://github.com/debezium/debezium/blob/main/debezium-core/src/main/java/io/debezium/transforms/tracing/DebeziumTextMap.java

* Logging JSON
  * https://stackoverflow.com/questions/53730449/is-there-a-recommended-way-to-get-spring-boot-to-json-format-logs-with-logback
  * https://github.com/logfellow/logstash-logback-encoder

* Swagger
  * https://oingdaddy.tistory.com/272

* Test
  * https://www.baeldung.com/spring-data-disable-auto-config

* Dockerfile
  * https://godekdls.github.io/Spring%20Boot/container-images/
  * https://baeji77.github.io/spring/dev/Spring-boot-gradle-build/
  * https://www.baeldung.com/spring-boot-docker-images
