package com.gestionturnos.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class ConexionDB {

  private static HikariDataSource dataSource;

  static {
    try {
      HikariConfig config = new HikariConfig();

      // Detecta automáticamente si está en Docker o en Windows (Tests locales)
      boolean isRunningInDocker = new File("/.dockerenv").exists();
      String host = isRunningInDocker ? "db" : "localhost";
      String port = isRunningInDocker ? "3306" : "3307";

      // Aplica la conexión según el entorno
      config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/gestion_turnos?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8");
      config.setUsername("app_user");
      config.setPassword("0404");
      config.setDriverClassName("com.mysql.cj.jdbc.Driver");

      config.setInitializationFailTimeout(60000);
      config.setMaximumPoolSize(10);
      config.setMinimumIdle(2);
      config.setIdleTimeout(30000);
      config.setConnectionTimeout(20000);

      dataSource = new HikariDataSource(config);
      System.out.println(">>> CONEXIÓN A LA BD EXITOSA DESDE: " + host + ":" + port + " <<<");

    } catch (Exception e) {
      System.err.println(">>> ERROR CRÍTICO AL INICIALIZAR HIKARICP <<<");
      e.printStackTrace();
      throw new RuntimeException("Fallo al inicializar el pool de conexiones", e);
    }
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}