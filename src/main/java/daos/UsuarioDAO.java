package daos;

import com.gestionturnos.config.ConexionDB;
import exceptions.DatabaseException;
import models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements CrudDAO<Usuario, Integer> {

  @Override
  public Usuario crear(Usuario usuario) {
    String sql = "INSERT INTO usuarios (email, password, rol, activo, nombre, apellido, dni, telefono, especialidad) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      stmt.setString(1, usuario.getEmail());
      stmt.setString(2, usuario.getPassword());
      stmt.setString(3, usuario.getRol());
      stmt.setBoolean(4, usuario.getActivo() != null ? usuario.getActivo() : true);
      stmt.setString(5, usuario.getNombre());
      stmt.setString(6, usuario.getApellido());
      stmt.setString(7, usuario.getDni());
      stmt.setString(8, usuario.getTelefono());
      stmt.setString(9, usuario.getEspecialidadNombre());

      stmt.executeUpdate();

      try (ResultSet rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          usuario.setId(rs.getInt(1));
        }
      }
      return usuario;

    } catch (SQLException e) {
      throw new DatabaseException("Error al crear el usuario en la BD", e);
    }
  }

  @Override
  public Optional<Usuario> buscarPorId(Integer id) {
    String sql = "SELECT * FROM usuarios WHERE id = ?";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapearUsuario(rs));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Error al buscar el usuario por ID", e);
    }
    return Optional.empty();
  }

  public Optional<Usuario> buscarPorDni(String dni) {
    String sql = "SELECT * FROM usuarios WHERE dni = ?";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, dni);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapearUsuario(rs));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Error al buscar el usuario por DNI", e);
    }
    return Optional.empty();
  }

  // Trae los doctores activos directamente leyendo el campo especialidad
  public List<Usuario> listarDoctoresConEspecialidad() {
    List<Usuario> doctores = new ArrayList<>();
    String sql = "SELECT * FROM usuarios WHERE rol = 'doctor' AND activo = true";

    try (Connection conn = ConexionDB.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        doctores.add(mapearUsuario(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return doctores;
  }

  @Override
  public List<Usuario> listarTodos() {
    List<Usuario> usuarios = new ArrayList<>();
    String sql = "SELECT * FROM usuarios";

    try (Connection conn = ConexionDB.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        usuarios.add(mapearUsuario(rs));
      }
    } catch (SQLException e) {
      throw new DatabaseException("Error al listar los usuarios", e);
    }
    return usuarios;
  }

  @Override
  public boolean actualizar(Usuario usuario) {
    String sql = "UPDATE usuarios SET email = ?, password = ?, rol = ?, activo = ?, nombre = ?, apellido = ?, dni = ?, telefono = ?, especialidad = ? WHERE id = ?";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, usuario.getEmail());
      stmt.setString(2, usuario.getPassword());
      stmt.setString(3, usuario.getRol());
      stmt.setBoolean(4, usuario.getActivo());
      stmt.setString(5, usuario.getNombre());
      stmt.setString(6, usuario.getApellido());
      stmt.setString(7, usuario.getDni());
      stmt.setString(8, usuario.getTelefono());
      stmt.setString(9, usuario.getEspecialidadNombre());
      stmt.setInt(10, usuario.getId());

      return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
      throw new DatabaseException("Error al actualizar el usuario", e);
    }
  }

  @Override
  public boolean eliminar(Integer id) {
    String sql = "DELETE FROM usuarios WHERE id = ?";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, id);
      return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
      throw new DatabaseException("Error al eliminar el usuario", e);
    }
  }

  private Usuario mapearUsuario(ResultSet rs) throws SQLException {
    Usuario u = new Usuario();
    u.setId(rs.getInt("id"));
    u.setEmail(rs.getString("email"));
    u.setPassword(rs.getString("password"));
    u.setRol(rs.getString("rol"));
    u.setActivo(rs.getBoolean("activo"));
    u.setFechaRegistro(rs.getTimestamp("fecha_registro"));

    u.setNombre(rs.getString("nombre"));
    u.setApellido(rs.getString("apellido"));
    u.setDni(rs.getString("dni"));
    u.setTelefono(rs.getString("telefono"));

    try {
      u.setEspecialidadNombre(rs.getString("especialidad"));
    } catch (SQLException e) {
      u.setEspecialidadNombre("Médico General / Guardia");
    }

    return u;
  }
}