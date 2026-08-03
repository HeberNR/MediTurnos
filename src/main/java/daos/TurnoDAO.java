package daos;

import com.gestionturnos.config.ConexionDB;
import exceptions.DatabaseException;
import models.Turno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TurnoDAO implements CrudDAO<Turno, Integer> {

  @Override
  public Turno crear(Turno turno) {
    String insertSQL = "INSERT INTO turnos (paciente_id, doctor_id, fecha_turno, hora_turno, estado, motivo_consulta) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

      stmt.setInt(1, turno.getPacienteId());
      stmt.setInt(2, turno.getDoctorId());
      stmt.setDate(3, turno.getFechaTurno());
      stmt.setTime(4, turno.getHoraTurno());
      stmt.setString(5, turno.getEstado()); // <-- Pasamos el estado ("pendiente")
      stmt.setString(6, turno.getMotivoConsulta());

      stmt.executeUpdate();

      try (ResultSet rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          turno.setId(rs.getInt(1));
        }
      }
      return turno;

    } catch (SQLException e) {
      // Imprimimos el error exacto en la consola por si es un tema de formato de fecha
      e.printStackTrace();
      throw new DatabaseException("Error al crear el turno", e);
    }
  }

  @Override
  public Optional<Turno> buscarPorId(Integer id) {
    // Usamos la vista para traer todos los datos completos
    String selectByIdSQL = "SELECT * FROM vista_turnos_detallados WHERE turno_id = ?";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(selectByIdSQL)) {

      stmt.setInt(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapearTurnoDesdeVista(rs));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Error al buscar el turno por ID", e);
    }
    return Optional.empty();
  }

  @Override
  public List<Turno> listarTodos() {
    List<Turno> turnos = new ArrayList<>();
    // Usamos la VISTA
    String selectAllSQL = "SELECT * FROM vista_turnos_detallados";

    try (Connection conn = ConexionDB.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(selectAllSQL)) {

      while (rs.next()) {
        turnos.add(mapearTurnoDesdeVista(rs));
      }
    } catch (SQLException e) {
      throw new DatabaseException("Error al listar los turnos", e);
    }
    return turnos;
  }

  @Override
  public boolean actualizar(Turno turno) {
    // Actualizamos la tabla original, NO la vista
    String updateSQL = "UPDATE turnos SET fecha_turno = ?, hora_turno = ?, estado = ?, motivo_consulta = ? WHERE id = ?";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

      stmt.setDate(1, turno.getFechaTurno());
      stmt.setTime(2, turno.getHoraTurno());
      stmt.setString(3, turno.getEstado());
      stmt.setString(4, turno.getMotivoConsulta());
      stmt.setInt(5, turno.getId());

      return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
      throw new DatabaseException("Error al actualizar el turno", e);
    }
  }

  // MÉTODO NUEVO: Actualiza exclusivamente el estado clínico y observaciones del doctor
  public void actualizarAtencionTurno(int turnoId, String estado, String diagnostico, String observaciones) {
    String sql = "UPDATE turnos SET estado = ?, diagnostico = ?, observaciones = ? WHERE id = ?";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, estado);
      stmt.setString(2, diagnostico);
      stmt.setString(3, observaciones);
      stmt.setInt(4, turnoId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DatabaseException("Error al actualizar la atención del turno", e);
    }
  }

  @Override
  public boolean eliminar(Integer id) {
    String deleteSQL = "DELETE FROM turnos WHERE id = ?";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {

      stmt.setInt(1, id);
      return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
      throw new DatabaseException("Error al eliminar el turno", e);
    }
  }

  // Método auxiliar para mapear los resultados de la VISTA a nuestro objeto Java
  private Turno mapearTurnoDesdeVista(ResultSet rs) throws SQLException {
    Turno t = new Turno();
    t.setId(rs.getInt("turno_id"));
    t.setPacienteId(rs.getInt("paciente_id"));
    t.setDoctorId(rs.getInt("doctor_id"));
    t.setFechaTurno(rs.getDate("fecha_turno"));
    t.setHoraTurno(rs.getTime("hora_turno"));
    t.setEstado(rs.getString("estado"));
    t.setMotivoConsulta(rs.getString("motivo_consulta"));

    // Mapeamos campos clínicos nuevos (Historial médico)
    t.setDiagnostico(rs.getString("diagnostico"));
    t.setObservaciones(rs.getString("observaciones"));

    // Mapeamos los campos extra de la vista
    t.setPacienteNombreCompleto(rs.getString("paciente_nombre") + " " + rs.getString("paciente_apellido"));
    t.setPacienteDni(rs.getString("dni")); // <-- ESTA ES LA LÍNEA MÁGICA QUE FALTABA
    t.setDoctorNombreCompleto(rs.getString("doctor_nombre") + " " + rs.getString("doctor_apellido"));
    t.setEspecialidad(rs.getString("especialidad"));

    return t;
  }
}