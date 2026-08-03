package models;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

public class Turno {
  private Integer id;
  private Integer pacienteId;
  private Integer doctorId;
  private Date fechaTurno;
  private Time horaTurno;
  private String estado;
  private String motivoConsulta;
  private Timestamp fechaSolicitud;

  private String diagnostico;
  private String observaciones;

  // Atributos extra para mapear la vista SQL
  private String pacienteNombreCompleto;
  private String pacienteDni; // <-- NUEVO CAMPO
  private String doctorNombreCompleto;
  private String especialidad;

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public Integer getPacienteId() { return pacienteId; }
  public void setPacienteId(Integer pacienteId) { this.pacienteId = pacienteId; }
  public Integer getDoctorId() { return doctorId; }
  public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
  public Date getFechaTurno() { return fechaTurno; }
  public void setFechaTurno(Date fechaTurno) { this.fechaTurno = fechaTurno; }
  public Time getHoraTurno() { return horaTurno; }
  public void setHoraTurno(Time horaTurno) { this.horaTurno = horaTurno; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public String getMotivoConsulta() { return motivoConsulta; }
  public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }
  public Timestamp getFechaSolicitud() { return fechaSolicitud; }
  public void setFechaSolicitud(Timestamp fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
  public String getDiagnostico() { return diagnostico; }
  public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
  public String getObservaciones() { return observaciones; }
  public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
  public String getPacienteNombreCompleto() { return pacienteNombreCompleto; }
  public void setPacienteNombreCompleto(String pacienteNombreCompleto) { this.pacienteNombreCompleto = pacienteNombreCompleto; }

  // Getter y Setter del DNI
  public String getPacienteDni() { return pacienteDni; }
  public void setPacienteDni(String pacienteDni) { this.pacienteDni = pacienteDni; }

  public String getDoctorNombreCompleto() { return doctorNombreCompleto; }
  public void setDoctorNombreCompleto(String doctorNombreCompleto) { this.doctorNombreCompleto = doctorNombreCompleto; }
  public String getEspecialidad() { return especialidad; }
  public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Turno turno = (Turno) o;
    return Objects.equals(id, turno.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}