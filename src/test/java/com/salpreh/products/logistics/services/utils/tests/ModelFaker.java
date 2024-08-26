package com.salpreh.products.logistics.services.utils.tests;

import com.salpreh.products.products.models.Product;
import com.salpreh.products.products.models.Supplier;
import com.salpreh.products.stores.models.Store;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ModelFaker {

  public static Product createProduct(String barcode, String name) {
    return new Product(barcode, name, null, null, 0, 0, List.of(), List.of());
  }

  public static Store createStore(Long id, String name) {
    return new Store(id, name, List.of());
  }

  public static Supplier createSupplier(Long id, String name) {
    return new Supplier(id, name, null, null);
  }
}
