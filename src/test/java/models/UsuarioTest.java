package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

  @Test
  void testUsuarioEqualsYHashCode() {
    Usuario u1 = new Usuario();
    u1.setId(1);
    u1.setDni("12345678");
    u1.setEmail("test@test.com");

    Usuario u2 = new Usuario();
    u2.setId(1);
    u2.setDni("12345678");
    u2.setEmail("test@test.com");

    // Probamos que equals y hashCode funcionen bien
    assertEquals(u1, u2);
    assertEquals(u1.hashCode(), u2.hashCode());

    // Probamos el toString
    assertNotNull(u1.toString());

    // Probamos el getter de especialidad para cubrir esa línea de código
    Usuario u3 = new Usuario();
    u3.setEspecialidadNombre("Cardiología");
    assertEquals("Cardiología", u3.getEspecialidadNombre());
  }
}