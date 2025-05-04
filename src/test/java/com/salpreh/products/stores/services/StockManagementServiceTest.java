package com.salpreh.products.stores.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import com.salpreh.products.logistics.models.events.PalletCreated;
import com.salpreh.products.products.ProductReadUseCasePort;
import com.salpreh.products.stores.entities.StoreStockEntity;
import com.salpreh.products.stores.entities.StoreStockEntity.StoreStockPk;
import com.salpreh.products.stores.repositories.StoreStockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.modulith.events.CompletedEventPublications;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest
class StockManagementServiceTest {

  @MockBean
  public ProductReadUseCasePort productReadUseCase;

  @Autowired
  public CompletedEventPublications completedEventPublications;
  @Autowired
  public StoreStockRepository storeStockRepository;

  @Autowired
  public StockManagementService stockManagementService;

  @Test
  void givenNonExistentProduct_whenManagePalletStock_thenSkip(Scenario scenario) {
    // given
    Long storeCode = 3003L;
    String productBarcode = "00000000000001";
    int quantity = 10;
    PalletCreated event = PalletCreated.builder()
      .storeId(storeCode)
      .productId(productBarcode)
      .units(quantity)
      .build();

    // when & then
    scenario.publish(() -> event)
      .andWaitForStateChange(() -> completedEventPublications.findAll().stream().findFirst())
      .andVerify(completedEvent -> {
        assertTrue(storeStockRepository.findById(StoreStockPk.of(storeCode, productBarcode)).isEmpty());
      });
  }

  @Test
  void givenNonExistingStock_whenManagePalletStock_shouldCreate(Scenario scenario) {
    // given
    Long storeCode = 3002L;
    String productBarcode = "00000000000001";
    int quantity = 10;
    PalletCreated event = PalletCreated.builder()
      .storeId(storeCode)
      .productId(productBarcode)
      .units(quantity)
      .build();

    given(productReadUseCase.exists(productBarcode)).willReturn(true);

    // when & then
    assertTrue(storeStockRepository.findById(StoreStockPk.of(storeCode, productBarcode)).isEmpty());

    scenario.publish(() -> event)
      .andCleanup(() -> storeStockRepository.deleteById(StoreStockPk.of(storeCode, productBarcode)))
      .andWaitForStateChange(() -> storeStockRepository.findById(StoreStockPk.of(storeCode, productBarcode)))
      .andVerify(storeStockOp -> {
        assertTrue(storeStockOp.isPresent());

        StoreStockEntity storeStock = storeStockOp.get();
        assertEquals(quantity, storeStock.getQuantity());
      });
  }

  @Test
  void givenExistingStock_whenManagePalletStock_shouldUpdate(Scenario scenario) {
    // given
    Long storeCode = 3001L;
    String productBarcode = "00000000000001";
    int baseQuantity = 80;
    int quantity = 10;
    PalletCreated event = PalletCreated.builder()
      .storeId(storeCode)
      .productId(productBarcode)
      .units(quantity)
      .build();

    given(productReadUseCase.exists(productBarcode)).willReturn(true);

    // when & then
    assertEquals(
      baseQuantity,
      storeStockRepository.findById(StoreStockPk.of(storeCode, productBarcode)).get().getQuantity()
    );

    scenario.publish(() -> event)
      .andWaitForStateChange(() -> storeStockRepository
        .findById(StoreStockPk.of(storeCode, productBarcode))
        .filter(ss -> ss.getQuantity() > baseQuantity))
      .andVerify(storeStockOp -> {
        assertTrue(storeStockOp.isPresent());

        StoreStockEntity storeStock = storeStockOp.get();
        assertEquals(baseQuantity + quantity, storeStock.getQuantity());
      });
  }
}
