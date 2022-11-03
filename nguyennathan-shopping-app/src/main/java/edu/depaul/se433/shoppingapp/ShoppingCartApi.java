/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/shoppingapp")
/**
 * This is the web interface to the shopping application.  It maps REST
 * endpoints to underlying service classes.
 */
public class ShoppingCartApi {

  @Resource(name = "shoppingCart")
  ShoppingCart shoppingCart;

  @Resource(name = "purchaseAgent")
  PurchaseAgent purchaseAgent;

  @GetMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Map<Long, PurchaseItem> getCart() {
    return shoppingCart.getItems();
  }

  @GetMapping(path = "/checkout")
  public String checkout(
      @RequestParam(value="customer-name") String name,
      @RequestParam(value="state") String state,
      @RequestParam(value="shipping") String shipping) {
    Bill bill = TotalCostCalculator.calculate(shoppingCart,state, ShippingType.valueOf(shipping));
    purchaseAgent.save(Purchase.make(name, LocalDate.now(), bill.total(), state, shipping));
    shoppingCart.clear();
    return "ok";
  }

  @PostMapping(path = "/items")
  public String addItem(@RequestBody PurchaseItem newItem) {
    shoppingCart.addItem(newItem);

    System.out.println(newItem);
    return "Cart contains " + shoppingCart.itemCount() + " items";
  }

  @GetMapping(path = "/price")
  public double getPrice() {

    return shoppingCart.cost();
  }

  @GetMapping(value = "/totalprice", produces = MediaType.APPLICATION_JSON_VALUE)
  public Bill getTotalPrice(
      @RequestParam(value="state") String state,
      @RequestParam(value="shipping") String shipping) {
    return TotalCostCalculator.calculate(shoppingCart,state, ShippingType.valueOf(shipping));
  }

  @GetMapping(path = "/average")
  public double getAvergaPurchase(@RequestParam(value="name") String name) {
    return purchaseAgent.averagePurchase(name);
  }

  @Bean
  @SessionScope
  public ShoppingCart shoppingCart() {
    return new ShoppingCart();
  }

  @Bean
  @SessionScope
  public PurchaseAgent purchaseAgent() throws IOException {
    return new PurchaseAgent(new PurchaseDBO());
  }
}
