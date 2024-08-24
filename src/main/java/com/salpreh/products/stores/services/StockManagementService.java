package com.salpreh.products.stores.services;

import com.salpreh.products.logistics.models.events.PalletCreated;
import com.salpreh.products.products.ProductReadUseCasePort;
import com.salpreh.products.stores.entities.StoreStockEntity.StoreStockPk;
import com.salpreh.products.stores.mappers.StoreMapper;
import com.salpreh.products.stores.models.StoreStock;
import com.salpreh.products.stores.repositories.StoreRepository;
import com.salpreh.products.stores.repositories.StoreStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockManagementService {

  private final StoreStockRepository storeStockRepository;
  private final StoreRepository storeRepository;
  private final ProductReadUseCasePort productReadUseCase;
  private final StoreMapper mapper;

  @Async
  @TransactionalEventListener
  public void managePalletStock(PalletCreated pallet) {
    if (!isValidPallet(pallet)) return;

    StoreStock stock = storeStockRepository.findById(StoreStockPk.of(pallet.getStoreId(), pallet.getProductId()))
      .map(mapper::toModel)
      .orElseGet(() -> mapper.toModelEmpty(pallet));

    stock.addQuantity(pallet.getUnits());
    storeStockRepository.save(mapper.toEntity(stock));
  }

  private boolean isValidPallet(PalletCreated palletCreated) {
    if (!productReadUseCase.exists(palletCreated.getProductId())) {
      log.warn("Pallet with non existing product ({}). Stock update skipped.", palletCreated.getProductId());
      return false;
    }
    if (!storeRepository.existsById(palletCreated.getStoreId())) {
      log.warn("Pallet with non existing store ({}). Stock update skipped.", palletCreated.getStoreId());
      return false;
    }

    return true;
  }
}
