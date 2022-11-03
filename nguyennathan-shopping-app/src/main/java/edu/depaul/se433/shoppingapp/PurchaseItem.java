/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
/**
 * A class that represents a purchased item.  Notice however that it
 * contains a quantity value, therefore the total value of this item is
 * a calculation.
 */
class PurchaseItem {

  private Long id;
  private String name;

  public Double getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  private Double unitPrice;
  private Integer quantity;

  private PurchaseItem() {}

  PurchaseItem(String name, double unitPrice, int quantity) {
    this.name = name;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
  }

  public void id(Long id) {
    this.id = id;
  }

  public Long id() {
    return id;
  }

  public double value() {
    return unitPrice * quantity;
  }
}
