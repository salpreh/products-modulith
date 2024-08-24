package com.salpreh.products.logistics.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.salpreh.products.logistics.exceptions.EanProcessingException;
import com.salpreh.products.logistics.models.Pallet;
import com.salpreh.products.products.ProductReadUseCasePort;
import com.salpreh.products.products.SupplierReadUseCasePort;
import com.salpreh.products.products.models.Product;
import com.salpreh.products.products.models.Supplier;
import com.salpreh.products.stores.StoreReadUseCasePort;
import com.salpreh.products.stores.models.Store;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class Ean128DecoderTest {

  @Mock
  private ProductReadUseCasePort productReadUseCase;
  @Mock
  private SupplierReadUseCasePort supplierReadUseCase;
  @Mock
  private StoreReadUseCasePort storeReadUseCase;

  @InjectMocks
  private PalletUseCase palletUseCase;

  @Test
  void givenValidEan_whenDecode_shouldProcessCorrectly() {
    // given
    String productCode = "12345678912345";
    Long supplierId = 55L;
    Long storeId = 1L;
    String ean = "0012345678912345678901123456789123451012*112201013101000010412000000000005541000000000000013735*";

    given(productReadUseCase.getProduct(productCode))
      .willReturn(Optional.of(createProduct(productCode, "product")));
    given(supplierReadUseCase.getSupplier(supplierId))
      .willReturn(Optional.of(createSupplier(supplierId, "supplier")));
    given(storeReadUseCase.getStore(storeId))
      .willReturn(Optional.of(createStore(storeId, "store")));

    // when
    Pallet pallet = palletUseCase.decodeEan128(ean);

    // then
    assertEquals("123456789123456789", pallet.getId());
    assertEquals(productCode, pallet.getProductId());
    assertEquals("product", pallet.getProductName());
    assertEquals(supplierId, pallet.getSupplierId());
    assertEquals("supplier", pallet.getSupplierName());
    assertEquals(storeId, pallet.getStoreId());
    assertEquals("store", pallet.getStoreName());
    assertEquals("12", pallet.getBatchId());
    assertEquals(LocalDate.parse("2022-01-01"), pallet.getProductionDate());
    assertEquals(1000.0, pallet.getWeight());
    assertEquals(35, pallet.getUnits());
  }

  @Test
  void givenInvalidEan_whenMissingRequiredIa_shouldThrowException() {
    // given
    String ean = "0112345678912345";

    // when
    Executable decode = () -> palletUseCase.decodeEan128(ean);

    // then
    assertThrows(EanProcessingException.class, decode);
  }

  @Test
  void givenInvalidEan_whenProductDoNotExists_shouldThrowException() {
    // given
    String ean = "0012345678912345678901123456789123451012*112201013101000010412000000000005541000000000000013735*";
    given(productReadUseCase.getProduct(anyString()))
      .willReturn(Optional.empty());

    // when
    Executable decode = () -> palletUseCase.decodeEan128(ean);

    // then
    assertThrows(EanProcessingException.class, decode);
  }

  private Product createProduct(String barcode, String name) {
    return new Product(barcode, name, null, null, 0, 0, List.of(), List.of());
  }

  private Store createStore(Long id, String name) {
    return new Store(id, name, List.of());
  }

  private Supplier createSupplier(Long id, String name) {
    return new Supplier(id, name, null, null);
  }
}
