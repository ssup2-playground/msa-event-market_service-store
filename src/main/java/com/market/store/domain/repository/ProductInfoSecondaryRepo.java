package com.market.store.domain.repository;

import com.market.store.domain.entity.ProductInfo;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInfoSecondaryRepo extends JpaRepository<ProductInfo, UUID> {
  public List<ProductInfo> findByName(String name, Pageable pageable);
}
