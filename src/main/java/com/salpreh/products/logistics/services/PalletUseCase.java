package com.salpreh.products.logistics.services;

import com.salpreh.products.logistics.PalletUseCasePort;
import com.salpreh.products.logistics.entities.PalletEntity;
import com.salpreh.products.logistics.mappers.PalletMapper;
import com.salpreh.products.logistics.models.Pallet;
import com.salpreh.products.logistics.repositories.PalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PalletUseCase implements PalletUseCasePort {

  private final Ean128Decoder eanDecoder;
  private final PalletRepository palletRepository;
  private final PalletMapper mapper;

  private final ApplicationEventPublisher eventPublisher;

  @Override
  public Pallet decodeEan128(String ean) {
    return eanDecoder.decodeEan128(ean);
  }

  @Override
  @Transactional
  public Pallet createPallet(String ean) {
    Pallet pallet = eanDecoder.decodeEan128(ean);

    PalletEntity palletEntity = mapper.toEntity(pallet);
    palletRepository.save(palletEntity);

    eventPublisher.publishEvent(mapper.toEvent(pallet));

    return pallet;
  }

}
