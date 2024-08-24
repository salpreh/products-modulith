package com.salpreh.products.logistics.services;

import com.salpreh.products.logistics.exceptions.EanProcessingException;
import com.salpreh.products.logistics.models.Pallet;
import com.salpreh.products.logistics.models.internal.Ean128Constants;
import com.salpreh.products.logistics.models.internal.Ean128IA;
import com.salpreh.products.logistics.models.internal.ProcessedIA;
import com.salpreh.products.products.ProductReadUseCasePort;
import com.salpreh.products.products.SupplierReadUseCasePort;
import com.salpreh.products.products.models.Product;
import com.salpreh.products.products.models.Supplier;
import com.salpreh.products.stores.StoreReadUseCasePort;
import com.salpreh.products.stores.models.Store;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Ean128Decoder {
  private final ProductReadUseCasePort productReadUseCase;
  private final StoreReadUseCasePort storeReadUseCase;
  private final SupplierReadUseCasePort supplierReadUseCase;

  public Pallet decodeEan128(String ean) throws EanProcessingException {
    List<ProcessedIA> processedIAs = process(ean);
    if (!validateRequired(processedIAs))
      throw new EanProcessingException("Missing required IAs in EAN code");

    return createPallet(processedIAs);
  }

  private List<ProcessedIA> process(String ean) {
    char[] currentIA = new char[128];
    char[] eanChars = ean.toCharArray();
    int iaIdx = 0;
    List<ProcessedIA> processedIAs = new ArrayList<>();
    for (int eanIdx = 0; eanIdx < ean.length();) {
      currentIA[iaIdx++] = eanChars[eanIdx++];
      ProcessedIA processedIA = process(new String(currentIA, 0, iaIdx), ean, eanIdx);
      if (processedIA.isProcessed()) {
        processedIAs.add(processedIA);
        eanIdx = processedIA.getNewIndex();
        iaIdx = 0;
      }
    }

    if (iaIdx != 0)
      throw new EanProcessingException("Unable to process unknown IA. Index: " + (ean.length() - iaIdx - 1));

    return processedIAs;
  }

  private ProcessedIA process(String ia, String ean, int currentIdx) {
    Ean128IA eanIA = Ean128IA.fromCode(ia);
    if (eanIA == null) return ProcessedIA.unprocessed();

    int newIdx;
    String value;
    if (eanIA.isVariable()) {
      int markIdx = ean.indexOf(Ean128Constants.VARIABLE_CHAR, currentIdx);
      if (eanIA.getMinSize() > markIdx - currentIdx || eanIA.getMaxSize() < markIdx - currentIdx)
        throw new EanProcessingException("Invalid EAN IA size: " + ia);

      newIdx = markIdx + 1;
      value = ean.substring(currentIdx, markIdx);
    } else {
      newIdx = currentIdx + eanIA.getMaxSize();
      value = ean.substring(currentIdx, newIdx);
    }

    if (eanIA.hasExtraProcessing()) value = extraProcessing(eanIA, value);

    return ProcessedIA.create(eanIA, newIdx, value);
  }

  private String extraProcessing(Ean128IA ia, String value) {
    return switch (ia) {
      case WEIGHT -> parseWeight(value);
      default -> value;
    };
  }

  private boolean validateRequired(List<ProcessedIA> processedIas) {
    Set<Ean128IA> processed = processedIas.stream()
      .map(ProcessedIA::getIa)
      .collect(Collectors.toSet());

    return processed.containsAll(Ean128IA.getRequiredIas());
  }

  private String parseWeight(String value) {
    int pow = Character.getNumericValue(value.charAt(0));
    if (pow == -1) throw new EanProcessingException("Unable to parse weight IA");
    double weightKgs = Integer.parseInt(value.substring(1)) / Math.pow(10, pow);

    return String.valueOf(weightKgs * 1000);
  }

  private Pallet createPallet(List<ProcessedIA> ias) {
    Pallet.PalletBuilder builder = Pallet.builder();
    for (ProcessedIA ia : ias) {
      switch (ia.getIa()) {
        case PALLET_ID -> builder.id(ia.getData());
        case PRODUCT_ID -> {
          String barcode = ia.getData();
          Product product = productReadUseCase.getProduct(ia.getData())
            .orElseThrow(() -> new EanProcessingException("EAN product id do not exists: " + barcode));

          builder.productId(product.barcode());
          builder.productName(product.name());
        }
        case SUPPLIER_ID -> {
          long supplierId = Long.parseLong(ia.getData());
          Supplier supplier = supplierReadUseCase.getSupplier(supplierId)
            .orElseThrow(() -> new EanProcessingException("EAN supplier id do not exists: " + supplierId));

          builder.supplierId(supplier.id());
          builder.supplierName(supplier.name());
        }
        case DELIVERY_SITE_ID -> {
          long storeId = Long.parseLong(ia.getData());
          Store store = storeReadUseCase.getStore(storeId)
            .orElseThrow(() -> new EanProcessingException("EAN store id do not exists: " + storeId));

          builder.storeId(store.code());
          builder.storeName(store.name());
        }
        case BATCH_ID -> builder.batchId(ia.getData());
        case PRODUCTION_DATE -> builder.productionDate(LocalDate.parse(ia.getData(), Ean128Constants.DT_FORMATTER));
        case WEIGHT -> builder.weight(Double.valueOf(ia.getData()));
        case QUANTITY -> builder.units(Integer.parseInt(ia.getData()));
      }
    }

    return builder.build();
  }
}
