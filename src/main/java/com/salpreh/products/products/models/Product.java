package com.salpreh.products.products.models;

import java.util.List;

public record Product(
  String barcode,
  String name,
  String description,
  String imageUrl,
  double purchasePrice,
  double salePrice,
  List<Supplier> suppliers,
  List<String> tags
) {

}
