/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcConnectionPool;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Script;

/**
 * Manages the database connection related to purchases
 */
public class PurchaseDBO {

  private static String jdbcUrl;
  private Jdbi jdbi;

  public PurchaseDBO() throws IOException {
    initialize();
  }

  private void savePurchase(String name, LocalDate purchaseDate, double cost, String state, String shipping) {

    jdbi.useHandle(handle -> {
      handle.createUpdate("INSERT INTO purchases(name, pdate, cost, state, shipping_type) VALUES (?, ?, ?, ?, ?)")
          .bind(0, name)
          .bind(1, purchaseDate)
          .bind(2, cost)
          .bind(3, state)
          .bind(4, shipping)
          .execute();
          });
  }

  public void savePurchase(Purchase p) {
    savePurchase(
            p.getCustomerName(),
            p.getPurchaseDate(),
            p.getCost(),
            p.getState(),
            p.getShipping()
            );
  }
  
  public List<Purchase> getPurchases(String user) {
    List<Purchase> purchases = jdbi.withHandle(handle -> {
      return handle.createQuery("SELECT * FROM purchases ORDER BY id_num")
          .mapToBean(Purchase.class)
          .list();

    });

    return purchases;
  }

  private void initialize() throws IOException {
    jdbcUrl  = "jdbc:h2:" + System.getProperty("user.dir") + "/src/main/resources/purchases";
    DataSource ds = JdbcConnectionPool.create(jdbcUrl,
        "sa",
        "");
    jdbi = Jdbi.create(ds);

    String script = getScript("create.sql");
    int[] results = jdbi.withHandle(handle -> {
      Script s = new Script(handle, script);
      return s.execute();
    });
  }

  private String getScript(String scriptName) {
    InputStream in = getClass().getResourceAsStream("/" + scriptName);
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    return reader.lines().collect(Collectors.joining("\n"));
  }
}
