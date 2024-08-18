package com.salpreh.products.products.models.commands;

import java.util.List;

public record UpsertProductCommand (
  String barcode,
  String name,
  String description,
  String imageUrl,
  double purchasePrice,
  double sellingPrice,
  List<Long> suppliers,
  List<String> tags
){}
