package com.salpreh.products.logistics.models;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pallet {
  private String id;
  private String productId;
  private String productName;
  private String batchId;
  private LocalDate productionDate;
  private Double weight;
}
