package exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseExceptionTest {

  @Test
  void testDatabaseException() {
    // Simulamos un error interno
    Throwable causa = new RuntimeException("Falla interna de la base de datos");
    DatabaseException excepcion = new DatabaseException("Error de conexión", causa);

    // Verificamos que la excepción guarde bien el mensaje y la causa
    assertEquals("Error de conexión", excepcion.getMessage());
    assertEquals(causa, excepcion.getCause());
  }
}