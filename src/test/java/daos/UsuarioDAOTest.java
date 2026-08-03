package daos;

import models.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOTest {

  private UsuarioDAO usuarioDAO;
  private Usuario usuarioPrueba;

  @BeforeEach
  void setUp() {
    // Instanciamos el DAO real, sin Mockito
    usuarioDAO = new UsuarioDAO();

    // Preparamos un usuario con datos "basura" para probar
    usuarioPrueba = new Usuario();
    usuarioPrueba.setNombre("Test");
    usuarioPrueba.setApellido("Integration");
    usuarioPrueba.setDni("99887766");
    usuarioPrueba.setEmail("test.integration@mediturnos.com");
    usuarioPrueba.setPassword("hashfalso123");
    usuarioPrueba.setRol("paciente");
    usuarioPrueba.setActivo(true);
    usuarioPrueba.setTelefono("1122334455");
  }

  @AfterEach
  void tearDown() {
    // LIMPIEZA: Si el test logró guardar el usuario en Docker, lo borramos.
    // Así mantenemos la base de datos limpia para la próxima vez que ejecutemos "mvn test".
    if (usuarioPrueba.getId() != null) {
      usuarioDAO.eliminar(usuarioPrueba.getId());
    }
  }

  @Test
  void crearYBuscarUsuario_DebeGuardarYRecuperarDeLaBD() {
    // 1. Probamos la inserción real en la base de datos
    Usuario creado = usuarioDAO.crear(usuarioPrueba);

    // Verificamos que MySQL le haya asignado un ID autoincremental
    assertNotNull(creado.getId(), "El ID no debería ser nulo después de guardar");
    assertTrue(creado.getId() > 0, "El ID debe ser mayor a 0");

    // 2. Probamos la recuperación (SELECT) por DNI
    Optional<Usuario> buscado = usuarioDAO.buscarPorDni("99887766");

    // Verificamos que el registro haya vuelto intacto
    assertTrue(buscado.isPresent(), "El usuario debería existir en la base de datos");
    assertEquals("test.integration@mediturnos.com", buscado.get().getEmail());
    assertEquals("paciente", buscado.get().getRol());
    assertEquals("Test", buscado.get().getNombre());
  }

  @Test
  void buscarPorDni_DebeRetornarVacio_CuandoDniNoExiste() {
    // Probamos qué pasa si le mandamos un DNI que no está en la tabla
    Optional<Usuario> buscado = usuarioDAO.buscarPorDni("00000000");
    assertFalse(buscado.isPresent(), "Debería retornar Optional.empty() para un DNI inexistente");
  }

  @Test
  void actualizarUsuario_DebeModificarLosDatosEnLaBD() {
    // 1. Creamos el usuario inicial
    Usuario creado = usuarioDAO.crear(usuarioPrueba);
    assertNotNull(creado.getId());

    // 2. Modificamos sus datos en memoria
    creado.setNombre("NombreActualizado");
    creado.setTelefono("99998888");

    // 3. Ejecutamos el UPDATE
    boolean actualizado = usuarioDAO.actualizar(creado);
    assertTrue(actualizado, "El update debería retornar true");

    // 4. Verificamos que los cambios se hayan guardado en MySQL
    Optional<Usuario> buscado = usuarioDAO.buscarPorDni("99887766");
    assertTrue(buscado.isPresent());
    assertEquals("NombreActualizado", buscado.get().getNombre());
    assertEquals("99998888", buscado.get().getTelefono());
  }

  @Test
  void eliminarUsuario_DebeRemoverElRegistroDeLaBD() {
    // 1. Creamos el usuario
    Usuario creado = usuarioDAO.crear(usuarioPrueba);
    assertNotNull(creado.getId());

    // 2. Ejecutamos el DELETE
    boolean eliminado = usuarioDAO.eliminar(creado.getId());
    assertTrue(eliminado, "El delete debería retornar true");

    // 3. Verificamos que ya no exista
    Optional<Usuario> buscado = usuarioDAO.buscarPorDni("99887766");
    assertFalse(buscado.isPresent(), "El usuario ya no debería encontrarse en la base de datos");
  }

  @Test
  void buscarPorId_Y_ListarTodos_DebenRetornarRegistros() {
    Usuario creado = usuarioDAO.crear(usuarioPrueba);
    assertNotNull(creado.getId());

    // Probamos buscarPorId
    Optional<Usuario> buscado = usuarioDAO.buscarPorId(creado.getId());
    assertTrue(buscado.isPresent());
    assertEquals("99887766", buscado.get().getDni());

    // Probamos listarTodos
    var todos = usuarioDAO.listarTodos();
    assertFalse(todos.isEmpty(), "La lista de usuarios no debería estar vacía");
  }

  @Test
  void listarDoctoresConEspecialidad_DebeRetornarListaDeDoctores() {
    // Creamos un doctor temporal para la prueba
    Usuario doctorPrueba = new Usuario();
    doctorPrueba.setNombre("Doctor");
    doctorPrueba.setApellido("Test Especialidad");
    doctorPrueba.setDni("9988776655");
    doctorPrueba.setEmail("doctor.especialidad.test@mediturnos.com");
    doctorPrueba.setPassword("12345");
    doctorPrueba.setRol("doctor");
    doctorPrueba.setEspecialidadNombre("Cardiologia");
    doctorPrueba.setActivo(true);

    Usuario creado = usuarioDAO.crear(doctorPrueba);

    try {
      var doctores = usuarioDAO.listarDoctoresConEspecialidad();
      assertFalse(doctores.isEmpty(), "Debería retornar al menos un doctor con especialidad");
    } finally {
      // Limpieza del doctor
      usuarioDAO.eliminar(creado.getId());
    }
  }
}