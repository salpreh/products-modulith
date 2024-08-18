package com.salpreh.products.products;

import com.salpreh.products.products.models.Product;
import com.salpreh.products.products.models.commands.UpsertProductCommand;

public interface ProductWriteUseCasePort {
  Product create(UpsertProductCommand createCommand);
  Product update(String barcode, UpsertProductCommand updateCommand);
  void delete(String barcode);
}
