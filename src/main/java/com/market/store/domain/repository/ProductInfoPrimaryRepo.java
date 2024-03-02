package com.market.store.domain.repository;

import com.market.store.domain.entity.ProductInfo;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductInfoPrimaryRepo extends JpaRepository<ProductInfo, UUID> {
  public List<ProductInfo> findByName(String name, Pageable pageable);

  public void deleteByStoreId(UUID storeId);

  @Transactional
  @Modifying
  @Query(value = "UPDATE ProductInfo p SET p.quantity = p.quantity + :increment WHERE id = :id")
  public int increaseQuantity(@Param("id") UUID id, @Param("increment") int increment);

  @Transactional
  @Modifying
  @Query(value = "UPDATE ProductInfo p SET p.quantity = p.quantity - :decrement WHERE id = :id")
  public int decreaseQuantity(@Param("id") UUID id, @Param("decrement") int decrement);
}
