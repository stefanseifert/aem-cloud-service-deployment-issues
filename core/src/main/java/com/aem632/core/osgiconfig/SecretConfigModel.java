/* Copyright (c) pro!vision GmbH. All rights reserved. */
package com.aem632.core.osgiconfig;

import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

/**
 * Model for reading secret configurations.
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class SecretConfigModel {

  @OSGiService
  private SecretConfigService service;

  /**
   * @return Items
   */
  public List<Item> getItems() {
    return service.getItems();
  }

}
