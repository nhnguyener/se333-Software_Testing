/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

import lombok.Data;
import org.apache.commons.math3.util.Precision;

import javax.persistence.Entity;

@Data
@Entity
/**
 * A value object that represents the total cost of a transation. Intended to be
 * used in transport to external systems.
 */
public class Bill {

  public double getInitialCost() {
    return initialCost;
  }

  public double getShipping() {
    return shipping;
  }

  public double getTax() {
    return tax;
  }

  public double getTotal() {
    return total;
  }

  private final double initialCost;
  private final double shipping;
  private final double tax;
  private final double total;

  public Bill(double initialCost, double shipping, double tax, double total) {
    this.initialCost = initialCost;
    this.shipping = shipping;
    this.tax = Precision.round(tax,2);
    this.total = Precision.round(total, 2);
  }

  public double total() {
    return total;
  }
}
