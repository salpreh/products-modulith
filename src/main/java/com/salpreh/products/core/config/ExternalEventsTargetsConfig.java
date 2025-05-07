package com.salpreh.products.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "products-service.externalized-events.targets")
@Getter @Setter
public class ExternalEventsTargetsConfig {
  private String palletCreated = "products.logistics.pallet-created";
}
