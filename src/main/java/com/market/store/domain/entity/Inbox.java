package com.market.store.domain.entity;

import com.market.store.pkg.entity.BaseCreatedEntity;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inboxes")
public class Inbox extends BaseCreatedEntity {
  @Id
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "aggregatetype")
  @Length(max = 255)
  private String aggregateType;

  @Column(name = "eventtype")
  @Length(max = 255)
  private String eventType;

  @Length(max = 255)
  private String payload;

  @Column(name = "spancontext")
  @Length(max = 255)
  private String spanContext;
}
