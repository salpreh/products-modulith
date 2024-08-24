package com.salpreh.products.logistics.repositories;

import com.salpreh.products.logistics.entities.PalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PalletRepository extends JpaRepository<PalletEntity, String> {
}
