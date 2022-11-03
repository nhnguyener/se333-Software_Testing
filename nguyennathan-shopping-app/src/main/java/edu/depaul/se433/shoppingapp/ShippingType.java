/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

/**
 * The application supports 2 types of shipping: standard and next day.
 * This enum provides that choice and also holds the costs associated
 * with the choice.  To change the costs, just change it here.
 */
public enum ShippingType {
  FREE_TEST(0.0),
  UNKNOWN_TEST(-1.0),
  STANDARD(10.0),
  NEXT_DAY(25.0);

  public final double value;

  ShippingType(double value) {
    this.value = value;
  }
}
