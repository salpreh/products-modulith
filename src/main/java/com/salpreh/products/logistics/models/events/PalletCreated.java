package com.salpreh.products.logistics.models.events;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;
import org.jmolecules.event.annotation.DomainEvent;

@Value
@Builder
@DomainEvent
public class PalletCreated {
  private String id;
  private String productId;
  private Long supplierId;
  private Long storeId;
  private String batchId;
  private LocalDate productionDate;
  private Double weight;
  private Integer units;
}
