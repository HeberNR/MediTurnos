package daos;

import models.Turno;
import models.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TurnoDAOTest {

  private TurnoDAO turnoDAO;
  private UsuarioDAO usuarioDAO;

  private Usuario pacientePrueba;
  private Usuario doctorPrueba;
  private Turno turnoPrueba;

  @BeforeEach
  void setUp() {
    turnoDAO = new TurnoDAO();
    usuarioDAO = new UsuarioDAO(); // Necesitamos este DAO para crear los dueños del turno

    // 1. Crear Paciente Falso para la clave foránea
    pacientePrueba = new Usuario();
    pacientePrueba.setNombre("Paciente");
    pacientePrueba.setApellido("Test");
    pacientePrueba.setDni("11111111");
    pacientePrueba.setEmail("paciente.test@mediturnos.com");
    pacientePrueba.setPassword("12345");
    pacientePrueba.setRol("paciente");
    pacientePrueba.setActivo(true);
    usuarioDAO.crear(pacientePrueba);

    // 2. Crear Doctor Falso para la clave foránea
    doctorPrueba = new Usuario();
    doctorPrueba.setNombre("Doctor");
    doctorPrueba.setApellido("Test");
    doctorPrueba.setDni("22222222");
    doctorPrueba.setEmail("doctor.test@mediturnos.com");
    doctorPrueba.setPassword("12345");
    doctorPrueba.setRol("doctor");
    doctorPrueba.setEspecialidadNombre("Cardiología");
    doctorPrueba.setActivo(true);
    usuarioDAO.crear(doctorPrueba);

    // 3. Preparar el Turno Falso cruzando los IDs autogenerados por MySQL
    turnoPrueba = new Turno();
    turnoPrueba.setPacienteId(pacientePrueba.getId());
    turnoPrueba.setDoctorId(doctorPrueba.getId());
    turnoPrueba.setFechaTurno(Date.valueOf("2026-10-10"));
    turnoPrueba.setHoraTurno(Time.valueOf("10:30:00"));
    turnoPrueba.setEstado("pendiente");
    turnoPrueba.setMotivoConsulta("Chequeo de rutina");
  }

  @AfterEach
  void tearDown() {
    // LIMPIEZA ESTRATÉGICA:
    // Borramos de "hijo" a "padres" para evitar errores de restricción de clave foránea.
    if (turnoPrueba.getId() != null) {
      turnoDAO.eliminar(turnoPrueba.getId());
    }
    if (pacientePrueba.getId() != null) {
      usuarioDAO.eliminar(pacientePrueba.getId());
    }
    if (doctorPrueba.getId() != null) {
      usuarioDAO.eliminar(doctorPrueba.getId());
    }
  }

  @Test
  void crearYBuscarTurno_DebeGuardarYRecuperarDeLaVistaSQL() {
    // Insertamos en la tabla original
    Turno creado = turnoDAO.crear(turnoPrueba);
    assertNotNull(creado.getId(), "El ID del turno no debe ser nulo tras guardarlo");

    // Lo leemos. ¡Atención! Esto lee desde "vista_turnos_detallados"
    Optional<Turno> buscado = turnoDAO.buscarPorId(creado.getId());

    assertTrue(buscado.isPresent(), "El turno debe existir en la base de datos");
    Turno turnoRecuperado = buscado.get();

    // Verificamos campos nativos del turno
    assertEquals("pendiente", turnoRecuperado.getEstado());
    assertEquals("Chequeo de rutina", turnoRecuperado.getMotivoConsulta());

    // Verificamos que la Vista SQL hizo bien el JOIN y trajo los datos cruzados
    assertEquals("Paciente Test", turnoRecuperado.getPacienteNombreCompleto());
    assertEquals("Doctor Test", turnoRecuperado.getDoctorNombreCompleto());
    assertEquals("Cardiología", turnoRecuperado.getEspecialidad());
  }

  @Test
  void actualizarAtencionTurno_DebeGuardarDiagnosticoYCambiarEstado() {
    Turno creado = turnoDAO.crear(turnoPrueba);

    // Ejecutamos la carga del historial médico (como si el doctor atendiera al paciente)
    turnoDAO.actualizarAtencionTurno(creado.getId(), "atendido", "Todo en orden", "Tomar mucha agua");

    // Buscamos de nuevo para verificar
    Optional<Turno> buscado = turnoDAO.buscarPorId(creado.getId());
    assertTrue(buscado.isPresent());

    assertEquals("atendido", buscado.get().getEstado());
    assertEquals("Todo en orden", buscado.get().getDiagnostico());
    assertEquals("Tomar mucha agua", buscado.get().getObservaciones());
  }

  @Test
  void actualizarTurnoYListarTodos_DebenFuncionarCorrectamente() {
    Turno creado = turnoDAO.crear(turnoPrueba);
    assertNotNull(creado.getId());

    // Probamos actualizar(Turno)
    creado.setMotivoConsulta("Motivo Modificado por Test");
    creado.setEstado("confirmado");
    boolean actualizado = turnoDAO.actualizar(creado);
    assertTrue(actualizado, "El turno debería actualizarse correctamente");

    // Verificamos el cambio
    Optional<Turno> buscado = turnoDAO.buscarPorId(creado.getId());
    assertTrue(buscado.isPresent());
    assertEquals("Motivo Modificado por Test", buscado.get().getMotivoConsulta());
    assertEquals("confirmado", buscado.get().getEstado());

    // Probamos listarTodos
    var todos = turnoDAO.listarTodos();
    assertFalse(todos.isEmpty(), "El listado general de turnos no debe estar vacío");
  }
}