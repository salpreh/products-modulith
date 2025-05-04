package com.salpreh.products.core.config;

import com.salpreh.products.logistics.models.events.PalletCreated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.events.EventExternalizationConfiguration;
import org.springframework.modulith.events.RoutingTarget;

@Configuration
public class EventExternalizationConfig {

  @Bean
  public EventExternalizationConfiguration eventExternalizationConfiguration(ExternalEventsTargetsConfig eventsTargets) {
    return EventExternalizationConfiguration.externalizing()
      .select(EventExternalizationConfiguration.annotatedAsExternalized())
      .route(PalletCreated.class, ev -> RoutingTarget.forTarget(eventsTargets.getPalletCreated()).withoutKey())
      .routeKey(PalletCreated.class, PalletCreated::getId)
      .build();
  }
}
