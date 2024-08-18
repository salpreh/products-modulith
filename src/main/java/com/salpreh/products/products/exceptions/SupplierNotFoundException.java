package com.salpreh.products.products.exceptions;

public class SupplierNotFoundException extends RuntimeException {

  private static final String MSG = "Supplier with id %d not found";

  public SupplierNotFoundException(Long id) {
    super(String.format(MSG, id));
  }
}
