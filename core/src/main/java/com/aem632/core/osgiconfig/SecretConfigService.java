/* Copyright (c) pro!vision GmbH. All rights reserved. */
package com.aem632.core.osgiconfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.aem632.core.osgiconfig.SecretConfigService.SecretConfig;

/**
 * Test service handling secret configs.
 */
@Component(service = SecretConfigService.class)
@Designate(ocd = SecretConfig.class)
public class SecretConfigService {

  /**
   * OSGi config
   */
  @ObjectClassDefinition(name = "aem632 Secret Config",
      description = "Test OSGI secret handling in OSGi configs.")
  public @interface SecretConfig {

    /**
     * @return Labels
     */
    @AttributeDefinition(name = "Labels", description = "Labels for string values")
    String[] labels();

    /**
     * @return String values
     */
    @AttributeDefinition(name = "String Values", description = "Configuration values")
    String[] values();

  }

  private List<Item> items;

  @Activate
  private void activate(SecretConfig config) {
    this.items = new ArrayList<>();
    for (int i=0; i<config.labels().length && i<config.values().length; i++) {
      this.items.add(new Item(config.labels()[i], config.values()[i]));
    }
  }

  /**
   * @return Items
   */
  public List<Item> getItems() {
    return Collections.unmodifiableList(this.items);
  }

}
