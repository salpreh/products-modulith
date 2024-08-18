package com.salpreh.products.logistics.models.internal;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum Ean128IA {
  PALLET_ID("00", 18, 18, true),
  PRODUCT_ID("01", 14, 14, true),
  BATCH_ID("10", 1, 20, false),
  PRODUCTION_DATE("11", 6, 6, false),
  WEIGHT("310", 7, 7, false, true);

  private final String code;
  private final int minSize;
  private final int maxSize;
  private final boolean required;
  private final boolean extraProcessing;
  private static final Map<String, Ean128IA> eanByCode;
  static {
    eanByCode = Arrays.stream(Ean128IA.values())
      .collect(Collectors.toMap(Ean128IA::getCode, Function.identity()));
  }
  private static final Set<Ean128IA> requiredIas;
  static {
    requiredIas = Arrays.stream(Ean128IA.values())
      .filter(Ean128IA::isRequired)
      .collect(Collectors.toSet());
  }

  Ean128IA(String code, int minSize, int maxSize, boolean required) {
    this(code, minSize, maxSize, required, false);
  }

  Ean128IA(String code, int minSize, int maxSize, boolean required, boolean extraProcessing) {
    this.code = code;
    this.minSize = minSize;
    this.maxSize = maxSize;
    this.required = required;
    this.extraProcessing = extraProcessing;
  }

  public boolean isVariable() {
    return minSize < maxSize;
  }

  public boolean hasExtraProcessing() {
    return extraProcessing;
  }

  public static Ean128IA fromCode(String code) {
    return eanByCode.get(code);
  }

  public static Set<Ean128IA> getRequiredIas() {
    return requiredIas;
  }
}
