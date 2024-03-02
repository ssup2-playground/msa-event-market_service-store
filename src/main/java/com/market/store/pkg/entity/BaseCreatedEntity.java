package com.market.store.pkg.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseCreatedEntity {
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Date createdAt;
}
