package com.salpreh.products.stores.services;

import com.salpreh.products.stores.StoreReadUseCasePort;
import com.salpreh.products.stores.mappers.StoreMapper;
import com.salpreh.products.stores.models.Store;
import com.salpreh.products.stores.repositories.StoreRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreReadUseCase implements StoreReadUseCasePort {

  private final StoreRepository storeRepository;
  private final StoreMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Optional<Store> getStore(long code) {
    return storeRepository.findById(code)
      .map(mapper::toModel);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Store> getStores(int page, int size) {
    return storeRepository.findAll(PageRequest.of(page, size))
      .map(mapper::toModel);
  }
}
