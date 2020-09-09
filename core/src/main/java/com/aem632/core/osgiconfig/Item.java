/* Copyright (c) pro!vision GmbH. All rights reserved. */
package com.aem632.core.osgiconfig;

/**
 * Configured item
 */
public class Item {

  private final String label;
  private final String value;

  /**
   * @param label Label
   * @param value Value
   */
  public Item(String label, String value) {
    this.label = label;
    this.value = value;
  }

  /**
   * @return Label
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * @return Value
   */
  public String getValue() {
    return this.value;
  }

}
