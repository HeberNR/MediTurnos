package enums;

public enum Especialidad {
  MEDICO_GENERAL("Medico General / Guardia"),
  CARDIOLOGIA("Cardiologia"),
  PEDIATRIA("Pediatria"),
  CLINICA_MEDICA("Clinica Medica"),
  DERMATOLOGIA("Dermatologia"),
  TRAUMATOLOGIA("Traumatologia"),
  OFTALMOLOGIA("Oftalmologia"),
  ODONTOLOGIA("Odontologia"),
  GINECOLOGIA("Ginecologia"),
  PSIQUIATRIA("Psiquiatria"),
  NEUROLOGIA("Neurologia");

  private final String nombre;

  Especialidad(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }
}