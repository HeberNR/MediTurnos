package models;

import java.sql.Timestamp;
import java.util.Objects;

public class Usuario {
  private Integer id;
  private String email;
  private String password;
  private String nombre;
  private String apellido;
  private String dni;
  private String telefono;
  private String rol;
  private Boolean activo;
  private Timestamp fechaRegistro;
  private String especialidadNombre;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }

  public Timestamp getFechaRegistro() {
    return fechaRegistro;
  }

  public void setFechaRegistro(Timestamp fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }

  public String getEspecialidadNombre() {
    return especialidadNombre;
  }

  public void setEspecialidadNombre(String especialidadNombre) {
    this.especialidadNombre = especialidadNombre;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Usuario usuario = (Usuario) o;
    return Objects.equals(id, usuario.id) ||
        Objects.equals(dni, usuario.dni) ||
        Objects.equals(email, usuario.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dni, email);
  }

  @Override
  public String toString() {
    return "Usuario{" +
        "id=" + id +
        ", nombre='" + nombre + '\'' +
        ", apellido='" + apellido + '\'' +
        ", dni='" + dni + '\'' +
        ", email='" + email + '\'' +
        ", rol='" + rol + '\'' +
        ", activo=" + activo +
        ", especialidadNombre='" + especialidadNombre + '\'' +
        '}';
  }
}