package com.salpreh.products.products;

import com.salpreh.products.products.models.Supplier;
import java.util.Optional;

public interface SupplierReadUseCasePort {
  Optional<Supplier> getSupplier(long code);
}
