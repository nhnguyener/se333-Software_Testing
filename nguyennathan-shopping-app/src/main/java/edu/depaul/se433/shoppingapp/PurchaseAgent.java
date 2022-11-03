/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

import org.jdbi.v3.core.JdbiException;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * The Purchase agent uses aPurchaseDBO to save and retrieve purchase information.
 * The business logic should be in this class while the details of database connections
 * etc. should be in PurchaseDBO
 */
public class PurchaseAgent {
  private PurchaseDBO dbo;

  public PurchaseAgent(PurchaseDBO dbo) {
    this.dbo = dbo;
  }

  public void save(Purchase purchase) {
    if (! purchase.equals(null)) {
      dbo.savePurchase(purchase);
    }
  }

  public List<Purchase> getPurchases(String name) {
    try {
      return dbo.getPurchases(name);
    } catch (JdbiException e) {
      return new ArrayList<>();
    }
  }

  public double averagePurchase(String user) {
    List<Purchase> purchases = dbo.getPurchases(user);
    short cnt = 0;
    double total = 0.0;
    for (short i = 0; i < purchases.size(); i++) {
      Purchase p = purchases.get(i);
      cnt++;
      total += p.getCost();
    }
    if (cnt > 0) {
      return total / cnt;
    } else {
      return 0.0;
    }
  }
}
