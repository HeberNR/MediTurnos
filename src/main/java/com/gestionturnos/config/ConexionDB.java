package com.gestionturnos.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConexionDB {

  private static volatile HikariDataSource dataSource;

  private static HikariDataSource createDataSource() {
    HikariConfig config = new HikariConfig();

    // Lee configuración de variables de entorno o usa tus valores locales por defecto (puerto 3307)
    String host = System.getenv().getOrDefault("DB_HOST", "localhost");
    String port = System.getenv().getOrDefault("DB_PORT", "3307");
    String dbName = System.getenv().getOrDefault("DB_NAME", "gestion_turnos");
    String user = System.getenv().getOrDefault("DB_USER", "root");
    String password = System.getenv().getOrDefault("DB_PASSWORD", "0404");

    config.setJdbcUrl(
        "jdbc:mysql://" + host + ":" + port + "/" + dbName
            + "?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8");
    config.setUsername(user);
    config.setPassword(password);
    config.setDriverClassName("com.mysql.cj.jdbc.Driver");

    config.setInitializationFailTimeout(60000);
    config.setMaximumPoolSize(10);
    config.setMinimumIdle(2);
    config.setIdleTimeout(30000);
    config.setConnectionTimeout(20000);

    return new HikariDataSource(config);
  }

  private static HikariDataSource getDataSource() {
    if (dataSource == null) {
      synchronized (ConexionDB.class) {
        if (dataSource == null) {
          dataSource = createDataSource();
          System.out.println(">>> CONEXIÓN A LA BD CONFIGURADA DESDE: " + dataSource.getJdbcUrl() + " <<<");
        }
      }
    }
    return dataSource;
  }

  public static Connection getConnection() throws SQLException {
    return getDataSource().getConnection();
  }
}