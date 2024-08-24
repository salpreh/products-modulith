package com.salpreh.products.logistics.mappers;

import com.salpreh.products.logistics.entities.PalletEntity;
import com.salpreh.products.logistics.models.Pallet;
import com.salpreh.products.logistics.models.events.PalletCreated;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PalletMapper {

  PalletEntity toEntity(Pallet pallet);
  Pallet toModel(PalletEntity entity);
  PalletCreated toEvent(Pallet pallet);
}
