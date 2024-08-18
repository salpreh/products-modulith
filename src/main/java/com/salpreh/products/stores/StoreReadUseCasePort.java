package com.salpreh.products.stores;

import com.salpreh.products.stores.models.Store;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface StoreReadUseCasePort {
  Optional<Store> getStore(long code);
  Page<Store> getStores(int page, int size);
}
