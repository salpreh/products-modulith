package com.salpreh.products.stores.models;

import java.util.List;

public record Store(Long code, String name, List<StoreStock> stock) {}
