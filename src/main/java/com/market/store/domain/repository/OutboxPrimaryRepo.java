package com.market.store.domain.repository;

import com.market.store.domain.entity.Outbox;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxPrimaryRepo extends JpaRepository<Outbox, UUID> {}
