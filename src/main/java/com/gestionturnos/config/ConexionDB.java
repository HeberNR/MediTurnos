package com.gestionturnos.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConexionDB {

  private static HikariDataSource dataSource;

  // El bloque static se ejecuta una sola vez cuando arranca la app
  static {
    HikariConfig config = new HikariConfig();

    // Credenciales apuntando al Docker
    config.setJdbcUrl("jdbc:mysql://db:3306/gestion_turnos_db");
    config.setUsername("root");
    config.setPassword("0404");
    config.setDriverClassName("com.mysql.cj.jdbc.Driver");

    // Optimizaciones de rendimiento
    config.setMaximumPoolSize(10);
    config.setMinimumIdle(2);
    config.setIdleTimeout(30000);
    config.setConnectionTimeout(20000);

    dataSource = new HikariDataSource(config);
  }

  // Método que van a usar los DAOs para hablar con la base
  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}