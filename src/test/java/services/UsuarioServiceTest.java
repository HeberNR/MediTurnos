package services;

import daos.UsuarioDAO;
import models.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

  @Mock
  private UsuarioDAO usuarioDAO;

  @InjectMocks
  private UsuarioService usuarioService;

  private Usuario usuarioExistente;

  @BeforeEach
  void setUp() {
    usuarioExistente = new Usuario();
    usuarioExistente.setId(1);
    usuarioExistente.setEmail("test@mediturnos.com");
    usuarioExistente.setDni("12345678");
    // Hasheamos una contraseña real para que BCrypt.checkpw funcione en los tests
    usuarioExistente.setPassword(BCrypt.hashpw("password123", BCrypt.gensalt()));
    usuarioExistente.setRol("paciente");
    usuarioExistente.setActivo(true);
    usuarioExistente.setNombre("Juan");
    usuarioExistente.setApellido("Pérez");
  }

  @Test
  void registrarUsuario_DebeLanzarExcepcion_CuandoEmailDuplicado() {
    when(usuarioDAO.listarTodos()).thenReturn(Arrays.asList(usuarioExistente));

    IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
      usuarioService.registrarUsuario(
          "test@mediturnos.com", "password123", "paciente",
          "Carlos", "Gómez", "87654321", "11223344", null
      );
    });

    assertEquals("El email ya se encuentra registrado en el sistema.", excepcion.getMessage());
    verify(usuarioDAO, never()).crear(any(Usuario.class));
  }

  @Test
  void registrarUsuario_DebeLanzarExcepcion_CuandoDniDuplicado() {
    when(usuarioDAO.listarTodos()).thenReturn(Arrays.asList(usuarioExistente));

    IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
      usuarioService.registrarUsuario(
          "nuevo@mediturnos.com", "password123", "paciente",
          "Ana", "Gómez", "12345678", "11223344", null
      );
    });

    assertEquals("El DNI ya se encuentra registrado en el sistema.", excepcion.getMessage());
    verify(usuarioDAO, never()).crear(any(Usuario.class));
  }

  @Test
  void login_DebeRetornarUsuario_CuandoCredencialesSonValidas() {
    when(usuarioDAO.listarTodos()).thenReturn(Arrays.asList(usuarioExistente));

    Optional<Usuario> resultado = usuarioService.login("test@mediturnos.com", "password123");

    assertTrue(resultado.isPresent());
    assertEquals("test@mediturnos.com", resultado.get().getEmail());
  }

  @Test
  void login_DebeRetornarEmpty_CuandoPasswordEsIncorrecta() {
    when(usuarioDAO.listarTodos()).thenReturn(Arrays.asList(usuarioExistente));

    Optional<Usuario> resultado = usuarioService.login("test@mediturnos.com", "claveerronea");

    assertFalse(resultado.isPresent());
  }

  @Test
  void login_DebeRetornarEmpty_CuandoUsuarioInactivo() {
    usuarioExistente.setActivo(false); // Inactivamos al usuario
    when(usuarioDAO.listarTodos()).thenReturn(Arrays.asList(usuarioExistente));

    Optional<Usuario> resultado = usuarioService.login("test@mediturnos.com", "password123");

    assertFalse(resultado.isPresent());
  }

  @Test
  void login_DebeRetornarEmpty_CuandoEmailOPasswordSonNull() {
    assertFalse(usuarioService.login(null, "password123").isPresent());
    assertFalse(usuarioService.login("test@mediturnos.com", null).isPresent());
  }

  @Test
  void registrarUsuario_DebeLanzarExcepcion_CuandoEmailInvalido() {
    IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
      usuarioService.registrarUsuario(
          "emailmalformado", "password123", "paciente",
          "Juan", "Pérez", "112233", "11223344", null
      );
    });
    assertEquals("El formato del email no es válido.", excepcion.getMessage());
  }

  @Test
  void registrarUsuario_DebeLanzarExcepcion_CuandoFaltanDatosPersonales() {
    // Nombre vacío
    assertThrows(IllegalArgumentException.class, () -> {
      usuarioService.registrarUsuario(
          "valido@test.com", "password123", "paciente",
          "", "Pérez", "112233", "11223344", null
      );
    });

    // DNI vacío
    assertThrows(IllegalArgumentException.class, () -> {
      usuarioService.registrarUsuario(
          "valido@test.com", "password123", "paciente",
          "Juan", "Pérez", "", "11223344", null
      );
    });
  }

  @Test
  void registrarUsuario_DebeAsignarEspecialidad_CuandoEsDoctor() {
    when(usuarioDAO.listarTodos()).thenReturn(Arrays.asList());
    when(usuarioDAO.crear(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Usuario doctorCreado = usuarioService.registrarUsuario(
        "doctor@mediturnos.com", "password123", "doctor",
        "House", "MD", "55443322", "11223344", "Cardiología"
    );

    assertNotNull(doctorCreado);
    assertEquals("Cardiología", doctorCreado.getEspecialidadNombre());
  }
}