package com.salpreh.products.logistics.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.salpreh.products.logistics.exceptions.EanProcessingException;
import com.salpreh.products.logistics.models.Pallet;
import com.salpreh.products.products.ProductReadUseCasePort;
import com.salpreh.products.products.models.Product;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PalletUseCaseTest {

  @Mock
  private ProductReadUseCasePort productReadUseCase;

  @InjectMocks
  private PalletUseCase palletUseCase;

  @Test
  void givenValidEan_whenDecode_shouldProcessCorrectly() {
    // given
    String productCode = "12345678912345";
    String ean = "0012345678912345678901123456789123451012*112201013101000010";
    BDDMockito.given(productReadUseCase.getByBarcode(productCode))
      .willReturn(Optional.of(createProduct(productCode, "name")));

    // when
    Pallet pallet = palletUseCase.decodeEan128(ean);

    // then
    assertEquals("123456789123456789", pallet.getId());
    assertEquals(productCode, pallet.getProductId());
    assertEquals("12", pallet.getBatchId());
    assertEquals(LocalDate.parse("2022-01-01"), pallet.getProductionDate());
    assertEquals(1000.0, pallet.getWeight());
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

  private Product createProduct(String barcode, String name) {
    return new Product(barcode, name, null, null, 0, 0, List.of(), List.of());
  }
}
