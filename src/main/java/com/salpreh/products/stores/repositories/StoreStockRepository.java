package com.salpreh.products.stores.repositories;

import com.salpreh.products.stores.entities.StoreStockEntity;
import com.salpreh.products.stores.entities.StoreStockEntity.StoreStockPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreStockRepository extends JpaRepository<StoreStockEntity, StoreStockPk> {
}
