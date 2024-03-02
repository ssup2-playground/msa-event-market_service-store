package com.market.store.domain.repository;

import com.market.store.domain.entity.StoreInfo;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreInfoSecondaryRepo extends JpaRepository<StoreInfo, UUID> {
  public List<StoreInfo> findByName(String name, Pageable pageable);
}
