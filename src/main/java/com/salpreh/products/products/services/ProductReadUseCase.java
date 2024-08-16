package com.salpreh.products.products.services;

import com.salpreh.products.products.ProductReadUseCasePort;
import com.salpreh.products.products.mappers.ProductMapper;
import com.salpreh.products.products.models.Product;
import com.salpreh.products.products.repositories.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReadUseCase implements ProductReadUseCasePort {

  private final ProductRepository productRepository;
  private final ProductMapper mapper;

  @Override
  public Optional<Product> getByBarcode(String barcode) {
    return productRepository.findById(barcode)
      .map(mapper::toModel);
  }

  @Override
  public Page<Product> getAll(int page, int size) {
    return productRepository.findAll(PageRequest.of(page, size))
      .map(mapper::toModel);
  }
}
