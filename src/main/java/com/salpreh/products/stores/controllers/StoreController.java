package com.salpreh.products.stores.controllers;

import com.salpreh.products.stores.StoreReadUseCasePort;
import com.salpreh.products.stores.models.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreReadUseCasePort storeReadUseCase;

  @GetMapping("/{storeCode}")
  public ResponseEntity<Store> getStore(@PathVariable long storeCode) {
    return storeReadUseCase.getStore(storeCode)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<Page<Store>> getStores(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(storeReadUseCase.getStores(page, size));
  }
}
