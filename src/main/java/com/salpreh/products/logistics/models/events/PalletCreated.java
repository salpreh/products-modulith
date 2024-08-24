package com.salpreh.products.logistics.models.events;

import com.salpreh.products.stores.models.events.StockUpdateEvent;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;
import org.jmolecules.event.annotation.DomainEvent;

@Value
@Builder
@DomainEvent
public class PalletCreated implements StockUpdateEvent {
  private String id;
  private String productId;
  private Long supplierId;
  private Long storeId;
  private String batchId;
  private LocalDate productionDate;
  private Double weight;
  private Integer units;

  @Override
  public Long getStoreCode() {
    return storeId;
  }

  @Override
  public String getProductBarcode() {
    return productId;
  }

  @Override
  public long getQuantity() {
    return units;
  }
}
