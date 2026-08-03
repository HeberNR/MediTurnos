package enums;

public enum Especialidad {
  MEDICO_GENERAL("Médico General / Guardia"),
  CARDIOLOGIA("Cardiología"),
  PEDIATRIA("Pediatría"),
  CLINICA_MEDICA("Clínica Médica"),
  DERMATOLOGIA("Dermatología"),
  TRAUMATOLOGIA("Traumatología"),
  OFTALMOLOGIA("Oftalmología"),
  ODONTOLOGIA("Odontología"),
  GINECOLOGIA("Ginecología"),
  PSIQUIATRIA("Psiquiatría"),
  NEUROLOGIA("Neurología");

  private final String nombre;

  Especialidad(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }
}