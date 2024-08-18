package com.salpreh.products.products.exceptions;

public class ProductNotFoundException extends RuntimeException {

  private static final String MSG = "Product with barcode %s not found";

  public ProductNotFoundException(String barcode) {
    super(String.format(MSG, barcode));
  }
}
