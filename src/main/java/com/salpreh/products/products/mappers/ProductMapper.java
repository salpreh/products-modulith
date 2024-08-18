package com.salpreh.products.products.mappers;

import com.salpreh.products.products.entities.ProductEntity;
import com.salpreh.products.products.entities.SupplierEntity;
import com.salpreh.products.products.models.Product;
import com.salpreh.products.products.models.Supplier;
import com.salpreh.products.products.models.commands.UpsertProductCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  Product toModel(ProductEntity src);
  ProductEntity toEntity(Product src);

  @Mapping(target = "suppliers", ignore = true)
  ProductEntity toEntity(UpsertProductCommand src);
  @Mapping(target = "suppliers", ignore = true)
  @Mapping(target = "barcode", ignore = true)
  void toEntity(@MappingTarget ProductEntity target, UpsertProductCommand src);

  Supplier toModel(SupplierEntity src);
}
