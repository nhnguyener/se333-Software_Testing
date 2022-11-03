/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

/**
 * Calculates the final cost of the purchases in a ShoppingCart. To do
 * this we also need a state and a shipping type.  Returns a Bill,
 * which can be serialized and returned to the customer
 */
public class TotalCostCalculator {

  public static Bill calculate(ShoppingCart cart, String state, ShippingType shipping) {
    double initialCost = cart.cost();

    double shippingCost = getShippingCost(shipping, initialCost);
    double costWithShipping = initialCost + shippingCost;
    double tax = TaxCalculator.calculate(costWithShipping, state);
    double total = initialCost + tax + shippingCost;
    return  new Bill(initialCost,shippingCost,tax, total);
  }

  public static double calculate(double initialCost, String state, ShippingType shipping) {
    double shippingCost = getShippingCost(shipping, initialCost);
    double costWithShipping = initialCost + shippingCost;
    double tax = TaxCalculator.calculate(costWithShipping, state);
    return initialCost + tax + shippingCost;
  }

  private static double getShippingCost(ShippingType shipping, double initialCost) {
    if (initialCost > 50.0) {
      return 0.0;
    } else {
      return shipping.value;
    }
  }
}
