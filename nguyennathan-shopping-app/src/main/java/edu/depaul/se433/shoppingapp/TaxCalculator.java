/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculated a tax amount, given a purchase amount and a state value
 */
public class TaxCalculator {

  private static Map<String, Double> taxRates = new HashMap<>();

  static {
    taxRates.put("CA", 0.06);
    taxRates.put("IL", 0.06);
    taxRates.put("NY", 0.06);
  }

  public static double calculate(double initialCost, String state) {
    if (state.equals("CA") || state.equals("IL") || state.equals("NY")) {
      return initialCost * 0.06;
    } else {
      return 0.0;
    }
  }
}
