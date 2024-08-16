package com.salpreh.products.products.models;

public record Supplier(String name, String iban, Address address) {

  public record Address(String street, String city, String zipCode, String countryCode) {}
}
