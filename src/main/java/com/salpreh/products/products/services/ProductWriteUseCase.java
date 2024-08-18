package com.salpreh.products.products.services;

import com.salpreh.products.products.ProductWriteUseCasePort;
import com.salpreh.products.products.entities.ProductEntity;
import com.salpreh.products.products.entities.SupplierEntity;
import com.salpreh.products.products.exceptions.ProductNotFoundException;
import com.salpreh.products.products.exceptions.SupplierNotFoundException;
import com.salpreh.products.products.mappers.ProductMapper;
import com.salpreh.products.products.models.Product;
import com.salpreh.products.products.models.commands.UpsertProductCommand;
import com.salpreh.products.products.repositories.ProductRepository;
import com.salpreh.products.products.repositories.SupplierRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductWriteUseCase implements ProductWriteUseCasePort {

  private final ProductRepository productRepository;
  private final SupplierRepository supplierRepository;
  private final ProductMapper mapper;

  @Override
  public Product create(UpsertProductCommand createCommand) {
    Set<SupplierEntity> suppliers = getSuppliers(createCommand);

    ProductEntity product = mapper.toEntity(createCommand);
    product.setSuppliers(suppliers);

    return mapper.toModel(productRepository.save(product));
  }

  @Override
  public Product update(String barcode, UpsertProductCommand updateCommand) {
    ProductEntity product = productRepository.findById(barcode)
      .orElseThrow(() -> new ProductNotFoundException(barcode));
    Set<SupplierEntity> suppliers = getSuppliers(updateCommand);

    mapper.toEntity(product, updateCommand);
    product.setSuppliers(suppliers);

    return mapper.toModel(productRepository.save(product));
  }

  @Override
  public void delete(String barcode) {
    productRepository.deleteById(barcode);
  }

  private Set<SupplierEntity> getSuppliers(UpsertProductCommand createCommand) throws SupplierNotFoundException {
    return createCommand.suppliers().stream()
      .map(id -> Pair.of(id, supplierRepository.findById(id)))
      .map(sd -> sd.getSecond().orElseThrow(() -> new SupplierNotFoundException(sd.getFirst())))
      .collect(Collectors.toSet());
  }
}
