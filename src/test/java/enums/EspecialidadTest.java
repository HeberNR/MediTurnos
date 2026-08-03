package enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EspecialidadTest {

  @Test
  void testEnumEspecialidad() {
    // Ejecutamos values() para cubrir la iteración del Enum
    Especialidad[] especialidades = Especialidad.values();
    assertTrue(especialidades.length > 0);

    // Probamos valueOf() y el getter
    Especialidad esp = Especialidad.valueOf("CARDIOLOGIA");
    assertEquals("Cardiologia", esp.getNombre());
  }
}