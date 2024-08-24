package com.salpreh.products.stores.mappers;

import com.salpreh.products.logistics.models.events.PalletCreated;
import com.salpreh.products.stores.entities.StoreEntity;
import com.salpreh.products.stores.entities.StoreStockEntity;
import com.salpreh.products.stores.models.Store;
import com.salpreh.products.stores.models.StoreStock;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoreMapper {
  Store toModel(StoreEntity src);
  StoreEntity toEntity(Store src);

  @Mapping(target = "productBarcode", source = "id.productBarcode")
  @Mapping(target = "storeCode", source = "id.storeCode")
  StoreStock toModel(StoreStockEntity src);

  @InheritInverseConfiguration
  StoreStockEntity toEntity(StoreStock src);

  @Mapping(target = "storeCode", source = "storeId")
  @Mapping(target = "productBarcode", source = "productId")
  @Mapping(target = "quantity", constant = "0L")
  StoreStock toModelEmpty(PalletCreated src);
}
