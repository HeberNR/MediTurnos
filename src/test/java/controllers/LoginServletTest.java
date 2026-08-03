package controllers;

import models.Usuario;
import services.UsuarioService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServletTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private HttpSession session;

  @Mock
  private RequestDispatcher dispatcher;

  @Mock
  private UsuarioService usuarioService;

  @InjectMocks
  private LoginServlet loginServlet;

  @Test
  public void doGet_DebeRedirigirALoginJsp() throws Exception {
    when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);

    loginServlet.doGet(request, response);

    verify(dispatcher).forward(request, response);
  }

  @Test
  public void doPost_LoginExitoso_DebeGuardarSesionYRedirigir() throws Exception {
    // Simulamos lo que el usuario escribe en el formulario
    when(request.getParameter("email")).thenReturn("test@mediturnos.com");
    when(request.getParameter("password")).thenReturn("123456");
    when(request.getSession()).thenReturn(session);

    // Simulamos que el servicio encuentra al usuario
    Usuario mockUsuario = new Usuario();
    mockUsuario.setEmail("test@mediturnos.com");
    when(usuarioService.login("test@mediturnos.com", "123456")).thenReturn(Optional.of(mockUsuario));

    // Ejecutamos el Servlet
    loginServlet.doPost(request, response);

    // Verificamos que se haya guardado en sesión y redirigido al dashboard
    verify(session).setAttribute("usuarioLogueado", mockUsuario);
    verify(response).sendRedirect(request.getContextPath() + "/dashboard");
  }

  @Test
  public void doPost_LoginFallido_DebeMostrarError() throws Exception {
    // Simulamos credenciales incorrectas
    when(request.getParameter("email")).thenReturn("mal@mediturnos.com");
    when(request.getParameter("password")).thenReturn("clavemala");

    // Simulamos que el servicio NO encuentra al usuario (Optional.empty)
    when(usuarioService.login(anyString(), anyString())).thenReturn(Optional.empty());
    when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);

    // Ejecutamos el Servlet
    loginServlet.doPost(request, response);

    // Verificamos que se haya seteado el atributo de error y se recargue la página
    verify(request).setAttribute(eq("error"), anyString());
    verify(dispatcher).forward(request, response);
  }
}