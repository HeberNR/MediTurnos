package services;

import daos.TurnoDAO;
import daos.UsuarioDAO;
import models.Turno;
import models.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurnoServiceTest {

  @Mock
  private TurnoDAO turnoDAO;

  @Mock
  private UsuarioDAO usuarioDAO;

  @InjectMocks
  private TurnoService turnoService;

  private Turno turnoValido;

  @BeforeEach
  void setUp() {
    turnoValido = new Turno();
    turnoValido.setId(1);
    turnoValido.setPacienteId(10);
    turnoValido.setDoctorId(20);
    turnoValido.setFechaTurno(Date.valueOf("2026-12-01"));
    turnoValido.setHoraTurno(Time.valueOf("10:00:00"));
    turnoValido.setMotivoConsulta("Chequeo general");
    turnoValido.setEstado("pendiente");
  }

  @Test
  void solicitarTurno_DebeLanzarExcepcion_CuandoFechaEsNull() {
    turnoValido.setFechaTurno(null);

    IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
      turnoService.solicitarTurno(turnoValido);
    });

    assertEquals("La fecha y hora del turno son obligatorias.", excepcion.getMessage());
    verify(turnoDAO, never()).crear(any(Turno.class));
  }

  @Test
  void solicitarTurno_DebeCrearTurnoPendiente_CuandoDatosSonValidos() {
    when(turnoDAO.crear(any(Turno.class))).thenReturn(turnoValido);

    Turno turnoCreado = turnoService.solicitarTurno(turnoValido);

    assertEquals("pendiente", turnoValido.getEstado());
    assertNotNull(turnoCreado);
    verify(turnoDAO, times(1)).crear(turnoValido);
  }

  @Test
  void obtenerTurnosPorPaciente_DebeFiltrarCorrectamente() {
    Turno otroTurno = new Turno();
    otroTurno.setPacienteId(99); // De otro paciente

    when(turnoDAO.listarTodos()).thenReturn(Arrays.asList(turnoValido, otroTurno));

    List<Turno> turnosPaciente = turnoService.obtenerTurnosPorPaciente(10);

    assertEquals(1, turnosPaciente.size());
    assertEquals(10, turnosPaciente.get(0).getPacienteId());
  }

  @Test
  void cambiarEstadoTurno_DebeActualizarEstado_CuandoTurnoExiste() {
    when(turnoDAO.buscarPorId(1)).thenReturn(Optional.of(turnoValido));
    when(turnoDAO.actualizar(any(Turno.class))).thenReturn(true);

    boolean resultado = turnoService.cambiarEstadoTurno(1, "cancelado");

    assertTrue(resultado);
    assertEquals("cancelado", turnoValido.getEstado());
    verify(turnoDAO, times(1)).actualizar(turnoValido);
  }

  @Test
  void cambiarEstadoTurno_DebeRetornarFalso_CuandoTurnoNoExiste() {
    when(turnoDAO.buscarPorId(99)).thenReturn(Optional.empty());

    boolean resultado = turnoService.cambiarEstadoTurno(99, "cancelado");

    assertFalse(resultado);
    verify(turnoDAO, never()).actualizar(any(Turno.class));
  }

  @Test
  void buscarTurno_DebeRetornarOptionalDeTurno() {
    when(turnoDAO.buscarPorId(1)).thenReturn(Optional.of(turnoValido));

    Optional<Turno> resultado = turnoService.buscarTurno(1);

    assertTrue(resultado.isPresent());
    assertEquals(1, resultado.get().getId());
  }

  @Test
  void obtenerTurnosPorDoctor_DebeFiltrarCorrectamente() {
    Turno turnoDoctor1 = new Turno();
    turnoDoctor1.setDoctorId(20);
    Turno turnoDoctor2 = new Turno();
    turnoDoctor2.setDoctorId(99); // Otro doctor

    when(turnoDAO.listarTodos()).thenReturn(Arrays.asList(turnoDoctor1, turnoDoctor2));

    List<Turno> turnosDelDoctor = turnoService.obtenerTurnosPorDoctor(20);

    assertEquals(1, turnosDelDoctor.size());
    assertEquals(20, turnosDelDoctor.get(0).getDoctorId());
  }

  @Test
  void obtenerTurnosPendientes_DebeRetornarSoloPendientes() {
    Turno t1 = new Turno();
    t1.setEstado("pendiente");
    Turno t2 = new Turno();
    t2.setEstado("atendido");

    when(turnoDAO.listarTodos()).thenReturn(Arrays.asList(t1, t2));

    List<Turno> pendientes = turnoService.obtenerTurnosPendientes();

    assertEquals(1, pendientes.size());
    assertEquals("pendiente", pendientes.get(0).getEstado());
  }

  @Test
  void solicitarTurno_DebeLanzarExcepcion_CuandoHoraEsNull() {
    turnoValido.setHoraTurno(null);

    IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
      turnoService.solicitarTurno(turnoValido);
    });

    assertEquals("La fecha y hora del turno son obligatorias.", excepcion.getMessage());
    verify(turnoDAO, never()).crear(any(Turno.class));
  }

  @Test
  void listarTodosLosTurnos_DebeRetornarListaCompleta() {
    when(turnoDAO.listarTodos()).thenReturn(Arrays.asList(turnoValido));

    List<Turno> todos = turnoService.listarTodosLosTurnos();

    assertEquals(1, todos.size());
    verify(turnoDAO, times(1)).listarTodos();
  }

  @Test
  void registrarAtencionMedica_DebeLlamarAlDao() {
    doNothing().when(turnoDAO).actualizarAtencionTurno(anyInt(), anyString(), anyString(), anyString());

    assertDoesNotThrow(() -> {
      turnoService.registrarAtencionMedica(1, "atendido", "Sin novedad", "Reposo");
    });

    verify(turnoDAO, times(1)).actualizarAtencionTurno(1, "atendido", "Sin novedad", "Reposo");
  }

  @Test
  void crearTurnoPresencial_DebeActualizarPaciente_CuandoExisteConNombreGenerico() {
    Usuario pacienteExistente = new Usuario();
    pacienteExistente.setId(10);
    pacienteExistente.setDni("33445566");
    pacienteExistente.setNombre("Paciente");
    pacienteExistente.setApellido("Presencial");

    when(usuarioDAO.buscarPorDni("33445566")).thenReturn(Optional.of(pacienteExistente));
    when(usuarioDAO.actualizar(any(Usuario.class))).thenReturn(true);
    when(turnoDAO.crear(any(Turno.class))).thenReturn(turnoValido);

    assertDoesNotThrow(() -> {
      turnoService.crearTurnoPresencial(
          "33445566", "Carlos", "López", 2,
          Date.valueOf("2026-12-01"), Time.valueOf("10:00:00"), "Control"
      );
    });

    verify(usuarioDAO, times(1)).buscarPorDni("33445566");
    verify(usuarioDAO, times(1)).actualizar(any(Usuario.class));
    verify(turnoDAO, times(1)).crear(any(Turno.class));
  }

  @Test
  void crearTurnoPresencial_DebeCrearPacienteNuevo_CuandoDniNoExiste() {
    when(usuarioDAO.buscarPorDni("99887766"))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(new Usuario() {{ setId(15); }}));

    when(usuarioDAO.crear(any(Usuario.class))).thenReturn(null);
    when(turnoDAO.crear(any(Turno.class))).thenReturn(turnoValido);

    assertDoesNotThrow(() -> {
      turnoService.crearTurnoPresencial(
          "99887766", "María", "Gómez", 2,
          Date.valueOf("2026-12-01"), Time.valueOf("11:00:00"), "Primera vez"
      );
    });

    verify(usuarioDAO, times(1)).crear(any(Usuario.class));
    verify(turnoDAO, times(1)).crear(any(Turno.class));
  }
}