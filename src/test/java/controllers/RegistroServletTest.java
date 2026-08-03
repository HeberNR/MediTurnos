package controllers;

import models.Usuario;
import services.UsuarioService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistroServletTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private RequestDispatcher dispatcher;

  @Mock
  private UsuarioService usuarioService;

  @InjectMocks
  private RegistroServlet registroServlet;

  @Test
  public void doGet_DebeRedirigirARegistroJsp() throws Exception {
    when(request.getRequestDispatcher("/WEB-INF/views/registro.jsp")).thenReturn(dispatcher);

    registroServlet.doGet(request, response);

    verify(dispatcher).forward(request, response);
  }

  @Test
  public void doPost_RegistroExitoso_DebeRedirigirALogin() throws Exception {
    // Simulamos los datos del formulario
    when(request.getParameter("nombre")).thenReturn("Juan");
    when(request.getParameter("apellido")).thenReturn("Perez");
    when(request.getParameter("dni")).thenReturn("11223344");
    when(request.getParameter("email")).thenReturn("juan@test.com");
    when(request.getParameter("password")).thenReturn("123456");
    when(request.getParameter("rol")).thenReturn("paciente");
    when(request.getParameter("telefono")).thenReturn("5551234");
    when(request.getParameter("especialidad")).thenReturn(null);

    // Simulamos que el servicio registra bien al usuario
    Usuario mockUsuario = new Usuario();
    when(usuarioService.registrarUsuario(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any())).thenReturn(mockUsuario);
    when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    // Ejecutamos
    registroServlet.doPost(request, response);

    // Verificamos redirección al login
    verify(request).setAttribute(eq("mensajeExito"), anyString());
    verify(dispatcher).forward(request, response);
  }

  @Test
  public void doPost_RegistroFallido_DebeMostrarError() throws Exception {
    // Simulamos datos
    when(request.getParameter("nombre")).thenReturn("Juan");
    when(request.getParameter("apellido")).thenReturn("Perez");
    when(request.getParameter("dni")).thenReturn("11223344");
    when(request.getParameter("email")).thenReturn("juan@test.com");
    when(request.getParameter("password")).thenReturn("123456");
    when(request.getParameter("rol")).thenReturn("paciente");
    when(request.getParameter("telefono")).thenReturn("5551234");
    when(request.getParameter("especialidad")).thenReturn(null);

    // Simulamos que el servicio tira una excepción (ej: DNI duplicado)
    when(usuarioService.registrarUsuario(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), any()))
        .thenThrow(new IllegalArgumentException("El DNI ya existe"));

    when(request.getRequestDispatcher("/WEB-INF/views/registro.jsp")).thenReturn(dispatcher);

    // Ejecutamos
    registroServlet.doPost(request, response);

    // Verificamos que ataje el error y vuelva al formulario
    verify(request).setAttribute("error", "El DNI ya existe");
    verify(dispatcher).forward(request, response);
  }
}