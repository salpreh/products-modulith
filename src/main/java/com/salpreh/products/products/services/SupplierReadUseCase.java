package com.salpreh.products.products.services;

import com.salpreh.products.products.SupplierReadUseCasePort;
import com.salpreh.products.products.mappers.ProductMapper;
import com.salpreh.products.products.models.Supplier;
import com.salpreh.products.products.repositories.SupplierRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierReadUseCase implements SupplierReadUseCasePort {

  private final SupplierRepository supplierRepository;
  private final ProductMapper mapper;

  @Override
  public Optional<Supplier> getSupplier(long code) {
    return supplierRepository.findById(code)
      .map(mapper::toModel);
  }
}
