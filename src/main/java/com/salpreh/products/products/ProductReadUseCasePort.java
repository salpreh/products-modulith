package com.salpreh.products.products;

import com.salpreh.products.products.models.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProductReadUseCasePort {
  Optional<Product> getProduct(String barcode);
  Page<Product> getAll(int page, int size);
}
