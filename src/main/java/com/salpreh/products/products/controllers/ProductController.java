package com.salpreh.products.products.controllers;

import com.salpreh.products.products.ProductReadUseCasePort;
import com.salpreh.products.products.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductReadUseCasePort productReadUseCase;

  @GetMapping("/{barcode}")
  public ResponseEntity<Product> getProduct(@PathVariable String barcode) {
    return productReadUseCase.getByBarcode(barcode)
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<Page<Product>> getProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(productReadUseCase.getAll(page, size));
  }
}
