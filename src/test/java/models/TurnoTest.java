package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TurnoTest {

  @Test
  void testTurnoEqualsYHashCode() {
    Turno t1 = new Turno();
    t1.setId(1);

    Turno t2 = new Turno();
    t2.setId(1);

    // Probamos equals y hashCode
    assertEquals(t1, t2);
    assertEquals(t1.hashCode(), t2.hashCode());

    // Probamos el setter/getter extra que agregamos para el DNI en la vista
    t1.setPacienteDni("112233");
    assertEquals("112233", t1.getPacienteDni());
  }
}