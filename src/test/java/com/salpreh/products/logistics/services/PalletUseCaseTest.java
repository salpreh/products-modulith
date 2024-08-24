package com.salpreh.products.logistics.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.salpreh.products.logistics.mappers.PalletMapper;
import com.salpreh.products.logistics.models.Pallet;
import com.salpreh.products.logistics.models.events.PalletCreated;
import com.salpreh.products.logistics.repositories.PalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PalletUseCaseTest {

  @MockBean
  private Ean128Decoder eanDecoder;
  @MockBean
  private PalletRepository palletRepository;
  @MockBean
  private ApplicationEventPublisher eventPublisher;

  private PalletMapper mapper = Mappers.getMapper(PalletMapper.class);

  private PalletUseCase palletUseCase;

  @BeforeEach
  void setUp() {
    palletUseCase = new PalletUseCase(eanDecoder, palletRepository, mapper, eventPublisher);
  }

  @Test
  void givenValidEan_whenDecode_shouldProcessCorrectly() {
    // given
    String ean = "ean";
    Pallet pallet = createPallet();
    given(eanDecoder.decodeEan128(ean)).willReturn(pallet);

    // when
    Pallet decodedPallet = palletUseCase.decodeEan128(ean);

    // then
    assertEquals(pallet, decodedPallet);
  }

  @Test
  void givenCreatePallet_whenCreate_shouldProcessCorrectly() {
    // given
    String ean = "ean";
    Pallet pallet = createPallet();
    given(eanDecoder.decodeEan128(ean)).willReturn(pallet);
    given(palletRepository.save(any())).willReturn(mapper.toEntity(pallet));

    // when
    Pallet createdPallet = palletUseCase.createPallet(ean);

    // then
    assertEquals(pallet, createdPallet);

    ArgumentCaptor<PalletCreated> palletCreatedAC = ArgumentCaptor.forClass(PalletCreated.class);
    verify(eventPublisher).publishEvent(palletCreatedAC.capture());

    assertPalletCreated(pallet, palletCreatedAC.getValue());
  }

  void assertPalletCreated(Pallet pallet, PalletCreated palletCreated) {
    assertEquals(pallet.getId(), palletCreated.getId());
    assertEquals(pallet.getProductId(), palletCreated.getProductId());
    assertEquals(pallet.getSupplierId(), palletCreated.getSupplierId());
    assertEquals(pallet.getStoreId(), palletCreated.getStoreId());
    assertEquals(pallet.getBatchId(), palletCreated.getBatchId());
    assertEquals(pallet.getProductionDate(), palletCreated.getProductionDate());
    assertEquals(pallet.getWeight(), palletCreated.getWeight());
    assertEquals(pallet.getUnits(), palletCreated.getUnits());
  }

  private Pallet createPallet() {
    return Pallet.builder()
      .id("id")
      .productId("productId")
      .productName("productName")
      .supplierId(1L)
      .supplierName("supplierName")
      .storeId(1L)
      .storeName("storeName")
      .build();
  }
}
