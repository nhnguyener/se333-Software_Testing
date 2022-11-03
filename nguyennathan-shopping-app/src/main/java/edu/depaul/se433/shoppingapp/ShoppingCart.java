/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the items the customer has selected for purchase.  We can
 * add items, get an item count and get the total pre-tax cost of the
 * items collected here.
 */
public class ShoppingCart {

  private Map<Long, PurchaseItem> items = new HashMap<>();

  public void addItem(PurchaseItem newItem) {
    newItem.id(new Long(items.size() + 1));
    items.put(newItem.id(), newItem);
  }

  public int itemCount() {
    return items.size();
  }

  public void clear() { items.clear();}

  public double cost() {
    double total = 0.0;
    for(Map.Entry<Long, PurchaseItem> item : items.entrySet()) {
      total += item.getValue().value();
    }
    return total;
  }

  public Map<Long, PurchaseItem> getItems() {
    return items;
  }

}
