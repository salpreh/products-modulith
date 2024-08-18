package com.salpreh.products.stores.repositories;

import com.salpreh.products.stores.entities.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
