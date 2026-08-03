package services;

import daos.UsuarioDAO;
import models.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class UsuarioService {

  private final UsuarioDAO usuarioDAO;

  public UsuarioService(UsuarioDAO usuarioDAO) {
    this.usuarioDAO = usuarioDAO;
  }

  // Agregamos 'especialidad' al final de los parámetros
  public Usuario registrarUsuario(String email, String password, String rol, String nombre, String apellido, String dni, String telefono, String especialidad) {

    // 1. Validar campos obligatorios
    if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
      throw new IllegalArgumentException("El email y la contraseña son obligatorios.");
    }

    // 2. Validar formato básico de email
    if (!email.contains("@") || !email.contains(".")) {
      throw new IllegalArgumentException("El formato del email no es válido.");
    }

    // Campos personales obligatorios
    if (nombre == null || nombre.trim().isEmpty() || apellido == null || apellido.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre y el apellido son obligatorios.");
    }
    if (dni == null || dni.trim().isEmpty()) {
      throw new IllegalArgumentException("El DNI es obligatorio.");
    }

    // 3. Validar EMAIL DUPLICADO
    boolean emailExiste = usuarioDAO.listarTodos().stream()
        .anyMatch(u -> u.getEmail().equalsIgnoreCase(email.trim()));
    if (emailExiste) {
      throw new IllegalArgumentException("El email ya se encuentra registrado en el sistema.");
    }

    // VALIDACIÓN: DNI DUPLICADO
    boolean dniExiste = usuarioDAO.listarTodos().stream()
        .anyMatch(u -> u.getDni().equalsIgnoreCase(dni.trim()));
    if (dniExiste) {
      throw new IllegalArgumentException("El DNI ya se encuentra registrado en el sistema.");
    }

    // 4. Si pasó todas las validaciones, armamos el usuario y hasheamos la clave
    Usuario nuevoUsuario = new Usuario();
    nuevoUsuario.setEmail(email.trim().toLowerCase());
    String hashedPass = BCrypt.hashpw(password, BCrypt.gensalt());
    nuevoUsuario.setPassword(hashedPass);
    nuevoUsuario.setRol(rol);
    nuevoUsuario.setActivo(true);

    nuevoUsuario.setNombre(nombre.trim());
    nuevoUsuario.setApellido(apellido.trim());
    nuevoUsuario.setDni(dni.trim());
    nuevoUsuario.setTelefono(telefono != null ? telefono.trim() : "");

    // NUEVA LÓGICA: Seteamos la especialidad solo si es un doctor
    if ("doctor".equalsIgnoreCase(rol) && especialidad != null && !especialidad.trim().isEmpty()) {
      nuevoUsuario.setEspecialidadNombre(especialidad.trim());
    }

    return usuarioDAO.crear(nuevoUsuario);
  }

  public Optional<Usuario> login(String email, String password) {
    if (email == null || password == null) {
      return Optional.empty();
    }

    Optional<Usuario> usuarioOpt = usuarioDAO.listarTodos().stream()
        .filter(u -> u.getEmail().equalsIgnoreCase(email.trim()) && u.getActivo())
        .findFirst();

    if (usuarioOpt.isPresent()) {
      Usuario u = usuarioOpt.get();
      if (BCrypt.checkpw(password, u.getPassword())) {
        return Optional.of(u);
      }
    }
    return Optional.empty();
  }
}