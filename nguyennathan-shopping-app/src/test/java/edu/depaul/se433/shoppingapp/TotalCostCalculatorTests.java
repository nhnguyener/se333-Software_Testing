package edu.depaul.se433.shoppingapp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TotalCostCalculatorTests {

  private TotalCostCalculator calculator;
  private ShoppingCart cart;
  private PurchaseItem pitem;
  private PurchaseAgent pagent;
  private PurchaseDBO pdbo;

  @BeforeEach
  void setup() throws IOException {
    calculator = new TotalCostCalculator();
    cart = new ShoppingCart();
    pdbo = new PurchaseDBO();
  }

  // test Bill creation

  @Test
  @DisplayName("Valid Bill Creation: Initial Cost")
  void tc01() {
    pitem = new PurchaseItem("bread", 1.0, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "IL", ShippingType.STANDARD);

    assertEquals(1.0, bill.getInitialCost());
  }

  @Test
  @DisplayName("Valid Bill Creation: STANDARD Shipping")
  void tc02() {
    pitem = new PurchaseItem("bread", 1.0, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "IL", ShippingType.STANDARD);

    assertEquals(10.0, bill.getShipping());
  }

  @Test
  @DisplayName("Valid Bill Creation: NEXT_DAY Shipping")
  void tc03() {
    pitem = new PurchaseItem("bread", 1.0, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "IL", ShippingType.NEXT_DAY);

    assertEquals(25.0, bill.getShipping());
  }

  @Test
  @DisplayName("Valid Bill Creation: Get Tax (Non Tax State)")
  void tc04() {
    pitem = new PurchaseItem("bread", 1.0, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.STANDARD);

    assertEquals(0.0, bill.getTax());
  }

  @Test
  @DisplayName("Valid Bill Creation: Check Total ($1.0 + $10.0 - $11.0)")
  void tc05() {
    pitem = new PurchaseItem("bread", 1.0, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.STANDARD);

    assertEquals(11.0, bill.getTotal());
  }

  //Boundary Tests: Purchase Amount

  @Test
  @DisplayName("Smallest Purchasable Amount: Min - 1")
  void bt01() {
    pitem = new PurchaseItem("bread", 0.99, 1);
    cart.addItem(pitem);

    assertThrows(IllegalArgumentException.class, () -> calculator.calculate(cart, "MA", ShippingType.FREE_TEST) );
  }

  @Test
  @DisplayName("Smallest Purchasable Amount: Min")
  void bt02() {
    pitem = new PurchaseItem("bread", 1.00, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.FREE_TEST);

    assertEquals(1.00, bill.getTotal());
  }

  @Test
  @DisplayName("Smallest Purchasable Amount: Min + 1")
  void bt03() {
    pitem = new PurchaseItem("bread", 1.01, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.FREE_TEST);

    assertEquals(1.01, bill.getTotal());
  }

  @Test
  @DisplayName("Purchasable Amount: Nominal")
  void bt04() {
    pitem = new PurchaseItem("sports car", 49999.99, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.FREE_TEST);

    assertEquals(49999.99, bill.getTotal());
  }

  @Test
  @DisplayName("Largest Purchasable Amount: Max - 1")
  void bt05() {
    pitem = new PurchaseItem("mansion", 99999.98, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.FREE_TEST);

    assertEquals(99999.98, bill.getTotal());
  }

  @Test
  @DisplayName("Largest Purchasable Amount: Max")
  void bt06() {
    pitem = new PurchaseItem("mansion", 99999.99, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.FREE_TEST);

    assertEquals(99999.99, bill.getTotal());
  }

  @Test
  @DisplayName("Largest Purchasable Amount: Max + 1")
  void bt07() {
    pitem = new PurchaseItem("mansion", 100000.00, 1);
    cart.addItem(pitem);

    assertThrows(IllegalArgumentException.class, () -> calculator.calculate(cart, "MA", ShippingType.FREE_TEST) );
  }

  //Shipping Tests

  @Test
  @DisplayName("Standard Shipping: Nom - 1")
  void st01() {
    pitem = new PurchaseItem("bread", 49.99, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.STANDARD);

    assertEquals(10.00, bill.getShipping());
  }

  @Test
  @DisplayName("Standard Shipping: Nom")
  void st02() {
    pitem = new PurchaseItem("bread", 50.00, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.STANDARD);

    assertEquals(10.00, bill.getShipping());
  }

  @Test
  @DisplayName("Standard Shipping: Nom + 1")
  void st03() {
    pitem = new PurchaseItem("bread", 50.01, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.STANDARD);

    assertEquals(0.00, bill.getShipping());
  }

  @Test
  @DisplayName("Next Day Shipping: Nom")
  void st04() {
    pitem = new PurchaseItem("bread", 49.99, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.NEXT_DAY);

    assertEquals(25.00, bill.getShipping());
  }

  @Test
  @DisplayName("Next Day Shipping: Nom")
  void st05() {
    pitem = new PurchaseItem("bread", 50.00, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.NEXT_DAY);

    assertEquals(25.00, bill.getShipping());
  }

  @Test
  @DisplayName("Next Day Shipping: Nom + 1")
  void st06() {
    pitem = new PurchaseItem("bread", 50.01, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.STANDARD);

    assertEquals(25.00, bill.getShipping());
  }

  //Tax Test

  @Test
  @DisplayName("Tax Test: IL")
  void tt01() {
    pitem = new PurchaseItem("bread", 1.00, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "IL", ShippingType.FREE_TEST);

    assertEquals(0.06, bill.getTax());
  }

  @Test
  @DisplayName("Tax Test: CA")
  void tt02() {
    pitem = new PurchaseItem("bread", 1.00, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "IL", ShippingType.FREE_TEST);

    assertEquals(0.06, bill.getTax());
  }

  @Test
  @DisplayName("Tax Test: NY")
  void tt03() {
    pitem = new PurchaseItem("bread", 1.00, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "IL", ShippingType.FREE_TEST);

    assertEquals(0.06, bill.getTax());
  }

  @Test
  @DisplayName("Tax Test: Random State (MA)")
  void tt04() {
    pitem = new PurchaseItem("bread", 1.00, 1);
    cart.addItem(pitem);
    Bill bill = calculator.calculate(cart, "MA", ShippingType.FREE_TEST);

    assertEquals(0.00, bill.getTax());
  }

  /*
   * Begin Equivalence Partitioning
   */

  //weak normal tests

  @ParameterizedTest
  @MethodSource("provideWeakTestInput")
  @DisplayName("Weak Normal Test")
  void wnt01(double initialCost, String state, ShippingType shipping, Double expected) {
    Double testVal = calculator.calculate(initialCost, state, shipping);
    assertEquals(expected, testVal);
  }

  private static Stream<Arguments> provideWeakTestInput() {
    return Stream.of(
        //           iC   state  ShippingType           expected
        Arguments.of(10.00, "IL", ShippingType.STANDARD, 21.20),
        Arguments.of(100.00, "MA", ShippingType.STANDARD, 100.00),
        Arguments.of(10.00, "IL", ShippingType.NEXT_DAY, 37.10)
    );
  }

  //weak robust tests

  @ParameterizedTest
  @MethodSource("provideWeakTestInput")
  @DisplayName("Weak Robust Test Pass")
  void wrt01(double initialCost, String state, ShippingType shipping, Double expected) {
    Double testVal = calculator.calculate(initialCost, state, shipping);
    assertEquals(expected, testVal);
  }

  @ParameterizedTest
  @MethodSource("provideWeakTestInputFail")
  @DisplayName("Weak Robust Test Fail")
  void wrt02(double initialCost, String state, ShippingType shipping) {
    assertThrows(IllegalArgumentException.class, () -> calculator.calculate(initialCost, state, shipping));
  }

  private static Stream<Arguments> provideWeakTestInputFail() {
    return Stream.of(
        //           iC   state  ShippingType
        Arguments.of(0.00, "IL", ShippingType.STANDARD),
        Arguments.of(100.00, "NARNIA", ShippingType.STANDARD)
        //Arguments.of(10.00, "IL", ShippingType.UNKNOWN_TEST)
    );
  }

  //Strong Normal Tests

  @ParameterizedTest
  @MethodSource("provideStrongTestInput")
  @DisplayName("Strong Normal Test")
  void snt01(double initialCost, String state, ShippingType shipping, Double expected) {
    Double testVal = calculator.calculate(initialCost, state, shipping);
    assertEquals(expected, testVal);
  }

  private static Stream<Arguments> provideStrongTestInput() {
    return Stream.of(
        //           iC   state  ShippingType           expected
        Arguments.of(10.00, "MA", ShippingType.STANDARD, 20.00),
        Arguments.of(100.00, "MA", ShippingType.STANDARD, 100.00),
        Arguments.of(10.00, "IL", ShippingType.STANDARD, 21.20),
        Arguments.of(100.00, "IL", ShippingType.STANDARD, 106.00),

        Arguments.of(10.00, "MA", ShippingType.NEXT_DAY, 35.00),
        Arguments.of(100.00, "MA", ShippingType.NEXT_DAY, 125.00),
        Arguments.of(10.00, "IL", ShippingType.NEXT_DAY, 37.10),
        Arguments.of(100.00, "IL", ShippingType.NEXT_DAY, 132.50)
    );
  }

  //Strong robust tests

  @ParameterizedTest
  @MethodSource("provideStrongTestInput")
  @DisplayName("Strong Robust Test Pass")
  void srt01(double initialCost, String state, ShippingType shipping, Double expected) {
    Double testVal = calculator.calculate(initialCost, state, shipping);
    assertEquals(expected, testVal);
  }

  @ParameterizedTest
  @MethodSource("provideStrongTestInputFail")
  @DisplayName("Strong Robust Test Fail")
  void srt02(double initialCost, String state, ShippingType shipping) {
    assertThrows(IllegalArgumentException.class, () -> calculator.calculate(initialCost, state, shipping));
  }

  private static Stream<Arguments> provideStrongTestInputFail() {
    return Stream.of(
        //           iC   state  ShippingType
        Arguments.of(-10.00, "MA", ShippingType.STANDARD),
        Arguments.of(100000.00, "MA", ShippingType.STANDARD),
        Arguments.of(-10.00, "IL", ShippingType.STANDARD),
        Arguments.of(100000.00, "IL", ShippingType.STANDARD),
        Arguments.of(-10.00, "MA", ShippingType.NEXT_DAY),
        Arguments.of(100000.00, "MA", ShippingType.NEXT_DAY),
        Arguments.of(-10.00, "IL", ShippingType.NEXT_DAY),
        Arguments.of(100000.00, "IL", ShippingType.NEXT_DAY),

        Arguments.of(10.00, "NARNIA", ShippingType.STANDARD),
        Arguments.of(100.00, "NARNIA", ShippingType.STANDARD),
        Arguments.of(10.00, "NARNIA", ShippingType.NEXT_DAY),
        Arguments.of(100.00, "NARNIA", ShippingType.NEXT_DAY)

        //Arguments.of(0.00, "MA", ShippingType.UNKNOWN_TEST)

    );
  }

  /*
   * Coverage Tests
   */

  @Test
  @DisplayName("Purchase Agent Tests")
  void pt01() {
    pagent = new PurchaseAgent(pdbo);
    Purchase pur = new Purchase();

    pur.make("Dog", LocalDate.now(), 5.00, "MA", "STANDARD");
    pagent.save(pur);
    ArrayList e = new ArrayList();
    assertEquals(e, pagent.getPurchases("Dog"));
  }

  @Test
  @DisplayName("Purchase Agent Tests")
  void pt02() {
    pagent = new PurchaseAgent(pdbo);
    Purchase pur = null;

    assertThrows(NullPointerException.class, () -> pagent.save(pur));
  }



}
