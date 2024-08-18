package com.salpreh.products.stores.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "store_stock")
public class StoreStockEntity {

  @EmbeddedId
  private StoreStockPk id;

  private long quantity;

  @ManyToOne(cascade = {CascadeType.REFRESH})
  @JoinColumn(name = "store_code", insertable = false, updatable = false)
  private StoreEntity store;

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof StoreStockEntity)) return false;

      return id != null && id.equals(((StoreStockEntity) o).getId());
  }

  @Override
  public int hashCode() {
      return getClass().hashCode();
  }

  @Data
  @Embeddable
  public static class StoreStockPk implements Serializable {
    @Column(name = "store_code")
    private Long storeCode;

    @Column(name = "product_barcode")
    private String productBarcode;
  }
}
