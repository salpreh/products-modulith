package com.salpreh.products.stores.services;

import com.salpreh.products.products.ProductReadUseCasePort;
import com.salpreh.products.stores.entities.StoreStockEntity.StoreStockPk;
import com.salpreh.products.stores.mappers.StoreMapper;
import com.salpreh.products.stores.models.StoreStock;
import com.salpreh.products.stores.models.events.StockUpdateEvent;
import com.salpreh.products.stores.repositories.StoreRepository;
import com.salpreh.products.stores.repositories.StoreStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockManagementService {

  private final StoreStockRepository storeStockRepository;
  private final StoreRepository storeRepository;
  private final ProductReadUseCasePort productReadUseCase;
  private final StoreMapper mapper;

  @ApplicationModuleListener
  public void managePalletStock(StockUpdateEvent stockUpdate) {
    if (!isValid(stockUpdate)) return;

    StoreStock stock = storeStockRepository.findById(StoreStockPk.of(stockUpdate.getStoreCode(), stockUpdate.getProductBarcode()))
      .map(mapper::toModel)
      .orElseGet(() -> mapper.toModelEmpty(stockUpdate));

    stock.addQuantity(stockUpdate.getQuantity());
    storeStockRepository.save(mapper.toEntity(stock));
  }

  private boolean isValid(StockUpdateEvent palletCreated) {
    if (!productReadUseCase.exists(palletCreated.getProductBarcode())) {
      log.warn("Pallet with non existing product ({}). Stock update skipped.", palletCreated.getProductBarcode());
      return false;
    }
    if (!storeRepository.existsById(palletCreated.getStoreCode())) {
      log.warn("Pallet with non existing store ({}). Stock update skipped.", palletCreated.getStoreCode());
      return false;
    }

    return true;
  }
}
