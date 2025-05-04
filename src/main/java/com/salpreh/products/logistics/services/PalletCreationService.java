package com.salpreh.products.logistics.services;

import com.salpreh.products.logistics.models.events.PalletCreated;
import com.salpreh.products.products.SupplierReadUseCasePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PalletCreationService {

  private final SupplierReadUseCasePort supplierReadUseCase;

  /**
   * Dummy action to play with multiple event handlers
   * @param event The event containing information about the created pallet.
   */
  @ApplicationModuleListener
  public void onPalletCreated(PalletCreated event) {
    supplierReadUseCase.getSupplier(event.getSupplierId()).ifPresentOrElse(
      supplier -> log.info("Pallet created with supplier: {}", supplier),
      () -> log.warn("Pallet created with non-existing supplier: {}", event.getSupplierId())
    );
  }
}
