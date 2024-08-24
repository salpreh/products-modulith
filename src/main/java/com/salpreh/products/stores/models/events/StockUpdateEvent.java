package com.salpreh.products.stores.models.events;

public interface StockUpdateEvent {
  Long getStoreCode();
  String getProductBarcode();
  long getQuantity();
}
