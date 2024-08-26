package com.salpreh.products.logistics.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import com.salpreh.products.logistics.entities.PalletEntity;
import com.salpreh.products.logistics.models.Pallet;
import com.salpreh.products.logistics.models.events.PalletCreated;
import com.salpreh.products.logistics.repositories.PalletRepository;
import com.salpreh.products.logistics.services.utils.tests.ModelFaker;
import com.salpreh.products.products.ProductReadUseCasePort;
import com.salpreh.products.products.SupplierReadUseCasePort;
import com.salpreh.products.products.models.Product;
import com.salpreh.products.products.models.Supplier;
import com.salpreh.products.stores.StoreReadUseCasePort;
import com.salpreh.products.stores.models.Store;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest
class PalletUseCaseModuleTest {

  @MockBean
  private StoreReadUseCasePort storeReadUseCase;
  @MockBean
  private ProductReadUseCasePort productReadUseCase;
  @MockBean
  private SupplierReadUseCasePort supplierReadUseCase;

  @Autowired
  private PalletUseCase palletUseCase;
  @Autowired
  private PalletRepository palletRepository;

  @Test
  void givenExistingResources_whenDecodeEan_shouldDecode() {
    // given
    String ean = "0012345678912345678901123456789123451012*112201013101000010412000000000000241000000000000013735*";
    Store store = ModelFaker.createStore(1L, "store");
    Supplier supplier = ModelFaker.createSupplier(2L, "supplier");
    Product product = ModelFaker.createProduct("12345678912345", "product");

    given(storeReadUseCase.getStore(1L)).willReturn(Optional.of(store));
    given(supplierReadUseCase.getSupplier(2L)).willReturn(Optional.of(supplier));
    given(productReadUseCase.getProduct("12345678912345")).willReturn(Optional.of(product));

    // when
    Pallet pallet = palletUseCase.decodeEan128(ean);

    // then
    assertEquals("123456789123456789", pallet.getId());
    assertEquals(LocalDate.parse("2022-01-01"), pallet.getProductionDate());
    assertEquals(1000.0, pallet.getWeight());
    assertEquals(35, pallet.getUnits());
    assertPalletReferences(store, supplier, product, pallet);
  }

  @Test
  void givenExistingResources_whenCreatePallet_shouldCreatePallet() {
    // given
    String ean = "0012345678912345678901123456789123451012*112201013101000010412000000000000241000000000000013735*";
    Store store = ModelFaker.createStore(1L, "store");
    Supplier supplier = ModelFaker.createSupplier(2L, "supplier");
    Product product = ModelFaker.createProduct("12345678912345", "product");

    given(storeReadUseCase.getStore(1L)).willReturn(Optional.of(store));
    given(supplierReadUseCase.getSupplier(2L)).willReturn(Optional.of(supplier));
    given(productReadUseCase.getProduct("12345678912345")).willReturn(Optional.of(product));

    // when
    Pallet pallet = palletUseCase.createPallet(ean);

    // then
    PalletEntity dbPallet = palletRepository.findById("123456789123456789").orElseGet(() -> {
      throw new AssertionError("Pallet not found in database");
    });

    assertEquals("123456789123456789", dbPallet.getId());
    assertEquals("123456789123456789", pallet.getId());
  }

  @Test
  void givenAvailableResources_whenCreatePallet_shouldEmmitEvent(Scenario scenario) {
    // given
    String ean = "0012345678912345678901123456789123451012*112201013101000010412000000000000241000000000000013735*";
    Store store = ModelFaker.createStore(1L, "store");
    Supplier supplier = ModelFaker.createSupplier(2L, "supplier");
    Product product = ModelFaker.createProduct("12345678912345", "product");

    given(storeReadUseCase.getStore(1L)).willReturn(Optional.of(store));
    given(supplierReadUseCase.getSupplier(2L)).willReturn(Optional.of(supplier));
    given(productReadUseCase.getProduct("12345678912345")).willReturn(Optional.of(product));

    // when & then
    scenario.stimulate(() -> palletUseCase.createPallet(ean))
      .andCleanup(() -> palletRepository.deleteAll())
      .andWaitForEventOfType(PalletCreated.class)
      .toArriveAndVerify(p -> {
        assertEquals("123456789123456789", p.getId());
        assertEquals(store.code(), p.getStoreId());
        assertEquals(supplier.id(), p.getSupplierId());
        assertEquals(product.barcode(), p.getProductId());
      });
  }

  private void assertPalletReferences(Store expectedStore, Supplier expectedSupplier, Product expectedProduct, Pallet pallet) {
    assertEquals(expectedStore.code(), pallet.getStoreId());
    assertEquals(expectedStore.name(), pallet.getStoreName());
    assertEquals(expectedSupplier.id(), pallet.getSupplierId());
    assertEquals(expectedSupplier.name(), pallet.getSupplierName());
    assertEquals(expectedProduct.barcode(), pallet.getProductId());
    assertEquals(expectedProduct.name(), pallet.getProductName());
  }
}
