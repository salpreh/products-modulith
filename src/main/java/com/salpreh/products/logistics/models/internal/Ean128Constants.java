package com.salpreh.products.logistics.models.internal;

import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Ean128Constants {
  public static final String VARIABLE_CHAR = "*";
  public static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
}
