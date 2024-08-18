package com.salpreh.products.logistics;

import com.salpreh.products.logistics.models.Pallet;

public interface PalletUseCasePort {

  Pallet decodeEan128(String ean);
}
