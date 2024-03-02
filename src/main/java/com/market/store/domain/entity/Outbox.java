package com.market.store.domain.entity;

import com.market.store.pkg.entity.BaseCreatedEntity;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "outboxes")
public class Outbox extends BaseCreatedEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "aggregatetype")
  @Length(max = 255)
  private String aggregateType;

  @Column(name = "aggregateid")
  @Length(max = 255)
  private String aggregateId;

  @Column(name = "eventtype")
  @Length(max = 255)
  private String eventType;

  @Length(max = 255)
  private String payload;

  @Column(name = "spancontext")
  @Length(max = 255)
  private String spanContext;
}
