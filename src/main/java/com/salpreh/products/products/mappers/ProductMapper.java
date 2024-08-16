package com.salpreh.products.products.mappers;

import com.salpreh.products.products.entities.ProductEntity;
import com.salpreh.products.products.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  Product toModel(ProductEntity entity);
  ProductEntity toEntity(Product model);
}
