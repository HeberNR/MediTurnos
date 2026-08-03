package services;

import daos.TurnoDAO;
import daos.UsuarioDAO;
import models.Turno;
import models.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TurnoService {

  private final TurnoDAO turnoDAO;
  private final UsuarioDAO usuarioDAO;

  // Constructor original (lo usan los Servlets)
  public TurnoService(TurnoDAO turnoDAO) {
    this.turnoDAO = turnoDAO;
    this.usuarioDAO = new UsuarioDAO(); // Por defecto usa el real
  }

  // Nuevo constructor (lo usan los Tests para inyectar mocks)
  public TurnoService(TurnoDAO turnoDAO, UsuarioDAO usuarioDAO) {
    this.turnoDAO = turnoDAO;
    this.usuarioDAO = usuarioDAO;
  }

  public Turno solicitarTurno(Turno nuevoTurno) {
    if (nuevoTurno.getFechaTurno() == null || nuevoTurno.getHoraTurno() == null) {
      throw new IllegalArgumentException("La fecha y hora del turno son obligatorias.");
    }
    nuevoTurno.setEstado("pendiente");
    return turnoDAO.crear(nuevoTurno);
  }

  /**
   * Método adaptador que usa el AdminTurnosServlet para procesar los datos de tipo String
   * que vienen del formulario web y derivarlos a la lógica de creación presencial.
   */
  public void agendarTurnoAdministrativo(int doctorId, String dni, String nombre, String apellido, String fechaStr, String horaStr, String motivo) {
    if (fechaStr == null || fechaStr.isEmpty() || horaStr == null || horaStr.isEmpty()) {
      throw new IllegalArgumentException("La fecha y la hora son obligatorias.");
    }

    Date fecha = Date.valueOf(fechaStr); // Convierte "YYYY-MM-DD" a java.sql.Date

    // Aseguramos formato HH:mm:ss por si el input time manda solo HH:mm
    if (horaStr.length() == 5) {
      horaStr += ":00";
    }
    Time hora = Time.valueOf(horaStr); // Convierte "HH:mm:ss" a java.sql.Time

    // Reutilizamos toda la lógica robusta de creación y manejo de pacientes presenciales
    crearTurnoPresencial(dni, nombre, apellido, doctorId, fecha, hora, motivo);
  }

  public void crearTurnoPresencial(String dniPaciente, String nombre, String apellido, int doctorId, Date fecha, Time hora, String motivo) {
    Optional<Usuario> pacienteOpt = usuarioDAO.buscarPorDni(dniPaciente);
    int pacienteId;

    if (pacienteOpt.isPresent()) {
      Usuario pacienteExistente = pacienteOpt.get();
      pacienteId = pacienteExistente.getId();

      if ("Paciente".equalsIgnoreCase(pacienteExistente.getNombre()) && "Presencial".equalsIgnoreCase(pacienteExistente.getApellido())) {
        pacienteExistente.setNombre(nombre);
        pacienteExistente.setApellido(apellido);
        usuarioDAO.actualizar(pacienteExistente);
      }

    } else {
      Usuario nuevoPaciente = new Usuario();
      nuevoPaciente.setNombre(nombre);
      nuevoPaciente.setApellido(apellido);
      nuevoPaciente.setDni(dniPaciente);
      nuevoPaciente.setEmail(dniPaciente + "@temp.com");
      nuevoPaciente.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
      nuevoPaciente.setRol("paciente");
      nuevoPaciente.setActivo(true);

      usuarioDAO.crear(nuevoPaciente);
      pacienteId = usuarioDAO.buscarPorDni(dniPaciente).get().getId();
    }

    Turno turno = new Turno();
    turno.setPacienteId(pacienteId);
    turno.setDoctorId(doctorId);
    turno.setFechaTurno(fecha);
    turno.setHoraTurno(hora);
    turno.setEstado("pendiente");
    turno.setMotivoConsulta(motivo);

    turnoDAO.crear(turno);
  }

  public Optional<Turno> buscarTurno(Integer id) {
    return turnoDAO.buscarPorId(id);
  }

  public List<Turno> obtenerTurnosPorPaciente(Integer pacienteId) {
    List<Turno> todosLosTurnos = turnoDAO.listarTodos();
    return todosLosTurnos.stream()
        .filter(turno -> turno.getPacienteId().equals(pacienteId))
        .collect(Collectors.toList());
  }

  public List<Turno> obtenerTurnosPorDoctor(Integer doctorId) {
    return turnoDAO.listarTodos().stream()
        .filter(turno -> turno.getDoctorId().equals(doctorId))
        .collect(Collectors.toList());
  }

  public List<Turno> obtenerTurnosPendientes() {
    return turnoDAO.listarTodos().stream()
        .filter(turno -> "pendiente".equalsIgnoreCase(turno.getEstado()))
        .collect(Collectors.toList());
  }

  public boolean cambiarEstadoTurno(Integer turnoId, String nuevoEstado) {
    Optional<Turno> turnoExistente = turnoDAO.buscarPorId(turnoId);
    if (turnoExistente.isPresent()) {
      Turno turno = turnoExistente.get();
      turno.setEstado(nuevoEstado);
      return turnoDAO.actualizar(turno);
    }
    return false;
  }

  public void registrarAtencionMedica(Integer turnoId, String nuevoEstado, String diagnostico, String observaciones) {
    turnoDAO.actualizarAtencionTurno(turnoId, nuevoEstado, diagnostico, observaciones);
  }

  public List<Turno> listarTodosLosTurnos() {
    return turnoDAO.listarTodos();
  }
}