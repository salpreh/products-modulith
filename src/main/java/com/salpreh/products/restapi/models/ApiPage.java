package com.salpreh.products.restapi.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiPage<T> {
  private List<T> data;
  private PageInfo page;

  @Data
  @Builder
  public static class PageInfo {
    private int requestedPage;
    private int numElements;
    private int totalPages;
    private long totalElements;
  }
}
