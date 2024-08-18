package com.salpreh.products.restapi.controllers;

import com.salpreh.products.products.ProductReadUseCasePort;
import com.salpreh.products.products.ProductWriteUseCasePort;
import com.salpreh.products.products.models.Product;
import com.salpreh.products.products.models.commands.UpsertProductCommand;
import com.salpreh.products.restapi.mappers.ApiMapper;
import com.salpreh.products.restapi.models.ApiPage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductReadUseCasePort productReadUseCase;
  private final ProductWriteUseCasePort productWriteUseCase;
  private final ApiMapper mapper;

  @GetMapping("/{barcode}")
  public ResponseEntity<Product> getProduct(@PathVariable String barcode) {
    return productReadUseCase.getByBarcode(barcode)
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<ApiPage<Product>> getProducts(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    var products = mapper.toApiPage(productReadUseCase.getAll(page, size));

    return ResponseEntity.ok(products);
  }

  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody UpsertProductCommand createCommand) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(productWriteUseCase.create(createCommand));
  }

  @PutMapping("/{barcode}")
  public ResponseEntity<Product> updateProduct(
    @PathVariable String barcode,
    @RequestBody UpsertProductCommand updateCommand
  ) {
    return ResponseEntity.ok(productWriteUseCase.update(barcode, updateCommand));
  }

  @DeleteMapping("/{barcode}")
  public ResponseEntity<Void> deleteProduct(@PathVariable String barcode) {
    productWriteUseCase.delete(barcode);

    return ResponseEntity.noContent().build();
  }
}
